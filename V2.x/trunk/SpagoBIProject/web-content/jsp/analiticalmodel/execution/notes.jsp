<%--
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
--%>

<%@page import="it.eng.spagobi.analiticalmodel.document.handlers.BIObjectNotesManager"%>
<%@page import="it.eng.spagobi.commons.utilities.ChannelUtilities"%>

<%
String widthNotes = "700";
String heightNotes = "300";
BIObjectNotesManager objectNotesManager = new BIObjectNotesManager();
String execIdentifier = objectNotesManager.getExecutionIdentifier(obj);
String nameUser = (String)userProfile.getUserUniqueIdentifier();
String linkFck = urlBuilder.getResourceLink(request, "/js/FCKeditor/fckeditor.js");
%>

<SCRIPT language='JavaScript' src='<%=linkFck%>'></SCRIPT>


<script type="text/javascript">

    var locked<%=uuid%> = false;
    var xmlHttp<%=uuid%> = null;
    var holdLockInterval<%=uuid%> = null;


    function reloadNotes<%=uuid%>() {
       url="<%=GeneralUtilities.getSpagoBiContextAddress()%>/BIObjectNotesService?";
       pars = "task=getNotes&biobjid=<%=obj.getId()%>&execidentifier=<%=execIdentifier%>";
       new Ajax.Request(url,
          {
            method: 'post',
            parameters: pars,
            onSuccess: function(transport){
                            response = transport.responseText || "";
                            getNotesCallback<%=uuid%>(response);
                        },
            onFailure: somethingWentWrong
          }
        );
	   }


    function requireLock<%=uuid%>() {
	      cleanError<%=uuid%>();
	      if(locked<%=uuid%>){
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
                            requireLockCallback<%=uuid%>(response);
                        },
            onFailure: somethingWentWrong
          }
        );
    }

    function holdLock<%=uuid%>() {
        url = "<%=GeneralUtilities.getSpagoBiContextAddress()%>/BIObjectNotesService";
		    pars = "task=holdLock&user=<%=nameUser%>&execidentifier=<%=execIdentifier%>";
        new Ajax.Request(url,
          {
            method: 'post',
            parameters: pars,
            onSuccess: function(transport){
                            response = transport.responseText || "";
                            holdLockCallback<%=uuid%>(response);
                        },
            onFailure: somethingWentWrong
          }
        );
    }


    function saveNotes<%=uuid%>() {
	      cleanError<%=uuid%>();
	      var editor = FCKeditorAPI.GetInstance('editorfckarea<%=uuid%>') ;
	      xhtml = editor.GetXHTML(false);
	      if(!locked<%=uuid%>){
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
                            saveNotesCallback<%=uuid%>(response);
                        },
            onFailure: somethingWentWrong
          }
        );
    }


    function getNotesCallback<%=uuid%>(response){
        if(responseHasError<%=uuid%>(response)) {
         		error = getResponseError<%=uuid%>(response);
         		divError = document.getElementById('notesErrorMessage<%=uuid%>');
         		divError.innerHTML = error;
         		return;
        }
        editor = FCKeditorAPI.GetInstance('editorfckarea<%=uuid%>');
        while(response.indexOf("@-@-@")!=-1) {
          response = response.replace(/@-@-@/, "&");
        }
        editor.SetHTML(response, false);
        try{
          	editor.EditingArea.Document.body.contentEditable="false";
        }catch(e){
            // not IE
        }
        fillAlertExistNotes<%=uuid%>(response);
    }


    function requireLockCallback<%=uuid%>(response){
  			if(responseHasError<%=uuid%>(response)) {
          		error = getResponseError<%=uuid%>(response);
          		divError = document.getElementById('notesErrorMessage<%=uuid%>');
          		divError.innerHTML = error;
          		return;
        }
        // editor locked
        locked<%=uuid%>=true;
        document.getElementById('notesLockImg<%=uuid%>').style.display='none';
        document.getElementById('notesSaveImg<%=uuid%>').style.display='inline';
        document.getElementById('notesReloadImg<%=uuid%>').style.display='none';
        editor = FCKeditorAPI.GetInstance('editorfckarea<%=uuid%>') ;
        while(response.indexOf("@-@-@")!=-1) {
          response = response.replace(/@-@-@/, "&");
        }
        editor.SetHTML(response, false);
        try{
      	   	editor.EditingArea.Document.body.contentEditable="true";
        }catch(e){
           	// not IE
        }
        holdLockInterval<%=uuid%> = setInterval("holdLock<%=uuid%>()", 30000);
        fillAlertExistNotes<%=uuid%>(response);
	  }


    function holdLockCallback<%=uuid%>(){
  	 		// do nothing (the hold lock request is useful only to keep alive the lock)
	  }


    function saveNotesCallback<%=uuid%>(response){
      	locked<%=uuid%> = false;
      	document.getElementById('notesLockImg<%=uuid%>').style.display='inline';
      	document.getElementById('notesSaveImg<%=uuid%>').style.display='none';
      	document.getElementById('notesReloadImg<%=uuid%>').style.display='inline';
      	clearInterval(holdLockInterval<%=uuid%>);
      	try{
      	   editor.EditingArea.Document.body.contentEditable="false";
      	}catch(e){
           // not IE
      	}
  			if(responseHasError<%=uuid%>(response)) {
         		error = getResponseError<%=uuid%>(response);
         		divError = document.getElementById('notesErrorMessage<%=uuid%>');
         		divError.innerHTML = error;
         		return;
        }
        reloadNotes<%=uuid%>();
    }


    function initializeNotes<%=uuid%>() {
       reloadNotes<%=uuid%>();
	   }


	  function somethingWentWrong() {
        alert('Something went wrong ...');
    }


    function responseHasError<%=uuid%>(response){
    	if(response.indexOf('SpagoBIError:')!=-1) {
    		return true;
    	} else {
    	    return false;
    	}
    }

    function getResponseError<%=uuid%>(response) {
    	error = response.substring(response.indexOf('SpagoBIError:')+13);
    	return error;
    }

    function cleanError<%=uuid%>(){
    	divError = document.getElementById('notesErrorMessage<%=uuid%>');
        divError.innerHTML = "";
    }


    function fillAlertExistNotes<%=uuid%>(notes){
       iconempty = document.getElementById('iconNotesEmpty<%=uuid%>');
       iconfilled = document.getElementById('iconNotesFilled<%=uuid%>');
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




<div id="divNotes<%=uuid%>" style="heigth:100%;width:100%;display:none;background-color:#efefde;overflow:hidden;">

  <div id="notesLockImg<%=uuid%>" style="float:left;display:inline;padding:5px;">
      <a href="javascript:requireLock<%=uuid%>()">
          <img title='<spagobi:message key = "sbi.execution.notes.lockeditor" />'
               alt='<spagobi:message key = "sbi.execution.notes.lockeditor" />'
               src='<%= urlBuilder.getResourceLink(request, "/img/lock16.gif")%>' />
      </a>
  </div>
  <div id="notesSaveImg<%=uuid%>" style="float:left;display:none;padding:5px;">
      <a href="javascript:saveNotes<%=uuid%>()">
          <img title='<spagobi:message key = "sbi.execution.notes.savenotes" />'
          		alt='<spagobi:message key = "sbi.execution.notes.savenotes" />'
               src='<%= urlBuilder.getResourceLink(request, "/img/save16.gif")%>' />
      </a>
  </div>
  <div id="notesReloadImg<%=uuid%>" style="float:left;display:inline;padding:5px;">
      <a href="javascript:reloadNotes<%=uuid%>()">
          <img title='<spagobi:message key = "sbi.execution.notes.reloadnotes" />'
          		alt='<spagobi:message key = "sbi.execution.notes.reloadnotes" />'
               src='<%= urlBuilder.getResourceLink(request, "/img/reload16.gif")%>' />
      </a>
  </div>
  <div id="notesErrorMessage<%=uuid%>"  style="float:left;color:red;padding:5px;font-family:arial;font-size:11px;">

  </div>
  <div style="clear:left;"></div>


  <textarea id="editorfckarea<%=uuid%>" name="editorfckarea<%=uuid%>"></textarea>


</div>



<script>

  var oFCKeditor<%=uuid%> = new FCKeditor('editorfckarea<%=uuid%>');
  oFCKeditor<%=uuid%>.BasePath = "<%=ChannelUtilities.getSpagoBIContextName(request) + "/js/FCKeditor/"%>";
  oFCKeditor<%=uuid%>.ToolbarSet = 'SbiObjectNotes';
  oFCKeditor<%=uuid%>.Height = <%=heightNotes%> - 35;
  oFCKeditor<%=uuid%>.Width = <%=widthNotes%> - 5;
  oFCKeditor<%=uuid%>.ReplaceTextarea();

  var noteOpen<%=uuid%> = false;

  function opencloseNotesEditor<%=uuid%>() {
    if(noteOpen<%=uuid%>) {
      // do nothing (close notes with window button)
    } else {
      noteOpen<%=uuid%> = true;
      openNotes<%=uuid%>(false);
    }
  }

  function openNotes<%=uuid%>(automatic) {
    frameFcke<%=uuid%> = document.getElementById('editorfckarea<%=uuid%>___Frame');
    if(frameFcke<%=uuid%>!=null) {
        frameFcke<%=uuid%>.height= <%=heightNotes%> - 35;
        frameFcke<%=uuid%>.width= <%=widthNotes%> - 5;
    }
    diviframeobj = document.getElementById('divIframe<%=uuid%>');
    //pos = findPos(diviframeobj);
    win<%=uuid%> = null;
  
    
    if(automatic) {
       //win<%=uuid%> = new Window('win_notes_<%=uuid%>', {className: "alphacube", title: "Notes for <%=title%>", top:pos[1], left:pos[0], width:<%=widthNotes%>, height:<%=heightNotes%>, hideEffect:Element.hide, showEffect:Element.show});
       win<%=uuid%> = new Window('win_notes_<%=uuid%>', {className: "alphacube", title: "Notes for <%=title%>", width:<%=widthNotes%>, height:<%=heightNotes%>, hideEffect:Element.hide, showEffect:Element.show});
  	   win<%=uuid%>.setDestroyOnClose();
       win<%=uuid%>.setContent('divNotes<%=uuid%>', false, false);
       //win<%=uuid%>.show(false);
       win<%=uuid%>.showCenter(false);
       win<%=uuid%>.minimize();
    } else {
       //win<%=uuid%> = new Window('win_notes_<%=uuid%>', {className: "alphacube", title: "Notes for <%=title%>", top:pos[1], left:pos[0], width:<%=widthNotes%>, height:<%=heightNotes%>, hideEffect:Element.hide, showEffect:Element.show});
       win<%=uuid%> = new Window('win_notes_<%=uuid%>', {className: "alphacube", title: "Notes for <%=title%>", width:<%=widthNotes%>, height:<%=heightNotes%>, hideEffect:Element.hide, showEffect:Element.show});
  	   win<%=uuid%>.setDestroyOnClose();
       win<%=uuid%>.setContent('divNotes<%=uuid%>', false, false);
       //win<%=uuid%>.show();
       win<%=uuid%>.showCenter();
    }


    observerClose<%=uuid%> = {
      onClose: function(eventName, win) {
        if(win == win<%=uuid%>) {
          noteOpen<%=uuid%> = false;
          document.getElementById('divNotes<%=uuid%>').style.display='none';
        }
      }
    }
    Windows.addObserver(observerClose<%=uuid%>);

    observerResize<%=uuid%> = {
      onResize: function(eventName, win) {
        if(win == win<%=uuid%>) {
            heightwin = win.getSize().height;
            widthwin = win.getSize().width;
            frameFcke<%=uuid%> = document.getElementById('editorfckarea<%=uuid%>___Frame');
            frameFcke<%=uuid%>.height=heightwin - 35;
            frameFcke<%=uuid%>.width=widthwin - 5;
        }
      }
    }
    Windows.addObserver(observerResize<%=uuid%>);


    observerEndResize<%=uuid%> = {
      onEndResize: function(eventName, win) {
        if(win == win<%=uuid%>) {
            heightwin = win.getSize().height;
            widthwin = win.getSize().width;
            frameFcke<%=uuid%> = document.getElementById('editorfckarea<%=uuid%>___Frame');
            frameFcke<%=uuid%>.height=heightwin - 35;
            frameFcke<%=uuid%>.width=widthwin - 5;
        }
      }
    }
    Windows.addObserver(observerEndResize<%=uuid%>);


    observerMaximize<%=uuid%> = {
      onMaximize: function(eventName, win) {
        if(win == win<%=uuid%>) {
            heightwin = win.getSize().height;
            widthwin = win.getSize().width;
            frameFcke<%=uuid%> = document.getElementById('editorfckarea<%=uuid%>___Frame');
            frameFcke<%=uuid%>.height=heightwin - 35;
            frameFcke<%=uuid%>.width=widthwin - 5;
        }
      }
    }
    Windows.addObserver(observerMaximize<%=uuid%>);


  }


  <% if(false) { // TODO controllare a che serve, se serve per aprire automaticamente l'editor %>
  	try{
    	SbiJsInitializer.automaticOpenNotes<%=uuid%> = function() {openNotes<%=uuid%>(true);};
    } catch (err) {
        alert('Cannot open automatically the editor note');
    }
  <% } %>
</script>