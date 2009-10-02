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
		title: 'Datamart 1'
		, rootNodeText: 'Datamart'
		, ddGroup: 'gridDDGroup'
		, type: 'datamartstructuretree'
		, preloadTree: true
		, baseParams: {}
	}, config || {});
	
	Ext.apply(this, c);
	
	this.services = new Array();
	var params = {};
	
	this.services['loadTree'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'GET_TREE_ACTION'
		, baseParams: params
	});

	this.services['getParameters'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'GET_PARAMETERS_ACTION'
		, baseParams: params
	});
	
	this.services['getAttributes'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'GET_ATTRIBUTES_ACTION'
		, baseParams: params
	});
	
	this.addEvents('load', 'click');
	
	this.initTree(c);
	
	Ext.apply(c, {
		title: this.title
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
	, preloadTree: true
	, tree: null
	, type: null
	, calculatedFieldWizard : null
	
	// --------------------------------------------------------------------------------
	// public methods
	// --------------------------------------------------------------------------------
	
	, load: function(params) {
		if(params) {
			this.treeLoader.baseParams = params;
		}
		this.tree.setRootNode(this.createRootNode());
	}

	, expandAll: function() {
		this.tree.expandAll();
	}
	
	, collapseAll: function() {
		this.tree.collapseAll();
	}
	
	, addCalculatedField: function() {
		var selectNode;
		
		if(this.calculatedFieldWizard === null) {
			this.initCalculatedFieldWizard();
		}
		selectNode = this.tree.getSelectionModel().getSelectedNode();
		var type = selectNode.attributes.type || selectNode.attributes.attributes.type;
		var text = selectNode.text || selectNode.attributes.text;
		if(type === 'entity') {
			var fields = new Array();
			for(var i = 0; i < selectNode.attributes.children.length; i++) {
				var child = selectNode.attributes.children[i];
				var childType = child.attributes.type || child.attributes.attributes.type;
				if(childType === 'field') {
					var field = {
						uniqueName: child.id,
						alias: child.text,
						text: child.attributes.field, 
						qtip: child.attributes.entity + ' : ' + child.attributes.field, 
						type: 'field', 
						value: 'dmFields[\'' + child.id + '\']'
					};	
					fields.push(field);
				}
			}
			this.calculatedFieldWizard.validationService.params = {fields: Ext.util.JSON.encode(fields)};
		
			this.calculatedFieldWizard.setExpItems('fields', fields);
		}
		
		this.calculatedFieldWizard.setTargetNode(selectNode);
		
		this.calculatedFieldWizard.show();
	}
	
	// --------------------------------------------------------------------------------
	// private methods
	// --------------------------------------------------------------------------------
	
	, createRootNode: function() {		
		var node = new Ext.tree.AsyncTreeNode({
	        text		: this.rootNodeText,
	        iconCls		: 'database',
	        expanded	: true,
	        draggable	: false
	    });
		return node;
	}
	
	, initTree: function(config) {
		
		this.treeLoader = new Ext.tree.TreeLoader({
	        baseParams: this.baseParams || {},
	        dataUrl: this.services['loadTree']
	    });
		this.treeLoader.on('load', this.oonLoad, this);
		this.treeLoader.on('loadexception', this.oonLoadException, this);
		
		this.rootNode = this.createRootNode();
		
		this.tree = new Ext.tree.TreePanel({
	        collapsible: true,
	        
	        enableDD: true,
	        ddGroup: this.ddGroup,
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
	        preloadTree		 : this.preloadTree,
	        root 			 : this.rootNode
	    });	
		
		this.tree.type = this.type;
		
		this.tree.on('click', function(node) {this.fireEvent('click', this, node);}, this);
	}
	
	, initCalculatedFieldWizard: function() {
		
		var fields = new Array();
		
		var parametersLoader = new Ext.tree.TreeLoader({
	        baseParams: {},
	        dataUrl: this.services['getParameters']
	    });
		
		var attributesLoader = new Ext.tree.TreeLoader({
	        baseParams: {},
	        dataUrl: this.services['getAttributes']
	    });
		
		var crossFn = Ext.util.Format.htmlEncode("String label = 'bestByRegion';") + '<br>' + 
			Ext.util.Format.htmlEncode("String text= fields['salesRegion'];") + '<br>' + 
			Ext.util.Format.htmlEncode("String params= 'region=5';") + '<br>' + 
			Ext.util.Format.htmlEncode("String subobject;") + '<p>' + 
			Ext.util.Format.htmlEncode("String result = '';") + '<p>' + 
			Ext.util.Format.htmlEncode("result +='<a href=\"#\" onclick=\"javascript:sendMessage({';") + '<br>' + 
			Ext.util.Format.htmlEncode("result +='\\'label\\':\\'' + label + '\\'';") + '<br>' + 
			Ext.util.Format.htmlEncode("result +=', parameters:\\'' + params + '\\'';") + '<br>' + 
			Ext.util.Format.htmlEncode("result +=', windowName: this.name';") + '<br>' + 
			Ext.util.Format.htmlEncode("if(subobject != null) result +=', subobject:\\'' + subobject +'\\'';") + '<br>' + 
			Ext.util.Format.htmlEncode("result += '},\\'crossnavigation\\')\"';") + '<br>' + 
			Ext.util.Format.htmlEncode("result += '>' + text + '</a>';") + '<p>' + 
			Ext.util.Format.htmlEncode("return result;");
		
		var functions = [
		    {
			    text: 'link'
			    , qtip: 'create a link to external web page'
			    , type: 'function'
			    , value: Ext.util.Format.htmlEncode('\'<a href="${URL}">\' + ${LABEL} + \'</a>\'')
		    }, {
			    text: 'image' , 
			    qtip: 'include an external image'
			    , type: 'function'
			    , value: Ext.util.Format.htmlEncode('\'<img src="${URL}"></img>\'')
		    }, {
			    text: 'cross-navigation'
			    , qtip: 'create a cross navigation link'
			    , type: 'function'
			    , value: crossFn
		    }
		];
		
		
		//this.treeLoader.on('load', this.oonLoad, this);
		//this.treeLoader.on('loadexception', this.oonLoadException, this);
		
		this.calculatedFieldWizard = new Sbi.qbe.CalculatedFieldWizard({
    		title: 'Calculated Field Wizard',
    		expItemGroups: [
    		    {name:'fields', text: 'Fileds'}, 
    		    {name:'parameters', text: 'Parameters', loader: parametersLoader}, 
    		    {name:'attributes', text: 'Attributes', loader: attributesLoader},
    		    {name:'functions', text: 'Functions'}
    		],
    		fields: fields,
    		functions: functions,
    		validationService: {
				serviceName: 'VALIDATE_EXPRESSION_ACTION'
				, baseParams: {contextType: 'datamart'}
				, params: null
			}
    	});

    	this.calculatedFieldWizard.on('apply', function(win, formState, targetNode){
    		
      		//formState.type = 'calculatedField';
    		var node = new Ext.tree.TreeNode({
    			text: formState.alias,
    			leaf: true,
    			type: 'calculatedField',
    			formState: formState, 
    			iconCls: 'calculation'
    		});
			//Ext.apply(node.attributes, formState);
    		
			if (!targetNode.isExpanded()) {
    			targetNode.expand(false, true, function() {targetNode.appendChild( node );});
    		} else {
    			targetNode.appendChild( node );
    		}
    		
    	}, this);
	}
	
	, oonLoad: function(treeLoader, node, response) {
		this.rootNode = this.tree.root;
		this.fireEvent('load', this, treeLoader, node, response);
	}
	
	, oonLoadException: function(treeLoader, node, response) {
		Sbi.exception.ExceptionHandler.handleFailure(response, treeLoader.baseParams || {});
	}
});