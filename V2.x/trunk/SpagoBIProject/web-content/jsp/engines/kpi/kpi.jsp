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

<%@ include file="/jsp/commons/portlet_base.jsp"%>
<%@ page import="java.util.Map" %>
<%@page import="it.eng.spago.navigation.LightNavigationManager"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@page import="org.jfree.chart.JFreeChart"%>
<%@page import="org.jfree.chart.ChartUtilities"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="org.jfree.chart.imagemap.StandardToolTipTagFragmentGenerator"%>
<%@page import="org.jfree.chart.imagemap.StandardURLTagFragmentGenerator"%>
<%@page import="org.jfree.chart.ChartRenderingInfo"%>
<%@page import="org.jfree.data.general.Dataset"%>
<%@page import="org.safehaus.uuid.UUIDGenerator"%>
<%@page import="it.eng.spagobi.kpi.config.bo.*"%>
<%@page import="it.eng.spagobi.kpi.threshold.bo.*"%>
<%@page import="it.eng.spagobi.kpi.model.bo.*"%>
<%@page import="org.safehaus.uuid.UUID"%>
<%@page import="org.jfree.chart.entity.StandardEntityCollection"%>
<%@page import="it.eng.spago.error.EMFErrorHandler"%>
<%@page import="java.util.Vector"%>
<%@page import="it.eng.spagobi.analiticalmodel.document.bo.BIObject"%>
<%@page import="org.jfree.data.category.DefaultCategoryDataset"%>
<%@page import="it.eng.spagobi.commons.bo.UserProfile"%>
<%@page import="it.eng.spagobi.engines.kpi.utils.StyleLabel"%>
<%@page import="it.eng.spagobi.engines.kpi.bo.ChartImpl"%>

<%
	boolean docComposition=false;
	SourceBean sbModuleResponse = (SourceBean) aServiceResponse.getAttribute("ExecuteBIObjectModule");
 	EMFErrorHandler errorHandler=aResponseContainer.getErrorHandler();
	if(errorHandler.isOK()){    
		SessionContainer permSession = aSessionContainer.getPermanentContainer();

		if(userProfile==null){
			userProfile = (IEngUserProfile) permSession.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
			userId=(String)((UserProfile)userProfile).getUserId();
		}

	}
	ExecutionInstance instanceO = contextManager.getExecutionInstance(ExecutionInstance.class.getName());
	String execContext = instanceO.getExecutionModality();
   	// if in document composition case do not include header.jsp
	   if (execContext == null || !execContext.equalsIgnoreCase(SpagoBIConstants.DOCUMENT_COMPOSITION)){%>
				<%@ include file="/jsp/analiticalmodel/execution/header.jsp"%>
				<%
			   }
   		else // in document composition case doesn't call header so set Object and uuid
			   {
	   				docComposition=true;
			   }
	%>
	<link rel="stylesheet" type="text/css" href="<%=urlBuilder.getResourceLink(request, "css/printImage.css")%>" media="print">
		
	
	<link type="text/css" rel="stylesheet" href="<%=urlBuilder.getResourceLink(request, "css/extjs/ext-ux-slidezone.css")%>"/>
	<script type="text/javascript" src="<%=urlBuilder.getResourceLink(request, "js/extjs/Ext.ux.SlideZone.js")%>"></script>	
	<%
	String title = (String)sbModuleResponse.getAttribute("title");
	String subTitle = (String)sbModuleResponse.getAttribute("subName");
	if (title!=null){
		%>
		<div align="center" style="font:Arial;font-size:24;color:blue"><%=title%></div>
			<%}if (subTitle!=null){%>
		<div align="center" style="font:Arial;font-size:16;color:aqua"><%=subTitle%></div>
		<%}%>	
	<%
		//ChartImpl sbi = (ChartImpl)sbModuleResponse.getAttribute("sbi");
		List charts =(List)sbModuleResponse.getAttribute("charts");
		Boolean show_chart = (Boolean)sbModuleResponse.getAttribute("show_chart");		
		
		if (!show_chart){
		List kpiValues =(List)sbModuleResponse.getAttribute("kpiValues");
			Iterator kpiVIt = kpiValues.iterator();
			String text = "";
			while (kpiVIt.hasNext()){
				KpiValue kpiV = (KpiValue)kpiVIt.next();
				Resource r = kpiV.getR();
				List thresholds = kpiV.getThresholds();
				String value = kpiV.getValue();
				Double weight = kpiV.getWeight();
						%>			
					<div>*******************************************</div>
					<% if (r!=null){
					//creates a document without the representation of the kpivalues but only with its values for each resoruce
					String resName = r.getName();%>
					<div>RESOURCE = <%=resName%></div>
					<% }%>
					<div>Value = <%=value%></div>
					<div>Weight = <%=weight%></div>					
					<div>Thresholds:</div>
	
					<%
					Iterator threshIt = thresholds.iterator();
					while(threshIt.hasNext()){
						Threshold t = (Threshold)threshIt.next();
						String type = t.getType();
						Double min = null;
						Double max = null;						
						%>
						<div>Threshold Type: <%=type%></div>
						<%
						if (type.equals("RANGE")){						
							min = t.getMinValue();
							max = t.getMaxValue();
							%>
							<div>Min: <%=min.toString()%></div>
							<div>Max: <%=max.toString()%></div>
							<%														
						}else if (type.equals("MINIMUM")){						
							min = t.getMinValue();
							%>
							<div>Min: <%=min.toString()%></div>
							<%
						}else if (type.equals("MAXIMUM")){							
							max = t.getMaxValue();
							%>
							<div>Max: <%=max.toString()%></div>
							<%
						}					
					}
				}				
		}else if(!charts.isEmpty()){
			
			%> <!-- BEGIN OF TABLE WITH CHARTS -->
			<br>
			   <table align="left">
    				<tr>
     		<%
			Iterator it = charts.iterator();
     		int j = 0;
     		int colonne = 1 ;
			while(it.hasNext()){
				ChartImpl sbi = (ChartImpl)it.next();
				////////////// Chart creation///////////////////
				
				JFreeChart chart=null;
				// create the chart
				chart = sbi.createChart();
				//Create the temporary file
				BIObject objO = instanceO.getBIObject();
				String uuidO=instanceO.getExecutionId();
				String executionFlowIdO=instanceO.getFlowId();
				String executionId = uuidO;
				executionId = executionId.replaceAll("-", "");

				ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
				//Saving image on a temporary file
				String dir=System.getProperty("java.io.tmpdir");
				String path=dir+"/"+executionId+j+".png";
				java.io.File file1 = new java.io.File(path);
				ChartUtilities.saveChartAsPNG(file1, chart, sbi.getWidth(), sbi.getHeight(), info);
				
				String urlPng=GeneralUtilities.getSpagoBiContext() + GeneralUtilities.getSpagoAdapterHttpUrl() + 
				"?ACTION_NAME=GET_PNG&NEW_SESSION=TRUE&userid="+userId+"&path="+path+"&"+LightNavigationManager.LIGHT_NAVIGATOR_DISABLED+"=TRUE";
		
				if(colonne==6){
					%>
					</tr><tr>
					<%
					colonne = 1;
				}
				
				%>
									<!-- Begin drawing the page -->

			   <td>
			   <div align="center">
			   <img id="image" src="<%=urlPng%>" BORDER="1" alt="Error in displaying the chart" USEMAP="#chart"/>
			   </div>
			   </td>
				
				<%j++;
				colonne ++;
			}
		}
		%>
				</tr>
			</table>
			<!--END OF TABLE WITH CHARTS -->			

		