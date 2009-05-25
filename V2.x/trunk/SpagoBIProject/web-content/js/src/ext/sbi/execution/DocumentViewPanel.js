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
  * - name (mail)
  */

Ext.ns("Sbi.execution");

Sbi.execution.DocumentViewPanel = function(config) {
	
	// always declare exploited services first!
	var params = {LIGHT_NAVIGATOR_DISABLED: 'TRUE', SBI_EXECUTION_ID: null};
	this.services = new Array();
	this.services['getUrlForExecutionService'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'GET_URL_FOR_EXECUTION_ACTION'
		, baseParams: params
	});
	
	// iframe creation
	this.miframe = new Ext.ux.ManagedIframePanel({
                frameConfig : {
                      autoCreate : {
                          id : 'frameA',
                          name : 'frameA'
                      },
                      disableMessaging : false
                  }
                  , loadMask  : true
                  //, defaultSrc : 'http://spagobi.eng.it'
    });
	
	
	var c = Ext.apply({}, config, {
		layout: 'fit'
		, items: [this.miframe]
	});
	
	// constructor
    Sbi.execution.DocumentViewPanel.superclass.constructor.call(this, c);
	
    this.addEvents('loadurlfailure');	
};

Ext.extend(Sbi.execution.DocumentViewPanel, Ext.Panel, {
    
    // static contens and methods definitions
	miframe : null
   
    // public methods
	, loadUrlForExecution: function( executionInstance ) {
		Ext.Ajax.request({
	        url: this.services['getUrlForExecutionService'],
	        params: executionInstance,
	        callback : function(options , success, response){
	  	  		if(success) {   
		      		if(response !== undefined && response.responseText !== undefined) {
		      			var content = Ext.util.JSON.decode( response.responseText );
		      			if(content !== undefined) {
		      				alert( content.url );	
		      				this.miframe.getFrame().setSrc( content.url );
		      			} 
		      		} else {
		      			Sbi.exception.ExceptionHandler.showErrorMessage('Server response is empty', 'Service Error');
		      		}
	  	  		} else { 
		  	  		if(response !== undefined && response.responseText !== undefined) {
		      			var content = Ext.util.JSON.decode( response.responseText );
		      			if(content !== undefined && content.errors !== undefined) {
		      				this.fireEvent('loadurlfailure', content.errors);
		      			} 
		      		} else {
		      			Sbi.exception.ExceptionHandler.showErrorMessage('Server response is empty', 'Service Error');
		      		}
	  	  		}
	        },
	        scope: this,
			  failure: Sbi.exception.ExceptionHandler.handleFailure      
	   });
	
	}
});