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
  * - by Andrea Gioia (andrea.gioia@eng.it)
  */

Ext.ns("Sbi.formbuilder");

Sbi.formbuilder.StaticCloseFilterWizard = function(config) {
	
	var defaultSettings = {
		// set default values here
		title: 'Static close filter definition'
		, autoScroll: true
		, width: 550
		, height: 340
		, baseState: {
			filterTitle: undefined		
		}
		, modal: true
		, resizable: false
	};
	if (Sbi.settings && Sbi.settings.formbuilder && Sbi.settings.formbuilder.staticClosedXORFiltersWizard) {
		defaultSettings = Ext.apply(defaultSettings, Sbi.settings.formbuilder.staticClosedXORFiltersWizard);
	}
	var c = Ext.apply(defaultSettings, config || {});
	
	Ext.apply(this, c);
	
	this.services = this.services || new Array();	
	this.services['getQueryFields'] = this.services['getQueryFields'] || Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'GET_QUERY_FIELDS_ACTION'
		, baseParams: new Object()
	});
	
	this.init();
	
	c = Ext.apply(c, {
		layout: 'fit',
		width: this.width,
		height: this.height,
		closeAction:'hide',
		plain: true,
		title: this.title,
		items: [this.formPanel]
	});
	
	// constructor
    Sbi.formbuilder.StaticCloseFilterWizard.superclass.constructor.call(this, c);
    
    if(this.hasBuddy === 'true') {
		this.buddy = new Sbi.commons.ComponentBuddy({
    		buddy : this
    	});
	}
	
    this.addEvents('apply');
    
};

Ext.extend(Sbi.formbuilder.StaticCloseFilterWizard, Ext.Window, {

	formPanel: null
	, filterTitleField: null
	, baseState: null
	, targetFilter: null
	, hasBuddy: null
    , buddy: null
	
	
	// --------------------------------------------------------------------------
	// Public
	// --------------------------------------------------------------------------
	
	, getFormState: function() {
		var s = {};
		
		s.text = this.filterTitleField.getValue();
		s.leftOperandValue = this.leftOperandField.getValue();
		s.leftOperandDesc = this.leftOperandField.getValue();
		s.operator = this.operatorField.getValue();
		s.rightOperandValue = this.rightOperandField.getValue();
		
		return s;
	}

	, setFormState: function(s) {
		this.filterTitleField.setValue(s.text);
		this.leftOperandField.setValue(s.leftOperandValue);
		this.operatorField.setValue(s.operator);
		this.rightOperandField.setValue(s.rightOperandValue);
	}
	
	, resetFormState: function() {
		this.filterTitleField.reset();
		this.leftOperandField.reset();
		this.operatorField.reset();
		this.rightOperandField.reset();
	}
	
	, setTarget: function(targetFilter) {
		this.targetFilter = targetFilter;
		
		if(this.targetFilter === null) {
			this.resetFormState();
		} else {
			this.setFormState(this.targetFilter.getContents());
		}
	}
	
	, getTarget: function() {
		return this.targetFilter;
	}
	
	// --------------------------------------------------------------------------
	// Private
	// --------------------------------------------------------------------------
	
	, init: function() {
		var items = [];
		
		this.filterTitleField = new Ext.form.TextField({
			fieldLabel:'Name' ,
			name:'filterTitleField',
    		value: this.baseState.filterTitle,
    		allowBlank: false, 
    		inputType:'text',
    		maxLength:50,
    		width:250
    	});
    	items.push(this.filterTitleField);
    	
    	var s = new Ext.data.JsonStore({
    		root: 'results'
    		, fields: ['id', 'alias']
    		, url: this.services['getQueryFields']
    	});
    	
    	
    	this.leftOperandField = new Sbi.widgets.LookupField({
    		fieldLabel:'Field',
			name: 'leftOperand',
    		store: s,
    		valueField: 'id',
    		displayField: 'alias',
    		descriptionField: 'alias',
    		cm: new Ext.grid.ColumnModel([
	    		new Ext.grid.RowNumberer(),
	    		{
	    			header: "Filed",
	    		    dataIndex: 'alias',
	    		    width: 75
	    		}
    		])
    	});
    	this.leftOperandField.on('change', this.onFormStateChange, this);
    	items.push(this.leftOperandField);
    	
    	this.operatorField = new Sbi.qbe.FilterComboBox({
    		fieldLabel:'Operator',
    		name:'operator'
    	});
    	this.operatorField.on('change', this.onFormStateChange, this);
    	items.push(this.operatorField);
    	
    	this.rightOperandField = new Ext.form.TextField({
    		fieldLabel:'Value' ,
			name:'rightOperand',
    	});
    	this.rightOperandField.on('change', this.onFormStateChange, this);
    	this.rightOperandField.on('keydown', this.onFormStateChangeX, this);
    	
    	
    	
    	items.push(this.rightOperandField);
    	
    	
    	
    	var sm = new Ext.grid.RowSelectionModel({singleSelect:true});
    	sm.on('rowselect', this.onRowSelect, this);
    	
    	this.filterGrid = new Sbi.qbe.FilterGridPanel({
    		//title: 'Expand this panel to add other filters...'
    		//, collapsible: true
    		//, titleCollapse: true
    		//, collapsed: true
    		gridHeight: 140
    		, gridStyle: 'padding:0px'
    		, sm: sm
    		, enableTbExpWizardBtn: false
    		, columns : {
				'filterId': {hideable: true, hidden: true, sortable: false}
				, 'filterDescripion': {hideable: true, hidden: true, sortable: false}
				, 'leftOperandDescription': {hideable: false, hidden: false, sortable: false}
				, 'leftOperandType': {hideable: true, hidden: true, sortable: false}
				, 'operator': {hideable: false, hidden: false, sortable: false}
				, 'rightOperandDescription': {hideable: false, hidden: false, sortable: false}				
				, 'rightOperandType': {hideable: true, hidden: true, sortable: false}
				, 'booleanConnector': {hideable: true, hidden: false, sortable: false}	
				, 'deleteButton': {hideable: true, hidden: true, sortable: false}
				, 'promptable': {hideable: true, hidden: true, sortable: false}	
			}
    	});
    	this.filterGrid.on('render', function(){
    		this.filterGrid.addFilter();
    	}, this);
    	items.push(this.filterGrid);
    	
    	
    	this.formPanel = new Ext.form.FormPanel({
    		frame:true,
    		labelWidth: 80,
    		defaults: {
    			width: 225
    		},
    	    bodyStyle:'padding:5px 5px 0',
    	    buttonAlign : 'center',
    	    items: items,
    	    buttons: [{
    			text: 'Save',
    		    handler: function(){
    	    		this.fireEvent('apply', this, this.getTarget(), this.getFormState());
                	this.hide();
            	}
            	, scope: this
    	    },{
    		    text: 'Cancel',
    		    handler: function(){
                	this.hide();
            	}
            	, scope: this
    		}]
    	});
    	
    	
	}
	
	, onRowSelect: function(selectionModel, rowIndex, r) {
		//alert(r.data.toSource());
	}
	
	, onFormStateChange: function(field, newValue, oldValue) {
		alert('keydown');
	}
	
	, onFormStateChange: function(field, newValue, oldValue) {
		alert('change to ' + newValue);
		alert(field.name);
		var r = this.filterGrid.sm.getSelected();
		if(r === undefined) {
			this.filterGrid.sm.selectRow(this.filterGrid.store.getCount()-1);
			r = this.filterGrid.sm.getSelected();
		}		
		var i = this.filterGrid.store.indexOf(r);
		
		if(field.name === 'leftOperand') {
			
		} else if(field.name === 'operator') {
			this.filterGrid.modifyFilter({operator: newValue}, i);
		} else if(field.name === 'rightOperand') {
			this.filterGrid.modifyFilter({rightOperandDescription: newValue, rightOperandValue: newValue}, i);
		}
	}
	
});