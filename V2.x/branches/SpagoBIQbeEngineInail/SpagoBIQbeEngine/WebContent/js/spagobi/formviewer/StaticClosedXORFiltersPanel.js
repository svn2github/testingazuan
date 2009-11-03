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

Sbi.formviewer.StaticClosedXORFiltersPanel = function(aStaticClosedXORFiltersGroup, config) {
	
	var defaultSettings = {
			// set default values here
	};
	if (Sbi.settings && Sbi.settings.qbe && Sbi.settings.qbe.staticClosedXORFiltersPanel) {
		defaultSettings = Ext.apply(defaultSettings, Sbi.settings.qbe.staticClosedXORFiltersPanel);
	}
	var c = Ext.apply(defaultSettings, config || {});
	
	this.init(aStaticClosedXORFiltersGroup);
	
	Ext.apply(c, {
		frame: true
        , items: this.items
        , width: 300
        , height: 150
	});
	
	// constructor
    Sbi.formviewer.StaticClosedXORFiltersPanel.superclass.constructor.call(this, c);

};

Ext.extend(Sbi.formviewer.StaticClosedXORFiltersPanel, Ext.form.FormPanel, {
    
	items: null
	
	// private methods
	
	, init: function(aStaticClosedXORFiltersGroup) {
		this.items = [];
		
		this.items = {
            xtype: 'fieldset',
            title: aStaticClosedXORFiltersGroup.title,
            autoHeight: true,
            defaultType: 'radio',
            items: []
        }
		
		// TODO e se non c'è l'allowNoSelection????
		if (aStaticClosedXORFiltersGroup.allowNoSelection !== null && aStaticClosedXORFiltersGroup.allowNoSelection === true) {
			// create No Selection Item
			this.items.items.push({
				hideLabel: true,
				boxLabel: aStaticClosedXORFiltersGroup.noSelectionText,
				name: aStaticClosedXORFiltersGroup.id,
				inputValue: 'noSelection'
			});
		}
		
		for (var i = 0; i < aStaticClosedXORFiltersGroup.options.length; i++) {
			// create items
			var aFilter = aStaticClosedXORFiltersGroup.options[i];
			this.items.items.push({
				hideLabel: true,
                boxLabel: aFilter.text,
                name: aStaticClosedXORFiltersGroup.id,
                inputValue: aFilter.rightOperandValue
			});
		}
	}
	
	// public methods
	
	, setState: function(state) {
	
	}

	, getState: function() {
		
	}
  	
});