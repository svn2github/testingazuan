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

Sbi.formbuilder.FormBuilderPage = function(config) {
	
	var defaultSettings = {
		title: LN('Form Builder'),
	};
		
	if(Sbi.settings && Sbi.settings.formbuilder && Sbi.settings.formbuilder.formBuilderPage) {
		defaultSettings = Ext.apply(defaultSettings, Sbi.settings.formbuilder.formBuilderPage);
	}
		
	var c = Ext.apply(defaultSettings, config || {});
		
	Ext.apply(this, c);
		
	/*
	this.services = this.services || new Array();	
	this.services['doThat'] = this.services['doThat'] || Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'DO_THAT_ACTION'
		, baseParams: new Object()
	});
	
	this.addEvents('customEvents');
	*/
		
		
	this.initQueryFieldsPanel();
	this.initFiltersTemplatePanel();
		
	this.toolbar = new Ext.Toolbar({
		items: [
		    '->'
		    , {
				text: 'Refresh',
				handler: function() {this.queryFieldsPanel.refresh();},
				scope: this
		    }
		  ]
	});
	
	
	c = Ext.apply(c, {
		title: this.title,
		border: true,
		//bodyStyle:'background:green',
		style:'padding:3px',
      	layout: 'border',      	
      	tbar: this.toolbar,
      	items: [this.queryFieldsPanel, this.filtersTemplatePanel]
	});

	// constructor
	Sbi.formbuilder.FormBuilderPage.superclass.constructor.call(this, c);
};

Ext.extend(Sbi.formbuilder.FormBuilderPage, Ext.Panel, {
    
    services: null
    , queryFieldsPanel: null
    , filtersTemplatePanel: null
   
   
    // public methods
    
    , initQueryFieldsPanel: function(c) {
    	
		this.queryFieldsPanel = new Sbi.formbuilder.QueryFieldsPanel({
			region:'west',
			split: true, 
			layout:'fit',
			border: false,
	        width:250,
	        //margins: '5 5 5 5',
	        collapsible: true,
	        collapseFirst: false
	        //bodyStyle:'background:red',
		});
    }
    
    , initFiltersTemplatePanel: function(c) {
    	this.filtersTemplatePanel = new Sbi.formbuilder.FiltersTemplatePanel({
    		region:'center',
	        autoScroll: true,
			containerScroll: true,
			layout: 'fit',
			border: false
    	});    	
    }
    
});