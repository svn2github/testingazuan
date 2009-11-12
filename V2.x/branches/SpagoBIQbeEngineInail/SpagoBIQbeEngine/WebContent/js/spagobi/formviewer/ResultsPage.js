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

Ext.ns("Sbi.formviewer");

Sbi.formviewer.ResultsPage = function(config) {	
	var defaultSettings = {
		//title: LN('sbi.qbe.queryeditor.title')
	};
		
	if(Sbi.settings && Sbi.settings.formviewer && Sbi.settings.formviewer.resultsPage) {
		defaultSettings = Ext.apply(defaultSettings, Sbi.settings.formviewer.resultsPage);
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
    , groupInputField: null
    , formState: null
   
    // -- public methods -----------------------------------------------------------------------
    
    , getFormState: function() {
		return this.formState;
	}

	, setFormState: function(formState) {
		this.formState = formState;
	}
    
    , loadResults: function() {
		//var values = this.groupInputField.getValuesList();
    	var values = [];
    	for (var i in this.formState.groupingVariables) {
    		values.push(this.formState.groupingVariables[i]);
    	}
		var baseParams = {groupFields: Ext.util.JSON.encode(values), formstate: Ext.util.JSON.encode(this.formState)}
		this.masterResultsPanel.execQuery(baseParams);
	}
    
    // -- private methods -----------------------------------------------------------------------
        
    , initControlPanel: function() {
		var p = {type: 'groupable'};
		store = new Ext.data.JsonStore({
			url: this.services['getSelectedColumns']
			, baseParams: p
			, root: 'fields'
		    , fields: ['name', 'alias']
		});
		
		store.on('loadexception', function(store, options, response, e) {
			Sbi.exception.ExceptionHandler.handleFailure(response, options);
		});
		
		this.groupInputField = new Ext.ux.form.SuperBoxSelect({
	        editable: true			    
		    , forceSelection: false
		    , store: store
		    , fieldLabel: 'Group By'
		    , anchor:'100%'
		    , displayField: 'alias'
		    , valueField: 'name'
		    , emptyText: ''
		    , typeAhead: false
		    , triggerAction: 'all'
		    , selectOnFocus: true
		    , autoLoad: false
		});
		
		var submitButton = new Ext.Button({
			text: "Ricalcola",
            scope: this,
            handler: this.loadResults

		});
	
		this.controlPanel = new Ext.Panel({
			region: 'north'
			, layout: 'border'
			, height: 50
			, items: [
			     new Ext.form.FormPanel({
			    	 region: 'center', 
			    	 bodyStyle:'padding:10px',
			    	 items: [this.groupInputField]
			     })
				, new Ext.form.FormPanel({
					region: 'east', 
					split: false, 
					bodyStyle:'padding:13 10 13 5',
					width: 85, 
					items: [submitButton]
				})
			]
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
			minWidth: 320,
			
			services: {
				loadDataStore: Sbi.config.serviceRegistry.getServiceUrl({
					serviceName: 'EXECUTE_MASTER_QUERY_ACTION'
					, baseParams: new Object()
				})
			}
		});
	
		this.detailResultsPanel = new Sbi.formviewer.DataStorePanel({
			region: 'center',
			frame: false, 
		    border: false,
		    
		    services: {
				loadDataStore: Sbi.config.serviceRegistry.getServiceUrl({
					serviceName: 'EXECUTE_DETAIL_QUERY_ACTION'
					, baseParams: new Object()
				})
			}
		});
		
		this.masterResultsPanel.grid.on("rowdblclick", function(grid,  rowIndex, e){
	    	var row;
	       	var record = grid.getStore().getAt( rowIndex );
	       	var baseParams = grid.getStore().baseParams;
	       	var fields = Ext.util.JSON.decode(baseParams.groupFields);
	       	var filters = new Array();
	       	for(var i = 0; i < fields.length; i++) {
	       		var filter = {
	       			columnName: fields[i],
	       			value: record.get('column-'+(i+1))
	       		};
	       		filters.push(filter);
	       	}
	       	
	       	var baseParams = {filters: Ext.util.JSON.encode(filters), formState: Ext.util.JSON.encode(this.formState)}
			this.detailResultsPanel.execQuery(baseParams);
	    }, this);
	}
});