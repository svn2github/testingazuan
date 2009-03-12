package it.eng.spagobi.tools.scheduler.utils;

import it.eng.spagobi.analiticalmodel.document.bo.BIObject;

public abstract class JavaClassDestination implements IJavaClassDestination {

	BIObject biObj=null;
	byte[] documentByte=null;
	
	public abstract void execute();
	
	public byte[] getDocumentByte() {
		return documentByte;
	}

	public void setDocumentByte(byte[] documentByte) {
		this.documentByte = documentByte;
	}

	public BIObject getBiObj() {
		return biObj;
	}

	public void setBiObj(BIObject biObj) {
		this.biObj = biObj;
	}

	
	
	
	
}
