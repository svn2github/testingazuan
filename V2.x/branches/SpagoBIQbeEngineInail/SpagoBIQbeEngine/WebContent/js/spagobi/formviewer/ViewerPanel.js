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

Sbi.formviewer.ViewerPanel = function(config) {
	
	var c = Ext.apply({
		// set default values here
	}, config || {});
	
	this.init(c);
	
	c = Ext.apply(c, {
		title: 'Viewer',
		layout: 'table',
	    layoutConfig: {
	        columns: 1
	    },
		autoScroll: true, 
  		items: this.items
	});
	
	// constructor
    Sbi.formviewer.ViewerPanel.superclass.constructor.call(this, c);

};

Ext.extend(Sbi.formviewer.ViewerPanel, Ext.Panel, {
    
    services: null
    , staticFiltersPanel: null
   
    // private methods
    , init: function(config) {
		this.items =  [];
		this.staticFiltersPanel = new Sbi.formviewer.StaticClosedFiltersPanel(config.staticFilters); 
		this.items.push(this.staticFiltersPanel);
		this.openFiltersPanel = new Sbi.formviewer.StaticOpenFiltersPanel(config.openFilters); 
		this.items.push(this.openFiltersPanel);
	}
    
    
    // public methods
    
    , setState: function(state) {
	
    }

	, getState: function() {
		
	}
  	
});