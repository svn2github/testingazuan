package it.eng.spagobi.studio.geo.editors.model.bo;

import it.eng.spagobi.studio.geo.editors.model.geo.Format;
import it.eng.spagobi.studio.geo.editors.model.geo.GuiParam;

import java.io.Serializable;
import java.util.Vector;

public class Label implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6686644116440080130L;
	private String position;
	private String className;
	private Format format;
	private String text;
	private Vector<GuiParam> params;
	
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public Format getFormat() {
		return format;
	}
	public void setFormat(Format format) {
		this.format = format;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Vector<GuiParam> getParams() {
		return params;
	}
	public void setParams(Vector<GuiParam> params) {
		this.params = params;
	}

}
