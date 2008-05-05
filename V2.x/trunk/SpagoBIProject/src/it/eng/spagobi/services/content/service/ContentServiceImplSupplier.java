/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2008 Engineering Ingegneria Informatica S.p.A.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

**/
package it.eng.spagobi.services.content.service;

import it.eng.spago.error.EMFInternalError;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.analiticalmodel.document.bo.BIObject;
import it.eng.spagobi.analiticalmodel.document.bo.ObjTemplate;
import it.eng.spagobi.analiticalmodel.document.dao.IObjTemplateDAO;
import it.eng.spagobi.commons.bo.UserProfile;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.utilities.ObjectsAccessVerifier;
import it.eng.spagobi.services.content.bo.Content;
import it.eng.spagobi.services.security.bo.SpagoBIUserProfile;
import it.eng.spagobi.services.security.exceptions.SecurityException;
import it.eng.spagobi.services.security.service.ISecurityServiceSupplier;
import it.eng.spagobi.services.security.service.SecurityServiceSupplierFactory;

import org.apache.log4j.Logger;

import sun.misc.BASE64Encoder;

public class ContentServiceImplSupplier {
    static private Logger logger = Logger.getLogger(ContentServiceImplSupplier.class);

    /**
     * Read template.
     * 
     * @param user the user
     * @param document the document
     * 
     * @return the content
     * 
     * @throws SecurityException the security exception
     * @throws EMFUserError the EMF user error
     * @throws EMFInternalError the EMF internal error
     */
    public Content readTemplate(String user, String document) throws SecurityException, EMFUserError, EMFInternalError {
	logger.debug("IN");
	logger.debug("user="+user);
	logger.debug("document="+document);
	BIObject biobj = null;
	Content content = new Content();
	try {
	    Integer id = new Integer(document);

	    biobj = DAOFactory.getBIObjectDAO().loadBIObjectById(id);

	    // Check if the user can execute the document
	    IEngUserProfile profile = null;
	    ISecurityServiceSupplier supplier = SecurityServiceSupplierFactory.createISecurityServiceSupplier();
	    try {
      		SpagoBIUserProfile userProfile = supplier.createUserProfile(user);
      		profile = new UserProfile(userProfile);
	    } catch (Exception e) {
    		logger.error("Reading user information... ERROR", e);
    		throw new SecurityException();
	    }

	    ObjectsAccessVerifier.canSee(biobj, profile);

	    IObjTemplateDAO tempdao = DAOFactory.getObjTemplateDAO();
	    ObjTemplate temp = tempdao.getBIObjectActiveTemplate(biobj.getId());
	    if (temp==null){
	       logger.warn("The template is NULL...");
	       throw new SecurityException();
      } 
      byte[] template = temp.getContent();

	    BASE64Encoder bASE64Encoder = new BASE64Encoder();
	    content.setContent(bASE64Encoder.encode(template));
	    logger.debug("template read");
	    content.setFileName(temp.getName());
	    return content;
	} catch (NumberFormatException e) {
	    logger.error("NumberFormatException", e);
	    throw e;
	} catch (EMFUserError e) {
	    logger.error("EMFUserError", e);
	    throw e;
	} catch (EMFInternalError e) {
	    logger.error("EMFUserError", e);
	    throw e;
	} finally {
	    logger.debug("OUT");
	}
    }

}
