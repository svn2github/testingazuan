package it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.metadata;

import java.util.Vector;

public class MetadataDocument {
	private String name;
	private Vector<MetadataParameter> parameters;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Vector<MetadataParameter> getParameters() {
		return parameters;
	}
	public void setParameters(Vector<MetadataParameter> parameters) {
		this.parameters= parameters;
	}


}
