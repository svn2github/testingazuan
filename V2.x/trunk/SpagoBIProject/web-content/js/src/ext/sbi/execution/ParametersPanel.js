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
	if (c.parameters) {
		this.parametersPreference = c.parameters;
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
	
	//var cw = 1/c.columnNo;
	var w = (350 * c.columnNo) + 40;
	var columnsBaseConfig = [];
	for(var i = 0; i < c.columnNo; i++) {		
		columnsBaseConfig[i] = {
			//columnWidth: cw,
			width: 350,
            layout: 'form',
            border: false,
            bodyStyle:'padding:5px 5px 5px 5px'
		}
	}
	

	c = Ext.apply({}, c, {
		labelAlign: c.labelAlign,
        border: false,
        bodyStyle:'padding:10px 0px 10px 10px',
        autoScroll: true,
        items: [{
            layout:'column',
            width: w, 
            border: false,
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
	
    this.addEvents('beforesynchronize', 'synchronize', 'readyforexecution');	
};

Ext.extend(Sbi.execution.ParametersPanel, Ext.FormPanel, {
    
    services: null
    , executionInstance: null
    , parametersPreference: null
    
    , fields: null
    , columns: null
    
    
   
    // ----------------------------------------------------------------------------------------
    // public methods
    // ----------------------------------------------------------------------------------------
    
    
    , synchronize: function( executionInstance ) {
		var sync = this.fireEvent('beforesynchronize', this, executionInstance, this.executionInstance);
		this.executionInstance = executionInstance;
		this.loadParametersForExecution( this.executionInstance );
	}
	
	, getFormState: function() {
		var state;
		
		state = {};
		for(p in this.fields) {
			var field = this.fields[p];
			var value = field.getValue();
			state[field.name] = value;
			var rawValue = field.getRawValue();
			if (rawValue !== undefined) {
				// TODO to improve: the value of the field should be an object with actual value and its description
				// Conflicts with other parameters are avoided since the parameter url name max lenght is 20
				state[field.name + '_field_visible_description'] = rawValue;
			}
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
				var fieldDescription = fieldName + '_field_visible_description';
				var rawValue = state[fieldDescription];
				if (rawValue !== undefined && rawValue != null) {
					this.fields[fieldName].setRawValue( rawValue );
				}
			}
		}
	}
	
	, applyViewPoint: function(v) {
		for(var p in v) {
			var str = '' + v[p];
			if(str.split(';').length > 1) {
				v[p] = str.split(';');
			}
		}
		this.setFormState(v);
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
		
		// clears the form
		for(p in this.fields) {
			// if input field has an element (it means that the field was displayed)
			if (this.fields[p].el !== undefined) {
				// retrieves the element containing label plus input field and removes it
				var el = this.fields[p].el.up('.x-form-item');
				this.columns[this.fields[p].columnNo].remove( this.fields[p], true );
				el.remove();
			}
		}
		
		this.fields = {};
		var preferenceState = undefined;
		if (this.parametersPreference) {
			preferenceState = Ext.urlDecode(this.parametersPreference);
		}
		
		var nonTransientField = 0;
		for(var i = 0; i < parameters.length; i++) {
			var field = this.createField( executionInstance, parameters[i] );
			if(parameters[i].valuesCount !== undefined && parameters[i].valuesCount == 1) {
				field.isTransient = true;
				field.setValue(parameters[i].value);
				this.fields[parameters[i].id] = field;
			} else if (preferenceState !== undefined && preferenceState[parameters[i].id] !== undefined) {
				field.isTransient = true;
				field.setValue(preferenceState[parameters[i].id]);
				this.fields[parameters[i].id] = field;
			} else {
				field.isTransient = false;
				field.columnNo = (nonTransientField++)%this.columns.length;
				this.fields[parameters[i].id] = field;
				this.columns[field.columnNo].add( field );
			}
		}
		
		
		if(parameters.length == 0) {
			Ext.DomHelper.append(this.body, '<div class="x-grid-empty">' + LN('sbi.execution.parametersselection.noParametersToBeFilled') + '</div>');
		} else {
			var thereAreParametersToBeFilled = false;
			var o = this.getFormState();
			for(p in o) {
				// must check is this.fields[p] is undefined because form state contains also parameters' descriptions
				if(this.fields[p] != undefined && this.fields[p].isTransient === false) {
					thereAreParametersToBeFilled = true;
					break;
				}
			}
			if(thereAreParametersToBeFilled !== true) {
				Ext.DomHelper.append(this.body, '<div class="x-grid-empty">' + LN('sbi.execution.parametersselection.noParametersToBeFilled') + '</div>');
			}
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
		
		if(parameters.length == 0) {
			this.fireEvent('readyforexecution', this);
		} else 
		/*if (this.parametersPreference) {
			var readyForExecution = true;
			var preferenceState = Ext.urlDecode(this.parametersPreference);
			this.setFormState(preferenceState);
			var o = this.getFormState();
			for(p in o) {
				if(o[p] !== preferenceState[p] && this.fields[p].isTransient === false) {
					readyForExecution = false;
					break;
				}
			}
			if(readyForExecution === true) {
				this.fireEvent('readyforexecution', this);
			}
		} else 
		*/
		{
			var readyForExecution = true;
			var o = this.getFormState();
			for(p in o) {
				// must check is this.fields[p] is undefined because form state contains also parameters' descriptions
				if(this.fields[p] != undefined && this.fields[p].isTransient === false) {
					readyForExecution = false;
					break;
				}
			}
			if(readyForExecution === true) {
				this.fireEvent('readyforexecution', this);
			}
		}
		
		this.fireEvent('synchronize', this, readyForExecution, this.parametersPreference);
		
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
				var p = Sbi.commons.JSON.encode(this.getFormState());
				o.params = o.params || {};
				o.params.PARAMETERS = p;
				return true;
			}, this);
			
			store.load(/*{params: param}*/);
			
			field = new Ext.form.ComboBox(Ext.apply(baseConfig, {
				tpl: '<tpl for="."><div ext:qtip="{label} ({value}): {description}" class="x-combo-list-item">{label}</div></tpl>'
                , editable  : true			    
			    , forceSelection : false
			    , store :  store
			    , displayField:'label'
			    , valueField:'value'
			    , emptyText: ''
			    , typeAhead: false
			    //, typeAheadDelay: 1000
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
			
		} else if(p.selectionType === 'LIST' || p.selectionType ===  'CHECK_LIST') {
			
			var params = Ext.apply({}, {
				PARAMETER_ID: p.id
				, MODE: 'complete'
			}, executionInstance);
			
			var store = this.createStore();
			store.on('beforeload', function(store, o) {
				var p = Sbi.commons.JSON.encode(this.getFormState());
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
				baseConfig.format = Sbi.config.localizedDateFormat;
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