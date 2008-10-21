/**
 * 
 */
package it.eng.spagobi.tools.dataset.common.datastore;

/**
 * @author Angelo Bernabei
 *         angelo.bernabei@eng.it
 */
public interface IField {
    Object getValue();
    IFieldMeta getFieldMeta();
    public void setValue(Object value) ;
}
