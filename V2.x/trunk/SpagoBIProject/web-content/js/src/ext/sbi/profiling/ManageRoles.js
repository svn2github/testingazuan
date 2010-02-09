/**
 * SpagoBI - The Business Intelligence Free Platform
 *
 * Copyright (C) 2004 - 2008 Engineering Ingegneria Informatica S.p.A.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 **/

/**
 * Object name
 * 
 * [description]
 * 
 * 
 * Public Properties
 * 
 * [list]
 * 
 * 
 * Public Methods
 * 
 * [list]
 * 
 * 
 * Public Events
 * 
 * [list]
 * 
 * Authors - Monica Franceschini (monica.franceschini@eng.it)
 */
Ext.ns("Sbi.profiling");

Sbi.profiling.ManageRoles = function(config) { 
	var paramsList = {MESSAGE_DET: "ROLES_LIST"};
	this.services = new Array();
	this.services['manageRolesList'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'MANAGE_ROLES_ACTION'
		, baseParams: paramsList
	});
	
    rolesStore = new Ext.data.JsonStore({
    	autoLoad: false    	
    	,fields: ['id', 'name', 'description', 'typeCd']
    	, root: 'samples'
		, url: this.services['manageRolesList']
		
    });
    
    rolesStore.load();

	   // the DefaultColumnModel expects this blob to define columns. It can be extended to provide
	   // custom or reusable ColumnModels
	   var colModel = new Ext.grid.ColumnModel([
	       {id:'name',header: "name", width: 50, sortable: true, locked:false, dataIndex: 'name'},
	       {header: "description", width: 150, sortable: true, dataIndex: 'description'}
	   ]);

	   Ext.getBody().createChild({tag: 'h2', html: 'Manage Roles'});
  	   var typeData =[
	                   ['22', 'USER']
		               ,['23', 'ADMIN']
		               ,['24', 'DEV_ROLE']
		               ,['25', 'TEST_ROLE']
		               ,['26', 'MODEL_ADMIN']
		              ];
	   
	   /*
	   *    Here is where we create the Form
	   */
	   var gridForm = new Ext.FormPanel({
	          id: 'role-form',
	          frame: true,
	          labelAlign: 'left',
	          title: 'Roles',
	          bodyStyle:'padding:5px',
	          width: 750,
	          layout: 'column',    // Specifies that the items will now be arranged in columns
	          items: [{
	              columnWidth: 0.60,
	              layout: 'fit',
	              items: {
	                  xtype: 'grid',
	                  ds: rolesStore,
	                  cm: colModel,
	                  sm: new Ext.grid.RowSelectionModel({
	                      singleSelect: true,
	                      listeners: {
	                          rowselect: function(sm, row, rec) {
	                              Ext.getCmp("role-form").getForm().loadRecord(rec);
	                          }
	                      }
	                  }),
	                  autoExpandColumn: 'name',
	                  height: 350,
	                  title:'Roles',
	                  border: true,
	                  listeners: {
	                      viewready: function(g) {
	                          g.getSelectionModel().selectRow(0);
	                      } // Allow rows to be rendered.
	                  }
	              }
	          },{
	              columnWidth: 0.4,
	              xtype: 'fieldset',
	              labelWidth: 90,
	              title:'Role details',
	              defaults: {width: 140, border:false},    // Default config options for child items
	              defaultType: 'textfield',
	              autoHeight: true,
	              bodyStyle: Ext.isIE ? 'padding:0 0 5px 15px;' : 'padding:10px 15px;',
	              border: false,
	              style: {
	                  "margin-left": "10px", // when you add custom margin in IE 6...
	                  "margin-right": Ext.isIE6 ? (Ext.isStrict ? "-10px" : "-13px") : "0"  // you have to adjust for it somewhere else
	              },
	              items: [{
	                  fieldLabel: 'ID',
	                  name: 'id'
	              },{
	                  fieldLabel: 'Name',
	                  name: 'name'
	              },{
	                  fieldLabel: 'Description',
	                  name: 'description'
	              },{
	                  xtype: 'combo',
	                  columns: 'auto',
	                  fieldLabel: 'Role Type',
	                  name: 'typeCd',	                  
	                  store: new Ext.data.SimpleStore({
	            	         id:0
	            	        ,fields:
	            	            [
	            	                'typeId',   //numeric value is the key
	            	                'typeCd' //the text value is the value
	            	            ]
	            	        ,data: typeData
	            	  })
	              }]
	          }],
	          buttons: [{
	              text: 'Submit',
	              handler: function() {
		        	  gridForm.getForm().getEl().dom.action = 'XXX';
		        	  gridForm.getForm().getEl().dom.method = 'GET';
		        	  gridForm.getForm().submit();
	              }
	          }],

	          renderTo: Ext.getBody()
	      });
}

Ext.extend(profiling.ManageRoles, Ext.FormPanel, {

});

Ext.reg('manageroles', Sbi.profiling.ManageRoles);
