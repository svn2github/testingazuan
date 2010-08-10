package it.eng.spagobi.mockObjects;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.portlet.PortletPreferences;
import javax.portlet.ReadOnlyException;
import javax.portlet.ValidatorException;

public class PortletPreferencesImplMock implements PortletPreferences {

	private HashMap table = new HashMap();
	
	public boolean isReadOnly(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public String getValue(String arg0, String arg1) {
		return (String) table.get(arg0);
	}

	public String[] getValues(String arg0, String[] arg1) {
		return new String[] {"",""};
	}

	public void setValue(String arg0, String arg1) throws ReadOnlyException {
		table.put(arg0, arg1);
	}

	public void setValues(String arg0, String[] arg1) throws ReadOnlyException {
		// TODO Auto-generated method stub

	}

	public Enumeration getNames() {
		// TODO Auto-generated method stub
		return null;
	}

	public Map getMap() {
		// TODO Auto-generated method stub
		return null;
	}

	public void reset(String arg0) throws ReadOnlyException {
		// TODO Auto-generated method stub

	}

	public void store() throws IOException, ValidatorException {
		// TODO Auto-generated method stub

	}

}
