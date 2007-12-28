package it.eng.spagobi.services.datasource.service;

import it.eng.spagobi.services.common.AbstractServiceImpl;
import it.eng.spagobi.services.datasource.DataSourceService;
import it.eng.spagobi.services.datasource.bo.SpagoBiDataSource;
import it.eng.spagobi.services.security.exceptions.SecurityException;

import org.apache.log4j.Logger;

public class DataSourceServiceImpl extends AbstractServiceImpl implements DataSourceService {
    static private Logger logger = Logger.getLogger(DataSourceServiceImpl.class);
    private DataSourceSupplier supplier=new DataSourceSupplier();

    

    public DataSourceServiceImpl(){
	super();
    }
    public SpagoBiDataSource getDataSource(String token,String user, String documentId) {
	logger.debug("IN");
	try {
	    validateTicket(token, user);
	    return supplier.getDataSource(documentId);
	} catch (SecurityException e) {
	    logger.error("SecurityException", e);
	    return null;
	} finally {
	    logger.debug("OUT");
	}	

    }
    public SpagoBiDataSource[] getAllDataSource(String token,String user){
	logger.debug("IN");
	try {
	    validateTicket(token, user);
	    return supplier.getAllDataSource();
	} catch (SecurityException e) {
	    logger.error("SecurityException", e);
	    return null;
	} finally {
	    logger.debug("OUT");
	}	

    }
}
