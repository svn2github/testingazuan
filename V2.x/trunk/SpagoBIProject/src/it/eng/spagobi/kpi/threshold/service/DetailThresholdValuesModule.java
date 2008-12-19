package it.eng.spagobi.kpi.threshold.service;

import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.module.detail.impl.DefaultDetailModule;
import it.eng.spago.dispatching.service.detail.impl.DelegatedDetailService;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.kpi.threshold.utils.DetailThresholdsValueUtil;

public class DetailThresholdValuesModule extends DefaultDetailModule {

	public void service(SourceBean request, SourceBean response) throws Exception {
		
		String message = (String) request.getAttribute("MESSAGE");
		if (message == null) {	
			message = SpagoBIConstants.DETAIL_SELECT;
		}
		// DETAIL_SELECT
		if (message.equalsIgnoreCase(SpagoBIConstants.DETAIL_SELECT)){
			String id = (String)request.getAttribute("ID");
			DetailThresholdsValueUtil.selectThresholdValue(Integer.parseInt(id), response);
		}
		// DETAIL_UPDATE
		if (message.equalsIgnoreCase(DelegatedDetailService.DETAIL_UPDATE)){
			String id = (String) request.getAttribute("ID");
			DetailThresholdsValueUtil.updateThresholdValueFromRequest(request, Integer.parseInt(id));
			response.setAttribute("ID", Integer.parseInt(id));
			response.setAttribute("MESSAGE", SpagoBIConstants.DETAIL_SELECT);
			DetailThresholdsValueUtil.selectThresholdValue(Integer.parseInt(id), response);
		}
		// DETAIL_INSERT
		if (message.equalsIgnoreCase(DelegatedDetailService.DETAIL_INSERT)){
			DetailThresholdsValueUtil.newThresholdValue(request,response);
		}
	}
}
