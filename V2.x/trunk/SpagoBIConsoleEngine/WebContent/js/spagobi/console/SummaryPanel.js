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
  * - Antonella Giachino (antonella.giachino@eng.it)
  */

Ext.ns("Sbi.console");

Sbi.console.SummaryPanel = function(config) {
	
		var defaultSettings = {
			layout: 'fit'
			, region: 'north'
			, height: 215
			, split: true
			//, collapseMode: 'mini'
			, collapsible: true
	        , collapseFirst: false
		};
		
		if(Sbi.settings && Sbi.settings.console && Sbi.settings.console.summaryPanel) {
			defaultSettings = Ext.apply(defaultSettings, Sbi.settings.console.summaryPanel);
		}
		
		var c = Ext.apply(defaultSettings, config || {});
		
		Ext.apply(this, c);
		
		var x, y, z, widgetPanel;
		
		x = new Sbi.console.ChartWidget({chartType: 'line'});
		y = new Sbi.console.ChartWidget({chartType: 'bar'});
		z = new Sbi.console.ChartWidget({chartType: 'pie'});
		widgetPanel = new Sbi.console.WidgetPanel({
			storeManager: this.storeManager
			, items: [x, y, z]
		});
		
		c = Ext.apply(c, {  	
			
	      	items: [widgetPanel]
			//html: 'Io sono il summary panel'
		});

		// constructor
		Sbi.console.SummaryPanel.superclass.constructor.call(this, c);
    
		//this.addEvents();
};

Ext.extend(Sbi.console.SummaryPanel, Ext.Panel, {
    
    services: null
    
   
    //  -- public methods ---------------------------------------------------------
    
    
    
    //  -- private methods ---------------------------------------------------------
    
    
    
    
});