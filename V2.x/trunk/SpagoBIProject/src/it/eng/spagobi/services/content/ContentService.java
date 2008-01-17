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
    /**
     * 
     * @param token String
     * @param user String
     * @param nameSubObject String
     * @return  Content
     */
    Content readSubObjectContent(String token,String user,String nameSubObject);
    /**
     * 
     * @param token String
     * @param user String
     * @param documentiId String
     * @param analysisName String
     * @param analysisDescription String
     * @param visibilityBoolean String
     * @param content String
     * @return String
     */
    String saveSubObject(String token,String user,String documentiId,String analysisName,String analysisDescription,String visibilityBoolean,String content);
    /**
     * 
     * @param token String
     * @param user String
     * @param documentiId String
     * @param templateName String
     * @param content String
     * @return String
     */
    String saveObjectTemplate(String token,String user,String documentiId,String templateName,String content);
    /**
     * 
     * @param token String
     * @param user String
     * @param biobjectId String
     * @param fileName String
     * @return Content
     */
    Content downloadAll(String token,String user,String biobjectId,String fileName);
    
    /**
     * Replaces PublishServlet !!!!
     * @param token String
     * @param user String
     * @param name String
     * @param description String
     * @param encrypted String
     * @param visible String
     * @param type String
     * @param state String
     * @param functionalityCode String
     * @param template String
     * @return String
     */
    String publishTemplate(String token,String user,HashMap attributes);
    
    /**
     * Replaces MapCatalogueManagerServlet !!!!
     * @param token String
     * @param user String
     * @param operation String
     * @return String
     */
    String mapCatalogue(String token, String user, String operation,String path,String featureName,String mapName);
	
}
