package it.eng.spagobi.services.content;

import it.eng.spagobi.services.content.bo.Content;



/**
 * This is the ContentService interfaces
 * @author Bernabei Angelo
 *
 */
public interface ContentService {

    	/**
    	 * return the user profile informations
    	 * @param token
    	 * @return
    	 */
    Content readTemplate(String token,String user,String document);
	
}
