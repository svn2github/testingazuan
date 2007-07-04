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
<%@ include file="/jsp/portlet_base.jsp"%>

<%@ page import="java.util.Map,
                 java.util.Set,
                 java.util.Iterator,
                 it.eng.spagobi.constants.SpagoBIConstants,
                 javax.portlet.PortletURL,
                 it.eng.spago.navigation.LightNavigationManager,
                 it.eng.spagobi.services.modules.ExecuteBIObjectModule,
                 it.eng.spagobi.bo.Domain,
                 java.util.List" %>
<%@page import="java.util.HashMap"%>
<%@page import="it.eng.spagobi.utilities.ChannelUtilities"%>
<%@page import="it.eng.spagobi.bo.dao.audit.AuditManager"%>
<%@page import="it.eng.spagobi.bo.BIObject"%>
<%@page import="it.eng.spago.security.IEngUserProfile"%>
<%@page import="it.eng.spagobi.constants.ObjectsTreeConstants"%>
<%
	// control if the portlet act with single object modality.
	// get the modality of the portlet (single object execution, entire tree or filter tree)
	boolean isSingleObjExec = false;
	String modality = (String)aSessionContainer.getAttribute(SpagoBIConstants.MODALITY);
	if( (modality!=null) && modality.equals(SpagoBIConstants.SINGLE_OBJECT_EXECUTION_MODALITY) )
		isSingleObjExec = true;
	
    // get module response
    SourceBean moduleResponse = (SourceBean)aServiceResponse.getAttribute("ExecuteBIObjectModule");
	// get the BiObject from the response
    BIObject obj = (BIObject)moduleResponse.getAttribute(ObjectsTreeConstants.SESSION_OBJ_ATTR);
   	// get the user profile from session
	SessionContainer permSession = aSessionContainer.getPermanentContainer();
	IEngUserProfile userProfile = (IEngUserProfile)permSession.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
	// get the execution role
	String executionRole = (String)aSessionContainer.getAttribute(SpagoBIConstants.ROLE);

    // get the actor
    String actor = (String)aSessionContainer.getAttribute(SpagoBIConstants.ACTOR);
	
    String title = (String)moduleResponse.getAttribute("title");
    String displayTitleBar = (String)moduleResponse.getAttribute("displayTitleBar");
    String movie = ChannelUtilities.getSpagoBIContextName(request);
    //String movie = renderRequest.getContextPath();
    String relMovie = (String)moduleResponse.getAttribute("movie");
    if(relMovie.startsWith("/"))
    	movie = movie + relMovie;
    else movie = movie + "/" + relMovie;
	String width = (String)moduleResponse.getAttribute("width");
	String height = (String)moduleResponse.getAttribute("height");
	String dataurl = ChannelUtilities.getSpagoBIContextName(request);
	//String dataurl = renderRequest.getContextPath();
	String dataurlRel = (String)moduleResponse.getAttribute("dataurl");
	if(dataurlRel.startsWith("/"))
		dataurl = dataurl + dataurlRel;
	else dataurl = dataurl + "/" + dataurlRel;
	Map confParameters = (Map)moduleResponse.getAttribute("confParameters");
	Map dataParameters = (Map)moduleResponse.getAttribute("dataParameters");
	
	// AUDIT insert
	AuditManager auditManager = AuditManager.getInstance();
	String auditModality = (modality != null) ? modality : "NORMAL_EXECUTION";
	Integer auditId = auditManager.insertAudit(obj, userProfile, executionRole, auditModality);
	// adding parameters for AUDIT updating
	if (auditId != null) {
		dataParameters.put(AuditManager.AUDIT_ID, auditId.toString());
	}
	
	List possibleStateChanges = (List)moduleResponse.getAttribute("possibleStateChanges");
	// start to create the calling url
	// put the two dimensio parameter
	movie += "?paramHeight="+height+"&paramWidth="+width; 
	// create the dataurl string
	dataurl += "?";
	// for each data parameter append to the dataurl 
	Set dataKeys = dataParameters.keySet();
	Iterator iterDataKeys = dataKeys.iterator();
	while(iterDataKeys.hasNext()) {
		String name = (String)iterDataKeys.next();
		String value = (String)dataParameters.get(name);
	    dataurl += name + "%3D" + value + "%26"; 
	}
	
    // for each conf parameter append to the movie url  
	Set confKeys = confParameters.keySet();
	Iterator iterConfKeys = confKeys.iterator();
	while(iterConfKeys.hasNext()) {
		String name = (String)iterConfKeys.next();
		String value = (String)confParameters.get(name);
		movie += "&" + name + "=" + value; 
	}
	
    // append to the calling url the dataurl	
	movie += "&dataurl=" + dataurl;
	
	// build the back link
	Map backUrlPars = new HashMap();
    backUrlPars.put("PAGE", "BIObjectsPage");
    backUrlPars.put(SpagoBIConstants.ACTOR, actor);
    backUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_BACK_TO, "1");
    String backUrl = urlBuilder.getUrl(request, backUrlPars);
%>


<% 
	// IF NOT SINGLE OBJECT MODALITY SHOW DEFAULT TITLE BAR WITH BACK BUTTON
	if(!isSingleObjExec) {
%>

<table class='header-table-portlet-section'>
			<tr class='header-row-portlet-section'>
    			<td class='header-title-column-portlet-section' style='vertical-align:middle;'>
           			<%=title%>
       			</td>
       			<td class='header-empty-column-portlet-section'>&nbsp;</td>
       			<td class='header-button-column-portlet-section'>
           			<a href='<%=backUrl%>'>
                 		<img title='<spagobi:message key = "SBIDev.docConf.execBIObject.backButt" />' 
                      		 class='header-button-image-portlet-section'
                      		 src='<%=urlBuilder.getResourceLink(request, "/img/back.png")%>' 
                      		 alt='<spagobi:message key = "SBIDev.docConf.execBIObject.backButt" />' />
           			</a>
       			</td>
		   		<% if (!possibleStateChanges.isEmpty()) {
			   			Map formUrlPars = new HashMap();
			   			formUrlPars.put("PAGE", ExecuteBIObjectModule.MODULE_PAGE);
			   			formUrlPars.put(SpagoBIConstants.ACTOR, actor);
			   			formUrlPars.put(SpagoBIConstants.MESSAGEDET, SpagoBIConstants.EXEC_CHANGE_STATE);
			   			formUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
			   		    String formUrl = urlBuilder.getUrl(request, formUrlPars);
    			%>
       			<form method='POST' action='<%=formUrl%>' id='changeStateForm'  name='changeStateForm'>
	       		<td class='header-select-column-portlet-section'>
      				<select class='portlet-form-field' name="newState">
      				<% 
      		    	Iterator iterstates = possibleStateChanges.iterator();
      		    	while(iterstates.hasNext()) {
      		    		Domain state = (Domain)iterstates.next();
      				%>
      					<option value="<%=state.getValueId() + "," + state.getValueCd()  %>"><%=state.getValueName()%></option>
      				<%}%>
      				</select>
      			</td>
      			<td class='header-select-column-portlet-section'>
      				<input type='image' class='header-button-image-portlet-section' 
      				       src='<%=urlBuilder.getResourceLink(request, "/img/updateState.png")%>' 
      				       title='<spagobi:message key = "SBIDev.docConf.execBIObject.upStateButt" />' 
      				       alt='<spagobi:message key = "SBIDev.docConf.execBIObject.upStateButt" />'/> 
      			</td>
        		</form>
       			<% } %>
   			</tr>
		</table>
		
		
		






<% 
	// IF SINGLE OBJECT MODALITY SHOW SLIM TITLE BAR
	} else {
%>
		<% 
		  // display the title bar only if the title has been setted
		  if(displayTitleBar.trim().equalsIgnoreCase("true")) { %>
			<table width='100%' cellspacing='0' border='0'>	
				<tr>
					<td align="center" style='vertical-align:middle;' class="portlet-section-header"  >
						<%=title%>
					</td>
				</tr>
			</table>
		<% } %>
<%  } %>

<% // HTML CODE FOR THE FLASH COMPONENT %>
<center>  
       <object  classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" 
                codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,0,0" 
                type="application/x-shockwave-flash"
                data="<%=movie%>"  
                width="<%=width%>" 
                height="<%=height%>" >
       	  <param name="movie" value="<%=movie%>">
       	  <param name="quality" value="high">
       	  <param name="scale" value="noscale">
       	  <param name="salign" value="LT">
       	  <param name="menu" value="false">
       	  <param name="wmode" value="transparent">
        <EMBED  src="<%=movie%>" 
                quality=high 
                width="<%=width%>" 
                height="<%=height%>" 
                wmode="transparent" 
   			 TYPE="application/x-shockwave-flash" PLUGINSPAGE="http://www.macromedia.com/go/getflashplayer">
   		</EMBED>
	</object>    
</center>