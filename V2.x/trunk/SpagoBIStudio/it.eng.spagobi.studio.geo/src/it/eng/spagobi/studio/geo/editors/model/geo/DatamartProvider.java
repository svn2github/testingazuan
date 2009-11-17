package it.eng.spagobi.studio.geo.editors.model.geo;

public class DatamartProvider {
	private String className;
	private String hierarchy;
	private String level;
	private Metadata metadata;
	private Hierarchies hierarchies;
	private Dataset dataset;
	
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getHierarchy() {
		return hierarchy;
	}
	public void setHierarchy(String hierarchy) {
		this.hierarchy = hierarchy;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public Metadata getMetadata() {
		return metadata;
	}
	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}
	public Hierarchies getHierarchies() {
		return hierarchies;
	}
	public void setHierarchies(Hierarchies hierarchies) {
		this.hierarchies = hierarchies;
	}
	public Dataset getDataset() {
		return dataset;
	}
	public void setDataset(Dataset dataset) {
		this.dataset = dataset;
	}

}
