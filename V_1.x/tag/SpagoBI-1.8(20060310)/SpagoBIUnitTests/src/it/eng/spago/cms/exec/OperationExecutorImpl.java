package it.eng.spago.cms.exec;

import it.eng.spago.cms.CmsNode;
import it.eng.spago.cms.IExecutionService;
import it.eng.spago.cms.constants.Constants;
import it.eng.spago.cms.exceptions.OperationExecutionException;
import it.eng.spago.cms.init.CMSManager;
import it.eng.spago.cms.operations.DeleteOperation;
import it.eng.spago.cms.operations.GetOperation;
import it.eng.spago.cms.operations.RestoreOperation;
import it.eng.spago.cms.operations.SearchOperation;
import it.eng.spago.cms.operations.SetOperation;
import it.eng.spago.error.EMFInternalError;
import it.eng.spago.tracing.TracerSingleton;

import java.util.List;

public class OperationExecutorImpl implements IExecutionService {

	public CmsNode doGetOperation(GetOperation getOp) throws OperationExecutionException {
		CmsNode node = null;
		CMSConnection connection = getConnection();
		GetObjectImpl getObjImpl = new GetObjectImpl();
		node = getObjImpl.execute(getOp.getOperationDescriptor(), connection);
		closeConnection(connection);
		return node; 
	}

	public CmsNode doGetOperation(GetOperation getOp, String name, String password) throws OperationExecutionException {
		CmsNode node = null;
		CMSConnection connection = getConnection();
		GetObjectImpl getObjImpl = new GetObjectImpl();
		node = getObjImpl.execute(getOp.getOperationDescriptor(), connection);
		closeConnection(connection);
		return node; 
	}
	
	
	public void doSetOperation(SetOperation setOp) throws OperationExecutionException {
		CMSConnection connection = getConnection();
		SetObjectImpl setObjImpl = new SetObjectImpl();
		setObjImpl.execute(setOp.getOperationDescriptor(), connection);
		closeConnection(connection);
	}

	public void doSetOperation(SetOperation setOp, String name, String password) throws OperationExecutionException {
		CMSConnection connection = getConnection();
		SetObjectImpl setObjImpl = new SetObjectImpl();
		setObjImpl.execute(setOp.getOperationDescriptor(), connection);
		closeConnection(connection);
	}
	
	public void doDeleteOperation(DeleteOperation delOp) throws OperationExecutionException {
		CMSConnection connection = getConnection();
		DeleteObjectImpl delObjImpl = new DeleteObjectImpl();
		delObjImpl.execute(delOp.getOperationDescriptor(), connection);
		closeConnection(connection);
	}

	public void doDeleteOperation(DeleteOperation delOp, String name, String password) throws OperationExecutionException {
		CMSConnection connection = getConnection();
		DeleteObjectImpl delObjImpl = new DeleteObjectImpl();
		delObjImpl.execute(delOp.getOperationDescriptor(), connection);
		closeConnection(connection);
	}
	
	
	public void doRestoreOperation(RestoreOperation resOp) throws OperationExecutionException {
		CMSConnection connection = getConnection();
		RestoreObjectImpl resObjImpl = new RestoreObjectImpl();
		resObjImpl.execute(resOp.getOperationDescriptor(), connection);
		closeConnection(connection);
	}

	public void doRestoreOperation(RestoreOperation resOp, String name, String password) throws OperationExecutionException {
		CMSConnection connection = getConnection();
		RestoreObjectImpl resObjImpl = new RestoreObjectImpl();
		resObjImpl.execute(resOp.getOperationDescriptor(), connection);
		closeConnection(connection);
	}

	public List doSearchOperation(SearchOperation searchOp) throws OperationExecutionException {
		List nodes = null;
		CMSConnection connection = getConnection();
		SearchObjectImpl searchObjImpl = new SearchObjectImpl();
		nodes = searchObjImpl.execute(searchOp.getOperationDescriptor(), connection);
		closeConnection(connection);
		return nodes; 
	}

	public List doSearchOperation(SearchOperation searchOp, String name, String password) throws OperationExecutionException {
		List nodes = null;
		CMSConnection connection = getConnection();
		SearchObjectImpl searchObjImpl = new SearchObjectImpl();
		nodes = searchObjImpl.execute(searchOp.getOperationDescriptor(), connection);
		closeConnection(connection);
		return nodes; 
	}
	
	
	private CMSConnection getConnection() {
		CMSConnection connection = null;
	    try {
	    	connection = CMSManager.getInstance().getConnection();
	    } catch (EMFInternalError e1) {
	    	TracerSingleton.log(Constants.NAME_MODULE, TracerSingleton.MAJOR,
	    						"DeleteObjectImpl::execute: cannot get a connection");				
	    	return null;
		}
	    return connection;
	}
	
	
	
		
	/**
	 * Close the connection passed
	 * @param conn, connection to close
	 */
	private void closeConnection(CMSConnection conn) {
		if( conn != null && !conn.isClose() ) {
			conn.close();
		}
	}
	
	
	
}
