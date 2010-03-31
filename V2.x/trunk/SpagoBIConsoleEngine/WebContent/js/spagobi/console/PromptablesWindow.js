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
  * - Antonella Giachino (antonella.giachino@eng.it)
  */

Ext.ns("Sbi.console");

Sbi.console.PromptablesWindow = function(config) {

	var defaultSettings = Ext.apply({}, config || {}, {
		title: 'Parameters window'
		, width: 500
		, height: 300
		, hasBuddy: false	
		, modal: true
	});
	
		
	if(Sbi.settings && Sbi.settings.console && Sbi.settings.console.promptablesWindow) {
		defaultSettings = Ext.apply(defaultSettings, Sbi.settings.console.promptablesWindow);
	}
		
	var c = Ext.apply(defaultSettings, config || {});

	Ext.apply(this, c);
		
	this.initFormPanel();	

	this.closeButton = new Ext.Button({
		text: LN('sbi.console.promptables.btnOK'),
		handler: function(){
        	this.hide();
        	this.fireEvent('click', this);
        }
        , scope: this
	});
	
	

	c = Ext.apply(c, {  	
		layout: 'fit'
	,	closeAction:'hide'
	,	plain: true
	,	modal:true
	,	title: this.title
	,	buttonAlign : 'center'
	,	buttons: [this.closeButton]
	,	items: [this.formPanel]
	});

	// constructor
	Sbi.console.PromptablesWindow.superclass.constructor.call(this, c);
	
	this.addEvents('click');
    
};

Ext.extend(Sbi.console.PromptablesWindow, Ext.Window, {

    serviceName: null
    , formPanel: null
    
   , params: null
   , closeButton: null
    
    // public methods

    
    // private methods

    , initFormPanel: function() {	
		var fields = [];

        	for(p in this.promptables) {       
        		var tmpField = new Ext.form.TextField({
        			id: this.promptables[p]
    	          , fieldLabel: p
    	          , width: 150    	     
    	        });
        		
        		fields.push(tmpField);
        		this.params[p] = null;
	        }  			   

    	this.formPanel = new  Ext.FormPanel({
    		  //title:  LN('sbi.console.promptables.title'),
    		  margins: '50 50 50 50',
	          labelAlign: 'left',
	          bodyStyle:'padding:5px',
	          width: 850,
	          height: 600,
	          layout: 'form',
	          trackResetOnLoad: true,
	          items: fields
	      });
    	 
    }
    
    
});