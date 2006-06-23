package it.eng.spago.cms.exec;

import it.eng.spago.base.Constants;
import it.eng.spago.base.SourceBean;
import it.eng.spago.cms.exceptions.PathNotValidException;
import it.eng.spago.cms.util.OperationDescriptorUtils;
import it.eng.spago.cms.util.Path;
import it.eng.spago.cms.util.constants.CMSConstants;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFInternalError;
import it.eng.spago.tracing.TracerSingleton;

import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;


/**
 * 
 * Implements methods for perform rename operation on 
 * a node of the repository
 * 
 */

public class RenameObjectImpl {
	
	/**
	 * 
	 * Implements the rename operation on the repository
	 * 
	 * @param parameters, SourceBean of operation configuration
	 * @param conn, Connection to the repository
	 * @return SourceBean with information about the result
	 * of the operation
	 * @throws EMFInternalError
	 */
	protected void execute(SourceBean parameters, CMSConnection conn) throws EMFInternalError {

		SourceBean sourceBean = null;
		Session session = conn.getSession();
		String oldPathStr = null;
		String newPathStr = null;
		
		try {		
			// get path node
		    oldPathStr = OperationDescriptorUtils.getStringAttribute(parameters, CMSConstants.PATH);
			// get path new node
		    newPathStr = OperationDescriptorUtils.getStringAttribute(parameters, CMSConstants.NEWPATH);
			Path oldPath = Path.create(oldPathStr);
			Path newPath = Path.create(newPathStr);
			if(oldPath.isRootNode()) {
				TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.MAJOR,
		    	"RenameObjectImpl::execute: impossible to rename root node");				
			    throw new EMFInternalError(EMFErrorSeverity.ERROR, 
			    "RenameObjectImpl::execute: impossible to rename root node");
			} 
			if(newPath.isRootNode()) {
				TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.MAJOR,
		    	"RenameObjectImpl::execute: new path not valid");				
			    throw new EMFInternalError(EMFErrorSeverity.ERROR, 
			    "RenameObjectImpl::execute: new path not valid");
			} 
			// control if new path already exists
			/*
			try {
				session.getRootNode().getNode(newPath.getRootRelativePathStr());
				TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.MAJOR,
		    	"RenameObjectImpl::execute: new path already exists");				
			    throw new EMFInternalError(EMFErrorSeverity.ERROR, 
			    "RenameObjectImpl::execute: new path already exists");
			}
			catch (PathNotFoundException pnfe) {
				// ignore, the node doesn't exist
			}
			*/
			String nameWSP = session.getWorkspace().getName();
			session.getWorkspace().clone(nameWSP, oldPath.getAbsPathStr(), newPath.getAbsPathStr(), false);
			
			
		} catch (PathNotFoundException e) {
			TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.MINOR,
	    	"RenameObjectImpl::execute: object not found");
			throw new EMFInternalError(EMFErrorSeverity.ERROR, 
			"RenameObjectImpl::execute: object not found");
		} catch (RepositoryException e) {
			TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.CRITICAL,
	    	"RenameObjectImpl::execute: repository access error");
			throw new EMFInternalError(EMFErrorSeverity.ERROR, 
			"RenameObjectImpl::execute: repository access error");
		} catch (PathNotValidException e) {
			TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.MAJOR,
	    	"RenameObjectImpl::execute: malformed path");
			throw new EMFInternalError(EMFErrorSeverity.ERROR, 
			"RenameObjectImpl::execute: malformed path");
		} 
	}	
	
}
