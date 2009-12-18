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

Sbi.formbuilder.FiltersTemplatePanel = function(config) {
	
	var defaultSettings = {
		title: LN('Form Builder'),
	};
		
	if(Sbi.settings && Sbi.settings.formbuilder && Sbi.settings.formbuilder.filtersTemplatePanel) {
		defaultSettings = Ext.apply(defaultSettings, Sbi.settings.formbuilder.filtersTemplatePanel);
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
		
	c = Ext.apply(c, {
		title: this.title,
		border: true,
		bodyStyle:'padding:3px',
      	layout: 'fit',      	
      	items: [{
    	    html: 'destroy me please'
    	}]
	});

	// constructor
	Sbi.formbuilder.FiltersTemplatePanel.superclass.constructor.call(this, c);
	
	this.on('afterlayout', this.init, this);
};

Ext.extend(Sbi.formbuilder.FiltersTemplatePanel, Ext.Panel, {
    
	services: null
    , staticClosedFiltersEditorPanel: null
    , staticOpenFiltersEditorPanel: null
    , dynamicFiltersEditorPanel: null
    , groupingVariablesEditorPanel: null
   
    
    // public methods
    
    , init: function() {
		//alert('In');
		
		this.removeListener('afterlayout', this.init, this);
				
		this.remove(0);
		this.doLayout(true);
		
		this.staticClosedFiltersEditorPanel = new Sbi.formbuilder.StaticClosedFiltersEditorPanel();
		this.add(this.staticClosedFiltersEditorPanel);
		this.doLayout(true);
				
		//alert('Out');
	}
    
});