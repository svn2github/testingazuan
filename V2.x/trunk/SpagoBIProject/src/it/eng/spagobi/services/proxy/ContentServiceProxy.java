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
     * @param user
     * @param session
     */
    public ContentServiceProxy(String user,HttpSession session) {
	super( user,session);
    }
    private ContentServiceProxy() {
	super();
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
     * @param document
     * @return
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
     * @param attributes
     * @return
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
     * @param operation
     * @param path
     * @param featureName
     * @param mapName
     * @return
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
     * @param nameSubObject
     * @return
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
     * @param documentiId
     * @param analysisName
     * @param analysisDescription
     * @param visibilityBoolean
     * @param content
     * @return
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
     * @param documentiId
     * @param templateName
     * @param content
     * @return
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
     * @param biobjectId
     * @param fileName
     * @return
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
