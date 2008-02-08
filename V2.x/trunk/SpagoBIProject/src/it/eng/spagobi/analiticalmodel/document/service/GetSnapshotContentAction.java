package it.eng.spagobi.analiticalmodel.document.service;

import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.action.AbstractHttpAction;
import it.eng.spagobi.analiticalmodel.document.bo.Snapshot;
import it.eng.spagobi.analiticalmodel.document.dao.ISnapshotDAO;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.commons.dao.DAOFactory;

import javax.servlet.http.HttpServletResponse;

public class GetSnapshotContentAction extends AbstractHttpAction {

	public void service(SourceBean request, SourceBean response) throws Exception {
		freezeHttpResponse();
		HttpServletResponse httpResp = getHttpResponse();
		String idSnapStr = (String)request.getAttribute(SpagoBIConstants.SNAPSHOT_ID);
		Integer idSnap = new Integer(idSnapStr);
		ISnapshotDAO snapdao = DAOFactory.getSnapshotDAO();
		Snapshot snap = snapdao.loadSnapshot(idSnap);
		byte[] content = snap.getContent();
		httpResp.setContentLength(content.length);
		httpResp.getOutputStream().write(content);
		httpResp.getOutputStream().flush();
	}

}
