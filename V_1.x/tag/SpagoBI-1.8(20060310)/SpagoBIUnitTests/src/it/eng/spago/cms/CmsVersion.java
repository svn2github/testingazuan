package it.eng.spago.cms;

public class CmsVersion {

	String name = "";
	String dataCreation = "";
	
	public CmsVersion(String namein, String datain) {
		name = namein;
		dataCreation = datain;
	}
	
	public String getDataCreation() {
		return dataCreation;
	}
	public void setDataCreation(String dataCreation) {
		this.dataCreation = dataCreation;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
