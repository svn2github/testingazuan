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

Sbi.console.DetailPanel = function(config) {
	
		var defaultSettings = {
			layout: 'fit'
			, bodyStyle: 'padding: 8px'
			, region: 'center'
		};
		
		if(Sbi.settings && Sbi.settings.console && Sbi.settings.console.detailPanel) {
			defaultSettings = Ext.apply(defaultSettings, Sbi.settings.console.detailPanel);
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
		
		
		this.initDetailPages(c.detailPagesConfig || {});
		
		c = Ext.apply(c, {  
	      	items: [new Ext.TabPanel({
	      		activeTab: 0
	      		, items: this.pages
	      	})]
			//, html: 'Io sono il detail panel'
		});

		// constructor
		Sbi.console.DetailPanel.superclass.constructor.call(this, c);
    
		//this.addEvents();
};

Ext.extend(Sbi.console.DetailPanel, Ext.Panel, {
    
    //services: null
    pages: null
    
   
    //  -- public methods ---------------------------------------------------------
    
    
    
    //  -- private methods ---------------------------------------------------------
    
    , initDetailPages: function(conf) {
		this.pages = new Array();
		
		var detailPage = null;
		
		detailPage = new Sbi.console.DetailPage({
			title: 'Pag. 1'
			, msg: 'Io sono la prima pagina'
		});
		this.pages.push(detailPage);
		
		detailPage = new Sbi.console.DetailPage({
			title: 'Pag. 2'
			, msg: 'Io sono la seconda pagina'
		});
		this.pages.push(detailPage);
		
		detailPage = new Sbi.console.DetailPage({
			title: 'Pag. 3'
			, msg: 'Io sono la terza pagina'
		});
		this.pages.push(detailPage);
		
		/*
		var detailPage = new Ext.Panel({
			layout: 'fit'
			, title: 'Pagina 1'
			, html: 'Io sono la prima pagina'
		});
		this.pages.push(detailPage);	
		*/	
	}
    
    
});