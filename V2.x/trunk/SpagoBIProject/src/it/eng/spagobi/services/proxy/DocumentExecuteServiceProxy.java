/**
 * 
 */
package it.eng.spagobi.services.proxy;

import it.eng.spagobi.services.datasource.stub.DataSourceServiceServiceLocator;
import it.eng.spagobi.services.execute.stub.DocumentExecuteServiceServiceLocator;
import it.eng.spagobi.services.security.exceptions.SecurityException;

import java.util.HashMap;

import javax.servlet.http.HttpSession;
import javax.xml.rpc.ServiceException;

import org.apache.log4j.Logger;

/**
 * @author Angelo Bernabei
 *         angelo.bernabei@eng.it
 */
public class DocumentExecuteServiceProxy extends AbstractServiceProxy{

    
    static private Logger logger = Logger.getLogger(DocumentExecuteServiceProxy.class);
    
    /**
     * The Constructor.
     * 
     * @param user userId
     * @param session Http Session
     */
    public DocumentExecuteServiceProxy(String user,HttpSession session) {
	super(user, session);
    }
    
    private DocumentExecuteServiceProxy() {
	super();
    } 
    
    /**
     * LookUp Method
     * @return
     * @throws SecurityException
     */
    private it.eng.spagobi.services.execute.stub.DocumentExecuteService lookUp() throws SecurityException {
	try {
	    DocumentExecuteServiceServiceLocator locator = new DocumentExecuteServiceServiceLocator();
	    it.eng.spagobi.services.execute.stub.DocumentExecuteService service=null;
	    if (serviceUrl!=null ){
		    service = locator.getDocumentExecuteService(serviceUrl);		
	    }else {
		    service = locator.getDocumentExecuteService();		
	    }
	    return service;
	} catch (ServiceException e) {
	    logger.error("Error during service execution", e);
	    throw new SecurityException();
	}
    }
    
    /**
     * Return the  image of a Chart
     * @param documentLabel
     * @param parameters
     * @return
     */
    public byte[] executeChart(String documentLabel,HashMap parameters){
	logger.debug("IN.documentLabel="+documentLabel);
	if (documentLabel==null || documentLabel.length()==0){
	    logger.error("documentLabel is NULL");
	    return null;
	}	
	try {
	    return lookUp().executeChart(readTicket(), userId,documentLabel,parameters);
	} catch (Exception e) {
	    logger.error("Error during Service LookUp",e);
	}finally{
	    logger.debug("OUT");
	}
	return null;	
    }
}
