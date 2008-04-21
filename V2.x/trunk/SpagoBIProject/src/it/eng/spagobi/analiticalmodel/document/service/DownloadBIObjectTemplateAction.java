package it.eng.spagobi.analiticalmodel.document.service;

import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.action.AbstractHttpAction;
import it.eng.spagobi.analiticalmodel.document.bo.ObjTemplate;
import it.eng.spagobi.analiticalmodel.document.dao.IObjTemplateDAO;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.services.BaseProfileAction;

import javax.servlet.http.HttpServletResponse;

public class DownloadBIObjectTemplateAction extends BaseProfileAction {

	public void service(SourceBean request, SourceBean response) throws Exception {
		super.service(request, response);
		freezeHttpResponse();
		HttpServletResponse httpResp = getHttpResponse();
		String idTemplateStr = (String)request.getAttribute("TEMP_ID");
		Integer idTemplate = new Integer(idTemplateStr);
		IObjTemplateDAO objtempdao = DAOFactory.getObjTemplateDAO();
		ObjTemplate objTemp = objtempdao.loadBIObjectTemplate(idTemplate);
		byte[] content = objTemp.getContent(); 
		httpResp.setHeader("Content-Disposition","attachment; filename=\"" + objTemp.getName() + "\";");
		httpResp.setContentLength(content.length);
		httpResp.getOutputStream().write(content);
		httpResp.getOutputStream().flush();
	}

}
