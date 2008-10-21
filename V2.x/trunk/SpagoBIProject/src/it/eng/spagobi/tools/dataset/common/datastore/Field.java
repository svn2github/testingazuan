/**
 * 
 */
package it.eng.spagobi.tools.dataset.common.datastore;


/**
 * @author Angelo Bernabei
 *         angelo.bernabei@eng.it
 */
public class Field implements IField {

	Object value = null;

    public Field(Object value) {
		super();
		this.value = value;
	}


	public IFieldMeta getFieldMeta() {
	// TODO Auto-generated method stub
	return null;
    }


    public Object getValue() {

    	return this.value;
    }


	public void setValue(Object value) {
		this.value = value;
	}

}
