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
           , tbar: this.tbSave
		   , items: this.tabItems
		});
 	   this.addBtn = new Ext.Toolbar.Button({
           text: LN('sbi.generic.add'),
	            iconCls: 'icon-add',
	            handler: this.addNewItem,
	            width: 30,
	            scope: this
	            });
	   this.deleteBtn = new Ext.Toolbar.Button({
          text: LN('sbi.generic.delete'),
	            iconCls: 'icon-remove',
	            handler: this.deleteItem,
	            width: 30,
	            scope: this
	            });
	   
 	    this.tb = new Ext.Toolbar({
 	    	buttonAlign : 'left',
 	    	items:[this.addBtn, 
 	    	       //{xtype: 'tbspacer'}, 
 	    	       this.deleteBtn	]
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
           tbar: this.tb,
           collapsible:true,
           shadow:true,
           root: {
               nodeType: 'async',
               text: this.rootNodeText,
               id: this.rootNodeId
           }
	   		
	    });
 	   
 	  this.mainTree.addListener('click',this.fillDetail,this );
 	  this.mainTree.addListener('render',this.renderTree,this );
 	  this.mainTree.addListener('beforeload',this.styleTree,this );

 	  this.mainTree.on('append', function(tree, p, node){
 		 //alert("appended");
 		//node.select.defer(100, node);

 	  });

 	  /*form fields editing*/
 	  this.detailFieldName.addListener('change',this.editNode,this);
 	  this.detailFieldCode.addListener('change',this.editNode,this);
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
   	               width: 200,
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

	,save : function() {		
		alert('Abstract Method: it needs to be overridden');
    }
	, fillDetail : function(node, e) {	
		var val = node.text;
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
	, renderTree: function(tree) {		
		tree.getLoader().nodeParameter = 'id';
		tree.getRootNode().expand(false, /*no anim*/ false);


    }
	, styleTree: function(node){	
		//Ext.fly(this.mainTree.getEl()).frame("ff0000");
/*
 		if(node.attributes.kpi === undefined || node.attributes.kpi == null ){ 
 			node.cls ="tree-model-nokpi";
 			
 		}else{
 			node.cls ="tree-model-kpi";
 		}*/
 	}
	, editNode: function(field, newVal, oldVal) {		
		var node = this.mainTree.getSelectionModel().getSelectedNode();
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
			
			this.mainTree.render();
		}
    }
	, addNewItem : function(){

		var parent = this.mainTree.getSelectionModel().getSelectedNode();
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
	, deleteItem : function(){
		var node = this.mainTree.getSelectionModel().getSelectedNode();
		if(node === undefined || node == null){
			alert("Select node to delete");
			return;
		}
		node.remove();
		this.mainTree.render();
	}
});
