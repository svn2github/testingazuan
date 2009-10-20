package it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition;


public class Parameter {
	private String label;
	private String type;
	private String sbiParLabel;
	private String defaultVal;
	private Refresh refresh;
	
	//attributo che non verrà salvato nell'xml del template ma individua la navigazione
	private String navigationName;
	

	public String getNavigationName() {
		return navigationName;
	}
	public void setNavigationName(String navigationName) {
		this.navigationName = navigationName;
	}
	public Refresh getRefresh() {
		return refresh;
	}
	public void setRefresh(Refresh refresh) {
		this.refresh = refresh;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSbiParLabel() {
		return sbiParLabel;
	}
	public void setSbiParLabel(String sbiParLabel) {
		this.sbiParLabel = sbiParLabel;
	}
	public String getDefaultVal() {
		return defaultVal;
	}
	public void setDefaultVal(String defaultVal) {
		this.defaultVal = defaultVal;
	}


}
