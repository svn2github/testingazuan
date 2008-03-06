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

<%@ include file="/jsp/commons/portlet_base.jsp"%>
<%@ page import="java.util.Map" %>
<%@page import="it.eng.spago.security.IEngUserProfile"%>
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
<%@page import="it.eng.spagobi.engines.chart.bo.charttypes.piecharts.SimplePie"%>
<%@page import="it.eng.spagobi.engines.chart.bo.charttypes.barcharts.SimpleBar"%>
<%@page import="it.eng.spagobi.commons.constants.SpagoBIConstants"%>
<link rel="stylesheet" type="text/css" href="<%=urlBuilder.getResourceLink(request, "css/printImage.css")%>" media="print">
  
  
  
  <% 
  
	String title=""; 
	if(aServiceResponse.getAttribute("title")!=null){
	title= (String)aServiceResponse.getAttribute("title");
	}
	
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
      				       title='<spagobi:message key = "SBIDev.docConf.execBIObject.printDocument" />' 
      				       alt='<spagobi:message key = "SBIDev.docConf.execBIObject.printDocument" />'/>  
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
	
ChartImpl sbi = (ChartImpl)aServiceResponse.getAttribute("sbi");
String documentid=(String)aServiceResponse.getAttribute("documentid");
Dataset dataset=(Dataset)aServiceResponse.getAttribute("dataset");
//Boolean changeViewChecked=(Boolean)aServiceResponse.getAttribute("changeviewchecked");

boolean changeViewMode=false;
if(request.getParameter("changeviewmode")!=null){
	String ch=(String)request.getParameter("changeviewmode");
	if(ch.equalsIgnoreCase("true")){
		changeViewMode=true;
	}
}

String changeVieLabel="";
if(sbi.isChangeableView()){
	if(sbi.getType().equalsIgnoreCase("PIECHART")){
		changeVieLabel=SimplePie.CHANGE_VIEW_LABEL;
		((SimplePie)sbi).setChangeViewChecked(changeViewMode);
	}
	if(sbi.getType().equalsIgnoreCase("BARCHART") && sbi.getSubtype().equalsIgnoreCase("simplebar")){
		changeVieLabel=SimpleBar.CHANGE_VIEW_LABEL;
		((SimpleBar)sbi).setChangeViewChecked(changeViewMode);
	}
}



	SessionContainer permSession = aSessionContainer.getPermanentContainer();
	
	if(userProfile==null){
	userProfile = (IEngUserProfile) permSession.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
    userId=(String)userProfile.getUserUniqueIdentifier();
	}
    
	String  rootUrl=urlBuilder.getUrl(request,new HashMap());
	if(sbi.getType().equalsIgnoreCase("BARCHART") && sbi.getSubtype().equalsIgnoreCase("linkablebar")){
		((LinkableBar)sbi).setRootUrl(rootUrl);
	}
    
	JFreeChart chart=null;
	// create the chart
			chart = sbi.createChart(title,dataset);


		//Create the temporary dir
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
		
	//String urlAction=urlBuilder.getResourceLink(request, "/servlet/AdapterHTTP?ACTION_NAME=GET_JFREECHART&NEW_SESSION=TRUE&userid="+userId+"&documentid="+documentId);


    
 
				

	   		Map refreshUrlPars = new HashMap();
	   			refreshUrlPars.put("ACTION_NAME", "CREATE_CHART");
	   			refreshUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
	   			refreshUrlPars.put("documentid", documentid);
	   			String refreshUrl = urlBuilder.getUrl(request, refreshUrlPars);
	   			

		   		Map refreshUrlPars2 = new HashMap();
			   			refreshUrlPars2.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
		   			String refreshUrl2 = urlBuilder.getUrl(request, refreshUrlPars);
		   			
		   			
		   			
	   			
	   			
	   			
	   			
	   			
				%>

	<div align=center>
	<%
	    String urlPng=urlBuilder.getResourceLink(request, "/servlet/AdapterHTTP?ACTION_NAME=GET_PNG&NEW_SESSION=TRUE&userid="+userId+"&path="+path);

	
	
	// It passess changeView in POST (recall the actions) Not used.
	
	/*
	    if(sbi.isChangeableView()){
	    	%>
	    	<form  name="changeviewchecked" action="<%=refreshUrl%>" method="POST" >
				<%if(changeViewChecked.equals(new Boolean(true))){ %>
 					<input name="changeviewchecked" type="checkbox" value="true" checked onclick="this.form.submit()" align="left">aaaa</input>
 							<%}
					else{%>
								<input name="changeviewchecked" type="checkbox" value="true" onclick="this.form.submit()" align="left">aaa</input>
							<%} %>
			</form> 
			<BR>
	    	<%
	    } 
	*/
	    
	    
	    
	    if(sbi.isChangeableView()){
	    	%>
	    	<form  name="changeviewmode" action="<%=refreshUrl2%>" method="GET" >
				<%if(changeViewMode){ %>
 					<input name="changeviewmode" type="checkbox" value="true" checked onclick="this.form.submit()" align="left"><%=changeVieLabel%></input>
 							<%}
					else{%>
								<input name="changeviewmode" type="checkbox" value="true" onclick="this.form.submit()" align="left"><%=changeVieLabel%></input>
							<%} %>
			</form> 
			<BR>
	    	<%
	    }
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    if(sbi.isLinkable()){
		PrintWriter pw = new PrintWriter(out);
		ChartUtilities.writeImageMap(pw, "chart", info,new StandardToolTipTagFragmentGenerator(),new StandardURLTagFragmentGenerator());
	    }
	    %>
		
    <img id="image" src="<%=urlPng%>" BORDER=1 width="AUTO" height="AUTO" alt="Error in displaying the chart" USEMAP="#chart"/>
    </div>
    <%} %>

    

    