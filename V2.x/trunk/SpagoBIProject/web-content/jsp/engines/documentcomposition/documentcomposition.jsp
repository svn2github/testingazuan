
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
                 it.eng.spagobi.engines.documentcomposition.utils.DocumentCompositionUtils,
                 it.eng.spagobi.engines.documentcomposition.configuration.DocumentCompositionConfiguration,
                 it.eng.spagobi.engines.documentcomposition.configuration.DocumentCompositionConfiguration.Document,
                 java.util.Map,
                 it.eng.spagobi.commons.utilities.GeneralUtilities" %>
<%@page import="java.util.HashMap"%>
<%@page import="it.eng.spago.base.SourceBean"%>
<%@page import="org.apache.log4j.Logger"%>

<%@page import="it.eng.spagobi.engines.documentcomposition.SpagoBIDocumentCompositionInternalEngine"%>
<%@page import="it.eng.spagobi.commons.constants.SpagoBIConstants" %>
                 
<%! private static transient Logger logger=Logger.getLogger(SpagoBIDocumentCompositionInternalEngine.class);%>

<% 
	logger.debug("IN");
	
    //acquisizione info come template a cui girare la richiesta
    String nameTemplate = "";
 	
    //get object configuration
    DocumentCompositionConfiguration docConfig = null;
    docConfig = (DocumentCompositionConfiguration)aSessionContainer.getAttribute("docConfig");
     
    //get template file
    nameTemplate = docConfig.getTemplateFile();
    logger.debug("name TemplateFile: " + nameTemplate);
    
    //get list of documents
    List lstDoc = docConfig.getDocumentsArray();
    
    //get information for document composition
    Map lstUrl = new HashMap();
    Map lstStyle = new HashMap();
    Map lstUrlParams  = new HashMap();
    Map lstDocLinked = new HashMap();
    Map lstFieldLinked = new HashMap();
    Map lstDocStyle = new HashMap();
    
    //loop on documents
    for (int i = 0; i < lstDoc.size(); i++){
    	//gets url, parameters and other informations
    	Document tmpDoc = (Document)lstDoc.get(i);
    	String tmpUrl = DocumentCompositionUtils.getEngineUrl(tmpDoc.getSbiObjLabel(), aSessionContainer, aRequestContainer.getServiceRequest());
    	String tmpStyle = (tmpDoc.getStyle()==null)?"":tmpDoc.getStyle(); 
    	HashMap tmpUrlParams = (HashMap)aSessionContainer.getAttribute(tmpDoc.getLabel());
    	HashMap tmpInfoDocLinked = (HashMap)docConfig.getInfoDocumentLinked(tmpDoc.getLabel());
    	
    	lstDocStyle.put("STYLE_"+tmpDoc.getLabel(), (String)tmpInfoDocLinked.get("STYLE_"+tmpDoc.getLabel()));

    	if (tmpInfoDocLinked != null && tmpInfoDocLinked.size() > 0){
    		lstDocLinked.put("MAIN_DOC_LABEL_"+(i),tmpDoc.getLabel());
    		//loop on document linked 
	    	for (int j=0; j<tmpInfoDocLinked.size(); j++){
	    		String tmpLabelDoc = (String)tmpInfoDocLinked.get("LABEL_DOC_"+(j));
				if (tmpLabelDoc != null && !tmpLabelDoc.equals("")){
					if ((String)tmpInfoDocLinked.get("SBI_LABEL_PAR_MASTER_"+tmpDoc.getNumOrder()+"_"+(j)) != null) 
		    			lstFieldLinked.put("SBI_LABEL_PAR_MASTER_"+i+"_"+(j), (String)tmpInfoDocLinked.get("SBI_LABEL_PAR_MASTER_"+tmpDoc.getNumOrder()+"_"+(j)));
					
					//get parameters linked
					Integer numDocLinked = new Integer("0");					
					String strNumDocLinked = (String)tmpInfoDocLinked.get("NUM_DOC_LINKED_"+tmpDoc.getNumOrder()+"_"+(j));
					if (strNumDocLinked != null && !strNumDocLinked.equals("")){
						numDocLinked = Integer.valueOf(strNumDocLinked);
						lstFieldLinked.put("NUM_DOC_FIELD_LINKED_"+i+"_"+(j), numDocLinked);
					}
					//loop on parameters of document linked
					for(int k=0; k < numDocLinked.intValue(); k++){
						String tmpSubDoc = (String)tmpInfoDocLinked.get("SBI_LABEL_DOC_"+(k));
						tmpSubDoc = tmpSubDoc.substring(tmpSubDoc.indexOf("|")+1);
						String tmpParDoc = (String)tmpInfoDocLinked.get("SBI_LABEL_PAR_"+(k));
						if (tmpParDoc != null && !tmpParDoc.equals("")){
							lstDocLinked.put("DOC_LABEL_LINKED_"+i+"_"+(j)+"_"+k, (String)tmpInfoDocLinked.get("SBI_LABEL_DOC_"+(k)));
							lstFieldLinked.put("DOC_FIELD_LINKED_"+i+"_"+(j)+"_"+k, tmpParDoc);
						}
					}
				}
			}
    	}

    	lstUrl.put("URL_DOC_" + (i), tmpUrl);
    	lstStyle.put("STYLE_DOC_"+(i),tmpStyle);
    	lstUrlParams.put("PARAMS_DOC_"+(i),tmpUrlParams);
    	lstUrlParams.put("SBI_DOC_LABEL_"+(i),  tmpDoc.getSbiObjLabel() + "|" + tmpDoc.getLabel());
    	
    	logger.debug("url for iframe_"+(i)+ " : " + tmpUrl + " Style: " + tmpStyle );
    	logger.debug("parameters for iframe_"+(i)+ " : " + tmpUrlParams);

    }
    aSessionContainer.setAttribute("urlIframe", GeneralUtilities.getSpagoBiContextAddress()+"/jsp/engines/documentcomposition/documentcomposition_Iframe.jsp");
    aSessionContainer.setAttribute("docUrls", lstUrl);
    aSessionContainer.setAttribute("docUrlParams", lstUrlParams);
    aSessionContainer.setAttribute("docLinked", lstDocLinked);
    aSessionContainer.setAttribute("fieldLinked", lstFieldLinked);
    aSessionContainer.setAttribute("panelStyle", lstDocStyle);
    aSessionContainer.setAttribute("docStyle", lstStyle);
   
    //include jsp requested
    getServletContext().getRequestDispatcher(nameTemplate).include(request,response);    
	    
    logger.debug("OUT");

    %>
    
    
<spagobi:error/>

