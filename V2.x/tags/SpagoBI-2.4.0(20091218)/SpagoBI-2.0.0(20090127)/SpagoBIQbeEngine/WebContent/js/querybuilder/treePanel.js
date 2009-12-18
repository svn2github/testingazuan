/**
  * Qbe - Tree Component
  * by Andrea Gioia
  */
  
  
Ext.override(Ext.tree.TreeLoader, {
    createNode : function(attr){
    	//alert("Hello");
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
        	//alert(attr.attributes.field);
        } else {
        	resultNode = new Ext.tree.AsyncTreeNode(attr);
        }
        
        return resultNode;
        
        //return(resultNode ? resultNode: new Ext.tree.AsyncTreeNode(attr));
    }
}) ; 
  
  
Ext.namespace('it.eng.spagobi.engines.qbe.querybuilder.treePanel');


// shorthand alias
var treePanelComponent = it.eng.spagobi.engines.qbe.querybuilder.treePanel;
 
// public variables goes here. Do not use them before invocation of method init.

var loadTreeDataServiceUrl = it.eng.spagobi.engines.qbe.serviceregistry.module.getServiceUrl('GET_TREE_ACTION');
/**
  * Initialize the treePanel Component.
  */
treePanelComponent.init = function() {
	
}

it.eng.spagobi.engines.qbe.querybuilder.treePanel.treePtr;
it.eng.spagobi.engines.qbe.querybuilder.treePanel.treeLoaderPtr;
it.eng.spagobi.engines.qbe.querybuilder.treePanel.rootNodePtr;

it.eng.spagobi.engines.qbe.querybuilder.treePanel.getFoodmartTreePanel = function() {
	
	var treeLoader = new Ext.tree.TreeLoader({
        baseParams:{DATAMART_NAME: 'xxx'},
        dataUrl   :loadTreeDataServiceUrl
    });
    if(!treePanelComponent.treeLoaderPtr) {
    	//alert('treeLoaderPtr');
    	treePanelComponent.treeLoaderPtr = treeLoader;
    }
    
   
    var tree = new Ext.tree.TreePanel({
        //title          : 'Datamart Tree',
        collapsible      : true,
        enableDD		 : true,
        dropConfig		: {
					        isValidDropPoint : function(n, pt, dd, e, data){
					          return false;
					        }      
      					  },
      	ddGroup			 : 'gridDDGroup',
        animCollapse     : true,
        collapseFirst	 : false,
        border           : false,
        //id             : "datamart_tree",
        autoScroll       : true,
        containerScroll  : true,
        animate          : false,
        trackMouseOver 	 : true,
        useArrows 		 : true,
        loader           : treeLoader
    });	
    if(!treePanelComponent.treePtr) {
    	//alert('treePtr');
    	treePanelComponent.treePtr = tree;
    }
    
    
    // SET the root node.
    
     
    var rootNode = new Ext.tree.AsyncTreeNode({
        text		: 'Datamart',
        iconCls		: 'database',
        expanded	: true,
        draggable	: false
        //id		: 'rootNode'
    });
    if(!treePanelComponent.rootNodePtr) {
    	//alert('rootNodePtr');
    	treePanelComponent.rootNodePtr = rootNode;
    }
    
 	
 	
    tree.setRootNode(rootNode);
    
    
	tree.addListener('click', it.eng.spagobi.engines.qbe.querybuilder.treePanel.selectNode);
    
    return tree;    
};


treePanelComponent.selectNode = function(node, e) {
  //alert(node.attributes.field);
  if(node.attributes.field && node.attributes.type == 'field') {
    var record = new it.eng.spagobi.engines.qbe.querybuilder.selectGrid.app.Record({
    	 id: node.id,
         entity: node.attributes.entity , 
         field: node.attributes.field  
      });
      
    it.eng.spagobi.engines.qbe.querybuilder.selectGrid.app.addRow(record); 
  }
};

treePanelComponent.refresh = function() {
	//alert('refresh + tree');
	this.treeLoaderPtr.load(this.rootNodePtr, function(){});
};

