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
Ext.ns("Sbi.alarms");

Sbi.alarms.ManageAlarms = function(config) { 
	var paramsList = { MESSAGE_DET: "ALARMS_LIST"};
	var paramsSave = {LIGHT_NAVIGATOR_DISABLED: 'TRUE', MESSAGE_DET: "ALARM_INSERT"};
	var paramsDel = {LIGHT_NAVIGATOR_DISABLED: 'TRUE', MESSAGE_DET: "ALARM_DELETE"};
	
	this.services = new Array();
	this.services['manageAlarmsList'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'MANAGE_ALARMS_ACTION'
		, baseParams: paramsList
	});
	this.services['saveAlarmService'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'MANAGE_ALARMS_ACTION'
		, baseParams: paramsSave
	});
	
	this.services['deleteAlarmService'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'MANAGE_ALARMS_ACTION'
		, baseParams: paramsDel
	});	
	

	this.initStores(config);

	this.initManageAlarms();
	
	Ext.getCmp('alarmsgrid').store.on('load', function(){
		 var grid = Ext.getCmp('alarmsgrid');
		 
		 if(this.alarmsStore.getTotalCount()>0){
			 grid.fireEvent('rowclick', grid, 0);
			 grid.getSelectionModel().selectRow(0);
		 }
	 }, this, {
	 single: true
    });
	
	this.alarmsStore.load();
 
    Ext.getCmp('alarmsgrid').on('delete', this.deleteSelectedAlarm, this);	
};

Ext.extend(Sbi.alarms.ManageAlarms, Ext.FormPanel, {
	  gridForm:null
	, alarmsStore:null
	, kpiStore: null
	, thresholdsStore: null
	, contactsStore: null
	
	, colModel:null
	, typeData: null
	, buttons: null
	
	, detailItems: null
	, detailTab: null
    , kpiTab: null
    , contactsTab: null
	, tabs: null
	
	, contactsGridPanel: null
	, contactsGrid: null
	, smContacts: null
	
	, alarmsEmptyStore: null
	, kpisEmptyStore: null
	, documentsEmptyStore: null

	, initStores: function (config) {
	
		this.alarmsStore = new Ext.data.JsonStore({
	    	autoLoad: false  
	    	,fields: ['id'
	    			  , 'name'
	    	          , 'description'
	    	          , 'label'
	    	          , 'modality'
	    	          , 'singleEvent'
	    	          , 'autoDisabled'
	    	          , 'text'
	    	          , 'url'
	    	         // , 'contacts'
	    	          ]
	    	, root: 'samples'
			, url: this.services['manageAlarmsList']			
		});	
		
		this.kpiStore = new Ext.data.SimpleStore({
			autoLoad: false 
			, data: config.kpisEmptyList
	        , fields : [ 'id', 'kpiName', 'kpiModel' ]
	    });
	    
	    this.thresholdsStore = new Ext.data.SimpleStore({
	    	id: 'id',
	        fields : [ 'id', 'name', 'description', 'checked' ]
	    });
	    
	    this.documentsStore = new Ext.data.SimpleStore({
	    	id: 'id',
	        fields : [ 'id', 'name', 'description', 'checked' ]
	    });
	    
	    this.contactsStore = new Ext.data.SimpleStore({
	    	id: 'id',
	        fields : [ 'id', 'name', 'email', 'mobile', 'resources' ]
	    });
	    
	    this.kpisEmptyStore = config.kpisEmptyList;

	    /*
	    this.alarmsEmptyStore = config.alarmsEmptyList;
	    this.documentsEmptyStore = config.documentsEmptyList;
	    */
  
    }
    
  ,initTabs: function(){
  
      this.detailTab = new Ext.Panel({
		        title: LN('sbi.alarms.details')
		        , itemId: 'detail'
		        , layout: 'fit'
		        , items: {
		 		   	     itemId: 'alarm-detail',   	              
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
			                 fieldLabel: LN('sbi.alarms.alarmLabel'),
			                 name: 'label',
			                 width : 250,
			                 allowBlank: false
			                // validationEvent:true,
	  		            	// maxLength:100,
	  		            	// minLength:1,
	  		            	// regex : new RegExp("^([a-zA-Z1-9_\x2D])+$", "g"),
	  		            	// regexText : LN('sbi.users.wrongFormat')
			             },{
			                 fieldLabel:  LN('sbi.alarms.alarmName'),
			                 name: 'name',
			                 width : 250,
			                 allowBlank: false
			                // validationEvent:true,
	  		            	// maxLength:255,
	  		            	// minLength:1,
	  		            	// regex : new RegExp("^([a-zA-Z0-9_\x2D\s\x2F])+$", "g"),
	  		            	// regexText : LN('sbi.users.wrongFormat')
			             },{
			                 fieldLabel: LN('sbi.alarms.alarmDescr'),
			                 width : 250,
			                 name: 'description'
			                // allowBlank: false,
			                // validationEvent:true,
	  		            	// maxLength:160,
	  		            	// minLength:1
			             },{
				            xtype: 'radiogroup',
				            itemId: 'modality',
				            name: 'mod',
				            boxMinWidth  : 50,
				            boxMinHeight  : 100,
				            fieldLabel: LN('sbi.alarms.alarmModality'),
				            items: [
				             		{boxLabel: LN('sbi.alarms.MAIL'),name: 'modality', inputValue: 1, checked: true},
							        {boxLabel: LN('sbi.alarms.SMS'),name: 'modality', inputValue: 2}	
				            ]
				         },{
				            xtype: 'checkboxgroup',
				            itemId: 'options',
				            columns: 2,
				            boxMinWidth  : 200,
				            boxMinHeight  : 100,
				            hideLabel  : false,
				            fieldLabel: LN('sbi.alarms.options'),
				            items: [
				                {boxLabel: LN('sbi.alarms.alarmSingleEvent'), name: 'singleEvent', checked:false},
				                {boxLabel: LN('sbi.alarms.alarmAutoDisabled'), name: 'autoDisabled', checked:true}
				            ]
			             },{
			                 fieldLabel:  LN('sbi.alarms.alarmMailUrl'),
			                 width : 250,
			                 name: 'url'
			                // allowBlank: false,
			                // validationEvent:true,
	  		            	// maxLength:160,
	  		            	// minLength:1
			             },{
			                 fieldLabel:  LN('sbi.alarms.alarmMailText'),
			                 xtype: 'textarea',
			                 width : 250,
			                 height : 80,
			                 name: 'text'
			                // allowBlank: false,
			                // validationEvent:true,
	  		            	// maxLength:160,
	  		            	// minLength:1
		             }]
		    	}
		 });
      

    	 this.kpiCheckColumn = new Ext.grid.CheckboxSelectionModel( {header: ' ',singleSelect: false, scope:this, dataIndex: 'id'} );
		 this.kpiCheckColumn.on('rowselect', this.onKpiSelect, this);
		 this.kpiCheckColumn.on('rowdeselect', this.onKpiDeselect, this);
	     this.kpiCm = new Ext.grid.ColumnModel({
	         // specify any defaults for each column
	         defaults: {
	             sortable: true // columns are not sortable by default           
	         },
	         columns: [
	             {
	                 id: 'id',
	                 header: 'Kpi Instance Id',
	                 dataIndex: 'id',
	                 width: 120
	             }, {
	                 header: 'Kpi Model',
	                 dataIndex: 'kpiModel',
	                 width: 130
	             }, {
	                 header: 'Kpi Name',
	                 dataIndex: 'kpiName',
	                 width: 120
	             },
	             this.kpiCheckColumn // the plugin instance
	         ]
	     });
		this.kpiGrid = new Ext.grid.EditorGridPanel ({
			id: 'kpi-grid',
			store: this.kpiStore,
			autoHeight : true,
			cm: this.kpiCm,
			sm: this.kpiCheckColumn,
			frame: true,
			//plugins: this.kpiCheckColumn,
            viewConfig : {
	            forceFit : true,
	            scrollOffset : 2
	        // the grid will never have scrollbars
	        },
	        singleSelect : true,
	        clicksToEdit : 2

		}); 

		
      	this.kpiTab = new Ext.Panel({
		        title: LN('sbi.alarms.kpis')
		        , id : 'alarmKpi'
		        , layout: 'fit'
		        , itemId: 'kpis'
		        , scope: this
		        , items: [
		            this.kpiGrid
		        	,{
		                 fieldLabel:  LN('sbi.alarms.alarmKpiThreshold'),
		                 name: 'alarmKpiThreshold'

		             },{
		                 fieldLabel:  LN('sbi.alarms.alarmKpiDetailDoc'),
		                 name: 'alarmKpiDetailDoc'
		             }]
		    });

		/*    
      this.contactsTab = new Ext.Panel({
		        title: LN('sbi.alarms.contacts')
		        , id : 'contactsList'
	            , items : [ this.contactsGridPanel ]
		        , itemId: 'contacts'
		    });*/
    }
	,onKpiSelect: function(){
	}
	
	,onKpiDeselect: function(){
	
	}
	
	,initManageAlarms: function(){
	
		//this.initContactsGridPanel();

	    this.deleteColumn = new Ext.grid.ButtonColumn({
	       header:  ' ',
	       dataIndex: 'id',
	       iconCls: 'icon-remove',
	       clickHandler: function(e, t) {
	          var index = Ext.getCmp("alarmsgrid").getView().findRowIndex(t);
	          
	          var selectedRecord = Ext.getCmp("alarmsgrid").store.getAt(index);
	          var userId = selectedRecord.get('id');
	          Ext.getCmp("alarmsgrid").fireEvent('delete', userId, index);
	       }
	       ,width: 25
	       ,renderer : function(v, p, record){
	           return '<center><img class="x-mybutton-'+this.id+' grid-button ' +this.iconCls+'" width="16px" height="16px" src="'+Ext.BLANK_IMAGE_URL+'"/></center>';
	       }
        });
       
        this.colModel = new Ext.grid.ColumnModel([
         {header: LN('sbi.alarms.alarmLabel'), width: 150, sortable: true, dataIndex: 'label'},
         {header: LN('sbi.alarms.alarmName'), width: 150, sortable: true, dataIndex: 'name'},
         this.deleteColumn
        ]);
     	   

	    this.tbSave = new Ext.Toolbar({
 	    	buttonAlign : 'right', 	    	
 	    	items:[new Ext.Toolbar.Button({
 	            text: LN('sbi.alarms.update'),
 	            iconCls: 'icon-save',
 	            handler: this.save,
 	            width: 30,
 	            id: 'save-btn',
 	            scope: this
 	        })
 	    	]
 	    });
 	    

 	   this.initTabs();	  

 	    
 	   this.tabs = new Ext.TabPanel({
           enableTabScroll : true
           , activeTab : 0
           , autoScroll : true
           , width: 450
           , height: 450
           , itemId: 'tabs'
           , tbar: this.tbSave 
		   , items: [ this.detailTab
		   , this.kpiTab
		   //, this.contactsTab
		   ]
		 });

	    this.tb = new Ext.Toolbar({
 	    	buttonAlign : 'right',
 	    	items:[new Ext.Toolbar.Button({
 	            text: LN('sbi.alarms.add'),
 	            iconCls: 'icon-add',
 	            handler: this.addNewAlarm,
 	            width: 30,
 	            scope: this
 	        })
 	    	]
 	    });
 	
 	    
   	   this.gridForm = new Ext.FormPanel({
   	          id: 'alarm-form',
   	          frame: true,
   	          labelAlign: 'left',
   	          title: LN('sbi.alarms.manageAlarms'),
   	          bodyStyle:'padding:5px',
   	          width: 850,
   	          layout: 'column',
   	          items: [{
   	              columnWidth: 0.90,
   	              layout: 'fit',
   	              items: {
   	        	  	  id: 'alarmsgrid',
   	                  xtype: 'grid',
   	                  ds: this.alarmsStore,   	                  
   	                  cm: this.colModel,
   	                  plugins: this.deleteColumn,
   	                  sm: new Ext.grid.RowSelectionModel({   	                  	  
   	                      singleSelect: true,
   	                      scope:this,   	                   
	   	                  fillContacts : function(row, rec) {	 
							Ext.getCmp("contacts-form").store.removeAll();
	   	                   	var tempArr = rec.data.userRoles;
	   	                  	var length = rec.data.userRoles.length;
	   	                  	for(var i=0;i<length;i++){
	   	                  		var tempRecord = new Ext.data.Record({"description":tempArr[i].description, "name":tempArr[i].name, "id":tempArr[i].id });
							    Ext.getCmp("contacts-form").store.addSorted(tempRecord);
							    Ext.getCmp("contacts-form").store.commitChanges();
							    Ext.getCmp("contacts-form").selModel.unlock();
							    if(tempArr[i].checked){
							    	var roleId = tempRecord.get('id');				    	
							    	Ext.getCmp("contacts-form").fireEvent('recToSelect', roleId, i);
							    }

	   	                  	}	
	   	                  	
	   	                  },
	   	                  fillOptions : function(row, rec) {	 
	   	                  	var singleEvent = rec.get('singleEvent');
	   	                  	var autoDisabled = rec.get('autoDisabled');
	   	                  	alert(singleEvent);
	   	                  	alert(autoDisabled);
	   	                  	Ext.getCmp("detail").items.each(function(item){	   	                   		  
	   	                   		  if(item.getItemId() == 'options'){
	   	                   		  	item.setValue({
									    'singleEvent': singleEvent,
									    'autoDisabled': autoDisabled
									});
	   	                   		  }
	   	                   	});
	   	                   	/*
	   	                  	Ext.getCmp("detail").items[7].setValue({
							    'singleEvent': singleEvent,
							    'autoDisabled': autoDisabled
							});*/
	   	                  	
	   	                  	var modality = rec.get('modality');
	   	                  	alert(modality);
	   	                  	if(modality =='SMS'){
	   	                  		Ext.getCmp("modality").setValue([false, true]);
	   	                  	}else{
	   	                  		Ext.getCmp("modality").setValue([true, false]);
	   	                  	}
	   	                  		        
	   	                  },
	   	                  /*fillAttributes : function(row, rec) {	 
	   	                    Ext.getCmp("attributes-form").store.removeAll();
	   	                  	var tempArr = rec.data.userAttributes;
	   	                  	var length = rec.data.userAttributes.length;
	   	                  	for(var i=0;i<length;i++){
	   	                  		var tempRecord = new Ext.data.Record({"value":tempArr[i].value,"name":tempArr[i].name,"id":tempArr[i].id });
							    Ext.getCmp("attributes-form").store.add(tempRecord);	
	   	                  	}			        
 
	   	                  },*/

   	                      listeners: {
   	                          rowselect: function(sm, row, rec) {   
   	                          	  Ext.getCmp('save-btn').enable();
   	                              Ext.getCmp("alarm-form").getForm().loadRecord(rec);  	
   	                              this.fillOptions(row, rec);
   	                              	 
   	                	  		  //this.fillAttributes(row, rec);
   	                	  		  //this.fillContacts(row, rec);   	                                  	                              
   	                          }
   	                      }
   	                  }),
   	                  height: 450,
   	                  title:LN('sbi.alarms.alarmsList'),
   	                  tbar: this.tb,
   	                  border: true
  	                 ,listeners: {
   	                      viewready: function(g) {
   	                      	  g.getView().refresh();
	   	                      g.fireEvent('rowclick', g, 0);
							  g.getSelectionModel().selectRow(0); 	                      
   	                      }  
   	                  }
   	              }
   	          }, this.tabs
   	          ],
   	          renderTo: Ext.getBody()
   	      });

	}

    
    , initContactsGridPanel : function() {
       
    	this.smContacts = new Ext.grid.CheckboxSelectionModel( {header: ' ',singleSelect: false, scope:this, dataIndex: 'id'} );
		//this.smContacts.on('rowselect', this.onRoleSelect, this);
		//this.smContacts.on('rowdeselect', this.onRoleDeselect, this);
		
        this.cmContacts = new Ext.grid.ColumnModel([
	         //{id:'id',header: "id", dataIndex: 'id'},
	         {header: LN('sbi.alarmcontact.name'), width: 45, sortable: true, dataIndex: 'name'},
	         {header: LN('sbi.alarmcontact.resources'), width: 65, sortable: true, dataIndex: 'resources'},
	         {header: LN('sbi.alarmcontact.email'), width: 45, sortable: true, dataIndex: 'email'},
	         {header: LN('sbi.alarmcontact.mobile'), width: 45, sortable: true, dataIndex: 'mobile'},
	         this.smContacts 
	    ]);

		this.contactsGrid = new Ext.grid.GridPanel({
			  store: this.thresholdsStore
			, id: 'contacts-form'
   	     	, cm: this.cmContacts
   	     	, sm: this.smContacts
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
		this.contactsGrid.superclass.constructor.call(this);
		
		Ext.getCmp("contacts-form").on('recToSelect', function(roleId, index){		
			Ext.getCmp("contacts-form").selModel.selectRow(index,true);
		});

	}
	
	,save : function() {
		   
	   var values = this.gridForm.getForm().getValues();

       var newRec = null;
    	
	   var params = {
	      	userId : values['userId'],
	      	fullName : values['fullName'],
	      	pwd : values['pwd']            
	   }
       params.id = values['id'];
      
       if(values['id'] !== null && values['id'] !== undefined && values['id']==0){
          newRec =new Ext.data.Record({'userId': values['userId'],'fullName': values['fullName'],'pwd':values['pwd']});	          
       }
      
      var rolesSelected = Ext.getCmp("contacts-form").selModel.getSelections();
      var lengthR = rolesSelected.length;
         var roles =new Array();
         for(var i=0;i<lengthR;i++){
          	var role ={'name':rolesSelected[i].get("name"),'id':rolesSelected[i].get("id"),'description':rolesSelected[i].get("description"),'checked':true};
			roles.push(role);
        }
      params.userRoles =  Ext.util.JSON.encode(roles);

      var userRoles =new Array();
      var tempArr = Ext.getCmp("contacts-form").store;
      var length = Ext.getCmp("contacts-form").store.data.length;

      for(var i=0;i<length;i++){
        var selected = false;
        for(var j=0;j<lengthR;j++){
        	if(rolesSelected[j].get("id")===tempArr.getAt(i).get("id")){
        		selected = true;
        		var role ={'name':tempArr.getAt(i).get("name"),'id':tempArr.getAt(i).get("id"),'description':tempArr.getAt(i).get("description"),'checked':true};
				userRoles.push(role);
        		break;
        	}
        }
        if(!selected){
        	var role ={'name':tempArr.getAt(i).get("name"),'id':tempArr.getAt(i).get("id"),'description':tempArr.getAt(i).get("description"),'checked':false};
			userRoles.push(role);
		 }
       }	
   
       var modifAttributes = this.kpiStore.getModifiedRecords();
       var lengthA = modifAttributes.length;
       var attrs =new Array();
       for(var i=0;i<lengthA;i++){
          	var attr ={'name':modifAttributes[i].get("name"),'id':modifAttributes[i].get("id"),'value':modifAttributes[i].get("value")};
			attrs.push(attr);
       }
       params.userAttributes =  Ext.util.JSON.encode(attrs);      
      
      
       var userAttributes = new Array();
       var tempArr = Ext.getCmp("attributes-form").store;
       var length = Ext.getCmp("attributes-form").store.data.length;

       for(var i=0;i<length;i++){
       		var attr ={'name':tempArr.getAt(i).get("name"),'id':tempArr.getAt(i).get("id"),'value':tempArr.getAt(i).get("value")};
		    userAttributes.push(attr);
   		}	

   	   if(newRec!==null){
		   newRec.set('userRoles', userRoles);
		   newRec.set('userAttributes', userAttributes);
	   }        
      
      Ext.Ajax.request({
          url: this.services['saveAlarmService'],
          params: params,
          method: 'GET',
          success: function(response, options) {
			if (response !== undefined) {		
      		if(response.responseText !== undefined) {
      			var content = Ext.util.JSON.decode( response.responseText );
      			if(content.responseText !== 'Operation succeded') {
                    Ext.MessageBox.show({
                        title: LN('sbi.alarms.error'),
                        msg: content,
                        width: 150,
                        buttons: Ext.MessageBox.OK
                   });
      			}else{
      			    
					var idTemp = content.id;
					if(newRec!==null){
						newRec.set('id', idTemp);
						this.alarmsStore.add(newRec);
					}else{
						var record;
						var lengthS = this.alarmsStore.getCount();
						for(var i=0;i<lengthS;i++){
				   	        var tempRecord = this.alarmsStore.getAt(i);
				   	        if(tempRecord.data.id==idTemp){
				   	        	record = tempRecord;
							}			   
				   	    }	
				   	    record.set('userAttributes', userAttributes);
				   	    record.set('userRoles', userRoles);
						//this.alarmsStore.getById(idTemp).set('userAttributes', userAttributes);
						//this.alarmsStore.getById(idTemp).set('userRoles', userRoles);
					}
					this.kpiStore.commitChanges();
					this.thresholdsStore.commitChanges();
					this.alarmsStore.commitChanges();
				
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
                   title: LN('sbi.alarms.validationError'),
                   msg: errMessage,
                   width: 400,
                   buttons: Ext.MessageBox.OK
              });
     		}else{
               Ext.MessageBox.show({
                   title:LN('sbi.alarms.error'),
                   msg: 'Error in Saving User',
                   width: 150,
                   buttons: Ext.MessageBox.OK
              });
     		}
          }
          ,scope: this
    		 });
	
    }
	, addNewAlarm : function(){
		Ext.getCmp('save-btn').enable();
		var emptyRecToAdd =new Ext.data.Record({userId:'', 
											fullName:'', 
											pwd:'',
											userRoles:'',
											userAttributes:'',
											id: 0
											});
	
		Ext.getCmp('alarm-form').getForm().loadRecord(emptyRecToAdd);
		
		Ext.getCmp("kpi-grid").store.removeAll();
	   	var tempAttrArr = this.kpisEmptyStore;
	   	var length = this.kpisEmptyStore.length;
        for(var i=0;i<length;i++){
        	var tempRecord = new Ext.data.Record({"kpiName":tempAttrArr[i].kpiName, "kpiModel":tempAttrArr[i].kpiModel,"id":tempAttrArr[i].id });
    		Ext.getCmp("kpi-grid").store.add(tempRecord);	
        }		
        /*
        Ext.getCmp("contacts-form").store.removeAll();
        var tempRolesArr = this.alarmsEmptyStore;
        var length2 = this.alarmsEmptyStore.length;
        for(var i=0;i<length2;i++){
          	var tempRecord = new Ext.data.Record({"description":tempRolesArr[i].description,"name":tempRolesArr[i].name,"id":tempRolesArr[i].id });
			Ext.getCmp("contacts-form").store.add(tempRecord);								   
        }	
		*/
		Ext.getCmp('alarm-form').doLayout();
	}
	
	, deleteSelectedAlarm: function(userId, index) {
		Ext.MessageBox.confirm(
		     LN('sbi.alarms.confirm'),
            LN('sbi.alarms.confirmDelete'),            
            function(btn, text) {
                if (btn=='yes') {
                	if (userId != null) {	

						Ext.Ajax.request({
				            url: this.services['deleteAlarmService'],
				            params: {'ID': userId},
				            method: 'GET',
				            success: function(response, options) {
								if (response !== undefined) {
									//this.thresholdsStore.load();
									var sm = Ext.getCmp('alarmsgrid').getSelectionModel();
									var deleteRow = sm.getSelected();
									this.alarmsStore.remove(deleteRow);
									this.alarmsStore.commitChanges();
									var grid = Ext.getCmp('alarmsgrid');
									if(this.alarmsStore.getCount()>0){
										grid.getSelectionModel().selectRow(0);
										grid.fireEvent('rowclick', grid, 0);
									}
								} else {
									Sbi.exception.ExceptionHandler.showErrorMessage('Error while deleting Alarm', 'Service Error');
								}
				            },
				            failure: function() {
				                Ext.MessageBox.show({
				                    title: LN('sbi.alarms.error'),
				                    msg: 'Error in deleting Alarm',
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

Ext.reg('managealarms', Sbi.alarms.ManageAlarms);
