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
<%@page import="it.eng.spagobi.engines.chart.charttypes.CreateJFreeChart"%>
<%@page import="org.jfree.chart.ChartUtilities"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="org.jfree.chart.imagemap.StandardToolTipTagFragmentGenerator"%>
<%@page import="org.jfree.chart.imagemap.StandardURLTagFragmentGenerator"%>
<link rel="stylesheet" type="text/css" href="<%=urlBuilder.getResourceLink(request, "css/printImage.css")%>" media="print">
  

<%
	
    // get module response
    SourceBean moduleResponse = (SourceBean)aServiceResponse.getAttribute("ExecuteBIObjectModule");

    // get the user profile from session
   	
	SessionContainer permSession = aSessionContainer.getPermanentContainer();
	IEngUserProfile userProfile = (IEngUserProfile)permSession.getAttribute(IEngUserProfile.ENG_USER_PROFILE);

    String title = (String)moduleResponse.getAttribute("title");
    //String userId= (String)moduleResponse.getAttribute("userid");
    String documentId= (String)moduleResponse.getAttribute("documentid");
	
    IEngUserProfile profile = (IEngUserProfile) permSession.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
    String userId=(String)profile.getUserUniqueIdentifier();
   
    
    String urlAction=urlBuilder.getResourceLink(request, "/servlet/AdapterHTTP?ACTION_NAME=GET_JFREECHART&NEW_SESSION=TRUE&userid="+userId+"&documentid="+documentId);


 
    boolean threeD=false;
  	if(request.getParameter("threed")!=null){
  		String three=(String)request.getParameter("threed");
  		if(three.equalsIgnoreCase("true")){
  			threeD=true;
  		}
  	}
    
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
    
    // build the back link
	Map backUrlPars = new HashMap();
    backUrlPars.put("PAGE", "BIObjectsPage");
    backUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_BACK_TO, "1");
    String backUrl = urlBuilder.getUrl(request, backUrlPars);
    
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
	   			Map refreshUrlPars = new HashMap();
	   			//refreshUrlPars.put("userid", userId);
	   			refreshUrlPars.put("title", title);
	   			//refreshUrlPars.put("documentid", documentId);
	   			refreshUrlPars.put("MESSAGEDET", "EXEC_PHASE_CREATE_PAGE");
	   			refreshUrlPars.put("PAGE", "ExecuteBIObjectPage");
	   			//refreshUrlPars.put("OBJECT_ID", "28");
	   			refreshUrlPars.put("NAVIGATOR_FREEZE", "TRUE");
	   			
	   			
	   			
	   			//refreshUrlPars.put(SpagoBIConstants.PUBLISHER_NAME, "CHARTKPI");
	   			String refreshUrl = urlBuilder.getUrl(request, refreshUrlPars);
				String  rootUrl=urlBuilder.getUrl(request,new HashMap());
				%>
				
				


				

				
	<div align=center>
		<%
		CreateJFreeChart c=new CreateJFreeChart();
		c.setThreeDCecked(threeD);
		c.setRootUrl(rootUrl);
		JFreeChart chart=c.createChart(userId,documentId);
		String path=c.getPath();
	    String urlPng=urlBuilder.getResourceLink(request, "/servlet/AdapterHTTP?ACTION_NAME=GET_PNG&NEW_SESSION=TRUE&userid="+userId+"&path="+path);

	    if(c.getChangeView().equals(new Boolean(true))){
	    	%>
	    	<form  name="threed" action="<%=refreshUrl%>" method="GET" >
				<%if(threeD){ %>
 					<input name="threed" type="checkbox" value="true" checked onclick="this.form.submit()" align="left"> 3D View</input>
 							<%}
					else{%>
								<input name="threed" type="checkbox" value="true" onclick="this.form.submit()" align="left"> 3D View</input>
							<%} %>
			</form> 
	    	<%
	    }
	    
	    if(c.getLinkable().equals(new Boolean(true))){
		PrintWriter pw = new PrintWriter(out);
		ChartUtilities.writeImageMap(pw, "chart", c.getInfo(),new StandardToolTipTagFragmentGenerator(),new StandardURLTagFragmentGenerator());
	    }

	    
	    
	    
	    %>
		
    <img id="image" src="<%=urlPng%>" BORDER=1 width="AUTO" height="AUTO" alt="Error in displaying the chart" USEMAP="#chart"/>
    </div>
    
    <spagobi:error/>
    

    