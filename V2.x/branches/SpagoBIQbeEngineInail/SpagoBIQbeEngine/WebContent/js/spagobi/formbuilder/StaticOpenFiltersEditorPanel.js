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
  * - Davide Zerbetto (davide.zerbetto@eng.it)
  */

Ext.ns("Sbi.formbuilder");

Sbi.formbuilder.StaticOpenFiltersEditorPanel = function(openFilters, config) {
	
	var defaultSettings = {
		// set default values here
		title: 'Static open filters'
        , layout: 'column'
		, frame: true
		, autoScroll: true
		, autoWidth: true
		, autoHeight: true
	};
	if (Sbi.settings && Sbi.settings.formbuilder && Sbi.settings.formbuilder.staticOpenFiltersEditorPanel) {
		defaultSettings = Ext.apply(defaultSettings, Sbi.settings.formbuilder.staticOpenFiltersEditorPanel);
	}
	
	var c = Ext.apply(defaultSettings, config || {});
	this.baseConfig = c;
	
	var params = {LIGHT_NAVIGATOR_DISABLED: 'TRUE'};
	this.services = new Array();
	this.services['getFilterValuesService'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'GET_FILTER_VALUES_ACTION'
		, baseParams: params
	});
	
	this.init(openFilters);
	
	Ext.apply(c, {
		tools: this.tools
  		, items: this.contents
	});
	
	// constructor
    Sbi.formbuilder.StaticOpenFiltersEditorPanel.superclass.constructor.call(this, c);
    
    this.on('render', this.initDD, this);
    
    //this.doLayout();
    
};

Ext.extend(Sbi.formbuilder.StaticOpenFiltersEditorPanel, Ext.Panel, {

	contents: null
	, empty: null
	, emptyMsgPanel: null
	
	, loadContents: function(staticFiltersConf) {
		Sbi.qbe.commons.Utils.unimplementedFunction('loadContents');
	}
	
	, initDD: function() {
		this.removeListener('render', this.initDD, this);
		this.dropTarget = new Sbi.formbuilder.StaticOpenFiltersEditorPanelDropTarget(this);
	}

	, init: function(openFiltersConf) {
		if(openFiltersConf !== undefined) {
			this.loadContents(openFiltersConf);	
		} else {
			this.initEmptyMsgPanel();
			this.initTools();
			this.contents = [this.emptyMsgPanel];
		}
	}
	
	, initEmptyMsgPanel: function() {
		this.empty = true;
		this.emptyMsgPanel = new Ext.Panel({
			html: 'drag a field here to create a new static open filter'
		});
	}
	
	, initTools: function() {
		this.tools = [];
		this.tools.push({
		    id:'delete',
		    qtip: 'clear all',
		    handler: function(event, toolEl, panel){
		  		this.clearContents();
		    }
		    , scope: this
		});
	}
	
	, clearContents: function() {
		Sbi.qbe.commons.Utils.unimplementedFunction('clearContents');
	}
	
	, addFilter: function(openFilter) {
		this.remove(this.emptyMsgPanel);
		var field = this.createField(openFilter);
		
		var panelTbar = new Ext.Toolbar({
			items: [
			    '->' , {
					text: 'Remove',
					handler: function() {this.ownerCt.destroy();},
					scope: panelTbar
			    }
			  ]
		});
		
		var fieldPanel = new Ext.Panel({
			items: [field]
			, layout: 'form' // form layout required: input field labels are displayed only with this layout
			, width: 350
			, tbar: panelTbar
		});
		
		this.add(fieldPanel);
		this.doLayout();
	}
	
	, createStore: function(openFilter) {
		
		var entityId = (openFilter.query != undefined && openFilter.query.field != undefined) ? openFilter.query.field : openFilter.field;
		var orderField = (openFilter.query != undefined && openFilter.query.orderField != undefined) ? openFilter.query.orderField : undefined;
		var orderType = (openFilter.query != undefined && openFilter.query.orderType != undefined) ? openFilter.query.orderType : undefined;
		
		var store = new Ext.data.JsonStore({
			url: this.services['getFilterValuesService']
		});
		var baseParams = {'ENTITY_ID': entityId, 'ORDER_ENTITY': orderField, 'ORDER_TYPE': orderType};
		store.baseParams = baseParams;
		
		store.on('loadexception', function(store, options, response, e) {
			Sbi.exception.ExceptionHandler.handleFailure(response, options);
		});
		
		return store;
		
	}
	
	, createField: function( openFilter ) {
		var field;
		
		var baseConfig = {
	       fieldLabel: openFilter.text
		   , name : openFilter.id
		   , width: this.baseConfig.fieldWidth
		   , allowBlank: true
		};
		
		var store = this.createStore(openFilter);
		
		var maxSelectionNumber = 1;
		if (openFilter.singleSelection === undefined || openFilter.singleSelection === null || openFilter.singleSelection === true) {
			maxSelectionNumber = 1;
		} else {
			maxSelectionNumber = openFilter.maxSelectedNumber;
		}
		
		field = new Ext.ux.form.SuperBoxSelect(Ext.apply(baseConfig, {
			editable: true			    
		    , forceSelection: false
		    , store: store
		    , displayField: 'column-1'
		    , valueField: 'column-1'
		    , emptyText: ''
		    , typeAhead: false
		    , triggerAction: 'all'
		    , selectOnFocus: true
		    , autoLoad: false
		    , maxSelection: maxSelectionNumber
		    , width: 200
		    , maxHeight: 250
		}));

		return field;
	}
	
});

/*
Ext.extend(Sbi.formbuilder.StaticOpenFiltersEditorPanel, Ext.Panel, {
    
	services: null
	, contents: null
	, empty: null
	, emptyMsgPanel: null
	, tools: null
	
	
	// --------------------------------------------------------------------------------
	// public methods
	// --------------------------------------------------------------------------------
		
	, loadContents: function(staticFiltersConf) {
		Sbi.qbe.commons.Utils.unimplementedFunction('loadContents');
		this.initEmptyMsgPanel();
		this.initTools();
		this.contents = [this.emptyMsgPanel];	
	}

	, clearContents: function() {
		Sbi.qbe.commons.Utils.unimplementedFunction('clearContents');
	}
	
	, addStaticClosedXORFilters: function(staticFiltersGroupConf) {
		if(this.empty === true) {
			this.remove(0, true);
			this.doLayout();
			this.empty = false;
		}
				
		var staticFiltersForm = new Sbi.formbuilder.StaticClosedXORFiltersEditorPanel(staticFiltersGroupConf);		
		this.add(staticFiltersForm);
		this.doLayout();	
	}

	// --------------------------------------------------------------------------------
	// private methods
	// --------------------------------------------------------------------------------
	
	, init: function(staticFiltersConf) {
		if(staticFiltersConf !== undefined) {
			this.loadContents(staticFiltersConf);	
		} else {
			this.initEmptyMsgPanel();
			this.initTools();
			this.contents = [this.emptyMsgPanel];
		}
	}
	
	, initEmptyMsgPanel: function() {
		this.empty = true;
		this.emptyMsgPanel = new Ext.Panel({
			html: 'drag a field here to create a new static filter'
		});
	}

	, initTools: function() {
		this.tools = [];
		
		this.tools.push({
		    id:'plus',
		    qtip: 'Add static closed filter',
		    handler: function(event, toolEl, panel){
		  		this.addStaticClosedXORFilters();
		    }
		    , scope: this
		});
		
		this.tools.push({
		    id:'delete',
		    qtip: 'clear all',
		    handler: function(event, toolEl, panel){
		  		this.clearContents();
		    }
		    , scope: this
		});
	}
	
	
	
	

	   
	
  	
});
*/