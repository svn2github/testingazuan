package it.eng.spagobi.services.datasource;

import it.eng.spagobi.services.datasource.bo.SpagoBiDataSource;

/**
 * @author Bernabei Angelo
 *
 */
public interface DataSourceService {

    SpagoBiDataSource getDataSource(String token,String documentId);
    
    SpagoBiDataSource[] getAllDataSource(String token);
}
