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
  * - Andrea Gioia (mail)
  */

Ext.ns("Sbi.xxx");

Sbi.formviewer.ResultsPage = function(config) {	
	var defaultSettings = {
			//title: LN('sbi.qbe.queryeditor.title')
		};
		
		if(Sbi.settings && Sbi.settings.qbe && Sbi.settings.qbe.queryBuilderPanel) {
			defaultSettings = Ext.apply(defaultSettings, Sbi.settings.qbe.queryBuilderPanel);
		}
		
		var c = Ext.apply(defaultSettings, config || {});
		
		Ext.apply(this, c);
		
		
		this.services = this.services || new Array();	
		this.services['getSelectedColumns'] = this.services['getSelectedColumns'] || Sbi.config.serviceRegistry.getServiceUrl({
			serviceName: 'GET_SELECTED_COLUMNS_ACTION'
			, baseParams: new Object()
		});
		
		this.addEvents('execute');
		
		this.initControlPanel(c.controlPanelConfig || {});
		this.initMasterDetailPanel(c.masterDetailPanelConfig || {});
		
		c = Ext.apply(c, {
		    layout:'border',
		    bodyStyle:'background:green',
		    items: [this.controlPanel, this.masterResultsPanel, this.detailResultsPanel]
		});
		
		
		
		// constructor
	    Sbi.formviewer.ResultsPage.superclass.constructor.call(this, c);
};

Ext.extend(Sbi.formviewer.ResultsPage, Ext.Panel, {
    
    services: null
    , controlPanel: null
    , masterResultsPanel: null
    , detailResultsPanel: null
   
    // public methods
    
    , initControlPanel: function() {
		var p = {type: 'groupable'};
		var store = new Ext.data.JsonStore({
			url: this.services['getSelectedColumns']
			, params: p
		});
		
		store.on('loadexception', function(store, options, response, e) {
			Sbi.exception.ExceptionHandler.handleFailure(response, options);
		});
		
		this.field = new Ext.ux.form.SuperBoxSelect({
	        editable: true			    
		    , forceSelection: false
		    , store: store
		    , fieldLabel: 'Group On'
		    , width: 200
		    , displayField: 'column-1'
		    , valueField: 'column-1'
		    , emptyText: ''
		    , typeAhead: false
		    //, typeAheadDelay: 1000
		    , triggerAction: 'all'
		    , selectOnFocus: true
		    , autoLoad: false
		    //, maxSelection: 10
		});
	
		this.controlPanel = new Ext.form.FormPanel({
			layout: 'fit'
			, region: 'north'
			, bodyStyle:'padding:10px'
			, height: 50
			, items: [this.field]
		});
	}
    
    , initMasterDetailPanel: function() {
		this.masterResultsPanel = new Sbi.formviewer.DataStorePanel({
			region: 'west',
			split: true,
			collapsible: true,
			autoScroll: true,
			frame: false, 
			border: true,
			width: 320,
			minWidth: 320	
		});
	
		this.detailResultsPanel = new Sbi.formviewer.DataStorePanel({
			region: 'center',
			frame: false, 
		    border: false
		});
	}
});