/**
 * 
 */
package it.eng.spagobi.tools.dataset.bo;

import it.eng.spagobi.tools.datasource.bo.DataSource;

/**
 * @author Angelo Bernabei
 *         angelo.bernabei@eng.it
 */
public class QueryDataSet extends DataSet {
    private String query=null;
    private DataSource dataSource=null;
    public String getQuery() {
        return query;
    }
    public void setQuery(String query) {
        this.query = query;
    }
    public DataSource getDataSource() {
        return dataSource;
    }
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
