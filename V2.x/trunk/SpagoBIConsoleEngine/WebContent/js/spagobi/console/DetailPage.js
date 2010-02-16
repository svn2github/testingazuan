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
  * 
  * - Andrea Gioia (andrea.gioia@eng.it)
  * - Antonella Giachino (antonella.giachino@eng.it)
  */

Ext.ns("Sbi.console");

Sbi.console.DetailPage = function(config) {
	
		var defaultSettings = {
			title: 'DetailPage'
		};
		
		if(Sbi.settings && Sbi.settings.console && Sbi.settings.console.detailPage) {
			defaultSettings = Ext.apply(defaultSettings, Sbi.settings.console.detailPage);
		}
		
		var c = Ext.apply(defaultSettings, config || {});
		var navigationBarConfig = c.navigationBar || {};
		delete c.navigationBar;
		//var navigationBar = c.navigationBar || {};
		//delete c.navigationBar;
		
		Ext.apply(this, c);
		

		
		this.initNavigationToolbar(navigationBarConfig);
		this.initGridPanel(c.gridPanel || {});
		
		c = Ext.apply(c, {  	
			//html: this.msg
			tbar: this.navigationToolbar
	      	, items: [this.gridPanel]	      	
		});

		// constructor
		Sbi.console.DetailPage.superclass.constructor.call(this, c);
    
};

Ext.extend(Sbi.console.DetailPage, Ext.Panel, {
    
    services: null
    , navigationToolbar: null
    , gridPanel: null
   
    //  -- public methods ---------------------------------------------------------
    
    //  -- private methods ---------------------------------------------------------
    , initNavigationToolbar: function(navigationBarConf) {
    	this.navigationToolbar = new Sbi.console.NavigationToolbar(navigationBarConf);
    }
    
    , initGridPanel: function(conf){
     
    	//template simulator
        var tmpTable = {                                                            
                          dataset: {
                                // LABEL del dataset in spagobi
                              	label: 'testConsole'
                  			        // parametri statici e dinamici definiti come navigationBar
                                 	, params: {}  
                                 	, refreshTime: '30' // in secondi
                          }};
                          
        
        
        this.gridPanel = new Sbi.console.GridPanel({table: tmpTable});
     // this.gridPanel = new Sbi.console.GridPanel(conf);
    }
    
    
});