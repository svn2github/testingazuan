/**
 * 
 */
package it.eng.spagobi.tools.dataset.bo;

/**
 * @author Angelo Bernabei
 *         angelo.bernabei@eng.it
 */
public interface IField {

    /**
     * Metadata of this field
     * @return IFieldMetaData
     */
    IFieldMetaData getFieldMetaData();
    
    /**
     * Return value of this filed
     * @return Object
     */
    Object getValue();
}
