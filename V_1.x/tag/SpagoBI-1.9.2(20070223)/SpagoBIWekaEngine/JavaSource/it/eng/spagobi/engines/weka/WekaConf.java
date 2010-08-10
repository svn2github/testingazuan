/**
 *
 *	LICENSE: see COPYING file
 *
**/
package it.eng.spagobi.engines.weka;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;

import org.apache.log4j.Logger;
import org.xml.sax.InputSource;

public class WekaConf {
	private SourceBean config = null;
	
	private static transient Logger logger = Logger.getLogger(WekaConf.class);

	private static WekaConf instance = null;
	
	public static WekaConf getInstance(){
		if(instance==null) instance = new WekaConf();
		return instance;
	}
	
	private WekaConf() {
		try {
			config = SourceBean.fromXMLStream(
					new InputSource(getClass().getResourceAsStream("/engine-config.xml")));
		} catch (SourceBeanException e) {
			logger.error("Impossible to load configuration for weka engine", e);
		}
	}
	
	public SourceBean getConfig() {
		return config;
	}
	
}
