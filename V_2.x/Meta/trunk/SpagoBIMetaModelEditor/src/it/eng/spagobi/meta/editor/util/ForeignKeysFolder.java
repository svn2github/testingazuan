/*
 * This class is a container for PhysicalForeignKey objects
 * used only to group this in the TreeViewer visualization
 */
package it.eng.spagobi.meta.editor.util;

public class ForeignKeysFolder {
	
	private Object[] arrayFK;
	
	public ForeignKeysFolder(Object[] pk){
		arrayFK = pk;
	}

	public void setArrayFK(Object[] arrayFK) {
		this.arrayFK = arrayFK;
	}

	public Object[] getArrayFK() {
		return arrayFK;
	}
	
	

}
