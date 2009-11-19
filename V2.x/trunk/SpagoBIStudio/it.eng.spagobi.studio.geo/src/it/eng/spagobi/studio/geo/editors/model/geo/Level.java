package it.eng.spagobi.studio.geo.editors.model.geo;

import java.io.Serializable;

public class Level  implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5406582217159680931L;
	private String name;
	private String columnId;
	private String columnDesc;
	private String featureName;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getColumnId() {
		return columnId;
	}
	public void setColumnId(String columnId) {
		this.columnId = columnId;
	}
	public String getColumnDesc() {
		return columnDesc;
	}
	public void setColumnDesc(String columnDesc) {
		this.columnDesc = columnDesc;
	}
	public String getFeatureName() {
		return featureName;
	}
	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}

}
