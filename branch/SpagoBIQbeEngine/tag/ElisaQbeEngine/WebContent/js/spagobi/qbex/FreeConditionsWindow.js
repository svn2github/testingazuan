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
		title: 'Fill free conditions'
		, width: 500
		, height: 250
		, hasBuddy: false
	});
	
	Ext.apply(this, c);
		
	this.initFormPanel(c);
	
	
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
	
	this.addEvents('apply');    
};

Ext.extend(Sbi.qbe.FreeConditionsWindow, Ext.Window, {
    
	fields: null
	, hasBuddy: null
    , buddy: null
    , formItems: null
   
   
    // public methods
	,getFormState : function() {      
      	var formState = {};
      	
      	for (var i = 0; i < this.formItems.length; i++) {
      		var aFormItem = this.formItems[i];
      		formState[aFormItem.getName()] = aFormItem.getValue();
      	}

      	return formState;
    }

	//private methods
	, initFormPanel: function(config) {
		
		this.formItems = [];
		
		var freeFilters = config.freeFilters;
		
		for (var i = 0; i < freeFilters.length; i++) {
			var aFilter = freeFilters[i];
	    	var aField = new Ext.form.TextField({
	    		name: aFilter.fname,
	    		allowBlank:true, 
	    		inputType:'text',
	    		maxLength:200,
	    		width:250,
	    		fieldLabel: aFilter.fname,
	    		value: aFilter.odesc
	    	});
	    	this.formItems.push(aField);
		}
    	
    	this.formPanel = new Ext.form.FormPanel({
    		frame:true,
    	    bodyStyle:'padding:5px 5px 0',
    	    buttonAlign : 'center',
    	    items: this.formItems,
    	    buttons: [{
    			text: 'Apply',
    		    handler: function(){
    	    		this.fireEvent('apply', this.getFormState());
                	this.close();
            	}
            	, scope: this
    	    },{
    		    text: 'Cancel',
    		    handler: function(){
                	this.close();
            	}
            	, scope: this
    		}]
    	 });
    }
});