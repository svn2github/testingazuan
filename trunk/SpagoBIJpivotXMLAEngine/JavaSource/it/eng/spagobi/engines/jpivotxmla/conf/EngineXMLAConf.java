/**
 * 
 * LICENSE: see COPYING file. 
 * 
 */
package it.eng.spagobi.engines.jpivotxmla.conf;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;

import org.apache.log4j.Logger;
import org.xml.sax.InputSource;

public class EngineXMLAConf {
	private SourceBean config = null;
	
	private static transient Logger logger = Logger.getLogger(EngineXMLAConf.class);

	private static EngineXMLAConf instance = null;
	
	public static EngineXMLAConf getInstance(){
		if(instance==null) instance = new EngineXMLAConf();
		return instance;
	}
	
	private EngineXMLAConf() {
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
	
	public String getConnectionUrl(String connectionName) {
		String connectionUrl = null;
		
		try {
			SourceBean connSb = (SourceBean)config.getFilteredSourceBeanAttribute("CONNECTION", "name", connectionName);	
			connectionUrl = (String)connSb.getAttribute("url");
		} catch (Exception e) {
			logger.error("Problems occurred while reading configuration file", e);
		}	
		
		return connectionUrl;
	}
	
	public String getDefaultConnectionUrl() {
		String connectionUrl = null;
		
		try {
			SourceBean connSb = (SourceBean)config.getFilteredSourceBeanAttribute("CONNECTION", "isDefault", "true");	
			connectionUrl = (String)connSb.getAttribute("url");
		} catch (Exception e) {
			logger.error("Problems occurred while reading configuration file", e);
		}	
		
		return connectionUrl;
	}
}
