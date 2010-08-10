qx.Class.define("spagobi.ui.custom.FunctionalTree",{

  extend : spagobi.ui.Tree,
  
  construct : function()  {//type
 
	  		//this.base(arguments,{root: "Functionalities"}); 
	  		this.tree = spagobi.app.data.DataService.loadTreeNodes();
	  		this.base(arguments,this.tree.treeStructure.root); 
		  	this.createFunctionalTree();
		  			  	
  },
  
  members : {
  	
  			insertWin: undefined,
  			nodeX: undefined,
  			nodeY: undefined,
  			contextMenu: undefined,
  			previousNode: undefined,
  			tree: undefined,
  			
  			createFunctionalTree : function(){
  			
  				
  				 	for(var p in this.tree.treeStructure){	//check as for..in is changed in v0.8.1
  				 		
  				 		if(p != 'root'){
  				 			this.addNode(this.tree.treeStructure[p]);
  				 		}
  				 					
  				 	}
  				
  			//this._add(this.tree, {height: "100%"});
  			
  			
  			this.addListener("contextmenu",this._createMenu,this);	//right-click calls the function
  			/*
  			function context(){
			//This function takes care of Net 6 and IE.
			return false;
			}

			document.onmousedown=right;
			document.oncontextmenu=context;
			*/ 
  	},
  
     /**
	               * Function to show a Context Menu associated to the node when it is clicked
	               * <p> It is called by a generic event handler associated with the tree
	               * <p><p>
	               * *Example :- *
	               * <p> var myTree = new spagobi.ui.Tree({root: "Root"});
	               * <p> myTree.addListener("click",myTree.onClickMenu,myTree);
	               *                                                                                     	               
	 */
          _createMenu : function(e){
          				//e is Qooxdoo wrapper event for Javascript mouseevent e._native
          				//alert(spagobi.commons.CoreUtils.toStr(e._native));
          				
          				
          				// prevents default context menu by browser over tree node clicks
          				qx.core.Init.getApplication().getRoot().setNativeContextMenu(false);
          				
          				/*
          				//but another right-click over the contextmenu still gets the browser menu 
          				if(navigator.appName === 'Netscape'){
          					e._native.preventDefault();
          				}
          				else{
          					e._native.returnValue = true;
          				}
          				*/
          				          				
          				var insertCmd = new qx.event.Command();
        				insertCmd.addListener("execute", this._insertCmd,this);
        				
	               		var deleteCmd = new qx.event.Command();
        				deleteCmd.addListener("execute", this.deleteNode,this);
        				
          				var moveUpCmd = new qx.event.Command();
        				moveUpCmd.addListener("execute", this.moveUpNode,this);
        				
        				var moveDownCmd = new qx.event.Command();
        				moveDownCmd.addListener("execute", this.moveDownNode,this);
          				
          				this.contextMenu = new qx.ui.menu.Menu;
          				
	             		var insertButton = new qx.ui.menu.Button("Insert",null,insertCmd); 
	               		var deleteButton = new qx.ui.menu.Button("Delete",null,deleteCmd);
	               		var moveUpButton = new qx.ui.menu.Button("Move Up",null,moveUpCmd);
	             	  	var moveDownButton = new qx.ui.menu.Button("Move Down",null,moveDownCmd);
	               		
	               		var node = this.getSelectedItem();
	               		
	               		//check if right click is done without selecting any node
	               		if(node == undefined){
	               			return;
	               		}
	               		
	               		//clicking outside the tree should not show the context menu
	               		
	               		if(this.previousNode == undefined){
	               			this.previousNode = node;
	               		}
	               		else if(node == this.previousNode){
	               				//compare mouse co-ordinates with node coordinates
	               				
	               				//var childrenAbove = this.getRoot().getChildren().indexOf(node);
	               				//var nodeTop = childrenAbove * node.getheight();
	               				//if(e._native.clientY){
	               				
	               				var ele = node.getContentElement();
	               				var obj = ele._element;
			               		var curleft = 0;
			               		var curtop = 0;
			               		do {
									curleft += obj.offsetLeft;
									curtop += obj.offsetTop;
			               		} while (obj = obj.offsetParent);//(obj = obj.offsetParent)
	               				
	               				//alert(curleft + "," + curtop + "," + e._native.clientX + "," + e._native.clientY);
	               				
	               				var nodeBounds = node.getBounds();
	               				if(e._native.clientY < curtop || e._native.clientY > curtop+nodeBounds.height){
	               					return;
	               				}
	               			//this.previousNode = node;	
	               		}		
	               		//}
	               		//else{		//different node selected
	               		
	               		//alert(e.getDocumentLeft() + ","+ e.getScreenLeft() +"," + e.getViewportLeft() + "," +e._native.clientX);
	               		//alert(spagobi.commons.CoreUtils.toStr(ele));//_createDomElement
	               		//alert(ele._element.offsetLeft);
	               		
	               		
	               		
	               		 
	               		//alert(ele._element.left);//spagobi.commons.CoreUtils.toStr(ele)
	               		//var arr = this.getApplicationRoot().getChildren();
	               		
	               		//alert(this.getLayoutParent());
	               		//var nodeBounds = node.getBounds();	
	               		//alert("" + nodeBounds.left + "," + nodeBounds.top + ","+ nodeBounds.width + ","+ nodeBounds.height);
	               		//node.getLayoutProperties();
	               		//var nodeLayputProp = node.getLayoutProperties();
	               		//alert(spagobi.commons.CoreUtils.toStr(nodeLayputProp));
	               		
	               		/*
	               		if(this.nodeX != undefined && this.nodeY != undefined){
	               			if(this.nodeX != e._native.clientX && this.nodeY != e._native.clientY){	//click done outside any node
	               				return;
	               			}
	               		}
	               		
	               		this.nodeX = e._native.clientX;
	               		this.nodeY = e._native.clientY;
	               		*/
	               		
	               		
	               		if(node == this.getRoot()){		// If Root Node
	               				this.contextMenu.add(insertButton);
		               			//this.contextMenu.add(deleteButton);
	               				
	               		} else {
		               		//	var selectionManager = this.getManager();
		               		//	var item = this.getSelectedItem();
		               			
		               			if(node instanceof qx.ui.tree.TreeFile){	//leaf nodes don't have insert option	
		               				
		               				if (this.getNextSibling(node) == undefined && this.getPreviousSibling(node) == undefined ){
	                 					this.contextMenu.add(deleteButton);
	                 				}
		               			
		               				else if(this.getNextSibling(node) == undefined){	// last child cannot be moved down
		               					this.contextMenu.add(deleteButton);
		               					this.contextMenu.add(moveUpButton);               				
		               				}
	                 				else if(this.getPreviousSibling(node) == undefined){		// first child cannot be moved up
		               					this.contextMenu.add(deleteButton);
		               					this.contextMenu.add(moveDownButton);
		               				}
	                 				else{
		               					this.contextMenu.add(deleteButton);
		               					this.contextMenu.add(moveUpButton);
		               					this.contextMenu.add(moveDownButton);
		               					
		               				}	
		               			} else{		// For folders, you have insert option even if they are at leaf
		               				if (this.getNextSibling(node) == undefined && this.getPreviousSibling(node) == undefined ){
	                 					this.contextMenu.add(insertButton);
		               					this.contextMenu.add(deleteButton);
	                 				}
		               				else if(this.getPreviousSibling(node) == undefined){		// first child cannot be moved up
		               					this.contextMenu.add(insertButton);
		               					this.contextMenu.add(deleteButton);
		               					this.contextMenu.add(moveDownButton);
		               				}
		               				else if(this.getNextSibling(node) == undefined){	// last child cannot be moved down
		               					this.contextMenu.add(insertButton);
		               					this.contextMenu.add(deleteButton);
		               					this.contextMenu.add(moveUpButton);	               				
		               				}
	                 				//var previousItem = selectionManager.getPrevious(this.getSelectedItem()); //getLeadItem(), getNextSibling, getPreviousSibling, 
	                 				 
	                 				else{
		               					this.contextMenu.add(insertButton);
		               					this.contextMenu.add(deleteButton);
		               					this.contextMenu.add(moveUpButton);
		               					this.contextMenu.add(moveDownButton);
	                 				}
	               				}
	               		}
	               		this.getApplicationRoot().add(this.contextMenu, {left: e._native.clientX, top: e._native.clientY});
	               		//e.getScreenLeft() is also same as e._native.clientX
	               		this.contextMenu.setOpener(this.getSelectedItem());
	               		this.contextMenu.show();
	               		
	               		//}//node!=this.previousNode
          			//}//this.previousNode is undefined
          			
	               		this.previousNode = node;
	               		
	               		/*
	               		if (this.contextMenu.isVisible() == false){
	               			//alert("1");
	               		}
	               		else{
	               			//alert("2");
	               		}
	               		*/ 
          },
	               
	               /**
	                * Event Listener Function called when the Insert option of Context menu is clicked
	                * <p>It is called internally by the onClickMenu() function
	                * <p><p>
	                * 
	                * <p> *Example :- *
	                * <p> var insertCommand = new qx.event.Command();
        			* <p> insertCommand.addListener("execute", this._insertCmd,this);
	                * <p> var insertButton = new qx.legacy.ui.menu.Button("Insert",null,insertCommand);
	                * 
	                */
	               _insertCmd: function(e){
	               	
	               		//alert(e);	//Data Event
	               		this.insertWin = new qx.ui.window.Window("Insert");
						//this.insertWin.setSpace(200, 400, 200, 250);
						this.insertWin.setLayout(new qx.ui.layout.VBox(20));
						
						this.atom = new qx.ui.container.Composite(new qx.ui.layout.HBox);
						var nameLabel = new qx.ui.basic.Label("Name:");
						//nameLabel.setLocation(50, 50);
						this.atom.add(nameLabel);
						
						this.nameText = new qx.ui.form.TextField();	//global .. MAYBE can make LOCAL .. try
						//this.nameText.setLocation(100, 50);
						this.atom.add(this.nameText);
						this.atom.setUserData('newNodeName',this.nameText);
						
						this.insertWin.add(this.atom);
						
						/*
						var parentLabel = new qx.legacy.ui.basic.Label("Parent:");
						parentLabel.setLocation(50, 100);
						
						var parentText = new qx.legacy.ui.form.TextField();
						parentText.setLocation(100, 100);
						*/
						
						var goButton = new qx.ui.form.Button("Insert");
				        //goButton.setLocation(150, 150);
				        goButton.addListener("execute", this._insertDetails,this
				        /*
				        function(e) {
				          
						var c = {};
						c.name = nameText.getValue();
						c.parent = parentText.getValue();
						c.id = "node"+this._nodeId;
						this._nodeId++;
						this.addNode(c);
				        }*/
				        );
						this.insertWin.add(goButton);
						
						this.insertWin.center();
						this.insertWin.open();
						
						
						//this.getApplicationRoot().add(this.insertWin);
						
						},
	              
	              /**
	               * Event Listener function to get the details of the new node to be created
	               * <p> It is called internally by the _insertCmd() function
	               */ 
	               _insertDetails: function(e){
	               		
						var btn = e.getTarget();
	               		//alert( btn.getParent());// canvousLayout
	               		//var win = btn.getParent();
	               		//alert("children count: "+win.getChildrenLength());
	               		//var nameText = win.getChildren()[1];
	               		//var parentText = win.getChildren()[3] ;
	               		
						var c = {};
						c.name = this.atom.getUserData('newNodeName').getValue(); //"Report";(working)
						
						c.parent = this.getUserData(this.getSelectedItem().getLabel());//returns the atom
						c.id = "node"+this._nodeId;
						this._nodeId++;
						var node = this.addNode(c);
						this.insertWin.close();
						
						//set the new node as selected node
						var treeNode = node.getUserData('node');
						var treeNodeParent = treeNode.getParent();
						if(treeNodeParent.isOpen() == false){
							treeNodeParent.setOpen(true);
						}
						this.select(treeNode);
						
						this.previousNode = this.getSelectedItem(); //new created node
	               },
	               
	               deleteNode: function(e){
	               		this.base(arguments); //calls deleteNode of parent class
	               		this.previousNode = this.getSelectedItem();	//parent of delected node
	               }
  
  
  }
  });