package it.eng.spagobi.wapp.util;

import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.wapp.bo.Menu;

public class MenuUtilities {

	Menu parent=null;
	
	public static String getMenuPath(Menu menu) {
		try{
		if(menu.getParentId()==null){
			return menu.getName();
		}
		else{
		Menu parent=DAOFactory.getMenuDAO().loadMenuByID(menu.getParentId());		
		return getMenuPath(parent)+" > "+menu.getName();
		}
		}
		catch (Exception e) {
			return "";
		}
	}
	
}
