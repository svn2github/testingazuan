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
	this.services['loadCatalogue'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'GET_CATALOGUE_ACTION'
		, baseParams: params
	});
	this.services['addQuery'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'ADD_QUERY_ACTION'
		, baseParams: params
	});
	
	this.services['selectQuery'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'SELECT_QUERY_ACTION'
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

    , addQuery: function() {   	
    	Ext.Ajax.request({
		   	url: this.services['addQuery'],
		   	success: function(response, options) {
    			if(response !== undefined && response.responseText !== undefined) {
					var content = Ext.util.JSON.decode( response.responseText );
					var node = new Ext.tree.TreeNode(content);
					node.attributes = content.attributes;
					this.rootNode.appendChild( node );
				} else {
			      		Sbi.exception.ExceptionHandler.showErrorMessage('Server response is empty', 'Service Error');
			    }     					
   			},
   			failure: Sbi.exception.ExceptionHandler.handleFailure,
   			scope: this
		});	   
    }
    
    , getQueries: function() {
    	var items = [];
    	if( this.rootNode.childNodes && this.rootNode.childNodes.length > 0 ) {
			for(var i = 0; i < this.rootNode.childNodes.length; i++) {
				items.push(this.rootNode.childNodes[i].attributes);
			}
		}
    	
    	return items;
    }
    
    , getSelectedQuery: function() {
    	var selectedNode;    	
    	selectedNode = this.tree.getSelectionModel().getSelectedNode();
    	return selectedNode && selectedNode.attributes? selectedNode.attributes.query : undefined;
    }
    
    , synchronizeSelectedQuery: function(query) {
    	var selectedNode;    	
    	selectedNode = this.tree.getSelectionModel().getSelectedNode();
    	if(selectedNode) selectedNode.attributes.query = query;
    }

	
	// private methods
	
	, initTree: function(config) {
		
		this.treeLoader = new Ext.tree.TreeLoader({
	        dataUrl: this.services['loadCatalogue']
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