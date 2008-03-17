/**
 * 
 */
package it.eng.spagobi.tools.dataset.bo;

import java.util.Iterator;
import java.util.List;

/**
 * @author Angelo Bernabei
 *         angelo.bernabei@eng.it
 */
public interface IDataSetMetaData {
    
    /**
     * Return the name of the DataSet
     * @return String
     */
    String getDataSetName();
    
    /**
     * Return the information of all fields.
     * @return List of IFieldMetaData
     */
    List getAllMetaData();
    
    /**
     * Return an Iterator for scroll the element of a row.
     * @return Iterator of IFieldMetaData
     */
    Iterator getIterator();    
    
}
