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
  * - name (mail)
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
		
		Ext.apply(this, c);
		
		/*
		this.services = this.services || new Array();	
		this.services['doThat'] = this.services['doThat'] || Sbi.config.serviceRegistry.getServiceUrl({
			serviceName: 'DO_THAT_ACTION'
			, baseParams: new Object()
		});
		*/
		
		
		this.initSummaryPanel(c.summaryPanelConfig || {});
		this.initDetailPanel(c.detailPannelConfig || {});
	
		c = Ext.apply(c, {  	
	      	items: [this.summaryPanel, this.detailPanel]
		});

		// constructor
		Sbi.console.ConsolePanel.superclass.constructor.call(this, c);
    
		this.addEvents();
};

Ext.extend(Sbi.console.ConsolePanel, Ext.Panel, {
    
    services: null
    , summaryPanel: null
    , detailPanel: null
   
   
    //  -- public methods ---------------------------------------------------------
    
    
    
    //  -- private methods ---------------------------------------------------------
    
    , initSummaryPanel: function(conf) {
		this.summaryPanel = new Ext.Panel({
			layout: 'fit'
			, region: 'north'
			, html: 'Io sono il summary panel'
		});
	}

	, initDetailPanel: function(conf) {
		this.detailPanel = new Ext.Panel({
			layout: 'fit'
			, region: 'center'
			, html: 'Io sono il detail panel'
		});
	}


    
    
});