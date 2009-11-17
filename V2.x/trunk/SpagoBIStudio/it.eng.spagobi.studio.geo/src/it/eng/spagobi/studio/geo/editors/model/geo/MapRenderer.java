package it.eng.spagobi.studio.geo.editors.model.geo;

public class MapRenderer {
	
	private String className;
	private Measures measures;
	private Layers layers;

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
