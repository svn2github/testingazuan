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
    public SpagoBiDataSource getDataSource(String token, String documentLabel,
	    String engineLabel) {
	logger.debug("IN");
	if (activeSso){
		try {
		    if (validateTicket(token)){
			return supplier.getDataSource(documentLabel, engineLabel);
		    }else{
			logger.error("Token NOT VALID");
			return null;
		    }
		} catch (SecurityException e) {
		    e.printStackTrace();
		    return null;
		}finally{
		    logger.debug("OUT");
		}
	}else {
	logger.debug("OUT");
	return supplier.getDataSource(documentLabel, engineLabel);
	}
    }

}
