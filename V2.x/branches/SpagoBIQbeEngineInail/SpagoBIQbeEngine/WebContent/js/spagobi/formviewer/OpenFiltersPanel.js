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

Ext.ns("Sbi.formviewer");

Sbi.formviewer.OpenFiltersPanel = function(openFilters) {
	
	var settings = {
			columnNo: 3
			, columnWidth: 350
			, labelAlign: 'left'
			, fieldWidth: 200	
			, maskOnRender: false
	};
	
	if (Sbi.settings && Sbi.settings.formviewer && Sbi.settings.formviewer.openFiltersPanel) {
		settings = Sbi.settings.formviewer.openFiltersPanel;
	}
	
	var params = {LIGHT_NAVIGATOR_DISABLED: 'TRUE'};
	this.services = new Array();
	this.services['getFilterValuesService'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'GET_FILTER_VALUES_ACTION'
		, baseParams: params
	});
	
	var c = Ext.apply({}, settings || {});
	this.baseConfig = c;
	
	this.formWidth = (c.columnWidth * c.columnNo) + 40;
	var columnsBaseConfig = [];
	for(var i = 0; i < c.columnNo; i++) {		
		columnsBaseConfig[i] = {
			width: c.columnWidth,
            layout: 'form',
            border: false,
            bodyStyle:'padding:5px 5px 5px 5px'
		}
	}

	c = Ext.apply({}, c, {
		title: 'Open Filters',
		labelAlign: c.labelAlign,
        border: false,
        frame: true,
        autoScroll: true,
        items: [{
            layout:'column',
            width: this.formWidth, 
            border: false,
            items: columnsBaseConfig
        }]
	});
	
	// constructor
    Sbi.formviewer.OpenFiltersPanel.superclass.constructor.call(this, c);

	var columnContainer = this.items.get(0);
	this.columns = [];
	for(var i = 0; i < c.columnNo; i++) {
		this.columns[i] = columnContainer.items.get(i);
	}
	
	this.init(openFilters);
    
};

Ext.extend(Sbi.formviewer.OpenFiltersPanel, Ext.FormPanel, {
    
	services: null
	, fields: null
	   
	// private methods
	   
	, init: function(openFilters) {
		
		this.fields = {};
	
		var fieldsCounter = 0;
		for(var i = 0; i < openFilters.length; i++) {
			var field = this.createField( openFilters[i] );
			field.columnNo = (fieldsCounter++)%this.columns.length;
			this.fields[openFilters[i].id] = field;
			this.columns[field.columnNo].add( field );
		}
		
		this.doLayout();
	
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
		
		field = new Ext.form.ComboBox(Ext.apply(baseConfig, {
			//tpl: '<tpl for="."><div ext:qtip="{label} ({value}): {description}" class="x-combo-list-item">{label}</div></tpl>'
            editable: true			    
		    , forceSelection: false
		    , store: store
		    , displayField: 'column-1'
		    , valueField: 'column-1'
		    , emptyText: ''
		    , typeAhead: false
		    //, typeAheadDelay: 1000
		    , triggerAction: 'all'
		    , selectOnFocus:true
		    , autoLoad: false
		}));
		
		/*
		field = new Sbi.widgets.LookupField(Ext.apply(baseConfig, {
			  store: store
				, params: params
				, singleSelect: openFilter.singleSelection
		}));
		*/
		
		return field;
	}


	, createStore: function(openFilter) {
		
		var store = new Ext.data.JsonStore({
			url: this.services['getFilterValuesService'] + '&ENTITY_ID=' + openFilter.field
		});
		
		store.on('loadexception', function(store, options, response, e) {
			Sbi.exception.ExceptionHandler.handleFailure(response, options);
		});
		
		return store;
		
	}	

	// public methods
	
	, setState: function(state) {
	
	}
	
	, getState: function() {
		var state = [];
		//for (var i = 0; i < this.forms.length; i++) {
		//	var aForm = this.forms[i];
		//	state.push(aForm.getState());
		//}
		return state;
	}
  	
});