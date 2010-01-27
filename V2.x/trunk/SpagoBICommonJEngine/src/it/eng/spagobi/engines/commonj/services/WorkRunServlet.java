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
package it.eng.spagobi.engines.commonj.services;

import it.eng.spagobi.engines.commonj.CommonjEngine;
import it.eng.spagobi.engines.commonj.exception.WorkExecutionException;
import it.eng.spagobi.engines.commonj.exception.WorkNotFoundException;
import it.eng.spagobi.engines.commonj.runtime.CommonjWork;
import it.eng.spagobi.engines.commonj.runtime.WorksRepository;
import it.eng.spagobi.utilities.engines.AbstractEngineStartServlet;
import it.eng.spagobi.utilities.engines.EngineStartServletIOManager;
import it.eng.spagobi.utilities.engines.SpagoBIEngineException;

import org.apache.log4j.Logger;

public class WorkRunServlet extends AbstractEngineStartServlet {


	private static final long serialVersionUID = 1L;

	private static transient Logger logger = Logger.getLogger(WorkRunServlet.class);


	public void doService( EngineStartServletIOManager servletIOManager ) throws SpagoBIEngineException {

		WorksRepository worksRepository;
		CommonjWork work;

		logger.debug("IN");

		try {		

			//servletIOManager.auditServiceStartEvent();

			super.doService(servletIOManager);

			work = new CommonjWork( servletIOManager.getTemplateAsSourceBean() ); 
			CommonjEngine cm=new CommonjEngine();
			worksRepository = CommonjEngine.getWorksRepository();

			try {
				worksRepository.runJob(work, servletIOManager.getEnv());
			} catch (WorkNotFoundException ex) {
				logger.error(ex.getMessage());
				throw new SpagoBIEngineException("Work not found", "work.not.existing");

			}catch(WorkExecutionException ex) {
				logger.error(ex.getMessage(), ex);
				throw new SpagoBIEngineException("Work execution error","work.exectuion.error");
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				throw new SpagoBIEngineException("Work execution error", "work.exectuion.error");
			}

			servletIOManager.tryToWriteBackToClient("commonj.process.started");

		}
		catch (Exception e) {
			logger.error("Error in servlet",e);
			e.printStackTrace();		
			}
		finally {
			logger.debug("OUT");
		}
	}
}
