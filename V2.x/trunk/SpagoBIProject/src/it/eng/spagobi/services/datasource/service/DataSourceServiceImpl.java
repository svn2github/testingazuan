package it.eng.spagobi.services.datasource.service;

import it.eng.spagobi.services.common.AbstractServiceImpl;
import it.eng.spagobi.services.datasource.DataSourceService;
import it.eng.spagobi.services.datasource.bo.SpagoBiDataSource;
import it.eng.spagobi.services.security.exceptions.SecurityException;

import org.apache.log4j.Logger;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;

/**
 * Provide the Data Source information
 */
public class DataSourceServiceImpl extends AbstractServiceImpl implements DataSourceService {
    static private Logger logger = Logger.getLogger(DataSourceServiceImpl.class);
    private DataSourceSupplier supplier=new DataSourceSupplier();

    

    /**
     * 
     */
    public DataSourceServiceImpl(){
	super();
    }
    /**
     * 
     * @param token  String
     * @param user String
     * @param documentId String
     * @return SpagoBiDataSource
     */
    public SpagoBiDataSource getDataSource(String token,String user, String documentId) {
	logger.debug("IN");
	Monitor monitor =MonitorFactory.start("spagobi.service.datasource.getDataSource");
	try {
	    validateTicket(token, user);
	    return supplier.getDataSource(documentId);
	} catch (SecurityException e) {
	    logger.error("SecurityException", e);
	    return null;
	} finally {
	    monitor.stop();
	    logger.debug("OUT");
	}	

    }
    /**
     * 
     * @param token  String
     * @param user String
     * @param label String
     * @return SpagoBiDataSource
     */    
    public SpagoBiDataSource getDataSourceByLabel(String token,String user,String label){
	logger.debug("IN");
	Monitor monitor =MonitorFactory.start("spagobi.service.datasource.getDataSource");
	try {
	    validateTicket(token, user);
	    return supplier.getDataSourceByLabel(label);
	} catch (SecurityException e) {
	    logger.error("SecurityException", e);
	    return null;
	} finally {
	    monitor.stop();
	    logger.debug("OUT");
	}	
    }
    /**
     * 
     * @param token String
     * @param user String
     * @return SpagoBiDataSource[]
     */
    public SpagoBiDataSource[] getAllDataSource(String token,String user){
	logger.debug("IN");
	Monitor monitor =MonitorFactory.start("spagobi.service.datasource.getAllDataSource");
	try {
	    validateTicket(token, user);
	    return supplier.getAllDataSource();
	} catch (SecurityException e) {
	    logger.error("SecurityException", e);
	    return null;
	} finally {
	    monitor.stop();
	    logger.debug("OUT");
	}	

    }
}
