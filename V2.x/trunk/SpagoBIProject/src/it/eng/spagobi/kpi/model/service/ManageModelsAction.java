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
package it.eng.spagobi.kpi.model.service;

import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.validation.EMFValidationError;
import it.eng.spagobi.analiticalmodel.document.x.AbstractSpagoBIAction;
import it.eng.spagobi.analiticalmodel.document.x.SaveMetadataAction;
import it.eng.spagobi.chiron.serializer.SerializationException;
import it.eng.spagobi.chiron.serializer.SerializerFactory;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.metadata.SbiExtRoles;
import it.eng.spagobi.commons.utilities.messages.MessageBuilder;
import it.eng.spagobi.kpi.model.bo.Model;
import it.eng.spagobi.kpi.model.dao.IModelDAO;
import it.eng.spagobi.profiling.bean.SbiAttribute;
import it.eng.spagobi.profiling.bean.SbiUser;
import it.eng.spagobi.profiling.bo.UserBO;
import it.eng.spagobi.profiling.dao.ISbiUserDAO;
import it.eng.spagobi.utilities.exceptions.SpagoBIServiceException;
import it.eng.spagobi.utilities.service.JSONAcknowledge;
import it.eng.spagobi.utilities.service.JSONSuccess;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.validator.GenericValidator;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ManageModelsAction extends AbstractSpagoBIAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8920524215721282986L;
	// logger component
	private static Logger logger = Logger.getLogger(SaveMetadataAction.class);
	private final String MESSAGE_DET = "MESSAGE_DET";
	// type of service
	private final String MODELS_LIST = "MODELS_LIST";
	private final String MODEL_NODES_LIST = "MODEL_NODES_LIST";
	private final String MODEL_NODES_SAVE = "MODEL_NODES_SAVE";
	private final String MODEL_NODE_DELETE = "MODEL_NODE_DELETE";

	
	private final String MODEL_DOMAIN_TYPE_ROOT = "MODEL_ROOT";
	private final String MODEL_DOMAIN_TYPE_NODE = "MODEL_NODE";

	
	private final String NODES_TO_SAVE = "nodes";



	@Override
	public void doService() {
		logger.debug("IN");
		IModelDAO modelDao;
		try {
			modelDao = DAOFactory.getModelDAO();
		} catch (EMFUserError e1) {
			logger.error(e1.getMessage(), e1);
			throw new SpagoBIServiceException(SERVICE_NAME,	"Error occurred");
		}
		Locale locale = getLocale();

		String serviceType = this.getAttributeAsString(MESSAGE_DET);
		logger.debug("Service type "+serviceType);
		
		if (serviceType != null && serviceType.equalsIgnoreCase(MODELS_LIST)) {
			
			try {				
				List modelRootsList = modelDao.loadModelsRoot();
				
				logger.debug("Loaded models list");
				JSONArray modelsListJSON = (JSONArray) SerializerFactory.getSerializer("application/json").serialize(modelRootsList,locale);
				JSONObject modelsResponseJSON = createJSONResponseModelsList(modelsListJSON, new Integer(6));

				writeBackToClient(new JSONSuccess(modelsResponseJSON));

			} catch (Throwable e) {
				logger.error("Exception occurred while retrieving model tree", e);
				throw new SpagoBIServiceException(SERVICE_NAME,
						"Exception occurred while retrieving model tree", e);
			}
		  }else if (serviceType != null && serviceType.equalsIgnoreCase(MODEL_NODES_LIST)) {
			
			try {				
				String parentId = (String)getAttributeAsString("ID");
				//String parentId = "1";
				Model aModel = modelDao.loadModelWithChildrenById(Integer.parseInt(parentId));
				
				logger.debug("Loaded model tree");
				JSONArray modelChildrenJSON = (JSONArray) SerializerFactory.getSerializer("application/json").serialize(aModel.getChildrenNodes(),	locale);
				//JSONObject treeResponseJSON = createJSONResponseModelTree(modelChildrenJSON, aModel.getName());

				writeBackToClient(new JSONSuccess(modelChildrenJSON));

			} catch (Throwable e) {
				logger.error("Exception occurred while retrieving model tree", e);
				throw new SpagoBIServiceException(SERVICE_NAME,
						"Exception occurred while retrieving model tree", e);
			}
		} else if (serviceType != null	&& serviceType.equalsIgnoreCase(MODEL_NODES_SAVE)) {
			JSONArray nodesToSaveJSON = getAttributeAsJSONArray(NODES_TO_SAVE);
			List<Model> modelNodes = null;
			if(nodesToSaveJSON != null){
				try {
					modelNodes = deserializeNodesJSONArray(nodesToSaveJSON);
					
					//save them
					saveModelNodes(modelNodes);
					
				} catch (JSONException e) {
					logger.error(e.getMessage(), e);
					throw new SpagoBIServiceException(SERVICE_NAME,
							"Exception retrieving model nodes to save", e);
				}
			}
			
		} else if (serviceType != null	&& serviceType.equalsIgnoreCase(MODEL_NODE_DELETE)) {
			//TODO node delete
		}else if(serviceType == null){
			try {
				List nodeTypesNodes = DAOFactory.getDomainDAO().loadListDomainsByType(MODEL_DOMAIN_TYPE_NODE);
				List nodeTypesRoot = DAOFactory.getDomainDAO().loadListDomainsByType(MODEL_DOMAIN_TYPE_ROOT);
				List nodeTypes = new ArrayList();
				nodeTypes.addAll(nodeTypesNodes);
				nodeTypes.addAll(nodeTypesRoot);
				getSessionContainer().setAttribute("nodeTypesList", nodeTypes);
				
			} catch (EMFUserError e) {
				logger.error(e.getMessage(), e);
				throw new SpagoBIServiceException(SERVICE_NAME,
						"Exception retrieving model types", e);
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
	private JSONObject createJSONResponseModelsList(JSONArray rows, Integer totalModelsNumber)throws JSONException {
		JSONObject results;
		results = new JSONObject();
		results.put("total", totalModelsNumber);
		results.put("title", "ModelsList");
		results.put("rows", rows);
		return results;
	}
	private List<Model> deserializeNodesJSONArray(JSONArray rows) throws JSONException{
		List<Model> toReturn = new ArrayList<Model>();
		for(int i=0; i< rows.length(); i++){
			JSONObject obj = (JSONObject)rows.get(i);
			Model model = new Model();
			model.setId(obj.getInt("id"));
			model.setCode(obj.getString("code"));
			model.setDescription(obj.getString("description"));
			model.setLabel(obj.getString("label"));
			model.setName(obj.getString("name"));
			model.setTypeCd(obj.getString("type"));
			model.setTypeDescription(obj.getString("typeDescr"));
			try{
				model.setKpiId(obj.getInt("kpiId"));
			}catch(Throwable t){
				//nothing
				model.setKpiId(null);
			}
			String value = obj.getString("toSave");
			toReturn.add(model);
		}	
		return toReturn;
	}
	
	private String saveModelNodes(List<Model> nodesToSave){
		return null;
	}
}
