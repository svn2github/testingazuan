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
	
	// variables for preferences and for shortcuts/parameters panel synchronization
	this.isParameterPanelReady = false;
	this.isSubobjectPanelReady = false;
	this.preferenceSubobjectId = null;
	this.isSnapshotPanelReady = false;
	this.preferenceSnapshotId = null;
	
	// always declare exploited services first!
	var params = {LIGHT_NAVIGATOR_DISABLED: 'TRUE', SBI_EXECUTION_ID: null};
	this.services = new Array();
	this.services['saveViewpointService'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'SAVE_VIEWPOINT_ACTION'
		, baseParams: params
	});
	 
    this.addEvents('beforetoolbarinit', 'beforesynchronize', 'synchronize', 'synchronizeexception', 'movenextrequest', 'moveprevrequest');	
	
    this.shortcutsHiddenPreference = config.shortcutsHidden !== undefined ? config.shortcutsHidden : false;
    
	this.init(c);
	
	this.centerPanel = new Ext.Panel({
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
		});
	
	var shortcutsHidden = (!Sbi.user.functionalities.contains('SeeViewpointsFunctionality') 
							&& !Sbi.user.functionalities.contains('SeeSnapshotsFunctionality') 
							&& !Sbi.user.functionalities.contains('SeeSubobjectsFunctionality'))
							||
							this.shortcutsHiddenPreference;
	
	this.southPanel = new Ext.Panel({
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
			, hidden: shortcutsHidden
	});
	
	c = Ext.apply({}, c, {
		layout: 'fit',
		tbar: this.toolbar,
		//autoScroll : true,
		items: [{
			layout: 'border',
			listeners: {
			    'render': {
	            	fn: function() {
	          	 		this.loadingMask = new Sbi.decorator.LoadMask(this.body, {msg:LN('sbi.execution.parametersselection.loadingmsg')}); 
	          	 		if(this.maskOnRender === true) this.loadingMask.show();
	            	},
	            	scope: this
	          	}
	        },   	        
			items: [this.centerPanel, this.southPanel]
		}]
	});   
	
	
	this.parametersPanel.on('beforesynchronize', function(){if(this.loadingMask) this.loadingMask.show();}, this);
	this.parametersPanel.on('synchronize', function(){if(this.loadingMask) this.loadingMask.hide();}, this);
	/*
	this.parametersPanel.on('readyforexecution', function(){
		this.executionInstance.isPossibleToComeBackToParametersPage = false;
		this.fireEvent('movenextrequest', this);
	}, this);
	*/
	this.parametersPanel.on('readyforexecution', function(){
		this.isParameterPanelReady = true;
		this.startExecutionAutomatically();
	}, this);
	this.shortcutsPanel.on('applyviewpoint', this.parametersPanel.applyViewPoint, this.parametersPanel);
	this.shortcutsPanel.on('viewpointexecutionrequest', this.onExecuteViewpoint, this);
	this.shortcutsPanel.on('subobjectexecutionrequest', this.onExecuteSubobject, this);
	this.shortcutsPanel.on('snapshotexcutionrequest', this.onExecuteSnapshot, this);
	
	this.shortcutsPanel.subobjectsPanel.on('ready', function(preferenceSubobjectId){
		this.isSubobjectPanelReady = true;
		this.preferenceSubobjectId = preferenceSubobjectId;
		this.startExecutionAutomatically();
	}, this);
	
	this.shortcutsPanel.snapshotsPanel.on('ready', function(preferenceSnapshotId){
		this.isSnapshotPanelReady = true;
		this.preferenceSnapshotId = preferenceSnapshotId;
		this.startExecutionAutomatically();
	}, this);
	
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
		
		this.toolbar.items.each( function(item) {
			this.toolbar.items.remove(item);
            item.destroy();           
        }, this); 
		
		this.fireEvent('beforetoolbarinit', this, this.toolbar);
		
		this.toolbar.addFill();
		
		if (executionInstance.isPossibleToComeBackToRolePage == undefined || executionInstance.isPossibleToComeBackToRolePage === true) {
			this.toolbar.addButton(new Ext.Toolbar.Button({
				iconCls: 'icon-back' 
				, tooltip: LN('sbi.execution.parametersselection.toolbar.back')
			    , scope: this
			    , handler : function() {this.fireEvent('moveprevrequest');}
			}));
		}
		
		this.toolbar.addSeparator();
		
		this.toolbar.addButton(new Ext.Toolbar.Button({
			iconCls: 'icon-clear'
			, tooltip: LN('sbi.execution.parametersselection.toolbar.clear')
		   	, scope: this
		   	, handler : function() {
				this.clearParametersForm();
			}
		}));
		
		if (Sbi.user.functionalities.contains('SeeViewpointsFunctionality')) {
			this.toolbar.addButton(new Ext.Toolbar.Button({
				iconCls: 'icon-save'
				, tooltip: LN('sbi.execution.parametersselection.toolbar.save')
			   	, scope: this
			   	, handler : function() {
					this.saveParametersFormStateAsViewpoint();
				}
			}));
		}
		
		this.toolbar.addSeparator();
		
		this.toolbar.addButton(new Ext.Toolbar.Button({
			iconCls: 'icon-execute'
			, tooltip: LN('sbi.execution.parametersselection.toolbar.next')
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
				params.viewpoint = Sbi.commons.JSON.encode( formState );
				Ext.Ajax.request({
			          url: this.services['saveViewpointService'],
			          
			          params: params,
			          
			          callback : function(options, success, response){
						if(success && response !== undefined) {   
				      		if(response.responseText !== undefined) {
				      			var content = Ext.util.JSON.decode( response.responseText );
				      			if(content !== undefined) {
				      				Ext.MessageBox.show({
					      				title: 'Status',
					      				msg: LN('sbi.execution.viewpoints.msg.saved'),
					      				modal: false,
					      				buttons: Ext.MessageBox.OK,
					      				width:300,
					      				icon: Ext.MessageBox.INFO 			
					      			});
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
			items: [new Ext.Toolbar.Button({iconCls: 'icon-back'})]
		});
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
	
	, startExecutionAutomatically: function() {
		// must wait subobjects panel and snapshots panel have been loaded
		// For parameters instead, we cannot wait: the 'readyforexecution' may not be fired
		if (this.isSubobjectPanelReady === false || this.isSnapshotPanelReady === false) {
			return;
		}
		
		// subobject preference wins: if a subobject preference is specified, subobject is executed
		if (this.preferenceSubobjectId != null) {
			this.executionInstance.isPossibleToComeBackToParametersPage = false;
			this.onExecuteSubobject(this.preferenceSubobjectId);
			return;
		}
		// snapshot preference follows: if a snapshot preference is specified, snapshot is executed
		if (this.preferenceSnapshotId != null) {
			this.executionInstance.isPossibleToComeBackToParametersPage = false;
			this.onExecuteSnapshot(this.preferenceSnapshotId);
			return;
		}
		// parameters form follows: if there are no parameters to be filled, start main document execution
		if (this.isParameterPanelReady == true) {
			this.executionInstance.isPossibleToComeBackToParametersPage = false;
			this.fireEvent('movenextrequest', this);
		}
	}
	
	, onExecuteViewpoint: function(v) {
		this.parametersPanel.applyViewPoint(v);
		this.fireEvent('movenextrequest');
	}
	
	, onExecuteSubobject: function (subObjectId) {
		this.executionInstance.SBI_SUBOBJECT_ID = subObjectId;
		this.fireEvent('movenextrequest');
	}
	
	, onExecuteSnapshot: function (snapshotId) {
		this.executionInstance.SBI_SNAPSHOT_ID = snapshotId;
		this.fireEvent('movenextrequest');
	}
	
});