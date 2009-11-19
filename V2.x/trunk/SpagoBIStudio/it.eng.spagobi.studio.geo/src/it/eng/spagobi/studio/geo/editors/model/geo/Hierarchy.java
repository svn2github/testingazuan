package it.eng.spagobi.studio.geo.editors.model.geo;

import java.io.Serializable;

public class Hierarchy  implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1082027570216423244L;
	private String name;
	private String type;
	private Levels levels;
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
	public Levels getLevels() {
		return levels;
	}
	public void setLevels(Levels levels) {
		this.levels = levels;
	}

}
