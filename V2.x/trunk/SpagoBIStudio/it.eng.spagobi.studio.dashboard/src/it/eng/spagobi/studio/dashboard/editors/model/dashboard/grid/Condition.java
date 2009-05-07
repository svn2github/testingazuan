package it.eng.spagobi.studio.dashboard.editors.model.dashboard.grid;

public class Condition {

	private String operator;
	private String value1;
	private String value2;
	private String conditioncolor;
	private String tooltip;
	private boolean showvalueintotooltip;
	
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getValue1() {
		return value1;
	}
	public void setValue1(String value1) {
		this.value1 = value1;
	}
	public String getValue2() {
		return value2;
	}
	public void setValue2(String value2) {
		this.value2 = value2;
	}
	public String getConditioncolor() {
		return conditioncolor;
	}
	public void setConditioncolor(String conditioncolor) {
		this.conditioncolor = conditioncolor;
	}
	public String getTooltip() {
		return tooltip;
	}
	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}
	public boolean isShowvalueintotooltip() {
		return showvalueintotooltip;
	}
	public void setShowvalueintotooltip(boolean showvalueintotooltip) {
		this.showvalueintotooltip = showvalueintotooltip;
	}
	
}
