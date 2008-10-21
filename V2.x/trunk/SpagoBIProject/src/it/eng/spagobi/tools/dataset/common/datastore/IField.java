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
    public IFieldMeta getMetadata();
	public void setMetadata(IFieldMeta metadata);
    public void setValue(Object value) ;
}
