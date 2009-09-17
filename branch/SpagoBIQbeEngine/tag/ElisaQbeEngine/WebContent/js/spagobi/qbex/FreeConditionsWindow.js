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

Ext.ns("Sbi.qbe");

Sbi.qbe.FreeConditionsWindow = function(config) {	
	
	var c = Ext.apply({}, config || {}, {
		title: LN('sbi.qbe.freeconditionswindow.title')
		, width: 550
		, height: 250
		, hasBuddy: false
	});
	
	Ext.apply(this, c);
	
	this.freeFilters = config.freeFilters;
	this.initDefaultValues();
	this.initLastValues();
	
	this.initFormPanel();
	
	// constructor
	Sbi.qbe.FreeConditionsWindow.superclass.constructor.call(this, {
		layout: 'fit',
		width: this.width,
		height: this.height,
		closeAction:'close',
		plain: true,
		title: this.title,
		items: [this.formPanel]
    });
    
	if(c.hasBuddy === 'true') {
		this.buddy = new Sbi.commons.ComponentBuddy({
    		buddy : this
    	});
	}
	
	this.addEvents('apply', 'saveDefaults');    
};

Ext.extend(Sbi.qbe.FreeConditionsWindow, Ext.Window, {
    
	fields: null
	, hasBuddy: null
    , buddy: null
    , formItems: null
    , defaultvalues: null
    , lastvalues: null
   
    // public methods
	,getFormState : function() {      
      	var formState = {};
      	
      	for (var i = 0; i < this.formItems.length; i++) {
      		var aFormItem = this.formItems[i];
      		formState[aFormItem.getName()] = aFormItem.getValue();
      	}

      	return formState;
    }

	, setFormState: function (formState) {
	  	for (var i = 0; i < this.formItems.length; i++) {
	  		var aFormItem = this.formItems[i];
	  		if (formState[aFormItem.getName()] !== undefined && formState[aFormItem.getName()] !== null) {
	  			aFormItem.setValue(formState[aFormItem.getName()]);
	  		}
	  	}
	}
	
	, restoreDefaults: function () {
		if (this.defaultvalues != null) {
			this.setFormState(this.defaultvalues);
		}
	}
	
	, restoreLast: function () {
		if (this.lastvalues != null) {
			this.setFormState(this.lastvalues);
		}
	}

	//private methods
	, initDefaultValues: function () {
		this.defaultvalues = null;
		for (var i = 0; i < this.freeFilters.length; i++) {
			var aFilter = this.freeFilters[i];
			if (aFilter.rightOperandDefaultValue !== null) {
				if (this.defaultvalues == null) {
					this.defaultvalues = {};
				}
				this.defaultvalues[aFilter.filterId] = aFilter.rightOperandDefaultValue;
			}
		}
	}
	
	, initLastValues: function () {
		this.lastvalues = null;
		for (var i = 0; i < this.freeFilters.length; i++) {
			var aFilter = this.freeFilters[i];
			if (aFilter.lastvalue !== null) {
				if (this.lastvalues == null) {
					this.lastvalues = {};
				}
				this.lastvalues[aFilter.filterId] = aFilter.rightOperandLastValue;
			}
		}
	}
	
	, initFormPanel: function() {
		
		this.formItems = [];
		
		for (var i = 0; i < this.freeFilters.length; i++) {
			var aFilter = this.freeFilters[i];
			var fieldLabel = '';
			if (aFilter.leftOperandAggregator !== undefined && aFilter.leftOperandAggregator !== null 
					&& aFilter.leftOperandAggregator != '' && aFilter.leftOperandAggregator != 'NONE') {
				fieldLabel = aFilter.filterId + ' [' + aFilter.leftOperandAggregator + '(' + aFilter.leftOperandDescription + ')]';
			} else {
				fieldLabel = aFilter.filterId + ' [' + aFilter.leftOperandDescription + ']';
			}
				
	    	var aField = new Ext.form.TextField({
	    		name: aFilter.filterId,
	    		allowBlank:true, 
	    		inputType:'text',
	    		maxLength:200,
	    		width:200,
	    		fieldLabel: fieldLabel,
	    		labelStyle: 'width:250',
	    		value: (aFilter.rightOperandDefaultValue !== null) ? aFilter.rightOperandDefaultValue : ''
	    	});
	    	this.formItems.push(aField);
		}
    	
		var buttons = this.initButtons();
		
    	this.formPanel = new Ext.form.FormPanel({
    		frame:true,
    	    bodyStyle:'padding:5px 5px 0',
    	    buttonAlign : 'center',
    	    items: this.formItems,
    	    buttons: buttons
    	 });
    }
	
	, initButtons: function () {
		var buttons = [];
		buttons.push({
			text: LN('sbi.qbe.freeconditionswindow.buttons.text.apply'),
		    handler: function(){
	    		this.fireEvent('apply', this.getFormState());
            	this.close();
        	}
        	, scope: this
	    });
		
		if (Sbi.user.isPowerUser === true) {
			buttons.push({
				text: LN('sbi.qbe.freeconditionswindow.buttons.text.saveasdefaults'),
			    handler: function(){
	    			this.fireEvent('saveDefaults', this.getFormState());
	    		}
	        	, scope: this
		    });
		}
		
		if (this.defaultvalues !== null) {
			buttons.push({
				text: LN('sbi.qbe.freeconditionswindow.buttons.text.restoredefaults'),
			    handler: this.restoreDefaults
	        	, scope: this
		    });
		}
		
		if (Sbi.user.isPowerUser === true) {
			if (this.lastvalues !== null) {
				buttons.push({
					text: LN('sbi.qbe.freeconditionswindow.buttons.text.restorelast'),
				    handler: this.restoreLast
		        	, scope: this
			    });
			}
		}
		
		if (Sbi.user.isPowerUser === true) {
			buttons.push({
			    text: LN('sbi.qbe.freeconditionswindow.buttons.text.cancel'),
			    handler: function(){
	            	this.close();
	        	}
	        	, scope: this
			});
		}
		
		return buttons;
	}
});