/**
 * 
 * LICENSE: see COPYING file. 
 * 
 */
package it.eng.spagobi.engines.jasperreport;

import it.eng.spago.base.SourceBean;

import java.util.List;

import net.sf.jasperreports.engine.JRExporter;

public class ExporterFactory {	
	
	static public JRExporter getExporter(String format) {
		JRExporter exporter = null;
		
		SourceBean config = JasperReportConf.getInstance().getConfig();
		SourceBean exporterConfig = (SourceBean) config.getFilteredSourceBeanAttribute ("EXPORTERS.EXPORTER", "format", format);
		if(exporterConfig == null) return null;
		String exporterClassName = (String)exporterConfig.getAttribute("class");
		if(exporterClassName == null) return exporter;
		
		try {
			exporter = (JRExporter)Class.forName(exporterClassName).newInstance();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return exporter;
	}
	
	static public String getMIMEType(String format) {
		String mimeType = null;
		SourceBean config = JasperReportConf.getInstance().getConfig();
		SourceBean exporterConfig = (SourceBean) config.getFilteredSourceBeanAttribute ("EXPORTERS.EXPORTER", "format", format);
		if(exporterConfig == null) return null;
		mimeType = (String)exporterConfig.getAttribute("mime");
		return mimeType;
	}
	
	static public String getDefaultType(){
		String defaultType = null;
		SourceBean config = JasperReportConf.getInstance().getConfig();
		defaultType = (String)config.getAttribute("EXPORTERS.default");
		if(defaultType == null) defaultType = "html";
		return defaultType;
	}
}
