package it.eng.spagobi.studio.geo.editors.model.geo;

import java.io.Serializable;

public class GuiParam implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4429630117918527159L;
	private String name;
	private String value;
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}
