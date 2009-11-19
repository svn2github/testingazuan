package it.eng.spagobi.studio.geo.editors.model.geo;

import java.io.Serializable;

public class Colours implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5638393931138636831L;
	private String type;
	private String outboundColour;
	private String nullValuesColor;
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
	public String getOutboundColour() {
		return outboundColour;
	}
	public void setOutboundColour(String outboundColour) {
		this.outboundColour = outboundColour;
	}
	public String getNullValuesColor() {
		return nullValuesColor;
	}
	public void setNullValuesColor(String nullValuesColor) {
		this.nullValuesColor = nullValuesColor;
	}

}
