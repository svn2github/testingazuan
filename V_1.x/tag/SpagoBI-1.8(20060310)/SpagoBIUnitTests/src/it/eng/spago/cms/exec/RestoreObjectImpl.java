package it.eng.spago.cms.exec;

import it.eng.spago.base.SourceBean;
import it.eng.spago.cms.constants.Constants;
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
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.version.Version;


/**
 * 
 * Implements method for perform the restore operation
 * on the repository
 * 
 */
public class RestoreObjectImpl {
	
	/**
	 * Implements the restore operation on the repository
	 * 
	 * @param parameters, SourceBean of operation configuration
	 * @param conn, Connection to the repository
	 * @throws EMFInternalError
	 */
	protected void execute(SourceBean parameters, CMSConnection conn) throws OperationExecutionException {
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
            // create the path object  
			Path path = Path.create(pathStr);            
			// get node
			if(path.isRootNode()) {
				TracerSingleton.log(Constants.NAME_MODULE, TracerSingleton.MAJOR,
		    	"RestoreObjectImpl::execute: cannot restore root node");				
			    throw new OperationExecutionException("RestoreObjectImpl::execute: cannot restore root node");
			} else {
		    	node = (Node)session.getRootNode().getNode(path.getRootRelativePathStr());
		    	if(!node.isCheckedOut()) {
		    		node.checkout();
		    	}
		    }									
			// control if version to restore exist 
			versionExist = RepositoryNodeUtils.versionNameExist(node, versionStr);
	        if(!versionExist) {
	    		TracerSingleton.log(Constants.NAME_MODULE, TracerSingleton.MAJOR,
		    	"RestoreObjectImpl::execute: version doesn't exist");				
			    throw new OperationExecutionException("RestoreObjectImpl::execute: version doesn't exist");
	    	}	        
			// get the version
			Version version = node.getVersionHistory().getVersion(versionStr);
			// restore the version
			node.restore(version, true);
			// checkout node
			node.checkout();
		} catch (PathNotFoundException e) {
			TracerSingleton.log(Constants.NAME_MODULE, TracerSingleton.MINOR,
	    	"RestoreObjectImpl::execute: object not found");
			throw new OperationExecutionException("RestoreObjectImpl::execute: object not found");
		} catch (RepositoryException e) {
			TracerSingleton.log(Constants.NAME_MODULE, TracerSingleton.CRITICAL,
	    	"RestoreObjectImpl::execute: repository access error");
			throw new OperationExecutionException("RestoreObjectImpl::execute: repository access error");
		} catch (PathNotValidException e) {
			TracerSingleton.log(Constants.NAME_MODULE, TracerSingleton.MAJOR,
	    	"RestoreObjectImpl::execute:  malformed path");
			throw new OperationExecutionException("RestoreObjectImpl::execute: malformed path");
		} 
	}	
	
}
