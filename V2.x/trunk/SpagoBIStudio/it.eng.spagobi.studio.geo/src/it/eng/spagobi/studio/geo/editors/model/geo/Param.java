package it.eng.spagobi.studio.geo.editors.model.geo;

import java.io.Serializable;

public class Param  implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7558764620485105790L;
	private String name;
	private String value;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
}
