package it.eng.spagobi.studio.geo.editors.model.geo;

import java.io.Serializable;
import java.util.Vector;

public class Window   implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2076909374067636713L;


	private String name;
	private Vector<GuiParam> params;


	public Vector<GuiParam> getParams() {
		return params;
	}

	public void setParams(Vector<GuiParam> params) {
		this.params = params;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
