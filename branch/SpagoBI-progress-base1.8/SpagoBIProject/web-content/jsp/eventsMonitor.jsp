<%@ include file="/jsp/portlet_base.jsp"%>

<%@ page import="it.eng.spagobi.constants.SpagoBIConstants,
         		it.eng.spagobi.events.EventsManager,
         		it.eng.spago.base.SourceBean,
         		java.util.List"
%>

<H1>Event Monitor</H1>
<P>
<%
	SourceBean moduleResponse = (SourceBean) aServiceResponse.getAttribute("PortletEventsMonitorModule"); 
	if(moduleResponse == null) {
		System.err.println(aServiceResponse.toXML(false));
%>
	<P><H3>ERRORE !!!</H3>
	<P> <%= aServiceResponse.toXML(false) %>
<%
	} else {
		List firedEventsList = (List) moduleResponse.getAttribute("firedEventsList");
		if(firedEventsList.size() == 0) {
%>
	<P><H3><I>Events log is empty !!!</I></H3>H3>
<%
		} else {
			for(int i = 0; i < firedEventsList.size(); i++) {
				EventsManager.FiredEvent firedEvent = (EventsManager.FiredEvent)firedEventsList.get(i);
%>
	<P> <%=firedEvent.id%> - <%=firedEvent.user%> - <%=firedEvent.desc%>
<%		
			}
		}
	}
%>