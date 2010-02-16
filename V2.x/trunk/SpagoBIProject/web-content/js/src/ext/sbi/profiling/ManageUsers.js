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
	var paramsList = { MESSAGE_DET: "USERS_LIST"};
	var paramsSave = {LIGHT_NAVIGATOR_DISABLED: 'TRUE', MESSAGE_DET: "USER_INSERT"};
	var paramsDel = {LIGHT_NAVIGATOR_DISABLED: 'TRUE', MESSAGE_DET: "USER_DELETE"};
	
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
	
	this.initStores(config);
	
	this.initManageUsers();
	   
   	Ext.getCmp('usergrid').store.on('load', function(){
	 var grid = Ext.getCmp('usergrid');
	 grid.getSelectionModel().selectRow(0);
	 grid.fireEvent('rowclick', grid, 0);
	 }, this, {
	 single: true
   });
   
   	Ext.getCmp('usergrid').on('delete', this.deleteSelectedUser, this);
	
}

Ext.extend(Sbi.profiling.ManageUsers, Ext.FormPanel, {
	gridForm:null
	, usersStore:null
	, colModel:null
	, typeData: null
	, buttons: null
	, tabs: null
	, attributesGridPanel: null
	, attributesStore: null
	, smRoles: null
	, rolesGrid: null
	, rolesStore: null
	, rolesEmptyStore: null
	, attributesEmptyStore: null

	, initStores: function (config) {
	
		this.usersStore = new Ext.data.JsonStore({
	    	autoLoad: false    	
	    	,fields: ['userId'
	    			  , 'id'
	    	          , 'fullName'
	    	          , 'pwd'
	    	          , 'userRoles'
	    	          , 'userAttributes'
	    	          ]
	    	, root: 'samples'
			, url: this.services['manageUsersList']			
		});
		
		this.usersStore.load();
		
		this.attributesStore = new Ext.data.SimpleStore({
	        fields : [ 'id', 'name', 'value' ]
	    });
	    
	    this.rolesStore = new Ext.data.SimpleStore({
	    	id: 'id',
	        fields : [ 'id', 'name', 'description', 'checked' ]
	    });

	    
	    this.attributesEmptyStore = config.attributesEmpyList;
	    this.rolesEmptyStore = config.rolesEmptyList;
	    
    }
	
	,initManageUsers: function(){
		
	   this.initAttributesGridPanel();
	   this.initRolesGridPanel();
		   
	   //alert("initManageUsers");
       this.deleteColumn = new Ext.grid.ButtonColumn({
	       header:  ' ',
	       dataIndex: 'id',
	       iconCls: 'icon-remove',
	       clickHandler: function(e, t) {
	          var index = Ext.getCmp("usergrid").getView().findRowIndex(t);
	          
	          var selectedRecord = Ext.getCmp("usergrid").store.getAt(index);
	          var userId = selectedRecord.get('id');
	          Ext.getCmp("usergrid").fireEvent('delete', userId, index);
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
	   }];
	   


 	   this.tabs = new Ext.TabPanel({
           enableTabScroll : true
           , activeTab : 0
           , autoScroll : true
           , width: 450
           , height: 450
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
		                 name: 'id',
		                 hidden: true
		             },{
		                 fieldLabel: 'User ID',
		                 name: 'userId'
		             },{
		                 fieldLabel: 'Full Name',
		                 name: 'fullName'
		             },{
		                 fieldLabel: 'Password',
		                 name: 'pwd',
		                 inputType: 'password'
		             },{
		                 fieldLabel: 'Confirm Password',
		                 name: 'confirmpwd',
		                 inputType: 'password'
		             }]
		    	
		    	}
		    },{
		        title: 'Roles'
		        , id : 'rolesList'
		        //,html: 'Is Able to'
		        , layout: 'fit'
		        , items: [this.rolesGrid]
		        , itemId: 'roles'
		    },{
		        title: 'Attributes'
		        , id : 'attrList'
	            , items : [ this.attributesGridPanel ]
	           // , autoWidth : true
	           // , autoHeight : true
		        //, layout: 'fit'
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
   	          width: 850,
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
	   	                  fillRoles : function(row, rec) {	 
							Ext.getCmp("roles-form").store.removeAll();
	   	                   	var tempArr = rec.data.userRoles;
	   	                  	var length = rec.data.userRoles.length;
	   	                  	for(var i=0;i<length;i++){
	   	                  		var tempRecord = new Ext.data.Record({"description":tempArr[i].description, "name":tempArr[i].name, "id":tempArr[i].id });
							    Ext.getCmp("roles-form").store.addSorted(tempRecord);
							    Ext.getCmp("roles-form").store.commitChanges();
							    Ext.getCmp("roles-form").selModel.unlock();
							    if(tempArr[i].checked){
							    	var roleId = tempRecord.get('id');							    	
							    	Ext.getCmp("roles-form").fireEvent('recToSelect', roleId, i);
							    }

	   	                  	}	
	   	                  	
	   	                  },
	   	                  fillAttributes : function(row, rec) {	 
	   	                    Ext.getCmp("attributes-form").store.removeAll();
	   	                  	var tempArr = rec.data.userAttributes;
	   	                  	var length = rec.data.userAttributes.length;
	   	                  	for(var i=0;i<length;i++){
	   	                  		var tempRecord = new Ext.data.Record({"value":tempArr[i].value,"name":tempArr[i].name,"id":tempArr[i].id });
							    Ext.getCmp("attributes-form").store.add(tempRecord);	
	   	                  	}			        
 
	   	                  },
   	                      listeners: {
   	                          rowselect: function(sm, row, rec) {   
   	                              Ext.getCmp("user-form").getForm().loadRecord(rec);  	
   	                          	  this.fillRoles(row, rec);  
   	                	  		  this.fillAttributes(row, rec);
   	                                  	                              
   	                          }
   	                      }
   	                  }),
   	                  //autoExpandColumn: 'fullName',
   	                  height: 450,
   	                  title:'Users list',
	   	 	   	      tools:[{
	  		   	        id:'plusprofile'
	  		   	        ,iconCls: 'icon-add'
	  		   	        ,qtip: 'New User'
	  		   	        ,handler: this.addNewUser
	  		   	        ,scope: this
	   	 	   	      },],
   	                  border: true
  	                 ,listeners: {
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
	
	, initAttributesGridPanel : function() {
        
        this.attributesGridPanel = new Ext.grid.EditorGridPanel({
            id: 'attributes-form',
            store : this.attributesStore,
            autoHeight : true,
            columns : [ {          	
                header : 'Name',
                width : 75,
                sortable : true,
                dataIndex : 'name'
            }, {           	
                header : 'Value',
                width : 75,
                sortable : true,
                dataIndex : 'value',
                editor : new Ext.form.TextField({}) 
            } ],
            viewConfig : {
                forceFit : true,
                scrollOffset : 2
            // the grid will never have scrollbars
            },
            singleSelect : true,
            clicksToEdit : 2
        });

    }
    
    , initRolesGridPanel : function() {
       
    	this.smRoles = new Ext.grid.CheckboxSelectionModel( {header: ' ',singleSelect: false, scope:this, dataIndex: 'id'} );
		this.smRoles.on('rowselect', this.onRoleSelect, this);
		this.smRoles.on('rowdeselect', this.onRoleDeselect, this);
		
        this.cmRoles = new Ext.grid.ColumnModel([
	         //{id:'id',header: "id", dataIndex: 'id'},
	         {header: "name", width: 45, sortable: true, dataIndex: 'name'},
	         {header: "description", width: 65, sortable: true, dataIndex: 'description'}
	         ,this.smRoles 
	    ]);

		this.rolesGrid = new Ext.grid.GridPanel({
			  store: this.rolesStore
			, id: 'roles-form'
   	     	, cm: this.cmRoles
   	     	, sm: this.smRoles
   	     	, frame: false
   	     	, border:false  
   	     	, collapsible:false
   	     	, loadMask: true
   	     	, viewConfig: {
   	        	forceFit:true
   	        	, enableRowBody:true
   	        	, showPreview:true
   	     	}
			, scope: this
		});
		this.rolesGrid.superclass.constructor.call(this);

		
		Ext.getCmp("roles-form").on('recToSelect', function(roleId, index){
			//alert(roleId);
			//alert(index);
			
			Ext.getCmp("roles-form").selModel.selectRow(index);
		});

	}
	
	,onRoleSelect: function(){
	}
	
	,onRoleDeselect: function(){
	
	}
	
	,save : function() {
		   
	   var values = this.gridForm.getForm().getValues();

       if(values['pwd']===values['confirmpwd']){
	      	
			var params = {
	        	userId : values['userId'],
	        	fullName : values['fullName'],
	        	pwd : values['pwd']            
	        }
	        if(values['id'] !== null && values['id'] !== undefined ){
	        	params.id = values['id'];
	        }
	        
	        var rolesSelected = Ext.getCmp("roles-form").selModel.getSelections();
	        var lengthR = rolesSelected.length;
            var roles =new Array();
            for(var i=0;i<lengthR;i++){
             	var role ={'name':rolesSelected[i].get("name"),'id':rolesSelected[i].get("id"),'description':rolesSelected[i].get("description")};
 				roles.push(role);
           }
	        params.userRoles =  Ext.util.JSON.encode(roles);
	        
	        var modifAttributes = this.attributesStore.getModifiedRecords();
            var lengthA = modifAttributes.length;
            var attrs =new Array();
            for(var i=0;i<lengthA;i++){
             	var attr ={'name':modifAttributes[i].get("name"),'id':modifAttributes[i].get("id"),'value':modifAttributes[i].get("value")};
 				attrs.push(attr);
           }
	        params.userAttributes =  Ext.util.JSON.encode(attrs);
	        
	        var newRec =new Ext.data.Record({'userId': values['userId'],'fullName': values['fullName'],'pwd':values['pwd']});
	        
	        Ext.Ajax.request({
	            url: this.services['saveUserService'],
	            params: params,
	            method: 'GET',
	            success: function(response, options) {
					if (response !== undefined) {		
			      		if(response.responseText !== undefined) {
			      			var content = Ext.util.JSON.decode( response.responseText );
			      			alert(content.toSource());
			      			
			      			if(content.responseText !== 'Operation succeded') {
			                    Ext.MessageBox.show({
			                        title: 'Error',
			                        msg: content,
			                        width: 150,
			                        buttons: Ext.MessageBox.OK
			                   });
			      			}else{
								var idTemp = content.id;
								newRec.set('id', idTemp);
								
								this.usersStore.add(newRec);
								this.usersStore.commitChanges();
								this.attributesStore.commitChanges();
			      			}
			      		} else {
			      			Sbi.exception.ExceptionHandler.showErrorMessage('Server response is empty', 'Service Error');
			      		}
					} else {
						Sbi.exception.ExceptionHandler.showErrorMessage('Error while saving User', 'Service Error');
					}
	            },
	            failure: function(response) {
		      		if(response.responseText !== undefined) {
		      			var content = Ext.util.JSON.decode( response.responseText );
		      			var errMessage ='';
						for (var count = 0; count < content.errors.length; count++) {
							var anError = content.errors[count];
		        			if (anError.localizedMessage !== undefined && anError.localizedMessage !== '') {
		        				errMessage += anError.localizedMessage;
		        			} else if (anError.message !== undefined && anError.message !== '') {
		        				errMessage += anError.message;
		        			}
		        			if (count < content.errors.length - 1) {
		        				errMessage += '<br/>';
		        			}
						}
	
		                Ext.MessageBox.show({
		                    title: 'Validation Error',
		                    msg: errMessage,
		                    width: 400,
		                    buttons: Ext.MessageBox.OK
		               });
		      		}else{
		                Ext.MessageBox.show({
		                    title: 'Error',
		                    msg: 'Error in Saving User',
		                    width: 150,
		                    buttons: Ext.MessageBox.OK
		               });
		      		}
	            }
	            ,scope: this
       		 });
			
		}else{
			alert('Password and Confirm Password fields must be equal!')			
		}
	
    }
	, addNewUser : function(){

		var emptyRecToAdd =new Ext.data.Record({userId:'', 
											fullName:'', 
											pwd:'',
											userRoles:'',
											userAttributes:'',
											id: 0
											});
	
		Ext.getCmp('user-form').getForm().loadRecord(emptyRecToAdd);
		
		Ext.getCmp("attributes-form").store.removeAll();
	   	var tempAttrArr = this.attributesEmptyStore;
	   	var length = this.attributesEmptyStore.length;
        for(var i=0;i<length;i++){
        	var tempRecord = new Ext.data.Record({"value":tempAttrArr[i].value,"name":tempAttrArr[i].name,"id":tempAttrArr[i].id });
    		Ext.getCmp("attributes-form").store.add(tempRecord);	
        }		
        
        Ext.getCmp("roles-form").store.removeAll();
        var tempRolesArr = this.rolesEmptyStore;
        var length2 = this.rolesEmptyStore.length;
        for(var i=0;i<length2;i++){
          	var tempRecord = new Ext.data.Record({"description":tempRolesArr[i].description,"name":tempRolesArr[i].name,"id":tempRolesArr[i].id });
			Ext.getCmp("roles-form").store.add(tempRecord);								   
        }	
        
        // this.smRoles.selectFirstRow();
       // this.rolesGrid.getSelectionModel().selectFirstRow();
       //this.rolesGrid.getColumnModel().getColumnById('2').selectRow(2);
        //alert(this.rolesGrid.getColumnModel().getColumnById('2').selectRow(2)) ;
       // this.smRoles.selectFirstRow();
		
		Ext.getCmp('user-form').doLayout();
	}
	
	, deleteSelectedUser: function(userId, index) {
		Ext.MessageBox.confirm(
            'Please confirm',
            'Confirm User delete?',            
            function(btn, text) {
                if (btn=='yes') {
                	if (userId != null) {	

						Ext.Ajax.request({
				            url: this.services['deleteUserService'],
				            params: {'ID': userId},
				            method: 'GET',
				            success: function(response, options) {
								if (response !== undefined) {
									//this.rolesStore.load();
									var sm = Ext.getCmp('usergrid').getSelectionModel();
									var deleteRow = sm.getSelected();
									this.usersStore.remove(deleteRow);
									this.usersStore.commitChanges();
								} else {
									Sbi.exception.ExceptionHandler.showErrorMessage('Error while deleting User', 'Service Error');
								}
				            },
				            failure: function() {
				                Ext.MessageBox.show({
				                    title: 'Error',
				                    msg: 'Error in deleting User',
				                    width: 150,
				                    buttons: Ext.MessageBox.OK
				               });
				            }
				            ,scope: this
			
						});
					} else {
						Sbi.exception.ExceptionHandler.showWarningMessage('Operation failed', 'Warning');
					}
                }
            },
            this
		);
	}

});

Ext.reg('manageusers', Sbi.profiling.ManageUsers);
