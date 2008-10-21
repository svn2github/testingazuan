package it.eng.spagobi.tools.dataset.common.datastore;

public class FieldMetadata implements IFieldMeta {


	String name = "";
	String type = "";
	String property= "";
	
	public FieldMetadata() {
		super();
		this.name = "";
		this.type = "";
		this.property= "";
	}

	public FieldMetadata(String name, String type, String property) {
		super();
		this.name = name;
		this.type = type;
		this.property = property;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

}
