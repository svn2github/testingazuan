package it.eng.spagobi.studio.core.util;

import it.eng.spagobi.sdk.documents.bo.SDKDocumentParameter;

import java.util.ArrayList;
import java.util.List;

public class ParametersMetadata {

	private List<SDKDocumentParameter> parameters = new ArrayList<SDKDocumentParameter>();

	

	public ParametersMetadata(List<SDKDocumentParameter> parameters) {
		super();
		this.parameters = parameters;
	}

	public void add(SDKDocumentParameter parameter) {
		parameters.add(parameter);
	}

	public List getContent() {
		return parameters;
	}



}
