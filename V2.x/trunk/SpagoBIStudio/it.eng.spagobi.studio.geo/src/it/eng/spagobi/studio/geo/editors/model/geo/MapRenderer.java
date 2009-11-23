package it.eng.spagobi.studio.geo.editors.model.geo;

import java.io.Serializable;

public class MapRenderer  implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8609302485242818211L;
	private String className;
	private Measures measures;
	private Layers layers;
	private GuiSettings guiSettings;

	public GuiSettings getGuiSettings() {
		return guiSettings;
	}

	public void setGuiSettings(GuiSettings guiSettings) {
		this.guiSettings = guiSettings;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public Measures getMeasures() {
		return measures;
	}

	public void setMeasures(Measures measures) {
		this.measures = measures;
	}

	public Layers getLayers() {
		return layers;
	}

	public void setLayers(Layers layers) {
		this.layers = layers;
	}

}
