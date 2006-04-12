/*
 * Created on 3-mag-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package it.eng.spagobi.engines.jasperreport;

import it.eng.spago.base.SourceBean;
import it.eng.spagobi.utilities.SpagoBIAccessUtils;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRTextExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXmlExporter;
import net.sf.jasperreports.engine.fill.JRFileVirtualizer;

import org.apache.log4j.Logger;
import org.safehaus.uuid.UUID;
import org.safehaus.uuid.UUIDGenerator;

/**
 * Jasper Report implementation built to provide all methods to
 * run a report inside SpagoBI. It is the jasper report Engine implementation
 * for SpagoBI.
 * 
 * @author Zoppello
 */
public class JasperReportRunner {
	
	private String templatePath = null;
	private String spagobibaseurl = null;
	private static transient Logger logger = Logger.getLogger(JasperReportRunner.class);
	
	/**
	 * Class Constructor
	 * 
	 * @param spagobibaseurl The basic url for SpagoBI
	 * @param templatePath The path for the report template
	 */
	public JasperReportRunner(String spagobibaseurl, String templatePath) {
		super();
		this.templatePath = templatePath;
		this.spagobibaseurl = spagobibaseurl;
	}
	
	/**
	 * This method, known all input information, runs a report with JasperReport 
	 * inside SpagoBI. it is the Jasper Report Engine's core method.
	 * 
	 * @param ds The input Data Source for the Report
	 * @param parameters The input parameters map
	 * @param servletContext The java servlet context object
	 * @param servletResponse The java http servlet response object
	 * @throws Exception If any Exception occurred
	 */
	public void runReport(Connection conn, Map parameters, OutputStream out, ServletContext servletContext, 
			                HttpServletResponse servletResponse, HttpServletRequest servletRequest) throws Exception {
		
		/* TODO Since this is the back-end (logic) part of the JasperEngine the direct use of  HttpServletResponse, 
		 * HttpServletRequest and ServletContext objects shuold be pushed back to JasperReportServlet that is 
		 * the front-end (control) part of the engine */
		
		try {								
			String tmpDirectory = System.getProperty("java.io.tmpdir");
			
			// get the classpath used by JasperReprorts Engine (by default equals to WEB-INF/lib)
			String webinflibPath = servletContext.getRealPath("WEB-INF") + System.getProperty("file.separator") + "lib";
			logger.debug("JasperReports lib-dir is [" + this.getClass().getName()+ "]");
			
			
			// get all jar file names in the classpath
			logger.debug("Reading jar files from lib-dir...");
			StringBuffer jasperReportClassPathStringBuffer  = new StringBuffer();
			File f = new File(webinflibPath);
			String fileToAppend = null;
			if (f.isDirectory()){
				String[] jarFiles = f.list();
				for (int i=0; i < jarFiles.length; i++){
					String namefile = jarFiles[i];
					if(!namefile.endsWith("jar"))
						continue; // the inclusion of txt files causes problems
					fileToAppend = webinflibPath + System.getProperty("file.separator")+ jarFiles[i];
					logger.debug("Appending jar file [" + fileToAppend + "] to JasperReports classpath");
					jasperReportClassPathStringBuffer.append(fileToAppend);
					jasperReportClassPathStringBuffer.append(System.getProperty("path.separator"));  
				}
			}
			String jasperReportClassPath = jasperReportClassPathStringBuffer.toString();
			jasperReportClassPath = jasperReportClassPath.substring(0, jasperReportClassPath.length() - 1);
			
			// set classpath property
			System.setProperty("jasper.reports.compile.class.path", jasperReportClassPath);
			logger.debug("Set [jasper.reports.compile.class.path properties] to value [" + System.getProperty("jasper.reports.compile.class.path")+"]");
			
			// set tmpdir property
			System.setProperty("jasper.reports.compile.temp", tmpDirectory);
			logger.debug("Set [jasper.reports.compile.temp] to value [" + System.getProperty("jasper.reports.compile.temp")+"]");
			
			
			byte[] jcrContent = new SpagoBIAccessUtils().getContent(this.spagobibaseurl, this.templatePath);
			InputStream is = new java.io.ByteArrayInputStream(jcrContent);						
			logger.debug("Compiling template file ...");
			JasperReport report  = JasperCompileManager.compileReport(is);
			logger.debug("Template file compiled  succesfully");
			
			
			// Create the virtualizer
			JRFileVirtualizer virtualizer = null; 
			boolean isVirtualizationActive = false;
			SourceBean config = JasperReportConf.getInstance().getConfig();
			String active = (String)config.getAttribute("VIRTUALIZER.active");
			if(active != null) isVirtualizationActive = active.equalsIgnoreCase("true");
			
			if(isVirtualizationActive) {
				logger.debug("Virtualization of fill process is active");
				String maxSizeStr = (String)config.getAttribute("VIRTUALIZER.maxSize");
				int maxSize = 2; 
				if(maxSizeStr!=null) maxSize = Integer.parseInt(maxSizeStr);
				String dir = (String)config.getAttribute("VIRTUALIZER.dir");
				if(dir == null){
					//dir = servletContext.getRealPath("WEB-INF") + System.getProperty("file.separator") + "jrcache";
					dir = tmpDirectory;
				}
				dir = dir + System.getProperty("file.separator") + "jrcache";
				File file = new File(dir);
				file.mkdirs();
				logger.debug("Max page cached during virtualization process: " + maxSize);
				logger.debug("Dir used as storing area during virtualization: " + dir);
				virtualizer = new JRFileVirtualizer(maxSize, dir);
				parameters.put(JRParameter.REPORT_VIRTUALIZER, virtualizer);
				virtualizer.setReadOnly(true);
			}
			
			logger.debug("Filling report ...");
			JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, conn);
			logger.debug("Report filled succesfully");
						
			logger.debug("Exporting report ...");
			String outputType = (String) parameters.get("param_output_format");
			if(outputType == null) {
				logger.debug("Output type is not specified. Default type will be used");
				outputType = ExporterFactory.getDefaultType();
			}
			logger.debug("Output format is [" + outputType + "]");
			
			JRExporter exporter = ExporterFactory.getExporter(outputType);	
			String mimeType = ExporterFactory.getMIMEType(outputType);
			
			if(exporter != null) logger.debug("Configured exporter class [" + exporter.getClass().getName() + "]");
			else logger.debug("Exporter class [null]");
			logger.debug("Configured MIME type [" + mimeType + "]");
			
			// for base types use default exporter, mimeType and parameters if these 
			// are not specified by configuration file 
			if (outputType.equalsIgnoreCase("csv")) {
		    	if(mimeType == null) mimeType = "text/plain";
		    	servletResponse.setContentType(mimeType);
		    	if(exporter == null) exporter = new JRCsvExporter();
		    } else if (outputType.equalsIgnoreCase("html")) {
		    	if(mimeType == null) mimeType = "text/html";
		    	servletResponse.setContentType(mimeType);
		    	if(exporter == null) exporter = new JRHtmlExporter();
		    	exporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN, Boolean.FALSE);
		    	exporter.setParameter(JRHtmlExporterParameter.BETWEEN_PAGES_HTML, "");
		    	// define the map structure for report images
		    	HashMap m_imagesMap = new HashMap();
		    	UUIDGenerator generator = UUIDGenerator.getInstance();
		    	UUID uuid = generator.generateTimeBasedUUID();
		    	String mapName = uuid.toString();
		    	servletRequest.getSession().setAttribute(mapName, m_imagesMap);
		    	exporter.setParameter(JRHtmlExporterParameter.IMAGES_MAP,m_imagesMap);
		    	exporter.setParameter(JRHtmlExporterParameter.IMAGES_URI, "image.jsp?mapname="+mapName+"&image=");
		    } else if (outputType.equalsIgnoreCase("xls")) {
		    	if(mimeType == null) mimeType = "application/vnd.ms-excel";
		    	servletResponse.setContentType(mimeType);
		    	if(exporter == null) exporter = new JRXlsExporter();
		    } else if (outputType.equalsIgnoreCase("xml")) {
		    	if(mimeType == null) mimeType = "text/xml";
		    	servletResponse.setContentType(mimeType);
		    	if(exporter == null) exporter = new JRXmlExporter();
		    } else if (outputType.equalsIgnoreCase("txt")) {
		    	if(mimeType == null) mimeType = "text/plain";
		    	servletResponse.setContentType(mimeType);
		    	if(exporter == null) exporter = new JRTextExporter(); 
		    } else if (outputType.equalsIgnoreCase("pdf"))	{			
		    	if(mimeType == null) mimeType = "application/pdf";
		    	servletResponse.setContentType(mimeType);
		    	if(exporter == null) exporter = new JRPdfExporter(); 	
		    } 		
		    else {
		    	if(mimeType != null && exporter != null) servletResponse.setContentType(mimeType);
		    	else {
		    		logger.warn("Impossible to load exporter for type " + outputType);
		    		logger.warn("Pdf exporter will be used");
		    		servletResponse.setContentType("application/pdf");
			    	exporter = new JRPdfExporter(); 
		    	}
		    }
			
			logger.debug("MIME type of response is [" + servletResponse.getContentType()+ "]");
			logger.debug("Exporter class used is  [" + exporter.getClass().getName()+ "]");
			
			
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
	        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, out);
	        exporter.exportReport();
	        logger.debug("Report exported succesfully");
		}catch(Exception e){
			logger.error("An exception has occured", e);
			throw e;
		}finally{
			try{
			if ((conn != null) && (!conn.isClosed()))
				conn.close();
			}catch(SQLException sqle){
				
			}
			
		}
	}	
}
