package it.eng.spagobi.importexport.to;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spagobi.importexport.ImportExportConstants;
import it.eng.spagobi.utilities.GeneralUtilities;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.io.FileInputStream;

public class AssociationFile {

	private String id = "";
	private String name = "";
	private String description = "";
	private long dateCreation = 0;
	
	public long getDateCreation() {
		return dateCreation;
	}
	public void setDateCreation(long dateCreation) {
		this.dateCreation = dateCreation;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public static boolean isValidContent(String xmlStr) {
		try {
			if (xmlStr == null || xmlStr.trim().equals("")) throw new Exception("Empty content in input!");
			SourceBean associationSBtmp = SourceBean.fromXMLString(xmlStr);
			SourceBean roleAssSBtmp = (SourceBean)associationSBtmp.getAttribute("ROLE_ASSOCIATIONS");
			if(roleAssSBtmp==null) throw new Exception("Cannot recover ROLE_ASSOCIATIONS bean");
			SourceBean engineAssSBtmp = (SourceBean)associationSBtmp.getAttribute("ENGINE_ASSOCIATIONS");
			if(engineAssSBtmp==null) throw new Exception("Cannot recover ENGINE_ASSOCIATIONS bean");
			SourceBean connectionAssSBtmp = (SourceBean)associationSBtmp.getAttribute("CONNECTION_ASSOCIATIONS");
			if(connectionAssSBtmp==null) throw new Exception("Cannot recover CONNECTION_ASSOCIATIONS bean");
			return true;
		} catch (Exception e) {
			SpagoBITracer.major(ImportExportConstants.NAME_MODULE, AssociationFile.class.getName(), "isValidContent", 
					"Association file not valid: \n " + e.getMessage());
			return false;
		}
	}
	
	public static boolean isValidContent(byte[] bytes) {
		if (bytes == null || bytes.length == 0) return false;
		else return isValidContent(new String(bytes));
	}
	
	public static boolean isValidContent(FileInputStream fis) {
		if (fis == null) return false;
		byte[] bytes = GeneralUtilities.getByteArrayFromInputStream(fis);
		return isValidContent(bytes);
	}
}
