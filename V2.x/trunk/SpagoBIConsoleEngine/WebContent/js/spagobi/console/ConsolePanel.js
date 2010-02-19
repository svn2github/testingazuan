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

Sbi.console.ConsolePanel = function(config) {
	
	var defaultSettings = {
		title: LN('sbi.qbe.queryeditor.title'),
		layout: 'border'
	};
		
	if(Sbi.settings && Sbi.settings.console && Sbi.settings.console.consolePanel) {
		defaultSettings = Ext.apply(defaultSettings, Sbi.settings.console.consolePanel);
	}
		
	var c = Ext.apply(defaultSettings, config || {});
	
	var datasetsConfig = c.datasets || [];
	delete c.datasets;
	
	var summaryPanelConfig = c.summaryPanel || {};
	delete c.summaryPanel;
	
	var detailPanelConfig = c.detailPanel || {};
	delete c.detailPanel;
		
	Ext.apply(this, c);
		
	
	this.initStoreManager(datasetsConfig);
	summaryPanelConfig.storeManager = this.storeManager;
	this.initSummaryPanel(summaryPanelConfig);
	detailPanelConfig.storeManager = this.storeManager;
	this.initDetailPanel(detailPanelConfig);
	
	c = Ext.apply(c, {  	
	   	items: [this.summaryPanel, this.detailPanel]
	});

	// constructor
	Sbi.console.ConsolePanel.superclass.constructor.call(this, c);
};

Ext.extend(Sbi.console.ConsolePanel, Ext.Panel, {
    
    services: null
    , storeManager: null
    , summaryPanel: null
    , detailPanel: null
   
   
    //  -- public methods ---------------------------------------------------------
    
    
    
    //  -- private methods ---------------------------------------------------------
    
    , initStoreManager: function(datasetsConfig) {
		
		datasetsConfig = datasetsConfig || [];
		
		this.storeManager = new Ext.util.MixedCollection();
		for(var i = 0, l = datasetsConfig.length, s; i < l; i++) {
			s = new Ext.data.JsonStore({
				id: datasetsConfig[i].id
				, url: Sbi.config.serviceRegistry.getServiceUrl({
					serviceName: 'GET_CONSOLE_DATA_ACTION'
					, baseParams: {ds_label: datasetsConfig[i].label}
				})
				, autoLoad: false
			}); 
			this.storeManager.add(datasetsConfig[i].id, s);
		}
		
		// for easy debug purpose
		var testStore = new Ext.data.JsonStore({
	        fields:['name', 'visits', 'views'],
	        data: [
	            {name:'Jul 07', visits: 245000, views: 3000000},
	            {name:'Aug 07', visits: 240000, views: 3500000},
	            {name:'Sep 07', visits: 355000, views: 4000000},
	            {name:'Oct 07', visits: 375000, views: 4200000},
	            {name:'Nov 07', visits: 490000, views: 4500000},
	            {name:'Dec 07', visits: 495000, views: 5800000},
	            {name:'Jan 08', visits: 520000, views: 6000000},
	            {name:'Feb 08', visits: 620000, views: 7500000}
	        ]
	    });
		this.storeManager.add('testStore', testStore);
	}
    
    , initSummaryPanel: function(conf) {
		this.summaryPanel = new Sbi.console.SummaryPanel(conf);
	}

	, initDetailPanel: function(conf) {
		this.detailPanel = new Sbi.console.DetailPanel(conf);
	}


    
    
});