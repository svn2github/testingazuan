/*
 *  Data structure for a Business Class
 */
package eng.it.spagobimeta.model;

import java.util.ArrayList;
import java.util.Collection;

public class BusinessClass {
	
	private String name;
	private Collection<BCField> fieldsCollection; 
	private BusinessModel bmParent;

    /*
	 * Constructor
	 */
	public BusinessClass(String bcName, BusinessModel parent){
		name = bcName;
		fieldsCollection = new ArrayList<BCField>();
		bmParent = parent;
	}
	
	/*
	 * Add a field to the Business Class
	 */
	public void addField(BCField field) {
		fieldsCollection.add(field);
	}
	
	/*
	 * Remove a field to the Business Class
	 */
	public void removeBc(BCField field) {
		fieldsCollection.remove(field);
	}	

	/*
	 * Get the fields collection inside the Business Class
	 */
	public Collection<BCField> getFieldsCollection() {
		return fieldsCollection;
	}
	
	/*
	 * Set Business Class name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/*
	 * Get Business Class name
	 */
	public String getName() {
		return name;
	}
	
	/*
	 * Get Business Model parent of this Business Class
	 */
	public BusinessModel getBmParent() {
		return bmParent;
	}
}
