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

<%@ include file="/WEB-INF/jsp/commons/portlet_base.jsp"%>
<%@ page import="java.util.Map"%>
<%@page import="it.eng.spago.navigation.LightNavigationManager"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@page import="org.jfree.chart.JFreeChart"%>
<%@page import="org.jfree.chart.ChartUtilities"%>
<%@page import="java.io.PrintWriter"%>
<%@page	import="org.jfree.chart.imagemap.StandardToolTipTagFragmentGenerator"%>
<%@page	import="org.jfree.chart.imagemap.StandardURLTagFragmentGenerator"%>
<%@page import="it.eng.spagobi.engines.chart.bo.ChartImpl"%>
<%@page import="org.jfree.chart.ChartRenderingInfo"%>
<%@page	import="it.eng.spagobi.engines.chart.bo.charttypes.barcharts.LinkableBar"%>
<%@page import="org.jfree.data.general.Dataset"%>
<%@page import="org.safehaus.uuid.UUIDGenerator"%>

<%@page import="org.safehaus.uuid.UUID"%>
<%@page import="org.jfree.chart.entity.StandardEntityCollection"%>
<%@page import="it.eng.spago.error.EMFErrorHandler"%>
<%@page import="java.util.Vector"%>
<%@page	import="it.eng.spagobi.engines.chart.bo.charttypes.barcharts.BarCharts"%>
<%@page	import="it.eng.spagobi.engines.chart.bo.charttypes.barcharts.StackedBarGroup"%>
<%@page	import="it.eng.spagobi.engines.chart.bo.charttypes.piecharts.LinkablePie"%>
<%@page	import="it.eng.spagobi.engines.chart.bo.charttypes.ILinkableChart"%>
<%@page import="it.eng.spagobi.engines.chart.utils.DatasetMap"%>
<%@page	import="it.eng.spagobi.engines.chart.bo.charttypes.clusterchart.ClusterCharts"%>
<%@page import="it.eng.spagobi.analiticalmodel.document.bo.BIObject"%>
<%@page import="org.jfree.data.category.DefaultCategoryDataset"%>
<%@page import="org.jfree.data.general.DefaultValueDataset"%>
<%@page import="it.eng.spagobi.commons.bo.UserProfile"%>

<%
	boolean docComposition=false;
	SourceBean sbModuleResponse = (SourceBean) aServiceResponse.getAttribute("ExecuteBIObjectModule");
	Integer executionAuditId_chart = null;
	ExecutionInstance instanceO = contextManager.getExecutionInstance(ExecutionInstance.class.getName());
	String execContext = instanceO.getExecutionModality();
   	// if in document composition case do not include header.jsp
	   if (execContext == null || !execContext.equalsIgnoreCase(SpagoBIConstants.DOCUMENT_COMPOSITION)){%>
<%@ include file="/WEB-INF/jsp/analiticalmodel/execution/header.jsp"%>
<%
				executionAuditId_chart = executionAuditId;	   
	   }
   		else // in document composition case doesn't call header so set Object and uuid
			   {
	   				docComposition=true;
	   				ExecutionInstance instance = contextManager.getExecutionInstance(ExecutionInstance.class.getName());
	   				AuditManager auditManager = AuditManager.getInstance();
	   				executionAuditId_chart = auditManager.insertAudit(instance.getBIObject(), null, userProfile, instance.getExecutionRole(), instance.getExecutionModality());	   				
			   }
   	
	BIObject objO = instanceO.getBIObject();
	String uuidO=instanceO.getExecutionId();
	String executionFlowIdO=instanceO.getFlowId();
	
	
   	%>

<%-- div with wait while loading message --%>
<div id="divLoadingMessage<%= uuidO %>" style="display: inline;">
<img
	src='<%= urlBuilder.getResourceLinkByTheme(request, "/img/analiticalmodel/loading.gif", currTheme)%>' />
<spagobi:message key='sbi.execution.pleaseWait' /></div>

<link rel="stylesheet" type="text/css"
	href="<%=urlBuilder.getResourceLinkByTheme(request, "css/printImage.css",currTheme)%>"
	media="print">


<link type="text/css" rel="stylesheet"
	href="<%=urlBuilder.getResourceLink(request, "js/lib/ext-2.0.1/resources/css/ext-ux-slidezone.css")%>" />
<script type="text/javascript"
	src="<%=urlBuilder.getResourceLink(request, "js/lib/ext-2.0.1/Ext.ux.SlideZone.js")%>"></script>


<% 
	String maxSlider="0";
	String minSlider="0";
	
	//String valueSlider="1";
	String refreshUrl = "";
	
	boolean filterCatGroup=false;
	boolean filterSeries=false;
	boolean filterCategories=false;
	
	Vector seriesNames=null;
	Vector catGroupsNames=null;
	
	String refreshUrlCategory="";
	String refreshUrlSerie="";

 	EMFErrorHandler errorHandler=aResponseContainer.getErrorHandler();
	if(errorHandler.isOK()){    
		SessionContainer permSession = aSessionContainer.getPermanentContainer();

		if(userProfile==null){
			userProfile = (IEngUserProfile) permSession.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
			userId=(String)((UserProfile)userProfile).getUserId();
		}

		ChartImpl sbi = (ChartImpl)sbModuleResponse.getAttribute("sbi");
		String documentid=(objO.getId()).toString();
		DatasetMap datasetMap=(DatasetMap)sbModuleResponse.getAttribute("datasets");
		DatasetMap copyDatasets=null;
		
		//get width for slider; it is 80% of the whole
		int sliderWidth=(sbi.getWidth()/100)*80;
		
		// get wich pars can the user set
		Vector changePars=(Vector)sbi.getPossibleChangePars();
		//check for each one if a changeparameter has ben set
		for (Iterator iterator = changePars.iterator(); iterator.hasNext();) {
			String par = (String) iterator.next();
			if(request.getParameter(par)!=null){
				String ch=(String)request.getParameter(par);
				if(ch.equalsIgnoreCase("true")){
					sbi.setChangeViewsParameter(par,true);}
				else {
					if(ch.equalsIgnoreCase("false"));
					{
						sbi.setChangeViewsParameter(par,false);}
					}
				}
		}

		
	// in the case of document composition check if serie or category have been previously defined
	datasetMap.setSelectedSeries(new Vector());
	if(sbiMode.equalsIgnoreCase("WEB") || docComposition){
		if(sbModuleResponse.getAttribute("category")!=null){
		String catS=(String)sbModuleResponse.getAttribute("category");
		Double catD=Double.valueOf(catS);
		datasetMap.setCategoryCurrent(catD.intValue());
		}
		if(sbModuleResponse.getAttribute("serie")!=null){
		List selectedSeriesTemp=(List)sbModuleResponse.getAttributeAsList("serie");
		datasetMap.setSelectedSeries(new Vector(selectedSeriesTemp));
		}
		else{
			datasetMap.getSelectedSeries().add("allseries");   
		}
	}

	if(sbi.getType().equalsIgnoreCase("BARCHART") || sbi.getType().equalsIgnoreCase("CLUSTERCHART")){
		//if(sbi.getSubtype().equalsIgnoreCase("simplebar") || sbi.getSubtype().equalsIgnoreCase("linkableBar") || sbi.getSubtype().equalsIgnoreCase("stacked_bar") || sbi.getSubtype().equalsIgnoreCase("stacked_bar_group")){
		if(sbi.getSubtype().equalsIgnoreCase("simplebar") || sbi.getSubtype().equalsIgnoreCase("linkableBar") || sbi.getSubtype().equalsIgnoreCase("stacked_bar")){
			// returns a new datasets map filtered
			copyDatasets=datasetMap.filteringSimpleBarChart(request,(BarCharts)sbi,sbiMode,docComposition);
			filterCatGroup=((BarCharts)sbi).isFilterCatGroups();
		}
		else if(sbi.getSubtype().equalsIgnoreCase("overlaid_barline") || sbi.getSubtype().equalsIgnoreCase("overlaid_stackedbarline")){
			copyDatasets=datasetMap.filteringMultiDatasetBarChart(request,(BarCharts)sbi,sbiMode,docComposition);
		}
		else if(sbi.getSubtype().equalsIgnoreCase("simplecluster")){
			copyDatasets=datasetMap.filteringClusterChart(request,(ClusterCharts)sbi,sbiMode,docComposition);
		}
		else if(sbi.getSubtype().equalsIgnoreCase("stacked_bar_group")){
			copyDatasets=datasetMap.filteringGroupedBarChart(request,(StackedBarGroup)sbi,sbiMode,docComposition);
		}
	}
	else {copyDatasets=datasetMap;}
	



///// Linkable chart case/////////////////

	HashMap rootPar=new HashMap();
	if(sbi.isLinkable()){
		
		boolean linkableBar=false;
		if(sbi instanceof LinkableBar)linkableBar=true;
		else linkableBar=false;

		String rootDocParameter="";
		rootDocParameter=((ILinkableChart)sbi).getDocument_Parameters(((ILinkableChart)sbi).getDrillParameter());

		//rootPar.put(ObjectsTreeConstants.PARAMETERS,rootDocParameter);
	
		String drillLabel="";
			drillLabel=((ILinkableChart)sbi).getDrillLabel();

		
		if(drillLabel!=null && drillLabel!="" && !docComposition){
			rootPar.put(ObjectsTreeConstants.OBJECT_LABEL,drillLabel);
		}
		rootPar.put("PAGE",ExecuteBIObjectModule.MODULE_PAGE);
		rootPar.put(SpagoBIConstants.MESSAGEDET, SpagoBIConstants.EXEC_CROSS_NAVIGATION);
		rootPar.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "TRUE");
		rootPar.put("EXECUTION_FLOW_ID", executionFlowIdO);
		rootPar.put("SOURCE_EXECUTION_ID", uuidO);

		
		
		
		//get from the linkableBar the label and eventually the parameters to pass
		if(((ILinkableChart)sbi).getDrillLabel()!=null)
					rootPar.put(ObjectsTreeConstants.OBJECT_LABEL,((ILinkableChart)sbi).getDrillLabel());


		// Old way portlet		
		String  rootUrl=urlBuilder.getUrl(request,rootPar);
		rootUrl=rootUrl+"&PARAMETERS="+rootDocParameter;
	
		String completeUrl=rootUrl;
	
		
		if(docComposition){			
			((ILinkableChart)sbi).setMode(SpagoBIConstants.DOCUMENT_COMPOSITION);
		
			completeUrl="javascript:parent.execCrossNavigation(this.name, '"+drillLabel+"','"+rootDocParameter;
		}
		else
		{
			((ILinkableChart)sbi).setMode("normal");
		}
			((ILinkableChart)sbi).setRootUrl(completeUrl);

	}

		
	////////////// Chart creation/////////////////////
	String urlPng = "";
	boolean showSlider = false;
	Map refreshUrlPars = new HashMap();
	//if the chart is multiple every serie represent a dataset and it creates a chart for every dataset
	if(sbi.getMultichart()){
		if (sbi.getOrientationMultichart().equalsIgnoreCase("horizontal")){ %>
		<br>
		<table align="center">
			<tr>
		<% }
		HashMap mapDataset = copyDatasets.getDatasets();
		DatasetMap tmpDatasetMap = null;
		String precKey = "";
		String  key = "";
		int idx = 0;
		Iterator iterator = mapDataset.keySet().iterator();
		key = (String ) iterator.next();
		while (iterator.hasNext()){	
				tmpDatasetMap = new DatasetMap();
				precKey = (precKey.indexOf("__") > 0 ) ? precKey.substring(0,precKey.indexOf("__")): precKey;
				String tmpKey = (key.indexOf("__") > 0 ) ? key.substring(0,key.indexOf("__")): key;
				while (precKey.equals("") || precKey.equalsIgnoreCase(tmpKey)){
					precKey = tmpKey;
					
					DefaultValueDataset dataset=(DefaultValueDataset) mapDataset.get(key);  
					Dataset copyDataset=null;
					try {
						copyDataset = (DefaultValueDataset)dataset.clone();	// clone dataset
					} catch (CloneNotSupportedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					tmpDatasetMap.getDatasets().put(key, copyDataset);
					if (iterator.hasNext()){
						key = (String ) iterator.next();
						tmpKey = (key.indexOf("__") > 0 ) ? key.substring(0,key.indexOf("__")): key;
					}
					else
						break; //precKey = "__"; // force 
				}
				precKey = "";
				JFreeChart chart=null;
				// create the chart
				chart = sbi.createChart(tmpDatasetMap);
			
				//Create the temporary file
				String executionId = uuidO;
				executionId = executionId.replaceAll("-", "");
	
				ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
				//Saving image on a temporary file
				String dir=System.getProperty("java.io.tmpdir");
				String path_param=executionId+idx;
				String path=dir+"/"+executionId+idx+".png";
				java.io.File file1 = new java.io.File(path);
				ChartUtilities.saveChartAsPNG(file1, chart, sbi.getWidth(), sbi.getHeight(), info);
	
				// Set the Url for refreshing the chart: in case of document composition specify also the execution page and document_id
				refreshUrlPars = new HashMap();
				refreshUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
					
				if(sbiMode.equalsIgnoreCase("WEB") || docComposition==true){
				   	Map pars = new HashMap();
				   	pars.put("PAGE", "ExecuteBIObjectPage");
				   	pars.put("MESSAGEDET", "EXEC_PHASE_RUN");
				   	refreshUrl = urlBuilder.getUrl(request, pars);
					}
					else{
						if(urlBuilder instanceof PortletUrlBuilder){
						   	refreshUrl = ((PortletUrlBuilder)urlBuilder).getRenderUrl(request, refreshUrlPars);
						}
						else{
						  	refreshUrl = urlBuilder.getUrl(request, refreshUrlPars);
						}
					}
				   	
					//String urlPng=urlBuilder.getResourceLink(request, "/servlet/AdapterHTTP?ACTION_NAME=GET_PNG&NEW_SESSION=TRUE&userid="+userId+"&path="+path);
					String auditParameter="";
					if(executionAuditId_chart!=null){
						auditParameter="&"+AuditManager.AUDIT_ID+"="+executionAuditId_chart.toString();
					}
					
					urlPng=GeneralUtilities.getSpagoBiContext() + GeneralUtilities.getSpagoAdapterHttpUrl() + 
							"?ACTION_NAME=GET_PNG&NEW_SESSION=TRUE&userid="+userId+"&path="+path_param+"&"+LightNavigationManager.LIGHT_NAVIGATOR_DISABLED+"=TRUE"+auditParameter;
	
					if (sbi.getOrientationMultichart().equalsIgnoreCase("horizontal")){
			%>
			<!-- Begin drawing the page for speedometerMultiValue-->
				<td> 
				<%} %>
					<div align="center">
						<img id="image" src="<%=urlPng%>" BORDER="0" alt="" USEMAP="#chart" />
					</div>
				<%if (sbi.getOrientationMultichart().equalsIgnoreCase("horizontal")){ %>
				</td>
				<%} %>
			
		
		<% idx++;
		} //while (iterator.hasNext()){
		if (sbi.getOrientationMultichart().equalsIgnoreCase("horizontal")){ %>	
			</tr>
		</table>
		<%} 
	} //if(sbi.getMultichart())
		
	else{
	    JFreeChart chart=null;
		// create the chart
		chart = sbi.createChart(copyDatasets);

		//Create the temporary file
		String executionId = uuidO;
		executionId = executionId.replaceAll("-", "");

		ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
		//Saving image on a temporary file
		String dir=System.getProperty("java.io.tmpdir");
		String path_param=executionId;
		String path=dir+"/"+executionId+".png";
		java.io.File file1 = new java.io.File(path);
		ChartUtilities.saveChartAsPNG(file1, chart, sbi.getWidth(), sbi.getHeight(), info);

		/// Chart created and saved
		// Set the Url for refreshing the chart: in case of document composition specify also the execution page and document_id
		refreshUrlPars = new HashMap();
		refreshUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
			
		if(sbiMode.equalsIgnoreCase("WEB") || docComposition==true){
		   	Map pars = new HashMap();
		   	pars.put("PAGE", "ExecuteBIObjectPage");
		   	pars.put("MESSAGEDET", "EXEC_PHASE_RUN");
		   	refreshUrl = urlBuilder.getUrl(request, pars);
			}
			else{
				if(urlBuilder instanceof PortletUrlBuilder){
				   	refreshUrl = ((PortletUrlBuilder)urlBuilder).getRenderUrl(request, refreshUrlPars);
				}
				else{
				  	refreshUrl = urlBuilder.getUrl(request, refreshUrlPars);
				}
			}
		   	
			//String urlPng=urlBuilder.getResourceLink(request, "/servlet/AdapterHTTP?ACTION_NAME=GET_PNG&NEW_SESSION=TRUE&userid="+userId+"&path="+path);
			String auditParameter="";
			if(executionAuditId_chart!=null){
				auditParameter="&"+AuditManager.AUDIT_ID+"="+executionAuditId_chart.toString();
			}
			
			urlPng=GeneralUtilities.getSpagoBiContext() + GeneralUtilities.getSpagoAdapterHttpUrl() + 
					"?ACTION_NAME=GET_PNG&NEW_SESSION=TRUE&userid="+userId+"&path="+path_param+"&"+LightNavigationManager.LIGHT_NAVIGATOR_DISABLED+"=TRUE"+auditParameter;
			
			//add the serie parameter
		if(datasetMap.getSelectedSeries().contains("allseries")){
			refreshUrlCategory=refreshUrl+"&serie=allseries";
		}
		else{
			refreshUrlCategory=refreshUrl;
			for(Iterator iterator = datasetMap.getSelectedSeries().iterator(); iterator.hasNext();){
				String serieS=(String)iterator.next();
				refreshUrlCategory=refreshUrlCategory+"&serie="+serieS;
			}	
		} 
		Vector tmpVec = datasetMap.getSelectedCatGroups();
		//if(datasetMap.getSelectedCatGroups().contains("allgroups")){
		if(tmpVec != null){
			if (tmpVec.contains("allgroups")){
		
				refreshUrlCategory=refreshUrl+"&cat_group=allgroups";
			}
			else{
				refreshUrlCategory=refreshUrl;
				for(Iterator iterator = tmpVec.iterator(); iterator.hasNext();){
					String groupS=(String)iterator.next();
					refreshUrlCategory=refreshUrlCategory+"&cat_group="+groupS;
				}	
			}  	
		}
		

	if(sbi.isFilter() && (sbi.getType().equalsIgnoreCase("BARCHART") || sbi.getType().equalsIgnoreCase("CLUSTERCHART"))){
	if(sbi instanceof BarCharts){
		if(((BarCharts)sbi).isFilterSeries()==true)filterSeries=true;
	}
	//else
		//if(sbi instanceof ClusterCharts){
		//	if(((ClusterCharts)sbi).isFilterSeries()==true)filterSeries=true;
		//}
	
	}
	
	showSlider = false;
	if(sbi.isSlider() && datasetMap.isMakeSlider()){
		showSlider=true;
	}


	/// If it is a linkable graph  write the MAP
	if(sbi.isLinkable()){
		PrintWriter pw = new PrintWriter(out);
		ChartUtilities.writeImageMap(pw, "chart", info,new StandardToolTipTagFragmentGenerator(),new StandardURLTagFragmentGenerator());
	}
	
%>
<!-- Begin drawing the page -->
<br>
<table align="left">
	<tr>
		<td>
		<table align="center" width="80%">

			<% 
		    // No slider needed
		if(!showSlider){
	
		    %>
			<tr>
			   <td>
				<div align="center">
					<img id="image" src="<%=urlPng%>" BORDER="0" alt="" USEMAP="#chart" />
				</div>
			   </td>
			</tr>
			<%}
		else{   /////////////////////// Beginslider creation //////////////////////////
			maxSlider=datasetMap.getCatsnum().toString();
			minSlider="1";
		%>
			
		<script type="text/javascript" language="JAVASCRIPT">
		<!--
			arrayCats=new Array(<%=datasetMap.getCatsnum().intValue()%>);
			-->
		</script>

		<%
		for (Iterator iterator = datasetMap.getCategories().keySet().iterator(); iterator.hasNext();){  
			Integer key=(Integer)iterator.next();
			String name=(String)datasetMap.getCategories().get(key);
		%>

		<script type="text/javascript" language="JAVASCRIPT">
		<!--
			arrayCats[<%=key%>]='<%=name%>';
		-->
		</script>
		<%} %>

			<%-- ORIG
			<td width="75%" align="center">
				<span class='portlet-form-field-label'> 
					<a href="javascript:void(0)" onClick="document.location.href=getAllActionUrl();"> View all </a>
						<%=datasetMap.getCatTitle()%> or select from 
				</span> 
				<span class='portlet-form-field-label' id="slider_1_1_value" width="10%" align="right">
				</span> 
				<a href="javascript:void(0)" onClick="document.location.href=getActionUrl();"> 
					<span id="slider1"></span> 
				</a>
			</td> --%>
			<%if(sbi.getPositionSlider().equalsIgnoreCase("top")){ %>
				<tr>
					<td>		
						<table id="slider" align="center">  
							<tr>
								<td width="15%">&nbsp;</td>														
								<td width="60%" align="center">							
									<a href="javascript:void(0)" onClick="document.location.href=getActionUrl();"> 
										<span  id="slider1"></span> 
									</a>	
								</td>
								<td width="15%" align="left">							
									<input class='portlet-form-input-field' type="text"	 id="slider_1_1_value" readonly style="width:30px;"/>
									<input type="submit" value="All" onClick="document.location.href=getAllActionUrl();"/>
								</td>					
								
							</tr>
						 </table> 
					</td>
				</tr>
			<%}	%>	
			<tr>
				<td align="center">
					<div>
						<img id="image" src="<%=urlPng%>" BORDER=0 alt="" USEMAP="#chart" />
					</div>
				</td>
		    </tr>
		    <%if(sbi.getPositionSlider().equalsIgnoreCase("bottom")){ %>
				<tr>
					<td>		
						<table id="slider" align="center">  
							<tr>
								<td width="15%">&nbsp;</td>														
								<td width="60%" align="center">							
									<a href="javascript:void(0)" onClick="document.location.href=getActionUrl();"> 
										<span  id="slider1"></span> 
									</a>	
								</td>
								<td width="15%" align="left">							
									<input class='portlet-form-input-field' type="text"	 id="slider_1_1_value" readonly style="width:30px;"/>
									<input type="submit" value="All" onClick="document.location.href=getAllActionUrl();"/>
								</td>					
								
							</tr>
						 </table> 
					</td>
				</tr>
			<%}	%>	
		  <!-- </table>  -->

		<% 
		}
		/////////////////////// End slider creation ////////////////////////// 
		%>
	 </td>
   </tr>



	<% 	 

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// form to limit the series if it is a barchart
	
	seriesNames=new Vector();
	catGroupsNames=new Vector();
	if(filterSeries || filterCatGroup){
			//sets the URL
			if(sbiMode.equalsIgnoreCase("WEB") || docComposition)
			{
				refreshUrlPars.put("OBJECT_ID",documentid);
			}
			else
			{
				refreshUrlSerie=refreshUrl;
			}
			%>
	<tr>
		<td>


		<table id="filterSeriesOrCatGroups" align="left">
			<!-- START FORM  -->
			<form id='serieform' name="serie" action="<%=refreshUrl%>"
				method="POST">
				<input type="hidden" name="<%=LightNavigationManager.LIGHT_NAVIGATOR_DISABLED%>" value="TRUE" /> 
				<% 	
				//refreshUrlPars.put("category",new Integer(datasetMap.getCategoryCurrent()));
					for(Iterator iterator = refreshUrlPars.keySet().iterator(); iterator.hasNext();)
						{
							String name = (String) iterator.next();
							String value=(refreshUrlPars.get(name)).toString();
						%> 	
						<input type="hidden" name="<%=name%>" value="<%=value%>" /> 
						<%}%> 
						
						<!--  ROW FOR SELECT THE SERIES--> <%
	 
	                if(filterSeries==true){ %>
			              <tr>
				             <td>
				              <div align="center" class='div_detail_form'>
				                <%				
		                           String tlab=((datasetMap.getSerTitle()!=null && !datasetMap.getSerTitle().equalsIgnoreCase("")) ? datasetMap.getSerTitle() : "series");
 								// for each possible serie 
								   if(datasetMap.getSeries()!=null){	
									 for (Iterator iterator = datasetMap.getSeries().iterator(); iterator.hasNext();) {
											String ser = (String) iterator.next(); 
											// insert the serie names for evidencing the series
											seriesNames.add(ser);
											if(datasetMap.getSelectedSeries().contains(ser) || datasetMap.getSelectedSeries().contains("allseries")){
												%> 
												<input id="serie_<%=ser%>" name="serie" value="<%=ser%>" type="checkbox" checked='checked' /> 
												<span style="<%=datasetMap.getFilterStyle()%>"><%=ser%></span> <% }
											else{ %>
												<input id="serie_<%=ser%>" name="serie" value="<%=ser%>" type="checkbox" /> 
												<span style="<%=datasetMap.getFilterStyle()%>"><%=ser%></span> <% } 
		 									}%>  
		 								<a onclick="enableSerie()" title="check all series" alt='<spagobi:message key = "SBIDev.paramUse.checkAllFreeRoles" />'>
											 <img src='<%=urlBuilder.getResourceLinkByTheme(request, "/img/expertok.gif", currTheme)%>' />
										</a> 
										<a onclick="disableSerie()" title="uncheck all series" alt='<spagobi:message key = "SBIDev.paramUse.uncheckAllFreeRoles" />'>
											<img src='<%= urlBuilder.getResourceLinkByTheme(request, "/img/erase.png", currTheme)%>' />
										</a> 
										<%
									} 
								if(filterCatGroup==false){ %> 
								   <input type="submit" value="Apply" />
								<%} %>
							</div>
						</td>
					</tr>
				<%}%>
			<!--  ROW FOR SELECT THE SERIES-->

			<!--  ROW FOR SELECT THE CATS GROUPS-->
			<%
			if(filterCatGroup==true){  	// filter cat group
			%>
				<input type="hidden" name="<%=LightNavigationManager.LIGHT_NAVIGATOR_DISABLED%>" value="TRUE" />
			<% 	
				//refreshUrlPars.put("category",new Integer(datasetMap.getCategoryCurrent()));
				for(Iterator iterator = refreshUrlPars.keySet().iterator(); iterator.hasNext();)
					{
						String name = (String) iterator.next();
						String value=(refreshUrlPars.get(name)).toString();
						%>
						<input type="hidden" name="<%=name%>" value="<%=value%>" />
				  <%}%>
			 <tr>
			   <td>
				 <div align="center" class='div_detail_form'>
				 <%     	
				  // for each possible category group
					if(((BarCharts)sbi).getCatGroupNames()!=null){	
						for (Iterator iterator = ((BarCharts)sbi).getCatGroupNames().iterator(); iterator.hasNext();) {
							String group = (String) iterator.next(); 
							catGroupsNames.add(group);
							//if(datasetMap.getSelectedCatGroups().contains(group) || datasetMap.getSelectedCatGroups().contains("allgroups")){
							if(datasetMap.getSelectedCatGroups().contains(group)){
							%> 
								<input id="cat_group_<%=group%>" name="cat_group" value="<%=group%>" type="radio" checked='checked' /> 
								<%--<span class="portlet-font" ><%=group%></span> --%>
								<span style="<%=datasetMap.getFilterStyle()%>" ><%=group%></span>  
							<%}
							else{ 
							%> 
							<input id="cat_group_<%=group%>" name="cat_group" value="<%=group%>" type="radio" /> 
							<%--<span class="portlet-font"><%=group%></span> --%> 
							<span style="<%=datasetMap.getFilterStyle()%>"><%=group%></span> 
							<%} 
		 				}
					}%> 
				   <input type="submit" value="Apply" />
				  </div>
			   </td>
			</tr>
			<%} //close filter cat group case%>
		</form>
		<!--CLOSE FORM  -->
<!-- 
		</div>
		</td>
		</tr>
 -->		
	</table>
		<!-- </div>--></td>
	</tr>

</table>
<% 
///////////////////////////////////////////////////////////////////////////////////////////////////////////////// end serie limit
}
%>


<%
	////////////////////////////////////////////Radio Buttons IF THERE ARE changeable parameters//////////////////////////////////////////////////////////
	
		%>
<div>
<table id="changepars" align="center">
	<tr>




		<%   if(sbi.isChangeableView() && !docComposition){
	    		// for each possible parameter to change creates a checkbox
	    		for (Iterator iterator = changePars.iterator(); iterator.hasNext();) {
	    			String par = (String) iterator.next(); %>
		<td align="right">
		<div class='div_detail_form'><span
			class='portlet-form-field-label'> <%=sbi.getChangeViewParameterLabel(par,0)%>
		</span></div>
		</td>
		<td align="left">
		<form name="<%=par%>" action="<%=refreshUrl%>" method="POST">
		<%if(sbi.getChangeViewParameter(par)){ %> <input type="radio"
			name="<%=par%>" value="false" onclick="this.form.submit()"
			align="left" /><%=sbi.getChangeViewParameterLabel(par,1)%> <BR>
		<input type="radio" name="<%=par%>" value="true" checked
			onclick="this.form.submit()" align="left" /><%=sbi.getChangeViewParameterLabel(par,2)%>
		<%}
	    		  		else {%> <input type="radio" name="<%=par%>" value="false"
			checked onclick="this.form.submit()" align="left" /> <%=sbi.getChangeViewParameterLabel(par,1)%><BR>
		<input type="radio" name="<%=par%>" value="true"
			onclick="this.form.submit()" align="left" /> <%=sbi.getChangeViewParameterLabel(par,2)%>
		<%} %>
		</form>
		</td>
		<%} // close for on cheangeable pars
	  				}%>
	</tr>
</table>
</div>

<% 
 if(showSlider || filterSeries){ %>

<script type="text/javascript" language="JavaScript">
 
 	function getValue() {return Test.slideZone1.getSlider('start1_1').value;}

	function getAllActionUrl() {
		
		var variable="&category=0";
		var second=variable;
		var url="<%=refreshUrlCategory%>";
		var finalUrl=url+second;
		return finalUrl;
		}
 
	function getActionUrl() {
	// if we are in document composition case then call BObjectExecutePage
		
		var variable="&category=";
		var value=getValue();
		var second=variable+value;
		var url="<%=refreshUrlCategory%>";
		var finalUrl=url+second;
		return finalUrl;
	
}


var checkableSeries = new Array(); 
<% int i=0;
	for (Iterator iterator = seriesNames.iterator(); iterator.hasNext();) {
		String ser = (String) iterator.next();
	  	%>
		checkableSeries[<%=i%>]='<%=ser%>';
	  	<%
	  	i++;
	  }
%>

	function enableSerie() {	
		for (x=0;x<checkableSeries.length;x=x+1)  {
			ser = checkableSeries[x];
			but=document.getElementById('serie_'+ser);	
			but.checked = true;
		}
	}

	function disableSerie() {	
		for (x=0;x<checkableSeries.length;x=x+1)  {
			ser = checkableSeries[x];
			but=document.getElementById('serie_'+ser);	
			but.checked = false;
		}
	}
	
	
	var checkableGroups = new Array(); 
<% int l=0;
	for (Iterator iterator = catGroupsNames.iterator(); iterator.hasNext();) {
		String cat = (String) iterator.next();
	  	%>
		checkableGroups[<%=l%>]='<%=cat%>';
	  	<%
	  	l++;
	  }
%>

	Ext.onReady(function() {

		Test = {};

		Test.slideZone1 = new Ext.ux.SlideZone('slider1', {  
			type: 'horizontal',
			size:<%=sliderWidth%>,
			sliderWidth: 18,
			sliderHeight: 21,
			maxValue: <%=maxSlider%>,
			minValue: <%=minSlider%>,
			sliderSnap: 20, //orig value 1
			sliders: [{ value: <%=datasetMap.getValueSlider()%>,  name: 'start1_1'}]
			 });
	
		Test.slideZone1.getSlider('start1_1').on('drag',
				function() {
					value= arrayCats[parseInt(this.value)];
					//orig document.getElementById('slider_1_1_value').innerHTML=value;
					document.getElementById('slider_1_1_value').value=value;
					//$('slider_1_1_value').innerHTML =value;
				})
	
			//document.getElementById('slider_1_1_value').innerHTML = currentName;		
		//$('slider_1_1_value').innerHTML = currentName;
	
	});
	
</script>

<%}
} //anto%>

<%
Integer refreshSeconds=objO.getRefreshSeconds();
if(refreshSeconds!=null && refreshSeconds.intValue()>0){
Integer refreshConvert=new Integer(refreshSeconds.intValue()*1000);
%>

<script type="text/javascript">

function refreshpage(){
if(document.getElementById('refreshimage<%= uuidO %>')){
	document.getElementById('refreshimage<%= uuidO %>').click();
	}
}
</script>

<script type="text/javascript">

    //setTimeout('window.location.reload()', <%=refreshConvert%>);
   setTimeout('javascript:refreshpage()', <%=refreshConvert%>);
   
</script>
<%} %>




<% 
	} // End no error case
	else
	{    // ERROR CASE; TRACE ON Sbi_AUDIT
	AuditManager auditManager = AuditManager.getInstance();
	if(executionAuditId_chart!=null){
		auditManager.updateAudit(executionAuditId_chart, null, new Long(System.currentTimeMillis()), "EXECUTION_FAILED", "Config Error", null);		
	   }
	}
	%>

<%-- when the execution is performed, the please while loading message is hidden --%>
<script type="text/javascript">
document.getElementById('divLoadingMessage<%= uuidO %>').style.display = 'none';
</script>






