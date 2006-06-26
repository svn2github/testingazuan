/**
 * 
 * LICENSE: see BIRT.LICENSE.txt file
 * 
 */
package it.eng.spagobi.engines.birt;

import it.eng.spagobi.utilities.SpagoBIAccessUtils;

import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.eclipse.birt.core.framework.IPlatformContext;
import org.eclipse.birt.core.framework.PlatformServletContext;
import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.FORenderOption;
import org.eclipse.birt.report.engine.api.HTMLActionHandler;
import org.eclipse.birt.report.engine.api.HTMLEmitterConfig;
import org.eclipse.birt.report.engine.api.HTMLRenderContext;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.HTMLServerImageHandler;
import org.eclipse.birt.report.engine.api.IGetParameterDefinitionTask;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.IScalarParameterDefn;
import org.eclipse.birt.report.engine.api.ReportEngine;

public class BirtReportRunnerODA {

	protected String templatePath = null;
	protected String spagobibaseurl = null;
	protected String dateformat = null;
	protected EngineConfig config = new EngineConfig();
	protected transient Logger logger = Logger.getLogger(BirtReportRunnerODA.class);
	
	/**
	 * Class Constructor
	 * 
	 * @param spagobibaseurl The basic url for SpagoBI
	 * @param templatePath The path for the report template
	 * @param dateformat The String representing the date format
	 */
	public BirtReportRunnerODA(String spagobibaseurl, String templatePath, String dateformat) {
		super();
		this.templatePath = templatePath;
		this.spagobibaseurl = spagobibaseurl;
		dateformat = dateformat.replaceAll("D", "d");
		dateformat = dateformat.replaceAll("m", "M");
		dateformat = dateformat.replaceAll("Y", "y");
		this.dateformat = dateformat;
		config = new EngineConfig();
	}
	
	public void runReport(Map parameters,
			ServletContext servletContext, HttpServletResponse servletResponse,
			HttpServletRequest servletRequest) throws Exception {

		config.setEngineHome("");
		String pluginUrl = "http://" + servletRequest.getServerName() 
				+ ":" + servletRequest.getServerPort() + servletRequest.getContextPath() + "/plugins/";
		logger.debug("Engines"+ this.getClass().getName() + "runReport(): using plugin url " + pluginUrl);
		IPlatformContext context = new PlatformServletContext(servletContext, pluginUrl);
    			
		config.setEngineContext(context);
		ReportEngine engine = new ReportEngine(config);
		logger.debug("Engines"+ this.getClass().getName() + "runReport(): trying to get content of the template with path = ["
				+ templatePath + "] calling the SpagoBI base url = [" + spagobibaseurl + "]");
		byte[] jcrContent = new SpagoBIAccessUtils().getContent(
				this.spagobibaseurl, this.templatePath);
		logger.debug("Engines"+ this.getClass().getName() + "runReport(): template document retrieved.");
		InputStream is = new java.io.ByteArrayInputStream(jcrContent);
		IReportRunnable design = engine.openReportDesign(is);
		logger.debug("Engines"+ this.getClass().getName() + "runReport(): report template opened successfully.");
		IRunAndRenderTask task = engine.createRunAndRenderTask(design);
		logger.debug("Engines"+ this.getClass().getName() + "runReport(): RunAndRenderTask created successfully.");
		Map reportParams = findReportParams(parameters, engine, design);
		task.setParameterValues(reportParams);
		String outputFormat = (String) parameters.get("param_output_format");
		logger.debug("Engines"+ this.getClass().getName()+ "runReport() param_output_format -- [" + outputFormat +"]");
		IRenderOption options;
		if (outputFormat != null && outputFormat.equalsIgnoreCase("pdf")) {
			options = new FORenderOption();
			servletResponse.setContentType("application/pdf");
			options.setOutputStream((OutputStream) servletResponse.getOutputStream());
		} else if (outputFormat != null && outputFormat.equalsIgnoreCase("html")) {
			options = new HTMLRenderOption();
			HashMap appContext = prepareHtmlRendering(servletContext, servletRequest);
			task.setAppContext(appContext);
			((HTMLRenderOption) options).setEmbeddable(true);
			options.setOutputStream((OutputStream) servletResponse.getOutputStream());
		} else {
			logger.debug("Engines"+ this.getClass().getName()+ "runReport() Output format parameter not set or not valid. Using default output format: HTML.");
			options = new HTMLRenderOption();
			HashMap appContext = prepareHtmlRendering(servletContext, servletRequest);
			task.setAppContext(appContext);
			((HTMLRenderOption) options).setEmbeddable(true);
			options.setOutputStream((OutputStream) servletResponse.getOutputStream());
		}
		options.setOutputFormat(outputFormat);
		task.setRenderOption(options);
		task.run();
	}
	
	
	protected Map findReportParams(Map parameters, ReportEngine engine, IReportRunnable design) {
		HashMap toReturn = new HashMap();
		IGetParameterDefinitionTask task = engine.createGetParameterDefinitionTask(design);
		Collection paramsColl = task.getParameterDefns(false);
		Iterator it = paramsColl.iterator();
		while (it.hasNext()) {
			IScalarParameterDefn param = (IScalarParameterDefn) it.next();
			String paramName = param.getName();
			Object paramValueObj = parameters.get(paramName);
			String paramValueString = null;
			if (paramValueObj != null) paramValueString = paramValueObj.toString();
			if (paramValueString == null || paramValueString.trim().equals("")) {
				logger.debug("Engines"+ this.getClass().getName()+ "findReportParams() The report parameter " + paramName + " has no values set.");
				continue;
			}
			int paramType = param.getDataType();
			Object paramValue = null;
			if (IScalarParameterDefn.TYPE_DATE_TIME == paramType) {
				SimpleDateFormat dateFormat = new SimpleDateFormat(dateformat);
				try {
					paramValue = dateFormat.parse(paramValueString);
				} catch (ParseException e) {
					logger.debug("Engines"+ this.getClass().getName()+ "findReportParams() Error parsing the string [" + paramValueString + "] as a date using the format [" + dateformat + "].");
					e.printStackTrace();
				}
			} else if (IScalarParameterDefn.TYPE_BOOLEAN == paramType) {
				try {
					paramValue = new Boolean (paramValueString);
				} catch (Exception e) {
					logger.debug("Engines"+ this.getClass().getName()+ "findReportParams() Error parsing the string [" + paramValueString + "] as a Boolean.");
					e.printStackTrace();
				}
			} else if (IScalarParameterDefn.TYPE_DECIMAL == paramType) {
				try {
					// Spago uses Double (and Float) number format
					paramValue = new Double (paramValueString);
				} catch (Exception e) {
					logger.debug("Engines"+ this.getClass().getName()+ "findReportParams() Error parsing the string [" + paramValueString + "] as a number.");
					e.printStackTrace();
				}
			} else if (IScalarParameterDefn.TYPE_FLOAT == paramType) {
				try {
					// Spago uses Double (and Float) number format
					paramValue = new Float (paramValueString);
				} catch (Exception e) {
					logger.debug("Engines"+ this.getClass().getName()+ "findReportParams() Error parsing the string [" + paramValueString + "] as a float.");
					e.printStackTrace();
				}
			} else if (IScalarParameterDefn.TYPE_STRING == paramType) {
				paramValue = paramValueString;
			}
			
			if (paramValue == null) paramValue = paramValueString;
			
			toReturn.put(paramName, paramValue);
			
		}

		return toReturn;
	}
	
	protected HashMap prepareHtmlRendering (ServletContext servletContext, HttpServletRequest servletRequest) {
		
		// Register new image handler
		HTMLEmitterConfig emitterConfig = new HTMLEmitterConfig();
		emitterConfig.setActionHandler(new HTMLActionHandler());
		HTMLServerImageHandler imageHandler = new HTMLServerImageHandler();
		emitterConfig.setImageHandler(imageHandler);
		config.getEmitterConfigs().put("html",emitterConfig);
		
		String imageDirectory = servletContext.getRealPath("/report/images");
		String contextPath = servletRequest.getContextPath();
		logger.debug("Engines"+ this.getClass().getName() + "runReport(): context path " + contextPath);
		String imageBaseUrl = "/image.jsp?imagePath=" + "/report/images" + "&" + "imageID=";
		
		HTMLRenderContext renderContext = new HTMLRenderContext();
		renderContext.setImageDirectory(imageDirectory);
		renderContext.setBaseImageURL(contextPath + imageBaseUrl);
		renderContext.setBaseURL(contextPath + "/frameset");
		//renderContext.setSupportedImageFormats( svgFlag ? "PNG;GIF;JPG;BMP;SVG" : "PNG;GIF;JPG;BMP" ); //$NON-NLS-1$ //$NON-NLS-2$
		
		HashMap appContext = new HashMap();
		appContext.put(DataEngine.DATASET_CACHE_OPTION, Boolean.TRUE);
		appContext.put(EngineConstants.APPCONTEXT_BIRT_VIEWER_HTTPSERVET_REQUEST, servletRequest);
		appContext.put(EngineConstants.APPCONTEXT_CLASSLOADER_KEY, BirtReportRunnerODA.class.getClassLoader());
		appContext.put(EngineConstants.APPCONTEXT_HTML_RENDER_CONTEXT,
				renderContext);
		
		return appContext;
	}
	
}
