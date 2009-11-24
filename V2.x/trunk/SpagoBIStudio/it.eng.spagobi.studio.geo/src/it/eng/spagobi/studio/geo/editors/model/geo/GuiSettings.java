package it.eng.spagobi.studio.geo.editors.model.geo;

import java.io.Serializable;
import java.util.Vector;

public class GuiSettings   implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6664124887478223504L;
	private Vector<GuiParam> params;
	private Windows windows;
	public Vector<GuiParam> getParams() {
		return params;
	}
	public void setParams(Vector<GuiParam> params) {
		this.params = params;
	}
	public Windows getWindows() {
		return windows;
	}
	public void setWindows(Windows windows) {
		this.windows = windows;
	}

}
