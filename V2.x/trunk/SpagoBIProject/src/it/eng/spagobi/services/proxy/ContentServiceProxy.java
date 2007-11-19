package it.eng.spagobi.services.proxy;

import it.eng.spagobi.services.content.bo.Content;
import it.eng.spagobi.services.content.stub.ContentServiceServiceLocator;

import java.io.IOException;

import javax.servlet.http.HttpSession;

import edu.yale.its.tp.cas.client.CASReceipt;
import edu.yale.its.tp.cas.client.filter.CASFilter;
import edu.yale.its.tp.cas.proxy.ProxyTicketReceptor;

public class ContentServiceProxy {

    private HttpSession session = null;

    private String readTicket() throws IOException {
	CASReceipt cr = (CASReceipt) session
		.getAttribute(CASFilter.CAS_FILTER_RECEIPT);
	return ProxyTicketReceptor.getProxyTicket(cr.getPgtIou(),
		"https://localhost:8443/SpagoBI/proxyReceptor");

    }

    public ContentServiceProxy() {

    }

    public ContentServiceProxy(HttpSession session) {
	this.session = session;
    }

    public Content readTemplate(String user, String document) {
	try {
	    // String ticket=readTicket();
	    String ticket = "";
	    ContentServiceServiceLocator locator = new ContentServiceServiceLocator();
	    it.eng.spagobi.services.content.stub.ContentService service = locator
		    .getContentService();
	    return service.readTemplate(ticket, user, document);
	} catch (Exception e) {
	    System.err.println(e.toString());

	}
	return null;
    }

}
