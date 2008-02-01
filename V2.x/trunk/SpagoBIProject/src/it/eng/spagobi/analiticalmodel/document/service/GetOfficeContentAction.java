package it.eng.spagobi.analiticalmodel.document.service;

import java.io.InputStream;
import java.security.Principal;
import java.util.Properties;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.dispatching.action.AbstractHttpAction;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.analiticalmodel.document.bo.Snapshot;
import it.eng.spagobi.analiticalmodel.document.dao.ISnapshotDAO;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.dao.IBinContentDAO;
import it.eng.spagobi.services.common.IProxyService;
import it.eng.spagobi.services.common.IProxyServiceFactory;
import it.eng.spagobi.services.content.bo.Content;
import it.eng.spagobi.services.content.service.ContentServiceImplSupplier;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import sun.misc.BASE64Decoder;

public class GetOfficeContentAction extends AbstractHttpAction {

    private static transient Logger logger=Logger.getLogger(GetOfficeContentAction.class);
    
	public void service(SourceBean request, SourceBean responseSb) throws Exception {
	    logger.debug("IN");
	    freezeHttpResponse();
	    
		HttpServletResponse response = getHttpResponse();
		HttpServletRequest req = getHttpRequest();
		String userId=null;
		ConfigSingleton config = ConfigSingleton.getInstance();
		SourceBean validateSB =(SourceBean) config.getAttribute("SPAGOBI_SSO.ACTIVE");
		String active = (String) validateSB.getCharacters();
		if (active != null && active.equals("true")){
			IProxyService proxy=IProxyServiceFactory.createProxyService();
			userId=proxy.readUserId(req.getSession());
			logger.debug("got userId from IProxyService="+userId);
		}else {
		    	userId = (String)request.getAttribute("userId");
		    	logger.debug("got userId from Request="+userId);
		}

		String documentId = (String)request.getAttribute("documentId");
		logger.debug("got parameters userId="+userId+" and documentId="+documentId.toString());
		
		ContentServiceImplSupplier c = new ContentServiceImplSupplier();
		Content template = c.readTemplate(userId, documentId);
		String templateFileName = template.getFileName();

		logger.debug("Template Read");

		if(templateFileName==null){
			logger.warn("Template has no name");
			templateFileName="";
		}
		
		response.setHeader("Cache-Control: ",""); // leave blank to avoid IE errors
		response.setHeader("Pragma: ",""); // leave blank to avoid IE errors 
		response.setHeader("content-disposition","attachment; filename="+templateFileName);	
		
		String fileExtension = null;
		int extindex = templateFileName.lastIndexOf(".");
		if (extindex != -1) fileExtension = templateFileName.substring(extindex + 1);
		if (fileExtension != null) {
		 		InputStream is;
		 		Thread t= Thread.currentThread();
		 		ClassLoader cl=t.getContextClassLoader();
		 		is=cl.getResourceAsStream("MIMEtypes-extensions.properties");
		 		Properties props = new Properties();
		 		props.load(is);
		 		String mimetype = props.getProperty(fileExtension);
		 		if (mimetype != null && !mimetype.trim().equals("")) {
		 			response.setContentType(mimetype.trim());
		 			logger.debug("Content type: "+mimetype.trim());
		 			}
		 	}
		else
		{
			logger.warn("no file extension");	
		}

		BASE64Decoder bASE64Decoder = new BASE64Decoder();
		byte[] templateContent = bASE64Decoder.decodeBuffer(template.getContent());
		response.setContentLength(templateContent.length);
		response.getOutputStream().write(templateContent);
		response.getOutputStream().flush();		
		logger.debug("OUT");
	}

}
