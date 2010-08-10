package it.eng.spagobi.mockObjects;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;

import java.util.Enumeration;

import javax.portlet.PortletContext;
import javax.portlet.PortletSession;

public class PortletSessionImplMock implements PortletSession {

	SourceBean content = null;
	
	public PortletSessionImplMock() {
		try {
			content = new SourceBean("PortletSessionContent");
		} catch (SourceBeanException e) {
			e.printStackTrace();
		}
	}
	
	public Object getAttribute(String key) {
		return content.getAttribute(key);
	}

	public Object getAttribute(String arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	public Enumeration getAttributeNames() {
		// TODO Auto-generated method stub
		return null;
	}

	public Enumeration getAttributeNames(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public long getCreationTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	public long getLastAccessedTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getMaxInactiveInterval() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void invalidate() {
		// TODO Auto-generated method stub

	}

	public boolean isNew() {
		// TODO Auto-generated method stub
		return false;
	}

	public void removeAttribute(String arg0) {
		// TODO Auto-generated method stub

	}

	public void removeAttribute(String arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	public void setAttribute(String key, Object value) {
		try {
			content.setAttribute(key,value);
		} catch (SourceBeanException e) {
			e.printStackTrace();
		}
	}

	public void setAttribute(String arg0, Object arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	public void setMaxInactiveInterval(int arg0) {
		// TODO Auto-generated method stub

	}

	public PortletContext getPortletContext() {
		// TODO Auto-generated method stub
		return null;
	}

}
