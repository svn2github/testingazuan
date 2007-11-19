package it.eng.spagobi.services.datasource.service;

import it.eng.spagobi.services.datasource.DataSourceService;
import it.eng.spagobi.services.datasource.bo.SpagoBiDataSource;

public class DataSourceServiceImpl implements DataSourceService {

    public SpagoBiDataSource getDataSource(String token, String documentLabel,
	    String engineLabel) {
	SpagoBiDataSource tmp=new SpagoBiDataSource();
	tmp.setDriver("org.driver");
	tmp.setJndiName("jndi/pippo");
	return tmp;
    }

}
