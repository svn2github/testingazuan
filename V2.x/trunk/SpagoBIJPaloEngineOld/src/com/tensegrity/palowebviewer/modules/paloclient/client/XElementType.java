package com.tensegrity.palowebviewer.modules.paloclient.client;

public class XElementType implements IElementType {
	
	private String name;
	
	public static final boolean isConsolidated(XElement element){
		return isType(element, XConsolidatedType.instance);	
	}
	
	public static final boolean isString(XElement element){
		return isType(element, XStringType.instance);
	}
	
	public static final boolean isNumeric(XElement element){
		return isType(element, XNumericType.instance);
	}
	
	public static final boolean isType(XElement element, IElementType type){
		boolean result = false;
		if(element != null) {
			result = type.equals(element.getType());
		}
		return result;
	}

	
	public XElementType(){
		
	}
	
	public XElementType(String name){
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public boolean equals(Object obj) {
		boolean r = false;
		if (obj instanceof XElementType) {
			XElementType el = (XElementType) obj;
			r = el.getName().equals(name);
		}
		return r;
	}

	public int hashCode() {
		return name.hashCode();
	}
	
	

}
