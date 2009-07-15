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

Sbi.qbe.DataMartStructurePanel = function(config) {
	var c = Ext.apply({
		// set default values here
	}, config || {});
	
	this.services = new Array();
	var params = {};
	this.services['loadTree'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'GET_TREE_ACTION'
		, baseParams: params
	});
	
	this.addEvents('click');
	
	this.initTree(c);
	
	Ext.apply(c, {
		title:'Datamart 1'
		, layout: 'fit'
		, border:false
		, autoScroll: true
		, containerScroll: true
		, items: [this.tree]
	});
	
	
	// constructor
	Sbi.qbe.DataMartStructurePanel.superclass.constructor.call(this, c);
    
    
};

Ext.extend(Sbi.qbe.DataMartStructurePanel, Ext.Panel, {
    
	services: null
	, treeLoader: null
	, rootNode: null
	, tree: null
	
	// public methods
	
	, load: function() {
		this.treeLoader.load(this.rootNode, function(){});
	}

	/*
	, selectNode: function(node) {
		this.fireEvent('click', this, node);
		if(node.attributes.field && node.attributes.type == 'field') {
		    var record = new QueryBuilderPanel.selectGridPanel.Record({
		    	 id: node.id,
		         entity: node.attributes.entity , 
		         field: node.attributes.field  
		      });
		      
		    QueryBuilderPanel.selectGridPanel.addRow(record); 
		 }
	}
	*/
	
	// private methods
	
	, initTree: function(config) {
		
		this.treeLoader = new Ext.tree.TreeLoader({
	        baseParams:{DATAMART_NAME: 'xxx'},
	        dataUrl: this.services['loadTree']
	    });
		
		this.rootNode = new Ext.tree.AsyncTreeNode({
	        text		: 'Datamart',
	        iconCls		: 'database',
	        expanded	: true,
	        draggable	: false
	    });
		
		this.tree = new Ext.tree.TreePanel({
	        collapsible: true,
	        
	        enableDD: true,
	        ddGroup: 'gridDDGroup',
	        dropConfig: {
				isValidDropPoint : function(n, pt, dd, e, data){
					return false;
				}      
	      	},
	      	
	      	
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
		
		//this.tree.setRootNode(this.rootNode);
		
		this.tree.on('click', function(node) {this.fireEvent('click', this, node);}, this);
	}
});