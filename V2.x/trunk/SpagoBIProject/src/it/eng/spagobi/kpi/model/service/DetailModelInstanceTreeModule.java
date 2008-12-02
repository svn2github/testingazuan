package it.eng.spagobi.kpi.model.service;

import java.util.ArrayList;
import java.util.List;

import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.module.detail.impl.DefaultDetailModule;
import it.eng.spago.dispatching.service.detail.impl.DelegatedDetailService;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.kpi.model.bo.ModelInstance;
import it.eng.spagobi.kpi.model.utils.DetailModelInstanceUtil;

public class DetailModelInstanceTreeModule extends DefaultDetailModule{
	
	public void service(SourceBean request, SourceBean response) throws Exception {
		
		String message = (String) request.getAttribute("MESSAGE");
		if (message == null) {	
			message = SpagoBIConstants.DETAIL_SELECT;
		}
		// DETAIL_SELECT
		if (message.equalsIgnoreCase(SpagoBIConstants.DETAIL_SELECT)){
			String id = (String)request.getAttribute("MODEL_ID");
			DetailModelInstanceUtil.selectModelInstance(Integer.parseInt(id), response);
		}
		// DETAIL_UPDATE
		if (message.equalsIgnoreCase(DelegatedDetailService.DETAIL_UPDATE)){
			String idModel = (String) request.getAttribute("MODEL_ID");
			DetailModelInstanceUtil.updateModelInstanceFromRequest(request, Integer.parseInt(idModel));
		}
		// DETAIL_INSERT
		if (message.equalsIgnoreCase(DelegatedDetailService.DETAIL_INSERT)){
			String parentId = (String)request.getAttribute("MODEL_ID");
			if(parentId != null && parentId.equalsIgnoreCase("null"))
				parentId = null;
			DetailModelInstanceUtil.newModelInstance(request, response, Integer.parseInt(parentId));
		}
	
		String parentId = (String)request.getAttribute("ID");
		List result = new ArrayList();
		ModelInstance aModelInstance = DAOFactory.getModelInstanceDAO().loadModelInstanceWithChildrenById(Integer.parseInt(parentId));
		result.add(aModelInstance);
		response.setAttribute(SpagoBIConstants.FUNCTIONALITIES_LIST, result);
	}

}