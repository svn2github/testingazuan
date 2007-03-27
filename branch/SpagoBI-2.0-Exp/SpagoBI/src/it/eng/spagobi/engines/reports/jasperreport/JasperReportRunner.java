/**
 * 
 * LICENSE: see COPYING file. 
 * 
 */
package it.eng.spagobi.engines.reports.jasperreport;

import it.eng.spagobi.bo.dao.DAOFactory;
import it.eng.spagobi.bo.dao.IBIObjectCMSDAO;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
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

import org.safehaus.uuid.UUID;
import org.safehaus.uuid.UUIDGenerator;

/**
 * Jasper Report implementation built to provide all methods to
 * run a report inside SpagoBI. It is the jasper report Engine implementation
 * for SpagoBI.
 */
public class JasperReportRunner {
	
	/**
	 * This method, known all input information, runs a report with JasperReport 
	 * inside SpagoBI. iIt is the Jasper Report Engine's core method.
	 * 
	 * @param ds The input Data Source for the Report
	 * @param parameters The input parameters map
	 * @param servletContext The java servlet context object
	 * @param servletResponse The java http servlet response object
	 * @throws Exception If any Exception occurred
	 */
	public void runReport(Connection conn, Map parameters, OutputStream out, 
			              HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws Exception {
		try {								
			InputStream is = getTemplateContent(parameters);					
			JasperReport report  = JasperCompileManager.compileReport(is);
			JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, conn);
			String outputType = (String) parameters.get("param_output_format");
			if(outputType == null) {
				outputType = "HTML";
			}
			JRExporter exporter = null;	
			String mimeType = "";
			if (outputType.equalsIgnoreCase("csv")) {
		    	mimeType = "text/plain";
		    	servletResponse.setContentType(mimeType);
		    	exporter = new JRCsvExporter();
		    } else if (outputType.equalsIgnoreCase("html")) {
		    	mimeType = "text/html";
		    	servletResponse.setContentType(mimeType);
		    	exporter = new JRHtmlExporter();
		    	exporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN, Boolean.FALSE);
		    	exporter.setParameter(JRHtmlExporterParameter.BETWEEN_PAGES_HTML, "");
		    	HashMap m_imagesMap = new HashMap();
		    	UUIDGenerator generator = UUIDGenerator.getInstance();
		    	UUID uuid = generator.generateTimeBasedUUID();
		    	String mapName = uuid.toString();
		    	servletRequest.getSession().setAttribute(mapName, m_imagesMap);
		    	exporter.setParameter(JRHtmlExporterParameter.IMAGES_MAP,m_imagesMap);
		    	exporter.setParameter(JRHtmlExporterParameter.IMAGES_URI, "AdapterHTTP?PAGE=ExecuteJasperReportPage&TASK=GET_REPORT_IMAGE&mapname="+mapName+"&image=");
		    } else if (outputType.equalsIgnoreCase("xls")) {
		    	mimeType = "application/vnd.ms-excel";
		    	servletResponse.setContentType(mimeType);
		    	exporter = new JRXlsExporter();
		    } else if (outputType.equalsIgnoreCase("xml")) {
		    	mimeType = "text/xml";
		    	servletResponse.setContentType(mimeType);
		    	exporter = new JRXmlExporter();
		    } else if (outputType.equalsIgnoreCase("txt")) {
		    	mimeType = "text/plain";
		    	servletResponse.setContentType(mimeType);
		    	exporter = new JRTextExporter(); 
		    } else if (outputType.equalsIgnoreCase("pdf"))	{			
		    	mimeType = "application/pdf";
		    	servletResponse.setContentType(mimeType);
		    	exporter = new JRPdfExporter(); 	
		    } 
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
	        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, out);
	        exporter.exportReport();
	        
		}catch(Exception e){
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), 
					            "runReport", "Error while producing report", e);
		}
	}	
	
	
	
	private InputStream getTemplateContent(Map parameters) throws Exception {
		InputStream is = null;		
		String biObjectPath = (String)parameters.get("biobjectPath");
		IBIObjectCMSDAO biobjcmsdao = DAOFactory.getBIObjectCMSDAO();
		is = biobjcmsdao.getTemplate(biObjectPath);
		return is;
	}
	
	
	
	
}
