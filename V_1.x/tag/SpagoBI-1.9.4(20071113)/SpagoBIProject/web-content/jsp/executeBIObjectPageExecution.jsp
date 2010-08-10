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

<%@ include file="/jsp/shareProfile.jsp"%>

<%@ page import="it.eng.spagobi.bo.BIObject,
                 java.util.List,
                 it.eng.spagobi.constants.ObjectsTreeConstants,
                 java.util.Iterator,
                 it.eng.spagobi.bo.Engine,
                 it.eng.spagobi.bo.Domain,
                 it.eng.spagobi.bo.BIObjectParameter,
                 it.eng.spagobi.bo.dao.IDomainDAO,
                 it.eng.spagobi.bo.dao.DAOFactory,
                 it.eng.spagobi.constants.SpagoBIConstants,
                 it.eng.spagobi.services.modules.BIObjectsModule,
                 it.eng.spagobi.services.modules.ExecuteBIObjectModule,
                 it.eng.spago.navigation.LightNavigationManager,
                 java.util.Map,
                 java.util.HashMap,
                 it.eng.spagobi.utilities.GeneralUtilities,
                 it.eng.spagobi.managers.BIObjectNotesManager,
                 it.eng.spago.base.SessionContainer,
                 it.eng.spago.security.IEngUserProfile,
                 it.eng.spagobi.utilities.SpagoBITracer,
                 it.eng.spagobi.utilities.ChannelUtilities,
                 it.eng.spagobi.bo.dao.audit.AuditManager,
                 it.eng.spagobi.managers.ExecutionManager,
                 it.eng.spagobi.managers.ExecutionManager.ExecutionInstance,
                 it.eng.spagobi.drivers.IEngineDriver" %>

<%

    // get module response
    SourceBean moduleResponse = (SourceBean)aServiceResponse.getAttribute("ExecuteBIObjectModule");

	String executionId = (String) aServiceRequest.getAttribute("spagobi_execution_id");
	if (executionId == null) executionId = (String) moduleResponse.getAttribute("spagobi_execution_id");
	String flowId = (String) aServiceRequest.getAttribute("spagobi_flow_id");
	if (flowId == null) flowId = "";
	
	ExecutionManager executionManager = ExecutionManager.getInstance();
	ExecutionManager.ExecutionInstance instance = executionManager.getExecution(executionId);
	boolean isDrillRequest = false;
	boolean isRefreshRequest = false;
	if (instance != null) {
		flowId = instance.getFlowId();
	}
	String lastExecutionId = executionManager.getLastExecutionId(flowId);
	if (lastExecutionId != null && !lastExecutionId.equalsIgnoreCase(flowId)) {
		isDrillRequest = true;
	}
	if (isDrillRequest && lastExecutionId !=null && !lastExecutionId.equalsIgnoreCase(executionId)) {
		isRefreshRequest = true;
	}	

	// get the BiObject
	BIObject obj = null;
	if (isRefreshRequest) {
		obj = executionManager.getLastExecutionObject(flowId);
	} else {
	    obj = (BIObject)moduleResponse.getAttribute(ObjectsTreeConstants.SESSION_OBJ_ATTR);
	}
	// get the url of the engine
	Engine engine = obj.getEngine();
    String engineurl = engine.getUrl();
    
   	// get the actor
    String actor = (String)aSessionContainer.getAttribute(SpagoBIConstants.ACTOR);

   	// get the user profile from session
	SessionContainer permSession = aSessionContainer.getPermanentContainer();
	IEngUserProfile userProfile = (IEngUserProfile)permSession.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
   	
	// get the execution role
	String executionRole = (String)aSessionContainer.getAttribute(SpagoBIConstants.ROLE);
	
    // get the map of parameters dor execution call
    Map mapPars = null;
    if (isRefreshRequest) {
    	String driverClassName = obj.getEngine().getDriverName();
		IEngineDriver aEngineDriver = (IEngineDriver)Class.forName(driverClassName).newInstance();
		mapPars = aEngineDriver.getParameterMap(obj, userProfile, executionRole);
		//mapPars.put("username", userProfile.getUserUniqueIdentifier().toString());
		//mapPars.put("spagobicontext", GeneralUtilities.getSpagoBiContextAddress());
    	//mapPars = executionManager.getLastExecutionParametersMap(flowId);
    } else {
    	mapPars = (Map)moduleResponse.getAttribute(ObjectsTreeConstants.REPORT_CALL_URL);
    }
	
    mapPars.put("username", userProfile.getUserUniqueIdentifier().toString());
    mapPars.put("spagobicontext", GeneralUtilities.getSpagoBiContextAddress());
    
	// try to get the modality
	boolean isSingleObjExec = false;
	String modality = (String)aSessionContainer.getAttribute(SpagoBIConstants.MODALITY);
   	if( (modality!=null) && modality.equals(SpagoBIConstants.SINGLE_OBJECT_EXECUTION_MODALITY) )
   		isSingleObjExec = true;
	
	AuditManager auditManager = AuditManager.getInstance();
	String auditModality = (modality != null) ? modality : "NORMAL_EXECUTION";
	Integer executionAuditId = auditManager.insertAudit(obj, userProfile, executionRole, auditModality);
	// adding parameters for AUDIT updating
	if (executionAuditId != null) {
		mapPars.put(AuditManager.AUDIT_ID, executionAuditId.toString());
		mapPars.put(AuditManager.AUDIT_SERVLET, GeneralUtilities.getSpagoBiAuditManagerServlet());
	}
	
	// build the string of the title
    String title = "";
    title = obj.getName();
    String objDescr = obj.getDescription();
    if( (objDescr!=null) && !(objDescr.trim().equals("")) )
    	title += ": " + objDescr;

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
    backUrlPars.put(SpagoBIConstants.ACTOR, actor);
    backUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_BACK_TO, "1");
    String backUrl = urlBuilder.getUrl(request, backUrlPars);

	// build the refresh url
	Map refreshUrlPars = new HashMap();
	if (!isSingleObjExec) {
		refreshUrlPars.put("PAGE", BIObjectsModule.MODULE_PAGE);
		refreshUrlPars.put(SpagoBIConstants.ACTOR, actor);
		refreshUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
	} else {
		refreshUrlPars.put("PAGE", "DirectExecutionPage");
		refreshUrlPars.put("OPERATION", "REFRESH");
	}
    String refreshUrl = urlBuilder.getUrl(request, refreshUrlPars);

	IDomainDAO domaindao = DAOFactory.getDomainDAO();
	List states = domaindao.loadListDomainsByType("STATE");
    List possibleStates = new java.util.ArrayList();
    if (actor.equalsIgnoreCase(SpagoBIConstants.DEV_ACTOR)){
    	Iterator it = states.iterator();
    	 while(it.hasNext()) {
      		    	Domain state = (Domain)it.next();
      		    	if (state.getValueCd().equalsIgnoreCase("TEST")){
      					possibleStates.add(state);
      				}
      	}
    } else if (actor.equalsIgnoreCase(it.eng.spagobi.constants.SpagoBIConstants.TESTER_ACTOR)){
    	Iterator it = states.iterator();
    	 while(it.hasNext()) {
      		    	Domain state = (Domain)it.next();
      		    	if ((state.getValueCd().equalsIgnoreCase("DEV")) || ((state.getValueCd().equalsIgnoreCase("REL")))) {
      					possibleStates.add(state);
      				}
      	}
    }

    // check if maximize is able 
    boolean maximizeAble = false;
    String maximizeAbleStr = ChannelUtilities.getPreferenceValue(aRequestContainer, SpagoBIConstants.PREFERENCE_MAXIMIZE_ABLE, "FALSE");
    if(maximizeAbleStr.equalsIgnoreCase("true")) {
    	maximizeAble = true;
    }
    
    // check if notes editor is able
    boolean edNoteAble = false;
    String edNoteAbleStr = ChannelUtilities.getPreferenceValue(aRequestContainer, SpagoBIConstants.PREFERENCE_NOTES_EDITOR_ABLE, "FALSE");
    if(edNoteAbleStr.equalsIgnoreCase("true")) {
    	edNoteAble = true;
    }
    // get the preference width dimension for notes editor
	int widthNotes = 600;
	String widthNoteEditorStr = ChannelUtilities.getPreferenceValue(aRequestContainer, SpagoBIConstants.PREFERENCE_NOTES_EDITOR_WIDTH, "600");
	try{
		widthNotes = new Integer(widthNoteEditorStr).intValue();
	} catch (Exception e) {
		widthNotes = 600;
		SpagoBITracer.warning(SpagoBIConstants.NAME_MODULE, "executeBIObjectPageExecution",
				              " ", "Error while recovering the width preference of the notes editor", e);
	}
	// get the preference height dimension for notes editor
	int heightNotes = 300;
	String heightNoteEditorStr = ChannelUtilities.getPreferenceValue(aRequestContainer, SpagoBIConstants.PREFERENCE_NOTES_EDITOR_HEIGHT, "300");
	try{
		heightNotes = new Integer(heightNoteEditorStr).intValue();
	} catch (Exception e) {
		heightNotes = 300;
		SpagoBITracer.warning(SpagoBIConstants.NAME_MODULE, "executeBIObjectPageExecution",
				              " ", "Error while recovering the height preference of the notes editor", e);
	}
	// check if the editor notes as to be opened automatically
	boolean notesEditOpen = false;
	String notesEditOpenStr = ChannelUtilities.getPreferenceValue(aRequestContainer, SpagoBIConstants.PREFERENCE_NOTES_EDITOR_OPEN, "false");
	if(notesEditOpenStr.equalsIgnoreCase("true")){
		notesEditOpen = true;
	}

	
%>












 <!-- ************************************************************************* -->
 <!-- ******************** START TITLE PART  ********************************** -->
 <!-- ************************************************************************* -->

<%
	// IF NOT SINGLE OBJECT MODALITY SHOW DEFAULT TITLE BAR
	if(!isSingleObjExec) {
%>

<table class='header-table-portlet-section'>
	<tr class='header-row-portlet-section'>
    	<td class='header-title-column-portlet-section' style='vertical-align:middle;'>
        <div id="navigationBar<%=executionId%>">
           &nbsp;&nbsp;&nbsp;<%=title%>
        </div>
       </td>
       <td class='header-empty-column-portlet-section'>&nbsp;</td>
       <td class='header-button-column-portlet-section'>
           <a href='<%=backUrl%>'>
                 <img title='<spagobi:message key = "SBIDev.docConf.execBIObjectParams.backButt" />'
                      class='header-button-image-portlet-section'
                      src='<%= urlBuilder.getResourceLink(request, "/img/back.png")%>'
                      alt='<spagobi:message key = "SBIDev.docConf.execBIObjectParams.backButt" />' />
           </a>
       </td>
       <% if ((actor.equalsIgnoreCase(SpagoBIConstants.DEV_ACTOR)) ||
    		  (actor.equalsIgnoreCase(SpagoBIConstants.TESTER_ACTOR))) {
    	   	Map formUrlPars = new HashMap();
    	   	formUrlPars.put("PAGE", ExecuteBIObjectModule.MODULE_PAGE);
    	   	formUrlPars.put(SpagoBIConstants.ACTOR, actor);
    	   	formUrlPars.put(SpagoBIConstants.MESSAGEDET, ObjectsTreeConstants.EXEC_CHANGE_STATE);
    	   	formUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
    	    String formUrl = urlBuilder.getUrl(request, formUrlPars);
    	%>
       <form method='POST' action='<%=formUrl%>' id='changeStateForm'  name='changeStateForm'>
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
      			<!--br/-->
      		</td>
      		<td class='header-select-column-portlet-section'>
      			<input type='image' class='header-button-image-portlet-section'
      			       src='<%= urlBuilder.getResourceLink(request, "/img/updateState.png")%>'
      			       title='<spagobi:message key = "SBIDev.docConf.execBIObjectParams.updateButt" />'
      			       alt='<spagobi:message key = "SBIDev.docConf.execBIObjectParams.updateButt" />'/>
      		</td>
        </form>
       <% } %>


       <%
        if(maximizeAble) {
        %>
		<td style='visibility:visible;' id='maximizeCell<%=executionId%>_space' class='header-empty-column-single-object-execution-portlet-section'>&nbsp;</td>
		<td style='visibility:visible;' id='maximizeCell<%=executionId%>' class='header-button-column-single-object-execution-portlet-section'>
			<a style="text-decoration:none;" href='javascript:maximize<%=executionId%>();'>
				<img width="22px" height="22px"
					src='<%= urlBuilder.getResourceLink(request, "/img/maximize32.jpg")%>'
					name='maximize'
					alt='<%=msgBuilder.getMessage("SBIExecution.maximize", "messages", request)%>'
					title='<%=msgBuilder.getMessage("SBIExecution.maximize", "messages", request)%>' />
			</a>
		</td>
		<script>
			if(!isIE7() && !isMoz()) {
        		$('maximizeCell<%=executionId%>').style.visibility	= 'hidden';
        		$('maximizeCell<%=executionId%>').style.width	= '1px';
        		$('maximizeCell<%=executionId%>_space').style.visibility	= 'hidden';
        		$('maximizeCell<%=executionId%>_space').style.width	= '1px';
        	}   
		</script>
        <%
        }
        %>

       <%
        if(edNoteAble) {
       %>

        <td class='header-empty-column-portlet-section'>&nbsp;</td>
        <td class='header-button-column-portlet-section'>
           <a id="iconNotesEmpty<%=executionId%>" href='javascript:opencloseNotesEditor<%=executionId%>()'>
               <img width="22px" height="22px" title='<spagobi:message key = "sbi.execution.notes.opencloseeditor" />'
                    src='<%= urlBuilder.getResourceLink(request, "/img/notesEmpty.jpg")%>'
                    alt='<spagobi:message key = "sbi.execution.notes.opencloseeditor" />' />
           </a>
           <a id="iconNotesFilled<%=executionId%>" style="display:none;"
              href='javascript:opencloseNotesEditor<%=executionId%>()'>
               <img width="22px" height="22px" title='<spagobi:message key = "sbi.execution.notes.opencloseeditor" />'
                    src='<%= urlBuilder.getResourceLink(request, "/img/notes.jpg")%>'
                    alt='<spagobi:message key = "sbi.execution.notes.opencloseeditor" />' />
           </a>
         </td>

       <% } %>

   </tr>
</table>


<%
	// IF SINGLE OBJECT MODALITY SHOW THE PROPER TITLE BAR
	} else {
%>

<table  width='100%' cellspacing='0' border='0' id="singleobjecttitlebar<%=executionId%>">
	<tr>
		<td class='header-title-column-single-object-execution-portlet-section' style='vertical-align:middle;'>
		      <div id="navigationBar<%=executionId%>">
		        &nbsp;&nbsp;&nbsp;<%=title%>
		      </div>
		</td>
		<td class='header-empty-column-single-object-execution-portlet-section'>&nbsp;</td>
		<td class='header-button-column-single-object-execution-portlet-section'>
			<a style="text-decoration:none;" href='<%=refreshUrl%>' id="singleobjectrefreshlink">
				<img width="22px" height="22px"
					src='<%= urlBuilder.getResourceLink(request, "/img/updateState.png")%>'
					name='refresh'
					alt='<%=msgBuilder.getMessage("SBIExecution.refresh", "messages", request)%>'
					title='<%=msgBuilder.getMessage("SBIExecution.refresh", "messages", request)%>' />
			</a>
		</td>
		
		
		<%
        if(maximizeAble) {
        %>
		<td style='visibility:visible;' id='maximizeCell<%=executionId%>_space' class='header-empty-column-single-object-execution-portlet-section'>&nbsp;</td>
		<td style='visibility:visible;' id='maximizeCell<%=executionId%>' class='header-button-column-single-object-execution-portlet-section'>
			<a style="text-decoration:none;" href='javascript:maximize<%=executionId%>();'>
				<img width="22px" height="22px"
					src='<%= urlBuilder.getResourceLink(request, "/img/maximize32.jpg")%>'
					name='maximize'
					alt='<%=msgBuilder.getMessage("SBIExecution.maximize", "messages", request)%>'
					title='<%=msgBuilder.getMessage("SBIExecution.maximize", "messages", request)%>' />
			</a>
		</td>
		<script>
			if(!isIE7() && !isMoz()) {
        		$('maximizeCell<%=executionId%>').style.visibility	= 'hidden';
        		$('maximizeCell<%=executionId%>').style.width	= '1px';
        		$('maximizeCell<%=executionId%>_space').style.visibility	= 'hidden';
        		$('maximizeCell<%=executionId%>_space').style.width	= '1px';
        	}   
		</script>
        <%
        }
        %>

		
		<%
        if(edNoteAble) {
       %>
        <td class='header-empty-column-single-object-execution-portlet-section'>&nbsp;</td>
        <td class='header-button-column-single-object-execution-portlet-section'>
           <a id="iconNotesEmpty<%=executionId%>" href='javascript:opencloseNotesEditor<%=executionId%>()'>
               <img width="22px" height="22px" title='<spagobi:message key = "sbi.execution.notes.opencloseeditor" />'
                    src='<%= urlBuilder.getResourceLink(request, "/img/notesEmpty.jpg")%>'
                    alt='<spagobi:message key = "sbi.execution.notes.opencloseeditor" />' />
           </a>
           <a id="iconNotesFilled<%=executionId%>" style="display:none;"
              href='javascript:opencloseNotesEditor<%=executionId%>()'>
               <img width="22px" height="22px" title='<spagobi:message key = "sbi.execution.notes.opencloseeditor" />'
                    src='<%= urlBuilder.getResourceLink(request, "/img/notes.jpg")%>'
                    alt='<spagobi:message key = "sbi.execution.notes.opencloseeditor" />' />
           </a>
        </td>
       <% } %>
    </tr>
</table>

<% } %>

<!-- ************************************************************************* -->
 <!-- ******************** END TITLE PART  ********************************** -->
 <!-- ************************************************************************* -->












<!-- ***************************************************************** -->
<!-- ***************************************************************** -->
<!-- **************** START MAXIMIZE ********************************* -->
<!-- ***************************************************************** -->
<!-- ***************************************************************** -->


<div id='maximizebackground<%=executionId%>' style='display:none;position:absolute;width:98%;height:98%;top:1%;left:1%;background-color:white;border:2px solid #dddddd;'>
	<table height='5%' width='100%' cellspacing='0' border='0'>
		<tr>
			<td class='header-title-column-single-object-execution-portlet-section' style='vertical-align:middle;'>
		        &nbsp;&nbsp;&nbsp;<%=title%>
			</td>
			<td class='header-empty-column-single-object-execution-portlet-section'>&nbsp;</td>
			<td class='header-button-column-single-object-execution-portlet-section'>
				<a style="text-decoration:none;" href='javascript:minimize<%=executionId%>()' >
					<img width="22px" height="22px"
						src='<%= urlBuilder.getResourceLink(request, "/img/erase32.png")%>'
						name='close'
						alt='<%=msgBuilder.getMessage("SBIExecution.close", "messages", request)%>'
						title='<%=msgBuilder.getMessage("SBIExecution.close", "messages", request)%>' />
				</a>
			</td>
		</tr>
	</table>
</div> 
		
<script>
      dimensionHolder<%=executionId%> = new functDimensionHolder<%=executionId%>();
    
      function functDimensionHolder<%=executionId%>() {
        this.width = 0;
        this.height = 0;
      }
	
      function maximize<%=executionId%>() {      
        if(!isIE7() && !isMoz()) {
        	alert('Works only with Explorer 7 or Firefox ...');
        	return;
        }   
        divbg = document.getElementById('maximizebackground<%=executionId%>');
        divbg.style.display='inline';
        ifram = document.getElementById('iframeexec<%=executionId%>');
        clientHeight = ifram.clientHeight;
		clientWidth = ifram.clientWidth;						
        dimensionHolder<%=executionId%>.width = clientWidth;
        dimensionHolder<%=executionId%>.height = clientHeight;
        ifram.style.position='absolute';
        ifram.style.left='2%';
        ifram.style.top='7%';
        ifram.style.width='96%';
        ifram.style.height='91%';
	  }
      
      
      function minimize<%=executionId%>() {
          divbg = document.getElementById('maximizebackground<%=executionId%>');
          divbg.style.display='none';
          ifram = document.getElementById('iframeexec<%=executionId%>');
          ifram.style.position='relative';
          ifram.style.top='0px';
          ifram.style.height=dimensionHolder<%=executionId%>.height + 'px';
      }
      
</script>



<!-- ***************************************************************** -->
<!-- ***************************************************************** -->
<!-- **************** END MAXIMIZE *********************************** -->
<!-- ***************************************************************** -->
<!-- ***************************************************************** -->















<!-- ***************************************************************** -->
<!-- ***************************************************************** -->
<!-- **************** START BLOCK NOTES EDITOR *********************** -->
<!-- ***************************************************************** -->
<!-- ***************************************************************** -->

<%
	if(edNoteAble) {
		BIObjectNotesManager objectNotesManager = new BIObjectNotesManager();
		String execIdentifier = objectNotesManager.getExecutionIdentifier(obj);
		String nameUser = (String)userProfile.getUserUniqueIdentifier();
		String linkFck = urlBuilder.getResourceLink(request, "/js/FCKeditor/fckeditor.js");
%>

<SCRIPT language='JavaScript' src='<%=linkFck%>'></SCRIPT>


<script type="text/javascript">

    var locked<%=executionId%> = false;
    var xmlHttp<%=executionId%> = null;
    var holdLockInterval<%=executionId%> = null;


    function reloadNotes<%=executionId%>() {
       url="<%=GeneralUtilities.getSpagoBiContextAddress()%>/BIObjectNotesService?";
       pars = "task=getNotes&biobjid=<%=obj.getId()%>&execidentifier=<%=execIdentifier%>";
       new Ajax.Request(url,
          {
            method: 'post',
            parameters: pars,
            onSuccess: function(transport){
                            response = transport.responseText || "";
                            getNotesCallback<%=executionId%>(response);
                        },
            onFailure: somethingWentWrong
          }
        );
	   }


    function requireLock<%=executionId%>() {
	      cleanError<%=executionId%>();
	      if(locked<%=executionId%>){
	    		return;
	    	}
		    url="<%=GeneralUtilities.getSpagoBiContextAddress()%>/BIObjectNotesService";
		    pars = "task=requireLock&biobjid=<%=obj.getId()%>&user=<%=nameUser%>&execidentifier=<%=execIdentifier%>";
        new Ajax.Request(url,
          {
            method: 'post',
            parameters: pars,
            onSuccess: function(transport){
                            response = transport.responseText || "";
                            requireLockCallback<%=executionId%>(response);
                        },
            onFailure: somethingWentWrong
          }
        );
    }

    function holdLock<%=executionId%>() {
        url = "<%=GeneralUtilities.getSpagoBiContextAddress()%>/BIObjectNotesService";
		    pars = "task=holdLock&user=<%=nameUser%>&execidentifier=<%=execIdentifier%>";
        new Ajax.Request(url,
          {
            method: 'post',
            parameters: pars,
            onSuccess: function(transport){
                            response = transport.responseText || "";
                            holdLockCallback<%=executionId%>(response);
                        },
            onFailure: somethingWentWrong
          }
        );
    }


    function saveNotes<%=executionId%>() {
	      cleanError<%=executionId%>();
	      var editor = FCKeditorAPI.GetInstance('editorfckarea<%=executionId%>') ;
	      xhtml = editor.GetXHTML(false);
	      if(!locked<%=executionId%>){
	    		return;
	    	}
	    	while(xhtml.indexOf("&")!=-1) {
          xhtml = xhtml.replace(/&/, "@-@-@");
        }
	      url="<%=GeneralUtilities.getSpagoBiContextAddress()%>/BIObjectNotesService";
        pars="task=saveNotes&biobjid=<%=obj.getId()%>&user=<%=nameUser%>&execidentifier=<%=execIdentifier%>&notes="+xhtml;
        new Ajax.Request(url,
          {
            method: 'post',
            parameters: pars,
            onSuccess: function(transport){
                            response = transport.responseText || "";
                            saveNotesCallback<%=executionId%>(response);
                        },
            onFailure: somethingWentWrong
          }
        );
    }


    function getNotesCallback<%=executionId%>(response){
        if(responseHasError<%=executionId%>(response)) {
         		error = getResponseError<%=executionId%>(response);
         		divError = document.getElementById('notesErrorMessage<%=executionId%>');
         		divError.innerHTML = error;
         		return;
        }
        editor = FCKeditorAPI.GetInstance('editorfckarea<%=executionId%>');
        while(response.indexOf("@-@-@")!=-1) {
          response = response.replace(/@-@-@/, "&");
        }
        editor.SetHTML(response, false);
        try{
          	editor.EditingArea.Document.body.contentEditable="false";
        }catch(e){
            // not IE
        }
        fillAlertExistNotes<%=executionId%>(response);
    }


    function requireLockCallback<%=executionId%>(response){
  			if(responseHasError<%=executionId%>(response)) {
          		error = getResponseError<%=executionId%>(response);
          		divError = document.getElementById('notesErrorMessage<%=executionId%>');
          		divError.innerHTML = error;
          		return;
        }
        // editor locked
        locked<%=executionId%>=true;
        document.getElementById('notesLockImg<%=executionId%>').style.display='none';
        document.getElementById('notesSaveImg<%=executionId%>').style.display='inline';
        document.getElementById('notesReloadImg<%=executionId%>').style.display='none';
        editor = FCKeditorAPI.GetInstance('editorfckarea<%=executionId%>') ;
        while(response.indexOf("@-@-@")!=-1) {
          response = response.replace(/@-@-@/, "&");
        }
        editor.SetHTML(response, false);
        try{
      	   	editor.EditingArea.Document.body.contentEditable="true";
        }catch(e){
           	// not IE
        }
        holdLockInterval<%=executionId%> = setInterval("holdLock<%=executionId%>()", 30000);
        fillAlertExistNotes<%=executionId%>(response);
	  }


    function holdLockCallback<%=executionId%>(){
  	 		// do nothing (the hold lock request is useful only to keep alive the lock)
	  }


    function saveNotesCallback<%=executionId%>(response){
      	locked<%=executionId%> = false;
      	document.getElementById('notesLockImg<%=executionId%>').style.display='inline';
      	document.getElementById('notesSaveImg<%=executionId%>').style.display='none';
      	document.getElementById('notesReloadImg<%=executionId%>').style.display='inline';
      	clearInterval(holdLockInterval<%=executionId%>);
      	try{
      	   editor.EditingArea.Document.body.contentEditable="false";
      	}catch(e){
           // not IE
      	}
  			if(responseHasError<%=executionId%>(response)) {
         		error = getResponseError<%=executionId%>(response);
         		divError = document.getElementById('notesErrorMessage<%=executionId%>');
         		divError.innerHTML = error;
         		return;
        }
        reloadNotes<%=executionId%>();
    }


    function initializeNotes<%=executionId%>() {
       reloadNotes<%=executionId%>();
	   }


	  function somethingWentWrong() {
        alert('Something went wrong ...');
    }


    function responseHasError<%=executionId%>(response){
    	if(response.indexOf('SpagoBIError:')!=-1) {
    		return true;
    	} else {
    	    return false;
    	}
    }

    function getResponseError<%=executionId%>(response) {
    	error = response.substring(response.indexOf('SpagoBIError:')+13);
    	return error;
    }

    function cleanError<%=executionId%>(){
    	divError = document.getElementById('notesErrorMessage<%=executionId%>');
        divError.innerHTML = "";
    }


    function fillAlertExistNotes<%=executionId%>(notes){
       iconempty = document.getElementById('iconNotesEmpty<%=executionId%>');
       iconfilled = document.getElementById('iconNotesFilled<%=executionId%>');
       notes = notes.replace(/^\s*|\s*$/g,"");
       if(notes!=""){
          iconempty.style.display='none';
          iconfilled.style.display='inline';
       } else {
          iconempty.style.display='inline';
          iconfilled.style.display='none';
       }
    }

  </script>




<div id="divNotes<%=executionId%>" style="heigth:100%;width:100%;display:none;background-color:#efefde;overflow:hidden;">

  <div id="notesLockImg<%=executionId%>" style="float:left;display:inline;padding:5px;">
      <a href="javascript:requireLock<%=executionId%>()">
          <img title='<spagobi:message key = "sbi.execution.notes.lockeditor" />'
               alt='<spagobi:message key = "sbi.execution.notes.lockeditor" />'
               src='<%= urlBuilder.getResourceLink(request, "/img/lock16.gif")%>' />
      </a>
  </div>
  <div id="notesSaveImg<%=executionId%>" style="float:left;display:none;padding:5px;">
      <a href="javascript:saveNotes<%=executionId%>()">
          <img title='<spagobi:message key = "sbi.execution.notes.savenotes" />'
          		alt='<spagobi:message key = "sbi.execution.notes.savenotes" />'
               src='<%= urlBuilder.getResourceLink(request, "/img/save16.gif")%>' />
      </a>
  </div>
  <div id="notesReloadImg<%=executionId%>" style="float:left;display:inline;padding:5px;">
      <a href="javascript:reloadNotes<%=executionId%>()">
          <img title='<spagobi:message key = "sbi.execution.notes.reloadnotes" />'
          		alt='<spagobi:message key = "sbi.execution.notes.reloadnotes" />'
               src='<%= urlBuilder.getResourceLink(request, "/img/reload16.gif")%>' />
      </a>
  </div>
  <div id="notesErrorMessage<%=executionId%>"  style="float:left;color:red;padding:5px;font-family:arial;font-size:11px;">

  </div>
  <div style="clear:left;"></div>


  <textarea id="editorfckarea<%=executionId%>" name="editorfckarea<%=executionId%>"></textarea>


</div>



<script>

  var oFCKeditor<%=executionId%> = new FCKeditor('editorfckarea<%=executionId%>');
  oFCKeditor<%=executionId%>.BasePath = "<%=ChannelUtilities.getSpagoBIContextName(request) + "/js/FCKeditor/"%>";
  oFCKeditor<%=executionId%>.ToolbarSet = 'SbiObjectNotes';
  oFCKeditor<%=executionId%>.Height = <%=heightNotes%> - 35;
  oFCKeditor<%=executionId%>.Width = <%=widthNotes%> - 5;
  oFCKeditor<%=executionId%>.ReplaceTextarea();

  var noteOpen<%=executionId%> = false;

  function opencloseNotesEditor<%=executionId%>() {
    if(noteOpen<%=executionId%>) {
      // do nothing (close notes with window button)
    } else {
      noteOpen<%=executionId%> = true;
      openNotes<%=executionId%>(false);
    }
  }

  function openNotes<%=executionId%>(automatic) {
    frameFcke<%=executionId%> = document.getElementById('editorfckarea<%=executionId%>___Frame');
    if(frameFcke<%=executionId%>!=null) {
        frameFcke<%=executionId%>.height= <%=heightNotes%> - 35;
        frameFcke<%=executionId%>.width= <%=widthNotes%> - 5;
    }
    diviframeobj = document.getElementById('divIframe<%=executionId%>');
    pos = findPos(diviframeobj);
    win<%=executionId%> = null;
  
    
    if(automatic) {
       win<%=executionId%> = new Window('win_notes_<%=executionId%>', {className: "alphacube", title: "Notes for <%=title%>", top:pos[1], left:pos[0], width:<%=widthNotes%>, height:<%=heightNotes%>, hideEffect:Element.hide, showEffect:Element.show});
  	   win<%=executionId%>.setDestroyOnClose();
       win<%=executionId%>.setContent('divNotes<%=executionId%>', false, false);
       win<%=executionId%>.show(false);
       win<%=executionId%>.minimize();
    } else {
       win<%=executionId%> = new Window('win_notes_<%=executionId%>', {className: "alphacube", title: "Notes for <%=title%>", top:pos[1], left:pos[0], width:<%=widthNotes%>, height:<%=heightNotes%>, hideEffect:Element.hide, showEffect:Element.show});
  	   win<%=executionId%>.setDestroyOnClose();
       win<%=executionId%>.setContent('divNotes<%=executionId%>', false, false);
       win<%=executionId%>.show();
    }


    observerClose<%=executionId%> = {
      onClose: function(eventName, win) {
        if(win == win<%=executionId%>) {
          noteOpen<%=executionId%> = false;
          document.getElementById('divNotes<%=executionId%>').style.display='none';
        }
      }
    }
    Windows.addObserver(observerClose<%=executionId%>);

    observerResize<%=executionId%> = {
      onResize: function(eventName, win) {
        if(win == win<%=executionId%>) {
            heightwin = win.getSize().height;
            widthwin = win.getSize().width;
            frameFcke<%=executionId%> = document.getElementById('editorfckarea<%=executionId%>___Frame');
            frameFcke<%=executionId%>.height=heightwin - 35;
            frameFcke<%=executionId%>.width=widthwin - 5;
        }
      }
    }
    Windows.addObserver(observerResize<%=executionId%>);


    observerEndResize<%=executionId%> = {
      onEndResize: function(eventName, win) {
        if(win == win<%=executionId%>) {
            heightwin = win.getSize().height;
            widthwin = win.getSize().width;
            frameFcke<%=executionId%> = document.getElementById('editorfckarea<%=executionId%>___Frame');
            frameFcke<%=executionId%>.height=heightwin - 35;
            frameFcke<%=executionId%>.width=widthwin - 5;
        }
      }
    }
    Windows.addObserver(observerEndResize<%=executionId%>);


    observerMaximize<%=executionId%> = {
      onMaximize: function(eventName, win) {
        if(win == win<%=executionId%>) {
            heightwin = win.getSize().height;
            widthwin = win.getSize().width;
            frameFcke<%=executionId%> = document.getElementById('editorfckarea<%=executionId%>___Frame');
            frameFcke<%=executionId%>.height=heightwin - 35;
            frameFcke<%=executionId%>.width=widthwin - 5;
        }
      }
    }
    Windows.addObserver(observerMaximize<%=executionId%>);


  }


  <% if(notesEditOpen) { %>
  	try{
    	SbiJsInitializer.automaticOpenNotes<%=executionId%> = function() {openNotes<%=executionId%>(true);};
    } catch (err) {
        alert('Cannot open automatically the editor note');
    }
  <% } %>



</script>




<%	} %>

<!-- ***************************************************************** -->
<!-- ***************************************************************** -->
<!-- **************** END BLOCK NOTES EDITOR ************************* -->
<!-- ***************************************************************** -->
<!-- ***************************************************************** -->





















<!-- ***************************************************************** -->
<!-- ***************************************************************** -->
<!-- **************** START BLOCK IFRAME ***************************** -->
<!-- ***************************************************************** -->
<!-- ***************************************************************** -->

<%
  if(!heightSetted) {
%>
	<script>
		
		pos<%=executionId%> = null; 
	
		function adaptSize<%=executionId%>Funct() {
				
			// calculate height of the visible area
			heightVisArea = 0;

			if(isIE5()) { heightVisArea = top.document.body.clientHeight; }
			if(isIE6()) { heightVisArea = top.document.body.clientHeight; }
			if(isIE7()) { heightVisArea = top.document.documentElement.clientHeight }
			if(isMoz()) { heightVisArea = top.innerHeight; }
	
			// get the frame div object
			diviframeobj = document.getElementById('divIframe<%=executionId%>');
			// find the frame div position
			pos<%=executionId%> = findPos(diviframeobj);	
						
			// calculate space below position frame div
			spaceBelowPos = heightVisArea - pos<%=executionId%>[1];
			// set height to the frame
			iframeEl = document.getElementById('iframeexec<%=executionId%>');
			iframeEl.style.height = spaceBelowPos + 'px';
	
	    	// to give time to the browser to update the dom (dimension of the iframe)
		  	setTimeout("adaptSize<%=executionId%>_2Part()", 250);
		}
	
	
	  	function adaptSize<%=executionId%>_2Part() {
        
        	// calculate height of the win area and height footer
			heightWinArea = 0;
  			heightFooter = 0;
  			if(isIE5()) {
  				heightWinArea = document.body.scrollHeight;
  				heightFooter = heightWinArea - heightVisArea;
  			}
  			if(isIE6()) {
  				heightWinArea = document.body.scrollHeight;
  				heightFooter = heightWinArea - heightVisArea;
  			}
  			if(isIE7()) {
  				heightWinArea = document.body.offsetHeight;
  				heightFooter = heightWinArea - heightVisArea;
  			}
  			if(isMoz()) {
  				heightWinArea = document.body.offsetHeight;
  				heightFooter = (heightWinArea - heightVisArea);
  			}	 
  	
  			// calculate height of the frame
  			heightFrame = heightVisArea - pos<%=executionId%>[1] - heightFooter;
  			iframeEl = document.getElementById('iframeexec<%=executionId%>');
  			iframeEl.style.height = heightFrame + 'px';
    }
	
	
		try {
			SbiJsInitializer.adaptSize<%=executionId%> = adaptSize<%=executionId%>Funct;
	    } catch (err) {
			alert('Cannot resize the document view area');
		}

	</script>
<%
  }
%>



<center id='centerDivIframe'>
<div id="divIframe<%=executionId%>" style="width:100%;overflow=auto;">

           <%
			String heightStr = "";
			if(heightSetted)
				heightStr = "height:"+heightArea+"px;";
           %>

            <iframe id="iframeexec<%=executionId%>"
                    name="iframeexec<%=executionId%>"
                    src=""
                    style="width:100%;<%=heightStr%>;"
                    frameborder="0" >
			</iframe>

			<%
			String target = "iframeexec" + executionId;
			if (isDrillRequest && !isRefreshRequest) target = "_self";
			%>
         	<form name="formexecution<%=executionId%>"
                id='formexecution<%=executionId%>' method="post"
         	      action="<%=engineurl%>"
         	      target='<%=target%>'>
         	<%
				java.util.Set keys = mapPars.keySet();
				Iterator iterKeys = keys.iterator();
				while(iterKeys.hasNext()) {
         	    	String key = iterKeys.next().toString();
         	    	String value = mapPars.get(key).toString();
	         	%>
	         		<input type="hidden" name="<%=key%>" value="<%=value%>" />
	         	<%
         	   }
         	%>
	         	  <center>
	         	    <input id="button<%=executionId%>" type="submit" value="View Output"  style='display:inline;'/>
				  </center>
			</form>
            
            <%-- This script updates the execution manager and the navigation bar --%>
            <script>

            updateExecutioManager<%=executionId%>();

			function updateExecutioManager<%=executionId%>() {
				winName = window.name;
				if (window.name != null && window.name.match('iframeexec')) {
					//winName = winName.substring(10);
					document.getElementById('formexecution<%=executionId%>').target="_self";
				}
	        	url = "<%=GeneralUtilities.getSpagoBiContextAddress()%>/servlet/AdapterHTTP";
		    	pars = "NEW_SESSION=TRUE&ACTION_NAME=UPDATE_EXECUTION_MANAGER&windowName=" + winName;
		    	pars +="&spagobi_execution_id=<%=executionId%>";
		    	pars += "&BIOBJECT_ID=<%=obj.getId()%>";
		    	pars += "&spagobi_execution_role=<%=executionRole%>";
		    	pars += "&<%=LightNavigationManager.LIGHT_NAVIGATOR_DISABLED%>=TRUE";
		    	<%
		    	List parameters = obj.getBiObjectParameters();
		    	Iterator parametersIt = parameters.iterator();
		    	while (parametersIt.hasNext()) {
		    		BIObjectParameter aParameter = (BIObjectParameter) parametersIt.next();
		    		List parValues = aParameter.getParameterValues();
		    		if (parValues != null) {
			    		Iterator parValuesIt = parValues.iterator();
			    		while (parValuesIt.hasNext()) {
			    			String aParValue = parValuesIt.next().toString();
				    		%>
				    		pars += "&<%=aParameter.getParameterUrlName()%>=<%=aParValue%>";
				    		<%
			    		}
		    		} else {
			    		%>
			    		pars += "&<%=aParameter.getParameterUrlName()%>=NULL";
			    		<%
		    		}
		    	}
		    	%>
        		new Ajax.Request(url,
          			{
            			method: 'post',
            			parameters: pars,
           				onSuccess: function(transport){
                            			response = transport.responseText || "";
                            			refreshNavigationBar<%=executionId%>(response);
                        			},
           				onFailure: doNothing<%=executionId%>,
           				asynchronous: false
         			}
       			);
			}

            function refreshNavigationBar<%=executionId%>(html) {
            	winName = window.name;
            	navBarDiv = null;
				if (winName != null && winName.match('iframeexec')) {
					winName = winName.substring(10);
					navBarDiv = parent.document.getElementById("navigationBar"+ winName);
				} else {
            		navBarDiv = parent.document.getElementById("navigationBar<%=executionId%>");
            	}
            	if (navBarDiv != null) {
            		navBarDiv.innerHTML = html;
            	}
            }
            
            function doNothing<%=executionId%>() {}
            
			function GetXmlHttpObject<%=executionId%>(){ 
				var objXMLHttp=null
				if(window.XMLHttpRequest)	{
					objXMLHttp=new XMLHttpRequest()
				} else if (window.ActiveXObject) {
					objXMLHttp=new ActiveXObject("Microsoft.XMLHTTP")
				}
				return objXMLHttp
			}

            </script>
            
            <%-- This script submits the document execution form --%>
            <script>
			    button = document.getElementById('button<%=executionId%>');
				button.style.display='none';
				button.click();
            </script>

</div>
</center>

<script type="text/javascript">

    try{
      window.onload = SbiJsInitializer.initialize;
  	} catch (err) {
      alert('Cannot execute javascript initialize functions');
  	}
  	
</script>


<!-- ***************************************************************** -->
<!-- ***************************************************************** -->
<!-- **************** END BLOCK IFRAME ******************************* -->
<!-- ***************************************************************** -->
<!-- ***************************************************************** -->

























<!-- ***************************************************************** -->
<!-- ***************************************************************** -->
<!-- **************** START IFRAMES NAVIGATOR ************************ -->
<!-- ***************************************************************** -->
<!-- ***************************************************************** -->
<%--



			if (window != top) {
				totalVisArea = 0;
				if(isIE5) { totalVisArea = window.document.body.clientHeight; }
				if(isIE6) { totalVisArea = window.document.body.clientHeight; }
				if(isIE7) { totalVisArea = window.document.body.clientHeight; }
				if(isMoz) { totalVisArea = window.innerHeight; }
				iframeEl = document.getElementById('iframeexec<%=executionId%>');
				titleBar = document.getElementById('singleobjecttitlebar<%=executionId%>');
				if (titleBar.style.display != 'none') {
					totalVisArea = totalVisArea - titleBar.clientHeight - 30;
				}
				iframeEl.style.height = totalVisArea + 'px';
				return;
			}


onLoad="this.contentWindow.document.getElementById('singleobjecttitlebar<%=executionId%>').style.display='none'"


var iframeHeight<%=executionId%> = 0;
	var isIE = false;
	var isIE5 = false;
	var isIE6 = false;
	var isIE7 = false;
	var isMoz = false;
	// finds the browser name
	navigatorname = navigator.appName;
	navigatorversion = navigator.appVersion;
	navigatorname = navigatorname.toLowerCase();
	isIE = (navigatorname.indexOf('explorer') != -1);
	isIE5 = ( (navigatorname.indexOf('explorer') != -1) && (navigatorversion.indexOf('MSIE 5') != -1) );
	isIE6 = ( (navigatorname.indexOf('explorer') != -1) && (navigatorversion.indexOf('MSIE 6') != -1) );
	isIE7 = ( (navigatorname.indexOf('explorer') != -1) && (navigatorversion.indexOf('MSIE 7') != -1) );
	isMoz = (navigatorname.indexOf('explorer') == -1);

	function adaptSize<%=executionId%>Funct() {
		// try to find out the height if the content of the iframe (it works if the iframe comes from the same
		// domain of the main page)
		try {
			// evaluates the iframe current height
			iframeEl = document.getElementById('iframeexec<%=executionId%>');
			offsetHeight = 0;
			clientHeight = 0;
			if(isIE5) {
				offsetHeight = iframeEl.contentWindow.document.body.scrollHeight;
				clientHeight = iframeEl.clientHeight;
			}
			if(isIE6) {
				offsetHeight = iframeEl.contentWindow.document.body.scrollHeight;
				clientHeight = iframeEl.clientHeight;
			}
			if(isIE7) {
				offsetHeight = iframeEl.contentWindow.document.body.scrollHeight;
				clientHeight = iframeEl.clientHeight;
			}
			if(isMoz) {
				offsetHeight = iframeEl.contentWindow.document.body.offsetHeight;
				clientHeight = iframeEl.clientHeight;
			}
			// adjusts current iframe height
			if (offsetHeight != clientHeight + 40) {
				heightFrame = offsetHeight + 40;
				iframeEl.style.height = heightFrame + 'px';
			}
			// saves the current iframe height into a variable
			iframeHeight<%=executionId%> = heightFrame;
			// adjusts parent iframe height
			if (window != top) {
				parentIFrame = parent.document.getElementsByTagName('iframe')[0];
				parentHeightFrame = heightFrame + 50;
				parentIFrame.style.height = parentHeightFrame + 'px';
			}
			setTimeout('adaptSize<%=requestIdentity%>Funct()', 500);

		} catch (err) { // in case the previous code generates an error the iframe is sized to the visible area
						// between iframe start position and footer start position
						
			iframeEl = document.getElementById('iframeexec<%=executionId%>');
			
			if (window != top) {
				try {
					parent.adaptSize<%=flowId%>Funct();
					iframeParent = parent.document.getElementById('iframeexec<%=flowId%>');
					newHeight = iframeParent.clientHeight;
					titleBar = document.getElementById('singleobjecttitlebar<%=executionId%>');
					if (titleBar != null && titleBar.style.display != 'none') {
						//alert('titleBar.clientHeight ' + titleBar.clientHeight);
						newHeight = newHeight - titleBar.clientHeight - 30;
					}
					iframeEl.style.height = newHeight + 'px';
				} catch (err) {
				}
				return;
			}

			// calculate height of the visible area
			heightVisArea = 0;
			if(isIE5) { heightVisArea = top.document.body.clientHeight; }
			if(isIE6) { heightVisArea = top.document.body.clientHeight; }
			if(isIE7) { heightVisArea = top.document.documentElement.clientHeight }
			if(isMoz) { heightVisArea = top.innerHeight; }
			// get the frame div object
			diviframeobj = document.getElementById('divIframe<%=executionId%>');
			// find the frame div position
			pos = findPos(diviframeobj);
						
			// calculate space below position frame div
			spaceBelowPos = heightVisArea - pos[1];
			// set height to the frame
			iframeEl.style.height = spaceBelowPos + 'px';
	
			// calculate height of the win area and height footer
			heightWinArea = 0;
			heightFooter = 0;
			if(isIE5) {
				//heightWinArea = window.document.body.scrollHeight;
				heightWinArea = document.body.scrollHeight;
				heightFooter = heightWinArea - heightVisArea;
			}
			if(isIE6) {
				//heightWinArea = window.document.body.scrollHeight;
				heightWinArea = document.body.scrollHeight;
				heightFooter = heightWinArea - heightVisArea;
			}
			if(isIE7) {
				//heightWinArea = window.document.body.offsetHeight;
				heightWinArea = document.body.offsetHeight;
				heightFooter = heightWinArea - heightVisArea;
			}
			if(isMoz) {
				//heightWinArea = window.document.body.offsetHeight;
				heightWinArea = document.body.offsetHeight;
				heightFooter = (heightWinArea - heightVisArea) + 15;
			}
	
			// calculate height of the frame
			heightFrame = heightVisArea - pos[1] - heightFooter;
			// set height to the frame
			iframeEl = document.getElementById('iframeexec<%=executionId%>');
			iframeEl.style.height = heightFrame + 'px';
		}
	}
	
	try {
		SbiJsInitializer.adaptSize<%=executionId%> = adaptSize<%=executionId%>Funct;
    } catch (err) {
		alert('Cannot resize the document view area');
	}






<script>
function getFramesArray() {
  return iframesNavigator;
}
function changeFrame<%=flowId%>(index) {
  tmp = top.getFramesArray();
  tmp.removeFrames('<%=flowId%>', index);
  refreshNavigationBar<%=flowId%>();
}
function refreshNavigationBar<%=flowId%>() {
  tmp = top.getFramesArray();
  nestedIFrames = tmp.getFrames('<%=flowId%>');
  html = "";
  for (count = 0; count < nestedIFrames.length; count++) {
    aFrame = nestedIFrames[count];
    aFrameLabel = aFrame[1];
		if (count == nestedIFrames.length - 1) {
			html += "&nbsp;" + aFrameLabel;
		} else {
			html += "&nbsp;<a href='javascript:changeFrame<%=flowId%>(" + count + ")'>" + aFrameLabel + "</a>";
		}
    if (count < frames.length - 1) {
      html += "&nbsp;&gt;";
    }
  }
  navBarDiv = top.document.getElementById("navigationBar<%=flowId%>");
  navBarDiv.innerHTML = html;
}
tmp = top.getFramesArray();
tmp.addFrame('<%=flowId%>', document.getElementById('button<%=executionId%>'), '<%=obj.getName()%>');
refreshNavigationBar<%=flowId%>();
</script>
--%>
<!-- ***************************************************************** -->
<!-- ***************************************************************** -->
<!-- **************** END IFRAMES NAVIGATOR ************************** -->
<!-- ***************************************************************** -->
<!-- ***************************************************************** -->
