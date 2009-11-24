package it.eng.spagobi.studio.geo.editors.model.geo;

import java.io.Serializable;
import java.util.Vector;


public class Windows   implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2376495538216584683L;
	private Defaults defaults;
	private Vector<Window> window;
	
	public Defaults getDefaults() {
		return defaults;
	}
	public void setDefaults(Defaults defaults) {
		this.defaults = defaults;
	}
	public Vector<Window> getWindow() {
		return window;
	}
	public void setWindow(Vector<Window> window) {
		this.window = window;
	}

}
