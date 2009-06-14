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

Sbi.execution.ParametersSelectionPage = function(config) {
	
	var c = Ext.apply({
		columnNo: 3
		, labelAlign: 'left'
		, maskOnRender: false
	}, config || {});
	
	this.maskOnRender = c.maskOnRender;
	
	
	// always declare exploited services first!
	var params = {LIGHT_NAVIGATOR_DISABLED: 'TRUE', SBI_EXECUTION_ID: null};
	this.services = new Array();
	this.services['saveViewpointService'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'SAVE_VIEWPOINT_ACTION'
		, baseParams: params
	});
	 
    this.addEvents('beforetoolbarinit', 'beforesynchronize', 'synchronize', 'synchronizeexception', 'movenextrequest', 'moveprevrequest');	
	
	this.init(c);
	
	c = Ext.apply({}, c, {
		layout: 'fit',
		tbar: this.toolbar,
		items: [{
			layout: 'border',
			listeners: {
			    'render': {
	            	fn: function() {
	          	 		this.loadingMask = new Sbi.decorator.LoadMask(this.body, {msg:'Loading parameters ...'}); 
	          	 		if(this.maskOnRender === true) this.loadingMask.show();
	            	},
	            	scope: this
	          	}
	        },      	
			items: [{
				region:'center'
			    , border: false
			    , frame: false
			    , collapsible: false
			    , collapsed: false
			    , hideCollapseTool: true
			    , titleCollapse: true
			    , collapseMode: 'mini'
			    , split: true
			    , autoScroll: true
			    , layout: 'fit'
			    , items: [this.parametersPanel]
			}, {
				region:'south'
				, border: false
				, frame: false
				, collapsible: true
				, collapsed: false
				, hideCollapseTool: true
				, titleCollapse: true
				, collapseMode: 'mini'
				, split: true
				, autoScroll: true
				, height: 280
				, layout: 'fit'
				, items: [this.shortcutsPanel]
			}]
		}]
	});   
	
	this.parametersPanel.on('beforesynchronize', function(){if(this.loadingMask) this.loadingMask.show();}, this);
	this.parametersPanel.on('synchronize', function(){if(this.loadingMask) this.loadingMask.hide();}, this);
	this.parametersPanel.on('readyforexecution', function(){this.fireEvent('movenextrequest', this);}, this);
	
	this.shortcutsPanel.on('applyviewpoint', this.parametersPanel.applyViewPoint, this.parametersPanel);
	this.shortcutsPanel.on('viewpointexecutionrequest', this.parametersPanel.applyViewPoint, this.parametersPanel);
	
	// constructor
    Sbi.execution.ParametersSelectionPage.superclass.constructor.call(this, c);
    
    
};

Ext.extend(Sbi.execution.ParametersSelectionPage, Ext.Panel, {
    
	services: null
	, executionInstance: null
	
	, toolbar: null
	
    , parametersPanel: null
    , shortcutsPanel: null
  
    , saveViewpointWin: null
    
    , loadingMask: null
    , maskOnRender: null
   
    // ----------------------------------------------------------------------------------------
    // public methods
    // ----------------------------------------------------------------------------------------
    
   
    , synchronize: function( executionInstance ) {
		if(this.fireEvent('beforesynchronize', this, executionInstance, this.executionInstance) !== false){
			this.executionInstance = executionInstance;
			this.synchronizeToolbar( executionInstance );
			
			this.parametersPanelSynchronizationPending = true;
			this.parametersPanel.synchronize( this.executionInstance );
			
			this.shortcutsPanelSynchronizationPending = true;
			this.shortcutsPanel.synchronize( this.executionInstance );
		}
	}

	, synchronizeToolbar: function( executionInstance ){
		this.fireEvent('beforetoolbarinit', this, this.toolbar);
		
		this.toolbar.addFill();
		
		this.toolbar.addButton(new Ext.Toolbar.Button({
			iconCls: 'icon-back' 
		    , scope: this
		    , handler : function() {this.fireEvent('moveprevrequest');}
		}));
		
		this.toolbar.addSeparator();
		
		this.toolbar.addButton(new Ext.Toolbar.Button({
			iconCls: 'icon-clear'
		   	, scope: this
		   	, handler : function() {
				this.clearParametersForm();
			}
		}));
		
		if (Sbi.user.functionalities.contains('SeeViewpointsFunctionality')) {
			this.toolbar.addButton(new Ext.Toolbar.Button({
				iconCls: 'icon-save'
			   	, scope: this
			   	, handler : function() {
					this.saveParametersFormStateAsViewpoint();
				}
			}));
		}
		
		this.toolbar.addSeparator();
		
		this.toolbar.addButton(new Ext.Toolbar.Button({
			iconCls: 'icon-execute'
			, scope: this
			, handler : function() {this.fireEvent('movenextrequest');}
		}));
	}

	, clearParametersForm: function() {
		this.parametersPanel.clear();
	}
	
	, saveParametersFormStateAsViewpoint: function() {
		if(this.saveViewpointWin === null) {
			this.saveViewpointWin = new Sbi.widgets.SaveWindow();
			this.saveViewpointWin.on('save', function(w, state) {
				var params = Ext.apply({}, state, this.executionInstance);
				var formState = this.parametersPanel.getFormState();
				for(var p in formState) {
					if(formState[p] instanceof Array ) {
						formState[p] = formState[p].join(';');
					}
				}
				params.viewpoint = Ext.util.JSON.encode( formState );
				Ext.Ajax.request({
			          url: this.services['saveViewpointService'],
			          
			          params: params,
			          
			          callback : function(options, success, response){
						if(success && response !== undefined) {   
				      		if(response.responseText !== undefined) {
				      			var content = Ext.util.JSON.decode( response.responseText );
				      			if(content !== undefined) {
				      				alert('Viewpoint saved succesfully');
				      				this.shortcutsPanel.viewpointsPanel.addViewpoints(content);
				      			} 
				      		} else {
				      			Sbi.exception.ExceptionHandler.showErrorMessage('Server response is empty', 'Service Error');
				      		}
			    	   }			    	  	
			          },
			          scope: this,
			  		  failure: Sbi.exception.ExceptionHandler.handleFailure      
			     });
			}, this);
		}
		this.saveViewpointWin.show();
	}
	
	
    
	
	// ----------------------------------------------------------------------------------------
	// private methods
	// ----------------------------------------------------------------------------------------
	
	, init: function( config ) {
		this.initToolbar(config);
		this.initParametersPanel(config);
		this.initShortcutsPanel(config);
	}
	
	, initToolbar: function( config ) {
		this.toolbar = new Ext.Toolbar({
			items: ['']
		});
		
		/*
		this.toolbar.on('render', function() {
			this.fireEvent('beforetoolbarinit', this, this.toolbar);
			
			this.toolbar.addFill();
			
			this.toolbar.addButton(new Ext.Toolbar.Button({
				iconCls: 'icon-back' 
			    , scope: this
			    , handler : function() {this.fireEvent('moveprevrequest');}
			}));
			
			this.toolbar.addSeparator();
			
			this.toolbar.addButton(new Ext.Toolbar.Button({
				iconCls: 'icon-clear'
			   	, scope: this
			   	, handler : function() {
					this.clearParametersForm();
				}
			}));
			
			if (Sbi.user.functionalities.contains('SeeViewpointsFunctionality')) {
				this.toolbar.addButton(new Ext.Toolbar.Button({
					iconCls: 'icon-save'
				   	, scope: this
				   	, handler : function() {
						this.saveParametersFormStateAsViewpoint();
					}
				}));
			}
			
			this.toolbar.addSeparator();
			
			this.toolbar.addButton(new Ext.Toolbar.Button({
				iconCls: 'icon-execute'
				, scope: this
				, handler : function() {this.fireEvent('movenextrequest');}
			}));
		}, this);
		*/
	}
	
	, initParametersPanel: function( config ) {
		this.parametersPanel = new Sbi.execution.ParametersPanel(config);
		this.parametersPanel.on('synchronize', function() {
			if(this.shortcutsPanelSynchronizationPending === false) {
				this.fireEvent('synchronize', this);
			}
			this.parametersPanelSynchronizationPending = false;
		}, this)
		return this.parametersPanel;
	}
	
	, initShortcutsPanel: function( config ) {
		this.shortcutsPanel = new Sbi.execution.ShortcutsPanel(config);
		this.shortcutsPanel.on('synchronize', function() {
			if(this.parametersPanelSynchronizationPending === false) {
				this.fireEvent('synchronize', this);
			}
			this.shortcutsPanelSynchronizationPending = false;
		}, this)
		return this.shortcutsPanel;
	}
	
	, onExecuteViewpoint: function(v) {
		this.parametersPanel.setFormState(v);
	}
	
});