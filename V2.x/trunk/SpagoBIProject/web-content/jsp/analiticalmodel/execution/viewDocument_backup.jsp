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
                 java.util.Iterator,
                 it.eng.spagobi.commons.constants.ObjectsTreeConstants,
                 it.eng.spagobi.analiticalmodel.document.service.ExecuteBIObjectModule,
                 it.eng.spagobi.commons.utilities.GeneralUtilities,
                 it.eng.spago.base.SourceBean,
                 it.eng.spagobi.analiticalmodel.document.bo.BIObject,
                 it.eng.spagobi.analiticalmodel.document.bo.Viewpoint,
                 it.eng.spago.util.StringUtils,
                 it.eng.spago.security.IEngUserProfile,
                 it.eng.spago.base.SessionContainer,                 
                 it.eng.spago.navigation.LightNavigationManager" %>
<%@ page import="java.util.Date"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.HashMap"%>
<%@page import="org.safehaus.uuid.UUIDGenerator"%>
<%@page import="org.safehaus.uuid.UUID"%>
<%@page import="it.eng.spagobi.analiticalmodel.document.bo.Snapshot"%>
<%@page import="it.eng.spagobi.analiticalmodel.document.bo.SubObject"%>

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
	// get subObject List
	List subObjs = (List)moduleResponse.getAttribute(SpagoBIConstants.SUBOBJECT_LIST);
	// get snapshot List
	List snapshots = (List)moduleResponse.getAttribute(SpagoBIConstants.SNAPSHOT_LIST);
	// get viewpoint List
	List viewpoints = (List)moduleResponse.getAttribute(SpagoBIConstants.VIEWPOINT_LIST);
	
	
	// try to get the "NO_PARAMETERS", if it is present the object has only subobjects but not
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
    
   	// build the url for the save-parameters form as view-point   	
   	Map saveVPUrlPars = new HashMap();
   	saveVPUrlPars.put("PAGE", "ValidateExecuteBIObjectPage");
   	saveVPUrlPars.put(SpagoBIConstants.MESSAGEDET, SpagoBIConstants.VIEWPOINT_SAVE);
   	saveVPUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");   	
   	saveVPUrlPars.put("parameters", obj.getBiObjectParameters());    	
   	if (executionId != null && flowId != null) {
   		saveVPUrlPars.put("spagobi_execution_id", executionId);
   		saveVPUrlPars.put("spagobi_flow_id", flowId);
   	}

    
   	// build the url for the parameters form
   	Map execUrlPars = new HashMap();
   	execUrlPars.put("PAGE", "ValidateExecuteBIObjectPage");  
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
    backUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_BACK_TO, "1");
   	if (executionId != null && flowId != null) {
   		backUrlPars.put("spagobi_execution_id", executionId);
   		backUrlPars.put("spagobi_flow_id", flowId);
   	}
    String backUrl = urlBuilder.getUrl(request, backUrlPars);
   
    String save=urlBuilder.getResourceLink(request, "/servlet/AdapterHTTP?ACTION_NAME=SAVE_OBJECT&NEW_SESSION=TRUE&op=a");
    String read=urlBuilder.getResourceLink(request, "/servlet/AdapterHTTP?ACTION_NAME=SAVE_OBJECT&NEW_SESSION=TRUE&op=b");
    String saveJS=urlBuilder.getResourceLink(request, "js/analiticalmodel/save_viewpoint.js");
%>

<script type="text/javascript">
<!--
	var readUrl='<%=read%>';
	var saveUrl='<%=save%>';
	var saveJSUrl='<%=saveJS%>';
	

	
//-->
</script>

<LINK rel='StyleSheet' href='<%=urlBuilder.getResourceLink(request, "css/analiticalmodel/portal_admin.css")%>' type='text/css' />
<LINK rel='StyleSheet' href='<%=urlBuilder.getResourceLink(request, "css/analiticalmodel/form.css")%>' type='text/css' />
<LINK rel='StyleSheet' href='<%=urlBuilder.getResourceLink(request, "css/analiticalmodel/table.css")%>' type='text/css' />


<script type="text/javascript" src="<%=urlBuilder.getResourceLink(request, "jsp/analiticalmodel/execution/viewDocument.js")%>"></script>

<table id="container" border='0'>
<tr>
<td>
		<div id="main_content">
		<!-- Report Toolbar -->
			<div id="horizontalNavigation">
				<div class="slider_header">
					<ul>
					    <li class="arrow"><a href="#" id="toggle_Parameters" >&nbsp;Input Parameters</a></li>
						<li class="arrow"><a href="#" id="toggle_ViewPoint" >&nbsp; Viewpoint</a></li>
						<li class="arrow"><a href="#" id="toggle_SubObject" >&nbsp; Subobject</a></li>
						<li class="arrow"><a href="#" id="toggle_SnapSphot" >&nbsp; SnapSphot</a></li>

					</ul> 
				</div> 

				
				<div class="clear"></div>
				
		<!-- ViewPoint -->
				<div id="popout_ViewPoint" class="popout">
					<div class="popout_selector">
						<h4 class="popout_label"><%=msgBuilder.getMessage("SBIDev.docConf.viewPoint.title", "messages", request)%></h4> 
							<table id="ToSort">
								<thead>
									<tr>
										<th><spagobi:message key='SBIDev.docConf.viewPoint.name' /></th>
										<th><spagobi:message key='SBIDev.docConf.viewPoint.owner' /></th>
										<th><spagobi:message key='SBIDev.docConf.viewPoint.description' /></th>
										<th><spagobi:message key='SBIDev.docConf.viewPoint.scope' /></th>
										<th><spagobi:message key='SBIDev.docConf.viewPoint.dateCreation' /></th>
									</tr>
								</thead>
								<tbody>
				 <% 
				 	if (viewpoints!=null ){
				 
				    Iterator iterVP =  viewpoints.iterator();
				    Viewpoint vp = null;
				    String ownerVP = "";				    
				    String nameVP = "";
				    String descrVP = "";
				    String scopeVP = "";
				    String contentVP = "";
				    Date creationDateVP = null;
				    String execVPUrl = null;
				    String deleteVPUrl = null;
				    String viewVPUrl = null;				    				   
					boolean alternate = false;
					String rowClass = "";
					   
		            while(iterVP.hasNext()) {
		            	vp = (Viewpoint)iterVP.next();
						rowClass = (alternate) ? "portlet-section-alternate" : "portlet-section-body";
						alternate = !alternate;
						nameVP = vp.getVpName();
						ownerVP = vp.getVpOwner();
						descrVP = vp.getVpDesc();
						scopeVP = vp.getVpScope();
						contentVP = vp.getVpValueParams();
						creationDateVP = vp.getVpCreationDate();
						


            		    String tmpContent = (contentVP==null)?"":contentVP.replace("&amp;"," - ");            		    
            	        ConfigSingleton conf = ConfigSingleton.getInstance();
            		    SourceBean formatSB = (SourceBean) conf.getAttribute("DATA-ACCESS.DATE-FORMAT");
            		    String format = (String) formatSB.getAttribute("format");
            		    format = format.replaceAll("D", "d");
            		    format = format.replaceAll("m", "M");
            		    format = format.replaceAll("Y", "y");
            		    String date = StringUtils.dateToString(creationDateVP, format);
		                
		         %>								
									  <tr>
										<td><%= nameVP %></td>
										<td><%= ownerVP %></td>
										<td><%=descrVP %></td>
										<td><%=scopeVP %></td>
										<td><%=date %></td>
									 </tr>
<%
		            } // while
				 	} // if
%>
							 	</tbody>
							 </table>
					</div>					
				</div>
				
				<div class="clear"></div>
				
			<!-- Parameters -->
				<div id="popout_Parameters" class="popout">
					<div class="popout_selector">
						<h4 class="popout_label">Input parameters:</h4> 
							<div class="buttons"> 
									<ul>
										<li><a href="#" id="p_execute_button" class="button p_execute_button"><b><b><b>Execute</b></b></b></a></li>
										<li><a href="#" id="p_save_button"    class="button p_save_button"><b><b><b>Save</b></b></b></a></li>
									</ul>
							</div>
					</div>
					<div class="form">
					    <spagobi:ParametersGenerator modality="EXECUTION_MODALITY"  requestIdentity="<%=requestIdentity%>"/>
					
						<div class='errors-object-details-div'>
							<spagobi:error/>
						</div>  
					</div>
				</div>
				
								<div class="clear"></div>
		<!-- SubObject -->
				<div id="popout_SubObject" class="popout">
					<div class="popout_selector">
						<h4 class="popout_label"><%=msgBuilder.getMessage("SBIDev.docConf.subBIObject.title", "messages", request)%></h4> 
							<table id="ToSort">
								<thead>
									<tr>
										<th><spagobi:message key='SBIDev.docConf.subBIObject.name' /></th>
										<th><spagobi:message key='SBIDev.docConf.subBIObject.owner' /></th>
										<th><spagobi:message key='SBIDev.docConf.subBIObject.description' /></th>
										<th><spagobi:message key='SBIDev.docConf.subBIObject.creationDate' /></th>
										<th><spagobi:message key='SBIDev.docConf.subBIObject.lastModificationDate' /></th>
										<th><spagobi:message key='SBIDev.docConf.subBIObject.visibility' /></th>										
									</tr>
								</thead>
								<tbody>	
								<%
								if (subObjs!=null) {
								Iterator iterSubs =  subObjs.iterator();
							    	   SubObject subObj = null;
							    	   Integer idSub = null;
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
					    	                subObj = (SubObject)iterSubs.next();
										
											idSub = subObj.getId();
											nameSub = subObj.getName();
					                        descr = subObj.getDescription();
					                        owner = subObj.getOwner();
					                        creationDate = subObj.getCreationDate().toString();
					                        lastModificationDate = subObj.getLastChangeDate().toString();
					                        
					                        visib = "Private";
					                        if(subObj.getIsPublic().booleanValue()) {
					                        	visib = "Public";
					                        } 
					                        if(owner.equals(currentUser)) {
					                        	delete = "delete";
					                        }

								%>	
									  <tr>
									    <td><%= nameSub %></td>
										<td><%= owner %></td>
										<td><%= descr %></td>
										<td><%=creationDate %></td>
										<td><%=lastModificationDate %></td>
										<td><%=visib %></td>
									 </tr>	
								<%
					                   }
								}
								
								%>	
								</tbody>
							 </table>
					</div>	
				</div>
				
									
		<!-- SnapShot -->
				<div id="popout_SnapSphot" class="popout">
					<div class="popout_selector">
						<h4 class="popout_label"><%=msgBuilder.getMessage("SBIDev.docConf.snapshots.title", "messages", request)%></h4> 
							<table id="ToSort">
								<thead>
									<tr>
										<th><spagobi:message key='SBIDev.docConf.snapshots.name' /></th>
										<th><spagobi:message key='SBIDev.docConf.snapshots.description' /></th>
										<th><spagobi:message key='SBIDev.docConf.snapshots.dateCreation' /></th>									
									</tr>
								</thead>
								<tbody>	
								<%
								if (snapshots!=null) {
								    Iterator iterSnap =  snapshots.iterator();
								    Snapshot snap = null;
								    String nameSnap = "";
								    String descrSnap = "";
								    Date creationDate = null;

								   
								            while(iterSnap.hasNext()) {
								            	snap = (Snapshot)iterSnap.next();
												nameSnap = snap.getName();
												descrSnap = snap.getDescription();
												creationDate = snap.getDateCreation();

								%>	
									  <tr>
									    <td><%= nameSnap %></td>
										<td><%= descrSnap %></td>
										<td><%= creationDate.toString() %></td>

									 </tr>	
								<%
					                   }
								}
								
								%>	
								</tbody>
							 </table>
					</div>	
				</div>
				
			
		<!--/Report Toolbar -->
	</div><!--/main_content--> 
		
		<div class="clear"></div>
			
			
		<!-- Document Toolbar  -->
				<div id="Title"> 
					<div id="report_header"> 		
							<h1 class="title"><%=obj.getDescription()%></h1>					
					</div> 
				</div>
					<div class="clear"></div>	
						<div class="alert">
							<h4>info:</h4>
								<ul><li><a href="#">3 comments</a></li></ul>
						</div>					
				<div id="ControlPanel"> 
					 <div class="buttons"> 
								<ul>
									<li><a href="#" id="v_refresh_button" class="button"><b><b><b>Refresh</b></b></b></a></li>
									<li><a href="#" id="v_save_button" class="button v_save_button"><b><b><b>Save</b></b></b></a></li>
									<li><a href="#" id="v_send_button" class="button v_send_button"><b><b><b>Send</b></b></b></a></li>
									<li><a href="#" id="v_publish_button" class="button v_publish_button"><b><b><b>Publish</b></b></b></a></li>
									<li><a href="#" id="v_note_button" class="button v_note_button"><b><b><b>Note</b></b></b></a></li>
									<li><a href="#" id="v_meta_button" class="button"><b><b><b>Meta</b></b></b></a></li>
								</ul> 
					 </div> 
				</div>
			<!-- / Document Toolbar  -->
</td>
</tr>
<tr>
<td>	
<div id='documentContainer'>
</div>
</td>
</tr>
</table>

	
