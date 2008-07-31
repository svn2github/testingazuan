<%@tag language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/tlds/spagobi.tld" prefix="spagobi" %>

<%@attribute name="snapshotsList" required="true" type="java.util.List"%>

<%@tag import="java.util.Iterator"%>
<%@tag import="it.eng.spagobi.analiticalmodel.document.bo.Snapshot"%>

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

<%
RequestContainer requestContainer = ChannelUtilities.getRequestContainer(request);
IUrlBuilder urlBuilder = UrlBuilderFactory.getUrlBuilder(requestContainer.getChannelType());
IEngUserProfile profile = (IEngUserProfile) requestContainer.getSessionContainer().getPermanentContainer().getAttribute(IEngUserProfile.ENG_USER_PROFILE);
if (snapshotsList == null || snapshotsList.size() == 0) {
	%>
	<div class='portlet-font'><spagobi:message key="SBIDev.docConf.snapshots.nosnapshots"/></div>
	<%
} else {
	%>
	<table style='width:100%;' align='left'>
		<thead>
			<tr>
				<td style='vertical-align:middle;' align='left' class='portlet-section-header'>
					<spagobi:message key="SBIDev.docConf.snapshots.name"/>
				</td>
				<td align='left' class='portlet-section-header'>&nbsp;</td>
				<td style='vertical-align:middle;' align='left' class='portlet-section-header'>
					<spagobi:message key="SBIDev.docConf.snapshots.description"/>
				</td>
		   		<td align='left' class='portlet-section-header'>&nbsp;</td>
		   		<td style='vertical-align:middle;' align='left' class='portlet-section-header'>
		   			<spagobi:message key="SBIDev.docConf.snapshots.dateCreation"/>
		   		</td>
		   		<td align='left' class='portlet-section-header'>&nbsp;</td>
		   		<td align='left' class='portlet-section-header'>&nbsp;</td>
		   		<td align='left' class='portlet-section-header'>&nbsp;</td>
	 		</tr>
		</thead>
		<tboby>
	<%
	Iterator iterSnap =  snapshotsList.iterator();
    Snapshot snap = null;
    String nameSnap = null;
    String descrSnap = null;
    Date creationDate = null;
    String execSnapUrl = null;
    String deleteSnapUrl = null;
	boolean alternate = false;
	String rowClass = null;
	   
    while(iterSnap.hasNext()) {
    	snap = (Snapshot)iterSnap.next();
		rowClass = (alternate) ? "portlet-section-alternate" : "portlet-section-body";
		alternate = !alternate;
		nameSnap = snap.getName();
		descrSnap = snap.getDescription();
		creationDate = snap.getDateCreation();
		
		Map execSnapUrlPars = new HashMap();
		execSnapUrlPars.put("PAGE", ExecuteBIObjectModule.MODULE_PAGE);
	    execSnapUrlPars.put(SpagoBIConstants.MESSAGEDET, SpagoBIConstants.EXEC_SNAPSHOT_MESSAGE);
	    execSnapUrlPars.put(SpagoBIConstants.SNAPSHOT_ID, snap.getId());
	    execSnapUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED,"true");
	    execSnapUrl = urlBuilder.getUrl(request, execSnapUrlPars);
		
	    Map deleteSnapUrlPars = new HashMap();
	    deleteSnapUrlPars.put("PAGE", ExecuteBIObjectModule.MODULE_PAGE);
	    deleteSnapUrlPars.put(SpagoBIConstants.MESSAGEDET, SpagoBIConstants.ERASE_SNAPSHOT_MESSAGE);
	    deleteSnapUrlPars.put(SpagoBIConstants.SNAPSHOT_ID, snap.getId());
	    deleteSnapUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED,"true");
	    deleteSnapUrl = urlBuilder.getUrl(request, deleteSnapUrlPars);
        %>
	    <tr class='portlet-font'>
	    	<td style='vertical-align:middle;' class='<%= rowClass %>'><%= nameSnap %></td>
	    	<td class='<%= rowClass %>' width='20px'>&nbsp;</td>
	    	<td style='vertical-align:middle;' class='<%= rowClass %>' ><%= descrSnap %></td>
	    	<td class='<%= rowClass %>' width='20px'>&nbsp;</td>
	    	<td style='vertical-align:middle;' class='<%= rowClass %>' ><%= creationDate.toString() %></td>
	    	<td class='<%= rowClass %>' width='20px'>&nbsp;</td>
	    	<td style='vertical-align:middle;' class='<%= rowClass %>' width='40px'>
	    <%
        if (!profile.isAbleToExecuteAction(SpagoBIConstants.DOCUMENT_MANAGEMENT_ADMIN)) {
        	%>
        			&nbsp;
        	<%
        } else {
        	%>
        			<a href="javascript:var conf = confirm('<spagobi:message key="ConfirmMessages.DeleteSnapshot" />'); 
        								if (conf) {document.location='<%= deleteSnapUrl.toString() %>';}">
        				<img 
        					src='<%= urlBuilder.getResourceLink(request, "/img/erase.gif") %>' 
        					name='deleteSnapshot' alt='<spagobi:message key="SBIDev.docConf.ListdocDetParam.deleteCaption"/>' 
        					title='<spagobi:message key="SBIDev.docConf.ListdocDetParam.deleteCaption"/>' />
        			</a>
        	<%
        }
	    %>
        	</td>
        	<td style='vertical-align:middle;' class='<%= rowClass %>' width='40px'>
        		<a href='<%= execSnapUrl %>'>
        			<img 
						src='<%= urlBuilder.getResourceLink(request, "/img/exec.gif") %>' 
						name='execSnap' 
						alt='<spagobi:message key="SBIDev.docConf.execBIObjectParams.execButt"/>' 
						title='<spagobi:message key="SBIDev.docConf.execBIObjectParams.execButt"/>' />
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