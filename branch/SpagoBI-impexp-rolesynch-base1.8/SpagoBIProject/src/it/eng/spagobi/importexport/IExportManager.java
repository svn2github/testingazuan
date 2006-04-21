package it.eng.spagobi.importexport;

import java.util.List;
import it.eng.spago.error.EMFInternalError;
import it.eng.spago.error.EMFUserError;

public interface IExportManager {

	public void prepareExport(String pathExpFold, String nameExpFile) throws EMFUserError, EMFInternalError;
	
	public void exportObjects(List objPaths) throws EMFUserError, EMFInternalError;
	
}
