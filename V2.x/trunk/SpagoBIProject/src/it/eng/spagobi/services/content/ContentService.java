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
    String publishTemplate(String token,String user,String name,String description,String encrypted,String visible,String type,String state,String functionalityCode,String template);
    
    /**
     * Replaces MapCatalogueManagerServlet !!!!
     * @param token
     * @param user
     * @param operation
     * @return
     */
    String mapCatalogue(String token,String user,String operation);
	
}
