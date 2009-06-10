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


Ext.ns("Sbi.execution");

Sbi.execution.ParametersPanel = function(config) {
	
	var c = Ext.apply({
		columnNo: 3
		, labelAlign: 'left'
	}, config || {});
	
	
	this.parametersPreference = undefined;
	if (config.parameters) {
		this.parametersPreference = config.parameters;
	}
	
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
	
	var cw = 1/c.columnNo;
	var columnsBaseConfig = [];
	for(var i = 0; i < c.columnNo; i++) {		
		columnsBaseConfig[i] = {
			columnWidth: cw,
            layout: 'form',
            border: false,
            bodyStyle:'padding:5px 5px 5px 5px'
		}
	}
  
	c = Ext.apply({}, c, {
		labelAlign: c.labelAlign,
        border: false,
        bodyStyle:'padding:10px 0px 10px 10px',
        items: [{
            layout:'column',
            border: false,
            autoScroll: true,
            items: columnsBaseConfig
        }]
	});
	
	// constructor
    Sbi.execution.ParametersPanel.superclass.constructor.call(this, c);
	
	var columnContainer = this.items.get(0);
	this.columns = [];
	for(var i = 0; i < c.columnNo; i++) {
		this.columns[i] = columnContainer.items.get(i);
	}
	
    //this.addEvents();	
};

Ext.extend(Sbi.execution.ParametersPanel, Ext.FormPanel, {
    
    services: null
    , fields: null
    , columns: null
    , parametersPreference: null
   
    // ----------------------------------------------------------------------------------------
    // public methods
    // ----------------------------------------------------------------------------------------
    
    /**
     * load data from server in order to synchronize panel content with the given 
     * execution instance (i.e. doc + role)
     */
    , synchronize: function( executionInstance ) {
		this.loadParametersForExecution( executionInstance );
	}
	
	, getFormState: function() {
		var state;
		
		state = {};
		for(p in this.fields) {
			var field = this.fields[p];
			var value = field.getValue();
			state[field.getName()] = value;
		}
		
		return state;
	}
	
	, setFormState: function( state ) {
		var state;	
		for(p in state) {
			var fieldName = p;
			var fieldValue = state[p];
			if(this.fields[fieldName]) {
				this.fields[fieldName].setValue( fieldValue );
			}
		}
	}
	
	
	, clear: function() {
		for(p in this.fields) {
			this.fields[p].reset();
		}
	}
	
	// ----------------------------------------------------------------------------------------
	// private methods
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
		this.fields = {};
		for(var i = 0; i < parameters.length; i++) {
			//alert(parameters[i].toSource());
			var field = this.createField( executionInstance, parameters[i] );
			this.fields[parameters[i].id] = field;
			this.columns[i%this.columns.length].add( field );
		}
		this.doLayout();
		
		for(var j = 0; j < parameters.length; j++) {
			
			if(parameters[j].dependencies.length > 0) {
				var field = this.fields[parameters[j].id];
				var p = parameters[j];
				
				field.on('focus', function(f){
					for(var i = 0; i < f.dependencies.length; i++) {
						var field = this.fields[ f.dependencies[i] ];
						field.getEl().addClass('x-form-dependent-field');                         
					}		
					//alert(f.getName() + ' get focus');
				}, this, {delay:250});
				
				field.on('blur', function(f){
					//alert(f.getName() + ' lose focus');
					for(var i = 0; i < f.dependencies.length; i++) {
						var field = this.fields[ f.dependencies[i] ];
						field.getEl().removeClass('x-form-dependent-field');                         
					}
				}, this);
				
				for(var i = 0; i < field.dependencies.length; i++) {
					var f = this.fields[ field.dependencies[i] ];
					f.dependants = f.dependants || [];
					f.dependants.push( field.getName() );                      
				}	
			}			
		}
		
		
		for(var p in this.fields) {
			this.fields[p].on('change', function(f, newVal, oldVal) {
				if(f.dependants !== undefined) {
					for(var i = 0; i < f.dependants.length; i++) {
						var field = this.fields[ f.dependants[i] ];
						if(field.initialConf.selectionType === 'COMBOBOX'){ 
							field.store.load();
						}
					}
				}
			}, this); 
			
		}
		
		if (this.parametersPreference) {
			var preferenceState = Ext.urlDecode(this.parametersPreference);
			this.setFormState(preferenceState);
		}
		
	}
	
	, createField: function( executionInstance, p ) {
		var field;
		
		//alert(p.id + ' - ' + p.selectionType + ' - ' + !p.mandatory);
		var baseConfig = {
	       fieldLabel: p.label
		   , name : p.id
		   , width: 200
		   , allowBlank: !p.mandatory
		};
		
		var labelStyle = '';
		labelStyle += (p.mandatory === true)?'font-weight:bold;': '';
		labelStyle += (p.dependencies.length > 0)?'font-style: italic;': '';
		baseConfig.labelStyle = labelStyle;
		
		//if(p.dependencies.length > 0) baseConfig.fieldClass = 'background-color:yellow;';
		
		if(p.selectionType === 'COMBOBOX') {
			var baseParams = {};
			Ext.apply(baseParams, executionInstance);
			Ext.apply(baseParams, {
				PARAMETER_ID: p.id
				, MODE: 'simple'
			});
			
			var store = this.createStore();
			store.baseParams  = baseParams;
			store.on('beforeload', function(store, o) {
				var p = Sbi.commons.Format.toString(this.getFormState());
				o.params = o.params || {};
				o.params.PARAMETERS = p;
				return true;
			}, this);
			
			field = new Ext.form.ComboBox(Ext.apply(baseConfig, {
				tpl: '<tpl for="."><div ext:qtip="{label} ({value}): {description}" class="x-combo-list-item">{label}</div></tpl>'
                , editable  : true			    
			    , forceSelection : true
			    , store :  store
			    , displayField:'label'
			    , valueField:'value'
			    , emptyText: ''
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
					
			store.load(/*{params: param}*/);
						
		} else if(p.selectionType === 'LIST' || p.selectionType ===  'CHECK_LIST') {
			
			var params = Ext.apply({}, {
				PARAMETER_ID: p.id
				, MODE: 'complete'
			}, executionInstance);
			
			var store = this.createStore();
			store.on('beforeload', function(store, o) {
				var p = Sbi.commons.Format.toString(this.getFormState());
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
		
		field.initialConf = p;
		field.dependencies = p.dependencies;
		
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