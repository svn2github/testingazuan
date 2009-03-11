package it.eng.spagobi.tools.scheduler.utils;

public class JavaClassDestination implements IJavaClassDestination {

	Integer biObjID=null;
	byte[] documentByte=null;
	
	public void execute() {
		
	}

	public Integer getBiObjID() {
		return biObjID;
	}

	public void setBiObjID(Integer biObjID) {
		this.biObjID = biObjID;
	}

	public byte[] getDocumentByte() {
		return documentByte;
	}

	public void setDocumentByte(byte[] documentByte) {
		this.documentByte = documentByte;
	}

	
	
	
	
}
