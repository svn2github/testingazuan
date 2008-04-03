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
<%@page import="it.eng.spagobi.engines.chart.bo.ChartImpl"%>
<%@page import="org.jfree.chart.ChartRenderingInfo"%>
<%@page import="it.eng.spagobi.engines.chart.bo.charttypes.barcharts.LinkableBar"%>
<%@page import="org.jfree.data.general.Dataset"%>
<%@page import="org.safehaus.uuid.UUIDGenerator"%>

<%@page import="org.safehaus.uuid.UUID"%>
<%@page import="org.jfree.chart.entity.StandardEntityCollection"%>
<%@page import="it.eng.spago.error.EMFErrorHandler"%>
<%@page import="java.util.Vector"%>
<%@page import="it.eng.spagobi.engines.chart.bo.charttypes.barcharts.BarCharts"%>


	<%@page import="org.jfree.data.category.DefaultCategoryDataset"%>


<%
BIObject objO=null;
String uuidO="";
boolean docComposition=false;

SourceBean sbModuleResponse = (SourceBean) aServiceResponse.getAttribute("ExecuteBIObjectModule");
   String execContext = (String)sbModuleResponse.getAttribute(SpagoBIConstants.EXECUTION_CONTEXT);
   if (execContext == null || !execContext.equalsIgnoreCase(SpagoBIConstants.DOCUMENT_COMPOSITION)){%>
		<%@ include file="/jsp/analiticalmodel/execution/header.jsp"%>
	<%	
objO=obj;
	uuidO=uuid;

   }
   else // in document composition case doesn't call header so set Object and uuid
   {
	   docComposition=true;
	   UUIDGenerator uuidGenO  = UUIDGenerator.getInstance();
	   UUID uuidObjO = uuidGenO.generateTimeBasedUUID();
	   uuidO = uuidObjO.toString();
	   uuidO = uuidO.replaceAll("-", "");
	   
	   objO = (BIObject) sbModuleResponse.getAttribute(ObjectsTreeConstants.SESSION_OBJ_ATTR);
   }
   %>

<!-- 
<link rel="stylesheet" type="text/css" href="<%=urlBuilder.getResourceLink(request, "css/printImage.css")%>" media="print">
-->	
	<%@page import="it.eng.spagobi.analiticalmodel.document.bo.BIObject"%>
<link type="text/css" rel="stylesheet" href="<%=urlBuilder.getResourceLink(request, "css/extjs/ext-ux-slidezone.css")%>"/>
	<script type="text/javascript" src="<%=urlBuilder.getResourceLink(request, "js/extjs/Ext.ux.SlideZone.js")%>"></script>	
  
  
  <% 
  	String titleChart="";
	String maxSlider="";
	String minSlider="";
	String valueSlider="1";
	String refreshUrl = "";
	HashMap categories=null;
	String serie="allseries";
	List series=null;
	int numberCatVisualization=1;
	int catsnum=0;
	int ticks=1;
	int categoryCurrent=0;
	String categoryCurrentName="";
	String catTitle="category";
	String serTitle="serie";
	String refreshUrlCategory="";
	String refreshUrlSerie="";

 EMFErrorHandler errorHandler=aResponseContainer.getErrorHandler();
	if(errorHandler.isOK()){    
SessionContainer permSession = aSessionContainer.getPermanentContainer();

if(userProfile==null){
	userProfile = (IEngUserProfile) permSession.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
	userId=(String)userProfile.getUserUniqueIdentifier();
}


if(sbModuleResponse.getAttribute("title")!=null){titleChart=(String)sbModuleResponse.getAttribute("title");}
	//ChartImpl sbi = (ChartImpl)aServiceResponse.getAttribute("sbi");
ChartImpl sbi = (ChartImpl)sbModuleResponse.getAttribute("sbi");




String documentid=(objO.getId()).toString();
//String documentid=(String)aServiceResponse.getAttribute("documentid");
Dataset dataset=(Dataset)sbModuleResponse.getAttribute("dataset");
Dataset copyDataset=null;



// get wich pars can the user set
Vector changePars=(Vector)sbi.getPossibleChangePars();
//check for each one if a changeparameter has ben set
	for (Iterator iterator = changePars.iterator(); iterator.hasNext();) {
			String par = (String) iterator.next();
			if(request.getParameter(par)!=null){
				String ch=(String)request.getParameter(par);
				if(ch.equalsIgnoreCase("true")){
					sbi.setChangeViewsParameter(par,true);}
				else {if(ch.equalsIgnoreCase("false"));
					{
						sbi.setChangeViewsParameter(par,false);}
							}
				}
		}



	// in the case of document composition check if serie or category have been previously defined

	if(docComposition){
		if(sbModuleResponse.getAttribute("category")!=null){
		String catS=(String)sbModuleResponse.getAttribute("category");
		Double catD=Double.valueOf(catS);
		categoryCurrent=catD.intValue();
		}
		if(sbModuleResponse.getAttribute("serie")!=null){
		serie=(String)sbModuleResponse.getAttribute("serie");
		
		}
		
		
	}

/////////////////////////////////////////////////////// Case few category has been selected//////////////////////////////////////////
		if(sbi.getType().equalsIgnoreCase("BARCHART")){
	series=new Vector(((DefaultCategoryDataset)dataset).getRowKeys());
			categories=(HashMap)((BarCharts)sbi).getCategories();
			catsnum=((BarCharts)sbi).getCategoriesNumber();
			numberCatVisualization=(((BarCharts)sbi).getNumberCatVisualization()).intValue();
			catTitle=((BarCharts)sbi).getCategoryLabel();
			serTitle=((BarCharts)sbi).getValueLabel();
			DefaultCategoryDataset catDataset=(DefaultCategoryDataset)dataset;
			copyDataset=new DefaultCategoryDataset();					
			copyDataset=(DefaultCategoryDataset)catDataset.clone();

			// if slider specifies a category than set view from that point
			if(request.getParameter("category")!=null){
				String catS=(String)request.getParameter("category");
			Double catD=Double.valueOf(catS);
				categoryCurrent=catD.intValue();
			}
			else{ //else set view from first category
				categoryCurrent=1;
			}
				valueSlider=(new Integer(categoryCurrent)).toString();
				HashMap cats=(HashMap)((BarCharts)sbi).getCategories();
				
				if(categoryCurrent!=0){
					categoryCurrentName=(String)cats.get(new Integer(categoryCurrent));
				copyDataset=sbi.filterDataset(copyDataset,categories,categoryCurrent,numberCatVisualization);				
				}
				else{
					categoryCurrentName="All";
				}
				
				// choose a serie
			if(request.getParameter("serie")!=null)
				{serie=(String)request.getParameter("serie");}
			if(!serie.equalsIgnoreCase("allseries")){
				copyDataset=((BarCharts)sbi).filterDatasetSeries(copyDataset,serie);	
				int wichSerie=series.indexOf(serie)+1;
				((BarCharts)sbi).setCurrentSerie(wichSerie);
				
			}
		}


	if(copyDataset==null){copyDataset=dataset;}

///////////////////////////////////////////////////// End category case//////////////////////////////////////////

	
///// Linkable chart case/////////////////777

	HashMap rootPar=new HashMap();
	if(sbi.getType().equalsIgnoreCase("BARCHART") && sbi.getSubtype().equalsIgnoreCase("linkablebar")){
		
		String rootDocParameter=((LinkableBar)sbi).getDocument_Parameters(((LinkableBar)sbi).getDrillParameter());
		if(!rootDocParameter.equals("")){
		rootPar.put("DOCUMENT_PARAMETERS",rootDocParameter);}
		
		//rootPar.put("PAGE","ExecuteBIObjectPage");
		rootPar.put("PAGE","DirectExecutionPage");
		//rootPar.put("MODULE","DirectExecutionModule");
		rootPar.put("OPERATION","Execute");
		//rootPar.put("MESSAGEDET","EXEC_PHASE_CREATE_PAGE");
		rootPar.put("USERNAME",userId);

		//get from the linkableBar the label and eventually the parameters to pass
		if(((LinkableBar)sbi).getDrillLabel()!=null)
					rootPar.put(ObjectsTreeConstants.OBJECT_LABEL,((LinkableBar)sbi).getDrillLabel());
		
		//for each parameter needed
		HashMap parameters=((LinkableBar)sbi).getDrillParameter();
		if(parameters!=null){
			for(Iterator iterator=parameters.keySet().iterator(); iterator.hasNext();){
				String name = (String) iterator.next();			
				String value=(String)parameters.get(name);
				rootPar.put(name,value);
			}
		}

		String  rootUrl=urlBuilder.getUrl(request,rootPar);
	
		String completeUrl=rootUrl;
	
		if(docComposition){
			((LinkableBar)sbi).setMode(SpagoBIConstants.DOCUMENT_COMPOSITION);
			completeUrl="javascript:parent.parent.execDrill(this.name, '"+rootUrl;
				}
			else
			{
				((LinkableBar)sbi).setMode("normal");
			}
		

		((LinkableBar)sbi).setRootUrl(completeUrl);
	}

		
	////////////// Chart creation/////////////////////77
	
	JFreeChart chart=null;
	// create the chart
	chart = sbi.createChart(titleChart,copyDataset);

		//Create the temporary file
		//UUIDGenerator uuidGen = UUIDGenerator.getInstance();
		//UUID uuid = uuidGen.generateTimeBasedUUID();
		String executionId = uuidO;
		executionId = executionId.replaceAll("-", "");

		ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
		//Saving image on a temporary file
		String dir=System.getProperty("java.io.tmpdir");
		String path=dir+"/"+executionId+".png";
		java.io.File file1 = new java.io.File(path);
		ChartUtilities.saveChartAsPNG(file1, chart, sbi.getWidth(), sbi.getHeight(), info);

		/// Chart created and saved
	   		
		
		// Set the Url for refreshing the chart: in case of document composition specify also the execution page and document_id
		Map refreshUrlPars = new HashMap();
			refreshUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
			if(docComposition==true){
		   	refreshUrl=GeneralUtilities.getSpagoBiContextAddress() + GeneralUtilities.getSpagoAdapterHttpUrl() + "?PAGE=ExecuteBIObjectPage&MESSAGEDET=EXEC_PHASE_CREATE_PAGE&OBJECT_ID="+documentid;
			}
			else{
		   	refreshUrl = urlBuilder.getUrl(request, refreshUrlPars);
			}
		   	


			//String urlPng=urlBuilder.getResourceLink(request, "/servlet/AdapterHTTP?ACTION_NAME=GET_PNG&NEW_SESSION=TRUE&userid="+userId+"&path="+path);
			String urlPng=GeneralUtilities.getSpagoBiContextAddress() + GeneralUtilities.getSpagoAdapterHttpUrl() + "?ACTION_NAME=GET_PNG&NEW_SESSION=TRUE&userid="+userId+"&path="+path;
			refreshUrlCategory=refreshUrl+"&serie="+serie;
		   	
		   	
		   	// form to limit the series if it is a barchart
if(sbi.getType().equalsIgnoreCase("BARCHART")){
		//sets the URL
	if(docComposition)
	{
		refreshUrlSerie=GeneralUtilities.getSpagoBiContextAddress() + GeneralUtilities.getSpagoAdapterHttpUrl();
		refreshUrlPars.put("PAGE","ExecuteBIObjectPage");
		refreshUrlPars.put("MESSAGEDET","EXEC_PHASE_CREATE_PAGE");
		refreshUrlPars.put("OBJECT_ID",documentid);
		
	}
	else
	{
		refreshUrlSerie=refreshUrl;
	}
	%>
	<div class='div_detail_form'>
		<span class='portlet-form-field-label'>
			Select from <%=serTitle%>
		</span>
	</div>
		
	<form name="serie" action="<%=refreshUrlSerie%>" method="GET" >	
<% 	
	refreshUrlPars.put("category",new Integer(categoryCurrent));
	for(Iterator iterator = refreshUrlPars.keySet().iterator(); iterator.hasNext();)
	{
	String name = (String) iterator.next();
	String value=(refreshUrlPars.get(name)).toString();
%>		
	<input type="hidden" name="<%=name%>" value="<%=value%>"/>	

	<%
	}%>
	<select name="serie" onchange="this.form.submit();">
	<%if(serie.equalsIgnoreCase("allseries")){ %>
		<option value="allseries" selected="selected">View all</option>
	<%} else {%>
		<option value="allseries">View all <%=serTitle%></option>
	<%} %>
		
	<%     	
	    // for each possible serie 
	    	for (Iterator iterator = series.iterator(); iterator.hasNext();) {
	    		String ser = (String) iterator.next(); 
	    		if(ser.equals(serie)){
	    		%>
		<option value="<%=ser%>" selected="selected"><%=ser%></option>
	<%}else{ %>
		<option value="<%=ser%>"><%=ser%></option>
	<%} 
	} %>
	
	
	
	</select>
	</form>
<% 
}

	/// If it is a linkable graph
	    if(sbi.isLinkable()){
		PrintWriter pw = new PrintWriter(out);
		ChartUtilities.writeImageMap(pw, "chart", info,new StandardToolTipTagFragmentGenerator(),new StandardURLTagFragmentGenerator());
	    }
	
	
	
	
	

	    boolean makeSlider=false;
	    if((sbi.getType().equalsIgnoreCase("BARCHART")) && (catsnum)>numberCatVisualization){
		makeSlider=true;	    	
	    }

	    // No slider needed
	    if(makeSlider==false){
	    %>
  	 <div align="center">
	
   <img id="image" src="<%=urlPng%>" BORDER="1" alt="Error in displaying the chart" USEMAP="#chart"/>

	</div>
	<%}
	else{
	// Slider needed
	%>
	
		<div align="center">
		
    	    	<% /////////////////////// Beginslider creation //////////////////////////
	//if it's a barchart creates the slider! Only if categories number more than how many you have to show
    	
	//calculate the number of ticks
		//if((catsnum%numberCatVisualization)==0){ticks=catsnum/numberCatVisualization;}
		//else{ticks=((catsnum)/numberCatVisualization)+1;}
		
		//The maximun is number_categories/numbercatvisualization, parte intera		
		//maxSlider=new Integer(ticks).toString(); 
		
		maxSlider=new Integer(catsnum).toString();
		
		minSlider="1";
	%>
	
	<script type="text/javascript" language="JAVASCRIPT">
		<!--
		arrayCats=new Array(<%=catsnum%>);
		-->
	</script>
		<%
		for (Iterator iterator = categories.keySet().iterator(); iterator.hasNext();){  
			Integer key=(Integer)iterator.next();
			String name=(String)categories.get(key);
	%>

		<script type="text/javascript" language="JAVASCRIPT">
			<!--
				arrayCats[<%=key%>]='<%=name%>';
			//-->
		</script>
	<%} %>
	
	
	

	<form id="sliderform">
		<table class="slidertableclass" align="center" >
			<tr>
				<td width="75%" align="center">
					<a href="javascript:void(0)" onClick="document.location.href=getActionUrl();">
							<div id="slider1"></div> 
					</a>
					<div id="output1"> 
						<table align="center">
							<tr>
								<td id="slider_1_1_value" width="10%" align="right"  class="sliderstatusclass"></td>
								<!--  <td width="10%" align="left"><a href="javascript:void(0)" onClick="document.location.href=getActionUrl();">Select Category</a></td> --> 
								<td width="15%" align="center" class="sliderstatusclass"><a href="javascript:void(0)" onClick="document.location.href=getAllActionUrl();">View all <%=catTitle%></a></td>
							</tr>
						</table>
					</div>
</td>
			</tr>
		</table>
	
	<BR>
</form> 
 <img id="image" src="<%=urlPng%>" BORDER=1 alt="Error in displaying the chart" USEMAP="#chart"/>    

</div>

	<% 
	}
	/////////////////////// End slider creation ////////////////////////// 
	%>
    
    
    
    
    
    
    
<%

	
////////////////////////////////////////////Radio Buttons IF THERE ARE changeable parameters//////////////////////////////////////////////////////////

if(sbi.isChangeableView() && !docComposition){
	%>
	<table id="changepars" align="center"><tr>
	<%     	
	    // for each possible parameter to change creates a checkbox
	    	for (Iterator iterator = changePars.iterator(); iterator.hasNext();) {
	    		String par = (String) iterator.next(); %>
	  
	<td align="right">
			<div class='div_detail_form'>
					<span class='portlet-form-field-label'>
						<%=sbi.getChangeViewParameterLabel(par,0)%> 
					</span>
				</td>
				<td align="left">
	    		<form  name="<%=par%>" action="<%=refreshUrl%>" method="GET" >
	    		  <%if(sbi.getChangeViewParameter(par)){ %>
	    		  <input type="radio" name="<%=par%>" value="false" onclick="this.form.submit()" align="left"/><%=sbi.getChangeViewParameterLabel(par,1)%> <BR>
 				  <input type="radio" name="<%=par%>" value="true"  checked  onclick="this.form.submit()" align="left"/><%=sbi.getChangeViewParameterLabel(par,2)%>  
 			  <%}
	    		  else {%>
	    		 <input type="radio" name="<%=par%>" value="false" checked onclick="this.form.submit()" align="left"/>  <%=sbi.getChangeViewParameterLabel(par,1)%><BR>
				<input type="radio" name="<%=par%>" value="true" onclick="this.form.submit()" align="left"/>  <%=sbi.getChangeViewParameterLabel(par,2)%>
 	    		  <%} %>
 	    		</form>
 	     </div>
	   </td>
	     	    	<% 
	    	}
	%>  </tr></table>
		<BR>
		<BR>
	</div>
	<% 
		    }
	
	
	
	
	
	
	
	
	
	
	} // End no error case%>




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
		

	
	
	Ext.onReady(function() {

		Test = {};

		Test.slideZone1 = new Ext.ux.SlideZone('slider1', {  
			type: 'horizontal',size:500, sliderWidth: 18,sliderHeight: 21,maxValue: <%=maxSlider%>,minValue: <%=minSlider%>,sliderSnap: 1,sliders: [{ value: <%=valueSlider%>,  name: 'start1_1'
					}]
			 });
	
		Test.slideZone1.getSlider('start1_1').on('drag',
		function() {
		value= arrayCats[parseInt(this.value)];
		value="View <%=catTitle%>: "+value;		
		$('slider_1_1_value').innerHTML =value;
				}
		)
	
		currentName="View <%=catTitle%>:";
						
		$('slider_1_1_value').innerHTML = currentName;
	
	});
	
</script>









    