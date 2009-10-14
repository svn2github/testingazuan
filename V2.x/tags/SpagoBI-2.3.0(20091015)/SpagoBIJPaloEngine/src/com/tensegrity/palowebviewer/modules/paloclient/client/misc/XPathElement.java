package com.tensegrity.palowebviewer.modules.paloclient.client.misc;

import com.tensegrity.palowebviewer.modules.paloclient.client.IXConsts;
import com.tensegrity.palowebviewer.modules.paloclient.client.XObject;
import com.tensegrity.palowebviewer.modules.util.client.Escaper;

public class XPathElement implements IXConsts {
	
	private final String id;
	private final String name;
	private final int typeId;
	

	public XPathElement(String id, String name, int typeId) {
		if(id == null)
			throw new IllegalArgumentException("ID can not be null.");
		if(name == null)
			throw new IllegalArgumentException("Name can not be null.");
		this.id = id;
		this.name = name;
		this.typeId = typeId;
	}

	public String getId(){
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getTypeName() {
		String result = XHelper.typeToString(getTypeId());
		return result;
	}
	
	public String toString() {
		String result = "XPathElement[";
		result += id;
		result += ":";
		result += name;
		result += ":";
		result += typeId;
		result += "]";
		return result;
	}
	
	public int getTypeId () {
		return typeId;
	}
	
	public String encode() {
		String[] fields = new String[3];
		fields[0] = id;
		fields[1] = name;
		fields[2] = getTypeName();
		String result = Escaper.escapeAndConcat(fields, XPATH_FIELD_SEPARATOR);
		return result;
	}
	
	public boolean equals(Object obj) {
		boolean result = obj instanceof XPathElement;
		if (result) {
			XPathElement another = (XPathElement) obj;
			result = id.equals(another.id) && name.equals(another.name) && typeId == another.typeId;
		}
		return result;
	}

	public static XPathElement create(XObject x) {
		String id = x.getId();
		String name = x.getName();
		int typeId = x.getTypeID();
		XPathElement result = new XPathElement(id, name, typeId);
		return result;
	}
	
	public static XPathElement create(String encoded) {
		String[] splited = Escaper.splitAndUnescape(encoded, XPATH_FIELD_SEPARATOR);
		String id = splited[0];
		String name = splited[1];
		int typeId = XHelper.typeNameToId(splited[2]);
		XPathElement result = new XPathElement(id, name, typeId);
		return result;
	}
	
	public int hashCode() {
		return name.hashCode();
	}
	
}
