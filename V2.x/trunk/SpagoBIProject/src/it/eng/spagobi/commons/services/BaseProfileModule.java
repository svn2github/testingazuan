package it.eng.spagobi.commons.services;

import org.apache.log4j.Logger;

import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.module.AbstractHttpModule;

public class BaseProfileModule extends AbstractHttpModule{
	
	static Logger logger = Logger.getLogger(BaseProfileModule.class);
	
	public void service(SourceBean request, SourceBean response) throws Exception {
		// TODO clean session 
	}

}
