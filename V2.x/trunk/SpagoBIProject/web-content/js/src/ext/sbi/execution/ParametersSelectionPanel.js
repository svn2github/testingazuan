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
   
   
    // public methods
	
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
		var state = {ParProduct : ['664'], ParMonth : ['1']};
		return state;
	}
	
	// private methods
	
	, clear: function() {
	
	}
	
	, createField: function( executionInstance, p ) {
		var field;
		
		alert(p.id + ' - ' + p.selectionType);
	
		
		if(p.selectionType === 'COMBOBOX') {
			var param = {};
			Ext.apply(param, executionInstance);
			Ext.apply(param, {
				PARAMETER_ID: p.id
				, MODE: 'simple'
			});
			
			
			var store = new Ext.data.Store(this.inputFieldOptionsStoreConfig);
			
			field = new Ext.form.ComboBox({
				tpl: '<tpl for="."><div ext:qtip="{label} ({value}): {description}" class="x-combo-list-item">{label}</div></tpl>',
                editable  : true,
			    fieldLabel : p.label,
			    forceSelection : true,
			    name : p.id,
			    store :  store,
			    displayField:'label',
			    valueField:'value',
			    emptyText: 'select value',
			    typeAhead: true,
			    triggerAction: 'all',
			    width: 150,
			    selectOnFocus:true,
			    autoLoad: false,
			    mode : 'local',
			    listeners: {
			    	'select': {
			       		fn: function(){}
			       		, scope: this
			    	}
			    }
			});				
						
			store.load({params: param});
						
		} else if(p.selectionType === 'LIST') {
			var params = {};
			Ext.apply(params, executionInstance);
			Ext.apply(params, {
				PARAMETER_ID: p.id
				, MODE: 'complete'
			});
			field = new Sbi.widgets.LookupField({
				fieldLabel: p.label
				, name : p.id
				,  width: 150
				, store: new Ext.data.Store(this.inputFieldOptionsStoreConfig)
				, params: params
			});
			
			/*
			field = new Ext.form.TriggerField({
				fieldLabel: p.label
				, name : p.id
				, triggerClass: 'x-form-search-trigger'
				,  width: 150
			});
			
			
			field.on("render", function(field) {
				field.trigger.on("click", function(e) {
					this.onLookUp(field, executionInstance); 
				}, this);
			}, this);
			*/
					
			
		} else if(p.selectionType ===  'CHECK_LIST') {		
			field = new Ext.form.TriggerField({
				fieldLabel: p.label
				, name : p.id
				, triggerClass: 'x-form-search-trigger'
				,  width: 150
			});
		} else { 
			field = new Ext.form.TriggerField({
				fieldLabel: p.label
				, name : p.id
				, triggerClass: 'x-form-search-trigger'
				,  width: 150
			});
		}
		
		return field;
	}
	
	, onLookUp: function(f, executionInstance) {
		
		var cm = new Ext.grid.ColumnModel([
		      new Ext.grid.RowNumberer(),
		      {
		    	  header: "Data",
		          dataIndex: 'data',
		          width: 75
		      }
		]);
		
		var store = new Ext.data.Store(this.inputFieldOptionsStoreConfig);
		store.on('metachange', function( store, meta ) {
			   meta.fields[0] = new Ext.grid.RowNumberer();
			   cm.setConfig(meta.fields);
		}, this);
		
		
		
		var win = new Ext.Window({
			title: 'Select value ...',   
            layout      : 'fit',
            width       : 500,
            height      : 300,
            closeAction :'hide',
            plain       : true,
            items       : [
            	new Ext.grid.GridPanel({
            		 store: store,
            	     cm: cm,
            	     frame: false,
            	     border:false,  
            	     collapsible:false,
            	     loadMask: true,
            	     viewConfig: {
            	        forceFit:true,
            	        enableRowBody:true,
            	        showPreview:true
            	     }
            	})
            ]
		});
		win.show(f);
		
		var param = {};
		Ext.apply(param, executionInstance);
		Ext.apply(param, {
			PARAMETER_ID: f.name
			, MODE: 'complete'
		});
		store.load({params: param});
	}
	
	
});