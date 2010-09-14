/*
 * This class is a container for PhysicalPrimaryKey objects
 * used only to group this in the TreeViewer visualization
 */
package it.eng.spagobi.meta.editor.util;

public class PrimaryKeysFolder {
	
	private Object[] arrayPK;
	
	public PrimaryKeysFolder(Object[] pk){
		arrayPK = pk;
	}

	public void setArrayPK(Object[] arrayPK) {
		this.arrayPK = arrayPK;
	}

	public Object[] getArrayPK() {
		return arrayPK;
	}
	
	

}
