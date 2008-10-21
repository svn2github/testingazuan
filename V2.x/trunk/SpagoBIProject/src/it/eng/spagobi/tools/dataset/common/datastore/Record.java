/**
 * 
 */
package it.eng.spagobi.tools.dataset.common.datastore;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * @author Angelo Bernabei
 *         angelo.bernabei@eng.it
 */
public class Record implements IRecord {

	List fields = new ArrayList();

    public Record(List fields) {
		super();
		this.fields = fields;
	}
    
    public Record() {
		super();
		this.fields = new ArrayList();
	}


	public IField getFieldById(int position) {
    	
		IField field = (IField)fields.get(position);  	
    	return field;
    }
	
	public void appendField(IField f) {
    	
		fields.add(f);	
    }
	
	public void insertField(int position, IField f) {
    	
		fields.add(position, f);	
    }


    public IField getFieldByName(String name) {
    	
    	IField toReturn = null;
    	Iterator itF=  fields.iterator();
    	while (itF.hasNext()){
    		IField f =(IField)itF.next();
    		String fName = f.getMetadata().getName();
    		if (fName.equals(name)){
    			toReturn = f;
    			break;
    		}
    	}
      return toReturn;
    }


	public List getFields() {
		return this.fields;
	}


	public void setFields(List fields) {
		this.fields = fields;
	}

}
