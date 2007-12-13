package it.eng.spagobi.services.proxy;


import it.eng.spagobi.services.datasource.bo.SpagoBiDataSource;
import it.eng.spagobi.services.datasource.stub.DataSourceServiceServiceLocator;
import it.eng.spagobi.services.security.exceptions.SecurityException;

import javax.servlet.http.HttpSession;
import javax.xml.rpc.ServiceException;

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
    private DataSourceServiceProxy() {
	super();
    }   
    
    private it.eng.spagobi.services.datasource.stub.DataSourceService lookUp() throws SecurityException {
	try {
	    DataSourceServiceServiceLocator locator = new DataSourceServiceServiceLocator();
	    it.eng.spagobi.services.datasource.stub.DataSourceService service=null;
	    if (serviceUrl!=null ){
		    service = locator.getDataSourceService(serviceUrl);		
	    }else {
		    service = locator.getDataSourceService();		
	    }
	    return service;
	} catch (ServiceException e) {
	    logger.error("Error during service execution", e);
	    throw new SecurityException();
	}
    }
    
    
    public SpagoBiDataSource getDataSource(String user,String documentId) {
	logger.debug("IN");
	try {
	    String ticket = "";
	    if (ssoIsActive){
		ticket=readTicket();
	    }

	    return lookUp().getDataSource(ticket, user,documentId);
	} catch (Exception e) {
	    logger.error("Error during Service LookUp",e);
	}finally{
	    logger.debug("OUT");
	}
	return null;
    }

}
