package it.eng.spagobi.analiticalmodel.document.service;

import javax.servlet.http.HttpServletResponse;

import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.action.AbstractHttpAction;
import it.eng.spagobi.analiticalmodel.document.bo.ObjTemplate;
import it.eng.spagobi.analiticalmodel.document.dao.IObjTemplateDAO;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.dao.IBinContentDAO;

public class DownloadBIObjectTemplateAction extends AbstractHttpAction {

	public void service(SourceBean request, SourceBean response) throws Exception {
		freezeHttpResponse();
		HttpServletResponse httpResp = getHttpResponse();
		String idTemplateStr = (String)request.getAttribute("TEMP_ID");
		Integer idTemplate = new Integer(idTemplateStr);
		IObjTemplateDAO objtempdao = DAOFactory.getObjTemplateDAO();
		IBinContentDAO bincondao = DAOFactory.getBinContentDAO();
		ObjTemplate objTemp = objtempdao.loadBIObjectTemplate(idTemplate);
		byte[] content = bincondao.getBinContent(objTemp.getBinId()); 
		httpResp.setHeader("Content-Disposition","attachment; filename=\"" + objTemp.getName() + "\";");
		httpResp.setContentLength(content.length);
		httpResp.getOutputStream().write(content);
		httpResp.getOutputStream().flush();
	}

}
