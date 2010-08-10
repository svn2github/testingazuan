<%@ include file="/jsp/portlet_base.jsp"%>

<%@ page import="it.eng.spagobi.bo.BIObject,
                 java.util.List,
                 it.eng.spagobi.constants.ObjectsTreeConstants,
                 java.util.Iterator,
                 it.eng.spagobi.bo.Engine,
                 javax.portlet.PortletURL,
                 it.eng.spagobi.bo.Domain,
                 it.eng.spagobi.bo.BIObjectParameter,
                 it.eng.spagobi.bo.dao.IDomainDAO,
                 it.eng.spagobi.bo.dao.DAOFactory,
                 it.eng.spagobi.constants.SpagoBIConstants,
                 it.eng.spagobi.services.modules.TreeObjectsModule,
                 it.eng.spagobi.services.modules.ExecuteBIObjectModule,
                 it.eng.spagobi.utilities.PortletUtilities,
                 it.eng.spago.navigation.LightNavigationManager,
                 org.apache.commons.httpclient.HttpClient,
                 org.apache.commons.httpclient.methods.PostMethod,
                 it.eng.spago.base.PortletAccess,
                 javax.portlet.PortletRequest, 
                 javax.portlet.PortletSession,
                 it.eng.spago.base.ApplicationContainer,
                 java.util.Map" %>

<%
    java.util.Random rand = new java.util.Random();
    int randint = rand.nextInt();
    randint = Math.abs(randint);
    String requestIdentity = "request" + randint;  
    // get module response
    SourceBean moduleResponse = (SourceBean)aServiceResponse.getAttribute("ExecuteBIObjectModule");
	// get the BiObject from the response
    BIObject obj = (BIObject)moduleResponse.getAttribute(ObjectsTreeConstants.SESSION_OBJ_ATTR);
	// get the url of the engine
	Engine engine = obj.getEngine();
    String engineurl = engine.getUrl();
    // get the map of parameters dor execution call
    Map mapPars = (Map)moduleResponse.getAttribute(ObjectsTreeConstants.REPORT_CALL_URL);
   	// get the actor
    String actor = (String)aSessionContainer.getAttribute(SpagoBIConstants.ACTOR);
	
    
	// build the string of the title
    String title = "";
    title = obj.getName();
    String objDescr = obj.getDescription();
    if( (objDescr!=null) && !(objDescr.trim().equals("")) ) 
    	title += ": " + objDescr;
    
	// try to get the modality
	boolean isSingleObjExec = false;
	String modality = (String)aSessionContainer.getAttribute(SpagoBIConstants.MODALITY);
   	if( (modality!=null) && modality.equals(SpagoBIConstants.SINGLE_OBJECT_EXECUTION_MODALITY) )
   		isSingleObjExec = true;
   	
   	// try to get from the session the heigh of the output area
   	String heightArea = (String)aSessionContainer.getAttribute(SpagoBIConstants.HEIGHT_OUTPUT_AREA);
   	if( (heightArea==null) || (heightArea.trim().equals("")) )
   		heightArea = "400";
   	
   	
   	// build the back link
   	PortletURL backUrl = renderResponse.createActionURL();
	backUrl.setParameter("PAGE", "TreeObjectsPage");
	backUrl.setParameter(SpagoBIConstants.ACTOR, actor);
	backUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_BACK_TO, "1");
	
	// build the refresh button
	PortletURL refreshUrl = renderResponse.createActionURL();
	refreshUrl.setParameter("PAGE", TreeObjectsModule.MODULE_PAGE);
	refreshUrl.setParameter(SpagoBIConstants.ACTOR, actor);
	refreshUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
	
	
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

<style>
@IMPORT url("/spagobi/css/table.css");
</style>

<% 
	// IF NOT SINGLE OBJECT MODALITY SHOW DEFAULT TITLE BAR
	if(!isSingleObjExec) {
%>

<table class='header-table-portlet-section'>
	<tr class='header-row-portlet-section'>
    	<td class='header-title-column-portlet-section'>
           <%=title%>
       </td>
       <td class='header-empty-column-portlet-section'>&nbsp;</td>
       <td class='header-button-column-portlet-section'>
           <a href='<%= backUrl.toString() %>'>
                 <img title='<spagobi:message key = "SBIDev.docConf.execBIObjectParams.backButt" />' 
                      class='header-button-image-portlet-section'
                      src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/back.png")%>' 
                      alt='<spagobi:message key = "SBIDev.docConf.execBIObjectParams.backButt" />' />
           </a>
       </td>
       <% if ((actor.equalsIgnoreCase(SpagoBIConstants.DEV_ACTOR)) || 
    		  (actor.equalsIgnoreCase(SpagoBIConstants.TESTER_ACTOR))) {
    	   	PortletURL formUrl = renderResponse.createActionURL();
  		    formUrl.setParameter("PAGE", ExecuteBIObjectModule.MODULE_PAGE);
  		   	formUrl.setParameter(SpagoBIConstants.ACTOR,actor );
		   	formUrl.setParameter(SpagoBIConstants.MESSAGEDET, 
				                ExecuteBIObjectModule.EXEC_CHANGE_STATE);
			formUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
    		  
    		  %>
       <form method='POST' action='<%= formUrl.toString() %>' id='changeStateForm'  name='changeStateForm'>
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
      			<input type='image' class='header-button-image-portlet-section' src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/updateState.png")%>' title='<spagobi:message key = "SBIDev.docConf.execBIObjectParams.updateButt" />' alt='<spagobi:message key = "SBIDev.docConf.execBIObjectParams.updateButt" />'/> 
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
		<td class='header-title-column-single-object-execution-portlet-section'>
			<%=title%>
		</td>
		<td class='header-empty-column-single-object-execution-portlet-section'>&nbsp;</td>
		<td class='header-button-column-single-object-execution-portlet-section'>
			<a style="text-decoration:none;" href='<%=refreshUrl.toString()%>'> 
				<img width="20px" height="20px"
					src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/updateState.png")%>' 
					name='refresh' 
					alt='<%=PortletUtilities.getMessage("SBIExecution.refresh", "messages")%>' 
					title='<%=PortletUtilities.getMessage("SBIExecution.refresh", "messages")%>' /> 
			</a>
		</td>
</table>

<%--table width='100%' cellspacing='0' border='0'>	
	<tr>
		<td class="portlet-section-header">
			&nbsp;&nbsp;<%=title%>
		</td>
		<td class="portlet-section-header" width="40px" align="center" valign="middle">
			<a style="text-decoration:none;" href='<%=refreshUrl.toString()%>'>
				<img width="20px" height="20px" 
				     src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/refresh.png")%>' 
				     name='refresh' 
				     alt='<%=PortletUtilities.getMessage("SBIExecution.refresh", "messages")%>' 
				     title='<%=PortletUtilities.getMessage("SBIExecution.refresh", "messages")%>' /> 
      		</a>
		</td>
	</tr>
</table--%>


<% } %>

<table width='100%' height='100%'>
 	<tr> 
       <td width='100%' >
             
            <iframe style='display:inline;' id='iframeexec<%=requestIdentity%>' 
                    name='iframeexec<%=requestIdentity%>'  
         			src="" width='100%' height="<%=heightArea%>">
         	</iframe>     
                                
         	<form name="formexecution<%=requestIdentity%>" id='formexecution<%=requestIdentity%>' method="post" 
         	      action="<%=engineurl%>" 
         	      target='iframeexec<%=requestIdentity%>'>
         	<%
         		java.util.Set keys = mapPars.keySet();
         	    Iterator iterKeys = keys.iterator();
         	    while(iterKeys.hasNext()) {
         	    	String key = iterKeys.next().toString();
         	    	String value = mapPars.get(key).toString();
         	%>
         		<input type="hidden" name="<%=key%>" value="<%=value%>" />
         	<%     	
         	    }
         	%> 
         	<center>
         	<input id="button<%=requestIdentity%>" type="submit" value="View Output"  style='display:inline;'/>
			</center>
			</form>
         
            <script>
              button = document.getElementById('button<%=requestIdentity%>');
              button.style.display='none';
              button.click();               
            </script>
                
       </td>
      
        
    </tr>
</table>





 
