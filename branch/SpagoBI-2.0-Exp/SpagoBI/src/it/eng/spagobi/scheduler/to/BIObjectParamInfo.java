package it.eng.spagobi.scheduler.to;

public class BIObjectParamInfo {

	private String label = "";
	private String urlname = "";
	private String value = "";
	
	public BIObjectParamInfo(String lab, String uname, String val) {
		this.label = lab;
		this.urlname = uname;
		this.value = val;
	}
	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getUrlname() {
		return urlname;
	}
	public void setUrlname(String urlname) {
		this.urlname = urlname;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	
	
}
