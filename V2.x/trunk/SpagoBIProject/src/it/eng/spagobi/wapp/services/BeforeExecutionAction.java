package it.eng.spagobi.wapp.services;
import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.action.AbstractHttpAction;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.wapp.bo.Menu;

public class BeforeExecutionAction extends AbstractHttpAction{

	public void service(SourceBean serviceRequest, SourceBean serviceResponse)
			throws Exception {
		
		String menuId=(String)serviceRequest.getAttribute("MENU_ID");
		RequestContainer requestContainer=RequestContainer.getRequestContainer();
		SessionContainer sessionContainer=requestContainer.getSessionContainer();
		
		if(menuId!=null){
		
			Menu menu=DAOFactory.getMenuDAO().loadMenuByID(Integer.valueOf(menuId));
			boolean hideBar=menu.isHideExecBar();
			if(hideBar)sessionContainer.setAttribute("TOOLBAR_VISIBLE", Boolean.valueOf(false));
			serviceResponse.setAttribute("objectid", menu.getObjId().toString());
			//serviceResponse.setAttribute("menuid",menuId);
		}
	}

}
