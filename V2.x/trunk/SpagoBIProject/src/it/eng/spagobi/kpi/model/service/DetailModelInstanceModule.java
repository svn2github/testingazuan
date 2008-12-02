package it.eng.spagobi.kpi.model.service;

import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.module.detail.impl.DefaultDetailModule;
import it.eng.spago.dispatching.service.detail.impl.DelegatedDetailService;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.kpi.model.utils.DetailModelInstanceUtil;


public class DetailModelInstanceModule extends DefaultDetailModule{
	
	public void service(SourceBean request, SourceBean response) throws Exception {
		String message = (String) request.getAttribute("MESSAGE");
		if (message == null) {	
			message = SpagoBIConstants.DETAIL_SELECT;
		}
		// DETAIL_SELECT
		if (message.equalsIgnoreCase(SpagoBIConstants.DETAIL_SELECT)){
			String idModel = (String)request.getAttribute("ID");
			DetailModelInstanceUtil.selectModelInstance(Integer.parseInt(idModel), response);
		}
		// DETAIL_UPDATE
		if (message.equalsIgnoreCase(DelegatedDetailService.DETAIL_UPDATE)){
			String idModel = (String) request.getAttribute("ID");
			DetailModelInstanceUtil.updateModelInstanceFromRequest(request, Integer.parseInt(idModel));
		}
		// DETAIL_INSERT
		if (message.equalsIgnoreCase(DelegatedDetailService.DETAIL_INSERT)){
			DetailModelInstanceUtil.newModelInstance(request,response, null);
		}
	}

}
