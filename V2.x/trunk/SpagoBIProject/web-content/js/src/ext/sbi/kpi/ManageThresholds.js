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
 * Authors - Chiara Chiarelli (chiara.chiarelli@eng.it)
 */
Ext.ns("Sbi.kpi");

Sbi.kpi.ManageThresholds = function(config) {
	 
	var paramsList = {MESSAGE_DET: "THRESHOLDS_LIST"};
	var paramsSave = {LIGHT_NAVIGATOR_DISABLED: 'TRUE',MESSAGE_DET: "THRESHOLD_INSERT"};
	var paramsDel = {LIGHT_NAVIGATOR_DISABLED: 'TRUE',MESSAGE_DET: "THRESHOLD_DELETE"};
	
	this.configurationObject = {};
	
	this.nodeTypesCd = config.nodeTypesCd;
	this.thrSeverityTypesCd = config.thrSeverityTypesCd;
	this.drawSelectColumn = config.drawSelectColumn;
	
	this.configurationObject.manageListService = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'MANAGE_THRESHOLDS_ACTION'
		, baseParams: paramsList
	});
	this.configurationObject.saveItemService = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'MANAGE_THRESHOLDS_ACTION'
		, baseParams: paramsSave
	});
	this.configurationObject.deleteItemService = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'MANAGE_THRESHOLDS_ACTION'
		, baseParams: paramsDel
	});
	
	this.initConfigObject();
	
	config.configurationObject = this.configurationObject;	
	
	var c = Ext.apply({}, config || {}, {});

	Sbi.kpi.ManageThresholds.superclass.constructor.call(this, c);	 	
	
	this.rowselModel.addListener('rowselect',this.fillThrValues,this);
};

Ext.extend(Sbi.kpi.ManageThresholds, Sbi.widgets.ListDetailForm, {
	
	configurationObject: null
	, gridForm:null
	, mainElementsStore:null
	, severityStore: null
	, nodeTypesCd: null
	, thrSeverityTypesCd: null
	, drawSelectColumn: null
	
	
	,activateThrValuesForm:function(combo,record,index){

		var thrTypeSelected = record.get('typeCd');
		if(thrTypeSelected != null && thrTypeSelected=='MINIMUM'){
			this.tempThrV.setVisible(false);
			this.thrMinOrMaxDetail.setVisible(true);
			this.detailThrMin.enable();
            this.detailThrMinClosed.enable();
            this.detailThrMax.disable();
            this.detailThrMaxClosed.disable();
		}else if (thrTypeSelected != null && thrTypeSelected=='MAXIMUM'){
			this.tempThrV.setVisible(false);
			this.thrMinOrMaxDetail.setVisible(true);
			this.detailThrMin.disable();
            this.detailThrMinClosed.disable();
            this.detailThrMax.enable();
            this.detailThrMaxClosed.enable();
		}else if (thrTypeSelected != null && thrTypeSelected=='RANGE'){
			this.tempThrV.setVisible(true);
			this.thrMinOrMaxDetail.setVisible(false);			
		}
	}

	, fillThrValues : function(sm, row, rec) {	 
          	var tempArr = rec.data.thrValues;
         	var length = rec.data.thrValues.length;
         	this.activateThrValuesForm(null, rec, row);
         	/*if(length>0){
	         	var tempRecord = new Ext.data.Record({"label":tempArr[0].label, "position":tempArr[0].position});
	         	this.thrMinOrMaxDetail.items	    
         	}  */    	
    }

	,initConfigObject:function(){
	    this.configurationObject.fields = ['id'
		                     	          , 'name'
		                    	          , 'code'
		                    	          , 'description'   
		                    	          , 'typeCd'
		                    	          , 'thrValues'
		                    	          , 'label'
		                    	          , 'position'
		                    	          , 'min'
		                    	          , 'minIncluded'
		                    	          , 'max'
		                    	          , 'maxIncluded'
		                    	          , 'val'
		                    	          , 'color'
		                    	          , 'severityCd'
		                    	          ];
		
		this.configurationObject.emptyRecToAdd = new Ext.data.Record({
										  id: 0,
										  name:'', 
										  code:'', 
										  description:'',
										  typeCd: '',
										  thrValues: []
										 });   
		
		this.configurationObject.gridColItems = [
		                                         {id:'name',header: LN('sbi.generic.name'), width: 50, sortable: true, locked:false, dataIndex: 'name'},
		                                         {header: LN('sbi.generic.code'), width: 150, sortable: true, dataIndex: 'code'}
		                                        ];
		
		if(this.drawSelectColumn){
			this.configurationObject.drawSelectColumn = true;
		}

		this.configurationObject.panelTitle = LN('sbi.thresholds.panelTitle');
		this.configurationObject.listTitle = LN('sbi.thresholds.listTitle');
		
		this.initTabItems();
    }

	,initTabItems: function(){

		//Store of the combobox
 	    this.typesStore = new Ext.data.SimpleStore({
 	        fields: ['typeCd'],
 	        data: this.nodeTypesCd,
 	        autoLoad: false
 	    });
 	    
 	   this.severityStore = new Ext.data.SimpleStore({
	        fields: ['severityCd'],
	        data: this.thrSeverityTypesCd,
	        autoLoad: false
	    });
 	    
 	   //START list of detail fields
 	   var detailFieldId = {
               name: 'id',
               hidden: true
           };
 		   
 	   var detailFieldName = new Ext.form.TextField({
          	 maxLength:100,
        	 minLength:1,
        	 regex : new RegExp("^([a-zA-Z1-9_\x2F])+$", "g"),
        	 regexText : LN('sbi.roles.alfanumericString'),
             fieldLabel: LN('sbi.generic.name'),
             allowBlank: false,
             validationEvent:true,
             name: 'name'
         });
 			  
 	   var detailFieldCode = new Ext.form.TextField({
          	 maxLength:20,
        	 minLength:0,
        	 regex : new RegExp("^([A-Za-z0-9_])+$", "g"),
        	 regexText : LN('sbi.roles.alfanumericString2'),
             fieldLabel:LN('sbi.generic.code'),
             validationEvent:true,
             name: 'code'
         });	  
 		   
 	   var detailFieldDescr = new Ext.form.TextArea({
          	 maxLength:160,
        	 width : 250,
             height : 80,
        	 regex : new RegExp("^([a-zA-Z1-9_\x2F])+$", "g"),
        	 regexText : LN('sbi.roles.alfanumericString'),
             fieldLabel: LN('sbi.generic.descr'),
             validationEvent:true,
             name: 'description'
         });	 		   
 		   
 	   var detailFieldNodeType =  new Ext.form.ComboBox({
        	  name: 'typeCd',
              store: this.typesStore,
              fieldLabel: LN('sbi.generic.type'),
              displayField: 'typeCd',   // what the user sees in the popup
              valueField: 'typeCd',        // what is passed to the 'change' event
              typeAhead: true,
              forceSelection: true,
              mode: 'local',
              triggerAction: 'all',
              selectOnFocus: true,
              editable: false,
              allowBlank: false,
              validationEvent:true
          });  
 	  detailFieldNodeType.addListener('select',this.activateThrValuesForm,this);
 	  //END list of detail fields
 	   
 	  this.tempThrV = new Sbi.kpi.ManageThresholdValues({});
 	  
 	  var detailThrPosition = new Ext.form.TextField({
 			 maxLength:20,
             fieldLabel: 'Position',
             validationEvent:true,
             name: 'position'
         });	 
 	  
 	  var detailThrLabel = new Ext.form.TextField({
 			 maxLength:20,
             fieldLabel: 'Label',
             validationEvent:true,
             name: 'label'
         });	
 	  
 	 this.detailThrMin = new Ext.form.NumberField({
 			 maxLength:20,
             fieldLabel: 'Min Value',
             validationEvent:true,
             name: 'min'
         });
 	 
 	this.detailThrMinClosed = new Ext.form.Checkbox({
			 maxLength:20,
            fieldLabel: 'Included?',
            validationEvent:true,
            name: 'minIncluded'
        });
 	
 	 this.detailThrMax = new Ext.form.NumberField({
 			 maxLength:20,
             fieldLabel: 'Max Value',
             validationEvent:true,
             name: 'max'
         });
 	 
 	this.detailThrMaxClosed = new Ext.form.Checkbox({
			 maxLength:20,
			 xtype: 'checkbox',
            fieldLabel: 'Included?',
            validationEvent:true,
            name: 'maxIncluded'
 		});
 	
 	var detailThrValue = new Ext.form.NumberField({
			 maxLength:20,
            fieldLabel: 'Value',
            validationEvent:true,
            name: 'val'
        });
 	
 	var detailThrColor = new Ext.ux.ColorField({
 			fieldLabel: 'Color', 
 			value: '#FFFFFF', 
 			msgTarget: 'qtip', 
 			name: 'color',
 			fallback: true});
 	
 	var detailThrSeverity = new Ext.form.ComboBox({
      	  name: 'severityCd',
          store: this.severityStore,
          fieldLabel: 'Severity',
          displayField: 'severityCd',   // what the user sees in the popup
          valueField: 'severityCd',        // what is passed to the 'change' event
          typeAhead: true,
          forceSelection: true,
          mode: 'local',
          triggerAction: 'all',
          selectOnFocus: true,
          editable: false,
          allowBlank: false,
          validationEvent:true,
          xtype: 'combo'
      });  
 	  
 	  this.thrMinOrMaxDetail = new Ext.form.FieldSet({  	
             labelWidth: 90,
             defaults: {width: 140, border:false},    
             defaultType: 'textfield',
             autoHeight: true,
             autoScroll  : true,
             bodyStyle: Ext.isIE ? 'padding:0 0 5px 15px;' : 'padding:10px 15px;',
             border: true,
             style: {
                 "margin-left": "10px", 
                 "margin-top": "10px", 
                 "margin-right": Ext.isIE6 ? (Ext.isStrict ? "-10px" : "-13px") : "10px"  
             },
             items: [detailThrPosition, detailThrLabel, this.detailThrMin, 
                     this.detailThrMinClosed, this.detailThrMax, this.detailThrMaxClosed, 
                     detailThrValue, detailThrColor, detailThrSeverity]
    	});

 	  this.detailItem = new Ext.form.FieldSet({ 
		   		 id: 'items-detail',   	
	 		   	 itemId: 'items-detail',   	              
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
	             items: [detailFieldId, detailFieldName, detailFieldCode, 
	                     detailFieldDescr, detailFieldNodeType]
	    	});
 	   
 	   this.thrValuesItem = new Ext.Panel({
		        title: 'Values'
			        , id : 'thr-values'
			        , layout: 'fit'
			        , autoScroll: true
			        , items: [this.tempThrV,this.thrMinOrMaxDetail]
			        , itemId: 'thrValues'
			        , scope: this
			    });
 		
 	   this.configurationObject.tabItems = [{
		        title: LN('sbi.generic.details')
		        , itemId: 'detail'
		        , width: 430
		        , items: [this.detailItem]
		    },this.thrValuesItem];
	}
	
    //OVERRIDING save method
	,save : function() {

		var values = this.gridForm.getForm().getFieldValues();
		var idRec = values['id'];
		var newRec;
	
		if(idRec ==0 || idRec == null || idRec === ''){
			newRec =new Ext.data.Record({
					name: values['name'],
					code: values['code'],
			        description: values['description'],		
			        typeCd: values['typeCd']
			});	  
			
		}else{
			var record;
			var length = this.mainElementsStore.getCount();
			for(var i=0;i<length;i++){
	   	        var tempRecord = this.mainElementsStore.getAt(i);
	   	        if(tempRecord.data.id==idRec){
	   	        	record = tempRecord;
				}			   
	   	    }	
			record.set('name',values['name']);
			record.set('code',values['code']);
			record.set('description',values['description']);
			record.set('typeCd',values['typeCd']);						
		}

        var params = {
        	name : values['name'],
        	code : values['code'],
        	description : values['description'],
        	typeCd : values['typeCd']
        };
        
        if(idRec){
        	params.id = idRec;
        }
        
        Ext.Ajax.request({
            url: this.services['saveItemService'],
            params: params,
            method: 'GET',
            success: function(response, options) {
				if (response !== undefined) {			
		      		if(response.responseText !== undefined) {

		      			var content = Ext.util.JSON.decode( response.responseText );
		      			if(content.responseText !== 'Operation succeded') {
			                    Ext.MessageBox.show({
			                        title: LN('sbi.generic.error'),
			                        msg: content,
			                        width: 150,
			                        buttons: Ext.MessageBox.OK
			                   });
			      		}else{
			      			var itemId = content.id;
			      			if(newRec != null && newRec != undefined && itemId != null && itemId !==''){
			      				newRec.set('id', itemId);
			      				this.mainElementsStore.add(newRec);  
			      			}
			      			this.mainElementsStore.commitChanges();
			      			if(itemId != null && itemId !==''){
					            this.rowselModel.selectLastRow(true);
				            }
			      			
			      			Ext.MessageBox.show({
			                        title: LN('sbi.generic.result'),
			                        msg: LN('sbi.generic.resultMsg'),
			                        width: 200,
			                        buttons: Ext.MessageBox.OK
			                });
			      		}      				 

		      		} else {
		      			Sbi.exception.ExceptionHandler.showErrorMessage(LN('sbi.generic.serviceResponseEmpty'), LN('sbi.generic.serviceError'));
		      		}
				} else {
					Sbi.exception.ExceptionHandler.showErrorMessage(LN('sbi.generic.savingItemError'), LN('sbi.generic.serviceError'));
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
	                    title: LN('sbi.generic.validationError'),
	                    msg: errMessage,
	                    width: 400,
	                    buttons: Ext.MessageBox.OK
	               });
	      		}else{
	                Ext.MessageBox.show({
	                    title: LN('sbi.generic.error'),
	                    msg: LN('sbi.generic.savingItemError'),
	                    width: 150,
	                    buttons: Ext.MessageBox.OK
	               });
	      		}
            }
            ,scope: this
        });
    }

});