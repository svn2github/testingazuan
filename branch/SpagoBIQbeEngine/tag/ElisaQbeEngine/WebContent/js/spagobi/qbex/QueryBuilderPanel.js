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
	
	this.selectGridPanel = new Sbi.qbe.SelectGridPanel(c);
    this.filterGridPanel = new Sbi.qbe.FilterGridPanel(c);
 
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
    
  
    this.selectGridPanel.on('filter', function(panel, record) {
    	this.filterGridPanel.addRow(record);
    }, this);
    
    
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
    
    this.centerRegionPanel = new Ext.Panel({ 
        
    	title:'Query Editor',
        region:'center',
        autoScroll: true,
		containerScroll: true,
		
        margins: '5 5 5 0',
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
				   	callback: function(success, response, options) {
	       				if(success) {
	       					this.showSaveViewWindow();
	       				}
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
        	Ext.Ajax.request({
				    url: this.services['synchronizeQuery'],
				    callback: function(success, response, options) {
	       				if(success) {
	       					this.executeQuery();
	       				}
	       			},
	       			scope: this,
				    failure: Sbi.exception.ExceptionHandler.handleFailure,					
				    params: this.getParams
				});                      
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
	
    
	c = Ext.apply(c, {
      	title: 'Query',
      	layout: 'border',
      	//style: 'background:red;',
      	border: false,
      	items: [this.westRegionPanel, this.centerRegionPanel]
	});

	// constructor
    Sbi.qbe.QueryBuilderPanel.superclass.constructor.call(this, c);
};

Ext.extend(Sbi.qbe.QueryBuilderPanel, Ext.Panel, {
    
    services: null
    , qbeStructurePanel: null
    , dataMartStructurePanel: null
    , selectGridPanel: null
    , filterGridPanel: null
    , saveQueryWindow: null
    , saveViewWindow: null
   
    , getQuery: function() {
		var query = {};
		query.fileds =  this.selectGridPanel.getRowsAsJSONParams();
		query.distinct = this.selectGridPanel.distinctCheckBox.getValue();
		query.filters = this.filterGridPanel.getRowsAsJSONParams();
		query.filterExpression = this.filterGridPanel.getFiltersExpressionAsJSON();
		return query;
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
		
		//var qFields = this.selectGridPanel.getFields();
		//var qFilters = this.selectGridPanel.getFilters();
		//var qFilterExp = getFiltersExpression();
		
		var queryStr = '{';
    	queryStr += 'fields : ' + this.selectGridPanel.getRowsAsJSONParams() + ',';
    	queryStr += 'filters : ' + this.filterGridPanel.getRowsAsJSONParams() + ',';
    	queryStr += 'distinct : ' + this.selectGridPanel.distinctCheckBox.getValue() + ',';
    	queryStr += 'expression: ' +  this.filterGridPanel.getFiltersExpressionAsJSON();
    	queryStr += '}';
		//var qRecords =  this.selectGridPanel.getRowsAsJSONParams();
	    //var qFilters = this.filterGridPanel.getRowsAsJSONParams();
	    //var qFilterExp = this.filterGridPanel.getFiltersExpressionAsJSON();
	    
	    var url = this.services['saveQuery'];
	    url += '&queryName=' + meta.name;
	    url += '&queryDescription=' + meta.description;
	    url += '&queryScope=' + meta.scope;
	    url += '&query=' +queryStr;
	    //url += '&queryRecords=' + query.fields;
	    //url += '&queryFilters=' + query.filters;
	    //url += '&queryFilterExp=' + query.filterExpression;
	    
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
	
	

	
});