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

Sbi.formbuilder.StaticClosedXORFilterWizard = function(config) {
	
	var defaultSettings = {
		// set default values here
		title: 'Static close filter definition'
		, autoScroll: true
		, width: 350
		, height: 265
		, baseState: {
			filterTitle: undefined		
		}
	};
	if (Sbi.settings && Sbi.settings.formbuilder && Sbi.settings.formbuilder.staticClosedXORFiltersWizard) {
		defaultSettings = Ext.apply(defaultSettings, Sbi.settings.formbuilder.staticClosedXORFiltersWizard);
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
		title: this.title,
		items: [this.formPanel]
	});
	
	// constructor
    Sbi.formbuilder.StaticClosedXORFilterWizard.superclass.constructor.call(this, c);
    
    if(this.hasBuddy === 'true') {
		this.buddy = new Sbi.commons.ComponentBuddy({
    		buddy : this
    	});
	}
	
    this.addEvents('apply');
    
};

Ext.extend(Sbi.formbuilder.StaticClosedXORFilterWizard, Ext.Window, {

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
		
		s.filterTitle = this.filterTitleField.getValue();
		
		return s;
	}

	, setFormState: function(s) {
	
	}
	
	, setTarget: function(targetFilter) {
		this.targetFilter = targetFilter;
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
	
});