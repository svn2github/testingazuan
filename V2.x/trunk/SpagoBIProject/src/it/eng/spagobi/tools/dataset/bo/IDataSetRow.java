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
public interface IDataSetRow {

    /**
     * Return the List of elements
     * @return List of IField
     */
    List getList();
    
    /**
     * Return the single element of the i position
     * @param i int
     * @return String
     */
    IField getElement(int i);
    
    /**
     * Return an Iterator for scroll the element of a row.
     * @return Iterator of IField
     */
    Iterator getIterator();
}
