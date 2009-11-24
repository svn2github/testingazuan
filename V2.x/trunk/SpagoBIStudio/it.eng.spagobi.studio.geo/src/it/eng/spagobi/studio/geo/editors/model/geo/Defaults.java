package it.eng.spagobi.studio.geo.editors.model.geo;

import java.io.Serializable;
import java.util.Vector;

public class Defaults   implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5540121554888127793L;
	private Vector<GuiParam> params;

	public Vector<GuiParam> getParams() {
		return params;
	}

	public void setParams(Vector<GuiParam> params) {
		this.params = params;
	}

}
