/**
 * 
 * LICENSE: see BIRT.LICENSE.txt file
 * 
 */
package it.eng.spagobi.engines.birt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
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
import org.safehaus.uuid.UUID;
import org.safehaus.uuid.UUIDGenerator;
import org.xml.sax.InputSource;

import sun.misc.BASE64Decoder;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.commons.utilities.SpagoBIUtilities;
import it.eng.spagobi.commons.utilities.StringUtilities;
import it.eng.spagobi.engines.birt.exceptions.ConnectionDefinitionException;
import it.eng.spagobi.engines.birt.utilities.ParameterConverter;
import it.eng.spagobi.services.common.EnginConf;
import it.eng.spagobi.services.common.SsoServiceFactory;
import it.eng.spagobi.services.common.SsoServiceInterface;
import it.eng.spagobi.services.content.bo.Content;
import it.eng.spagobi.services.proxy.ContentServiceProxy;
import it.eng.spagobi.utilities.DynamicClassLoader;
import it.eng.spagobi.utilities.ParametersDecoder;
import it.eng.spagobi.utilities.SpagoBIAccessUtils;
import it.eng.spagobi.utilities.engines.AbstractEngineStartServlet;
import it.eng.spagobi.utilities.engines.EngineStartServletIOManager;
import it.eng.spagobi.utilities.engines.SpagoBIEngineException;

/**
 * @author Zerbetto (davide.zerbetto@eng.it)
 * 
 * DATE            CONTRIBUTOR/DEVELOPER                        NOTE
 * 02-10-2008      Zerbetto Davide/Julien Decreuse (Smile)		Upgrade to Birt 2.3.0 API
 **/
public class BirtReportServletX extends AbstractEngineStartServlet {

	private IReportEngine birtReportEngine = null;
	protected static Logger logger = Logger.getLogger(BirtReportServletX.class);
	private static String CONNECTION_NAME = "connectionName";
	public static final String JS_EXT_ZIP = ".zip";
	public static final String JS_FILE_ZIP = "JS_File";
	public static final String RTF_FORMAT = "RTF";
	private String flgTemplateStandard = "true";
	Map reportParams = null;

	
	public void init(ServletConfig servletConfig) throws ServletException {
		super.init(servletConfig);
		logger.debug("Initializing SpagoBI BirtReport Engine...");
		BirtEngine.initBirtConfig();
		logger.debug(":init:Inizialization of SpagoBI BirtReport Engine ended succesfully");
	}

	public void destroy() {
		super.destroy();
		BirtEngine.destroyBirtEngine();
	}

	public void doService( EngineStartServletIOManager servletIOManager ) throws SpagoBIEngineException {

		logger.debug("IN");
		
		try {
			logger.debug("User Id: " + servletIOManager.getUserId());
			logger.debug("Document Id: " + servletIOManager.getDocumentId());
			logger.debug("Audit Id: " + servletIOManager.getAuditId());
			
			servletIOManager.auditServiceStartEvent();
			
			runReport(servletIOManager);
			servletIOManager.auditServiceEndEvent();	

		} catch (Throwable t) {
			logger.error("Error during report production \n\n " + e);
			String message = "<html><body><center>" + e.getDescription() + "</center></body></html>";
			try {
				servletIOManager.writeBackToClient(500, message, false, "errorrmsg", "text/html");
			} catch (IOException e) {
				logger.error("Impossible to write back to the client the message: [" + message + "]", e);
			}
			servletIOManager.auditServiceErrorEvent( e.getDescription() );
		} finally {
			logger.debug("OUT");
		}
	}
	
	public static final String OUTPUT_TYPE = "outputType";
	public static final String TEMPLATE_FILE_NAME = "template_file_name";
	
	public static final String DEFAULT_TEMPLATE_FILE_NAME = "report";
	
	protected void runReport(EngineStartServletIOManager servletIOManager) throws Exception {
		
		String outputType;
		String template;
		String templateFileName;
		IReportRunnable design;
		IRunAndRenderTask task;
		
		logger.debug("IN");
		
		design = null;
		
		try {
						
			birtReportEngine = BirtEngine.getBirtEngine(servletIOManager.getRequest(), getServletContext());
			
			outputType = servletIOManager.getParameterAsString(OUTPUT_TYPE);		
			logger.debug("Parameter [" + OUTPUT_TYPE + "] is equal to [" + outputType + "]");
	
			templateFileName = servletIOManager.getParameterAsString(TEMPLATE_FILE_NAME);
			logger.debug("Parameter [" + TEMPLATE_FILE_NAME + "] is equal to [" + templateFileName + "]");
			if ( StringUtilities.isEmpty(templateFileName)) {
				templateFileName = DEFAULT_TEMPLATE_FILE_NAME;
			}
			
			logger.debug("Report locale [" + servletIOManager.getLocale().getCountry() + "/" + servletIOManager.getLocale().getLanguage() + "]");
			
			template = servletIOManager.getTemplateAsString();
			
			logger.debug("Parsing report template ...");
			try {
				design = birtReportEngine.openReportDesign( template );
			} catch(Throwable e) {
				throw new RuntimeException("Impossible to parse report template: \n" + template, e);
			}
			logger.debug("... report template parsed succesfully");
			
			
			
			
			task = birtReportEngine.createRunAndRenderTask(design);
			task.setLocale( servletIOManager.getLocale() );
			
			reportParams = findReportParams(servletIOManager.getRequest(), design);
			if( servletIOManager.getDataSource().checkIsJndi() ) {
				reportParams.put("connectionName", servletIOManager.getDataSource().getJndi());
			} else {
				reportParams.put("driver", servletIOManager.getDataSource().getDriver());
				reportParams.put("url", servletIOManager.getDataSource().getUrlConnection());
				reportParams.put("user", servletIOManager.getDataSource().getUser());
				reportParams.put("pwd", (servletIOManager.getDataSource().getPwd().equals("")) ? " " : servletIOManager.getDataSource().getPwd());
			}
			
			SsoServiceInterface proxyService = SsoServiceFactory.createProxyService();
			String token = proxyService.readTicket(servletIOManager.getHttpSession());			
			String kpiUrl = EnginConf.getInstance().getSpagoBiServerUrl()+"/publicjsp/kpiValueXml.jsp?SECURITY_TOKEN="+token+"&USERID="+ servletIOManager.getUserId();
			reportParams.put("KpiDSXmlUrl", kpiUrl);
			
			
			
			//gets static resources with SBI_RESOURCE_PATH system's parameter
			String resourcePath=EnginConf.getInstance().getResourcePath()+"/img/";
			String entity=(String)reportParams.get(SpagoBIConstants.SBI_ENTITY);
			// IF exist an ENTITY  parameter concat to resourcePath
			if (entity!=null && entity.length()>0){
				resourcePath=resourcePath.concat(entity+"/");
			}
			logger.debug("SetUp resourcePath:"+resourcePath);
			reportParams.put("SBI_RESOURCE_PATH", resourcePath);
			
			
			
			task.setParameterValues(reportParams);
			task.validateParameters();
	
						
			
			IRenderOption renderOption = null; 
			String contentType;
			String contentDisposition;
	
			if ( IBirtConstants.PDF_RENDER_FORMAT.equalsIgnoreCase( outputType ) ) {
				renderOption = new PDFRenderOption();
				renderOption.setOutputFormat(IBirtConstants.PDF_RENDER_FORMAT);
				contentType = "application/pdf";
				contentDisposition = "inline; filename=" + templateFileName + ".pdf";		
			} else if ( IBirtConstants.HTML_RENDER_FORMAT.equalsIgnoreCase( outputType ) ) {
				renderOption = prepareHtmlRenderOption(getServletContext(), servletIOManager.getRequest());
				renderOption.setOutputFormat(IBirtConstants.HTML_RENDER_FORMAT);
				contentType = "text/html";
				contentDisposition = "inline; filename=" + templateFileName + ".html";
			} else if ( IBirtConstants.DOC_RENDER_FORMAT.equalsIgnoreCase( outputType ) ) {
				renderOption = prepareHtmlRenderOption(getServletContext(), servletIOManager.getRequest());
				renderOption.setOutputFormat(IBirtConstants.DOC_RENDER_FORMAT);
				contentType = "application/msword";
				contentDisposition = "inline; filename=" + templateFileName + ".doc";
			} else if ( RTF_FORMAT.equalsIgnoreCase( outputType ) ) {
				renderOption = prepareHtmlRenderOption(getServletContext(), servletIOManager.getRequest());
				renderOption.setOutputFormat(RTF_FORMAT);
				contentType = "application/rtf";
				contentDisposition = "inline; filename=" + templateFileName + ".rtf";
			} else if ( "xls".equalsIgnoreCase( outputType ) ) {
				renderOption = prepareHtmlRenderOption(getServletContext(), servletIOManager.getRequest());
				renderOption.setOutputFormat("xls");
				contentType = "application/vnd.ms-excel";
				contentDisposition = "inline; filename=" + templateFileName + ".xls";;
			} else if ( "ppt".equalsIgnoreCase( outputType ) ) {
				renderOption = prepareHtmlRenderOption(getServletContext(), servletIOManager.getRequest());
				renderOption.setOutputFormat("ppt");
				contentType = "application/vnd.ms-powerpoint";
				contentDisposition = "inline; filename=" + templateFileName + ".ppt";
			} else if ( IBirtConstants.POSTSCRIPT_RENDER_FORMAT.equalsIgnoreCase( outputType ) ) {
				renderOption = new PDFRenderOption();
				renderOption.setOutputFormat(IBirtConstants.POSTSCRIPT_RENDER_FORMAT);
				contentType = "application/postscript";
				contentDisposition = "inline; filename=" + templateFileName + ".ps";
			} else {
				logger.debug(" Output format parameter not set or not valid. Using default output format: HTML.");
				outputType = IBirtConstants.HTML_RENDER_FORMAT;
				renderOption = prepareHtmlRenderOption(getServletContext(), servletIOManager.getRequest());
				renderOption.setOutputFormat(IBirtConstants.HTML_RENDER_FORMAT);
				contentType = "text/html";
				contentDisposition = "inline; filename=" + templateFileName + ".html";
			}
			task.setRenderOption(renderOption);
			
			
			Map context = BirtUtility.getAppContext(servletIOManager.getRequest());
			task.setAppContext(context);
			
			servletIOManager.getResponse().setHeader("Content-Disposition", contentDisposition);
			servletIOManager.getResponse().setHeader("Content-Type", contentType);
			servletIOManager.getResponse().setContentType(contentType);
			
			renderOption.setOutputStream( (OutputStream)servletIOManager.getResponse().getOutputStream() );
			
			
			
			try {
				task.run();
			} catch(Exception e) {
				logger.error("Error while running the report", e);
			}
			task.close();
			
			
		} catch (Throwable t) {
			logger.error("Unpredicted error occurred while executing report", t);
			throw new RuntimeException("Unpredicted error occurred while executing report", t);
		} finally {
			logger.debug("OUT");
		}

	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	private String decodeParameter(Object paramaterValue) {
		if (paramaterValue == null)
			return null;
		else {
			String paramaterValueStr = paramaterValue.toString();
			String toReturn = "";
			ParametersDecoder decoder = new ParametersDecoder();
			if (decoder.isMultiValues(paramaterValueStr)) {
				List values = decoder.decode(paramaterValueStr);
				// toReturn = (String) values.get(0);
				for (int i = 0; i < values.size(); i++) {
					toReturn += (i > 0 ? "," : "");
					toReturn += values.get(i);
				}
			} else {
				toReturn = paramaterValueStr;
			}
			return toReturn;
		}
	}

	protected HTMLRenderOption prepareHtmlRenderOption(ServletContext servletContext, HttpServletRequest servletRequest)
	throws Exception {
		logger.debug("IN");
		String imageDirectory = servletContext.getRealPath("/report/images");
		String contextPath = servletRequest.getContextPath();
		//String imageBaseUrl = "/BirtImageServlet?params="+reportParams+"&imagePath=/report/images&imageID=";
		String imageBaseUrl = "/BirtImageServlet?imagePath=/report/images&imageID=";

		// Register new image handler
		HTMLRenderOption renderOption = new HTMLRenderOption();
		renderOption.setActionHandler(new HTMLActionHandler());
		HTMLServerImageHandler imageHandler = new HTMLServerImageHandler();
		renderOption.setImageHandler(imageHandler);
		renderOption.setImageDirectory(imageDirectory);
		renderOption.setBaseImageURL(contextPath + imageBaseUrl);
		renderOption.setEmbeddable(false);
		this.birtReportEngine.getConfig().getEmitterConfigs().put("html", renderOption);
		logger.debug("OUT");
		return renderOption;

	}
	

	private InputStream getTemplateContent(EngineStartServletIOManager servletIOManager, ServletContext servletContext) throws IOException {
		logger.debug("IN");
		ContentServiceProxy contentProxy = new ContentServiceProxy(userId, servletRequest.getSession());

		HashMap requestParameters = ParametersDecoder.getDecodedRequestParameters(servletRequest);
		Content template = contentProxy.readTemplate(documentId,requestParameters);
		logger.debug("Read the template=" + template.getFileName());
		
		InputStream is = null;
		byte[] templateContent = null;
		try {
			BASE64Decoder bASE64Decoder = new BASE64Decoder();
			templateContent = bASE64Decoder.decodeBuffer(template.getContent());
			is = new java.io.ByteArrayInputStream(templateContent);
		}catch (Throwable t){
			logger.warn("Error on decompile",t); 
		}
		

		if (template.getFileName().indexOf(".zip") > -1) {
			flgTemplateStandard = "false";
		}else{
			flgTemplateStandard = "true";
		}
		
		SpagoBIAccessUtils util = new SpagoBIAccessUtils();
		UUIDGenerator uuidGen  = UUIDGenerator.getInstance();
		UUID uuid_local = uuidGen.generateTimeBasedUUID();
		String executionId = uuid_local.toString();
		executionId = executionId.replaceAll("-", "");
		boolean propertiesLoaded=false;
		if (flgTemplateStandard.equalsIgnoreCase("false")) {
			logger.debug("The template is a .ZIP file");
			File fileZip = new File(getJRTempDir(servletContext,executionId), JS_FILE_ZIP + JS_EXT_ZIP);
			FileOutputStream foZip = new FileOutputStream(fileZip);
			foZip.write(templateContent);
			foZip.close();
			util.unzip(fileZip, getJRTempDir(servletContext, executionId));
			JarFile zipFile = new JarFile(fileZip);
			Enumeration totalZipEntries = zipFile.entries();
			File jarFile = null;
			while (totalZipEntries.hasMoreElements()) {
				ZipEntry entry = (ZipEntry) totalZipEntries.nextElement();
				if (entry.getName().endsWith(".jar")) {
					jarFile = new File(getJRTempDirName(servletContext,executionId)+ entry.getName());
					// set classloader with jar
					ClassLoader previous = Thread.currentThread().getContextClassLoader();
					DynamicClassLoader dcl = new DynamicClassLoader(jarFile, previous);
					Thread.currentThread().setContextClassLoader(dcl);
				}
				else if (entry.getName().endsWith(".rptdesign")) {
					// set InputStream with report
					File birtFile = new File(getJRTempDirName(servletContext, executionId)+ entry.getName());
					InputStream isBirt = new FileInputStream(birtFile);
					byte[] templateRptDesign = new byte[0];
					templateRptDesign = util.getByteArrayFromInputStream(isBirt);
					is = new java.io.ByteArrayInputStream(templateRptDesign);
				}
				if (entry.getName().endsWith(".properties")) {
					propertiesLoaded=true;
				}					
			}
			String resourcePath = getJRTempDirName(servletContext,executionId);
			if(resourcePath!=null){
				this.birtReportEngine.getConfig().setResourcePath(resourcePath);
			}
			
		}else{
			
			SourceBean engineConfig=null;
			if (getClass().getResource("/engine-config.xml")!=null){
				InputSource source=new InputSource(getClass().getResourceAsStream("/engine-config.xml"));
				try {
					engineConfig = SourceBean.fromXMLStream(source);
				} catch (SourceBeanException e) {
					logger.error("SourceBean exception for engine config",e);
					e.printStackTrace();
				}   
			}
			
			SourceBean sb = (SourceBean)engineConfig.getAttribute("RESOURCE_PATH_JNDI_NAME");
			String path = (String) sb.getCharacters();
			String resPath= SpagoBIUtilities.readJndiResource(path);			
			resPath+="/birt_messages/";

			if(resPath!=null){
				this.birtReportEngine.getConfig().setResourcePath(resPath);
			}
		}
		
		
		logger.debug("OUT");
		return is;
	}

	protected Map findReportParams(HttpServletRequest request, IReportRunnable design)
	throws ConnectionDefinitionException {
		logger.debug("IN");
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

			if (paramValueString == null || paramValueString.trim().equals("")) {
				logger.debug(this.getClass().getName() + "findReportParams() The report parameter " + paramName
						+ " has no values set.");
				continue;
				//logger.debug(this.getClass().getName() + "findReportParams() The report parameter " + paramName
				//		+ " has no values set. Gets default value.");
				//paramValueString = param.getDefaultValue();
			}

			int paramType = param.getDataType();
			/*
			 * The ParameterConverter converts a single value. Multi-value
			 * parameters are assumed to contains values that are String type.
			 * If they are not Strings (list of dates, list of numbers, ...) the
			 * converter will not work.
			 */
			Object paramValue = ParameterConverter.convertParameter(paramType, paramValueString, dateformat);
			if (paramValue == null)
				paramValue = paramValueString;

			toReturn.put(paramName, paramValue);
			logger.debug("PUT "+paramName+"/"+paramValueString);

		}
		logger.debug("OUT");
		return toReturn;
	}
	
	
	private String getJRTempDirName(ServletContext servletContext, String executionId) {	
		logger.debug("IN");
		String jrTempDir = servletContext.getRealPath("tmpdir") + System.getProperty("file.separator") +
		"reports" +  System.getProperty("file.separator") +
		"JS_dir_" + executionId + System.getProperty("file.separator");
		logger.debug("OUT");
		return jrTempDir;		
	}
	
	private File getJRTempDir(ServletContext servletContext, String executionId) {
		logger.debug("IN");
		File jrTempDir = null;		

		String jrTempDirStr = getJRTempDirName(servletContext, executionId);
		jrTempDir = new File(jrTempDirStr.substring(0, jrTempDirStr.length()-1));
		jrTempDir.mkdirs();
		logger.debug("OUT");
		return jrTempDir;		
	}
	
	
	
	/**
	 * This method injects the HTML header into the report HTML output.
	 * This is necessary in order to inject the document.domain javascript directive
	 */
	/* commented by Davide Zerbetto on 12/10/2009: there are problems with MIF (Ext ManagedIFrame library) library
	protected void injectHTMLHeader(HttpServletResponse response) throws IOException {
		logger.debug("IN");
		String header = null;
		try {
			SourceBean config = EnginConf.getInstance().getConfig();
			SourceBean htmlHeaderSb = (SourceBean) config.getAttribute("HTML_HEADER");
			header = htmlHeaderSb.getCharacters();
			if (header == null || header.trim().equals("")) {
				throw new Exception("HTML_HEADER not configured");
			}
			header = header.replaceAll("\\$\\{SBI_DOMAIN\\}", EnginConf.getInstance().getSpagoBiDomain());
		} catch (Exception e) {
			logger.error("Error while retrieving HTML_HEADER from engine configuration.", e);
			logger.info("Using default HTML header", e);
			StringBuffer buffer = new StringBuffer();
			buffer.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">");
			buffer.append("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"></meta>");
			buffer.append("  <script type=\"text/javascript\">");
			buffer.append("    document.domain='" + EnginConf.getInstance().getSpagoBiDomain() + "';");
			buffer.append("  </script>");
			buffer.append("</head><body>");
			header = buffer.toString();
		}
		response.getOutputStream().write(header.getBytes());
		logger.debug("OUT");
	}
	*/
	
	/**
	 * This method injects the HTML footer into the report HTML output.
	 * See injectHTMLHeader method
	 */
	/* commented by Davide Zerbetto on 12/10/2009: there are problems with MIF (Ext ManagedIFrame library) library
	protected void injectHTMLFooter(HttpServletResponse response) throws IOException {
		logger.debug("IN");
		String footer = null;
		try {
			SourceBean config = EnginConf.getInstance().getConfig();
			SourceBean htmlHeaderSb = (SourceBean) config.getAttribute("HTML_FOOTER");
			footer = htmlHeaderSb.getCharacters();
			if (footer == null || footer.trim().equals("")) {
				throw new Exception("HTML_FOOTER not configured");
			}
		} catch (Exception e) {
			logger.error("Error while retrieving HTML_FOOTER from engine configuration.", e);
			logger.info("Using default HTML footer", e);
			StringBuffer buffer = new StringBuffer();
			buffer.append("</body></html>");
			footer = buffer.toString();
		}
		response.getOutputStream().write(footer.getBytes());
		logger.debug("OUT");
	}
	*/

}
