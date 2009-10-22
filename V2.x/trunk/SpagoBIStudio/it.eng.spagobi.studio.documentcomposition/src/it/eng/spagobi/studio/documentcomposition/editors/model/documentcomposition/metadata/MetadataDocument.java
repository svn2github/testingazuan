package it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.metadata;

import it.eng.spagobi.studio.core.util.ParametersMetadata;

import java.util.Vector;

public class MetadataDocument {
	
	
	private Integer id;
	private String name;
	private String label;
	private String description;
	private String type;
	private String engine;
	private String dataSet;
	private String dataSource;
	private String templateName;
	private String state;
	private Vector<MetadataParameter> metadataParameters;

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getEngine() {
		return engine;
	}
	public void setEngine(String engine) {
		this.engine = engine;
	}
	public String getDataSet() {
		return dataSet;
	}
	public void setDataSet(String dataSet) {
		this.dataSet = dataSet;
	}
	public String getDataSource() {
		return dataSource;
	}
	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}
	public String getTemplateName() {
		return templateName;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public Vector<MetadataParameter> getMetadataParameters() {
		return metadataParameters;
	}
	public void setMetadataParameters(Vector<MetadataParameter> metadataParameters) {
		this.metadataParameters = metadataParameters;
	}

}
