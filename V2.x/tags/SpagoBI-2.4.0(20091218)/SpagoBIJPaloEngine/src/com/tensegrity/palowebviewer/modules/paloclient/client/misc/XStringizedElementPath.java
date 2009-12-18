package com.tensegrity.palowebviewer.modules.paloclient.client.misc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.tensegrity.palowebviewer.modules.paloclient.client.XDimension;
import com.tensegrity.palowebviewer.modules.paloclient.client.XElement;
import com.tensegrity.palowebviewer.modules.paloclient.client.XElementPath;
import com.tensegrity.palowebviewer.modules.util.client.Arrays;


//wraps by ids XElementPath
public class XStringizedElementPath implements IsSerializable {
	
	public static XStringizedElementPath stringize(XElementPath path){
		XStringizedElementPath r = new XStringizedElementPath();
		XDimension[] dims = path.getDimensions();
		for (int i = 0; i < dims.length; i++) {
			XDimension dimension = dims[i];
			XElement[] elements = path.getPart(dimension);
			String[] elementIds = new String[elements.length];
			for (int j = 0; j < elements.length; j++) {
				XElement element = elements[j];
				elementIds[j] = element.getId();
			}
			r.addPart(dimension.getId(), elementIds);
		}
		return r;
	}
	
	private List dimensionIds = new ArrayList(); //keeps dimension order
	private Map map = new HashMap(); //key: Dimension id , value: String[] of element path
	private int hashCode = -1;
	
	public XStringizedElementPath(){
		
	}
	//TODO: hack for GWT bug
	private XStringizedElementPath(List list){
		dimensionIds = (List)list.get(0);
		map = (Map)list.get(1);
	}

	//TODO: hack for GWT bug
	public List toList() {
		List result = new ArrayList();
		result.add(dimensionIds);
		result.add(map);
		return result;
	}

	//TODO: hack for GWT bug
	public static XStringizedElementPath fromList(List list) {
		return new XStringizedElementPath(list);
	}
	
	private void addPart(String dimensionId, String[] path){
		if(dimensionId == null) throw new IllegalArgumentException("dimension id can't be null");
		if(path == null) throw new IllegalArgumentException("path can't be null");
		if(dimensionIds.contains(dimensionId)) throw new RuntimeException("dimension with id '" + dimensionId + "' already added");
		dimensionIds.add(dimensionId);
		map.put(dimensionId, path);
		hashCode = -1;
	}
	
	public String[] getDimensionIds(){
		String[] r = new String[dimensionIds.size()];
		for (int i = 0; i < r.length; i++) {
			r[i] = (String) dimensionIds.get(i);
		}
		return r;
	}
	
	public String[] getPart(String dimensionId){
		return (String[])map.get(dimensionId);
	}

	public boolean equals(Object obj) {
		boolean r = false;
		if (obj instanceof XStringizedElementPath) {
			XStringizedElementPath path = (XStringizedElementPath) obj;
			r = dimensionIds.equals(path.dimensionIds);
			for(Iterator it = map.entrySet().iterator(); it.hasNext() && r;){
				Map.Entry entry = (Map.Entry)it.next();
				String[] myElements = (String[]) entry.getValue();
				String[] otherElements = (String[]) path.map.get(entry.getKey());
				r = Arrays.equals(myElements, otherElements);
			}
		}
		return r;
	}

	public int hashCode() {
		if(hashCode == -1){
			hashCode = 0;
			String[] dims = getDimensionIds();
			for (int i = 0; i < dims.length; i++) {
				String dimension = dims[i];
				hashCode += (dimension.hashCode()*i);
			}
		}
		return hashCode;
	}
	
	
}
