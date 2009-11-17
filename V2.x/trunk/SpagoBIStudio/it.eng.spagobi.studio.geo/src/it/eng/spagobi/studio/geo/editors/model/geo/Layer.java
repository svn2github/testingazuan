package it.eng.spagobi.studio.geo.editors.model.geo;

public class Layer {
	private String name;
	private String description;
	private String selected;
	private String defaultFillColour;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getSelected() {
		return selected;
	}
	public void setSelected(String selected) {
		this.selected = selected;
	}
	public String getDefaultFillColour() {
		return defaultFillColour;
	}
	public void setDefaultFillColour(String defaultFillColour) {
		this.defaultFillColour = defaultFillColour;
	}


}
