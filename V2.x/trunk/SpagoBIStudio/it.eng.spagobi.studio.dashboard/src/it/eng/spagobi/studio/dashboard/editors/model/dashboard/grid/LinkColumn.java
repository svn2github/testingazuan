package it.eng.spagobi.studio.dashboard.editors.model.dashboard.grid;

public class LinkColumn extends Column {

	private boolean onlyheader;
	private String fixedquerystring;
	private String prefixvalue;
	
	public boolean isOnlyheader() {
		return onlyheader;
	}
	public void setOnlyheader(boolean onlyheader) {
		this.onlyheader = onlyheader;
	}
	public String getFixedquerystring() {
		return fixedquerystring;
	}
	public void setFixedquerystring(String fixedquerystring) {
		this.fixedquerystring = fixedquerystring;
	}
	public String getPrefixvalue() {
		return prefixvalue;
	}
	public void setPrefixvalue(String prefixvalue) {
		this.prefixvalue = prefixvalue;
	}
}
