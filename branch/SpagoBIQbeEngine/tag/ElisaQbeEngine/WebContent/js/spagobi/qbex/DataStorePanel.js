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

Ext.ns("Sbi.widgets");

Sbi.widgets.DataStorePanel = function(config) {
	
	var c = Ext.apply({
		// set default values here
	}, config || {});
	
	this.services = new Array();
	var params = {};
	this.services['loadDataStore'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'EXEC_QUERY_ACTION'
		, baseParams: params
	});
	this.services['exportDataStore'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'EXPORT_RESULT_ACTION'
		, baseParams: params
	});
	
	
	
	this.initStore();
	this.initPanel();
	
	c = Ext.apply(c, {
		title: 'Results',  
		layout: 'fit',
		items: [this.grid]
	})
	
	// constructor
	Sbi.widgets.DataStorePanel.superclass.constructor.call(this, c);
    
    this.addEvents();
};

Ext.extend(Sbi.widgets.DataStorePanel, Ext.Panel, {
    
    services: null
	, store: null
	, paging: null
	, pageSize: null
	, pageNumber: null
   
   
    // public methods
	
	, execQuery:  function(query) {  
		this.store.removeAll();
		this.store.baseParams = {id: query.id};
		this.store.load({params: {start: 0, limit: 25 }});
	}

	, exportResultToCsv: function() {
		this.exportResult('text/csv');
	}

	, exportResultToRtf: function() {
		this.exportResult('application/rtf');
	}

	, exportResultToXls: function() {
		this.exportResult('application/vnd.ms-excel');
	}

	, exportResultToPdf: function() {
		this.exportResult('application/pdf');
	}

	, exportResultToJrxml: function() {
		this.exportResult('text/jrxml');
	}

	, exportResult: function(mimeType) {
		
		var form = document.getElementById('export-form');
		if(!form) {
			alert('form is undefined');
			var dh = Ext.DomHelper;
			form = dh.append(Ext.getBody(), {
			    id: 'export-form'
			    , tag: 'form'
			    , method: 'post'
			    , cls: 'export-form'
			});
			alert('form creato');
		}
		
		form.action = this.services['exportDataStore'] + '&MIME_TYPE=' + mimeType +'&RESPONSE_TYPE=RESPONSE_TYPE_ATTACHMENT';
		alert('form.action: ' + form.action);
		form.submit();
	}
  

	
	// private methods
	
	, initStore: function() {
		
		this.proxy = new Ext.data.HttpProxy({
	           url: this.services['loadDataStore']
	           , timeout : 300000
	   		   , failure: this.onDataStoreLoadException
	    });
		
		this.store = new Ext.data.Store({
	        proxy: this.proxy,
	        reader: new Ext.data.JsonReader(),
	        remoteSort: true
	    });
		
		this.store.on('metachange', function( store, meta ) {
		   meta.fields[0] = new Ext.grid.RowNumberer();
		   this.grid.getColumnModel().setConfig(meta.fields);
		}, this);
		
		this.store.on('load', this.onDataStoreLoaded, this);
	}

	, initPanel: function() {
		var cm = new Ext.grid.ColumnModel([
			new Ext.grid.RowNumberer(), 
			{
				header: "Data",
				dataIndex: 'data',
				width: 75
			}
		]);
		
		this.exportTBar = new Ext.Toolbar({
			items: [
			    new Ext.Toolbar.Button({
		            tooltip:'Export in pdf',
		            iconCls:'pdf',
		            handler: this.exportResultToPdf,
		            scope: this
			    }),
			    new Ext.Toolbar.Button({
		            tooltip:'Export in rtf',
		            iconCls:'rtf',
		            handler: this.exportResultToRtf,
		            scope: this
			    }),
			    new Ext.Toolbar.Button({
		            tooltip:'Export in xls',
		            iconCls:'xls',
		            handler: this.exportResultToXls,
		            scope: this
			    }),
			    new Ext.Toolbar.Button({
		            tooltip:'Export in csv',
		            iconCls:'csv',
		            handler: this.exportResultToCsv,
		            scope: this
			    }),
			    new Ext.Toolbar.Button({
		            tooltip:'Export in jrxml',
		            iconCls:'jrxml',
		            handler: this.exportResultToJrxml,
		            scope: this
			    })
			]
		});
		
		this.warningMessageItem = new Ext.Toolbar.TextItem('<font color="red">Max records number limit [' + Sbi.config.queryLimit.maxRecords + '] exceeded!!</font>');
		
		this.pagingTBar = new Ext.PagingToolbar({
            pageSize: 25,
            store: this.store,
            displayInfo: true,
            displayMsg: 'Displaying topics {0} - {1} of {2}',
            emptyMsg: "No topics to display"
        });
		this.pagingTBar.on('render', function() {
			this.pagingTBar.addItem(this.warningMessageItem);
			this.warningMessageItem.setVisible(false);
		}, this);
		
		
		//this.warningMessageItem.setVisible(false);
		
		// create the Grid
	    this.grid = new Ext.grid.GridPanel({
	    	store: this.store,
	        cm: cm,
	        clicksToEdit:1,
	        style:'padding:10px',
	        frame: true,
	        border:true,  	        
	        collapsible:true,
	        loadMask: true,
	        viewConfig: {
	            forceFit:true,
	            enableRowBody:true,
	            showPreview:true
	        },
	        
	        tbar:this.exportTBar,
	        bbar: this.pagingTBar
	    });   
	}

	, onDataStoreLoaded: function(store) {
		 var recordsNumber = store.getTotalCount();
       	 /* if query returned to records, the 'load' event isn't fired by the store
		 if(recordsNumber == 0) {
       		alert("Query returns no data.");
       	 }
       	 */
       	 if (Sbi.config.queryLimit.maxRecords !== undefined && recordsNumber > Sbi.config.queryLimit.maxRecords) {
       		if (Sbi.config.queryLimit.isBlocking) {
       			Sbi.exception.ExceptionHandler.showErrorMessage('Max records number limit [' + Sbi.config.queryLimit.maxRecords + '] exceeded!!', 'ERROR');
       		} else {
       			this.warningMessageItem.show();
       		}
       	 } else {
       		this.warningMessageItem.hide();
       	 }
	}
	
	, onDataStoreLoadException: function(response, options) {
		Sbi.exception.ExceptionHandler.handleFailure(response, options);
	}

});