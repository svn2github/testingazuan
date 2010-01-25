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

Sbi.formviewer.StaticClosedOnOffFiltersPanel = function(aStaticClosedOnOffFiltersGroup, config) {
	
	var defaultSettings = {
		// set default values here
		frame: true
		, autoScroll: true
		//, autoWidth: true
		, autoHeight: true
        //, width: aStaticClosedOnOffFiltersGroup.width || 300
        //, height: aStaticClosedOnOffFiltersGroup.height || 150
	};
	if (Sbi.settings && Sbi.settings.formviewer && Sbi.settings.formviewer.staticClosedOnOffFiltersPanel) {
		defaultSettings = Ext.apply(defaultSettings, Sbi.settings.formviewer.staticClosedOnOffFiltersPanel);
	}
	var c = Ext.apply(defaultSettings, config || {});
	
	this.init(aStaticClosedOnOffFiltersGroup);
	
	Ext.apply(c, {
		items: this.items
	});
	
	// constructor
    Sbi.formviewer.StaticClosedOnOffFiltersPanel.superclass.constructor.call(this, c);

};

Ext.extend(Sbi.formviewer.StaticClosedOnOffFiltersPanel, Ext.form.FormPanel, {
    
	items: null
	
	// private methods
	
	, init: function(aStaticClosedOnOffFiltersGroup) {
		this.items = [];
		
		for (var i = 0; i < aStaticClosedOnOffFiltersGroup.filters.length; i++) {
			// create items
			var aFilter = aStaticClosedOnOffFiltersGroup.filters[i];
			this.items.push({
				xtype: 'checkbox',
				hideLabel: true,
                boxLabel: aFilter.text,
                name: aFilter.id
			});
		}
		
	}
	
	// public methods
	
	, getFormState: function() {
		var state = this.getForm().getValues();
		return state;
	}
  	
});