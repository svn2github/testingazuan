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
		

		var params = {ds_label: config.table.dataset.label};
		this.services = new Array();
	  	this.services['getDataService'] = Sbi.config.serviceRegistry.getServiceUrl({
	  		serviceName: 'GET_CONSOLE_DATA_ACTION'
	  		, baseParams: params
	  	});
	  	
	  	this.initStore();
		this.initColumnModel();
		this.initSelectionModel();
		
		this.initFilterBar(c.FilterBarConf || {});
		


		var c = Ext.apply({}, {
			store: this.store
			, cm: this.columnModel
			, sm: this.selectionModel
			, loadMask: true
	        , viewConfig: {
            	forceFit:false,
            	autoFill: true,
            	enableRowBody:true,
            	showPreview:true
        	}
		});   
	
		// constructor
		Sbi.console.GridPanel.superclass.constructor.call(this, c);
    
		//this.addEvents();
};

Ext.extend(Sbi.console.GridPanel, Ext.grid.GridPanel, {
    
	services: null
	, store: null
	, columnModel: null
	, selectionModel: null
	, filterBar: null
    
   
    //  -- public methods ---------------------------------------------------------
    
    //  -- private methods ---------------------------------------------------------
    
	, initStore: function() {
		this.store = new Ext.data.JsonStore({
		    url: this.services['getDataService']
			, autoLoad: true
	    }); 
    
		this.store.on('loadexception', function(store, options, response, e){
	    	Sbi.exception.ExceptionHandler.handleFailure(response, options);
	    }, this);
		
		this.store.on('metachange', this.onMetaChange, this);
		//this.store.on('load', this.onDataStoreLoaded, this);
	}

	, initColumnModel: function() {
		this.columnModel = new Ext.grid.ColumnModel([
			new Ext.grid.RowNumberer(), 
			{
				header: "Data",
			    dataIndex: 'data',
			    width: 75
			}
		]);
	}
	
	, initSelectionModel: function() {
		this.selectionModel = new Ext.grid.RowSelectionModel({
			singleSelect: false
		});
	}
	
	
	, onMetaChange: function( store, meta ) {

		for(var i = 0; i < meta.fields.length; i++) {
			if(meta.fields[i].type) {
				var t = meta.fields[i].type;
			    meta.fields[i].renderer  =  Sbi.locale.formatters[t];			   
			}
			   
			if(meta.fields[i].subtype && meta.fields[i].subtype === 'html') {
				meta.fields[i].renderer  =  Sbi.locale.formatters['html'];
			}
		}
		
		meta.fields[0] = new Ext.grid.RowNumberer();
		this.getColumnModel().setConfig(meta.fields);
	}
	
	
	















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
      
      var tmpFilters = [{text: 'Brands'
                    , column: 'brandname'
                    , operator: 'EQUALS_TO'
                    , operand: 'DISTINCT'}
                    , {text: 'Products'
                    , column: 'productName'
                    , operator: 'EQUALS_TO'
                    , operand: 'DISTINCT'}
                    , {text: 'Years'
                    , column: 'year'
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
                                                                 , filters: tmpFilters}, this.store);          	          	
      }   
  }
});