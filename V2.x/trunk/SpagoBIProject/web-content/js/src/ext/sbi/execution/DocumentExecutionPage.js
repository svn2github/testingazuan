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
	
	// apply defaults values
	config = Ext.apply({
		// no defaults
	}, config || {});
	
	// check mandatory values
	// ...
		
	// declare exploited services
	var params = {LIGHT_NAVIGATOR_DISABLED: 'TRUE', SBI_EXECUTION_ID: null};
	this.services = new Array();
	this.services['getUrlForExecutionService'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'GET_URL_FOR_EXECUTION_ACTION'
		, baseParams: params
	});
	this.services['showSendToForm'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'SHOW_SEND_TO_FORM'
		, baseParams: params
	});
	
	this.services['saveIntoPersonalFolder'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'SAVE_PERSONAL_FOLDER'
		, baseParams: params
	});
	
	// add events
    this.addEvents('beforetoolbarinit', 'beforesynchronize', 'moveprevrequest', 'loadurlfailure', 'crossnavigation', 'beforerefresh');
          
	
    this.init(config);    
    
    this.shortcutsPanel.on('applyviewpoint', this.parametersPanel.applyViewPoint, this.parametersPanel);
    this.shortcutsPanel.on('viewpointexecutionrequest', function(v) {
    	this.parametersPanel.applyViewPoint(v);
		this.refreshExecution();
    }, this);
    
    
    
	var c = Ext.apply({}, config, {
		layout: 'border'
		, tbar: this.toolbar
		, items: [this.miframe, this.southPanel, this.northPanel]
	});
	
	// constructor
    Sbi.execution.DocumentExecutionPage.superclass.constructor.call(this, c);
	
};

Ext.extend(Sbi.execution.DocumentExecutionPage, Ext.Panel, {
    
    // static contents and methods definitions
	services: null
	, executionInstance: null
	
	, toolbar: null
	, miframe : null
	, parametersPanel: null
    , shortcutsPanel: null
    , southPanel: null
    , northPanel: null
   
	// ----------------------------------------------------------------------------------------
	// public methods
	// ----------------------------------------------------------------------------------------
    
    
    , synchronize: function( executionInstance, synchronizeSliders ) {
		
		if(this.fireEvent('beforesynchronize', this, executionInstance, this.executionInstance) !== false){
			this.executionInstance = executionInstance;
			this.synchronizeToolbar( executionInstance );
			
			if(synchronizeSliders === undefined || synchronizeSliders === true) {
				if(executionInstance.PARAMETERS !== undefined) {
					var parameters = Ext.util.JSON.decode( executionInstance.PARAMETERS );
					parameters = Ext.urlEncode(parameters);		
					this.parametersPanel.parametersPreference = parameters;
				}
				this.parametersPanel.synchronize(executionInstance);
				this.shortcutsPanel.synchronize(executionInstance);
			}
			
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
		}
	}

	, synchronizeToolbar: function( executionInstance ){
		
		this.toolbar.items.each( function(item) {
			this.toolbar.items.remove(item);
            item.destroy();           
        }, this); 
		
		this.fireEvent('beforetoolbarinit', this, this.toolbar);
		
		this.toolbar.addFill();
		
		this.toolbar.addButton(new Ext.Toolbar.Button({
			iconCls: 'icon-back' 
			, tooltip: LN('sbi.execution.executionpage.toolbar.back')
			, scope: this
			, handler : function() {this.fireEvent('moveprevrequest');}
		}));
		
		if (!this.executionInstance.SBI_SNAPSHOT_ID) {
			this.toolbar.addButton(new Ext.Toolbar.Button({
				iconCls: 'icon-refresh' 
				, tooltip: LN('sbi.execution.executionpage.toolbar.refresh')
			    , scope: this
			    , handler : this.refreshExecution			
			}));
		}
		
		this.toolbar.addButton(new Ext.Toolbar.Button({
			iconCls: 'icon-rating' 
			, tooltip: LN('sbi.execution.executionpage.toolbar.rating')
		    , scope: this
		    , handler : this.rateExecution	
		}));
		
		this.toolbar.addButton(new Ext.Toolbar.Button({
			iconCls: 'icon-print' 
			, tooltip: LN('sbi.execution.executionpage.toolbar.print')
		    , scope: this
		    , handler : this.printExecution
		}));
		
		if (Sbi.user.functionalities.contains('SendMailFunctionality') && !executionInstance.SBI_SNAPSHOT_ID
				/*&& this.document.type == 'REPORT'*/) {
			this.toolbar.addButton(new Ext.Toolbar.Button({
				iconCls: 'icon-sendMail' 
				, tooltip: LN('sbi.execution.executionpage.toolbar.send')
		     	, scope: this
		    	, handler : this.sendExecution
			}));
		}
		
		if (Sbi.user.functionalities.contains('SaveIntoFolderFunctionality') && !executionInstance.SBI_SNAPSHOT_ID) {
			this.toolbar.addButton(new Ext.Toolbar.Button({
				iconCls: 'icon-saveIntoPersonalFolder' 
				, tooltip: LN('sbi.execution.executionpage.toolbar.save')
		     	, scope: this
		    	, handler : this.saveExecution
			}));
		}

		if (Sbi.user.functionalities.contains('SaveRememberMeFunctionality') && !executionInstance.SBI_SNAPSHOT_ID) {
			this.toolbar.addButton(new Ext.Toolbar.Button({
				iconCls: 'icon-saveRememberMe'
				, tooltip: LN('sbi.execution.executionpage.toolbar.bookmark')
		     	, scope: this
		    	, handler :this.bookmarkExecution
			}));
		}
		
		if (Sbi.user.functionalities.contains('SeeNotesFunctionality') && !executionInstance.SBI_SNAPSHOT_ID) {
			this.toolbar.addButton(new Ext.Toolbar.Button({
				iconCls: 'icon-notes' 
				, tooltip: LN('sbi.execution.executionpage.toolbar.annotate')
		     	, scope: this
		    	, handler : this.annotateExecution
			}));
		}
		
		if (Sbi.user.functionalities.contains('SeeMetadataFunctionality') && !this.executionInstance.SBI_SNAPSHOT_ID) {
			this.toolbar.addButton(new Ext.Toolbar.Button({
				iconCls: 'icon-metadata' 
				, tooltip: LN('sbi.execution.executionpage.toolbar.metadata')
		     	, scope: this
		    	, handler : this.metaExecution
			}));
		}
	}

	, refreshExecution: function() {
		
		var formState = this.parametersPanel.getFormState();
		var formStateStr = Sbi.commons.JSON.encode( formState );
		
		if(this.fireEvent('beforerefresh', this, this.executionInstance, formState) !== false){
			if(formStateStr !== this.executionInstance.PARAMETERS) { // todo: if(parametersPanel.isDirty())		
				this.executionInstance.PARAMETERS = formStateStr;
				this.synchronize( this.executionInstance, false );
			} else {
				this.miframe.getFrame().setSrc( null ); // refresh the iframe with the latest url
			}
		}
	}
	
	, rateExecution: function() {
		this.win_rating = new Sbi.execution.toolbar.RatingWindow({'OBJECT_ID': this.executionInstance.OBJECT_ID});
		this.win_rating.show();
	}
	
	, printExecution: function() {
		this.miframe.getFrame().print();
	}
	
	, sendExecution: function () {
		var sendToIframeUrl = this.services['showSendToForm'] 
		        + '&objlabel=' + this.executionInstance.OBJECT_LABEL
		        + '&objid=' + this.executionInstance.OBJECT_ID
				+ '&' + Sbi.commons.Format.toStringOldSyntax(this.parametersPanel.getFormState());
		this.win_sendTo = new Sbi.execution.toolbar.SendToWindow({'url': sendToIframeUrl});
		this.win_sendTo.show();
	}
	
	, saveExecution: function () {
		Ext.Ajax.request({
	          url: this.services['saveIntoPersonalFolder'],
	          params: {documentId: this.executionInstance.OBJECT_ID},
	          callback : function(options , success, response){
	    	  	if (success && response !== undefined) {   
		      		if(response.responseText !== undefined) {
		      			var responseText = response.responseText;
		      			var iconSaveToPF;
		      			var message;
		      			if (responseText=="sbi.execution.stpf.ok") {
		      				message = LN('sbi.execution.stpf.ok');
		      				iconSaveToPF = Ext.MessageBox.INFO;
		      			}
		      			if (responseText=="sbi.execution.stpf.alreadyPresent") {
		      				message = LN('sbi.execution.stpf.alreadyPresent');
		      				iconSaveToPF = Ext.MessageBox.WARNING;
		      			}
		      			if (responseText=="sbi.execution.stpf.error") {
		      				message = LN('sbi.execution.stpf.error');
		      				iconSaveToPF = Ext.MessageBox.ERROR;
		      			}

		      			var messageBox = Ext.MessageBox.show({
		      				title: 'Status',
		      				msg: message,
		      				modal: false,
		      				buttons: Ext.MessageBox.OK,
		      				width:300,
		      				icon: iconSaveToPF,
		      				animEl: 'root-menu'        			
		      			});
		      		} else {
		      			Sbi.exception.ExceptionHandler.showErrorMessage('Server response is empty', 'Service Error');
		      		}
	    	  	}
	          },
	          scope: this,
	  		  failure: Sbi.exception.ExceptionHandler.handleFailure      
	     });
	}
	
	, bookmarkExecution: function () {
		this.win_saveRememberMe = new Sbi.execution.toolbar.SaveRememberMeWindow({'SBI_EXECUTION_ID': this.executionInstance.SBI_EXECUTION_ID});
		this.win_saveRememberMe.show();
	}
	
	, annotateExecution: function () {
		this.win_notes = new Sbi.execution.toolbar.NotesWindow({'SBI_EXECUTION_ID': this.executionInstance.SBI_EXECUTION_ID});
		this.win_notes.show();
	}
	
	, metaExecution: function () {
		this.win_metadata = new Sbi.execution.toolbar.MetadataWindow({'OBJECT_ID': this.executionInstance.OBJECT_ID});
		this.win_metadata.show();
	}
	
	// ----------------------------------------------------------------------------------------
	// private methods
	// ----------------------------------------------------------------------------------------
	
	, init: function( config ) {
		this.initToolbar(config);
		this.initNorthPanel(config);
		this.initCenterPanel(config);
		this.initSouthPanel(config);
	}
	
	, initToolbar: function( config ) {
		this.toolbar = new Ext.Toolbar({
			items: ['']
		});
		
		this.toolbar.on('render', function() {
			
		}, this);
		
	}
	
	, initNorthPanel: function( config ) {
		this.parametersPanel = new Sbi.execution.ParametersPanel(config);
		
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
	}
	
	, initCenterPanel: function( config ) {
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
			//this.miframe.iframe.execScript("this.uiType = 'ext'; alert(uiType); alert(execCrossNavigation);");
			this.miframe.iframe.execScript("uiType = 'ext';");
			var scriptFn = 	"parent.execCrossNavigation = function(d,l,p) {" +
							"	sendMessage({'label': l, parameters: p},'crossnavigation');" +
							"};";
			//this.miframe.iframe.execScript("parent.execCrossNavigation = function(d,l,p) {alert('LABEL: ' + l + '; PARAMETERS: '+ p);}");
			this.miframe.iframe.execScript(scriptFn);
			
			

		}, this);
	}
	
	, initSouthPanel: function( config ) {
		this.shortcutsPanel = new Sbi.execution.ShortcutsPanel(config);
		
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
	}
});
