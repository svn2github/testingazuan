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
import it.eng.spagobi.bo.Domain;
import it.eng.spagobi.bo.Engine;
import it.eng.spagobi.bo.ExecutionController;
import it.eng.spagobi.bo.LowFunctionality;
import it.eng.spagobi.bo.dao.DAOFactory;
import it.eng.spagobi.bo.dao.IBIObjectDAO;
import it.eng.spagobi.bo.dao.IDomainDAO;
import it.eng.spagobi.bo.dao.IEngineDAO;
import it.eng.spagobi.bo.dao.ILowFunctionalityDAO;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.utilities.ExecutionProxy;
import it.eng.spagobi.utilities.SpagoBITracer;
import it.eng.spagobi.utilities.UploadedFile;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class SaveToPersonalFolderServlet extends HttpServlet{
	
	public void init(ServletConfig config) throws ServletException {
        super.init(config);
     } 
	
	public void service(HttpServletRequest request, HttpServletResponse response) {
		
		final String OK = "30";
		final String ERROR = "40";
		final String NAMENOTFOUND = "80";
		final String LABELNOTFOUND = "70";
		
		String retCode = "";
		try{
			
			// GET PARAMETER
			String objLabel = "";
			String namenewdoc = "";
			String descrnewdoc = "";
			String labelnewdoc = "";

			String queryStr = "";
			Enumeration parNames = request.getParameterNames();
			while(parNames.hasMoreElements()) {
				String parName = (String)parNames.nextElement();
				if(parName.equals("objlabel")){
					objLabel = request.getParameter("objlabel");
				} else if(parName.equals("namenewdoc")) {
					namenewdoc = request.getParameter("namenewdoc");
				} else if(parName.equals("descrnewdoc")) {
					descrnewdoc = request.getParameter("descrnewdoc");
				} else if(parName.equals("labelnewdoc")) {
					labelnewdoc = request.getParameter("labelnewdoc");
				} else {
					String value = request.getParameter(parName);
					queryStr += parName + "=" + value + "&";
				}
			}
			
			if(namenewdoc.equals("")) {
				retCode = NAMENOTFOUND;
				throw new Exception("Document Name not found");
			}
			if(labelnewdoc.equals("")) {
				retCode = LABELNOTFOUND;
				throw new Exception("Document Label not found");
			}
			
			
			String returnedContentType = "";
			String fileextension = "";
			byte[] documentBytes = null;
			IBIObjectDAO biobjdao = DAOFactory.getBIObjectDAO();
			BIObject biobj = biobjdao.loadBIObjectByLabel(objLabel);
			// create the execution controller 
			ExecutionController execCtrl = new ExecutionController();
			execCtrl.setBiObject(biobj);
			// fill parameters 
			execCtrl.refreshParameters(biobj, queryStr);
			// exec the document only if all its parameter are filled
			if(execCtrl.directExecution()) {
				ExecutionProxy proxy = new ExecutionProxy();
				proxy.setBiObject(biobj);
				HttpSession httpSession = request.getSession();
	            IEngUserProfile profile = (IEngUserProfile)httpSession.getAttribute(IEngUserProfile.ENG_USER_PROFILE);   	
				documentBytes = proxy.exec(profile);
				returnedContentType = proxy.getReturnedContentType();
				fileextension = proxy.getFileExtensionFromContType(returnedContentType);
			}

		  
			// SAVE NEW DOCUMENT
			// recover office document sbidomains
			IDomainDAO domainDAO = DAOFactory.getDomainDAO();
			Domain officeDocDom = domainDAO.loadDomainByCodeAndValue("BIOBJ_TYPE", "OFFICE_DOC");
			// recover development sbidomains
			Domain relDom = domainDAO.loadDomainByCodeAndValue("STATE", "REL");
			// recover engine
			IEngineDAO engineDAO = DAOFactory.getEngineDAO();
			List engines = engineDAO.loadAllEnginesForBIObjectType(officeDocDom.getValueCd());
			if(engines.isEmpty()) {
				throw new Exception("No suitable engines for the new document");
			}
			Engine engine = (Engine)engines.get(0);
			// load the template
			UploadedFile uploadedFile = new UploadedFile();
			uploadedFile.setFieldNameInForm("document");
			uploadedFile.setFileName("document" + fileextension);
			uploadedFile.setSizeInBytes(documentBytes.length);
			uploadedFile.setFileContent(documentBytes);
			
			
			// load user root functionality 
			HttpSession httpSession = request.getSession();
            IEngUserProfile profile = (IEngUserProfile)httpSession.getAttribute(IEngUserProfile.ENG_USER_PROFILE);  
			String username = (String)profile.getUserUniqueIdentifier();
			ILowFunctionalityDAO functdao = DAOFactory.getLowFunctionalityDAO();
			LowFunctionality funct = functdao.loadLowFunctionalityByPath("/"+username, false);
			Integer functId = funct.getId();
			List storeInFunctionalities = new ArrayList();
			storeInFunctionalities.add(functId);
			// create biobject		
			BIObject newbiobj = new BIObject();
			newbiobj.setName(namenewdoc);
			newbiobj.setDescription(descrnewdoc);
			newbiobj.setLabel(labelnewdoc);
			newbiobj.setEncrypt(new Integer(0));
			newbiobj.setEngine(engine);
			newbiobj.setRelName("");
			newbiobj.setBiObjectTypeCode(officeDocDom.getValueCd());
			newbiobj.setBiObjectTypeID(officeDocDom.getValueId());
			newbiobj.setStateCode(relDom.getValueCd());
			newbiobj.setStateID(relDom.getValueId());
			newbiobj.setVisible(new Integer(0));
			newbiobj.setTemplate(uploadedFile);
			newbiobj.setFunctionalities(storeInFunctionalities);
			IBIObjectDAO objectDAO = DAOFactory.getBIObjectDAO();
			// check if the object already exists and then insert it
			BIObject biobjexist = objectDAO.loadBIObjectByLabel(labelnewdoc);
			if(biobjexist==null){
				objectDAO.insertBIObject(newbiobj);
			} else {
				newbiobj.setId(biobjexist.getId());
				objectDAO.modifyBIObject(newbiobj);
			}
			retCode = OK;
		} catch (Exception e) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), 
					            "service", "Error while executing and saving object ", e);
			if(retCode.equals("")) {
				retCode = ERROR;
			}
		} finally {			
			try{
				response.getOutputStream().write(retCode.getBytes());
	        	response.getOutputStream().flush();	
			} catch (Exception ex) {
				SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), 
						            "service", "Error while sending response to client", ex);
			}
		}
	}
		
	
}	

