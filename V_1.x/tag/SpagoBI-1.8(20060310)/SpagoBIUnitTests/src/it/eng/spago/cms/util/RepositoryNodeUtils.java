package it.eng.spago.cms.util;



import javax.jcr.Node;

import javax.jcr.version.Version;
import javax.jcr.version.VersionIterator;
import it.eng.spago.base.Constants;
import it.eng.spago.tracing.TracerSingleton;

/**
 * 
 * Utility class that implements methods to perform controls and extract information 
 * on the content repository and his nodes.
 * 
 */

public class RepositoryNodeUtils {

	/**
	 * The method controls if the node has a version with the input name. 
	 * 
	 * @param node Node to control
	 * @param version Name version to search inside the node
	 * @return boolean, true if the version exists false otherwise
	 */
	public static boolean versionNameExist(Node node, String version) {
        
		boolean find = false;
		try {
			VersionIterator iter = node.getVersionHistory().getAllVersions();
			while(iter.hasNext()) {
				Version ver = iter.nextVersion();
				if(ver.getName().equals(version)) {
					find = true;
					break;
				}
			}
		} catch(Exception e) {
			TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.MAJOR,
	    	"UtilNode::versionExist: error during the recovery versions of the node" + e);	
		}
    	return find;
	}
	
}
