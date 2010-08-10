package it.eng.spagobi.mockObjects;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Locale;

import javax.portlet.PortletURL;
import javax.portlet.RenderResponse;

public class RenderResponseImplMock implements RenderResponse {
	
	private String url = null;
	
	public String getContentType() {
		// TODO Auto-generated method stub
		return null;
	}

	public PortletURL createRenderURL() {
		// TODO Auto-generated method stub
		return null;
	}

	public PortletURL createActionURL() {
		return new PortletURLImplMock();
	}

	public String getNamespace() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setTitle(String arg0) {
		// TODO Auto-generated method stub

	}

	public void setContentType(String arg0) {
		// TODO Auto-generated method stub

	}

	public String getCharacterEncoding() {
		// TODO Auto-generated method stub
		return null;
	}

	public PrintWriter getWriter() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public Locale getLocale() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setBufferSize(int arg0) {
		// TODO Auto-generated method stub

	}

	public int getBufferSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void flushBuffer() throws IOException {
		// TODO Auto-generated method stub

	}

	public void resetBuffer() {
		// TODO Auto-generated method stub

	}

	public boolean isCommitted() {
		// TODO Auto-generated method stub
		return false;
	}

	public void reset() {
		// TODO Auto-generated method stub

	}

	public OutputStream getPortletOutputStream() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public void addProperty(String arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	public void setProperty(String arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	public String encodeURL(String arg0) {
		String toReturn = url+"/"+arg0;
		return toReturn;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
