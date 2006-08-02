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
package it.eng.spagobi.pamphlets;


import it.eng.spago.base.ApplicationContainer;
import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spago.workflow.api.IWorkflowAssignment;
import it.eng.spago.workflow.api.IWorkflowConnection;
import it.eng.spago.workflow.api.IWorkflowEngine;
import it.eng.spagobi.pamphlets.dao.IPamphletsCmsDao;
import it.eng.spagobi.pamphlets.dao.PamphletsCmsDaoImpl;
import it.eng.spagobi.utilities.GeneralUtilities;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class PamphletsImageServlet extends HttpServlet{
	
	public void init(ServletConfig config) throws ServletException {
        super.init(config);
     } 

	public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		OutputStream out = null;
		String task = "";
		try{
	 		
	 		task = (String)request.getParameter("task");		
	 		out = response.getOutputStream();
	 		
	 		if(task.equalsIgnoreCase("getTemplateImage")){
	 			String pathimg = (String)request.getParameter("pathimg");
			 	if(pathimg!=null) {
				 	File imgFile = new File(pathimg);
				 	FileInputStream fis = new FileInputStream(imgFile);
				 	byte[] content = GeneralUtilities.getByteArrayFromInputStream(fis);
				 	out.write(content);
				 	out.flush();
		            fis.close();
		            imgFile.delete();
		            return;
			 	} 
	 		} else if(task.equalsIgnoreCase("downloadFinalDocument")){
	 			String activityKey = request.getParameter("ActivityKey");
		 		ApplicationContainer applicationCont = ApplicationContainer.getInstance();
		 		RequestContainer reqCont = RequestContainer.getRequestContainer();
		 		SessionContainer sessCont = reqCont.getSessionContainer();
		 		IEngUserProfile userProfile = (IEngUserProfile)sessCont.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
		 		IWorkflowEngine wfEngine = (IWorkflowEngine)applicationCont.getAttribute("WfEngine");
		 		IWorkflowConnection wfConnection = wfEngine.getWorkflowConnection();
		 		wfConnection.open((String)userProfile.getUserUniqueIdentifier(), (String)userProfile.getUserAttribute("password"));
		 		IWorkflowAssignment wfAssignment = wfConnection.getWorkflowAssignment(activityKey);
		 		Map context = wfAssignment.getContext();
		 		String pathPamphlet = (String)context.get("PathPamphlet");
		 		// GET NAME OF THE PAMPHLET
		 		IPamphletsCmsDao pampdao = new PamphletsCmsDaoImpl();
		 		String pampName = pampdao.getPamphletName(pathPamphlet);
		 		byte[] finalDocBytes = pampdao.getFinalDocument(pathPamphlet);
			 	response.setHeader("Content-Disposition","attachment; filename=\"" + pampName + ".ppt" + "\";");
	 			response.setContentLength(finalDocBytes.length);
	 			out.write(finalDocBytes);
	 			out.flush();
	            return;
	 		}
	 		
	 	}catch(Exception e){
	 		SpagoBITracer.major("SpagoBI",getClass().getName(),
	 				               "service","Exception during execution of task " + task, e);
	 	}
	 }
	
}
