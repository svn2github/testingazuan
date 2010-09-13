/*
 *  Data structure for a Business Model
 */

package it.eng.spagobi.meta.editor.model;

import java.util.ArrayList;
import java.util.Collection;

public class BusinessModel {
	
	private String name;
	private Collection<BusinessClass> bcCollection;

    /*
	 * Constructor
	 */
	public BusinessModel(String bmName){
		name = bmName;
		bcCollection = new ArrayList<BusinessClass>();
	}
    
	/*
	 * Empty Constructor
	 */
	public BusinessModel(){

	}
	/*
	 * Add a Business Class to the Business Model
	 */
	public void addBc(BusinessClass bc) {
		bcCollection.add(bc);
	}
	
	/*
	 * Remove a Business Class to the Business Model
	 */
	public void removeBc(BusinessClass bc) {
		bcCollection.remove(bc);
	}
	
	/*
	 * Get the Business Classes collection inside the Business Model
	 */
	public Collection<BusinessClass> getBcCollection() {
		return bcCollection;
	}
	
	/*
	 * Set Business Model name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/*
	 * get Business Model name
	 */
	public String getName() {
		return name;
	}
	
	
}
