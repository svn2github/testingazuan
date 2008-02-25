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


<%@ include file="/jsp/commons/portlet_base.jsp"%>

<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@page import="it.eng.spagobi.hotlink.rememberme.bo.RememberMe"%>
<%@page import="it.eng.spagobi.hotlink.rememberme.bo.HotLink"%>
<%@page import="it.eng.spagobi.hotlink.modules.HotLinkModule"%>
<%@page import="it.eng.spagobi.hotlink.constants.HotLinkConstants"%>

<%

SourceBean moduleResponse = (SourceBean) aServiceResponse.getAttribute(HotLinkModule.MODULE_NAME); 
List rememberMeList = (List) moduleResponse.getAttribute(HotLinkConstants.REMEMBER_ME);
List mostPopularList = (List) moduleResponse.getAttribute(HotLinkConstants.MOST_POPULAR);
List myRecentlyUsedList = (List) moduleResponse.getAttribute(HotLinkConstants.MY_RECENTLY_USED);
%>

<table class='header-table-portlet-section'>		
	<tr class='header-row-portlet-section'>
		<td class='header-title-column-portlet-section-no-buttons' 
		    style='vertical-align:middle;padding-left:5px;'>
			<spagobi:message key = "sbi.hotlink.title" />
		</td>
	</tr>
</table>

<div class="div_background_no_img">
	<div style="width:40%;" class="div_detail_area_forms">
		<p>
		<a href="#RememberMe" id="toggler_RememberMe" >RememberMe</a>
		<div id="popout_RememberMe" >
		<table style="margin:10px;padding:10px">
			<%
			Iterator rememberMeListIt = rememberMeList.iterator();
			while (rememberMeListIt.hasNext()) {
				RememberMe rm = (RememberMe) rememberMeListIt.next();
				Map params = new HashMap();
				params.put("PAGE", "HOT_LINK_PAGE");
				params.put("OPERATION", "EXECUTE");
				params.put("DOC_ID", rm.getObjId().toString());
				params.put("PARAMETERS", rm.getParameters());
				String executeUrl = urlBuilder.getUrl(request, params);
				params = new HashMap();
				params.put("PAGE", "HOT_LINK_PAGE");
				params.put("OPERATION", "DELETE_REMEMBER_ME");
				params.put("REMEMBER_ME_ID", rm.getId().toString());
				String deleteUrl = urlBuilder.getUrl(request, params);
				%>
				<tr>
					<td>
						<div class='portlet-section-subheader' style='margin:5px' >
							<a href='<%= executeUrl %>'><%= rm.getDocumentName() + 
								(rm.getDocumentDescription() != null && !rm.getDocumentDescription().trim().equals("")? ": " + rm.getDocumentDescription() : "") %></a><br/>
							<spagobi:message key = "sbi.hotlink.documentType" /> : <%= rm.getDocumentType() %><br/>
							<spagobi:message key = "sbi.hotlink.engineName" /> : <%= rm.getEngineName() %><br/>
							<spagobi:message key = "sbi.hotlink.parameters" /> : <%= (rm.getParameters() != null) ?  rm.getParameters() : "" %><br/>
						</div>
					</td>
					<td style="vertical-align:middle;">
						<a href='<%= deleteUrl %>'>
							<img title='<spagobi:message key = "sbi.hotlink.deleteRememberMe" />'
								src='<%= urlBuilder.getResourceLink(request, "/img/erase.gif")%>'
								alt='<spagobi:message key = "sbi.hotlink.deleteRememberMe" />' />
						</a>
					</td>
				</tr>
			<%
			}
			%>
		</table>
		</div>
		</p>
		
		<p>
		<a href="#Most popular" id="toggler_MostPopular">Most popular</a>
		<div id="popout_MostPopular">
		<table style="margin:10px;padding:10px">
			<%
			Iterator mostPopularListIt = mostPopularList.iterator();
			while (mostPopularListIt.hasNext()) {
				HotLink hotlink = (HotLink) mostPopularListIt.next();
				Map params = new HashMap();
				params.put("PAGE", "HOT_LINK_PAGE");
				params.put("OPERATION", "EXECUTE");
				params.put("DOC_ID", hotlink.getObjId().toString());
				params.put("PARAMETERS", hotlink.getParameters());
				String executeUrl = urlBuilder.getUrl(request, params);
				%>
				<tr>
					<td>
						<div class='portlet-section-subheader' style='margin:5px' >
							<a href='<%= executeUrl %>'><%= hotlink.getDocumentName() + 
								(hotlink.getDocumentDescription() != null && !hotlink.getDocumentDescription().trim().equals("")? ": " + hotlink.getDocumentDescription() : "") %></a><br/>
							<spagobi:message key = "sbi.hotlink.documentType" /> : <%= hotlink.getDocumentType() %><br/>
							<spagobi:message key = "sbi.hotlink.engineName" /> : <%= hotlink.getEngineName() %><br/>
							<spagobi:message key = "sbi.hotlink.parameters" /> : <%= (hotlink.getParameters() != null) ?  hotlink.getParameters() : "" %><br/>
						</div>
					</td>
					<td style="vertical-align:middle;">
						&nbsp;
					</td>
				</tr>
			<%
			}
			%>
		</table>
		</div>
		</p>
		
		<p>
		<a href="#My recently used" id="toggler_MyRecentlyUsed">My recently used</a>
		<div id="popout_MyRecentlyUsed">
		<table style="margin:10px;padding:10px">
			<%
			Iterator myRecentlyUsedListIt = myRecentlyUsedList.iterator();
			while (myRecentlyUsedListIt.hasNext()) {
				HotLink hotlink = (HotLink) myRecentlyUsedListIt.next();
				Map params = new HashMap();
				params.put("PAGE", "HOT_LINK_PAGE");
				params.put("OPERATION", "EXECUTE");
				params.put("DOC_ID", hotlink.getObjId().toString());
				params.put("PARAMETERS", hotlink.getParameters());
				String executeUrl = urlBuilder.getUrl(request, params);
				%>
				<tr>
					<td>
						<div class='portlet-section-subheader' style='margin:5px' >
							<a href='<%= executeUrl %>'><%= hotlink.getDocumentName() + 
								(hotlink.getDocumentDescription() != null && !hotlink.getDocumentDescription().trim().equals("")? ": " + hotlink.getDocumentDescription() : "") %></a><br/>
							<spagobi:message key = "sbi.hotlink.documentType" /> : <%= hotlink.getDocumentType() %><br/>
							<spagobi:message key = "sbi.hotlink.engineName" /> : <%= hotlink.getEngineName() %><br/>
							<spagobi:message key = "sbi.hotlink.parameters" /> : <%= (hotlink.getParameters() != null) ?  hotlink.getParameters() : "" %><br/>
						</div>
					</td>
					<td style="vertical-align:middle;">
						&nbsp;
					</td>
				</tr>
			<%
			}
			%>
		</table>
		</div>
		</p>
	</div>
</div>

<script type="text/javascript">

toggle('popout_RememberMe','toggler_RememberMe', false);
toggle('popout_MostPopular','toggler_MostPopular', true);
toggle('popout_MyRecentlyUsed','toggler_MyRecentlyUsed', true);

</script>

<%@ include file="/jsp/commons/footer.jsp"%>