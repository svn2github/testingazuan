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
package it.eng.spagobi.services.analyticalmodel.service;

import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.analiticalmodel.document.bo.BIObject;
import it.eng.spagobi.analiticalmodel.document.dao.IBIObjectDAO;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.bo.BIObjectParameter;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.dao.IBIObjectParameterDAO;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.utilities.ObjectsAccessVerifier;
import it.eng.spagobi.engines.config.bo.Engine;
import it.eng.spagobi.services.analyticalmodel.document.bo.Document;
import it.eng.spagobi.services.behaviouralmodel.analyticaldriver.bo.DocumentParameter;
import it.eng.spagobi.services.session.service.SessionServiceImpl;
import java.util.*;
import org.apache.log4j.Logger;

public class DocumentServiceImpl {

    private static Logger logger = Logger.getLogger(DocumentServiceImpl.class);

    public DocumentServiceImpl(){}

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

    public DocumentParameter[] getDocumentParameters(Integer documentId, String roleName) {
        DocumentParameter parameters[];
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

    public HashMap getAdmissibleValues(Integer documentParameterId, String roleName)
    {
        return null;
    }

}

