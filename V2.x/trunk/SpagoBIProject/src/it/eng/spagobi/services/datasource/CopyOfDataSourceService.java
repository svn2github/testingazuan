package it.eng.spagobi.services.datasource;

import it.eng.spagobi.services.datasource.bo.SpagoBiDataSource;

/**
 * @author Bernabei Angelo
 *
 */
public interface CopyOfDataSourceService {

    SpagoBiDataSource getDataSource(String token,String user,String documentId);
    
    SpagoBiDataSource[] getAllDataSource(String token,String user);
}
