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

<%@ page import="it.eng.spago.navigation.LightNavigationManager,
				it.eng.spagobi.importexport.ImportExportConstants" %>
<%@page import="it.eng.spagobi.importexport.ImportResultInfo"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.HashMap"%>
<%@page import="it.eng.spagobi.utilities.ChannelUtilities"%>
<%@page import="it.eng.spagobi.importexport.to.AssociationFile"%>

<%  
	SourceBean moduleResponse = (SourceBean)aServiceResponse.getAttribute("TreeObjectsModule"); 
	String exportFilePath = (String)aServiceRequest.getAttribute(ImportExportConstants.EXPORT_FILE_PATH);
	ImportResultInfo iri = (ImportResultInfo)aServiceRequest.getAttribute(ImportExportConstants.IMPORT_RESULT_INFO);

   	Map backUrlPars = new HashMap();
	backUrlPars.put("ACTION_NAME", "START_ACTION");
	backUrlPars.put("PUBLISHER_NAME", "LoginSBISettingsPublisher");
	backUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_RESET, "true");
	String backUrl = urlBuilder.getUrl(request, backUrlPars);
   
	Map formExportUrlPars = new HashMap();
	formExportUrlPars.put("PAGE", "ImportExportPage");
	formExportUrlPars.put("MESSAGEDET", "Export");
	String formExportUrl = urlBuilder.getUrl(request, formExportUrlPars);
   
	Map formImportUrlPars = new HashMap();
	if (ChannelUtilities.isPortletRunning()) {
		formImportUrlPars.put("PAGE", "ImportExportPage");
	}
	String formImportUrl = urlBuilder.getUrl(request, formImportUrlPars);
  
	String downloadUrl = ChannelUtilities.getSpagoBIContextName(request);
	downloadUrl += "/servlet/AdapterHTTP?ACTION_NAME=DOWNLOAD_FILE_ACTION";
	if((exportFilePath!=null) && !exportFilePath.trim().equalsIgnoreCase("") ) {
		downloadUrl += "&OPERATION=download&PATH="+  exportFilePath;
	}
   
%>

<table class='header-table-portlet-section'>
	<tr class='header-row-portlet-section'>
		<td class='header-title-column-portlet-section' style='vertical-align:middle;padding-left:5px;'>
			<spagobi:message key = "SBISet.importexport" bundle="component_impexp_messages"/>
		</td>
		<%if(ChannelUtilities.isPortletRunning()) { %>
		<td class='header-empty-column-portlet-section'>&nbsp;</td>
		<td class='header-button-column-portlet-section'>
			<a href='<%=backUrl%>'> 
      			<img class='header-button-image-portlet-section' 
      				 title='<spagobi:message key = "Sbi.back" bundle="component_impexp_messages" />' 
      				 src='<%=urlBuilder.getResourceLink(request, "/components/importexport/img/back.png")%>' 
      				 alt='<spagobi:message key = "Sbi.back"  bundle="component_impexp_messages"/>' />
			</a>
		</td>
		<% } %>
	</tr>
</table>

<script>
	function submitExportForm() {
		var divprog = document.getElementById('divProgress');
		divprog.style.display='inline';
		document.getElementById('exportForm').submit();
		var divprog = document.getElementById('divDownload');
		divprog.style.display='none';
	}
	
	function submitDownloadForm(actionurl) {
		downform = document.getElementById('downForm');
		var divdown = document.getElementById('divDownload');
		divdown.style.display='none';
		downform.submit();
	}
	
</script>




<div class="div_background_no_img">


	<spagobi:error/>

 
  <form method='POST' action='<%=formExportUrl%>' id='exportForm' name='exportForm'> 
	<div style="float:left;width:50%;" class="div_detail_area_forms">
		<div class='portlet-section-header' style="float:left;width:88%;">	
				<spagobi:message key = "SBISet.export" bundle="component_impexp_messages"/>
		</div>
		<div style="float:left;width:10%;">
		  <center>
			 <a href="javascript:submitExportForm()">
					<img src= '<%=urlBuilder.getResourceLink(request, "/components/importexport/img/importexport32.gif") %>'
						title='<spagobi:message key = "SBISet.export" bundle="component_impexp_messages"/>' 
						alt='<spagobi:message key = "SBISet.export" bundle="component_impexp_messages"/>' />
				</a>
		  </center>
		</div>
		<div id="divProgress"  
			 style="clear:left;margin-left:15px;padding-top:15px;display:none;color:#074B88;">
			<spagobi:message key = "SBISet.importexport.opProg" bundle="component_impexp_messages"/>
		</div>
		<div id="divDownload" 
			 style="clear:left;margin-left:15px;padding-top:15px;display:none;color:#074B88;">	 
			<spagobi:message key = "SBISet.importexport.opComplete"  bundle="component_impexp_messages"/>
			<a style='text-decoration:none;color:#CC0000;' href="javascript:submitDownloadForm()">
				<spagobi:message key = "Sbi.download" bundle="component_impexp_messages"/>
			</a>
		</div>
		<div style="clear:left;margin-left:15px;padding-top:10px;">
			<spagobi:message key = "SBISet.importexport.nameExp" bundle="component_impexp_messages"/>
			: 
			<input type="text" name="exportFileName" size="30" />
            <br/>
            <input type="checkbox" name="exportSubObj" />
			<spagobi:message key = "SBISet.importexport.expSubView" bundle="component_impexp_messages"/>
			<br/>
			<input type="checkbox" name="exportSnapshots" />	
			<spagobi:message key = "SBISet.importexport.expSnapshots" bundle="component_impexp_messages"/>
		</div>
		<div style="clear:left;margin-bottom:10px;">
			<spagobi:treeObjects moduleName="TreeObjectsModule"  
				htmlGeneratorClass="it.eng.spagobi.presentation.treehtmlgenerators.AdminExportTreeHtmlGenerator" />
		</div>
	</div>
	</form>


























	<form method='POST' action='<%=downloadUrl%>' id='downForm' name='downForm'>
	</form>

    <form method='POST' action='<%=formImportUrl%>' id='importForm' name='importForm' enctype="multipart/form-data">
	<% if(ChannelUtilities.isWebRunning()) { %>
		<input type="hidden" name="PAGE" value="ImportExportPage" />
	<% } %>
 	<div style="float:left;width:45%" class="div_detail_area_forms">
		<div class='portlet-section-header' style="float:left;width:78%;">
				<spagobi:message key = "SBISet.import" bundle="component_impexp_messages"/>
		</div>
		<div style="float:left;width:20%;">
		  <center>
			<a class='link_without_dec' href="javascript:document.getElementById('importForm').submit()">
					<img src= '<%= urlBuilder.getResourceLink(request, "/components/importexport/img/importexport32.gif") %>'
						title='<spagobi:message key = "SBISet.import" bundle="component_impexp_messages"/>' 
						alt='<spagobi:message key = "SBISet.import" bundle="component_impexp_messages"/>' />
			</a>
			&nbsp;
			<a class='link_without_dec' href="javascript:showAssList('MANAGE')">
					<img src= '<%= urlBuilder.getResourceLink(request, "/img/association32.jpg") %>'
						width="28px" height="28px"
						title='<spagobi:message key = "impexp.manageAss" bundle="component_impexp_messages"/>' 
						alt='<spagobi:message key = "impexp.manageAss" bundle="component_impexp_messages"/>' />
			</a>
			</center>
		</div>
		<div style="clear:left;margin-bottom:10px;padding-top:10px;">
			<table>
				<tr>
					<td>
						<spagobi:message key = "SBISet.importexport.fileArchive" bundle="component_impexp_messages"/>
						:
					</td>
					<td> 
						<input type="file"  name="exportedArchive" />
					</td>
				</tr>
				<tr height='20px'><td colspan="2"></td></tr>
				<tr>
					<td colspan="2" />
						<fieldset class='fieldset'>
							<legend class='form_legend'>
								<spagobi:message key = "impexp.Associations" bundle="component_impexp_messages"/>
							</legend>
							<input type="radio"  name="importAssociationKind" CHECKED value="noassociations"  
						       onclick="document.getElementById('rowChoseAssFile').style.display='none'"/>
						    <spagobi:message key = "impexp.withoutAss" bundle="component_impexp_messages"/>
						    <br/>
						    <input type="radio"  name="importAssociationKind" value="predefinedassociations"
						       onclick="document.getElementById('rowChoseAssFile').style.display='inline' "/>
						    <spagobi:message key = "impexp.mandatoryAss" bundle="component_impexp_messages"/>
						    <br/>     
						    <input type="radio"  name="importAssociationKind" value="defaultassociations"
						       onclick="document.getElementById('rowChoseAssFile').style.display='inline'" /> 
						    <spagobi:message key = "impexp.defaultAss" bundle="component_impexp_messages"/>
						    <br/>
						    <br/>
						    <table id="rowChoseAssFile" style='display:none;'>
						    	<tr height='25px'>
						    		<td>
						    			<spagobi:message key = "impexp.savedAss" bundle="component_impexp_messages"/>
										: &nbsp;&nbsp;
						    		</td>
						    		<td>
						    			<input type="text" id="textReadOnlyAssName" name="textReadOnlyAssName" readonly value="" />
						    			<input type="hidden" id="hidAssId" name="hidAssId" value="" />
						    			<a class='link_without_dec' href="javascript:showAssList('SELECT')">
						    				<img src= '<%= urlBuilder.getResourceLink(request, "/img/detail.gif") %>'
												 title='<spagobi:message key = "impexp.selectFromList" bundle="component_impexp_messages"/>' 
												alt='<spagobi:message key = "impexp.selectFromList" bundle="component_impexp_messages"/>' />
										</a>
						    		</td>
						    	</tr>
						    	<tr height='25px'>
						    		<td>
						    			<spagobi:message key = "SBISet.importexport.fileAssociation" bundle="component_impexp_messages"/>
										: &nbsp;&nbsp;
						    		</td>
						    		<td>
						    			<input type="file"  name="associationsFile"  />
						    		</td>
						    	</tr>
						    </table>
						</fieldset>
					</td>
				</tr>
			</table>
			<input type='hidden' name='MESSAGEDET' value='Import' />
			
		</div>
		
		<%
		if(iri!=null) {
		%>	
		<div id="divImportResult" style="clear:left;color:#074B88;">	 		 
			<%
				String pathLogFile = iri.getPathLogFile();
				if( (pathLogFile!=null) && !pathLogFile.equals("") ) {	
					 String downloadLogUrl = ChannelUtilities.getSpagoBIContextName(request);
					 downloadLogUrl += "/servlet/AdapterHTTP?ACTION_NAME=DOWNLOAD_FILE_ACTION";
					 downloadLogUrl += "&OPERATION=downloadLog&PATH=" + pathLogFile;	
			%>
			<spagobi:message key = "SBISet.importexport.opComplete" bundle="component_impexp_messages"/>
			<ul>
				<li>
					<a style='text-decoration:none;color:#CC0000;' href='<%=downloadLogUrl%>'>
						<spagobi:message key = "Sbi.downloadLog" bundle="component_impexp_messages"/>
					</a>
				</li>
			<% 	}
				String pathAssFile = iri.getPathAssociationsFile();
				if( (pathAssFile!=null) && !pathAssFile.equals("") ) {	
					 String downloadAssUrl = ChannelUtilities.getSpagoBIContextName(request);
					 downloadAssUrl += "/servlet/AdapterHTTP?ACTION_NAME=DOWNLOAD_FILE_ACTION";
					 downloadAssUrl += "&OPERATION=downloadLog&PATH=" + pathAssFile;
		    %>
				<li>
					<a style='text-decoration:none;color:#CC0000;' href='<%=downloadAssUrl%>'>
						<spagobi:message key = "Sbi.downloadAss" bundle="component_impexp_messages"/>
					</a>
				</li>
				<li>
					<a style='text-decoration:none;color:#CC0000;' href='javascript:openCloseSaveAssForm()'>
						<spagobi:message key = "impexp.saveAss" bundle="component_impexp_messages"/>
					</a>
				</li>
			</ul>
			<%	
				}
				Map manualTasks = iri.getManualTasks();
				if(!manualTasks.isEmpty()) {
			%>
			<br/>
			<br/>
			<span class="portlet-form-field-label" style="color:#CC0000;">
				<spagobi:message key = "impexp.manualtask.exists" bundle="component_impexp_messages"/>
			</span>
			<ul>
			<% 	
					Set keys = manualTasks.keySet();
			    	Iterator keysIter = keys.iterator();
			    	while(keysIter.hasNext()){
			    		String key = (String)keysIter.next();
			    		String path = (String)manualTasks.get(key);
			    		String downloadManualTaskUrl = ChannelUtilities.getSpagoBIContextName(request);
			    		downloadManualTaskUrl += "/servlet/AdapterHTTP?ACTION_NAME=DOWNLOAD_FILE_ACTION";
			    		downloadManualTaskUrl += "&OPERATION=downloadManualTask&PATH=" + path;	
		    %>
		    	<li>
		    		<a style='text-decoration:none;color:#074B88;font-size:9px;' href='<%=downloadManualTaskUrl%>'>
						<%=key%>
					</a>
		    	</li>
		    <%
			   	 	}
			%>
			</ul>
			<%
				}
			%>
		</div>
		<%
		}
		%>
	</div>
	</form>


	<div style="clear:left;">
			&nbsp;
	</div>


<%
	if((exportFilePath!=null) && !exportFilePath.trim().equalsIgnoreCase("") ) {
%>
	<script>
		var divprog = document.getElementById('divProgress');
		divprog.style.display='none';
		var divprog = document.getElementById('divDownload');
		divprog.style.display='inline';
	</script>
<% 
	}
%>




























	<%
	AssociationFile assFile = (AssociationFile) aServiceRequest.getAttribute(ImportExportConstants.IMPORT_ASSOCIATION_FILE);
	String associationName = (assFile != null) ? assFile.getName() : "";
	String associationDescription = (assFile != null) ? assFile.getDescription() : "";
	%>
	
	<div id="divSaveAssFileForm" name="divSaveAssFileForm" style="display:none;" >
		<table>
			<tr height='25px'>
				<td>&nbsp;&nbsp;
					<spagobi:message key = "impexp.name" bundle="component_impexp_messages"/>:
				</td>
				<td><input type="text" name="NAME" id="nameNewAssToSave" value="<%=associationName%>" /></td>
			</tr>
			<tr height='25px'>
				<td>&nbsp;&nbsp;
					<spagobi:message key = "impexp.description" bundle="component_impexp_messages"/>:
				</td>
				<td><input type="text" name="DESCRIPTION" id="descriptionNewAssToSave"  value="<%=associationDescription%>"/></td>
			</tr>
			<tr height='45px' valign='middle'>
				<td>&nbsp;</td>
				<td>
				   <a class='link_without_dec' href='javascript:checkIfExists()'>
					   <img src= '<%= urlBuilder.getResourceLink(request, "/img/Save.gif") %>'
							title='<spagobi:message key = "impexp.save" bundle="component_impexp_messages"/>' 
							alt='<spagobi:message key = "impexp.save" bundle="component_impexp_messages"/>' />
					</a>
				</td>
			</tr>
			<tr height='45px' valign='middle'>
				<td>&nbsp;</td>
				<td><div id="divshowresultsave">&nbsp;</div></td>
			</tr>
		</table>
	</div>
	

	<script>
	
	    winlistass = null;

		function showAssList(modality) {
			if(winlistass!=null) return;
			getlisturl = "<%=ChannelUtilities.getSpagoBIContextName(request)%>";
			getlisturl += "/servlet/AdapterHTTP?";
			getlisturl += "ACTION_NAME=MANAGE_IMPEXP_ASS_ACTION&MESSAGE=GET_ASSOCIATION_FILE_LIST";
			getlisturl += "&language=<%=locale.getLanguage()%>&country=<%=locale.getCountry()%>";
			getlisturl += "&MODALITY=" + modality;
			winlistass = new Window('win_list_ass', {className: "alphacube", title: "<spagobi:message key = "impexp.listAssFile" bundle="component_impexp_messages"/>", width:550, height:350, hideEffect:Element.hide, showEffect:Element.show, destroyOnClose:true});
       		winlistass.setURL(getlisturl);
       		winlistass.setDestroyOnClose();
       		winlistass.showCenter();
	 	} 
	    
	    
	    winsaveass = null;
	    
	    function openCloseSaveAssForm() {
			winsaveass = new Window('win_save_ass', {className: "alphacube", title: "<spagobi:message key = "impexp.saveAss" bundle="component_impexp_messages"/>", width:300, height:130, hideEffect:Element.hide, showEffect:Element.show, destroyOnClose:true});
       		winsaveass.setContent('divSaveAssFileForm', false, false);
       		winsaveass.setDestroyOnClose();
       		winsaveass.showCenter();
		}
		
		
		observerClose = {
      		onClose: function(eventName, win) {
        		if(win == winlistass) {
          			winlistass = null;
        		}
        		if(win == winsaveass) {
        			document.getElementById('divSaveAssFileForm').style.display='none';
        		}
     		 }
   	 	}
   		Windows.addObserver(observerClose);
		
		
		
		
		
		
		function selectAssFile(idass, nameass) {
	    	text = document.getElementById('textReadOnlyAssName');
	    	hid = document.getElementById('hidAssId');
	        text.value=nameass;
	        hid.value=idass;
	        Windows.closeAll();
	    }
		
		
		function checkIfExists() {
			nameass = document.getElementById('nameNewAssToSave').value;
			if (nameass==""){
				alert('<spagobi:message key = "Sbi.saving.nameNotSpecified" bundle="component_impexp_messages"/>');
				return;
			} 
			// check if the association file alreay exists
			checkAssUrl = "<%=ChannelUtilities.getSpagoBIContextName(request)%>";
			checkAssUrl += "/servlet/AdapterHTTP?";
			pars = "ACTION_NAME=MANAGE_IMPEXP_ASS_ACTION&MESSAGE=CHECK_IF_EXISTS&ID=" + document.getElementById('nameNewAssToSave').value;
			new Ajax.Request(checkAssUrl,
          		{
            		method: 'post',
            		parameters: pars,
            		onSuccess: function(transport){
                    	        	response = transport.responseText || "";
                        	    	saveAss(response);
                        	   },
            		onFailure: somethingWentWrongSaveAss,
            		asynchronous: false
         		 }
       		 );
		}	
		
		
		function saveAss(exists) {
			if (exists != "true" || confirm("<spagobi:message key = "Sbi.saving.alreadyExisting" bundle="component_impexp_messages"/>")) {
	       		saveAssUrl = "<%=ChannelUtilities.getSpagoBIContextName(request)%>";
				saveAssUrl += "/servlet/AdapterHTTP?";
				pars = "ACTION_NAME=MANAGE_IMPEXP_ASS_ACTION&MESSAGE=SAVE_ASSOCIATION_FILE&OVERWRITE=TRUE";
				pars += "&language=<%=locale.getLanguage()%>&country=<%=locale.getCountry()%>";
				<%
					if( (iri!=null) && (iri.getPathAssociationsFile()!=null) && !iri.getPathAssociationsFile().equals("") ) {	
						String pathassfile = iri.getPathAssociationsFile();
						pathassfile = pathassfile.replaceAll("\\\\", "/");
				%>
				pars += "&PATH=<%=pathassfile%>";
				<%
					}
				%>
				nameass = document.getElementById('nameNewAssToSave').value;
				if(nameass==""){
					alert('<spagobi:message key = "Sbi.saving.nameNotSpecified" bundle="component_impexp_messages"/>');
					return;
				} 
				pars += "&NAME=" + nameass;
				descriptionass = document.getElementById('descriptionNewAssToSave').value;
				pars += "&DESCRIPTION=" + descriptionass;
				new Ajax.Request(saveAssUrl,
	          		{
	            		method: 'post',
	            		parameters: pars,
	            		onSuccess: function(transport){
	                    	        	response = transport.responseText || "";
	                        	    	showSaveAssResult(response);
	                        	   },
	            		onFailure: somethingWentWrongSaveAss
	         		 }
	       		 );
       		 }
	 	} 
	 	
	 	function somethingWentWrongSaveAss() {
	        divres = document.getElementById('divshowresultsave');
			divres.innerHTML="Saving service not reachable.";
     	}
     
	    function showSaveAssResult(response) {
			divres = document.getElementById('divshowresultsave');
			divres.innerHTML=response;
	    }
	    
	    
	</script>




</div>