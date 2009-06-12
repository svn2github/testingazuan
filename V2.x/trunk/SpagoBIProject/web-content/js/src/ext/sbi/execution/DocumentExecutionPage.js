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

Sbi.execution.DocumentExecutionPage = function(config) {
	
	// always declare exploited services first!
	var params = {LIGHT_NAVIGATOR_DISABLED: 'TRUE', SBI_EXECUTION_ID: null};
	this.services = new Array();
	this.services['getUrlForExecutionService'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'GET_URL_FOR_EXECUTION_ACTION'
		, baseParams: params
	});
	
	this.miframe = new Ext.ux.ManagedIframePanel({
		region:'center'
        , frameConfig : {
			autoCreate : { },
			disableMessaging : false
        }
        , loadMask  : true
        , disableMessaging :false
        , listeners  : {
        		
        	'message:subobjectsaved': {
        		fn: function(srcFrame, message) {
	        		this.shortcutsPanel.synchronizeSubobjects(this.executionInstance);
		        }
        		, scope: this
        	}
        
        	, 'message:crossnavigation' : {
        		fn: function(srcFrame, message){
                	var config = {
                		document: {'label': message.data.label}
            			, preferences: {
                			parameters: message.data.parameters
                		}
            	    };
            	    this.fireEvent('crossnavigation', config);
      
   
        		}
        		, scope: this
            }
        }
    });
	this.miframe.on('documentloaded', function() {
		this.miframe.iframe.execScript("parent = document;");
		var scriptFn = 	"parent.execCrossNavigation = function(d,l,p) {" +
						"	sendMessage({'label': l, parameters: p},'crossnavigation');" +
						"};";
		//this.miframe.iframe.execScript("parent.execCrossNavigation = function(d,l,p) {alert('LABEL: ' + l + '; PARAMETERS: '+ p);}");
		this.miframe.iframe.execScript(scriptFn);
		
		

	}, this);
	
    this.init(config);
    
    this.southPanel = new Ext.Panel({
		region:'south'
		, border: false
		, frame: false
		, collapsible: true
		, collapsed: true
		, hideCollapseTool: true
		, titleCollapse: true
		, collapseMode: 'mini'
		, split: true
		, autoScroll: true
		, height: 280
		, layout: 'fit'
		, items: [this.shortcutsPanel]
    });
    
    this.northPanel = new Ext.Panel({
		region:'north'
		, border: false
		, frame: false
		, collapsible: true
		, collapsed: true
		, hideCollapseTool: true
		, titleCollapse: true
		, collapseMode: 'mini'
		, split: true
		, autoScroll: true
		, height: 130
		, layout: 'fit'
		, items: [this.parametersPanel]
    });
    
    
    this.shortcutsPanel.on('applyviewpoint', this.parametersPanel.applyViewPoint, this.parametersPanel);
    
	var c = Ext.apply({}, config, {
		layout: 'border'
		, items: [this.miframe, this.southPanel, this.northPanel]
	});
	
	// constructor
    Sbi.execution.DocumentExecutionPage.superclass.constructor.call(this, c);
	
    this.addEvents('loadurlfailure', 'crossnavigation');
    
};

Ext.extend(Sbi.execution.DocumentExecutionPage, Ext.Panel, {
    
    // static contents and methods definitions
	services: null
	, miframe : null
	, parametersPanel: null
    , shortcutsPanel: null
    , southPanel: null
    , executionInstance: null
   
	// ----------------------------------------------------------------------------------------
	// public methods
	// ----------------------------------------------------------------------------------------
	, loadUrlForExecution: function( executionInstance, reloadSliders ) {
		this.executionInstance = executionInstance;
		
		if(reloadSliders === undefined || reloadSliders === true) {
			if(executionInstance.PARAMETERS !== undefined) {
				var parameters = Ext.util.JSON.decode( executionInstance.PARAMETERS );
				parameters = Ext.urlEncode(parameters);		
				this.parametersPanel.parametersPreference = parameters;
			}
			this.parametersPanel.synchronize(executionInstance);
			this.shortcutsPanel.synchronize(executionInstance);
		}
		
		//alert('zuk');
		
	
		Ext.Ajax.request({
	        url: this.services['getUrlForExecutionService'],
	        params: executionInstance,
	        callback : function(options , success, response){
	  	  		if(success) {   
		      		if(response !== undefined && response.responseText !== undefined) {
		      			var content = Ext.util.JSON.decode( response.responseText );
		      			if(content !== undefined) {
		      				if(content.errors !== undefined && content.errors.length > 0) {
		      					this.fireEvent('loadurlfailure', content.errors);
		      				} else {
		      					this.miframe.getFrame().setSrc( content.url );
		      					this.add(this.miframe);
		      				}
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
	
		// synchronize shortcutsPanel (subobjects, snapshots, viewpoints)
		this.shortcutsPanel.synchronize( executionInstance );
		
	}

	, refreshExecution: function() {
		var formState = this.parametersPanel.getFormState();
		var formStateStr = Sbi.commons.JSON.encode( formState );
		
		if(formStateStr !== this.executionInstance.PARAMETERS) { // todo: if(parametersPanel.isDirty())		
			this.executionInstance.PARAMETERS = formStateStr;
			this.loadUrlForExecution( this.executionInstance, false );
		} else {
			this.miframe.getFrame().setSrc( null ); // refresh the iframe with the latest url
		}
		
	}
	
	, print: function() {
		this.miframe.getFrame().print();
	}
	
	// ----------------------------------------------------------------------------------------
	// private methods
	// ----------------------------------------------------------------------------------------
	
	, init: function( config ) {
		this.initShortcutsPanel(config);
		this.initParametersPanel(config);
	}
	
	, initParametersPanel: function( config ) {
		this.parametersPanel = new Sbi.execution.ParametersPanel(config);
		return this.parametersPanel;
	}
	
	, initShortcutsPanel: function( config ) {
		this.shortcutsPanel = new Sbi.execution.ShortcutsPanel(config);
		return this.shortcutsPanel;
	}
});
