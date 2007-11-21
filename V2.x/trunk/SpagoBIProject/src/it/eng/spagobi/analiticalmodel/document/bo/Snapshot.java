package it.eng.spagobi.analiticalmodel.document.bo;

import java.io.Serializable;
import java.util.Date;

public class Snapshot implements Serializable {
	
	private Integer id = null;
	private Integer biobjId = null;
	private String name = null;
	private String description = null;
	private Date dateCreation = null;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getBiobjId() {
		return biobjId;
	}
	public void setBiobjId(Integer biobjId) {
		this.biobjId = biobjId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getDateCreation() {
		return dateCreation;
	}
	public void setDateCreation(Date dateCreation) {
		this.dateCreation = dateCreation;
	}
	
	
}
