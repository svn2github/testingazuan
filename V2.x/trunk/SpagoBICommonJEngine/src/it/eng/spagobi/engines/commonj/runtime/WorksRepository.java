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
package it.eng.spagobi.engines.commonj.runtime;

import it.eng.spagobi.engines.commonj.exception.WorkExecutionException;
import it.eng.spagobi.engines.commonj.exception.WorkNotFoundException;
import it.eng.spagobi.utilities.callbacks.audit.AuditAccessUtils;


import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class WorksRepository {
	private File rootDir;

	/**
	 * Instantiates a new runtime repository.
	 * 
	 * @param rootDir the root dir
	 */
	public WorksRepository(File rootDir) {
		this.rootDir = rootDir;
	}




	/**
	 * Run work.
	 * 
	 * @param work the CommonJWork
	 * @param env the environment

	 * 
	 * @throws WorkNotFoundException the job not found exception
	 * @throws WorkExecutionException the job execution exception
	 */
	public void runJob(CommonjWork work, Map env) throws WorkNotFoundException, WorkExecutionException {
		CommonjWorkRunner workRunner=new CommonjWorkRunner(this);
		if(workRunner != null) {
			workRunner.run(work, env);
		}		
	}


	/**
	 * Gets the root dir.
	 * 
	 * @return the root dir
	 */
	public File getRootDir() {
		return rootDir;
	}

	/**
	 * Sets the root dir.
	 * 
	 * @param rootDir the new root dir
	 */
	public void setRootDir(File rootDir) {
		this.rootDir = rootDir;
	}

	/**
	 * Gets the executable work project dir.
	 * 
	 * @param work the work
	 * 
	 * @return the executable work project dir
	 */
	public File getExecutableJobProjectDir(CommonjWork work) {
	File workDir = new File(rootDir, work.getWorkName());
	File projectDir = new File(workDir, work.getWorkName());
	return projectDir;
	}



	public File getExecutableWorkProjectDir(CommonjWork work) {
		File worksDir = new File(rootDir, work.getWorkName());
		return worksDir;
	}


	/**
	 * Gets the executable job dir.
	 * 
	 * @param job the job
	 * 
	 * @return the executable job dir
	 */
	public File getExecutableWorkDir(CommonjWork work) {
		File workDir = new File(rootDir, work.getWorkName());
		return workDir;
	}

	
	/**
	 * Gets the executable job file.
	 * 
	 * @param job the job
	 * 
	 * @return the executable job file
	 */
	public File getExecutableWorkFile(CommonjWork work) {
		File jobExecutableFile = new File(getExecutableWorkDir(work), work.getWorkName());	
		return jobExecutableFile;
	}

	/**
	 * Contains job.
	 * 
	 * @param job the job
	 * 
	 * @return true, if successful
	 */
	public boolean containsWork(CommonjWork work) {
	
		File workFolder=new File(rootDir, work.getWorkName());	
		boolean exists=workFolder.exists();
		return exists;
	}
	
	

	/**
	 * The main method.
	 * 
	 * @param args the arguments
	 * 
	 * @throws ZipException the zip exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void main(String[] args) throws ZipException, IOException {
		File rootDir = new File("C:\\Prototipi\\SpagoBI-Demo-1.9.2\\webapps\\SpagoBITalendEngine\\RuntimeRepository");
		File zipFile = new File("C:\\Prototipi\\TalendJob2.zip");
		WorksRepository runtimeRepository = new WorksRepository(rootDir);
		WorkDeploymentDescriptor jobDeploymentDescriptor = new WorkDeploymentDescriptor("PP2", "perl");
		//runtimeRepository.deployJob(jobDeploymentDescriptor, new ZipFile(zipFile));
	}
}
