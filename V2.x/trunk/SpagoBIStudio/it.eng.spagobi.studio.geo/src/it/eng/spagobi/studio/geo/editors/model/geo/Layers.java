package it.eng.spagobi.studio.geo.editors.model.geo;

import java.util.Vector;

public class Layers {
	
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
