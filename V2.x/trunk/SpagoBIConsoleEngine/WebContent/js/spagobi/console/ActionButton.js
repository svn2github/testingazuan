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
  * - Antonella Giachino (antonella.giachino@eng.it)
  */

Ext.ns("Sbi.console");

Sbi.console.ActionButton = function(config) {

		var defaultSettings = {
			iconCls: config.actionConf.name
			//,tooltip: (config.actionConf.tooltip === undefined)?config.actionConf.name : config.actionConf.tooltip 
			,hidden: config.actionConf.hidden
			,scope:this
		};
		
		if(Sbi.settings && Sbi.settings.console && Sbi.settings.console.actionButton) {
			defaultSettings = Ext.apply(defaultSettings, Sbi.settings.console.actionButton);
		}
	
		var c = Ext.apply(defaultSettings, config || {});
	
		Ext.apply(this, c);

		//this.addEvents('customEvents');
	    this.initServices();
	  //  this.initButton();
	    
        c = Ext.apply(c, this);
      
      
	    // constructor
	    Sbi.console.ActionButton.superclass.constructor.call(this, c);
	    this.on('click', this.execAction, this);
	    this.store.on('load', this.initButton, this);
       //this.addEvents();
}; 

Ext.extend(Sbi.console.ActionButton, Ext.Button, {
    
    services: null
    , ACTIVE_VALUE: 0
	, INACTIVE_VALUE: 1
	, USER_ID: 'userId'
		
	, FILTERBAR_ACTIONS: {		
		  monitor: {serviceName: 'UPDATE_ACTION', images: 'monitor'}
		, monitor_inactive: {serviceName: 'UPDATE_ACTION', images: 'monitor_inactive'}
		, errors: {serviceName: 'UPDATE_ACTION', images: {active: 'errors', inactive: 'errors_inactive'}} 
		, alarms: {serviceName: 'UPDATE_ACTION', images: {active: 'alarms', inactive: 'alarms_inactive'}}
		, views: {serviceName: 'UPDATE_ACTION', images: {active: '../img/ico_views.gif', inactive: '../img/ico_views_inactive.gif'}}
		, refresh: {serviceName: 'REFRESH_ACTION', images: 'refresh'}
	}
   
    // public methods
	, resolveParameters: function(parameters, context) {
		var results = {};  

		results = Ext.apply(results, parameters.staticParams);
		
		var dynamicParams = parameters.dynamicParams;
	    if(dynamicParams) {        	
	    	var msgErr = ""; 
	      	for (var i = 0, l = dynamicParams.length; i < l; i++) {      		     
	      		var param = dynamicParams[i]; 
	        	for(p in param) { 
	        		if(p === 'scope') continue;
	        			if (param.scope === 'env'){ 
			            	if (p !== this.USER_ID && context[p] === undefined ) {              	 	 	      
			            		msgErr += 'Parameter "' + p + '" undefined into request. <p>';
		                    } else {          	 	 		           	 	 		  
		                    	results[param[p]] = context[p];
		                    	//alert("" +  param[p] + " " +  context[p]);
		                    } 	 		 
	                }          	 	 		   
	          		    
	        	 }          			   
	      	} 
	      	
	      	var metaParams = parameters.metaParams;
		    if(metaParams) {  
		    	results['metaParams'] = Ext.util.JSON.encode(metaParams);
		    }
	      	
	        if  (msgErr != ""){
	        	Sbi.Msg.showError(msgErr, 'Service Error');
	        }		  
	    }
	    
	    return results;
	}

    , execAction: function(){
    	
    	var flgValue = null;
    	
    	if (this.actionConf.name === 'monitor' || this.actionConf.name === 'monitor_inactive'){     		
    		this.store.filterPlugin.removeFilter(this.store.getFieldNameByAlias(this.actionConf.checkColumn));
    		var newFilter = (this.actionConf.name === 'monitor') ? this.INACTIVE_VALUE : this.ACTIVE_VALUE;    	
    		this.store.filterPlugin.addFilter(this.store.getFieldNameByAlias(this.actionConf.checkColumn), newFilter);    		
    		this.store.filterPlugin.applyFilters();	   
    		return;
    	}else if (this.actionConf.name === 'refresh'){    		
    		this.store.loadStore();
    		
    		this.store.filterPlugin.resetFilters;
    		//this.store.filterPlugin.applyFilters();
    		
    		return;
    	} else if (this.actionConf.name === 'errors' || this.actionConf.name === 'errors_inactive'){  
    		flgValue = (this.iconCls === 'errors')? this.INACTIVE_VALUE: this.ACTIVE_VALUE;
    		this.executionContext.errors_flag = flgValue;
    	} else if (this.actionConf.name === 'alarms' || this.actionConf.name === 'alarms_inactive'){      		
    		flgValue = (this.iconCls === 'alarms')? this.INACTIVE_VALUE: this.ACTIVE_VALUE;
    		this.executionContext.alarms_flag = flgValue;
    	} else if (this.actionConf.name === 'views' || this.actionConf.name === 'views_inactive'){      		
    		flgValue = (this.iconCls === 'views')? this.INACTIVE_VALUE: this.ACTIVE_VALUE;
    		this.executionContext.views_check = flgValue;
    	}
    	
    	
		var params = this.resolveParameters(this.actionConf.config, this.executionContext);
		params = Ext.apply(params, {
				message: this.actionConf.name, 
				userId: Sbi.user.userId 
			}); 
				
			Ext.Ajax.request({
			url: this.services[this.actionConf.name]	       
	       	, params: params 			       
	    	, success: function(response, options) {
	    		if(response !== undefined && response.responseText !== undefined) {
						var content = Ext.util.JSON.decode( response.responseText );
						if (content !== undefined) {				      			  
						//	alert(content.toSource());
						}				      		
				} else {
					Sbi.Msg.showError('Server response is empty', 'Service Error');
				}
	    	}
	    	, failure: Sbi.exception.ExceptionHandler.onServiceRequestFailure
	    	, scope: this     
	    });  
	}
 
    
    
    // private methods
    , initServices: function() {
    	this.services = this.services || new Array();	
		this.images = this.images || new Array();	
				
		for(var actionName in this.FILTERBAR_ACTIONS) {
			var actionConfig = this.FILTERBAR_ACTIONS[actionName];
			this.services[actionName] = this.services[actionName] || Sbi.config.serviceRegistry.getServiceUrl({
				serviceName: actionConfig.serviceName
				, baseParams: new Object()
			});
		}
    }
    
    , initButton: function(){
    	//this.store.loadStore();
    	
    	var dataIdx = this.store.getFieldNameByAlias(this.actionConf.flagColumn);
    	
    	if (dataIdx !== undefined ){
    		//alert(dataIdx);
    		var resFindActive = this.store.findExact(dataIdx,this.ACTIVE_VALUE);
    		
    		//alert("resFindActive: " + resFindActive);
    		if (resFindActive != -1){  
    			//alert("it should be active icon...");
	    		this.iconCls = this.FILTERBAR_ACTIONS[ this.actionConf.name ].images["active"]; 
	    		this.tooltip = this.actionConf.tooltipActive;
    		}else {    		    		
    			//alert("it should be inactive icon...");
    			this.iconCls = this.FILTERBAR_ACTIONS[ this.actionConf.name ].images[ "inactive"]; 
    			this.tooltip = this.actionConf.tooltipInactive;
    	    }
    		//alert(this.tooltip);
    	}
    	this.doLayout();
    	
    }
  
});
    
