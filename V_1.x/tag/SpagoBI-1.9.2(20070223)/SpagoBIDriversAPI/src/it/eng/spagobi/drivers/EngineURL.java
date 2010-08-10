package it.eng.spagobi.drivers;

import java.util.Map;

public class EngineURL {
	
	private String mainURL = null;
	
	private Map parameters = null;

	public EngineURL(String mainURL, Map parameters) {
		this.mainURL = mainURL;
		this.parameters = parameters;
	}
	
	public String getMainURL() {
		return mainURL;
	}

	public void setMainURL(String mainURL) {
		this.mainURL = mainURL;
	}

	public Map getParameters() {
		return parameters;
	}

	public void setParameters(Map parameters) {
		this.parameters = parameters;
	}

	public void addParameter(String key, Object value) {
		parameters.put(key, value);
	}
	
	public void addParameters(Map parameters) {
		this.parameters.putAll(parameters);
	}
}
