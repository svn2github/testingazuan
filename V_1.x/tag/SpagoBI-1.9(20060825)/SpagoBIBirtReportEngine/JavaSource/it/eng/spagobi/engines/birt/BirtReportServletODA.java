/**
 * 
 * LICENSE: see BIRT.LICENSE.txt file
 * 
 */
package it.eng.spagobi.engines.birt;

import java.io.ByteArrayOutputStream;
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
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import sun.misc.BASE64Decoder;

public class BirtReportServletODA extends HttpServlet {

	protected transient Logger logger = Logger.getLogger(BirtReportServletODA.class);
	protected transient PublicKey publicKeyDSASbi = null;
	protected transient boolean securityAble = true;
	
	public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        String secAblePar = servletConfig.getInitParameter("SECURITY_ABLE");
        if (!secAblePar.equalsIgnoreCase("true")) {
        	securityAble = false;
        }
        if (securityAble) {
        	// get spagobi public key
        	publicKeyDSASbi = getPublicKey();
        	// Identity hashMap
        	Map identities = new HashMap();
        	// set into the context
        	ServletContext context = this.getServletContext();
        	context.setAttribute("IDENTITIES", identities);
        }
     } // public void init(ServletConfig config) throws ServletException

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
	 * Authenticate the caller (must be SpagoBI)
	 * @param request HttpRequest
	 * @param response HttpResponse
	 * @return boolean, true if autheticated false otherwise
	 */
	protected boolean authenticate(HttpServletRequest request, HttpServletResponse response) {
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

	 	if (securityAble && !authenticate(request, response)) {
	 		PrintWriter writer = response.getWriter();
	 		String resp = "<html><body><center><h2>Unauthorized</h2></center></body></html>";
	 		writer.write(resp);
	 		writer.flush();
	 		writer.close();
	 		return;
	 	} else {
	 		String template = (String)params.get("templatePath");
	 		String spagobibase = (String)params.get("spagobiurl");
	 		String dateformat = (String)params.get("dateformat");
	 		BirtReportRunnerODA birtReportRunner = new BirtReportRunnerODA(spagobibase, template, dateformat);
	 		try {
	 			birtReportRunner.runReport(params, getServletContext(), response, request);
	 		} catch (Exception e){
	 			e.printStackTrace();
	 		}
	 	}
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
