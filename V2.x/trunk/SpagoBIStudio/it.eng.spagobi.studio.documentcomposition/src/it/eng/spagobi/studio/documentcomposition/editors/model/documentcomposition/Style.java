package it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition;

public class Style {
	private String style;
	private String mode="auto";

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public Style(String style) {
		super();
		this.style = style;
	}

	public Style() {
		super();
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}
	
	

	
}
