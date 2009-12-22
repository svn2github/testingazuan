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

Sbi.formbuilder.StaticOpenFilterEditorPanel = function(openFilter, config) {
	
	this.openFilter = openFilter;
	
	var defaultSettings = {
		// set default values here
		layout: 'form' // form layout required: input field labels are displayed only with this layout
		, frame: true
		, autoScroll: true
		, autoWidth: true
		, autoHeight: true
	};
	if (Sbi.settings && Sbi.settings.formbuilder && Sbi.settings.formbuilder.staticOpenFilterEditorPanel) {
		defaultSettings = Ext.apply(defaultSettings, Sbi.settings.formbuilder.staticOpenFilterEditorPanel);
	}
	
	var c = Ext.apply(defaultSettings, config || {});
	this.baseConfig = c;
	
	var params = {LIGHT_NAVIGATOR_DISABLED: 'TRUE'};
	this.services = new Array();
	this.services['getFilterValuesService'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'GET_FILTER_VALUES_ACTION'
		, baseParams: params
	});
	
	this.field = this.createField();
	
	var panelTbar = new Ext.Toolbar({
		items: [
		    '->' , {
				text: 'Edit',
				handler: function() {this.editFilter();},
				scope: this
		    } , {
				text: 'Remove',
				handler: function() {this.destroy();},
				scope: this
		    }
		  ]
	});
	
	Ext.apply(c, {
		items: [this.field]
		, tbar: panelTbar
	});
	
	// constructor
    Sbi.formbuilder.StaticOpenFilterEditorPanel.superclass.constructor.call(this, c);
    
};

Ext.extend(Sbi.formbuilder.StaticOpenFilterEditorPanel, Ext.Panel, {
	
	openFilter: null
	, field: null
	
	, createStore: function() {
		
		var entityId = (this.openFilter.query != undefined && this.openFilter.query.field != undefined) ? this.openFilter.query.field : this.openFilter.field;
		var orderField = (this.openFilter.query != undefined && this.openFilter.query.orderField != undefined) ? this.openFilter.query.orderField : undefined;
		var orderType = (this.openFilter.query != undefined && this.openFilter.query.orderType != undefined) ? this.openFilter.query.orderType : undefined;
		
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
	
	, createField: function() {
		var field;
		
		var baseConfig = {
	       fieldLabel: this.openFilter.text
		   , name : this.openFilter.id
		   , width: this.baseConfig.fieldWidth
		   , allowBlank: true
		};
		
		var store = this.createStore(this.openFilter);
		
		var maxSelectionNumber = 1;
		if (this.openFilter.singleSelection === undefined || this.openFilter.singleSelection === null || this.openFilter.singleSelection === true) {
			maxSelectionNumber = 1;
		} else {
			maxSelectionNumber = this.openFilter.maxSelectedNumber;
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
	
	, editFilter: function() {
		var staticOpenFilterWindow = new Sbi.formbuilder.StaticOpenFilterWizard(this.openFilter, {});
		staticOpenFilterWindow.show();
		staticOpenFilterWindow.on('apply', function(openFilter) {this.modifyFilter(openFilter);} , this);
	}
	
	, modifyFilter: function(openFilter) {
		this.openFilter = openFilter;
		this.remove(this.field);
		this.field = this.createField();
		this.add(this.field);
		this.doLayout();
	}
	
	, getOpenFilter: function() {
		return this.openFilter;
	}
	
});