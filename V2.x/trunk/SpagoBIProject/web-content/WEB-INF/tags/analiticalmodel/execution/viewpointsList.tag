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
	%>
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
				  <td align='left' class='portlet-section-header'>&nbsp;</td>
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
    String deleteVPUrl = null;
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
			
	    Map deleteVPUrlPars = new HashMap();
	    deleteVPUrlPars.put("PAGE", ExecuteBIObjectModule.MODULE_PAGE);
	    deleteVPUrlPars.put(SpagoBIConstants.MESSAGEDET, SpagoBIConstants.VIEWPOINT_ERASE);
	    deleteVPUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED,"true");
	    deleteVPUrlPars.put("vpId",vp.getVpId());
	    deleteVPUrl = urlBuilder.getUrl(request, deleteVPUrlPars);

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
					<%
	                //if (ownerVP.equals(profile.getUserUniqueIdentifier().toString())) {
	                if (ownerVP.equals(((UserProfile)profile).getUserId().toString()) || profile.getFunctionalities().contains(SpagoBIConstants.DOCUMENT_MANAGEMENT_ADMIN)) {
	                	%>
	                 	<a href="javascript:var conf = confirm('<spagobi:message key="ConfirmMessages.DeleteViewpoint"/>'); 
	                 											if (conf) {document.location='<%= deleteVPUrl.toString() %>';}">
	                 		<img 
	                 			src='<%= urlBuilder.getResourceLink(request, "/img/erase.gif") %>' 
	                 			name='deleteViewpoint' alt='<spagobi:message key="SBIDev.docConf.ListdocDetParam.deleteCaption" />' 
	                 			title='<spagobi:message key="SBIDev.docConf.ListdocDetParam.deleteCaption" />' 
	                 		/>
	                 	</a>
	                 	<%
	                }
					%>
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
				</tr>
     	<%
    }
    %>
    </tboby>
	</table>
    <%
}
%>