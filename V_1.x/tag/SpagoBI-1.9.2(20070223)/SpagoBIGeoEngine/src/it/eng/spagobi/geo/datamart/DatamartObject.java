/**
 *
 *	LICENSE: see COPYING file
 *
**/
package it.eng.spagobi.geo.datamart;

import it.eng.spagobi.geo.configuration.MapConfiguration;

import java.util.Map;

/**
 * Contains the data recovered from the datawarehouse and 
 * associated to the svg map
 */
public class DatamartObject{
    
	/**
	 * Mpa of id and values related to the svg map 
	 */
	private Map values = null;

	/**
	 * Constructor
	 */
    public DatamartObject() {
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
    
    /**
     * Recover the svg style associated to a particular id. The method recovers from the
     * data map the value associated to the id and then, using the value, it recovers
     * the svg style associated to the value (from the document template)
     * @param id the id of the element 
     * @param conf The configuration of the map
     * @return the svg style string assocaited to the id 
     */
    public String getStyleForId(String id, MapConfiguration conf) {
    	Integer value = (Integer)values.get(id);
    	if(value!=null) {
    		String style = conf.getStyle(value.intValue());
    		if(style==null) {
    			return " ";
    		} else {
        		return style;
    		}
    	} else {
    		return " ";
    	}
    }
}