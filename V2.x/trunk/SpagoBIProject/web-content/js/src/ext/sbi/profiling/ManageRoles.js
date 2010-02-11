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
	var paramsDel = {MESSAGE_DET: "ROLE_DELETE"};
	
	this.services = new Array();
	this.services['manageRolesList'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'MANAGE_ROLES_ACTION'
		, baseParams: paramsList
	});
	this.services['saveRoleService'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'MANAGE_ROLES_ACTION'
		, baseParams: paramsSave
	});
	
	this.services['deleteRoleService'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'MANAGE_ROLES_ACTION'
		, baseParams: paramsDel
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
	
	//this.rolesStore.on('load', this.onLoad, this);
	this.rolesStore.load();
	
	//Ext.getBody().createChild({tag: 'h2', html: 'Manage Roles'});
	
	this.initManageRoles();
	   
   	Ext.getCmp('rolegrid').store.on('load', function(){
	 var grid = Ext.getCmp('rolegrid');
	 grid.getSelectionModel().selectRow(0);
	 grid.fireEvent('rowclick', grid, 0)
	 }, this, {
	 single: true
   });
   	this.addEvents('delete', 'ready');
}

Ext.extend(Sbi.profiling.ManageRoles, Ext.FormPanel, {
	
	gridForm:null
	, rolesStore:null
	, colModel:null
	,typeData: null
	,buttons: null
	, tabs: null

	
	,initManageRoles: function(){


       this.deleteColumn = new Ext.grid.ButtonColumn({
	       header:  ' ',
	       dataIndex: 'id',
	       iconCls: 'icon-remove',
	       scope: this,
	       clickHandler: function(e, t) {
	          //var index = Ext.getCmp("rolegrid").getView().findRowIndex(t);
	          //var selectedRecord = this.rolesStore.getAt(index);
	          //var roleId = selectedRecord.get('id');
	          alert("dddddddd");
	          //Ext.getCmp("rolegrid").fireEvent('delete', roleId);
	       }
	       ,width: 25
	       ,renderer : function(v, p, record){
	           return '<center><img class="x-mybutton-'+this.id+' grid-button ' +this.iconCls+'" width="16px" height="16px" src="'+Ext.BLANK_IMAGE_URL+'"/></center>';
	       }
       });

       this.colModel = new Ext.grid.ColumnModel([
         {id:'name',header: "name", width: 50, sortable: true, locked:false, dataIndex: 'name'},
         {header: "description", width: 150, sortable: true, dataIndex: 'description'},
         this.deleteColumn
      ]);

   	   //this.typeData =config;
     	   
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
 	   
 	    /*====================================================================
 	     * CheckGroup Is able to
 	     *====================================================================*/

 	    this.checkGroup = {
           xtype:'fieldset'
           ,id: 'checks-form'
           ,columnWidth: 0.8
           ,autoHeight:true
           ,autoWidth: true
           , defaults: {
               anchor: '-20' // leave room for error icon
           }
           ,items :[
				{
		            xtype: 'checkboxgroup',
		            itemId: 'isAbleToSave',
		            columns: 1,
		            boxMinWidth  : 150,
		            boxMinHeight  : 100,
		            hideLabel  : false,
		            fieldLabel: 'Save'
		            ,items: [
		                {boxLabel: 'Personal Folder', name: 'savePersonalFolder', checked:'savePersonalFolder',inputValue: 1},
		                {boxLabel: 'Metadata', name: 'saveMeta', checked:'saveMeta',inputValue: 1},
		                {boxLabel: 'Remember Me', name: 'saveRemember', checked:'saveRemember',inputValue: 1},
		                {boxLabel: 'Subobjects', name: 'saveSubobj', checked:'saveSubobj',inputValue: 1}
		            ]
		        },
		        {
		            xtype: 'checkboxgroup',
		            itemId: 'isAbleToSee',
		            columns: 1,
		            boxMinWidth  : 150,
		            boxMinHeight  : 100,
		            hideLabel  : false,
		            fieldLabel: 'See',
		            items: [
		                {boxLabel: 'Metadata', name: 'seeMeta', checked: 'seeMeta', inputValue: 1},
		                {boxLabel: 'Notes', name: 'seeNotes', checked:'seeNotes',inputValue: 1},
		                {boxLabel: 'Snapshots', name: 'seeSnapshot', checked:'seeSnapshot',inputValue: 1},
		                {boxLabel: 'Subobjects', name: 'seeSubobj', checked:'seeSubobj',inputValue: 1},
		                {boxLabel: 'Viewpoints', name: 'seeViewpoints', checked:'seeViewpoints',inputValue: 1}
		            ]
		        },
		        {
		            xtype: 'checkboxgroup',
		            columns: 1,
		            boxMinWidth  : 150,
		            hideLabel  : false,
		            fieldLabel: 'Send',
		            itemId: 'isAbleToSend',
		            //height:200,
		            items: [
		                {boxLabel: 'Mail', name: 'sendMail', checked:'sendMail',inputValue: 1}
		            ]
		        },
		        {
		            xtype: 'checkboxgroup',
		            columns: 1,
		            boxMinWidth  : 150,
		            hideLabel  : false,
		            fieldLabel: 'Build',
		            itemId: 'isAbleToBuild',
		            //height:200,
		            items: [
		                {boxLabel: 'QBE', name: 'buildQbe', checked:'buildQbe',inputValue: 1}
		            ]
		        }
           ]
 	    };
 	    this.typesStore = new Ext.data.SimpleStore({
 	        fields: ['typeCd'],
 	        data: config,
 	        autoLoad: false
 	    });

 	   
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
		 		   	itemId: 'role-detail',   	              
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
		                 fieldLabel: 'Name',
		                 name: 'name'
		             },{
		                 fieldLabel: 'Description',
		                 name: 'description'
		             },{
		                 fieldLabel: 'Code',
		                 name: 'code'
		             }, {
		            	 name: 'typeCd',
		                  store: this.typesStore,
		                  fieldLabel: 'Role Type',
		                  displayField: 'typeCd',   // what the user sees in the popup
		                  valueField: 'typeCd',        // what is passed to the 'change' event
		                  typeAhead: true,
		                  forceSelection: true,
		                  mode: 'local',
		                  triggerAction: 'all',
		                  selectOnFocus: true,
		                  editable: false,
		                  xtype: 'combo'
		             }]
		    	
		    	}
		    },{
		        title: 'Authorizations'
		        //,html: 'Is Able to'
		        , layout: 'fit'
		        , items: this.checkGroup
		        , itemId: 'checks'
		    }]
		});


   	   /*
   	   *    Here is where we create the Form
   	   */
   	   this.gridForm = new Ext.FormPanel({
   	          id: 'role-form',
   	          frame: true,
   	          labelAlign: 'left',
   	          title: 'Roles management',
   	          buttons: this.addBtn,
   	          bodyStyle:'padding:5px',
   	          width: 750,
   	          layout: 'column',
   	          items: [{
   	              columnWidth: 0.90,
   	              layout: 'fit',
   	              items: {
   	        	  	  id: 'rolegrid',
   	                  xtype: 'grid',
   	                  ds: this.rolesStore,   	                  
   	                  cm: this.colModel,
   	                  sm: new Ext.grid.RowSelectionModel({
   	                      singleSelect: true,
   	                      scope:this,   	                   
	   	                  fillChecks : function(row, rec) {	  
	   	                   	  Ext.getCmp("checks-form").items.each(function(item){	   	                   		  
	   	                   		  if(item.getItemId() == 'isAbleToSave'){
		   	                   		  item.setValue('saveMeta', rec.get('saveMeta'));
		   	                   		  item.setValue('saveRemember', rec.get('saveRemember'));
		   	                   		  item.setValue('saveSubobj', rec.get('saveSubobj'));	   	              
		   	                   		  item.setValue('savePersonalFolder', rec.get('savePersonalFolder'));
	   	                   		  }else if(item.getItemId() == 'isAbleToSee'){
		   	                   		  item.setValue('seeMeta', rec.get('seeMeta'));
		   	                   		  item.setValue('seeNotes', rec.get('seeNotes'));
		   	                   		  item.setValue('seeSnapshot', rec.get('seeSnapshot'));	   	              
		   	                   		  item.setValue('seeSubobj', rec.get('seeSubobj'));
		   	                   		  item.setValue('seeViewpoints', rec.get('seeViewpoints'));
	   	                   		  }else if(item.getItemId() == 'isAbleToSend'){
		   	                   		  item.setValue('sendMail', rec.get('sesendMaileMeta'));
	   	                   		  }else if(item.getItemId() == 'isAbleToBuild'){
		   	                   		  item.setValue('buildQbe', rec.get('buildQbe'));
	   	                   		  }

			   	        	  });
	   	                   	 
	   	                  },
   	                      listeners: {
   	                          rowselect: function(sm, row, rec) {   	  
   	                	  		  this.fillChecks(row, rec);
   	                              Ext.getCmp("role-form").getForm().loadRecord(rec);      	                              
   	                          }
   	                      }
   	                  }),
   	                  autoExpandColumn: 'name',
   	                  height: 350,
   	                  title:'Roles list',
	   	 	   	      tools:[{
	  		   	        id:'plus'
	  		   	        //,iconCls: 'icon-add'
	  		   	        ,qtip: 'New role'
	  		   	        ,handler: this.addNewRole
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
        var lastRec = this.rolesStore.getCount();

        var newRec = this.rolesStore.getAt(lastRec -1 );
        this.rolesStore.addSorted(newRec);
        
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
					this.rolesStore.commitChanges();

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
	, addNewRole : function(){
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

		Ext.getCmp("role-form").getForm().loadRecord(emptyRecToAdd);
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
		Ext.getCmp("role-form").doLayout();
		
		//do not add record to the grid
	   	//var grid = Ext.getCmp('rolegrid');	   	 
	   	//grid.getSelectionModel().getSelected().hide(grid.getSelectionModel().getSelected());

	}
	, fillNewRecord : function(){
        var lastRec = this.rolesStore.getCount();

        var record = this.rolesStore.getAt(lastRec -1 );
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

Ext.reg('manageroles', Sbi.profiling.ManageRoles);
