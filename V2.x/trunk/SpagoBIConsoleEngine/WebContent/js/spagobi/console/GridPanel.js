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

Sbi.console.GridPanel = function(config) {
	
		var defaultSettings = {
			//title: LN('sbi.qbe.queryeditor.title')
		};
		
		if(Sbi.settings && Sbi.settings.console && Sbi.settings.console.gridPanel) {
			defaultSettings = Ext.apply(defaultSettings, Sbi.settings.console.gridPanel);
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
		
		
		this.initFilterBar(c.FilterBarConf || {});
		
		c = Ext.apply(c, {layout: 'fit'
		                  , region: 'center'
		                  , bodyStyle: 'padding: 8px'
		                  , tbar: this.filterBar
    		              , html: 'Io sono il grid panel !!!!!!!!!'
		});

		// constructor
		Sbi.console.GridPanel.superclass.constructor.call(this, c);
    
		//this.addEvents();
};

Ext.extend(Sbi.console.GridPanel, Ext.Panel, {
    
    services: null
   , filterBar: null
    
   
    //  -- public methods ---------------------------------------------------------
    
    
    
    //  -- private methods ---------------------------------------------------------
    
   , initFilterBar: function(filterBar) {
      
      //Template simulator:
      var tmpActions = [{name: 'refresh'
                    , hidden: false}
                    , {name: 'errors'
                    , hidden: false}
                    , {name: 'warnings'
                    , hidden: false}
                    , {name: 'views'
                    , hidden: false}];
      
      var tmpFilters = [{text: 'Filtro 1'
                    , column: 'column1'
                    , operator: 'EQUALS_TO'
                    , operand: 'DISTINCT'}
                    , {text: 'Filtro 2'
                    , column: 'column2'
                    , operator: 'EQUALS_TO'
                    , operand: 'DISTINCT'}
                    , {text: 'Filtro 3'
                    , column: 'column3'
                    , operator: 'EQUALS_TO'
                    , operand: 'DISTINCT'}
                    , {text: 'Filtro 4'
                    , column: 'column4'
                    , operator: 'EQUALS_TO'
                    , operand: 'DISTINCT'}];
                    
      var tmpDefaults = { operator: 'EQUALS_TO'
                        , operand: 'DISTINCT'};

      this.filterBar = new Sbi.console.FilteringToolbar({type:'custom'  //custom | default | automatic
                                                        , defaults: tmpDefaults
                                                        , actions: tmpActions
                                                        , filters: tmpFilters});
     }
    
    
});