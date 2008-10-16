package it.eng.spagobi.tools.dataset.common;

import it.eng.spago.security.IEngUserProfile;

import java.util.HashMap;

public interface IDataSetExecutor {
	
	String execute(IEngUserProfile profile,String dataSetLabel,HashMap parameters);

}
