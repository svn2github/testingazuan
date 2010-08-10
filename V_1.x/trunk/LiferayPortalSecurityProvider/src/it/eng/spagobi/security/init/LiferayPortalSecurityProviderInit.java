package it.eng.spagobi.security.init;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.init.InitializerIFace;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.io.IOException;
import java.io.InputStream;

import org.xml.sax.InputSource;

public class LiferayPortalSecurityProviderInit implements InitializerIFace {
	
	private SourceBean _config = null;
	
	public SourceBean getConfig() {
		return _config;
	}

	public void init(SourceBean config) {
		_config = config;
		SpagoBITracer.debug(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),"init()", 
		        "NOT IMPLEMENTED");
	}

}
