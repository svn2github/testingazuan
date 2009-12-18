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
  */

Ext.ns("Sbi.execution");

Sbi.execution.ExecutionPanel = function(config) {
	
	// declare exploited services
	var params = {LIGHT_NAVIGATOR_DISABLED: 'TRUE', SBI_EXECUTION_ID: null};
	this.services = new Array();
	this.services['getDocumentInfoService'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'GET_DOCUMENT_INFO_ACTION'
		, baseParams: params
	});
	
	this.documentsStack = [];
	
	var title = config.title;
	var closable = config.closable !== undefined || true;
	if(config.title !== undefined) delete config.title;
	if(config.closable !== undefined) delete config.closable;
	
	this.activeDocument = new Sbi.execution.ExecutionWizard( config );
	this.documentsStack.push( this.activeDocument );
	
	this.activeDocument.on('beforetoolbarinit', this.setBreadcrumbs, this);
	//this.activeDocument.tb.on('beforeinit', this.setBreadcrumbs, this);	
	this.activeDocument.documentExecutionPage.on('crossnavigation', this.loadCrossNavigationTargetDocument , this);
	/*
	this.activeDocument.parametersSelectionPage.parametersPanel.on('beforesynchronize', function(){
		this.doLayout();
		alert('AAA');
		this.activeDocument.parametersSelectionPage.southPanel.expand();
	}, this);
	*/
	
	var c = Ext.apply({}, config || {}, {
		title: title
		, closable: closable
		, border: false
		, activeItem: 0
		, hideMode: !Ext.isIE ? 'nosize' : 'display'
		, layout: 'card'
		, items: [this.activeDocument]
	});
	
	// constructor
    Sbi.execution.ExecutionPanel.superclass.constructor.call(this, c);
    
    
    
    
    //this.addEvents();	
};

Ext.extend(Sbi.execution.ExecutionPanel, Ext.Panel, {

	documentsStack: null
	, activeDocument: null
	
	, execute : function( doc ) {
		this.activeDocument.execute( doc );
	}

	, loadCrossNavigationTargetDocument: function( config ) {
		
		Ext.Ajax.request({
	        url: this.services['getDocumentInfoService'],
	        params: {'label' : config.document.label},
	        callback : function(options , success, response){
	  	  		if (success) {
		      		if(response !== undefined && response.responseText !== undefined) {
		      			var content = Ext.util.JSON.decode( response.responseText );
		      			if (content !== undefined) {
		      				if (content.found == false) {
		      					Sbi.exception.ExceptionHandler.showWarningMessage('Required document not found', 'Configuration Error');
		      				} else {
		      					if (content.canSee == false) {
		      						Sbi.exception.ExceptionHandler.showWarningMessage('User cannot see required document', 'Configuration Error');
		      					} else {
		      						config.document = content.document;
		      						this.executeCrossNavigation(config);
		      					}
		      				}
		      			} else {
		      				Sbi.exception.ExceptionHandler.showErrorMessage('Server response cannot be decoded', 'Service Error');
		      			}
		      		} else {
		      			Sbi.exception.ExceptionHandler.showErrorMessage('Server response is empty', 'Service Error');
		      		}
	  	  		} else {
	  	  			Sbi.exception.ExceptionHandler.showErrorMessage('Error while loading document', 'Service Error');
	  	  		}
	        },
	        scope: this,
			failure: Sbi.exception.ExceptionHandler.handleFailure      
	   });
		
	}
	
	, executeCrossNavigation: function( config ) {
		var prevActiveDoc = this.activeDocument;
		
		this.activeDocument = new Sbi.execution.ExecutionWizard( {preferences: config.preferences} );
		this.documentsStack.push( this.activeDocument );
		
		this.activeDocument.on('beforetoolbarinit', this.setBreadcrumbs, this);
		//this.activeDocument.tb.on('beforeinit', this.setBreadcrumbs, this);	
		this.activeDocument.documentExecutionPage.on('crossnavigation', this.loadCrossNavigationTargetDocument , this);
		
		//this.swapPanel(prevActiveDoc, this.activeDocument);
		this.add(this.activeDocument);
		this.doLayout();
		this.getLayout().setActiveItem(this.documentsStack.length -1);
		
		this.activeDocument.execute(config.document);
	}
	
	, setBreadcrumbs: function(tb) {
		
		tb.addSpacer();
		tb.addDom('<image width="12" height="12" src="../themes/sbi_default/img/analiticalmodel/execution/link16x16.gif"></image>');
		tb.addSpacer();
		
		for(var i = 0; i < this.documentsStack.length-1; i++) {
			this.documentsStack[i].document = this.documentsStack[i].document || {};
			
			tb.add({
				text: this.documentsStack[i].document.name || 'doc-' + (i+1)
				, stackIndex: i
			    , listeners: {
        			'click': {
                  		fn: this.onBreadCrumbClick,
                  		scope: this
                	} 
        		}
			});
			tb.addSpacer();
			tb.addDom('<image width="3" height="6" src="../themes/sbi_default/img/analiticalmodel/execution/c-sep.gif"></image>');
			tb.addSpacer();
		}
		
		tb.add({
			text: this.documentsStack[this.documentsStack.length-1].document.name || 'doc-' + (this.documentsStack.length)
			, stackIndex: this.documentsStack.length-1
			, disabled: true
			, cls: 'sbi-last-folder'
		    , listeners: {
    			'click': {
              		fn: this.onBreadCrumbClick,
              		scope: this
            	} 
    		}
		});
	}
	
	, onBreadCrumbClick: function(b, e) {
		var prevActiveDoc =  this.activeDocument;		
		this.activeDocument = this.documentsStack[b.stackIndex];
				
		//this.swapPanel(prevActiveDoc, this.activeDocument);
		this.getLayout().setActiveItem(b.stackIndex);
		
		for(var i = this.documentsStack.length-1; i > b.stackIndex; i--) {
			var el = this.documentsStack.pop();
			this.remove(el);
			el.destroy();
			//alert('destroy: ' + el.document.label);
			
		}		
	}
	
	
	
	
	
	
});
