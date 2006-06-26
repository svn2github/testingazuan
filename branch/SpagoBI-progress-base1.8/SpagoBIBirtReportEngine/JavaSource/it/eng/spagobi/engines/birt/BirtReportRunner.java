/**
 * 
 * LICENSE: see BIRT.LICENSE.txt file
 * 
 */
package it.eng.spagobi.engines.birt;

import it.eng.spagobi.utilities.SpagoBIAccessUtils;

import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.birt.core.framework.IPlatformContext;
import org.eclipse.birt.core.framework.PlatformServletContext;
import org.eclipse.birt.report.engine.api.FORenderOption;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.ReportEngine;

public class BirtReportRunner extends BirtReportRunnerODA {

	public BirtReportRunner(String spagobibaseurl, String templatePath, String dateformat) {
		super(spagobibaseurl, templatePath, dateformat);
	}

	public void runReport(Connection connection, Map parameters,
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
		
		HashMap appContext = new HashMap();
		appContext.put("org.eclipse.birt.report.data.oda.subjdbc.SubOdaJdbcDriver", connection);
		
		IRenderOption options;
		if (outputFormat != null && outputFormat.equalsIgnoreCase("pdf")) {
			options = new FORenderOption();
			servletResponse.setContentType("application/pdf");
			options.setOutputStream((OutputStream) servletResponse.getOutputStream());
		} else if (outputFormat != null && outputFormat.equalsIgnoreCase("html")) {
			options = new HTMLRenderOption();
			HashMap htmlAppContext = prepareHtmlRendering(servletContext, servletRequest);
			appContext.putAll(htmlAppContext);
			((HTMLRenderOption) options).setEmbeddable(true);
			options.setOutputStream((OutputStream) servletResponse.getOutputStream());
		} else {
			logger.debug("Engines"+ this.getClass().getName()+ "runReport() Output format parameter not set or not valid. Using default output format: HTML.");
			options = new HTMLRenderOption();
			HashMap htmlAppContext = prepareHtmlRendering(servletContext, servletRequest);
			appContext.putAll(htmlAppContext);
			((HTMLRenderOption) options).setEmbeddable(true);
			options.setOutputStream((OutputStream) servletResponse.getOutputStream());
		}
		options.setOutputFormat(outputFormat);
		task.setRenderOption(options);
		task.setAppContext(appContext);
		task.run();
	}
}
