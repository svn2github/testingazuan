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



<qbe:message key="QBE.Tab.FieldSelection" var="fieldSelectionLabel" />
<qbe:message key="QBE.Tab.FieldSelectionTooltip" var="fieldSelectionTooltip" />
<qbe:message key="QBE.Tab.Conditions" var="fieldConditionsLabel" />
<qbe:message key="QBE.Tab.ConditionsTooltip" var="fieldConditionsTooltip" />
<qbe:message key="QBE.Tab.Ordering" var="fieldOrderingLabel" />
<qbe:message key="QBE.Tab.OrderingTooltip" var="fieldOrderingTooltip" />
<qbe:message key="QBE.Tab.Grouping" var="fieldGropingLabel" />
<qbe:message key="QBE.Tab.GroupingTooltip" var="fieldGroupingTooltip" />
<qbe:message key="QBE.Tab.Resume" var="resumeLabel" />
<qbe:message key="QBE.Tab.ResumeTooltip" var="resumeTooltip" />
<qbe:message key="QBE.Tab.Save" var="saveLabel" />
<qbe:message key="QBE.Tab.SaveTooltip" var="saveTooltip" />
<qbe:message key="QBE.Tab.Preview" var="previewLabel" />
<qbe:message key="QBE.Tab.PreviewTooltip" var="previewTooltip" />
<qbe:message key="QBE.Tab.Export" var="exportLabel" />
<qbe:message key="QBE.Tab.ExportTooltip" var="exportTooltip" />

 <div style='visibility:visible;width:100%' class='UITabs'>
 
	<div class="first-tab-level" style="background-color:#f8f8f8">
	
		<div style="overflow: hidden;width:100%">
		
			
			<qbe:tab id="DIV_FIELD_SELECTION" msg="Field Selection" label="${fieldSelectionLabel}" tooltip="${fieldSelectionTooltip}"/>
			<qbe:tab id="DIV_FIELD_CONDITION" msg="Conditions" label="${fieldConditionsLabel}" tooltip="${fieldConditionsTooltip}"/>
			<qbe:tab id="DIV_FIELD_ORDER_BY" msg="Ordering" label="${fieldOrderingLabel}" tooltip="${fieldOrderingTooltip}"/>
			<qbe:tab id="DIV_FIELD_GROUP_BY" msg="Grouping" label="${fieldGropingLabel}" tooltip="${fieldGroupingTooltip}"/>
			<qbe:tab id="DIV_RESUME_QUERY" msg="ResumeQuery" label="${resumeLabel}" tooltip="${resumeTooltip}"/>			
			<qbe:tab id="DIV_SAVE_QUERY" msg="SaveQuery" label="${saveLabel}" tooltip="${saveTooltip}"/>
			<qbe:tab id="DIV_EXEC" msg="ExecQuery" label="${previewLabel}" tooltip="${previewTooltip}"/>
						
			<c:if test="${jasperTemplateBuilderVisible}">
				<qbe:tab id="DIV_EXPORT" msg="Export" label="${exportLabel}" tooltip="${exportTooltip}"/>
			</c:if>
						
			<c:if test="${geoTemplateBuilderVisible}">
				<qbe:tab id="DIV_GEO" msg="Geo" label="Geo Designer" tooltip="Geo Designer"/>			
			</c:if>
					
			<c:if test="${geoViewerVisible}">
				<qbe:tab id="DIV_GEO_VIEWER" msg="GeoViewer" label="Geo Viewer" tooltip="Geo Viewer"/>			
			</c:if>
			
			<c:if test="${query.subqueryModeActive}">
				<div class='btab' align="right" id='DIV_SAVE_SUBQUERY' title="Save Subquery">
				<input type='button' value='Save Subquery' style="font-size:10px;height:20px;"  onclick="javascript:vediSchermo('save','DIV_SAVE_SUBQUERY')"/>
			</div>
			</c:if>
			
			
		</div>
		
	</div>
	
</div>

	<qbe:url type="action" var="actionUrl"/>

		<form id="frmGoSelection" name="frmGoSelection" action="${actionUrl}" method="post">
			<input class='qbe' type="hidden" name="ACTION_NAME" value="PUBLISH_ACTION"/>
			<input class='qbe' type="hidden" name="PUBLISHER_NAME" value="SELECT_FIELDS_FOR_SELECTION_PUBLISHER"/>
		</form>		
		
		<form id="frmGoCondition" name="frmGoCondition" action="${actionUrl}" method="post">
			<input class='qbe'type="hidden" name="ACTION_NAME" value="PUBLISH_ACTION"/>
			<input class='qbe' type="hidden" name="PUBLISHER_NAME" value="SELECT_FIELDS_FOR_CONDITION_PUBLISHER"/>
		</form>	
		
		<form id="frmGoOrderBy" name="frmGoOrderBy" action="${actionUrl}" method="post">
			<input class='qbe' type="hidden" name="ACTION_NAME" value="PUBLISH_ACTION"/>
			<input class='qbe' type="hidden" name="PUBLISHER_NAME" value="SELECT_FIELDS_FOR_ORDERBY_PUBLISHER"/>
		</form>	
		
		<form id="frmGoGroupBy" name="frmGoGroupBy" action="${actionUrl}" method="post">
			<input class='qbe' type="hidden" name="ACTION_NAME" value="PUBLISH_ACTION"/>
			<input class='qbe' type="hidden" name="PUBLISHER_NAME" value="SELECT_FIELDS_FOR_GROUPBY_PUBLISHER"/>
		</form>	
	
		<form id="frmComposeQuery" name="frmComposeQuery" action="${actionUrl}" method="post">
			<input class='qbe' type="hidden" name="ACTION_NAME" value="COMPOSE_RESUME_QUERY_ACTION"/>
		</form>	
		
		<form id="frmExecuteQuery" name="frmExecuteQuery" action="${actionUrl}" method="post">
			<input class='qbe' type="hidden" name="ACTION_NAME" value="EXECUTE_QUERY_AND_SAVE_ACTION"/>
		</form>
		
		<form id="frmExportResult" name="frmExportResult" action="${actionUrl}" method="post">
			<input class='qbe' type="hidden" name="ACTION_NAME" value="EXPORT_ACTION"/>
		</form>
		
		<form id="frmSaveQuery" name="frmSaveQuery" action="${actionUrl}" method="post">
			<input class='qbe' type="hidden" name="ACTION_NAME" value="EXECUTE_QUERY_AND_SAVE_FROM_SAVE_ACTION"/>
		</form>
		
		<form id="frmGeo" name="frmGeo" action="${actionUrl}" method="post">
			<input class='qbe' type="hidden" name="ACTION_NAME" value="GENERATE_GEO_TEMPLATE_ACTION"/>
		</form>
		
		<form id="frmGeoViewer" name="frmGeoViewer" action="${actionUrl}" method="post">
			<input class='qbe' type="hidden" name="ACTION_NAME" value="VIEW_ON_MAP"/>
		</form>
						
		<form id="frmSaveSubQuery" name="frmSaveSubQuery" action="${actionUrl}" method="post">
			<input class='qbe' type="hidden" name="ACTION_NAME" value="PUBLISH_ACTION"/>
			<input id="publisher" type="hidden" name="PUBLISHER_NAME" value="EXIT_FROM_SUBQUERY_PUBLISHER"/>
		</form>

