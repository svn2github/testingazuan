<%--
SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2008 Engineering Ingegneria Informatica S.p.A.

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
//START CONSTRUCTING TREE FOR EACH RESOURCE
	
		KpiResourceBlock block = (KpiResourceBlock) blocksIt.next();
		options = block.getOptions();
		HashMap parMap = block.getParMap() ;
		KpiLine root = block.getRoot();
		Resource r = null;
		Date d = block.getD();
		if(block.getR()!=null){
			r = block.getR();
		}

		String id = "";
    	if(currTheme==null)currTheme=ThemesManager.getDefaultTheme();
		if (r!=null){
			
			String resourceName = r.getName();
			Integer resourceId = r.getId();
			%>
			<!-- START DIV containing each resource tree -->
			<div id='<%=resourceName%>' >	
			
			<!-- START Table containing a specific resource -->		
			<table class='kpi_table' id='KPI_TABLE<%=resourceId%>' >
			<TBODY>
			<% if (options.getDisplay_bullet_chart() && options.getDisplay_threshold_image() ){%>
				 <tr class='kpi_resource_section' >
				 	<td colspan='10' id='ext-gen58' >
				 		<spagobi:message key="sbi.kpi.RESOURCE" /><%=resourceName%>
				 	</td>
				 </tr>
			<%}else{%>
				 <tr class='kpi_resource_section' >
				 	<td colspan='9' id='ext-gen58' >
				 		<spagobi:message key="sbi.kpi.RESOURCE" /><%=resourceName%>
				 	</td>
				 </tr>
			<%}
			id = "node"+resourceId;
			
		}else{%>
			
			<!-- START Table in case there are no resources -->	
			<table class='kpi_table' id='KPI_TABLE' >
			<TBODY>
			<% 
			id = "node1";
		}%>
			
			<!-- START TITLE ROW -->
		 		 <tr class='kpi_first_line_section_odd' >
		 		 
		 		 	<!-- START MODEL TITLE COLUMN -->
					<td width='53%'  class='kpi_first_line_td' style='text-align:left;' >
						<%=options.getModel_title()%>
					</td>
					<td width='4%' >
						<div></div>
					</td>
					<!-- END MODEL TITLE COLUMN -->
					
					<!-- START KPI TITLE COLUMN -->
				<% if(options.getKpi_title()!=null){%>
					<td  width='9%' class='kpi_first_line_td' >
						<%=options.getKpi_title()%>
					</td>
				<% }else{ %>
					<td  width='9%' class='kpi_first_line_td' >
						<div></div>
					</td>
					<!-- END KPI TITLE COLUMN -->
					
					<!-- START KPI WEIGHT COLUMN -->
				<% } 
				
				if (options.getDisplay_weight() && options.getWeight_title()!=null){ %>
					<td width='5%' class='kpi_first_line_td' >
						<%=options.getWeight_title()%>
					</td>
				<% }else{%>
					<td width='5%' class='kpi_first_line_td' >
						<div></div>
					</td>
					<!-- END KPI WEIGHT COLUMN -->
					
					<!-- START BULLET CHART AND THRESHOLD IMAGE COLUMN -->
				<% } 
				
				if (options.getDisplay_bullet_chart() && options.getDisplay_threshold_image() && options.getBullet_chart_title()!=null && options.getBullet_chart_title()!=null){ %>
					<td width='15%' class='kpi_first_line_td' style='text-align:center;' >
						<%=options.getBullet_chart_title()%>
					</td>
					<td width='7%' class='kpi_first_line_td' >
						<%=(options.getThreshold_image_title()!=null?options.getThreshold_image_title():"")%>
					</td>
				<% }else if(options.getDisplay_bullet_chart()  && options.getBullet_chart_title()!=null){%>
					<td width='22%' class='kpi_first_line_td' style='text-align:center;' >
						<%=options.getBullet_chart_title()%>
					</td>
				<% }else if(options.getDisplay_threshold_image()  && options.getThreshold_image_title()!=null){%>
					<td width='22%' class='kpi_first_line_td' style='text-align:center;' >
						<%=options.getThreshold_image_title()%>
					</td>
				<% }else{%>
					<td width='22%' class='kpi_first_line_td' style='text-align:center;' >
						<div></div>
					</td>
				<% }%>
					<!-- END BULLET CHART AND THRESHOLD IMAGE COLUMN -->
					
				<td width='3%' ><div></div></td>
				<td width='2%' ><div></div></td>
				<td width='2%' ><div></div></td>
		 </tr>
		 <!-- END TITLE ROW -->
		
		<% StringBuffer _htmlStream = new StringBuffer();
		   ExecutionInstance instance = contextManager.getExecutionInstance(ExecutionInstance.class.getName());
		   //KpiUtils k = new KpiUtils();
		   _htmlStream = addItemForTree(id ,instance,userId,0,false,request, root,_htmlStream,options,currTheme,parMap,r,d);%>
		   
		<%= _htmlStream%>
			</TBODY>
			</TABLE>
			<!-- END Table containing a specific resource -->	
		<%if (r!=null){	%>
			</div>
			<!-- END DIV containing each resource tree -->
		<%}
//END CONSTRUCTING TREE FOR EACH RESOURCE
			
%>			