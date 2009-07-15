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

Ext.ns("Sbi.qbe");

Sbi.qbe.QueryBuilderPanel = function(config) {
	
	var c = Ext.apply({
		// set default values here
	}, config || {});
	
	this.services = new Array();
	var params = {};
	this.services['synchronizeQuery'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'REFRESH_QUERY_ACTION'
		, baseParams: params
	});
	this.services['saveQuery'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'SAVE_QUERY_ACTION'
		, baseParams: params
	});
	this.services['validateQuery'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'VALIDATE_QUERY_ACTION'
		, baseParams: params
	});
	this.services['saveView'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'CREATE_VIEW_ACTION'
		, baseParams: params
	});
	
	
	this.addEvents('execute');
		
	this.initWestRegionPanel(c);
	this.initCenterRegionPanel(c);
	this.initEastRegionPanel(c);
		
	c = Ext.apply(c, {
      	title: 'Query',
      	layout: 'border',
      	frame: false, 
      	border: false,
      	items: [this.westRegionPanel, this.centerRegionPanel, this.eastRegionPanel]
	});

	// constructor
    Sbi.qbe.QueryBuilderPanel.superclass.constructor.call(this, c);
};

Ext.extend(Sbi.qbe.QueryBuilderPanel, Ext.Panel, {
    
    services: null
    
    , eastRegionPanel: null
    , centerRegionPanel: null
    , westRegionPanel: null
    
    , qbeStructurePanel: null
    , queryCataloguePanel: null
    , dataMartStructurePanel: null
    , selectGridPanel: null
    , filterGridPanel: null
    
    , saveQueryWindow: null
    , saveViewWindow: null
   
    
    , setQuery: function(query) {
		this.selectGridPanel.setFields(query.fields);
		this.selectGridPanel.distinctCheckBox.setValue(query.distinct);
		this.filterGridPanel.setFilters(query.filters);
		
		this.filterGridPanel.setFiltersExpression(query.filterExpression);
		if(query.filterExpression && query.filterExpression.fromWizard === true) {
			this.filterGridPanel.setWizardExpression(true);		
		} else {
			this.filterGridPanel.setWizardExpression(false);	
		}
		
	}
    
    , getQuery: function(asObject) {
    	var query = {};
    	
    	if(asObject) {
    		var selectedQuery = this.queryCataloguePanel.getSelectedQuery()
    		if(selectedQuery)query.id = selectedQuery.id;
    		query.fields = this.selectGridPanel.getFields();
    		query.distinct = this.selectGridPanel.distinctCheckBox.getValue();
    		query.filters = this.filterGridPanel.getFilters();
    		query.filterExpression = this.filterGridPanel.getFiltersExpression();
    		query.filterExpression.fromWizard = this.filterGridPanel.isWizardExpression();
    	} else {		
			query.fileds =  this.selectGridPanel.getRowsAsJSONParams();
			query.distinct = this.selectGridPanel.distinctCheckBox.getValue();
			query.filters = this.filterGridPanel.getRowsAsJSONParams();
			query.filterExpression = this.filterGridPanel.getFiltersExpressionAsJSON();
    	}
		return query;
	}
    
    
    /**
	 * apply all performed changes to the selected query 
	 */
	, applyChanges: function() {
		this.queryCataloguePanel.synchronizeSelectedQuery( this.getQuery(true) );
	}
	
	/**
	 * undo all performed changes reverting to the previous status of the selected query 
	 */
	, resetChanges: function() {
		var q = this.queryCataloguePanel.getSelectedQuery();
		this.setQuery(q);
	}
	
	/**
	 * synchronize queries contained into the client side representation of the catalogue with
	 * its server side representation
	 */
	, commitQueryCatalogue: function(handleSuccessFn) {
		handleSuccessFn = handleSuccessFn || function(response, options) {
			alert('Catalogue synchronized succefully with the server');
		};
		
		this.applyChanges();
		
		alert('committing catalogue');
		var params = {
				catalogue: Ext.util.JSON.encode(this.queryCataloguePanel.getQueries())
		};
		
		Ext.Ajax.request({
		    url: this.services['synchronizeQuery'],
		    success: handleSuccessFn,
   			scope: this,
		    failure: Sbi.exception.ExceptionHandler.handleFailure,	
		    params: params
		});                      	
	}
   
    // public methods
    , executeQuery: function() {
    	var q = this.getQuery();
    	this.fireEvent('execute', this, q);
    }

	, saveQuery: function(meta) {
		var qName = meta.name;
		var qDescription = meta.description;
		var qScope = meta.scope;
		var queryStr = '{';
    	queryStr += 'fields : ' + this.selectGridPanel.getRowsAsJSONParams() + ',';
    	queryStr += 'filters : ' + this.filterGridPanel.getRowsAsJSONParams() + ',';
    	queryStr += 'distinct : ' + this.selectGridPanel.distinctCheckBox.getValue() + ',';
    	queryStr += 'expression: ' +  this.filterGridPanel.getFiltersExpressionAsJSON();
    	queryStr += '}';
		
	    var url = this.services['saveQuery'];
	    url += '&queryName=' + meta.name;
	    url += '&queryDescription=' + meta.description;
	    url += '&queryScope=' + meta.scope;
	    url += '&query=' +queryStr;
	    
	    Ext.Ajax.request({
			url:  url,
			success: function(response, options) {
				var content;
				
				content = Ext.util.JSON.decode( response.responseText );
				content.text = content.text || "";
				if (content.text.match('OK - ')) {
					try {
						parent.loadSubObject(window.name, content.text.substr(5));
					} catch (ex) {}
					try {
						sendMessage("Subobject saved!!!!","subobjectsaved");
					} catch (ex) {}
				}
			},
			failure: Sbi.exception.ExceptionHandler.handleFailure					
		});   
	}
	
	, saveView: function(meta) {
		var url = this.services['saveView'];			
	    url += '&viewName=' + meta.name;
	       
       	Ext.Ajax.request({
				url:  url,
				callback: function(success, response, options) {
       				if(success) {
       					this.dataMartStructurePanel.load();
       				}
       			},
       			scope: this,
				failure: Sbi.exception.ExceptionHandler.handleFailure		
       	});   
       	
	}
    
	, showSaveQueryWarning: function() {
		Ext.Msg.confirm('WARNING','The query is incorrect; do you want to save it anyway?', function(btn) {
			if (btn == 'yes') {
				this.showSaveQueryWindow();
			}
		}, this);
	}

	, showSaveQueryWindow: function(){
	    if(this.saveQueryWindow === null) {
	    	this.saveQueryWindow = new Sbi.widgets.SaveWindow({
	    		title: 'Save query ...'
	    	});
	    	this.saveQueryWindow.on('save', function(win, formState){this.saveQuery(formState)}, this);
		}
	    this.saveQueryWindow.show();
	}
	
	, showSaveViewWindow: function() {
		if(this.saveViewWindow === null) {
			this.saveViewWindow = new Sbi.widgets.SaveWindow({
	    		title: 'Save query as view...'
	    		, descriptionFieldVisible: false
	    		, scopeFieldVisible: false
	    	});
			this.saveViewWindow.on('save', function(win, formState){this.saveView(formState)}, this);
		}
		this.saveViewWindow.show();
	}
	
	
	
	
	, getParams: function() {
    	
    	var queryStr = '{';
    	queryStr += 'fields : ' + this.selectGridPanel.getRowsAsJSONParams() + ',';
    	queryStr += 'distinct : ' + this.selectGridPanel.distinctCheckBox.getValue() + ',';
    	queryStr += 'filters : ' + this.filterGridPanel.getRowsAsJSONParams() + ',';
    	queryStr += 'expression: ' +  this.filterGridPanel.getFiltersExpressionAsJSON();
    	queryStr += '}';
    	
    	
    	var params = {
    		query: queryStr 
    	};        	
    	
    	return params;
    }
	
	// -- private methods --------------------------------------------------------------------
	
	, init: function(c) {
		this.initWestRegionPanel(c);
	}
	
	, initWestRegionPanel: function(c) {
		
		this.dataMartStructurePanel = new Sbi.qbe.DataMartStructurePanel(c);
		this.qbeStructurePanel = new Ext.Panel({
	        id:'treepanel',
	        collapsible: true,
	        margins:'0 0 0 5',
	        layout:'accordion',
	        layoutConfig:{
	          animate:true
	        },
	        items: [this.dataMartStructurePanel]
	    });
		
		this.westRegionPanel = new Ext.Panel({
	        title:'Schema',
	        region:'west',
	        width:250,
	        margins: '5 5 5 5',
	        layout:'fit',
	        collapsible: true,
	        collapseMode: 'mini',
	        collapseFirst: false,
	        
	        tools:[{
	          id:'pin',
	          qtip:'Expand all',
	          // hidden:true,
	          handler: function(event, toolEl, panel){
	            // refresh logic
	          }
	        }, {
	          id:'unpin',
	          qtip:'Collapse all',
	          // hidden:true,
	          handler: function(event, toolEl, panel){
	            // refresh logic
	          }
	        }, {
	          id:'gear',
	          qtip:'Flat view',
	          // hidden:true,
	          handler: function(event, toolEl, panel){
	            // refresh logic
	          }
	        }, {
	          id:'plus',
	          qtip:'Add calulated field',
	          // hidden:true,
	          handler: function(event, toolEl, panel){
	            // refresh logic
	          }
	        }],
	        
	        items:[this.qbeStructurePanel]
	    });
		
		this.dataMartStructurePanel.on('click', function(panel, node) {
	    	if(node.attributes.field && node.attributes.type == 'field') {
			    var record = new this.selectGridPanel.Record({
			    	 id: node.id,
			         entity: node.attributes.entity , 
			         field: node.attributes.field  
			      });
			      
			    this.selectGridPanel.addRow(record); 
			 }
	    }, this);
	}
	
	, initCenterRegionPanel: function(c) {
		this.selectGridPanel = new Sbi.qbe.SelectGridPanel(c);
	    this.filterGridPanel = new Sbi.qbe.FilterGridPanel(c);
	    	    
	    this.centerRegionPanel = new Ext.Panel({ 
	        
	    	title:'Query Editor',
	        region:'center',
	        autoScroll: true,
			containerScroll: true,
			
	        margins: '5 5 5 5',
	        tools:[{
          id:'save',
          qtip:'Save query as subobject',
          handler: function(event, toolEl, panel){
        	// if validation is enabled, validate query before saving
        	if (Sbi.config.queryValidation.isEnabled) {
        		// synchronize query first
            	Ext.Ajax.request({
				    url: this.services['synchronizeQuery'],
				    success: function(response, options) {
            			// then validate query
    	        		Ext.Ajax.request({
	    	        		url: this.services['validateQuery'],
	    	        		success: function(response, options) {
	            				 var result = Ext.util.JSON.decode( response.responseText );
	            		       	 if (result !== undefined && result !== null && result.validationResult !== undefined) {
	            		       		if (result.validationResult) {
	            		       			// validation was successful
	            		       			this.showSaveQueryWindow();
	            		       		} else {
	            		       			// validation failed, check if validation is blocking
	            		       			if (Sbi.config.queryValidation.isBlocking) {
	            		       				Sbi.exception.ExceptionHandler.showErrorMessage('Cannot save query since it is incorrect!', 'ERROR');
	            		       			} else {
	            		       				this.showSaveQueryWarning();
	            		       			}
	            		       		}
	            		       	 } else {
	            		       		alert("An error occurred while validating query");
	            		       	 }
	    	        		},
	    	        		scope: this,
	    	        		failure: Sbi.exception.ExceptionHandler.handleFailure
	    	        	});
	       			},
	       			scope: this,
				    failure: Sbi.exception.ExceptionHandler.handleFailure,					
				    params: this.getParams
				});
        	} else {
        		this.showSaveQueryWindow();
        	}
          },
          scope: this
        }, {
	          id:'saveView',
	          qtip:'Save query as view',
	          handler: function(event, toolEl, panel){
	        	Ext.Ajax.request({
					   	url: this.services['synchronyzeQuery'],
					   	success: function(success, response, options) {
		       				this.showSaveViewWindow();
		       			},
		       			scope: this,
		       			failure: Sbi.exception.ExceptionHandler.handleFailure,					
		       			params: this.getParams
					});	   
				},
	            scope: this 
	        },{
	          id:'gear',
	          qtip:'Execute query',
	          handler: function(event, toolEl, panel){
	        	this.commitQueryCatalogue(function() {
	        		alert('catalogue committed');
    				this.fireEvent('execute', this, this.queryCataloguePanel.getSelectedQuery());
       			});
	        	
	        	/*
	        	Ext.Ajax.request({
					    url: this.services['synchronizeQuery'],
					    success: function(response, options) {
	        				this.fireEvent('execute', this, this.getQuery());
		       			},
		       			scope: this,
					    failure: Sbi.exception.ExceptionHandler.handleFailure,					
					    params: this.getParams
					});
					*/                      
	          },
	          scope: this
	        },{
	          id:'search',
	          qtip:'Validate query',
	          // hidden:true,
	          handler: function(event, toolEl, panel){
	            // refresh logic
	          }
	        }, {
	          id:'help',
	          qtip:'Help me please',
	          handler: function(event, toolEl, panel){
	            // refresh logic
	          }
	        }],
	        
	        items: [this.selectGridPanel, this.filterGridPanel]
	    });
	    
	    this.selectGridPanel.on('filter', function(panel, record) {
	    	this.filterGridPanel.addRow(record);
	    }, this);
	}
	
	
	
	, initEastRegionPanel: function(c) {
		
		this.queryCataloguePanel = new Sbi.qbe.QueryCataloguePanel({margins: '0 5 0 0',});
		
		this.eastRegionPanel = new Ext.Panel({
	        title:'Query Catalogue',
	        region:'east',
	        width:250,
	        margins: '5 5 5 5',
	        layout:'fit',
	        collapsible: true,
	        collapseMode: 'mini',
	        collapseFirst: false,
	        tools:[
		        {
		          id:'delete',
		          qtip:'Delete query',
		          // hidden:true,
		          handler: function(event, toolEl, panel){
		        	//this.queryCataloguePanel.addQuery();
		          }, 
		          scope: this
		        }, {
		          id:'plus',
		          qtip:'Add query',
		          // hidden:true,
		          handler: function(event, toolEl, panel){
		        	this.queryCataloguePanel.addQuery();
		          },
		          scope: this
		        }
		    ],
	        items: [this.queryCataloguePanel]
	    });
		
		
		this.queryCataloguePanel.on('beforeselect', function(panel, newquery, oldquery){
			// save changes applied to old query before to move to the new selected one
			this.applyChanges(); 
			this.setQuery( newquery );
			
			/*
			if(oldnode) {
				oldnode.attributes.query = this.getQuery(true);
			}
			this.setQuery( newnode.attributes.query );
			*/
		}, this);
	}
		
});