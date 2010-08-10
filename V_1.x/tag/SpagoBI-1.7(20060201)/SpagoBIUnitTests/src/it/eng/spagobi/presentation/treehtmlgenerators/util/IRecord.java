package it.eng.spagobi.presentation.treehtmlgenerators.util;

public interface IRecord {
	
	public String getHead();
	
	public String getName();

	public void updFields(IRecord record);
	
}
