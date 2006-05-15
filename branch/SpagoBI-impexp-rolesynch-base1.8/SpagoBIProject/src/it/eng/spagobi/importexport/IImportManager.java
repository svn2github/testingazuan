package it.eng.spagobi.importexport;

import it.eng.spago.error.EMFUserError;

import java.util.List;

public interface IImportManager {

	public void prepareImport(String pathImportTmpFold, String archiveName, byte[] archiveContent) throws EMFUserError;
	
	public String getExportVersion();
	
	public String getCurrentVersion();
	
	public void importObjects() throws EMFUserError;
	
	public List getExportedRoles() throws EMFUserError;
	
	public List getExportedEngines() throws EMFUserError;
	
	public List getExportedConnections() throws EMFUserError;
	
	public void commitAllChanges() throws EMFUserError;
	
	public void checkExistingMetadata() throws EMFUserError;
	
	public void stopImport();
	
	public MetadataAssociations getMetadataAssociation();
}
