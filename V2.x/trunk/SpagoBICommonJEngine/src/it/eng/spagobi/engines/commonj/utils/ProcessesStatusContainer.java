package it.eng.spagobi.engines.commonj.utils;

import it.eng.spagobi.engines.commonj.runtime.CommonjWorkContainer;

import java.util.HashMap;


public class ProcessesStatusContainer {


	private static ProcessesStatusContainer istanza;
	/** Maps process Pid to its container*/
	public HashMap<String, CommonjWorkContainer> pidContainerMap;

	/** Maps process Pid to its parameters*/
	public HashMap<String, java.util.Map> pidParametersMap;

	
	
	private ProcessesStatusContainer()
	{
		pidContainerMap = new HashMap<String, CommonjWorkContainer>();
	}

	public static ProcessesStatusContainer getInstance()
	{
		if (istanza == null)
		{
			istanza = new ProcessesStatusContainer();
		}

		return istanza;
	}

	public HashMap<String, CommonjWorkContainer> getPidContainerMap() {
		return pidContainerMap;
	}

	public void setPidContainerMap(
			HashMap<String, CommonjWorkContainer> pidContainerMap) {
		this.pidContainerMap = pidContainerMap;
	}


	
}
