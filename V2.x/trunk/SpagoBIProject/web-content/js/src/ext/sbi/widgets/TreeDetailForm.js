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
 * [list]
 * 
 * 
 * Public Events
 * 
 * [list]
 * 
 * Authors - Monica Franceschini
 */

Ext.ns("Sbi.widgets");

Sbi.widgets.TreeDetailForm = function(config) {

	var conf = config.configurationObject;
	this.services = new Array();
	this.services['manageTreeService'] = conf.manageTreeService;
	this.tabItems = conf.tabItems;
	this.rootNodeText = conf.root;
	this.rootNodeId = conf.rootId;
	this.treeTitle = conf.treeTitle;

	this.initWidget();
	this.initContextMenu();
	
	var c = Ext.apply({}, config, this.gridForm);
   	
   	Sbi.widgets.TreeDetailForm.superclass.constructor.call(this,c);	
};

Ext.extend(Sbi.widgets.TreeDetailForm, Ext.FormPanel, {
	
	gridForm:null	
	, tabs: null
	, tabItems: null

	, treeLoader: null
	, rootNode: null
	, rootNodeId: null
	, preloadTree: true
	, rootNodeText: null
	, treeTitle: null
	, menu: null
	
	, selectedNodeToEdit: null

	, initContextMenu: function() {
	
		 this.menu = new Ext.menu.Menu({
            id:'actions',
            items: [
            // ACID operations on nodes
            '-',{
           	 text:'Add Model Node',
                iconCls:'icon-add',
                handler:function(){
           	   		this.addNewItem(this.ctxNode);	         	 	
	             },
                scope: this
            },{
           	 text:'Remove Model Node',
                iconCls:'icon-remove',
                handler:  function() {
	            	 this.deleteItem(this.ctxNode);
                },
                scope: this
            }]
        });

	}	
	,initWidget: function(){

 	    this.tbSave = new Ext.Toolbar({
 	    	buttonAlign : 'right', 	    	
 	    	items:[new Ext.Toolbar.Button({
 	            text: LN('sbi.generic.update'),
 	            iconCls: 'icon-save',
 	            handler: this.save,
 	            width: 30,
 	            id: 'save-btn',
 	            scope: this
 	            })
 	    	]
 	    });

 	   this.tabs = new Ext.TabPanel({
           enableTabScroll : true
           , id: 'tab-panel'
           , activeTab : 0
           , columnWidth: 0.6
           , autoScroll : true
           , width: 450          
           , height: 490
           , itemId: 'tabs' 
		   , items: this.tabItems
		});



 	   this.mainTree =  new Ext.tree.TreePanel({
           title: this.treeTitle,
           width: 250,
           height: 230,
           userArrows: true,
           animate: true,
           autoScroll: true,
           dataUrl: this.services['manageTreeService'],
	       preloadTree	: this.preloadTree,
           enableDD	: true,
           dropConfig : {appendOnly:true},
           scope: this,
           collapsible:true,
           shadow:true,
           tbar: this.tbSave,
           root: {
               nodeType: 'async',
               text: this.rootNodeText,
               id: this.rootNodeId
           }
	   		
	    });
 	    this.setListeners();
 	    this.mainTree.on('contextmenu', this.onContextMenu, this);


   	   /*
   	   *    Here is where we create the Form
   	   */
   	  this.gridForm = new Ext.FormPanel({
   	          id: 'items-form',
   	          frame: true,
   	          autoScroll: true,
   	          labelAlign: 'left',
   	          title: this.panelTitle,
   	          width: 600,
   	          height: 550,
   	          //layout: 'column',
              layout:'border',
	          layoutConfig: {
		          titleCollapse: false,
		          animate: true,
		          activeOnTop: false

	          },
   	          trackResetOnLoad: true,
   	          items: [
   	              {
   	               region:'west',
   	               margins: '5 0 0 5',
   	               width: 300,
   	               collapsible: true,   // make collapsible
   	               cmargins: '5 5 0 5', // adjust top margin when collapsed
   	               columnWidth: 0.4,
   	               layout: 'fit',
   	               items: this.mainTree
   	              }, {
		   		    border: false
				    , frame: false
				    , collapsible: false
				    , collapsed: false
				    , hideCollapseTool: true
				    , titleCollapse: true
				    , collapseMode: 'mini'
				    , split: true
				    , region:'center',
       	           margins: '5 0 0 5',
       	           width: 200,
       	           collapsible: true,   // make collapsible
       	           cmargins: '5 5 0 5', // adjust top margin when collapsed
       	           columnWidth: 0.4,
       	           layout: 'fit',
       	           items: this.tabs
   	              }
   	          ]
   	      });
	}
	, setListeners: function(){
	 	  this.mainTree.getSelectionModel().addListener('selectionchange',this.fillDetail,this );
	 	  this.mainTree.addListener('render',this.renderTree,this );

	 	  this.mainTree.addListener('beforeappend', this.styleTree, this);

	 	  /*form fields editing*/
	 	  this.detailFieldName.addListener('focus',this.selectNode,this);
	 	  this.detailFieldName.addListener('change',this.editNode,this);
	 	  
	 	  this.detailFieldCode.addListener('focus',this.selectNode,this);
	 	  this.detailFieldCode.addListener('change',this.editNode,this);
	 	  
	 	 this.detailFieldDescr.addListener('focus',this.selectNode,this);
	 	  this.detailFieldDescr.addListener('change',this.editDescr,this);
	 	  
	 	  this.detailFieldLabel.addListener('focus',this.selectNode,this);
	 	  this.detailFieldLabel.addListener('change',this.editLabel,this);
	 	  
	 	 this.detailFieldTypeDescr.addListener('focus',this.selectNode,this);
	 	  this.detailFieldTypeDescr.addListener('change',this.editTypeDescr,this);
	 	  
	 	 this.detailFieldKpi.addListener('focus',this.selectNode,this);
	 	  this.detailFieldKpi.addListener('change',this.editKpi,this);
	 	  
	 	 this.detailFieldNodeType.addListener('focus',this.selectNode,this);
	 	  this.detailFieldNodeType.addListener('change',this.editType,this);
	}
	,save : function() {		
		alert('Abstract Method: it needs to be overridden');
    }
	, fillDetail : function(sel, node) {	
		
		var val = node.text;
		if(val != null && val !== undefined){
			var aPosition = val.indexOf(" - ");
			
			var name = node.attributes.name;
			var code =	node.attributes.code;
			if(aPosition !== undefined && aPosition != -1){
				name = val.substr(aPosition + 3);
				code = val.substr(0 , aPosition)
			}
	
			this.detailFieldDescr.setValue(node.attributes.description);
			this.detailFieldTypeDescr.setValue(node.attributes.typeDescr);
			this.detailFieldLabel.setValue(node.attributes.label);
			this.detailFieldKpi.setValue(node.attributes.kpi);
			this.detailFieldNodeType.setValue(node.attributes.type);
	
			this.detailFieldName.setValue(name);
			this.detailFieldCode.setValue(code);
		}
    }
	, renderTree: function(tree) {		
		tree.getLoader().nodeParameter = 'id';
		tree.getRootNode().expand(false, /*no anim*/ false);
    }
	, styleTree: function(tree, thisNode, node){
 		if(node.attributes.kpi !== undefined 
 				&& node.attributes.kpi != null 
 				&& node.attributes.kpi != ''){ 
 			node.attributes.iconCls = 'has-kpi';	
 		}
 		//node.attributes.cls = 'has-error';
	}
	, editNode: function(field, newVal, oldVal) {
		var node = this.selectedNodeToEdit;
		
		if(node !== undefined){

			var val = node.text;
			var aPosition = val.indexOf(" - ");
			var name = "";
			var code =	"";
			if(aPosition !== undefined && aPosition != -1){
				name = val.substr(aPosition + 3);
				code = val.substr(0 , aPosition);
				//replace text
				if(field.getName() == 'name'){
					name = newVal;
				}else if(field.getName() == 'code'){
					code = newVal;
				}
			}
			var text = code +" - "+name;
			node.setText( text);
			//this.mainTree.render();
		}
    }
	, editKpi: function(field, newVal, oldVal) {	
		var node = this.selectedNodeToEdit;
		if(node !== undefined && node !==  null){
			node.attributes.toSave = true;
			node.attributes.kpi = newVal;	
		}
    }
	, editType: function(field, newVal, oldVal) {	
		var node = this.selectedNodeToEdit;
		if(node !== undefined && node !==  null){
			node.attributes.toSave = true;
			node.attributes.type = newVal;	
		}
    }
	, editTypeDescr: function(field, newVal, oldVal) {	
		var node = this.selectedNodeToEdit;
		if(node !== undefined && node !==  null){
			node.attributes.toSave = true;
			node.attributes.typeDescr = newVal;	
		}
    }
	, editDescr: function(field, newVal, oldVal) {	
		var node = this.selectedNodeToEdit;
		if(node !== undefined && node !==  null){
			node.attributes.toSave = true;
			node.attributes.description = newVal;	
		}
    }
	, editLabel: function(field, newVal, oldVal) {	
		var node = this.selectedNodeToEdit;
		if(node !== undefined && node !==  null){
			node.attributes.toSave = true;
			node.attributes.description = newVal;	
		}
    }
	, selectNode: function(field) {	
		this.selectedNodeToEdit = this.mainTree.getSelectionModel().getSelectedNode();
		
    }
	, addNewItem : function(parent){
		if(parent === undefined || parent == null){
			alert("Select parent node");
			return;
		}else{
			parent.leaf=false;
		}
		
		var node = new Ext.tree.TreeNode({
            text:'... - ...',
            leaf:true,
            cls: 'image-node',
            allowDrag:false
        });
		this.mainTree.render();
		if(!parent.isExpanded()){
			parent.expand(false, /*no anim*/ false);
		}
		this.mainTree.render();
		parent.appendChild(node);

		this.mainTree.fireEvent('click', node);
	}
	, deleteItem : function(node){

		if(node === undefined || node == null){
			alert("Select node to delete");
			return;
		}
		node.remove();
		this.mainTree.render();
	}
	, onContextMenu : function(node, e){
        if(this.menu == null){ // create context menu on first right click
        	this.initContextMenu();
        }
        
        if(this.ctxNode){
            this.ctxNode.ui.removeClass('x-node-ctx');
            this.ctxNode = null;
        }
        
        this.ctxNode = node;
        this.ctxNode.ui.addClass('x-node-ctx');
        this.menu.showAt(e.getXY());

    }
});
