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

Sbi.formviewer.StaticClosedOnOffFiltersPanel = function(aStaticFiltersORGroup) {
	
	this.init(aStaticFiltersORGroup);
	
	c = {
		frame: true
        , items: this.items
        , width: 200
        , height: 150
	};
	
	// constructor
    Sbi.formviewer.StaticClosedOnOffFiltersPanel.superclass.constructor.call(this, c);

};

Ext.extend(Sbi.formviewer.StaticClosedOnOffFiltersPanel, Ext.form.FormPanel, {
    
	items: null
	
	// private methods
	
	, init: function(config) {
		this.items = [];
		
		/*
		this.items = {
            xtype: 'fieldset',
            title: config.title,
            autoHeight: true,
            defaultType: 'checkbox',
            items: []
        }
        */
		
		for (var i = 0; i < config.options.length; i++) {
			// create items
			var aFilter = config.options[i];
			this.items.push({
				xtype: 'checkbox',
				hideLabel: true,
                boxLabel: aFilter.text,
                name: config.id
			});
		}
	}
	
	// public methods
	
	, setState: function(state) {
	
	}

	, getState: function() {
		
	}
  	
});