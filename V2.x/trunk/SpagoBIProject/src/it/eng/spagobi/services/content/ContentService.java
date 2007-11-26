package it.eng.spagobi.services.content;

import java.util.HashMap;

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
  
    Content readSubObjectContent(String token,String user,String nameSubObject);
    
    String saveSubObject(String token,String user,String nameSubObject,String publicVisibility,String content,String description);
    
    String saveObjectTemplate(String token,String user,String templateName,String content);
    
    Content downloadAll(String token,String user,String biobjectId,String fileName);
    
    /**
     * Replaces PublishServlet !!!!
     * @param token
     * @param user
     * @param name
     * @param description
     * @param encrypted
     * @param visible
     * @param type
     * @param state
     * @param functionalityCode
     * @param template
     * @return
     */
    String publishTemplate(String token,String user,HashMap attributes);
    
    /**
     * Replaces MapCatalogueManagerServlet !!!!
     * @param token
     * @param user
     * @param operation
     * @return
     */
    String mapCatalogue(String token, String user, String operation,String path,String featureName,String mapName);
	
}
