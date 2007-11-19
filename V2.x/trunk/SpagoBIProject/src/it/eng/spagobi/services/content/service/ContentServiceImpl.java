package it.eng.spagobi.services.content.service;

import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.analiticalmodel.document.bo.BIObject;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.utilities.UploadedFile;
import sun.misc.BASE64Encoder;
import it.eng.spagobi.services.content.bo.Content;

public class ContentServiceImpl {

    public Content readTemplate(String token, String user, String document) {
// IMPLEMENTARE I CONTROLLI
	BIObject biobj = null;
	Content content=new Content();
	try {
	    Integer id = new Integer(document);
	    biobj = DAOFactory.getBIObjectDAO().loadBIObjectById(id);
	    biobj.loadTemplate();
	    UploadedFile uploadedFile = biobj.getTemplate();
	    byte[] template = uploadedFile.getFileContent();
	    BASE64Encoder bASE64Encoder = new BASE64Encoder();
	    content.setContent(bASE64Encoder.encode(template));
	    content.setFileName(uploadedFile.getFileName());
	    return content;
	} catch (NumberFormatException e) {
	    e.printStackTrace();
	} catch (EMFUserError e) {
	    e.printStackTrace();
	}
	return null;
    }
}
