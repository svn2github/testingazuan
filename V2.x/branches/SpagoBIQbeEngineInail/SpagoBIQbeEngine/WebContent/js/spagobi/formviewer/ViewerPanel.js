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

Ext.ns("Sbi.formviewer");

Sbi.formviewer.ViewerPanel = function(template, config) {
	
	var defaultSettings = {
		// set default values here
		layout: 'table',
	    layoutConfig: {
	        columns: 1
	    },
		autoScroll: true
	};
	if (Sbi.settings && Sbi.settings.qbe && Sbi.settings.qbe.viewerPanel) {
		defaultSettings = Ext.apply(defaultSettings, Sbi.settings.qbe.viewerPanel);
	}
	var c = Ext.apply(defaultSettings, config || {});
	
	this.init(template);
	
	this.toolbar = new Ext.Toolbar({
		items: [{
			text: 'getFormState',
			handler: this.getFormState,
			scope: this
		}]
	});
	
	Ext.apply(c, {
		title: LN('sbi.formviewer.formviewerpanel.title')
		, tbar: this.toolbar
  		, items: this.items
	});
	
	// constructor
    Sbi.formviewer.ViewerPanel.superclass.constructor.call(this, c);

};

Ext.extend(Sbi.formviewer.ViewerPanel, Ext.Panel, {
    
    services: null
    , staticClosedFiltersPanel: null
    , staticOpenFiltersPanel: null
    , dynamicFiltersPanel: null
   
    // private methods
    , init: function(template) {
		this.items = [];
		if (template.staticClosedFilters !== undefined && template.staticClosedFilters !== null && template.staticClosedFilters.length > 0) {
			this.staticClosedFiltersPanel = new Sbi.formviewer.StaticClosedFiltersPanel(template.staticClosedFilters); 
			this.items.push(this.staticClosedFiltersPanel);
		}
		if (template.staticOpenFilters !== undefined && template.staticOpenFilters !== null && template.staticOpenFilters.length > 0) {
			this.staticOpenFiltersPanel = new Sbi.formviewer.StaticOpenFiltersPanel(template.staticOpenFilters); 
			this.items.push(this.staticOpenFiltersPanel);
		}
		if (template.dynamicFilters !== undefined && template.dynamicFilters !== null && template.dynamicFilters.length > 0) {
			this.dynamicFiltersPanel = new Sbi.formviewer.DynamicFiltersPanel(template.dynamicFilters); 
			this.items.push(this.dynamicFiltersPanel);
		}
		
	}
    
    // public methods

	, getFormState: function() {
		var state = {};
		if (this.staticClosedFiltersPanel !== null) {
			state.staticClosedFilters = this.staticClosedFiltersPanel.getFormState();
		}
		if (this.staticOpenFiltersPanel !== null) {
			state.staticOpenFilters = this.staticOpenFiltersPanel.getFormState();
		}
		if (this.dynamicFiltersPanel !== null) {
			state.dynamicFilters = this.dynamicFiltersPanel.getFormState();
		}
		alert(state.toSource());
	}
  	
});