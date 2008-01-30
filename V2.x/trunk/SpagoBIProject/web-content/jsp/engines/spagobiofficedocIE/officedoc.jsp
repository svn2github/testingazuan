
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
                 java.util.Map,
                 org.safehaus.uuid.UUIDGenerator,
                 org.safehaus.uuid.UUID,
                 it.eng.spago.security.IEngUserProfile" %>
<%@page import="java.util.HashMap"%>
<%@page import="it.eng.spagobi.commons.utilities.ChannelUtilities"%>
                 

<%
    UUIDGenerator uuidGen  = UUIDGenerator.getInstance();
    UUID uuid = uuidGen.generateTimeBasedUUID();
    String requestIdentity = "request" + uuid.toString();  
    // get module response
    SourceBean moduleResponse = (SourceBean)aServiceResponse.getAttribute("ExecuteBIObjectModule");
    // get the id of the document
    Integer biobjectId = (Integer) moduleResponse.getAttribute("biobjectId");
    // get the name of the template file
	String templateFileName = (String) moduleResponse.getAttribute("templateFileName");
   	//get the user profile from session
	SessionContainer permSession = aSessionContainer.getPermanentContainer();
	IEngUserProfile userProfile = (IEngUserProfile)permSession.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
	String userId=(String)userProfile.getUserUniqueIdentifier();
	// get the string of the title
    String title = (String) moduleResponse.getAttribute("title");
    
	// try to get the modality
	boolean isSingleObjExec = false;
	String modality = (String)aSessionContainer.getAttribute(SpagoBIConstants.MODALITY);
   	if( (modality!=null) && modality.equals(SpagoBIConstants.SINGLE_OBJECT_EXECUTION_MODALITY) )
   		isSingleObjExec = true;
   	
   	// try to get from the session the heigh of the output area
   	boolean heightSetted = false;
   	String heightArea = (String)aSessionContainer.getAttribute(SpagoBIConstants.HEIGHT_OUTPUT_AREA);
   	if( (heightArea==null) || (heightArea.trim().equals("")) ) {
   		heightArea = "500";
   	} else {
   		heightSetted = true;
   	}
   	
   	// build the back link
   	Map backUrlPars = new HashMap();
	backUrlPars.put("PAGE", "BIObjectsPage");
	backUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_BACK_TO, "1");
	String backUrl = urlBuilder.getUrl(request, backUrlPars);
   	
	
	// build the refresh button
    Map refreshUrlPars = new HashMap();
    refreshUrlPars.put("PAGE", BIObjectsModule.MODULE_PAGE);
    refreshUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
	String refreshUrl = urlBuilder.getUrl(request, refreshUrlPars);
	
	IDomainDAO domaindao = DAOFactory.getDomainDAO();
	List states = domaindao.loadListDomainsByType("STATE");
    List possibleStates = new java.util.ArrayList();
    if (userProfile.isAbleToExecuteAction(SpagoBIConstants.DOCUMENT_MANAGEMENT_ADMIN)){
    	Iterator it = states.iterator();
    	 while(it.hasNext()) {
      		    	Domain state = (Domain)it.next();
      		    	if (state.getValueCd().equalsIgnoreCase("TEST")){ 
      					possibleStates.add(state);
      				}
      	}  
    } 
    if (userProfile.isAbleToExecuteAction(SpagoBIConstants.DOCUMENT_MANAGEMENT_TEST)){
    	Iterator it = states.iterator();
    	 while(it.hasNext()) {
      		    	Domain state = (Domain)it.next();
      		    	if ((state.getValueCd().equalsIgnoreCase("DEV")) || ((state.getValueCd().equalsIgnoreCase("REL")))) { 
      					possibleStates.add(state);
      				}
      	}  
    } 
%>


<% 
	String formUrl="";	
// IF NOT SINGLE OBJECT MODALITY SHOW DEFAULT TITLE BAR
	if(!isSingleObjExec) {
%>



<% 
		boolean canChangeState = false;
		if (userProfile.isAbleToExecuteAction(SpagoBIConstants.DOCUMENT_MANAGEMENT_DEV) ||
			userProfile.isAbleToExecuteAction(SpagoBIConstants.DOCUMENT_MANAGEMENT_TEST)){
			canChangeState = true;
    	    Map formUrlPars = new HashMap();
    	    formUrlPars.put("PAGE", ExecuteBIObjectModule.MODULE_PAGE);
    	    formUrlPars.put(SpagoBIConstants.MESSAGEDET, ObjectsTreeConstants.EXEC_CHANGE_STATE);
    	    formUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
    		formUrl = urlBuilder.getUrl(request, formUrlPars);		
		} 			  
%>

<%
		if(canChangeState) {
%>
		<%@page import="it.eng.spago.base.SourceBean"%>
<form method='POST' action='<%= formUrl %>' id='changeStateForm'  name='changeStateForm'>
<%
		}
%>


<table class='it.eng.spagobi.commons.utilitiestion'>
	<tr class='header-row-portlet-section'>
    	<td class='header-title-column-portlet-section' style='vertical-align:middle;'>
           &nbsp;&nbsp;&nbsp;<%=title%>
       </td>
       <td class='header-empty-column-portlet-section'>&nbsp;</td>
       <td class='header-button-column-portlet-section'>
           <a href='<%= backUrl %>'>
                 <img title='<spagobi:message key = "SBIDev.docConf.execBIObjectParams.backButt" />' 
                      class='header-button-image-portlet-section'
                      src='<%= urlBuilder.getResourceLink(request, "/img/back.png")%>' 
                      alt='<spagobi:message key = "SBIDev.docConf.execBIObjectParams.backButt" />' />
           </a>
       </td>    
<%
		if(canChangeState) {
%>
       <td class='header-select-column-portlet-section'>
      		<select class='portlet-form-field' name="newState">
      		<% 
      	    Iterator iterstates = possibleStates.iterator();
      	    while(iterstates.hasNext()) {
      	    	Domain state = (Domain)iterstates.next();
      		%>
      			<option value="<%=state.getValueId() + "," + state.getValueCd()  %>"><%=state.getValueName()%></option>
      		<%  } %>
      		</select>
   		</td>
   		<td class='header-select-column-portlet-section'>
   			<input type='image' class='header-button-image-portlet-section' src='<%= urlBuilder.getResourceLink(request, "/img/updateState.png")%>' title='<spagobi:message key = "SBIDev.docConf.execBIObjectParams.updateButt" />' alt='<spagobi:message key = "SBIDev.docConf.execBIObjectParams.updateButt" />'/> 
   		</td>
<%
		}
%>
   </tr>
</table>

<%
		if(canChangeState) {
%>
		</form>
<%
		}
%>


<% 
	// IF SINGLE OBJECT MODALITY SHOW THE PROPER TITLE BAR
	} else {
%>

<table width='100%' cellspacing='0' border='0'>
	<tr>
		<td class='header-title-column-single-object-execution-portlet-section' style='vertical-align:middle;'>
			&nbsp;&nbsp;&nbsp;<%=title%>
		</td>
		<td class='header-empty-column-single-object-execution-portlet-section'>&nbsp;</td>
		<td class='header-button-column-single-object-execution-portlet-section'>
			<a style="text-decoration:none;" href='<%=refreshUrl%>'> 
				<img width="20px" height="20px"
					src='<%= urlBuilder.getResourceLink(request, "/img/updateState.png")%>' 
					name='refresh' 
					alt='<spagobi:message key = "SBIExecution.refresh"/>' 
					title='<spagobi:message key = "SBIExecution.refresh"/>' /> 
			</a>
		</td>
</table>



<% } %>


<!-- ***************************************************************** -->
<!-- ***************************************************************** -->
<!-- **************** START BLOCK IFRAME ***************************** -->
<!-- ***************************************************************** -->
<!-- ***************************************************************** -->


<script>
		function adaptSize() {
			iframe = window.frames['iframeexec<%=requestIdentity%>'];
			navigatorname = navigator.appName;
			height = 0;
			navigatorname = navigatorname.toLowerCase();
			if(navigatorname.indexOf('explorer')) {
				height = iframe.document.body.offsetHeight;
			} else {
				height = iframe.innerHeight;
			}
			iframeEl = document.getElementById('iframeexec<%=requestIdentity%>');
			height = height + 100;
			if(height < 300){
				height = 300;
			}
			iframeEl.style.height = height + 100 + 'px';
		}
		
</script>


<div id="divIframe<%=requestIdentity%>" style="width:98%;float:left;padding-left:2%;">
           
           <%
           		String onloadStr = " ";
           		if(!heightSetted)
           			onloadStr = " onload='adaptSize();' ";
           		String heightStr = "height:400px;";
           		if(heightSetted)
           			heightStr = "height:"+heightArea+"px;";
           %> 
            
            <iframe <%=onloadStr%> 
				   style='display:inline;<%=heightStr%>' 
				   id='iframeexec<%=requestIdentity%>' 
                   name='iframeexec<%=requestIdentity%>'  
                   src="<%= urlBuilder.getResourceLink(request, "/jsp/engines/spagobiofficedocIE/officeReader.jsp?userId="+userId+"&documentId="+biobjectId.toString())%>"
                   frameborder=0  
			       width='100%' >

         	</iframe>       
                      

            
</div>
       


<!-- ***************************************************************** -->
<!-- ***************************************************************** -->
<!-- **************** END BLOCK IFRAME ******************************* -->
<!-- ***************************************************************** -->
<!-- ***************************************************************** -->