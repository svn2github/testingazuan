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

Sbi.formbuilder.StaticCloseFilterGroupWizard = function(config) {
	
	var defaultSettings = {
		// set default values here
		title: LN('sbi.formbuilder.staticclosefiltergroupwizard.title')
		, autoScroll: true
		, width: 350
		, height: 300
		, baseState: {
			groupTitle: undefined,
			singleSelection: true,
			allowNoSelection: true			
		}
	};
	if (Sbi.settings && Sbi.settings.formbuilder && Sbi.settings.formbuilder.staticClosedXORFiltersWindow) {
		defaultSettings = Ext.apply(defaultSettings, Sbi.settings.formbuilder.staticClosedXORFiltersWindow);
	}
	var c = Ext.apply(defaultSettings, config || {});
	
	Ext.apply(this, c);
	
	this.init();
	
	c = Ext.apply(c, {
		layout: 'fit',
		width: this.width,
		height: this.height,
		closeAction:'hide',
		plain: true,
		modal: true,
		resizable: false,
		title: this.title,
		items: [this.formPanel]
	});
	
	// constructor
    Sbi.formbuilder.StaticCloseFilterGroupWizard.superclass.constructor.call(this, c);
    
    if(this.hasBuddy === 'true') {
		this.buddy = new Sbi.commons.ComponentBuddy({
    		buddy : this
    	});
	}
	
    this.addEvents('apply');
    
};

Ext.extend(Sbi.formbuilder.StaticCloseFilterGroupWizard, Ext.Window, {

	formPanel: null
	, groupTitleField: null
	, singleSelectionField: null
	, allowNoSelectionField: null
	, noSelectionTextField: null
	, baseState: null
	, targetFilterGroup: null
	, hasBuddy: null
    , buddy: null
	
	
	// --------------------------------------------------------------------------
	// Public
	// --------------------------------------------------------------------------
	
	, getFormState : function () {
		var s = {};
		
		s.groupTitle = this.groupTitleField.getValue(s);
		s.singleSelection = this.singleSelectionField['true'].getValue()
		s.allowNoSelection = this.allowNoSelectionField['true'].getValue();  
		s.noSelectionText = this.noSelectionTextField.getValue();
		return s;
	}

	, setFormState: function(s) {
		if(s.groupTitle) {
			this.groupTitleField.setValue(s.groupTitle);
		}
		
		if(s.singleSelection) {
			this.singleSelectionField['true'].setValue(s.singleSelection === true);
			this.singleSelectionField['false'].setValue(s.singleSelection === false);
		}
		
		if(s.allowNoSelection) {
			this.allowNoSelectionField['true'].setValue(s.allowNoSelection === true);
			this.allowNoSelectionField['false'].setValue(s.allowNoSelection === false);
		}
		
		if(s.noSelectionText) {
			this.noSelectionTextField.setValue(s.noSelectionText);
		}
	}
	
	, resetFormState: function() {
		this.groupTitleField.reset();
		
		var s = Ext.apply(this.baseState || {}, {
			singleSelection: true
			, allowNoSelection: false
			, noSelectionText: LN('sbi.formbuilder.staticclosefiltergroupwizard.fields.noselectiontext')
		})
		this.setFormState(s);
	}
	
	, setTarget: function(targetFilterGroup) {
		this.targetFilterGroup = targetFilterGroup;
		
		if(this.targetFilterGroup === null) {
			this.resetFormState();
		} else {
			this.setFormState(this.targetFilter.getContents());
		}
	}
	
	, getTarget: function() {
		return this.targetFilterGroup;
	}
	
	
	// --------------------------------------------------------------------------
	// Private
	// --------------------------------------------------------------------------
	
	, init: function() {
		var items = [];
		
		this.groupTitleField = new Ext.form.TextField({
			fieldLabel: LN('sbi.formbuilder.staticclosefiltergroupwizard.fields.grouptitle.label'),
    		name:'groupTitle',
    		value: this.baseState.groupTitle,
    		allowBlank: false, 
    		inputType:'text',
    		maxLength:150,
    		style:'margin-bottom:10px;'
    	});
    	items.push(this.groupTitleField);
    	
    	    	
    	this.singleSelectionField = {};
    	
    	this.singleSelectionField['true'] = new Ext.form.Radio({
    		fieldLabel: LN('sbi.formbuilder.staticclosefiltergroupwizard.fields.enablesingleselection.label'),
			boxLabel: LN('sbi.formbuilder.staticclosefiltergroupwizard.fields.enablesingleselection.yes'),
			name: 'singleSelection',
			checked: (this.baseState.singleSelection === true),
			inputValue: true
		});
    	//items.push(this.singleSelectionField['true']);
    	this.singleSelectionField['true'].on('check', function(cb, checked){
    		if(checked === true){
    			this.enableNoSelectionField(true);
    		}
    	}, this);
    	
    	
    	this.singleSelectionField['false'] = new Ext.form.Radio({
    		fieldLabel: '',
    		labelSeparator: '',
			boxLabel: LN('sbi.formbuilder.staticclosefiltergroupwizard.fields.enablesingleselection.no'),
			name: 'singleSelection',
			checked: (this.baseState.singleSelection === false),
            inputValue: false,
            style:'margin-bottom:20px;'
		});
    	//items.push(this.singleSelectionField['fasle']);
    	this.singleSelectionField['false'].on('check', function(cb, checked){
    		if(checked === true){
    			this.enableNoSelectionField(false);
    		}
    	}, this);
    	
    	
    	this.allowNoSelectionField = {};
    	
    	this.allowNoSelectionField['true'] = new Ext.form.Radio({
    		fieldLabel: LN('sbi.formbuilder.staticclosefiltergroupwizard.fields.allownoselection.label'),
			boxLabel: LN('sbi.formbuilder.staticclosefiltergroupwizard.fields.allownoselection.yes'),
			name: 'allowNoSelectionField',
			checked: (this.baseState.allowNoSelection === true),
			inputValue: true,
			disabled: (this.baseState.singleSelection === false)
		});
    	//items.push(this.allowNoSelectionField['true']);
    	
    	this.allowNoSelectionField['false'] = new Ext.form.Radio({
    		fieldLabel: '',
    		labelSeparator: '',
			boxLabel: LN('sbi.formbuilder.staticclosefiltergroupwizard.fields.allownoselection.no'),
			name: 'allowNoSelectionField',
			checked: (this.baseState.allowNoSelection === false),
            inputValue: false,
            disabled: (this.baseState.singleSelection === false)
		});
    	//items.push(this.allowNoSelectionField['false']);
    		
    	this.noSelectionTextField = new Ext.form.TextField({
			fieldLabel: LN('sbi.formbuilder.staticclosefiltergroupwizard.fields.noselectionoptionlabel.label'),
    		name:'noSelectionTextField',
    		value: this.baseState.noSelectionTextField || LN('sbi.formbuilder.staticclosefiltergroupwizard.fields.noselectiontext'),
    		allowBlank: true, 
    		inputType:'text',
    		maxLength:150
    		//, style:'margin-bottom:10px;'
    	});
    	
    	var fieldSet = new Ext.form.FieldSet({
    		title: LN('sbi.formbuilder.staticclosefiltergroupwizard.fields.options'),
    		autoHeight: true,
    		autoWidth: true,
    		labelWidth: 150,
    		items: [
    		   this.singleSelectionField['true'], this.singleSelectionField['false'], 
    		   this.allowNoSelectionField['true'], this.allowNoSelectionField['false'],
    		   this.noSelectionTextField
    		]
    	});
    	items.push(fieldSet);
    	
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
    			text: LN('sbi.formbuilder.staticclosefiltergroupwizard.buttons.apply'),
    		    handler: function(){
    	    		this.fireEvent('apply', this, this.getTarget(), this.getFormState());
                	this.hide();
            	}
            	, scope: this
    	    },{
    		    text: LN('sbi.formbuilder.staticclosefiltergroupwizard.buttons.cancel'),
    		    handler: function(){
                	this.hide();
            	}
            	, scope: this
    		}]
    	 });
	}
	
	, enableNoSelectionField: function(enable) {
		if(enable === true) {
			this.allowNoSelectionField['true'].enable();
			this.allowNoSelectionField['false'].enable();
			this.noSelectionTextField.enable();
		} else {
			this.allowNoSelectionField['true'].disable();
			this.allowNoSelectionField['false'].disable();
			this.noSelectionTextField.disable();
		}
	}
});