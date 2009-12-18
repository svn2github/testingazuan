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
<%@ page import="java.util.Set" %>
<%@page import="java.net.URLEncoder"%>

<% 
   SourceBean sbModuleResponse = (SourceBean) aServiceResponse.getAttribute("ExecuteBIObjectModule");
   ExecutionInstance instanceO = contextManager.getExecutionInstance(ExecutionInstance.class.getName());

   String execContext = instanceO.getExecutionModality();

   Integer executionAuditId_dash = null;
   
	if (execContext == null || !execContext.equalsIgnoreCase(SpagoBIConstants.DOCUMENT_COMPOSITION)){%>
		<%@ include file="/WEB-INF/jsp/analiticalmodel/execution/header.jsp"%>
		<%
		executionAuditId_dash = executionAuditId;
	} else {
		ExecutionInstance instance = contextManager.getExecutionInstance(ExecutionInstance.class.getName());
		AuditManager auditManager = AuditManager.getInstance();
		executionAuditId_dash = auditManager.insertAudit(instance.getBIObject(), null, userProfile, instance.getExecutionRole(), instance.getExecutionModality());
   }

    String movie = ChannelUtilities.getSpagoBIContextName(request);
    //String movie = renderRequest.getContextPath();
    String relMovie = (String)sbModuleResponse.getAttribute("movie");
    if(relMovie.startsWith("/"))
    	movie = movie + relMovie;
    else movie = movie + "/" + relMovie;
	String width = (String)sbModuleResponse.getAttribute("width");
	String height = (String)sbModuleResponse.getAttribute("height");
	String dataurl = ChannelUtilities.getSpagoBIContextName(request);
	//String dataurl = renderRequest.getContextPath();
	String dataurlRel = (String)sbModuleResponse.getAttribute("dataurl");
	if(dataurlRel.startsWith("/"))
		dataurl = dataurl + dataurlRel;
	else dataurl = dataurl + "/" + dataurlRel;
	Map confParameters = (Map)sbModuleResponse.getAttribute("confParameters");
	Map dataParameters = (Map)sbModuleResponse.getAttribute("dataParameters");
	dataParameters.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
	// adding parameters for AUDIT updating
	if (executionAuditId_dash != null) {
		dataParameters.put(AuditManager.AUDIT_ID, executionAuditId_dash.toString());
	}
	
	// start to create the calling url
	// put the two dimensio parameter
	movie += "?paramHeight="+height+"&paramWidth="+width; 
	// create the dataurl string
	if (dataurl.contains("?")) dataurl += "&";
	else dataurl += "?";
	// for each data parameter append to the dataurl 
	Set dataKeys = dataParameters.keySet();
	Iterator iterDataKeys = dataKeys.iterator();
	while(iterDataKeys.hasNext()) {
		String name = (String)iterDataKeys.next();
		String value = (String)dataParameters.get(name);
	    dataurl += name + "=" + value + "&"; 
	}
	
    // for each conf parameter append to the movie url  
	Set confKeys = confParameters.keySet();
	Iterator iterConfKeys = confKeys.iterator();
	while(iterConfKeys.hasNext()) {
		String name = (String)iterConfKeys.next();
		String value = (String)confParameters.get(name);
		movie += "&" + name + "=" + value; 
	}
	
    // append to the calling url the dataurl	
	movie += "&dataurl=" + URLEncoder.encode(dataurl);
    
%>


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
       	  <param name="wmode" value="transparent">
        <EMBED  src="<%=movie%>" 
                quality=high 
                width="<%=width%>" 
                height="<%=height%>" 
                wmode="transparent" 
   			 TYPE="application/x-shockwave-flash" PLUGINSPAGE="http://www.macromedia.com/go/getflashplayer">
   		</EMBED>
	</object>    
</center>

<%@ include file="/WEB-INF/jsp/commons/footer.jsp"%>