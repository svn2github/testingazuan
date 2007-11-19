package it.eng.spagobi.services.proxy;


import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spagobi.services.datasource.bo.SpagoBiDataSource;
import it.eng.spagobi.services.datasource.stub.DataSourceServiceServiceLocator;

import java.io.IOException;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import edu.yale.its.tp.cas.client.CASReceipt;
import edu.yale.its.tp.cas.client.filter.CASFilter;
import edu.yale.its.tp.cas.proxy.ProxyTicketReceptor;

/**
 * This is a proxy for using the Data Source Service:
 * Ex.
 * 
	DataSourceServiceProxy proxyDS=new DataSourceServiceProxy();
	SpagoBiDataSource ds=proxyDS.getDataSource("doc", "label");
	logger.debug("DS="+ds.getJndiName());
	
 * @author Bernabei Angelo
 *
 */
public class DataSourceServiceProxy {
    static private Logger logger = Logger.getLogger(DataSourceServiceProxy.class);
    
    private HttpSession session = null;
    private boolean activeSso=false;
    
    private String readTicket() throws IOException {
	CASReceipt cr = (CASReceipt) session
		.getAttribute(CASFilter.CAS_FILTER_RECEIPT);
	return ProxyTicketReceptor.getProxyTicket(cr.getPgtIou(),
		"https://localhost:8443/SpagoBI/proxyReceptor");

    }

    public DataSourceServiceProxy() {
	ConfigSingleton config = ConfigSingleton.getInstance();
        SourceBean validateSB = (SourceBean)config.getAttribute("SPAGOBI_SSO.ACTIVE");
        String active = (String)validateSB.getCharacters();
        if (active!=null && active.equals("true")) activeSso=true;
        logger.debug("Read activeSso="+activeSso);
    }

    public DataSourceServiceProxy(HttpSession session) {
	this.session = session;
    }

    public SpagoBiDataSource getDataSource(String documentLabel,String engineLabel) {
	logger.debug("IN");
	try {
	    String ticket = "";
	    if (activeSso){
		ticket=readTicket();
	    }
	    DataSourceServiceServiceLocator locator = new DataSourceServiceServiceLocator();
	    it.eng.spagobi.services.datasource.stub.DataSourceService service = locator.getDataSourceService();
	    return service.getDataSource(ticket, documentLabel,engineLabel);
	} catch (Exception e) {
	    logger.error("Errore during Service LookUp",e);

	}finally{
	    logger.debug("OUT");
	}
	return null;
    }

}
