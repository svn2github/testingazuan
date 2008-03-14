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
<%@page import="it.eng.spagobi.commons.dao.IDomainDAO"%>
<%@page import="it.eng.spagobi.commons.dao.DAOFactory"%>
<%@page import="java.util.List"%>
<%@page import="it.eng.spagobi.commons.bo.Domain"%>
<%@page import="it.eng.spagobi.analiticalmodel.document.service.ExecuteBIObjectModule"%>
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


	<link rel="stylesheet" type="text/css" href="<%=urlBuilder.getResourceLink(request, "css/printImage.css")%>" media="print">
	<link type="text/css" rel="stylesheet" href="<%=urlBuilder.getResourceLink(request, "css/extjs/ext-ux-slidezone.css")%>"/>
	<script type="text/javascript" src="<%=urlBuilder.getResourceLink(request, "js/extjs/Ext.ux.SlideZone.js")%>"></script>	
  
  
  <% 
  
	String title=""; 
	String maxSlider="";
	String minSlider="";
	String valueSlider="1";
	String refreshUrl2 = "";
	HashMap categories=null;
	int numberCatVisualization=1;
	int catsnum=0;
	int ticks=1;
	int categoryCurrent=0;
	
	
     // build the back link
	Map backUrlPars = new HashMap();
    backUrlPars.put("PAGE", "BIObjectsPage");
    backUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_BACK_TO, "1");
    String backUrl = urlBuilder.getUrl(request, backUrlPars);
    
    

	IDomainDAO domaindao = DAOFactory.getDomainDAO();
	List states = domaindao.loadListDomainsByType("STATE");
    List possibleStates = new java.util.ArrayList();
	if (userProfile.isAbleToExecuteAction(SpagoBIConstants.DOCUMENT_MANAGEMENT_DEV)){
    	Iterator it = states.iterator();
    	 while(it.hasNext()) {
      		    	Domain state = (Domain)it.next();
      		    	if (state.getValueCd().equalsIgnoreCase("TEST")){
      					possibleStates.add(state);
      				}
      	}
    } 
    else if(userProfile.isAbleToExecuteAction(SpagoBIConstants.DOCUMENT_MANAGEMENT_TEST)){
    	Iterator it = states.iterator();
    	 while(it.hasNext()) {
      		    	Domain state = (Domain)it.next();
      		    	if ((state.getValueCd().equalsIgnoreCase("DEV")) || ((state.getValueCd().equalsIgnoreCase("REL")))) {
      					possibleStates.add(state);
      				}
      	}
    }
    
    

    
    %>
    

<table id="spagoBiHeader" class='header-table-portlet-section'>
			<tr class='header-row-portlet-section'>
    			<td class='header-title-column-portlet-section' style='vertical-align:middle;'>
           			<%=title%>
       			</td>
       			<td class='header-empty-column-portlet-section'>&nbsp;</td>
       			<td class='header-button-column-portlet-section'>
           			<a href='<%=backUrl%>'>
                 		<img title='<spagobi:message key = "SBIDev.docConf.execBIObject.backButt" />' 
                      		 class='header-button-image-portlet-section'
                      		 src='<%=urlBuilder.getResourceLink(request, "/img/back.png")%>' 
                      		 alt='<spagobi:message key = "SBIDev.docConf.execBIObject.backButt" />' />
           			</a>
       			</td>
		   		<% if (!possibleStates.isEmpty()) {
			   			Map formUrlPars = new HashMap();
			   			formUrlPars.put("PAGE", ExecuteBIObjectModule.MODULE_PAGE);
			   			formUrlPars.put(SpagoBIConstants.MESSAGEDET, SpagoBIConstants.EXEC_CHANGE_STATE);
			   			formUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
			   		    String formUrl = urlBuilder.getUrl(request, formUrlPars);
    			%>
       			<form method='POST' action='<%=formUrl%>' id='changeStateForm'  name='changeStateForm'>
	       		<td class='header-select-column-portlet-section'>
      				<select class='portlet-form-field' name="newState">
      				<% 
      		    	Iterator iterstates = possibleStates.iterator();
      		    	while(iterstates.hasNext()) {
      		    		Domain state = (Domain)iterstates.next();
      				%>
      					<option value="<%=state.getValueId() + "," + state.getValueCd()  %>"><%=state.getValueName()%></option>
      				<%}%>
      				</select>
      			</td>
      			<td class='header-select-column-portlet-section'>
      				<input type='image' class='header-button-image-portlet-section' 
      				       src='<%=urlBuilder.getResourceLink(request, "/img/updateState.png")%>' 
      				       title='<spagobi:message key = "SBIDev.docConf.execBIObject.upStateButt" />' 
      				       alt='<spagobi:message key = "SBIDev.docConf.execBIObject.upStateButt" />'/> 
      			</td>
      			<td class='header-select-column-portlet-section'>
      					<A id="linkText" HREF="#" onClick="window.print();return false">
      				<IMG class='header-button-image-portlet-section' 
      				       src='<%=urlBuilder.getResourceLink(request, "/img/Print.gif")%>' 
      				       title='<spagobi:message key = "sbi.execution.print" />' 
      				       alt='<spagobi:message key = "sbi.execution.print" />'/>  
      				       </A>
      			</td>
        		</form>
       			<% } %>
   			</tr>
		</table>
  

  

	



  
  <%
 EMFErrorHandler errorHandler=aResponseContainer.getErrorHandler();
	if(!errorHandler.isOK()){
  	
  %>

    <spagobi:error/>
  
  <%}
	else
	{%>
    
<%
SessionContainer permSession = aSessionContainer.getPermanentContainer();

if(userProfile==null){
	userProfile = (IEngUserProfile) permSession.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
	userId=(String)userProfile.getUserUniqueIdentifier();
}


if(aServiceResponse.getAttribute("title")!=null){title= (String)aServiceResponse.getAttribute("title");}
	ChartImpl sbi = (ChartImpl)aServiceResponse.getAttribute("sbi");
String documentid=(String)aServiceResponse.getAttribute("documentid");
Dataset dataset=(Dataset)aServiceResponse.getAttribute("dataset");
Dataset copyDataset;


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


/////////////////////////////////////////////////////// Case few category has been selected//////////////////////////////////////////
		if(sbi.getType().equalsIgnoreCase("BARCHART")){
			categories=(HashMap)((BarCharts)sbi).getCategories();
			catsnum=((BarCharts)sbi).getCategoriesNumber();
			numberCatVisualization=(((BarCharts)sbi).getNumberCatVisualization()).intValue();
			if(request.getParameter("category")!=null){
				String catS=(String)request.getParameter("category");
				Double catD=Double.valueOf(catS);
				categoryCurrent=catD.intValue();
				if(categoryCurrent==0) valueSlider="1";
				else valueSlider=(new Integer(categoryCurrent)).toString();
				if(categoryCurrent!=0){
				HashMap cats=(HashMap)((BarCharts)sbi).getCategories();
				copyDataset=sbi.filterDataset(dataset,categories,categoryCurrent,numberCatVisualization);				
				}
				else{copyDataset=dataset;}
			}
			else{copyDataset=dataset;}
		} 
			else{copyDataset=dataset;
			}

///////////////////////////////////////////////////// End category case//////////////////////////////////////////

	
///// Linkable chart case/////////////////777

	HashMap rootPar=new HashMap();
	rootPar.put("PAGE","DirectExecutionPage");
	rootPar.put("MODULE","DirectExecutionModule");
	rootPar.put("DOCUMENT_LABEL","Report");
	rootPar.put("OPERATION","Execute");
	rootPar.put("USERNAME",userId);
	
	String  rootUrl=urlBuilder.getUrl(request,rootPar);
	if(sbi.getType().equalsIgnoreCase("BARCHART") && sbi.getSubtype().equalsIgnoreCase("linkablebar")){
		((LinkableBar)sbi).setRootUrl(rootUrl);
	}

	////////////// Chart creation/////////////////////77
	
	JFreeChart chart=null;
	// create the chart
	chart = sbi.createChart(title,copyDataset);

		//Create the temporary file
		UUIDGenerator uuidGen = UUIDGenerator.getInstance();
		UUID uuid = uuidGen.generateTimeBasedUUID();
		String executionId = uuid.toString();
		executionId = executionId.replaceAll("-", "");

		ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
		//Saving image on a temporary file
		String dir=System.getProperty("java.io.tmpdir");
		String path=dir+"/"+executionId+".png";
		java.io.File file1 = new java.io.File(path);
		ChartUtilities.saveChartAsPNG(file1, chart, sbi.getWidth(), sbi.getHeight(), info);

		/// Chart created and saved
	   			
		Map refreshUrlPars2 = new HashMap();
			refreshUrlPars2.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
		   	refreshUrl2 = urlBuilder.getUrl(request, refreshUrlPars2);
		String urlPng=urlBuilder.getResourceLink(request, "/servlet/AdapterHTTP?ACTION_NAME=GET_PNG&NEW_SESSION=TRUE&userid="+userId+"&path="+path);

		
////////////////////////////////////////////Radio Buttons IF THERE ARE changeable parameters//////////////////////////////////////////////////////////
	
		    if(sbi.isChangeableView()){
	%>
	<table align="center"><tr>
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
	    		<form  name="<%=par%>" action="<%=refreshUrl2%>" method="GET" >
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
		if((catsnum%numberCatVisualization)==0){ticks=catsnum/numberCatVisualization;}
		else{ticks=((catsnum)/numberCatVisualization)+1;}
		
		//The maximun is number_categories/numbercatvisualization, parte intera		
		maxSlider=new Integer(ticks).toString(); 
		minSlider="1"; 
		String allCatsUrl=refreshUrl2+"&category=0";
	%>
	

	<form>
		<table class="slidertableclass" align="center" >
	<tr>
	<td width="75%" align="center">
	<a href="javascript:void(0)" onClick="document.location.href=getActionUrl();"><div id="slider1"></div> </a>
	<div id="output1"> 
		<table align="center">
		<tr>
		<td id="slider_1_1_value" width="10%" align="right"  class="sliderstatusclass"></td>
		<!--  <td width="10%" align="left"><a href="javascript:void(0)" onClick="document.location.href=getActionUrl();">Select Category</a></td> --> 
		<td width="15%" align="center" class="sliderstatusclass"><a href="javascript:void(0)" onClick="document.location.href=getAllActionUrl();">View All Categories</a></td>
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
    
<%} // End no error case%>




 <script type="text/javascript" language="JavaScript">
 
 	function getValue() {return Test.slideZone1.getSlider('start1_1').value;}

	function getAllActionUrl() {
		
		var variable="&category=0";
		var second=variable;
		var url="<%=refreshUrl2%>";
		var finalUrl=url+second;
		return finalUrl;
		}
 
	function getActionUrl() {
	
		var variable="&category=";
		var value=getValue();
		var second=variable+value;
		var url="<%=refreshUrl2%>";
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
		value= parseInt(this.value);
		value="Zoom on categories: "+value;	
		value=value+ " of "+<%=ticks%>;	
		$('slider_1_1_value').innerHTML =value;
				}
		)
	
	current=<%=categoryCurrent%>;
	
	if(current==0)
	{
	
	current="All categories";
	
	}
	else{
		current="Zoom on categories: "+current;
		current=current+ " of "+<%=ticks%>;
		}
				
		$('slider_1_1_value').innerHTML = current;
	
	});
	
</script>









    