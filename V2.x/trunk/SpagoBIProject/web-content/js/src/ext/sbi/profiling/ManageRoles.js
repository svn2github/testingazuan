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
	var paramsSave = {MESSAGE_DET: "ROLE_INSERT"};
	this.services = new Array();
	this.services['manageRolesList'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'MANAGE_ROLES_ACTION'
		, baseParams: paramsList
	});
	this.services['saveRoleService'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'MANAGE_ROLES_ACTION'
		, baseParams: paramsSave
	});
	
	this.rolesStore = new Ext.data.JsonStore({
    	autoLoad: false    	
    	,fields: ['id'
    	          , 'name'
    	          , 'description'
    	          , 'typeCd'
    	          , 'savePersonalFolder'
    	          , 'saveMeta'
    	          , 'saveRemember'
    	          , 'saveSubobj'
    	          , 'seeMeta'
    	          , 'seeNotes'
    	          , 'seeSnapshot'
    	          , 'seeSubobj'
    	          , 'seeViewpoints'
    	          , 'sendMail'
    	          , 'buildQbe'
    	          ]
    	, root: 'samples'
		, url: this.services['manageRolesList']
		
	});
	this.rolesStore.load();
	
	//Ext.getBody().createChild({tag: 'h2', html: 'Manage Roles'});
	
	this.initManageRoles();

	

}

Ext.extend(Sbi.profiling.ManageRoles, Ext.FormPanel, {
	
	gridForm:null
	, rolesStore:null
	, colModel:null
	,typeData: null
	,buttons: null
	
	,initManageRoles: function(){

       this.colModel = new Ext.grid.ColumnModel([
          {id:'name',header: "name", width: 50, sortable: true, locked:false, dataIndex: 'name'},
          {header: "description", width: 150, sortable: true, dataIndex: 'description'}
       ]);
   	   this.typeData =[
   	                   ['22', 'USER']
   		               ,['23', 'ADMIN']
   		               ,['24', 'DEV_ROLE']
   		               ,['25', 'TEST_ROLE']
   		               ,['26', 'MODEL_ADMIN']
   		              ];
     	   
 	   this.buttons = [{
        text : 'Save'
	        , scope : this
	        , handler : this.save
	   }];
 	   
 	    /*====================================================================
 	     * CheckGroup example
 	     *====================================================================*/
 	    this.checkGroup = {
           xtype:'fieldset',
           columnWidth: 0.8,
           title: 'Is Able to',
           collapsible: true,
           autoHeight:true,
           autoWidth: true,
           collapsed: true,   // initially collapse the group
           collapsible: true,

           defaults: {
               anchor: '-20' // leave room for error icon
           },

           items :[
				{
		            xtype: 'checkboxgroup',
		            columns: 1,
		            boxMinWidth  : 150,
		            boxMinHeight  : 100,
		            hideLabel  : false,
		            fieldLabel: 'Save',
		            items: [
		                {boxLabel: 'Personal Folder', name: 'savePersonalFolder'},
		                {boxLabel: 'Metadata', name: 'saveMeta'},
		                {boxLabel: 'Remember Me', name: 'saveRemember'},
		                {boxLabel: 'Subobjects', name: 'saveSubobj'}
		            ]
		        },
		        {
		            xtype: 'checkboxgroup',
		            columns: 1,
		            boxMinWidth  : 150,
		            boxMinHeight  : 100,
		            hideLabel  : false,
		            fieldLabel: 'See',
		            items: [
		                {boxLabel: 'Metadata', name: 'seeMeta'},
		                {boxLabel: 'Notes', name: 'seeNotes'},
		                {boxLabel: 'Snapshots', name: 'seeSnapshot'},
		                {boxLabel: 'Subobjects', name: 'seeSubobj'},
		                {boxLabel: 'Viewpoints', name: 'seeViewpoints'}
		            ]
		        },
		        {
		            xtype: 'checkboxgroup',
		            columns: 1,
		            boxMinWidth  : 150,
		            hideLabel  : false,
		            fieldLabel: 'Send',
		            //height:200,
		            items: [
		                {boxLabel: 'Mail', name: 'sendMail'}
		            ]
		        },
		        {
		            xtype: 'checkboxgroup',
		            columns: 1,
		            boxMinWidth  : 150,
		            hideLabel  : false,
		            fieldLabel: 'Build',
		            //height:200,
		            items: [
		                {boxLabel: 'QBE', name: 'buildQbe'}
		            ]
		        }
           ]
 	    };

   	   
   	   /*
   	   *    Here is where we create the Form
   	   */
   	   this.gridForm = new Ext.FormPanel({
   	          id: 'role-form',
   	          frame: true,
   	          labelAlign: 'left',
   	          title: 'Roles',
   	          bodyStyle:'padding:5px',
   	          width: 750,
   	          layout: 'column',    
   	          items: [{
   	              columnWidth: 0.60,
   	              layout: 'fit',
   	              items: {
   	                  xtype: 'grid',
   	                  ds: this.rolesStore,
   	                  cm: this.colModel,
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
   	                      } 
   	                  }
   	              }
   	          },{
   	              columnWidth: 0.4,
   	              xtype: 'fieldset',
   	              labelWidth: 90,
   	              title:'Role details',
   	              defaults: {width: 140, border:false},    
   	              defaultType: 'textfield',
   	              autoHeight: true,
   	              bodyStyle: Ext.isIE ? 'padding:0 0 5px 15px;' : 'padding:10px 15px;',
   	              border: false,
   	              style: {
   	                  "margin-left": "10px", 
   	                  "margin-right": Ext.isIE6 ? (Ext.isStrict ? "-10px" : "-13px") : "0"  
   	              },
   	              items: [{
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
   	            	                'typeId' , 
   	            	                'typeCd' 
   	            	            ]
   	            	        ,data: this.typeData
   	            	  })
   	              },
   	              this.checkGroup]
   	          }],
   	          buttons: this.buttons,
   	          buttonAlign: 'right',
   	          renderTo: Ext.getBody()
   	      });
	
	}
	,save : function() {

        var modifiedRecords = new Array();
        modifiedRecords = modifiedRecords.concat(this.rolesStore.getModifiedRecords());

        var modifiedRole = new Array();
        for (var i = 0; i < modifiedRecords.length; i++) {
        	modifiedRole.push(modifiedRecords[i].data);
        }
        var params = {
            ROLE: Ext.util.JSON.encode(modifiedRole)
        };
        
       
        Ext.Ajax.request({
            url: this.services['saveRoleService'],
            success: function(response, options) {
				if (response !== undefined) {
					Ext.MessageBox.hide();
					this.rolesStore.commitChanges();

				} else {
					Sbi.exception.ExceptionHandler.showErrorMessage('Error while saving Role', 'Service Error');
				}
            },
            failure: Sbi.exception.ExceptionHandler.handleFailure,    
            scope: this,
            params: params
        });
    }
});

Ext.reg('manageroles', Sbi.profiling.ManageRoles);
