<%@ include file="/jsp/portlet_base.jsp"%>

<%@ page import="it.eng.spagobi.constants.SpagoBIConstants,
         		it.eng.spagobi.events.EventsManager,
         		it.eng.spagobi.bo.EventLog,
         		it.eng.spago.base.SourceBean,
         		java.util.List,
         		javax.portlet.PortletURL,
         		it.eng.spago.navigation.LightNavigationManager,
         		it.eng.spagobi.bo.Subreport,
         		it.eng.spagobi.bo.BIObject,
         		it.eng.spagobi.utilities.PortletUtilities,
         		it.eng.spagobi.utilities.GeneralUtilities,
         		it.eng.spagobi.constants.ObjectsTreeConstants"
%>

<%
	SourceBean moduleResponse = (SourceBean) aServiceResponse.getAttribute("DetailEventLogModule"); 
	EventLog event = (EventLog) moduleResponse.getAttribute("firedEvent");
	BIObject biObject = (BIObject) moduleResponse.getAttribute("biobject");
	// if the process has ended the 'startEventIdStr' and 'result' variables are not null
	String startEventIdStr = (String) moduleResponse.getAttribute("startEventId");
	String result = (String) moduleResponse.getAttribute("operation-result");
	
	PortletURL backUrl = renderResponse.createActionURL();
   	backUrl.setParameter("PAGE", "EVENTS_MONITOR_PAGE");
   	backUrl.setParameter("REFRESH", "TRUE");
   	backUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_BACK_TO, "1");
   	
	PortletURL backToListUrl = renderResponse.createActionURL();
	backToListUrl.setParameter("PAGE", "EVENTS_MONITOR_PAGE");
	backToListUrl.setParameter("REFRESH", "TRUE");
	backToListUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_RESET, "true");
%>

<table class='header-table-portlet-section'>		
	<tr class='header-row-portlet-section'>
		<td class='header-title-column-portlet-section' 
		    style='vertical-align:middle;padding-left:5px;'>
			<spagobi:message key = "sbievents.detail.title" />
		</td>
		<td class='header-empty-column-portlet-section'>&nbsp;</td>
		<td class='header-button-column-portlet-section'>
			<a href='<%= backToListUrl.toString() %>'> 
      			<img class='header-button-image-portlet-section' title='<spagobi:message key = "sbievents.detail.backToListButton" />' src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/listView.png")%>' alt='<spagobi:message key = "sbievents.detail.backToListButton" />' />
			</a>
		</td>
		<td class='header-button-column-portlet-section'>
			<a href='<%= backUrl.toString() %>'> 
      			<img class='header-button-image-portlet-section' title='<spagobi:message key = "sbievents.detail.backButton" />' src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/back.png")%>' alt='<spagobi:message key = "sbievents.detail.backButton" />' />
			</a>
		</td>
	</tr>
</table>

<div class="div_background_no_img">
	<div style="width:70%;" class="div_detail_area_forms">
		<table style="margin:10px;">
			<tr>
				<td class='portlet-form-field-label'><spagobi:message key = "sbievents.detail.id" /></td>
				<td class='portlet-section-subheader'><p style='margin:5px'><%=event.getId()%></p></td>
			</tr>
			<tr>
				<td class='portlet-form-field-label'><spagobi:message key = "sbievents.detail.date" /></td>
				<td class='portlet-section-subheader'><p style='margin:5px'><%=event.getDate().toString()%></p></td>
			</tr>
			<tr>
				<td class='portlet-form-field-label'><spagobi:message key = "sbievents.detail.user" /></td>
				<td class='portlet-section-subheader'><p style='margin:5px'><%=event.getUser()%></p></td>
			</tr>
			<tr>
				<td class='portlet-form-field-label'><spagobi:message key = "weka.execution.documentDetails" /></td>
				<td class='portlet-section-subheader'>
					<p style='margin:5px'>
					<%=biObject.getLabel() + ": " + biObject.getName() + " [" + biObject.getDescription() + "]"%>
					</p>
				</td>
			</tr>
			<tr>
				<td class='portlet-form-field-label'><spagobi:message key = "sbievents.detail.description" /></td>
				<%
				String description = event.getDesc();
				if (description != null) {
					description = description.replaceAll("&gt;", ">");
					description = description.replaceAll("&lt;", "<");
					description = description.replaceAll("&quot;", "\"");
					description = GeneralUtilities.replaceInternationalizedMessages(description);
				}

				%>
				<td class='portlet-section-subheader' style='text-align:left'>
					<p style='margin:5px'><%=(description == null ? "" : description)%>
					<%
					if (startEventIdStr != null) {
						PortletURL startEventDetailUrl = renderResponse.createActionURL();
						startEventDetailUrl.setParameter("PAGE", "DetailEventLogPage");
						startEventDetailUrl.setParameter("MESSAGEDET", "DETAIL_SELECT");
						startEventDetailUrl.setParameter("ID", startEventIdStr);
						%>
						<spagobi:message key = "weka.execution.startEventId" /> <a href='<%= startEventDetailUrl.toString() %>'><%=startEventIdStr%></a>.						
						<%
					}
					%>
					</p>
				</td>
			</tr>
			<tr>
				<td class='portlet-form-field-label'><spagobi:message key = "weka.execution.relatedDocuments" /></td>
				<td class='portlet-section-subheader'>
					<p style='margin:5px'>
					<%
					List linkedBIObjects = (List) moduleResponse.getAttribute("linkedBIObjects");
					if (linkedBIObjects.size() == 0) {
						%>
						<spagobi:message key = "weka.execution.noRelatedDocuments" />
						<%
					} else {
						for (int i = 0; i < linkedBIObjects.size(); i++) {
							BIObject linkedObject = (BIObject) linkedBIObjects.get(i);
							if (startEventIdStr != null && result != null && result.equalsIgnoreCase("success")) {
								// if it is an end process event, shows the execution links to correlated documents
								PortletURL linkedObjectExecutionUrl = renderResponse.createActionURL();
								linkedObjectExecutionUrl.setParameter("PAGE", "ExecuteBIObjectPage"); 
								linkedObjectExecutionUrl.setParameter("MESSAGEDET", "EXEC_PHASE_CREATE_PAGE");  
								linkedObjectExecutionUrl.setParameter("OBJECT_ID", linkedObject.getId().toString());
								linkedObjectExecutionUrl.setParameter("ACTOR", "USER_ACTOR");
				   				%>
				   				<a href='<%=linkedObjectExecutionUrl.toString() %>'><%=linkedObject.getLabel() + ": " + linkedObject.getName() + " [" + linkedObject.getDescription() + "]"%></a>
				   				<br/>
				   				<%
							} else {
								// if it's a start process event or the process did not success, shows the correlated documents with no links for execution
								%>
								<%=linkedObject.getLabel() + ": " + linkedObject.getName() + " [" + linkedObject.getDescription() + "]"%>
								<br/>
								<%
							}
						}
					}
					%>
					</p>
				</td>
			</tr>
		</table>
	</div>
</div>

<%--

	
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
--%>
