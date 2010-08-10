package it.eng.spagobi.importexport.to.dao;

import java.util.List;

import it.eng.spagobi.importexport.to.AssociationFile;

public interface IAssociationFileDAO {

	public void saveAssociationFile(AssociationFile assfile, byte[] content);
	
	public List getAssociationFiles();
	
	public void deleteAssociationFile(AssociationFile assfile);
	
	public byte[] getContent(AssociationFile assfile);
	
	public AssociationFile loadFromID(String id);
	
	public boolean exists(String id);
	
}
