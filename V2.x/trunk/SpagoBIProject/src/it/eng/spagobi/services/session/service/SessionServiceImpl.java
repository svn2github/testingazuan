/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2008 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.services.session.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.analiticalmodel.document.bo.BIObject;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.bo.BIObjectParameter;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.utilities.ObjectsAccessVerifier;
import it.eng.spagobi.commons.utilities.UserUtilities;
import it.eng.spagobi.engines.config.bo.Engine;
import it.eng.spagobi.services.session.bo.Document;
import it.eng.spagobi.services.session.bo.DocumentParameter;
import it.eng.spagobi.services.security.service.ISecurityServiceSupplier;
import it.eng.spagobi.services.security.service.SecurityServiceSupplierFactory;
import it.eng.spagobi.services.session.exceptions.AuthenticationException;

import org.apache.axis.MessageContext;
import org.apache.axis.session.Session;
import org.apache.log4j.Logger;

public class SessionServiceImpl {
	
	static private Logger logger = Logger.getLogger(SessionServiceImpl.class);

	public void openSession(String userName, String password) throws AuthenticationException {
		logger.debug("IN");
    	ISecurityServiceSupplier supplier = SecurityServiceSupplierFactory.createISecurityServiceSupplier();
    	Object ris= supplier.checkAuthentication(userName, password);
    	if (ris == null) {
    		logger.error("Authentication failed for username [" + userName + "]!");
    		throw new AuthenticationException();
    	}
    	logger.debug("Authentication successful for username [" + userName + "]!");
    	//SpagoBIUserProfile user = supplier.createUserProfile(userName);
    	IEngUserProfile profile=null;
		try {
			profile = UserUtilities.getUserProfile(userName);
		} catch (Exception e) {
			logger.error("Exception for username [" + userName + "]!",e);
		}
    	logger.debug("User profile for username [" + userName + "] created.");
		MessageContext mc = MessageContext.getCurrentContext();
        Session wsSession =  mc.getSession();
        wsSession.set(IEngUserProfile.ENG_USER_PROFILE, profile);
        logger.debug("User profile for username [" + userName + "] put on session.");
        logger.debug("OUT");
	}
	
	public void openSessionWithToken(String userName, String token) throws AuthenticationException {
		logger.debug("IN");
        logger.debug("OUT");
	}
	
    public boolean isValidSession() {
        logger.debug("IN");
        boolean toReturn = false;
        IEngUserProfile profile = getUserProfile();
        toReturn = profile != null;
        logger.debug((new StringBuilder("OUT: isValidSession = ")).append(toReturn).toString());
        return toReturn;
    }
	
	public void closeSession() {
		logger.debug("IN");
		MessageContext mc = MessageContext.getCurrentContext();
        Session wsSession =  mc.getSession();
        wsSession.remove(IEngUserProfile.ENG_USER_PROFILE);
        wsSession.invalidate();
		logger.debug("OUT");
	}
	
	public static IEngUserProfile getUserProfile() {
		logger.debug("IN");
		MessageContext mc = MessageContext.getCurrentContext();
        Session wsSession =  mc.getSession();
        IEngUserProfile profile = (IEngUserProfile) wsSession.get(IEngUserProfile.ENG_USER_PROFILE);
        logger.debug("OUT");
        return profile;
	}
	
    public Document[] getDocuments(String type, String state, String folderPath) {
        Document documents[];
        logger.debug("IN");
        documents = (Document[])null;
        try {
            IEngUserProfile profile = SessionServiceImpl.getUserProfile();
            List list = DAOFactory.getBIObjectDAO().loadAllBIObjects();
            List toReturn = new ArrayList();
            if(list != null) {
                for(Iterator it = list.iterator(); it.hasNext();) {
                    BIObject obj = (BIObject)it.next();
                    if(ObjectsAccessVerifier.canSee(obj, profile))
                    {
                        Document aDoc = new Document();
                        aDoc.setId(obj.getId());
                        aDoc.setLabel(obj.getLabel());
                        aDoc.setName(obj.getName());
                        aDoc.setDescription(obj.getDescription());
                        aDoc.setType(obj.getBiObjectTypeCode());
                        Engine engine = obj.getEngine();
                        aDoc.setEngineId(engine.getId());
                        aDoc.setEngineLabel(engine.getLabel());
                        aDoc.setEngineName(engine.getName());
                        toReturn.add(aDoc);
                    }
                }

            }
            documents = new Document[toReturn.size()];
            documents = (Document[])toReturn.toArray(documents);
        } catch(Exception e) {
            logger.error(e);
        }
        logger.debug("OUT");
        return documents;
    }

    public String[] getCorrectRolesForExecution(Integer documentId) {
        String toReturn[];
        logger.debug("IN");
        toReturn = (String[])null;
        try {
            it.eng.spago.security.IEngUserProfile profile = SessionServiceImpl.getUserProfile();
            List correctRoles = ObjectsAccessVerifier.getCorrectRolesForExecution(documentId, profile);
            if (correctRoles != null)
            {
                toReturn = (String[])correctRoles.toArray();
            }
        } catch(Exception e) {
            logger.error(e);
        }
        logger.debug("OUT");
        return toReturn;
    }

    public it.eng.spagobi.services.session.bo.DocumentParameter[] getDocumentParameters(Integer documentId, String roleName) {
        it.eng.spagobi.services.session.bo.DocumentParameter parameters[];
        logger.debug("IN");
        parameters = (DocumentParameter[])null;
        try {
            List parametersList = DAOFactory.getBIObjectParameterDAO().loadBIObjectParametersById(documentId);
            List toReturn = new ArrayList();
            if(parametersList != null)
            {
                DocumentParameter aDocParameter;
                for(Iterator it = parametersList.iterator(); it.hasNext(); toReturn.add(aDocParameter))
                {
                    BIObjectParameter parameter = (BIObjectParameter)it.next();
                    aDocParameter = new DocumentParameter();
                    aDocParameter.setId(parameter.getId());
                    aDocParameter.setLabel(parameter.getLabel());
                    aDocParameter.setUrlName(parameter.getParameterUrlName());
                }

            }
            parameters = (DocumentParameter[])toReturn.toArray();
        } catch(Exception e) {
            logger.error(e);
        }
        logger.debug("OUT");
        return parameters;
    }

    public HashMap getAdmissibleValues(Integer documentParameterId, String roleName) {
        return null;
    }
	
}
