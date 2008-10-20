/**
 * 
 */
package it.eng.spagobi.tools.dataset.common.datastore;

/**
 * @author Angelo Bernabei
 *         angelo.bernabei@eng.it
 */
public interface IRecord {

    IField getFieldByName(String name);
    IField getFieldById(int position);
    
}
