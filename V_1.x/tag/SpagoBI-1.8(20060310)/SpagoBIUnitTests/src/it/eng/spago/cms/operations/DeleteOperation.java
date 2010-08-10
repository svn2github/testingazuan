package it.eng.spago.cms.operations;

import it.eng.spago.base.SourceBean;
import it.eng.spago.cms.exceptions.BuildOperationException;
import it.eng.spago.cms.constants.Constants;
import it.eng.spago.tracing.TracerSingleton;

/*
	<OPERATION name="">
		<DELETEOPERATION path="?" version="?" />
	</OPERATION> 
*/


public class DeleteOperation {

	
	private SourceBean operationDescriptor = null;
	
	
	public DeleteOperation() throws BuildOperationException{	
		try {
			operationDescriptor = new SourceBean(Constants.NAME_DELETE_OPERATION);
			operationDescriptor.setAttribute(Constants.PATH, "");
			operationDescriptor.setAttribute(Constants.VERSION, "");
		} catch (Exception e) {
			TracerSingleton.log(Constants.NAME_MODULE, TracerSingleton.CRITICAL,
                                "DeleteOperation::<init>(): Error during the creation of the delete operation" +
                                "SourceBean");
			throw new BuildOperationException("DeleteOperation::<init>(): Error during the creation of the " +
					                          "delete operation SourceBean", e);
		}
	}
	
	public DeleteOperation(String path) throws BuildOperationException{	
		try {
			operationDescriptor = new SourceBean(Constants.NAME_DELETE_OPERATION);
			operationDescriptor.setAttribute(Constants.PATH, path);
			operationDescriptor.setAttribute(Constants.VERSION, "");
		} catch (Exception e) {
			TracerSingleton.log(Constants.NAME_MODULE, TracerSingleton.CRITICAL,
                                "DeleteOperation::<init>(String): Error during the creation of the delete operation" +
                                "SourceBean");
			throw new BuildOperationException("DeleteOperation::<init>(String): Error during the creation of the " +
					                          "delete operation SourceBean", e);
		}
	}
	
	public DeleteOperation(String path, String version) throws BuildOperationException{	
		try {
			operationDescriptor = new SourceBean(Constants.NAME_DELETE_OPERATION);
			operationDescriptor.setAttribute(Constants.PATH, path);
			operationDescriptor.setAttribute(Constants.VERSION, version);
		} catch (Exception e) {
			TracerSingleton.log(Constants.NAME_MODULE, TracerSingleton.CRITICAL,
                                "DeleteOperation::<init>(String, String): Error during the creation of the delete operation" +
                                "SourceBean");
			throw new BuildOperationException("DeleteOperation::<init>(String, String): Error during the creation of the " +
					                          "delete operation SourceBean", e);
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
                                "DeleteOperation::setPath: Error during the path setting");
            throw new BuildOperationException("DeleteOperation::setPath: Error during the path setting", e);
		}
	}
	
	public void setVersion(String version) throws BuildOperationException {
		try {
			operationDescriptor.updAttribute(Constants.VERSION, version);
		} catch (Exception e) {
			TracerSingleton.log(Constants.NAME_MODULE, TracerSingleton.CRITICAL,
                                "DeleteOperation::setVersion: Error during the version setting");
            throw new BuildOperationException("DeleteOperation::setVersion: Error during the version setting", e);
		}
	}
	
	
}
