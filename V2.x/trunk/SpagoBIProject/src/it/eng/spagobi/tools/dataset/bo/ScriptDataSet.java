package it.eng.spagobi.tools.dataset.bo;

import it.eng.spagobi.services.dataset.bo.SpagoBiDataSet;

import org.apache.log4j.Logger;

public class ScriptDataSet extends DataSetConfig {
	private String script;
	private static transient Logger logger=Logger.getLogger(FileDataSet.class);
	
	public String getScript() {
		return script;
	}
	
	public ScriptDataSet() {
		super();
	}
	

	public ScriptDataSet(DataSetConfig a) {
    	setDsId(a.getDsId());
    	setLabel(a.getLabel());
    	setName(a.getName());
    	setDescription(a.getDescription());
	}

	public ScriptDataSet(String script) {
		super();
		this.script = script;
	}

	public void setScript(String script) {
		this.script = script;
	}


	public SpagoBiDataSet toSpagoBiDataSet() {
		SpagoBiDataSet sbd = new SpagoBiDataSet();
		sbd.setLabel(getLabel());
		sbd.setName(getName());
		sbd.setParameters(getParameters());
		sbd.setDescription(getDescription());
		sbd.setType("SbiScriptDataSet");
		sbd.setScript(script);
		return sbd;
	}
	
	

}
