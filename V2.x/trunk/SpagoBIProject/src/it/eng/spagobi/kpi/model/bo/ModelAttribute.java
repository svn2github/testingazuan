package it.eng.spagobi.kpi.model.bo;

public class ModelAttribute {
	
	String name = null;
	String code = null;
	String descr = null;
	String type = null;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public ModelAttribute() {
		super();
		// TODO Auto-generated constructor stub
	}

}
