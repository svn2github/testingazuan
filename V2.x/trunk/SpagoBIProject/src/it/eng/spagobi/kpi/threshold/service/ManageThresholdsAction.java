/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2009 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.kpi.threshold.service;

import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.analiticalmodel.document.x.AbstractSpagoBIAction;
import it.eng.spagobi.chiron.serializer.SerializerFactory;
import it.eng.spagobi.commons.bo.Domain;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.kpi.threshold.bo.Threshold;
import it.eng.spagobi.kpi.threshold.dao.IThresholdDAO;
import it.eng.spagobi.utilities.exceptions.SpagoBIServiceException;
import it.eng.spagobi.utilities.service.JSONAcknowledge;
import it.eng.spagobi.utilities.service.JSONSuccess;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

	public class ManageThresholdsAction extends AbstractSpagoBIAction{
		// logger component
	private static Logger logger = Logger.getLogger(ManageThresholdsAction.class);
	private final String MESSAGE_DET = "MESSAGE_DET";
	// type of service
	private final String THRESHOLDS_LIST = "THRESHOLDS_LIST";
	private final String THRESHOLD_INSERT = "THRESHOLD_INSERT";
	private final String THRESHOLD_DELETE = "THRESHOLD_DELETE";
	
	private final String THRESHOLD_DOMAIN_TYPE = "THRESHOLD_TYPE";
	
	// RES detail
	private final String ID = "id";
	private final String NAME = "name";
	private final String CODE = "code";
	private final String DESCRIPTION = "description";
	private final String NODE_TYPE_CODE = "typeCd";
	
	public static String START = "start";
	public static String LIMIT = "limit";
	public static Integer START_DEFAULT = 0;
	public static Integer LIMIT_DEFAULT = 16;
	
	@Override
	public void doService() {
		logger.debug("IN");
		IThresholdDAO thrDao;
		try {
			thrDao = DAOFactory.getThresholdDAO();
		} catch (EMFUserError e1) {
			logger.error(e1.getMessage(), e1);
			throw new SpagoBIServiceException(SERVICE_NAME,	"Error occurred");
		}
		Locale locale = getLocale();
	
		String serviceType = this.getAttributeAsString(MESSAGE_DET);
		logger.debug("Service type "+serviceType);
		if (serviceType != null && serviceType.equalsIgnoreCase(THRESHOLDS_LIST)) {
			
			try {		
				
				Integer start = getAttributeAsInteger( START );
				Integer limit = getAttributeAsInteger( LIMIT );
				
				if(start==null){
					start = START_DEFAULT;
				}
				if(limit==null){
					limit = LIMIT_DEFAULT;
				}
	
				Integer totalItemsNum = thrDao.countThresholds();
				List thresholds = thrDao.loadPagedThresholdList(start,limit);
				logger.debug("Loaded thresholds list");
				JSONArray resourcesJSON = (JSONArray) SerializerFactory.getSerializer("application/json").serialize(thresholds, locale);
				JSONObject resourcesResponseJSON = createJSONResponseResources(resourcesJSON, totalItemsNum);
	
				writeBackToClient(new JSONSuccess(resourcesResponseJSON));
	
			} catch (Throwable e) {
				logger.error("Exception occurred while retrieving thresholds", e);
				throw new SpagoBIServiceException(SERVICE_NAME,
						"Exception occurred while retrieving thresholds", e);
			}
		} else if (serviceType != null	&& serviceType.equalsIgnoreCase(THRESHOLD_INSERT)) {
			String id = getAttributeAsString(ID);
			String code = getAttributeAsString(CODE);
			String name = getAttributeAsString(NAME);
			String description = getAttributeAsString(DESCRIPTION);
			String typeCD = getAttributeAsString(NODE_TYPE_CODE);		
			
			List<Domain> domains = (List<Domain>)getSessionContainer().getAttribute("nodeTypesList");
			
		    HashMap<String, Integer> domainIds = new HashMap<String, Integer> ();
		    if(domains != null){
			    for(int i=0; i< domains.size(); i++){
			    	domainIds.put(domains.get(i).getValueCd(), domains.get(i).getValueId());
			    }
		    }
		    
		    Integer typeID = domainIds.get(typeCD);
		    if(typeID == null){
		    	logger.error("Threshold type CD does not exist");
		    	throw new SpagoBIServiceException(SERVICE_NAME,	"Threshold Type ID is undefined");
		    }
	
			if (name != null && typeID != null && code != null) {
				Threshold thr = new Threshold();
				thr.setName(name);
				thr.setThresholdTypeCode(typeCD);
				thr.setThresholdTypeId(typeID);
				thr.setCode(code);
				
				if(description != null){
					thr.setDescription(description);
				}			
				
				try {
					if(id != null && !id.equals("") && !id.equals("0")){							
						thr.setId(Integer.valueOf(id));
						thrDao.modifyThreshold(thr);
						logger.debug("threshold "+id+" updated");
						JSONObject attributesResponseSuccessJSON = new JSONObject();
						attributesResponseSuccessJSON.put("success", true);
						attributesResponseSuccessJSON.put("responseText", "Operation succeded");
						attributesResponseSuccessJSON.put("id", id);
						writeBackToClient( new JSONSuccess(attributesResponseSuccessJSON) );
					}else{
						Integer resourceID = thrDao.insertThreshold(thr);
						logger.debug("New threshold inserted");
						JSONObject attributesResponseSuccessJSON = new JSONObject();
						attributesResponseSuccessJSON.put("success", true);
						attributesResponseSuccessJSON.put("responseText", "Operation succeded");
						attributesResponseSuccessJSON.put("id", resourceID);
						writeBackToClient( new JSONSuccess(attributesResponseSuccessJSON) );
					}
	
				} catch (Throwable e) {
					logger.error(e.getMessage(), e);
					throw new SpagoBIServiceException(SERVICE_NAME,
							"Exception occurred while saving new threshold", e);
				}
								
			}else{
				logger.error("Resource name, code or type are missing");
				throw new SpagoBIServiceException(SERVICE_NAME,	"Please fill threshold name, code and type");
			}
		} else if (serviceType != null	&& serviceType.equalsIgnoreCase(THRESHOLD_DELETE)) {
			Integer id = getAttributeAsInteger(ID);
			try {
				thrDao.deleteThreshold(id);
				logger.debug("Resource deleted");
				writeBackToClient( new JSONAcknowledge("Operation succeded") );
			} catch (Throwable e) {
				logger.error("Exception occurred while retrieving resource to delete", e);
				throw new SpagoBIServiceException(SERVICE_NAME,
						"Exception occurred while retrieving resource to delete", e);
			}
		}else if(serviceType == null){
			try {
				List nodeTypes = DAOFactory.getDomainDAO().loadListDomainsByType(THRESHOLD_DOMAIN_TYPE);
				getSessionContainer().setAttribute("nodeTypesList", nodeTypes);
				
			} catch (EMFUserError e) {
				logger.error(e.getMessage(), e);
				throw new SpagoBIServiceException(SERVICE_NAME,
						"Exception retrieving resources types", e);
			}
		}
		logger.debug("OUT");
	}
	
	/**
	 * Creates a json array with children users informations
	 * 
	 * @param rows
	 * @return
	 * @throws JSONException
	 */
	private JSONObject createJSONResponseResources(JSONArray rows, Integer totalResNumber)
			throws JSONException {
		JSONObject results;
	
		results = new JSONObject();
		results.put("total", totalResNumber);
		results.put("title", "Thresholds");
		results.put("rows", rows);
		return results;
	}
}
