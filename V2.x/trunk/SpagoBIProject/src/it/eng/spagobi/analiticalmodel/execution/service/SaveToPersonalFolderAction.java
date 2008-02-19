package it.eng.spagobi.analiticalmodel.execution.service;


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
import it.eng.spagobi.analiticalmodel.document.dao.BIObjectDAOHibImpl;
import it.eng.spagobi.analiticalmodel.document.dao.IBIObjectDAO;
import it.eng.spagobi.analiticalmodel.document.handlers.ExecutionController;
import it.eng.spagobi.analiticalmodel.functionalitytree.bo.LowFunctionality;
import it.eng.spagobi.analiticalmodel.functionalitytree.dao.ILowFunctionalityDAO;
import it.eng.spagobi.analiticalmodel.functionalitytree.dao.LowFunctionalityDAOHibImpl;
import it.eng.spagobi.commons.bo.Domain;
import it.eng.spagobi.commons.bo.UserProfile;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.dao.IDomainDAO;
import it.eng.spagobi.commons.utilities.ExecutionProxy;
import it.eng.spagobi.commons.utilities.SpagoBITracer;
import it.eng.spagobi.engines.config.bo.Engine;
import it.eng.spagobi.engines.config.dao.IEngineDAO;
import it.eng.spagobi.engines.officedocument.GetOfficeContentAction;
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

import antlr.collections.impl.Vector;


public class SaveToPersonalFolderAction extends AbstractHttpAction{

	private static transient Logger logger=Logger.getLogger(SaveToPersonalFolderAction.class);

	public void service(SourceBean request, SourceBean responseSb) {

		logger.debug("IN");


		final String OK = "30";
		final String ERROR = "40";
		final String ALREADYPRESENT="100";

		String retCode = "";

		HttpServletResponse response = getHttpResponse();

		try{

			// GET PARAMETERS
			String queryStr = "";
			String userId="";
			String document="";

			List list=request.getContainedAttributes();


			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				SourceBeanAttribute object = (SourceBeanAttribute) iterator.next();
				String parName=object.getKey();
				if(parName.equals("userId")) {
					userId = (String)request.getAttribute("userid");
				} 
				else if(parName.equals("document")) {
					document = (String)request.getAttribute("document");
				} 
				else {
					String value = (String)request.getAttribute(parName);
					queryStr += parName + "=" + value + "&";
				}
			}

			if(userId=="" || userId==null){
				logger.error("User Id not found");
				throw new Exception("User Id not found");
			}

			if(document=="" || document==null){
				logger.error("Document has no id associated");
				throw new Exception("Document Id not found");
			}


			logger.debug("Access to the database");
			LowFunctionalityDAOHibImpl lowFunctionalityDAOHibImpl=new LowFunctionalityDAOHibImpl();
			boolean exists=lowFunctionalityDAOHibImpl.checkUserRootExists(userId);

			LowFunctionality lf=new LowFunctionality();

			if(exists){
				logger.debug("Personal Folder found");	
				lf=lowFunctionalityDAOHibImpl.loadLowFunctionalityByPath("/"+userId, false);
			}
			else{
				logger.error("Personal Folder doesn't exists");
				throw new Exception("Personal Folder doesn't exists");
			}


			Integer idFunction=lf.getId();
			if(idFunction==null){
				logger.error("No function associated");
				throw new Exception("No function associated");
			}

			// Load document
			BIObject biObject;
			BIObjectDAOHibImpl biObjectDAOHibImpl=new BIObjectDAOHibImpl();
			biObject=biObjectDAOHibImpl.loadBIObjectById(Integer.valueOf(document));
			if(biObject==null){
				logger.error("Could not load document");
				throw new Exception("Could not load document");
			}

			List funcs=biObject.getFunctionalities();
			if(funcs==null){
				funcs=new ArrayList();
			}
			if(!funcs.contains(idFunction)){
				funcs.add(idFunction);
				biObject.setFunctionalities(funcs);
				biObjectDAOHibImpl.modifyBIObject(biObject);
				logger.debug("Object modified");
				retCode = OK;
			}
			else{
				logger.warn("the object is already associated to the functionality");
				retCode=ALREADYPRESENT;
			}
			
		} catch (Exception e) {
			logger.error("Error while modifying object");
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


