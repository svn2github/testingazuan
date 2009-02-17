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
import it.eng.spagobi.behaviouralmodel.check.bo.Check;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.utilities.ObjectsAccessVerifier;
import it.eng.spagobi.commons.utilities.UserUtilities;
import it.eng.spagobi.engines.config.bo.Engine;
import it.eng.spagobi.services.session.bo.Document;
import it.eng.spagobi.services.session.bo.DocumentParameter;
import it.eng.spagobi.services.security.bo.SpagoBIUserProfile;
import it.eng.spagobi.services.security.service.ISecurityServiceSupplier;
import it.eng.spagobi.services.security.service.SecurityServiceSupplierFactory;
import it.eng.spagobi.services.session.exceptions.AuthenticationException;
import it.eng.spagobi.services.session.exceptions.NonExecutableDocumentException;

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
        Document documents[] = null;
        logger.debug("IN");
        try {
            IEngUserProfile profile = SessionServiceImpl.getUserProfile();
            List list = DAOFactory.getBIObjectDAO().loadBIObjects(type, state, folderPath);
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
                        aDoc.setState(obj.getStateCode());
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

    public String[] getCorrectRolesForExecution(Integer documentId) throws NonExecutableDocumentException {
        String toReturn[] = null;
        logger.debug("IN: documentId = [" + documentId + "]");
        try {
            IEngUserProfile profile = SessionServiceImpl.getUserProfile();
            BIObject obj = DAOFactory.getBIObjectDAO().loadBIObjectById(documentId);
            if (!ObjectsAccessVerifier.canSee(obj, profile)) {
            	logger.error("User [" + ((SpagoBIUserProfile) profile).getUserName() + "] cannot execute document with id = [" + documentId + "]");
            	throw new NonExecutableDocumentException();
            }
            List correctRoles = ObjectsAccessVerifier.getCorrectRolesForExecution(documentId, profile);
            if (correctRoles != null) {
            	toReturn = new String[correctRoles.size()];
                toReturn = (String[]) correctRoles.toArray(toReturn);
            }
        } catch(NonExecutableDocumentException e) {
            throw e;
        } catch(Exception e) {
            logger.error(e);
        }
        logger.debug("OUT");
        return toReturn;
    }

    public DocumentParameter[] getDocumentParameters(Integer documentId, String roleName) throws NonExecutableDocumentException {
        DocumentParameter parameters[] = null;
        logger.debug("IN: documentId = [" + documentId + "]; roleName = [" + roleName + "]");
        try {
            IEngUserProfile profile = SessionServiceImpl.getUserProfile();
            BIObject obj = DAOFactory.getBIObjectDAO().loadBIObjectById(documentId);
            if (!ObjectsAccessVerifier.canSee(obj, profile)) {
            	logger.error("User [" + ((SpagoBIUserProfile) profile).getUserName() + "] cannot execute document with id = [" + documentId + "]");
            	throw new NonExecutableDocumentException();
            }
            List correctRoles = ObjectsAccessVerifier.getCorrectRolesForExecution(documentId, profile);
            if (correctRoles == null || correctRoles.size() == 0) {
            	logger.error("User [" + ((SpagoBIUserProfile) profile).getUserName() + "] has no roles to execute document with id = [" + documentId + "]");
            	throw new NonExecutableDocumentException();
            }
            if (!correctRoles.contains(roleName)) {
            	logger.error("Role [" + roleName + "] is not a valid role for executing document with id = [" + documentId + "] for user [" + ((SpagoBIUserProfile) profile).getUserName() + "]");
            	throw new NonExecutableDocumentException();
            }
        	
            obj = DAOFactory.getBIObjectDAO().loadBIObjectForExecutionByIdAndRole(obj.getId(), roleName);
            List parametersList = obj.getBiObjectParameters();
            List toReturn = new ArrayList();
            if (parametersList != null) {
                DocumentParameter aDocParameter;
                for (Iterator it = parametersList.iterator(); it.hasNext(); toReturn.add(aDocParameter)) {
                    BIObjectParameter parameter = (BIObjectParameter)it.next();
                    aDocParameter = new DocumentParameter();
                    aDocParameter.setId(parameter.getId());
                    aDocParameter.setLabel(parameter.getLabel());
                    aDocParameter.setUrlName(parameter.getParameterUrlName());
                    List checks = parameter.getParameter().getChecks();
                    List newChecks = new ArrayList<it.eng.spagobi.services.session.bo.Check>();
                    if (checks != null && !checks.isEmpty()) {
                    	Iterator checksIt = checks.iterator();
                    	while (checksIt.hasNext()) {
                    		Check aCheck = (Check) checksIt.next();
                    		it.eng.spagobi.services.session.bo.Check newCheck = new it.eng.spagobi.services.session.bo.Check();
                    		newCheck.setId(aCheck.getCheckId());
                    		newCheck.setLabel(aCheck.getLabel());
                    		newCheck.setName(aCheck.getName());
                    		newCheck.setDescription(aCheck.getDescription());
                    		newCheck.setType(aCheck.getValueTypeCd());
                    		newCheck.setFirstValue(aCheck.getFirstValue());
                    		newCheck.setSecondValue(aCheck.getSecondValue());
                    		newChecks.add(newCheck);
                    	}
                    }
                    it.eng.spagobi.services.session.bo.Check[] checksArray = new it.eng.spagobi.services.session.bo.Check[newChecks.size()];
                    checksArray = (it.eng.spagobi.services.session.bo.Check[]) newChecks.toArray(checksArray);
                    aDocParameter.setChecks(checksArray);
                }
            }
            parameters = new DocumentParameter[toReturn.size()];
            parameters = (DocumentParameter[]) toReturn.toArray(parameters);
        } catch(NonExecutableDocumentException e) {
            throw e;
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
