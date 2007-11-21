package it.eng.spagobi.services.proxy;


import it.eng.spagobi.services.datasource.bo.SpagoBiDataSource;
import it.eng.spagobi.services.datasource.stub.DataSourceServiceServiceLocator;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

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
public class DataSourceServiceProxy extends AbstractServiceProxy{
    
    static private Logger logger = Logger.getLogger(DataSourceServiceProxy.class);
    

    public DataSourceServiceProxy(HttpSession session) {
	super(session);
    }
    public DataSourceServiceProxy() {
	super();
    }   
    public SpagoBiDataSource getDataSource(String documentId) {
	logger.debug("IN");
	try {
	    String ticket = "";
	    if (ssoIsActive){
		ticket=readTicket();
	    }
	    DataSourceServiceServiceLocator locator = new DataSourceServiceServiceLocator();
	    it.eng.spagobi.services.datasource.stub.DataSourceService service = locator.getDataSourceService();
	    return service.getDataSource(ticket, documentId);
	} catch (Exception e) {
	    logger.error("Error during Service LookUp",e);
	}finally{
	    logger.debug("OUT");
	}
	return null;
    }

}
