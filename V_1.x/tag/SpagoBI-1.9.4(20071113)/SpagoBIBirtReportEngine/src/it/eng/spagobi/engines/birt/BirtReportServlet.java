/**
 * 
 * LICENSE: see BIRT.LICENSE.txt file
 * 
 */
package it.eng.spagobi.engines.birt;

import it.eng.spagobi.engines.birt.exceptions.ConnectionDefinitionException;
import it.eng.spagobi.engines.birt.exceptions.ConnectionParameterNotValidException;
import it.eng.spagobi.engines.birt.exceptions.ConnectionsDefinitionFileNotValidException;
import it.eng.spagobi.engines.birt.utilities.ParameterConverter;
import it.eng.spagobi.utilities.ParametersDecoder;
import it.eng.spagobi.utilities.callbacks.audit.AuditAccessUtils;

import java.io.ByteArrayOutputStream;
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
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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
import org.eclipse.birt.report.IBirtConstants;
import org.eclipse.birt.report.engine.api.HTMLActionHandler;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.HTMLServerImageHandler;
import org.eclipse.birt.report.engine.api.IGetParameterDefinitionTask;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.IScalarParameterDefn;
import org.eclipse.birt.report.engine.api.PDFRenderOption;
import org.eclipse.birt.report.utility.BirtUtility;

import sun.misc.BASE64Decoder;

public class BirtReportServlet extends HttpServlet {

	private IReportEngine birtReportEngine = null;
	protected static Logger logger = Logger.getLogger(BirtReportServlet.class);
	
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
        BirtEngine.initBirtConfig();
     } // public void init(ServletConfig config) throws ServletException

	public void destroy() {
		super.destroy();
		BirtEngine.destroyBirtEngine();
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
		} catch (DocumentException de) {
			logger.error(this.getClass().getName()+ ":getPublicKey:" +
					 "Error during parsing of the security configuration file", de);
		} catch (IOException e) {
			logger.error(this.getClass().getName()+ ":getPublicKey:" +
					"Error retriving the key file", e);
		} catch (NoSuchAlgorithmException e) {
			logger.error(this.getClass().getName()+ ":getPublicKey:" +
					 "DSA Alghoritm not avaiable", e);
		} catch (InvalidKeySpecException e) {
			logger.error(this.getClass().getName()+ ":getPublicKey:" +
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
		String tokenClearStr = (String)request.getParameter("TOKEN_CLEAR");
	    if (tokenClearStr == null || tokenClearStr.trim().equals("")) {
	    	logger.error(this.getClass().getName()+ ":authenticate:" +
	                 "Token clear null or empty");
	        return false;
	    }
		String tokenSign64 = (String)request.getParameter("TOKEN_SIGN");
		if (tokenSign64 == null || tokenSign64.trim().equals("")) {
			logger.error(this.getClass().getName()+ ":authenticate:" +
		                 "Token signed null or empty");
		    return false;
		}
	    String identity = (String)request.getParameter("IDENTITY");
	    if (identity == null || identity.trim().equals("")) {
	    	logger.error(this.getClass().getName()+ ":authenticate:" +
		                 "Identity null or empty");
		    return false;
		}	
		byte[] tokenClear = tokenClearStr.getBytes();
		if (tokenClear == null) {
			logger.error(this.getClass().getName()+ ":authenticate:" +
		                 "Token clear after base 64 decoding null");
		    return false;
		}
		byte[] tokenSign = decodeBase64(tokenSign64); 
		if (tokenClear == null) {
			logger.error(this.getClass().getName()+ ":authenticate:" +
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
		
		logger.info(this.getClass().getName() + ":service: Start processing a new request...");	

	 	if (securityAble && !authenticate(request, response)) {
	 		logger.error(this.getClass().getName()+ ":service:" + "Authentication failed");
	 		PrintWriter writer = response.getWriter();
	 		String resp = "<html><body><center><h2>Unauthorized</h2></center></body></html>";
	 		writer.write(resp);
	 		writer.flush();
	 		writer.close();
	 		return;
	 	} else {
	 		if (securityAble) 
				logger.info(this.getClass().getName() +":service: Caller authenticated succesfully");
	 		
			// AUDIT UPDATE
			String auditId = request.getParameter("SPAGOBI_AUDIT_ID");
			AuditAccessUtils auditAccessUtils = 
				(AuditAccessUtils) request.getSession().getAttribute("SPAGOBI_AUDIT_UTILS");
			if (auditAccessUtils != null) auditAccessUtils.updateAudit(auditId, new Long(System.currentTimeMillis()), null, 
					"EXECUTION_STARTED", null, null);
	 		try {
	 			runReport(request, response);
				// AUDIT UPDATE
	 			if (auditAccessUtils != null) auditAccessUtils.updateAudit(auditId, null, new Long(System.currentTimeMillis()), 
						"EXECUTION_PERFORMED", null, null);
	 		} catch (ConnectionDefinitionException e){
	 			logger.error(this.getClass().getName()+ ":service:" +
		      			"Error during report production \n\n " + e);
		 		PrintWriter writer = response.getWriter();
		 		String resp = "<html><body><center>" + e.getDescription() + "</center></body></html>";
		 		writer.write(resp);
		 		writer.flush();
		 		writer.close();
				// AUDIT UPDATE
				if (auditAccessUtils != null) auditAccessUtils.updateAudit(auditId, null, new Long(System.currentTimeMillis()), 
						"EXECUTION_FAILED", e.getDescription(), null);
	 		} catch (Exception e){
	 			logger.error(this.getClass().getName()+ ":service:" +
		      			"Error during report production \n\n ", e);
				// AUDIT UPDATE
				if (auditAccessUtils != null) auditAccessUtils.updateAudit(auditId, null, new Long(System.currentTimeMillis()), 
						"EXECUTION_FAILED", e.getMessage(), null);
	 		}
	 	}
	 	
	 	logger.info(this.getClass().getName() + ":service: Request processed");
	 	
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
			logger.error(this.getClass().getName()+ ":decodeBase64:" +
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
			logger.error(this.getClass().getName()+ ":verifySignature:" + 
				     "DSA Algorithm not avaiable", e);
			return false;
		} catch (InvalidKeyException e) {
			logger.error(this.getClass().getName()+ ":verifySignature:" +  
				     "Invalid Key", e);
			return false;
		} catch (SignatureException e) {
			logger.error(this.getClass().getName()+ ":verifySignature:" + 
				     "Error while verifing the exception", e);
			return false;
		} 
	}
	
	private String decodeParameter(Object paramaterValue) {
		if (paramaterValue == null) return null;
		else {
			String paramaterValueStr = paramaterValue.toString();
			String toReturn = "";
			ParametersDecoder decoder = new ParametersDecoder();
			if (decoder.isMultiValues(paramaterValueStr)) {			
				List values = decoder.decode(paramaterValueStr);
				//toReturn = (String) values.get(0);
				for(int i = 0; i < values.size(); i++) {
					toReturn += (i > 0 ? "," : "");
					toReturn += values.get(i);
				}
			} else {
				toReturn = paramaterValueStr;
			}
			return toReturn;
		}
	}
	
	
	protected HTMLRenderOption prepareHtmlRenderOption (ServletContext servletContext, 
			HttpServletRequest servletRequest) throws Exception {
		
		String imageDirectory = servletContext.getRealPath("/report/images");
		String contextPath = servletRequest.getContextPath();
		String imageBaseUrl = "/BirtImageServlet?imagePath=" + "/report/images" + "&" + "imageID=";
		
		// Register new image handler
		HTMLRenderOption renderOption = new HTMLRenderOption();
		renderOption.setActionHandler(new HTMLActionHandler());
		HTMLServerImageHandler imageHandler = new HTMLServerImageHandler();
		renderOption.setImageHandler(imageHandler);
		renderOption.setImageDirectory(imageDirectory);
		renderOption.setBaseImageURL(contextPath + imageBaseUrl);
		renderOption.setEmbeddable(false);
		this.birtReportEngine.getConfig().getEmitterConfigs().put("html", renderOption);
		
		return renderOption;
		
		/*
		HTMLEmitterConfig emitterConfig = new HTMLEmitterConfig();
		emitterConfig.setActionHandler(new HTMLActionHandler());
		HTMLServerImageHandler imageHandler = new HTMLServerImageHandler();
		emitterConfig.setImageHandler(imageHandler);
		this.birtReportEngine.getConfig().getEmitterConfigs().put("html", emitterConfig);
		
		HTMLRenderContext renderContext = new HTMLRenderContext();
		renderContext.setImageDirectory(imageDirectory);
		renderContext.setBaseImageURL(contextPath + imageBaseUrl);
		renderContext.setBaseURL(contextPath + "/frameset");
		//renderContext.setSupportedImageFormats( svgFlag ? "PNG;GIF;JPG;BMP;SVG" : "PNG;GIF;JPG;BMP" ); //$NON-NLS-1$ //$NON-NLS-2$
		
		HashMap appContext = new HashMap();
		//appContext.put(DataEngine.DATASET_CACHE_OPTION, Boolean.TRUE);
		appContext.put(EngineConstants.APPCONTEXT_BIRT_VIEWER_HTTPSERVET_REQUEST, servletRequest);
		appContext.put(EngineConstants.APPCONTEXT_CLASSLOADER_KEY, BirtReportServlet.class.getClassLoader());
		//appContext.put(EngineConstants.APPCONTEXT_HTML_RENDER_CONTEXT, renderContext);
		appContext.put(EngineConstants.APPCONTEXT_HTML_RENDER_CONTEXT, renderOption);
		*/

	}
	
	private InputStream getTemplateContent(HttpServletRequest servletRequest) throws IOException {
		InputStream is = null;		
		
		String templateBase64Coded = (String) servletRequest.getParameter("template");
		BASE64Decoder bASE64Decoder = new BASE64Decoder();
		byte[] templateContent = bASE64Decoder.decodeBuffer(templateBase64Coded);
				
		is = new java.io.ByteArrayInputStream(templateContent);	
		
		return is;
	}
	
	protected Map findReportParams(HttpServletRequest request, IReportRunnable design) throws ConnectionDefinitionException {
		
		String dateformat = request.getParameter("dateformat");
		if (dateformat != null) {
			dateformat = dateformat.replaceAll("D", "d");
			dateformat = dateformat.replaceAll("m", "M");
			dateformat = dateformat.replaceAll("Y", "y");
		}
		
		HashMap toReturn = new HashMap();
		IGetParameterDefinitionTask task = birtReportEngine.createGetParameterDefinitionTask(design);
		Collection paramsColl = task.getParameterDefns(false);
		Iterator it = paramsColl.iterator();
		while (it.hasNext()) {
			IScalarParameterDefn param = (IScalarParameterDefn) it.next();
			String paramName = param.getName();
			String paramValueString = request.getParameter(paramName);
			paramValueString = decodeParameter(paramValueString);
			
			if (paramName.trim().equalsIgnoreCase("connectionName")) {
				String connection = findConnectionName(paramValueString, param);
				if (connection != null) toReturn.put(paramName, connection);
				continue;
			}
			
			if (paramValueString == null || paramValueString.trim().equals("")) {
				logger.debug(this.getClass().getName()+ "findReportParams() The report parameter " + paramName + " has no values set.");
				continue;
			}
			
			int paramType = param.getDataType();
			/* 
			 * The ParameterConverter converts a single value.
			 * Multi-value parameters are assumed to contains values that are String type. 
			 * If they are not Strings (list of dates, list of numbers, ...) the converter will not work.
			 */
			Object paramValue = ParameterConverter.convertParameter(paramType, paramValueString, dateformat);
			if (paramValue == null) paramValue = paramValueString;
			
			toReturn.put(paramName, paramValue);
			
		}

		return toReturn;
	}
	
	private String findConnectionName(String requestConnectionParameterValue, IScalarParameterDefn param) throws ConnectionDefinitionException {
		if (requestConnectionParameterValue != null && !requestConnectionParameterValue.trim().equals("")) {
			logger.debug(this.getClass().getName()+ "findConnectionName(): " +
					"Connection name specified in request with value [" + requestConnectionParameterValue + "]");
			String configuredJndiConnection = findConfiguredConnection("name", requestConnectionParameterValue);
			if (configuredJndiConnection != null) {
				logger.debug(this.getClass().getName()+ "findConnectionName(): " +
						"Connection name found in config file.");
		        return configuredJndiConnection;
			} else {
				logger.error(this.getClass().getName()+ "findConnectionName(): " +
					"Connection name NOT found in config file. " +
					"Request connection parameter will not be changed.");
				throw new ConnectionParameterNotValidException("Connection with name [" + 
						requestConnectionParameterValue + "] not found in engine-config.xml file.");
			}
		} else {
			logger.debug(this.getClass().getName()+ "findConnectionName() Connection name " +
					"not specified in request; looking for a default connection" );
			String defaultJndiConnection = findConfiguredConnection("isDefault", "true");
			if (defaultJndiConnection != null) {
				logger.debug(this.getClass().getName()+ "findConnectionName(): " +
						"Default connection name found in config file.");
				return defaultJndiConnection;
			} else {
				logger.error(this.getClass().getName()+ "findConnectionName(): " +
					"Default connection name NOT found in config file. " +
					"Returning null.");
				throw new ConnectionParameterNotValidException("No default connection defined in " +
						"engine-config.xml file.");
			}
		}
	}
	
	private String findConfiguredConnection(String attributeName, String attributeValue) throws ConnectionDefinitionException {
		String toReturn = null;
		try {
			// read the engines configuration file
			SAXReader reader = new SAXReader();
			Document document = null;
			try {
				document = reader.read(getClass().getResourceAsStream("/engine-config.xml"));
			} catch (DocumentException de) {
				logger.error(this.getClass().getName()+ "findConfiguredConnection():"+
						     "Error during parsing of the engine configuration file", de);
				throw new ConnectionsDefinitionFileNotValidException("engine-config.xml file is not correct");
			}
			// get the engine configuration node
			Node connectionDescriptorNode = document.selectSingleNode( "//ENGINE-CONFIGURATION/CONNECTION" +
					"[@" + attributeName + "='" + attributeValue + "'");
			if (connectionDescriptorNode != null) {
				logger.debug(this.getClass().getName()+ "findConfiguredConnection(): " +
						"Default connection name found in config file.");
				String iniCont = connectionDescriptorNode.valueOf("@initialContext");
		        String resName = connectionDescriptorNode.valueOf("@resourceName");
		        
		        if (iniCont == null || iniCont.trim().equals("") || 
		        		resName == null || resName.trim().equals("")) {
		        	String nodeName = connectionDescriptorNode.valueOf("@name");
		        	logger.error(this.getClass().getName()+ "getConnection(): " +
		        			"Missing initial context or resource name for connection [" 
		        			+ nodeName + "]" +
		        					". Control engine-config.xml file.");
		        	throw new ConnectionDefinitionException("" +
		        			"Missing initial context or resource name for connection [" 
		        			+ nodeName + "]" +
		        					". Control engine-config.xml file.");
		        }
		        
		        if (iniCont == null || iniCont.trim().equals("")) {
		        	toReturn = resName;
		        } else {
		        	toReturn = iniCont + "/" + resName;
		        }
			}
		} catch (ConnectionDefinitionException e) {
			throw e;
		} catch (Exception e) {
			logger.error(this.getClass().getName()+ "findConfiguredConnection():"+
					     "Error during parsing of the engine configuration file", e);
		}
		return toReturn;
	}
	
	protected void runReport(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		ServletContext servletContext = getServletContext();
		this.birtReportEngine = BirtEngine.getBirtEngine(request, servletContext);
		IReportRunnable design = null;
		InputStream is = getTemplateContent(request);
		logger.debug(this.getClass().getName() + "runReport(): template document retrieved.");
		// Open the report design
		design = birtReportEngine.openReportDesign(is);
		logger.debug(this.getClass().getName() + "runReport(): report design opened successfully.");
		// Create task to run and render the report,
		IRunAndRenderTask task = birtReportEngine.createRunAndRenderTask(design);
		logger.debug(this.getClass().getName() + "runReport(): RunAndRenderTask created successfully.");
		// Set parameters for the report
		Map reportParams = findReportParams(request, design);
		task.setParameterValues(reportParams);
		task.validateParameters();

		String outputFormat = request.getParameter("param_output_format");
		logger.debug(this.getClass().getName()+ "runReport() param_output_format -- [" + outputFormat +"]");
		String templateFileName = request.getParameter("template_file_name");
		if (templateFileName == null || templateFileName.trim().equals("")) templateFileName = "report";
		IRenderOption renderOption = null;
		
		if (outputFormat != null && outputFormat.equalsIgnoreCase(IBirtConstants.PDF_RENDER_FORMAT)) {
			renderOption = new PDFRenderOption();			
			renderOption.setOutputFormat(IBirtConstants.PDF_RENDER_FORMAT);
			// renderOption.setOutputFileName produces a file on bin directory that is not 
			// sent to response
			//renderOption.setOutputFileName(templateFileName + ".pdf");
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition","inline; filename=" + templateFileName + ".pdf" );
		} else if (outputFormat != null && outputFormat.equalsIgnoreCase(IBirtConstants.HTML_RENDER_FORMAT)) {
			renderOption = prepareHtmlRenderOption(servletContext, request);
			renderOption.setOutputFormat(IBirtConstants.HTML_RENDER_FORMAT);
		} else if (outputFormat != null && outputFormat.equalsIgnoreCase(IBirtConstants.DOC_RENDER_FORMAT)) {
			renderOption = prepareHtmlRenderOption(servletContext, request);
			renderOption.setOutputFormat(IBirtConstants.DOC_RENDER_FORMAT);
			//renderOption.setOutputFileName(templateFileName + ".doc");
			response.setContentType("application/msword");
			response.setHeader("Content-disposition","inline; filename=" + templateFileName + ".doc" );
		} else if (outputFormat != null && outputFormat.equalsIgnoreCase("xls")) {
			renderOption = prepareHtmlRenderOption(servletContext, request);
			renderOption.setOutputFormat("xls");
			//renderOption.setOutputFileName(templateFileName + ".xls");
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition","inline; filename=" + templateFileName + ".xls" );
		} else if (outputFormat != null && outputFormat.equalsIgnoreCase("ppt")) {
			renderOption = prepareHtmlRenderOption(servletContext, request);
			renderOption.setOutputFormat("ppt");
			//renderOption.setOutputFileName(templateFileName + ".ppt");
			response.setContentType("application/vnd.ms-powerpoint");
			response.setHeader("Content-disposition","inline; filename=" + templateFileName + ".ppt" );
		} else if (outputFormat != null && outputFormat.equalsIgnoreCase(IBirtConstants.POSTSCRIPT_RENDER_FORMAT)) {
			renderOption = new PDFRenderOption();
			renderOption.setOutputFormat(IBirtConstants.POSTSCRIPT_RENDER_FORMAT);
			//renderOption.setOutputFileName(templateFileName + ".ps");
			response.setHeader("Content-disposition","inline; filename=" + templateFileName + ".ps" );
		} else {
			logger.debug(this.getClass().getName()+ "runReport() Output format parameter not set or not valid. Using default output format: HTML.");
			renderOption = prepareHtmlRenderOption(servletContext, request);
			renderOption.setOutputFormat(IBirtConstants.HTML_RENDER_FORMAT);
		}
		Map context = BirtUtility.getAppContext(request, BirtReportServlet.class.getClassLoader());
		task.setAppContext(context);
		renderOption.setOutputStream((OutputStream) response.getOutputStream());
		task.setRenderOption(renderOption);
		task.run();
		task.close();
		
	}
	
}
