package it.eng.spagobi.wapp.services;

import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.action.AbstractAction;
import it.eng.spagobi.commons.dao.DAOFactory;

public class MoveDownMenuAction extends AbstractAction {

	public static String ACTION_NAME = "MOVE_DOWN_MENU";
	
	/* (non-Javadoc)
	 * @see it.eng.spago.dispatching.service.ServiceIFace#service(it.eng.spago.base.SourceBean, it.eng.spago.base.SourceBean)
	 */
	public void service(SourceBean request, SourceBean response) throws Exception {
		String idMenu = (String) request.getAttribute(DetailMenuModule.MENU_ID);
		Integer id = new Integer(idMenu);
		DAOFactory.getMenuDAO().moveDownMenu(id);
	}

}
