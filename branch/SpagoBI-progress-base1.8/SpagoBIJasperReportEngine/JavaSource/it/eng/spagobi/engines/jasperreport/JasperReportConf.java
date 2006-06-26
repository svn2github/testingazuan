/**
 * 
 * LICENSE: see COPYING file. 
 * 
 */
package it.eng.spagobi.engines.jasperreport;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;

import org.apache.log4j.Logger;
import org.xml.sax.InputSource;

public class JasperReportConf {
	private SourceBean config = null;
	
	private static transient Logger logger = Logger.getLogger(JasperReportConf.class);

	private static JasperReportConf instance = null;
	
	public static JasperReportConf getInstance(){
		if(instance==null) instance = new JasperReportConf();
		return instance;
	}
	
	private JasperReportConf() {
		try {
			logger.debug("Resource: " + getClass().getResource("/engine-config.xml"));
			config = SourceBean.fromXMLStream(
					new InputSource(getClass().getResourceAsStream("/engine-config.xml")));
		} catch (SourceBeanException e) {
			logger.error("Impossible to load configuration for jasper report engine", e);
		}
	}
	
	public SourceBean getConfig() {
		return config;
	}
	
}
