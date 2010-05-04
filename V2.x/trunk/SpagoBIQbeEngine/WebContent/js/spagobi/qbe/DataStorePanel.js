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

Ext.ns("Sbi.widgets");

Sbi.widgets.DataStorePanel = function(config) {
	
	this.baseConfig = config;
	
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
	this.services['exportToExternalService'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'INVOKE_EXTERNAL_SERVICE_ACTION'
		, baseParams: params
	});
		
	this.initStore();
	this.initPanel();
	
	c = Ext.apply(c, {
		title: LN('sbi.qbe.datastorepanel.title'),  
		layout: 'fit',
		items: [this.grid]
	});
	
	// constructor
	Sbi.widgets.DataStorePanel.superclass.constructor.call(this, c);
    
    this.addEvents();
};

Ext.extend(Sbi.widgets.DataStorePanel, Ext.Panel, {
    
    services: null
	, baseConfig: null
    , store: null
	, paging: null
	, pageSize: null
	, pageNumber: null
   
   
	// ---------------------------------------------------------------------------------------------------
    // public methods
	// ---------------------------------------------------------------------------------------------------
	
	, execQuery:  function(query, freeFiltersForm) {  
		this.store.removeAll();
		this.store.baseParams = {id: query.id};
		var requestParameters = Ext.apply({start: 0, limit: 25 }, freeFiltersForm || {});
		this.store.load({params: requestParameters});
	}


	, exportResult: function(mimeType) {
		var form = document.getElementById('export-form');
		if(!form) {
			var dh = Ext.DomHelper;
			form = dh.append(Ext.getBody(), {
			    id: 'export-form'
			    , tag: 'form'
			    , method: 'post'
			    , cls: 'export-form'
			});
		}
		
		form.action = this.services['exportDataStore'] + '&MIME_TYPE=' + mimeType +'&RESPONSE_TYPE=RESPONSE_TYPE_ATTACHMENT';
		form.submit();
	}
  	
	// ---------------------------------------------------------------------------------------------------
	// private methods
	// ---------------------------------------------------------------------------------------------------
	
	, renderHtml: function(value, meta, record, row, col, store){
		
	}
	
	
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
			
		  
		   for(var i = 0; i < meta.fields.length; i++) {
			   if(meta.fields[i].type) {
				   var t = meta.fields[i].type;
				   //if(t === 'float' || t ==='int') t = 'number';
				   if (meta.fields[i].format) { // format is applied only to numbers
					   var format = Sbi.qbe.commons.Format.getFormatFromJavaPattern(meta.fields[i].format);
					   var f = Ext.apply( Sbi.locale.formats[t], format);
					   meta.fields[i].renderer = Sbi.qbe.commons.Format.numberRenderer(f);
				   } else {
					   meta.fields[i].renderer = Sbi.locale.formatters[t];
				   }			   
			   }
			   
			   if(meta.fields[i].subtype && meta.fields[i].subtype === 'html') {
				   meta.fields[i].renderer  =  Sbi.locale.formatters['html'];
			   }
			   
		   }
		   meta.fields[0] = new Ext.grid.RowNumberer();
		   this.grid.getColumnModel().setConfig(meta.fields);
		   
		   
		   this.alias2FieldMetaMap = {};
		   var fields = meta.fields;
		   for(var i = 0, l = fields.length, f; i < l; i++) {
			   f = fields[i];
			   if( typeof f === 'string' ) {
				   f = {name: f};
			   }
			   f.header = f.header || f.name;
			   this.alias2FieldMetaMap[f.header] = f;
			   //if(!this.alias2FieldMetaMap[f.header]) {
			   //	this.alias2FieldMetaMap[f.header] = new Array();
			   //}
			   //this.alias2FieldMetaMap[f.header].push(f);
		   }
		   
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
		            tooltip: LN('sbi.qbe.datastorepanel.button.tt.exportto') + ' pdf',
		            iconCls:'pdf',
		            handler: this.exportResult.createDelegate(this, ['application/pdf']),
		            scope: this
			    }),
			    new Ext.Toolbar.Button({
		            tooltip:LN('sbi.qbe.datastorepanel.button.tt.exportto') + ' rtf',
		            iconCls:'rtf',
		            handler: this.exportResult.createDelegate(this, ['application/rtf']),
		            scope: this
			    }),
			    new Ext.Toolbar.Button({
		            tooltip:LN('sbi.qbe.datastorepanel.button.tt.exportto') + ' xls',
		            iconCls:'xls',
		            handler: this.exportResult.createDelegate(this, ['application/vnd.ms-excel']),
		            scope: this
			    }),
			    new Ext.Toolbar.Button({
		            tooltip:LN('sbi.qbe.datastorepanel.button.tt.exportto') + ' csv',
		            iconCls:'csv',
		            handler: this.exportResult.createDelegate(this, ['text/csv']),
		            scope: this
			    }),
			    new Ext.Toolbar.Button({
		            tooltip:LN('sbi.qbe.datastorepanel.button.tt.exportto') + ' jrxml',
		            iconCls:'jrxml',
		            handler: this.exportResult.createDelegate(this, ['text/jrxml']),
		            scope: this
			    })
			]
		});
		
		this.warningMessageItem = new Ext.Toolbar.TextItem('<font color="red">' 
				+ LN('sbi.qbe.datastorepanel.grid.beforeoverflow') 
				+ ' [' + Sbi.config.queryLimit.maxRecords + '] '
				+ LN('sbi.qbe.datastorepanel.grid.afteroverflow') 
				+ '</font>');
		
		
		this.pagingTBar = new Ext.PagingToolbar({
            pageSize: 25,
            store: this.store,
            displayInfo: true,
            displayMsg: LN('sbi.qbe.datastorepanel.grid.displaymsg'),
            emptyMsg: LN('sbi.qbe.datastorepanel.grid.emptymsg'),
            beforePageText: LN('sbi.qbe.datastorepanel.grid.beforepagetext'),
            afterPageText: LN('sbi.qbe.datastorepanel.grid.afterpagetext'),
            firstText: LN('sbi.qbe.datastorepanel.grid.firsttext'),
            prevText: LN('sbi.qbe.datastorepanel.grid.prevtext'),
            nextText: LN('sbi.qbe.datastorepanel.grid.nexttext'),
            lastText: LN('sbi.qbe.datastorepanel.grid.lasttext'),
            refreshText: LN('sbi.qbe.datastorepanel.grid.refreshtext')
        });
		this.pagingTBar.on('render', function() {
			this.pagingTBar.addItem(this.warningMessageItem);
			this.warningMessageItem.setVisible(false);
		}, this);
		
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
	            forceFit:false,
	            autoFill: true,
	            enableRowBody:true,
	            showPreview:true
	        },
	        
	        //tbar:this.exportTBar,
	        bbar: this.pagingTBar
	    });
	    
	    // START CONTEXT MENU FOR EXTERNAL SERVICES INTEGRATION
	    if (this.baseConfig.externalServicesConfig.length > 0) {
	    	
			// the row context menu
		    var externalServicesMenuItems = [];
		    for (var counter = 0; counter < this.baseConfig.externalServicesConfig.length; counter++) {
		    	externalServicesMenuItems.push({
		    		id: this.baseConfig.externalServicesConfig[counter].id,
					text: this.baseConfig.externalServicesConfig[counter].description,
					scope: this,
					handler: function(item) {
						var selectedRecords = new Array();
						for (var i = 0; i < menu.selectedRecords.length; i++) {
							var record = menu.selectedRecords[i];
							var myRecord = this.adjustRecordHeaders(record.data);
							selectedRecords.push(myRecord);
						}
						var params = {
								"id": item.id
								, "records": Sbi.commons.JSON.encode(selectedRecords)
						};
						this.callExternalService(params);
					}
		    	});
		    }
		    
		   	var menu = 
				new Ext.menu.Menu({
					items: externalServicesMenuItems
			});
		    
		    this.grid.on(
				'rowcontextmenu', 
				function(grid, rowIndex, e) {
					var sm = grid.getSelectionModel();
					if (!sm.isSelected(rowIndex)) {
						sm.clearSelections();
						sm.selectRow(rowIndex, true);
					}
					var records = sm.getSelections();
					e.stopEvent();
					menu.selectedRecords = records;
					menu.showAt(e.getXY());
				}
		    );
	    
	    } 
	    // END CONTEXT MENU FOR EXTERNAL SERVICES INTEGRATION
	    
	}
	
	
	/**
	 * Utility method that returns the JSON object associated to a grid record data:
	 * the JSON object has this structure:
	 * {
	 * 		"header_column_1": "row_cell_1"
	 * 		, "header_column_2": "row_cell_2"
	 * 		, ....
	 * }
	 */
	, adjustRecordHeaders: function(data) {
		var toReturn = {};
		for (header in this.alias2FieldMetaMap) {
			if (header !== undefined) {
				var field = this.alias2FieldMetaMap[header];
				toReturn[header] = data[field.name];
			}
		}
		return toReturn;
	}
	
	/**
	 * This method calls the action for external services integration and shows service response.
	 */
	, callExternalService: function(params) {
		Ext.MessageBox.wait('Please wait...', 'Processing');
		Ext.Ajax.request({
		    url: this.services['exportToExternalService'],
		    success: function(response, options) {
				var content = Ext.util.JSON.decode( response.responseText );
				if (content.missingcolumns) {
		    		Ext.Msg.show({
	 				   title: LN('sbi.qbe.datastorepanel.externalservices.errors.title'),
	 				   msg: LN('sbi.qbe.datastorepanel.externalservices.errors.missingcolumns') + ' ' + content.missingcolumns,
					   buttons: Ext.Msg.OK,
					   icon: Ext.MessageBox.WARNING
			 		});
				} else {
		    		Ext.Msg.show({
	 				   title: LN('sbi.qbe.datastorepanel.externalservices.title'),
	 				   msg: LN('sbi.qbe.datastorepanel.externalservices.serviceresponse') + ' ' + content.serviceresponse,
					   buttons: Ext.Msg.OK,
					   icon: Ext.MessageBox.INFO
		 			});
				}
			},
		    failure: Sbi.exception.ExceptionHandler.handleFailure,
		    scope: this,
		    params: params
		});
	}
	
	, onDataStoreLoaded: function(store) {
		 var recordsNumber = store.getTotalCount();
       	 if(recordsNumber == 0) {
       		Ext.Msg.show({
				   title: LN('sbi.qbe.messagewin.info.title'),
				   msg: LN('sbi.qbe.datastorepanel.grid.emptywarningmsg'),
				   buttons: Ext.Msg.OK,
				   icon: Ext.MessageBox.INFO
			});
       	 }
       	 
       	 if (Sbi.config.queryLimit.maxRecords !== undefined && recordsNumber > Sbi.config.queryLimit.maxRecords) {
       		if (Sbi.config.queryLimit.isBlocking) {
       			Sbi.exception.ExceptionHandler.showErrorMessage(this.warningMessageItem, LN('sbi.qbe.messagewin.error.title'));
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