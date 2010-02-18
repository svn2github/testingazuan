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
  * WidgetPanel
  * 
  * handle layout of widgets (maybe also d&d)
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

Ext.ns("Sbi.console");

Sbi.console.WidgetPanel = function(config) {
	
		var defaultSettings = {
			layout:'column'
			, columnNumber: 3
		};
		
		if(Sbi.settings && Sbi.settings.console && Sbi.settings.console.widgetPanel) {
			defaultSettings = Ext.apply(defaultSettings, Sbi.settings.console.widgetPanel);
		}
		
		var c = Ext.apply(defaultSettings, config || {});
		
		this.widgetContainer = new Sbi.console.WidgetContainer();
		
		if(c.items !== undefined) {
			this.widgetContainer.register(c.items);
			var x = c.items[0];
			delete c.items;
			alert('Costruttore WidgetPanel: ' + x.getStore('testStore'));
		}
		
		Ext.apply(this, c);
				
		this.widgetColumns = [];
		for(var i = 0; i < this.columnNumber; i++) {
			var w = this.columnWidths? this.columnWidths[i]: 1/this.columnNumber; 
			this.widgetColumns.push(
				new Ext.Panel({
					columnWidth: w
					, baseCls:'x-plain'
					, bodyStyle:'padding:5px 3px 3px 5px'
				})
			);
		}
		
		
		c = Ext.apply(c, {  	
	      	items: this.widgetColumns
		});

		
		// constructor
		Sbi.console.WidgetPanel.superclass.constructor.call(this, c);
    
};

Ext.extend(Sbi.console.WidgetPanel, Ext.Panel, {
    
	columnNumber: null
	, widgetColumns: null
	, widgetContainer: null
    
    //  -- public methods ---------------------------------------------------------
    
    , addWidget: function(widget) {
		var widgets = this.widgetContainer.getWidgets();
		var index = widgets.getCount();
		this.widgetContainer.register(widget);
		this.widgetColumns[index%this.columnNumber].add(widget);
	}
    
    //  -- private methods ---------------------------------------------------------
    
    , onRender: function(ct, position) {
		Sbi.console.WidgetPanel.superclass.onRender.call(this, ct, position);
		
		alert('widgetPanel RENDER');
		
		var widgets = this.widgetContainer.getWidgets();
		
		alert('WidgetPanel IN: ' + widgets.get(0).getStore('testStore'));
		
		widgets.each(function(widget, index, length) {
			this.widgetColumns[index%this.columnNumber].add(widget);
		}, this);
		
		alert('WidgetPanel OUT: ' + widgets.get(0).getStore('testStore'));
	}

	
    
    
});