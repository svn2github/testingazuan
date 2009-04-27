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
package it.eng.spagobi.sdk.documents.impl;


import it.eng.spago.base.SourceBean;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.analiticalmodel.document.bo.BIObject;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.bo.BIObjectParameter;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.bo.Parameter;
import it.eng.spagobi.behaviouralmodel.check.bo.Check;
import it.eng.spagobi.behaviouralmodel.lov.bo.ILovDetail;
import it.eng.spagobi.behaviouralmodel.lov.bo.LovDetailFactory;
import it.eng.spagobi.behaviouralmodel.lov.bo.LovResultHandler;
import it.eng.spagobi.behaviouralmodel.lov.bo.ModalitiesValue;
import it.eng.spagobi.commons.bo.UserProfile;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.utilities.ObjectsAccessVerifier;
import it.eng.spagobi.engines.config.bo.Engine;
import it.eng.spagobi.sdk.AbstractSDKService;
import it.eng.spagobi.sdk.documents.DocumentsService;
import it.eng.spagobi.sdk.documents.bo.Constraint;
import it.eng.spagobi.sdk.documents.bo.Document;
import it.eng.spagobi.sdk.documents.bo.DocumentParameter;
import it.eng.spagobi.sdk.documents.bo.Functionality;
import it.eng.spagobi.sdk.documents.bo.Template;
import it.eng.spagobi.sdk.exceptions.NonExecutableDocumentException;
import it.eng.spagobi.sdk.exceptions.NotAllowedOperationException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;


public class DocumentsServiceImpl extends AbstractSDKService implements DocumentsService {
    
    static private Logger logger = Logger.getLogger(DocumentsServiceImpl.class);

	public Template downloadTemplate(Integer documentId)
			throws NotAllowedOperationException {
		// TODO Auto-generated method stub
		return null;
	}

    public HashMap getAdmissibleValues(Integer documentParameterId, String roleName) throws NonExecutableDocumentException {
    	HashMap values = new HashMap<String, String>();
        logger.debug("IN: documentParameterId = [" + documentParameterId + "]; roleName = [" + roleName + "]");
        try {
            IEngUserProfile profile = getUserProfile();
            BIObjectParameter documentParameter = DAOFactory.getBIObjectParameterDAO().loadForDetailByObjParId(documentParameterId);
            BIObject obj = DAOFactory.getBIObjectDAO().loadBIObjectById(documentParameter.getBiObjectID());
            if (!ObjectsAccessVerifier.canSee(obj, profile)) {
            	logger.error("User [" + ((UserProfile) profile).getUserName() + "] cannot execute document with id = [" + obj.getId() + "]");
            	throw new NonExecutableDocumentException();
            }
            List correctRoles = ObjectsAccessVerifier.getCorrectRolesForExecution(obj.getId(), profile);
            if (correctRoles == null || correctRoles.size() == 0) {
            	logger.error("User [" + ((UserProfile) profile).getUserName() + "] has no roles to execute document with id = [" + obj.getId() + "]");
            	throw new NonExecutableDocumentException();
            }
            if (!correctRoles.contains(roleName)) {
            	logger.error("Role [" + roleName + "] is not a valid role for executing document with id = [" + obj.getId() + "] for user [" + ((UserProfile) profile).getUserName() + "]");
            	throw new NonExecutableDocumentException();
            }
            
            // reload BIObjectParameter in execution modality
            BIObjectParameter biParameter = null;
            obj = DAOFactory.getBIObjectDAO().loadBIObjectForExecutionByIdAndRole(obj.getId(), roleName);
            List biparameters = obj.getBiObjectParameters();
            Iterator biparametersIt = biparameters.iterator();
            while (biparametersIt.hasNext()) {
            	BIObjectParameter aDocParameter = (BIObjectParameter) biparametersIt.next();
            	if (aDocParameter.getId().equals(documentParameterId)) {
            		biParameter = aDocParameter;
            		break;
            	}
            }
            
			Parameter par = biParameter.getParameter();
			ModalitiesValue paruse = par.getModalityValue();
			if (paruse.getITypeCd().equals("MAN_IN")) {
				logger.debug("Document parameter is manual input. An empty HashMap will be returned.");
			} else {
	        	String lovResult = biParameter.getLovResult();
	        	if (lovResult == null) {
	        		String lovprov = paruse.getLovProvider();
	            	ILovDetail lovDetail = LovDetailFactory.getLovFromXML(lovprov);
	    			lovResult = lovDetail.getLovResult(profile);
	    			LovResultHandler lovResultHandler = new LovResultHandler(lovResult);
	    			biParameter.setLovResult(lovResult);
	    			List rows = lovResultHandler.getRows();
	    			Iterator it = rows.iterator();
	    			while (it.hasNext()) {
	    				SourceBean row = (SourceBean) it.next();
	    				String value = (String) row.getAttribute(lovDetail.getValueColumnName());
	    				String description = (String) row.getAttribute(lovDetail.getDescriptionColumnName());
	    				values.put(value, description);
	    			}
	        	}
			}
        } catch(NonExecutableDocumentException e) {
            throw e;
        } catch(Exception e) {
            logger.error(e);
        }
        logger.debug("OUT");
        return values;
    }
	
    public String[] getCorrectRolesForExecution(Integer documentId) throws NonExecutableDocumentException {
        String[] toReturn = null;
        logger.debug("IN: documentId = [" + documentId + "]");
        try {
            IEngUserProfile profile = getUserProfile();
            BIObject obj = DAOFactory.getBIObjectDAO().loadBIObjectById(documentId);
            if (!ObjectsAccessVerifier.canSee(obj, profile)) {
            	logger.error("User [" + ((UserProfile) profile).getUserName() + "] cannot execute document with id = [" + documentId + "]");
            	throw new NonExecutableDocumentException();
            }
            List correctRoles = ObjectsAccessVerifier.getCorrectRolesForExecution(documentId, profile);
            if (correctRoles != null) {
            	toReturn = new String[correctRoles.size()];
                toReturn = (String[]) correctRoles.toArray(toReturn);
            } else {
            	toReturn = new String[0];
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
            IEngUserProfile profile = getUserProfile();
            BIObject obj = DAOFactory.getBIObjectDAO().loadBIObjectById(documentId);
            if (!ObjectsAccessVerifier.canSee(obj, profile)) {
            	logger.error("User [" + ((UserProfile) profile).getUserName() + "] cannot execute document with id = [" + documentId + "]");
            	throw new NonExecutableDocumentException();
            }
            List correctRoles = ObjectsAccessVerifier.getCorrectRolesForExecution(documentId, profile);
            if (correctRoles == null || correctRoles.size() == 0) {
            	logger.error("User [" + ((UserProfile) profile).getUserName() + "] has no roles to execute document with id = [" + documentId + "]");
            	throw new NonExecutableDocumentException();
            }
            if (!correctRoles.contains(roleName)) {
            	logger.error("Role [" + roleName + "] is not a valid role for executing document with id = [" + documentId + "] for user [" + ((UserProfile) profile).getUserName() + "]");
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
                    List newConstraints = new ArrayList<Constraint>();
                    if (checks != null && !checks.isEmpty()) {
                    	Iterator checksIt = checks.iterator();
                    	while (checksIt.hasNext()) {
                    		Check aCheck = (Check) checksIt.next();
                    		Constraint constraint = new Constraint();
                    		constraint.setId(aCheck.getCheckId());
                    		constraint.setLabel(aCheck.getLabel());
                    		constraint.setName(aCheck.getName());
                    		constraint.setDescription(aCheck.getDescription());
                    		constraint.setType(aCheck.getValueTypeCd());
                    		constraint.setFirstValue(aCheck.getFirstValue());
                    		constraint.setSecondValue(aCheck.getSecondValue());
                    		newConstraints.add(constraint);
                    	}
                    }
                    it.eng.spagobi.sdk.documents.bo.Constraint[] constraintsArray = new it.eng.spagobi.sdk.documents.bo.Constraint[newConstraints.size()];
                    constraintsArray = (it.eng.spagobi.sdk.documents.bo.Constraint[]) newConstraints.toArray(constraintsArray);
                    aDocParameter.setConstraints(constraintsArray);
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

    public Document[] getDocumentsAsList(String type, String state, String folderPath) {
        Document documents[] = null;
        logger.debug("IN");
        try {
            IEngUserProfile profile = getUserProfile();
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

	public Functionality[] getDocumentsAsTree(String initialPath) {
		// TODO Auto-generated method stub
		return null;
	}

	public Integer saveNewDocument(Document document, Template template,
			Integer functionalityId) throws NotAllowedOperationException {
		// TODO Auto-generated method stub
		return null;
	}

	public void uploadTemplate(Integer documentId, Template template)
			throws NotAllowedOperationException {
		// TODO Auto-generated method stub
	}

}
