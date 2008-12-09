qx.Class.define("spagobi.ui.custom.FunctionalClassDummy",
{
  extend : qx.ui.container.Composite,//qx.ui.splitpane.Pane,//qx.legacy.ui.splitpane.HorizontalSplitPane,
  
  construct : function(type)
  {
   // this.base(arguments, "1*", "2*");
    this.base(arguments);//,"1*","4*");//, "2*"); //  180
    this.setLayout(new qx.ui.layout.Dock);
     //this.setShowKnob(false);
    this.horSplit = new qx.ui.splitpane.Pane();
    this.add(this.horSplit,{width:'100%',height:'100%'});
    
 	if(type === 'funcManagement') {
   	var leftPart = new qx.ui.container.Composite(new qx.ui.layout.VBox);
//  		leftPart.setWidth("100%");
//   		leftPart.setHeight("100%");
 //   	leftPart.setOverflow("auto"); 
  //  	leftPart.setBackgroundColor('white');
 // 		var border = new qx.ui.decoration.Single(1);
  //  	leftPart.setDecorator(new qx.ui.decoration.Single(1));//leftPart.setBorder(new qx.legacy.ui.core.Border(1));
    	
  	var headerLabel = new qx.ui.basic.Label("Functionalities Tree");
   		
  	leftPart.add(headerLabel);
   
  	var tree = new spagobi.ui.Tree({root: "Functionalities"});
		
  	
  	var node1 = tree.addNode({
		  							name  : "Report",
		  							parent: tree,
		  							data  : {
		  							 			label : 'ReportLabel',
		  							 			name  : 'ReportName',
		  							 			desc  : 'ReportDesc',
		  							 			func  : [
		  							 						{
		  							 							role	: '/admin',
		  							 							dev		: true,
		  							 							test	: true,
		  							 							exe		: true
		  							 							
		  							 						},
		  							 						{
		  							 							role	: '/community/direction',
		  							 							dev		: true,
		  							 							test	: true,
		  							 							exe		: true
		  							 							
		  							 						}
		  							 			]	
		  							 		}
		  							 		
		  					});
		  					
	  				
  		var node2 = tree.addNode({
		  							name  : "OLAP",
		  							parent: tree,
		  							data  : {
		  							 			label : 'OLAPLabel',
		  							 			name  : 'OLAPName',
		  							 			desc  : 'OLAPDesc',
		  							 			func  : [
		  							 						{
		  							 							role	: '/community',
		  							 							dev		: true,
		  							 							test	: true,
		  							 							exe		: true
		  							 							
		  							 						},
		  							 						{
		  							 							role	: '/guest',
		  							 							dev		: true,
		  							 							test	: true,
		  							 							exe		: true
		  							 							
		  							 						}
		  							 			]
		  							 		}
  								});
  		var node3 = tree.addNode({
		  							name  : "myOLAP",
		  							parent: node2,
		  							data  : {
		  							 			label : 'myOLAP Label',
		  							 			name  : 'myOLAP Name',
		  							 			desc  : 'myOLAP Desc'
		  							 		}
  								});
  		var node4 = tree.addNode({
		  							name  : "DashBoard",
		  							parent: tree,
		  							data  : {
		  							 			label : 'DashBoardLabel',
		  							 			name  : 'DashBoardName',
		  							 			desc  : 'DashBoardDesc'
		  							 		}	
  								});
  		var node5 = tree.addNode({
		  							name  : "myDashBoardFolder",
		  							parent: node4,  
		  							data  : {
		  							 			label : 'myDashBoardFolderLabel',
		  							 			name  : 'myDashBoardFolderName',
		  							 			desc  : 'myDashBoardFolderDesc'
		  							 		}
		  						});
  		var node6 = tree.addNode({
		  							name  : "myDashBoard",
		  							parent: node4,  
		  							data  : {
		  							 			label : 'myDashBoard Label',
		  							 			name  : 'myDashBoard Name',
		  							 			desc  : 'myDashBoard Desc'
		  							 		}
  								});
  		
  		this._tree = tree;						
  		//tree.addListener("click",tree.onClickMenu,tree);		//contextmenu				
  		tree.addListener("changeSelection",this.showInfo,this);
  		
  		
  		//leftPart.setBackgroundColor('white');
  		
  		leftPart.add(tree);//,{height:330}
  		this.horSplit.add(leftPart);
  	//	var headerLabel1 = new spagobi.ui.custom.FunctionalityTreeSubClass();
   
        
  		//leftPart.setOverflow("auto");	
  		
  	//	this.horSplit.add(headerLabel,0);
  		
  		
  		var rightPart = new spagobi.ui.custom.FunctionalityTreeSubClass();
  		
  		this.horSplit.add(rightPart,1);
  		
  	
  
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
  		
  		this._right = rightPart;
  			
   } 	
  },
  
  members :
  {
	 horSplit : undefined,
  	_tree : undefined,
  	_right: undefined,
  	
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
  		//	alert("1");
  			return;
  			
	    } else {
	    //	alert("2");
       		//	var selectionManager = this._tree.getManager();
       			var item = this._tree.getSelectedItem();
       		//	alert(item.data.label);
       		//	alert("Save Button getting enabled now");
       			this._saveButton.setEnabled(true);
       		//	alert("Delete Button getting enabled now");
       			this._deleteButton.setEnabled(true);
       			
       			if(item instanceof qx.ui.tree.TreeFile){
       		//		alert("Never");			//leaf nodes don't have insert option	
       				this._createButton.setEnabled(false);
       				
					if(this._tree.getPreviousSiblingOf(item) == null){		// first child cannot be moved up
       						this._moveUpButton.setEnabled(false);
       					}
       					else{
       						this._moveUpButton.setEnabled(true);
       					}
       			
       					if(this._tree.getNextSiblingOf(item) == null){	// last child cannot be moved down
       						this._moveDownButton.setEnabled(false);   
       					}
       					else{
       						this._moveDownButton.setEnabled(true); 
       					}
     				//} 
       			}	// end of file nodes
       			else{											// If not Files (i.e. if Folders)
       			//		alert("Create Button getting enabled now");
       						this._createButton.setEnabled(true);
       			//			alert("Save Button getting enabled now");
	        			this._saveButton.setEnabled(true);
	        	//		alert("Delete Button getting enabled now");
  						this._deleteButton.setEnabled(true);
  					//	alert(this._tree.getPreviousSiblingOf(item));
       					if(this._tree.getPreviousSiblingOf(item) == null){		// first child cannot be moved up
       						
       			//				alert("Move Up Button getting enabled now");
       							this._moveUpButton.setEnabled(false);
       					}
       					else{
       				//		alert("Move Down Button getting enabled now");
       						this._moveUpButton.setEnabled(true);
       					}
       			
       					if(this._tree.getNextSiblingOf(item) == null){	// last child cannot be moved down
       							alert(this._tree.getNextSiblingOf(item));
       					//		alert("Move Up Button getting enabled now");
       							this._moveDownButton.setEnabled(false);   
       					}
       					else{
       					//	alert("Move Down Button getting enabled now");
       					alert(this._tree.getNextSiblingOf(item));
       						this._moveDownButton.setEnabled(true); 
       					}	
   				}	// end of folder nodes
	    } // end of non-root nodes
	    
  	},
  	
  	// gets the data from Left part and sets in right part
  	showInfo: function(e){
  		
  		this.showButtons();
  		
  		var nodeData = {};
  		
  		if(this._tree.getSelectedItem() == this._tree.getRoot()){// If Root Node Remember change this._tree to this._tree.getRoot()
  			
  				nodeData.tree = this._tree;// Remember change this._tree to this._tree.getRoot()
  		//		alert("1"); 
  		}
  		else{
  		//	alert("2");
	    	nodeData = this._tree.getNodeData();	// Calls getNodeData() function of Tree.js
  		}
  	//	alert("3");	
  		this._right.setData(nodeData);				// Calls setData() function of FunctionalityTreeSubClass.js
  		
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
    	
    	var str = '';
    	for(p in info) {
    		str += p + ': ' + info[p] + ';\n'
    	}
		alert(str);
	},
	
	deleteNode: function(e){
		this._tree.deleteTreeNode();
	},
	
	clearAll: function(e){
		this._right.resetOldData();
	},
	
	createNode: function(e){
		
		
		var dataObject = this._right.getData();
		var parent = this._tree.getSelectedItem();
		alert(parent);
	//	alert(dataObject.label);
		alert(parent.getLabel());
		var nodeObject = {};
		nodeObject.name = dataObject.label;
		nodeObject.parent = this._tree.getUserData(parent.getLabel());	//nodeid = label .. to be changed
		alert(nodeObject.parent);
	//	alert(nodeObject.parent);
		nodeObject.data = dataObject;
		
		var treeNode = this._tree.addNode(nodeObject);
		this._tree.select(treeNode.getUserData('node'));
				
	}
    
  }
  
});  