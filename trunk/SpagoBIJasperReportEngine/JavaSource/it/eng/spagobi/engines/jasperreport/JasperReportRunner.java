/*
 * Created on 3-mag-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package it.eng.spagobi.engines.jasperreport;

import it.eng.spagobi.utilities.SpagoBIAccessUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRTextExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXmlExporter;

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
	private transient Logger logger = Logger.getLogger(JasperReportRunner.class);
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
	 * @return The content of report execution, stored into a byte[] object
	 * @throws Exception If any Exception occurred
	 */
	public byte[] runReport(Connection conn, Map parameters, ServletContext servletContext, 
			                HttpServletResponse servletResponse, HttpServletRequest servletRequest) throws Exception {
		try{
			String tmpDirectory = System.getProperty("java.io.tmpdir");
			logger.debug("Engines"+ this.getClass().getName() + "runReport() Computing the classpath for JasperReport Debugging");
			String webinflibPath = servletContext.getRealPath("WEB-INF") + System.getProperty("file.separator") + "lib";
			logger.debug("Engines"+ this.getClass().getName()+"runReport() WEB-INF/lib/ path is ["+this.getClass().getName()+"]");
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
					logger.debug("Engines"+ this.getClass().getName()+ "runReport() Appending to Jasper Report ClassPath jar file [" + fileToAppend + "]");
					jasperReportClassPathStringBuffer.append(fileToAppend);
					jasperReportClassPathStringBuffer.append(System.getProperty("path.separator"));  
				}
			}
			String jasperReportClassPath = jasperReportClassPathStringBuffer.toString();
			jasperReportClassPath = jasperReportClassPath.substring(0, jasperReportClassPath.length() - 1);
			System.setProperty("jasper.reports.compile.temp", tmpDirectory);
			System.setProperty("jasper.reports.compile.class.path", jasperReportClassPath);
			logger.debug("Engines"+ this.getClass().getName()+ "runReport() jasper.reports.compile.temp -- [" + System.getProperty("jasper.reports.compile.temp")+"]");
			logger.debug("Engines"+ this.getClass().getName()+ "runReport() jasper.reports.compile.class.path-- [" + System.getProperty("jasper.reports.compile.class.path")+"]");
			byte[] jcrContent = new SpagoBIAccessUtils().getContent(this.spagobibaseurl, this.templatePath);
			InputStream is = new java.io.ByteArrayInputStream(jcrContent);
			String outputType = (String)parameters.get("param_output_format");
			logger.debug("Engines"+ this.getClass().getName()+ "runReport() param_output_format -- [" +outputType+"]");
			JasperReport report  = JasperCompileManager.compileReport(is);
			
			byte[] output;
		    JasperPrint jasperPrint;
			if ((outputType != null) && (!(outputType.equalsIgnoreCase("pdf")))){
					logger.debug("Engines" + this.getClass().getName() + "runReport() Output format is not PDF");
					logger.debug("Engines" + this.getClass().getName() + "runReport() Try to fill Jasper Print");
					jasperPrint = JasperFillManager.fillReport(report, parameters, conn);
					logger.debug("Engines" +  this.getClass().getName()+ "runReport() Jasper Print Filled OK");
					JRExporter exporter = null;

				    if (outputType.equalsIgnoreCase("csv")) {
				    	servletResponse.setContentType("text/plain");
				    	exporter = new JRCsvExporter();
				    } else if (outputType.equalsIgnoreCase("html")) {
				    	servletResponse.setContentType("text/html");
				    	exporter = new JRHtmlExporter();
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
				    	servletResponse.setContentType("application/vnd.ms-excel");
				    	exporter = new JRXlsExporter();
				    } else if (outputType.equalsIgnoreCase("xml")) {
				    	servletResponse.setContentType("text/xml");
				    	exporter = new JRXmlExporter();
				    }else if (outputType.equalsIgnoreCase("txt")) {
				    	servletResponse.setContentType("text/plain");
				    	exporter = new JRTextExporter(); 
				    } 
				    
				   
				    
				    logger.debug("Engines"+ this.getClass().getName()+ "runReport() Exporter Class [" + exporter.getClass().getName()+"]");
				    output = exportReportToBytes(jasperPrint, exporter);
				    logger.debug("Engines"+ this.getClass().getName()+ "runReport() Report OK");
				    return output;
			}else {
					logger.debug("Engines"+ this.getClass().getName()+ "runReport() Output format PDF");
					servletResponse.setContentType("application/pdf");
					return JasperRunManager.runReportToPdf(report, parameters, conn);
					
			}
		}catch(Exception e){
			logger.debug("Engines"+ this.getClass().getName()+ "runReport() An exception has occured", e);
			throw e;
		}finally{
			try{
			if ((conn != null) && (!conn.isClosed()))
				conn.close();
			}catch(SQLException sqle){
				
			}
			
		}
	}
	/**
	 * Transforms the report content into a byte[] object.
	 * 
	 * @param jasperPrint A jasper engine utility used to fill a report
	 * @param exporter	A jasper engine utility used to export a report
	 * @return The report transformed into bytes
	 * @throws JRException If any Exception occurred
	 */
	private byte[] exportReportToBytes(JasperPrint jasperPrint, JRExporter exporter) throws JRException {
        byte[] output;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);

        exporter.exportReport();

        output = baos.toByteArray();
        return output;
    }
	
	
	

	
	
}
