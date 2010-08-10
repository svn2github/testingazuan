package it.eng.spagobi.tools.dataset.common.dataproxy;

import it.eng.spago.error.EMFInternalError;
import it.eng.spago.security.IEngUserProfile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public abstract class AbstractDataProxy implements IDataProxy{

	HashMap parametersMap;
	IEngUserProfile profile;

	public void bindParameters(HashMap _parametersMap){
		parametersMap=_parametersMap;
	}

	public HashMap getParametersMap() {
		return parametersMap;
	}

	public void setParametersMap(HashMap parametersMap) {
		this.parametersMap = parametersMap;
	}

	public IEngUserProfile getProfile() {
		return profile;
	}

	public void setProfile(IEngUserProfile profile) {
		this.profile = profile;
	}



}
