<%@tag language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/tlds/spagobi.tld" prefix="spagobi" %>

<%@attribute name="viewpointsList" required="true" type="java.util.List"%>

<%@tag import="java.util.Iterator"%>
<%@tag import="it.eng.spagobi.analiticalmodel.document.bo.Viewpoint"%>
<%@tag import="it.eng.spago.base.RequestContainer"%>
<%@tag import="it.eng.spagobi.commons.utilities.ChannelUtilities"%>
<%@tag import="it.eng.spagobi.commons.utilities.urls.UrlBuilderFactory"%>
<%@tag import="it.eng.spagobi.commons.utilities.urls.IUrlBuilder"%>
<%@tag import="it.eng.spago.security.IEngUserProfile"%>
<%@tag import="java.util.Map"%>
<%@tag import="java.util.HashMap"%>
<%@tag import="it.eng.spagobi.analiticalmodel.document.service.ExecuteBIObjectModule"%>
<%@tag import="it.eng.spagobi.commons.constants.SpagoBIConstants"%>
<%@tag import="it.eng.spago.navigation.LightNavigationManager"%>
<%@tag import="java.util.Date"%>
<%@tag import="it.eng.spago.configuration.ConfigSingleton"%>
<%@tag import="it.eng.spago.base.SourceBean"%>
<%@tag import="it.eng.spago.util.StringUtils"%>
<%@tag import="it.eng.spagobi.commons.bo.UserProfile"%>

<%
RequestContainer requestContainer = ChannelUtilities.getRequestContainer(request);
IUrlBuilder urlBuilder = UrlBuilderFactory.getUrlBuilder(requestContainer.getChannelType());
IEngUserProfile profile = (IEngUserProfile) requestContainer.getSessionContainer().getPermanentContainer().getAttribute(IEngUserProfile.ENG_USER_PROFILE);
if (viewpointsList == null || viewpointsList.size() == 0) {
	%>
	<div class='portlet-font'><spagobi:message key="SBIDev.docConf.viewPoint.noViewPoints"/></div>
	<%
} else {
    Map deleteVPUrlPars = new HashMap();
    deleteVPUrlPars.put("PAGE", ExecuteBIObjectModule.MODULE_PAGE);
    deleteVPUrlPars.put(SpagoBIConstants.MESSAGEDET, SpagoBIConstants.VIEWPOINT_ERASE);
    deleteVPUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED,"true");
    String deleteVPUrl = urlBuilder.getUrl(request, deleteVPUrlPars);
	%>
	<form method='POST' action='<%= deleteVPUrl %>' id='viewpointsForm' name='viewpointsForm'>
		<table style='width:100%;' align='left'>
			<thead>
				<tr>
				  <td style='vertical-align:middle;' align='left' class='portlet-section-header'>
				      <spagobi:message key="SBIDev.docConf.viewPoint.name"/>
				  </td>
				  <td align='left' class='portlet-section-header'>&nbsp;</td>
				  <td style='vertical-align:middle;' align='left' class='portlet-section-header'>
				      <spagobi:message key="SBIDev.docConf.viewPoint.owner"/>
				  </td>
				  <td align='left' class='portlet-section-header'>&nbsp;</td>
				  <td style='vertical-align:middle;' align='left' class='portlet-section-header'>
				      <spagobi:message key="SBIDev.docConf.viewPoint.description"/>
				  </td>
				  <td align='left' class='portlet-section-header'>&nbsp;</td>
				  <td style='vertical-align:middle;' align='left' class='portlet-section-header'>
				      <spagobi:message key="SBIDev.docConf.viewPoint.scope"/>
				  </td>
				  <td align='left' class='portlet-section-header'>&nbsp;</td>
				  <td style='vertical-align:middle;' align='left' class='portlet-section-header'>
				      <spagobi:message key="SBIDev.docConf.viewPoint.dateCreation"/>
				  </td>
				  <td align='left' class='portlet-section-header'>&nbsp;</td>
				  <td align='left' class='portlet-section-header'>&nbsp;</td>
				  <td align='left' class='portlet-section-header'>
				  
				  	<img 
       						src='<%= urlBuilder.getResourceLink(request, "/img/expertok.gif") %>' 
       						name='selectDeselectAllImg' alt='<spagobi:message key="SBIDev.docConf.viewPoint.selectAll"/>' 
       						title='<spagobi:message key="SBIDev.docConf.viewPoint.selectAll"/>' 
       						onClick="selectDeselectAllViewpoints();" />
						<img 
	       					src='<%= urlBuilder.getResourceLink(request, "/img/analiticalmodel/ico_delete.gif") %>' 
	       					alt='<spagobi:message key="SBIDev.docConf.ListdocDetParam.deleteCaption"/>' 
	       					title='<spagobi:message key="SBIDev.docConf.ListdocDetParam.deleteCaption"/>' 
	       					onClick="deleteViewpoints();" />
	       					
       				<script>
	       				function deleteViewpoints() {
							checks = document.getElementsByName('vpId');
							atLeastOneSelected = false;
							for (var i = 0; i < checks.length; i++) {
								check = checks[i];
								if (check.checked) {
									atLeastOneSelected = true;
									break;
								}
							}
							if (!atLeastOneSelected) {
								alert('<spagobi:message key="SBIDev.docConf.viewPoint.noViewPointsSelected" />');
								return;
							}
							var conf = confirm('<spagobi:message key="ConfirmMessages.DeleteViewpoint" />');
							if (conf) {
								document.getElementById('viewpointsForm').submit();
							}
						}
						
						selectableViewpoints = new Array();
						<%
						if (viewpointsList != null && viewpointsList.size() > 0) {
							Iterator iterVPs =  viewpointsList.iterator();
							while(iterVPs.hasNext()) {
								Viewpoint vp = (Viewpoint) iterVPs.next();
								if (vp.getVpOwner().equals(((UserProfile)profile).getUserId().toString())
										|| profile.isAbleToExecuteAction(SpagoBIConstants.DOCUMENT_MANAGEMENT_ADMIN)) {
									%>
									selectableViewpoints.push(<%= vp.getVpId() %>);
									<%
								}
							}
						}
						%>
						
						selectedViewpoints = new Array();
						
						function selectDeselectAllViewpoints() {
							if (selectedViewpoints.length == 0) {
								selectAllViewpoints();
							} else if (selectedViewpoints.length == selectableViewpoints.length) {
								deselectAllViewpoints();
							} else {
								selectAllViewpoints();
							}
						}
						
						function selectAllViewpoints() {
							checks = document.getElementsByName('vpId');
							for (var i = 0; i < checks.length; i++) {
								check = checks[i];
								if (!check.checked) {
									check.click();
								}
							}
						}
						
						function deselectAllViewpoints() {
							checks = document.getElementsByName('vpId');
							for (var i = 0; i < checks.length; i++) {
								check = checks[i];
								if (check.checked) {
									check.click();
								}
							}
						}
					</script>
				  
				  </td>
				</tr>
			</thead>
			<tboby>
	<%
	Iterator iterVP =  viewpointsList.iterator();
    Viewpoint vp = null;
    String ownerVP = null;				    
    String nameVP = null;
    String descrVP = null;
    String scopeVP = null;
    Date creationDateVP = null;
    String execVPUrl = null;
    String viewVPUrl = null;				    				   
	boolean alternate = false;
	String rowClass = null;
		   
    while (iterVP.hasNext()) {
    	vp = (Viewpoint)iterVP.next();
		rowClass = (alternate) ? "portlet-section-alternate" : "portlet-section-body";
		alternate = !alternate;
		nameVP = vp.getVpName();
		ownerVP = vp.getVpOwner();						
		descrVP = vp.getVpDesc();
		scopeVP = vp.getVpScope();
		creationDateVP = vp.getVpCreationDate();
		
		Map execVPUrlPars = new HashMap();
		execVPUrlPars.put("PAGE", ExecuteBIObjectModule.MODULE_PAGE);
		execVPUrlPars.put(SpagoBIConstants.MESSAGEDET, SpagoBIConstants.VIEWPOINT_EXEC);	
		execVPUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED,"true");						
		execVPUrlPars.put("content", vp.getVpValueParams());
		execVPUrlPars.put("vpId",vp.getVpId());
		execVPUrl = urlBuilder.getUrl(request, execVPUrlPars);
			
	    Map viewVPUrlPars = new HashMap();
	    viewVPUrlPars.put("PAGE", ExecuteBIObjectModule.MODULE_PAGE);
	    viewVPUrlPars.put(SpagoBIConstants.MESSAGEDET, SpagoBIConstants.VIEWPOINT_VIEW);
	    viewVPUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED,"true");
	    viewVPUrlPars.put("vpId",vp.getVpId());
	    viewVPUrl = urlBuilder.getUrl(request, viewVPUrlPars);

        ConfigSingleton conf = ConfigSingleton.getInstance();
	    SourceBean formatSB = (SourceBean) conf.getAttribute("DATA-ACCESS.DATE-FORMAT");
	    String format = (String) formatSB.getAttribute("format");
	    format = format.replaceAll("D", "d");
	    format = format.replaceAll("m", "M");
	    format = format.replaceAll("Y", "y");
	    String date = StringUtils.dateToString(creationDateVP, format);
        %>
				<tr class='portlet-font'>
					<td style='vertical-align:middle;' class='<%= rowClass %>'><%= nameVP %></td>
					<td class='<%= rowClass %>' width='20px'>&nbsp;</td>
					<td style='vertical-align:middle;' class='<%= rowClass %>'><%= ownerVP %></td>
					<td class='<%= rowClass %>' width='20px'>&nbsp;</td>
					<td style='vertical-align:middle;' class='<%= rowClass %>' ><%= descrVP %></td>
					<td class='<%= rowClass %>' width='20px'>&nbsp;</td>
					<td style='vertical-align:middle;' class='<%= rowClass %>' ><%= scopeVP %></td>
					<td class='<%= rowClass %>' width='20px'>&nbsp;</td>
					<td style='vertical-align:middle;' class='<%= rowClass %>' ><%= date %></td>
					<td style='vertical-align:middle;' class='<%= rowClass %>' width='40px'>
				    	<a href="javascript:document.location='<%= viewVPUrl.toString() %>';">
				    		<img 
				    			src='<%= urlBuilder.getResourceLink(request, "/img/notes.jpg") %>' 
				       			name='getViewpoint' alt='<spagobi:message key="SBIDev.docConf.viewPoint.viewButt" />' 
				        		title='<spagobi:message key="SBIDev.docConf.viewPoint.viewButt" />'
				        	/>
				    	</a>
					</td>
					<td style='vertical-align:middle;' class='<%= rowClass %>' width='40px'>
						<a href='<%= execVPUrl %>'>
					 		<img 
					  	   			src='<%= urlBuilder.getResourceLink(request, "/img/exec.gif") %>'
					  	       		name='execSnap'
					  	        	alt='<spagobi:message key="SBIDev.docConf.execBIObjectParams.execButt" />'
					       			title='<spagobi:message key="SBIDev.docConf.execBIObjectParams.execButt" />'
							/>
						</a>
					</td>
					<td style='vertical-align:middle;text-align:center;' class='<%= rowClass %>' width='40px'>
					<%
	                if (ownerVP.equals(((UserProfile)profile).getUserId().toString()) || profile.getFunctionalities().contains(SpagoBIConstants.DOCUMENT_MANAGEMENT_ADMIN)) {
	                	%>
							<input type="checkbox" name="vpId" id="vpId"
				   					value="<%= vp.getVpId() %>" 
				   					onClick="if (this.checked) {selectedViewpoints.push(this.value);} else {selectedViewpoints.removeFirst(this.value);}"/>
	                 	<%
	                } else {
	           			%>
           					&nbsp;
           				<%
	                }
					%>
				</tr>
     	<%
    }
    %>
    		</tboby>
		</table>
	</form>
    <%
}
%>