package it.eng.spagobi.services.datasource.service;


import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.services.datasource.bo.SpagoBiDataSource;
import it.eng.spagobi.tools.datasource.bo.DataSource;
import it.eng.spagobi.analiticalmodel.document.bo.BIObject;
import org.apache.log4j.Logger;

public class DataSourceSupplier {
	static private Logger logger = Logger.getLogger(DataSourceSupplier.class);
	
    public SpagoBiDataSource getDataSource( String documentLabel,String engineLabel) {
        
		SpagoBiDataSource sbds = new SpagoBiDataSource();  
    		
    	//gets data source data from database
		try{
			BIObject obj = DAOFactory.getBIObjectDAO().loadBIObjectById(Integer.valueOf(documentLabel));
			if (obj == null){
				logger.warn("The object with id " + documentLabel + " is not found on the database.");
				return null;
			}
			DataSource ds = DAOFactory.getDataSourceDAO().loadDataSourceByID(obj.getDataSourceId());
			if (ds == null){
				logger.warn("The data source with id " + obj.getDataSourceId() + " is not found on the database.");
				return null;
			}
			sbds.setJndiName(ds.getJndi());
			sbds.setUrl(ds.getUrlConnection());
			sbds.setUser(ds.getUser());
			sbds.setPassword(ds.getPwd());
			sbds.setDriver(ds.getDriver());
		}catch (Exception e){
			logger.error("The data source is not correctly returned", e);
		}
    	return sbds;
    }
    
    public SpagoBiDataSource[] getAllDataSource(){

    	return null;
    }    
}
