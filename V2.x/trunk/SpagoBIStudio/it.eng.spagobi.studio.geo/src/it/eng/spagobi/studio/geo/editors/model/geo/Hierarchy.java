package it.eng.spagobi.studio.geo.editors.model.geo;

import java.io.Serializable;
import java.util.Vector;

public class Hierarchy  implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1082027570216423244L;
	private String name;
	private String type;
	private Vector<Level> levels;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Vector<Level> getLevels() {
		return levels;
	}
	public void setLevels(Vector<Level> levels) {
		this.levels = levels;
	}


}
