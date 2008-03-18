/**
 * 
 */
package it.eng.spagobi.tools.dataset.metadata;

import it.eng.spagobi.tools.datasource.metadata.SbiDataSource;

/**
 * @author Angelo Bernabei
 *         angelo.bernabei@eng.it
 */
public class SbiQueryDataSet extends SbiDataSet{
    private String query=null;
    private SbiDataSource dataSource=null;
    
    public String getQuery() {
        return query;
    }
    public void setQuery(String query) {
        this.query = query;
    }
    public SbiDataSource getDataSource() {
        return dataSource;
    }
    public void setDataSource(SbiDataSource dataSource) {
        this.dataSource = dataSource;
    }
}
