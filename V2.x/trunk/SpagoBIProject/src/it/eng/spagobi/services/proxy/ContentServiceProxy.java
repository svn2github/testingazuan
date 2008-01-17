package it.eng.spagobi.services.proxy;

import java.util.HashMap;

import it.eng.spagobi.services.content.bo.Content;
import it.eng.spagobi.services.content.stub.ContentServiceServiceLocator;
import it.eng.spagobi.services.security.exceptions.SecurityException;

import javax.servlet.http.HttpSession;
import javax.xml.rpc.ServiceException;

import org.apache.log4j.Logger;

/**
 * 
 * Proxy of Content Service
 *
 */
public final class ContentServiceProxy extends AbstractServiceProxy{


    static private Logger logger = Logger.getLogger(ContentServiceProxy.class);

    /**
     * use this i engine context only
     * @param user user ID
     * @param session http session
     */
    public ContentServiceProxy(String user,HttpSession session) {
	super( user,session);
    }
    
    private ContentServiceProxy() {
	super ();
    }    

    private it.eng.spagobi.services.content.stub.ContentService lookUp() throws SecurityException {
	try {
	    ContentServiceServiceLocator locator = new ContentServiceServiceLocator();   
	    it.eng.spagobi.services.content.stub.ContentService service=null;
	    if (serviceUrl!=null ){
		    service = locator.getContentService(serviceUrl);		
	    }else {
		    service = locator.getContentService();		
	    }
	    return service;
	} catch (ServiceException e) {
	    logger.error("Error during service execution", e);
	    throw new SecurityException();
	}
    }
    /**
     * 
     * @param document String
     * @return Content
     */
    public Content readTemplate(String document) {
	logger.debug("IN");
	try {
	    return lookUp().readTemplate(readTicket(), userId, document);
	} catch (Exception e) {
	    logger.error("Error during service execution",e);

	}finally{
	    logger.debug("IN");
	}
	return null;
    }
    /**
     * 
     * @param attributes HashMap 
     * @return String
     */
    public String publishTemplate( HashMap attributes) {
	logger.debug("IN");
	try {
	    return lookUp().publishTemplate(readTicket(), userId, attributes);
	} catch (Exception e) {
	    logger.error("Error during service execution",e);

	}finally{
	    logger.debug("IN");
	}
	return null;
    }
    /**
     * 
     * @param operation String
     * @param path String
     * @param featureName String
     * @param mapName String
     * @return String
     */
    public String mapCatalogue( String operation,String path,String featureName,String mapName){
	logger.debug("IN");
	try {
	    return lookUp().mapCatalogue(readTicket(), userId, operation,path,featureName,mapName);
	} catch (Exception e) {
	    logger.error("Error during service execution",e);

	}finally{
	    logger.debug("IN");
	}
	return null;	
    }
    /**
     * 
     * @param nameSubObject String
     * @return Content
     */
    public Content readSubObjectContent(String nameSubObject){
	logger.debug("IN");
	try {
	    return lookUp().readSubObjectContent(readTicket(), userId, nameSubObject);
	} catch (Exception e) {
	    logger.error("Error during service execution",e);

	}finally{
	    logger.debug("IN");
	}
	return null;
    }
    /**
     * 
     * @param documentiId String
     * @param analysisName String
     * @param analysisDescription String
     * @param visibilityBoolean String
     * @param content  String
     * @return  String
     */
    public String saveSubObject(String documentiId,String analysisName,String analysisDescription,String visibilityBoolean,String content){
	logger.debug("IN");
	try {
	    return lookUp().saveSubObject(readTicket(), userId, documentiId,analysisName, analysisDescription, visibilityBoolean, content);
	} catch (Exception e) {
	    logger.error("Error during service execution",e);

	}finally{
	    logger.debug("IN");
	}
	return null;
    }
    /**
     * 
     * @param documentiId  String
     * @param templateName String
     * @param content String
     * @return String
     */
    public String saveObjectTemplate(String documentiId,String templateName,String content){
	logger.debug("IN");
	try {
	    return lookUp().saveObjectTemplate(readTicket(), userId, documentiId, templateName, content);
	} catch (Exception e) {
	    logger.error("Error during service execution",e);

	}finally{
	    logger.debug("IN");
	}
	return null;
    }

    /**
     * 
     * @param biobjectId String
     * @param fileName String
     * @return  String
     * 
     */
    public Content downloadAll(String biobjectId,String fileName){
	logger.debug("IN");
	try {
	    return lookUp().downloadAll(readTicket(), userId, biobjectId, fileName);
	} catch (Exception e) {
	    logger.error("Error during service execution",e);

	}finally{
	    logger.debug("IN");
	}
	return null;
    }
    
}
