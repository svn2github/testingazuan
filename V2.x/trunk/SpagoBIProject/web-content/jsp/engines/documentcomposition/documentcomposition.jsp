
<!--
SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005 Engineering Ingegneria Informatica S.p.A.

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
-->
<%@ include file="/jsp/commons/portlet_base.jsp"%>

<%@ page import="java.util.List,
                 it.eng.spagobi.commons.constants.ObjectsTreeConstants,
                 java.util.Iterator,
                 it.eng.spagobi.commons.bo.Domain,
                 it.eng.spagobi.commons.dao.IDomainDAO,
                 it.eng.spagobi.commons.dao.DAOFactory,
                 it.eng.spagobi.analiticalmodel.document.service.BIObjectsModule,
                 it.eng.spagobi.analiticalmodel.document.service.ExecuteBIObjectModule,
                 it.eng.spago.navigation.LightNavigationManager,
                 it.eng.spagobi.engines.documentcomposition.utils.DocumentCompositionUtils,
                 it.eng.spagobi.engines.documentcomposition.configuration.DocumentCompositionConfiguration,
                 it.eng.spagobi.engines.documentcomposition.configuration.DocumentCompositionConfiguration.Document,
                 java.util.Map,
                 java.util.ArrayList,
                 org.safehaus.uuid.UUIDGenerator,
                 org.safehaus.uuid.UUID,
                 it.eng.spago.security.IEngUserProfile,
                 it.eng.spagobi.commons.utilities.GeneralUtilities,
                 javax.portlet.*" %>
<%@page import="java.util.HashMap"%>
<%@page import="it.eng.spagobi.commons.utilities.ChannelUtilities"%>
<%@page import="it.eng.spago.base.SourceBean"%>
<%@page import="org.apache.log4j.Logger"%>

<%@page import="it.eng.spagobi.engines.documentcomposition.SpagoBIDocumentCompositionInternalEngine"%>
                 
<%! private static transient Logger logger=Logger.getLogger(SpagoBIDocumentCompositionInternalEngine.class);%>

<% 
	logger.debug("IN");
	
    //acquisizione info come template a cui girare la richiesta
    String nameTemplate = "";
   
    // get the user profile from session
	SessionContainer permSession = aSessionContainer.getPermanentContainer();
    IEngUserProfile userProfile = (IEngUserProfile)permSession.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
    logger.debug("userProfile: " + userProfile);
 	
    //get object configuration
    DocumentCompositionConfiguration docConfig = null;
    docConfig = (DocumentCompositionConfiguration)aSessionContainer.getAttribute("docConfig");
  
    //get template file
    nameTemplate = docConfig.getTemplateFile();
    logger.debug("name TemplateFile: " + nameTemplate);
    
    //get list of documents
    List lstDoc = docConfig.getDocumentsArray();
    //get url for engine
    Map lstUrl = new HashMap();
    Map lstHeight = new HashMap();
    Map lstWidth = new HashMap();
    Map lstParams  = new HashMap();
    
    //for (int i = 0; i < docSbiLables.size(); i++){
    for (int i = 0; i < lstDoc.size(); i++){
    	//gets url, parameters and other informations
    	Document tmpDoc = (Document)lstDoc.get(i);
    	String tmpUrl = DocumentCompositionUtils.getEngineUrl(tmpDoc.getSbiObjLabel(), aSessionContainer, aRequestContainer.getServiceRequest());
    	String tmpHeight = (tmpDoc.getDimensionHeight()==null)?"300px":tmpDoc.getDimensionHeight(); 
    	String tmpWidth = (tmpDoc.getDimensionWidth()==null)?"800px":tmpDoc.getDimensionWidth();
    	HashMap tmpParams = (HashMap)aSessionContainer.getAttribute(tmpDoc.getLabel());
    	lstUrl.put("URL_DOC_" + (i+1), tmpUrl);
    	lstHeight.put("HEIGHT_DOC_"+(i+1),tmpHeight);
    	lstWidth.put("WIDTH_DOC_"+(i+1),tmpWidth);
    	lstParams.put("PARAMS_DOC_"+(i+1),tmpParams);
    	lstParams.put("DOC_LABEL_"+(i+1), tmpDoc.getLabel());
    	
    	logger.debug("url for iframe_"+(i+1)+ " : " + tmpUrl + " Height: " + tmpHeight + " Width: " + tmpWidth);
    	logger.debug("parameters for iframe_"+(i+1)+ " : " + tmpParams);
    }
    aSessionContainer.setAttribute("urlIframe", GeneralUtilities.getSpagoBiContextAddress()+"/jsp/engines/documentcomposition/documentcomposition_Iframe.jsp");
    aSessionContainer.setAttribute("docUrls", lstUrl);
    aSessionContainer.setAttribute("docHeight", lstHeight);
    aSessionContainer.setAttribute("docWidth", lstWidth);
    aSessionContainer.setAttribute("docParams", lstParams);
    aSessionContainer.setAttribute("flgDocComposite", "true");
     
    //include jsp requested
    getServletContext().getRequestDispatcher(nameTemplate).include(request,response);    
	    
    logger.debug("OUT");

    %>
    
    
<spagobi:error/>

