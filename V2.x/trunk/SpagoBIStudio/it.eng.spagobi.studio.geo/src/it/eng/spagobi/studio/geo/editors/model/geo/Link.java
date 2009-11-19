package it.eng.spagobi.studio.geo.editors.model.geo;

import java.io.Serializable;
import java.util.Vector;

public class Link  implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1535770528379383577L;
	private String hierarchy;
	private String level;
	private Vector<LinkParam> param;
	public String getHierarchy() {
		return hierarchy;
	}
	public void setHierarchy(String hierarchy) {
		this.hierarchy = hierarchy;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public Vector<LinkParam> getParam() {
		return param;
	}
	public void setParam(Vector<LinkParam> param) {
		this.param = param;
	}


}
