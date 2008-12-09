/**
 * SpagoBI - The Business Intelligence Free Platform
 *
 * Copyright (C) 2004 - 2008 Engineering Ingegneria Informatica S.p.A.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 **/
package it.eng.spagobi.engines.geo.dataset;



import java.util.Map;
import java.util.Set;

// TODO: Auto-generated Javadoc
/**
 * The Class DataSet.
 * 
 * @author Andrea Gioia (andrea.gioia@eng.it)
 */
public class DataSet {
    
	/** The meta data. */
	private DataSetMetaData metaData;
	
	/**
	 * Gets the meta data.
	 * 
	 * @return the meta data
	 */
	public DataSetMetaData getMetaData() {
		return metaData;
	}
	
	
	
	
	
	
	
	
	
	
	
	/** The target feature name. */
	private String targetFeatureName;
	
	/** The kpi names. */
	private String[] kpiNames;
	
	/** The selected kpi. */
	private int selectedKpi;
	
	/** The ordered kpi values map. */
	Map orderedKpiValuesMap = null;
	
	/** Map of id and values related to the svg map. */
	private Map values = null;

	/** Map of id and values related to the svg map. */
	private Map links = null;
	
	/**
	 * Constructor.
	 */
    public DataSet() {
        super();
    }

    /**
     * Returns the data map.
     * 
     * @return the data map
     */
	public Map getValues() {
		return values;
	}

	/**
	 * Sets the data map.
	 * 
	 * @param data map of data recovered from datawarehouse
	 */
	public void setValues(Map data) {
		this.values = data;
	}
    
	/**
	 * checks if the data map contains an id (as a key).
	 * 
	 * @param id the id to check
	 * 
	 * @return true if the id is contained, falses otherwise
	 */
    public boolean hasId(String id) {
    	return values.containsKey(id);
    }
    
    /**
     * Gets the attributese by id.
     * 
     * @param id the id
     * 
     * @return the attributese by id
     */
    public Map getAttributeseById(String id) {
    	return (Map)values.get(id);
    }
    
    
    
    /**
     * Recover the detail document link associated to a particular id. The method recovers from the
     * data map the value link associated to the id
     * 
     * @param id the id of the element
     * 
     * @return the document detail link associated to the id
     */
    public String getLinkForId(String id) {
    	String link = (String)links.get(id);
    	return link;
    }
    
    
    /**
     * Returns a list of couples id - link (map).
     * 
     * @return map of the link associated to ids
     */
	public Map getLinks() {
		return links;
	}

	/**
	 * Sets the map of associations between ids and links.
	 * 
	 * @param links the associations map
	 */
	public void setLinks(Map links) {
		this.links = links;
	}

	/**
	 * Gets the target feature name.
	 * 
	 * @return the target feature name
	 */
	public String getTargetFeatureName() {
		return targetFeatureName;
	}

	/**
	 * Sets the target feature name.
	 * 
	 * @param targetFeatureName the new target feature name
	 */
	public void setTargetFeatureName(String targetFeatureName) {
		this.targetFeatureName = targetFeatureName;
	}

	/**
	 * Gets the kpi names.
	 * 
	 * @return the kpi names
	 */
	public String[] getKpiNames() {
		return kpiNames;
	}

	/**
	 * Sets the kpi names.
	 * 
	 * @param kpiNames the new kpi names
	 */
	public void setKpiNames(String[] kpiNames) {
		this.kpiNames = kpiNames;
	}

	/**
	 * Gets the selected kpi.
	 * 
	 * @return the selected kpi
	 */
	public int getSelectedKpi() {
		return selectedKpi;
	}

	/**
	 * Sets the selected kpi.
	 * 
	 * @param selectedKpi the new selected kpi
	 */
	public void setSelectedKpi(int selectedKpi) {
		this.selectedKpi = selectedKpi;
	}

	
	/**
	 * Gets the ordered kpi values map.
	 * 
	 * @return the ordered kpi values map
	 */
	public Map getOrderedKpiValuesMap() {
		return orderedKpiValuesMap;
	}

	/**
	 * Sets the ordered kpi values map.
	 * 
	 * @param orderedKpiValuesMap the new ordered kpi values map
	 */
	public void setOrderedKpiValuesMap(Map orderedKpiValuesMap) {
		this.orderedKpiValuesMap = orderedKpiValuesMap;
	}
	
	/**
	 * Gets the ordered kpi values set.
	 * 
	 * @param kpiName the kpi name
	 * 
	 * @return the ordered kpi values set
	 */
	public Set getOrderedKpiValuesSet(String kpiName) {
		return (Set)orderedKpiValuesMap.get(kpiName);
	}

	
    
}