package it.eng.spagobi.analiticalmodel.execution.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.action.AbstractAction;
import it.eng.spago.dispatching.action.AbstractHttpAction;

import org.apache.log4j.Logger;

public class SaveObjectAction extends AbstractAction {

    private static transient Logger logger=Logger.getLogger(SaveObjectAction.class);
    
	public void service(SourceBean request, SourceBean responseSb) throws Exception {
	    logger.debug("IN");
		

		
		String operation=(String)request.getAttribute("op");
		if (operation!=null && operation.equals("a")){
		    logger.debug("operation="+operation); 
		}else {
		    
		}
	    logger.debug("OUT");
	}

}
