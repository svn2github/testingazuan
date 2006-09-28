<!--
SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005 Engineering Ingegneria Informatica S.p.A.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
-->


<%@ include file="/jsp/portlet_base.jsp"%>

<%@ page import="javax.portlet.PortletURL,
				it.eng.spago.navigation.LightNavigationManager,
				it.eng.spagobi.booklets.constants.BookletsConstants,
				java.util.List,
				java.util.Iterator,
				it.eng.spagobi.bo.Role,
				it.eng.spagobi.booklets.bo.ConfiguredBIDocument,
				it.eng.spagobi.constants.SpagoBIConstants,
				it.eng.spagobi.booklets.bo.WorkflowConfiguration" %>

<%@page import="it.eng.spagobi.utilities.PortletUtilities"%>
<%@page import="it.eng.spagobi.booklets.bo.PresentationVersion"%>
<%@page import="it.eng.spagobi.booklets.dao.BookletsCmsDaoImpl"%>

<%
   SourceBean moduleResponse = (SourceBean)aServiceResponse.getAttribute("ExecuteBIObjectModule"); 
   if(moduleResponse==null){
	   moduleResponse = (SourceBean)aServiceResponse.getAttribute(BookletsConstants.BOOKLET_COLLABORATION_MODULE); 
   }
   List presVersions = (List)moduleResponse.getAttribute(BookletsConstants.BOOKLET_PRESENTATION_VERSIONS);
   String pathConfBook = (String)moduleResponse.getAttribute(BookletsConstants.PATH_BOOKLET_CONF);
   
   PortletURL backUrl = renderResponse.createActionURL();
   backUrl.setParameter("LIGHT_NAVIGATOR_BACK_TO", "1");
   
   PortletURL runCollaborationUrl = renderResponse.createActionURL();
   runCollaborationUrl.setParameter("PAGE", BookletsConstants.BOOKLET_COLLABORATION_PAGE);
   runCollaborationUrl.setParameter("OPERATION", BookletsConstants.OPERATION_RUN_NEW_COLLABORATION);
   runCollaborationUrl.setParameter(BookletsConstants.PATH_BOOKLET_CONF, pathConfBook);
   runCollaborationUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
   
   PortletURL eraseVersionUrl = renderResponse.createActionURL();
   eraseVersionUrl.setParameter("PAGE", BookletsConstants.BOOKLET_COLLABORATION_PAGE);
   eraseVersionUrl.setParameter("OPERATION", BookletsConstants.OPERATION_DELETE_PRESENTATION_VERSION);
   eraseVersionUrl.setParameter(BookletsConstants.PATH_BOOKLET_CONF, pathConfBook);
   eraseVersionUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
   
   PortletURL publishUrl = renderResponse.createActionURL();
   publishUrl.setParameter("PAGE", BookletsConstants.BOOKLET_COLLABORATION_PAGE);
   publishUrl.setParameter("OPERATION", BookletsConstants.OPERATION_PREPARE_PUBLISH_PRESENTATION_PAGE);
   publishUrl.setParameter(BookletsConstants.PATH_BOOKLET_CONF, pathConfBook);
   
   String contextAddress = GeneralUtilities.getSpagoBiContextAddress();
   String downloadVersionUrl = BookletServiceUtils.getBookletServiceUrl() + "?" +
   							   BookletsConstants.BOOKLET_SERVICE_TASK + "=" + 
   							   BookletsConstants.BOOKLET_SERVICE_TASK_DOWN_PRESENTATION_VERSION + "&" +
		                       BookletsConstants.PATH_BOOKLET_CONF+"="+pathConfBook;
   
%>




	<!-- ********************* TITOLO **************************  -->


	
	<%@page import="it.eng.spagobi.utilities.GeneralUtilities"%>
<%@page import="it.eng.spagobi.booklets.utils.BookletServiceUtils"%>
<table class='header-table-portlet-section'>
		<tr class='header-row-portlet-section'>
			<td class='header-title-column-portlet-section' style='vertical-align:middle;padding-left:5px;'>
				<spagobi:message key="book.Execution" bundle="component_booklets_messages" />
			</td>
			<td class='header-empty-column-portlet-section'>&nbsp;</td>
			<td class='header-button-column-portlet-section'>
				<a href='<%= backUrl.toString() %>'> 
	      			<img class='header-button-image-portlet-section' 
	      				 title='<spagobi:message key = "book.back" bundle="component_booklets_messages" />' 
	      				 src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/components/booklets/img/back.png")%>' 
	      				 alt='<spagobi:message key = "book.back"  bundle="component_booklets_messages"/>' />
				</a>
			</td>
		</tr>
	</table>
	
	
	
	<br/>
	<br/>
	
	
	<div style="float:left;" class='portlet-form-field-label'>
		&nbsp;&nbsp;&nbsp;<spagobi:message key = "book.StartBookletDiscussion"  bundle="component_booklets_messages"/> ...
	</div>
	<div style="float:left;padding-left:15px;">
		<a href='<%= runCollaborationUrl.toString() %>'> 
	    <img class='header-button-image-portlet-section' 
	    	 title='<spagobi:message key = "book.StartBookletDiscussion" bundle="component_booklets_messages" />' 
	    	 src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/components/booklets/img/exec.jpg")%>' 
	    	 alt='<spagobi:message key = "book.StartBookletDiscussion"  bundle="component_booklets_messages"/>' />
		</a>
	</div>
	<div style="clear:left;">&nbsp;</div>

	
	
	<div style='width:100%;visibility:visible;' 
		 class='UITabs' 
		 id='presapprov' 
		 name='presapprov'>
		<div class="first-tab-level" style="background-color:#f8f8f8">
			<div style="overflow: hidden; width:100%">
				<div class='tab'>
					<spagobi:message key = "book.ApprovedPresentations"  bundle="component_booklets_messages"/>
				</div>
			</div>
		</div>
	</div>
	
	
	<table style='width:100%;'> 
	     <tr>
	       <td style='vertical-align:middle;' align="left" class="portlet-section-header">
	           <spagobi:message key = "book.PresentationName"  bundle="component_booklets_messages"/>
	       </td>
	       <td align="left" class="portlet-section-header">&nbsp;</td>
	       <td style='vertical-align:middle;' align="left" class="portlet-section-header">
	          <spagobi:message key = "book.CreationDate"  bundle="component_booklets_messages"/>
	       </td>
	       <td align="left" class="portlet-section-header">&nbsp;</td>
	       <td align="center" style='vertical-align:middle;' align="left" class="portlet-section-header">
	          <spagobi:message key = "book.VersionName"  bundle="component_booklets_messages"/>
	       </td>
	       <td align="left" class="portlet-section-header">&nbsp;</td>
	       <td align="center" style='vertical-align:middle;' align="center" class="portlet-section-header">
	          <spagobi:message key = "book.Approved"  bundle="component_booklets_messages"/>
	       </td>
	       <td align="left" class="portlet-section-header" colspan='4' >&nbsp;</td>
	     </tr>
	     <tr> 
		 <% Iterator iterPresVersions =  presVersions.iterator();
     		boolean alternate = false;
			String rowClass = "";
			while(iterPresVersions.hasNext()) {
    	    	PresentationVersion presVer = (PresentationVersion)iterPresVersions.next();
				rowClass = (alternate) ? "portlet-section-alternate" : "portlet-section-body";
				alternate = !alternate;
		 %>
         <tr class='portlet-font'>
         	<td style='vertical-align:middle;' class='<%= rowClass %>'>
           		<%= presVer.getPresentationName() %>
            </td>
            <td class='<%= rowClass %>' width="20px">&nbsp;</td> 
            <td style='vertical-align:middle;' class='<%= rowClass %>' >
            	<%= presVer.getCreationDate() %>
            </td>
            <td class='<%= rowClass %>' width="20px">&nbsp;</td> 
            <td align="center" style='vertical-align:middle;' class='<%= rowClass %>' >
            	<%= presVer.getVersionName() %>
            </td>
            <td class='<%= rowClass %>' width="20px">&nbsp;</td>
            <td align="center" style='vertical-align:middle;' class='<%= rowClass %>' >
            	<% out.print(presVer.isApproved()); %>
            </td> 
            <td class='<%= rowClass %>' width="20px">&nbsp;</td> 
            <td style='vertical-align:middle;' class='<%= rowClass %>' width="40px">
            	<% if(!presVer.isCurrentVersion()) { %>
                <a href='<%=eraseVersionUrl.toString()+"&"+BookletsConstants.BOOKLET_PRESENTATION_VERSION_NAME+"="+presVer.getVersionName()%>'> 
			    <img title='<spagobi:message key = "book.erase" bundle="component_booklets_messages" />' 
			    	 src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/components/booklets/img/erase.gif")%>' 
			    	 alt='<spagobi:message key = "book.erase"  bundle="component_booklets_messages"/>' />
				</a>
				<% } else { out.print("&nbsp"); } %>        		
            </td>
            <td style='vertical-align:middle;' class='<%= rowClass %>' width="40px">
                <a href='<%=downloadVersionUrl.toString() + "&" + BookletsConstants.BOOKLET_PRESENTATION_VERSION_NAME + "=" + presVer.getVersionName()%>'> 
			    <img title='<spagobi:message key = "book.download" bundle="component_booklets_messages" />' 
			    	 src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/components/booklets/img/download16.gif")%>' 
			    	 alt='<spagobi:message key = "book.download"  bundle="component_booklets_messages"/>' />
				</a>              		
            </td>
            <td style='vertical-align:middle;' class='<%= rowClass %>' width="40px">
            	<% if(presVer.isApproved()) { %>
                <a href='<%=publishUrl.toString()+"&"+BookletsConstants.BOOKLET_PRESENTATION_VERSION_NAME+"="+presVer.getVersionName()%>'> 
			    <img title='<spagobi:message key = "book.deploy" bundle="component_booklets_messages" />' 
			    	 src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/components/booklets/img/deploy16.png")%>' 
			    	 alt='<spagobi:message key = "book.deploy"  bundle="component_booklets_messages"/>' />
				</a>
				<% } else { out.print("&nbsp"); } %>        		
            </td>
         </tr> 
         <% } %>
    </table>          


	<br/>
	<br/>

