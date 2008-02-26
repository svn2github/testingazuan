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
                 it.eng.spagobi.commons.constants.AdmintoolsConstants,
                 it.eng.spagobi.commons.constants.SpagoBIConstants,
                 it.eng.spagobi.commons.utilities.GeneralUtilities,
                 it.eng.spago.base.SourceBean,
                 it.eng.spago.configuration.ConfigSingleton,
                 it.eng.spagobi.analiticalmodel.document.bo.BIObject,
                 it.eng.spagobi.analiticalmodel.document.bo.Viewpoint,
                 it.eng.spago.util.StringUtils,
                 it.eng.spago.security.IEngUserProfile,
                 it.eng.spago.base.SessionContainer,                 
                 it.eng.spago.navigation.LightNavigationManager" %>
<%@ page import="java.util.Date"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.HashMap"%>
<%@page import="it.eng.spagobi.commons.utilities.ChannelUtilities"%>
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
	// get subObject List
	List subObjs = (List)moduleResponse.getAttribute(SpagoBIConstants.SUBOBJECT_LIST);
	// get snapshot List
	List snapshots = (List)moduleResponse.getAttribute(SpagoBIConstants.SNAPSHOT_LIST);
	// get viewpoint List
	List viewpoints = (List)moduleResponse.getAttribute(SpagoBIConstants.VIEWPOINT_LIST);
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
    String saveVPUrl = urlBuilder.getUrl(request, saveVPUrlPars);

    
   	// build the url for the parameters form
   	Map execUrlPars = new HashMap();
   	execUrlPars.put("PAGE", "ValidateExecuteBIObjectPage");
//   	execUrlPars.put("PAGE", ExecuteBIObjectModule.MODULE_PAGE);  
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
   		
%>

<!-- script for viewpoints management -->
<%@page import="it.eng.spagobi.analiticalmodel.document.bo.Snapshot"%>
<%@page import="it.eng.spagobi.analiticalmodel.document.bo.SubObject"%>
<script type="text/javascript">		
		var viewpointopen<%=requestIdentity%> = false;
		var winVP<%=requestIdentity%> = null;
		function opencloseViewPoint<%=requestIdentity%>() {
			if(!viewpointopen<%=requestIdentity%>){
				viewpointopen<%=requestIdentity%> = true;
				openViewPoint<%=requestIdentity%>();
			}
		}
		function openViewPoint<%=requestIdentity%>(){
			if(winVP<%=requestIdentity%>==null) {
				winVP<%=requestIdentity%> = new Window('viewPoint<%=requestIdentity%>', {className: "alphacube", 
									title:"Save Viewpoints",  resizable: true, width:550, 
									height:150, top:70, left:100, destroyOnClose: false});
         	winVP<%=requestIdentity%>.setContent('viewpointdiv<%=requestIdentity%>', false, false);
         	winVP<%=requestIdentity%>.showCenter(true);         	
		    } else {
         	winVP<%=requestIdentity%>.showCenter(true);
         	
		    }
		}
		
		observerVP<%=requestIdentity%> = { onClose: function(eventName, win) {
			if (win == winVP<%=requestIdentity%>) {
				viewpointopen<%=requestIdentity%> = false;
			}
		  }
		}
		Windows.addObserver(observerVP<%=requestIdentity%>);
		
		function saveViewpoint<%=requestIdentity%>(nameVP, descVP, scopeVP){			
		
			if (nameVP == null || nameVP.value == ""){
				alert('<%=msgBuilder.getMessage("6000", "messages", request)%>');
				return;
			}
			if (scopeVP == null || scopeVP.value == ""){
				alert('<%=msgBuilder.getMessage("6001", "messages", request)%>');
				return;
			}
		
			var mainForm = document.getElementById('paramsValueForm<%=requestIdentity%>');			
			mainForm.SUBMESSAGEDET.value = '<%=SpagoBIConstants.VIEWPOINT_SAVE%>';		
			mainForm.tmp_nameVP.value = nameVP.value;		
			mainForm.tmp_descVP.value = descVP.value;		
			mainForm.tmp_scopeVP.value = scopeVP.value;								
			mainForm.submit();
		}
		
	
Ext.onReady(function() {

	var popout_ViewPoint = Ext.get('viewpoint2div<%=requestIdentity%>');
    popout_ViewPoint.enableDisplayMode("block");
    popout_ViewPoint.toggle(true);
  
	var popout_SubObject = Ext.get('subobjectdiv<%=requestIdentity%>');
    popout_SubObject.enableDisplayMode("block");  
    popout_SubObject.toggle(true);
 
 	var popout_SnapSphot = Ext.get('snapshotsdiv<%=requestIdentity%>');
    popout_SnapSphot.enableDisplayMode("block");  
    popout_SnapSphot.toggle(true);
   

    Ext.get('toggle_ViewPoint').on('click', function(){
      
        popout_ViewPoint.toggle(true);
 
    });

    Ext.get('toggle_SubObject').on('click', function(){
      
        popout_SubObject.toggle(true);
 
    });  
    Ext.get('toggle_SnapSphot').on('click', function(){
      
        popout_SnapSphot.toggle(true);
 
    }); 
    
    
 
});	

					
</script>

<LINK rel='StyleSheet' href='<%=urlBuilder.getResourceLink(request, "css/analiticalmodel/portal_admin.css")%>' type='text/css' />
<LINK rel='StyleSheet' href='<%=urlBuilder.getResourceLink(request, "css/analiticalmodel/form.css")%>' type='text/css' />
<LINK rel='StyleSheet' href='<%=urlBuilder.getResourceLink(request, "css/analiticalmodel/table.css")%>' type='text/css' />




<form method='POST' action='<%=execUrl%>' id='paramsValueForm<%=requestIdentity%>' name='paramsValueForm'>	
	<input type='hidden' name='SUBMESSAGEDET' value='' />
	<input type='hidden' name='tmp_nameVP' value='' />
	<input type='hidden' name='tmp_descVP' value='' />
	<input type='hidden' name='tmp_scopeVP' value='' />

	
		<div class='errors-object-details-div'>
			<spagobi:error/>
		</div>

						
	<% 
	// IF NOT SINGLE OBJECT MODALITY SHOW panel selector
	if(!isSingleObjExec) {
	%>
	

<div class="slider_header">
	<ul>
	    <li class="rigthElement"><a href="<%= backUrl %>" id="back" >&nbsp;</a></li>
		<li class="arrow"><a href="#" id="toggle_ViewPoint" >&nbsp; <%=msgBuilder.getMessage("SBIDev.docConf.viewPoint.labelTogle", "messages", request)%></a></li>
		<li class="arrow"><a href="#" id="toggle_SubObject" >&nbsp; <%=msgBuilder.getMessage("SBIDev.docConf.subBIObject.labelTogle", "messages", request)%></a></li>
		<li class="arrow"><a href="#" id="toggle_SnapSphot" >&nbsp; <%=msgBuilder.getMessage("SBIDev.docConf.snapshots.labelTogle", "messages", request)%></a></li>
	</ul> 
</div> 		
	
	<% 
	// IF SINGLE OBJECT MODALITY SHOW THE PROPER TITLE BAR
	} else {
	%>	
<div class="slider_header">
	<ul>
		<li class="arrow"><a href="#" id="toggle_ViewPoint" >&nbsp; <%=msgBuilder.getMessage("SBIDev.docConf.viewPoint.labelTogle", "messages", request)%></a></li>
		<li class="arrow"><a href="#" id="toggle_SubObject" >&nbsp; <%=msgBuilder.getMessage("SBIDev.docConf.subBIObject.labelTogle", "messages", request)%></a></li>
		<li class="arrow"><a href="#" id="toggle_SnapSphot" >&nbsp; <%=msgBuilder.getMessage("SBIDev.docConf.snapshots.labelTogle", "messages", request)%></a></li>

	</ul> 
</div> 	

	<% } %>
<div class="buttons">
		<ul>
			<li><a href="javascript:document.getElementById('paramsValueForm<%=requestIdentity%>').submit()" id="v_execute_button" class="button"><b><b><b>Execute report</b></b></b></a></li>
			<li><a href="javascript:saveViewpoint<%=requestIdentity%>(document.getElementById('nameVP<%=requestIdentity%>'),document.getElementById('descVP<%=requestIdentity%>'),document.getElementById('scopeVP<%=requestIdentity%>'))" id="v_save_button" class="button v_save_button"><b><b><b>Save view point</b></b></b></a></li>
			
		</ul> 
</div>		
<div class='div_background_no_img'>

	    <spagobi:dynamicPage modality="EXECUTION_MODALITY"  requestIdentity="<%=requestIdentity%>"/>
	</div>


</form>

<!-- VIEWPOINT DIV  -->
<div id='viewpointdiv<%=requestIdentity%>' style='width:100%;display:none;'>
<table class='header-table-portlet-section'>
	<tr class='header-row-portlet-section'>
		<td class='header-title-column-portlet-section' 
		    style='vertical-align:middle;padding-left:5px;'>
			Analysis Details
		</td>		
		<td class='header-empty-column-portlet-section'>&nbsp;</td>
		<td class='header-button-column-portlet-section'>
			<a href="javascript:saveViewpoint<%=requestIdentity%>(document.getElementById('nameVP<%=requestIdentity%>'),document.getElementById('descVP<%=requestIdentity%>'),document.getElementById('scopeVP<%=requestIdentity%>'))">
                    <img width="20px" height="20px" 
	  	   		src='<%=urlBuilder.getResourceLink(request, "/img/save.png")%>' 
	  	        name='saveViewPoint' 
	  	        alt='<%=msgBuilder.getMessage("SBIDev.docConf.viewPoint.saveButt", "messages", request)%>' 
	            title='<%=msgBuilder.getMessage("SBIDev.docConf.viewPoint.saveButt", "messages", request)%>' />
		     </a>
			
		</td>
		
	</tr>
</table>
<table align="left" width="100%" cellspacing="0" border="0" id = "viewpoint_detail" >
	<tr>
		<td>
			<div class="div_detail_area_forms">
				<div class='div_detail_label'>
					<span class='portlet-form-field-label'>
						<spagobi:message key = "SBIDev.docConf.viewPoint.name" />
					</span>
				</div>
				<div class='div_detail_form'>
					<input class='portlet-form-input-field' type="text" style='width:230px;' 
							name="nameVP" id="nameVP<%=requestIdentity%>" value="" maxlength="20"/>
				</div>
				
				<div class='div_detail_label'>
					<span class='portlet-form-field-label'>
						<spagobi:message key = "SBIDev.docConf.viewPoint.description" />
					</span>
				</div>
				<div class='div_detail_form'>
					<input class='portlet-form-input-field' type="text" style='width:230px;' 
							name="descVP" id="descVP<%=requestIdentity%>" value="" maxlength="20"/>
				</div>
				
				<div class='div_detail_label'>
					<span class='portlet-form-field-label'>
						<spagobi:message key = "SBIDev.docConf.viewPoint.scope" />
					</span>
				</div>
				<div class='div_detail_form'>
					 <select id="scopeVP<%=requestIdentity%>" name="scopeVP" >
					    <option value=""/>
					    <option value="Public"  /><spagobi:message key = "SBIDev.docConf.viewPoint.scopePublic" />
					    <option value="Private" /><spagobi:message key = "SBIDev.docConf.viewPoint.scopePrivate" />
					  </select>
				</div>
			</div>
		</td>
	</tr>
</table>

</div>


<% if( ((subObjs!=null)&&(subObjs.size()!=0))  ||  ((snapshots!=null)&&(snapshots.size()!=0)) ||
		(viewpoints!=null) && viewpoints.size()!=0) { %>

<div id='subobjectdiv<%=requestIdentity%>' >		
	<% if( (subObjs!=null)&&(subObjs.size()!=0) ) { %>

			<div class="popout_selector">
				<h4 class="popout_label"><%=msgBuilder.getMessage("SBIDev.docConf.subBIObject.title", "messages", request)%></h4> 
						
		    <table style='width:100%;' align="left"> 
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
			     </tr> 
				              
				    	<% Iterator iterSubs =  subObjs.iterator();
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
					
					rowClass = (alternate) ? "portlet-section-alternate" : "portlet-section-body";
					alternate = !alternate;
					
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
		                        Map execSubObjUrlPars = new HashMap();
		                        execSubObjUrlPars.put("PAGE", ExecuteBIObjectModule.MODULE_PAGE );
		                        execSubObjUrlPars.put(SpagoBIConstants.MESSAGEDET, "EXEC_SUBOBJECT");
		                        execSubObjUrlPars.put(SpagoBIConstants.SUBOBJECT_ID, idSub);
		                        execSubObjUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED,"true");
		            		   	if (executionId != null && flowId != null) {
		            		   		execSubObjUrlPars.put("spagobi_execution_id", executionId);
		            		   		execSubObjUrlPars.put("spagobi_flow_id", flowId);
		            		   	}
		            		    execSubObjUrl = urlBuilder.getUrl(request, execSubObjUrlPars);
		                        
		            		    Map deleteSubObjUrlPars = new HashMap();
		            		    deleteSubObjUrlPars.put("PAGE", ExecuteBIObjectModule.MODULE_PAGE);
		            		    deleteSubObjUrlPars.put(SpagoBIConstants.MESSAGEDET, "DELETE_SUBOBJECT");
		            		    deleteSubObjUrlPars.put(SpagoBIConstants.SUBOBJECT_ID, idSub);
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
		                        	<%
		                        	if(owner.equals(currentUser)) {
		                        	%>
		                        	<td style='vertical-align:middle;' class='<%= rowClass %>' width="40px">
		                        		<% String eraseMsg = msgBuilder.getMessage("ConfirmMessages.DeleteSubObject", "messages", request); %>
		                        		<a href="javascript:var conf = confirm('<%=eraseMsg%>'); if(conf) {document.location='<%=deleteSubObjUrl.toString()%>';}">
		                        			<img width="20px" height="20px" 
						  	   		src='<%= urlBuilder.getResourceLink(request, "/img/erase.gif")%>' 
						  	                name='deleteSub' 
						  	                alt='<%=msgBuilder.getMessage("SBIDev.docConf.ListdocDetParam.deleteCaption", "messages", request)%>' 
						                        title='<%=msgBuilder.getMessage("SBIDev.docConf.ListdocDetParam.deleteCaption", "messages", request)%>' />
		                        		</a>
		                        	</td>
		                        	<%} else {%>
		                        	<td style='vertical-align:middle;' class='<%= rowClass %>' width="40px">
		                        		&nbsp;
		                        	</td>
		                        	<%} %>
		                        	<td style='vertical-align:middle;' class='<%= rowClass %>' width="40px">
		                        		<a href="<%=execSubObjUrl%>">
		                        			<img width="20px" height="20px" 
						  	   		src='<%= urlBuilder.getResourceLink(request, "/img/exec.gif")%>' 
						  	                name='execSub' 
						  	                alt='<%=msgBuilder.getMessage("SBIDev.docConf.execBIObjectParams.execButt", "messages", request)%>' 
						                        title='<%=msgBuilder.getMessage("SBIDev.docConf.execBIObjectParams.execButt", "messages", request)%>' />
		                        		</a>
		                        	</td>
		                        	
		                        </tr> 
		                  <% } %>           
			  </table> 
			  <br/>	
			  </div>	
	  <% } %>
</div>	  
	  
	  
 
	  
<div id='snapshotsdiv<%=requestIdentity%>' class="popout">	  
	<% if( (snapshots!=null)&&(snapshots.size()!=0) ) { %>
			<div class="popout_selector">
				<h4 class="popout_label"><%=msgBuilder.getMessage("SBIDev.docConf.snapshots.title", "messages", request)%></h4> 

		    <table style='width:100%;' align="left"> 
		    	<thead>
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
			     </tr> 
				  </thead>          
				 <% Iterator iterSnap =  snapshots.iterator();
				    Snapshot snap = null;
				    String nameSnap = "";
				    String descrSnap = "";
				    Date creationDate = null;
				    String execSnapUrl = null;
				    String deleteSnapUrl = null;
					boolean alternate = false;
					String rowClass = "";
					   
		            while(iterSnap.hasNext()) {
		            	snap = (Snapshot)iterSnap.next();
						rowClass = (alternate) ? "portlet-section-alternate" : "portlet-section-body";
						alternate = !alternate;
						nameSnap = snap.getName();
						descrSnap = snap.getDescription();
						creationDate = snap.getDateCreation();
						
						Map execSnapUrlPars = new HashMap();
						execSnapUrlPars.put("PAGE", ExecuteBIObjectModule.MODULE_PAGE);
            		    execSnapUrlPars.put(SpagoBIConstants.MESSAGEDET, SpagoBIConstants.EXEC_SNAPSHOT_MESSAGE);
            		    execSnapUrlPars.put(SpagoBIConstants.SNAPSHOT_ID, snap.getId());
            		    execSnapUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED,"true");
            		   	if (executionId != null && flowId != null) {
            		   		execSnapUrlPars.put("spagobi_execution_id", executionId);
            		   		execSnapUrlPars.put("spagobi_flow_id", flowId);
            		   	}
            		    execSnapUrl = urlBuilder.getUrl(request, execSnapUrlPars);
						
            		    Map deleteSnapUrlPars = new HashMap();
            		    deleteSnapUrlPars.put("PAGE", ExecuteBIObjectModule.MODULE_PAGE);
            		    deleteSnapUrlPars.put(SpagoBIConstants.MESSAGEDET, SpagoBIConstants.ERASE_SNAPSHOT_MESSAGE);
            		    deleteSnapUrlPars.put(SpagoBIConstants.SNAPSHOT_ID, snap.getId());
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
                    	if (!profile.isAbleToExecuteAction(SpagoBIConstants.DOCUMENT_MANAGEMENT_ADMIN)){
                    		%>
                    		&nbsp;
                    		<%
                    	} else {
                    	String eraseSnapMsg = msgBuilder.getMessage("ConfirmMessages.DeleteSnapshot", "messages", request); %>
                    	<a href="javascript:var conf = confirm('<%=eraseSnapMsg%>'); if(conf) {document.location='<%=deleteSnapUrl.toString()%>';}">
                    		<img width="20px" height="20px" 
                    			src='<%= urlBuilder.getResourceLink(request, "/img/erase.gif")%>' 
  	                			name='deleteSnapshot' alt='<%=msgBuilder.getMessage("SBIDev.docConf.ListdocDetParam.deleteCaption", "messages", request)%>' 
                        		title='<%=msgBuilder.getMessage("SBIDev.docConf.ListdocDetParam.deleteCaption", "messages", request)%>' 
                        	/>
                    	</a>
                    	 <% } %>
                    </td>
		            <td style='vertical-align:middle;' class='<%= rowClass %>' width="40px">
		                <a href="<%=execSnapUrl%>">
		                       <img width="20px" height="20px" 
						  	   		src='<%=urlBuilder.getResourceLink(request, "/img/exec.gif")%>' 
						  	        name='execSnap' 
						  	        alt='<%=msgBuilder.getMessage("SBIDev.docConf.execBIObjectParams.execButt", "messages", request)%>' 
						            title='<%=msgBuilder.getMessage("SBIDev.docConf.execBIObjectParams.execButt", "messages", request)%>' />
		               	</a>
		           	</td>
		         </tr> 
		         <% } %>           
			 </table> 
			 <br/>	
			 </div>	

	  <% } %>	  
</div>	

<div id='viewpoint2div<%=requestIdentity%>' >
	  	<% if( (viewpoints!=null)&&(viewpoints.size()!=0) ) { %>
	  	

				<h4 class="popout_label"><%=msgBuilder.getMessage("SBIDev.docConf.viewPoint.title", "messages", request)%></h4> 
				
	
		
		    <table style='width:100%;' align="left">    		         
			     <tr>
			       <td style='vertical-align:middle;' align="left" class="portlet-section-header">
			           <spagobi:message key='SBIDev.docConf.viewPoint.name' />
			       </td>
   			       <td align="left" class="portlet-section-header">&nbsp;</td>
			       <td style='vertical-align:middle;' align="left" class="portlet-section-header">
			           <spagobi:message key='SBIDev.docConf.viewPoint.owner' />
			       </td>
			       <td align="left" class="portlet-section-header">&nbsp;</td>
			       <td style='vertical-align:middle;' align="left" class="portlet-section-header">
			           <spagobi:message key='SBIDev.docConf.viewPoint.description' />
			       </td>
			       <td align="left" class="portlet-section-header">&nbsp;</td>
			       <td style='vertical-align:middle;' align="left" class="portlet-section-header">
			           <spagobi:message key='SBIDev.docConf.viewPoint.scope' />
			       </td>
			       <td align="left" class="portlet-section-header">&nbsp;</td>
			       <td style='vertical-align:middle;' align="left" class="portlet-section-header">
			           <spagobi:message key='SBIDev.docConf.viewPoint.dateCreation' />
			       </td>
			       <td align="left" class="portlet-section-header">&nbsp;</td>
			       <td align="left" class="portlet-section-header">&nbsp;</td>
			       <td align="left" class="portlet-section-header">&nbsp;</td>
			     </tr> 
				              
				 <% Iterator iterVP =  viewpoints.iterator();
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
						
						Map execVPUrlPars = new HashMap();
						execVPUrlPars.put("PAGE", ExecuteBIObjectModule.MODULE_PAGE);
						execVPUrlPars.put(SpagoBIConstants.MESSAGEDET, SpagoBIConstants.VIEWPOINT_EXEC);	
						execVPUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED,"true");						
						execVPUrlPars.put("content", vp.getVpValueParams());
						execVPUrlPars.put("vpId",vp.getVpId());
            		   	if (executionId != null && flowId != null) {
            		   		execVPUrlPars.put("spagobi_execution_id", executionId);
            		   		execVPUrlPars.put("spagobi_flow_id", flowId);
            		   	}
            		    execVPUrl = urlBuilder.getUrl(request, execVPUrlPars);
						
            		    Map deleteVPUrlPars = new HashMap();
            		    deleteVPUrlPars.put("PAGE", ExecuteBIObjectModule.MODULE_PAGE);
            		    deleteVPUrlPars.put(SpagoBIConstants.MESSAGEDET, SpagoBIConstants.VIEWPOINT_ERASE);
            		    deleteVPUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED,"true");
            		    deleteVPUrlPars.put("vpId",vp.getVpId());
            		   	if (executionId != null && flowId != null) {
            		   		deleteVPUrlPars.put("spagobi_execution_id", executionId);
            		   		deleteVPUrlPars.put("spagobi_flow_id", flowId);
            		   	}
            		    deleteVPUrl = urlBuilder.getUrl(request, deleteVPUrlPars);

            		    Map viewVPUrlPars = new HashMap();
            		    viewVPUrlPars.put("PAGE", ExecuteBIObjectModule.MODULE_PAGE);
            		    viewVPUrlPars.put(SpagoBIConstants.MESSAGEDET, SpagoBIConstants.VIEWPOINT_VIEW);
            		    viewVPUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED,"true");
            		    viewVPUrlPars.put("vpId",vp.getVpId());
            		   	if (executionId != null && flowId != null) {
            		   		viewVPUrlPars.put("spagobi_execution_id", executionId);
            		   		viewVPUrlPars.put("spagobi_flow_id", flowId);
            		   	}
            		    viewVPUrl = urlBuilder.getUrl(request, viewVPUrlPars);

            		    String tmpContent = (contentVP==null)?"":contentVP.replace("&amp;"," - ");            		    
            	        ConfigSingleton conf = ConfigSingleton.getInstance();
            		    SourceBean formatSB = (SourceBean) conf.getAttribute("DATA-ACCESS.DATE-FORMAT");
            		    String format = (String) formatSB.getAttribute("format");
            		    format = format.replaceAll("D", "d");
            		    format = format.replaceAll("m", "M");
            		    format = format.replaceAll("Y", "y");
            		    String date = StringUtils.dateToString(creationDateVP, format);
		                
		         %>
		         <tr class='portlet-font'> 
		           	<td style='vertical-align:middle;' class='<%= rowClass %>'><%= nameVP %></td>
		           	<td class='<%= rowClass %>' width="20px">&nbsp;</td> 
		           	<td style='vertical-align:middle;' class='<%= rowClass %>'><%= ownerVP %></td>
		           	<td class='<%= rowClass %>' width="20px">&nbsp;</td> 		           	
		            <td style='vertical-align:middle;' class='<%= rowClass %>' ><%=descrVP %></td>
		            <td class='<%= rowClass %>' width="20px">&nbsp;</td> 
		            <td style='vertical-align:middle;' class='<%= rowClass %>' ><%=scopeVP %></td>
		            <td class='<%= rowClass %>' width="20px">&nbsp;</td> 		            
		            <td style='vertical-align:middle;' class='<%= rowClass %>' ><%=date %></td>
					<td style='vertical-align:middle;' class='<%= rowClass %>' width="40px">
                    	<a href="javascript:document.location='<%=viewVPUrl.toString()%>';">
                    		<img width="20px" height="20px" 
                    			src='<%= urlBuilder.getResourceLink(request, "/img/notes.jpg")%>' 
  	                			name='getViewpoint' alt='<%=msgBuilder.getMessage("SBIDev.docConf.viewPoint.viewButt", "messages", request)%>' 
                        		title='<%=msgBuilder.getMessage("SBIDev.docConf.viewPoint.viewButt", "messages", request)%>' 
                        	/>
                    	</a>
                    </td>		            
                   <td style='vertical-align:middle;' class='<%= rowClass %>' width="40px">
                   <%if(ownerVP.equals(currentUser)) { %>                     
                    	<% 
                    	String eraseVPMsg = msgBuilder.getMessage("ConfirmMessages.DeleteViewpoint", "messages", request); %>
                    	<a href="javascript:var conf = confirm('<%=eraseVPMsg%>'); if(conf) {document.location='<%=deleteVPUrl.toString()%>';}">
                    		<img width="20px" height="20px" 
                    			src='<%= urlBuilder.getResourceLink(request, "/img/erase.gif")%>' 
  	                			name='deleteViewpoint' alt='<%=msgBuilder.getMessage("SBIDev.docConf.ListdocDetParam.deleteCaption", "messages", request)%>' 
                        		title='<%=msgBuilder.getMessage("SBIDev.docConf.ListdocDetParam.deleteCaption", "messages", request)%>' 
                        	/>
                    	</a>
                    <% }%>
                    </td>
		            <td style='vertical-align:middle;' class='<%= rowClass %>' width="40px">
		                <a href="<%=execVPUrl%>">
		                       <img width="20px" height="20px" 
						  	   		src='<%=urlBuilder.getResourceLink(request, "/img/exec.gif")%>' 
						  	        name='execSnap' 
						  	        alt='<%=msgBuilder.getMessage("SBIDev.docConf.execBIObjectParams.execButt", "messages", request)%>' 
						            title='<%=msgBuilder.getMessage("SBIDev.docConf.execBIObjectParams.execButt", "messages", request)%>' 
						       />
		               	</a>
		           	</td>
		         </tr> 
		         <% } %>           
			 </table> 
			 <br/>	
		  
	  <% } %>
	   
</div>	  

 <% } %>
 		   