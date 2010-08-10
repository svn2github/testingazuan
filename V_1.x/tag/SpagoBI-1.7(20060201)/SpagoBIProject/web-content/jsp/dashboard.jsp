<%@ include file="/jsp/portlet_base.jsp"%>

<%@ page import="java.util.Map,
                 java.util.Set,
                 java.util.Iterator,
                 it.eng.spagobi.constants.SpagoBIConstants,
                 javax.portlet.PortletURL,
                 it.eng.spago.navigation.LightNavigationManager,
                 it.eng.spagobi.services.modules.ExecuteBIObjectModule,
                 it.eng.spagobi.bo.Domain,
                 java.util.List" %>

<%
	// control if the portlet act with single object modality.
	// get the modality of the portlet (single object execution, entire tree or filter tree)
	boolean isSingleObjExec = false;
	String modality = (String)aSessionContainer.getAttribute(SpagoBIConstants.MODALITY);
	if( (modality!=null) && modality.equals(SpagoBIConstants.SINGLE_OBJECT_EXECUTION_MODALITY) )
		isSingleObjExec = true;
    // get the actor
    String actor = (String)aSessionContainer.getAttribute(SpagoBIConstants.ACTOR);
    // get the module response 
    SourceBean moduleResponse = (SourceBean)aServiceResponse.getAttribute("ExecuteBIObjectModule");
    String title = (String)moduleResponse.getAttribute("title");
    String displayTitleBar = (String)moduleResponse.getAttribute("displayTitleBar");
    String movie = (String)moduleResponse.getAttribute("movie");
	String width = (String)moduleResponse.getAttribute("width");
	String height = (String)moduleResponse.getAttribute("height");
	String dataurl = (String)moduleResponse.getAttribute("dataurl");
	Map confParameters = (Map)moduleResponse.getAttribute("confParameters");
	Map dataParameters = (Map)moduleResponse.getAttribute("dataParameters");
	List possibleStateChanges = (List)moduleResponse.getAttribute("possibleStateChanges");
	// start to create the calling url
	// put the two dimensio parameter
	movie += "?paramHeight="+height+"&paramWidth="+width; 
	// create the dataurl string
	dataurl += "?";
	// for each data parameter append to the dataurl 
	Set dataKeys = dataParameters.keySet();
	Iterator iterDataKeys = dataKeys.iterator();
	while(iterDataKeys.hasNext()) {
		String name = (String)iterDataKeys.next();
		String value = (String)dataParameters.get(name);
	    dataurl += name + "=" + value + "&"; 
	}
    // for each conf parameter append to the data url  
	Set confKeys = confParameters.keySet();
	Iterator iterConfKeys = confKeys.iterator();
	while(iterConfKeys.hasNext()) {
		String name = (String)iterConfKeys.next();
		String value = (String)confParameters.get(name);
	    dataurl += name + "=" + value + "&"; 
	}
    // append to the calling url the dataurl	
	movie += "&dataurl=" + dataurl;
	
    // OLD CODE
	//dataurl = dataurl.replaceAll("---", "&");
	//movie += "?paramHeight="+height+"&paramWidth="+width; 
	//movie += "&" + dataurl;
	
	// build the back link
   	PortletURL backUrl = renderResponse.createActionURL();
	backUrl.setParameter("PAGE", "TreeObjectsPage");
	backUrl.setParameter(SpagoBIConstants.ACTOR, actor);
	backUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_BACK_TO, "1");
	
%>



<style>
@IMPORT url("/spagobi/css/table.css");
</style>


<% 
	// IF NOT SINGLE OBJECT MODALITY SHOW DEFAULT TITLE BAR WITH BACK BUTTON
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
		   		<% if (!possibleStateChanges.isEmpty()) {
    	   				PortletURL formUrl = renderResponse.createActionURL();
  		    			formUrl.setParameter("PAGE", ExecuteBIObjectModule.MODULE_PAGE);
  		   				formUrl.setParameter(SpagoBIConstants.ACTOR,actor );
		   				formUrl.setParameter(SpagoBIConstants.MESSAGEDET, ExecuteBIObjectModule.EXEC_CHANGE_STATE);
						formUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
    			%>
       			<form method='POST' action='<%= formUrl.toString() %>' id='changeStateForm'  name='changeStateForm'>
	       		<td class='header-select-column-portlet-section'>
      				<select class='portlet-form-field' name="newState">
      				<% 
      		    	Iterator iterstates = possibleStateChanges.iterator();
      		    	while(iterstates.hasNext()) {
      		    		Domain state = (Domain)iterstates.next();
      				%>
      					<option value="<%=state.getValueId() + "," + state.getValueCd()  %>"><%=state.getValueName()%></option>
      				<%}%>
      				</select>
      			</td>
      			<td class='header-select-column-portlet-section'>
      				<input type='image' class='header-button-image-portlet-section' src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/updateState.png")%>' title='<spagobi:message key = "SBIDev.docConf.execBIObjectParams.updateButt" />' alt='<spagobi:message key = "SBIDev.docConf.execBIObjectParams.updateButt" />'/> 
      			</td>
        		</form>
       			<% } %>
   			</tr>
		</table>
		
		
		






<% 
	// IF SINGLE OBJECT MODALITY SHOW SLIM TITLE BAR
	} else {
%>
		<% 
		  // display the title bar only if the title has been setted
		  if(displayTitleBar.trim().equalsIgnoreCase("true")) { %>
			<table width='100%' cellspacing='0' border='0'>	
				<tr>
					<td align="center" class="portlet-section-header"><%=title%></td>
				</tr>
			</table>
		<% } %>
<%  } %>








<% // HTML CODE FOR THE FLASH COMPONENT %>
<center>  
       <object  classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" 
                codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,0,0" 
                type="application/x-shockwave-flash"
                data="<%=movie%>"  
                width="<%=width%>" 
                height="<%=height%>" >
       	  <param name="movie" value="<%=movie%>">
       	  <param name="quality" value="high">
       	  <param name="scale" value="noscale">
       	  <param name="salign" value="LT">
       	  <param name="menu" value="false">
        <EMBED  src="<%=movie%>" 
                quality=high 
                width="<%=width%>" 
                height="<%=height%>" 
   			 TYPE="application/x-shockwave-flash" PLUGINSPAGE="http://www.macromedia.com/go/getflashplayer">
   		</EMBED>
	</object>    
</center>

