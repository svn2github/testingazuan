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
	}, config || {});
	
	
	// always declare exploited services first!
	var params = {LIGHT_NAVIGATOR_DISABLED: 'TRUE', SBI_EXECUTION_ID: null};
	this.services = new Array();
	this.services['saveViewpointService'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'SAVE_VIEWPOINT_ACTION'
		, baseParams: params
	});
	
	
	this.init(c);
	
	c = Ext.apply({}, c, {
		layout: 'border',
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
	});   
	
	this.shortcutsPanel.on('applyviewpoint', this.onApplyViewpoint, this);
	this.shortcutsPanel.on('viewpointexecutionrequest', this.onExecuteViewpoint, this);
	
	// constructor
    Sbi.execution.ParametersSelectionPage.superclass.constructor.call(this, c);
    
    
    
    //this.addEvents();	
};

Ext.extend(Sbi.execution.ParametersSelectionPage, Ext.Panel, {
    
	services: null
    , parametersPanel: null
    , shortcutsPanel: null
    , saveViewpointWin: null
    , executionInstance: null
   
    // ----------------------------------------------------------------------------------------
    // public methods
    // ----------------------------------------------------------------------------------------
    
    /**
     * load data from server in order to synchronize panel content with the given 
     * execution instance (i.e. doc + role)
     */
    , synchronize: function( executionInstance ) {
		this.parametersPanel.synchronize( executionInstance );
		this.shortcutsPanel.synchronize( executionInstance );
		this.executionInstance = executionInstance;
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
		this.initParametersPanel(config);
		this.initShortcutsPanel(config);
	}
	
	, initParametersPanel: function( config ) {
		this.parametersPanel = new Sbi.execution.ParametersPanel(config);
		return this.parametersPanel;
	}
	
	, initShortcutsPanel: function( config ) {
		this.shortcutsPanel = new Sbi.execution.ShortcutsPanel(config);
		return this.shortcutsPanel;
	}
	
	, onApplyViewpoint: function(v) {
		for(var p in v) {
			alert(p + ': ' + v[p] + ':: '+  (typeof v[p]) );
			var str = '' + v[p];
			alert(str + ': ' + str.split(';') );
			if(str.split(';').length > 1) {
				alert('that\'s it: ' + p);
				v[p] = str.split(';');
			}
		}
		this.parametersPanel.setFormState(v);
	}
	
	, onExecuteViewpoint: function(v) {
		this.parametersPanel.setFormState(v);
	}
	
});