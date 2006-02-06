/*
 * Created on 4-mag-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package it.eng.spagobi.engines.jasperreport;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
import org.omg.PortableServer.ID_ASSIGNMENT_POLICY_ID;

import sun.misc.BASE64Decoder;

public class JasperReportServlet extends HttpServlet{
	
	private transient Logger logger = Logger.getLogger(JasperReportServlet.class);
	private transient PublicKey publicKeyDSASbi = null;
	private transient boolean securityAble = true;
	
	public void init(ServletConfig config) throws ServletException {
        super.init(config);
        String secAblePar = config.getInitParameter("SECURITY_ABLE");
        if(!secAblePar.equalsIgnoreCase("true")) {
        	securityAble = false;
        }
        if(securityAble) {
        	// get spagobi public key
        	publicKeyDSASbi = getPublicKey();
        	// Identity hashMap
        	Map identities = new HashMap();
        	// set into the context
        	ServletContext context = this.getServletContext();
        	context.setAttribute("IDENTITIES", identities);
        }
     } // public void init(ServletConfig config) throws ServletException
    
	public void service(
	        HttpServletRequest request,
	        HttpServletResponse response)
	        throws IOException, ServletException {
 	
 		Map params = new HashMap();
	 	Enumeration enumer = request.getParameterNames();
	 	String parName = null;
	 	String parValue = null;
	 	while (enumer.hasMoreElements()){
	 		parName = (String)enumer.nextElement();
	 		parValue = request.getParameter(parName);
	 		params.put(parName, parValue);
	 	}		

	 	if( securityAble && !authenticate(request, response) ) {
	 		PrintWriter writer = response.getWriter();
	 		String resp = "<html><body><center><h2>Unauthorized</h2></center></body></html>";
	 		writer.write(resp);
	 		writer.flush();
	 		writer.close();
	 		return;
	 	} else {
	 		String template = (String)params.get("templatePath");
	 		String spagobibase = (String)params.get("spagobiurl");
	 		JasperReportRunner jasperReportRunner = new JasperReportRunner(spagobibase, template);
	 		Connection con = getConnection(request.getParameter("connectionName"));
	 		if (con == null){
	 			logger.error("Engines "+this.getClass().getName()+ " service() Cannot obtain" +
	 				     " connection for engine ["+this.getClass().getName()+"] control" +
	 				     " configuration in engine-config.xml config file");
	 			return;
	 		}
	 		try{
	 			byte[] theReport = jasperReportRunner.runReport(con, params, getServletContext(), response, request);
	 			response.setContentLength(theReport.length);
	 			response.getOutputStream().write(theReport);
	 			response.getOutputStream().flush();
	 		}catch(Exception e){
	 			e.printStackTrace();
	 		}
	 	}
	 }
	
	/**
	 *  This methos see in spagbi.xml config file ( in section <ENGINE className=engineName> if a connection is configured 
	 *  if null is pass to this method a default datasource is returned if exist
	 * @param connectionName
	 * @return
	 */
	public Connection getConnection(String connectionName) {
		
		String engineClassName = this.getClass().getName();
		logger.debug("Engines" + this.getClass().getName()+ "getConnection() Try to retrieve configuration settings for engine [" + engineClassName + "]");
		// read the engines configuration file
		SAXReader reader = new SAXReader();
		Document document = null;
		try{
			document = reader.read(getClass().getResourceAsStream("/engine-config.xml"));
		}catch(DocumentException de){
			logger.error("Engines"+ this.getClass().getName()+ "getConnection():"+
					     "Error during parsing of the engine configuration file", de);
		}
		// get the engine configuration node
        Node connectionDescriptorNode = null;
		if(connectionName == null){
			logger.debug("Engines"+ this.getClass().getName()+ "getConnection() Connection name not specified for engine [" + engineClassName + "] look for a default connection" );
			connectionDescriptorNode = document.selectSingleNode( "//ENGINE-CONFIGURATION/CONNECTION[@isDefault='true'");
		}else{
			logger.debug("Engines"+this.getClass().getName()+ "getConnection() Searching for Connection name [" + connectionName +"] for engine [" + engineClassName + "] " );
			connectionDescriptorNode = document.selectSingleNode( "//ENGINE-CONFIGURATION/CONNECTION[@name='"+connectionName+"'");
		}
		// if configuration is jndi get datasource from jndi context
		String jndi = connectionDescriptorNode.valueOf("@isJNDI");
		if(jndi.equalsIgnoreCase("true")) {
			return getConnectionFromJndiDS(connectionDescriptorNode);
		} else {
			return getDirectConnection(connectionDescriptorNode);
		}
	}
	
	
	/**
	 * Get the connection from JNDI
	 * @param conDesc Xml node describing data connection
	 * @return Connection to database
	 */
	private Connection getConnectionFromJndiDS(Node conDesc) {
		Connection connection = null;
		String iniCont = conDesc.valueOf("@initialContext");
        String resName = conDesc.valueOf("@resourceName");
		Context initCtx;
		try {
			initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup(iniCont);
			DataSource ds = (DataSource) envCtx.lookup(resName);
			connection = ds.getConnection();
		} catch (NamingException ne) {
			logger.error("Engines"+ this.getClass().getName()+ "getConnectionFromJndiDS():"+
			         "JNDI error", ne);
		} catch (SQLException sqle) {
			logger.error("Engines"+ this.getClass().getName()+ "getConnectionFromJndiDS():"+
			         "Cannot retrive connection", sqle);
		}
		return connection;
	}
	
	
	/**
	 * Get the connection using jdbc (connection string direct to database)
	 * @param conDesc Xml node describing data connection
	 * @return Connection to database
	 */
	private Connection getDirectConnection(Node conDesc) {
		Connection connection = null;
	    try {
	        String driverName = conDesc.valueOf("@driver");
	        Class.forName(driverName);
	        String url = conDesc.valueOf("@jdbcUrl");
	        String username = conDesc.valueOf("@user");
	        String password = conDesc.valueOf("@password");
	        connection = DriverManager.getConnection(url, username, password);
	    } catch (ClassNotFoundException e) {
	    	logger.error("Engines"+ this.getClass().getName()+ "getDirectConnection():"+
				         "Driver not found", e);
	    } catch (SQLException e) {
	    	logger.error("Engines"+ this.getClass().getName()+ "getDirectConnection():"+
			             "Cannot retrive connection", e);
	    }
	    return connection;
	}

	
	
	/**
	 * Authenticate the caller (must be SpagoBI)
	 * @param request HttpRequest
	 * @param response HttpResponse
	 * @return boolean, true if autheticated false otherwise
	 */
	private boolean authenticate(HttpServletRequest request, HttpServletResponse response) {
		//String tokenClear64 = (String)request.getParameter("TOKEN_CLEAR");
		//if( (tokenClear64==null) || (tokenClear64.trim().equals("")) ) {
		//	logger.error("Engines"+ this.getClass().getName()+ "authenticate:"+
		//                 "Token clear null or empty");
		//    return false;
		//}
		// decode base 64 
		// byte[] tokenClear = decodeBase64(tokenClear64); 
		String tokenClearStr = (String)request.getParameter("TOKEN_CLEAR");
	    if( (tokenClearStr==null) || (tokenClearStr.trim().equals("")) ) {
	    	logger.error("Engines"+ this.getClass().getName()+ "authenticate:"+
	                 "Token clear null or empty");
	        return false;
	    }
		String tokenSign64 = (String)request.getParameter("TOKEN_SIGN");
		if( (tokenSign64==null) || (tokenSign64.trim().equals("")) ) {
			logger.error("Engines"+ this.getClass().getName()+ "authenticate:"+
		                 "Token signed null or empty");
		    return false;
		}
	    String identity = (String)request.getParameter("IDENTITY");
	    if( (identity==null) || (identity.trim().equals("")) ) {
			logger.error("Engines"+ this.getClass().getName()+ "authenticate:"+
		                 "Identity null or empty");
		    return false;
		}	
		byte[] tokenClear = tokenClearStr.getBytes();
		if(tokenClear==null) {
			logger.error("Engines"+ this.getClass().getName()+ "authenticate:"+
		                 "Token clear after base 64 decoding null");
		    return false;
		}
		byte[] tokenSign = decodeBase64(tokenSign64); 
		if(tokenClear==null) {
			logger.error("Engines"+ this.getClass().getName()+ "authenticate:"+
		                 "Token clear after base 64 decoding null");
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
		try{
			document = reader.read(getClass().getResourceAsStream("/security-config.xml"));
			Node publicKeyNode = document.selectSingleNode( "//SECURITY-CONFIGURATION/KEYS/SPAGOBI_PUBLIC_KEY_DSA");
			String namePubKey =  publicKeyNode.valueOf("@keyname");
			InputStream publicKeyIs = this.getClass().getClassLoader().getResourceAsStream(namePubKey);	    
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
		}catch(DocumentException de){
			logger.error("Engines"+ this.getClass().getName()+ "getPublicKey:"+
						 "Error during parsing of the security configuration file", de);
		} catch (IOException e) {
			logger.error("Engines"+ this.getClass().getName()+ "getPublicKey:"+
					 "Error retriving the key file", e);
		} catch (NoSuchAlgorithmException e) {
			logger.error("Engines"+ this.getClass().getName()+ "getPublicKey:"+
					 "DSA Alghoritm not avaiable", e);
		} catch (InvalidKeySpecException e) {
			logger.error("Engines"+ this.getClass().getName()+ "getPublicKey:"+
					 "Invalid Key", e);
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
		try{
			BASE64Decoder decoder = new BASE64Decoder();
			clear = decoder.decodeBuffer(encoded);
			return clear;
		} catch (IOException ioe) {
			logger.error("Engines"+ this.getClass().getName()+ "getPublicKey:"+
					     "Error during base64 decoding", ioe);
		}
		return clear;
	}
	
	
	/**
	 * Verify the signature 
	 * @param tokenclear Clear data
	 * @param tokensign Signed data
	 * @return
	 */
	private boolean verifySignature(byte[] tokenclear, byte[] tokensign) {
		try {
			Signature sign = Signature.getInstance("DSA");
			sign.initVerify(publicKeyDSASbi);
			sign.update(tokenclear);
			return sign.verify(tokensign);
		} catch (NoSuchAlgorithmException e) {
			logger.error("Engines"+ this.getClass().getName()+ "verifySignature:"+
				     "DSA Algorithm not avaiable", e);
			return false;
		} catch (InvalidKeyException e) {
			logger.error("Engines"+ this.getClass().getName()+ "verifySignature:"+
				     "Invalid Key", e);
			return false;
		} catch (SignatureException e) {
			logger.error("Engines"+ this.getClass().getName()+ "verifySignature:"+
				     "Error while verifing the exception", e);
			return false;
		} 
	}
	
	

	
}
