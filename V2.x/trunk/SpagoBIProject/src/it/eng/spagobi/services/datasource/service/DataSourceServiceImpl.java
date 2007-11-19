package it.eng.spagobi.services.datasource.service;

import org.apache.log4j.Logger;

import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spagobi.engines.drivers.jasperreport.JasperReportDriver;
import it.eng.spagobi.services.datasource.DataSourceService;
import it.eng.spagobi.services.datasource.bo.SpagoBiDataSource;
import it.eng.spagobi.services.security.service.ISecurityServiceSupplier;
import it.eng.spagobi.services.security.service.SecurityServiceSupplierFactory;

public class DataSourceServiceImpl implements DataSourceService {
    static private Logger logger = Logger.getLogger(DataSourceServiceImpl.class);
    private DataSourceSupplier supplier=new DataSourceSupplier();
    private String userId="";
    private String validateUrl =null;
    private String validateService =null;
    private boolean activeSso=false;
    
    public DataSourceServiceImpl(){
	logger.debug("IN");
	ConfigSingleton config = ConfigSingleton.getInstance();
	        SourceBean validateSB = (SourceBean)config.getAttribute("SPAGOBI_SSO.VALIDATE-USER.URL");
	validateUrl = (String)validateSB.getCharacters();
	logger.debug("Read validateUrl="+validateUrl);
	        validateSB = (SourceBean)config.getAttribute("SPAGOBI_SSO.VALIDATE-USER.SERVICE");
	validateService = (String)validateSB.getCharacters();
	logger.debug("Read validateService="+validateService);
        validateSB = (SourceBean)config.getAttribute("SPAGOBI_SSO.ACTIVE");
	String active = (String)validateSB.getCharacters();
	if (active!=null && active.equals("true")) activeSso=true;
	logger.debug("Read activeSso="+activeSso);
    }
    public SpagoBiDataSource getDataSource(String token, String documentLabel,
	    String engineLabel) {
	return supplier.getDataSource(documentLabel, engineLabel);
    }

}
