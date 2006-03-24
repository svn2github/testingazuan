package it.eng.spago.cms;

import java.util.List;

import it.eng.spago.cms.exceptions.OperationExecutionException;
import it.eng.spago.cms.operations.DeleteOperation;
import it.eng.spago.cms.operations.GetOperation;
import it.eng.spago.cms.operations.RestoreOperation;
import it.eng.spago.cms.operations.SearchOperation;
import it.eng.spago.cms.operations.SetOperation;

public interface IExecutionService {

	public void doSetOperation(SetOperation setOp) throws OperationExecutionException;
	public void doSetOperation(SetOperation setOp, String userId, String password) throws OperationExecutionException;
	
	public CmsNode doGetOperation(GetOperation getOp) throws OperationExecutionException;
	public CmsNode doGetOperation(GetOperation getOp, String userId, String password) throws OperationExecutionException;
	
	public void doRestoreOperation(RestoreOperation restOp) throws OperationExecutionException;
	public void doRestoreOperation(RestoreOperation restOp, String userId, String password) throws OperationExecutionException;
	
	public void doDeleteOperation(DeleteOperation delOp) throws OperationExecutionException;
	public void doDeleteOperation(DeleteOperation delOp, String userId, String password) throws OperationExecutionException;
	
	public List doSearchOperation(SearchOperation searchOp) throws OperationExecutionException;
	public List doSearchOperation(SearchOperation searchOp, String userId, String password) throws OperationExecutionException;
	
	
	
}
