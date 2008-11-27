package it.eng.spagobi.kpi.model.utils;

import it.eng.spago.base.SourceBean;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.kpi.model.bo.ModelInstance;

public class DetailModelInstanceUtil  {

	public static void selectModel(Integer id, SourceBean serviceResponse) throws Exception{
		ModelInstance toReturn = DAOFactory.getModelInstanceDAO().loadModelInstanceWithoutChildrenById(id);
		serviceResponse.setAttribute("MODELINSTANCE", toReturn);

	}

	public static void updateModelInstanceFromRequest(SourceBean serviceRequest, Integer id) throws Exception{
		ModelInstance modelInstance = getModelInstanceFromRequest(serviceRequest);
		modelInstance.setId(id);
		DAOFactory.getModelInstanceDAO().modifyModel(modelInstance);
	}

	private static ModelInstance getModelInstanceFromRequest(SourceBean serviceRequest) {
		String modelName = (String) serviceRequest.getAttribute("modelName");
		String modelDescription = (String) serviceRequest.getAttribute("modelDescription");
		
		ModelInstance toReturn = new ModelInstance();
		toReturn.setName(modelName);
		toReturn.setDescription(modelDescription);
		return toReturn;
	}

	public static void newModel(SourceBean serviceRequest, SourceBean serviceResponse,
			Integer parentId) throws Exception{
		ModelInstance toCreate = getModelInstanceFromRequest(serviceRequest);
//		if (id != null)
//			toCreate.setParentId(parentId);

		// insert the new model
		Integer modelId = DAOFactory.getModelInstanceDAO().insertModel(toCreate); 
		serviceResponse.setAttribute("ID", modelId);
		serviceResponse.setAttribute("MESSAGE",SpagoBIConstants.DETAIL_SELECT);
		selectModel(modelId, serviceResponse);
		
	}

}
