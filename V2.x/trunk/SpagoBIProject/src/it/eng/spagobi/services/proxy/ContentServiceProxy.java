package it.eng.spagobi.services.proxy;

import java.util.HashMap;

import it.eng.spagobi.services.content.bo.Content;
import it.eng.spagobi.services.content.stub.ContentServiceServiceLocator;
import it.eng.spagobi.services.security.exceptions.SecurityException;

import javax.servlet.http.HttpSession;
import javax.xml.rpc.ServiceException;

import org.apache.log4j.Logger;

public class ContentServiceProxy extends AbstractServiceProxy{


    static private Logger logger = Logger.getLogger(ContentServiceProxy.class);


    public ContentServiceProxy(HttpSession session) {
	super( session);
    }
    public ContentServiceProxy() {
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
    
    public Content readTemplate(String user, String document) {
	logger.debug("IN");
	try {
	    String ticket = "";
	    if (ssoIsActive){
		ticket=readTicket();
	    }
	    
	    return lookUp().readTemplate(ticket, user, document);
	} catch (Exception e) {
	    logger.error("Error during service execution",e);

	}finally{
	    logger.debug("IN");
	}
	return null;
    }
    
    public String publishTemplate( String user, HashMap attributes) {
	logger.debug("IN");
	try {
	    String ticket = "";
	    if (ssoIsActive){
		ticket=readTicket();
	    }
	    return lookUp().publishTemplate(ticket, user, attributes);
	} catch (Exception e) {
	    logger.error("Error during service execution",e);

	}finally{
	    logger.debug("IN");
	}
	return null;
    }
    
    public String mapCatalogue( String user, String operation,String path,String featureName,String mapName){
	logger.debug("IN");
	try {
	    String ticket = "";
	    if (ssoIsActive){
		ticket=readTicket();
	    }
	    return lookUp().mapCatalogue(ticket, user, operation,path,featureName,mapName);
	} catch (Exception e) {
	    logger.error("Error during service execution",e);

	}finally{
	    logger.debug("IN");
	}
	return null;	
    }
    
    public Content readSubObjectContent(String user,String nameSubObject){
	logger.debug("IN");
	try {
	    String ticket = "";
	    if (ssoIsActive){
		ticket=readTicket();
	    }
	    return lookUp().readSubObjectContent(ticket, user, nameSubObject);
	} catch (Exception e) {
	    logger.error("Error during service execution",e);

	}finally{
	    logger.debug("IN");
	}
	return null;
    }
    
    public String saveSubObject(String user,String documentiId,String analysisName,String analysisDescription,String visibilityBoolean,String content){
	logger.debug("IN");
	try {
	    String ticket = "";
	    if (ssoIsActive){
		ticket=readTicket();
	    }
	    return lookUp().saveSubObject(ticket, user, documentiId,analysisName, analysisDescription, visibilityBoolean, content);
	} catch (Exception e) {
	    logger.error("Error during service execution",e);

	}finally{
	    logger.debug("IN");
	}
	return null;
    }
    
    public String saveObjectTemplate(String user,String documentiId,String templateName,String content){
	logger.debug("IN");
	try {
	    String ticket = "";
	    if (ssoIsActive){
		ticket=readTicket();
	    }
	    return lookUp().saveObjectTemplate(ticket, user, documentiId, templateName, content);
	} catch (Exception e) {
	    logger.error("Error during service execution",e);

	}finally{
	    logger.debug("IN");
	}
	return null;
    }
    
    public Content downloadAll(String user,String biobjectId,String fileName){
	logger.debug("IN");
	try {
	    String ticket = "";
	    if (ssoIsActive){
		ticket=readTicket();
	    }
	    return lookUp().downloadAll(ticket, user, biobjectId, fileName);
	} catch (Exception e) {
	    logger.error("Error during service execution",e);

	}finally{
	    logger.debug("IN");
	}
	return null;
    }
    
}
