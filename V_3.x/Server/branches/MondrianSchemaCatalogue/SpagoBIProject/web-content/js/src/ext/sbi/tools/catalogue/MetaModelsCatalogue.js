/** SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. **/

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
 * Authors - Davide Zerbetto (davide.zerbetto@eng.it)
 */
Ext.ns("Sbi.tools.catalogue");

Sbi.tools.catalogue.MetaModelsCatalogue = function(config) {
	
	// apply defaults values
	config = Ext.apply({
		// no defaults
	}, config || {});
	
	var baseParams = {LIGHT_NAVIGATOR_DISABLED: 'TRUE'};
	
	this.configurationObject = {};
	
	this.configurationObject.manageListService = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'GET_META_MODELS_ACTION'
		, baseParams: baseParams
	});
	this.configurationObject.saveItemService = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'SAVE_META_MODEL_ACTION'
		, baseParams: baseParams
	});
	this.configurationObject.deleteItemService = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'DELETE_META_MODEL_ACTION'
		, baseParams: baseParams
	});

	this.init();
	
	config.configurationObject = this.configurationObject;
	config.singleSelection = true;
	config.fileUpload = true;  // this is a multipart form!!
	
	var c = Ext.apply({}, config);

	Sbi.profiling.ManageRoles.superclass.constructor.call(this, c);
	
	this.rowselModel.addListener('rowselect',function(sm, row, rec) { 
		this.getForm().loadRecord(rec);
     }, this);

};

Ext.extend(Sbi.tools.catalogue.MetaModelsCatalogue, Sbi.widgets.ListDetailForm, {
	
	configurationObject : null
	, detailPanel : null

	,
	init : function() {
	    this.configurationObject.fields = [ 'id' , 'name' , 'description' ];
		
		this.configurationObject.emptyRecToAdd = new Ext.data.Record({ id : 0, name : '' , description : ''});
		
		this.configurationObject.gridColItems = 
			[
				{header: LN('sbi.generic.name'), width: 200, sortable: true, locked: true, dataIndex: 'name'},
				{header: LN('sbi.generic.descr'), width: 220, sortable: true, dataIndex: 'description'}
			];
		
		this.configurationObject.panelTitle = LN('sbi.tools.catalogue.metaModelsCatalogue');
		this.configurationObject.listTitle = LN('sbi.tools.catalogue.metaModelsCatalogue');

		this.initDetailPanel();
		
		this.configurationObject.tabItems = [ this.detailPanel ];
    }

	,
	initDetailPanel : function() {
		
		//START list of detail fields
		var idField = {
			name				: 'id'
			, hidden			: true
		};
		
		var nameField = {
			maxLength 			: 100
			, minLength 		: 1
			, fieldLabel 		: LN('sbi.generic.name')
			, allowBlank 		: false
			, validationEvent	: true
			, name				: 'name'
		}; 
		
		var descrField = {
				maxLength 			: 500
				, fieldLabel 		: LN('sbi.generic.descr')
				, allowBlank 		: true
				, name				: 'description'
		};
		
		var uploadField = new Ext.form.TextField({
			inputType				: 'file'
			, fieldLabel			: LN('sbi.generic.upload')
			, allowBlank			: true
		});
		
		var downloadButton = new Ext.Toolbar.Button({
			text					: LN('sbi.generic.download')
			, handler				: function () {
				alert('not implemented yet');
			}
			, scope: this
		});
		
		this.versionsGridPanel = new Sbi.tools.catalogue.MetaModelsVersionsGridPanel( {} );
		
		//END list of detail fields
	 	   
		this.detailPanel = new Ext.Panel({
			title 		: LN('sbi.generic.details')
			, layout 	: 'fit'
			, items : [{
				columnWidth		: 0.4
				, xtype			: 'fieldset'
				, labelWidth	: 110
				, defaults		: { width: 220, border : false }  
				, defaultType	: 'textfield'
				, autoHeight	: true
				, autoScroll  	: true
				, bodyStyle		: Ext.isIE ? 'padding:0 0 5px 15px;' : 'padding:10px 15px;'
				, border		: false
				, style			: {
							"margin-left" 	: "20px" 
							, "margin-right" : Ext.isIE6 ? (Ext.isStrict ? "-20px" : "-23px") : "20"  
				}
				, items: [ idField , nameField , descrField , uploadField ]
				}
				, this.versionsGridPanel 
			]
			, buttons: [ downloadButton ]
		});
		
	}
	
	,
	save : function() {
		this.validate(this.doSave, this.showValidationErrors, this);
    }
	
	,
	doSave : function () {
		
        var form = this.getForm();
	    form.submit({
	         url : Sbi.config.serviceRegistry.getBaseUrlStr({})  // a multipart form cannot contain parameters on its main URL;
	         												     // they must POST parameters
	         , params : {
                 	ACTION_NAME : 'SAVE_META_MODEL_ACTION'
                 	, LIGHT_NAVIGATOR_DISABLED : 'TRUE'
             }
	         , waitMsg : 'Saving ...'
	         , success : function(form, action) {
		 			Ext.Msg.show({
					   title: 'saved'
					   , msg: 'saved'
					   , buttons: Ext.Msg.OK
					   , icon: Ext.MessageBox.INFO
					});
	         }
	         , failure : function (form, action) {
					Ext.Msg.show({
					   title: 'Error'
					   , msg: action.result.msg
					   , buttons: Ext.Msg.OK
					   , icon: Ext.MessageBox.ERROR
					});
	         }
	         , scope : this
	    });
		
//		var values = this.getFormState();
//		
//		Ext.Ajax.request({
//			url : this.services['saveItemService']
//			, params : values
//			, method : 'POST'
//			, success : this.doSaveHandler
//			, failure : Sbi.exception.ExceptionHandler.handleFailure
//			, scope : this
//		});
		
	}
	
	,
	getFormState : function () {
		var state = this.getForm().getFieldValues();
		if (!state['id']) {
			state['id'] = 0;
		}
		return state;
	}
	
	,
	doValidate : function () {
		// returns an array of errors; if no error returns an empty array
		var toReturn = new Array();
		var values = this.getForm().getFieldValues();
		var name = values['name'];
		if (name.trim() == '') {
			toReturn.push(LN('sbi.generic.validation.missingName'));
		}
		return toReturn;
	}
	
	,
	doSaveHandler : function(response, options) {
		if (response !== undefined && response.responseText !== undefined ) {
			this.commitChangesInList(response.responseText);
		} else {
  			Sbi.exception.ExceptionHandler.showErrorMessage('Server response is empty', 'Service Error');
  		}
	}
	
	,
	commitChangesInList : function ( serverResponseText ) {
		var responseContent = Ext.util.JSON.decode( serverResponseText );
		if (this.isNewMetaModel()) {
			var values = this.getFormState();
			var id = responseContent.id;
			values['id'] = id;
			var newRecord = new Ext.data.Record( values );
			this.mainElementsStore.add(newRecord);
			this.rowselModel.selectLastRow(true);
		} else {
			var values = this.getFormState();
			var record = this.getRecordById( values['id'] );
			record.set( 'name', values['name'] );
			record.set( 'description', values['description'] );
			this.mainElementsStore.commitChanges();
		}
	}
	
	,
	getRecordById : function ( id ) {
		var length = this.mainElementsStore.getCount();
		for ( var i = 0 ; i < length ; i++ ) {
   	        var aRecord = this.mainElementsStore.getAt(i);
   	        if (aRecord.data.id == id ){
   	        	return aRecord;
			}
   	    }
		return null;
	}
	
	,
	isNewMetaModel : function () {
		var values = this.getFormState();
		return values['id'] == 0;
	}
	
	,
	validate: function (successHandler, failureHandler, scope) {
		var errorArray = this.doValidate();
		if ( errorArray.length > 0 ) {
			return failureHandler.call(scope || this, errorArray);
		} else {
			return successHandler.call(scope || this);	
		}
	}
	
    ,
    showValidationErrors : function(errorsArray) {
    	var errMessage = '';
    	
    	for (var i = 0; i < errorsArray.length; i++) {
    		var error = errorsArray[i];
    		errMessage += error + '<br>';
    	}
    	
    	Sbi.exception.ExceptionHandler.showErrorMessage(errMessage, LN('sbi.generic.error'));
    }

});
