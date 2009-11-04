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

Sbi.formviewer.GroupingVariablesPanel = function(groupingVariables, config) {
	
	var defaultSettings = {
		// set default values here
		title: LN('sbi.formviewer.groupingvariablespanel.title')
		, columnNo: 2
		, columnWidth: 350
		, labelAlign: 'left'
		, fieldWidth: 200	
		, maskOnRender: false
		, frame: true
		, autoScroll: true
		, autoWidth: true
	};
	if (Sbi.settings && Sbi.settings.formviewer && Sbi.settings.formviewer.groupingVariablesPanel) {
		defaultSettings = Ext.apply(defaultSettings, Sbi.settings.formviewer.groupingVariablesPanel);
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
    Sbi.formviewer.GroupingVariablesPanel.superclass.constructor.call(this, c);
    
	var columnContainer = this.items.get(0);
	this.columns = [];
	for(var i = 0; i < c.columnNo; i++) {
		this.columns[i] = columnContainer.items.get(i);
	}

	this.init(groupingVariables);
	
};

Ext.extend(Sbi.formviewer.GroupingVariablesPanel, Ext.form.FormPanel, {
    
	services: null
	   
	// private methods
	   
	, init: function(groupingVariables) {
		var combo_1 = this.createFieldCombo( groupingVariables[0], 0 );
		var combo_2 = this.createFieldCombo( groupingVariables[1], 1 );
		this.columns[0].add( combo_1 );
		this.columns[1].add( combo_2 );
		this.doLayout();
	
	}

	, createFieldCombo: function(groupingVariable, pos) {
		
		var store = new Ext.data.JsonStore({
			data: groupingVariable.admissibleFields,
		    fields: ['field', 'text']
		});
		
		var combo = new Ext.form.ComboBox({
			name: groupingVariable.id
            , editable: false
            , fieldLabel: pos == 0 ? LN('sbi.formviewer.groupingvariablespanel.firstvariable') : LN('sbi.formviewer.groupingvariablespanel.secondvariable')
		    , forceSelection: false
		    , store: store
		    , mode : 'local'
		    , triggerAction: 'all'
		    , displayField: 'text'
		    , valueField: 'field'
		    , emptyText: ''
		});

		return combo;
	}
	   
	// public methods
	
	, getFormState: function() {
		var state = this.getForm().getValues();
		return state;
	}
  	
});