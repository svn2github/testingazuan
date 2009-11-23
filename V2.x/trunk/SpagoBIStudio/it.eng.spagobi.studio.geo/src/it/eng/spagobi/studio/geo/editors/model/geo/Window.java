package it.eng.spagobi.studio.geo.editors.model.geo;

import java.io.Serializable;

public class Window   implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2076909374067636713L;


	private String name;


	private Param param;

	public Param getParam() {
		return param;
	}

	public void setParam(Param param) {
		this.param = param;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
