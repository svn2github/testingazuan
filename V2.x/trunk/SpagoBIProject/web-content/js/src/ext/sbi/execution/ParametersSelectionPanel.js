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
  *  [list]
  * 
  * 
  * Public Events
  * 
  *  [list]
  * 
  * Authors
  * 
  * - Andrea Gioia (andrea.gioia@eng.it)
  */


/**
	 * Override Ext.FormPanel so that in case whe create a form without items it still has a item list.
	 * ERROR IS : this.items has no properties
	 */

	Ext.override(Ext.FormPanel, {
		// private
		initFields : function(){
			//BEGIN FIX It can happend that there is a form created without items (json)
			this.initItems();
			//END FIX
			var f = this.form;
			var formPanel = this;
			var fn = function(c){
				if(c.doLayout && c != formPanel){
					Ext.applyIf(c, {
						labelAlign: c.ownerCt.labelAlign,
						labelWidth: c.ownerCt.labelWidth,
						itemCls: c.ownerCt.itemCls
					});
					if(c.items){
						c.items.each(fn);
					}
				}else if(c.isFormField){
					f.add(c);
				}
			}
			this.items.each(fn);
		}
	});


Ext.ns("Sbi.execution");

Sbi.execution.ParametersSelectionPanel = function(config) {
	
	// always declare exploited services first!
	var params = {LIGHT_NAVIGATOR_DISABLED: 'TRUE', SBI_EXECUTION_ID: null};
	this.services = new Array();
	this.services['getParametersForExecutionService'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'GET_PARAMETERS_FOR_EXECUTION_ACTION'
		, baseParams: params
	});
	this.services['getParameterValueForExecutionService'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'GET_PARAMETER_VALUES_FOR_EXECUTION_ACTION'
		, baseParams: params
	});
	
	this.inputFieldOptionsStoreConfig = {
		proxy: new Ext.data.HttpProxy({
			url: this.services['getParameterValueForExecutionService']
		})
		   
	   	, reader: new Ext.data.JsonReader()
	};     
  

	
	var c = Ext.apply({}, config, {
		bodyStyle:'padding:16px 16px 16px 16px;'
		//, id: 'card-2', html: 'Document execution page'
	});   
	
	
	
	// constructor
    Sbi.execution.ParametersSelectionPanel.superclass.constructor.call(this, c);
    
    //this.addEvents();	
};

Ext.extend(Sbi.execution.ParametersSelectionPanel, Ext.FormPanel, {
    
    services: null
    , fields: null
    , inputFieldOption: null
    , inputFieldOptionsStoreConfig: null
   
    // ----------------------------------------------------------------------------------------
    // public methods
    // ----------------------------------------------------------------------------------------
    
	, loadParametersForExecution: function( executionInstance ) {
		
		Ext.Ajax.request({
	          url: this.services['getParametersForExecutionService'],
	          params: executionInstance,
	          callback : function(options , success, response){
	    	  	if(success && response !== undefined) {   
		      		if(response.responseText !== undefined) {
		      			var content = Ext.util.JSON.decode( response.responseText );
		      			if(content !== undefined) {
		      				alert( content.toSource() );	
		      				this.onParametersForExecutionLoaded(executionInstance, content);
		      			} 
		      		} else {
		      			Sbi.exception.ExceptionHandler.showErrorMessage('Server response is empty', 'Service Error');
		      		}
	    	  	}
	          },
	          scope: this,
	  		  failure: Sbi.exception.ExceptionHandler.handleFailure      
	     });
	}

	, onParametersForExecutionLoaded: function( executionInstance, parameters ) {
	
		for(var i = 0; i < parameters.length; i++) {
			var field = this.createField( executionInstance, parameters[i] ); 
			this.add(field);
		}
		this.doLayout();
	}
	
	, getFormState: function() {
		var state;
		
		var form = this.getForm(); 
		
		state = {};
		for(var i = 0;  i < form.items.getCount(); i++) {
			var item = form.items.get(i);
			var value = item.getValue();
			if(value instanceof Date) {
				value = value.toString();
			} 
			state[item.getName()] = value;
		}
		
		alert(state.toSource());
		
		return state;
	}
	
	, clear: function() {
		
	}
	
	// ----------------------------------------------------------------------------------------
	// private methods
	// ----------------------------------------------------------------------------------------
	
	
	
	, createField: function( executionInstance, p ) {
		var field;
		
		alert(p.id + ' - ' + p.selectionType + ' - ' + !p.mandatory);
		var baseConfig = {
	       fieldLabel: p.label
		   , name : p.id
		   , width: 200
		   , allowBlank: !p.mandatory
		};
		
		var allowBlank = !p.mandatory;
		
		if(p.selectionType === 'COMBOBOX') {
			var param = {};
			Ext.apply(param, executionInstance);
			Ext.apply(param, {
				PARAMETER_ID: p.id
				, MODE: 'simple'
			});
			
			this.inputFieldOptionsStoreConfig.reader = new Ext.data.JsonReader();
			var store = new Ext.data.Store(this.inputFieldOptionsStoreConfig);
			
			field = new Ext.form.ComboBox(Ext.apply(baseConfig, {
				tpl: '<tpl for="."><div ext:qtip="{label} ({value}): {description}" class="x-combo-list-item">{label}</div></tpl>'
                , editable  : true			    
			    , forceSelection : true
			    , store :  store
			    , displayField:'label'
			    , valueField:'value'
			    , emptyText: 'select value'
			    , typeAhead: true
			    , triggerAction: 'all'
			    , selectOnFocus:true
			    , autoLoad: false
			    , mode : 'local'
			    , listeners: {
			    	'select': {
			       		fn: function(){}
			       		, scope: this
			    	}
			    }
			}));
					
			store.load({params: param});
						
		} else if(p.selectionType === 'LIST') {
			var params = {};
			Ext.apply(params, executionInstance);
			Ext.apply(params, {
				PARAMETER_ID: p.id
				, MODE: 'complete'
			});
			
			this.inputFieldOptionsStoreConfig.reader = new Ext.data.JsonReader();
			var s = new Ext.data.Store(this.inputFieldOptionsStoreConfig);
			
			field = new Sbi.widgets.LookupField(Ext.apply(baseConfig, {
				  store: s
					, params: params
					, singleSelect: true
			}));
			
			
		} else if(p.selectionType ===  'CHECK_LIST') {		
			var params = {};
			Ext.apply(params, executionInstance);
			Ext.apply(params, {
				PARAMETER_ID: p.id
				, MODE: 'complete'
			});
			
			this.inputFieldOptionsStoreConfig.reader = new Ext.data.JsonReader();
			var s = new Ext.data.Store(this.inputFieldOptionsStoreConfig);
			
			field = new Sbi.widgets.LookupField(Ext.apply(baseConfig, {
				  store: s
				, params: params
				, singleSelect: false
			}));
		} else { 
			if(p.type === 'DATE') {				
				field = new Ext.form.DateField(baseConfig);
			} else if(p.type === 'NUMBER') {
				field = new Ext.form.NumberField(baseConfig);
			} else {
				field = new Ext.form.TextField(baseConfig);
			}
			
			
		}
		
		return field;
	}
	
});