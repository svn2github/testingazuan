package it.eng.spagobi.tools.distributionlist.bo;

import java.io.Serializable;
import java.util.List;

public class DistributionList implements Serializable {
	
	private int id;
	private String name = null;
	private String descr = null;
	private List emails = null;
	private List documents = null;
	

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescr() {
		return descr;
	}
	public void setDescr(String descr) {
		this.descr = descr;
	}
	public List getEmails() {
		return emails;
	}
	public void setEmails(List emails) {
		this.emails = emails;
	}
	public List getDocuments() {
		return documents;
	}
	public void setDocuments(List documents) {
		this.documents = documents;
	}
	

}
