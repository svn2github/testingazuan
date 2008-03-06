/**
 * 
 */
package it.eng.spagobi.utilities.threadmanager;

import java.util.ArrayList;
import java.util.Collection;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import commonj.work.Work;
import commonj.work.WorkItem;
import commonj.work.WorkListener;

import de.myfoo.commonj.work.FooWorkManager;

/**
 * @author Angelo Bernabei angelo.bernabei@eng.it
 */
public class WorkManager {

    private FooWorkManager wm = null;
    private static transient Logger logger = Logger.getLogger(WorkManager.class);

    public WorkManager() throws NamingException {
	init();
    }

    public void run(Work job, WorkListener listener) throws Exception {
	logger.debug("IN");
	try {
	    WorkItem wi = wm.schedule(job, listener);
	    
	} catch (Exception e) {
	    logger.error("Exception", e);
	    throw e;
	} finally {
	    logger.debug("OUT");
	}

    }

    public void init() throws NamingException {
	logger.debug("IN");
	try {
	    Context ctx = new InitialContext();
	    wm = (FooWorkManager) ctx.lookup("java:/comp/env/wm/WorkManager");
	    // wm = (FooWorkManager) ctx.lookup("wm/WorkManager");
	    // Object o = ctx.lookup("wm/FooWorkManager");
	    // System.out.println(o.getClass().getName());
	    // wm = (WorkManager) o;
	} catch (NamingException e) {
	    logger.error("IN", e);
	    throw e;
	} finally {
	    logger.debug("OUT");
	}

    }

}
