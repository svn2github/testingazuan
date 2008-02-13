package it.eng.spagobi.services;


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


import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanAttribute;
import it.eng.spago.dispatching.action.AbstractHttpAction;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.analiticalmodel.document.bo.BIObject;
import it.eng.spagobi.analiticalmodel.document.bo.ObjTemplate;
import it.eng.spagobi.analiticalmodel.document.dao.IBIObjectDAO;
import it.eng.spagobi.analiticalmodel.document.handlers.ExecutionController;
import it.eng.spagobi.analiticalmodel.document.service.GetOfficeContentAction;
import it.eng.spagobi.analiticalmodel.functionalitytree.bo.LowFunctionality;
import it.eng.spagobi.analiticalmodel.functionalitytree.dao.ILowFunctionalityDAO;
import it.eng.spagobi.commons.bo.Domain;
import it.eng.spagobi.commons.bo.UserProfile;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.dao.IDomainDAO;
import it.eng.spagobi.commons.utilities.ExecutionProxy;
import it.eng.spagobi.commons.utilities.SpagoBITracer;
import it.eng.spagobi.engines.config.bo.Engine;
import it.eng.spagobi.engines.config.dao.IEngineDAO;
import it.eng.spagobi.services.security.bo.SpagoBIUserProfile;
import it.eng.spagobi.services.security.exceptions.SecurityException;
import it.eng.spagobi.services.security.service.ISecurityServiceSupplier;
import it.eng.spagobi.services.security.service.SecurityServiceSupplierFactory;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;


public class SaveToPersonalFolderAction extends AbstractHttpAction{

    private static transient Logger logger=Logger.getLogger(SaveToPersonalFolderAction.class);

	public void service(SourceBean request, SourceBean responseSb) {

		 logger.debug("IN");
		
		final String OK = "30";
		final String ERROR = "40";
		final String NAMENOTFOUND = "80";
		final String LABELNOTFOUND = "70";

		
		String retCode = "";
		
		HttpServletResponse response = getHttpResponse();
		
		try{

			// GET PARAMETERS
			String objLabel = "";
			String namenewdoc = "";
			String descrnewdoc = "";
			String labelnewdoc = "";
			String queryStr = "";
			String userId="";
			
			List list=request.getContainedAttributes();
			
			
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				SourceBeanAttribute object = (SourceBeanAttribute) iterator.next();
				String parName=object.getKey();
								if(parName.equals("objlabel")){
					objLabel = (String)request.getAttribute("objlabel");
				} else if(parName.equals("namenewdoc")) {
					namenewdoc = (String)request.getAttribute("namenewdoc");
				} else if(parName.equals("descrnewdoc")) {
					descrnewdoc = (String)request.getAttribute("descrnewdoc");
				} else if(parName.equals("labelnewdoc")) {
					labelnewdoc = (String)request.getAttribute("labelnewdoc");
				} else if(parName.equals("userId")) {
					userId = (String)request.getAttribute("userid");
				} 
				else {
					String value = (String)request.getAttribute(parName);
					queryStr += parName + "=" + value + "&";
				}
			}
			// CHECK PARAMETERS
			if(namenewdoc.equals("")) {
				retCode = NAMENOTFOUND;
				logger.error("Document name not found");
				throw new Exception("Document Name not found");
			}
			if(labelnewdoc.equals("")) {
				retCode = LABELNOTFOUND;
				logger.error("Document label not found");
				throw new Exception("Document Label not found");
			}
			// EXECUTE BIOBJECT
			logger.debug("Starting object execution");
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
				//Principal principal = request.getUserPrincipal();
				IEngUserProfile profile = null;
				ISecurityServiceSupplier supplier=SecurityServiceSupplierFactory.createISecurityServiceSupplier();
				try {
					SpagoBIUserProfile user= supplier.createUserProfile(userId);
					profile=new UserProfile(user);
				} catch (Exception e) {
					logger.error("Security error");
					throw new SecurityException();
				}    	
				documentBytes = proxy.exec(profile);
				returnedContentType = proxy.getReturnedContentType();
				fileextension = proxy.getFileExtensionFromContType(returnedContentType);
				logger.debug("Document executed succesfully");
			}
			// SAVE NEW DOCUMENT
			// recover office document sbidomains
			logger.debug("Start saving new document");
			IDomainDAO domainDAO = DAOFactory.getDomainDAO();
			Domain officeDocDom = domainDAO.loadDomainByCodeAndValue("BIOBJ_TYPE", "OFFICE_DOC");
			// recover development sbidomains
			Domain relDom = domainDAO.loadDomainByCodeAndValue("STATE", "REL");
			// recover engine
			IEngineDAO engineDAO = DAOFactory.getEngineDAO();
			List engines = engineDAO.loadAllEnginesForBIObjectType(officeDocDom.getValueCd());
			if(engines.isEmpty()) {
				logger.error("No suitable engines for the new document");
				throw new Exception("No suitable engines for the new document");
			}
			Engine engine = (Engine)engines.get(0);
			// create the object template
			ObjTemplate objTemp = new ObjTemplate();
			objTemp.setActive(new Boolean(true));
			objTemp.setContent(documentBytes);
			objTemp.setName("document" + fileextension);
			// load user root functionality 
			IEngUserProfile profile = null;
			ISecurityServiceSupplier supplier=SecurityServiceSupplierFactory.createISecurityServiceSupplier();
			try {
				SpagoBIUserProfile user= supplier.createUserProfile(userId);
				profile=new UserProfile(user);
			} catch (Exception e) {
				logger.error("No suitable engines for the new document");
				throw new SecurityException();
			}   
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
			newbiobj.setDataSourceId(new Integer(1));
			newbiobj.setVisible(new Integer(0));
			newbiobj.setFunctionalities(storeInFunctionalities);
			IBIObjectDAO objectDAO = DAOFactory.getBIObjectDAO();
			// check if the object already exists and then insert it
			BIObject biobjexist = objectDAO.loadBIObjectByLabel(labelnewdoc);
			if(biobjexist==null){
				logger.debug("Inserting new Object");
				objectDAO.insertBIObject(newbiobj, objTemp);
			} else {
				logger.debug("Modifying already existing object");
				newbiobj.setId(biobjexist.getId());
				objectDAO.modifyBIObject(newbiobj, objTemp);
			}
			retCode = OK;
		} catch (Exception e) {
			logger.error("Error while executing and saving object");
			if(retCode.equals("")) {
				retCode = ERROR;
			}
		} finally {			
			try{
				
				response.getOutputStream().write(retCode.getBytes());
				response.getOutputStream().flush();	
				
			} catch (Exception ex) {
				logger.error("Error while sending response to client");
			}
		}		
	}


}	


