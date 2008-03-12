/**
 * 
 */
package it.eng.spagobi.utilities.threadmanager;


import it.eng.spago.base.SourceBean;
import it.eng.spagobi.services.common.EnginConf;

import java.util.ArrayList;
import java.util.Collection;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.sun.corba.se.spi.orbutil.threadpool.Work;

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
	    
	    SourceBean jndiSB = (SourceBean)EnginConf.getInstance().getConfig().getAttribute("JNDI_THREAD_MANAGER");
	    String jndi = (String) jndiSB.getCharacters();
	    
	    Context ctx = new InitialContext();
	    wm = (FooWorkManager) ctx.lookup(jndi);

	} catch (NamingException e) {
	    logger.error("IN", e);
	    throw e;
	} finally {
	    logger.debug("OUT");
	}

    }

}
