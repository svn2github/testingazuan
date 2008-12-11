package it.eng.spagobi.kpi.model.service;

import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.module.detail.impl.DefaultDetailModule;
import it.eng.spago.dispatching.service.detail.impl.DelegatedDetailService;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.kpi.model.utils.DetailModelInstanceUtil;
import it.eng.spagobi.kpi.model.utils.DetailModelUtil;

public class DetailModelModule extends DefaultDetailModule{
	
	public void service(SourceBean request, SourceBean response) throws Exception {
		
		String message = (String) request.getAttribute("MESSAGE");
		if (message == null) {	
			message = SpagoBIConstants.DETAIL_SELECT;
		}
		// DETAIL_SELECT
		if (message.equalsIgnoreCase(SpagoBIConstants.DETAIL_SELECT)){
			String parentId = (String)request.getAttribute("ID");
			DetailModelUtil.selectModel(Integer.parseInt(parentId), response);
		}
		// DETAIL_UPDATE
		if (message.equalsIgnoreCase(DelegatedDetailService.DETAIL_UPDATE)){
			String idModel = (String) request.getAttribute("ID");
			DetailModelUtil.updateModelFromRequest(request, Integer.parseInt(idModel));
			
			response.setAttribute("ID", Integer.parseInt(idModel));
			response.setAttribute("MESSAGE", SpagoBIConstants.DETAIL_SELECT);
			DetailModelUtil.selectModel(Integer.parseInt(idModel), response);
			
		}
		// DETAIL_INSERT
		if (message.equalsIgnoreCase(DelegatedDetailService.DETAIL_INSERT)){
			DetailModelUtil.newModel(request,response, null);
		}
	}

}