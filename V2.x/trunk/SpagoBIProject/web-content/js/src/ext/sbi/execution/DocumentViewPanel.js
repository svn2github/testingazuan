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
	
	this.miframe = new Ext.ux.ManagedIframePanel({
				region:'center'
                , frameConfig : {
                      autoCreate : { },
                      disableMessaging : false
                }
                , loadMask  : true
    });
	
	this.refreshButton =  new Ext.Toolbar.Button({
		iconCls: 'icon-refresh' 
     	, scope: this
    	, handler : this.refreshExecution
	});
	
	this.sendMailButton =  new Ext.Toolbar.Button({
		iconCls: 'icon-sendMail' 
     	, scope: this
    	, handler : this.sendMail
	});
	
	this.saveIntoPersonalFolderButton =  new Ext.Toolbar.Button({
		iconCls: 'icon-saveIntoPersonalFolder' 
     	, scope: this
    	, handler : this.saveIntoPersonalFolder
	});
	
	this.saveRememberMeButton =  new Ext.Toolbar.Button({
		iconCls: 'icon-saveRememberMe' 
     	, scope: this
    	, handler : this.saveRememberMe
	});
	
	this.notesButton =  new Ext.Toolbar.Button({
		iconCls: 'icon-notes' 
     	, scope: this
    	, handler : this.openNotesEditor
	});
	
	this.metadataButton =  new Ext.Toolbar.Button({
		iconCls: 'icon-metadata' 
     	, scope: this
    	, handler : this.showMetadata
	});
	
	this.ratingButton =  new Ext.Toolbar.Button({
		iconCls: 'icon-rating' 
     	, scope: this
    	, handler : this.showRating
	});
	
	this.printButton =  new Ext.Toolbar.Button({
		iconCls: 'icon-print' 
     	, scope: this
    	, handler : this.print
	});
	
    this.tb = new Ext.Toolbar({
    	cls: 'execution-toolbar'
        , items: ['->', this.refreshButton, this.sendMailButton, this.saveIntoPersonalFolderButton, this.saveRememberMeButton, 
                  this.notesButton, this.metadataButton, this.ratingButton, this.printButton]
    });
	
    this.toolbarPanel = new Ext.Panel({
    	region:'north'
        , border: false
        , frame: false
        , collapsible: true
        , collapsed: true
        , hideCollapseTool: true
        , titleCollapse: true
        , collapseMode: 'mini'
    	, tbar: this.tb
        , split: true
        , autoScroll: false
        , layout: 'fit'
    });	
    
	var c = Ext.apply({}, config, {
		layout: 'border'
		, items: [this.toolbarPanel, this.miframe]
	});
	
	// constructor
    Sbi.execution.DocumentViewPanel.superclass.constructor.call(this, c);
	
    this.addEvents('loadurlfailure');
    this.addEvents('sendMailButtonClicked');
    this.addEvents('saveIntoPersonalFolderButtonClicked');
    this.addEvents('saveRememberMeButtonClicked');
    this.addEvents('notesButtonClicked');
    this.addEvents('metadataButtonClicked');
    this.addEvents('ratingButtonClicked');
    
};

Ext.extend(Sbi.execution.DocumentViewPanel, Ext.Panel, {
    
    // static contents and methods definitions
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
		      				this.miframe.getFrame().setSrc( content.url );
		      				this.add(this.miframe);
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

	, refreshExecution: function() {
		this.miframe.getFrame().setSrc( null ); // refresh the iframe with the latest url
	}
	
	, sendMail: function() {
		this.fireEvent('sendMailButtonClicked');
	}
	
	, saveIntoPersonalFolder: function() {
		this.fireEvent('saveIntoPersonalFolderButtonClicked');
	}
	
	, saveRememberMe: function() {
		this.fireEvent('saveRememberMeButtonClicked');
	}
	
	, openNotesEditor: function() {
		this.fireEvent('notesButtonClicked');
	}
	
	, showMetadata: function() {
		this.fireEvent('metadataButtonClicked');
	}
	
	, showRating: function() {
		this.fireEvent('ratingButtonClicked');
	}
	
	, print: function() {
		this.miframe.getFrame().print();
	}
});