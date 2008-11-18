/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2008 Engineering Ingegneria Informatica S.p.A.

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

import java.util.ArrayList;
import java.util.List;

import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.module.detail.impl.DefaultDetailModule;
import it.eng.spago.dispatching.service.detail.impl.DelegatedDetailService;
import it.eng.spago.init.InitializerIFace;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.kpi.model.utils.ModelUtils;

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
		// DETAIL_INSERT
		if (message.equalsIgnoreCase(DelegatedDetailService.DETAIL_INSERT)){
			newModel(request,response);
		}
	}

	private void selectModel(String parentId, SourceBean serviceResponse) throws Exception{
		SourceBean modelAttribute = getAttribute(parentId, "QUERIES.SELECT_QUERY_MODEL");
		SourceBean domainsAttribute = getAttribute(parentId, "QUERIES.SELECT_QUERY_DOMAINS"); 
		SourceBean modelTypeAttribute = getAttribute(parentId, "QUERIES.SELECT_QUERY_MODEL_ATTR");
		serviceResponse.setAttribute("MODELATTRIBUTE", modelAttribute);
		serviceResponse.setAttribute("DOMAINSATTRIBUTE", domainsAttribute);
		serviceResponse.setAttribute("MODELTYPEATTRIBUTE", modelTypeAttribute);
	}

	private void newModel(SourceBean serviceRequest, SourceBean serviceResponse) throws Exception {
		String modelName = (String) serviceRequest.getAttribute("modelName");
		String modelDescription = (String) serviceRequest.getAttribute("modelDescription");
		String modelCode = (String) serviceRequest.getAttribute("modelCode");
		String modelTypeId = (String) serviceRequest.getAttribute("modelTypeId");
		Integer modelId;
		SourceBean attrId = null;
		List attrList = null;
		// insert the new model
		insertModel(modelName, modelDescription, modelCode, modelTypeId);
		// get the ID of the new model
		modelId = selectModelId(modelName, modelDescription, modelCode, modelTypeId);
		//get the list of all attribute of the model type
		attrId = getAttribute(modelTypeId, "QUERIES.SELECT_QUERY_MODEL_ATTR_ID_FROM_MODEL_ID");
		attrList = attrId.getAttributeAsList("ROW");
		// create the new attribute of the model type
		for(int i=0; i<attrList.size(); i++){
			SourceBean row = (SourceBean)attrList.get(i);
			Integer aId = (Integer)row.getAttribute("ATTR_ID");
			insertModelAttr(modelId.toString(), aId.toString());
		}
		serviceResponse.setAttribute("ID", modelId.toString());
		serviceResponse.setAttribute("MESSAGE",SpagoBIConstants.DETAIL_SELECT);
		// add the select attribute
		selectModel(modelId.toString(), serviceResponse);
		
	}

	private SourceBean getAttribute(String Id, String queryName) throws Exception {
		InitializerIFace serviceInitializer = (InitializerIFace) this;
		SourceBean statement =
            (SourceBean) serviceInitializer.getConfig().getAttribute(
                queryName);
		ArrayList inputParameters = new ArrayList();
		inputParameters.add(Id);
		return ModelUtils.selectQuery(statement,inputParameters);
	}

	private void updateModelFromRequest(SourceBean serviceRequest) throws Exception{
		String idModel = (String) serviceRequest
		.getAttribute("ID");
		String modelName = (String) serviceRequest.getAttribute("modelName");
		String modelDescription = (String) serviceRequest.getAttribute("modelDescription");
		String modelCode = (String) serviceRequest.getAttribute("modelCode");
		updateModel(idModel, modelName, modelDescription, modelCode);
		String modelAttributeName = (String)serviceRequest.getAttribute("MODELATTRIBUTESNAME");
		updateModelAttribute(serviceRequest, modelAttributeName);
		
	}

	private void updateModel(String idModel, String modelName, String modelDescription, String modelCode) throws Exception{
		InitializerIFace serviceInitializer = (InitializerIFace) this;
		SourceBean statement =
            (SourceBean) serviceInitializer.getConfig().getAttribute("QUERIES.UPDATE_QUERY_MODEL");
		ArrayList inputParameters = new ArrayList();
		inputParameters.add(modelCode);
		inputParameters.add(modelDescription);
		inputParameters.add(modelName);
		inputParameters.add(idModel);
		ModelUtils.updateQuery(statement, inputParameters);
	}

	private void updateModelAttribute(SourceBean serviceRequest, String modelAttributeName) throws Exception{
		// SBI_KPI_MODEL_ATTR IDS
		modelAttributeName  = modelAttributeName.replace('[',' ');
		modelAttributeName = modelAttributeName.replace(']',' ');
		String[] modelAttributeNameList = modelAttributeName.split(",");
		// SBI_KPI_MODEL ID
		String idModel = (String) serviceRequest.getAttribute("ID");
		//STATEMENT
		InitializerIFace serviceInitializer = (InitializerIFace) this;
		SourceBean statement =
			(SourceBean) serviceInitializer.getConfig().getAttribute("QUERIES.UPDATE_QUERY_MODEL_ATTR");
		for (int i = 0; i<modelAttributeNameList.length; i++) {
			String modelAttrId = modelAttributeNameList[i].trim();
			ArrayList inputParameters = new ArrayList();
			String value = (String) serviceRequest.getAttribute("M_ATTR" + modelAttrId);
			inputParameters.add(value);
			inputParameters.add(modelAttrId);
			ModelUtils.updateQuery(statement, inputParameters);
		}
	}

	private void insertModel(String modelName, String modelDescription,
			String modelCode, String modelTypeId) throws Exception{
		InitializerIFace serviceInitializer = (InitializerIFace) this;
		SourceBean statement =
            (SourceBean) serviceInitializer.getConfig().getAttribute("QUERIES.INSERT_QUERY_MODEL");
		ArrayList inputParameters = new ArrayList();
		inputParameters.add(modelCode);
		inputParameters.add(modelDescription);
		inputParameters.add(modelName);
		inputParameters.add(modelTypeId);
		ModelUtils.insertQuery(statement, inputParameters);
	}

	private void insertModelAttr(String modelId, String attrId) throws Exception{
		InitializerIFace serviceInitializer = (InitializerIFace) this;
		SourceBean statement =
            (SourceBean) serviceInitializer.getConfig().getAttribute("QUERIES.INSERT_QUERY_MODEL_ATTR");
		ArrayList inputParameters = new ArrayList();
		inputParameters.add(attrId);
		inputParameters.add(modelId);
		inputParameters.add("");
		ModelUtils.insertQuery(statement, inputParameters);
	}

	private Integer selectModelId(String modelName, String modelDescription,
			String modelCode, String modelTypeId) throws Exception{
		SourceBean result = null;
		InitializerIFace serviceInitializer = (InitializerIFace) this;
		SourceBean statement =
            (SourceBean) serviceInitializer.getConfig().getAttribute("QUERIES.SELECT_QUERY_MODEL_ID_FROM_ATTR");
		ArrayList inputParameters = new ArrayList();
		inputParameters.add(modelCode);
		inputParameters.add(modelDescription);
		inputParameters.add(modelName);
		inputParameters.add(modelTypeId);
		result = ModelUtils.selectQuery(statement, inputParameters);
		return (Integer)result.getAttribute("ROW.ID");
	}

}
