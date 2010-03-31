/**
Copyright (c) 2005-2010, Engineering Ingegneria Informatica s.p.a.
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
package it.eng.spagobi.engines.commonj.runtime;

import it.eng.spago.base.ApplicationContainer;
import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.engines.commonj.exception.WorkExecutionException;
import it.eng.spagobi.engines.commonj.exception.WorkNotFoundException;
import it.eng.spagobi.engines.commonj.process.CmdExecWork;
import it.eng.spagobi.engines.commonj.process.SpagoBIWork;
import it.eng.spagobi.services.proxy.EventServiceProxy;
import it.eng.spagobi.utilities.DynamicClassLoader;
import it.eng.spagobi.utilities.engines.AuditServiceProxy;
import it.eng.spagobi.utilities.engines.EngineConstants;
import it.eng.spagobi.utilities.threadmanager.WorkManager;

import java.io.File;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import commonj.work.Work;
import commonj.work.WorkEvent;
import commonj.work.WorkItem;
import de.myfoo.commonj.work.FooRemoteWorkItem;
import de.myfoo.commonj.work.FooWorkItem;


public class CommonjWorkRunner implements IWorkRunner {


	private WorksRepository worksRepository;

	public static final String DEFAULT_CONTEXT = "Default";

	private static transient Logger logger = Logger.getLogger(CommonjWorkRunner.class);

	CommonjWorkRunner(WorksRepository worksRepository) {
		this.worksRepository = worksRepository;
	}

	public void run(HttpSession session, CommonjWork work, Map parameters)  throws WorkNotFoundException, WorkExecutionException {

		logger.debug("IN");

		File executableWorkDir;    	

		try {
			logger.debug("Starting run method of work : " +
					"name = [" + work.getWorkName() + "] ; " +
					"to start class= [" + work.getClassName() + "] ; ");


			executableWorkDir = worksRepository.getExecutableWorkDir(work);

			if (!worksRepository.containsWork(work)) {	    		
				logger.error("work [" + 
						worksRepository.getRootDir().getPath()+"/"+work.getWorkName()+ "] not found in repository");
				throw new WorkNotFoundException("work [" + 
						worksRepository.getRootDir().getPath()+"/"+work.getWorkName()+ "] not found in repository");
			}

			logger.debug("Work [" + work.getWorkName() +"] succesfully found in repository");

			// load in memory all jars found in folder!
			loadJars(work, executableWorkDir);

			//String classToLoad="prova.Studente";
			String classToLoad=work.getClassName();

			WorkManager wm = new WorkManager();
			logger.debug("work manager instanziated");

			AuditServiceProxy auditServiceProxy=(AuditServiceProxy)parameters.get(EngineConstants.ENV_AUDIT_SERVICE_PROXY);
			EventServiceProxy eventServiceProxy=(EventServiceProxy)parameters.get(EngineConstants.ENV_EVENT_SERVICE_PROXY);

			Object executionRoleO=parameters.get(SpagoBIConstants.EXECUTION_ROLE);
			String executionRole=executionRoleO!=null ? executionRoleO.toString() : ""; 

			Object documentIdO=parameters.get("DOCUMENT_ID");
			String documentId=documentIdO!=null ? documentIdO.toString() : null; 

			CommonjWorkListener listener = new CommonjWorkListener(auditServiceProxy, eventServiceProxy);

			if(documentId!=null){
				listener.setBiObjectID(documentId);
			}

			listener.setExecutionRole(executionRole);
			listener.setWorkName(work.getWorkName());
			listener.setWorkClass(work.getClassName());
			logger.debug("Class to run "+work.getClassName());

			logger.debug("listener ready");

			Class clazz = Thread.currentThread().getContextClassLoader().loadClass(classToLoad);
			Object obj = clazz.newInstance();
			logger.debug("class loaded "+classToLoad);
			SpagoBIWork workToLaunch=null;
			// class loaded could be instance of CmdExecWork o di Work, testa se è il primo, se no è l'altra
			if (obj instanceof CmdExecWork) {
				logger.debug("Class specified extends CmdExecWork");
				workToLaunch = (CmdExecWork) obj;
				((CmdExecWork)obj).setCommand(work.getCommand());
				((CmdExecWork)obj).setCommandEnvironment(work.getCommand_environment());
				((CmdExecWork)obj).setCmdParameters(work.getCmdParameters());			
				((CmdExecWork)obj).setCmdParameters(work.getCmdParameters());			

			}
			else
				if (obj instanceof SpagoBIWork) {
					logger.debug("Class specified extends Work");
					workToLaunch=(SpagoBIWork)obj;
					workToLaunch.setSbiParameters(work.getSbiParametersMap());							
				}


			//wm.setInSession(session, workToLaunch, listener, work.getWorkName());

			int ti=0;			
//			FooRemoteWorkItem wi=null;
//			if(workToLaunch!=null){			
//			wi=(FooRemoteWorkItem)wm.runWithReturn(workToLaunch, listener);
//			}
//			else{
//			logger.error("An error occurred while starting up execution for work [" + work.getWorkName() + "] type of class "+work.getClassName()+" is wrong, it must extend commonj.work.Work");
//			throw new WorkExecutionException("An error occurred while starting up execution for work [" + work.getWorkName() + "] type is wrong, it must extend commonj.work.Work");
//			}


//			wi.release();



		} catch (Throwable e) {
			logger.error("An error occurred while starting up execution for work [" + work.getWorkName() + "]");
			throw new WorkExecutionException("An error occurred while starting up execution for work [" + work.getWorkName() + "]", e);
		}
	}



	/** Explores all jar files in directory workDir  to load them
	 *  TODO: extend to other types!
	 * @param work
	 * @param parameters
	 * @param workDir
	 */

	private void loadJars(CommonjWork work, File workDir) {
		logger.debug("IN");

		// pass all the .jar into the folder
		File[] files = workDir.listFiles();

		for (int i = 0; i < files.length; i++) {
			File file=files[i];
			String name=file.getName();
			String ext = name.substring(name.lastIndexOf('.')+1, name.length());
			if(ext.equalsIgnoreCase("jar")){
				//updateCurrentClassLoader(file);
				logger.debug("loading file "+file.getName());			
				ClassLoader previous = Thread.currentThread().getContextClassLoader();
				DynamicClassLoader dcl = new DynamicClassLoader(file, previous);
				Thread.currentThread().setContextClassLoader(dcl);

			}
		}

		logger.debug("OUT");
	}



}
