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
    UUIDGenerator uuidGen  = UUIDGenerator.getInstance();
    UUID uuid = uuidGen.generateTimeBasedUUID();
    String requestIdentity = "request" + uuid.toString();  
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
	int widthNoteEditor = 40;
		String widthNoteEditorStr = (String) prefs.getValue(SpagoBIConstants.PREFERENCE_NOTES_EDITOR_WIDTH, "40");
		try{
			widthNoteEditor = new Integer(widthNoteEditorStr).intValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		boolean notesEditOpen = false;
		String notesEditOpenStr = (String) prefs.getValue(SpagoBIConstants.PREFERENCE_NOTES_EDITOR_OPEN, "false");
		if(notesEditOpenStr.equalsIgnoreCase("true")){
			notesEditOpen = true;
		}
	
%>


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
           <a href='javascript:opencloseNotesEditor()'>
               <img title='<spagobi:message key = "sbi.execution.notes.opencloseeditor" />' 
                    src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/notes.jpg")%>' 
                    alt='<spagobi:message key = "sbi.execution.notes.opencloseeditor" />' />
           </a>
         </td>
       
       <td id="tdAlertNotesExists" class='tdAlertNotesExists' valign="middle" nowrap='true'>
           <div id="divAlertExistNotes" class="divAlertNotesExists">
           </div>
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
				<img width="20px" height="20px"
					src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/updateState.png")%>' 
					name='refresh' 
					alt='<%=PortletUtilities.getMessage("SBIExecution.refresh", "messages")%>' 
					title='<%=PortletUtilities.getMessage("SBIExecution.refresh", "messages")%>' /> 
			</a>
		</td>
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
%>


<SCRIPT language='JavaScript' src='<%=linkFck%>'>
</SCRIPT>
<script>
  
  var timeoutResize;
  var noteOpen = false;
  
  function opencloseNotesEditor() {
    if(noteOpen) {
      noteOpen = false;
      timeoutResize = setInterval("restrictNotesDiv()", 5);
    } else {
      noteOpen = true;
      timeoutResize = setInterval("enlargeNotesDiv()", 5);
    } 
  }
  
  function enlargeNotesDiv() {
    divIframe = document.getElementById('divIframe<%=requestIdentity%>');
    divNotes = document.getElementById('divNotes<%=requestIdentity%>');
    wDivIFrame = divIframe.style.width;
    wDivNotes = divNotes.style.width;
    wDivIFrame = wDivIFrame.substring(0, wDivIFrame.length - 1);
    wDivNotes = wDivNotes.substring(0, wDivNotes.length - 1);
    wDivIFrameNum = parseInt(wDivIFrame);
    wDivNotesNum = parseInt(wDivNotes);
    wDivIFrameNum = wDivIFrameNum - 1;
    wDivNotesNum = wDivNotesNum + 1;
    if(wDivIFrameNum<=<%=(100 - 2 - widthNoteEditor)%>){
      clearInterval(timeoutResize);
    }
    wDivIFrame = wDivIFrameNum + '%';
    wDivNotes = wDivNotesNum + '%';
    divIframe.style.width=wDivIFrame;
    divNotes.style.width=wDivNotes;
  }
  
  function restrictNotesDiv() {
    divIframe = document.getElementById('divIframe<%=requestIdentity%>');
    divNotes = document.getElementById('divNotes<%=requestIdentity%>');
    wDivIFrame = divIframe.style.width;
    wDivNotes = divNotes.style.width;
    wDivIFrame = wDivIFrame.substring(0, wDivIFrame.length - 1);
    wDivNotes = wDivNotes.substring(0, wDivNotes.length - 1);
    wDivIFrameNum = parseInt(wDivIFrame);
    wDivNotesNum = parseInt(wDivNotes);
    wDivIFrameNum = wDivIFrameNum + 1;
    wDivNotesNum = wDivNotesNum - 1;
    if(wDivIFrameNum>=98){
      clearInterval(timeoutResize);
    }
    wDivIFrame = wDivIFrameNum + '%';
    wDivNotes = wDivNotesNum + '%';
    divIframe.style.width=wDivIFrame;
    divNotes.style.width=wDivNotes;
  }
  
</script>


       
       
       
       
<div id="divNotes<%=requestIdentity%>" 
     style="background-color:#efefde;width:0%;float:left;overflow:hidden;">
  
  
  
  
  
  <script type="text/javascript">
    
    locked = false;
    var xmlHttp = null;
    var holdLockInterval = null;  
      
    
    
    function holdLock() {
    	   xmlHttp = GetXmlHttpObject();
    	   if(xmlHttp==null) {
			   alert ("Browser does not support HTTP Request");
			   return;
		    }
		   var url="<%=GeneralUtilities.getSpagoBiContextAddress()%>/BIObjectNotesService";
		   url=url+"?task=holdLock";
		   url=url+"&user=<%=nameUser%>";
		   url=url+"&execidentifier=<%=execIdentifier%>";
		   xmlHttp.onreadystatechange=stateChangedHoldLock; 
		   xmlHttp.open("GET",url,true);
		   xmlHttp.send(null); 
    }
      
      
      
    function saveNotes() {
	      cleanError();
	      var editor = FCKeditorAPI.GetInstance('editorfckarea') ;
	      xhtml = editor.GetXHTML(false);
	      if(!locked){
	    		return;
	    	}
	      xmlHttp = GetXmlHttpObject();
	      if(xmlHttp==null) {
			   alert ("Browser does not support HTTP Request");
			   return;
		  }
		  var url="<%=GeneralUtilities.getSpagoBiContextAddress()%>/BIObjectNotesService";
      	  var postdata ="task=saveNotes";
		  postdata=postdata+"&biobjid=<%=obj.getId()%>";
		  postdata=postdata+"&user=<%=nameUser%>";
		  postdata=postdata+"&execidentifier=<%=execIdentifier%>";
		  postdata=postdata+"&notes=" + xhtml;
		  xmlHttp.onreadystatechange=stateChangedSaveNotes; 
		  xmlHttp.open('POST',url,true);
      	  xmlHttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
      	  xmlHttp.setRequestHeader("Content-length", postdata.length);
      	  xmlHttp.setRequestHeader("Connection", "close");
		  xmlHttp.send(postdata); 
    }
    
    
    
    
    
    function requireLock() {
	       cleanError();
	       if(locked){
	    		return;
	    	}
    	   xmlHttp = GetXmlHttpObject();
    	   if(xmlHttp==null) {
			   alert ("Browser does not support HTTP Request");
			   return;
		    }
		   var url="<%=GeneralUtilities.getSpagoBiContextAddress()%>/BIObjectNotesService";
		   url=url+"?task=requireLock";
		   url=url+"&biobjid=<%=obj.getId()%>";
		   url=url+"&user=<%=nameUser%>";
		   url=url+"&execidentifier=<%=execIdentifier%>";
		   xmlHttp.onreadystatechange=stateChangedRequestLock; 
		   xmlHttp.open("GET",url,true);
		   xmlHttp.send(null); 
    }
    
    
    function reloadNotes() {
      		xmlHttp = GetXmlHttpObject();
      		if(xmlHttp==null) {
  			   alert ("Browser does not support HTTP Request");
  			   return;
  		  	}
    		var url="<%=GeneralUtilities.getSpagoBiContextAddress()%>/BIObjectNotesService";
    		url=url+"?task=getNotes";
    		url=url+"&biobjid=<%=obj.getId()%>";
    		url=url+"&execidentifier=<%=execIdentifier%>";
    		xmlHttp.onreadystatechange=stateChangedGetNotes; 
    		xmlHttp.open("GET",url,true);
    		xmlHttp.send(null); 
	}
    
    
    
     function FCKeditor_OnComplete( editorInstance ) {
      		xmlHttp = GetXmlHttpObject();
      		if(xmlHttp==null) {
  			   alert ("Browser does not support HTTP Request");
  			   return;
  		  	}
    		var url="<%=GeneralUtilities.getSpagoBiContextAddress()%>/BIObjectNotesService";
    		url=url+"?task=getNotes";
    		url=url+"&biobjid=<%=obj.getId()%>";
    		url=url+"&execidentifier=<%=execIdentifier%>";
    		xmlHttp.onreadystatechange=stateChangedGetNotes; 
    		xmlHttp.open("GET",url,true);
    		xmlHttp.send(null); 
	}
    
    
    
    function stateChangedHoldLock(){ 
  	 	if(xmlHttp.readyState==4 || xmlHttp.readyState=="complete") { 
  	 		// do nothing (the hold lock request is useful only to keep alive the lock)
  	 	}
	 } 

    
    
    function stateChangedSaveNotes(){ 
      	locked = false;
      	document.getElementById('notesLockImg').style.display='inline';
      	document.getElementById('notesSaveImg').style.display='none';
      	document.getElementById('notesReloadImg').style.display='inline';
      	clearInterval(holdLockInterval);
      	try{
      	   editor.EditingArea.Document.body.contentEditable="false";
      	}catch(e){
           // not IE
      	}
      	if(xmlHttp.readyState==4 || xmlHttp.readyState=="complete") { 
  			response=xmlHttp.responseText; 
  			if(responseHasError(response)) {
          		error = getResponseError(response);
          		divError = document.getElementById('notesErrorMessage');
          		divError.innerHTML = error;
          		return;
        	}
        	fillAlertExistNotes("Notes");	
       }
    }
    
    
    
    
    function stateChangedRequestLock(){ 
  	 	if(xmlHttp.readyState==4 || xmlHttp.readyState=="complete") { 
  			response=xmlHttp.responseText; 
  			if(responseHasError(response)) {
          		error = getResponseError(response);
          		divError = document.getElementById('notesErrorMessage');
          		divError.innerHTML = error;
          		return;
        	}
        	// editor locked
        	locked=true;
        	document.getElementById('notesLockImg').style.display='none';
        	document.getElementById('notesSaveImg').style.display='inline';
        	document.getElementById('notesReloadImg').style.display='none';
        	editor = FCKeditorAPI.GetInstance('editorfckarea') ;
            editor.SetHTML(response, false);
            try{
      	     	editor.EditingArea.Document.body.contentEditable="true";
          	}catch(e){
            	// not IE
          	}
          	holdLockInterval = setInterval("holdLock()", 30000);
          	fillAlertExistNotes(response);
  		}  
	 } 
    
   
    
    
    
    
    function stateChangedGetNotes(){ 
      	if(xmlHttp.readyState==4 || xmlHttp.readyState=="complete") { 
  			response=xmlHttp.responseText; 
      		if(responseHasError(response)) {
          		error = getResponseError(response);
          		divError = document.getElementById('notesErrorMessage');
          		divError.innerHTML = error;
          		return;
        	}
        	editor = FCKeditorAPI.GetInstance('editorfckarea') ;
          	editor.SetHTML(response, false);
          	try{
            	editor.EditingArea.Document.body.contentEditable="false";
          	}catch(e){
           		 // not IE
          	}   
          	fillAlertExistNotes(response);
       }
    }
    
    
    
    
    function GetXmlHttpObject(){ 
  		var objXMLHttp=null
  		if(window.XMLHttpRequest)	{
  			objXMLHttp=new XMLHttpRequest()
  		} else if (window.ActiveXObject) {
  			objXMLHttp=new ActiveXObject("Microsoft.XMLHTTP")
  		}
  		return objXMLHttp
  	}
    
    
    function responseHasError(response){
    	if(response.indexOf('SpagoBIError:')!=-1) {
    		return true;
    	} else {
    	    return false;
    	}
    }
    
    function getResponseError(response) {
    	error = response.substring(response.indexOf('SpagoBIError:')+13);
    	return error;
    }
    
    function cleanError(){
    	divError = document.getElementById('notesErrorMessage');
        divError.innerHTML = "";
    }
    
    
    function fillAlertExistNotes(notes){
       divalertNotes = document.getElementById('divAlertExistNotes');
       tdalertNotes = document.getElementById('tdAlertNotesExists');
       notes = notes.replace(/^\s*|\s*$/g,"");
       if(notes!=""){
        divalertNotes.innerHTML = "<spagobi:message key = "sbi.execution.notes.documentHasNotes" />";
       	tdalertNotes.style.width="150px";
       }
    }

  </script>
  
  
  
  
  
  
  <div id="notescloseImg" style="float:left;display:inline;padding:5px;">
      <a href="javascript:opencloseNotesEditor()">
          <img title='<spagobi:message key = "sbi.execution.notes.closeeditor" />' 
               alt='<spagobi:message key = "sbi.execution.notes.closeeditor" />'
               src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/prevPage.gif")%>' />
      </a>  
  </div>
  <div id="notesLockImg" style="float:left;display:inline;padding:5px;">
      <a href="javascript:requireLock()">
          <img title='<spagobi:message key = "sbi.execution.notes.lockeditor" />' 
               alt='<spagobi:message key = "sbi.execution.notes.lockeditor" />'
               src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/lock16.gif")%>' />
      </a>  
  </div>
  <div id="notesSaveImg" style="float:left;display:none;padding:5px;">
      <a href="javascript:saveNotes()">
          <img title='<spagobi:message key = "sbi.execution.notes.savenotes" />' 
          		alt='<spagobi:message key = "sbi.execution.notes.savenotes" />'
               src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/save16.gif")%>' />
      </a>   
  </div>
  <div id="notesReloadImg" style="float:left;display:inline;padding:5px;">
      <a href="javascript:reloadNotes()">
          <img title='<spagobi:message key = "sbi.execution.notes.reloadnotes" />' 
          		alt='<spagobi:message key = "sbi.execution.notes.reloadnotes" />'
               src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/reload16.gif")%>' />
      </a>   
  </div>
  <div id="notesErrorMessage"  style="float:left;color:red;padding:5px;font-family:arial;font-size:11px;">
  
  </div>
  <div style="clear:left;"></div>
  

  <textarea id="editorfckarea" name="editorfckarea"></textarea>
  
  
</div>       



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
				   src=""
                   frameborder=0  
			       width='100%' >
         	</iframe>       
                                
         	<form name="formexecution<%=requestIdentity%>" id='formexecution<%=requestIdentity%>' method="post" 
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
       


<!-- ***************************************************************** -->
<!-- ***************************************************************** -->
<!-- **************** END BLOCK IFRAME ******************************* -->
<!-- ***************************************************************** -->
<!-- ***************************************************************** -->









<!-- ***************************************************************** -->
<!-- **************** INITIALIZE NOTE EDITOR ************************* -->
<!-- ***************************************************************** -->
<%
	if(edNoteAble) {
%>
  <script type="text/javascript">
    	var oFCKeditor = new FCKeditor('editorfckarea');
    	oFCKeditor.BasePath = "<%=GeneralUtilities.getSpagoBiContextAddress() + "/js/FCKeditor/"%>";
      iframeEl = document.getElementById('iframeexec<%=requestIdentity%>');
      heightif = iframeEl.style.height;
      heightif = heightif + 'px';
      oFCKeditor.Height=heightif;
      oFCKeditor.ToolbarSet = 'SbiObjectNotes';
      //oFCKeditor.Create();
      oFCKeditor.ReplaceTextarea();
  </script>
<%
      if(notesEditOpen) {
%>    
        <script type="text/javascript">
            divIframe = document.getElementById('divIframe<%=requestIdentity%>');
            divNotes = document.getElementById('divNotes<%=requestIdentity%>');
            divIframe.style.width= <%=(100 - 2 - widthNoteEditor)%> + '%';
            divNotes.style.width=<%=widthNoteEditor%> + '%';
            noteOpen = true;
        </script>
<%    
      }
  }
%>

