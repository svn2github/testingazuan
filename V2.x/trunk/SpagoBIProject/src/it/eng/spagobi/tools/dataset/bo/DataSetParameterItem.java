package it.eng.spagobi.tools.dataset.bo;

public class DataSetParameterItem {

	private String name= "" ;
	private String type = "";
	
	/**
	 * Returns the name.
	 * @return the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * Sets the name
	 * @param The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * Returns the type.
	 * @return the type.
	 */
	public String getType() {
		return type;
	}
	/**
	 * Sets the type
	 * @param name The type to set.
	 */
	public void setType(String type) {
		this.type = type;
	}
	
}
