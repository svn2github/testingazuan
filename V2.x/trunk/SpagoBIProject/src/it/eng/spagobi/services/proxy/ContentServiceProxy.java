package it.eng.spagobi.services.proxy;

import java.util.HashMap;

import it.eng.spagobi.services.content.bo.Content;
import it.eng.spagobi.services.content.stub.ContentServiceServiceLocator;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

public class ContentServiceProxy extends AbstractServiceProxy{


    static private Logger logger = Logger.getLogger(ContentServiceProxy.class);


    public ContentServiceProxy(HttpSession session) {
	super( session);
    }
    public ContentServiceProxy() {
	super();
    }    

    public Content readTemplate(String user, String document) {
	logger.debug("IN");
	try {
	    String ticket = "";
	    if (ssoIsActive){
		ticket=readTicket();
	    }
	    ContentServiceServiceLocator locator = new ContentServiceServiceLocator();
	    it.eng.spagobi.services.content.stub.ContentService service = locator
		    .getContentService();
	    return service.readTemplate(ticket, user, document);
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
	    ContentServiceServiceLocator locator = new ContentServiceServiceLocator();
	    it.eng.spagobi.services.content.stub.ContentService service = locator
		    .getContentService();
	    return service.publishTemplate(ticket, user, attributes);
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
	    ContentServiceServiceLocator locator = new ContentServiceServiceLocator();
	    it.eng.spagobi.services.content.stub.ContentService service = locator
		    .getContentService();
	    return service.mapCatalogue(ticket, user, operation,path,featureName,mapName);
	} catch (Exception e) {
	    logger.error("Error during service execution",e);

	}finally{
	    logger.debug("IN");
	}
	return null;	
    }
}
