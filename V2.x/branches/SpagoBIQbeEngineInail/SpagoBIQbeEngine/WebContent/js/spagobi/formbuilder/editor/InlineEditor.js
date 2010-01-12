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
  * - Andrea Gioia (andrea.gioia@eng.it)
  */

Ext.ns("Sbi.formbuilder");

Sbi.formbuilder.InlineEditor = function(config) {
	
	var defaultSettings = {
		style: 'border:1px solid #ccc !important;'
	};
	if (Sbi.settings && Sbi.settings.formbuilder && Sbi.settings.formbuilder.inlineEditor) {
		defaultSettings = Ext.apply(defaultSettings, Sbi.settings.formbuilder.inlineEditor);
	}
	var c = Ext.apply(defaultSettings, config || {});
	
	
	
	Ext.apply(this, c);
	
	
	this.init();
	this.initButtons();
	
	Ext.apply(c, {
		cls: 'filter'
		, layout: 'column'
		, layoutConfig: {
		     columns: 3
		}
			
		, controls: this.buttons
		
		, items: [{
			columnWidth: .99,
			items: [this.filter]
		},{
		    width: 23,
		    items: [ this.buttons[0] ]
		},{
		    width: 23,
		    items: [ this.buttons[1] ]
		}]
	});
	
	// constructor
    Sbi.formbuilder.InlineEditor.superclass.constructor.call(this, c);
    
    this.on('render', function(f) {
		
		this.getEl().on('mouseover', function(el) {
			this.addClass('filter-select');
			this.controls[0].setVisible(true);
			this.controls[1].setVisible(true);
		}, this);
		
		this.getEl().on('mouseout', function(el) {
			this.removeClass('filter-select');
			this.controls[0].setVisible(false);
			this.controls[1].setVisible(false);
		}, this);
		
		this.getEl().on('dblclick', function(el) {
			this.fireEvent('actionrequest', 'edit', this);
		}, this);
		
	}, this);
};

Ext.extend(Sbi.formbuilder.InlineEditor, Ext.Panel, {
    
	buttons: null
	, filter: null
	
	// --------------------------------------------------------------------------------
	// public methods
	// --------------------------------------------------------------------------------
		
	, setContents: function(c) {
		alert('ERROR: setContents undefined');
	}
		
	
	, getContents: function() {
		alert('ERROR: getContents undefined');
	}
	
	// --------------------------------------------------------------------------------
	// private methods
	// --------------------------------------------------------------------------------
	
	, init: function() {
		this.filter = new Ext.Panel({
			html: 'filter item not defined'
		});
	}

	
	, initButtons: function() {
		this.buttons = [];
		
		var editBtn = new Ext.Button({
			tooltip: 'Edit filter',
	        cls: 'image-button',
	        iconCls: 'edit',
	        disabled: true,
	        hidden: true,
	        handler: function() {
				this.fireEvent('actionrequest', 'edit', this);
			}, 
			scope: this
	    });
		
		editBtn.on('render', function(b) {
			b.getEl().on('mouseover', function(b, e) {
				this.controls[0].enable();
			}, this);
			b.getEl().on('mouseout', function(el) {
				this.controls[0].disable();
			}, this);
		}, this);		
		this.buttons.push(editBtn);
		
		var deleteBtn = new Ext.Button({
			tooltip: 'Delete filter',
	        cls: 'image-button',
	        iconCls: 'editremove',
	        disabled: true,
	        hidden: true,
	        handler: function() {
				this.fireEvent('actionrequest', 'delete', this);
			}, 
			scope: this
	    });		
		deleteBtn.on('render', function(b) {
			b.getEl().on('mouseover', function(el) {
				this.controls[1].enable();
			}, this);
			b.getEl().on('mouseout', function(el) {
				this.controls[1].disable();
			}, this);
		}, this);
		this.buttons.push(deleteBtn);	


	}
  	
});