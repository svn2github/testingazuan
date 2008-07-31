<%@tag language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/tlds/spagobi.tld" prefix="spagobi" %>

<%@attribute name="subobjectsList" required="true" type="java.util.List"%>

<%@tag import="java.util.Iterator"%>
<%@tag import="it.eng.spagobi.analiticalmodel.document.bo.SubObject"%>
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
if (subobjectsList == null || subobjectsList.size() == 0) {
	%>
	
<div class='portlet-font'><spagobi:message key="SBIDev.docConf.subBIObject.nosubobjects"/></div>
	<%
} else {
	%>
		<table style='width:100%;' align='left'>
			<thead>
				<tr>
					<td style='vertical-align:middle;' align='left' class='portlet-section-header'>
					  	<spagobi:message key="SBIDev.docConf.subBIObject.name"/>
					</td>
					<td align='left' class='portlet-section-header'>&nbsp;</td>
					<td style='vertical-align:middle;' align='left' class='portlet-section-header'>
				   		<spagobi:message key="SBIDev.docConf.subBIObject.owner"/>
					</td>
					<td align='left' class='portlet-section-header'>&nbsp;</td>
					<td style='vertical-align:middle;' align='left' class='portlet-section-header'>
						<spagobi:message key="SBIDev.docConf.subBIObject.description"/>
					</td>
					<td align='left' class='portlet-section-header'>&nbsp;</td>
					<td style='vertical-align:middle;' align='left' class='portlet-section-header'>
						<spagobi:message key="SBIDev.docConf.subBIObject.creationDate"/>
					</td>
					<td align='left' class='portlet-section-header'>&nbsp;</td>
					<td style='vertical-align:middle;' align='left' class='portlet-section-header'>
						<spagobi:message key="SBIDev.docConf.subBIObject.lastModificationDate"/>
					</td>
					<td align='left' class='portlet-section-header'>&nbsp;</td>
					<td style='vertical-align:middle;' align='left' class='portlet-section-header'>
						<spagobi:message key="SBIDev.docConf.subBIObject.visibility"/>
					</td>
					<td align='left' class='portlet-section-header' colspan='3' >&nbsp;</td>
				</tr>
			</thead>
			<tboby>
	<%
	SubObject subObj = null;
	Integer idSub = null;
	String nameSub = null;
	String descr = null;
	String visib = null;
	String delete = null;
	String owner = null;
	String creationDate = null;
	String lastModificationDate = null;
	String execSubObjUrl = null;
	String deleteSubObjUrl = null;
	boolean alternate = false;
	String rowClass = null;
	
	Iterator iterSubs =  subobjectsList.iterator();
	while(iterSubs.hasNext()) {
		subObj = (SubObject)iterSubs.next();
		rowClass = (alternate) ? "portlet-section-alternate" : "portlet-section-body";
		alternate = !alternate;
		idSub = subObj.getId();
		nameSub = subObj.getName();
        descr = subObj.getDescription();
        owner = subObj.getOwner();
        creationDate = subObj.getCreationDate().toString();
        lastModificationDate = subObj.getLastChangeDate().toString();
                    
        visib = "Private";
        if (subObj.getIsPublic().booleanValue()) {
        	visib = "Public";
        } 
        if (owner.equals(profile.getUserUniqueIdentifier().toString())) {
        	delete = "delete";
        }
        Map execSubObjUrlPars = new HashMap();
        execSubObjUrlPars.put("PAGE", ExecuteBIObjectModule.MODULE_PAGE );
        execSubObjUrlPars.put(SpagoBIConstants.MESSAGEDET, "EXEC_SUBOBJECT");
        execSubObjUrlPars.put(SpagoBIConstants.SUBOBJECT_ID, idSub);
        execSubObjUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED,"true");
	    execSubObjUrl = urlBuilder.getUrl(request, execSubObjUrlPars);
        
	    Map deleteSubObjUrlPars = new HashMap();
	    deleteSubObjUrlPars.put("PAGE", ExecuteBIObjectModule.MODULE_PAGE);
	    deleteSubObjUrlPars.put(SpagoBIConstants.MESSAGEDET, "DELETE_SUBOBJECT");
	    deleteSubObjUrlPars.put(SpagoBIConstants.SUBOBJECT_ID, idSub);
	    deleteSubObjUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED,"true");
	    deleteSubObjUrl = urlBuilder.getUrl(request, deleteSubObjUrlPars);
        %>
				<tr class='portlet-font'>
       		    	<td style='vertical-align:middle;' class='<%= rowClass %>'>
       		    		<%= nameSub %>
           			</td>
           			<td class='<%= rowClass %>' width='20px'>&nbsp;</td> 
           			<td style='vertical-align:middle;' class='<%= rowClass %>' ><%= owner %></td>
           			<td class='<%= rowClass %>' width='20px'>&nbsp;</td> 
           			<td style='vertical-align:middle;' class='<%= rowClass %>' ><%= descr %></td>
           			<td class='<%= rowClass %>' width='20px'>&nbsp;</td> 
           			<td style='vertical-align:middle;' class='<%= rowClass %>' ><%= creationDate %></td>
           			<td class='<%= rowClass %>' width='20px'>&nbsp;</td> 
           			<td style='vertical-align:middle;' class='<%= rowClass %>' ><%= lastModificationDate %></td>
           			<td class='<%= rowClass %>' width='20px'>&nbsp;</td> 
           			<td style='vertical-align:middle;' class='<%= rowClass %>' ><%= visib %></td>
           			<td class='<%= rowClass %>' width='20px'>&nbsp;</td>
           			<%
           		if (owner.equals(profile.getUserUniqueIdentifier().toString())) {
           			%>
                   		<td style='vertical-align:middle;' class='<%= rowClass %>' width='40px'>
               				<a href="javascript:var conf = confirm('<spagobi:message key="ConfirmMessages.DeleteSubObject" />'); 
               									if (conf) {document.location='<%= deleteSubObjUrl.toString() %>';}">
               					<img 
	  	   							src='<%= urlBuilder.getResourceLink(request, "/img/erase.gif") %>' 
	  	   							name='deleteSub' 
	  	            				alt='<spagobi:message key="SBIDev.docConf.ListdocDetParam.deleteCaption" />' 
	                				title='<spagobi:message key="SBIDev.docConf.ListdocDetParam.deleteCaption" />' />
               				</a>
               			</td>
               		<%
           		} else {
           			%>
           				<td style='vertical-align:middle;' class='<%= rowClass %>' width='40px'>
           					&nbsp;
           				</td>
           			<%
           		}
           			%>
               		<td style='vertical-align:middle;' class='<%= rowClass %>' width='40px'>
           				<a href='<%= execSubObjUrl %>'>
           					<img 
  	   							src='<%= urlBuilder.getResourceLink(request, "/img/exec.gif") %>' 
  	   							name='execSub' 
  	            				alt='<spagobi:message key="SBIDev.docConf.execBIObjectParams.execButt" />' 
                				title='<spagobi:message key="SBIDev.docConf.execBIObjectParams.execButt" />' />
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