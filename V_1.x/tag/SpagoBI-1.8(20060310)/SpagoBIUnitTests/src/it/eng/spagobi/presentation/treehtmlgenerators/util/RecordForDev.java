package it.eng.spagobi.presentation.treehtmlgenerators.util;

import it.eng.spagobi.utilities.PortletUtilities;

public class RecordForDev implements IRecord{
	
	private String name;
	private int present;
	private int executable;
	private int detail;
	private int getParameters;
	private int deletable;
	
	public RecordForDev(String name, int present, int executable, int detail, int getParameters, int deletable) {
		this.executable = executable;
		this.present = present;
		this.name = name;
		this.detail = detail;
		this.getParameters = getParameters;
		this.deletable = deletable;
	}
	
	public RecordForDev(String row){
		int indexOfNameStart = row.indexOf("'",0);
		int indexOfNameEnd = row.indexOf("'",indexOfNameStart+1);
		this.name = row.substring(indexOfNameStart+1,indexOfNameEnd);
		this.name = PortletUtilities.getMessage(this.name ,"messages");
		int menuIndex = row.indexOf("'menu",indexOfNameEnd);
		if (menuIndex == -1){
			this.executable = 0;
			this.detail = 0;
			this.getParameters = 0;
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
			int indexOfGetParStart = row.indexOf("'", indexOfDetailEnd + 1);
			int indexOfGetParEnd = row.indexOf("'",
					indexOfGetParStart + 1);
			String getpar = row.substring(indexOfGetParStart + 1,
					indexOfGetParEnd);
			if (!getpar.equals("\\"))
				this.getParameters = 1;
			int indexOfDeletableStart = row.indexOf("'", indexOfGetParEnd + 1);
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
		String toReturn = fixLengthName+"\t"+"   "+this.present+"\t"+"    "+this.executable+"\t"+"\t"+"   "+this.detail+"\t"+"      "+this.getParameters+"\t"+"\t"+"    "+this.deletable;
		return toReturn;
	}
	
	public String getHead(){
		String toReturn = "Name                     "+"\t"+"Present"+"\t"+"Executable"+"\t"+"Detail"+"\t"+"Get parameters"+"\t"+"Deletable";
		return toReturn;
	}

	public String getName() {
		return name;
	}

	public void updFields(IRecord rec){
		RecordForDev record = (RecordForDev) rec;
		this.executable = record.executable;
		this.present = record.present;
		this.detail = record.detail;
		this.getParameters = record.getParameters;
		this.deletable = record.deletable;
	}
	
	public boolean equals(Object object) {
		RecordForDev aRec = (RecordForDev) object;
		if (this.name.equals(aRec.name) && this.executable == aRec.executable
				&& this.present == aRec.present && this.detail == aRec.detail
				&& this.getParameters == aRec.getParameters
				&& this.deletable == aRec.deletable)
			return true;
		else
			return false;
	}
	
}
