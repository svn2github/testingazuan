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
 * Authors - Chiara Chiarelli
 */
Ext.ns("Sbi.profiling");

Sbi.profiling.ManageUsers = function(config) { 
	
	var paramsList = {MESSAGE_DET: "USERS_LIST"};
	var paramsSave = {MESSAGE_DET: "USER_INSERT"};
	var paramsDel = {MESSAGE_DET: "USER_DELETE"};
	
	this.services = new Array();
	this.services['manageUsersList'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'MANAGE_USER_ACTION'
		, baseParams: paramsList
	});
	this.services['saveUserService'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'MANAGE_USER_ACTION'
		, baseParams: paramsSave
	});
	
	this.services['deleteUserService'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'MANAGE_USER_ACTION'
		, baseParams: paramsDel
	});
	
	this.usersStore = new Ext.data.JsonStore({
    	autoLoad: false    	
    	,fields: ['userId'
    	          , 'fullName'
    	          , 'userRoles'
    	          , 'userAttributes'
    	          ]
    	, root: 'samples'
		, url: this.services['manageUsersList']
		
	});
	
	this.usersStore.load();
	
	this.initManageUsers();
	   
   	Ext.getCmp('usergrid').store.on('load', function(){
	 var grid = Ext.getCmp('usergrid');
	 grid.getSelectionModel().selectRow(0);
	 grid.fireEvent('rowclick', grid, 0);
	 }, this, {
	 single: true
   });
	
}

Ext.extend(Sbi.profiling.ManageUsers, Ext.FormPanel, {
	gridForm:null
	, usersStore:null
	, colModel:null
	, typeData: null
	, buttons: null
	, tabs: null

	
	,initManageUsers: function(){


       this.deleteColumn = new Ext.grid.ButtonColumn({
	       header:  ' ',
	       dataIndex: 'id',
	       iconCls: 'icon-remove',
	       clickHandler: function(e, t) {
	       		alert('Son dentro');
	          var index = Ext.getCmp("usergrid").getView().findRowIndex(t);
	          var selectedRecord = this.usersStore.getAt(index);
	          var roleId = selectedRecord.get('id');
	          Ext.getCmp("usergrid").fireEvent('deleteRoleService', roleId);
	       }
	       ,width: 25
	       ,renderer : function(v, p, record){
	           return '<center><img class="x-mybutton-'+this.id+' grid-button ' +this.iconCls+'" width="16px" height="16px" src="'+Ext.BLANK_IMAGE_URL+'"/></center>';
	       }
       });
       
       this.colModel = new Ext.grid.ColumnModel([
        {id:'userId', header: "User ID", width: 150, sortable: true, dataIndex: 'userId'},
         {header: "Full Name", width: 150, sortable: true, dataIndex: 'fullName'},
         this.deleteColumn
       ]);
     	   
 	   this.buttons = [{
        text : 'Save'
	        , scope : this
	        , handler : this.save
	        , listeners:{
	        	click: {
	        		fn: this.fillNewRecord,
	        		scope: this
	        	}
 	   		}
	   }];

 	   
 	   this.tabs = new Ext.TabPanel({
           enableTabScroll : true
           , activeTab : 0
           , autoScroll : true
           , width: 320
           , height: 350
           , itemId: 'tabs'
		   , items: [{
		        title: 'Details'
		        , itemId: 'detail'
		        , layout: 'fit'
		        , items: {
		 		   	itemId: 'user-detail',   	              
		 		   	columnWidth: 0.4,
		             xtype: 'fieldset',
		             labelWidth: 90,
		             defaults: {width: 140, border:false},    
		             defaultType: 'textfield',
		             autoHeight: true,
		             autoScroll  : true,
		             bodyStyle: Ext.isIE ? 'padding:0 0 5px 15px;' : 'padding:10px 15px;',
		             border: false,
		             style: {
		                 "margin-left": "10px", 
		                 "margin-right": Ext.isIE6 ? (Ext.isStrict ? "-10px" : "-13px") : "0"  
		             },
		             items: [{
		                 fieldLabel: 'UserID',
		                 name: 'userId'
		             },{
		                 fieldLabel: 'FullName',
		                 name: 'fullName'
		             }]
		    	
		    	}
		    },{
		        title: 'Roles'
		        //,html: 'Is Able to'
		        , layout: 'fit'
		        //, items: this.checkGroup
		        , itemId: 'roles'
		    },{
		        title: 'Attributes'
		        //,html: 'Is Able to'
		        , layout: 'fit'
		        //, items: this.checkGroup
		        , itemId: 'attributes'
		    }]
		});


   	   /*
   	   *    Here is where we create the Form
   	   */
   	   this.gridForm = new Ext.FormPanel({
   	          id: 'user-form',
   	          frame: true,
   	          labelAlign: 'left',
   	          title: 'Users Management',
   	          buttons: this.addBtn,
   	          bodyStyle:'padding:5px',
   	          width: 750,
   	          layout: 'column',
   	          items: [{
   	              columnWidth: 0.90,
   	              layout: 'fit',
   	              items: {
   	        	  	  id: 'usergrid',
   	                  xtype: 'grid',
   	                  ds: this.usersStore,   	                  
   	                  cm: this.colModel,
   	                  plugins: this.deleteColumn,
   	                  sm: new Ext.grid.RowSelectionModel({
   	                      singleSelect: true,
   	                      scope:this,   	                   
	   	                  //fillChecks : function(row, rec) {	  
	   	                   	  
	   	                 // },
   	                      listeners: {
   	                          rowselect: function(sm, row, rec) {   	  
   	                	  		  //this.fillChecks(row, rec);
   	                              Ext.getCmp("user-form").getForm().loadRecord(rec);      	                              
   	                          }
   	                      }
   	                  }),
   	                  //autoExpandColumn: 'fullName',
   	                  height: 350,
   	                  title:'Users list',
	   	 	   	      tools:[{
	  		   	        id:'plus'
	  		   	        //,iconCls: 'icon-add'
	  		   	        ,qtip: 'New User'
	  		   	        ,handler: this.addNewUser
	  		   	        ,scope: this
	   	 	   	      },],
   	                  border: true,
   	                  listeners: {
   	                      viewready: function(g) {
   	                          g.getSelectionModel().selectRow(0);
   	                      } 
   	                  }
   	              }
   	          }, this.tabs
   	          ],

   	          buttons: this.buttons,
   	          buttonAlign: 'right',
   	          renderTo: Ext.getBody()
   	      });

	}
	,save : function() {
        //get the last record
        var lastRec = this.usersStore.getCount();

        var newRec = this.usersStore.getAt(lastRec -1 );
        this.usersStore.addSorted(newRec);
        
        var newRole = new Array();
        newRole.push(newRec.data);

        var params = {
            ROLE: Ext.util.JSON.encode(newRole)
        };
        
       
        Ext.Ajax.request({
            url: this.services['saveRoleService'],
            params: params,
            method: 'GET',
            success: function(response, options) {
				if (response !== undefined) {
					Ext.MessageBox.hide();
					this.usersStore.commitChanges();

				} else {
					Sbi.exception.ExceptionHandler.showErrorMessage('Error while saving Role', 'Service Error');
				}
            },
            failure: function() {
                Ext.MessageBox.show({
                    title: 'Error',
                    msg: 'Error on Saving Role',
                    width: 150,
                    buttons: Ext.MessageBox.OK
               });
            }
        });
    }
	, addNewUser : function(){
    	var emptyRecToAdd =new Ext.data.Record({name:'', 
    										label:'', 
    										description:'',
    										typeCd:'',
    										code:'',
    										saveSubobj: false,
    										seeSubobj:false,
    										seeViewpoints:false,
    										seeSnapshot:false,
    										seeNotes:false,
    										sendMail:false,
    										savePersonalFolder:false,
    										saveRemember:false,
    										seeMeta:false,
    										saveMeta:false,
    										buildQbe:false
    										});
    	//this.rolesStore.addSorted(emptyRecToAdd);

		Ext.getCmp("user-form").getForm().loadRecord(emptyRecToAdd);
        this.tabs.items.each(function(item)
        {		
        	if(item.getItemId() == 'checks'){

        		item.items.each(function(itemTab){

        			itemTab.items.each(function(item1){

        				item1.setValue({
							'saveSubobj': false,
							'seeSubobj':false,
							'seeViewpoints':false,
							'seeSnapshot':false,
							'seeNotes':false,
							'sendMail':false,
							'savePersonalFolder':false,
							'saveRemember':false,
							'seeMeta':false,
							'saveMeta':false,
							'buildQbe':false
        				});

        			});
        		});
        		
        	}
        	item.doLayout();
        });   
		Ext.getCmp("user-form").doLayout();
		
		//do not add record to the grid
	   	//var grid = Ext.getCmp('usergrid');	   	 
	   	//grid.getSelectionModel().getSelected().hide(grid.getSelectionModel().getSelected());

	}
	, fillNewRecord : function(){
        var lastRec = this.usersStore.getCount();

        var record = this.usersStore.getAt(lastRec -1 );
        var values = this.gridForm.getForm().getValues();
        var name =values['name'];
        var descr =values['description'];
        var typecd =values['typeCd'];
        var code =values['code'];
        var savePf =values['savePersonalFolder'];
        var saveSo =values['saveSubobj'];
        var seeSo =values['seeSubobj'];
        var seeV =values['seeViewpoints'];
        var seeSn =values['seeSnapshot'];
        var seeN =values['seeNotes'];
        var sandM =values['sendMail'];
        var saveRe =values['saveRemember'];
        var seeMe =values['seeMeta'];
        var saveMe =values['saveMeta'];
        var builQ =values['buildQbe'];
        
        
     // set the value (shows dirty flag):
        record.set('name', name);
        record.set('description', descr);
        record.set('code', code);
        record.set('typeCd', typecd);
        if(savePf == 1){
        	record.set('savePersonalFolder', true);
        }
        if(saveSo == 1){
        	record.set('saveSubobj', true);
        }
        if(seeSo == 1){
        	record.set('seeSubobj', true);
        }
        if(seeV == 1){
        	record.set('seeViewpoints', true);
        }
        if(seeSn == 1){
        	record.set('seeSnapshot', true);
        }
        if(seeN == 1){
        	record.set('seeNotes', true);
        }
        if(sandM == 1){
        	record.set('sendMail', true);
        }
        if(saveRe == 1){
        	record.set('saveRemember', true);
        }
        if(seeMe == 1){
        	record.set('seeMeta', true);
        }
        if(saveMe == 1){
        	record.set('saveMeta', true);
        }
        if(builQ == 1){
        	record.set('buildQbe', true);
        }
        if(savePf == 1){
        	record.set('savePersonalFolder', true);
        }

        // commit the change (removes dirty flag):
        record.commit();		
	}
	, deleteSelectedRole: function() {
		var recordSelected = this.getSelectionModel().getSelected();
		if (recordsSelected) {
			var id = recordSelected.get('id');
			
			Ext.Ajax.request({
		        url: this.services['deleteRoleService'],
		        params: {'SBI_EXECUTION_ID': this.executionInstance.SBI_EXECUTION_ID, 'id': id},
		        success : function(response, options) {
		      		if (response && response.responseText !== undefined) {
		      			var content = Ext.util.JSON.decode( response.responseText );
		      			if (content !== undefined && content.result == 'OK') {
			  	  			// removes the subobjects from the store
			  	  			for (var count = 0; count < recordsSelected.length; count++) {
			  	  				this.subObjectsStore.remove(recordsSelected[count]);
			  	  			}
		      			} else {
			      			Sbi.exception.ExceptionHandler.showErrorMessage('Error while deleting role', 'Service Error');
			      		}
		      		} else {
		      			Sbi.exception.ExceptionHandler.showErrorMessage('Error while deleting role', 'Service Error');
		      		}
		        },
		        scope: this,
				failure: Sbi.exception.ExceptionHandler.handleFailure      
			});
		} else {
			Sbi.exception.ExceptionHandler.showWarningMessage('Operation failed', 'Warning');
		}
	}
  

});

Ext.reg('manageusers', Sbi.profiling.ManageUsers);
