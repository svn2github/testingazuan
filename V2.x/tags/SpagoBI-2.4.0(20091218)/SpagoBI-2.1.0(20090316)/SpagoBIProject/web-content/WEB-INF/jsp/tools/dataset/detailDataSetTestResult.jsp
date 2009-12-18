<!--
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
-->


<%@ include file="/WEB-INF/jsp/commons/portlet_base.jsp"%>

<%@ page import="javax.portlet.PortletURL,
			it.eng.spagobi.commons.constants.SpagoBIConstants,
			it.eng.spagobi.commons.dao.DAOFactory,
			it.eng.spago.navigation.LightNavigationManager,
			java.util.List,
			java.util.ArrayList,
			java.util.Iterator"%>

<%@page import="it.eng.spagobi.commons.utilities.GeneralUtilities"%>
<%@page import="it.eng.spagobi.commons.utilities.PortletUtilities"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>

<%
	SourceBean detailMR = (SourceBean) aServiceResponse.getAttribute("DetailDataSetModule"); 
	SourceBean listLovMR = (SourceBean) aServiceResponse.getAttribute("ListTestDataSetModule"); 

	
	String modality = null;
	if (detailMR != null) modality = (String) detailMR.getAttribute(SpagoBIConstants.MODALITY);
	if (modality == null) modality = (String) aSessionContainer.getAttribute(SpagoBIConstants.MODALITY);

	String parametersXMLModified = (String)aSessionContainer.getAttribute(DetailDataSetModule.DATASET_MODIFIED);
	if (parametersXMLModified == null) 
		parametersXMLModified = "false";
	
	
	String messagedet = "";
  	if (modality.equals(SpagoBIConstants.DETAIL_INS))
		messagedet = SpagoBIConstants.DETAIL_INS;
	else messagedet = SpagoBIConstants.DETAIL_MOD;
	
  	Map saveUrlPars = new HashMap();
  	saveUrlPars.put("PAGE", "DetailDataSetPage");
  	saveUrlPars.put(SpagoBIConstants.MESSAGEDET, messagedet);
  	saveUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
  	saveUrlPars.put("RETURN_FROM_TEST_MSG","SAVE");
    String saveUrl = urlBuilder.getUrl(request, saveUrlPars);
  	
    Map backUrlPars = new HashMap();
    backUrlPars.put("PAGE", "DetailDataSetPage");
    backUrlPars.put(SpagoBIConstants.MESSAGEDET, modality);
    backUrlPars.put("modality", modality);
    backUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
    backUrlPars.put("RETURN_FROM_TEST_MSG", "DO_NOT_SAVE");
    if(!parametersXMLModified.trim().equals(""))
    	backUrlPars.put(DetailDataSetModule.DATASET_MODIFIED, parametersXMLModified);
    String backUrl = urlBuilder.getUrl(request, backUrlPars);
  	

%>



<%@page import="it.eng.spagobi.tools.dataset.service.DetailDataSetModule"%>






<!-- TITLE -->

<%@page import="it.eng.spago.error.EMFErrorHandler"%>
<table class='header-table-portlet-section'>		
	<tr class='header-row-portlet-section'>
		<td class='header-title-column-portlet-section'
		    style='vertical-align:middle;padding-left:5px;'>
			<spagobi:message key = "SBIDev.predLov.testPageTitle" />
		</td>
		<td class='header-empty-column-portlet-section'>&nbsp;</td>
		<td class='header-button-column-portlet-section'>
			<a href=<%=saveUrl%>>
				<img class='header-button-image-portlet-section'
					src='<%=urlBuilder.getResourceLink(request, "/img/saveAndGoBack.png")%>' 
					title='<spagobi:message key = "SBIDev.predLov.saveButt" />'  
					alt='<spagobi:message key = "SBIDev.predLov.saveButt" />' 
				/>
			</a>
		</td>
		<td class='header-button-column-portlet-section'>
			<a href="<%=backUrl%>"> 
      				<img class='header-button-image-portlet-section' 
      				     title='<spagobi:message key = "SBISet.Funct.backButt" />' 
      				     src='<%=urlBuilder.getResourceLink(request, "/img/back.png")%>' 
      				     alt='<spagobi:message key = "SBISet.Funct.backButt" />' />
			</a>
		</td>
	</tr>
</table>





<!-- BODY -->


<div class=''>


   <!-- ERROR TAG --> 
	<spagobi:error/>

<% EMFErrorHandler errorHandler=aResponseContainer.getErrorHandler();
if(errorHandler.isOK()){    %>
	<div width="100%">
			<spagobi:list moduleName="ListTestDataSetModule"/>
		</div>
<%} %>			 

</div>


				







