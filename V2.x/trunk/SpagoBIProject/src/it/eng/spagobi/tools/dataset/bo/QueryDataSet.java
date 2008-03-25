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
    
    
    
    public QueryDataSet() {
		super();
		// TODO Auto-generated constructor stub
	}
	public QueryDataSet(DataSet a) {
    	setDsId(a.getDsId());
    	setLabel(a.getLabel());
    	setName(a.getName());
    	setDescription(a.getDescription());
	}
    
	public DataSource getDataSource() {
        return dataSource;
    }
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
