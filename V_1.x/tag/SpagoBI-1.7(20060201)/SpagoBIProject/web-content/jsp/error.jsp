<%@ include file="/jsp/portlet_base.jsp"%>

<%@ page import="it.eng.spago.error.EMFErrorHandler, 
                 java.util.Collection, 
                 it.eng.spago.error.EMFAbstractError,
                 it.eng.spago.navigation.LightNavigationManager,
                 javax.portlet.PortletURL, 
                 java.util.HashMap,
                 java.util.Set,
                 java.util.List,
                 java.util.Iterator" %>

<!-- table width='100%' cellspacing='0' border='0'>		
	<tr height='40'>
		<th align='center'><spagobi:message key = "SBIErrorPage.title" /></th>
	</tr>
</table-->

<%
   
    List sessAttrs = aSessionContainer.getAttributeNames();
    Iterator iterAttrs = sessAttrs.iterator();
    String nameAttrs = null;
    while(iterAttrs.hasNext()) {
    	nameAttrs = (String)iterAttrs.next();
    	if(nameAttrs.startsWith("VALIDATE_PAGE_")) {
    		aSessionContainer.delAttribute(nameAttrs);
    	}
    }

    PortletURL sessionback = null;
    Object sessBackObj = aSessionContainer.getAttribute("NAVIGATION");
    if(sessBackObj!= null)
    	sessionback = (PortletURL)sessBackObj;


    EMFErrorHandler errorHandler = aResponseContainer.getErrorHandler();  
	Collection errors = errorHandler.getErrors();
	Iterator iter = errors.iterator();  
	Object addInfo = null;  
%>

<%
    PortletURL backUrl = null;
    if(sessionback!=null) {
    	backUrl = sessionback;
    } else {
    	backUrl = renderResponse.createActionURL();
		if( (addInfo!=null) && (addInfo instanceof HashMap) ) {
		     HashMap map = (HashMap)addInfo;
		     Set keys = map.keySet();
		     Iterator iterKey = keys.iterator();
		     while(iterKey.hasNext()) {
				String key = (String)iterKey.next();
				String value = (String)map.get(key);
				backUrl.setParameter(key, value);	     
		     }
		 } else {
		    backUrl.setParameter("NAVIGATOR_BACK", "1");   
		 } 
    }
    backUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_BACK_TO, "1");
%>

<style>
@IMPORT url("/spagobi/css/table.css");
</style>

<table class='header-table-portlet-section'>		
	<tr class='header-row-portlet-section'>
		<td class='header-title-column-portlet-section'>
			<spagobi:message key = "SBIErrorPage.title" />
		</td>
		<td class='header-empty-column-portlet-section'>&nbsp;</td>
		<td class='header-button-column-portlet-section'>
			<a href='<%= backUrl.toString() %>'> 
      			<img class='header-button-image-portlet-section' title='<spagobi:message key = "SBIErrorPage.backButt" />' src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/back.png")%>' alt='<spagobi:message key = "SBIErrorPage.backButt" />' />
			</a>
		</td>
	</tr>
</table>

<!-- br/-->

<div style='width:100%;text-align:center;'>
	<div class="portlet-msg-error">
	    <% 
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

<!--br/-->



<%--div style='width:100%;text-align:center;' >
	<a href='<%= backUrl.toString() %>' class='portlet-menu-item' >
    		<img src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/back.png")%>' alt='Back' />
	</a> 
	<br/>
	<a href='<%= backUrl.toString() %>'>
	<spagobi:message key = "SBIErrorPage.backButt" />
	</a>


</div--%>















