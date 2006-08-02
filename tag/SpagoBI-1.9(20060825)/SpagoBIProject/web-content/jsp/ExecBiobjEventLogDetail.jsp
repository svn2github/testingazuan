<%@ include file="/jsp/portlet_base.jsp"%>

<%@ page import="it.eng.spagobi.constants.SpagoBIConstants,
         		it.eng.spagobi.events.EventsManager,
         		it.eng.spagobi.bo.EventLog,
         		it.eng.spago.base.SourceBean,
         		java.util.List,
         		javax.portlet.PortletURL,
         		it.eng.spago.navigation.LightNavigationManager,
         		it.eng.spagobi.constants.SpagoBIConstants,
         		it.eng.spagobi.bo.Subreport,
         		it.eng.spagobi.bo.BIObject,
         		it.eng.spagobi.utilities.PortletUtilities,
         		it.eng.spagobi.constants.ObjectsTreeConstants"
%>

<%
	SourceBean moduleResponse = (SourceBean) aServiceResponse.getAttribute("DetailEventLogModule"); 
		// build the back link
   		PortletURL backUrl = renderResponse.createActionURL();
		backUrl.setParameter("PAGE", "DetailEventLogPage");
		backUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_BACK_TO, "1");
	
		EventLog firedEvent = (EventLog)moduleResponse.getAttribute("firedEvent");
		BIObject biObject = (BIObject)moduleResponse.getAttribute("biobject");
%>
	<table class='header-table-portlet-section'>
	<tr class='header-row-portlet-section'>
		<td class='header-title-column-portlet-section' style='vertical-align:middle;padding-left:5px;'>
			Event Log Details
		</td>
		<td class='header-empty-column-portlet-section'>&nbsp;</td>
		<td class='header-button-column-portlet-section'>
			<a style="text-decoration:none;" href='<%=backUrl.toString()%>'> 
				<img width="20px" height="20px"
					src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/back.png")%>' 
					name='refresh' 
					alt='<%=PortletUtilities.getMessage("SBIExecution.refresh", "messages")%>' 
					title='<%=PortletUtilities.getMessage("SBIExecution.refresh", "messages")%>' /> 
			</a>
		</td>
	</tr>
	</table>
	
	<P>Document executed by engine <I><%=biObject.getEngine().getName()%></I>
	<P>Execution termination was notifyed at <I><%= firedEvent.getDate().toString()%></I> with the following exit status:  <B><%=moduleResponse.getAttribute("status") %></B> 
	<P> 
	<P><H3>Document Infos ...</H3>
	<P><B>name:</B> <%=biObject.getName() %> 
 	<P><B>description:</B> <%=biObject.getDescription() %> 
  	<P><B>Type</B>: <%=biObject.getBiObjectTypeCode() %> 
 	<P><B>State:</B> <%=biObject.getStateCode()%>
 	<P><H3>Related Documents ...</H3>
<%		
		List linkedBIObjects = (List) moduleResponse.getAttribute("linkedBIObjects");
		if(linkedBIObjects.size() == 0) {			
%>
	<P><H3><I>No related documents !!!</I></H3>
<%
		} else {
			
			for(int i = 0; i < linkedBIObjects.size(); i++) {
				biObject = (BIObject)linkedBIObjects.get(i);
				PortletURL formUrl = renderResponse.createActionURL();
   				formUrl.setParameter("PAGE", "ExecuteBIObjectPage"); 
   				formUrl.setParameter("MESSAGEDET", "EXEC_PHASE_CREATE_PAGE");  
   				formUrl.setParameter("OBJECT_ID", biObject.getId().toString());
   				formUrl.setParameter("ACTOR", "USER_ACTOR");
				
%>
	<P> <%=biObject.getName() %> - <%=biObject.getDescription() %> - <%=biObject.getBiObjectTypeCode() %> - <%=biObject.getStateCode()%>
	- <a href='<%=formUrl.toString() %>'>execute</a>
<%		
			}
		}
%>
