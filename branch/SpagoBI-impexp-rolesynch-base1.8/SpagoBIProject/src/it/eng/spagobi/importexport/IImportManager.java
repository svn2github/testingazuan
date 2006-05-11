package it.eng.spagobi.importexport;

import it.eng.spago.error.EMFUserError;

import java.util.List;
import java.util.Map;

public interface IImportManager {

	public void prepareImport(String pathImportTmpFold, String archiveName, byte[] archiveContent) throws EMFUserError;
	
	public String getExportVersion();
	
	public String getCurrentVersion();
	
	public void importObjects() throws EMFUserError;
	
	public List getExportedRoles() throws EMFUserError;
	
	public List getExportedEngines() throws EMFUserError;
	
	public List getExportedConnections() throws EMFUserError;
	
	public void updateRoleReferences(Map roleAssociations) throws EMFUserError;
	
	public void updateEngineReferences(Map roleAssociations) throws EMFUserError;
	
	public void updateConnectionReferences(Map connAssociations) throws EMFUserError;
	
	public void updateMetadataReferences(ExistingMetadata emd) throws EMFUserError;
	
	public void commitAllChanges() throws EMFUserError;
	
	public ExistingMetadata checkExistingMetadata() throws EMFUserError;
	
	public void stopImport();
}
