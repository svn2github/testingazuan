package it.eng.spagobi.security.init;


import org.apache.log4j.Logger;

import it.eng.spago.base.SourceBean;
import it.eng.spago.init.InitializerIFace;



public class LiferayPortalSecurityProviderInit implements InitializerIFace {
	
	static private Logger logger = Logger.getLogger(LiferayPortalSecurityProviderInit.class);

	private SourceBean _config = null;
	
	public SourceBean getConfig() {
		return _config;
	}

	public void init(SourceBean config) {
		_config = config;
		logger.warn( "NOT IMPLEMENTED");
	}

}
