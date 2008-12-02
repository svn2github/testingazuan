package it.eng.spagobi.kpi.model.utils;

import java.util.List;

import it.eng.spago.base.SourceBean;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.kpi.model.bo.Model;
import it.eng.spagobi.kpi.model.bo.ModelInstance;

public class DetailModelInstanceUtil  {

	public static void selectModelInstance(Integer id, SourceBean serviceResponse) throws Exception{
		ModelInstance toReturn = DAOFactory.getModelInstanceDAO().loadModelInstanceWithoutChildrenById(id);
		serviceResponse.setAttribute("MODELINSTANCE", toReturn);

	}

	public static void updateModelInstanceFromRequest(SourceBean serviceRequest, Integer id) throws Exception{
		ModelInstance modelInstance = getModelInstanceFromRequest(serviceRequest);
		modelInstance.setId(id);
		DAOFactory.getModelInstanceDAO().modifyModel(modelInstance);
	}

	private static ModelInstance getModelInstanceFromRequest(SourceBean serviceRequest) {
		String modelName = (String) serviceRequest.getAttribute("modelInstanceName");
		String modelDescription = (String) serviceRequest.getAttribute("modelInstanceDescription");
		
		ModelInstance toReturn = new ModelInstance();
		toReturn.setName(modelName);
		toReturn.setDescription(modelDescription);
		return toReturn;
	}

	public static void newModelInstance(SourceBean serviceRequest, SourceBean serviceResponse,
			Integer parentId) throws Exception{
		ModelInstance toCreate = getModelInstanceFromRequest(serviceRequest);
		if (parentId != null)
			toCreate.setParentId(parentId);
		String modelId = (String) serviceRequest.getAttribute("KPI_MODEL_ID");
		Model model = new Model();
		model.setId(Integer.parseInt(modelId));
		toCreate.setModel(model);
//		// insert the new model
		Integer modelInstanceId = DAOFactory.getModelInstanceDAO().insertModelInstance(toCreate); 
		serviceResponse.setAttribute("ID", modelInstanceId);
		serviceResponse.setAttribute("MESSAGE",SpagoBIConstants.DETAIL_SELECT);
		selectModelInstance(modelInstanceId, serviceResponse);
		
	}

	public static List getCandidateModelChildren(Integer parentId){
		List candidateModelChildren = null;
		try {
			candidateModelChildren = DAOFactory.getModelInstanceDAO().getCandidateModelChildren(parentId);
		} catch (EMFUserError e) {
			
		}
		return candidateModelChildren;
	}

}
