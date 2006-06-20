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
package it.eng.spagobi.services;

import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.bo.BIObject;
import it.eng.spagobi.bo.dao.DAOFactory;
import it.eng.spagobi.bo.dao.IBIObjectCMSDAO;
import it.eng.spagobi.bo.dao.IBIObjectDAO;
import it.eng.spagobi.security.AnonymousCMSUserProfile;
import it.eng.spagobi.utilities.GeneralUtilities;
import it.eng.spagobi.utilities.JCRUtilities;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A servlet used to manage and control the content repository
 */
public class ContentRepositoryServlet extends HttpServlet{
	
	/**
	 * Init method definition
	 */
	public void init(ServletConfig config) throws ServletException {
        super.init(config);
     } // public void init(ServletConfig config) throws ServletException
    /**
     * Service method definition
     * 
     * @param request The http servlet request
     * @param response The http servlet response
     * @throws IOException If any exception occurred
     */
	public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
	 	
	 	try{
	 		String operation = request.getParameter("operation");
	 		if(operation != null) {
	 			if(operation.equalsIgnoreCase("getContent")) {
	 				String jcrPath = request.getParameter("jcrPath");
	 		 		if(jcrPath.endsWith("/template")) {
	 		 			int lastslash = jcrPath.lastIndexOf("/");
	 		 			jcrPath = jcrPath.substring(0, lastslash);
	 		 		}
	 		 		IBIObjectCMSDAO biObjCMSDAO = DAOFactory.getBIObjectCMSDAO();
	 		 		InputStream jcrContentStream = biObjCMSDAO.getTemplate(jcrPath);
		 			byte[] jcrContent = GeneralUtilities.getByteArrayFromInputStream(jcrContentStream);
		 			response.setContentLength(jcrContent.length);
				 	response.getOutputStream().write(jcrContent);
				 	response.getOutputStream().flush();
				 	return;
	 			}
	 			if(operation.equalsIgnoreCase("getSubObjectContent")) {
	 				String jcrPath = request.getParameter("jcrPath");
	 		 		if(jcrPath.endsWith("/template")) {
	 		 			int lastslash = jcrPath.lastIndexOf("/");
	 		 			jcrPath = jcrPath.substring(0, lastslash);
	 		 		}
	 		 		String nameSubObj = request.getParameter("nameSubObject");
	 		 		String user = request.getParameter("user");
	 		 		IBIObjectCMSDAO biObjCMSDAO = DAOFactory.getBIObjectCMSDAO();
	 		 		IEngUserProfile profile = new AnonymousCMSUserProfile(user);
	 		 		InputStream jcrContentStream = biObjCMSDAO.getSubObject(jcrPath, nameSubObj);
	 		 		byte[] jcrContent = GeneralUtilities.getByteArrayFromInputStream(jcrContentStream);
		 			response.setContentLength(jcrContent.length);
				 	response.getOutputStream().write(jcrContent);
				 	response.getOutputStream().flush();
				 	return;
	 			}
	 			if(operation.equalsIgnoreCase("saveSubObject")) {
	 				String jcrPath = request.getParameter("jcrPath");
	 		 		if(jcrPath.endsWith("/template")) {
	 		 			int lastslash = jcrPath.lastIndexOf("/");
	 		 			jcrPath = jcrPath.substring(0, lastslash);
	 		 		} 
	 		 		String nameSubObj = request.getParameter("nameSubObject");
	 		 		String user = request.getParameter("user");
	 		 		String visibilityStr = request.getParameter("publicVisibility");
	 		 		boolean visibility = false;
	 		 		if(visibilityStr.equalsIgnoreCase("true"))
	 		 			visibility = true;
	 		 		String content = request.getParameter("content");
	 		 		String descr = request.getParameter("description");
	 		 		IBIObjectCMSDAO biObjCMSDAO = DAOFactory.getBIObjectCMSDAO();
	 		 		IEngUserProfile profile = new AnonymousCMSUserProfile(user);
	 		 		biObjCMSDAO.saveSubObject(content.getBytes(), jcrPath, nameSubObj, 
	 		 				                  descr, visibility, profile);
	 		 		return;
	 			}
	 			
	 			
	 		// part for download of the template version
	 		// TODO manage with the operation parameter logic
	 		} else {
	 			String jcrPath = request.getParameter("jcrPath");
		 		String version = request.getParameter("version");
		 		InputStream jcrContentStream = JCRUtilities.getContentByPathAndVersion(jcrPath, version);
	 			byte[] jcrContent = GeneralUtilities.getByteArrayFromInputStream(jcrContentStream);
	 			String fileName = request.getParameter("fileName");
	 			if(fileName==null) {
	 				fileName = "fileRepository";
	 			}
	 			response.setHeader("Content-Disposition","attachment; filename=\"" + fileName + "\";");
	 			response.setContentLength(jcrContent.length);
			 	response.getOutputStream().write(jcrContent);
			 	response.getOutputStream().flush();
			 	return;
	 		}

	 		
	 	}catch(Exception e){
	 		SpagoBITracer.critical("SpagoBI",getClass().getName(),"service","Exception", e);
	 	}
	 }
	
	
	

}
