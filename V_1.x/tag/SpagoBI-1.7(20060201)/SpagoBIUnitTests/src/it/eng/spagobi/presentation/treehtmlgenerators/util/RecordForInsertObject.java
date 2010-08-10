package it.eng.spagobi.presentation.treehtmlgenerators.util;

import it.eng.spagobi.utilities.PortletUtilities;

public class RecordForInsertObject implements IRecord {

	private String name;
	private int present;
	private int checkable;
	
	public RecordForInsertObject(String name, int present, int checkable) {
		this.checkable = checkable;
		this.present = present;
		this.name = name;
	}
	
	public RecordForInsertObject(String row){
		int indexOfNameStart = row.indexOf("'",0);
		int indexOfNameEnd = row.indexOf("'",indexOfNameStart+1);
		this.name = row.substring(indexOfNameStart+1,indexOfNameEnd);
		this.name = PortletUtilities.getMessage(this.name ,"messages");
		int checkIndex = row.indexOf("PATH_PARENT",indexOfNameEnd);
		if (checkIndex == -1) this.checkable = 0;
		else this.checkable = 1;
		this.present = 1;
	}
	
	public String toString(){
		int fixLength = 25;
		String fixLengthName = null;
		if (this.name.length() >= fixLength) fixLengthName = this.name.substring(0,fixLength-1); 
		else {
			int diff = fixLength - this.name.length();
			fixLengthName = this.name;
			for (int j = 0; j < diff; j++) fixLengthName = fixLengthName + " "; 
		}
		String toReturn = fixLengthName+"\t"+"   "+this.present+"\t"+"    "+this.checkable;
		return toReturn;
	}
	
	public String getHead(){
		String toReturn = "Name                     "+"\t"+"Present"+"\t"+"Checkable";
		return toReturn;
	}

	public String getName() {
		return name;
	}

	public void updFields(IRecord rec){
		RecordForInsertObject record = (RecordForInsertObject) rec;
		this.checkable = record.checkable;
		this.present = record.present;
	}
	
	public boolean equals(Object object) {
		RecordForInsertObject aRec = (RecordForInsertObject) object;
		if (this.name.equals(aRec.name) && this.checkable == aRec.checkable
				&& this.present == aRec.present)
			return true;
		else
			return false;
	}

}
