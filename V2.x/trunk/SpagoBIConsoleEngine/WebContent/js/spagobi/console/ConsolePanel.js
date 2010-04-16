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
		title: LN('sbi.console.consolepanel.title'),
		layout: 'border'
	};
		
	if(Sbi.settings && Sbi.settings.console && Sbi.settings.console.consolePanel) {
		defaultSettings = Ext.apply(defaultSettings, Sbi.settings.console.consolePanel);
	}
		
	var c = Ext.apply(defaultSettings, config || {});
	
	var datasetsConfig = c.datasets || [];
	delete c.datasets;
	
	var summaryPanelConfig = c.summaryPanel;
	delete c.summaryPanel;
	
	var detailPanelConfig = c.detailPanel || {};
	detailPanelConfig.executionContext = c.executionContext; 
	delete c.detailPanel;
		
	Ext.apply(this, c);
		
	this.services = this.services || new Array();	
	this.services['export'] = this.services['export'] || Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'EXPORT_ACTION'
		, baseParams: new Object()
	});
	
	
	this.initStoreManager(datasetsConfig);
	if (summaryPanelConfig !== undefined){
		summaryPanelConfig.storeManager = this.storeManager;
		this.initSummaryPanel(summaryPanelConfig);
	}else{
		this.summaryPanel = {};
	}
	detailPanelConfig.storeManager = this.storeManager;
	this.initDetailPanel(detailPanelConfig);
	
	c = Ext.apply(c, {  	
		items: [this.summaryPanel, this.detailPanel, {
			title: 'Export panel'
			, hidden: true
			,region: 'south'
				, tools: [{
					id:'gear',
					qtip: LN('export as text'),
					hidden: false,
					handler: this.exportConsole,
					scope: this
				}]
				, html: 'Click on the button up here to export console document'
		}]
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
	
		this.storeManager = new Sbi.console.StoreManager({datasetsConfig: datasetsConfig});
		
	}
    
    , initSummaryPanel: function(conf) {
		this.summaryPanel = new Sbi.console.SummaryPanel(conf);
	}

	, initDetailPanel: function(conf) {
		this.detailPanel = new Sbi.console.DetailPanel(conf);
	}
	
	, exportConsole: function() {
		
		var detailPage = this.detailPanel.getActivePage();
	
		var columnConfigs = new Array();
		var cm, colNo;
		cm = detailPage.gridPanel.columnModel;
		colNo = cm.getColumnCount();
		for(var i = 0; i < colNo; i++) {
			var c = {};
			c.header = cm.getColumnHeader(i);
			c.id = cm.getColumnId(i);
			c.tooltip = cm.getColumnTooltip(i);
			c.width = cm.getColumnWidth(i);
			c.dataIndex = cm.getDataIndex(i);
			c.hidden = cm.isHidden(i);
			columnConfigs.push(c);
		}
		
		var params = {
			mimeType: 'application/pdf'
			, responseType: 'attachment'
			, datasetLabel: detailPage.getStore().getDsLabel()
			, meta: Ext.util.JSON.encode(columnConfigs)
		};
		
		Sbi.Sync.request({
			url: this.services['export']
			, params: params
		});
	}


    
    
});