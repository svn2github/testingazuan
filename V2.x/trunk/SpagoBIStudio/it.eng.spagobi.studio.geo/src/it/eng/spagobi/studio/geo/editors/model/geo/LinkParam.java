package it.eng.spagobi.studio.geo.editors.model.geo;

import java.io.Serializable;

public class LinkParam  implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8474078057560022116L;
	private String name;
	private String value;
	private String scope;
	private String type;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
