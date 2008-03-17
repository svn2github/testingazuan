/**
 * 
 */
package it.eng.spagobi.tools.dataset.bo;

/**
 * @author Angelo Bernabei
 *         angelo.bernabei@eng.it
 */
public interface IFieldMetaData {
    
    /**
     * Return the name of the field
     * @return String
     */ 
    String getName();
    
    /**
     * Return the description of the field
     * @return String
     */
    String getDescription();
    
    /**
     * Return the type of the field
     * @return String
     */
    String getType();
    
    /**
     * Return the URL of the field, name used to read data
     * @return String
     */
    String getUrl();
}
