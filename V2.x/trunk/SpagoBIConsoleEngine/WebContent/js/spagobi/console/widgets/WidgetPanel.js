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
			layout:'table'
			, columnNumber: 3
		};
		
		if(Sbi.settings && Sbi.settings.console && Sbi.settings.console.widgetPanel) {
			defaultSettings = Ext.apply(defaultSettings, Sbi.settings.console.widgetPanel);
		}
		
		var c = Ext.apply(defaultSettings, config || {});
		
		
		//for retrocompatibility with 'column' type layout. 
		//if (c.layoutConfig === undefined && c.columnNumber !== undefined){
		if (c.layout !== undefined && c.layout === 'column'){
			
			//c.layoutConfig = {columns: c.columnNumber};
			delete c.layout;
			c.layout = {}; 
			c.layoutConfig = {};
			c.layout = 'table';
			c.layoutConfig.columns = c.columnNumber;
			delete c.columnNumber;
			delete c.columnWidths;
			alert(c.layout.toSource());		
			alert(c.layoutConfig.toSource());			
		}
		
		this.widgetContainer = new Sbi.console.WidgetContainer({storeManager: c.storeManager});
		if(c.storeManager) {
			delete c.storeManager;
		}
		
		alert("WP - c.items: " + c.items.toSource());
		if(c.items !== undefined) {
			this.widgetContainer.register(c.items);
			var x = c.items[0];
			delete c.items;
		}
		
		Ext.apply(this, c);
	
		// constructor
		Sbi.console.WidgetPanel.superclass.constructor.call(this, c);
    
};

Ext.extend(Sbi.console.WidgetPanel, Sbi.console.Widget, {
    
	//columnNumber: null
	 widgetContainer: null
    
    //  -- public methods ---------------------------------------------------------
    
    , addWidget: function(widget) {
		var widgets = this.widgetContainer.getWidgets();
		var index = widgets.getCount();
		this.widgetContainer.register(widget);
	}
    
    //  -- private methods ---------------------------------------------------------
    
    , onRender: function(ct, position) {
    	
		Sbi.console.WidgetPanel.superclass.onRender.call(this, ct, position);
		
	    this.items.each( function(item) {
			this.items.remove(item);
	        item.destroy();           
	    }, this);   
		
		var widgets = this.widgetContainer.getWidgets();
		alert("WP - widgets: " + widgets.toSource());
		
		widgets.each(function(widget, index, length) {
			this.add(widget);
		}, this);
		
	}

    
});