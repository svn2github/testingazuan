package it.eng.spago.cms;

import java.io.InputStream;
import java.util.List;

public class CmsNode {

	String name = "";
	String path = "";
	String version = "";
	String uuid = "";
	String type = "";
	List versions = null;
	List childs = null;
	List properties = null;
	InputStream content = null;
	
	public CmsNode(String namein, String pathin, String versionin, String uuidin, String typein) {
		name = namein;
		path = pathin;
		version = versionin;
		uuid = uuidin;
		type = typein;
	}
	
	public CmsNode(String namein, String pathin, String versionin, String uuidin, 
			       String typein, InputStream contin) {
		name = namein;
		path = pathin;
		version = versionin;
		uuid = uuidin;
		type = typein;
		content = contin;
	}
	
	
	public CmsNode(String namein, String pathin, String versionin, String uuidin, String typein,
			       List childsin, List propsin, List versin, InputStream contin) {
		name = namein;
		path = pathin;
		version = versionin;
		uuid = uuidin;
		type = typein;
		childs = childsin;
		properties = propsin;
		versions = versin;
		content = contin;
	}
	
	
	public CmsNode(String namein, String pathin, String versionin, String uuidin, String typein,
		           List childsin, List propsin, List versin) {
	name = namein;
	path = pathin;
	version = versionin;
	uuid = uuidin;
	type = typein;
	childs = childsin;
	properties = propsin;
	versions = versin;
}
	
	
	public List getChilds() {
		return childs;
	}
	public void setChilds(List childs) {
		this.childs = childs;
	}
	public InputStream getContent() {
		return content;
	}
	public void setContent(InputStream content) {
		this.content = content;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public List getProperties() {
		return properties;
	}
	public void setProperties(List properties) {
		this.properties = properties;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public List getVersions() {
		return versions;
	}
	public void setVersions(List versions) {
		this.versions = versions;
	}
	
	
	
}
