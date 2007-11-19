package it.eng.spagobi.services.datasource.service;

import it.eng.spagobi.services.datasource.bo.SpagoBiDataSource;

public class DataSourceSupplier {

    public SpagoBiDataSource getDataSource( String documentLabel,String engineLabel) {
	SpagoBiDataSource tmp=new SpagoBiDataSource();
	tmp.setDriver("org.driver");
	tmp.setJndiName("jndi/pippo");
	return tmp;
    }
    
}
