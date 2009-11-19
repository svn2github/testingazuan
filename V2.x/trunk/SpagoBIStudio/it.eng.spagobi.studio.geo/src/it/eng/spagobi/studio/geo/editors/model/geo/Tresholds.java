package it.eng.spagobi.studio.geo.editors.model.geo;

import java.io.Serializable;

public class Tresholds  implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7314038587889055886L;
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
