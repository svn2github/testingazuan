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
		
		var params = {ds_label: 'testmeter'};
		this.services = new Array();
  	this.services['getDataService'] = Sbi.config.serviceRegistry.getServiceUrl({
  		serviceName: 'GET_CONSOLE_DATA_ACTION'
  		, baseParams: params
  	});
		
		this.store = new Ext.data.JsonStore({
          root: 'results'
        , idProperty: 'serie'
        , fields: ['serie', 'value']
		    , url: this.services['getDataService']
		    , autoLoad: true
    }); 
    this.store.on('loadexception', function(store, options, response, e){
    	Sbi.exception.ExceptionHandler.handleFailure(response, options);
    }, this);
    
    var sm = new Ext.grid.CheckboxSelectionModel();
		//var sm = new Ext.grid.RowSelectionModel({singleSelect:true});
		this.initFilterBar(c.FilterBarConf || {});
		
/*		c = Ext.apply(c, {layout: 'fit'
		                  , region: 'center'
		                  , bodyStyle: 'padding: 8px'
		                  , tbar: this.filterBar
    		            //  , html: 'Io sono il grid panel !!!!!!!!!'
		                });
*/

    var c = Ext.apply({}, {
          store: this.store
          , columns: [
                {header: 'Serie', sortable: true, width: 50, dataIndex: 'serie'}
              , {header: 'Value', sortable: true, width: 50, dataIndex: 'value'}
              , sm
          ]
       //   , plugins: [ this.applyColumn, this.execColumn ]
    		, viewConfig: {
            	forceFit: true
            , emptyText: ' '
    		}
          , tbar: this.filterBar
          , collapsible: false
          , autoScroll: true
          , sm : sm
          , hidden: false
  	});   
	
		// constructor
		Sbi.console.GridPanel.superclass.constructor.call(this, c);
    
		//this.addEvents();
};

Ext.extend(Sbi.console.GridPanel, Ext.grid.GridPanel, {
    
    services: null
   , store: null
   , filterBar: null
    
   
    //  -- public methods ---------------------------------------------------------
    
    , synchronize: function(  ) {  		
  			this.store.load();  	
  	}
    
    //  -- private methods ---------------------------------------------------------
    
   , initFilterBar: function(filterBar) {
      
      //Template simulator:
      var tmpActions = [{name: 'refresh'
                    , hidden: false
                    , config: {}}
                    , {name: 'errors'
                    ,  hidden: false
                    ,  config: {
                        staticParams: {param1: 'paramValue1'
                                      ,param2: 'paramValue2' }
                      }
                     }                    
                    , {name: 'errors_inactive'
                    ,  hidden: true
                    ,  config: {
                        staticParams: {param1: 'paramValue'}
                      }
                    }
                    , {name: 'warnings'
                    ,  hidden: false
                    ,  config: {
                        staticParams: {param1: 'paramValue'}
                      }
                     }
                    , {name: 'warnings_inactive'
                    ,  hidden: true
                    ,  config: {
                        staticParams: {param1: 'paramValue'}
                      }
                     }
                    , {name: 'views'
                    ,  hidden: false
                    ,  config: {
                        staticParams: {param1: 'paramValue'}
                      }
                     }                  
                    , {name: 'views_inactive'
                    ,  hidden: true
                    ,  config: {
                        staticParams: {param1: 'paramValue'}
                      }
                     }];
      
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
                    
      var tmpDefaults = { type: 'custom'
                        , operator: 'EQUALS_TO'
                        , operand: 'DISTINCT'};
                        
     // var type = 'custom'; //default | custom | automatic
 
      if (tmpDefaults.type === 'default'){
    	   alert("Default filterbar working in progress!!");
      } else if (tmpDefaults.type === 'custom' || tmpDefaults.type === 'automatic'){
          this.filterBar = new Sbi.console.CustomFilteringToolbar({defaults: tmpDefaults
                                                                 , actions: tmpActions
                                                                 , filters: tmpFilters});          	          	
      }   
  }
});