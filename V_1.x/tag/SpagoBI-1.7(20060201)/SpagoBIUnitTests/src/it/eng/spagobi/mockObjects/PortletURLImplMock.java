package it.eng.spagobi.mockObjects;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.portlet.PortletMode;
import javax.portlet.PortletModeException;
import javax.portlet.PortletSecurityException;
import javax.portlet.PortletURL;
import javax.portlet.WindowState;
import javax.portlet.WindowStateException;

public class PortletURLImplMock implements PortletURL {
	
	private HashMap table = new HashMap();

	public void setWindowState(WindowState arg0) throws WindowStateException {
		// TODO Auto-generated method stub

	}

	public void setPortletMode(PortletMode arg0) throws PortletModeException {
		// TODO Auto-generated method stub

	}

	public void setParameter(String arg0, String arg1) {
		table.put(arg0,arg1);

	}

	public void setParameter(String arg0, String[] arg1) {
		// TODO Auto-generated method stub

	}

	public void setParameters(Map arg0) {
		// TODO Auto-generated method stub

	}

	public void setSecure(boolean arg0) throws PortletSecurityException {
		// TODO Auto-generated method stub

	}
	
	public String toString() {
		String toReturn = null;
		Set keys = table.keySet();
		Iterator i = keys.iterator();
		while (i.hasNext()){
			String key = (String) i.next();
			String value = (String) table.get(key);
			toReturn += "&" + key +"="+value;
		}
		return toReturn;
	}

}
