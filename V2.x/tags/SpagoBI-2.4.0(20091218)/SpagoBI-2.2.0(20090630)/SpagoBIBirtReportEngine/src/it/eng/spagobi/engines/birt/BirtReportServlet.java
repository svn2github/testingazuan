/**
 * 
 * LICENSE: see BIRT.LICENSE.txt file
 * 
 */
package it.eng.spagobi.engines.birt;

import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.commons.bo.UserProfile;
import it.eng.spagobi.engines.birt.exceptions.ConnectionDefinitionException;
import it.eng.spagobi.engines.birt.exceptions.ConnectionParameterNotValidException;
import it.eng.spagobi.engines.birt.utilities.ParameterConverter;
import it.eng.spagobi.services.content.bo.Content;
import it.eng.spagobi.services.datasource.bo.SpagoBiDataSource;
import it.eng.spagobi.services.proxy.ContentServiceProxy;
import it.eng.spagobi.services.proxy.DataSourceServiceProxy;
import it.eng.spagobi.tools.datasource.bo.IDataSource;
import it.eng.spagobi.utilities.ParametersDecoder;
import it.eng.spagobi.utilities.callbacks.audit.AuditAccessUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.eclipse.birt.report.IBirtConstants;
import org.eclipse.birt.report.engine.api.EngineConfig;
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
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.utility.BirtUtility;
import org.eclipse.birt.report.utility.ParameterAccessor;

import sun.misc.BASE64Decoder;

/**
 * @author Zerbetto (davide.zerbetto@eng.it)
 * 
 * DATE            CONTRIBUTOR/DEVELOPER                        NOTE
 * 02-10-2008      Zerbetto Davide/Julien Decreuse (Smile)		Upgrade to Birt 2.3.0 API
 **/
public class BirtReportServlet extends HttpServlet {

	private IReportEngine birtReportEngine = null;
	protected static Logger logger = Logger.getLogger(BirtReportServlet.class);
	private static String CONNECTION_NAME = "connectionName";

	/* (non-Javadoc)
	 * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
	 */
	public void init(ServletConfig servletConfig) throws ServletException {
		super.init(servletConfig);
		logger.debug("Initializing SpagoBI BirtReport Engine...");
		BirtEngine.initBirtConfig();
		logger.debug(":init:Inizialization of SpagoBI BirtReport Engine ended succesfully");
	}

	/* (non-Javadoc)
	 * @see javax.servlet.GenericServlet#destroy()
	 */
	public void destroy() {
		super.destroy();
		BirtEngine.destroyBirtEngine();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#service(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		logger.debug("Start processing a new request...");

		// USER PROFILE
		HttpSession session = request.getSession();
		IEngUserProfile profile = (IEngUserProfile) session.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
		String documentId = (String) request.getParameter("document");
		String userId = (String) ((UserProfile)profile).getUserId();

		logger.debug("userId=" + userId);
		logger.debug("documentId=" + documentId);

		// AUDIT UPDATE
		String auditId = request.getParameter("SPAGOBI_AUDIT_ID");
		logger.debug("auditId=" + auditId);
		AuditAccessUtils auditAccessUtils = (AuditAccessUtils) request.getSession().getAttribute("SPAGOBI_AUDIT_UTILS");
		if (auditId != null) {
			if (auditAccessUtils != null)
				auditAccessUtils.updateAudit(session, userId, auditId, new Long(System.currentTimeMillis()), null,
						"EXECUTION_STARTED", null, null);
		}
		try {
			runReport(request, response);
			// AUDIT UPDATE
			if (auditId != null) {
				if (auditAccessUtils != null)
					auditAccessUtils.updateAudit(session, userId, auditId, null, new Long(System.currentTimeMillis()),
							"EXECUTION_PERFORMED", null, null);
			}
		} catch (ConnectionDefinitionException e) {
			logger.error("Error during report production \n\n " + e);
			PrintWriter writer = response.getWriter();
			String resp = "<html><body><center>" + e.getDescription() + "</center></body></html>";
			writer.write(resp);
			writer.flush();
			writer.close();
			// AUDIT UPDATE
			if (auditId != null) {
				if (auditAccessUtils != null)
					auditAccessUtils.updateAudit(session, userId, auditId, null, new Long(System.currentTimeMillis()),
							"EXECUTION_FAILED", e.getDescription(), null);
			}
		} catch (Exception e) {
			logger.error( "Error during report production \n\n ", e);
			// AUDIT UPDATE
			if (auditId != null) {
				if (auditAccessUtils != null)
					auditAccessUtils.updateAudit(session, userId, auditId, null, new Long(System.currentTimeMillis()),
							"EXECUTION_FAILED", e.getMessage(), null);
			}
		}

		logger.info(" Request processed");

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
		logger.debug("OUT");
		return renderOption;

	}

	private InputStream getTemplateContent(HttpServletRequest servletRequest) throws IOException {
		logger.debug("IN");
		HttpSession session = servletRequest.getSession();
		IEngUserProfile profile = (IEngUserProfile) session.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
		String documentId = (String) servletRequest.getParameter("document");
		String userId = (String) profile.getUserUniqueIdentifier();
		//String userId = (String)((UserProfile) profile).getUserId();
		logger.debug("userId=" + userId);
		logger.debug("documentId=" + documentId);

		ContentServiceProxy contentProxy = new ContentServiceProxy(userId, servletRequest.getSession());

		HashMap requestParameters = ParametersDecoder.getDecodedRequestParameters(servletRequest);
		Content template = contentProxy.readTemplate(documentId,requestParameters);
		logger.debug("Read the template=" + template.getFileName());
		InputStream is = null;
		try {
			BASE64Decoder bASE64Decoder = new BASE64Decoder();
			byte[] templateContent = bASE64Decoder.decodeBuffer(template.getContent());
			is = new java.io.ByteArrayInputStream(templateContent);
		}catch (Throwable t){
			logger.warn("Error on decompile",t); 
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

	/**
	 * 
	 * @param documentId
	 * @return jndi connection
	 * @throws ConnectionDefinitionException
	 */
	private SpagoBiDataSource findDataSource(HttpSession session, String userId, String documentId,String requestConnectionName)
	throws ConnectionDefinitionException {
		logger.debug("IN");
		if (documentId == null) {
			logger.error("Document identifier NOT found. Returning null.");
			throw new ConnectionParameterNotValidException("No default connection defined in "
					+ "engine-config.xml file.");
		}
		DataSourceServiceProxy proxyDS = new DataSourceServiceProxy(userId, session);
		IDataSource ds = null;
		if (requestConnectionName!=null){
			ds =proxyDS.getDataSourceByLabel(requestConnectionName);
		}else{
			ds =proxyDS.getDataSource(documentId);
		}
		if (ds==null) {
			logger.warn("Data Source IS NULL. There are problems reading DataSource informations");
			return null;
		}	
		logger.debug("OUT");
		return ds.toSpagoBiDataSource();
	}

	protected void runReport(HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.debug("IN");
		HttpSession session = request.getSession();
		IEngUserProfile profile = (IEngUserProfile) session.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
		String userId = (String) profile.getUserUniqueIdentifier();
		logger.debug("userId="+userId);
		String documentId = (String) request.getParameter("document");
		logger.debug("documentId=" + documentId);

		ServletContext servletContext = getServletContext();
		this.birtReportEngine = BirtEngine.getBirtEngine(request, servletContext);
		IReportRunnable design = null;
		InputStream is = getTemplateContent(request);
		logger.debug( "runReport(): template document retrieved.");
		// Open the report design
		design = birtReportEngine.openReportDesign(is);

		Locale locale = null;
		String language=request.getParameter("SBI_LANGUAGE");
		String country=request.getParameter("SBI_COUNTRY");
		if(language!=null && country!=null){
			locale=new Locale(language,country,"");
		}
		else{
			locale=Locale.ENGLISH;
		}
		
		logger.debug( "runReport(): report design opened successfully.");
		// Create task to run and render the report,
		IRunAndRenderTask task = birtReportEngine.createRunAndRenderTask(design);
		task.setLocale(locale);
		logger.debug( "runReport(): RunAndRenderTask created successfully.");
		// Set parameters for the report
		Map reportParams = findReportParams(request, design);

		String requestConnectionName = (String) request.getParameter(CONNECTION_NAME);
		logger.debug("requestConnectionName:" + requestConnectionName);
		SpagoBiDataSource sbds = findDataSource(request.getSession(), userId, documentId,requestConnectionName);
		if (sbds != null) {
			logger.debug("DataSource founded.");
			if (sbds.getJndiName() != null && sbds.getJndiName() != "") {
				reportParams.put("connectionName", sbds.getJndiName());
			} else {
				reportParams.put("driver", sbds.getDriver());
				reportParams.put("url", sbds.getUrl());
				reportParams.put("user", sbds.getUser());
				reportParams.put("pwd", (sbds.getPassword().equals("")) ? " " : sbds.getPassword());

			}
		}

		task.setParameterValues(reportParams);
		task.validateParameters();

		String outputFormat = request.getParameter("outputType");
		logger.debug("outputType -- [" + outputFormat + "]");



		String templateFileName = request.getParameter("template_file_name");
		logger.debug("templateFileName -- [" + templateFileName + "]");
		if (templateFileName == null || templateFileName.trim().equals(""))
			templateFileName = "report";
		IRenderOption renderOption = null;

		if (outputFormat != null && outputFormat.equalsIgnoreCase(IBirtConstants.PDF_RENDER_FORMAT)) {
			renderOption = new PDFRenderOption();
			renderOption.setOutputFormat(IBirtConstants.PDF_RENDER_FORMAT);
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition", "inline; filename=" + templateFileName + ".pdf");
		} else if (outputFormat != null && outputFormat.equalsIgnoreCase(IBirtConstants.HTML_RENDER_FORMAT)) {
			renderOption = prepareHtmlRenderOption(servletContext, request);
			renderOption.setOutputFormat(IBirtConstants.HTML_RENDER_FORMAT);
		} else if (outputFormat != null && outputFormat.equalsIgnoreCase(IBirtConstants.DOC_RENDER_FORMAT)) {
			renderOption = prepareHtmlRenderOption(servletContext, request);
			renderOption.setOutputFormat(IBirtConstants.DOC_RENDER_FORMAT);
			// renderOption.setOutputFileName(templateFileName + ".doc");
			response.setContentType("application/msword");
			response.setHeader("Content-disposition", "inline; filename=" + templateFileName + ".doc");
		} else if (outputFormat != null && outputFormat.equalsIgnoreCase("xls")) {
			renderOption = prepareHtmlRenderOption(servletContext, request);
			renderOption.setOutputFormat("xls");
			// renderOption.setOutputFileName(templateFileName + ".xls");
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition", "inline; filename=" + templateFileName + ".xls");
		} else if (outputFormat != null && outputFormat.equalsIgnoreCase("ppt")) {
			renderOption = prepareHtmlRenderOption(servletContext, request);
			renderOption.setOutputFormat("ppt");
			// renderOption.setOutputFileName(templateFileName + ".ppt");
			response.setContentType("application/vnd.ms-powerpoint");
			response.setHeader("Content-disposition", "inline; filename=" + templateFileName + ".ppt");
		} else if (outputFormat != null && outputFormat.equalsIgnoreCase(IBirtConstants.POSTSCRIPT_RENDER_FORMAT)) {
			renderOption = new PDFRenderOption();
			renderOption.setOutputFormat(IBirtConstants.POSTSCRIPT_RENDER_FORMAT);
			// renderOption.setOutputFileName(templateFileName + ".ps");
			response.setHeader("Content-disposition", "inline; filename=" + templateFileName + ".ps");
		} else {
			logger.debug(" Output format parameter not set or not valid. Using default output format: HTML.");
			renderOption = prepareHtmlRenderOption(servletContext, request);
			renderOption.setOutputFormat(IBirtConstants.HTML_RENDER_FORMAT);
		}

		Map context = BirtUtility.getAppContext(request);
		task.setAppContext(context);
		renderOption.setOutputStream((OutputStream) response.getOutputStream());
		task.setRenderOption(renderOption);
		task.run();
		task.close();
		logger.debug("OUT");

	}

}
