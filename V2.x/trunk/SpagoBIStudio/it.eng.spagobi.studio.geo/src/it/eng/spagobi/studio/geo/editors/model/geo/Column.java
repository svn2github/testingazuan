package it.eng.spagobi.studio.geo.editors.model.geo;

public class Column {
	private String type;
	private String columnId;
	private String hierarchy;
	private String level;
	private String aggFunction;
	
	private boolean choosenForTemplate;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getColumnId() {
		return columnId;
	}
	public void setColumnId(String columnId) {
		this.columnId = columnId;
	}
	public String getHierarchy() {
		return hierarchy;
	}
	public void setHierarchy(String hierarchy) {
		this.hierarchy = hierarchy;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getAggFunction() {
		return aggFunction;
	}
	public void setAggFunction(String aggFunction) {
		this.aggFunction = aggFunction;
	}
	public boolean isChoosenForTemplate() {
		return choosenForTemplate;
	}
	public void setChoosenForTemplate(boolean choosenForTemplate) {
		this.choosenForTemplate = choosenForTemplate;
	}


}
