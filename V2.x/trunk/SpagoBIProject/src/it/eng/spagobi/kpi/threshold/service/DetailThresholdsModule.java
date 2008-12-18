package it.eng.spagobi.kpi.threshold.service;

import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.module.detail.impl.DefaultDetailModule;
import it.eng.spago.dispatching.service.detail.impl.DelegatedDetailService;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.kpi.threshold.utils.DetailThresholdsUtil;

public class DetailThresholdsModule extends DefaultDetailModule{
	
	public void service(SourceBean request, SourceBean response) throws Exception {
		
		String message = (String) request.getAttribute("MESSAGE");
		if (message == null) {	
			message = SpagoBIConstants.DETAIL_SELECT;
		}
		// DETAIL_SELECT
		if (message.equalsIgnoreCase(SpagoBIConstants.DETAIL_SELECT)){
			String id = (String)request.getAttribute("ID");
			DetailThresholdsUtil.selectThreshold(Integer.parseInt(id), response);
		}
		// DETAIL_UPDATE
		if (message.equalsIgnoreCase(DelegatedDetailService.DETAIL_UPDATE)){
			String id = (String) request.getAttribute("ID");
			DetailThresholdsUtil.updateThresholdFromRequest(request, Integer.parseInt(id));
			response.setAttribute("ID", Integer.parseInt(id));
			response.setAttribute("MESSAGE", SpagoBIConstants.DETAIL_SELECT);
			DetailThresholdsUtil.selectThreshold(Integer.parseInt(id), response);
		}
		// DETAIL_INSERT
		if (message.equalsIgnoreCase(DelegatedDetailService.DETAIL_INSERT)){
			DetailThresholdsUtil.newThreshold(request,response);
		}
	}

}