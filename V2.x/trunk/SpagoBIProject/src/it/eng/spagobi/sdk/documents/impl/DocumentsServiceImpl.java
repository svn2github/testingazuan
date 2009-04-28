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
import it.eng.spagobi.analiticalmodel.functionalitytree.bo.LowFunctionality;
import it.eng.spagobi.analiticalmodel.functionalitytree.dao.ILowFunctionalityDAO;
import it.eng.spagobi.analiticalmodel.functionalitytree.dao.LowFunctionalityDAOHibImpl;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.bo.BIObjectParameter;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.bo.Parameter;
import it.eng.spagobi.behaviouralmodel.lov.bo.ILovDetail;
import it.eng.spagobi.behaviouralmodel.lov.bo.LovDetailFactory;
import it.eng.spagobi.behaviouralmodel.lov.bo.LovResultHandler;
import it.eng.spagobi.behaviouralmodel.lov.bo.ModalitiesValue;
import it.eng.spagobi.commons.bo.UserProfile;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.utilities.ObjectsAccessVerifier;
import it.eng.spagobi.sdk.AbstractSDKService;
import it.eng.spagobi.sdk.documents.DocumentsService;
import it.eng.spagobi.sdk.documents.bo.SDKDocument;
import it.eng.spagobi.sdk.documents.bo.SDKDocumentParameter;
import it.eng.spagobi.sdk.documents.bo.SDKFunctionality;
import it.eng.spagobi.sdk.documents.bo.SDKTemplate;
import it.eng.spagobi.sdk.exceptions.NonExecutableDocumentException;
import it.eng.spagobi.sdk.exceptions.NotAllowedOperationException;
import it.eng.spagobi.sdk.utilities.SDKObjectsConverter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;


public class DocumentsServiceImpl extends AbstractSDKService implements DocumentsService {
    
    static private Logger logger = Logger.getLogger(DocumentsServiceImpl.class);

	public SDKTemplate downloadTemplate(Integer documentId)
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

    public SDKDocumentParameter[] getDocumentParameters(Integer documentId, String roleName) throws NonExecutableDocumentException {
        SDKDocumentParameter parameters[] = null;
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
                SDKDocumentParameter aDocParameter;
                for (Iterator it = parametersList.iterator(); it.hasNext(); toReturn.add(aDocParameter)) {
                	BIObjectParameter parameter = (BIObjectParameter)it.next();
                	aDocParameter = new SDKObjectsConverter().fromBIObjectParameterToSDKDocumentParameter(parameter);
                	toReturn.add(aDocParameter);
                }
            }
            parameters = new SDKDocumentParameter[toReturn.size()];
            parameters = (SDKDocumentParameter[]) toReturn.toArray(parameters);
        } catch(NonExecutableDocumentException e) {
            throw e;
        } catch(Exception e) {
            logger.error(e);
        }
        logger.debug("OUT");
        return parameters;
    }

    public SDKDocument[] getDocumentsAsList(String type, String state, String folderPath) {
        SDKDocument documents[] = null;
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
                    	SDKDocument aDoc = new SDKObjectsConverter().fromBIObjectToSDKDocument(obj);
                        toReturn.add(aDoc);
                    }
                }
            }
            documents = new SDKDocument[toReturn.size()];
            documents = (SDKDocument[])toReturn.toArray(documents);
        } catch(Exception e) {
            logger.error(e);
        }
        logger.debug("OUT");
        return documents;
    }

	public SDKFunctionality getDocumentsAsTree(String initialPath) {
		logger.debug("IN: initialPath = [" + initialPath + "]");
		SDKFunctionality toReturn = null;
		try {
			IEngUserProfile profile = getUserProfile();
			ILowFunctionalityDAO functionalityDAO = DAOFactory.getLowFunctionalityDAO();
			boolean canSeeFunctionality;
			LowFunctionality initialFunctionality = null;
			if (initialPath == null || initialPath.trim().equals("")) {
				// loading root functionality, everybody can see it
				initialFunctionality = functionalityDAO.loadRootLowFunctionality(false);
				canSeeFunctionality = true;
			} else {
				initialFunctionality = functionalityDAO.loadLowFunctionalityByPath(initialPath, false);
				// if user is administrator, he can see all functionalities
				if (profile.isAbleToExecuteAction(SpagoBIConstants.DOCUMENT_MANAGEMENT_ADMIN)) {
					canSeeFunctionality = true;
				} else {
					// if user can exec or dev or test on functionality, he can see it, otherwise he cannot see it
					if (ObjectsAccessVerifier.canExec(initialFunctionality.getId(), profile) ||
							ObjectsAccessVerifier.canTest(initialFunctionality.getId(), profile) ||
							ObjectsAccessVerifier.canDev(initialFunctionality.getId(), profile)) {
						canSeeFunctionality = true;
					} else {
						canSeeFunctionality = false;
					}
				}
			}
			
			if (canSeeFunctionality) {
				toReturn = new SDKObjectsConverter().fromLowFunctionalityToSDKFunctionality(initialFunctionality);
				setFunctionalityContent(toReturn);
			}
				
        } catch(Exception e) {
            logger.error(e);
        }
        logger.debug("OUT");
        return toReturn;
	}
	
	private void setFunctionalityContent(SDKFunctionality parentFunctionality) throws Exception {
		logger.debug("IN");
		IEngUserProfile profile = getUserProfile();
		// loading contained documents
		List containedBIObjects = DAOFactory.getBIObjectDAO().loadBIObjects(parentFunctionality.getId(), profile);
		List visibleDocumentsList = new ArrayList();
		if (containedBIObjects != null && containedBIObjects.size() > 0) {
            for (Iterator it = containedBIObjects.iterator(); it.hasNext();) {
                BIObject obj = (BIObject) it.next();
                if (ObjectsAccessVerifier.checkProfileVisibility(obj, profile)) {
                	SDKDocument aDoc = new SDKObjectsConverter().fromBIObjectToSDKDocument(obj);
                	visibleDocumentsList.add(aDoc);
                }
            }
		}
		SDKDocument[] containedDocuments = new SDKDocument[visibleDocumentsList.size()];
		containedDocuments = (SDKDocument[]) visibleDocumentsList.toArray(containedDocuments);
		parentFunctionality.setContainedDocuments(containedDocuments);
		
		List containedFunctionalitiesList = DAOFactory.getLowFunctionalityDAO().loadChildFunctionalities(parentFunctionality.getId(), false);
		List visibleFunctionalitiesList = new ArrayList();
		for (Iterator it = containedFunctionalitiesList.iterator(); it.hasNext();) {
			LowFunctionality lowFunctionality = (LowFunctionality) it.next();
			boolean canSeeFunctionality;
			// if user is administrator, he can see all functionalities
			if (profile.isAbleToExecuteAction(SpagoBIConstants.DOCUMENT_MANAGEMENT_ADMIN)) {
				canSeeFunctionality = true;
			} else {
				// if user can exec or dev or test on functionality, he can see it, otherwise he cannot see it
				if (ObjectsAccessVerifier.canExec(lowFunctionality.getId(), profile) ||
						ObjectsAccessVerifier.canTest(lowFunctionality.getId(), profile) ||
						ObjectsAccessVerifier.canDev(lowFunctionality.getId(), profile)) {
					canSeeFunctionality = true;
				} else {
					canSeeFunctionality = false;
				}
			}
			if (canSeeFunctionality) {
				SDKFunctionality childFunctionality = new SDKObjectsConverter().fromLowFunctionalityToSDKFunctionality(lowFunctionality);
				visibleFunctionalitiesList.add(childFunctionality);
				// recursion
				setFunctionalityContent(childFunctionality);
			}
		}
		SDKFunctionality[] containedFunctionalities = new SDKFunctionality[visibleFunctionalitiesList.size()];
		containedFunctionalities = (SDKFunctionality[]) visibleFunctionalitiesList.toArray(containedFunctionalities);
		parentFunctionality.setContainedFunctionalities(containedFunctionalities);
		logger.debug("OUT");
	}

	public Integer saveNewDocument(SDKDocument document, SDKTemplate template,
			Integer functionalityId) throws NotAllowedOperationException {
		// TODO Auto-generated method stub
		return null;
	}

	public void uploadTemplate(Integer documentId, SDKTemplate template)
			throws NotAllowedOperationException {
		// TODO Auto-generated method stub
	}

}
