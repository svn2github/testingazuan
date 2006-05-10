package it.eng.spagobi.importexport;

import it.eng.spago.error.EMFInternalError;
import it.eng.spago.error.EMFUserError;

import java.util.List;

public interface IExportManager {

	public void prepareExport(String pathExpFold, String nameExpFile, boolean expSubObj) throws EMFUserError;
	
	public String exportObjects(List objPaths) throws EMFUserError;
	
	public void cleanExportEnvironment();
}
