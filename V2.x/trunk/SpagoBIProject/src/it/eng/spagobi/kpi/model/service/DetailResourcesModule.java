package it.eng.spagobi.kpi.model.service;

import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.module.detail.impl.DefaultDetailModule;
import it.eng.spago.dispatching.service.detail.impl.DelegatedDetailService;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.kpi.model.utils.DetailModelInstanceUtil;
import it.eng.spagobi.kpi.model.utils.DetailResourcesUtil;

public class DetailResourcesModule extends DefaultDetailModule {

	public void service(SourceBean request, SourceBean response)
			throws Exception {

		String message = (String) request.getAttribute("MESSAGE");
		if (message == null) {
			message = SpagoBIConstants.DETAIL_SELECT;
		}
		// DETAIL_SELECT
		if (message.equalsIgnoreCase(SpagoBIConstants.DETAIL_SELECT)) {
			String idResource = (String) request.getAttribute("ID");
			DetailResourcesUtil.selectResource(Integer.parseInt(idResource), response);
		}
		// DETAIL_UPDATE
		if (message.equalsIgnoreCase(DelegatedDetailService.DETAIL_UPDATE)) {
			String idResource = (String) request.getAttribute("ID");
			DetailResourcesUtil.updateResourceFromRequest(request, Integer
					.parseInt(idResource));
			response.setAttribute("ID", Integer.parseInt(idResource));
			response.setAttribute("MESSAGE", SpagoBIConstants.DETAIL_SELECT);
			DetailResourcesUtil.selectResource(Integer.parseInt(idResource), response);
		}
		// DETAIL_INSERT
		if (message.equalsIgnoreCase(DelegatedDetailService.DETAIL_INSERT)) {
			DetailResourcesUtil.newResource(request, response);
		}
	}

}
