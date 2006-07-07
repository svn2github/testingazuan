/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005 Engineering Ingegneria Informatica S.p.A.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

**/
package it.eng.qbe.export;

import it.businesslogic.ireport.export.JRTxtExporter;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
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
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXmlExporter;

/**
 * Compile, fill and export a report template to a file or stream
 * 
 * @author Gioia
 */
public class ReportRunner {
	
	public ReportRunner() {}
	
	public void run(File templateFile, File reportFile, String outputType, Connection conn) throws Exception {
		
		// assume that the report query in in sql format
		// TODO check this and adapt the behaviour
		
		InputStream is = new FileInputStream(templateFile);
		JasperReport report  = JasperCompileManager.compileReport(is);
		JasperPrint jasperPrint = JasperFillManager.fillReport(report, new HashMap(), conn);
			
		JRExporter exporter = null; 
			
		if (outputType.equalsIgnoreCase("text/html")) {
		   	exporter = new JRHtmlExporter();
		   	exporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN, Boolean.FALSE);
		   	exporter.setParameter(JRHtmlExporterParameter.BETWEEN_PAGES_HTML, "");
		} else if (outputType.equalsIgnoreCase("text/xml")) {
		   	exporter = new JRXmlExporter();
		} else if (outputType.equalsIgnoreCase("text/plain")) {
		   	//exporter = new JRTextExporter(); 
		   	exporter = new JRTxtExporter(); 
		} else if (outputType.equalsIgnoreCase("text/csv")) {
		   	exporter = new JRCsvExporter(); 	
		} else if (outputType.equalsIgnoreCase("application/pdf"))	{			
		   	exporter = new JRPdfExporter(); 	
		} else if (outputType.equalsIgnoreCase("application/rtf"))	{			
		   	exporter = new JRRtfExporter(); 		
		} else if (outputType.equalsIgnoreCase("application/vnd.ms-excel")) {
		   	exporter = new JRXlsExporter();
		} else {
		   	exporter = new JRPdfExporter();
		}
			
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
	    exporter.setParameter(JRExporterParameter.OUTPUT_FILE , reportFile);
	    exporter.exportReport();
	}
	
}
