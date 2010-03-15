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
	      //    ,  stateful: true
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
	// grid
	, store: null
	, columnModel: null
	, selectionModel: null
	
	// popup
	, errorWin: null
	, warningWin: null
	
	// conf bloks
	, filterBar: null
	, inlineCharts: null
	, inlineActions: null
	
	
	, GRID_ACTIONS: {
		start: {serviceName: 'START_ACTION', images: '../img/ico_start.gif'}
		, stop: {serviceName: 'STOP_ACTION', images: '../img/ico_stop.gif'}
		, informationlog: {serviceName: 'START_ACTION', images: '../img/ico_info.gif'}
		, crossnav: {serviceName: 'CROSS_ACTION', images: {cross_detail: '../img/ico_cross_detail.gif', popup_detail: '../img/ico_popup_detail.gif'}}
		, monitor: {serviceName: 'MONITOR_ACTION', images: {active: '../img/ico_monitor.gif', inactive: '../img/ico_monitor_inactive.gif'}}
		, errors: {serviceName: 'ERRORS_ACTION', images: '../img/ico_errors.gif'}
		, errors_inactive: {serviceName: 'ERRORS_ACTION', images: '../img/ico_errors_inactive.gif'}
		, warnings: {serviceName: 'WARNINGS_ACTION', images: '../img/ico_warnings.gif'}
		, warnings_inactive: {serviceName: 'WARNINGS_ACTION', images: '../img/ico_warnings_inactive.gif'}
		, views: {serviceName: 'VIEWS_ACTION', images: '../img/ico_views.gif'}
		, views_inactive: {serviceName: 'VIEWS_ACTION', images: '../img/ico_views_inactive.gif'}
		, refresh: {serviceName: 'REFRESH_ACTION', images: '../img/ico_refresh.gif'}
	}
   
    //  -- public methods ---------------------------------------------------------
    
    
	, resolveParameters: function(parameters, record, context) {
		var results = {};  
		
		results = Ext.apply(results, parameters.staticParams);
		
		var dynamicParams = parameters.dynamicParams;
	    if(dynamicParams) {        	
	    	var msgErr = ""; 
	      	for (var i = 0, l = dynamicParams.length; i < l; i++) {      		     
	      		var param = dynamicParams[i]; 
	        	for(p in param) { 
	        		if(p === 'scope') continue;
		            if (param.scope === 'dataset') {       	
	                	if(record.get(this.store.getFieldNameByAlias(p)) === undefined) {            	 	 		    
	                		msgErr += 'Parameter "' + p + '" undefined into dataset.<p>';
				        } else {
				        	results[param[p]] = record.get(this.store.getFieldNameByAlias(p)); 
				        }
		            } else if (param.scope === 'env'){ 
		            	if (context[p] === undefined) {              	 	 	      
		            		msgErr += 'Parameter "' + p + '" undefined into request. <p>';
	                    } else {          	 	 		           	 	 		  
	                    	results[param[p]] = context[p];
	                    } 	 		 
	                }          	 	 		   
	          		    
	        	 }          			   
	      	} 
	      	
	        if  (msgErr != ""){
	        	Sbi.Msg.showError(msgErr, 'Service Error');
	        }		  
	    }
	    
	    return results;
	}
	
	//, execCrossNav: function(docConfig, index){
	, execCrossNav: function(actionName, record, index, options){
		
		
		var msg = {
			label: options.document.label
	    	, windowName: this.name				
	    	, target: (options.target === 'new')? 'self': 'popup'						
	    };
	    
		var params =  this.resolveParameters(options.document, record, this.executionContext);
		var separator = '';
		msg.parameters = '';
		for(p in params) {
			msg.parameters += separator + p + '=' + params[p];
			separator = '&';
		}
	
		sendMessage(msg, 'crossnavigation');
	}

	
	, execAction: function(actionName, r, index, options) {
		
		var params = this.resolveParameters(options, r, this.executionContext);
		params = Ext.apply(params, {
  			message: actionName, 
        	userId: Sbi.user.userId 
  		}); 
  			 
  		Ext.Ajax.request({
	       	url: this.services[actionName] 			       
	       	, params: params 			       
	    	, success: function(response, options) {
	    		if(response !== undefined && response.responseText !== undefined) {
						var content = Ext.util.JSON.decode( response.responseText );
						if (content !== undefined) {				      			  
							alert(content.toSource());
						}				      		
    			} else {
    				Sbi.Msg.showError('Server response is empty', 'Service Error');
    			}
	    	}
	    	, failure: Sbi.exception.ExceptionHandler.onServiceRequestFailure
	    	, scope: this     
	    });

    }

	, showErrors: function(actionName, r, index, options) {
		if(this.errorWin === null) {
			this.errorWin = new Sbi.console.MasterDetailWindow({
				serviceName: 'GET_ERROR_LIST_ACTION'
			});
		}
		this.errorWin.reloadMasterList({id: 'Blue Label'});
		this.errorWin.show();
	}
	
	, showWarnings: function(actionName, r, index, options) {
		if(this.warningWin === null) {
			this.warningWin = new Sbi.console.MasterDetailWindow({
				serviceName: 'GET_WARNING_LIST_ACTION'
			});
		}
		this.warningWin.reloadMasterList({id: 'Jeffers'});
		this.warningWin.show();
	}
	
	, execMonitor: function(actionName, r, index, options) {
		
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
		this.store.remoteSort = false;  //local type		
		this.store.on('exception', Sbi.exception.ExceptionHandler.onStoreLoadException, this);
		this.store.on('load', this.onLoad, this);
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
		this.columnModel.defaultSortable = true;  
		
	}
	
	, initSelectionModel: function() {
		this.selectionModel = new Ext.grid.RowSelectionModel({
			singleSelect: false
		});
	}
	
	, initFilterBar: function(filterBarConf) {
		if (filterBarConf.type === 'default') {
			Sbi.Msg.showError('Toolbar of type [' + filterBarConf.type + '] is not yet supported');
		} else if (filterBarConf.type === 'custom' || filterBarConf.type === 'automatic') {
		    this.filterBar = new Sbi.console.CustomFilteringToolbar({filterBar: filterBarConf, store: this.store});          	  
		}  else {
			Sbi.Msg.showError('Toolbar of type [' + filterBarConf.type + '] is not supported');
		}
	}
	
	//redefines the renderer for inline charts.
	, updateInLineCharts: function(){

		for(var j = 0, len = this.inlineCharts.length; j < len; j++) {
			var idx = this.getColumnModel().findColumnIndex(this.store.getFieldNameByAlias(this.inlineCharts[j].column));
			this.getColumnModel().setRenderer(idx, this.createInlineChartRenderer(this.inlineCharts[j]) );			
		}
	}
	
	// -- callbacks ---------------------------------------------------------------------------------------------
	//defines the max, min and tot value on all records (only for columns visualized as chart)
	, onLoad: function(){
		var numRec = this.store.getCount();
		var minValue = 0;
		var maxValue = 0;
		var totValue = 0;
		
		for(var p = 0, len = this.inlineCharts.length; p < len; p++) {
			minValue = 0;
			maxValue = 0;
			totValue = 0;		
			if (this.inlineCharts[p] !== undefined){		
				for (var i=0; i < numRec; i++){
					var tmpRec = this.store.getAt(i);
					var tmpValue = tmpRec.get(this.store.getFieldNameByAlias(this.inlineCharts[p].column));
					if (tmpValue !== undefined){
						totValue = totValue + tmpValue;
						if ( tmpValue < minValue || i === 0) minValue = tmpValue;
						
						if ( tmpValue > maxValue ) maxValue = tmpValue;
					}
				}  

				//update initial value config with news								
				this.inlineCharts[p].maxValue = maxValue;
				this.inlineCharts[p].minValue = minValue;
				this.inlineCharts[p].totValue = totValue; 	
			}
		}
		
		this.updateInLineCharts();
	}
	
	, onMetaChange: function( store, meta ) {
		var i;
	    var fieldsMap = {};

		var tmpMeta =  Ext.apply({}, meta); // meta;
		var fields = tmpMeta.fields;
		tmpMeta.fields = new Array(fields.length);
		for(i = 0; i < fields.length; i++) {
			if( (typeof fields[i]) === 'string') {
				fields[i] = {name: fields[i]};
			}
			
			tmpMeta.fields[i] = Ext.apply({}, fields[i]);
			fieldsMap[fields[i].name] = i;
		}
		
		var inlineChartMap = {};
		if (this.inlineCharts) { 
			for(var j = 0, len = this.inlineCharts.length; j < len; j++) {
				inlineChartMap[ this.inlineCharts[j].column ] = this.inlineCharts[j];
			}
		}
		
		for(i = 0; i < tmpMeta.fields.length; i++) {			
			if(tmpMeta.fields[i].type) {
				var t = tmpMeta.fields[i].type;	
				tmpMeta.fields[i].renderer  =  Sbi.locale.formatters[t];			   
			}
			   
			if(tmpMeta.fields[i].subtype && tmpMeta.fields[i].subtype === 'html') {
				tmpMeta.fields[i].renderer  =  Sbi.locale.formatters['html'];
			}
			
			tmpMeta.fields[i].sortable = true;
	    
			var chartConf = null;
			if( (chartConf = inlineChartMap[tmpMeta.fields[i].header]) !== undefined ) {
				var renderer = this.createInlineChartRenderer(chartConf);
				if( renderer !== null ) {
					tmpMeta.fields[i].renderer  =  renderer;
				} else{
					Sbi.Msg.showWarning('Impossible to create inlineChart on column [' + tmpMeta.fields[i].header + ']');
				}
			}
		} 

	    //adds inline action buttons
		if (this.inlineActions) {
			for(var i = 0, l = this.inlineActions.length; i < l; i++){ 
				var column = this.createInlineActionColumn(this.inlineActions[i]);
				if(column !== null) {
					tmpMeta.fields.push( column );
				} else {
					Sbi.Msg.showWarning('Impossible to create inlineActionColumn [' + this.inlineActions[i].name + ']');
				}
				//hidden the configuration column linked to inlineActions				
				if (this.inlineActions[i].column) {
					var tmpName = this.store.getFieldNameByAlias(this.inlineActions[i].column);						
					if (tmpName !== undefined)  tmpMeta.fields[fieldsMap[tmpName]].hidden = true;
				}
  	  		}	
		}

		//adds numeration column    
		tmpMeta.fields[0] = new Ext.grid.RowNumberer();
	    //update columnmodel configuration
		this.getColumnModel().setConfig(tmpMeta.fields);
	}

	
	, createInlineChartRenderer: function(config) {
		var chartRenderer = null;
		if(config.type === 'bar') {
			renderer  =  Sbi.console.commons.Format.inlineBarRenderer(config);
		} else if(config.type === 'point') {
			renderer  =  Sbi.console.commons.Format.inlinePointRenderer(config);
		} else{
			Sbi.Msg.showWarning('InlineChart type [' + chartConf.type + '] is not supported');
		}
		return renderer;
	}
		
	, createInlineActionColumn: function(config) {
		var inlineActionColumn = null;
		var inlineActionColumnConfig = config;
		
		inlineActionColumnConfig = Ext.apply({
			grid: this
			, scope: this
		}, inlineActionColumnConfig);
		
		if (inlineActionColumnConfig.name === 'crossnav'){
			
			if (inlineActionColumnConfig.config.target === 'new') {
				inlineActionColumnConfig.imgSrc = this.images['cross_detail'];
			} else {
				inlineActionColumnConfig.imgSrc = this.images['popup_detail'];
			}
			inlineActionColumnConfig.handler = this.execCrossNav;
			
		}else if (inlineActionColumnConfig.name === 'monitor'){		
			
			if (inlineActionColumnConfig.column !== undefined){
				inlineActionColumnConfig.imgSrcActive = this.images['active'];			
				inlineActionColumnConfig.imgSrcInactive = this.images['inactive'];			
				//inlineActionColumnConfig.handler = this.execMonitor;		
				inlineActionColumnConfig.handler = this.execAction;
				//set the filter for view only active items (default)
				var tmpName = this.store.getFieldNameByAlias(inlineActionColumnConfig.column);
				if (tmpName !== undefined){
					if (this.store.filterPlugin.getFilter(tmpName) === undefined)
						this.store.filterPlugin.addFilter (tmpName, 1);
				}
			}else inlineActionColumnConfig.hidden = true;
			
			inlineActionColumn = new Sbi.console.InlineToggleActionColumn(inlineActionColumnConfig);			
			return inlineActionColumn;		
		} else if (inlineActionColumnConfig.name === 'errors'){	
			
			inlineActionColumnConfig.imgSrc = this.images[inlineActionColumnConfig.name];
			inlineActionColumnConfig.handler = this.showErrors;
			
		} else {
			
			inlineActionColumnConfig.imgSrc = this.images[inlineActionColumnConfig.name];
			inlineActionColumnConfig.handler = this.execAction;
		}
		
		inlineActionColumn = new Sbi.console.InlineActionColumn(inlineActionColumnConfig);
		
		return inlineActionColumn;
	}

});