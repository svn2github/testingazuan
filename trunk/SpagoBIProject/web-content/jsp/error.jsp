<%@ include file="/jsp/portlet_base.jsp"%>

<%@ page import="it.eng.spago.error.EMFErrorHandler, 
                 java.util.Collection, 
                 it.eng.spago.error.EMFAbstractError,
                 it.eng.spago.navigation.LightNavigationManager,
                 java.util.HashMap,
                 java.util.Set,
                 java.util.List,
                 java.util.Iterator" %>
<%@page import="javax.portlet.PortletURL"%>


<%
    // delete validation xml envelope from session
    List sessAttrs = aSessionContainer.getAttributeNames();
    Iterator iterAttrs = sessAttrs.iterator();
    String nameAttrs = null;
    while(iterAttrs.hasNext()) {
    	nameAttrs = (String)iterAttrs.next();
    	if(nameAttrs.startsWith("VALIDATE_PAGE_")) {
    		aSessionContainer.delAttribute(nameAttrs);
    	}
    }

    // try to get from the session a pre-built back url 
    PortletURL sessionback = null;
    Object sessBackObj = aSessionContainer.getAttribute("NAVIGATION");
    if(sessBackObj!= null)
    	sessionback = (PortletURL)sessBackObj;


    // recover error handler and error collection 
    EMFErrorHandler errorHandler = aResponseContainer.getErrorHandler();  
	Collection errors = errorHandler.getErrors();
	Iterator iter = errors.iterator();  
	
	// try to get addition info from one of the errors (the first add info found will be taken)
	Object addInfo = null;
	while(iter.hasNext()) {
		EMFAbstractError abErr = (EMFAbstractError)iter.next();
		Object errAddInfo = abErr.getAdditionalInfo();
	    if(errAddInfo!=null) {
	    	addInfo = errAddInfo;
	    	break;
	    }
	}
%>




<%
    // built url
	String backUrl = null;

    if(sessionback!=null) {
    	backUrl = sessionback.toString();
    } else {   
    	Map backUrlPars = new HashMap();     	
		if( (addInfo!=null) && (addInfo instanceof HashMap) ) {
		     HashMap map = (HashMap)addInfo;
		     Set keys = map.keySet();
		     Iterator iterKey = keys.iterator();
		     while(iterKey.hasNext()) {
				String key = (String)iterKey.next();
				String value = (String)map.get(key);
				backUrlPars.put(key, value);	     
		     }
		 } else {
			 backUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_BACK_TO, "1");
		 }
		 backUrl = urlBuilder.getUrl(request, backUrlPars);
    }
    
%>






<%@page import="java.util.Map"%>
<table class='header-table-portlet-section'>		
	<tr class='header-row-portlet-section'>
		<td class='header-title-column-portlet-section'>
			<spagobi:message key = "SBIErrorPage.title" />
		</td>
		<td class='header-empty-column-portlet-section'>&nbsp;</td>
		<td class='header-button-column-portlet-section'>
			<a href='<%= backUrl.toString() %>'> 
      			<img class='header-button-image-portlet-section' 
      			     title='<spagobi:message key = "SBIErrorPage.backButt" />' 
      			     src='<%=urlBuilder.getResourceLink(request, "/img/back.png")%>' 
      			     alt='<spagobi:message key = "SBIErrorPage.backButt" />' />
			</a>
		</td>
	</tr>
</table>



<div style='width:100%;text-align:center;'>
	<div class="portlet-msg-error">
	    <% 
	    	iter = errors.iterator(); 
	        EMFAbstractError error = null;
	        String description = "";
	    	while(iter.hasNext()) {
	    		error = (EMFAbstractError)iter.next();
	 		    description = error.getDescription();
	 		    if(addInfo==null) {
	 		    	addInfo = error.getAdditionalInfo();
	 		    }
	    %>
			<%= description %>
			<br/>
		<% } %>
	</div>
</div>		















