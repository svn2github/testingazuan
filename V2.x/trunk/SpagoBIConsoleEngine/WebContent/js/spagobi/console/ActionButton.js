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
			,tooltip: (config.actionConf.tooltip === undefined)?config.actionConf.name : config.actionConf.tooltip 
			,hidden: config.actionConf.hidden
			,scope:this
		};
		
		if(Sbi.settings && Sbi.settings.console && Sbi.settings.console.actionButton) {
			defaultSettings = Ext.apply(defaultSettings, Sbi.settings.console.actionButton);
		}
	
		var c = Ext.apply(defaultSettings, config || {});
	
		Ext.apply(this, c);

		//Services definition
		this.services = this.services || new Array();	
		this.services['refresh'] = this.services['refresh'] || Sbi.config.serviceRegistry.getServiceUrl({
			serviceName: 'REFRESH_ACTION'
			, baseParams: new Object()
		});
		this.services['errors'] = this.services['errors'] || Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'ERRORS_ACTION'
			, baseParams: new Object()
		});
		this.services['errors_inactive'] = this.services['errors'] || Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'ERRORS_ACTION'
			, baseParams: new Object()
		});
		this.services['warnings'] = this.services['warnings'] || Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'WARNINGS_ACTION'
			, baseParams: new Object()
		});
		this.services['warnings_inactive'] = this.services['errors'] || Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'WARNINGS_ACTION'
			, baseParams: new Object()
		});
		this.services['views'] = this.services['views'] || Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'VIEWS_ACTION'
			, baseParams: new Object()
		});
		this.services['views_inactive'] = this.services['errors'] || Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'VIEWS_ACTION'
			, baseParams: new Object()
		});
		this.services['monitor'] = this.services['monitor'] || Sbi.config.serviceRegistry.getServiceUrl({
			serviceName: 'MONITOR_ACTION'
		  , baseParams: new Object()
		});
		this.services['monitor_inactive'] = this.services['monitor_inactive'] || Sbi.config.serviceRegistry.getServiceUrl({
			serviceName: 'MONITOR_ACTION'
		  , baseParams: new Object()
		});
		
		//this.addEvents('customEvents');
		
      c = Ext.apply(c, this);

		// constructor
		//delete c.config;
		Sbi.console.ActionButton.superclass.constructor.call(this, c);
		this.on('click', this.execAction, this);
		//this.addEvents();
};

Ext.extend(Sbi.console.ActionButton, Ext.Button, {
    
    services: null
    , ACTIVE_VALUE: 0
	, INACTIVE_VALUE: 1
   
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

    , execAction: function(){
    	
    	if (this.actionConf.name === 'monitor' || this.actionConf.name === 'monitor_inactive'){     		
    		this.store.filterPlugin.removeFilter(this.store.getFieldNameByAlias(this.actionConf.column));
    		var newFilter = (this.actionConf.name === 'monitor') ? this.ACTIVE_VALUE : this.INACTIVE_VALUE;    	
    		this.store.filterPlugin.addFilter(this.store.getFieldNameByAlias(this.actionConf.column), newFilter);    		
    		this.store.filterPlugin.applyFilters();	   
    		return;
    	}
		var params = this.resolveParameters(this.actionConf.config, this.executionContext);
		params = Ext.apply(params, {
				message: (this.actionConf.messagge !== undefined)?this.actionConf.message : this.actionConf.name, 
				userId: Sbi.user.userId 
			}); 
				 
			Ext.Ajax.request({
			url: this.services[this.actionConf.name]	       
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
 
    
    
    // private methods
  
});
    
