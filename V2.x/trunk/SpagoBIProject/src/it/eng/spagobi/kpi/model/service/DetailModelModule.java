package it.eng.spagobi.kpi.model.service;

import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.module.detail.impl.DefaultDetailModule;
import it.eng.spago.dispatching.service.detail.impl.DelegatedDetailService;
import it.eng.spago.init.InitializerIFace;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.kpi.model.bo.Model;
import it.eng.spagobi.kpi.model.bo.ModelAttribute;

import java.util.ArrayList;
import java.util.List;


public class DetailModelModule extends DefaultDetailModule{
	
	public void service(SourceBean request, SourceBean response) throws Exception {
		String message = (String) request.getAttribute("MESSAGE");
		if (message == null) {	
			message = SpagoBIConstants.DETAIL_SELECT;
		}
		// DETAIL_SELECT
		if (message.equalsIgnoreCase(SpagoBIConstants.DETAIL_SELECT)){
			String parentId = (String)request.getAttribute("ID");
			selectModel(parentId, response);
		}
		// DETAIL_UPDATE
		if (message.equalsIgnoreCase(DelegatedDetailService.DETAIL_UPDATE)){	
			updateModelFromRequest(request);
		}
//		// DETAIL_INSERT
		if (message.equalsIgnoreCase(DelegatedDetailService.DETAIL_INSERT)){
			newModel(request,response);
		}
	}

	private void selectModel(String parentId, SourceBean serviceResponse) throws Exception{
		Model toReturn = DAOFactory.getModelDAO().loadModelWithoutChildrenById(Integer.parseInt(parentId));
		serviceResponse.setAttribute("MODEL", toReturn);
	}
	
	private void updateModelFromRequest(SourceBean serviceRequest) throws Exception{
		Model model = getModelFromRequest(serviceRequest);
		String idModel = (String) serviceRequest.getAttribute("ID");
		String modelAttributeName = (String)serviceRequest.getAttribute("MODELATTRIBUTESNAME");
		List modelAttributes = updateModelAttribute(serviceRequest, modelAttributeName);
		model.setId(Integer.parseInt(idModel));
		model.setModelAttributes(modelAttributes);
		DAOFactory.getModelDAO().modifyModel(model);		
	}

	/**
	 * Create the model attributes from the request. 
	 * @param serviceRequest
	 * @param modelAttributeName list of the id of the modelattribute
	 * @return list of ModelAttribute with the new value
	 */
	private List updateModelAttribute(SourceBean serviceRequest, String modelAttributeName){
		// SBI_KPI_MODEL_ATTR IDS
		modelAttributeName  = modelAttributeName.replace('[',' ');
		modelAttributeName = modelAttributeName.replace(']',' ');
		String[] modelAttributeNameList = modelAttributeName.split(",");
		List modelAttributes = new ArrayList(); 
		for (int i = 0; i<modelAttributeNameList.length; i++) {
			ModelAttribute mAttr = new ModelAttribute();
			String modelAttrId = modelAttributeNameList[i].trim();
			String value = (String) serviceRequest.getAttribute("M_ATTR" + modelAttrId);
			mAttr.setValue(value);
			mAttr.setId(Integer.parseInt(modelAttrId));
			modelAttributes.add(mAttr);
		}
		return modelAttributes;
	}
	
	private Model getModelFromRequest(SourceBean serviceRequest) {
		String modelName = (String) serviceRequest.getAttribute("modelName");
		String modelDescription = (String) serviceRequest.getAttribute("modelDescription");
		String modelCode = (String) serviceRequest.getAttribute("modelCode");

		Model toReturn = new Model();
		toReturn.setName(modelName);
		toReturn.setDescription(modelDescription);
		toReturn.setCode(modelCode);
		
		return toReturn;
	}

	private void newModel(SourceBean serviceRequest, SourceBean serviceResponse) throws Exception {
		Model toCreate = getModelFromRequest(serviceRequest);
		String modelTypeId = (String) serviceRequest.getAttribute("modelTypeId");

		// insert the new model
		Integer modelId = DAOFactory.getModelDAO().insertModel(toCreate, Integer.parseInt(modelTypeId));

		serviceResponse.setAttribute("ID", modelId);
		serviceResponse.setAttribute("MESSAGE",SpagoBIConstants.DETAIL_SELECT);
		selectModel(modelId.toString(), serviceResponse);
	}
}