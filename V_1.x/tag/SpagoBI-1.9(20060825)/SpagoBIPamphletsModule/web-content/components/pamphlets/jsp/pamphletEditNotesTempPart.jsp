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

<%@ page import="javax.portlet.PortletURL,
				it.eng.spago.navigation.LightNavigationManager,
				it.eng.spagobi.pamphlets.constants.PamphletsConstants,
				java.util.List,
				java.util.Iterator,
				it.eng.spagobi.pamphlets.bo.Pamphlet,
				it.eng.spagobi.bo.Role,
				it.eng.spagobi.pamphlets.bo.ConfiguredBIDocument,
				java.util.Map,
				java.util.Set,
				it.eng.spago.base.SessionContainer,
				it.eng.spago.base.ApplicationContainer,
				it.eng.spago.security.IEngUserProfile,
				it.eng.spago.workflow.api.IWorkflowEngine,
				it.eng.spago.workflow.api.IWorkflowConnection,
				it.eng.spago.workflow.api.IWorkflowAssignment,
				it.eng.spago.configuration.ConfigSingleton,
				it.eng.spago.base.SourceBean,
				java.io.File,
				it.eng.spagobi.pamphlets.dao.IPamphletsCmsDao,
				it.eng.spagobi.pamphlets.dao.PamphletsCmsDaoImpl,
				java.util.HashMap,
				java.io.FileOutputStream,
				it.eng.spagobi.utilities.GeneralUtilities,
				it.eng.spagobi.utilities.SpagoBITracer,
				it.eng.spago.error.EMFUserError,
				it.eng.spago.error.EMFErrorSeverity,
				it.eng.spago.error.EMFErrorHandler,
				it.eng.spagobi.constants.SpagoBIConstants" %>

<%  
    String activityKey = null;
    String pathTmpFold = null;
    Map imageurl = null;
    String notes = null;
    Iterator iterImgs = null;
	PortletURL backUrl = renderResponse.createActionURL();
    PortletURL saveNoteUrl = renderResponse.createActionURL();
    PortletURL closeNoteUrl = renderResponse.createActionURL();
    boolean logicExecuted = true;

	try{	
		// get activity key
   		activityKey = (String)aServiceResponse.getAttribute("ActivityKey");   
    	if(activityKey==null){
    		SourceBean moduleResponse = (SourceBean)aServiceResponse.getAttribute(PamphletsConstants.PAMPHLET_COLLABORATION_MODULE); 
    		activityKey = (String)moduleResponse.getAttribute("ActivityKey");   
    	}
        // get user profile
		ApplicationContainer applicationCont = ApplicationContainer.getInstance();
		IEngUserProfile userProfile = (IEngUserProfile)aSessionContainer.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
		// get workflow engine and connect
		IWorkflowEngine wfEngine = (IWorkflowEngine)applicationCont.getAttribute("WfEngine");
		IWorkflowConnection wfConnection = wfEngine.getWorkflowConnection();
		wfConnection.open((String)userProfile.getUserUniqueIdentifier(), (String)userProfile.getUserAttribute("password"));
		// get user assignments
		IWorkflowAssignment wfAssignment = wfConnection.getWorkflowAssignment(activityKey);
		// get parameters
		Map parameters = wfAssignment.getMappedParameters();
		Map context = wfAssignment.getContext();
		String pathPamphlet = (String)context.get("PathPamphlet");
	    String indPart = (String)parameters.get("indexPart");
		// get temp directory for the pamphlet module
	    ConfigSingleton configSing = ConfigSingleton.getInstance();
		SourceBean pathTmpFoldSB = (SourceBean)configSing.getAttribute("PAMPHLETS.PATH_TMP_FOLDER");
		pathTmpFold = (String)pathTmpFoldSB.getAttribute("path");
		File tempDir = new File(pathTmpFold); 
		tempDir.mkdirs();
		// get images stored into cms pamphlet part
		IPamphletsCmsDao pampdao = new PamphletsCmsDaoImpl();
		Map images = pampdao.getImagesOfTemplatePart(pathPamphlet, indPart);
	    byte[] notesByte = 	pampdao.getNotesTemplatePart(pathPamphlet, indPart);
	    notes = new String(notesByte);
	    // for each image store into the temp directory and save the url useful to recover it into the map
	    imageurl = new HashMap();
	    iterImgs = images.keySet().iterator();
	    while(iterImgs.hasNext()){
	    	String logicalName = (String)iterImgs.next();
	    	String logicalNameForStoring = pathPamphlet.replace('/', '_') + logicalName + ".jpg";
	    	byte[] content = (byte[])images.get(logicalName);
	    	File img = new File(tempDir, logicalNameForStoring);
	    	FileOutputStream fos = new FileOutputStream(img);
	    	fos.write(content);
	    	fos.flush();
	    	fos.close();
	    	// the url to recover the image is a spagobi servlet url
	    	String contextAddress = GeneralUtilities.getSpagoBiContextAddress();
	    	String recoverUrl = contextAddress + "/PamphletsImageService?task=getTemplateImage&pathimg=" + 
	    						pathTmpFold + "/" + logicalNameForStoring;
	    	imageurl.put(logicalName, recoverUrl); 
	    }
	   
	    // add parameters to back url
	    backUrl.setParameter("PAGE", PamphletsConstants.PAMPHLET_COLLABORATION_PAGE);
	    backUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_RESET, "true");
	
	    // add parameters to save url
	    saveNoteUrl.setParameter("PAGE", PamphletsConstants.PAMPHLET_COLLABORATION_PAGE);
	    saveNoteUrl.setParameter("OPERATION", PamphletsConstants.OPERATION_SAVE_NOTE);
	    saveNoteUrl.setParameter(PamphletsConstants.PATH_PAMPHLET, pathPamphlet);
	    saveNoteUrl.setParameter(PamphletsConstants.PAMPHLET_PART_INDEX, indPart);
	    saveNoteUrl.setParameter("ActivityKey", activityKey);
	    saveNoteUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");	
	      
	    // add parameter to close url
	    closeNoteUrl.setParameter("ACTION_NAME", "COMPLETE_OR_REJECT_ACTIVITY_ACTION");
	    closeNoteUrl.setParameter("CompleteActivity", "TRUE");
	    closeNoteUrl.setParameter("ActivityKey", activityKey);
	    closeNoteUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");	
	    
	    
	    
   	} catch (Exception e) {
   		logicExecuted = false;
   		EMFErrorHandler errorHandler =  aResponseContainer.getErrorHandler();
   		EMFUserError error = new EMFUserError(EMFErrorSeverity.ERROR, 100);
   		errorHandler.addError(error);
   	    SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, "jsp pamphletEditNotesTempPart",
   	    					"", "error while getting and storing images and notes", e);
   	}
    
%>




<table class='header-table-portlet-section'>
	<tr class='header-row-portlet-section'>
		<td class='header-title-column-portlet-section' style='vertical-align:middle;padding-left:5px;'>
			<spagobi:message key = "pamp.editnotes"  bundle="component_pamphlets_messages"/>
		</td>
		<td class='header-empty-column-portlet-section'>&nbsp;</td>
		<td class='header-button-column-portlet-section'>
			<a href='<%= backUrl.toString() %>'> 
      			<img class='header-button-image-portlet-section' 
      				 title='<spagobi:message key = "pamp.back" bundle="component_pamphlets_messages" />' 
      				 src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/components/pamphlets/img/back.png")%>' 
      				 alt='<spagobi:message key = "pamp.back"  bundle="component_pamphlets_messages"/>' />
			</a>
		</td>
		
		<td class='header-empty-column-portlet-section'>&nbsp;</td>
		<td class='header-button-column-portlet-section'>
			<a href="javascript:document.getElementById('formNotes').submit();"> 
      			<img class='header-button-image-portlet-section' 
      				 title='<spagobi:message key = "pamp.save" bundle="component_pamphlets_messages" />' 
      				 src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/components/pamphlets/img/save32.png")%>' 
      				 alt='<spagobi:message key = "pamp.save"  bundle="component_pamphlets_messages"/>' />
			</a>
		</td>

		<td class='header-empty-column-portlet-section'>&nbsp;</td>
		<td class='header-button-column-portlet-section'>
			<a href='<%=closeNoteUrl.toString()%>'> 
      			<img class='header-button-image-portlet-section' 
      				 title='<spagobi:message key = "pamp.closeDiscussion" bundle="component_pamphlets_messages" />' 
      				 src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/components/pamphlets/img/closeNotes32.png")%>' 
      				 alt='<spagobi:message key = "pamp.closeDiscussion"  bundle="component_pamphlets_messages"/>' />
			</a>
		</td>
		
	</tr>
</table>



<br/>

	
<spagobi:error/>	





	
<% if(logicExecuted) { %>
	
	<script>
			
			var tabs = new Array(<%=(imageurl.size() + 1)%>);
			<%
				iterImgs = imageurl.keySet().iterator();
				int prog = 0;
				while(iterImgs.hasNext()){
					String nameImg = (String)iterImgs.next();
			%>
					tabs[<%=prog%>]="tab<%=nameImg%>";
			<%
				prog ++;
				}
			%>
			tabs[<%=prog%>]="tabNote";
			
			
			var divs = new Array(<%=(imageurl.size() + 1)%>);
			<%
				iterImgs = imageurl.keySet().iterator();
				prog = 0;
				while(iterImgs.hasNext()){
					String nameImg = (String)iterImgs.next();
			%>
					divs[<%=prog%>]="div<%=nameImg%>";
			<%
				prog ++;
				}
			%>
			divs[<%=prog%>]="divNote";
			
			
			var selectedName = "Note"
			
			
		function changeTab(name) {
			for(i=0; i<divs.length; i++) {
			    completeTabName = tabs[i];
				completeDivName = divs[i];
				divobj = document.getElementById(completeDivName);
				tabobj = document.getElementById(completeTabName);
				divName = completeDivName.substring(3);
				tabName = completeTabName.substring(3);
				if(divName==name){
					divobj.style.display='inline';
				} else {
				  divobj.style.display='none';
				}
				if(tabName==name){
					tabobj.className='tab selected';
				} else {
				  tabobj.className='tab';
				}
			}
		}
	
	</script>
	
	
	
	<!-- ************************ START BUILT TABS ************** -->
	
	<div style='width:100%;' class='UITabs'>
		<div class="first-tab-level" style="background-color:#f8f8f8">
			<div style="overflow: hidden; width:  100%">
				
			<%
				iterImgs = imageurl.keySet().iterator();
				while(iterImgs.hasNext()){
					String nameImg = (String)iterImgs.next();
					String linkClass = "tab";
			%>
		
				<div id='tab<%=nameImg%>' class='<%=linkClass%>'>
					<a href="javascript:changeTab('<%=nameImg%>')" style="color:black;"> <%=nameImg%> </a>
				</div>
		
			<%	
				}
			%>
						
				<div id='tabNote' class='tab selected'>
					<a href="javascript:changeTab('Note')" style="color:black;">Note</a>
				</div>	
				
			</div>
		</div>
	</div>
	
	<!-- ************************ END BUILT TABS ************** -->
	
		
		
		
		
		
		
	

	<!-- ************************ START BUILT DIVS IMAGE AND NOTE ************** -->	
		
	<div style="width:100%;background-color:#f8f8f8;border:1 solid black;">
	<br/>	
	<%
		iterImgs = imageurl.keySet().iterator();
		while(iterImgs.hasNext()){
			String nameImg = (String)iterImgs.next();
			String url = (String)imageurl.get(nameImg);
	%>
		<div style="display:none;" name="div<%=nameImg%>" id="div<%=nameImg%>">
			<center>
				<img src="<%=url%>" />
			</center>
			<br/>
			<br/>
		</div>
	<%
		}
	%>
	    <div style="display:inline;" name="divNote" id="divNote" >
			<div name="notesdiv" id="notesdiv" >
				<form method="POST" id="formNotes" action="<%=saveNoteUrl.toString()%>" >
				<center>
					<b><spagobi:message key = "pamp.notes"  bundle="component_pamphlets_messages"/></b>
					<br/>
					<textarea name="notes" style="width:1000px;height:350px;"><%=notes%></textarea>
				<center>
				</form>
			</div>
			<br/>
			<br/>
			
		
		</div>
	
	
	<!-- ************************ START BUILT DIVS IMAGE AND NOTE ************** -->	
	
	</div>


<% } %>








