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

<%
	boolean jasperTemplateBuilderVisible = true;
	boolean geoTemplateBuilderVisible = false;
	boolean geoViewerVisible = false;
	Map functionalities = (Map)sessionContainer.getAttribute("FUNCTIONALITIES");
	
	if(functionalities != null) {
		Properties props = null;
		String pValue = null;
		
		props = (Properties)functionalities.get("jasperTemplateBuilder");
		if(props != null) {
			pValue = props.getProperty("visible");
			if(pValue != null && pValue.equalsIgnoreCase("FALSE")) jasperTemplateBuilderVisible = false;
		}
		
		props = (Properties)functionalities.get("geoTemplateBuilder");
		if(props != null) {
			pValue = props.getProperty("visible");
			if(pValue != null && pValue.equalsIgnoreCase("TRUE")) geoTemplateBuilderVisible = true;
		}
		
		props = (Properties)functionalities.get("geoViewer");
		if(props != null) {
			pValue = props.getProperty("visible");
			if(pValue != null && pValue.equalsIgnoreCase("TRUE")) geoViewerVisible = true;
		}
		
		
	}
	
	

%>

 <div style='visibility:visible;width:100%' class='UITabs'>
 
	<div class="first-tab-level" style="background-color:#f8f8f8">
	
		<div style="overflow: hidden;width:100%">
		
			<div class='tab' id='DIV_FIELD_SELECTION'>
				<a href="javascript:vediSchermo('Field Selection','DIV_FIELD_SELECTION')" 
				   title="<%=qbeMsg.getMessage(requestContainer,"QBE.Tab.FieldSelectionTooltip", bundle)%> " 
				   style="color:black;"
				   >
					<%=qbeMsg.getMessage(requestContainer,"QBE.Tab.FieldSelection", bundle) %>
				</a>
			</div>
			
			<div class='tab' id='DIV_FIELD_CONDITION'>
				<a href="javascript:vediSchermo('Conditions','DIV_FIELD_CONDITION')"
				   title="<%=qbeMsg.getMessage(requestContainer,"QBE.Tab.ConditionsTooltip", bundle) %>" 
				   style="color:black;"
				   >
					<%=qbeMsg.getMessage(requestContainer,"QBE.Tab.Conditions", bundle) %>
				</a>
			</div>
			
			<div class='tab' id='DIV_FIELD_ORDER_BY'>
				<a href="javascript:vediSchermo('Ordering','DIV_FIELD_ORDER_BY')" 
				   title="<%=qbeMsg.getMessage(requestContainer,"QBE.Tab.OrderingTooltip", bundle) %>"
				   style="color:black;"
				   >
					<%=qbeMsg.getMessage(requestContainer,"QBE.Tab.Ordering", bundle) %>
				</a>
			</div>
			
			<div class='tab' id='DIV_FIELD_GROUP_BY'>
				<a href="javascript:vediSchermo('Grouping','DIV_FIELD_GROUP_BY')"
				   title="<%=qbeMsg.getMessage(requestContainer,"QBE.Tab.GroupingTooltip", bundle) %>"
				   style="color:black;"
				   >
					<%=qbeMsg.getMessage(requestContainer,"QBE.Tab.Grouping", bundle) %>
				</a>
			</div>
			
			<div class='tab' id='DIV_RESUME_QUERY'>
				<a href="javascript:vediSchermo('ResumeQuery','DIV_RESUME_QUERY')"
				   title="<%=qbeMsg.getMessage(requestContainer,"QBE.Tab.ResumeTooltip", bundle) %>"
				   style="color:black;"
				   >
					<%=qbeMsg.getMessage(requestContainer,"QBE.Tab.Resume", bundle) %>
				</a>
			</div>
			
			<div class='tab' id='DIV_SAVE_QUERY'>
				<a href="javascript:vediSchermo('SaveQuery','DIV_SAVE_QUERY')"
				   title="<%=qbeMsg.getMessage(requestContainer,"QBE.Tab.SaveTooltip", bundle) %>"
				   style="color:black;"
				   >
					<%=qbeMsg.getMessage(requestContainer,"QBE.Tab.Save", bundle) %>
				</a>
			</div>
			
			<div class='tab' id='DIV_EXEC'>
				<a href="javascript:vediSchermo('Grouping','DIV_EXEC')"
				   title="<%=qbeMsg.getMessage(requestContainer,"QBE.Tab.PreviewTooltip", bundle) %>"
				   style="color:black;"
				   >
					<%=qbeMsg.getMessage(requestContainer,"QBE.Tab.Preview", bundle) %>
				</a>
			</div>
			
			<%
			if(jasperTemplateBuilderVisible) {
			%>
			<div class='tab' id='DIV_EXPORT'>
				<a href="javascript:vediSchermo('Export','DIV_EXPORT')"
				   title="<%=qbeMsg.getMessage(requestContainer,"QBE.Tab.ExportTooltip", bundle)%> " 
				   style="color:black;"
				   >
					<%=qbeMsg.getMessage(requestContainer,"QBE.Tab.Export", bundle) %>
				</a>
			</div>	
			<%} %>
			
			<%
			if(geoTemplateBuilderVisible) {
			%>
			<div class='tab' id='DIV_GEO'>
				<a href="javascript:vediSchermo('Geo','DIV_GEO')" 
				   title="GeoDesigner" 
				   style="color:black;"
				   >
					Geo Designer
				</a>
			</div>
			<%} %>
			
			<%
			if(geoViewerVisible) {
			%>
			<div class='tab' id='DIV_GEO_VIEWER'>
				<a href="javascript:vediSchermo('GeoViewer','DIV_GEO_VIEWER')" 
				   title="GeoViewer" 
				   style="color:black;"
				   >
					Geo Viewer
				</a>
			</div>
			<%} %>
			
			<%
			if(Utils.isSubQueryModeActive(sessionContainer)) {
			%>
			<div class='btab' align="right" id='DIV_SAVE_SUBQUERY' title="Save Subquery">
				<input type='button' value='Save Subquery' style="font-size:10px;height:20px;"  onclick="javascript:vediSchermo('save','DIV_SAVE_SUBQUERY')"/>
			</div>
			<%} %>
			
		</div>
		
	</div>
	
</div>

