package it.eng.spagobi.presentation.treehtmlgenerators.util;

import it.eng.spagobi.utilities.PortletUtilities;

public class RecordForAdmin implements IRecord {

	private String name;
	private int present;
	private int executable;
	private int detail;
	private int deletable;
	
	public RecordForAdmin(String name, int present, int executable, int detail, int deletable) {
		this.executable = executable;
		this.present = present;
		this.name = name;
		this.detail = detail;
		this.deletable = deletable;
	}
	
	public RecordForAdmin(String row){
		int indexOfNameStart = row.indexOf("'",0);
		int indexOfNameEnd = row.indexOf("'",indexOfNameStart+1);
		this.name = row.substring(indexOfNameStart+1,indexOfNameEnd);
		this.name = PortletUtilities.getMessage(this.name ,"messages");
		int menuIndex = row.indexOf("'menu",indexOfNameEnd);
		if (menuIndex == -1){
			this.executable = 0;
			this.detail = 0;
			this.deletable = 0;
		}
		else {
			int indexOfExecutionStart = row.indexOf("'", menuIndex + 1);
			int indexOfExecutionEnd = row.indexOf("'",
					indexOfExecutionStart + 1);
			String execution = row.substring(indexOfExecutionStart + 1,
					indexOfExecutionEnd);
			if (!execution.equals("\\"))
				this.executable = 1;
			int indexOfDetailStart = row.indexOf("'", indexOfExecutionEnd + 1);
			int indexOfDetailEnd = row.indexOf("'",
					indexOfDetailStart + 1);
			String detail = row.substring(indexOfDetailStart + 1,
					indexOfDetailEnd);
			if (!detail.equals("\\"))
				this.detail = 1;
			int indexOfDeletableStart = row.indexOf("'", indexOfDetailEnd + 1);
			int indexOfDeletableEnd = row.indexOf("'",
					indexOfDeletableStart + 1);
			String deletable = row.substring(indexOfDeletableStart + 1,
					indexOfDeletableEnd);
			if (!deletable.equals("\\"))
				this.deletable = 1;
		}
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
		String toReturn = fixLengthName+"\t"+"   "+this.present+"\t"+"    "+this.executable+"\t"+"\t"+"   "+this.detail+"\t"+"    "+this.deletable;
		return toReturn;
	}
	
	public String getHead(){
		String toReturn = "Name                     "+"\t"+"Present"+"\t"+"Executable"+"\t"+"Detail"+"\t"+"Deletable";
		return toReturn;
	}

	public String getName() {
		return name;
	}

	public void updFields(IRecord rec){
		RecordForAdmin record = (RecordForAdmin) rec;
		this.executable = record.executable;
		this.present = record.present;
		this.detail = record.detail;
		this.deletable = record.deletable;
	}

	public boolean equals(Object object) {
		RecordForAdmin aRec = (RecordForAdmin) object;
		if (this.name.equals(aRec.name) && this.executable == aRec.executable
				&& this.present == aRec.present && this.detail == aRec.detail
				&& this.deletable == aRec.deletable)
			return true;
		else
			return false;
	}
}
