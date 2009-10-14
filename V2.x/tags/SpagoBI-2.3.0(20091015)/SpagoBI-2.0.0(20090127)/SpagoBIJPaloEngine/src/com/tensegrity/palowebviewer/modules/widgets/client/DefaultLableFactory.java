package com.tensegrity.palowebviewer.modules.widgets.client;

/**
 * Default lable factory that generates label text based on Object.toString() method or 
 * "null" if the given object is null. 
 *
 */
public class DefaultLableFactory implements ILabelFactory {

	public String getLabel(Object object) {
		return object+"";
	}

}
