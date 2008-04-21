/**
 *
 *	LICENSE: see COPYING file
 *
**/
package it.eng.spagobi.engines.geo.dataset;



import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class DataSet {
    
	private DataSetMetaData metaData;
	
	public DataSetMetaData getMetaData() {
		return metaData;
	}
	
	
	
	
	
	
	
	
	
	
	
	private String targetFeatureName;
	
	private String[] kpiNames;
	private int selectedKpi;
	
	Map orderedKpiValuesMap = null;
	
	/**
	 * Map of id and values related to the svg map 
	 */
	private Map values = null;

	/**
	 * Map of id and values related to the svg map 
	 */
	private Map links = null;
	
	/**
	 * Constructor
	 */
    public DataSet() {
        super();
    }

    /**
     * Returns the data map 
     * @return the data map 
     */
	public Map getValues() {
		return values;
	}

	/**
	 * Sets the data map
	 * @param data map of data recovered from datawarehouse
	 */
	public void setValues(Map data) {
		this.values = data;
	}
    
	/**
	 * checks if the data map contains an id (as a key)
	 * @param id the id to check
	 * @return true if the id is contained, falses otherwise
	 */
    public boolean hasId(String id) {
    	return values.containsKey(id);
    }
    
    public Map getAttributeseById(String id) {
    	return (Map)values.get(id);
    }
    
    
    
    /**
     * Recover the detail document link associated to a particular id. The method recovers from the
     * data map the value link associated to the id 
     * @param id the id of the element 
     * @return the document detail link associated to the id 
     */
    public String getLinkForId(String id) {
    	String link = (String)links.get(id);
    	return link;
    }
    
    
    /**
     * Returns a list of couples id - link (map)
     * @return map of the link associated to ids
     */
	public Map getLinks() {
		return links;
	}

	/**
	 * Sets the map of associations between ids and links
	 * @param links the associations map
	 */
	public void setLinks(Map links) {
		this.links = links;
	}

	public String getTargetFeatureName() {
		return targetFeatureName;
	}

	public void setTargetFeatureName(String targetFeatureName) {
		this.targetFeatureName = targetFeatureName;
	}

	public String[] getKpiNames() {
		return kpiNames;
	}

	public void setKpiNames(String[] kpiNames) {
		this.kpiNames = kpiNames;
	}

	public int getSelectedKpi() {
		return selectedKpi;
	}

	public void setSelectedKpi(int selectedKpi) {
		this.selectedKpi = selectedKpi;
	}

	
	public Map getOrderedKpiValuesMap() {
		return orderedKpiValuesMap;
	}

	public void setOrderedKpiValuesMap(Map orderedKpiValuesMap) {
		this.orderedKpiValuesMap = orderedKpiValuesMap;
	}
	
	public Set getOrderedKpiValuesSet(String kpiName) {
		return (Set)orderedKpiValuesMap.get(kpiName);
	}

	
    
}