<%--
SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2008 Engineering Ingegneria Informatica S.p.A.

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
--%>


<%@ include file="/jsp/commons/portlet_base.jsp"%>

<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@page import="it.eng.spagobi.hotlink.rememberme.bo.RememberMe"%>
<%@page import="it.eng.spagobi.hotlink.rememberme.bo.HotLink"%>
<%@page import="it.eng.spagobi.hotlink.service.HotLinkModule"%>
<%@page import="it.eng.spagobi.hotlink.constants.HotLinkConstants"%>
<%@page import="it.eng.spago.navigation.LightNavigationManager"%>
<%@page import="it.eng.spagobi.monitoring.dao.AuditManager"%>
<%@page import="it.eng.spagobi.commons.dao.DAOFactory"%>

<%@ taglib uri='http://java.sun.com/portlet' prefix='portlet'%>

<portlet:defineObjects/>

<%
List rememberMeList = null;
List mostPopularList = null;
List myRecentlyUsedList = null;

SourceBean hotlinkSB = (SourceBean) spagoconfig.getAttribute(HotLinkConstants.HOTLINK);
SourceBean mostPopular = (SourceBean) hotlinkSB.getFilteredSourceBeanAttribute("SECTION", "name", HotLinkConstants.MOST_POPULAR);
SourceBean myRecentlyUsed = (SourceBean) hotlinkSB.getFilteredSourceBeanAttribute("SECTION", "name", HotLinkConstants.MY_RECENTLY_USED);
SourceBean rememberMe = (SourceBean) hotlinkSB.getFilteredSourceBeanAttribute("SECTION", "name", HotLinkConstants.REMEMBER_ME);
if (mostPopular != null) {
	int limit = Integer.parseInt((String) mostPopular.getAttribute(HotLinkConstants.ROWS_NUMBER));
	mostPopularList = AuditManager.getInstance().getMostPopular(userProfile, limit);
}
if (myRecentlyUsed != null) {
	int limit = Integer.parseInt((String) myRecentlyUsed.getAttribute(HotLinkConstants.ROWS_NUMBER));
	myRecentlyUsedList = AuditManager.getInstance().getMyRecentlyUsed(userProfile, limit);
}
if (rememberMe != null) {
	rememberMeList = DAOFactory.getRememberMeDAO().getMyRememberMe(userProfile.getUserUniqueIdentifier().toString());
}

%>

<table class='header-table-portlet-section'>		
	<tr class='header-row-portlet-section'>
		<td class='header-title-column-portlet-section-no-buttons' 
		    style='vertical-align:middle;padding-left:5px;'>
			<spagobi:message key = "sbi.hotlink.title" />
		</td>
	</tr>
</table>

<div style="width:80%;" class="div_detail_area_forms">
	<p style="margin: 10px">
		<div id="renderTo_RememberMe"></div>
	</p>	
	<p style="margin: 10px">
		<div id="renderTo_MostPopular"></div>
	</p>
	<p style="margin: 10px">
		<div id="renderTo_MyRecentlyUsed"></div>
	</p>
</div>

<%-- Start scripts for Remember Me list --%>
<% if (rememberMeList != null) { %>
<script type="text/javascript">
Ext.onReady(function(){

    Ext.state.Manager.setProvider(new Ext.state.CookieProvider());

    var myDataRememberMe = [
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
			executeUrl = executeUrl.replaceAll("&amp;", "&");
			params = new HashMap();
			params.put("PAGE", "HOT_LINK_PAGE");
			params.put("OPERATION", "DELETE_REMEMBER_ME");
			params.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "TRUE");
			params.put("REMEMBER_ME_ID", rm.getId().toString());
			String deleteUrl = urlBuilder.getUrl(request, params);
			deleteUrl = deleteUrl.replaceAll("&amp;", "&");
			%>['<%= rm.getName() %>','<%= rm.getDescription() %>','<%= rm.getDocumentLabel() %>','<%= rm.getDocumentName() %>',
				'<%= rm.getDocumentDescription() %>','<%= rm.getDocumentType() %>','<%= executeUrl %>','<%= deleteUrl %>']<%= rememberMeListIt.hasNext() ? "," : "" %><%
		}
		%>
    ];
    
    Ext.QuickTips.init();
    
    // create the data store
    var storeRememberMe = new Ext.data.SimpleStore({
        fields: [
           {name: 'Name'},
           {name: 'Description'},
           {name: 'DocumentLabel'},
           {name: 'DocumentName'},
           {name: 'DocumentDescription'},
           {name: 'DocumentType'},
           {name: 'Url'},
           {name: 'DeleteUrl'}
        ]
    });
    storeRememberMe.loadData(myDataRememberMe);
    
   	var menu = 
		new Ext.menu.Menu({
			id:'submenu',
			items: [{
				text:'<spagobi:message key = "sbi.hotlink.deleteRememberMe" />',
				scope: this,
				handler:function(){
					location.href = storeRememberMe.getAt(menu.rowIndex).get('DeleteUrl');
				}
			}]
		});
    
	Ext.ToolTip.prototype.onTargetOver =
		Ext.ToolTip.prototype.onTargetOver.createInterceptor(function(e) {
			this.baseTarget = e.getTarget();
		});
	Ext.ToolTip.prototype.onMouseMove =
		Ext.ToolTip.prototype.onMouseMove.createInterceptor(function(e) {
			if (!e.within(this.baseTarget)) {
				this.onTargetOver(e);
				return false;
			}
		});
    
    // create the Grid
    var gridRememberMe = new Ext.grid.GridPanel({
        store: storeRememberMe,
        columns: [
            {id: "Name", header: "Name", sortable: true, dataIndex: 'Name'},
            {header: "Document", sortable: true, dataIndex: 'DocumentLabel'},
            {header: "Document name", sortable: true, dataIndex: 'DocumentName'},
            {header: "Document description", sortable: true, dataIndex: 'DocumentDescription'},
            {header: "Document type", sortable: true, dataIndex: 'DocumentType'}
        ],
		onRender: function() {
        	Ext.grid.GridPanel.prototype.onRender.apply(this, arguments);
        	this.addEvents("beforetooltipshow");
	        this.tooltip = new Ext.ToolTip({
	        	renderTo: Ext.getBody(),
	        	target: this.view.mainBody,
	        	listeners: {
	        		beforeshow: function(qt) {
	        			var v = this.getView();
			            var row = v.findRowIndex(qt.baseTarget);
			            var cell = v.findCellIndex(qt.baseTarget);
			            this.fireEvent("beforetooltipshow", this, row, cell);
	        		},
	        		scope: this
	        	}
	        });
        },
		viewConfig: {
        	forceFit: true
		},
        stripeRows: true,
        collapsible: true,
        //autoExpandColumn: 'Document',
        height:200,
        width:600,
        title:'Remember Me',
		listeners: {
			render: function(g) {
				g.on("beforetooltipshow", function(grid, row, col) {
					if (storeRememberMe.getAt(row)) {
						grid.tooltip.body.update(storeRememberMe.getAt(row).get('Description'));
					}
				});
			}
		}
    });
    
	gridRememberMe.on(
		'rowclick', function(grid, rowIndex, e) {
			location.href = storeRememberMe.getAt(rowIndex).get('Url');
		}
	);
	
	gridRememberMe.on(
		'rowcontextmenu', function(grid, rowIndex, e) {
			var record = grid.getStore().getAt(rowIndex);
			e.stopEvent();
			menu.rowIndex = rowIndex;
			menu.showAt(e.getXY());
		}
	);
	
    gridRememberMe.render('renderTo_RememberMe');

    //gridRememberMe.getSelectionModel().selectFirstRow();
});
</script>
<% } %>
<%-- End scripts for Remember Me list --%>

<%-- Start scripts for most popular list --%>
<% if (mostPopularList != null && mostPopularList.size() > 0) { %>
<script type="text/javascript">
Ext.onReady(function(){

    Ext.state.Manager.setProvider(new Ext.state.CookieProvider());

    var myDataMostPopular = [
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
			executeUrl = executeUrl.replaceAll("&amp;", "&");
			%>['<%= hotlink.getDocumentLabel() %>','<%= hotlink.getDocumentName() %>','<%= hotlink.getDocumentDescription() %>',
				'<%= hotlink.getDocumentType() %>','<%= executeUrl %>']<%= mostPopularListIt.hasNext() ? "," : "" %><%
		}
		%>
    ];
    
    // create the data store
    var storeMostPopular = new Ext.data.SimpleStore({
        fields: [
           {name: 'DocumentLabel'},
           {name: 'DocumentName'},
           {name: 'DocumentDescription'},
           {name: 'DocumentType'},
           {name: 'Url'}
        ]
    });
    storeMostPopular.loadData(myDataMostPopular);
    
    // create the Grid
    var gridMostPopular = new Ext.grid.GridPanel({
        store: storeMostPopular,
        columns: [
            {id: "Document", header: "Document", sortable: true, dataIndex: 'DocumentLabel'},
            {header: "Document name", sortable: true, dataIndex: 'DocumentName'},
            {header: "Document description", sortable: true, dataIndex: 'DocumentDescription'},
            {header: "Document type", sortable: true, dataIndex: 'DocumentType'}
        ],
		viewConfig: {
        	forceFit: true
		},
        stripeRows: true,
        collapsible: true,
        //autoExpandColumn: 'Document',
        height:200,
        width:600,
        title:'Most Popular'
    });
	gridMostPopular.on(
		'rowclick', function(grid, rowIndex, e) {
			location.href = storeMostPopular.getAt(rowIndex).get('Url');
		}
	);
    gridMostPopular.render('renderTo_MostPopular');

    //gridMostPopular.getSelectionModel().selectFirstRow();
});
</script>
<% } %>
<%-- End scripts for most popular list --%>

<%-- Start scripts for my recently used list --%>
<% if (myRecentlyUsedList != null && myRecentlyUsedList.size() > 0) { %>
<script type="text/javascript">
Ext.onReady(function(){

    Ext.state.Manager.setProvider(new Ext.state.CookieProvider());

    var myDataMyRecentlyUsed = [
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
			executeUrl = executeUrl.replaceAll("&amp;", "&");
			%>['<%= hotlink.getDocumentLabel() %>','<%= hotlink.getDocumentName() %>','<%= hotlink.getDocumentDescription() %>',
				'<%= hotlink.getDocumentType() %>','<%= executeUrl %>']<%= myRecentlyUsedListIt.hasNext() ? "," : "" %><%
		}
		%>
    ];
    
    // create the data store
    var storeMyRecentlyUsed = new Ext.data.SimpleStore({
        fields: [
           {name: 'DocumentLabel'},
           {name: 'DocumentName'},
           {name: 'DocumentDescription'},
           {name: 'DocumentType'},
           {name: 'Url'}
        ]
    });
    storeMyRecentlyUsed.loadData(myDataMyRecentlyUsed);
    
    // create the Grid
    var gridMyRecentlyUsed = new Ext.grid.GridPanel({
        store: storeMyRecentlyUsed,
        columns: [
            {id: "Document", header: "Document", sortable: true, dataIndex: 'DocumentLabel'},
            {header: "Document name", sortable: true, dataIndex: 'DocumentName'},
            {header: "Document description", sortable: true, dataIndex: 'DocumentDescription'},
            {header: "Document type", sortable: true, dataIndex: 'DocumentType'}
        ],
		viewConfig: {
        	forceFit: true
		},
        stripeRows: true,
        collapsible: true,
        //autoExpandColumn: 'Document',
        height:200,
        width:600,
        title:'My Recently Used'
    });
	gridMyRecentlyUsed.on(
		'rowclick', function(grid, rowIndex, e) {
			location.href = storeMyRecentlyUsed.getAt(rowIndex).get('Url');
		}
	);
    gridMyRecentlyUsed.render('renderTo_MyRecentlyUsed');

    //gridMyRecentlyUsed.getSelectionModel().selectFirstRow();
});
</script>
<% } %>
<%-- End scripts for my recently used list --%>

<%@ include file="/jsp/commons/footer.jsp"%>