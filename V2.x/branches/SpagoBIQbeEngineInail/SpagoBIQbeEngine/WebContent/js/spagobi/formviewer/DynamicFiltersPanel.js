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

Sbi.formviewer.DynamicFiltersPanel = function(dynamicFilters, config) {
	
	var defaultSettings = {
		// set default values here
		columnNo: 3
		, columnWidth: 350
		, labelAlign: 'left'
		, fieldWidth: 200	
		, maskOnRender: false
		, title: LN('sbi.formviewer.dynamicfilterspanel.title')
		, frame: true
		, autoScroll: true
		, autoWidth: true
	};
	if (Sbi.settings && Sbi.settings.qbe && Sbi.settings.qbe.dynamicFiltersPanel) {
		defaultSettings = Ext.apply(defaultSettings, Sbi.settings.qbe.dynamicFiltersPanel);
	}
	var c = Ext.apply(defaultSettings, config || {});

	this.baseConfig = c;
	
	var columnsBaseConfig = [];
	for(var i = 0; i < c.columnNo; i++) {
		columnsBaseConfig[i] = {
			width: c.columnWidth,
            layout: 'form',
            border: false,
            bodyStyle:'padding:5px 5px 5px 5px'
		}
	}
	
	Ext.apply(c, {
        items: [{
            layout:'column',
            border: false,
            items: columnsBaseConfig
        }]
	});
	
	// constructor
    Sbi.formviewer.DynamicFiltersPanel.superclass.constructor.call(this, c);
    
	var columnContainer = this.items.get(0);
	this.columns = [];
	for(var i = 0; i < c.columnNo; i++) {
		this.columns[i] = columnContainer.items.get(i);
	}

	this.init(dynamicFilters);
	
};

Ext.extend(Sbi.formviewer.DynamicFiltersPanel, Ext.form.FormPanel, {
    
	services: null
	   
	// private methods
	   
	, init: function(dynamicFilters) {
		this.fields = {};
		
		var fieldsCounter = 0;
		for(var i = 0; i < dynamicFilters.length; i++) {
			var combo = this.createFieldCombo( dynamicFilters[i] );
			var valuesInputs = this.createFieldValuesInput( dynamicFilters[i] );
			this.fields[dynamicFilters[i].id + '_combo'] = combo;
			for(var j = 0; j < valuesInputs.length; j++) {
				var aValueInput = valuesInputs[j];
				this.fields[dynamicFilters[i].id + '_value_' + j] = aValueInput;
			}
			this.columns[0].add( combo );
			this.columns[1].add( valuesInputs[0] );
			if (valuesInputs.length > 1) {
				this.columns[2].add( valuesInputs[1] );
			} else {
				this.columns[2].add( new Ext.form.Field({hidden: true, labelSeparator: ''}) );
			}
		}
		
		this.doLayout();
	
	}

	, createFieldCombo: function(dynamicFilter) {
		var store = new Ext.data.Store({
			data: dynamicFilter.admissibleFields
			, reader: new Ext.data.JsonReader({id: 'field'}, [
               {name:'field'},
               {name:'text'}
       	    ])
		});

		var combo = new Ext.form.ComboBox({
            editable: false
            , fieldLabel: LN('sbi.formviewer.dynamicfilterspanel.variable')
		    , forceSelection: false
		    , store: store
		    , mode : 'local'
		    , triggerAction: 'all'
		    , displayField: 'text'
		    , valueField: 'field'
		    , emptyText: ''
		    , typeAhead: false
		});

		return combo;
	}
	
	, createFieldValuesInput: function(dynamicFilter) {
		var valuesInput = [];
		if (dynamicFilter.operator.toUpperCase() === 'BETWEEN') {
			valuesInput[0] = new Ext.form.TextField({
				fieldLabel: LN('sbi.formviewer.dynamicfilterspanel.fromvalue')
			   , name : 'fromvalue'
			   , allowBlank: true
			});
			valuesInput[1] = new Ext.form.TextField({
				fieldLabel: LN('sbi.formviewer.dynamicfilterspanel.tovalue')
			   , name : 'tovalue'
			   , allowBlank: true
			});
		} else {
			valuesInput[0] = new Ext.form.TextField({
				fieldLabel: LN('sbi.formviewer.dynamicfilterspanel.fromvalue')
			   , name : 'value'
			   , allowBlank: true
			});
		}
		return valuesInput;
	}

	   
	// public methods
	
	, setState: function(state) {
	
	}
	
	, getState: function() {
	}
  	
});