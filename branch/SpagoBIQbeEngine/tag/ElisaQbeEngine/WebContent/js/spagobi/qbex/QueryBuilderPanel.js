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
      	title: LN('sbi.qbe.queryeditor.title'),
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
    , havingGridPanel: null
    
    , saveQueryWindow: null
    , saveViewWindow: null
   
    
    , setQuery: function(query) {
		
		var parentQuery = this.queryCataloguePanel.getParentQuery(query.id);
		//this.filterGridPanel.enableValueWizard = (parentQuery !== null);
		this.filterGridPanel.query = query;
		this.filterGridPanel.parentQuery = parentQuery;
		
		this.selectGridPanel.setFields(query.fields);
		this.selectGridPanel.distinctCheckBox.setValue(query.distinct);
		this.filterGridPanel.setFilters(query.filters);
		
		this.filterGridPanel.setFiltersExpression(query.expression);
		if(query.isNestedExpression && query.isNestedExpression === true) {
			this.filterGridPanel.setWizardExpression(true);		
		} else {
			this.filterGridPanel.setWizardExpression(false);	
		}
		
		this.havingGridPanel.setFilters(query.havings);
	}
    
    , getQuery: function(asObject) {
    	var query = {};
    	
    	if(asObject) {
    		var selectedQuery = this.queryCataloguePanel.getSelectedQuery();
    		if(selectedQuery){
    			query.id = selectedQuery.id;
    			query.name = selectedQuery.name;
    			query.description = selectedQuery.description;
    		}
    		query.fields = this.selectGridPanel.getFields();
    		query.distinct = this.selectGridPanel.distinctCheckBox.getValue();
    		query.filters = this.filterGridPanel.getFilters();
    		query.expression = this.filterGridPanel.getFiltersExpression();
    		query.isNestedExpression = this.filterGridPanel.isWizardExpression();
    		query.havings = this.havingGridPanel.getFilters();
    	} else {		
    		//alert("get query as string is deprecated");
			query.fileds =  this.selectGridPanel.getRowsAsJSONParams();
			query.distinct = this.selectGridPanel.distinctCheckBox.getValue();
			query.filters = this.filterGridPanel.getRowsAsJSONParams();
			query.expression = this.filterGridPanel.getFiltersExpressionAsJSON();
    	}
		return query;
	}
    
    
    /**
	 * apply all performed changes to the selected query 
	 */
	, applyChanges: function() {
		var query = this.queryCataloguePanel.getSelectedQuery();
		if(query) {
			this.queryCataloguePanel.setQuery(query.id, this.getQuery(true) );
		}
	}
	
	/**
	 * undo all performed changes reverting to the previous status of the selected query 
	 */
	, resetChanges: function() {
		var query = this.queryCataloguePanel.getSelectedQuery();
		this.setQuery(query);
	}

	// --------------------------------------------------------------------------------
	// public methods
	// --------------------------------------------------------------------------------
	
    , executeQuery: function() {
    	this.applyChanges();
    	this.queryCataloguePanel.commit(function() {
			this.fireEvent('execute', this, this.queryCataloguePanel.getSelectedQuery());
		}, this);
    }
    
    , showSaveQueryWindow: function(){
	    if(this.saveQueryWindow === null) {
	    	this.saveQueryWindow = new Sbi.widgets.SaveWindow({
	    		title: 'Save query ...'
	    	});
	    	this.saveQueryWindow.on('save', function(win, formState){this.saveQuery(formState);}, this);
		}
	    this.saveQueryWindow.show();
	}
    
    , saveQuery: function(meta) {
    	this.applyChanges();
    	this.queryCataloguePanel.save(meta, function() {
    		// for old gui
    		try {
				parent.loadSubObject(window.name, content.text.substr(5));
			} catch (ex) {}
			// for new gui
			try {
				sendMessage("Subobject saved!!!!","subobjectsaved");
			} catch (ex) {}
			
			Ext.Msg.show({
				   title:'Query saved',
				   msg: 'Query saved succesfully',
				   buttons: Ext.Msg.OK,
				   icon: Ext.MessageBox.INFO
			});
		}, this);
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

	
	
	, showSaveViewWindow: function() {
		if(this.saveViewWindow === null) {
			this.saveViewWindow = new Sbi.widgets.SaveWindow({
	    		title: 'Save query as view...'
	    		, descriptionFieldVisible: false
	    		, scopeFieldVisible: false
	    	});
			this.saveViewWindow.on('save', function(win, formState){this.saveView(formState);}, this);
		}
		this.saveViewWindow.show();
	}
	
	
	
	
	, getParams: function() {
		Sbi.qbe.commons.deprectadeFunction('getParams', 'QueryBuilderPanel.js')
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
	
	// --------------------------------------------------------------------------------
	// 	private methods
	// --------------------------------------------------------------------------------
	
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
	        title:LN('sbi.qbe.queryeditor.westregion.title'),
	        region:'west',
	        width:250,
	        margins: '5 5 5 5',
	        layout:'fit',
	        collapsible: true,
	        //collapseMode: 'mini',
	        collapseFirst: false,
	        
	        tools:[{	// todo: marge pin and unpin button in one single toggle-button
	          id:'pin',
	          qtip: LN('sbi.qbe.queryeditor.westregion.tools.expand'),
	          // hidden:true,
	          handler: function(event, toolEl, panel){
	        	this.dataMartStructurePanel.expandAll();
	          }
	          , scope: this
	        }, {
	          id:'unpin',
	          qtip: LN('sbi.qbe.queryeditor.westregion.tools.collapse'),
	          // hidden:true,
	          handler: function(event, toolEl, panel){
	        	this.dataMartStructurePanel.collapseAll();
	          }
	          , scope: this
	        }, /*{
	          id:'gear',
	          qtip: LN('sbi.qbe.queryeditor.westregion.tools.flat'), 
	          // hidden:true,
	          handler: function(event, toolEl, panel){
	        	Sbi.qbe.commons.unimplementedFunction();
	          }
	          , scope: this
	        }, */ {
	          id:'plus',
	          qtip: LN('sbi.qbe.queryeditor.westregion.tools.addcalculated'),
	          // hidden:true,
	          handler: function(event, toolEl, panel){
	        	this.dataMartStructurePanel.addCalculatedField();
	          }
	          , scope: this
	        }],
	        
	        items:[this.qbeStructurePanel]
	    });
		
		this.dataMartStructurePanel.on('click', function(panel, node) {
			this.addNodeToSelectGrid(node);
	    }, this);
		
		/*
		 * work-around: when executing the following operations:
		 * 1. collapsing west region panel
		 * 2. expanding and collapsing SpagoBI parameters panel
		 * 3. executing query
		 * 4. coming back to query builder panel
		 * 5. expanding west region panel
		 * then the datamart structure tree was not displayed.
		 * This work-around forces the layout recalculation when west region panel is expanded
		 * TODO: try to remove it when upgrading Ext library
		 */
		this.westRegionPanel.on('expand', function() {
			this.westRegionPanel.doLayout();
		}, this);
	}
	
	, initCenterRegionPanel: function(c) {
		this.selectGridPanel = new Sbi.qbe.SelectGridPanel(c);
	    this.filterGridPanel = new Sbi.qbe.FilterGridPanel(c);
	    this.havingGridPanel = new Sbi.qbe.HavingGridPanel(c);
	    
	    this.centerRegionPanel = new Ext.Panel({ 
	    	title: LN('sbi.qbe.queryeditor.centerregion.title'),
	        region:'center',
	        autoScroll: true,
			containerScroll: true,
			layout: 'fit',
	        margins: '5 5 5 5',
	        tools:[{
	        	id:'save',
	        	qtip: LN('sbi.qbe.queryeditor.centerregion.tools.save'),
	        	handler: this.showSaveQueryWindow,
	        	scope: this
	        }, {
	          id:'saveView',
	          qtip: LN('sbi.qbe.queryeditor.centerregion.tools.view'),
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
	          qtip: LN('sbi.qbe.queryeditor.centerregion.tools.execute'),
	          handler: this.executeQuery,
	          scope: this
	        },{
	          id:'search',
	          qtip: LN('sbi.qbe.queryeditor.centerregion.tools.validate'),
	          // hidden:true,
	          handler: function(event, toolEl, panel){
	            // refresh logic
	          }
	        }, {
	          id:'help',
	          qtip: LN('sbi.qbe.queryeditor.centerregion.tools.help'),
	          handler: function(event, toolEl, panel){
	            // refresh logic
	          }
	        }],
	        
	        items: [this.selectGridPanel, this.filterGridPanel, this.havingGridPanel]
	    });
	    /*
	     * work-around for filters grids (where clause and having clause) resizing, since they are not automatically resized
	     * TODO: fix this problem with an Ext override
	    */ 
	    this.selectGridPanel.grid.on('resize', function (component, adjWidth, adjHeight, rawWidth, rawHeight) {
	    	if (this.filterGridPanel.grid.rendered) {
	    		var previousSize = this.filterGridPanel.grid.getSize();
		    	this.filterGridPanel.grid.setSize(adjWidth, previousSize.height);
	    	}
	    	if (this.havingGridPanel.grid.rendered) {
	    		var previousSize = this.havingGridPanel.grid.getSize();
		    	this.havingGridPanel.grid.setSize(adjWidth, previousSize.height);
	    	}
	    }, this);
	    
	    this.selectGridPanel.on('filter', function(panel, record) {
	    	filter = {
	    		leftOperandValue: record.data.id
				, leftOperandDescription: record.data.entity + ' : ' + record.data.field 
				, leftOperandType: 'Field Content'
			};
	    	this.filterGridPanel.addFilter(filter);
	    }, this);
	    
	    this.selectGridPanel.on('having', function(panel, record) {
	    	filter = {
	    		leftOperandValue: record.data.id
				, leftOperandDescription: record.data.entity + ' : ' + record.data.field 
				, leftOperandType: 'Field Content'
				, leftOperandAggregator: record.data.funct
			};
	    	this.havingGridPanel.addFilter(filter);
	    }, this);
	}
	
	
	
	, initEastRegionPanel: function(c) {
		
		this.queryCataloguePanel = new Sbi.qbe.QueryCataloguePanel({margins: '0 5 0 0', region: 'center'});
		this.documentParametersGridPanel = new Sbi.qbe.DocumentParametersGridPanel(
				{margins: '0 0 0 0', region: 'south'}
				, c.documentParametersStore
		);
		
		this.eastRegionPanel = new Ext.Panel({
	        title: LN('sbi.qbe.queryeditor.eastregion.title'),
	        region:'east',
	        width:250,
	        margins: '5 5 5 5',
	        layout:'border',
	        collapsible: true,
	        //collapseMode: 'mini',
	        collapseFirst: false,
	        collapsed: false,
	        tools:[
		        {
		          id:'delete',
		          qtip: LN('sbi.qbe.queryeditor.eastregion.tools.delete'),
		          // hidden:true,
		          handler: function(event, toolEl, panel){
		        	var q = this.queryCataloguePanel.getSelectedQuery();
		        	this.queryCataloguePanel.deleteQueries(q);
		          }, 
		          scope: this
		        }, {
		          id:'plus',
		          qtip:LN('sbi.qbe.queryeditor.eastregion.tools.add'),
		          // hidden:true,
		          handler: function(event, toolEl, panel){
		        	this.queryCataloguePanel.addQuery();
		          },
		          scope: this
		        }, {
			      id:'plus',
			      qtip:LN('sbi.qbe.queryeditor.eastregion.tools.insert'),
			      // hidden:true,
			      handler: function(event, toolEl, panel){
		        	var q = this.queryCataloguePanel.getSelectedQuery();
			        this.queryCataloguePanel.insertQuery(q);
			      },
			      scope: this
			   }
		    ],
	        items: [this.queryCataloguePanel, this.documentParametersGridPanel]
	    });
		
		
		this.queryCataloguePanel.on('beforeselect', function(panel, newquery, oldquery){
			// save changes applied to old query before to move to the new selected one
			this.applyChanges(); 
			this.setQuery( newquery );
			// required in order to be sure to have all query stored at the server side while
			// joining a subquery to a parent query selected entity
			this.queryCataloguePanel.commit(function() {
				// do nothings after commit for the moment
				// todo: implement hidingMask in order to block edinting while commiting
			}, this);
		}, this);
	}
	
	
	, addNodeToSelectGrid: function(node, recordBaseConfig) {
		if(node.attributes) {
    		if(node.attributes.type == 'field') {
			    
    			var field = {
			    	id: node.id,
			    	type: this.selectGridPanel.DATAMART_FIELD,
			    	entity: node.attributes.entity, 
			    	field: node.attributes.field,
			    	alias: node.attributes.field  
			    };				    	
			    this.selectGridPanel.addField(field);
			    
    		} else if(node.attributes.type == 'calculatedField') {	
 	    		var field = {
 	    			id: node.attributes.formState,
 	    			type: this.selectGridPanel.CALCULATED_FIELD,
 	    			entity: node.parentNode.text, 
			    	field: node.text,
 			        alias: node.text
 			    };
 	    		this.selectGridPanel.addField(field);
 	    		

 	    		var seeds =  Sbi.qbe.CalculatedFieldWizard.getUsedItemSeeds('dmFields', node.attributes.formState.expression);
 	    		for(var i = 0; i < seeds.length; i++) {
 	    			var n = node.parentNode.findChildBy(function(childNode) {
 	    				return childNode.id === seeds[i];
 	    			});
 	    			
 	    			if(n) {
 	    				this.dataMartStructurePanel.fireEvent('click', this.dataMartStructurePanel, n);
 	    			} else {
 	    				alert('node  [' + seeds + '] not contained in entity [' + node.parentNode.text + ']');
 	    			}
 	    			
 	    			
 	    		}
 	    		
    		}
		 }
	}
		
});