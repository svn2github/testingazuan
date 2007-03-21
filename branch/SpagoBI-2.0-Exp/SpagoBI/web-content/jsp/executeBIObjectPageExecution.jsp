<%@ include file="/jsp/portlet_base.jsp"%>

<%@ page import="it.eng.spagobi.bo.BIObject,
                 java.util.List,
                 it.eng.spagobi.constants.ObjectsTreeConstants,
                 java.util.Iterator,
                 it.eng.spagobi.bo.Engine,
                 javax.portlet.PortletURL,
                 it.eng.spagobi.bo.Domain,
                 it.eng.spagobi.bo.BIObjectParameter,
                 it.eng.spagobi.bo.dao.IDomainDAO,
                 it.eng.spagobi.bo.dao.DAOFactory,
                 it.eng.spagobi.constants.SpagoBIConstants,
                 it.eng.spagobi.services.modules.BIObjectsModule,
                 it.eng.spagobi.services.modules.ExecuteBIObjectModule,
                 it.eng.spagobi.utilities.PortletUtilities,
                 it.eng.spago.navigation.LightNavigationManager,
                 org.apache.commons.httpclient.HttpClient,
                 org.apache.commons.httpclient.methods.PostMethod,
                 it.eng.spago.base.PortletAccess,
                 javax.portlet.PortletRequest, 
                 javax.portlet.PortletSession,
                 it.eng.spago.base.ApplicationContainer,
                 java.util.Map,
                 org.safehaus.uuid.UUIDGenerator,
                 org.safehaus.uuid.UUID,
                 it.eng.spagobi.utilities.GeneralUtilities,
                 it.eng.spagobi.managers.BIObjectNotesManager,
                 it.eng.spago.base.SessionContainer,
                 it.eng.spago.security.IEngUserProfile,
                 javax.portlet.PortletPreferences" %>
 

                 

<%
    // identity string for object of the page
    UUIDGenerator uuidGen  = UUIDGenerator.getInstance();
    UUID uuid = uuidGen.generateTimeBasedUUID();
    String requestIdentity = "request" + uuid.toString();  
    requestIdentity = requestIdentity.replaceAll("-", "");
    // get module response
    SourceBean moduleResponse = (SourceBean)aServiceResponse.getAttribute("ExecuteBIObjectModule");
	// get the BiObject from the response
    BIObject obj = (BIObject)moduleResponse.getAttribute(ObjectsTreeConstants.SESSION_OBJ_ATTR);
	// get the url of the engine
	Engine engine = obj.getEngine();
    String engineurl = engine.getUrl();
    // get the map of parameters dor execution call
    Map mapPars = (Map)moduleResponse.getAttribute(ObjectsTreeConstants.REPORT_CALL_URL);
   	// get the actor
    String actor = (String)aSessionContainer.getAttribute(SpagoBIConstants.ACTOR);
	
    
	// build the string of the title
    String title = "";
    title = obj.getName();
    String objDescr = obj.getDescription();
    if( (objDescr!=null) && !(objDescr.trim().equals("")) ) 
    	title += ": " + objDescr;
    
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
   	PortletURL backUrl = renderResponse.createActionURL();
	backUrl.setParameter("PAGE", "BIObjectsPage");
	backUrl.setParameter(SpagoBIConstants.ACTOR, actor);
	backUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_BACK_TO, "1");
	
	// build the refresh button
	PortletURL refreshUrl = renderResponse.createActionURL();
	refreshUrl.setParameter("PAGE", BIObjectsModule.MODULE_PAGE);
	refreshUrl.setParameter(SpagoBIConstants.ACTOR, actor);
	refreshUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
	
	
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
    
    

    // check if notes editor is able
    boolean edNoteAble = false;
    PortletRequest portReq = PortletUtilities.getPortletRequest();
	PortletPreferences prefs = portReq.getPreferences();
	String edNoteAbleStr = (String) prefs.getValue(SpagoBIConstants.PREFERENCE_NOTES_EDITOR_ABLE, "FALSE");
    if(edNoteAbleStr.equalsIgnoreCase("true")) {
    	edNoteAble = true;
    }
    // get the preference width dimension for notes editor
	int widthNotes = 600;
	String widthNoteEditorStr = (String) prefs.getValue(SpagoBIConstants.PREFERENCE_NOTES_EDITOR_WIDTH, "600");
	try{
		widthNotes = new Integer(widthNoteEditorStr).intValue();
	} catch (Exception e) {
		widthNotes = 600;
		SpagoBITracer.warning(SpagoBIConstants.NAME_MODULE, "executeBIObjectPageExecution", 
				              " ", "Error while recovering the width preference of the notes editor", e);
	}
	// get the preference height dimension for notes editor
	int heightNotes = 300;
	String heightNoteEditorStr = (String) prefs.getValue(SpagoBIConstants.PREFERENCE_NOTES_EDITOR_HEIGHT, "300");
	try{
		heightNotes = new Integer(heightNoteEditorStr).intValue();
	} catch (Exception e) {
		heightNotes = 300;
		SpagoBITracer.warning(SpagoBIConstants.NAME_MODULE, "executeBIObjectPageExecution", 
				              " ", "Error while recovering the height preference of the notes editor", e);
	}
	// check if the editor notes as to be opened automatically	
	boolean notesEditOpen = false;
	String notesEditOpenStr = (String) prefs.getValue(SpagoBIConstants.PREFERENCE_NOTES_EDITOR_OPEN, "false");
	if(notesEditOpenStr.equalsIgnoreCase("true")){
		notesEditOpen = true;
	}
	
	// urls for resources 
	String linkSbijs = renderResponse.encodeURL(renderRequest.getContextPath() + "/js/spagobi.js");
%>


   <%@page import="it.eng.spagobi.utilities.SpagoBITracer"%>
<SCRIPT language='JavaScript' src='<%=linkSbijs%>'></SCRIPT>
      

<% 
	// IF NOT SINGLE OBJECT MODALITY SHOW DEFAULT TITLE BAR
	if(!isSingleObjExec) {
%>

<table class='header-table-portlet-section'>
	<tr class='header-row-portlet-section'>
    	<td class='header-title-column-portlet-section' style='vertical-align:middle;'>
           &nbsp;&nbsp;&nbsp;<%=title%>
       </td>
       <td class='header-empty-column-portlet-section'>&nbsp;</td>
       <td class='header-button-column-portlet-section'>
           <a href='<%= backUrl.toString() %>'>
                 <img title='<spagobi:message key = "SBIDev.docConf.execBIObjectParams.backButt" />' 
                      class='header-button-image-portlet-section'
                      src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/back.png")%>' 
                      alt='<spagobi:message key = "SBIDev.docConf.execBIObjectParams.backButt" />' />
           </a>
       </td>
       <% if ((actor.equalsIgnoreCase(SpagoBIConstants.DEV_ACTOR)) || 
    		  (actor.equalsIgnoreCase(SpagoBIConstants.TESTER_ACTOR))) {
    	   	PortletURL formUrl = renderResponse.createActionURL();
  		    formUrl.setParameter("PAGE", ExecuteBIObjectModule.MODULE_PAGE);
  		   	formUrl.setParameter(SpagoBIConstants.ACTOR,actor );
		   	formUrl.setParameter(SpagoBIConstants.MESSAGEDET, 
		   			ObjectsTreeConstants.EXEC_CHANGE_STATE);
			formUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
    		  
    		  %>
       <form method='POST' action='<%= formUrl.toString() %>' id='changeStateForm'  name='changeStateForm'>
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
      			<input type='image' class='header-button-image-portlet-section' src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/updateState.png")%>' title='<spagobi:message key = "SBIDev.docConf.execBIObjectParams.updateButt" />' alt='<spagobi:message key = "SBIDev.docConf.execBIObjectParams.updateButt" />'/> 
      		</td>
        </form>
       <% } %>
       
       
       
       <!-- ************************************************************************* -->
       <!-- ******************** START BLOCK BUTTON NOTES EDITOR ******************** -->
       <!-- ************************************************************************* -->
       
       <%
        if(edNoteAble) {
       %>
		
        <td class='header-empty-column-portlet-section'>&nbsp;</td>
        <td class='header-button-column-portlet-section'>
           <a id="iconNotesEmpty<%=requestIdentity%>" href='javascript:opencloseNotesEditor<%=requestIdentity%>()'>
               <img width="22px" height="22px" title='<spagobi:message key = "sbi.execution.notes.opencloseeditor" />' 
                    src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/notesEmpty.jpg")%>' 
                    alt='<spagobi:message key = "sbi.execution.notes.opencloseeditor" />' />
           </a>
           <a id="iconNotesFilled<%=requestIdentity%>" style="display:none;" 
              href='javascript:opencloseNotesEditor<%=requestIdentity%>()'>
               <img width="22px" height="22px" title='<spagobi:message key = "sbi.execution.notes.opencloseeditor" />' 
                    src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/notes.jpg")%>' 
                    alt='<spagobi:message key = "sbi.execution.notes.opencloseeditor" />' />
           </a>
         </td>
              
       <% } %>
       
       <!-- ************************************************************************* -->
       <!-- ******************** END BLOCK BUTTON NOTES EDITOR ********************** -->
       <!-- ************************************************************************* -->
       
   </tr>
</table>


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
			<a style="text-decoration:none;" href='<%=refreshUrl.toString()%>'> 
				<img width="22px" height="22px"
					src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/updateState.png")%>' 
					name='refresh' 
					alt='<%=PortletUtilities.getMessage("SBIExecution.refresh", "messages")%>' 
					title='<%=PortletUtilities.getMessage("SBIExecution.refresh", "messages")%>' /> 
			</a>
		</td>
		<%
        if(edNoteAble) {
       %>
        <td class='header-empty-column-single-object-execution-portlet-section'>&nbsp;</td>
        <td class='header-button-column-single-object-execution-portlet-section'>
           <a id="iconNotesEmpty<%=requestIdentity%>" href='javascript:opencloseNotesEditor<%=requestIdentity%>()'>
               <img width="22px" height="22px" title='<spagobi:message key = "sbi.execution.notes.opencloseeditor" />' 
                    src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/notesEmpty.jpg")%>' 
                    alt='<spagobi:message key = "sbi.execution.notes.opencloseeditor" />' />
           </a>
           <a id="iconNotesFilled<%=requestIdentity%>" style="display:none;" 
              href='javascript:opencloseNotesEditor<%=requestIdentity%>()'>
               <img width="22px" height="22px" title='<spagobi:message key = "sbi.execution.notes.opencloseeditor" />' 
                    src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/notes.jpg")%>' 
                    alt='<spagobi:message key = "sbi.execution.notes.opencloseeditor" />' />
           </a>
        </td>
       <% } %>
</table>



<% } %>











<!-- ***************************************************************** -->
<!-- ***************************************************************** -->
<!-- **************** START BLOCK NOTES EDITOR *********************** -->
<!-- ***************************************************************** -->
<!-- ***************************************************************** -->

<%
	if(edNoteAble) {
		BIObjectNotesManager objectNotesManager = new BIObjectNotesManager();
		String execIdentifier = objectNotesManager.getExecutionIdentifier(obj);
		SessionContainer permSession = aSessionContainer.getPermanentContainer();
		IEngUserProfile userProfile = (IEngUserProfile)permSession.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
		String nameUser = (String)userProfile.getUserUniqueIdentifier();
		String linkFck = renderResponse.encodeURL(renderRequest.getContextPath() + "/js/FCKeditor/fckeditor.js");
		String linkProto = renderResponse.encodeURL(renderRequest.getContextPath() + "/js/prototype/javascripts/prototype.js");
		String linkProtoWin = renderResponse.encodeURL(renderRequest.getContextPath() + "/js/prototype/javascripts/window.js");
		String linkProtoEff = renderResponse.encodeURL(renderRequest.getContextPath() + "/js/prototype/javascripts/effects.js");
		String linkProtoDefThem = renderResponse.encodeURL(renderRequest.getContextPath() + "/js/prototype/themes/default.css");
		String linkProtoMacThem = renderResponse.encodeURL(renderRequest.getContextPath() + "/js/prototype/themes/mac_os_x.css");
		String linkProtoAlphaThem = renderResponse.encodeURL(renderRequest.getContextPath() + "/js/prototype/themes/alphacube.css");
		String linkProtoSpreadThem = renderResponse.encodeURL(renderRequest.getContextPath() + "/js/prototype/themes/spread.css");
%>

<SCRIPT language='JavaScript' src='<%=linkFck%>'></SCRIPT>
<script type="text/javascript" src="<%=linkProto%>"></script>
<script type="text/javascript" src="<%=linkProtoWin%>"></script>
<script type="text/javascript" src="<%=linkProtoEff%>"></script>
<link href="<%=linkProtoDefThem%>" rel="stylesheet" type="text/css"/>
<link href="<%=linkProtoMacThem%>" rel="stylesheet" type="text/css"/> 
<link href="<%=linkProtoAlphaThem%>" rel="stylesheet" type="text/css"/> 
<link href="<%=linkProtoSpreadThem%>" rel="stylesheet" type="text/css"/> 


<script type="text/javascript">
    
    var locked<%=requestIdentity%> = false;
    var xmlHttp<%=requestIdentity%> = null;
    var holdLockInterval<%=requestIdentity%> = null;  
     
     
    function reloadNotes<%=requestIdentity%>() {
       url="<%=GeneralUtilities.getSpagoBiContextAddress()%>/BIObjectNotesService?";
       pars = "task=getNotes&biobjid=<%=obj.getId()%>&execidentifier=<%=execIdentifier%>";
       new Ajax.Request(url,
          {
            method: 'post',
            parameters: pars,
            onSuccess: function(transport){
                            response = transport.responseText || "";
                            getNotesCallback<%=requestIdentity%>(response);
                        },
            onFailure: somethingWentWrong 
          }
        );
	   }
    
    
    function requireLock<%=requestIdentity%>() {
	      cleanError<%=requestIdentity%>();
	      if(locked<%=requestIdentity%>){
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
                            requireLockCallback<%=requestIdentity%>(response);
                        },
            onFailure: somethingWentWrong 
          }
        );
    }  
    
    function holdLock<%=requestIdentity%>() {
        url = "<%=GeneralUtilities.getSpagoBiContextAddress()%>/BIObjectNotesService";
		    pars = "task=holdLock&user=<%=nameUser%>&execidentifier=<%=execIdentifier%>"; 
        new Ajax.Request(url,
          {
            method: 'post',
            parameters: pars,
            onSuccess: function(transport){
                            response = transport.responseText || "";
                            holdLockCallback<%=requestIdentity%>(response);
                        },
            onFailure: somethingWentWrong 
          }
        );
    }
    
    
    function saveNotes<%=requestIdentity%>() {
	      cleanError<%=requestIdentity%>();
	      var editor = FCKeditorAPI.GetInstance('editorfckarea<%=requestIdentity%>') ;
	      xhtml = editor.GetXHTML(false);
	      if(!locked<%=requestIdentity%>){
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
                            saveNotesCallback<%=requestIdentity%>(response);
                        },
            onFailure: somethingWentWrong 
          }
        );
    }    
   
   
    function getNotesCallback<%=requestIdentity%>(response){      
        if(responseHasError<%=requestIdentity%>(response)) {
         		error = getResponseError<%=requestIdentity%>(response);
         		divError = document.getElementById('notesErrorMessage<%=requestIdentity%>');
         		divError.innerHTML = error;
         		return;
        }
        editor = FCKeditorAPI.GetInstance('editorfckarea<%=requestIdentity%>');
        while(response.indexOf("@-@-@")!=-1) {
          response = response.replace(/@-@-@/, "&");
        }
        editor.SetHTML(response, false);
        try{
          	editor.EditingArea.Document.body.contentEditable="false";
        }catch(e){
            // not IE
        }   
        fillAlertExistNotes<%=requestIdentity%>(response);
    } 
     
     
    function requireLockCallback<%=requestIdentity%>(response){ 
  			if(responseHasError<%=requestIdentity%>(response)) {
          		error = getResponseError<%=requestIdentity%>(response);
          		divError = document.getElementById('notesErrorMessage<%=requestIdentity%>');
          		divError.innerHTML = error;
          		return;
        }
        // editor locked
        locked<%=requestIdentity%>=true;
        document.getElementById('notesLockImg<%=requestIdentity%>').style.display='none';
        document.getElementById('notesSaveImg<%=requestIdentity%>').style.display='inline';
        document.getElementById('notesReloadImg<%=requestIdentity%>').style.display='none';
        editor = FCKeditorAPI.GetInstance('editorfckarea<%=requestIdentity%>') ;
        while(response.indexOf("@-@-@")!=-1) {
          response = response.replace(/@-@-@/, "&");
        }
        editor.SetHTML(response, false);
        try{
      	   	editor.EditingArea.Document.body.contentEditable="true";
        }catch(e){
           	// not IE
        }
        holdLockInterval<%=requestIdentity%> = setInterval("holdLock<%=requestIdentity%>()", 30000);
        fillAlertExistNotes<%=requestIdentity%>(response); 
	  } 
     
     
    function holdLockCallback<%=requestIdentity%>(){ 
  	 		// do nothing (the hold lock request is useful only to keep alive the lock)
	  } 
     
     
    function saveNotesCallback<%=requestIdentity%>(response){ 
      	locked<%=requestIdentity%> = false;
      	document.getElementById('notesLockImg<%=requestIdentity%>').style.display='inline';
      	document.getElementById('notesSaveImg<%=requestIdentity%>').style.display='none';
      	document.getElementById('notesReloadImg<%=requestIdentity%>').style.display='inline';
      	clearInterval(holdLockInterval<%=requestIdentity%>);
      	try{
      	   editor.EditingArea.Document.body.contentEditable="false";
      	}catch(e){
           // not IE
      	}
  			if(responseHasError<%=requestIdentity%>(response)) {
         		error = getResponseError<%=requestIdentity%>(response);
         		divError = document.getElementById('notesErrorMessage<%=requestIdentity%>');
         		divError.innerHTML = error;
         		return;
        } 
        reloadNotes<%=requestIdentity%>();
    }  
    

    function initializeNotes<%=requestIdentity%>() {
       reloadNotes<%=requestIdentity%>();
	   }  
	   
	   
	  function somethingWentWrong() {
        alert('Something went wrong ...');
    } 
    
    
    function responseHasError<%=requestIdentity%>(response){
    	if(response.indexOf('SpagoBIError:')!=-1) {
    		return true;
    	} else {
    	    return false;
    	}
    }
    
    function getResponseError<%=requestIdentity%>(response) {
    	error = response.substring(response.indexOf('SpagoBIError:')+13);
    	return error;
    }
    
    function cleanError<%=requestIdentity%>(){
    	divError = document.getElementById('notesErrorMessage<%=requestIdentity%>');
        divError.innerHTML = "";
    }
    
    
    function fillAlertExistNotes<%=requestIdentity%>(notes){
       iconempty = document.getElementById('iconNotesEmpty<%=requestIdentity%>');
       iconfilled = document.getElementById('iconNotesFilled<%=requestIdentity%>');
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




<div id="divNotes<%=requestIdentity%>" style="heigth:100%;width:100%;display:none;background-color:#efefde;overflow:hidden;">
  
  <div id="notesLockImg<%=requestIdentity%>" style="float:left;display:inline;padding:5px;">
      <a href="javascript:requireLock<%=requestIdentity%>()">
          <img title='<spagobi:message key = "sbi.execution.notes.lockeditor" />' 
               alt='<spagobi:message key = "sbi.execution.notes.lockeditor" />'
               src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/lock16.gif")%>' />
      </a>  
  </div>
  <div id="notesSaveImg<%=requestIdentity%>" style="float:left;display:none;padding:5px;">
      <a href="javascript:saveNotes<%=requestIdentity%>()">
          <img title='<spagobi:message key = "sbi.execution.notes.savenotes" />' 
          		alt='<spagobi:message key = "sbi.execution.notes.savenotes" />'
               src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/save16.gif")%>' />
      </a>   
  </div>
  <div id="notesReloadImg<%=requestIdentity%>" style="float:left;display:inline;padding:5px;">
      <a href="javascript:reloadNotes<%=requestIdentity%>()">
          <img title='<spagobi:message key = "sbi.execution.notes.reloadnotes" />' 
          		alt='<spagobi:message key = "sbi.execution.notes.reloadnotes" />'
               src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/reload16.gif")%>' />
      </a>   
  </div>
  <div id="notesErrorMessage<%=requestIdentity%>"  style="float:left;color:red;padding:5px;font-family:arial;font-size:11px;">
  
  </div>
  <div style="clear:left;"></div>
  

  <textarea id="editorfckarea<%=requestIdentity%>" name="editorfckarea<%=requestIdentity%>"></textarea>
  
  
</div>   








<script>
  
  var oFCKeditor<%=requestIdentity%> = new FCKeditor('editorfckarea<%=requestIdentity%>');
  oFCKeditor<%=requestIdentity%>.BasePath = "<%=GeneralUtilities.getSpagoBiContextAddress() + "/js/FCKeditor/"%>";
  oFCKeditor<%=requestIdentity%>.ToolbarSet = 'SbiObjectNotes';
  oFCKeditor<%=requestIdentity%>.Height = <%=heightNotes%> - 35;
  oFCKeditor<%=requestIdentity%>.Width = <%=widthNotes%> - 5;
  oFCKeditor<%=requestIdentity%>.ReplaceTextarea();
   
  var noteOpen<%=requestIdentity%> = false;
    
  function opencloseNotesEditor<%=requestIdentity%>() {
    if(noteOpen<%=requestIdentity%>) {
      // do nothing (close notes with window button)
    } else {
      noteOpen<%=requestIdentity%> = true;
      openNotes<%=requestIdentity%>(false);
    } 
  }
  
  function openNotes<%=requestIdentity%>(automatic) { 
    
    frameFcke<%=requestIdentity%> = document.getElementById('editorfckarea<%=requestIdentity%>___Frame');
    if(frameFcke<%=requestIdentity%>!=null) {
        frameFcke<%=requestIdentity%>.height= <%=heightNotes%> - 35;
        frameFcke<%=requestIdentity%>.width= <%=widthNotes%> - 5; 
    }
            
    diviframeobj = document.getElementById('divIframe<%=requestIdentity%>');
    pos = findPos(diviframeobj);        
    win<%=requestIdentity%> = null;
    if(automatic) {
       win<%=requestIdentity%> = new Window('win_notes_<%=requestIdentity%>', {className: "alphacube", title: "Notes for <%=title%>", top:pos[1], left:pos[0], width:<%=widthNotes%>, height:<%=heightNotes%>});
  	   win<%=requestIdentity%>.setDestroyOnClose();
       win<%=requestIdentity%>.setContent('divNotes<%=requestIdentity%>', false, false);    
       win<%=requestIdentity%>.show(false);
       win<%=requestIdentity%>.minimize();
    } else {
       win<%=requestIdentity%> = new Window('win_notes_<%=requestIdentity%>', {className: "alphacube", title: "Notes for <%=title%>", top:pos[1], left:pos[0], width:<%=widthNotes%>, height:<%=heightNotes%>});
  	   win<%=requestIdentity%>.setDestroyOnClose();
       win<%=requestIdentity%>.setContent('divNotes<%=requestIdentity%>', false, false); 
       win<%=requestIdentity%>.show(false);   

    }
  	
  	
    observerClose<%=requestIdentity%> = { 
      onClose: function(eventName, win) {  
        if(win == win<%=requestIdentity%>) { 
          noteOpen<%=requestIdentity%> = false;
          document.getElementById('divNotes<%=requestIdentity%>').style.display='none';
        } 
      }
    }
    Windows.addObserver(observerClose<%=requestIdentity%>);
  
    observerResize<%=requestIdentity%> = { 
      onResize: function(eventName, win) {  
        if(win == win<%=requestIdentity%>) { 
            heightwin = win.getSize().height;
            widthwin = win.getSize().width;
            frameFcke<%=requestIdentity%> = document.getElementById('editorfckarea<%=requestIdentity%>___Frame');
            frameFcke<%=requestIdentity%>.height=heightwin - 35;
            frameFcke<%=requestIdentity%>.width=widthwin - 5; 
        }
      }
    }
    Windows.addObserver(observerResize<%=requestIdentity%>);
   
  
    observerEndResize<%=requestIdentity%> = { 
      onEndResize: function(eventName, win) { 
        if(win == win<%=requestIdentity%>) {   
            heightwin = win.getSize().height;
            widthwin = win.getSize().width;
            frameFcke<%=requestIdentity%> = document.getElementById('editorfckarea<%=requestIdentity%>___Frame');
            frameFcke<%=requestIdentity%>.height=heightwin - 35;
            frameFcke<%=requestIdentity%>.width=widthwin - 5; 
        }
      }
    }
    Windows.addObserver(observerEndResize<%=requestIdentity%>);
  
  
    observerMaximize<%=requestIdentity%> = { 
      onMaximize: function(eventName, win) { 
        if(win == win<%=requestIdentity%>) {    
            heightwin = win.getSize().height;
            widthwin = win.getSize().width;
            frameFcke<%=requestIdentity%> = document.getElementById('editorfckarea<%=requestIdentity%>___Frame');
            frameFcke<%=requestIdentity%>.height=heightwin - 35;
            frameFcke<%=requestIdentity%>.width=widthwin - 5; 
        }
      }
    }
    Windows.addObserver(observerMaximize<%=requestIdentity%>); 
  
  
  }
  
  
  <% if(notesEditOpen) { %> 
  	try{
    	SbiJsInitializer.automaticOpenNotes<%=requestIdentity%> = function() {openNotes<%=requestIdentity%>(true);};
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
		
		function adaptSize<%=requestIdentity%>Funct() {
		      
          navigatorname = navigator.appName;
		      navigatorversion = navigator.appVersion;
          navigatorname = navigatorname.toLowerCase();
          isIE = false;
          isIE5 = false;
          isIE6 = false;
          isIE7 = false;
          isMoz = false;
          isIE = (navigatorname.indexOf('explorer') != -1);
          isIE5 = ( (navigatorname.indexOf('explorer') != -1) && (navigatorversion.indexOf('MSIE 5') != -1) );
          isIE6 = ( (navigatorname.indexOf('explorer') != -1) && (navigatorversion.indexOf('MSIE 6') != -1) );
          isIE7 = ( (navigatorname.indexOf('explorer') != -1) && (navigatorversion.indexOf('MSIE 7') != -1) );
          isMoz = (navigatorname.indexOf('explorer') == -1);
          
          // calculate height of the visible area
          heightVisArea = 0;
          if(isIE5) { heightVisArea = window.document.body.clientHeight; }
          if(isIE6) { heightVisArea = window.document.body.clientHeight; }
          if(isIE7) { heightVisArea = window.document.documentElement.clientHeight }
          if(isMoz) { heightVisArea = window.innerHeight; }

          // get the frame div object
          diviframeobj = document.getElementById('divIframe<%=requestIdentity%>');
          // find the frame div position
          pos = findPos(diviframeobj);
          // calculate space below position frame div
          spaceBelowPos = heightVisArea - pos[1];          
          // set height to the frame
			    iframeEl = document.getElementById('iframeexec<%=requestIdentity%>');
			    iframeEl.style.height = spaceBelowPos + 'px';
              
          // calculate height of the win area and height footer
          heightWinArea = 0;
          heightFooter = 0;
          if(isIE5) {
             heightWinArea = window.document.body.scrollHeight;
				     heightFooter = heightWinArea - heightVisArea;
          }
          if(isIE6) {
             heightWinArea = window.document.body.scrollHeight;
				     heightFooter = heightWinArea - heightVisArea;
          }
          if(isIE7) {
				     heightWinArea = window.document.body.offsetHeight;
             heightFooter = heightWinArea - heightVisArea;
          }
          if(isMoz) {
			   	   heightWinArea = window.document.body.offsetHeight;
			   	   heightFooter = (heightWinArea - heightVisArea) + 15; 
			    }
			    
			    // calculate height of the frame
			    heightFrame = heightVisArea - pos[1] - heightFooter;
			    // set height to the frame
			    iframeEl = document.getElementById('iframeexec<%=requestIdentity%>');
			    iframeEl.style.height = heightFrame + 'px';
		  }
		
		
		try{
         	SbiJsInitializer.adaptSize<%=requestIdentity%> = adaptSize<%=requestIdentity%>Funct;
    	} catch (err) {
       		alert('Cannot resize the document view area'); 
    	}
		
</script>
<%  
  }
%>   


<center>
<div id="divIframe<%=requestIdentity%>" style="width:100%;">
           
           <%
           		 String heightStr = "";
               if(heightSetted)
           			heightStr = "height:"+heightArea+"px;";	
           %> 
            
            <iframe id="iframeexec<%=requestIdentity%>" 
                    name="iframeexec<%=requestIdentity%>"
                    src="" 
                    style="width:100%;<%=heightStr%>" 
                    frameborder="0" ></iframe>
                 
                 
                                
         	<form name="formexecution<%=requestIdentity%>" 
                id='formexecution<%=requestIdentity%>' method="post" 
         	      action="<%=engineurl%>" 
         	      target='iframeexec<%=requestIdentity%>'>
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
         	    <input id="button<%=requestIdentity%>" type="submit" value="View Output"  style='display:inline;'/>
			       </center>
			     </form>
         
            <script>
              button = document.getElementById('button<%=requestIdentity%>');
              button.style.display='none';
              button.click();          
            </script>   
                
</div>
</center>       


<!-- ***************************************************************** -->
<!-- ***************************************************************** -->
<!-- **************** END BLOCK IFRAME ******************************* -->
<!-- ***************************************************************** -->
<!-- ***************************************************************** -->








<script>
    
    try{
      window.onload = SbiJsInitializer.initialize;
  	} catch (err) {
      alert('Cannot execute javascript initialize functions'); 
  	}     
              
</script>

