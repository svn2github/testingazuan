package com.tensegrity.palowebviewer.modules.paloclient.client.misc;

import java.util.List;

import com.tensegrity.palowebviewer.modules.paloclient.client.IXConsts;
import com.tensegrity.palowebviewer.modules.paloclient.client.XAxis;
import com.tensegrity.palowebviewer.modules.paloclient.client.XConsolidatedElement;
import com.tensegrity.palowebviewer.modules.paloclient.client.XCube;
import com.tensegrity.palowebviewer.modules.paloclient.client.XDatabase;
import com.tensegrity.palowebviewer.modules.paloclient.client.XDimension;
import com.tensegrity.palowebviewer.modules.paloclient.client.XElement;
import com.tensegrity.palowebviewer.modules.paloclient.client.XElementNode;
import com.tensegrity.palowebviewer.modules.paloclient.client.XObject;
import com.tensegrity.palowebviewer.modules.paloclient.client.XRoot;
import com.tensegrity.palowebviewer.modules.paloclient.client.XServer;
import com.tensegrity.palowebviewer.modules.paloclient.client.XSubset;
import com.tensegrity.palowebviewer.modules.paloclient.client.XView;
import com.tensegrity.palowebviewer.modules.util.client.Arrays;

public class XArrays {

	public static XObject[] copy(XObject[] array, int i1, int i2){
		if(i1<0)
			i1 = 0;
		if(i2>array.length)
			i2 =array.length;
		XObject[] result = new XObject[i2-i1];
		for (int i = 0; i < result.length; i++) {
			result[i] = array[i+i1];
		}
		return result;
	}

	public static XObject[] copy(XObject[] array, int i1, int i2, int type){
		if(i1<0)
			i1 = 0;
		if(i2>array.length)
			i2 =array.length;
		XObject[] result = XArrays.newArray(type, i2-i1);
		for (int i = 0; i < result.length; i++) {
			result[i] = array[i+i1];
		}
		return result;
	}

	public static XObject[] copy(XObject[] array) {
	    final int size = array.length;
	    final XObject[] result = new XObject[size];
	    for( int i = 0 ; i < size ; i++ ) { 
	        result[i] = array[i];
	    } 
	    return result;
	}

	public static XObject[] copy(XObject[] array, int type) {
	    final int size = array.length;
	    final XObject[] result = XArrays.newArray(type, size);
	    for( int i = 0 ; i < size ; i++ ) { 
	        result[i] = array[i];
	    } 
	    return result;
	}

	/**
	 * Checks if two arrays has the same size and objects in them has equal names.
	 */
	public static boolean equalsByName(XObject[] array1, XObject[] array2) {
	    boolean result = true;
	    if(array1 == null)
	        result = array2 == null;
	    else if(array2 == null)
	        result = false;
	    else {
	        result = array1.length == array2.length;
	        for( int i = 0 ; (i < array1.length) && result ; i++ ) { 
	            XObject obj1 = array1[i];
	            XObject obj2 = array2[i];
	            result = XHelper.equalsById(obj1, obj2);
	        } 
	    }
	    return result;
	}

	public static int hashCode(XObject[] array) {
		int result = 0;
		for (int i = 0; i < array.length; i++) {
			XObject object = array[i];
			if(object != null) {
				result += (object.hashCode()*i);
			}
		}
		return result;
	}

	public static XObject findById(XObject[] objects, String id) {
	    XObject result = null;
	    int index = findIndexById(objects, id);
	    if(index >=0)
	        result = objects[index];
	    return result;
	}

	public static int findIndexById(XObject[] objects, String id) {
	    int result = -1;
	    if(objects != null)
	        for( int i = 0 ; i < objects.length ; i++ ) {
	            if((objects[i] != null) && Arrays.equals(id, objects[i].getId())) {
	                result  = i;
	                break;
	            }
	        } 
	    return result;
	}

	public static int findIndexById(List list, String id) {
	    int result = -1;
	    if(list != null) {
	    	int size = list.size();
	    	for (int i =0 ; i< size; i ++) {
				XObject object = (XObject) list.get(i);
	            if((object != null) && Arrays.equals(id,object.getId())) {
	                result  = i;
	                break;
	            }
			}
	    }
	    return result;
	}

	public static int findIndex(XObject[] objects, XObject o) {
		return Arrays.indexOf(objects, o);
	}

	public static XObject[] newArray(int type, int size) {
		XObject[] r = null;
		switch(type) {
		case IXConsts.TYPE_ROOT: {
			r = new XRoot[size];
			break;
		}
		case IXConsts.TYPE_SERVER: {
			r = new XServer[size];
			break;
		}
		case IXConsts.TYPE_DATABASE: {
			r = new XDatabase[size];
			break;
		}
		case IXConsts.TYPE_CUBE: {
			r = new XCube[size];
			break;
		}
		case IXConsts.TYPE_DIMENSION: {
			r = new XDimension[size];
			break;
		}
		case IXConsts.TYPE_ELEMENT: {
			r = new XElement[size];
			break;
		}
		case IXConsts.TYPE_CONSOLIDATED_ELEMENT: {
			r = new XConsolidatedElement[size];
			break;
		}
		case IXConsts.TYPE_VIEW: {
			r = new XView[size];
			break;
		}
		case IXConsts.TYPE_SUBSET: {
			r = new XSubset[size];
			break;
		}
		case IXConsts.TYPE_AXIS: {
			r = new XAxis[size];
			break;
		}
		case IXConsts.TYPE_ELEMENT_NODE: {
			r = new XElementNode[size];
			break;
		}
		default:{
			throw new IllegalArgumentException("incorrect type " + XHelper.typeToString(type));
		}
		}
		return r;
	}

	public static XObject[] toArray(List list){
	    XObject[] result = new XObject[list.size()];
	    for (int i = 0; i < result.length; i++) {
	        result[i] = (XObject)list.get(i);
	    }
	    return result;
	}

	public static XObject[] toArray(List list, int type){
	    int size = list.size();
	    XObject[] r = newArray(type, size);
	    for (int i = 0; i < r.length; i++) {
	        r[i] = (XObject)list.get(i);
	    }
	    return r;
	}

	public static XObject findByName(XObject[] objects, String name) {
	    XObject result = null;
	    int index = findIndexByName(objects, name);
	    if(index >=0)
	        result = objects[index];
	    return result;
	}

	public static int findIndexByName(XObject[] objects, String name) {
	    int result = -1;
	    if(objects != null)
	        for( int i = 0 ; i < objects.length ; i++ ) {
	            if((objects[i] != null) && Arrays.equals(name, objects[i].getName())) {
	                result  = i;
	                break;
	            }
	        } 
	    return result;
	}

}
