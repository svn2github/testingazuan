package it.eng.spagobi.studio.chart.utils;

public class DrillParameters {

	String name;
	String value;
	String type=RELATIVE;

	public static final String RELATIVE="RELATIVE";
	public static final String ABSOLUTE="ABSOLUTE";


	public DrillParameters(String name, String value, String type) {
		super();
		this.name = name;
		this.value = value;
		this.type = type;
	}
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public String toXml(){
		String toReturn="<PARAM ";
		toReturn+="name='"+name+"' ";
		toReturn+="type='"+type+"' ";
		if(value!=null){
			toReturn+="value='"+value+"' ";
		}
		toReturn+="/>\n";
		return toReturn;
	}


}
