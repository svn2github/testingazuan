package it.eng.spagobi.utilities.engines;

import java.util.Set;

public interface IEngineAnalysisState {

	void load(byte[] rowData) throws EngineException;

	byte[] store() throws EngineException;

	Object getProperty(Object pName);

	void setProperty(Object pName, Object pValue);
	
	boolean containsProperty(Object pName);

	Set propertyNameSet();
}