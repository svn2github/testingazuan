package it.eng.spagobi.commons.services;

import org.apache.log4j.Logger;

import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.action.AbstractHttpAction;

public class BaseProfileAction extends AbstractHttpAction{
	
	static Logger logger = Logger.getLogger(BaseProfileAction.class);
	
	public void service(SourceBean request, SourceBean response) throws Exception {
		// TODO clean session 
	}

}
