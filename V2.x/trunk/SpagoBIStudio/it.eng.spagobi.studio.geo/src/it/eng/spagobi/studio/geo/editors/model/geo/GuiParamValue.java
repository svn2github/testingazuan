package it.eng.spagobi.studio.geo.editors.model.geo;

import java.io.Serializable;

public class GuiParamValue implements Serializable{
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 6767368298875922579L;
	private String value;

}
