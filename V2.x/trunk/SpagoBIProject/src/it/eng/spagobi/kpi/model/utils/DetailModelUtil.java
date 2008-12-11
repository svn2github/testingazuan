package it.eng.spagobi.kpi.model.utils;

import it.eng.spago.base.SourceBean;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.kpi.model.bo.Model;
import it.eng.spagobi.kpi.model.bo.ModelAttribute;

import java.util.ArrayList;
import java.util.List;

public class DetailModelUtil {
	
	static public void selectModel(Integer parentId, SourceBean serviceResponse) throws Exception{
		Model toReturn = DAOFactory.getModelDAO().loadModelWithoutChildrenById(parentId);
		serviceResponse.setAttribute("MODEL", toReturn);
	}

	static public void updateModelFromRequest(SourceBean serviceRequest, Integer idModel) throws Exception{
		Model model = getModelFromRequest(serviceRequest);
		String modelAttributeName = (String)serviceRequest.getAttribute("MODELATTRIBUTESNAME");
		List modelAttributes = updateModelAttribute(serviceRequest, modelAttributeName);
		model.setId(idModel);
		model.setModelAttributes(modelAttributes);
		DAOFactory.getModelDAO().modifyModel(model);		
	}

	/**
	 * Create the model attributes from the request. 
	 * @param serviceRequest
	 * @param modelAttributeName list of the id of the modelattribute
	 * @return list of ModelAttribute with the new value
	 */
	static public List updateModelAttribute(SourceBean serviceRequest, String modelAttributeName){
		// SBI_KPI_MODEL_ATTR IDS
		modelAttributeName  = modelAttributeName.replace('[',' ');
		modelAttributeName = modelAttributeName.replace(']',' ');
		String[] modelAttributeNameList = modelAttributeName.split(",");
		List modelAttributes = new ArrayList(); 
		for (int i = 0; i<modelAttributeNameList.length; i++) {
			ModelAttribute mAttr = new ModelAttribute();
			String modelAttrId = modelAttributeNameList[i].trim();
			if(!modelAttrId.isEmpty()){
				String value = (String) serviceRequest.getAttribute("M_ATTR" + modelAttrId);
				mAttr.setValue(value);
				mAttr.setId(Integer.parseInt(modelAttrId));
				modelAttributes.add(mAttr);
			}
		}
		return modelAttributes;
	}

	static private Model getModelFromRequest(SourceBean serviceRequest) {
		String modelName = (String) serviceRequest.getAttribute("modelName");
		String modelDescription = (String) serviceRequest.getAttribute("modelDescription");
		String modelCode = (String) serviceRequest.getAttribute("modelCode");
		String modelKpiId = (String) serviceRequest.getAttribute("kpiId");
		Integer kpiId = null;
		
		if (modelKpiId!=null && Integer.parseInt(modelKpiId)!= -1){
			kpiId = Integer.parseInt(modelKpiId);
		}

		Model toReturn = new Model();
		toReturn.setName(modelName);
		toReturn.setDescription(modelDescription);
		toReturn.setCode(modelCode);
		toReturn.setKpiId(kpiId);

		return toReturn;
	}

	static public void newModel(SourceBean serviceRequest, SourceBean serviceResponse,Integer parentId) throws Exception {
		Model toCreate = getModelFromRequest(serviceRequest);
		if (parentId != null)
			toCreate.setParentId(parentId);
		
		String modelTypeId = (String) serviceRequest.getAttribute("modelTypeId");

		// insert the new model
		Integer modelId = DAOFactory.getModelDAO().insertModel(toCreate, Integer.parseInt(modelTypeId));

		serviceResponse.setAttribute("ID", modelId);
		serviceResponse.setAttribute("MESSAGE",SpagoBIConstants.DETAIL_SELECT);
		selectModel(modelId, serviceResponse);
	}

}
