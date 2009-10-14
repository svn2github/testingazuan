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

package it.eng.spagobi.engines.dashboard;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanAttribute;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.analiticalmodel.document.bo.BIObject;
import it.eng.spagobi.analiticalmodel.document.bo.ObjTemplate;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.bo.BIObjectParameter;
import it.eng.spagobi.commons.bo.UserProfile;
import it.eng.spagobi.commons.constants.ObjectsTreeConstants;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.engines.InternalEngineIFace;
import it.eng.spagobi.engines.drivers.exceptions.InvalidOperationRequest;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class SpagoBIDashboardInternalEngine implements InternalEngineIFace {

    private static transient Logger logger = Logger.getLogger(SpagoBIDashboardInternalEngine.class);

    public static final String messageBundle = "component_spagobidashboardIE_messages";

    /**
     * Executes the document and populates the response.
     * 
     * @param requestContainer The <code>RequestContainer</code> object (the session
     * can be retrieved from this object)
     * @param obj The <code>BIObject</code> representing the document to
     * be executed
     * @param response The response <code>SourceBean</code> to be populated
     * 
     * @throws EMFUserError the EMF user error
     */
    public void execute(RequestContainer requestContainer, BIObject obj, SourceBean response) throws EMFUserError {

	logger.debug("IN");

	try {

	    if (obj == null) {
		logger.error("The input object is null.");
		throw new EMFUserError(EMFErrorSeverity.ERROR, "100", messageBundle);
	    }

	    if (!obj.getBiObjectTypeCode().equalsIgnoreCase("DASH")) {
		logger.error("The input object is not a dashboard.");
		throw new EMFUserError(EMFErrorSeverity.ERROR, "1001", messageBundle);
	    }

	    byte[] contentBytes = null;
	    try {
		ObjTemplate template = DAOFactory.getObjTemplateDAO().getBIObjectActiveTemplate(obj.getId());
		if (template == null)
		    throw new Exception("Active Template null");
		contentBytes = template.getContent();
		if (contentBytes == null)
		    throw new Exception("Content of the Active template null");
	    } catch (Exception e) {
		logger.error("Error while recovering template content: \n" , e);
		throw new EMFUserError(EMFErrorSeverity.ERROR, "1002", messageBundle);
	    }
	    // get bytes of template and transform them into a SourceBean
	    SourceBean content = null;
	    try {
		String contentStr = new String(contentBytes);
		content = SourceBean.fromXMLString(contentStr);
	    } catch (Exception e) {
		logger.error("Error while converting the Template bytes into a SourceBean object");
		throw new EMFUserError(EMFErrorSeverity.ERROR, "1003", messageBundle);
	    }
	    // get information from the conf SourceBean and pass them into the
	    // response
	    String displayTitleBar = (String) content.getAttribute("displayTitleBar");
	    String movie = (String) content.getAttribute("movie");
	    String width = (String) content.getAttribute("DIMENSION.width");
	    String height = (String) content.getAttribute("DIMENSION.height");

	    String dataurl = (String) content.getAttribute("DATA.url");
	    // get all the parameters for data url
	    Map dataParameters = new HashMap();
	    SourceBean dataSB = (SourceBean) content.getAttribute("DATA");
	    List dataAttrsList = dataSB.getContainedSourceBeanAttributes();
	    Iterator dataAttrsIter = dataAttrsList.iterator();
	    if(obj.getDataSetId()!=null){
	    String dataSetId=obj.getDataSetId().toString();
	    dataParameters.put("datasetid", dataSetId);
	    }
	    
	    while (dataAttrsIter.hasNext()) {
		SourceBeanAttribute paramSBA = (SourceBeanAttribute) dataAttrsIter.next();
		SourceBean param = (SourceBean) paramSBA.getValue();
		String nameParam = (String) param.getAttribute("name");
		String valueParam = (String) param.getAttribute("value");
		dataParameters.put(nameParam, valueParam);
	    }
	    
	    // puts the document id
	    dataParameters.put("documentId", obj.getId().toString());
	    // puts the userId into parameters for data recovery
	    SessionContainer session = requestContainer.getSessionContainer();
	    IEngUserProfile profile = (IEngUserProfile) session.getPermanentContainer().getAttribute(
		    IEngUserProfile.ENG_USER_PROFILE);
	    dataParameters.put("userid", ((UserProfile)profile).getUserUniqueIdentifier());

	    // get all the parameters for dash configuration
	    Map confParameters = new HashMap();
	    SourceBean confSB = (SourceBean) content.getAttribute("CONF");
	    List confAttrsList = confSB.getContainedSourceBeanAttributes();
	    Iterator confAttrsIter = confAttrsList.iterator();
	    while (confAttrsIter.hasNext()) {
		SourceBeanAttribute paramSBA = (SourceBeanAttribute) confAttrsIter.next();
		SourceBean param = (SourceBean) paramSBA.getValue();
		String nameParam = (String) param.getAttribute("name");
		String valueParam = (String) param.getAttribute("value");
		confParameters.put(nameParam, valueParam);
	    }
	    // create the title
	    String title = "";
	    title += obj.getName();
	    String objDescr = obj.getDescription();
	    if ((objDescr != null) && !objDescr.trim().equals("")) {
		title += ": " + objDescr;
	    }
	    
		String parameters="";
	    //Search if the chart has parameters
		List parametersList=obj.getBiObjectParameters();
		logger.debug("Check for BIparameters and relative values");
		if(parametersList!=null){
			for (Iterator iterator = parametersList.iterator(); iterator.hasNext();) {
				BIObjectParameter par= (BIObjectParameter) iterator.next();
				String url=par.getParameterUrlName();
				List values=par.getParameterValues();
				if(values!=null){
					if(values.size()==1){
						String value=(String)values.get(0);
						parameters+="&"+url+"="+value;
						dataParameters.put(url, value);
						//parametersMap.put(url, value);
					}
				}

			}	

		}	   
	    
	    // set information into reponse
	    response.setAttribute(ObjectsTreeConstants.SESSION_OBJ_ATTR, obj);
	    response.setAttribute("movie", movie);
	    response.setAttribute("dataurl", dataurl);
	    response.setAttribute("width", width);
	    response.setAttribute("height", height);
	    response.setAttribute("displayTitleBar", displayTitleBar);
	    response.setAttribute("title", title);
	    response.setAttribute("confParameters", confParameters);
	    response.setAttribute("dataParameters", dataParameters);	    
	    // response.setAttribute("possibleStateChanges", possibleStates);
	    // set information for the publisher
	    response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, "DASHBOARD");

	} catch (EMFUserError error) {
	    logger.error("Cannot exec the dashboard", error);
	    throw error;
	} catch (Exception e) {
	    logger.error("Cannot exec the dashboard", e);
	    throw new EMFUserError(EMFErrorSeverity.ERROR, "100", messageBundle);
	} finally {
	    logger.debug("OUT");
	}

    }

    /**
     * The <code>SpagoBIDashboardInternalEngine</code> cannot manage
     * subobjects so this method must not be invoked.
     * 
     * @param requestContainer The <code>RequestContainer</code> object (the session
     * can be retrieved from this object)
     * @param obj The <code>BIObject</code> representing the document
     * @param response The response <code>SourceBean</code> to be populated
     * @param subObjectInfo An object describing the subobject to be executed
     * 
     * @throws EMFUserError the EMF user error
     */
    public void executeSubObject(RequestContainer requestContainer, BIObject obj, SourceBean response,
	    Object subObjectInfo) throws EMFUserError {
	// it cannot be invoked
	logger.error("SpagoBIDashboardInternalEngine cannot exec subobjects.");
	throw new EMFUserError(EMFErrorSeverity.ERROR, "101", messageBundle);
    }

    /**
     * Function not implemented. Thid method should not be called
     * 
     * @param requestContainer The <code>RequestContainer</code> object (the session
     * can be retrieved from this object)
     * @param response The response <code>SourceBean</code> to be populated
     * @param obj the obj
     * 
     * @throws InvalidOperationRequest the invalid operation request
     * @throws EMFUserError the EMF user error
     */
    public void handleNewDocumentTemplateCreation(RequestContainer requestContainer, BIObject obj, SourceBean response)
	    throws EMFUserError, InvalidOperationRequest {
	logger.error("SpagoBIDashboardInternalEngine cannot build document template.");
	throw new InvalidOperationRequest();

    }

    /**
     * Function not implemented. Thid method should not be called
     * 
     * @param requestContainer The <code>RequestContainer</code> object (the session
     * can be retrieved from this object)
     * @param response The response <code>SourceBean</code> to be populated
     * @param obj the obj
     * 
     * @throws InvalidOperationRequest the invalid operation request
     * @throws EMFUserError the EMF user error
     */
    public void handleDocumentTemplateEdit(RequestContainer requestContainer, BIObject obj, SourceBean response)
	    throws EMFUserError, InvalidOperationRequest {
	logger.error("SpagoBIDashboardInternalEngine cannot build document template.");
	throw new InvalidOperationRequest();
    }

}
