/**
 * 
 */
package it.eng.spagobi.tools.dataset.common.datastore;


/**
 * @author Angelo Bernabei
 *         angelo.bernabei@eng.it
 */
public class Field implements IField {

	IFieldMeta metadata = null;
	Object value = null;



    public Field(IFieldMeta metadata, Object value) {
		super();
		this.metadata = metadata;
		this.value = value;
	}
    


	public Field() {
		super();
		// TODO Auto-generated constructor stub
	}



	public Object getValue() {

    	return this.value;
    }


	public void setValue(Object value) {
		this.value = value;
	}


	public IFieldMeta getMetadata() {
		return metadata;
	}


	public void setMetadata(IFieldMeta metadata) {
		this.metadata = metadata;
	}

}
