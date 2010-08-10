package it.eng.spagobi.presentation.treehtmlgenerators.util;

import it.eng.spagobi.utilities.PortletUtilities;

public class RecordForExec implements IRecord {
	
	private String name;
	private int present;
	private int executable;
	private int userActor;
	private int testerActor;

	public RecordForExec(String name, int present, int executable, int userActor , int testerActor) {
		this.executable = executable;
		this.present = present;
		this.name = name;
		this.testerActor = testerActor;
		this.userActor = userActor;
	}
	
	public RecordForExec(String row){
		int indexOfNameStart = row.indexOf("'",0);
		int indexOfNameEnd = row.indexOf("'",indexOfNameStart+1);
		this.name = row.substring(indexOfNameStart+1,indexOfNameEnd);
		this.name = PortletUtilities.getMessage(this.name ,"messages");
		int indexOfExecStart = row.indexOf("'",indexOfNameEnd+1);
		if (indexOfExecStart == -1){
			this.executable = 0;
			this.userActor = 0;
			this.testerActor = 0;
			return;
		}
		int indexOfExecEnd = row.indexOf("'", indexOfExecStart + 1);
		String execution = row.substring(indexOfExecStart + 1, indexOfExecEnd);
		if (execution.equals("")) {
			this.executable = 0;
			this.testerActor = 0;
			this.userActor = 0;
		} else {
			this.executable = 1;
			int indexOfActorStart = row.indexOf("ACTOR=", indexOfExecStart + 1) + 5;
			int indexOfActorEnd = row.indexOf("&", indexOfActorStart + 1);
			String actor = row
					.substring(indexOfActorStart + 1, indexOfActorEnd);
			if (actor.equalsIgnoreCase("USER_ACTOR")) {
				this.testerActor = 0;
				this.userActor = 1;
			} else if (actor.equalsIgnoreCase("TESTER_ACTOR")) {
				this.testerActor = 1;
				this.userActor = 0;
			} else {
				this.testerActor = 0;
				this.userActor = 0;
			}
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
		String toReturn = fixLengthName+"\t"+"   "+this.present+"\t"+"    "+this.executable+"\t"+"\t"+"    "+this.userActor+"\t"+"\t"+"     "+this.testerActor;
		return toReturn;
	}
	
	public String getHead(){
		String toReturn = "Name                     "+"\t"+"Present"+"\t"+"Executable"+"\t"+"User actor"+"\t"+"Tester actor";
		return toReturn;
	}

	public String getName() {
		return name;
	}

	public void updFields(IRecord rec){
		RecordForExec record = (RecordForExec) rec;
		this.executable = record.executable;
		this.present = record.present;
		this.testerActor = record.testerActor;
		this.userActor = record.userActor;
	}	

	public boolean equals(Object object) {
		RecordForExec aRec = (RecordForExec) object;
		if (this.name.equals(aRec.name) && this.executable == aRec.executable
				&& this.present == aRec.present && this.testerActor == aRec.testerActor
				&& this.userActor == aRec.userActor)
			return true;
		else
			return false;
	}
}
