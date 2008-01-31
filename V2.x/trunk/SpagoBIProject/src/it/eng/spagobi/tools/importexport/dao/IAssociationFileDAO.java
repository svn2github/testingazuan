package it.eng.spagobi.tools.importexport.dao;

import java.util.List;

import it.eng.spagobi.tools.importexport.bo.AssociationFile;

public interface IAssociationFileDAO {

	public void saveAssociationFile(AssociationFile assfile, byte[] content);
	
	public List getAssociationFiles();
	
	public void deleteAssociationFile(AssociationFile assfile);
	
	public byte[] getContent(AssociationFile assfile);
	
	public AssociationFile loadFromID(String id);
	
	public boolean exists(String id);
	
}
