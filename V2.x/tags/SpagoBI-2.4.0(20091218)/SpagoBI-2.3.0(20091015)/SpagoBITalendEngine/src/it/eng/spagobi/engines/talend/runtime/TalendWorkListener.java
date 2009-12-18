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
	
	
    /* (non-Javadoc)
     * @see commonj.work.WorkListener#workAccepted(commonj.work.WorkEvent)
     */
    public void workAccepted(WorkEvent event) {
	logger.info("IN");

    }

    /* (non-Javadoc)
     * @see commonj.work.WorkListener#workCompleted(commonj.work.WorkEvent)
     */
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

    /* (non-Javadoc)
     * @see commonj.work.WorkListener#workRejected(commonj.work.WorkEvent)
     */
    public void workRejected(WorkEvent event) {
	logger.info("IN");

    }


    /* (non-Javadoc)
     * @see commonj.work.WorkListener#workStarted(commonj.work.WorkEvent)
     */
    public void workStarted(WorkEvent event) {
	logger.info("IN");

    }



}
