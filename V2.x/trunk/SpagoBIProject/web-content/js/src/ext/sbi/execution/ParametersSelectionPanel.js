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
	 * Override Ext.FormPanel so that in case we create a form without items it still has a item list.
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
			};
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
  
	this.parametersForm = new Ext.FormPanel({bodyStyle:'padding:16px 16px 16px 16px;'});
	
	var c = Ext.apply({}, config, {
		//bodyStyle:'padding:16px 16px 16px 16px;'
		//, id: 'card-2', html: 'Document execution page'
		items: [this.parametersForm]
	});   
	
	
	
	// constructor
    Sbi.execution.ParametersSelectionPanel.superclass.constructor.call(this, c);
    
    //this.addEvents();	
};

Ext.extend(Sbi.execution.ParametersSelectionPanel, Ext.Panel, {
    
    services: null
    , fields: null
    , inputFieldOption: null
    , inputFieldOptionsStoreConfig: null
    , parametersForm: null
   
    // ----------------------------------------------------------------------------------------
    // public methods
    // ----------------------------------------------------------------------------------------
    
	, loadParametersForExecution: function( executionInstance ) {
		Ext.Ajax.request({
	          url: this.services['getParametersForExecutionService'],
	          
	          params: executionInstance,
	          
	          callback : function(options, success, response){
	    	  	if(success && response !== undefined) {   
		      		if(response.responseText !== undefined) {
		      			var content = Ext.util.JSON.decode( response.responseText );
		      			if(content !== undefined) {
		      				//alert( content.toSource() );	
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
			this.parametersForm.add(field);
		}
		this.parametersForm.doLayout();
		this.doLayout();
	}
	
	, getFormState: function(asObject) {
		var r;
		if(asObject === undefined || asObject === true) {
			r = this.getFormStateAsObject();
		} else {
			r = this.getFormStateAsString();
		}
		
		return r;
	}
	
	, getFormStateAsObject: function() {
		var state;
		
		var form = this.parametersForm.getForm(); 
		
		state = {};
		for(var i = 0;  i < form.items.getCount(); i++) {
			var item = form.items.get(i);
			var value = item.getValue();
			state[item.getName()] = value;
		}
		
		return state;
	}
	
	, getFormStateAsString: function() {
		var formState = this.getFormStateAsObject();
		var str = '{';
		for (p in formState) {
			var obj = formState[p];
			if (typeof obj == 'object') {
				str += p + ': ['
				for (count in obj) {
					var temp = obj[count];
					if (typeof temp == 'function') {
						continue;
					}
					if (typeof obj == 'string') {
						// the String.escape function escapes the passed string for ' and \
						temp = String.escape(temp);
						str += '\'' + temp + '\', ';
					} else {
						str += temp + ', ';
					}
				}
				// removing last ', ' string
				if (str.length > 1 && str.substring(str.length - 3, str.length - 1) == ', ') {
					str = str.substring(0, str.length - 3);
				}
				str += '], ';
			} else if (typeof obj == 'string') {
				// the String.escape function escapes the passed string for ' and \
				obj = String.escape(obj);
				str += p + ': \'' +  obj + '\', ';
			} else {
				// case number or boolean
				str += p + ': ' +  obj + ', ';
			}
		}
		if (str.length > 1 && str.substring(str.length - 3, str.length - 1) == ', ') {
			str = str.substring(0, str.length - 3);
		}
		str += '}';
		
		return str;
	}
	
	// this method is used in order to invoke the old it.eng.spagobi.analiticalmodel.execution.service.ExecuteAndSendAction
	// Problems: parameters are sent in a plain string, not as a JSON object, date parameter management is missing,
	// the separator between values is ';'
	// This method should be removed when rewriting it.eng.spagobi.analiticalmodel.execution.service.ExecuteAndSendAction
	, getFormStateAsStringOldSyntax: function() {
		var formState = this.getFormStateAsObject();
		var str = '';
		for (p in formState) {
			var obj = formState[p];
			if (typeof obj == 'object') {
				str += p + '='
				for (count in obj) {
					var temp = obj[count];
					if (typeof temp == 'function') {
						continue;
					}
					if (typeof obj == 'string') {
						// the String.escape function escapes the passed string for ' and \
						temp = String.escape(temp);
						str += temp + ';'; // using ';' as separator between values (TODO: change separator)
					} else {
						str += temp + ';'; // using ';' as separator between values (TODO: change separator)
					}
				}
				// removing last ';' string
				if (str.length > 1 && str.substring(str.length - 2, str.length - 1) == ';') {
					str = str.substring(0, str.length - 2);
				}
				str += '&';
			} else if (typeof obj == 'string') {
				// the String.escape function escapes the passed string for ' and \
				obj = String.escape(obj);
				str += p + '=' +  obj + '&';
			} else {
				// case number or boolean
				str += p + '=' +  obj + '&';
			}
		}
		if (str.length > 1 && str.substring(str.length - 2, str.length - 1) == '&') {
			str = str.substring(0, str.length - 2);
		}
		alert(str);
		
		return str;
	}
	
	
	, clear: function() {
		
	}
	
	// ----------------------------------------------------------------------------------------
	// private methods
	// ----------------------------------------------------------------------------------------
	
	
	
	, createField: function( executionInstance, p ) {
		var field;
		
		//alert(p.id + ' - ' + p.selectionType + ' - ' + !p.mandatory);
		var baseConfig = {
	       fieldLabel: p.label
		   , name : p.id
		   , width: 200
		   , allowBlank: !p.mandatory
		};
		
		if(p.selectionType === 'COMBOBOX') {
			var param = {};
			Ext.apply(param, executionInstance);
			Ext.apply(param, {
				PARAMETER_ID: p.id
				, MODE: 'simple'
			});
			
			var store = this.createStore();
			
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
						
		} else if(p.selectionType === 'LIST' || p.selectionType ===  'CHECK_LIST') {
			
			var params = Ext.apply({}, {
				PARAMETER_ID: p.id
				, MODE: 'complete'
			}, executionInstance);
			
			var store = this.createStore();
			store.on('beforeload', function(store, o) {
				var p = this.getFormState(false);
				o.params.PARAMETERS = p;
				return true;
			}, this);
			
			field = new Sbi.widgets.LookupField(Ext.apply(baseConfig, {
				  store: store
					, params: params
					, singleSelect: (p.selectionType === 'LIST')
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
	
	, createStore: function() {
		var store;
		
		store = new Ext.data.JsonStore({
			url: this.services['getParameterValueForExecutionService']
		});
		
		store.on('loadexception', function(store, options, response, e) {
			var msg = '';
			var content = Ext.util.JSON.decode( response.responseText );
  			if(content !== undefined) {
  				//msg += 'Service name: ' + content.serviceName + '\n';
  				//msg += 'Error description: ' + content.message + '\n';
  				//msg += 'Error cause: ' + content.cause + '\n';
  				//msg += 'Stacktrace: ' + Ext.util.Format.ellipsis(content.stacktrace, 200);
  				msg += content.serviceName + ' : ' + content.message;
  			} else {
  				msg += 'Server response is empty';
  			}
			
			
			Sbi.exception.ExceptionHandler.showErrorMessage(msg, response.statusText);
		});
		
		return store;
		
	}
	
});