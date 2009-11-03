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

Sbi.formviewer.StaticClosedFiltersPanel = function(staticFilters, config) {
	
	var defaultSettings = {
		// set default values here
		title: 'Filtri statici chiusi',
		layout: 'table',
	    layoutConfig: {
	        columns: staticFilters.length
	    },
		frame: true,
		autoScroll: true,
		autoWidth: true
	};
	if (Sbi.settings && Sbi.settings.qbe && Sbi.settings.qbe.staticClosedFiltersPanel) {
		defaultSettings = Ext.apply(defaultSettings, Sbi.settings.qbe.staticClosedFiltersPanel);
	}
	var c = Ext.apply(defaultSettings, config || {});
	
	this.init(staticFilters);
	
	Ext.apply(c, {
  		items: this.forms
	});
	
	// constructor
    Sbi.formviewer.StaticClosedFiltersPanel.superclass.constructor.call(this, c);

};

Ext.extend(Sbi.formviewer.StaticClosedFiltersPanel, Ext.Panel, {
    
	services: null
	, forms: null
	   
	// private methods
	   
	, init: function(staticFilters) {
		this.forms = [];
		for (var i = 0; i < staticFilters.length; i++) {
			var aStaticFiltersGroup = staticFilters[i];
			var aStaticFiltersForm = null;
			if (aStaticFiltersGroup.singleSelection) {
				aStaticFiltersForm = new Sbi.formviewer.StaticClosedXORFiltersPanel(aStaticFiltersGroup);
			} else {
				aStaticFiltersForm = new Sbi.formviewer.StaticClosedOnOffFiltersPanel(aStaticFiltersGroup);
			}
			this.forms.push(aStaticFiltersForm);
		}
	}

	   
	// public methods
	
	, setState: function(state) {
	
	}
	
	, getState: function() {
		var state = [];
		for (var i = 0; i < this.forms.length; i++) {
			var aForm = this.forms[i];
			state.push(aForm.getState());
		}
		return state;
	}
  	
});