package it.eng.spagobi.services.content.service;

import it.eng.spago.error.EMFInternalError;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.analiticalmodel.document.bo.BIObject;
import it.eng.spagobi.analiticalmodel.document.bo.ObjTemplate;
import it.eng.spagobi.analiticalmodel.document.dao.IObjTemplateDAO;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.services.content.bo.Content;

import org.apache.log4j.Logger;

import sun.misc.BASE64Encoder;

public class ContentServiceImplSupplier {
	static private Logger logger = Logger.getLogger(ContentServiceImplSupplier.class);

	public Content readTemplate( String user, String document) {
		// TODO IMPLEMENTARE I CONTROLLI
		logger.debug("IN");
		BIObject biobj = null;
		Content content=new Content();
		try {
			Integer id = new Integer(document);
			biobj = DAOFactory.getBIObjectDAO().loadBIObjectById(id);

			IObjTemplateDAO tempdao = DAOFactory.getObjTemplateDAO();
			ObjTemplate temp = tempdao.getBIObjectActiveTemplate(biobj.getId());
			byte[] template = temp.getContent();
			
			BASE64Encoder bASE64Encoder = new BASE64Encoder();
			content.setContent(bASE64Encoder.encode(template));
			logger.debug("template read");
			content.setFileName(temp.getName());
			logger.debug("OUT");
			return content;
		} catch (NumberFormatException e) {
			logger.error("NumberFormatException",e);
		} catch (EMFUserError e) {
			logger.error("EMFUserError",e);
		} catch (EMFInternalError e) {
			logger.error("EMFUserError",e);
		}
		logger.error("OUT with errors");
		return null;	
	} 



}
