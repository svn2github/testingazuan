<%--
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
--%>

<%@page import="java.util.Iterator"%>
<%@page import="it.eng.spagobi.commons.dao.DAOFactory"%>
<%@page import="java.util.List"%>
<%@page import="it.eng.spagobi.commons.bo.Role"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="it.eng.spago.navigation.LightNavigationManager"%>
<%@page import="it.eng.spagobi.commons.utilities.GeneralUtilities"%>
<%@page import="it.eng.spagobi.commons.bo.Domain"%>

<%@ include file="/jsp/commons/portlet_base.jsp"%>

<%
Map backUrlParameters = new HashMap();
backUrlParameters.put("ACTION_NAME", "START_ACTION");
backUrlParameters.put("PUBLISHER_NAME", "LoginSBIToolsPublisher");
backUrlParameters.put(LightNavigationManager.LIGHT_NAVIGATOR_RESET, "true");
String backUrl = urlBuilder.getUrl(request, backUrlParameters);

Map synchUrlParameters = new HashMap();
synchUrlParameters.put("PAGE", "detailRolesPage");
synchUrlParameters.put("MESSAGEDET", "ROLES_SYNCH");
synchUrlParameters.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
String synchUrl = urlBuilder.getUrl(request, synchUrlParameters);
%>

<table class='header-table-portlet-section'>		
	<tr class='header-row-portlet-section'>
		<td class='header-title-column-portlet-section' 
		    style='vertical-align:middle;padding-left:5px;'>
			<spagobi:message key = "SBISet.ListRoles.title" />
		</td>
		<td class='header-empty-column-portlet-section'>&nbsp;</td>
		<td class='header-button-column-portlet-section'>
			<a href="<%=synchUrl%>"> 
      			<img class='header-button-image-portlet-section' title='<spagobi:message key = "SBISet.ListRoles.synchButton" />' src='<%=urlBuilder.getResourceLink(request, "/img/updateState.png")%>' alt='<spagobi:message key = "SBISet.ListRoles.synchButton" />' /> 
			</a>
		</td>
		<td class='header-button-column-portlet-section'>
			<a href="javascript:saveRoles();"> 
      			<img class='header-button-image-portlet-section' title='<spagobi:message key = "SBISet.ListRoles.saveButton" />' src='<%=urlBuilder.getResourceLink(request, "/img/save.png")%>' alt='<spagobi:message key = "SBISet.ListRoles.saveButton" />' /> 
			</a>
		</td>
		<td class='header-button-column-portlet-section'>
			<a href='<%=backUrl%>'> 
      			<img class='header-button-image-portlet-section' title='<spagobi:message key = "SBISet.ListRoles.backButton" />' src='<%=urlBuilder.getResourceLink(request, "/img/back.png")%>' alt='<spagobi:message key = "SBISet.ListRoles.backButton" />' />
			</a>
		</td>
	</tr>
</table>

<div style="width:80%;" class="div_detail_area_forms">
	<p style="margin: 10px">
		<div id="renderTo_Roles"></div>
	</p>
</div>

<%
List roles = DAOFactory.getRoleDAO().loadAllRoles();
%>

<script type="text/javascript">

var store;

Ext.grid.CheckColumn = function(config){
    Ext.apply(this, config);
    if(!this.id){
        this.id = Ext.id();
    }
    this.renderer = this.renderer.createDelegate(this);
};

Ext.grid.CheckColumn.prototype ={
    init : function(grid){
        this.grid = grid;
        this.grid.on('render', function(){
            var view = this.grid.getView();
            view.mainBody.on('mousedown', this.onMouseDown, this);
        }, this);
    },

	onMouseDown: function(e, t) {
	    if (t.className && t.className.indexOf('x-grid3-cc-'+this.id) != -1){
            e.stopEvent();
            var index = this.grid.getView().findRowIndex(t);
            var record = this.grid.store.getAt(index);
            record.set(this.dataIndex, !record.data[this.dataIndex]);
       	}
	},

    renderer : function(v, p, record){
        p.css += ' x-grid3-check-col-td'; 
        return '<div class="x-grid3-check-col'+(v?'-on':'')+' x-grid3-cc-'+this.id+'">&#160;</div>';
    }
};

Ext.onReady(function(){
	Ext.state.Manager.setProvider(new Ext.state.CookieProvider());
    var roles = [
    	<%
    	Iterator it = roles.iterator();
    	while (it.hasNext()) {
    		Role role = (Role) it.next();
    		HashMap params = new HashMap();
			params.put("PAGE", "DetailRolesPage");
			params.put("MESSAGEDET", "DETAIL_DEL");
			params.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "TRUE");
			params.put("EXT_ROLE_ID", role.getId().toString());
			String deleteUrl = urlBuilder.getUrl(request, params);
			deleteUrl = deleteUrl.replaceAll("&amp;", "&");
    		String roleTypeDescription = msgBuilder.getMessage("SBISet.ListRoles.roleType." + role.getRoleTypeCD(), request);
    		%>['<%= role.getId() %>', '<%= role.getName() %>', '<%= role.getDescription() %>', '<%= roleTypeDescription %>', 
    		 <%= role.isAbleToSeeSubobjects() %>, <%= role.isAbleToSeeSnapshots() %>,
    		 <%= role.isAbleToSeeViewpoints() %>, <%= role.isAbleToSeeNotes() %>, <%= role.isAbleToSeeMetadata() %>, 
    		 <%= role.isAbleToSendMail() %>, <%= role.isAbleToSaveRememberMe() %>, 
    		 <%= role.isAbleToSaveIntoPersonalFolder() %>,'<a href="<%=deleteUrl%>"><img src="<%=urlBuilder.getResourceLink(request, "/img/erase.gif")%>" /></a>']<%= it.hasNext() ? "," : "" %><%
    	}
    	%>
    ];
    
    Ext.QuickTips.init();
    
    // creates the roles data store
    store = new Ext.data.SimpleStore({
        fields: [
           {name: 'Id'},
           {name: 'Name'},
           {name: 'Description'},
           {name: 'Type'},
           {name: 'Subojects'},
           {name: 'Snapshots'},
           {name: 'Viewpoints'},
           {name: 'Notes'},
           {name: 'Metadata'},
           {name: 'SendMail'},
           {name: 'RememberMe'},
           {name: 'PersonalFolder'},
           {name: 'DeleteUrl'}
        ]
    });
    store.loadData(roles);
    
    var roleTypes = [
	    <%
	    List roleTypes = DAOFactory.getDomainDAO().loadListDomainsByType("ROLE_TYPE");
	  	Iterator roleTypesIt = roleTypes.iterator();
	  	while (roleTypesIt.hasNext()) {
	  		Domain roleType = (Domain) roleTypesIt.next();
	  		String roleDescription = msgBuilder.getMessage("SBISet.ListRoles.roleType." + roleType.getValueCd(), request);
	  		%>['<%= roleType.getValueCd() %>', '<%= roleDescription %>']<%= roleTypesIt.hasNext() ? "," : "" %><%
	  	}
	  	%>  
    ];
    // creates the role types data store
    roleTypesStore = new Ext.data.SimpleStore({
        fields: [
           {name: 'Code'},
           {name: 'Description'}
        ]
    });
    roleTypesStore.loadData(roleTypes);
    
    var checkColumnWidth = 100;
    
	var subObjectsColumn = new Ext.grid.CheckColumn({
		header: '<spagobi:message key="SBISet.ListRoles.columnSubobjects" />',
		tooltip: '<spagobi:message key="SBISet.ListRoles.columnSubobjectsTooltip" />',
		dataIndex: 'Subojects',
		align: 'center'
	});
	var snapshotsColumn = new Ext.grid.CheckColumn({
		header: '<spagobi:message key="SBISet.ListRoles.columnSnapshots" />',
		tooltip: '<spagobi:message key="SBISet.ListRoles.columnSnapshotsTooltip" />',
		dataIndex: 'Snapshots',
		align: 'center'
	});
	var viewpointsColumn = new Ext.grid.CheckColumn({
		header: '<spagobi:message key="SBISet.ListRoles.columnViewpoints" />',
		tooltip: '<spagobi:message key="SBISet.ListRoles.columnViewpointsTooltip" />',
		dataIndex: 'Viewpoints',
		align: 'center'
	});
	var notesColumn = new Ext.grid.CheckColumn({
		header: '<spagobi:message key="SBISet.ListRoles.columnNotes" />',
		tooltip: '<spagobi:message key="SBISet.ListRoles.columnNotesTooltip" />',
		dataIndex: 'Notes',
		align: 'center'
	});
	var metadataColumn = new Ext.grid.CheckColumn({
		header: '<spagobi:message key="SBISet.ListRoles.columnMetadata" />',
		tooltip: '<spagobi:message key="SBISet.ListRoles.columnMetadataTooltip" />',
		dataIndex: 'Metadata',
		align: 'center'
	});
	var sendMailColumn = new Ext.grid.CheckColumn({
		header: '<spagobi:message key="SBISet.ListRoles.columnSendMail" />',
		tooltip: '<spagobi:message key="SBISet.ListRoles.columnSendMailTooltip" />',
		dataIndex: 'SendMail',
		align: 'center'
	});
	var rememberMeColumn = new Ext.grid.CheckColumn({
		header: '<spagobi:message key="SBISet.ListRoles.columnRememberMe" />',
		tooltip: '<spagobi:message key="SBISet.ListRoles.columnRememberMeTooltip" />',
		dataIndex: 'RememberMe',
		align: 'center'
	});
	var personalFolderColumn = new Ext.grid.CheckColumn({
		header: '<spagobi:message key="SBISet.ListRoles.columnPersonalFolder" />',
		tooltip: '<spagobi:message key="SBISet.ListRoles.columnPersonalFolderTooltip" />',
		dataIndex: 'PersonalFolder',
		align: 'center'
	});

	var cm = new Ext.grid.ColumnModel([
        {
           header: "<spagobi:message key="SBISet.ListRoles.columnName" />",
           dataIndex: 'Name',
           sortable: true,
           renderer: function (value, p, record) {
     			p.attr = 'ext:qtip="' + record.get('Description') + '"';
     			return value;
           }
        },{
           header: "Type",
           dataIndex: 'Type',
           editor: new Ext.form.ComboBox({
               typeAhead: true,
               editable:false,
               triggerAction: 'all',
               store: roleTypesStore,
               displayField: 'Description',
               mode: 'local',
               listClass: 'x-combo-list-small'
            })
        },
        subObjectsColumn,
        snapshotsColumn,
        viewpointsColumn,
        notesColumn,
        metadataColumn,
        sendMailColumn,
        rememberMeColumn,
        personalFolderColumn,
        {
           header: '<spagobi:message key="SBISet.ListRoles.deleteCaption" />',
           dataIndex: 'DeleteUrl',
           align: 'center'
        }
    ]);
    
    // create the Grid
    var grid = new Ext.grid.EditorGridPanel({
    	cm: cm,
        store: store,
		plugins: [subObjectsColumn, snapshotsColumn, viewpointsColumn, notesColumn, metadataColumn, sendMailColumn, rememberMeColumn, personalFolderColumn],
		viewConfig: {
        	forceFit: true
		},
		width: 1200,
        stripeRows: true,
        collapsible: false
    });
    
    grid.render('renderTo_Roles');
    
});

function saveRoles() {
	var modifiedRecords = store.getModifiedRecords();
	var url = '<%=GeneralUtilities.getSpagoBIProfileBaseUrl(userId)%>';
	url += '&ACTION_NAME=MODIFY_ROLES_ACTION&FIELDS_ORDER=Type,Subojects,Snapshots,Viewpoints,Notes,Metadata,SendMail,RememberMe,PersonalFolder';
	var modifiedRoles = '';
	//for (key in modifiedRecords) {
	//	if (key == 'set' || key == 'get' || key == 'getKeys') continue;
	//	var record = modifiedRecords.get(key);
	for (i = 0; i < modifiedRecords.length; i++) {
		record = modifiedRecords[i];
		var roleId = record.get('Id');
		var roleType = record.get('Type');
		roleType = getRoleTypeCode(roleType);
		modifiedRoles += '' + roleId + ':' + roleType + ',' + record.get('Subojects') + ',' +  record.get('Snapshots') + ',' 
			+ record.get('Viewpoints') + ',' + record.get('Notes') + ',' + record.get('Metadata') + ',' 
			+ record.get('SendMail') + ',' + record.get('RememberMe') + ',' + record.get('PersonalFolder') + ';';
	}
	if (modifiedRoles == '') {
		alert('No modifications found on page');
		return;
	}
	
	url += '&MODIFIED_ROLES=' + modifiedRoles;
	var pars = '';
	Ext.MessageBox.wait('Please wait...', 'Processing');
	Ext.Ajax.request({
		url: url,
		method: 'get',
		success: function (result, request) {
			showMessage(result.responseText);
		},
		params: pars,
		failure: somethingWentWrong
	});

}

function getRoleTypeCode(roleType) {
	<%
  	roleTypesIt = roleTypes.iterator();
  	while (roleTypesIt.hasNext()) {
  		Domain roleType = (Domain) roleTypesIt.next();
  		String roleDescription = msgBuilder.getMessage("SBISet.ListRoles.roleType." + roleType.getValueCd(), request);
  		%>if (roleType == '<%= roleDescription %>') roleType = '<%= roleType.getValueCd() %>';<%
  	}
	%>
	return roleType;
}

function somethingWentWrong() {
	alert('Error while making Ajax request');
}

function showMessage(response) {
	var icon;
	if (response == 'SBISet.ListRoles.saveOk') {
		response = '<spagobi:message key="SBISet.ListRoles.saveOk" />';
		icon = Ext.MessageBox.INFO;
		store.commitChanges();
	}
	if (response == 'SBISet.ListRoles.errorWhileSaving') {
		response = '<spagobi:message key="SBISet.ListRoles.errorWhileSaving" />';
		icon = Ext.MessageBox.ERROR;
	}
	Ext.MessageBox.show({
		title: 'Status',
		msg: response,
		buttons: Ext.MessageBox.OK,
		width:300,
		icon: icon
	});
}
</script>

<%--
// modifiedRecords is an hashmap: the key is the role name, the value is the role row
var modifiedRecords = {
	set : function(foo,bar) {this[foo] = bar;},
	get : function(foo) {return this[foo];},
	getKeys: function() {
		var keys = new Array();
		for (var key in this) {
			if (key == 'set' || key == 'get' || key == 'getKeys') continue;
			keys.push(key);
		}
		return keys;
	}
}
--%>

<%@ include file="/jsp/commons/footer.jsp"%>