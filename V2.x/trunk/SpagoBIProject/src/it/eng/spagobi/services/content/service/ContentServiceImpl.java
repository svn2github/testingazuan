package it.eng.spagobi.services.content.service;


import it.eng.spago.error.EMFInternalError;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.analiticalmodel.document.bo.BIObject;
import it.eng.spagobi.analiticalmodel.document.bo.ObjTemplate;
import it.eng.spagobi.analiticalmodel.document.bo.SubObject;
import it.eng.spagobi.analiticalmodel.document.dao.IBIObjectDAO;
import it.eng.spagobi.analiticalmodel.document.dao.IObjTemplateDAO;
import it.eng.spagobi.analiticalmodel.document.dao.ISubObjectDAO;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.dao.IBinContentDAO;
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
        try {
            validateTicket(token,user);
            return readTemplate(user,document);
	} catch (SecurityException e) {
	    logger.error("SecurityException",e);
	    return null;
	}finally{
	    logger.debug("OUT");
	}        
    }

    public Content readSubObjectContent(String token,String user,String subObjectId){
        logger.debug("IN");
        try {
            validateTicket(token,user);
            return readSubObjectContent(user,subObjectId);
	} catch (SecurityException e) {
	    logger.error("SecurityException",e);
	    return null;
	}finally{
	    logger.debug("OUT");
	} 

    }
    
    public String saveSubObject(String token,String user,String documentiId,String analysisName,String analysisDescription,String visibilityBoolean,String content){
        logger.debug("IN");
        try {
            validateTicket(token,user);
            return saveSubObject(user,documentiId,analysisName,analysisDescription,visibilityBoolean,content);
	} catch (SecurityException e) {
	    logger.error("SecurityException",e);
	    return null;
	}finally{
	    logger.debug("OUT");
	}	

    }
    
    public String saveObjectTemplate(String token,String user,String documentiId,String templateName,String content){
        logger.debug("IN");
        try {
            validateTicket(token,user);
            return saveObjectTemplate(user,documentiId,templateName,content);
	} catch (SecurityException e) {
	    logger.error("SecurityException",e);
	    return null;
	}finally{
	    logger.debug("OUT");
	}

    }
    
    public Content downloadAll(String token,String user,String biobjectId,String fileName){
	return null;
    }
    
    // PRIVATE METHOD
    
    private Content readSubObjectContent(String user,String subObjectId){
    	logger.debug("IN");
    	Content content=new Content();
    	try {
    	    Integer id = new Integer(subObjectId);
    	    ISubObjectDAO subdao = DAOFactory.getSubObjectDAO();
    	    SubObject subobj = subdao.getSubObject(id);
    	    byte[] cont = subobj.getContent();
    	    BASE64Encoder bASE64Encoder = new BASE64Encoder();
    	    content.setContent(bASE64Encoder.encode(cont));
    	    content.setFileName(subobj.getName());
    	    return content;
    	} catch (NumberFormatException e) {
    	    logger.error("NumberFormatException",e);
    	} catch (EMFUserError e) {
    	    logger.error("EMFUserError",e);
    	} 
    	logger.debug("OUT");
    	return null;	
    }
    
    
    private String saveSubObject(String user,String documentiId,String analysisName,String analysisDescription,String visibilityBoolean,String content){
	logger.debug("IN");
	try {
	    IBIObjectDAO objdao = DAOFactory.getBIObjectDAO();
	    ISubObjectDAO subdao = DAOFactory.getSubObjectDAO();
	    Integer docId = new Integer(documentiId);
	    BIObject biobj = objdao.loadBIObjectById(docId);
	    SubObject  objSub = new SubObject();
	    objSub.setDescription(analysisDescription);
	    if (visibilityBoolean!=null && visibilityBoolean.equals("true")){
		objSub.setIsPublic(new Boolean(true));
	    }else{
		objSub.setIsPublic(new Boolean(false));
	    }
	    objSub.setOwner(user);
	    objSub.setName(analysisName);
	    objSub.setContent(content.getBytes());
	    subdao.saveSubObject(docId, objSub);
	} catch (NumberFormatException e) {
	    logger.error("NumberFormatException",e);
	    return "KO";
	} catch (EMFUserError e) {
	    logger.error("EMFUserError",e);
	    return "KO";
	}finally{
	    logger.debug("OUT");
	}
	return "OK";
    }
    
    private String saveObjectTemplate(String user,String documentiId,String templateName,String content){
	logger.debug("IN");
	try {
	    IBIObjectDAO objdao = DAOFactory.getBIObjectDAO();
	    Integer docId = new Integer(documentiId);
	    BIObject biobj = objdao.loadBIObjectById(docId);
	    ObjTemplate objTemp = new ObjTemplate();
	    objTemp.setActive(new Boolean(true));
	    objTemp.setContent(content.getBytes());
	    objTemp.setName(templateName);
	    objdao.modifyBIObject(biobj, objTemp);
	} catch (NumberFormatException e) {
	    logger.error("NumberFormatException",e);
	    return "KO";
	} catch (EMFUserError e) {
	    logger.error("EMFUserError",e);
	    return "KO";
	}finally{
	    logger.debug("OUT");
	}
	return "OK";
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
