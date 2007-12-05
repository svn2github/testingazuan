/**
 * 
 * LICENSE: see COPYING file. 
 * 
 */
package it.eng.spagobi.services.common;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;

import org.apache.log4j.Logger;
import org.xml.sax.InputSource;

public class EnginConf {
	private SourceBean config = null;
	
	private static transient Logger logger = Logger.getLogger(EnginConf.class);

	private static EnginConf instance = null;
	
	public static EnginConf getInstance(){
		if(instance==null) instance = new EnginConf();
		return instance;
	}
	
	private EnginConf() {
		try {
			logger.debug("Resource: " + getClass().getResource("/engine-config.xml"));
			if (getClass().getResource("/engine-config.xml")!=null){
				InputSource source=new InputSource(getClass().getResourceAsStream("/engine-config.xml"));
				config = SourceBean.fromXMLStream(source);    
			}else logger.debug("Impossible to load configuration for report engine");
		} catch (SourceBeanException e) {
			logger.error("Impossible to load configuration for report engine", e);
		}
	}
	
	public SourceBean getConfig() {
		return config;
	}
	
}
