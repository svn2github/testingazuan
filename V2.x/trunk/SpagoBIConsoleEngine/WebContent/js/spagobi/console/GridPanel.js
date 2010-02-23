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
			layout: 'fit'
		};
		
		if(Sbi.settings && Sbi.settings.console && Sbi.settings.console.gridPanel) {
			defaultSettings = Ext.apply(defaultSettings, Sbi.settings.console.gridPanel);
		}
		
		var c = Ext.apply(defaultSettings, config || {});
		//var filterBarConfig = c.filterBar || {};
		//delete c.filterBar;
		Ext.apply(this, c);
		
		this.services = this.services || new Array();	
		this.images = this.images || new Array();	
		
		var params = {ds_label: config.table.dataset.label};
	  this.services['getDataService'] = Sbi.config.serviceRegistry.getServiceUrl({
	  		serviceName: 'GET_CONSOLE_DATA_ACTION'
	  		, baseParams: params
	  	});
	  	
	 	this.services['start'] = this.services['start'] || Sbi.config.serviceRegistry.getServiceUrl({
			serviceName: 'START_ACTION'
			, baseParams: new Object()
		});
		this.images['start'] = this.images['start'] || "../img/ico_start.gif";
		
		this.services['stop'] = this.services['stop'] || Sbi.config.serviceRegistry.getServiceUrl({
			serviceName: 'STOP_ACTION'
			, baseParams: new Object()
		});
		this.images['stop'] = this.images['stop'] || "../img/ico_stop.gif";
		
		this.services['informationlog'] = this.services['informationlog'] || Sbi.config.serviceRegistry.getServiceUrl({
			serviceName: 'LOG_ACTION'
			, baseParams: new Object()
		});
		this.images['informationlog'] = this.images['informationlog'] || "../img/ico_info.gif";
		
		this.services['crossnav'] = this.services['crossnav'] || Sbi.config.serviceRegistry.getServiceUrl({
			serviceName: 'CROSS_ACTION'
			, baseParams: new Object()
		});
		this.images['cross_detail'] = this.images['cross_detail'] || "../img/ico_cross_detail.gif";
		this.images['popup_detail'] = this.images['popup_detail'] || "../img/ico_popup_detail.gif";
		
		this.services['monitor'] = this.services['monitor'] || Sbi.config.serviceRegistry.getServiceUrl({
			serviceName: 'MONITOR_ACTION'
			, baseParams: new Object()
		});
		this.images['monitor'] = this.images['monitor'] || "../img/ico_monitor.gif";
		
		this.services['monitor_inactive'] = this.services['monitor_inactive'] || Sbi.config.serviceRegistry.getServiceUrl({
			serviceName: 'MONITOR_ACTION'
			, baseParams: new Object()
		});
		this.images['monitor_inactive'] = this.images['monitor_inactive'] || "../img/ico_monitor_inactive.gif";
		
		this.services['errors'] = this.services['errors'] || Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'ERRORS_ACTION'
			, baseParams: new Object()
		});
		this.images['errors'] = this.images['errors'] || "../img/ico_errors.gif";
		
		this.services['errors_inactive'] = this.services['errors'] || Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'ERRORS_ACTION'
			, baseParams: new Object()
		});
		this.images['errors_inactive'] = this.images['errors_inactive'] || "../img/ico_errors_inactive.gif";
		
		this.services['warnings'] = this.services['warnings'] || Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'WARNINGS_ACTION'
			, baseParams: new Object()
		});
		this.images['warnings'] = this.images['warnings'] || "../img/ico_warnings.gif";
		
		this.services['warnings_inactive'] = this.services['errors'] || Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'WARNINGS_ACTION'
			, baseParams: new Object()
		});
		this.images['warnings_inactive'] = this.images['warnings_inactive'] || "../img/ico_warnings_inactive.gif";
		
		this.services['views'] = this.services['views'] || Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'VIEWS_ACTION'
			, baseParams: new Object()
		});
		this.images['views'] = this.images['views'] || "../img/ico_views.gif";
		
		this.services['views_inactive'] = this.services['errors'] || Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'VIEWS_ACTION'
			, baseParams: new Object()
		});
		this.images['views_inactive'] = this.images['views_inactive'] || "../img/ico_views_inactive.gif";
		
		this.services['refresh'] = this.services['refresh'] || Sbi.config.serviceRegistry.getServiceUrl({
			serviceName: 'REFRESH_ACTION'
			, baseParams: new Object()
		});
		this.images['refresh'] = this.images['refresh'] || "../img/ico_refresh.gif";
	  	
	  this.initStore();
		this.initColumnModel();
		this.initSelectionModel();	
		this.initFilterBar(c || {});

		var c = Ext.apply(c, {
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
      ,tbar: this.filterBar
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
	, headers: null
   
    //  -- public methods ---------------------------------------------------------
    
    , execInlineAction: function(e, t, action, paramConf){
        
        var index = this.getView().findRowIndex(t);
  		var record = this.store.getAt(index);   
        var dynParams =[];        
        	
  	    //adds dynamic parameters (from dataset or request)
        if(paramConf.dynamicParams) {    
           var msgErr = ""; 
      		 for (var i=0, l=paramConf.dynamicParams.length; i < l; i++){      		     
      		     var tmp = paramConf.dynamicParams[i];
        	 	 	 for(p in tmp) {
          	 	 	   if (p != 'scope'){
          	 	 		   var param = {};   
                     if (tmp['scope'] === 'dataset') {       	
            	 	 		   if(record.get(this.headers[p]) === undefined) {            	 	 		    
            	 	 		     msgErr += 'Parameter "' + p + '" undefined into dataset.<p>';
                       } else {
            	 	 		      param [tmp[p]] = record.get(this.headers[p]); 
            	 	 		      dynParams.push(param);
                       }
                      } else if (tmp['scope'] === 'env'){ 
                          if (this.executionContext[p] === undefined) {              	 	 	      
              	 	 	        msgErr += 'Parameter "' + p + '" undefined into request. <p>';
                          } else {          	 	 		           	 	 		  
                	 	 		    param [tmp[p]] = this.executionContext[p];
                	 	 		    dynParams.push(param);
                          } 	 		 
                      }          	 	 		   
          		     }
        			   }          			   
              } 	
              if  (msgErr != ""){
        			     Sbi.exception.ExceptionHandler.showErrorMessage(msgErr, 'Service Error');
              }		  
    		}
  		  		   
        Ext.Ajax.request({
			        url: this.services[action],  			       
			        params: {'message': action, 'userId': Sbi.user.userId, 
      					 'statParams': Ext.util.JSON.encode(paramConf.staticParams), 'dynParams': Ext.util.JSON.encode(dynParams)} ,			       
			        callback : function(options , success, response) {
			  	  		if (success) {
				      		if(response !== undefined && response.responseText !== undefined) {
				      			var content = Ext.util.JSON.decode( response.responseText );
				      			if (content !== undefined) {				      			  
				      				alert(content.toSource());
				      			}				      		
				      		} else {
				      			Sbi.exception.ExceptionHandler.showErrorMessage('Server response is empty', 'Service Error');
				      		}
			  	  		} else { 
			  	  			Sbi.exception.ExceptionHandler.showErrorMessage('Cannot exec action: ' + this.name, 'Service Error');
			  	  		}
			        },
			        scope: this,
					failure: Sbi.exception.ExceptionHandler.handleFailure      
				});
  			
			}
    
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
    //loads headers map with dataIndex info
    this.headers = [];
    

		for(var i = 0; i < meta.fields.length; i++) {
		
		  var localHeader = meta.fields[i].header;
          var localDataIndex = meta.fields[i].dataIndex;
          if (localHeader != ''){
            this.headers[localHeader] = localDataIndex;
      }
			if(meta.fields[i].type) {
				var t = meta.fields[i].type;
			    meta.fields[i].renderer  =  Sbi.locale.formatters[t];			   
			}
			   
			if(meta.fields[i].subtype && meta.fields[i].subtype === 'html') {
				meta.fields[i].renderer  =  Sbi.locale.formatters['html'];
			}
		}


    //adds inline action buttons
		for(var i=0, l= this.table.inlineActions.length; i < l; i++){
		 
		  var img = this.images[this.table.inlineActions[i].name];

		  //defines image's path
		  if (this.table.inlineActions[i].name === 'crossnav'){
		    if (this.table.inlineActions[i].config.target === 'new')
		      img = this.images['cross_detail'];
		    else
		      img = this.images['popup_detail'];
      }

      var bc = new Ext.grid.ButtonColumn({
           dataIndex: this.table.inlineActions[i].name + "-"+ i
			   , tooltip: this.table.inlineActions[i].name
         , imgSrc: img
         , clickHandler:  function(e, t){   
                this.grid.execInlineAction(e, t, this.action, this.parameters)     
         }
         , hideable: true
         , hidden: this.table.inlineActions[i].hidden
         , width: 25
         , action: this.table.inlineActions[i].name
         , parameters: this.table.inlineActions[i].config
      });
      bc.init(this);
      meta.fields.push(bc);	
  	}	
    //adds numeration column    
		meta.fields[0] = new Ext.grid.RowNumberer();
    //update columnmodel configuration
		this.getColumnModel().setConfig(meta.fields);

	}



   , initFilterBar: function(filterBarConf) {
	
      if (filterBarConf.type === 'default'){
    	   alert("Default filterbar working in progress!!");
    	   
      } else if (filterBarConf.type === 'custom' || filterBarConf.type === 'automatic') {

          this.filterBar = new Sbi.console.CustomFilteringToolbar({filterBar: filterBarConf
                                                                 , store: this.store
                                                                  });          	  
        	       	
      }   
  }

});