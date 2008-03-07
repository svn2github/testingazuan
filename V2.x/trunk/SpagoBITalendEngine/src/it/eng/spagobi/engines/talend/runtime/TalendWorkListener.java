/**
 * 
 */
package it.eng.spagobi.engines.talend.runtime;

import org.apache.log4j.Logger;

import commonj.work.WorkEvent;
import commonj.work.WorkException;
import commonj.work.WorkListener;

/**
 * @author Angelo Bernabei
 *         angelo.bernabei@eng.it
 */
public class TalendWorkListener implements WorkListener {

    private static transient Logger logger = Logger.getLogger(TalendWorkListener.class);
	
	
    public void workAccepted(WorkEvent event) {
	logger.info("IN");

    }

    public void workCompleted(WorkEvent event) {
	logger.info("IN");
	WorkException e= event.getException();
	if (e!=null) logger.error(e.getCause()); 
	
	try {
	    TalendWork processo = (TalendWork) event.getWorkItem().getResult();
	    if (!processo.isCompleteWithoutError()) logger.error("PROCES FAIL..."); 
	} catch (Exception ex) {
	    logger.error("Exception",ex); 
	}

    }

    public void workRejected(WorkEvent event) {
	logger.info("IN");

    }


    public void workStarted(WorkEvent event) {
	logger.info("IN");

    }



}
