package it.eng.spagobi.tools.dataset.bo;

import org.apache.log4j.Logger;

public class JClassDataSet extends DataSetConfig {
	 private String javaClassName = null;
	 private static transient Logger logger=Logger.getLogger(FileDataSet.class);
	 
	 
	public String getJavaClassName() {
		return javaClassName;
	}
	
	
	public JClassDataSet() {
		super();
	}

	public JClassDataSet(DataSetConfig a) {
    	setDsId(a.getDsId());
    	setLabel(a.getLabel());
    	setName(a.getName());
    	setDescription(a.getDescription());
	}

	public JClassDataSet(String javaClassName) {
		super();
		this.javaClassName = javaClassName;
	}


	public void setJavaClassName(String javaClassName) {
		this.javaClassName = javaClassName;
	}

}
