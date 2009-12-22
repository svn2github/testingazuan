package it.eng.spagobi.security.init;

import org.apache.log4j.Logger;

import it.eng.spago.base.Constants;
import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.init.InitializerIFace;
import it.eng.spago.tracing.TracerSingleton;
import it.eng.spagobi.commons.initializers.indexing.IndexingInitializer;
import it.eng.spagobi.security.RoleSynchronizer;

public class InternalSecurityInitializer implements InitializerIFace {

	static private Logger logger = Logger.getLogger(InternalSecurityInitializer.class);
	private SourceBean _config = null;
	
	public SourceBean getConfig() {
		return _config;
	}

	/* (non-Javadoc)
	 * @see it.eng.spago.init.InitializerIFace#init(it.eng.spago.base.SourceBean)
	 */
	public void init(SourceBean config) {
		logger.debug("IN");
		/* loads default users , attributes reading from xml*/
		
		
		logger.debug("OUT");

	}

}
