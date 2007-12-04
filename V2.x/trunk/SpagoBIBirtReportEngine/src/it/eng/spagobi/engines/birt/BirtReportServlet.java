/**
 * 
 * LICENSE: see BIRT.LICENSE.txt file
 * 
 */
package it.eng.spagobi.engines.birt;

import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.engines.birt.exceptions.ConnectionDefinitionException;
import it.eng.spagobi.engines.birt.exceptions.ConnectionParameterNotValidException;
import it.eng.spagobi.engines.birt.utilities.ParameterConverter;
import it.eng.spagobi.services.content.bo.Content;
import it.eng.spagobi.services.datasource.bo.SpagoBiDataSource;
import it.eng.spagobi.services.proxy.ContentServiceProxy;
import it.eng.spagobi.services.proxy.DataSourceServiceProxy;
import it.eng.spagobi.services.proxy.SecurityServiceProxy;
import it.eng.spagobi.services.security.exceptions.SecurityException;
import it.eng.spagobi.utilities.ParametersDecoder;
import it.eng.spagobi.utilities.callbacks.audit.AuditAccessUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
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
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

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

import sun.misc.BASE64Decoder;

public class BirtReportServlet extends HttpServlet {
	
	private IReportEngine birtReportEngine = null;
	protected static Logger logger = Logger.getLogger(BirtReportServlet.class);
	

	protected String documentId = null;
	protected String userId = null;
	
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
	
	
	public void service(
	        HttpServletRequest request,
	        HttpServletResponse response)
	        throws IOException, ServletException {
		
		logger.info(this.getClass().getName() + ":service: Start processing a new request...");	

		// USER PROFILE
		documentId=(String)request.getParameter("document");
		userId=(String)request.getParameter("userId");
		IEngUserProfile profile=null;
		try {
		    HttpSession session=request.getSession();
		    profile=(IEngUserProfile)session.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
		    if (profile==null){
				SecurityServiceProxy proxy=new SecurityServiceProxy();
				profile = proxy.getUserProfile(userId);
				session.setAttribute(IEngUserProfile.ENG_USER_PROFILE,profile);
		    }
		} catch (SecurityException e1) {
		    logger.error("SecurityException",e1);
		    throw new ServletException();
		}
		
	 	
			// AUDIT UPDATE
			String auditId = request.getParameter("SPAGOBI_AUDIT_ID");
			AuditAccessUtils auditAccessUtils = 
				(AuditAccessUtils) request.getSession().getAttribute("SPAGOBI_AUDIT_UTILS");
			if (auditId != null) {
				if (auditAccessUtils != null) auditAccessUtils.updateAudit(userId,auditId, new Long(System.currentTimeMillis()), null, 
						"EXECUTION_STARTED", null, null);
			}
	 		try {
	 			runReport(request, response);				
	 			// 	AUDIT UPDATE
	 			if (auditId != null) {
	 				if (auditAccessUtils != null) auditAccessUtils.updateAudit(userId,auditId, null, new Long(System.currentTimeMillis()), 
	 						"EXECUTION_PERFORMED", null, null);
	 			}
	 		} catch (ConnectionDefinitionException e){
	 			logger.error(this.getClass().getName()+ ":service:" +
		      			"Error during report production \n\n " + e);
		 		PrintWriter writer = response.getWriter();
		 		String resp = "<html><body><center>" + e.getDescription() + "</center></body></html>";
		 		writer.write(resp);
		 		writer.flush();
		 		writer.close();
				// AUDIT UPDATE
		 		if (auditId != null) {
		 			if (auditAccessUtils != null) auditAccessUtils.updateAudit(userId,auditId, null, new Long(System.currentTimeMillis()), 
		 					"EXECUTION_FAILED", e.getDescription(), null);
		 		}		 						
	 		} catch (Exception e){
	 			logger.error(this.getClass().getName()+ ":service:" +
		      			"Error during report production \n\n ", e);
				// AUDIT UPDATE
	 			if (auditId != null) {
		 			if (auditAccessUtils != null) auditAccessUtils.updateAudit(userId,auditId, null, new Long(System.currentTimeMillis()), 
		 					"EXECUTION_FAILED", e.getMessage(), null);
		 		}	
	 		}
	 	
	 	
	 	logger.info(" Request processed");
	 	
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


	}
	
	private InputStream getTemplateContent(HttpServletRequest servletRequest) throws IOException {
		ContentServiceProxy contentProxy = new ContentServiceProxy();;
		Content template = contentProxy.readTemplate(userId, documentId);
		logger.debug("Read the template."+template.getFileName());
		InputStream is = null;		
		BASE64Decoder bASE64Decoder = new BASE64Decoder();
		byte[] templateContent = bASE64Decoder.decodeBuffer(template.getContent());
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
	/**
	 * 
	 * @param documentId
	 * @return jndi connection
	 * @throws ConnectionDefinitionException
	 */
	private String findConnectionName(String documentId) throws ConnectionDefinitionException {
		String connectionLabel = null;
		if (documentId == null) {
			logger.error("Document identifier NOT found. Returning null.");
				throw new ConnectionParameterNotValidException("No default connection defined in " +
						"engine-config.xml file.");
		}
		DataSourceServiceProxy proxyDS = new DataSourceServiceProxy();		
		SpagoBiDataSource ds = proxyDS.getDataSource(documentId);
		//  get connection
		String jndi = ds.getJndiName();
		if (jndi != null && !jndi.equals("")) 
			connectionLabel =  ds.getJndiName();
		
		return connectionLabel;
	}
	
	/**
	 * 
	 * @param documentId
	 * @return jndi connection
	 * @throws ConnectionDefinitionException
	 */
	private SpagoBiDataSource findDataSource(String documentId) throws ConnectionDefinitionException {
		if (documentId == null) {
			logger.error("Document identifier NOT found. Returning null.");
				throw new ConnectionParameterNotValidException("No default connection defined in " +
						"engine-config.xml file.");
		}
		DataSourceServiceProxy proxyDS = new DataSourceServiceProxy();		
		return  proxyDS.getDataSource(documentId);		
	}
	
	/**
	 * This method, based on the data sources table, gets a 
	 * database connection and return it  
	 * @return the database connection
	 */
	public Connection getConnection(String documentId) {
		//calls service for gets data source object
		DataSourceServiceProxy proxyDS = new DataSourceServiceProxy();		
		SpagoBiDataSource ds = proxyDS.getDataSource(documentId);
		//  get connection
		String jndi = ds.getJndiName();
		if (jndi != null && !jndi.equals("")) {
			return getConnectionFromJndiDS(ds);
		} else {
			return getDirectConnection(ds);
		}
	}
	
	/**
	 * Get the connection from JNDI
	 * @param connectionConfig SourceBean describing data connection
	 * @return Connection to database
	 */
	private Connection getConnectionFromJndiDS(SpagoBiDataSource connectionConfig) {
		Connection connection = null;
		Context ctx ;		
		String resName = connectionConfig.getJndiName();		
		try {
			ctx = new InitialContext();								
			DataSource ds = (DataSource) ctx.lookup(resName);		
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
	 * @param connectionConfig SpagoBiDataSource describing data connection
	 * @return Connection to database
	 */
	private Connection getDirectConnection(SpagoBiDataSource connectionConfig) {
		Connection connection = null;
		try {
			String driverName = connectionConfig.getDriver();
			Class.forName(driverName);
			String url = connectionConfig.getUrl();
			String username = connectionConfig.getUser();
			String password = connectionConfig.getPassword();
			connection = DriverManager.getConnection(url, username, password);
		} catch (ClassNotFoundException e) {
			logger.error("Driver not found", e);
		} catch (SQLException e) {
			logger.error("Cannot retrive connection", e);
		}
		return connection;
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
		//String connection = findConnectionName(documentId);
		//if (connection != null) reportParams.put("connectionName", connection);
		SpagoBiDataSource sbds = findDataSource(documentId);
		if (sbds != null){
			logger.debug("DataSource founded.");
			if(sbds.getJndiName() != null && sbds.getJndiName() != ""){
				reportParams.put("connectionName", sbds.getJndiName());
			}
			else{
				reportParams.put("driver", sbds.getDriver());
				reportParams.put("url", sbds.getUrl());
				reportParams.put("user", sbds.getUser());
				reportParams.put("pwd", (sbds.getPassword().equals(""))?" ":sbds.getPassword());
				
			}
		}
				
		
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
