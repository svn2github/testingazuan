package it.eng.spagobi.studio.geo.editors.model.geo;


import java.io.Serializable;
import java.util.Vector;

public class Labels implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6947472784492592874L;

	private Vector<Label> label;

	public Vector<Label> getLabel() {
		return label;
	}

	public void setLabel(Vector<Label> label) {
		this.label = label;
	}
}
