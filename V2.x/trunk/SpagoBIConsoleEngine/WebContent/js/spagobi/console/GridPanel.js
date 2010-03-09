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
			, loadMask: false
		    , viewConfig: {
	          	forceFit:false,
	           	autoFill: true,
	           	enableRowBody:true,
	           	showPreview:true
	        }
		};
		
		if(Sbi.settings && Sbi.settings.console && Sbi.settings.console.gridPanel) {
			defaultSettings = Ext.apply(defaultSettings, Sbi.settings.console.gridPanel);
		}
		
		var c = Ext.apply(defaultSettings, config || {});
		
		c.storeId = c.dataset;
		delete c.dataset;	
		var tableConfig = c.table || {};
		var filterConfig =  c.filterBar || {};
		filterConfig.executionContext = c.executionContext;
		//delete c.filterBar;
		Ext.apply(this, c);
		
		this.initServices();
		this.initStore();
		this.initColumnModel();
		this.initSelectionModel();	
		this.initFilterBar(filterConfig);

		var c = Ext.apply(c, {
			store: this.store
			, cm: this.columnModel
			, sm: this.selectionModel
			, tbar: this.filterBar
		});   
		
		// constructor
		Sbi.console.GridPanel.superclass.constructor.call(this, c);
};

Ext.extend(Sbi.console.GridPanel, Ext.grid.GridPanel, {
    
	services: null
	, store: null
	, columnModel: null
	, selectionModel: null
	, filterBar: null
	, headers: null
	
	, GRID_ACTIONS: {
		start: {serviceName: 'START_ACTION', images: '../img/ico_start.gif'}
		, stop: {serviceName: 'STOP_ACTION', images: '../img/ico_stop.gif'}
		, informationlog: {serviceName: 'START_ACTION', images: '../img/ico_info.gif'}
		, crossnav: {serviceName: 'CROSS_ACTION', images: {cross_detail: '../img/ico_cross_detail.gif', popup_detail: '../img/ico_popup_detail.gif'}}
		, monitor: {serviceName: 'MONITOR_ACTION', images: '../img/ico_monitor.gif'}
		, monitor_inactive: {serviceName: 'MONITOR_ACTION', images: '../img/ico_monitor_inactive.gif'}
		, errors: {serviceName: 'ERRORS_ACTION', images: '../img/ico_errors.gif'}
		, errors_inactive: {serviceName: 'ERRORS_ACTION', images: '../img/ico_errors_inactive.gif'}
		, warnings: {serviceName: 'WARNINGS_ACTION', images: '../img/ico_warnings.gif'}
		, warnings_inactive: {serviceName: 'WARNINGS_ACTION', images: '../img/ico_warnings_inactive.gif'}
		, views: {serviceName: 'VIEWS_ACTION', images: '../img/ico_views.gif'}
		, views_inactive: {serviceName: 'VIEWS_ACTION', images: '../img/ico_views_inactive.gif'}
		, refresh: {serviceName: 'REFRESH_ACTION', images: '../img/ico_refresh.gif'}
	}
   
    //  -- public methods ---------------------------------------------------------
    
    , execInlineAction: function(e, t, action, paramConf){
        var index = this.getView().findRowIndex(t);

  		  if (action === 'crossnav'){
            //cross to new document
  		      this.execCrossNav(paramConf, index); 		    
  		  } else {
	  		    //executes back-end action
	  		    var dynParams =[];  
	  		    var record = this.store.getAt(index);
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
	            	 	 	} //for          			   
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
  			
	}
    
    //  -- private methods ---------------------------------------------------------
    
    
    , initServices: function() {
    	this.services = this.services || new Array();	
		this.images = this.images || new Array();	
				
		for(var actionName in this.GRID_ACTIONS) {
			var actionConf = this.GRID_ACTIONS[actionName];
			this.services[actionName] = this.services[actionName] || Sbi.config.serviceRegistry.getServiceUrl({
				serviceName: actionConf.serviceName
				, baseParams: new Object()
			});
			if( (typeof actionConf.images) === 'string' ) {
				this.images[actionName] = this.images[actionName] || actionConf.images;
			} else {
				for(var actionState in actionConf.images) {
					this.images[actionState] = this.images[actionState] || actionConf.images[actionState];
				}
			}
		}
    }
    
	, initStore: function() {
		this.store = this.storeManager.getStore(this.storeId);
		
		this.store.on('loadexception', function(store, options, response, e){
	    	Sbi.exception.ExceptionHandler.handleFailure(response, options);
	    }, this);
		
		this.store.on('metachange', this.onMetaChange, this);
	}

	, initColumnModel: function() {
	
		this.columnModel = new Ext.grid.ColumnModel([
			new Ext.grid.RowNumberer(), 
			{
				header: "Data",
			    dataIndex: 'data',
			    width: 100
			}
		]);
		
	}
	
	, initSelectionModel: function() {
		this.selectionModel = new Ext.grid.RowSelectionModel({
			singleSelect: false
		});
	}
	
	
	, onMetaChange: function( store, meta ) {
		//if(store.storeId === 'testConsole') alert('onMetaChange IN');

		var tmpMeta =  Ext.apply({}, meta); // meta;
		var fields = tmpMeta.fields;
		tmpMeta.fields = new Array(fields.length);
		for(var i = 0; i < fields.length; i++) {
			if( (typeof fields[i]) === 'string') {
				fields[i] = {name: fields[i]};
			}
			tmpMeta.fields[i] = Ext.apply({}, fields[i]);
		}
		
		this.headers = [];

		for(var i = 0; i < tmpMeta.fields.length; i++) {

			var localHeader = tmpMeta.fields[i].header;
			this.headers[localHeader] = this.store.getFieldNameByAlias(localHeader);
			if(tmpMeta.fields[i].type) {
				var t = tmpMeta.fields[i].type;
				tmpMeta.fields[i].renderer  =  Sbi.locale.formatters[t];			   
			}
			   
			if(tmpMeta.fields[i].subtype && tmpMeta.fields[i].subtype === 'html') {
				tmpMeta.fields[i].renderer  =  Sbi.locale.formatters['html'];
			}
	    
			//adds inline charts if it's necessary
			if (this.filterBar.inlineCharts){  	     
				for(var j=0, len= this.filterBar.inlineCharts.length; j < len; j++){
					if (this.filterBar.inlineCharts[j].column === tmpMeta.fields[i].header){
						if(this.filterBar.inlineCharts[j].type && this.filterBar.inlineCharts[j].type === 'bar' ) {
							tmpMeta.fields[i].renderer  =  Sbi.console.commons.Format.inlineBarRenderer(this.filterBar.inlineCharts[j]);
						}
  	            
						if(this.filterBar.inlineCharts[j].type && this.filterBar.inlineCharts[j].type === 'point') {
							tmpMeta.fields[i].renderer  =  Sbi.console.commons.Format.inlinePointRenderer(this.filterBar.inlineCharts[j]);
						}
					}            
				} //for on j
			} 
		} // for on i

	    //adds inline action buttons
		if (this.filterBar.inlineActions){
			for(var i=0, l= this.filterBar.inlineActions.length; i < l; i++){ 
				var img = this.images[this.filterBar.inlineActions[i].name];
  	
				//defines image's path
				if (this.filterBar.inlineActions[i].name === 'crossnav'){
					if (this.filterBar.inlineActions[i].config.target === 'new')
						img = this.images['cross_detail'];
					else
						img = this.images['popup_detail'];
					}
  	
					var bc = new Ext.grid.ButtonColumn({
						dataIndex: this.filterBar.inlineActions[i].name + "-"+ i
						, tooltip: (this.filterBar.inlineActions[i].tooltip != undefined)? this.filterBar.inlineActions[i].tooltip:this.filterBar.inlineActions[i].name
						, imgSrc: img
						, clickHandler:  function(e, t){   
							this.grid.execInlineAction(e, t, this.action, this.parameters)     
						}
						, hideable: true
						, hidden: this.filterBar.inlineActions[i].hidden
						, width: 25
						, action: this.filterBar.inlineActions[i].name
						, parameters: this.filterBar.inlineActions[i].config
					});
					bc.init(this);
					tmpMeta.fields.push(bc);	
  	  		}	
		}
	    //adds numeration column    
		tmpMeta.fields[0] = new Ext.grid.RowNumberer();
		
		//if(store.storeId === 'testConsole') alert('onMetaChange POW');
		
	    //update columnmodel configuration
		this.getColumnModel().setConfig(tmpMeta.fields);
		
		//if(store.storeId === 'testConsole') alert('onMetaChange OUT');
		
		
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

  , execCrossNav: function(docConfig, index){

	var record = this.store.getAt(index);   
    var msg = {
    			label:docConfig.document.label
    		  , windowName: this.name				
    		  , target: (docConfig.target === 'new')?'self':'popup'						
    		  };
    			
	var separator = '';
	//adds static parameters for cross navigation
	if(docConfig.document.staticParams) {
		msg.parameters = '';		
		for(p in docConfig.document.staticParams) {
			msg.parameters += separator + p + '=' + docConfig.document.staticParams[p];
			separator = '&';
		}
	}
	  
    //adds dynamic parameters (environment type) 
	if(docConfig.document.dynamicParams) {
	    var msgErr = ""; 
	    for (var i=0, l=docConfig.document.dynamicParams.length; i < l; i++){     
  		    var tmp = docConfig.document.dynamicParams[i];
  		    for(p in tmp) {
     	 	    if (p != 'scope'){
     	 	    	if (tmp['scope'] === 'dataset') {       	        	 	 		    
     	 	    	   if(record.get(this.headers[p]) === undefined) {
            	 	 		     msgErr += 'Parameter "' + p + '" undefined into dataset.<p>';
                       } else {
                          msg.parameters += separator + tmp[p] + '=' +record.get(this.headers[p]);
    				              separator = '&';            	 	 		  
                       }
                    } else  if (tmp['scope'] === 'env'){ 
                       if (this.executionContext[p] === undefined) {              	 	 	      
          	 	 	        msgErr += 'Parameter "' + p + '" undefined into request. <p>';
                       } else {          	 	 		           	 	 		  
        	 	 		    msg.parameters += separator + tmp[p] + '=' + this.executionContext[p];
				            separator = '&';
                       } 	 		 
                    }          	 	 		   
    	      }
    	   }
      	   if  (msgErr != ""){
          		Sbi.exception.ExceptionHandler.showErrorMessage(msgErr, 'Service Error');
           }	
	    }	
	}
//		alert("msg.parameters: " + msg.parameters.toSource());
	sendMessage(msg, 'crossnavigation');
  }
  
});