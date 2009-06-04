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

Ext.ns("Sbi.execution");

Sbi.execution.SaveRememberMeWindow = function(config) {
	
	this.rememberMeName = new Ext.form.TextField({
		id: 'nameRM',
		name: 'nameRM',
		allowBlank: false, 
		inputType: 'text',
		maxLength: 50,
		width: 250,
		fieldLabel: LN('sbi.rememberme.name') 
	});
	
	this.rememberMeDescr = new Ext.form.HtmlEditor({
        id:'descrRM',
        width: 550,
        height: 150,
		fieldLabel: LN('sbi.rememberme.descr')  
    });
	
    Ext.form.Field.prototype.msgTarget = 'side';
    this.saveRememberMeForm = new Ext.form.FormPanel({
        frame:true,
        bodyStyle:'padding:5px 5px 0',
        items: [this.rememberMeName, this.rememberMeDescr],
        buttons:[{text: LN('sbi.rememberme.save'), handler: this.saveRememberMe}]
    });
	
	this.buddy = undefined;
	
	var c = Ext.apply({}, config, {
		id:'popup_saveRM',
		layout:'fit',
		width:700,
		height:300,
		//closeAction:'hide',
		plain: true,
		title: LN('sbi.execution.saveRememberMe'),
		items: this.saveRememberMeForm
	});   
	
	// constructor
    Sbi.execution.SaveRememberMeWindow.superclass.constructor.call(this, c);
    
    if (this.buddy === undefined) {
    	this.buddy = new Sbi.commons.ComponentBuddy({
    		buddy : this
    	});
    }
    
};

Ext.extend(Sbi.execution.SaveRememberMeWindow, Ext.Window, {
	
	rememberMeName: null
	, rememberMeDescr: null
	, saveRememberMeForm: null
	
	, saveRememberMe: function () {
		alert('saveRememberMefunction');
	}
	
});