package it.eng.spagobi.services.content.service;

import java.util.HashMap;


import it.eng.spago.error.EMFInternalError;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.analiticalmodel.document.bo.BIObject;
import it.eng.spagobi.analiticalmodel.document.bo.ObjTemplate;
import it.eng.spagobi.analiticalmodel.document.dao.IObjTemplateDAO;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.dao.IBinContentDAO;
import it.eng.spagobi.commons.utilities.UploadedFile;
import it.eng.spagobi.services.common.AbstractServiceImpl;
import it.eng.spagobi.services.content.bo.Content;
import it.eng.spagobi.services.security.exceptions.SecurityException;

import org.apache.log4j.Logger;

import sun.misc.BASE64Encoder;

public class ContentServiceImpl extends AbstractServiceImpl{

    static private Logger logger = Logger.getLogger(ContentServiceImpl.class);
    

    public ContentServiceImpl(){
	super();
    }
    
    public Content readTemplate(String token, String user, String document) {
// TODO IMPLEMENTARE I CONTROLLI
	
        logger.debug("IN");
	userId=user;
	if (activeSso){
		try {
		    if (validateTicket(token)){
			return readTemplate(user,document);
		    }else{
			logger.error("Token NOT VALID");
			return null;
		    }
		} catch (SecurityException e) {
		    logger.error("SecurityException",e);
		    return null;
		}finally{
		    logger.debug("OUT");
		}
	}else{
	        logger.debug("OUT");
		// operazione locale
	        return readTemplate(user,document);
	}
    }

    public Content readSubObjectContent(String token,String user,String nameSubObject){
        logger.debug("IN");
	userId=user;
	if (activeSso){
		try {
		    if (validateTicket(token)){
			return readSubObjectContent(user,nameSubObject);
		    }else{
			logger.error("Token NOT VALID");
			return null;
		    }
		} catch (SecurityException e) {
		    logger.error("SecurityException",e);
		    return null;
		}finally{
		    logger.debug("OUT");
		}
	}else{
	        logger.debug("OUT");
		// operazione locale
	        return readSubObjectContent(user,nameSubObject);
	}
    }
    
    public String saveSubObject(String token,String user,String nameSubObject,String publicVisibility,String content,String description){
        logger.debug("IN");
	userId=user;
	if (activeSso){
		try {
		    if (validateTicket(token)){
			return saveSubObject(user,nameSubObject,publicVisibility,content,description);
		    }else{
			logger.error("Token NOT VALID");
			return null;
		    }
		} catch (SecurityException e) {
		    logger.error("SecurityException",e);
		    return null;
		}finally{
		    logger.debug("OUT");
		}
	}else{
	        logger.debug("OUT");
		// operazione locale
	        return saveSubObject(user,nameSubObject,publicVisibility,content,description);
	}
    }
    
    public String saveObjectTemplate(String token,String user,String templateName,String content){
	return null;
    }
    
    public Content downloadAll(String token,String user,String biobjectId,String fileName){
	return null;
    }
    
    // PRIVATE METHOD
    
    private Content readSubObjectContent(String user,String nameSubObject){
	return null;
    }
    
    private String saveSubObject(String user,String nameSubObject,String publicVisibility,String content,String description){
	return null;
    }
    
    private String saveObjectTemplate(String user,String templateName,String content){
	return null;
    }
    
    private Content downloadAll(String user,String biobjectId,String fileName){
	return null;
    }    
    
    


    
    private Content readTemplate( String user, String document) {
	logger.debug("IN");
	BIObject biobj = null;
	Content content=new Content();
	try {
	    Integer id = new Integer(document);
	    biobj = DAOFactory.getBIObjectDAO().loadBIObjectById(id);
	    
	    IObjTemplateDAO tempdao = DAOFactory.getObjTemplateDAO();
	    IBinContentDAO contdao = DAOFactory.getBinContentDAO();
	    ObjTemplate temp = tempdao.getBIObjectActiveTemplate(biobj.getId());
	    byte[] template = contdao.getBinContent(temp.getBinId());
	
	    BASE64Encoder bASE64Encoder = new BASE64Encoder();
	    content.setContent(bASE64Encoder.encode(template));
	    content.setFileName(temp.getName());
	    return content;
	} catch (NumberFormatException e) {
	    logger.error("NumberFormatException",e);
	} catch (EMFUserError e) {
	    logger.error("EMFUserError",e);
	} catch (EMFInternalError e) {
		 logger.error("EMFUserError",e);
	}
	logger.debug("OUT");
	return null;	
    } 
}
