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
<%@page import="it.eng.spago.navigation.LightNavigationManager"%>

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
<%--
<div style="width:80%;" class="div_detail_area_forms">
	<p>
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
	

<script type="text/javascript">
Ext.onReady(function(){

    Ext.state.Manager.setProvider(new Ext.state.CookieProvider());

    var myDataRememberMe = [
    	<%
			rememberMeListIt = rememberMeList.iterator();
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
				System.out.println("executeUrl: " + executeUrl);
				
				params = new HashMap();
				params.put("PAGE", "HOT_LINK_PAGE");
				params.put("OPERATION", "DELETE_REMEMBER_ME");
				params.put("REMEMBER_ME_ID", rm.getId().toString());
				String deleteUrl = urlBuilder.getUrl(request, params);
				%>['<%= rm.getDocumentLabel() %>','<%= rm.getDocumentName() %>','<%= rm.getDocumentDescription() %>','<%= rm.getDocumentType() %>','<%= executeUrl %>'],<%
			}
			%>
    ];
    
    // create the data store
    var storeRememberMe = new Ext.data.SimpleStore({
        fields: [
           {name: 'DocumentLabel'},
           {name: 'DocumentName'},
           {name: 'DocumentDescription'},
           {name: 'DocumentType'},
           {name: 'Url'}
        ]
    });
    storeRememberMe.loadData(myDataRememberMe);
    
    // create the Grid
    var gridRememberMe = new Ext.grid.GridPanel({
        store: storeRememberMe,
        columns: [
            {id: "Document", header: "Document", sortable: true, dataIndex: 'DocumentLabel'},
            {header: "Document name", sortable: true, dataIndex: 'DocumentName'},
            {header: "Document description", sortable: true, dataIndex: 'DocumentDescription'},
            {header: "Document type", sortable: true, dataIndex: 'DocumentType'},
        ],
		viewConfig: {
        	forceFit: true
		},
        stripeRows: true,
        collapsible: true,
        //autoExpandColumn: 'Document',
        height:350,
        width:600,
        title:'Remember Me'
    });
	gridRememberMe.on(
		'rowclick', function(grid, rowIndex, e) {
			location.href = storeRememberMe.getAt(rowIndex).get('Url');
		}
	);
    gridRememberMe.render('grid_renderTo');

    gridRememberMe.getSelectionModel().selectFirstRow();
});
</script>
	
	
	</div>
	</p>
	
	<p>
	<div id="popout_MostPopular">
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
		</thead>
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
            rowClass = (alternate) ? "portlet-section-alternate" : "portlet-section-body";
            alternate = !alternate;  
			%>
			<tr class='portlet-font'>
				<td class='<%= rowClass %>' ><a href='<%= executeUrl %>'><%= hotlink.getDocumentName() + 
							(hotlink.getDocumentDescription() != null && !hotlink.getDocumentDescription().trim().equals("")? ": " + hotlink.getDocumentDescription() : "") %></a>
				</td>
				<td class='<%= rowClass %>' ><%= hotlink.getSubObjName() != null ? hotlink.getSubObjName() : "" %></td>
				<td class='<%= rowClass %>' ><%= hotlink.getDocumentType() %></td>
				<td class='<%= rowClass %>' ><%= hotlink.getEngineName() %></td>
			</tr>
		<%
		}
		%>
	</table>
	</div>
	</p>
	
	<p>
	<div id="popout_MyRecentlyUsed">
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
		</thead>
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
            rowClass = (alternate) ? "portlet-section-alternate" : "portlet-section-body";
            alternate = !alternate;
			%>
			<tr class='portlet-font'>
				<td class='<%= rowClass %>' ><a href='<%= executeUrl %>'><%= hotlink.getDocumentName() + 
							(hotlink.getDocumentDescription() != null && !hotlink.getDocumentDescription().trim().equals("")? ": " + hotlink.getDocumentDescription() : "") %></a>
				</td>
				<td class='<%= rowClass %>' ><%= hotlink.getSubObjName() != null ? hotlink.getSubObjName() : "" %></td>
				<td class='<%= rowClass %>' ><%= hotlink.getDocumentType() %></td>
				<td class='<%= rowClass %>' ><%= hotlink.getEngineName() %></td>
			</tr>

		<%
		}
		%>
	</table>
	</div>
	</p>
	
	<p style="margin: 10px">
	<div id="popout_RememberMe_renderTo">
	</div>
	</p>
	
	<p style="margin: 10px">
	<div id="popout_MostPopular_renderTo">
	</div>
	</p>
	
	<p style="margin: 10px">
	<div id="popout_MyRecentlyUsed_renderTo">
	</div>
	</p>

	<p style="margin: 10px">
	<div id="grid_renderTo">
	</div>
	</p>
	
</div>


<script type="text/javascript">
Ext.onReady(function(){
    var p = new Ext.Panel({
        title: 'RememberMe',
        collapsible:true,
        renderTo: 'popout_RememberMe_renderTo',
        contentEl: 'popout_RememberMe'
    });
});
</script>

<script type="text/javascript">
Ext.onReady(function(){
    var p = new Ext.Panel({
        title: 'Most Popular',
        collapsible:true,
        renderTo: 'popout_MostPopular_renderTo',
        contentEl: 'popout_MostPopular'
    });
});
</script>

<script type="text/javascript">
Ext.onReady(function(){
    var p = new Ext.Panel({
        title: 'My Recently Used',
        collapsible:true,
        renderTo: 'popout_MyRecentlyUsed_renderTo',
        contentEl: 'popout_MyRecentlyUsed'
    });
});
</script>
--%>

<%@ include file="/jsp/commons/footer.jsp"%>