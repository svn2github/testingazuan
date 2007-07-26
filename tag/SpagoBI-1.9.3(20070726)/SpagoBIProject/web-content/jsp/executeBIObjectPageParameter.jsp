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

<%@ page import="java.util.List,
                 java.util.Iterator,
                 it.eng.spagobi.constants.ObjectsTreeConstants,
                 it.eng.spagobi.services.modules.ExecuteBIObjectModule,
                 it.eng.spagobi.constants.AdmintoolsConstants,
                 it.eng.spagobi.constants.SpagoBIConstants,
                 it.eng.spagobi.utilities.GeneralUtilities,
                 it.eng.spagobi.bo.BIObject,
                 it.eng.spago.security.IEngUserProfile,
                 it.eng.spago.base.SessionContainer,
                 it.eng.spago.navigation.LightNavigationManager" %>
<%@ page import="java.util.Date"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.HashMap"%>
<%@page import="it.eng.spagobi.utilities.ChannelUtilities"%>
<%@page import="org.safehaus.uuid.UUIDGenerator"%>
<%@page import="org.safehaus.uuid.UUID"%>

<% 
	// identity string for object of the page
	UUIDGenerator uuidGen  = UUIDGenerator.getInstance();
	UUID uuid = uuidGen.generateTimeBasedUUID();
	String requestIdentity = "request" + uuid.toString();
	requestIdentity = requestIdentity.replaceAll("-", "");
    // get object from the session 
    BIObject obj = (BIObject)aSessionContainer.getAttribute(ObjectsTreeConstants.SESSION_OBJ_ATTR);  
	//substitute profile attributes for FIX LOV parameters
	GeneralUtilities.subsituteBIObjectParametersLovProfileAttributes(obj,aSessionContainer);
    // get profile of the user
    SessionContainer permSess = aSessionContainer.getPermanentContainer();
    IEngUserProfile profile = (IEngUserProfile)permSess.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
    String currentUser = (String)profile.getUserUniqueIdentifier();
    // get response of the module
    SourceBean moduleResponse = (SourceBean)aServiceResponse.getAttribute("ExecuteBIObjectModule"); 
	// get actor from session
    String actor = (String)aSessionContainer.getAttribute(SpagoBIConstants.ACTOR); 
	// get subObject List
	List subObjs = (List)moduleResponse.getAttribute(SpagoBIConstants.SUBOBJECT_LIST);
	// get snapshot List
	List snapshots = (List)moduleResponse.getAttribute(SpagoBIConstants.SNAPSHOT_LIST);
	
	String typeObj = (String)moduleResponse.getAttribute(SpagoBIConstants.BIOBJECT_TYPE_CODE);
	
	// try to get the the "NO_PARAMETERS", if it is present the object has only subobjects but not
	// parameters
	String noParsStr = (String)moduleResponse.getAttribute("NO_PARAMETERS");
	boolean noPars = false;
	if(noParsStr!=null)
		noPars = true;
	
    // try to get the modality
	boolean isSingleObjExec = false;
	String modality = (String)aSessionContainer.getAttribute(SpagoBIConstants.MODALITY);
   	if( (modality!=null) && modality.equals(SpagoBIConstants.SINGLE_OBJECT_EXECUTION_MODALITY) ) 
   		isSingleObjExec = true;
   	
    //  build the string of the title
    String title = "";
    title = obj.getName();
    String objDescr = obj.getDescription();
    if( (objDescr!=null) && !(objDescr.trim().equals("")) ) 
    	title += ": " + objDescr;
   	
    String executionId = (String) aServiceRequest.getAttribute("spagobi_execution_id");
    String flowId = (String) aServiceRequest.getAttribute("spagobi_flow_id");
    
   	// build the url for the parameters form
   	Map execUrlPars = new HashMap();
   	execUrlPars.put("PAGE", "ValidateExecuteBIObjectPage");
   	execUrlPars.put(SpagoBIConstants.ACTOR, actor);
   	execUrlPars.put(SpagoBIConstants.MESSAGEDET, ObjectsTreeConstants.EXEC_PHASE_RUN);
   	execUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
   	if (executionId != null && flowId != null) {
   		execUrlPars.put("spagobi_execution_id", executionId);
   		execUrlPars.put("spagobi_flow_id", flowId);
   	}
    String execUrl = urlBuilder.getUrl(request, execUrlPars);
	    
   	// build the back url 
   	Map backUrlPars = new HashMap();
    backUrlPars.put("PAGE", "BIObjectsPage");
    backUrlPars.put(SpagoBIConstants.ACTOR, actor);
    backUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_BACK_TO, "1");
   	if (executionId != null && flowId != null) {
   		backUrlPars.put("spagobi_execution_id", executionId);
   		backUrlPars.put("spagobi_flow_id", flowId);
   	}
    String backUrl = urlBuilder.getUrl(request, backUrlPars);
   		
%>
	

<form method='POST' action='<%=execUrl%>' id='paramsValueForm<%=requestIdentity%>' name='paramsValueForm'>	


<% 
	// IF NOT SINGLE OBJECT MODALITY SHOW DEFAULT TITLE BAR
	if(!isSingleObjExec) {
%>

<table class='header-table-portlet-section'>		
	<tr class='header-row-portlet-section'>
		<td class='header-title-column-portlet-section' style='vertical-align:middle;padding-left:5px;'>
			<%=title%>
		</td>
		<td class='header-empty-column-portlet-section'>&nbsp;</td>
		<% if ( !noPars ) { %>
			<td class='header-button-column-portlet-section'>
				<a href="javascript:document.getElementById('paramsValueForm<%=requestIdentity%>').submit()"> 
      					<img class='header-button-image-portlet-section' 
					title='<spagobi:message key ="SBIDev.docConf.execBIObjectParams.execButt" />' 
					src='<%=urlBuilder.getResourceLink(request, "/img/exec.png")%>' 
					alt='<spagobi:message key = "SBIDev.docConf.execBIObjectParams.execButt" />' /> 
				</a>
			</td>
		<% } %>
		<td class='header-button-column-portlet-section'>
			<a href='<%= backUrl %>'> 
      				<img class='header-button-image-portlet-section' 
				title='<spagobi:message key = "SBIDev.docConf.execBIObjectParams.backButt" />' 
				src='<%=urlBuilder.getResourceLink(request, "/img/back.png")%>' 
				alt='<spagobi:message key = "SBIDev.docConf.execBIObjectParams.backButt" />' />
			</a>
		</td>
	</tr>
</table>


<% 
	// IF SINGLE OBJECT MODALITY SHOW THE PROPER TITLE BAR
	} else {
%>	
<table width='100%' cellspacing='0' border='0'>
	<tr>
		<td class='header-title-column-single-object-execution-portlet-section' 
		    style='vertical-align:middle;padding-left:5px;'>
			<%=title%>
		</td>
		<td class='header-empty-column-single-object-execution-portlet-section'>&nbsp;</td>
		<td class='header-button-column-single-object-execution-portlet-section'>
			<input type='image' 
				src='<%=urlBuilder.getResourceLink(request, "/img/exec22.png")%>' 
				name='exec' 
				alt='<%=msgBuilder.getMessage(aRequestContainer, "SBIDev.docConf.execBIObjectParams.execButt", "messages")%>' 
				title='<%=msgBuilder.getMessage(aRequestContainer, "SBIDev.docConf.execBIObjectParams.execButt", "messages")%>' />
		</td>
</table>
	

<% } %>

<div class='div_background_no_img' >
	
<!-- if there aren't parameters dont't show the parameter form  -->
<%-- if(!noPars) { --%>
	<spagobi:dynamicPage modality="EXECUTION_MODALITY" actor="<%=actor %>" />
<%-- } --%>


<div class='errors-object-details-div'>
	<spagobi:error/>
</div>	
 



<!--  ************************* CLOSE FORM ********************************** -->
</form>


<%--
// if is single object modality, does not consider neither subobjects nor snapshots and closes the page div
if (isSingleObjExec) {
	%>
	</div>
	<%
	return;
}
--%>

<% if( ((subObjs!=null)&&(subObjs.size()!=0))  ||  ((snapshots!=null)&&(snapshots.size()!=0))  ) { %>



	<!-- if there aren't parameters show the link for the new composition -->
	<% 
	if(noPars) { 
	   %>
	   <span class='portlet-font'>
				<%=msgBuilder.getMessage(aRequestContainer, "SBIDev.docConf.subBIObject.newComposition1", "messages")%>
		 </span>
	   <a href='<%=execUrl%>'
				class='portlet-form-field-label'
				onmouseover="this.style.color='#9297ac';"
				onmouseout="this.style.color='#074B88';">
				<%=msgBuilder.getMessage(aRequestContainer, "SBIDev.docConf.subBIObject.newComposition2", "messages")%>
		 </a>
	
	   <br/><br/>
	   <%
	} 
	%>

	
	
	<% if( (subObjs!=null)&&(subObjs.size()!=0) ) { %>
	
			<div style='width:100%;visibility:visible;' 
					 class='UITabs' 
					 id='tabPanelWithJavascript' 
					 name='tabPanelWithJavascript'>
				<div class="first-tab-level" style="background-color:#f8f8f8">
					<div style="overflow: hidden; width:100%">
						<div class='tab'>
							<%=msgBuilder.getMessage(aRequestContainer, "SBIDev.docConf.subBIObject.title","messages")%>
						</div>
					</div>
				</div>
			</div>
						
		    <table style='width:100%;'> 
			     <tr>
			       <td style='vertical-align:middle;' align="left" class="portlet-section-header">
			           <spagobi:message key='SBIDev.docConf.subBIObject.name' />
			       </td>
			       
			       <td align="left" class="portlet-section-header">&nbsp;</td>
			       <td style='vertical-align:middle;' align="left" class="portlet-section-header">
			           <spagobi:message key='SBIDev.docConf.subBIObject.owner' />
			       </td>
			       
			       <td align="left" class="portlet-section-header">&nbsp;</td>
			       <td style='vertical-align:middle;' align="left" class="portlet-section-header">
			           <spagobi:message key='SBIDev.docConf.subBIObject.description' />
			       </td>
			       
			        <td align="left" class="portlet-section-header">&nbsp;</td>
			       <td style='vertical-align:middle;' align="left" class="portlet-section-header">
			           <spagobi:message key='SBIDev.docConf.subBIObject.creationDate' />
			       </td>
			       
			        <td align="left" class="portlet-section-header">&nbsp;</td>
			       <td style='vertical-align:middle;' align="left" class="portlet-section-header">
			           <spagobi:message key='SBIDev.docConf.subBIObject.lastModificationDate' />
			       </td>
			       
			       <td align="left" class="portlet-section-header">&nbsp;</td>
			       <td style='vertical-align:middle;' align="left" class="portlet-section-header">
			           <spagobi:message key='SBIDev.docConf.subBIObject.visibility' />
			       </td>
			       <td align="left" class="portlet-section-header" colspan='3' >&nbsp;</td>
			     <tr> 
				              
				    	<% Iterator iterSubs =  subObjs.iterator();
				    	   BIObject.SubObjectDetail subObj = null;
				    	   String nameSub = "";
				    	   String descr = "";
				    	   String visib = null;
				    	   String delete = "";
				    	   String owner = "";
				    	   String creationDate = "";
				    	   String lastModificationDate = "";
				    	   String execSubObjUrl = null;
				    	   String deleteSubObjUrl = null;
					   
					   boolean alternate = false;
					   String rowClass = "";
					   
		                   while(iterSubs.hasNext()) {
		    	                subObj = (BIObject.SubObjectDetail)iterSubs.next();
					
					rowClass = (alternate) ? "portlet-section-alternate" : "portlet-section-body";
					alternate = !alternate;
					
		                        nameSub = subObj.getName();
		                        descr = subObj.getDescription();
		                        owner = subObj.getOwner();
		                        creationDate = subObj.getCreationDate();
		                        lastModificationDate = subObj.getLastModifcationDate();
		                        
		                        visib = "Private";
		                        if(subObj.isPublicVisible()) {
		                        	visib = "Public";
		                        } 
		                        if(owner.equals(currentUser)) {
		                        	delete = "delete";
		                        }
		                        Map execSubObjUrlPars = new HashMap();
		                        execSubObjUrlPars.put("PAGE", ExecuteBIObjectModule.MODULE_PAGE );
		                        execSubObjUrlPars.put(SpagoBIConstants.MESSAGEDET, "EXEC_SUBOBJECT");
		                        execSubObjUrlPars.put(SpagoBIConstants.ACTOR, actor);
		                        execSubObjUrlPars.put("NAME_SUB_OBJECT", nameSub);
		                        execSubObjUrlPars.put("DESCRIPTION_SUB_OBJECT", descr);
		                        execSubObjUrlPars.put("VISIBILITY_SUB_OBJECT", visib);
		                        execSubObjUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED,"true");
		            		   	if (executionId != null && flowId != null) {
		            		   		execSubObjUrlPars.put("spagobi_execution_id", executionId);
		            		   		execSubObjUrlPars.put("spagobi_flow_id", flowId);
		            		   	}
		            		    execSubObjUrl = urlBuilder.getUrl(request, execSubObjUrlPars);
		                        
		            		    Map deleteSubObjUrlPars = new HashMap();
		            		    deleteSubObjUrlPars.put("PAGE", ExecuteBIObjectModule.MODULE_PAGE);
		            		    deleteSubObjUrlPars.put(SpagoBIConstants.MESSAGEDET, "DELETE_SUBOBJECT");
		            		    deleteSubObjUrlPars.put(SpagoBIConstants.ACTOR, actor);
		            		    deleteSubObjUrlPars.put("NAME_SUB_OBJECT", nameSub);
		            		    deleteSubObjUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED,"true");
		            		   	if (executionId != null && flowId != null) {
		            		   		deleteSubObjUrlPars.put("spagobi_execution_id", executionId);
		            		   		deleteSubObjUrlPars.put("spagobi_flow_id", flowId);
		            		   	}
		            		    deleteSubObjUrl = urlBuilder.getUrl(request, deleteSubObjUrlPars);
		                   %>
		                        <tr class='portlet-font'>
		                        	<td style='vertical-align:middle;' class='<%= rowClass %>'>
		                        		<%= nameSub %>
		                        	</td>
		                        	
		                        	<td class='<%= rowClass %>' width="20px">&nbsp;</td> 
		                        	<td style='vertical-align:middle;' class='<%= rowClass%>' ><%=owner %></td>
		                        	
		                        	<td class='<%= rowClass %>' width="20px">&nbsp;</td> 
		                        	<td style='vertical-align:middle;' class='<%= rowClass%>' ><%=descr %></td>
		                        	
		                        	
		                        	<td class='<%= rowClass %>' width="20px">&nbsp;</td> 
		                        	<td style='vertical-align:middle;' class='<%=rowClass%>' ><%=creationDate %></td>
		                        	
		                        	
		                        	<td class='<%= rowClass %>' width="20px">&nbsp;</td> 
		                        	<td style='vertical-align:middle;' class='<%=rowClass%>' ><%=lastModificationDate %></td>
		                        	
		                        	
		                        	<td class='<%= rowClass %>' width="20px">&nbsp;</td> 
		                        	<td style='vertical-align:middle;' class='<%= rowClass %>' ><%=visib %></td>
		                        	
		                        	<td class='<%= rowClass %>' width="20px">&nbsp;</td> 
		                        	<td style='vertical-align:middle;' class='<%= rowClass %>' width="40px">
		                        		<a href="<%=execSubObjUrl%>">
		                        			<img width="20px" height="20px" 
						  	   		src='<%= urlBuilder.getResourceLink(request, "/img/exec.gif")%>' 
						  	                name='execSub' 
						  	                alt='<%=msgBuilder.getMessage(aRequestContainer, "SBIDev.docConf.execBIObjectParams.execButt", "messages")%>' 
						                        title='<%=msgBuilder.getMessage(aRequestContainer, "SBIDev.docConf.execBIObjectParams.execButt", "messages")%>' />
		                        		</a>
		                        	</td>
		                        	<%
		                        	if(owner.equals(currentUser)) {
		                        	%>
		                        	<td style='vertical-align:middle;' class='<%= rowClass %>' width="40px">
		                        		<% String eraseMsg = msgBuilder.getMessage(aRequestContainer, "ConfirmMessages.DeleteSubObject", "messages"); %>
		                        		<a href="javascript:var conf = confirm('<%=eraseMsg%>'); if(conf) {document.location='<%=deleteSubObjUrl.toString()%>';}">
		                        			<img width="20px" height="20px" 
						  	   		src='<%= urlBuilder.getResourceLink(request, "/img/erase.gif")%>' 
						  	                name='deleteSub' 
						  	                alt='<%=msgBuilder.getMessage(aRequestContainer, "SBIDev.docConf.ListdocDetParam.deleteCaption", "messages")%>' 
						                        title='<%=msgBuilder.getMessage(aRequestContainer, "SBIDev.docConf.ListdocDetParam.deleteCaption", "messages")%>' />
		                        		</a>
		                        	</td>
		                        	<%} else {%>
		                        	<td style='vertical-align:middle;' class='<%= rowClass %>' width="40px">
		                        		&nbsp;
		                        	</td>
		                        	<%} %>
		                        </tr> 
		                  <% } %>           
			  </table> 
			  <br/>	
	  <% } %>
	  
	  
	  
	  
	  
	  
	<% if( (snapshots!=null)&&(snapshots.size()!=0) ) { %>
	
			<div style='width:100%;visibility:visible;' 
					 class='UITabs' 
					 id='tabPanelWithJavascript' 
					 name='tabPanelWithJavascript'>
				<div class="first-tab-level" style="background-color:#f8f8f8">
					<div style="overflow: hidden; width:100%">
						<div class='tab'>
							<%=msgBuilder.getMessage(aRequestContainer, "SBIDev.docConf.snapshots.title","messages")%>
						</div>
					</div>
				</div>
			</div>
			
		
		    <table style='width:100%;'> 
			     <tr>
			       <td style='vertical-align:middle;' align="left" class="portlet-section-header">
			           <spagobi:message key='SBIDev.docConf.snapshots.name' />
			       </td>
			       <td align="left" class="portlet-section-header">&nbsp;</td>
			       <td style='vertical-align:middle;' align="left" class="portlet-section-header">
			           <spagobi:message key='SBIDev.docConf.snapshots.description' />
			       </td>
			       <td align="left" class="portlet-section-header">&nbsp;</td>
			       <td style='vertical-align:middle;' align="left" class="portlet-section-header">
			           <spagobi:message key='SBIDev.docConf.snapshots.dateCreation' />
			       </td>
			       <td align="left" class="portlet-section-header">&nbsp;</td>
			       <td align="left" class="portlet-section-header">&nbsp;</td>
			       <td align="left" class="portlet-section-header">&nbsp;</td>
			     <tr> 
				              
				 <% Iterator iterSnap =  snapshots.iterator();
				    BIObject.BIObjectSnapshot snap = null;
				    String nameSnap = "";
				    String descrSnap = "";
				    Date creationDate = null;
				    String execSnapUrl = null;
				    String deleteSnapUrl = null;
					boolean alternate = false;
					String rowClass = "";
					   
		            while(iterSnap.hasNext()) {
		            	snap = (BIObject.BIObjectSnapshot)iterSnap.next();
						rowClass = (alternate) ? "portlet-section-alternate" : "portlet-section-body";
						alternate = !alternate;
						nameSnap = snap.getName();
						descrSnap = snap.getDescription();
						creationDate = snap.getDateCreation();
						
						Map execSnapUrlPars = new HashMap();
						execSnapUrlPars.put("PAGE", ExecuteBIObjectModule.MODULE_PAGE);
            		    execSnapUrlPars.put(SpagoBIConstants.MESSAGEDET, SpagoBIConstants.EXEC_SNAPSHOT_MESSAGE);
            		    execSnapUrlPars.put(SpagoBIConstants.SNAPSHOT_PATH, snap.getPath());
            		   	if (executionId != null && flowId != null) {
            		   		execSnapUrlPars.put("spagobi_execution_id", executionId);
            		   		execSnapUrlPars.put("spagobi_flow_id", flowId);
            		   	}
            		    execSnapUrl = urlBuilder.getUrl(request, execSnapUrlPars);
						
            		    Map deleteSnapUrlPars = new HashMap();
            		    deleteSnapUrlPars.put("PAGE", ExecuteBIObjectModule.MODULE_PAGE);
            		    deleteSnapUrlPars.put(SpagoBIConstants.MESSAGEDET, SpagoBIConstants.ERASE_SNAPSHOT_MESSAGE);
            		    deleteSnapUrlPars.put(SpagoBIConstants.SNAPSHOT_PATH, snap.getPath());
            		    deleteSnapUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED,"true");
            		   	if (executionId != null && flowId != null) {
            		   		deleteSnapUrlPars.put("spagobi_execution_id", executionId);
            		   		deleteSnapUrlPars.put("spagobi_flow_id", flowId);
            		   	}
            		    deleteSnapUrl = urlBuilder.getUrl(request, deleteSnapUrlPars);
		                
		         %>
		         <tr class='portlet-font'>
		           	<td style='vertical-align:middle;' class='<%= rowClass %>'><%= nameSnap %></td>
		           	<td class='<%= rowClass %>' width="20px">&nbsp;</td> 
		            <td style='vertical-align:middle;' class='<%= rowClass %>' ><%=descrSnap %></td>
		            <td class='<%= rowClass %>' width="20px">&nbsp;</td> 
		            <td style='vertical-align:middle;' class='<%= rowClass %>' ><%=creationDate.toString() %></td>
		            <td class='<%= rowClass %>' width="20px">&nbsp;</td>
                    <td style='vertical-align:middle;' class='<%= rowClass %>' width="40px">
                    	<% 
                    	if (!actor.equalsIgnoreCase(SpagoBIConstants.ADMIN_ACTOR)) {
                    		%>
                    		&nbsp;
                    		<%
                    	} else {
                    	String eraseSnapMsg = msgBuilder.getMessage(aRequestContainer, "ConfirmMessages.DeleteSnapshot", "messages"); %>
                    	<a href="javascript:var conf = confirm('<%=eraseSnapMsg%>'); if(conf) {document.location='<%=deleteSnapUrl.toString()%>';}">
                    		<img width="20px" height="20px" 
                    			src='<%= urlBuilder.getResourceLink(request, "/img/erase.gif")%>' 
  	                			name='deleteSnapshot' alt='<%=msgBuilder.getMessage(aRequestContainer, "SBIDev.docConf.ListdocDetParam.deleteCaption", "messages")%>' 
                        		title='<%=msgBuilder.getMessage(aRequestContainer, "SBIDev.docConf.ListdocDetParam.deleteCaption", "messages")%>' 
                        	/>
                    	</a>
                    	 <% } %>
                    </td>
		            <td style='vertical-align:middle;' class='<%= rowClass %>' width="40px">
		                <a href="<%=execSnapUrl%>">
		                       <img width="20px" height="20px" 
						  	   		src='<%=urlBuilder.getResourceLink(request, "/img/exec.gif")%>' 
						  	        name='execSnap' 
						  	        alt='<%=msgBuilder.getMessage(aRequestContainer, "SBIDev.docConf.execBIObjectParams.execButt", "messages")%>' 
						            title='<%=msgBuilder.getMessage(aRequestContainer, "SBIDev.docConf.execBIObjectParams.execButt", "messages")%>' />
		               	</a>
		           	</td>
		         </tr> 
		         <% } %>           
			 </table> 
			 <br/>				  
	  <% } %>	  
	  
	  
	  
	  
	  
	  
 <% } %>

</div>



 		   
