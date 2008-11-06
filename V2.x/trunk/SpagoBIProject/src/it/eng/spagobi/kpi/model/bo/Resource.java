package it.eng.spagobi.kpi.model.bo;

public class Resource {
	
	String name = null;
	String coumn_name = null;
	String table_name = null;
	String type = null; //type of the resource, such as Project...
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCoumn_name() {
		return coumn_name;
	}

	public void setCoumn_name(String coumn_name) {
		this.coumn_name = coumn_name;
	}

	public String getTable_name() {
		return table_name;
	}

	public void setTable_name(String table_name) {
		this.table_name = table_name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Resource() {
		super();
		// TODO Auto-generated constructor stub
	}

}
