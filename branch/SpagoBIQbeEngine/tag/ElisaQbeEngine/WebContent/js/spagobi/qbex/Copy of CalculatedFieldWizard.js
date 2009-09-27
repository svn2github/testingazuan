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
  * CalculatedFieldWizard - short description
  * 
  * Object documentation ...
  * 
  * by Andrea Gioia (andrea.gioia@eng.it)
  */

Ext.ns("Sbi.qbe");

Sbi.qbe.CalculatedFieldWizard = function(config) {	
	
	var c = Ext.apply({}, config || {}, {
		title: 'Expression wizard ...'
		, width: 500
		, height: 250
		, hasBuddy: false
		
	});
	
	Ext.apply(this, c);
		
	this.initFormPanel(c);
	
	
	// constructor
	Sbi.widgets.SaveWindow.superclass.constructor.call(this, {
		layout: 'fit',
		width: this.width,
		height: this.height,
		closeAction:'hide',
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

Ext.extend(Sbi.qbe.CalculatedFieldWizard, Ext.Window, {
    
	aliasField: null
	, initialValueField: null
	, expressionField: null
	, hasBuddy: null
    , buddy: null
   
   
    // public methods
	,getFormState : function() {      
    	
      	var formState = {};
      	
      	if(this.aliasField) formState.alias= this.aliasField.getValue();
      	if(this.initialValueField) formState.initialValue= this.initialValueField.getValue();
      	
      	if(this.expressionField) {
      		formState.expression = this.expressionField.getValue();
      		formState.expression = Ext.util.Format.stripTags( formState.expression );
      		formState.expression = formState.expression.replace(/&nbsp;/g," ");
      	}
      	
      	return formState;
    }

	//private methods
	, initFormPanel: function(config) {
		
		var items = [];
		
		this.aliasField = new Ext.form.TextField({
    		name:'alias',
    		allowBlank:false, 
    		inputType:'text',
    		maxLength:50,
    		width:250,
    		fieldLabel:'Alias' 
    	});
    	items.push(this.aliasField);
    	
    	this.initialValueField = new Ext.form.TextField({
    		name:'initialvalue',
    		allowBlank:false, 
    		inputType:'text',
    		maxLength:200,
    		width:250,
    		fieldLabel:'Initial value' 
    	});
    	items.push(this.initialValueField);
    	
		
    	this.expressionField = new Ext.form.HtmlEditor({
    		name:'expression',
    	    frame: true,
    	    enableAlignments : false,
    	    enableColors : false,
    	    enableFont :  false,
    	    enableFontSize : false, 
    	    enableFormat : false,
    	    enableLinks :  false,
    	    enableLists : false,
    	    enableSourceEdit : false,
    	    fieldLabel:'Expression' 
    	});
    	items.push(this.expressionField);
    	    
    	    
    	this.formPanel = new Ext.form.FormPanel({
    		frame:true,
    	    bodyStyle:'padding:5px 5px 0',
    	    buttonAlign : 'center',
    	    items: items,
    	    buttons: [{
    			text: 'Save',
    		    handler: function(){
    	    		this.fireEvent('apply', this, this.getFormState());
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