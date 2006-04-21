<%@ include file="/jsp/portlet_base.jsp"%>

<%@ page import="javax.portlet.PortletURL,
				it.eng.spago.navigation.LightNavigationManager" %>

<%  
   PortletURL backUrl = renderResponse.createActionURL();
   backUrl.setParameter("ACTION_NAME", "START_ACTION");
   backUrl.setParameter("PUBLISHER_NAME", "LoginSBISettingsPublisher");
   backUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_RESET, "true");
   
   PortletURL formExportUrl = renderResponse.createActionURL();
   formExportUrl.setParameter("PAGE", "ImportExportPage");
   formExportUrl.setParameter("MESSAGEDET", "Export");
   
   PortletURL formImportUrl = renderResponse.createActionURL();
   formImportUrl.setParameter("PAGE", "ImportExportPage");
   formImportUrl.setParameter("MESSAGEDET", "Import");
   
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






<div class="div_background_no_img">

  <form method='POST' action='<%=formExportUrl.toString()%>' id='exportForm' name='importForm'>
	<div style="float:left;width:50%;" class="div_detail_area_forms">
		<div class='portlet-section-header' style="float:left;width:88%;">
				Export
		</div>
		<div style="float:left;width:10%;">
		  <center>
			<a href="javascript:document.getElementById('exportForm').submit()">
					<img src= '<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/importexport32.png") %>'
						title='<spagobi:message key = "SBISet.export" />' 
						alt='<spagobi:message key = "SBISet.export" />' />
				</a>
			</center>
		</div>
		<div style="clear:left;margin-bottom:10px;">
			<spagobi:treeObjects moduleName="TreeObjectsModule"  
				htmlGeneratorClass="it.eng.spagobi.presentation.treehtmlgenerators.AdminExportTreeHtmlGenerator" />
		</div>
	</div>
	</form>

    <form method='POST' action='<%=formImportUrl.toString()%>' id='importForm' name='importForm'>
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
			Export Archive: <input type="file" />
		</div>
	</div>
	</form>


	<div style="clear:left;">
			&nbsp;
	</div>











