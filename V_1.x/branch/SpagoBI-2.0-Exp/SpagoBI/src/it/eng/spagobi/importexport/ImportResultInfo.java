package it.eng.spagobi.importexport;

import java.util.HashMap;
import java.util.Map;

public class ImportResultInfo {

	String pathLogFile = "";
	Map manualTasks = new HashMap();
	
	public Map getManualTasks() {
		return manualTasks;
	}
	public void setManualTasks(Map manualTasks) {
		this.manualTasks = manualTasks;
	}
	public String getPathLogFile() {
		return pathLogFile;
	}
	public void setPathLogFile(String pathLogFile) {
		this.pathLogFile = pathLogFile;
	}
	
}
