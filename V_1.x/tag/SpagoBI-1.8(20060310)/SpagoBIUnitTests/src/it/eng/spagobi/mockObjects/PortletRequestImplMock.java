package it.eng.spagobi.mockObjects;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.PortalContext;
import javax.portlet.PortletMode;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;
import javax.portlet.WindowState;

public class PortletRequestImplMock implements PortletRequest, ActionRequest {

	private PortletPreferences portletPreferences = null;
	
	private PortletSession portletSession = null;
	
	public boolean isWindowStateAllowed(WindowState arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isPortletModeAllowed(PortletMode arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public PortletMode getPortletMode() {
		// TODO Auto-generated method stub
		return null;
	}

	public WindowState getWindowState() {
		// TODO Auto-generated method stub
		return null;
	}

	public PortletPreferences getPreferences() {
		return portletPreferences;
	}

	public PortletSession getPortletSession() {
		return portletSession;
	}

	public PortletSession getPortletSession(boolean arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getProperty(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public Enumeration getProperties(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public Enumeration getPropertyNames() {
		// TODO Auto-generated method stub
		return null;
	}

	public PortalContext getPortalContext() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getAuthType() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getContextPath() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getRemoteUser() {
		return "Davide";
	}

	public Principal getUserPrincipal() {
		return new PrincipalImplMock("Davide");
	}

	public boolean isUserInRole(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public Object getAttribute(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public Enumeration getAttributeNames() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getParameter(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public Enumeration getParameterNames() {
		// TODO Auto-generated method stub
		return null;
	}

	public String[] getParameterValues(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public Map getParameterMap() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isSecure() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setAttribute(String arg0, Object arg1) {
		// TODO Auto-generated method stub

	}

	public void removeAttribute(String arg0) {
		// TODO Auto-generated method stub

	}

	public String getRequestedSessionId() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isRequestedSessionIdValid() {
		// TODO Auto-generated method stub
		return false;
	}

	public String getResponseContentType() {
		// TODO Auto-generated method stub
		return null;
	}

	public Enumeration getResponseContentTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	public Locale getLocale() {
		// TODO Auto-generated method stub
		return null;
	}

	public Enumeration getLocales() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getScheme() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getServerName() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getServerPort() {
		// TODO Auto-generated method stub
		return 0;
	}

	public PortletPreferences getPortletPreferences() {
		return portletPreferences;
	}

	public void setPortletPreferences(PortletPreferences portletPreferences) {
		this.portletPreferences = portletPreferences;
	}

	public void setPortletSession(PortletSession portletSession) {
		this.portletSession = portletSession;
	}

	public InputStream getPortletInputStream() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public void setCharacterEncoding(String arg0) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		
	}

	public BufferedReader getReader() throws UnsupportedEncodingException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getCharacterEncoding() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getContentType() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getContentLength() {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
