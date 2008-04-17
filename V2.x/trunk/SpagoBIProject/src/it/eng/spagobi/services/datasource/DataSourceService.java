package it.eng.spagobi.services.datasource;

import it.eng.spagobi.services.datasource.bo.SpagoBiDataSource;

/**
 * @author Bernabei Angelo
 *
 */
public interface DataSourceService {

    /**
     * 
     * @param token  String
     * @param user String
     * @param documentId String
     * @return SpagoBiDataSource
     */
    SpagoBiDataSource getDataSource(String token,String user,String documentId);
    /**
     * 
     * @param token  String
     * @param user String
     * @param label String
     * @return SpagoBiDataSource
     */
    SpagoBiDataSource getDataSourceByLabel(String token,String user,String label);    
    /**
     * 
     * @param token String
     * @param user String
     * @return SpagoBiDataSource[]
     */
    SpagoBiDataSource[] getAllDataSource(String token,String user);
}
