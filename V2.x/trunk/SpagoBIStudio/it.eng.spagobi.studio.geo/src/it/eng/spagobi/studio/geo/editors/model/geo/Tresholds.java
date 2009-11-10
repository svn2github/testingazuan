package it.eng.spagobi.studio.geo.editors.model.geo;

public class Tresholds {
	private String type;
	private String lbValue;
	private String ubValue;
	private Param param;
	public Param getParam() {
		return param;
	}
	public void setParam(Param param) {
		this.param = param;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getLbValue() {
		return lbValue;
	}
	public void setLbValue(String lbValue) {
		this.lbValue = lbValue;
	}
	public String getUbValue() {
		return ubValue;
	}
	public void setUbValue(String ubValue) {
		this.ubValue = ubValue;
	}

}
