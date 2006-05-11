<%@ include file="/jsp/portlet_base.jsp"%>

<%@ page import="javax.portlet.PortletURL,
				it.eng.spago.navigation.LightNavigationManager,
				it.eng.spagobi.constants.SpagoBIConstants" %>

<%  
   SourceBean moduleResponse = (SourceBean)aServiceResponse.getAttribute("TreeObjectsModule"); 
   String exportFilePath = (String)moduleResponse.getAttribute(SpagoBIConstants.EXPORT_FILE_PATH);

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

%>

<table class='header-table-portlet-section'>
	<tr class='header-row-portlet-section'>
		<td class='header-title-column-portlet-section' style='vertical-align:middle;padding-left:5px;'>
			<spagobi:message key = "SBISet.importexport" />
		</td>
		<td class='header-empty-column-portlet-section'>&nbsp;</td>
		<td class='header-button-column-portlet-section'>
			<a href='<%= backUrl.toString() %>'> 
      			<img class='header-button-image-portlet-section' 
      				 title='<spagobi:message key = "Sbi.back" />' 
      				 src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/back.png")%>' 
      				 alt='<spagobi:message key = "Sbi.back" />' />
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
</script>




<div class="div_background_no_img">

 
  <form method='POST' action='<%=formExportUrl.toString()%>' id='exportForm' name='exportForm'> 
	<div style="float:left;width:50%;" class="div_detail_area_forms">
		<div class='portlet-section-header' style="float:left;width:88%;">	
				Export
		</div>
		<div style="float:left;width:10%;">
		  <center>
			 <a href="javascript:submitExportForm()">
					<img src= '<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/importexport32.png") %>'
						title='<spagobi:message key = "SBISet.export" />' 
						alt='<spagobi:message key = "SBISet.export" />' />
				</a>
		  </center>
		</div>
		<div id="divProgress" style="clear:left;margin-left:15px;padding-top:15px;display:none">
			Operation in Progress ... (Please wait)
		</div>
		<div id="divDownload" style="clear:left;margin-left:15px;padding-top:15px;display:none">
		<%
			String downloadUrl = renderRequest.getContextPath() + "/ExportService";
		    downloadUrl += "?OPERATION=download&PATH="+  exportFilePath;
		%>
			Operation Complete <a href='<%=downloadUrl%>'>Download</a>
		</div>
		<div style="clear:left;margin-left:15px;padding-top:10px;">
			Name Export: <input type="text" name="exportFileName" size="30" />
			<!-- Version 1.8 doesn't allow to export subobject
			&nbsp;&nbsp;&nbsp;&nbsp;
			Export SubObjects: <input type="checkbox" name="exportSubObj" />
			-->
		</div>
		<div style="clear:left;margin-bottom:10px;">
			<spagobi:treeObjects moduleName="TreeObjectsModule"  
				htmlGeneratorClass="it.eng.spagobi.presentation.treehtmlgenerators.AdminExportTreeHtmlGenerator" />
		</div>
	</div>
	</form>

    <form method='POST' action='<%=formImportUrl.toString()%>' id='importForm' name='importForm' enctype="multipart/form-data">
	<div style="float:left;width:45%" class="div_detail_area_forms">
		<div class='portlet-section-header' style="float:left;width:88%;">
				Import
		</div>
		<div style="float:left;width:10%;">
		  <center>
			<a href="javascript:document.getElementById('importForm').submit()">
					<img src= '<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/importexport32.png") %>'
						title='<spagobi:message key = "SBISet.import" />' 
						alt='<spagobi:message key = "SBISet.import" />' />
				</a>
			</center>
		</div>
		<div style="clear:left;margin-bottom:10px;padding-top:10px;">
			Export Archive: <input type="file"  name="exportedArchive" />
			<input type='hidden' name='MESSAGEDET' value='Import' />
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








