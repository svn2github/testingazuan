package it.eng.spagobi.studio.geo.editors.model.geo;

import java.io.Serializable;

public class MapProvider  implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 454546882825789321L;
	private String className;
	private String mapName;
	
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getMapName() {
		return mapName;
	}
	public void setMapName(String mapName) {
		this.mapName = mapName;
	}


}
