package it.eng.spago.cms.operations;

import it.eng.spago.base.SourceBean;
import it.eng.spago.cms.exceptions.BuildOperationException;
import it.eng.spago.cms.constants.Constants;
import it.eng.spago.tracing.TracerSingleton;

/*
    <OPERATION name="RESTORE_OBJECT">
  	    <RESTOREOPERATION path="?" version="?" />  
  	</OPERATION>
*/

public class RestoreOperation {

	private SourceBean operationDescriptor = null;
	
	public RestoreOperation() throws BuildOperationException {
		try {
			operationDescriptor = new SourceBean(Constants.NAME_RESTORE_OPERATION);
			operationDescriptor.setAttribute(Constants.PATH, "");
			operationDescriptor.setAttribute(Constants.VERSION, "");
		} catch (Exception e) {
			TracerSingleton.log(Constants.NAME_MODULE, TracerSingleton.CRITICAL,
                                "RestoreOperation::<init>(): Error during the creation of the restore operation" +
                                "SourceBean");
			throw new BuildOperationException("RestoreOperation::<init>(): Error during the creation of the " +
					                          "restore operation SourceBean", e);
		}
	}
	
	public RestoreOperation(String path) throws BuildOperationException {
		try {
			operationDescriptor = new SourceBean(Constants.NAME_RESTORE_OPERATION);
			operationDescriptor.setAttribute(Constants.PATH, path);
			operationDescriptor.setAttribute(Constants.VERSION, "");
		} catch (Exception e) {
			TracerSingleton.log(Constants.NAME_MODULE, TracerSingleton.CRITICAL,
                                "RestoreOperation::<init>(String): Error during the creation of the restore operation" +
                                "SourceBean");
			throw new BuildOperationException("RestoreOperation::<init>(String): Error during the creation of the " +
					                          "restore operation SourceBean", e);
		}
	}
	
	public RestoreOperation(String path, String version) throws BuildOperationException {
		try {
			operationDescriptor = new SourceBean(Constants.NAME_RESTORE_OPERATION);
			operationDescriptor.setAttribute(Constants.PATH, path);
			operationDescriptor.setAttribute(Constants.VERSION, version);
		} catch (Exception e) {
			TracerSingleton.log(Constants.NAME_MODULE, TracerSingleton.CRITICAL,
                                "RestoreOperation::<init>(String, String): " +
                                "Error during the creation of the restore operation" +
                                "SourceBean");
			throw new BuildOperationException("RestoreOperation::<init>(String, String): " +
											  "Error during the creation of the " +
					                          "restore operation SourceBean", e);
		}
	}
	
	public SourceBean getOperationDescriptor() {
		return operationDescriptor;
	}

	public void setPath(String path) throws BuildOperationException {
		try {
			operationDescriptor.updAttribute(Constants.PATH, path);
		} catch (Exception e) {
			TracerSingleton.log(Constants.NAME_MODULE, TracerSingleton.CRITICAL,
                                "RestoreOperation::setPath: Error during the path setting");
            throw new BuildOperationException("RestoreOperation::setPath: Error during the path setting", e);
		}
	}
	
	public void setVersion(String ver) throws BuildOperationException {
		try {
			operationDescriptor.updAttribute(Constants.VERSION, ver);
		} catch (Exception e) {
			TracerSingleton.log(Constants.NAME_MODULE, TracerSingleton.CRITICAL,
                                "RestoreOperation::setVersion: Error during the vversion setting");
            throw new BuildOperationException("RestoreOperation::setVersion: Error during the version setting", e);
		}
	}
	
	
}
