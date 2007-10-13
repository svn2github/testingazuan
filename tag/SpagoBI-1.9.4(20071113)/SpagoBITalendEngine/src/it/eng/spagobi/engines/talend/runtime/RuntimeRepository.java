/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005 Engineering Ingegneria Informatica S.p.A.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

**/
package it.eng.spagobi.engines.talend.runtime;

import it.eng.spagobi.engines.talend.exception.ContextNotFoundException;
import it.eng.spagobi.engines.talend.exception.JobExecutionException;
import it.eng.spagobi.engines.talend.exception.JobNotFoundException;
import it.eng.spagobi.engines.talend.utils.TalendScriptAccessUtils;
import it.eng.spagobi.engines.talend.utils.ZipUtils;
import it.eng.spagobi.utilities.callbacks.audit.AuditAccessUtils;


import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Andrea Gioia
 *
 */
public class RuntimeRepository {
	private File rootDir;
	
	public RuntimeRepository(File rootDir) {
		this.rootDir = rootDir;
	}
	
	public void deployJob(JobDeploymentDescriptor jobDeploymentDescriptor, ZipFile executableJobFiles) {
		File jobsDir = new File(rootDir, jobDeploymentDescriptor.getLanguage().toLowerCase());
		File projectDir = new File(jobsDir, jobDeploymentDescriptor.getProject());		
		ZipUtils.unzipSkipFirstLevel(executableJobFiles, projectDir);		
	}
	
	public void runJob(Job job, Map parameters, AuditAccessUtils auditAccessUtils, String auditId) throws JobNotFoundException, ContextNotFoundException, JobExecutionException {
		IJobRunner jobRunner = getJobRunner(job.getLanguage());
		if(jobRunner == null) return;
		jobRunner.run(job, parameters, auditAccessUtils, auditId);
	}
	
	public IJobRunner getJobRunner(String jobLanguage) {
		if(jobLanguage.equalsIgnoreCase("java")) {
			return new JavaJobRunner(this);
		} else if(jobLanguage.equalsIgnoreCase("perl")) {
			return new PerlJobRunner(this);
		} else {
			return null;
		}
	}

	public File getRootDir() {
		return rootDir;
	}

	public void setRootDir(File rootDir) {
		this.rootDir = rootDir;
	}
	
	public File getExecutableJobProjectDir(Job job) {
		File jobsDir = new File(rootDir, job.getLanguage().toLowerCase());
		File projectDir = new File(jobsDir, job.getProject());
		return projectDir;
	}
	
	public File getExecutableJobDir(Job job) {
		File jobDir = new File(getExecutableJobProjectDir(job), job.getName());
		return jobDir;
	}
	
	public File getExecutableJobFile(Job job) {
		File jobExecutableFile = new File(getExecutableJobDir(job), TalendScriptAccessUtils.getExecutableFileName(job));	
		return jobExecutableFile;
	}
	
	public boolean containsJob(Job job) {
		return getExecutableJobFile(job).exists();
	}
	
	public static void main(String[] args) throws ZipException, IOException {
		File rootDir = new File("C:\\Prototipi\\SpagoBI-Demo-1.9.2\\webapps\\SpagoBITalendEngine\\RuntimeRepository");
		File zipFile = new File("C:\\Prototipi\\TalendJob2.zip");
		RuntimeRepository runtimeRepository = new RuntimeRepository(rootDir);
		JobDeploymentDescriptor jobDeploymentDescriptor = new JobDeploymentDescriptor("PP2", "perl");
		runtimeRepository.deployJob(jobDeploymentDescriptor, new ZipFile(zipFile));
	}
}
