package it.eng.spagobi.services.datasource.service;

import it.eng.spagobi.analiticalmodel.document.bo.BIObject;
import it.eng.spagobi.commons.bo.Domain;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.dao.IDomainDAO;
import it.eng.spagobi.services.datasource.bo.SpagoBiDataSource;
import it.eng.spagobi.tools.datasource.bo.DataSource;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

public class DataSourceSupplier {
    static private Logger logger = Logger.getLogger(DataSourceSupplier.class);

    public SpagoBiDataSource getDataSource(String documentId) {
	logger.debug("IN.documentId:" + documentId);

	SpagoBiDataSource sbds = null;
	if (documentId == null)
	    return null;

	// gets data source data from database
	try {
	    BIObject obj = DAOFactory.getBIObjectDAO().loadBIObjectById(Integer.valueOf(documentId));
	    if (obj == null) {
		logger.error("The object with id " + documentId + " is not found on the database.");
		return null;
	    }
	    DataSource ds = DAOFactory.getDataSourceDAO().loadDataSourceByID(obj.getDataSourceId());
	    if (ds == null) {
		logger.error("The data source with id " + obj.getDataSourceId() + " is not found on the database.");
		return null;
	    }
	    
	    Domain dialectHB = DAOFactory.getDomainDAO().loadDomainById(ds.getDialectId());
	    if (ds == null) {
		logger.error("The data source with id " + obj.getDataSourceId() + " is not found on the database.");
		return null;
	    }	    
	    sbds = new SpagoBiDataSource();
	    sbds.setLabel(ds.getLabel());
	    sbds.setJndiName(ds.getJndi());
	    sbds.setUrl(ds.getUrlConnection());
	    sbds.setUser(ds.getUser());
	    sbds.setPassword(ds.getPwd());
	    sbds.setDriver(ds.getDriver());
	    sbds.setHibDialectName(dialectHB.getValueName());
	    sbds.setHibDialectClass(dialectHB.getValueDescription());
	    
	    logger.info("read DS: Label="+sbds.getLabel()+" Jndi="+sbds.getJndiName()+" HIB="+sbds.getHibDialectClass());
	    
	    //gets dialect informations
	    IDomainDAO domaindao = DAOFactory.getDomainDAO();
	    Domain doDialect = domaindao.loadDomainById(ds.getDialectId());
	    sbds.setHibDialectClass(doDialect.getValueDescription());
	    sbds.setHibDialectName(doDialect.getValueName());

	} catch (Exception e) {
	    logger.error("The data source is not correctly returned", e);
	    sbds=null;

	}
	logger.debug("OUT");
	return sbds;
    }

    public SpagoBiDataSource getDataSourceByLabel(String dsLabel) {
	logger.debug("IN");
	SpagoBiDataSource sbds = new SpagoBiDataSource();

	// gets data source data from database
	try {
	    DataSource ds = DAOFactory.getDataSourceDAO().loadDataSourceByLabel(dsLabel);
	    if (ds == null) {
		logger.warn("The data source with label " + dsLabel + " is not found on the database.");
		return null;
	    }
	    sbds.setLabel(ds.getLabel());
	    sbds.setJndiName(ds.getJndi());
	    sbds.setUrl(ds.getUrlConnection());
	    sbds.setUser(ds.getUser());
	    sbds.setPassword(ds.getPwd());
	    sbds.setDriver(ds.getDriver());
	    
	  //gets dialect informations
	    IDomainDAO domaindao = DAOFactory.getDomainDAO();
	    Domain doDialect = domaindao.loadDomainById(ds.getDialectId());
	    sbds.setHibDialectClass(doDialect.getValueDescription());
	    sbds.setHibDialectName(doDialect.getValueName());
	    
	} catch (Exception e) {
	    logger.error("The data source is not correctly returned", e);
	}
	logger.debug("OUT");
	return sbds;
    }

    public SpagoBiDataSource[] getAllDataSource() {
	logger.debug("IN");
	ArrayList tmpList = new ArrayList();

	// gets all data source from database
	try {
	    List lstDs = DAOFactory.getDataSourceDAO().loadAllDataSources();
	    if (lstDs == null) {
		logger.warn("Data sources aren't found on the database.");
		return null;
	    }

	    Iterator dsIt = lstDs.iterator();
	    while (dsIt.hasNext()) {
		DataSource ds = (DataSource) dsIt.next();
		SpagoBiDataSource sbds = new SpagoBiDataSource();
		sbds.setJndiName(ds.getJndi());
		sbds.setUrl(ds.getUrlConnection());
		sbds.setUser(ds.getUser());
		sbds.setPassword(ds.getPwd());
		sbds.setDriver(ds.getDriver());
		//gets dialect informations
	    IDomainDAO domaindao = DAOFactory.getDomainDAO();
	    Domain doDialect = domaindao.loadDomainById(ds.getDialectId());
	    sbds.setHibDialectClass(doDialect.getValueDescription());
	    sbds.setHibDialectName(doDialect.getValueName());
	    
		tmpList.add(sbds);
	    }
	} catch (Exception e) {
	    logger.error("The data sources are not correctly returned", e);
	}
	// mapping generic array list into array of SpagoBiDataSource objects
	SpagoBiDataSource[] arDS = new SpagoBiDataSource[tmpList.size()];
	for (int i = 0; i < tmpList.size(); i++) {
	    arDS[i] = (SpagoBiDataSource) tmpList.get(i);
	}
	logger.debug("OUT");
	return arDS;
    }
}
