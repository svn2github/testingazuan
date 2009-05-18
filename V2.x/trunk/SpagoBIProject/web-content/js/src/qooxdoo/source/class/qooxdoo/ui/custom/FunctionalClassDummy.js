qx.Class.define("qooxdoo.ui.custom.FunctionalClassDummy",
{
  extend : qx.ui.splitpane.Pane,
  
  construct : function(type)
  {
   
    this.base(arguments);	//,"1*","4*");//, "2*"); //  180 // this.base(arguments, "1*", "2*");
		   
 	
 	this.createTree();
 	var tree = this.getTree();	
 	
  	this.add(tree,0);
  	
  		
  	var rightPart = new qooxdoo.ui.custom.FunctionalityTreeSubClass();
  	this._right = rightPart;
  	
  	this.createRightSideToolbar();
  	this.createRightSideForm();
  	
  	this.add(rightPart,1);
  
	var toolBar = rightPart.getUserData('toolBar');
	
	this._createButton = toolBar[0].getUserData('create');
	this._createButton.addListener("execute", this.createNode,this);
	
	this._saveButton = toolBar[1].getUserData('save');
	this._saveButton.addListener("execute", this.save,this);
	
	this._deleteButton = toolBar[2].getUserData('delete');
	this._deleteButton.addListener("execute", this.deleteNode,this);
	
	this._moveUpButton = toolBar[3].getUserData('moveUp');
	this._moveUpButton.addListener("execute",this.moveUp,this);
	
	this._moveDownButton = toolBar[4].getUserData('moveDown');
	this._moveDownButton.addListener("execute",this.moveDown,this);
	
	this._clearAllButton = toolBar[5].getUserData('clearAll');
	this._clearAllButton.addListener("execute",this.clearAll,this);
	
	//Set Focus of buttons only when Node selected
	this._createButton.setEnabled(false);
	this._saveButton.setEnabled(false);
	this._deleteButton.setEnabled(false);
	this._moveUpButton.setEnabled(false);
	this._moveDownButton.setEnabled(false);
	
	//this._right = rightPart;
  			
   //} 	
  },
  
  members :
  {
	  	//	 horSplit : undefined,
  	_tree : undefined,
  	_right: undefined,
  	treeFunction: undefined,
  	
  	_createButton 	: undefined,
  	_saveButton 	: undefined,
  	_deleteButton 	: undefined,
  	_moveUpButton 	: undefined,
  	_moveDownButton : undefined,
  	
  	/*Show Insert, Delete, Move Up and Move Down Buttons */
  	showButtons: function(){
  		if(this._tree.getSelectedItem() == this._tree.getRoot()){		// If Root Node
	        this._createButton.setEnabled(true);
	        this._saveButton.setEnabled(false);
  			this._deleteButton.setEnabled(false);
  			this._moveUpButton.setEnabled(false);
  			this._moveDownButton.setEnabled(false);
  		return;
  			
	    } else {
	    
       				//	var selectionManager = this._tree.getManager();
       			var item = this._tree.getSelectedItem();
       			this._saveButton.setEnabled(true);
       			this._deleteButton.setEnabled(true);
       			
       			if(item instanceof qx.ui.tree.TreeFile){
       				
       				this._createButton.setEnabled(false);				//leaf nodes don't have insert option
       				
					if(this._tree.getPreviousSibling(item) == null){	// first child cannot be moved up
       						this._moveUpButton.setEnabled(false);
       				}
       				else{
       						this._moveUpButton.setEnabled(true);
       				}
       			
       				if(this._tree.getNextSibling(item) == null){		// last child cannot be moved down
       						this._moveDownButton.setEnabled(false);   
       				}
       				else{
       						this._moveDownButton.setEnabled(true); 
       				}
     				
       			}	// end of file nodes
       			else{											// If not Files (i.e. if Folders)
       					
       					this._createButton.setEnabled(true);
       					
       					if(this._tree.getPreviousSibling(item) == null){	// first child cannot be moved up
       						this._moveUpButton.setEnabled(false);
       					}
       					else{
       						this._moveUpButton.setEnabled(true);
       					}
       			
       					if(this._tree.getNextSibling(item) == null){		// last child cannot be moved down
       						this._moveDownButton.setEnabled(false);   
       					}
       					else{
       						this._moveDownButton.setEnabled(true); 
       					}	
   				}	// end of folder nodes
	    } // end of non-root nodes
	    
  	},
  	
  	showFormData: function(){
  		var nodeData = {};
  		
  		if(this._tree.getSelectedItem() == this._tree.getRoot()){// If Root Node Remember change this._tree to this._tree.getRoot()
  			
  			nodeData.tree = this._tree;// Remember change this._tree to this._tree.getRoot()
  		}
  		else{
  			nodeData = this._tree.getNodeData();	// Calls getNodeData() function of Tree.js
  		}
  	
  		this._right.setData(nodeData);				// Calls setData() function of FunctionalityTreeSubClass.js
  		
  	},
  	
  	// gets the data from Left part and sets in right part
  	showInfo: function(e){
  		
  		this.showButtons();
  		
  		/**@TODO
  		 * reset the form in case nay field was set before and is not set now
  		 */
  		this.showFormData();
  		
  		/*
  		var nodeData = {};
  		
  		if(this._tree.getSelectedItem() == this._tree.getRoot()){// If Root Node Remember change this._tree to this._tree.getRoot()
  			
  				nodeData.tree = this._tree;// Remember change this._tree to this._tree.getRoot()
  		
  		}
  		else{
  			nodeData = this._tree.getNodeData();	// Calls getNodeData() function of Tree.js
  		}
  	
  		this._right.setData(nodeData);				// Calls setData() function of FunctionalityTreeSubClass.js
  		*/
  	},
  	
  	moveUp: function(e){
  		this._tree.moveUpNode();		// Calls moveUpNode() function of Tree.js
  		this.showButtons();				// Change the toolbar button display based on new arrangement
  	},
  	
  	moveDown: function(e){
  		this._tree.moveDownNode();		// Calls moveDownNode() function of Tree.js
  		this.showButtons();				// Change the toolbar button display based on new arrangement
  	},
  	
  	/*
  	show: function() {
		if(!this.isVisibility()) {
			this.setVisibility(true);
		}
    }
    */
    
    save: function (e) {
    	var txt1 = this._right.getUserData('label');
    	var nodeLabel = txt1.getUserData('field');
    	
    	var txt2 = this._right.getUserData('name');
    	var nodeName = txt2.getUserData('field');
    	
    	var txt3 = this._right.getUserData('description');
    	var nodeDesc = txt3.getUserData('field');
    	
    	var table = this._right.getUserData('table');
    	
    	//alert(table.getDataRowRenderer().getHighlightFocusRow());
    	//table.getDataRowRenderer().setHighlightFocusRow(false);
    	//alert(table.getDataRowRenderer().getHighlightFocusRow());
    	
    	var info = {
    				label : nodeLabel.getChildren()[0].getValue(),
    				name  : nodeName.getChildren()[0].getValue(),
    				desc  : nodeDesc.getChildren()[0].getValue(),
    				roles : table.getUpdatedData() 		
    	};
    	
    	/*
    	var str = '';
    	for(p in info) {
    		str += p + ': ' + info[p] + ';\n'
    	}
		alert(str);
		*/
    	qooxdoo.commons.CoreUtils.dump(info);
	},
	
	deleteNode: function(e){
		this._tree.deleteNode();
	},
	
	clearAll: function(e){
		this._right.resetOldData();
	},
	
	createNode: function(e){
		
		
		var dataObject = this._right.getData();
		var parent = this._tree.getSelectedItem();
		
		var nodeObject = {};
		nodeObject.name = dataObject.label;
			//nodeObject.parent = this._tree.getUserData(parent.getLabel());	//nodeid = label .. to be changed
		nodeObject.parent = parent.getLabel();
		nodeObject.data = dataObject;
		
		var treeNode = this._tree.addNode(nodeObject);
		this._tree.select(treeNode.getUserData('node'));
				
	}
	
	, createTree: function(){
		
		this.treeFunction = qooxdoo.app.data.DataService.loadTreeNodes();
	   	var tree = new qooxdoo.ui.Tree(this.treeFunction.treeStructure.root);
		
		for(var p in this.treeFunction.treeStructure){	//check as for..in is changed in v0.8.1
	  		if(p != 'root'){
	  			tree.addNode(this.treeFunction.treeStructure[p]);
	  		}
	  	}
		
			//	tree.setWidth(250);	
	  						
	  		//tree.addListener("click",tree.onClickMenu,tree);		//contextmenu				
	  	tree.addListener("changeSelection",this.showInfo,this);
	  		
	  		/*
	  		//leftPart.setBackgroundColor('white');
	  		//		vContainer.add(tree);//,{height:330}
	 		// 		leftPart.add(vContainer,{ height:'100%'});//width:'30%',
	 		*/
	  	
	  	this._tree = tree;
	}
    
	, getTree: function(){
		return this._tree;
	}
	
	, getRightPart: function(){
		return this._right;
	}
	
	, createRightSideToolbar: function(){
		this.getRightPart().createToolbar();
	}
	
	, createRightSideForm: function(){
		this.getRightPart().createForm();
	}	
  }
  
});  