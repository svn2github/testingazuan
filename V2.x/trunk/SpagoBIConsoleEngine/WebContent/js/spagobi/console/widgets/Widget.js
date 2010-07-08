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

Ext.ns("Sbi.console");

Sbi.console.Widget = function(config) {
		var defaultSettings = {
			defaultMsg: 'Empty widget'
		};
		
		if(Sbi.settings && Sbi.settings.console && Sbi.settings.console.widget) {
			defaultSettings = Ext.apply(defaultSettings, Sbi.settings.console.widget);
		}
		
		var c = Ext.apply(defaultSettings, config || {});
		Ext.apply(this, c);
		
		this.msgPanel = new Ext.Panel({
			html: this.defaultMsg
		});
		
		c = Ext.apply(c, {  	
	      	items: [this.msgPanel]
		});
		

		// constructor
		Sbi.console.Widget.superclass.constructor.call(this, c);
    
};

Ext.extend(Sbi.console.Widget, Ext.Panel, {
    
    services: null
    , widgetContainer: null
    
   
    //  -- public methods ---------------------------------------------------------
    
    , setContainer: function(c) {
		this.widgetContainer = c;
	}

	, getStore: function(storeiId) {
		var store;
		
		if(this.widgetContainer) {
			var sm = this.widgetContainer.getStoreManager();
			if(sm) {
				store = sm.getStore(storeiId);
			} else {
				alert("getStore: storeManager not defined");
			}
		} else {
			alert("getStore: container not defined");
		}
		
		return store;
	}
    
    //  -- private methods ---------------------------------------------------------
    
	, onRender: function(ct, position) {
		Sbi.console.Widget.superclass.onRender.call(this, ct, position);
	}
    
    
});