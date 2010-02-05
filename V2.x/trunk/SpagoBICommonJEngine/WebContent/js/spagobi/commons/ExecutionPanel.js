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
  * ServiceRegistry - short description
  * 
  * Object documentation ...
  * 
  * by Chiara Chiarelli 
  */

Ext.ns("Sbi.commons");

Sbi.commons.ExecutionPanel = function(config) {

	var params = {
        LIGHT_NAVIGATOR_DISABLED : 'TRUE'
    };
    
    this.services = new Array();
    this.services['getStartService'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'START_WORK'
		, baseParams: params
	});
	
	this.services['getStopService'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'STOP_WORK'
		, baseParams: params
	});

	this.services['getStatusService'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'STATUS_WORK'
		, baseParams: params
	});

	
	this.document_id = config.document_id;
				
	this.buttons = [];
    this.buttons.push({
        text : 'Start'
        , scope : this
        , handler : this.startProcess
    });
    this.buttons.push({
        text : 'Stop'
        , scope : this
        , handler : this.stopProcess
        ,disabled: true
    });
 
   	 var c = Ext.apply( {}, config, {
        title : 'Process Execution',
        layout : 'fit',
        collapsible : false,
        collapsed : false,
        buttons: this.buttons,
        buttonAlign: 'left',
        autoWidth : true,
        autoHeight : true,
        region: 'center'
    });
	
	// constructor
    Sbi.commons.ExecutionPanel.superclass.constructor.call(this,c);
    
    
};

Ext.extend(Sbi.commons.ExecutionPanel, Ext.Panel, {
    
    // static contens and methods definitions
    services : null
    ,document_id : null
    ,buttons: null
   
    // public methods
    ,monitorStatus: function(){
    
    }
    
    ,startProcess : function() {
    	alert('start');
       Ext.Ajax.request({
	        url: this.services['getStartService'],
	        params: {'DOCUMENT_ID' : this.document_id },
	        success : function(response, options) {
	      		if(response !== undefined && response.responseText !== undefined) {
	      			var content = Ext.util.JSON.decode( response.responseText );
	      			if (content !== undefined) {
					//this.body="<h1>ciaoooo</h1>";
	      			this.setTitle(content.status);
	      			this.buttons[0].disabled=true;		
	      			this.buttons[1].disabled=false;		
	      			} else {
	      				Sbi.commons.ExceptionHandler.showErrorMessage('Server response cannot be decoded', 'Service Error');
	      			}
	      		} else {
	      			Sbi.commons.ExceptionHandler.showErrorMessage('Server response is empty', 'Service Error');
	      		}
	        },
	        scope: this,
			failure: Sbi.commons.ExceptionHandler.handleFailure      
	   });
	   
    }
    
    , stopProcess : function() {
     	Ext.Ajax.request({
	        url: this.services['getStopService'],
	        params: {'DOCUMENT_ID' : this.document_id },
	        success : function(response, options) {
	      		if(response !== undefined && response.responseText !== undefined) {
	      			var content = Ext.util.JSON.decode( response.responseText );
	      			if (content !== undefined) {
	  	      			this.buttons[0].disabled=false;		
	      				this.buttons[1].disabled=true;		
						//this.body="<h1>ciaoooo</h1>";
		      			alert(content.status);
		      			this.setTitle(content.status);
	      				
	      			} else {
	      				Sbi.commons.ExceptionHandler.showErrorMessage('Server response cannot be decoded', 'Service Error');
	      			}
	      		} else {
	      			Sbi.commons.ExceptionHandler.showErrorMessage('Server response is empty', 'Service Error');
	      		}
	        },
	        scope: this,
			failure: Sbi.commons.ExceptionHandler.handleFailure      
	   });
    }

    ,statusProcess : function() {
    
       Ext.Ajax.request({
	        url: this.services['getStatusService'],
	        params: {'DOCUMENT_ID' : this.document_id },
	        success : function(response, options) {
	      		if(response !== undefined && response.responseText !== undefined) {
	      			var content = Ext.util.JSON.decode( response.responseText );
	      			if (content !== undefined) {
					//this.body="<h1>ciaoooo</h1>";
		      		//alert(content.status);
	      			this.setTitle(content.status);
	      			} else {
	      				Sbi.commons.ExceptionHandler.showErrorMessage('Server response cannot be decoded', 'Service Error');
	      			}
	      		} else {
	      			Sbi.commons.ExceptionHandler.showErrorMessage('Server response is empty', 'Service Error');
	      		}
	        },
	        scope: this,
			failure: Sbi.commons.ExceptionHandler.handleFailure      
	   });
	   
    }
    

});