package it.eng.spago.cms;

import java.util.List;

import it.eng.spago.base.Constants;
import it.eng.spago.base.SourceBean;
import it.eng.spago.cms.exceptions.OperationExecutionException;
import it.eng.spago.cms.operations.DeleteOperation;
import it.eng.spago.cms.operations.GetOperation;
import it.eng.spago.cms.operations.RestoreOperation;
import it.eng.spago.cms.operations.SearchOperation;
import it.eng.spago.cms.operations.SetOperation;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.tracing.TracerSingleton;

public class CmsManager {

	IExecutionService execService = null;
	
	public CmsManager() {
		try {
			ConfigSingleton config = ConfigSingleton.getInstance();
			SourceBean execServiceSB = (SourceBean)config.getAttribute("CONTENTCONFIGURATION.EXECUTIONSERVICE");
			String classNameExecService = (String)execServiceSB.getAttribute("class");
			Class classExecService = Class.forName(classNameExecService);
			execService = (IExecutionService)classExecService.newInstance();
		} catch(Exception e) {
			TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.CRITICAL,
                    			"CmsManager::init: errore during execution service creation "+e);
		}
 	}
		
	public void execSetOperation(SetOperation setOp) throws OperationExecutionException {
		if(!controlService())
			return;
		execService.doSetOperation(setOp) ;
	}
	
	public void execSetOperation(SetOperation setOp, String user, String password) throws OperationExecutionException {
		if(!controlService())
			return;
		execService.doSetOperation(setOp, user, password);
	}
	
	public CmsNode execGetOperation(GetOperation getOp) throws OperationExecutionException {
		if(!controlService())
			return null;
		CmsNode node = execService.doGetOperation(getOp);
		return node;
	}
	
	public CmsNode execGetOperation(GetOperation getOp, String user, String password) throws OperationExecutionException {
		if(!controlService())
			return null;
		CmsNode node = execService.doGetOperation(getOp, user, password);
		return node;
	}
	
	public List execSearchOperation(SearchOperation searchOp) throws OperationExecutionException {
		if(!controlService())
			return null;
		List nodes = execService.doSearchOperation(searchOp);
		return nodes;
	}
	
	public List execSearchOperation(SearchOperation searchOp, String user, String password) throws OperationExecutionException {
		if(!controlService())
			return null;
		List nodes = execService.doSearchOperation(searchOp, user, password);
		return nodes;
	}
	
	
	public void execDeleteOperation(DeleteOperation delOp) throws OperationExecutionException {
		if(!controlService())
			return;
		execService.doDeleteOperation(delOp);
	}
	
	public void execDeleteOperation(DeleteOperation delOp, String user, String password) throws OperationExecutionException {
		if(!controlService())
			return;
		execService.doDeleteOperation(delOp, user, password);
	}
	
	
	public void execRestoreOperation(RestoreOperation restOp) throws OperationExecutionException {
		if(!controlService())
			return;
		execService.doRestoreOperation(restOp);
	}
	
	public void execRestoreOperation(RestoreOperation restOp, String user, String password) throws OperationExecutionException {
		if(!controlService())
			return;
		execService.doRestoreOperation(restOp, user, password);
	}
	
	
	private boolean controlService() {
		if(execService==null) {
			TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.CRITICAL,
        						"CmsManager::controlService: the execution service is null");
			return false;
		}else return true;
	}
	
	
}
