package it.eng.spagobi.booklets.utils;

import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spagobi.utilities.GeneralUtilities;


public class BookletServiceUtils {

	private static String getBookletServiceName() {
		ConfigSingleton config = ConfigSingleton.getInstance();
		SourceBean bookServiceSB = (SourceBean)config.getAttribute("BOOKLETS.BOOKLET_SERVICE");
		String bookletServName = (String)bookServiceSB.getAttribute("name");
		return bookletServName;
	}
	
	public static String getBookletServiceUrl() {
		String sbiContAdd = GeneralUtilities.getSpagoBiContextAddress();
		String bookletServName = getBookletServiceName();
		return sbiContAdd + "/" + bookletServName;
	}
}
