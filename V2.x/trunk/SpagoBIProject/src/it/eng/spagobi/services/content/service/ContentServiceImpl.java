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

    
    // PRIVATE METHOD

    
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
