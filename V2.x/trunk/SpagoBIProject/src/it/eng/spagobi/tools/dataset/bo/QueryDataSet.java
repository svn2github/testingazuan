/**
 * 
 */
package it.eng.spagobi.tools.dataset.bo;

import java.util.ArrayList;
import java.util.List;

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
    
    
	/**
	 * Gets the list of names of the profile attributes required
	 * @return list of profile attribute names
	 * @throws Exception
	 */
	public List getProfileAttributeNames() throws Exception {
		List names = new ArrayList();
		String query = getQuery();
		while(query.indexOf("${")!=-1) {
			int startind = query.indexOf("${");
			int endind = query.indexOf("}", startind);
			String attributeDef = query.substring(startind + 2, endind);
			if(attributeDef.indexOf("(")!=-1) {
				int indroundBrack = query.indexOf("(", startind);
				String nameAttr = query.substring(startind+2, indroundBrack);
				names.add(nameAttr);
			} else {
				names.add(attributeDef);
			}
			query = query.substring(endind);
		}
		return names;
	}
	
    
}
