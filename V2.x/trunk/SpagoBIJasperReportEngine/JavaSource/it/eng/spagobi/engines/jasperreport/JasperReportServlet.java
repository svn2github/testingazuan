/**
 * 
 * LICENSE: see COPYING file. 
 * 
 */
package it.eng.spagobi.engines.jasperreport;

import it.eng.spago.base.SourceBean;
import it.eng.spagobi.utilities.ParametersDecoder;
import it.eng.spagobi.utilities.callbacks.audit.AuditAccessUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
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
import java.util.List;
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
 * Process jasper report execution requests and returns bytes of the filled reports 
 */
public class JasperReportServlet extends HttpServlet {

	
	static Map extensions;
	static {
		extensions = new HashMap();
		extensions.put("jrxml", "text/jrxml");
		extensions.put("html", "text/html");
		extensions.put("xml", "text/xml");
		extensions.put("txt", "text/plain");
		extensions.put("csv", "text/csv");
		extensions.put("pdf", "application/pdf");
		extensions.put("rtf", "application/rtf");
		extensions.put("xls", "application/vnd.ms-excel");
	}
	
	/**
	 * Logger component
	 */
	private static transient Logger logger = Logger.getLogger(JasperReportServlet.class);
    /**
     * SpagoBI Public Key 
     */
	private transient PublicKey publicKeyDSASbi = null;

	/**
	 * security check able or not
	 */
	private transient boolean securityAble = true;

	
	
	
	
	/**
	 * Initialize the engine 
	 */
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		logger.debug(this.getClass().getName() +":init:Initializing SpagoBI JasperReport Engine...");
		String secAblePar = config.getInitParameter("SECURITY_ABLE");
		if (!secAblePar.equalsIgnoreCase("true")) {
			securityAble = false;
			logger.info(this.getClass().getName() +":init:Disable security mode");
		}
		if (securityAble) {
			// get spagobi public key
			publicKeyDSASbi = getPublicKey();
		}		
		logger.debug(this.getClass().getName() +":init:Inizialization " +
				      "of SpagoBI JasperReport Engine ended succesfully");
	}

	
	
	/**
	 * process jasper report execution requests
	 */
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		logger.debug(this.getClass().getName() +":service:Start processing a new request...");
		Map params = new HashMap();
		Enumeration enumer = request.getParameterNames();
		String parName = null;
		String parValue = null;
		logger.debug(this.getClass().getName() +":service:Reading request parameters...");
		while (enumer.hasMoreElements()) {
			parName = (String) enumer.nextElement();
			parValue = request.getParameter(parName);
			addParToParMap(params, parName, parValue);
			logger.debug(this.getClass().getName() +":service:Read " +
					"parameter [" + parName + "] with value [" + parValue + "] from request");
		}
		logger.debug(this.getClass().getName() +":service:Request parameters read sucesfully" + params);
		if (securityAble && !authenticate(request, response)) {
			PrintWriter writer = response.getWriter();
			String resp = "<html><body><center><h2>Unauthorized</h2></center></body></html>";
			logger.error(this.getClass().getName() +":service:Authentication failed");
			writer.write(resp);
			writer.flush();
			writer.close();
			return;
		} else {
			if(securityAble) 
				logger.info(this.getClass().getName() +":service:Caller authenticated succesfully");
			
			// AUDIT UPDATE
			String auditId = request.getParameter("SPAGOBI_AUDIT_ID");
			AuditAccessUtils auditAccessUtils = 
				(AuditAccessUtils) request.getSession().getAttribute("SPAGOBI_AUDIT_UTILS");
			if (auditAccessUtils != null) auditAccessUtils.updateAudit(auditId, new Long(System.currentTimeMillis()), null, 
					"EXECUTION_STARTED", null, null);
			
			String template = (String) params.get("templatePath");
			String spagobibase = (String) params.get("spagobiurl");
			JasperReportRunner jasperReportRunner = new JasperReportRunner(spagobibase, template);
			Connection con = getConnection(request.getParameter("connectionName"));
			if (con == null) {
				logger.error(this.getClass().getName() +":service:Cannot obtain"
						+ " connection for engine ["
						+ this.getClass().getName() + "] control"
						+ " configuration in engine-config.xml config file");
				// AUDIT UPDATE
				if (auditAccessUtils != null) auditAccessUtils.updateAudit(auditId, null, new Long(System.currentTimeMillis()), 
						"EXECUTION_FAILED", "No connection available", null);
				return;
			}
			try {
				String outputType = (String) params.get("param_output_format");
				String tmpdir = (String)JasperReportConf.getInstance().getConfig().getAttribute("GENERALSETTINGS.tmpdir");
				if(!tmpdir.startsWith("/")){
					String contRealPath = getServletContext().getRealPath("/");
					if(contRealPath.endsWith("\\")||contRealPath.endsWith("/")) {
						contRealPath = contRealPath.substring(0, contRealPath.length()-1);
					}
					tmpdir = contRealPath + "/" + tmpdir;
				}
				tmpdir = tmpdir + System.getProperty("file.separator") + "reports";
				File dir = new File(tmpdir);
				dir.mkdirs();
				File tmpFile = File.createTempFile("report", "." + outputType, dir);
				OutputStream out = new FileOutputStream(tmpFile);
				jasperReportRunner.runReport(con, params,out, getServletContext(), response, request);
				out.flush();
				out.close();
				
				if(outputType == null) outputType = ExporterFactory.getDefaultType();
				response.setHeader("Content-Disposition", "filename=\"report." + outputType + "\";");
				//response.setContentType((String)extensions.get(outputType));
				response.setContentLength((int)tmpFile.length());
				
				
				
				BufferedInputStream in = new BufferedInputStream(new FileInputStream(tmpFile));			
				int b = -1;
				while((b = in.read()) != -1) {
					response.getOutputStream().write(b);
				}
				response.getOutputStream().flush();
				in.close();
				// instant cleaning
				tmpFile.delete();
				
				// AUDIT UPDATE
				if (auditAccessUtils != null) auditAccessUtils.updateAudit(auditId, null, new Long(System.currentTimeMillis()), 
						"EXECUTION_PERFORMED", null, null);
			} catch (Exception e) {
				logger.error(this.getClass().getName() +":service:error " +
			      			"during report production \n\n " + e);
				// AUDIT UPDATE
				if (auditAccessUtils != null) auditAccessUtils.updateAudit(auditId, null, new Long(System.currentTimeMillis()), 
						"EXECUTION_FAILED", e.getMessage(), null);
			    return;
			}
		}
		logger.info(this.getClass().getName() +":service:Request processed");
	}

	
	
	
	
	/**
	 * @param params
	 * @param parName
	 * @param parValue
	 */
	private void addParToParMap(Map params, String parName, String parValue) {
		String newParValue;
		
		ParametersDecoder decoder = new ParametersDecoder();
		if(decoder.isMultiValues(parValue)) {			
			List values = decoder.decode(parValue);
			newParValue = "";
			for(int i = 0; i < values.size(); i++) {
				newParValue += (i>0?",":"");
				newParValue += values.get(i);
			}
			
		} else {
			newParValue = parValue;
		}
		
		params.put(parName, newParValue);
	}




	/**
	 * This method, based on the engine-config.xml configuration, gets a 
	 * database connection and return it 
	 * @param connectionName Logical name of the connection configuration (defined into engine-config.xml)
	 * @return the database connection
	 */
	public Connection getConnection(String connectionName) {
		String engineClassName = this.getClass().getName();
		logger.debug("Try to retrieve configuration settings for engine ["
						+ engineClassName + "]");
		SourceBean config = JasperReportConf.getInstance().getConfig();					
		String defaultConnectionName = (String)config.getAttribute("CONNECTIONS.default");
		if(defaultConnectionName == null)
			logger.warn("'default' attribute not specified in tag connections");
		else
			logger.debug("default connection name is [" + defaultConnectionName + "]");
		SourceBean defaultConnectionConfig = (SourceBean)config.getFilteredSourceBeanAttribute ("CONNECTIONS.CONNECTION", "name", defaultConnectionName);
		SourceBean connectionConfig = null;
		if(connectionName == null) {
			logger.debug("Connection name not specified for engine ["
					+ engineClassName
					+ "] look for a default connection");
			if(defaultConnectionConfig != null)
				connectionConfig = defaultConnectionConfig;
			else 
				return null;
		}
		else {
			logger.debug("Searching for Connection name ["
					+ connectionName + "] for engine [" + engineClassName
					+ "] ");
			connectionConfig = (SourceBean) config.getFilteredSourceBeanAttribute ("CONNECTIONS.CONNECTION", "name", connectionName);
			if(connectionConfig == null) {
				logger.debug("Connection ["
						+ connectionName + "] not defined for engine ["
						+ engineClassName
						+ "] use default connection");
				if(defaultConnectionConfig != null)
					connectionConfig = defaultConnectionConfig;
				else 
					return null;
			}
		}
		// if configuration is jndi get datasource from jndi context
		String jndi = (String)connectionConfig.getAttribute("isJNDI");
		if (jndi.equalsIgnoreCase("true")) {
			return getConnectionFromJndiDS(connectionConfig);
		} else {
			return getDirectConnection(connectionConfig);
		}
	}

	
	
	
	/**
	 * Get the connection from JNDI
	 * @param connectionConfig SourceBean describing data connection
	 * @return Connection to database
	 */
	private Connection getConnectionFromJndiDS(SourceBean connectionConfig) {
		Connection connection = null;
		String iniCont = (String)connectionConfig.getAttribute("initialContext");
		String resName = (String)connectionConfig.getAttribute("resourceName");
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
	 * @param connectionConfig SourceBean describing data connection
	 * @return Connection to database
	 */
	private Connection getDirectConnection(SourceBean connectionConfig) {
		Connection connection = null;
		try {
			String driverName = (String)connectionConfig.getAttribute("driver");
			Class.forName(driverName);
			String url = (String)connectionConfig.getAttribute("jdbcUrl");
			String username = (String)connectionConfig.getAttribute("user");
			String password = (String)connectionConfig.getAttribute("password");
			connection = DriverManager.getConnection(url, username, password);
		} catch (ClassNotFoundException e) {
			logger.error("Driver not found", e);
		} catch (SQLException e) {
			logger
					.error("Cannot retrive connection", e);
		}
		return connection;
	}

	
	
	
	/**
	 * Authenticate the caller (must be SpagoBI)
	 * @param request HttpRequest
	 * @param response HttpResponse
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
					.error("getPublicKey:"
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
	 * @param encoded String encoded with Base64 algorithm
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
	 * @param tokenclear Clear data
	 * @param tokensign Signed data
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

}
