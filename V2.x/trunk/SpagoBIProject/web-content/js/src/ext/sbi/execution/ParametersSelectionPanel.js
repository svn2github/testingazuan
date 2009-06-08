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

Sbi.execution.ParametersSelectionPanel = function(config) {
	
	var c = Ext.apply({
		columnNo: 3
		, labelAlign: 'left'
	}, config || {});
	
	
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
	
	
	this.init(c);
	
	c = Ext.apply({}, c, {
		layout: 'border',
		items: [{
			region:'center'
		    , border: false
		    , frame: false
		    , collapsible: false
		    , collapsed: false
		    , hideCollapseTool: true
		    , titleCollapse: true
		    , collapseMode: 'mini'
		    , split: true
		    , autoScroll: true
		    , layout: 'fit'
		    , items: [this.formPanel]
		}, {
			region:'south'
			, border: false
			, frame: false
			, collapsible: true
			, collapsed: false
			, hideCollapseTool: true
			, titleCollapse: true
			, collapseMode: 'mini'
			, split: true
			, autoScroll: true
			, height: 280
			, layout: 'fit'
			, items: [this.shortcutsPanel]
		}]
	});   
	
	
	
	// constructor
    Sbi.execution.ParametersSelectionPanel.superclass.constructor.call(this, c);
    
    //this.addEvents();	
};

Ext.extend(Sbi.execution.ParametersSelectionPanel, Ext.Panel, {
    
    services: null
    , fields: null
    , columns: null
    , formPanel: null
    , shortcutsPanel: null
   
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
		
		 this.subobjectsPanel.loadSubObjects( executionInstance );
	     this.snapshotsPanel.loadSubObjects( executionInstance );
	     this.viewpointsPanel.loadSubObjects( executionInstance );
	}

	, onParametersForExecutionLoaded: function( executionInstance, parameters ) {
		this.fields = [];
		for(var i = 0; i < parameters.length; i++) {
			//alert(parameters[i].toSource());
			this.fields[i] = this.createField( executionInstance, parameters[i] ); 
			this.columns[i%this.columns.length].add( this.fields[i] );
		}
		this.doLayout();
	}
	
	, getFormState: function() {
		var state;
		
		state = {};
		for(var i = 0;  i < this.fields.length; i++) {
			var item = this.fields[i];
			var value = item.getValue();
			state[item.getName()] = value;
		}
		
		return state;
	}
	
	
	, clear: function() {
		
	}
	
	// ----------------------------------------------------------------------------------------
	// private methods
	// ----------------------------------------------------------------------------------------
	
	, init: function( config ) {
		this.initFormPanel(config);
		this.initShortcutsPanel(config);
	}
	
	, initFormPanel: function( config ) {
		var cw = 1/config.columnNo;
		var columnsBaseConfig = [];
		for(var i = 0; i < config.columnNo; i++) {		
			columnsBaseConfig[i] = {
				columnWidth: cw,
	            layout: 'form',
	            border: false,
	            bodyStyle:'padding:5px 5px 5px 5px'
			}
		}
	  
		
		this.formPanel = new Ext.FormPanel({
		        labelAlign: config.labelAlign,
		        border: false,
		        bodyStyle:'padding:10px 0px 10px 10px',
		        items: [{
		            layout:'column',
		            border: false,
		            autoScroll: true,
		            items: columnsBaseConfig
		        }]
		});
		 
		var columnContainer = this.formPanel.items.get(0);
		this.columns = [];
		for(var i = 0; i < config.columnNo; i++) {
			this.columns[i] = columnContainer.items.get(i);
		}
		
		return this.formPanel;
	}
	
	, initShortcutsPanel: function( config ) {
		
		this.subobjectsPanel =  new Sbi.execution.SubobjectsPanel({border: false});
		this.snapshotsPanel =  new Sbi.execution.SnapshotsPanel({border: false});
		this.viewpointsPanel =  new Sbi.execution.ViewpointsPanel({border: false});
		
		this.shortcutsPanel = new Ext.Panel({
			layout:'accordion',
		    layoutConfig:{
		          animate:true
		    },
		    border: false,
		    items: [		        
		        this.viewpointsPanel
		      , this.subobjectsPanel
		      , this.snapshotsPanel		      
		    ]
		});			
		
		return this.shortcutsPanel;
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
		
		if(p.dependencies.length > 0) {
			field.on('focus', function(f){
				//f.el.addClass('x-form-dependent-field');
				//f.el.setStyle('background','#DFE8F6');

				//alert('pippo');
			});
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