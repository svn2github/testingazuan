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
/*
 * Created on 4-mag-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package it.eng.spagobi.engines.dossier;


import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spagobi.analiticalmodel.document.bo.BIObject;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.utilities.GeneralUtilities;
import it.eng.spagobi.engines.dossier.bo.DossierPresentation;
import it.eng.spagobi.engines.dossier.constants.BookletsConstants;
import it.eng.spagobi.engines.dossier.dao.DossierDAOHibImpl;
import it.eng.spagobi.engines.dossier.dao.IDossierDAO;
import it.eng.spagobi.engines.dossier.dao.IDossierPresentationsDAO;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.jbpm.JbpmConfiguration;
import org.jbpm.JbpmContext;
import org.jbpm.context.exe.ContextInstance;
import org.jbpm.graph.exe.ProcessInstance;
import org.jbpm.taskmgmt.exe.TaskInstance;


public class BookletsServlet extends HttpServlet {
	
	static private Logger logger = Logger.getLogger(BookletsServlet.class);
	
	public void init(ServletConfig config) throws ServletException {
        super.init(config);
     } 

	public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		OutputStream out = null;
		String task = "";
		try{
	 		task = request.getParameter(BookletsConstants.BOOKLET_SERVICE_TASK);		
	 		out = response.getOutputStream();
	 		if(task.equalsIgnoreCase(BookletsConstants.BOOKLET_SERVICE_TASK_GET_TEMPLATE_IMAGE)){
	 			String pathimg = (String)request.getParameter(BookletsConstants.BOOKLET_SERVICE_PATH_IMAGE);
			 	if(pathimg!=null) {
			 		if (!pathimg.startsWith("/") && !(pathimg.charAt(1) == ':')) {
			 			String root = ConfigSingleton.getRootPath();
			 			pathimg = root + "/" + pathimg;
			 		}
				 	File imgFile = new File(pathimg);
				 	FileInputStream fis = new FileInputStream(imgFile);
				 	byte[] content = GeneralUtilities.getByteArrayFromInputStream(fis);
				 	out.write(content);
				 	out.flush();
		            fis.close();
		            imgFile.delete();
		            return;
			 	} 
	 		} else if(task.equalsIgnoreCase(BookletsConstants.BOOKLET_SERVICE_TASK_DOWN_FINAL_DOC)){
	 			String activityKey = request.getParameter(SpagoBIConstants.ACTIVITYKEY);
	 			JbpmContext jbpmContext = null;
	 			Integer dossierId = null;
	 			Long workflowProcessId = null;
	 			try {
		 			JbpmConfiguration jbpmConfiguration = JbpmConfiguration.getInstance();
		 	    	        jbpmContext = jbpmConfiguration.createJbpmContext();
		 			long activityKeyId = Long.valueOf(activityKey).longValue();
		 			TaskInstance taskInstance = jbpmContext.getTaskInstance(activityKeyId);
		 			ContextInstance contextInstance = taskInstance.getContextInstance();
		 			ProcessInstance processInstance = contextInstance.getProcessInstance();
		 			workflowProcessId = new Long(processInstance.getId());
		 			String dossierIdStr = (String) contextInstance.getVariable(BookletsConstants.DOSSIER_ID);
		 			dossierId = new Integer(dossierIdStr);
	 			} finally {
	 				if (jbpmContext != null) jbpmContext.close();
	 			}
	 			if (dossierId != null) {
	 				BIObject dossier = DAOFactory.getBIObjectDAO().loadBIObjectById(dossierId);
	 				IDossierPresentationsDAO dpDAO = DAOFactory.getDossierPresentationDAO();
	 				DossierPresentation presentation = dpDAO.getCurrentPresentation(dossierId, workflowProcessId);
	 				byte[] finalDocBytes = presentation.getContent();
				 	response.setHeader("Content-Disposition","attachment; filename=\"" + dossier.getName() + ".ppt" + "\";");
		 			response.setContentLength(finalDocBytes.length);
		 			out.write(finalDocBytes);
		 			out.flush();
	 			} else {
	 				logger.error("Booklet configuration path not found!");
	 			}
	            return;
		 		
	 		} else if(task.equalsIgnoreCase(BookletsConstants.BOOKLET_SERVICE_TASK_DOWN_PRESENTATION_VERSION)) {
	 			String dossierIdStr = request.getParameter(BookletsConstants.DOSSIER_ID);
	 			Integer dossierId = new Integer(dossierIdStr);
	 			String versionStr = request.getParameter(BookletsConstants.VERSION_ID);
	 			Integer versionId = new Integer(versionStr);
 				BIObject dossier = DAOFactory.getBIObjectDAO().loadBIObjectById(dossierId);
 				IDossierPresentationsDAO dpDAO = DAOFactory.getDossierPresentationDAO();
 				byte[] finalDocBytes = dpDAO.getPresentationVersionContent(dossierId, versionId);
			 	response.setHeader("Content-Disposition","attachment; filename=\"" + dossier.getName() + ".ppt" + "\";");
	 			response.setContentLength(finalDocBytes.length);
	 			out.write(finalDocBytes);
	            return;
	            
	 		} else if(task.equalsIgnoreCase(BookletsConstants.BOOKLET_SERVICE_TASK_DOWN_OOTEMPLATE)) {
	 			String tempFolder = request.getParameter(BookletsConstants.DOSSIER_TEMP_FOLDER);
	 			IDossierDAO dossierDao = new DossierDAOHibImpl();
	 			String templateFileName = dossierDao.getPresentationTemplateFileName(tempFolder);
	 			InputStream templateIs = dossierDao.getPresentationTemplateContent(tempFolder);
	 			byte[] templateByts = GeneralUtilities.getByteArrayFromInputStream(templateIs);
	 			response.setHeader("Content-Disposition","attachment; filename=\"" + templateFileName + "\";");
	 			response.setContentLength(templateByts.length);
	 			out.write(templateByts);
	 			out.flush();
	            return;
	 			
	 		} else if(task.equalsIgnoreCase(BookletsConstants.BOOKLET_SERVICE_TASK_DOWN_WORKFLOW_DEFINITION)) {
	 			String tempFolder = request.getParameter(BookletsConstants.DOSSIER_TEMP_FOLDER);
	 			IDossierDAO dossierDao = new DossierDAOHibImpl();
	 			String workDefName = dossierDao.getProcessDefinitionFileName(tempFolder);
	 			InputStream workIs = dossierDao.getProcessDefinitionContent(tempFolder);
	 			byte[] workByts = GeneralUtilities.getByteArrayFromInputStream(workIs);
	 			response.setHeader("Content-Disposition","attachment; filename=\"" + workDefName + "\";");
	 			response.setContentLength(workByts.length);
	 			out.write(workByts);
	 			out.flush();
	            return;
	 		}
	 		
	 	} catch(Exception e) {
	 		logger.error("Exception during execution of task " + task, e);
	 	}
	 }
	
}
