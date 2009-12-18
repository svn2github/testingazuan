/**
Copyright (c) 2005-2008, Engineering Ingegneria Informatica s.p.a.
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

    * Redistributions of source code must retain the above copyright notice, this list of 
      conditions and the following disclaimer.
      
    * Redistributions in binary form must reproduce the above copyright notice, this list of 
      conditions and the following disclaimer in the documentation and/or other materials 
      provided with the distribution.
      
    * Neither the name of the Engineering Ingegneria Informatica s.p.a. nor the names of its contributors may
      be used to endorse or promote products derived from this software without specific
      prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND 
CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, 
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF 
MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR 
CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, 
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE

**/
package it.eng.spagobi.utilities.threadmanager;


import it.eng.spago.base.SourceBean;
import it.eng.spagobi.services.common.EnginConf;

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

    /**
     * Instantiates a new work manager.
     * 
     * @throws NamingException the naming exception
     */
    public WorkManager() throws NamingException {
	init();
    }

    /**
     * Run.
     * 
     * @param job the job
     * @param listener the listener
     * 
     * @throws Exception the exception
     */
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

    /**
     * Inits the.
     * 
     * @throws NamingException the naming exception
     */
    public void init() throws NamingException {
	logger.debug("IN");
	try {
	    
	    SourceBean jndiSB = (SourceBean)EnginConf.getInstance().getConfig().getAttribute("JNDI_THREAD_MANAGER");
	    String jndi = (String) jndiSB.getCharacters();
	    logger.debug("lookup jndi:"+jndi);
	    Context ctx = new InitialContext();
	    wm = (FooWorkManager) ctx.lookup(jndi);
	    logger.debug("JNDI lookup successfully executed!!");
	} catch (NamingException e) {
	    logger.error(e);
	    throw e;
	} catch (Exception e) {
	    logger.error(e);
	} catch (Throwable t) {
	    logger.error(t);
	} finally {
	    logger.debug("OUT");
	}

    }

}
