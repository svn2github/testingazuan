/**
 *
 *	LICENSE: see COPYING file
 *
**/
package it.eng.spagobi.engines.weka;

import it.eng.spago.base.SourceBean;
import it.eng.spagobi.utilities.SpagoBIAccessUtils;
import it.eng.spagobi.utilities.callbacks.events.EventsAccessUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import sun.misc.BASE64Decoder;

/**
 * Process weka execution requests and returns bytes of the filled
 * reports
 */
public class WekaServlet extends HttpServlet {
	
	/**
	 * Logger component
	 */
	private static transient Logger logger = Logger
			.getLogger(WekaServlet.class);

	/**
	 * SpagoBI Public Key
	 */
	private transient PublicKey publicKeyDSASbi = null;
		
	/**
	 * security check able or not
	 */
	private transient boolean securityAble = true;


	/**
	 * Input parameters map
	 */
	private Map params = null;
	
	
	
	public static final String TEMPLATE_PATH = "templatePath"; 
	public static final String CR_MANAGER_URL = "cr_manager_url"; 
	public static final String EVENTS_MANAGER_URL = "events_manager_url"; 
	public static final String EVENT = "event"; 
	public static final String USER = "user"; 
	public static final String CONNECTION = "connectionName"; 
	public static final String INPUT_CONNECTION = "inputConnectionName"; 
	public static final String OUTPUT_CONNECTION = "outputConnectionName"; 
	public static final String WRITE_MODE = "writeMode"; 
	public static final String KEYS = "keys";
	public static final String VERSIONING = "versioning";
	public static final String VERSION_COLUMN_NAME = "versionColumnName";
	public static final String VERSION = "version";
	
	
	public class RunnerThread extends Thread {
		private File file = null;		
		
		public RunnerThread(File file) {
			this.file = file;
		}
		
		public void run() {
			logger.info(this.getClass().getName() + ":service: Runner thread started succesfully");
			
			String events_manager_url = (String) params.get(EVENTS_MANAGER_URL);
			EventsAccessUtils eventsAccessUtils = new EventsAccessUtils(events_manager_url);	
			String eventId = (String) params.get(EVENT);
			String user = (String) params.get(USER);
			
			Connection con = getConnection((String)params.get(CONNECTION));
			Connection incon = (con!=null)?con: getConnection((String)params.get(INPUT_CONNECTION));
			Connection outcon = (con!=null)?con: getConnection((String)params.get(OUTPUT_CONNECTION));
			
			if (incon == null || outcon == null) {
				logger.error(this.getClass().getName() +":service:Cannot obtain"
						+ " connection for engine ["
						+ this.getClass().getName() + "] control"
						+ " configuration in engine-config.xml config file");
				return;
			}
			
			WekaKFRunner kfRunner = new WekaKFRunner(incon, outcon);
			
			logger.debug(this.getClass().getName() + ":service:Start parsing file: " + file);
			try {
				kfRunner.loadKFTemplate(file);
				kfRunner.setWriteMode((String)params.get(WRITE_MODE));
				kfRunner.setKeyColumnNames(parseKeysProp((String)params.get(KEYS)));
				String versioning = (String)params.get(VERSIONING);
				if(versioning != null && versioning.equalsIgnoreCase("true")){
					logger.debug(this.getClass().getName() + ":service:versioning activated");
					kfRunner.setVersioning(true);
					String str;
					if( (str = (String)params.get(VERSION_COLUMN_NAME)) != null) 
						kfRunner.setVersionColumnName(str);
					logger.debug(this.getClass().getName() + ":service:version column name is " + kfRunner.getVersionColumnName());
					if( (str = (String)params.get(VERSION)) != null) 
						kfRunner.setVersion(str);
					logger.debug(this.getClass().getName() + ":service:version is " + kfRunner.getVersion());
					
				}
				kfRunner.setupSavers();
				kfRunner.setupLoaders();
				logger.debug(this.getClass().getName() + ":service:Getting loaders & savers infos ...");
				logger.debug( "\n" + Utils.getLoderDesc(kfRunner.getLoaders()) );
				logger.debug( "\n" + Utils.getSaverDesc(kfRunner.getSavers()) );
				logger.debug(this.getClass().getName() + ":service:Executing knowledge flow ...");
				kfRunner.run(false, true);
				Map eventParams = new HashMap();				
				eventParams.put("operation-type", "biobj-execution");
				eventParams.put("operation-exit-status", "success");
				eventParams.put("biobj-path", params.get(TEMPLATE_PATH));
				eventsAccessUtils.fireEvent(eventId, user, "execution of weka flow terminated", eventParams);
			} catch (Exception e) {
				logger.error("Impossible to load/parse templete file", e);	
				eventsAccessUtils.fireEvent(eventId, user, "execution of weka flow terminated with errors", null);
			}
					
			file.delete();		
			
			// TODO put this block in a finally block
			try {	
				// is it correct to close connection retrived as JNDI resoureces ?
				if ((con != null) && (!con.isClosed())) con.close();
				if ((incon != null) && (!incon.isClosed())) incon.close();
				if ((outcon != null) && (!outcon.isClosed())) outcon.close();
			} catch(SQLException sqle){}
	    }				
	}
	
	
	/**
	 * Initialize the engine
	 */
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		logger.debug(this.getClass().getName()
				+ ":init:Initializing SpagoBI Weka Engine...");

		String secAblePar = config.getInitParameter("SECURITY_ABLE");
		if (!secAblePar.equalsIgnoreCase("true")) {
			securityAble = false;
			logger.info(this.getClass().getName()
					+ ":init:Disable security mode");
		}

		if (securityAble) {
			// get spagobi public key
			publicKeyDSASbi = getPublicKey();
			// Identity hashMap
			Map identities = new HashMap();
			// set into the context
			ServletContext context = this.getServletContext();
			context.setAttribute("IDENTITIES", identities);
			logger.info(this.getClass().getName()
					+ ":init:Enable security mode");
		}

		logger.debug(this.getClass().getName() + ":init:Inizialization "
				+ "of SpagoBI Weka Engine ended succesfully");
	}
	
	/**
	 * process weka execution requests
	 */
	public void service(HttpServletRequest request, HttpServletResponse response)
	throws IOException, ServletException {
	
		logger.debug(this.getClass().getName()
				+ ":service:Start processing a new request...");
				
		logger.debug(this.getClass().getName()
				+ ":service:Reading request parameters...");
		
		params = new HashMap();
		Enumeration enumer = request.getParameterNames();
		String parName = null;
		String parValue = null;
				
		while (enumer.hasMoreElements()) {
			parName = (String) enumer.nextElement();
			parValue = request.getParameter(parName);
			params.put(parName, parValue);
			logger.debug(this.getClass().getName() + ":service:Read "
					+ "parameter [" + parName + "] with value [" + parValue
					+ "] from request");
		}
		
		logger.debug(this.getClass().getName()
				+ ":service:Request parameters read sucesfully" + params);
		
		if (securityAble && !authenticate(request, response)) { // problems with user's authentication
			PrintWriter writer = response.getWriter();
			String resp = "<html><body><center><h2>Unauthorized</h2></center></body></html>";
			logger.error(this.getClass().getName()
					+ ":service:Authentication failed");
			writer.write(resp);
			writer.flush();
			writer.close();
			return;
		} else { // OK! the user has been authenticated sucesfully by the system
			
			if (securityAble)
				logger.info(this.getClass().getName() + ":service:Caller authenticated succesfully");
				
			logger.info(this.getClass().getName() + ":service:reading template file ...");
			String templatePath = (String) params.get(TEMPLATE_PATH);
			String cr_manager_url = (String) params.get(CR_MANAGER_URL);				
			byte[] template = new SpagoBIAccessUtils().getContent(cr_manager_url, templatePath);
			logger.info(this.getClass().getName() + ":service:template file has been read succesfully");
				
			logger.info(this.getClass().getName() + ":service:saving tamplete file to local temp dir ...");
			File file = File.createTempFile("weka", null);
			ParametersFiller.fill(new StringReader(new String(template)), new FileWriter(file), params);
						
			logger.info(this.getClass().getName() + ":service:template file saved succesfully to a local temp dir");
			
			// fork
			Thread runner = new RunnerThread(file);
			runner.start();				
						
			logger.info(this.getClass().getName() + ":service: Return the default waiting message");
								
			StringBuffer buffer = new StringBuffer();
			buffer.append("<html>\n");
			buffer.append("<head><title>Service Response</title></head>\n");
			buffer.append("<body>");
			buffer.append("Please wait! The engine is working hard for you at this moment\n");
			buffer.append("</body>\n");
			buffer.append("</html>\n");
			
			response.setContentLength(buffer.length());			
			response.setContentType("text/html");
			PrintWriter writer = response.getWriter();
			writer.print(buffer.toString());
			writer.flush();			
		}
		logger.info(this.getClass().getName() + ":service:Request processed");
	}

	
	
	
	
	
	
	
	/**
	 * This method, based on the engine-config.xml configuration, gets a
	 * database connection and return it
	 * 
	 * @param connectionName
	 *            Logical name of the connection configuration (defined into
	 *            engine-config.xml)
	 * @return the database connection
	 */
	public Connection getConnection(String connectionName) {
		String engineClassName = this.getClass().getName();
		logger.debug("Try to retrieve configuration settings for engine ["
				+ engineClassName + "]");
		SourceBean config = WekaConf.getInstance().getConfig();
		String defaultConnectionName = (String) config
				.getAttribute("CONNECTIONS.default");
		if (defaultConnectionName == null)
			logger.warn("'default' attribute not specified in tag connections");
		else
			logger.debug("default connection name is [" + defaultConnectionName
					+ "]");
		SourceBean defaultConnectionConfig = (SourceBean) config
				.getFilteredSourceBeanAttribute("CONNECTIONS.CONNECTION",
						"name", defaultConnectionName);
		SourceBean connectionConfig = null;
		if (connectionName == null) {
			logger.debug("Connection name not specified for engine ["
					+ engineClassName + "] look for a default connection");
			if (defaultConnectionConfig != null)
				connectionConfig = defaultConnectionConfig;
			else
				return null;
		} else {
			logger.debug("Searching for Connection name [" + connectionName
					+ "] for engine [" + engineClassName + "] ");
			connectionConfig = (SourceBean) config
					.getFilteredSourceBeanAttribute("CONNECTIONS.CONNECTION",
							"name", connectionName);
			if (connectionConfig == null) {
				logger.debug("Connection [" + connectionName
						+ "] not defined for engine [" + engineClassName
						+ "] use default connection");
				if (defaultConnectionConfig != null)
					connectionConfig = defaultConnectionConfig;
				else
					return null;
			}
		}
		// if configuration is jndi get datasource from jndi context
		String jndi = (String) connectionConfig.getAttribute("isJNDI");
		if (jndi.equalsIgnoreCase("true")) {
			return getConnectionFromJndiDS(connectionConfig);
		} else {
			return getDirectConnection(connectionConfig);
		}
	}

	/**
	 * Get the connection from JNDI
	 * 
	 * @param connectionConfig
	 *            SourceBean describing data connection
	 * @return Connection to database
	 */
	private Connection getConnectionFromJndiDS(SourceBean connectionConfig) {
		Connection connection = null;
		String iniCont = (String) connectionConfig
				.getAttribute("initialContext");
		String resName = (String) connectionConfig.getAttribute("resourceName");
		Context initCtx;
		try {
			initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup(iniCont);
			DataSource ds = (DataSource) envCtx.lookup(resName);
			connection = ds.getConnection();
		} catch (NamingException ne) {
			logger.error("JNDI error", ne);
		} catch (SQLException sqle) {
			logger.error("Cannot retrive connection", sqle);
		}
		return connection;
	}

	/**
	 * Get the connection using jdbc
	 * 
	 * @param connectionConfig
	 *            SourceBean describing data connection
	 * @return Connection to database
	 */
	private Connection getDirectConnection(SourceBean connectionConfig) {
		Connection connection = null;
		try {
			String driverName = (String) connectionConfig
					.getAttribute("driver");
			Class.forName(driverName);
			String url = (String) connectionConfig.getAttribute("jdbcUrl");
			String username = (String) connectionConfig.getAttribute("user");
			String password = (String) connectionConfig
					.getAttribute("password");
			connection = DriverManager.getConnection(url, username, password);
		} catch (ClassNotFoundException e) {
			logger.error("Driver not found", e);
		} catch (SQLException e) {
			logger.error("Cannot retrive connection", e);
		}
		return connection;
	}

	/**
	 * Authenticate the caller (must be SpagoBI)
	 * 
	 * @param request
	 *            HttpRequest
	 * @param response
	 *            HttpResponse
	 * @return boolean, true if autheticated false otherwise
	 */
	private boolean authenticate(HttpServletRequest request,
			HttpServletResponse response) {
		String tokenClearStr = (String) request.getParameter("TOKEN_CLEAR");
		if ((tokenClearStr == null) || (tokenClearStr.trim().equals(""))) {
			logger.error("Token clear null or empty");
			return false;
		}
		String tokenSign64 = (String) request.getParameter("TOKEN_SIGN");
		if ((tokenSign64 == null) || (tokenSign64.trim().equals(""))) {
			logger.error("Token signed null or empty");
			return false;
		}
		String identity = (String) request.getParameter("IDENTITY");
		if ((identity == null) || (identity.trim().equals(""))) {
			logger.error("Identity null or empty");
			return false;
		}
		byte[] tokenClear = tokenClearStr.getBytes();
		if (tokenClear == null) {
			logger.error("Token clear after base 64 decoding null");
			return false;
		}
		byte[] tokenSign = decodeBase64(tokenSign64);
		if (tokenClear == null) {
			logger.error("Token clear after base 64 decoding null");
			return false;
		}
		// verify the signature
		boolean sign = verifySignature(tokenClear, tokenSign);
		return sign;
	}

	/**
	 * Get the SpagoBI Public Key for a DSA alghoritm
	 * 
	 * @return Public Key for SpagoBI (DSA alghoritm)
	 */
	private PublicKey getPublicKey() {
		PublicKey pubKey = null;
		SAXReader reader = new SAXReader();
		Document document = null;
		try {
			document = reader.read(getClass().getResourceAsStream(
					"/security-config.xml"));
			Node publicKeyNode = document
					.selectSingleNode("//SECURITY-CONFIGURATION/KEYS/SPAGOBI_PUBLIC_KEY_DSA");
			String namePubKey = publicKeyNode.valueOf("@keyname");
			InputStream publicKeyIs = this.getClass().getClassLoader()
					.getResourceAsStream(namePubKey);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len;
			while ((len = publicKeyIs.read(buffer)) >= 0)
				baos.write(buffer, 0, len);
			publicKeyIs.close();
			baos.close();
			byte[] pubKeyByte = baos.toByteArray();
			// get the public key from bytes
			KeyFactory keyFactory = KeyFactory.getInstance("DSA");
			EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(pubKeyByte);
			pubKey = keyFactory.generatePublic(publicKeySpec);
		} catch (DocumentException de) {
			logger
					.error(
							"getPublicKey:"
									+ "Error during parsing of the security configuration file",
							de);
		} catch (IOException e) {
			logger.error("Error retriving the key file", e);
		} catch (NoSuchAlgorithmException e) {
			logger.error("DSA Alghoritm not avaiable", e);
		} catch (InvalidKeySpecException e) {
			logger.error("Invalid Key", e);
		}
		return pubKey;
	}

	/**
	 * Decode a Base64 String into a byte array
	 * 
	 * @param encoded
	 *            String encoded with Base64 algorithm
	 * @return byte array decoded
	 */
	private byte[] decodeBase64(String encoded) {
		byte[] clear = null;
		try {
			BASE64Decoder decoder = new BASE64Decoder();
			clear = decoder.decodeBuffer(encoded);
			return clear;
		} catch (IOException ioe) {
			logger.error("Error during base64 decoding", ioe);
		}
		return clear;
	}

	/**
	 * Verify the signature
	 * 
	 * @param tokenclear
	 *            Clear data
	 * @param tokensign
	 *            Signed data
	 * @return true if the signature is verified false otherwise
	 */
	private boolean verifySignature(byte[] tokenclear, byte[] tokensign) {
		try {
			Signature sign = Signature.getInstance("DSA");
			sign.initVerify(publicKeyDSASbi);
			sign.update(tokenclear);
			return sign.verify(tokensign);
		} catch (NoSuchAlgorithmException e) {
			logger.error("DSA Algorithm not avaiable", e);
			return false;
		} catch (InvalidKeyException e) {
			logger.error("Invalid Key", e);
			return false;
		} catch (SignatureException e) {
			logger.error("Error while verifing the exception", e);
			return false;
		}
	}
	
	private String[] parseKeysProp(String keysStr) {
		if(keysStr == null) return null;
		return keysStr.split(",");
	}

}
