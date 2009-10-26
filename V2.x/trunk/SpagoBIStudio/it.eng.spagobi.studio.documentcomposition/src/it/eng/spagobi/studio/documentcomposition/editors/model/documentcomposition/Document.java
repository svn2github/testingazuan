package it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition;

import java.util.Vector;



public class Document {
	
	private String sbiObjLabel;
	private Parameters parameters;//parameters
	private Style style; 
	
	public Style getStyle() {
		return style;
	}
	public void setStyle(Style style) {
		this.style = style;
	}
	

	public Parameters getParameters() {
		return parameters;
	}
	public void setParameters(Parameters parameters) {
		this.parameters = parameters;
	}
	public String getSbiObjLabel() {
		return sbiObjLabel;
	}
	public void setSbiObjLabel(String sbiObjLabel) {
		this.sbiObjLabel = sbiObjLabel;
	}


}
