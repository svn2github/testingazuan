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
				it.eng.spagobi.importexport.ImportExportConstants" %>

<%  
   SourceBean moduleResponse = (SourceBean)aServiceResponse.getAttribute("TreeObjectsModule"); 
   String exportFilePath = (String)aServiceRequest.getAttribute(ImportExportConstants.EXPORT_FILE_PATH);
   String importLogFilePath = (String)aServiceRequest.getAttribute(ImportExportConstants.IMPORT_LOG_FILE_PATH);

   PortletURL backUrl = renderResponse.createActionURL();
   backUrl.setParameter("ACTION_NAME", "START_ACTION");
   backUrl.setParameter("PUBLISHER_NAME", "LoginSBISettingsPublisher");
   backUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_RESET, "true");
   
   PortletURL formExportUrl = renderResponse.createActionURL();
   formExportUrl.setParameter("PAGE", "ImportExportPage");
   formExportUrl.setParameter("MESSAGEDET", "Export");
   backUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
  
   PortletURL formImportUrl = renderResponse.createActionURL();
   formImportUrl.setParameter("PAGE", "ImportExportPage");

   String downloadUrl = renderRequest.getContextPath() + "/ExportService";
   if((exportFilePath!=null) && !exportFilePath.trim().equalsIgnoreCase("") ) {
	   downloadUrl += "?OPERATION=download&PATH="+  exportFilePath;
   }
   
   String downloadLogUrl = renderRequest.getContextPath() + "/ExportService";
   if((importLogFilePath!=null) && !importLogFilePath.trim().equalsIgnoreCase("") ) {
	   downloadLogUrl += "?OPERATION=downloadLog&PATH="+  importLogFilePath;
   }
   
   
%>


<table class='header-table-portlet-section'>
	<tr class='header-row-portlet-section'>
		<td class='header-title-column-portlet-section' style='vertical-align:middle;padding-left:5px;'>
			<spagobi:message key = "SBISet.importexport" bundle="component_impexp_messages"/>
		</td>
		<td class='header-empty-column-portlet-section'>&nbsp;</td>
		<td class='header-button-column-portlet-section'>
			<a href='<%= backUrl.toString() %>'> 
      			<img class='header-button-image-portlet-section' 
      				 title='<spagobi:message key = "Sbi.back" bundle="component_impexp_messages" />' 
      				 src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/components/importexport/img/back.png")%>' 
      				 alt='<spagobi:message key = "Sbi.back"  bundle="component_impexp_messages"/>' />
			</a>
		</td>
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
	
	function submitDownloadLogForm(actionurl) {
		downLogform = document.getElementById('downLogForm');
		var divLogdown = document.getElementById('divLogDownload');
		divLogdown.style.display='none';
		downLogform.submit();
	}
</script>




<div class="div_background_no_img">

 
  <form method='POST' action='<%=formExportUrl.toString()%>' id='exportForm' name='exportForm'> 
	<div style="float:left;width:50%;" class="div_detail_area_forms">
		<div class='portlet-section-header' style="float:left;width:88%;">	
				<spagobi:message key = "SBISet.export" bundle="component_impexp_messages"/>
		</div>
		<div style="float:left;width:10%;">
		  <center>
			 <a href="javascript:submitExportForm()">
					<img src= '<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/components/importexport/img/importexport32.gif") %>'
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
			<spagobi:message key = "SBISet.importexport.expSubView" bundle="component_impexp_messages"/>
			<input type="checkbox" name="exportSubObj" />	
		</div>
		<div style="clear:left;margin-bottom:10px;">
			<spagobi:treeObjects moduleName="TreeObjectsModule"  
				htmlGeneratorClass="it.eng.spagobi.presentation.treehtmlgenerators.AdminExportTreeHtmlGenerator" />
		</div>
	</div>
	</form>



	<form method='POST' action='<%=downloadUrl%>' id='downForm' name='downForm'>
	</form>

    <form method='POST' action='<%=downloadLogUrl%>' id='downLogForm' name='downLogForm'>
	</form>

    <form method='POST' action='<%=formImportUrl.toString()%>' id='importForm' name='importForm' enctype="multipart/form-data">
	<div style="float:left;width:45%" class="div_detail_area_forms">
		<div class='portlet-section-header' style="float:left;width:88%;">
				<spagobi:message key = "SBISet.import" bundle="component_impexp_messages"/>
		</div>
		<div style="float:left;width:10%;">
		  <center>
			<a href="javascript:document.getElementById('importForm').submit()">
					<img src= '<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/components/importexport/img/importexport32.gif") %>'
						title='<spagobi:message key = "SBISet.import" bundle="component_impexp_messages"/>' 
						alt='<spagobi:message key = "SBISet.import" bundle="component_impexp_messages"/>' />
				</a>
			</center>
		</div>
		<div style="clear:left;margin-bottom:10px;padding-top:10px;">
			<spagobi:message key = "SBISet.importexport.fileArchive" bundle="component_impexp_messages"/>
			: 
			<input type="file"  name="exportedArchive" />
			<input type='hidden' name='MESSAGEDET' value='Import' />
		</div>
		<div id="divLogDownload" 
			 style="clear:left;display:none;color:#074B88;">	 
			<spagobi:message key = "SBISet.importexport.opComplete" bundle="component_impexp_messages"/>
			<a style='text-decoration:none;color:#CC0000;' href="javascript:submitDownloadLogForm()">
				<spagobi:message key = "Sbi.downloadLog" bundle="component_impexp_messages"/>
			</a>
		</div>
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
	if((importLogFilePath!=null) && !importLogFilePath.trim().equalsIgnoreCase("") ) {
%>
	<script>
		var divLogDown = document.getElementById('divLogDownload');
		divLogDown.style.display='inline';
	</script>
<% 
	}
%>







