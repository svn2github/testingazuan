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
  * - Andrea Gioia (adrea.gioia@eng.it)
  */

Ext.ns("Sbi.qbe");

Sbi.qbe.QueryCataloguePanel = function(config) {
	var c = Ext.apply({
		// set default values here
	}, config || {});
	
	this.services = new Array();
	var params = {};
	this.services['getCatalogue'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'GET_CATALOGUE_ACTION'
		, baseParams: params
	});
	
	this.services['setCatalogue'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'SET_CATALOGUE_ACTION'
		, baseParams: params
	});
	
	this.services['addQuery'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'ADD_QUERY_ACTION'
		, baseParams: params
	});
	
	this.services['deleteQueries'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'DELETE_QUERIES_ACTION'
		, baseParams: params
	});
	
	
	this.addEvents('beforeselect');
	
	this.initTree(c);
	
	Ext.apply(c, {
		layout: 'fit'
		, border:false
		, autoScroll: true
		, containerScroll: true
		, items: [this.tree]
	});
	
	
	// constructor
	Sbi.qbe.QueryCataloguePanel.superclass.constructor.call(this, c);
    
    
};

Ext.extend(Sbi.qbe.QueryCataloguePanel, Ext.Panel, {
    
	services: null
	, treeLoader: null
	, rootNode: null
	, tree: null
	
	// public methods
	
	, load: function() {
		this.treeLoader.load(this.rootNode, function(){});
	}
     
	
	, setQuery: function(queryItemId, query) {
		var oldQuery;
		var item = this.getQueryItemById(queryItemId);
		if(item) {
			oldQuery = item.query;
			item.query = query;
		}
		
		return oldQuery;
	}
	
	, getQuery: function(queryItemId) {
		var item = this.getQueryItemById(queryItemId);
		return item? item.query : undefined;
	}
	
	, setMeta: function(queryItemId, meta) {
		var oldMeta;
		var item = this.getQueryItemById(queryItemId);
		if(item) {
			oldMeta = item.meta;
			item.meta = meta;
		}
		
		return oldMeta;
	}
	
	
	, getMeta: function(queryItemId, meta) {
		var item = this.getQueryItemById(queryItemId);
		return item? item.meta : undefined;
	}


	, getQueryItems: function() {
		var queryItems = [];
    	if( this.rootNode.childNodes && this.rootNode.childNodes.length > 0 ) {
			for(var i = 0; i < this.rootNode.childNodes.length; i++) {
				queryItems.push( this.getQueryItemById(this.rootNode.childNodes[i].id) );
			}
		}
    	
    	return queryItems;
	}

	, getQueryItemById: function(queryId) {
		var queryItem;
		var queryNode = this.tree.getNodeById(queryId);
		
		if(queryNode) {
			
			queryItem = queryNode.attributes;
			queryItem.suqueries = [];
			if( this.queryNode.childNodes && this.queryNode.childNodes.length > 0 ) {
				for(var i = 0; i < this.queryNode.childNodes.length; i++) {
					var subquery = this.getQueryItemById( this.queryNode.childNodes[i].id );
					queryItems.suqueries.push( subquery );
				}
			}
		}
		
		return queryItem;
	}
	
	, getSelectedQueryItem: function() {
		var queryNode = this.tree.getSelectionModel().getSelectedNode();
		return queryNode? queryNode.attributes: undefined;
	}

	, addQueryItem: function(queryItem) {
		this.insertQueryItem(this.rootNode.id, queryItem);
	}

	, insertQueryItem: function(parentQueryItem, queryItem) {
		var nodeId = (typeof parentQueryItem === 'string')? parentQueryItem: parentQueryItem.meta.id;
		var parentQueryNode = this.tree.getNodeById(nodeId);
		 
		if(!queryItem) {
			this.createQueryNode(this.insertQueryNode.createDelegate(this, [parentQueryNode], 0), this);
		} else {
			 var queryNode = {
			    id: queryItem.meta.id
			   	, text: queryItem.meta.id
			   	, leaf: true
			   	, attributes: {
			 		query: queryItem.query
			 		, meta: queryItem.meta
			 		, iconCls: 'icon-query'
			    }
			 };
			 this.insertQueryNode(parentQueryNode, queryNode);			 
		}
	}
	
	, deleteQueryItems: function(queries) {
		this.deleteQueryNodes(queries);
	}
	
	
	, commit: function(callback, scope) {
		var params = {
				catalogue: Ext.util.JSON.encode(this.getQueryItems())
		};
		
		Ext.Ajax.request({
		    url: this.services['setCatalogue'],
		    success: callback,
		    failure: Sbi.exception.ExceptionHandler.handleFailure,	
		    scope: scope,
		    params: params
		});   
	}
	
	
	// private 
	
	, addQueryNode: function(queryNode) {
		this.insertQueryNode(this.rootNode, queryNode);
	}
	
	, insertQueryNode: function(parentQueryNode, queryNode) {
		if(!queryNode) {
			this.createQueryNode(this.insertQueryNode.createDelegate(this, [parentQueryNode], 0), this);
		} else {			
			parentQueryNode.leaf = false;					
			parentQueryNode.appendChild( queryNode );
			parentQueryNode.expand();
			queryNode.select();		
			
			var te = this.treeEditor;
			var edit = function(){
                te.editNode = queryNode;
                te.startEdit(queryNode.ui.textNode);
            };
			setTimeout(edit, 10);
		}
	}
	
	, createQueryNode: function(callback, scope) {
		Ext.Ajax.request({
		   	url: this.services['addQuery'],
		   	success: function(response, options) {
    			if(response !== undefined && response.responseText !== undefined) {
					var content = Ext.util.JSON.decode( response.responseText );
					var queryNode = new Ext.tree.TreeNode(content);
					queryNode.attributes = content.attributes;
					callback.call(scope, queryNode);
				} else {
			      	Sbi.exception.ExceptionHandler.showErrorMessage('Server response is empty', 'Service Error');
			    }     					
   			},
   			failure: Sbi.exception.ExceptionHandler.handleFailure,
   			scope: this
		});	   
	}
	
	, deleteQueryNodes: function(queries, callback, scope) {
		var p;
    	if(queries) {
    		if( !(queries instanceof Array) ) {
    			queries = [queries];
    		}
    		
    		for(var i = 0, p = []; i < queries.length; i++) {
    			var query = queries[i];
    			if(typeof query === 'string') {
    				p.push( query );
    			} else if(typeof query === 'object') {
    				p.push( query.id || query.meta.id );
    			} else {
    				alert('Invalid type [' + (typeof query) + '] for object query in function [deleteQueries]');
    			}
    		}
    		
			Ext.Ajax.request({
			   	url: this.services['deleteQueries'],
			   	params: {queries: Ext.util.JSON.encode(p)},
			
			   	success: function(response, options) {
			   		var q = Ext.util.JSON.decode( options.params.queries );
			   		for(var i = 0; i < q.length; i++) {
			   			var node = this.tree.getNodeById(q[i]);
			   			node.remove();
			   		}
			   		
			   		if(callback) callback.call(scope, q);
	   			},
	   			failure: Sbi.exception.ExceptionHandler.handleFailure,
	   			scope: this
			});	
    	}
	}
		
	
	
	
	
	
	
	
	
	
	/*
    , addQuery: function() {   	
    	
    	Ext.Ajax.request({
		   	url: this.services['addQuery'],
		   	success: function(response, options) {
    			if(response !== undefined && response.responseText !== undefined) {
					var content = Ext.util.JSON.decode( response.responseText );
					var node = new Ext.tree.TreeNode(content);
					node.attributes = content.attributes;
					this.rootNode.appendChild( node );
					this.tree.getSelectionModel().select(node);
					
					var te = this.treeEditor;
					var edit = function(){
	                    te.editNode = node;
	                    te.startEdit(node.ui.textNode);
	                };
					setTimeout(edit, 10);
				} else {
			      		Sbi.exception.ExceptionHandler.showErrorMessage('Server response is empty', 'Service Error');
			    }     					
   			},
   			failure: Sbi.exception.ExceptionHandler.handleFailure,
   			scope: this
		});	  
		
    	
    	this.addQueryItem();
    }
    */
	/*
    , insertQuery: function() {   	
    	
    	Ext.Ajax.request({
		   	url: this.services['addQuery'],
		   	success: function(response, options) {
    			if(response !== undefined && response.responseText !== undefined) {
					var content = Ext.util.JSON.decode( response.responseText );
					var node = new Ext.tree.TreeNode(content);
					node.attributes = content.attributes;
					var selectedNode = this.tree.getSelectionModel().getSelectedNode();
					selectedNode.leaf = false;					
					if(selectedNode) {
						selectedNode.appendChild( node );
						selectedNode.expand();
					}
					else this.rootNode.appendChild( node );
					
					node.select();					
				} else {
			      		Sbi.exception.ExceptionHandler.showErrorMessage('Server response is empty', 'Service Error');
			    }     					
   			},
   			failure: Sbi.exception.ExceptionHandler.handleFailure,
   			scope: this
		});
		  
    	this.insertQueryNode(this.tree.getSelectionModel().getSelectedNode() || this.rootNode);
    }
    
    */	 
    
    , getQueries: function() {
    	var items = [];
    	if( this.rootNode.childNodes && this.rootNode.childNodes.length > 0 ) {
			for(var i = 0; i < this.rootNode.childNodes.length; i++) {
				items.push(this.rootNode.childNodes[i].attributes);
			}
		}
    	
    	return items;
    }
    
 
    
    , synchronizeSelectedQuery: function(query) {
    	var selectedNode;    	
    	selectedNode = this.tree.getSelectionModel().getSelectedNode();
    	if(selectedNode) selectedNode.attributes.query = query;
    }
    
    
    /*
    , deleteQueries: function(queries) {
    	var p;
    	if(queries) {
    		if( !(queries instanceof Array) ) {
    			queries = [queries];
    		}
    		
    		for(var i = 0, p = []; i < queries.length; i++) {
    			var query = queries[i];
    			if(typeof query === 'string') {
    				p.push( query );
    			} else if(typeof query === 'object') {
    				p.push( query.id );
    			} else {
    				alert('Invalid type [' + (typeof query) + '] for object query in function [deleteQueries]');
    			}
    		}
    		
    		Ext.Ajax.request({
    		   	url: this.services['deleteQueries'],
    		   	params: {queries: Ext.util.JSON.encode(p)},
    		
    		   	success: function(response, options) {
    		   		var q = Ext.util.JSON.decode( options.params.queries );
    		   		for(var i = 0; i < q.length; i++) {
    		   			var node = this.tree.getNodeById(q[i]);
    		   			node.remove();
    		   			//this.rootNode.removeChild(node);
    		   		}
       			},
       			failure: Sbi.exception.ExceptionHandler.handleFailure,
       			scope: this
    		});	
    	}
    }
	*/
	
	// private methods
	
	, initTree: function(config) {
		
		this.treeLoader = new Ext.tree.TreeLoader({
	        dataUrl: this.services['getCatalogue']
	    });
		
		/*
		this.treeLoader.createNode = function(attr){
	        
			// apply baseAttrs, nice idea Corey!
	        if(this.baseAttrs){
	            Ext.applyIf(attr, this.baseAttrs);
	        }
	        if(this.applyLoader !== false){
	            attr.loader = this;
	        }
	        if(typeof attr.uiProvider == 'string'){
	           attr.uiProvider = this.uiProviders[attr.uiProvider] || eval(attr.uiProvider);
	        }
	        
	        var resultNode;
	        if(attr.leaf) {
	        	resultNode = new Ext.tree.TreeNode(attr);
	        	resultNode.attributes = attr.attributes;
	        } else {
	        	resultNode = new Ext.tree.AsyncTreeNode(attr);
	        }
	        
	        return resultNode;
	    }
	    */
		
		
		this.rootNode = new Ext.tree.AsyncTreeNode({
	        text		: 'Queries',
	        iconCls		: 'database',
	        expanded	: true,
	        draggable	: false
	    });
		
		this.tree = new Ext.tree.TreePanel({
	        collapsible: true,
	        
	        enableDD: false,
	        /*
	        ddGroup: 'gridDDGroup',
	        dropConfig: {
				isValidDropPoint : function(n, pt, dd, e, data){
					return false;
				}      
	      	},
	      	*/
	      	
	        animCollapse     : true,
	        collapseFirst	 : false,
	        border           : false,
	        autoScroll       : true,
	        containerScroll  : true,
	        animate          : false,
	        trackMouseOver 	 : true,
	        useArrows 		 : true,
	        loader           : this.treeLoader,
	        root 			 : this.rootNode
	    });	
		
		// add an inline editor for the nodes
	    this.treeEditor = new Ext.tree.TreeEditor(this.tree, {/* fieldconfig here */ }, {
	        allowBlank:false,
	        blankText:'A name is required',
	        selectOnFocus:true
	    });
		
		this.tree.getSelectionModel().on('beforeselect', this.onSelect, this);
		this.treeLoader.on('load', this.onLoad, this);
	}
	
	, onLoad: function(loader, node, response) {
		node.expand();
		
		if( node.childNodes && node.childNodes.length > 0 ) {
			this.tree.getSelectionModel().select( node.childNodes[0] );
		}
	}
	
	, onSelect: function(sm, newnode, oldnode) {
		var allowSelection = true;
		
		if(newnode.id !== this.rootNode.id) {
			var oldquery = oldnode?  oldnode.attributes.query: undefined;
			var b = this.fireEvent('beforeselect', this, newnode.attributes.query, oldquery);
			if(b === false) allowSelection = b;
		} else {
			allowSelection = false;
		}
		
		return allowSelection;
	}
	
});