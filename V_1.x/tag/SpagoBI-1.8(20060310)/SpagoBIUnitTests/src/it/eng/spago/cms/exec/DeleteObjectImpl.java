package it.eng.spago.cms.exec;

import it.eng.spago.base.Constants;
import it.eng.spago.base.SourceBean;
import it.eng.spago.cms.exceptions.OperationExecutionException;
import it.eng.spago.cms.exceptions.PathNotValidException;
import it.eng.spago.cms.util.OperationDescriptorUtils;
import it.eng.spago.cms.util.Path;
import it.eng.spago.cms.util.RepositoryNodeUtils;
import it.eng.spago.cms.util.constants.CMSConstants;
import it.eng.spago.error.EMFInternalError;
import it.eng.spago.tracing.TracerSingleton;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.ReferentialIntegrityException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;


/**
 * 
 * Implements method for perform the delete operation
 * on the repository
 * 
 */
public class DeleteObjectImpl {
	
	/**
	 * Implements the delete operation on the repository
	 * 
	 * @param parameters, SourceBean of operation configuration
	 * @param conn, Connection to the repository
	 * @return SourceBean with information about the result
	 * of the operation
	 * @throws EMFInternalError
	 */
	protected void execute(SourceBean parameters, CMSConnection conn) 
						 throws OperationExecutionException {

		SourceBean sourceBean = null;
		Session session = conn.getSession();
		String pathStr = null;
		String versionStr = null;
		boolean versionExist = false;
		Node node = null;
		
		try {		
			// get path string new object
		    pathStr = OperationDescriptorUtils.getStringAttribute(parameters, CMSConstants.PATH);
			// get version to retrive  (version can be null)			
            versionStr = OperationDescriptorUtils.getStringAttribute(parameters, CMSConstants.VERSION);
            // create Path  
			Path path = Path.create(pathStr);
			// get node
			if(path.isRootNode()) {
				TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.MAJOR,
		    	"DeleteObjectImpl::execute: cannot remove root node");				
			    throw new OperationExecutionException("DeleteObjectImpl::execute: " +
			    		  "cannot remove root node");
			} else {
		    	node = (Node)session.getRootNode().getNode(path.getRootRelativePathStr());
		    	if(!node.isCheckedOut()) {
		    		node.checkout();
		    	}
		    }			
			// if version is set control if version exist
			if( (versionStr!=null) && !(versionStr.trim().equals("")) ) {
				versionExist = RepositoryNodeUtils.versionNameExist(node, versionStr);
				if(!versionExist) {
		    		TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.MAJOR,
			    	"DeleteObjectImpl::execute: the version doesn't exist");				
				    throw new OperationExecutionException("DeleteObjectImpl::execute: " +
				    		"the version doesn't exist");
		    	}
			}			
			//  if version isn't set then remove node
			if( (versionStr!=null) && !(versionStr.trim().equals("")) ) {
				node.getVersionHistory().removeVersion(versionStr);
	    	}
			// if version is set remove only the version
	    	else {  
	    		node.remove();
	    	}
			// save changes
			session.save();	
		} catch (PathNotFoundException e) {
			TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.MINOR,
	    	"DeleteObjectImpl::execute: object not found");
			throw new OperationExecutionException("DeleteObjectImpl::execute: object not found");
		} catch (ReferentialIntegrityException e) {
			TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.CRITICAL,
	    	"DeleteObjectImpl::execute: cannot erase version, there's at least one reference. Probably the version to erase is the current version of the object");
			throw new OperationExecutionException("DeleteObjectImpl::execute: cannot erase version");
		} catch (RepositoryException e) {
			TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.CRITICAL,
	    	"DeleteObjectImpl::execute: repository access error");
			throw new OperationExecutionException("DeleteObjectImpl::execute: repository access error");
		} catch (PathNotValidException e) {
			TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.MAJOR,
	    	"DeleteObjectImpl::execute: malformed path");
			throw new OperationExecutionException("DeleteObjectImpl::execute: malformed path");
		} 
	}	
	
}
