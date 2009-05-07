package it.eng.spagobi.studio.dashboard.editors.model.dashboard.grid;

public class LightColumn extends Column {

	private String defaultcolor;
	private String defaulttooltip;
	private Condition[] conditions;
	
	public Condition[] getConditions() {
		return conditions;
	}
	public void setConditions(Condition[] conditions) {
		this.conditions = conditions;
	}
	public String getDefaultcolor() {
		return defaultcolor;
	}
	public void setDefaultcolor(String defaultcolor) {
		this.defaultcolor = defaultcolor;
	}
	public String getDefaulttooltip() {
		return defaulttooltip;
	}
	public void setDefaulttooltip(String defaulttooltip) {
		this.defaulttooltip = defaulttooltip;
	}
	
}
