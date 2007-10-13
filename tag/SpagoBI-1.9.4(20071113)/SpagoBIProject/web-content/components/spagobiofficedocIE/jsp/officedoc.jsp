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
<%@ include file="/jsp/portlet_base.jsp"%>

<%@ page import="it.eng.spagobi.bo.BIObject,
                 java.util.List,
                 it.eng.spagobi.constants.ObjectsTreeConstants,
                 java.util.Iterator,
                 it.eng.spagobi.bo.Engine,
                 it.eng.spagobi.bo.Domain,
                 it.eng.spagobi.bo.BIObjectParameter,
                 it.eng.spagobi.bo.dao.IDomainDAO,
                 it.eng.spagobi.bo.dao.DAOFactory,
                 it.eng.spagobi.constants.SpagoBIConstants,
                 it.eng.spagobi.services.modules.BIObjectsModule,
                 it.eng.spagobi.services.modules.ExecuteBIObjectModule,
                 it.eng.spago.navigation.LightNavigationManager,
                 org.apache.commons.httpclient.HttpClient,
                 org.apache.commons.httpclient.methods.PostMethod,
                 it.eng.spago.base.ApplicationContainer,
                 java.util.Map,
                 org.safehaus.uuid.UUIDGenerator,
                 org.safehaus.uuid.UUID,
                 it.eng.spagobi.utilities.GeneralUtilities,
                 it.eng.spagobi.managers.BIObjectNotesManager,
                 it.eng.spago.base.SessionContainer,
                 it.eng.spago.security.IEngUserProfile" %>
                 

<%
    UUIDGenerator uuidGen  = UUIDGenerator.getInstance();
    UUID uuid = uuidGen.generateTimeBasedUUID();
    String requestIdentity = "request" + uuid.toString();  
    // get module response
    SourceBean moduleResponse = (SourceBean)aServiceResponse.getAttribute("ExecuteBIObjectModule");
    // get the id of the document
    Integer biobjectId = (Integer) moduleResponse.getAttribute("biobjectId");
    // get the name of the template file
	String templateFileName = (String) moduleResponse.getAttribute("templateFileName");
   	// get the actor
    String actor = (String)aSessionContainer.getAttribute(SpagoBIConstants.ACTOR);
	// get the string of the title
    String title = (String) moduleResponse.getAttribute("title");
    
	// try to get the modality
	boolean isSingleObjExec = false;
	String modality = (String)aSessionContainer.getAttribute(SpagoBIConstants.MODALITY);
   	if( (modality!=null) && modality.equals(SpagoBIConstants.SINGLE_OBJECT_EXECUTION_MODALITY) )
   		isSingleObjExec = true;
   	
   	// try to get from the session the heigh of the output area
   	boolean heightSetted = false;
   	String heightArea = (String)aSessionContainer.getAttribute(SpagoBIConstants.HEIGHT_OUTPUT_AREA);
   	if( (heightArea==null) || (heightArea.trim().equals("")) ) {
   		heightArea = "500";
   	} else {
   		heightSetted = true;
   	}
   	
   	// build the back link
   	Map backUrlPars = new HashMap();
	backUrlPars.put("PAGE", "BIObjectsPage");
	backUrlPars.put(SpagoBIConstants.ACTOR, actor);
	backUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_BACK_TO, "1");
	String backUrl = urlBuilder.getUrl(request, backUrlPars);
   	
	
	// build the refresh button
    Map refreshUrlPars = new HashMap();
    refreshUrlPars.put("PAGE", BIObjectsModule.MODULE_PAGE);
    refreshUrlPars.put(SpagoBIConstants.ACTOR, actor);
    refreshUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
	String refreshUrl = urlBuilder.getUrl(request, refreshUrlPars);
	
	IDomainDAO domaindao = DAOFactory.getDomainDAO();
	List states = domaindao.loadListDomainsByType("STATE");
    List possibleStates = new java.util.ArrayList();
    if (actor.equalsIgnoreCase(SpagoBIConstants.DEV_ACTOR)){
    	Iterator it = states.iterator();
    	 while(it.hasNext()) {
      		    	Domain state = (Domain)it.next();
      		    	if (state.getValueCd().equalsIgnoreCase("TEST")){ 
      					possibleStates.add(state);
      				}
      	}  
    } else if (actor.equalsIgnoreCase(it.eng.spagobi.constants.SpagoBIConstants.TESTER_ACTOR)){
    	Iterator it = states.iterator();
    	 while(it.hasNext()) {
      		    	Domain state = (Domain)it.next();
      		    	if ((state.getValueCd().equalsIgnoreCase("DEV")) || ((state.getValueCd().equalsIgnoreCase("REL")))) { 
      					possibleStates.add(state);
      				}
      	}  
    } 
%>


<% 
	// IF NOT SINGLE OBJECT MODALITY SHOW DEFAULT TITLE BAR
	if(!isSingleObjExec) {
%>

<%@page import="java.util.HashMap"%>
<%@page import="it.eng.spagobi.utilities.ChannelUtilities"%>
<table class='header-table-portlet-section'>
	<tr class='header-row-portlet-section'>
    	<td class='header-title-column-portlet-section' style='vertical-align:middle;'>
           &nbsp;&nbsp;&nbsp;<%=title%>
       </td>
       <td class='header-empty-column-portlet-section'>&nbsp;</td>
       <td class='header-button-column-portlet-section'>
           <a href='<%= backUrl %>'>
                 <img title='<spagobi:message key = "SBIDev.docConf.execBIObjectParams.backButt" />' 
                      class='header-button-image-portlet-section'
                      src='<%= urlBuilder.getResourceLink(request, "/img/back.png")%>' 
                      alt='<spagobi:message key = "SBIDev.docConf.execBIObjectParams.backButt" />' />
           </a>
       </td>
       <% if ((actor.equalsIgnoreCase(SpagoBIConstants.DEV_ACTOR)) || 
    		  (actor.equalsIgnoreCase(SpagoBIConstants.TESTER_ACTOR))) {
    	   
    	    Map formUrlPars = new HashMap();
    	    formUrlPars.put("PAGE", ExecuteBIObjectModule.MODULE_PAGE);
    	    formUrlPars.put(SpagoBIConstants.ACTOR,actor );
    	    formUrlPars.put(SpagoBIConstants.MESSAGEDET, ObjectsTreeConstants.EXEC_CHANGE_STATE);
    	    formUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
    		String formUrl = urlBuilder.getUrl(request, formUrlPars);
    			  
      %>
       <form method='POST' action='<%= formUrl %>' id='changeStateForm'  name='changeStateForm'>
	       <td class='header-select-column-portlet-section'>
      			<select class='portlet-form-field' name="newState">
      			<% 
      		    Iterator iterstates = possibleStates.iterator();
      		    while(iterstates.hasNext()) {
      		    	Domain state = (Domain)iterstates.next();
      			%>
      				<option value="<%=state.getValueId() + "," + state.getValueCd()  %>"><%=state.getValueName()%></option>
      			<%  } %>
      			</select>
      			<!--br/-->
      		</td>
      		<td class='header-select-column-portlet-section'>
      			<input type='image' class='header-button-image-portlet-section' src='<%= urlBuilder.getResourceLink(request, "/img/updateState.png")%>' title='<spagobi:message key = "SBIDev.docConf.execBIObjectParams.updateButt" />' alt='<spagobi:message key = "SBIDev.docConf.execBIObjectParams.updateButt" />'/> 
      		</td>
        </form>
       <% } %>
   </tr>
</table>


<% 
	// IF SINGLE OBJECT MODALITY SHOW THE PROPER TITLE BAR
	} else {
%>

<table width='100%' cellspacing='0' border='0'>
	<tr>
		<td class='header-title-column-single-object-execution-portlet-section' style='vertical-align:middle;'>
			&nbsp;&nbsp;&nbsp;<%=title%>
		</td>
		<td class='header-empty-column-single-object-execution-portlet-section'>&nbsp;</td>
		<td class='header-button-column-single-object-execution-portlet-section'>
			<a style="text-decoration:none;" href='<%=refreshUrl%>'> 
				<img width="20px" height="20px"
					src='<%= urlBuilder.getResourceLink(request, "/img/updateState.png")%>' 
					name='refresh' 
					alt='<spagobi:message key = "SBIExecution.refresh"/>' 
					title='<spagobi:message key = "SBIExecution.refresh"/>' /> 
			</a>
		</td>
</table>



<% } %>


<!-- ***************************************************************** -->
<!-- ***************************************************************** -->
<!-- **************** START BLOCK IFRAME ***************************** -->
<!-- ***************************************************************** -->
<!-- ***************************************************************** -->


<script>
		function adaptSize() {
			iframe = window.frames['iframeexec<%=requestIdentity%>'];
			navigatorname = navigator.appName;
			height = 0;
			navigatorname = navigatorname.toLowerCase();
			if(navigatorname.indexOf('explorer')) {
				height = iframe.document.body.offsetHeight;
			} else {
				height = iframe.innerHeight;
			}
			iframeEl = document.getElementById('iframeexec<%=requestIdentity%>');
			height = height + 100;
			if(height < 300){
				height = 300;
			}
			iframeEl.style.height = height + 100 + 'px';
		}
		
</script>


<div id="divIframe<%=requestIdentity%>" style="width:98%;float:left;padding-left:2%;">
           
           <%
           		String onloadStr = " ";
           		if(!heightSetted)
           			onloadStr = " onload='adaptSize();' ";
           		String heightStr = "height:400px;";
           		if(heightSetted)
           			heightStr = "height:"+heightArea+"px;";
           %> 
            
            <iframe <%=onloadStr%> 
				   style='display:inline;<%=heightStr%>' 
				   id='iframeexec<%=requestIdentity%>' 
                   name='iframeexec<%=requestIdentity%>'  
				   src=""
                   frameborder=0  
			       width='100%' >
         	</iframe>       
                                
         	<form name="formexecution<%=requestIdentity%>" id='formexecution<%=requestIdentity%>' method="post" 
         	      action="<%=ChannelUtilities.getSpagoBiContentRepositoryServlet(request)%>/<%=templateFileName%>" 
         	      target='iframeexec<%=requestIdentity%>'>

         		<input type="hidden" name="operation" value="getTemplateFile" />
         		<input type="hidden" name="biobjectId" value="<%=biobjectId%>" />

         	<center>
         	<input id="button<%=requestIdentity%>" type="submit" value="View Output"  style='display:inline;'/>
			</center>
			</form>
         
            <script>
              button = document.getElementById('button<%=requestIdentity%>');
              button.style.display='none';
              button.click();               
            </script>
            
</div>
       


<!-- ***************************************************************** -->
<!-- ***************************************************************** -->
<!-- **************** END BLOCK IFRAME ******************************* -->
<!-- ***************************************************************** -->
<!-- ***************************************************************** -->