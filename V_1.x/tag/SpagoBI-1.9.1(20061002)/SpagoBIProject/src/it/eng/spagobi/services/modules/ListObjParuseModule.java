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
package it.eng.spagobi.services.modules;

/**
 * Implements a module which handles ObjParuse objects relevant to a BIObjectParameter, 
 * where a ObjParuse is a correlation between the parameter with another parameter 
 * of the same document: has methods for ObjParuse load, detail, modify/insertion and deleting operations. 
 * The <code>service</code> method has  a switch for all these operations, differentiated the ones 
 * from the others by a <code>message</code> String.
 * 
 * @author zerbetto
 */

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.dispatching.module.AbstractModule;
import it.eng.spago.error.EMFErrorHandler;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFInternalError;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.bo.BIObjectParameter;
import it.eng.spagobi.bo.ModalitiesValue;
import it.eng.spagobi.bo.ObjParuse;
import it.eng.spagobi.bo.ParameterUse;
import it.eng.spagobi.bo.dao.DAOFactory;
import it.eng.spagobi.constants.AdmintoolsConstants;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class ListObjParuseModule extends AbstractModule {

	private EMFErrorHandler errorHandler;
	
	protected SessionContainer session = null;
	
	public void init(SourceBean config) {
	}
	
	public void service(SourceBean request, SourceBean response) throws Exception {
		
		RequestContainer requestContainer = this.getRequestContainer();	
		session = requestContainer.getSessionContainer();
		
		String message = (String) request.getAttribute("MESSAGEDET");
		SpagoBITracer.debug(AdmintoolsConstants.NAME_MODULE, 
				            "ListObjParuseModule","service",
				            "begin of ListObjParuseModule modify/visualization service with message =" +message);
		
		errorHandler = getErrorHandler();
		
		try {
			if (message == null) {
				EMFUserError userError = new EMFUserError(EMFErrorSeverity.ERROR, 101);
				SpagoBITracer.debug(AdmintoolsConstants.NAME_MODULE, "ListObjParuseModule", "service", "The message parameter is null");
				throw userError;
			} 
			if (message.trim().equalsIgnoreCase(AdmintoolsConstants.DETAIL_SELECT)) {
				getDetailObjParuses(request, response);
			} 	else if (message.trim().equalsIgnoreCase(AdmintoolsConstants.DETAIL_MOD)) {
				modObjParuses(request, response);
			}	else if (message.trim().equalsIgnoreCase("EXIT_FROM_MODULE")) {
				exit(request, response);
			}
		} catch (EMFUserError eex) {
			errorHandler.addError(eex);
			return;
		} catch (Exception ex) {
			EMFInternalError internalError = new EMFInternalError(EMFErrorSeverity.ERROR, ex);
			errorHandler.addError(internalError);
			return;
		}
	}

	private void getDetailObjParuses(SourceBean request, SourceBean response) throws EMFUserError {
		try {
			
			BIObjectParameter objParameter = (BIObjectParameter) session.getAttribute("LookupBIObjectParameter");
			Integer objParId = objParameter.getId();
			List objParuses = DAOFactory.getObjParuseDAO().loadObjParuses(objParId);
			Integer objId = objParameter.getBiObjectID();
			List allObjParameters = DAOFactory.getBIObjectParameterDAO().loadBIObjectParametersById(objId);
			List otherObjParameters = new ArrayList();
			// otherObjParameters must contain all the BIObjectParameter of the document apart from the 
			// current BIObjectParameter
			Iterator allObjParametersIt = allObjParameters.iterator();
			while (allObjParametersIt.hasNext()) {
				BIObjectParameter aBIObjectParameter = (BIObjectParameter) allObjParametersIt.next();
				if (!aBIObjectParameter.getId().equals(objParId)) otherObjParameters.add(aBIObjectParameter);
			}
			Integer parId = objParameter.getParID();
			List allParuses = DAOFactory.getParameterUseDAO().loadParametersUseByParId(parId);
			List paruses = new ArrayList();
			// paruses must contain only parameter uses associated to query lov
			Iterator allParusesIt = allParuses.iterator();
			while (allParusesIt.hasNext()) {
				ParameterUse paruse = (ParameterUse) allParusesIt.next();
				if (paruse.getManualInput().intValue() == 0) {
					// manual input modality use cannot be considered;
					// only the modalities associated to query lov and 
					// that have not COMBOBOX selection type can be considered
					Integer lovId = paruse.getIdLov();
					ModalitiesValue lov = DAOFactory.getModalitiesValueDAO().loadModalitiesValueByID(lovId);
					String selectionType = paruse.getSelectionType();
					if (lov.getITypeCd().equalsIgnoreCase("QUERY") 
							&& !"COMBOBOX".equalsIgnoreCase(selectionType)) paruses.add(paruse);
				}
			}
			response.setAttribute("objParameter", objParameter);
			response.setAttribute("allParuses", paruses);
			response.setAttribute("objParuses", objParuses);
			response.setAttribute("otherObjParameters", otherObjParameters);
			
		} catch (Exception ex) {
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "ListObjParuseModule","getDetailObjParuses","Cannot fill response container", ex  );
			HashMap params = new HashMap();
			params.put(AdmintoolsConstants.PAGE, ListEnginesModule.MODULE_PAGE);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 1047, new Vector(), params);
		}
	}
	
	private void modObjParuses(SourceBean request, SourceBean response) throws EMFUserError, SourceBeanException {
		try {
			String objParIdStr = (String) request.getAttribute("obj_par_id");
			Integer objParId = new Integer (objParIdStr);
			// load the previous ObjParuse objects
			List oldObjParuses = DAOFactory.getObjParuseDAO().loadObjParuses(objParId);
			if (oldObjParuses == null) oldObjParuses = new ArrayList();
			List oldParusesId = new ArrayList();
			Iterator it = oldObjParuses.iterator();
			while (it.hasNext()) {
				ObjParuse objParuse = (ObjParuse) it.next();
				oldParusesId.add(objParuse.getParuseId().toString());
			}
			// load the ids of the ParameterUse objects for the new (or modified) ObjParuse objects
			List newParusesId = request.getAttributeAsList("paruse_id");
			if (newParusesId == null) newParusesId = new ArrayList();
			
			// delete the ObjParuse objects that were unchecked
			deleteUncheckedObjParuses(oldParusesId, newParusesId, objParId);
			
			// insert or modify the checked ObjParuse
			saveCheckedObjParuses(newParusesId, request, objParId);
			
			response.setAttribute("loopback", "true");
			
			session.setAttribute("RETURN_FROM_MODULE", "ListObjParuseModule");
			
		} catch (Exception ex) {
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "ListObjParuseModule","modObjParuses","Cannot fill response container", ex  );
			HashMap params = new HashMap();
			params.put(AdmintoolsConstants.PAGE, ListEnginesModule.MODULE_PAGE);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 1048, new Vector(), params);
		}
		
	}
	
	
	private void exit(SourceBean request, SourceBean response) throws EMFUserError, SourceBeanException {
		session.setAttribute("RETURN_FROM_MODULE", "ListObjParuseModule");
		response.setAttribute("loopback", "true");
	}

	private void saveCheckedObjParuses(List newParusesId, SourceBean request, Integer objParId) throws EMFUserError {
		Iterator it = newParusesId.iterator();
		while (it.hasNext()) {
			String paruseIdStr = (String) it.next();
			Integer paruseId = new Integer (paruseIdStr);
			ObjParuse newObjParuse = recoverObjParuseDetail(objParId, paruseId, request);
			ObjParuse objParuse = DAOFactory.getObjParuseDAO().loadObjParuse(objParId, paruseId);
			if (objParuse == null) {
				DAOFactory.getObjParuseDAO().insertObjParuse(newObjParuse);
			} else {
				DAOFactory.getObjParuseDAO().modifyObjParuse(newObjParuse);
			}
		}
	}

	private ObjParuse recoverObjParuseDetail(Integer objParId, Integer paruseId, SourceBean request) {
		String filterOperation = (String) request.getAttribute(SpagoBIConstants.TYPE_FILTER + "_" + paruseId);
		String filterColumn = (String) request.getAttribute(SpagoBIConstants.COLUMN_FILTER + "_" + paruseId);
		String objParFatherIdStr = (String) request.getAttribute("OBJ_PAR_FATHER_ID" + "_" + paruseId);
		Integer objParFatherId = new Integer(objParFatherIdStr);
		
		ObjParuse toReturn = new ObjParuse();
		toReturn.setObjParId(objParId);
		toReturn.setParuseId(paruseId);
		toReturn.setFilterColumn(filterColumn);
		toReturn.setFilterOperation(filterOperation);
		toReturn.setObjParFatherId(objParFatherId);
		
		return toReturn;
	}

	private void deleteUncheckedObjParuses(List oldParusesId, List newParusesId, Integer objParId) throws EMFUserError {
		Iterator it = oldParusesId.iterator();
		while (it.hasNext()) {
			String oldParuseId = (String) it.next();
			// if the paruse id was still checked the ObjParuse is not deleted
			if (newParusesId.contains(oldParuseId)) continue;
			// if the paruse id was unchecked the ObjParuse is deleted
			ObjParuse objParuseToDelete = new ObjParuse();
			objParuseToDelete.setObjParId(objParId);
			objParuseToDelete.setParuseId(new Integer(oldParuseId));
			DAOFactory.getObjParuseDAO().eraseObjParuse(objParuseToDelete);
		}
	}
}
