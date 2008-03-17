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
public interface IPojoDataSet extends IDataSet{

    /**
     * Return a List that contain object that implements IDataSetRow
     * @return List of IDataSetRow
     */
    List getAllRows();
    
    /**
     * Return an Iterator for scroll the element of a DataSet.
     * @return Iterator  of IDataSetRow
     */
    Iterator getIterator();    
}
