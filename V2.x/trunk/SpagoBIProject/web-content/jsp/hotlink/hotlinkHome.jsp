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
<%@page import="it.eng.spagobi.hotlink.service.HotLinkModule"%>
<%@page import="it.eng.spagobi.hotlink.constants.HotLinkConstants"%>

<%@ taglib uri='http://java.sun.com/portlet' prefix='portlet'%>

<portlet:defineObjects/>

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
	<div style="width:80%;" class="div_detail_area_forms">
		<p>
		<a href="#RememberMe" id="toggler_RememberMe" style="text-decoration:none;">RememberMe</a>
		<div id="popout_RememberMe" >
		
		<table style="margin:10px;padding:10px">
			<thead>
				<th class='portlet-section-header'>
					<spagobi:message key = "sbi.hotlink.document" />
				</th>
				<th class='portlet-section-header'>
					<spagobi:message key = "sbi.hotlink.subobject" />
				</th>
				<th class='portlet-section-header'>
					<spagobi:message key = "sbi.hotlink.documentType" />
				</th>
				<th class='portlet-section-header'>
					<spagobi:message key = "sbi.hotlink.engineName" />
				</th>
				<th class='portlet-section-header'>
					<spagobi:message key = "sbi.hotlink.parameters" />
				</th>
				<th class='portlet-section-header'>
					&nbsp;
				</th>
			</thead>
			<%
			boolean alternate = false;
	        String rowClass;
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
	            rowClass = (alternate) ? "portlet-section-alternate" : "portlet-section-body";
	            alternate = !alternate;  
				%>
				<tr class='portlet-font'>
					<td class='<%= rowClass %>' ><a href='<%= executeUrl %>'><%= rm.getDocumentName() + 
								(rm.getDocumentDescription() != null && !rm.getDocumentDescription().trim().equals("")? ": " + rm.getDocumentDescription() : "") %></a>
					</td>
					<td class='<%= rowClass %>' ><%= rm.getSubObjName() != null ? rm.getSubObjName() : "" %></td>
					<td class='<%= rowClass %>' ><%= rm.getDocumentType() %></td>
					<td class='<%= rowClass %>' ><%= rm.getEngineName() %></td>
					<td class='<%= rowClass %>' ><%= (rm.getParameters() != null) ?  rm.getParameters() : "" %></td>
					<td class='<%= rowClass %>' style="vertical-align:middle;">
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
		
<%--
<script type="text/javascript">
Ext.onReady(function(){

    Ext.state.Manager.setProvider(new Ext.state.CookieProvider());

    var myData = [
    	<%
			Iterator rememberMeListIt = rememberMeList.iterator();
			while (rememberMeListIt.hasNext()) {
				RememberMe rm = (RememberMe) rememberMeListIt.next();
				Map params = new HashMap();
				params.put("PAGE", "HOT_LINK_PAGE");
				params.put("OPERATION", "EXECUTE");
				params.put("DOC_ID", rm.getObjId().toString());
				params.put("PARAMETERS", rm.getParameters());
				String subObjName = rm.getSubObjName();
				if (subObjName != null) {
					params.put(SpagoBIConstants.SUBOBJECT_NAME, subObjName);
				}	
				String executeUrl = urlBuilder.getUrl(request, params);
				params = new HashMap();
				params.put("PAGE", "HOT_LINK_PAGE");
				params.put("OPERATION", "DELETE_REMEMBER_ME");
				params.put("REMEMBER_ME_ID", rm.getId().toString());
				String deleteUrl = urlBuilder.getUrl(request, params);
				String doc = rm.getDocumentName() + (rm.getDocumentDescription() != null && !rm.getDocumentDescription().trim().equals("")? ": " + rm.getDocumentDescription() : "");
				%>['<a href="<%= executeUrl %>"><%= doc %></a>','<%= rm.getDocumentType() %>','<%= rm.getEngineName() %>'],<%
			}
			%>
    ];
    
    // create the data store
    var store = new Ext.data.SimpleStore({
        fields: [
           {name: 'Document'},
           {name: 'Type'},
           {name: 'Engine'}
        ]
    });
    store.loadData(myData);
    
    // create the Grid
    var grid = new Ext.grid.GridPanel({
        store: store,
        columns: [
            {id: "Document", header: "Document", width: 75, sortable: false, dataIndex: 'Document'},
            {header: "Type", width: 75, sortable: false, dataIndex: 'Type'},
            {header: "Engine", width: 75, sortable: false, dataIndex: 'Engine'},
        ],
        stripeRows: true,
        autoExpandColumn: 'Document',
        height:350,
        width:600,
        title:'Array Grid'
    });

    grid.render('popout_RememberMe');

    grid.getSelectionModel().selectFirstRow();
});
</script>
--%>
		
		
		</div>
		</p>
		
		<p>
		<a href="#Most popular" id="toggler_MostPopular" style="text-decoration:none;">Most popular</a>
		<div id="popout_MostPopular">
			<%
			Iterator mostPopularListIt = mostPopularList.iterator();
			while (mostPopularListIt.hasNext()) {
				HotLink hotlink = (HotLink) mostPopularListIt.next();
				Map params = new HashMap();
				params.put("PAGE", "HOT_LINK_PAGE");
				params.put("OPERATION", "EXECUTE");
				params.put("DOC_ID", hotlink.getObjId().toString());
				params.put("PARAMETERS", hotlink.getParameters());
				String subObjName = hotlink.getSubObjName();
				if (subObjName != null) {
					params.put(SpagoBIConstants.SUBOBJECT_NAME, subObjName);
				}
				String executeUrl = urlBuilder.getUrl(request, params);
				%>
				<div class='portlet-section-subheader' style='margin:5px' >
					<a href='<%= executeUrl %>'><%= hotlink.getDocumentName() + 
						(hotlink.getDocumentDescription() != null && !hotlink.getDocumentDescription().trim().equals("")? ": " + hotlink.getDocumentDescription() : "") %></a><br/>
					<spagobi:message key = "sbi.hotlink.documentType" /> : <%= hotlink.getDocumentType() %><br/>
					<spagobi:message key = "sbi.hotlink.engineName" /> : <%= hotlink.getEngineName() %>
					<%
					if (hotlink.getParameters() != null && !hotlink.getParameters().trim().equals("")) {
						%>
						<br/>
						<spagobi:message key = "sbi.hotlink.parameters" /> : <%= hotlink.getParameters() %>
						<%
					}
					if (hotlink.getSubObjName() != null && !hotlink.getSubObjName().trim().equals("")) {
						%>
						<br/>
						<spagobi:message key = "sbi.hotlink.subobject" /> : <%= hotlink.getSubObjName() %>
						<%
					}
					%>
				</div>
			<%
			}
			%>
		</div>
		</p>
		
		<p>
		<a href="#My recently used" id="toggler_MyRecentlyUsed" style="text-decoration:none;">My recently used</a>
		<div id="popout_MyRecentlyUsed">
			<%
			Iterator myRecentlyUsedListIt = myRecentlyUsedList.iterator();
			while (myRecentlyUsedListIt.hasNext()) {
				HotLink hotlink = (HotLink) myRecentlyUsedListIt.next();
				Map params = new HashMap();
				params.put("PAGE", "HOT_LINK_PAGE");
				params.put("OPERATION", "EXECUTE");
				params.put("DOC_ID", hotlink.getObjId().toString());
				params.put("PARAMETERS", hotlink.getParameters());
				String subObjName = hotlink.getSubObjName();
				if (subObjName != null) {
					params.put(SpagoBIConstants.SUBOBJECT_NAME, subObjName);
				}
				String executeUrl = urlBuilder.getUrl(request, params);
				%>
				<div class='portlet-section-subheader' style='margin:5px' >
					<a href='<%= executeUrl %>'><%= hotlink.getDocumentName() + 
						(hotlink.getDocumentDescription() != null && !hotlink.getDocumentDescription().trim().equals("")? ": " + hotlink.getDocumentDescription() : "") %></a><br/>
					<spagobi:message key = "sbi.hotlink.documentType" /> : <%= hotlink.getDocumentType() %><br/>
					<spagobi:message key = "sbi.hotlink.engineName" /> : <%= hotlink.getEngineName() %><br/>
					<%
					if (hotlink.getParameters() != null && !hotlink.getParameters().trim().equals("")) {
						%>
						<br/>
						<spagobi:message key = "sbi.hotlink.parameters" /> : <%= hotlink.getParameters() %>
						<%
					}
					if (hotlink.getSubObjName() != null && !hotlink.getSubObjName().trim().equals("")) {
						%>
						<br/>
						<spagobi:message key = "sbi.hotlink.subobject" /> : <%= hotlink.getSubObjName() %>
						<%
					}
					%>
				</div>

			<%
			}
			%>
		</div>
		</p>
		
		
		<p>
		<div id="popout_RememberMe2" >
		<table style="margin:10px;padding:10px">
			<thead>
				<th class='portlet-section-header'>
					<spagobi:message key = "sbi.hotlink.document" />
				</th>
				<th class='portlet-section-header'>
					<spagobi:message key = "sbi.hotlink.subobject" />
				</th>
				<th class='portlet-section-header'>
					<spagobi:message key = "sbi.hotlink.documentType" />
				</th>
				<th class='portlet-section-header'>
					<spagobi:message key = "sbi.hotlink.engineName" />
				</th>
				<th class='portlet-section-header'>
					<spagobi:message key = "sbi.hotlink.parameters" />
				</th>
				<th class='portlet-section-header'>
					&nbsp;
				</th>
			</thead>
			<%
			rememberMeListIt = rememberMeList.iterator();
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
	            rowClass = (alternate) ? "portlet-section-alternate" : "portlet-section-body";
	            alternate = !alternate;  
				%>
				<tr class='portlet-font'>
					<td class='<%= rowClass %>' ><a href='<%= executeUrl %>'><%= rm.getDocumentName() + 
								(rm.getDocumentDescription() != null && !rm.getDocumentDescription().trim().equals("")? ": " + rm.getDocumentDescription() : "") %></a>
					</td>
					<td class='<%= rowClass %>' ><%= rm.getSubObjName() != null ? rm.getSubObjName() : "" %></td>
					<td class='<%= rowClass %>' ><%= rm.getDocumentType() %></td>
					<td class='<%= rowClass %>' ><%= rm.getEngineName() %></td>
					<td class='<%= rowClass %>' ><%= (rm.getParameters() != null) ?  rm.getParameters() : "" %></td>
					<td class='<%= rowClass %>' style="vertical-align:middle;">
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
		<div id="popout_RememberMe_renderTo">
		</div>
		</p>
		
	</div>
</div>

<script type="text/javascript">

toggle('popout_RememberMe','toggler_RememberMe', false);
toggle('popout_MostPopular','toggler_MostPopular', true);
toggle('popout_MyRecentlyUsed','toggler_MyRecentlyUsed', true);

</script>

<script type="text/javascript">
Ext.onReady(function(){
    var p = new Ext.Panel({
        title: 'RememberMe',
        collapsible:true,
        renderTo: 'popout_RememberMe_renderTo',
        contentEl: 'popout_RememberMe2'
    });
});
</script>


<iframe id='iframeLogin'
	name='iframeLogin'
	src='<%=renderResponse.encodeURL(renderRequest.getContextPath() + "/servlet/AdapterHTTP?ACTION_NAME=HIDDEN_LOGIN&userId=biadmin&NEW_SESSION=TRUE")%>'
	frameborder='0' >
</iframe>

<%@ include file="/jsp/commons/footer.jsp"%>