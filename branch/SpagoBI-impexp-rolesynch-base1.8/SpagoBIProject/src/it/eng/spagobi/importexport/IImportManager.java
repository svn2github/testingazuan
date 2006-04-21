package it.eng.spagobi.importexport;

import java.util.List;
import java.util.Map;

import it.eng.spago.error.EMFInternalError;
import it.eng.spago.error.EMFUserError;

public interface IImportManager {

	public void prepareImport(String pathImportTmpFold, String pathArchiveFile) throws EMFUserError, EMFInternalError;
	
	public String getExportVersion();
	
	public String getCurrentVersion();
	
	public void importObjects() throws EMFUserError, EMFInternalError;
	
	public List getExportedRoles() throws EMFUserError, EMFInternalError;
	
	public List getExportedEngines() throws EMFUserError, EMFInternalError;
	
	public void updateRoleReferences(Map roleAssociations) throws EMFUserError, EMFInternalError;
	
	public void updateEngineReferences(Map roleAssociations) throws EMFUserError, EMFInternalError;
	
	public void commitAllChanges() throws EMFUserError, EMFInternalError;
}
