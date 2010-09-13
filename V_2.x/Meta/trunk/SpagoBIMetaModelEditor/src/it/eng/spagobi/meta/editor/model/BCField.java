/*
 *  Data structure for a Business Class Field
 */
package it.eng.spagobi.meta.editor.model;

public class BCField {

	private String name;
	private String type;
	private BusinessClass bcParent;

    /*
	 * Constructor
	 */	
	public BCField(String n, String t, BusinessClass parent){
		name = n;
		type = t;
		bcParent = parent;
	}
   
	/*
	 * Set field name
	 */	
	public void setName(String name) {
		this.name = name;
	}
	
	/*
	 * Get field name
	 */	
	public String getName() {
		return name;
	}
	
	/*
	 * Set field type
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	/*
	 * Get field type
	 */
	public String getType() {
		return type;
	}
	
	/*
	 * Get Business Class parent of this field
	 */
	public BusinessClass getBcParent() {
		return bcParent;
	}
}
