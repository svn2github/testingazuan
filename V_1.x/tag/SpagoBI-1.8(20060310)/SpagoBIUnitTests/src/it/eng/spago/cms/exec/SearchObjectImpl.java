package it.eng.spago.cms.exec;

import it.eng.spago.base.SourceBean;
import it.eng.spago.cms.CmsNode;
import it.eng.spago.cms.constants.Constants;
import it.eng.spago.cms.exceptions.OperationExecutionException;
import it.eng.spago.cms.util.OperationDescriptorUtils;
import it.eng.spago.cms.util.constants.CMSConstants;
import it.eng.spago.error.EMFInternalError;
import it.eng.spago.tracing.TracerSingleton;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.InvalidQueryException;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;




/**
 * 
 * Implements method for perform the search operation
 * on the repository
 * 
 */
public class SearchObjectImpl {
	
	/**
	 * Implements the search operation on the repository
	 * 
	 * @param parameters, SourceBean of operation configuration
	 * @param conn, Connection to the repository
	 * @return SourceBean with information of the search result
	 * @throws EMFInternalError
	 */
	protected List execute(SourceBean operation, CMSConnection conn)  throws OperationExecutionException {
		List nodes = new ArrayList();
		Session session = conn.getSession();
		String queryStr = null;
		String lang = null;
		try {
			// get path string new object
		    queryStr = OperationDescriptorUtils.getStringAttribute(operation, CMSConstants.QUERY);
			// get the language of the query
		    lang = OperationDescriptorUtils.getStringAttribute(operation, CMSConstants.QUERYLANGUAGE);
			// Exec Query  
			QueryManager queryManager = session.getWorkspace().getQueryManager();
			Query query = null;
			if(lang.equalsIgnoreCase(CMSConstants.QUERYLANGUAGEJCRQL)) {
				query = queryManager.createQuery(queryStr, Query.SQL );
			} 
			else if(lang.equalsIgnoreCase(CMSConstants.QUERYLANGUAGEXPATH)) {
				query = queryManager.createQuery(queryStr, Query.XPATH);
			}
			// exec query
			QueryResult queryResult = query.execute();
			// retrive information from result			
			NodeIterator ni =  queryResult.getNodes();
			while (ni.hasNext()){
				Node node = ni.nextNode();
				String uuid = node.getUUID();
                String name = node.getName();
                String path = node.getPath();
                CmsNode cmsnode = new CmsNode(name, path, "", uuid, "");
                nodes.add(cmsnode);
			}
		} catch (InvalidQueryException e) {
			TracerSingleton.log(Constants.NAME_MODULE, TracerSingleton.MAJOR,
	    	"SearchObjectImpl::execute: query not valid");				
		    throw new OperationExecutionException("SearchObjectImpl::execute: query not valid");			
		} catch (RepositoryException e) {
			TracerSingleton.log(Constants.NAME_MODULE, TracerSingleton.CRITICAL,
	    	"SearchObjectImpl::execute: repository access error");
			throw new OperationExecutionException("SearchObjectImpl::execute: repository access error");
		} 
		return nodes;
	}	
	
}
