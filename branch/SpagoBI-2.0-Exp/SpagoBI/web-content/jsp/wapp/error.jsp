<%@ include file="/jsp/portlet_base.jsp"%>

<%@page import="java.util.Map"%>
<%@page import="it.eng.spago.error.EMFErrorHandler"%>
<%@page import="java.util.Collection"%>
<%@page import="java.util.Iterator"%>
<%@page import="it.eng.spago.error.EMFAbstractError"%>

<%
    // recover error handler and error collection 
    EMFErrorHandler errorHandler = aResponseContainer.getErrorHandler();  
	Collection errors = errorHandler.getErrors();
	Iterator iter = errors.iterator();  
%>


<table class='header-table-portlet-section'>		
	<tr class='header-row-portlet-section'>
		<td class='header-title-column-portlet-section'>
			<spagobi:message key = "SBIErrorPage.title" />
		</td>
		<%--
		<td class='header-empty-column-portlet-section'>&nbsp;</td>
		<td class='header-button-column-portlet-section'>
			<a href='javascript:history.back()'> 
      			<img class='header-button-image-portlet-section' 
      			     title='<spagobi:message key = "SBIErrorPage.backButt" />' 
      			     src='<%=urlBuilder.getResourceLink(request, "/img/back.png")%>' 
      			     alt='<spagobi:message key = "SBIErrorPage.backButt" />' />
			</a>
		</td>
		--%>
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
	    %>
		<%= description %>
		<br/>
		<% } %>
	</div>
</div>		















