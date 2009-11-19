package it.eng.spagobi.studio.geo.editors.model.geo;

import java.io.Serializable;
import java.util.Vector;

public class Layers  implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 719689238722747829L;
	private Vector<Layer> layer;
	private String mapName;

	public Vector<Layer> getLayer() {
		return layer;
	}

	public void setLayer(Vector<Layer> layer) {
		this.layer = layer;
	}

	public String getMapName() {
		return mapName;
	}

	public void setMapName(String mapName) {
		this.mapName = mapName;
	}

}
