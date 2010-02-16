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
  * WidgetContainer
  * 
  * handle:
  *  - widgets lifecycle management: register, unregister, lookup
  *  - shared resources: through env
  *  - intra-widgets comunications: sendMessage (asyncronous: point to point or broadcast)
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

Sbi.console.WidgetContainer = function(config) {
	
		var defaultSettings = {
			
		};
		
		if(Sbi.settings && Sbi.settings.console && Sbi.settings.console.widgetContainer) {
			defaultSettings = Ext.apply(defaultSettings, Sbi.settings.console.widgetContainer);
		}
		
		var c = Ext.apply(defaultSettings, config || {});
		
		Ext.apply(this, c);

		this.init();
	

		// constructor
		Sbi.console.WidgetContainer.superclass.constructor.call(this, c);
};

Ext.extend(Sbi.console.WidgetContainer, Ext.util.Observable, {
    
    widgets: null
    , env: null
    , storeManager: null
    
   
    //  -- public methods ---------------------------------------------------------
        
    , register: function(w) {
		this.widgets.addAll(w);
		w.setContainer(this);
	}

	, unregister: function(w) {
		this.remove(w);
		w.setContainer(null);
	}
	
	, lookup: function(w) {
		this.widgets.get(w);
	}
	
	
    
    //  -- private methods ---------------------------------------------------------
    
    , init: function() {
    	this.widgets = new Ext.util.MixedCollection();
	}
    
    
});