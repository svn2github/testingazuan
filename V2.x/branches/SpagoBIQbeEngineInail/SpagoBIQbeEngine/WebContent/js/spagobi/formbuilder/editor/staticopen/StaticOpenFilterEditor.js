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

Ext.ns("Sbi.formbuilder");

Sbi.formbuilder.StaticOpenFilterEditor = function(config) {
	
	var defaultSettings = {
		
	};
	if (Sbi.settings && Sbi.settings.formbuilder && Sbi.settings.formbuilder.staticOpenFilterEditor) {
		defaultSettings = Ext.apply(defaultSettings, Sbi.settings.formbuilder.staticOpenFilterEditor);
	}
	var c = Ext.apply(defaultSettings, config || {});
		
	Ext.apply(this, c);
	
	
	var params = {LIGHT_NAVIGATOR_DISABLED: 'TRUE'};
	this.services = new Array();
	this.services['getFilterValuesService'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'GET_FILTER_VALUES_ACTION'
		, baseParams: params
	});
	
	this.init();
	
	// constructor
	Ext.apply(c, {
		items: [this.filter]
	});
    Sbi.formbuilder.StaticOpenFilterEditor.superclass.constructor.call(this, c);
};

Ext.extend(Sbi.formbuilder.StaticOpenFilterEditor, Ext.Panel,  {
    
	filter: null
	, store: null
	
	// filter conf
	, text: null
	, singleSelection: null
	, maxSelectedNumber: null
	, query: null
	, field: null
	, orderField: null
	, orderType: null
	
	// --------------------------------------------------------------------------------
	// public methods
	// --------------------------------------------------------------------------------
		
	, setContents: function(c) {
		
	}
	
	, getContents: function() {
		
	}
	
	// --------------------------------------------------------------------------------
	// private methods
	// --------------------------------------------------------------------------------
	
	, init: function() {
		var baseConfig = {
	       fieldLabel: this.text || 'pippo'
		   , name : this.id
		   , width: this.baseConfig.fieldWidth
		   , allowBlank: true
		};
		
		this.initStore();
		
		var maxSelectionNumber = 1;
		if (this.singleSelection === undefined || this.singleSelection === null || this.singleSelection === true) {
			maxSelectionNumber = 1;
		} else {
			maxSelectionNumber = this.maxSelectedNumber;
		}
		
		this.filter = new Ext.ux.form.SuperBoxSelect(Ext.apply(baseConfig, {
			editable: true			    
		    , forceSelection: false
		    , store: this.store
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
	}
		
	, initStore: function() {
		var entityId = (this.query != undefined && this.query.field != undefined) ? this.query.field : this.field;
		var orderField = (this.query != undefined && this.query.orderField != undefined) ? this.query.orderField : undefined;
		var orderType = (this.query != undefined && this.query.orderType != undefined) ? this.query.orderType : undefined;
		
		this.store = new Ext.data.JsonStore({
			url: this.services['getFilterValuesService']
		});
		var baseParams = {'ENTITY_ID': entityId, 'ORDER_ENTITY': orderField, 'ORDER_TYPE': orderType};
		this.store.baseParams = baseParams;
		
		this.store.on('loadexception', function(store, options, response, e) {
			Sbi.exception.ExceptionHandler.handleFailure(response, options);
		});
	}
});