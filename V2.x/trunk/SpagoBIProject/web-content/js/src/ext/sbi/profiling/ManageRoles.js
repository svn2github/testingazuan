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
	       header:  'DELETE',
	       dataIndex: 'id',
	       iconCls: 'icon-remove',
	       clickHandler: function(e, t) {
	          var index = this.grid.getView().findRowIndex(t);
	          var selectedRecord = this.grid.rolesStore.getAt(index);
	          var roleId = selectedRecord.get('id');
	          this.grid.fireEvent('deleteRoleService', roleId);
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
   	   //this.colModel.push(this.deleteColumn);

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

 	   
 	   this.tabs = new Ext.TabPanel({
           enableTabScroll : true
           , activeTab : 0
           , autoScroll : true
           , width: 320
           , height: 350
           , itemId: 'tabs'
		   , items: [{
		        title: 'Role detail'
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
		             }]
		    	
		    	}
		    },{
		        title: 'Is Able to'
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
   	          title: 'Manage Roles',
   	          buttons: this.addBtn,
   	          bodyStyle:'padding:5px',
   	          width: 750,
   	          layout: 'column',
   	          items: [{
   	              columnWidth: 0.90,
   	              layout: 'fit',
   	              items: {
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
	   	                   			  /*
	   	                   			  item.items.each(function(check){
	   	                   				  alert(check.getName());
	   	                   				  check.setValue(check.getName(), rec.get(check.getName()));
	   	                   			  });
	   	                   			  */
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
   	                              //Ext.getCmp("role-form").getForm().disable();
   	                              
   	                          }
   	                      }
   	                  }),
   	                  autoExpandColumn: 'name',
   	                  height: 350,
   	                  title:'Roles',
	   	 	   	      tools:[{
	  		   	        id:'plus'
	  		   	        ,qtip: 'New role'
	  		   	        ,handler: this.addRole
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
	, addRole : function(){
		//Ext.getCmp("role-form").getForm().enable();
        this.tabs.items.each(function(item)
        {	//cleans every field of both tabs	
        	if(item.getItemId() == 'detail'){
        		
        		item.items.each(function(itemTab){
	        			        		
	                var  data={};
	                itemTab.items.each(function(itemform)
	                {
	                	//alert(itemform.getItemId());
	                	itemform.setValue(null);
	                });
        		});
        	}else{
        		//checkboxes
        		item.items.each(function(itemTab){
        			itemTab.items.each(function(item1){
		                var  data={};
		                item1.items.each(function(itemform)
		                {
		                	itemform.setValue(null);
		                });
        			});
        		});
        		
        	}
        	item.doLayout();
        });
        
	}

	
	
});

Ext.reg('manageroles', Sbi.profiling.ManageRoles);
