/*

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005 Engineering Ingegneria Informatica S.p.A.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

*/

/* *
 * @author Andrea Gioia (andrea.gioia@eng.it)
 * @author Amit Rana (amit.rana@eng.it)
 * @author Gaurav Jauhri (gaurav.jauhri@eng.it)
 * 
 */
 
/**
 * Class for creating the Tree Widget
 */
qx.Class.define("spagobi.ui.Tree", {
          
          extend: qx.ui.tree.Tree,
          
          /**
          * Constructor of Customized class
          * <p> It creates a Tree with the root name as the string passed to it
          * <p><p>
          * <p> Structure of object passed as parameter :                    
          * <p> config :{
          * <p>         root : String
          * <p>        }
          * <p><p>
          * 
          * <p> *Example :- *
          * <p> var myTree = new spagobi.ui.Tree("Root");
          * 
          * @param config - Object with properties described above
          * 
          */ 
          construct : function(config) {
          	
          //	 var trs = qx.legacy.ui.tree.TreeRowStructure.getInstance().standard(config.root);
		     this.base(arguments);
		     this._root = new qx.ui.tree.TreeFolder(config.root);
		     this._root.setOpen(true);
		//     this.setHideRoot(true);
		     this.setRoot(this._root);
		     this.setWidth(200);
		    // this.setUserData(config.root, this._root);//this.setUserData() // check for 'node'!!
			//  alert(this.getUserData(config.root)); 
		     var dummyatom = new qx.ui.basic.Atom();
		     dummyatom.setUserData('node',this._root);
		     this.setUserData(config.root, dummyatom);

          	
          	 //this.setWidth("100%"); // don't set it, else scroll bar appear permanantly
    		 //this.setHeight("100%");
    		 
          	 //this.setUserData('node', this);
          	 //this.setSelectedElement(null);
          	 //this.getManager().setSelectedItem(null);
          	 
          	 //alert(this.getSelectedElement());
          	 //alert(this.getManager().getSelectedItem());
          	 //this.setSelectedElement(this);
          	 //this.getSelectedElement().setSelected(true);
          },
          
          members: {
          		   _root : undefined,
          		   _nodeId : 0,
          		   nameText: undefined,
          		   atom: undefined,
          		   insertWin: undefined,
          		   
          		  /**
	               * Function to show a Context Menu associated to the node when it is clicked
	               * <p> It is called by a generic event handler associated with the tree
	               * <p><p>
	               * *Example :- *
	               * <p> var myTree = new spagobi.ui.Tree({root: "Root"});
	               * <p> myTree.addListener("click",myTree.onClickMenu,myTree);
	               *                                                                                     	               
	               */
	               /*
	               onClickMenu1: function(e){
	               	
	               	var insertCmd = new qx.event.Command();
        			insertCmd.addListener("execute", this._insertCmd,this);
        				
	               	var deleteCmd = new qx.event.Command();
        			deleteCmd.addListener("execute", this.deleteNode,this);
        				
	               	var contextMenu = new qx.ui.menu.Menu;
	               	
	               	var insertButton = new qx.ui.menu.Button("Insert",null,insertCmd); 
	               	var deleteButton = new qx.ui.menu.Button("Delete",null,deleteCmd);
	               	
	               	contextMenu.add(insertButton);
	               	contextMenu.add(deleteButton);
	               	
	               	this.getApplicationRoot().add(contextMenu, {left: e._native.clientX, top: e._native.clientY});
	               	contextMenu.setOpener(this.getSelectedItem());
	               	contextMenu.show();	
	               		
	               },
	               */
	               /*                     
	               onClickMenu: function(e){
	               		
	               		//alert(this.getManager().getSelectedItem());
	               		
	               		
	               		
	          			// if(this.getSelectedItem() == undefined){		// null
	               			//alert("no Element Selected");
	               		//	return;
	               		//}
	               		
	               		//alert(this.getSelectedElement().getLabel());
	               		//alert(this.getSelectedElement()); //qx.legacy.ui.tree.Tree / qx.legacy.ui.tree.TreeFolder / qx.legacy.ui.tree.Treefile
	               		//alert(this.getSelectedElement().getLabel());		//gives label of Node
	               		
	               		
	               		//var node = e.getValue();//error
	               		//var node = this.getSelectedElement();
	               		//alert("clicked: " + e.getTarget() + "tree node: " + this.getSelectedElement());
	               		//alert("left:" + this.getSelectedElement().getLeft() + ", width: " + this.getSelectedElement().getWidth());
	               		
	               		//var nodeLabel = e.getTarget()[0]._labelObject.getText();
	               		//alert(nodeLabel);
	               		
	               		var insertCmd = new qx.event.Command();
        				insertCmd.addListener("execute", this._insertCmd,this);
	               		
	               		var deleteCmd = new qx.event.Command();
        				deleteCmd.addListener("execute", this.deleteNode,this);
        				
        				var moveUpCmd = new qx.event.Command();
        				moveUpCmd.addListener("execute", this.moveUpNode,this);
        				
        				var moveDownCmd = new qx.event.Command();
        				moveDownCmd.addListener("execute", this.moveDownNode,this);
        									
	               		
	               		var insertButton = new qx.ui.menu.Button("Insert",null,insertCmd); // handleClick(var vItem, Event e)
	               		var deleteButton = new qx.ui.menu.Button("Delete",null,deleteCmd);
	               		var moveUpButton = new qx.ui.menu.Button("Move Up",null,moveUpCmd);
	               		var moveDownButton = new qx.ui.menu.Button("Move Down",null,moveDownCmd);
	               		
	               		contextMenu.add(insertButton);
	               		contextMenu.add(deleteButton);
	               		//contextMenu.add(moveUpButton);
	               		//contextMenu.add(moveDownButton);
	               		this.getApplicationRoot().add(contextMenu, {left: e._native.clientX, top: e._native.clientY});
	               		contextMenu.setOpener(this.getSelectedItem());
	               		contextMenu.show();
	               		
	               		
	               		if(this.getSelectedItem() == this){		// If Root Node
	               			contextMenu.add(insertButton);
	               			contextMenu.add(deleteButton);
	               		} else {
		               		//	var selectionManager = this.getManager();
		               			var item = this.getSelectedItem();
		               			
		               			if(item instanceof qx.ui.tree.TreeFile){			//leaf nodes don't have insert option	
		               				
		               				if(this.getPreviousSibling(item) == undefined){		// first child cannot be moved up
		               					contextMenu.add(deleteButton,moveDownButton);
		               				}
		               			
		               				else if(this.getNextSibling(item) == undefined){	// last child cannot be moved down
		               					contextMenu.add(deleteButton,moveUpButton);	               				
		               				}
	                 				else{
		               					contextMenu.add(deleteButton,moveUpButton,moveDownButton);
	                 				}	
		               			} else{															// For folders, you have insert option even if they are at leaf
		               				if(this.getPreviousSibling(item) == undefined){		// first child cannot be moved up
		               					contextMenu.add(insertButton,deleteButton,moveDownButton);
		               				}
		               			
		               				else if(this.getNextSibling(item) == undefined){	// last child cannot be moved down
		               					contextMenu.add(insertButton,deleteButton,moveUpButton);	               				
		               				}
	                 				//var previousItem = selectionManager.getPrevious(this.getSelectedItem()); //getLeadItem(), getNextSibling, getPreviousSibling, 
	                 				else{
		               					contextMenu.add(insertButton,deleteButton,moveUpButton,moveDownButton);
	                 				}
	               				}
	               		}
  						
  						
  						
  						//this.add(contextMenu);//.addToDocument();		//var d = qx.legacy.ui.core.ClientDocument.getInstance();	//d.add(contextMenu);
  						//contextMenu.show();
  						
  						//alert(contextMenu.getParent().getVisibility() + ", "+ contextMenu.getParent().getDisplay());
  						//alert(contextMenu.getVisibility() + ", "+ contextMenu.getDisplay());
  						//alert(contextMenu.isSeeable());
  						
  						
  						
  						if (contextMenu.isSeeable()){		//never gets executed
				            //alert('hi');
				            contextMenu.hide();
				            this.setSelectedElement(null);
				            //alert(this.getSelectedElement);
				        }
				        else{
				            
				  			var ele = this.getSelectedElement().getElement();
				  			contextMenu.setLeft(qx.html.Location.getPageBoxLeft(ele)); 
				  			contextMenu.setTop(qx.html.Location.getPageBoxBottom(ele));
				  			//contextMenu.show();
				  			
				  			//this.getSelectedElement().getLeft(); // NULL
				  			//alert(qx.legacy.html.Location.getPageBoxLeft(ele) + "," + qx.legacy.html.Location.getPageBoxRight(ele));	//same
				  			//alert(this.getSelectedElement().getLeft() + ", " + qx.legacy.html.Location.getPageBoxRight(ele));
				  			//alert(this.getSelectedElement().getTop()+ this.getSelectedElement().getHeight()+ ", " + qx.legacy.html.Location.getPageBoxBottom(ele));
				  			
				  			
				  			//contextMenu.setLeft(178);
				  			//contextMenu.setTop(300);
				  			
				  			//contextMenu.setPosition('bottom-left')
				  			
				  			//alert( contextMenu.getVisibility() + ","+ contextMenu.getDisplay() + "," +contextMenu.getParent().isSeeable());
				            //alert('hi');
				            contextMenu.setDisplay(true);
				            //alert( contextMenu.getVisibility() + ","+ contextMenu.getDisplay() + "," +contextMenu.getParent().isSeeable());
				            
				            //contextMenu.setLeft(qx.bom.element.Location.getRight(ele));
				            //contextMenu.setTop(qx.bom.element.Location.getBottom(ele));
				  					//contextMenu.setLeft(this.getSelectedElement().getLeft()); //not working
				  					//contextMenu.setTop(this.getSelectedElement().getBottom());//not working
				  			
				  			//if(this.getSelectedElement().getSelected() == true){
				            	//alert(this.getSelectedElement().getLabel());
				            	contextMenu.show();
				            //}
				        }
				  		
				        // e.stopPropagation(true);	//use ??
				        
				        //this.getManager().setSelectedItem(null);
				        
				        this.getSelectedElement().setSelected(false);
				        this.setSelectedElement(null);
				        //this.getManager().setSelectedItem(null);
				         
				         
	               },
	               */
	               
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
	               /* 
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
						
						
						//var parentLabel = new qx.legacy.ui.basic.Label("Parent:");
						//parentLabel.setLocation(50, 100);
						
						//var parentText = new qx.legacy.ui.form.TextField();
						//parentText.setLocation(100, 100);
						
						
						var goButton = new qx.ui.form.Button("Insert");
				        //goButton.setLocation(150, 150);
				        goButton.addListener("execute", this._insertDetails,this);
						this.insertWin.add(goButton);
						
						this.insertWin.center();
						this.insertWin.open();
						
						//this.getApplicationRoot().add(this.insertWin);
						
						},
	              */
	              /**
	               * Event Listener function to get the details of the new node to be created
	               * <p> It is called internally by the _insertCmd() function
	               */
	               /*
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
						
						//node.setSelected(true);
						
	               },
	               */
	               	
	      		  /**
	               * Function to add a Sub-folder(Leaf or Non-Leaf Node) in the tree
	               * <p> It also adds the checkbox field if set as true in the object property	               
	               * <p> It returns the reference of the created Node of type TreeFolder or TreeFile
	               * <p><p>                 	  
	               * <p> Structure of the object passed as parameter :                             
	               * <p> config : {
	               * <p>            name 	  : String (Name of the Node),
	               * <p>            init_icon : image path in double quotes (Icon to be shown when Node appears) - optional,
	               * <p>            click_icon: image path (Icon to be shown when Node is clicked) - optional,
	               * <p>            parent 	  : TreeFolder (Parent node to which new Node will be added),
	               * <p>            id 		  : String (to identify the Node of the tree),
	               * <p>            checkBox  : Boolean (if Checkbox is required or not) - optional,
	               * <p>            file	  : Boolean (true to indicate File Node)
	               * <p>         }
	               * <p>
                   * *Example :-* 
                   * The below code creates the tree as :	<p> Root
                   * 										<p>&nbsp;&nbsp; -- 	SubFolder1
                   * 										<p>&nbsp;&nbsp; -- |_| SubFolder2	
                   * 										<p>&nbsp;&nbsp;&nbsp;&nbsp;	-- |_| File1
                   * var myTree = new spagobi.ui.Tree({root: "Root" });
                   * var node1 = myTree.addNode({
                   * 							name  : "SubFolder1",
		  		   *  						   	parent: myTree,
		  		   *						   	id	 : "node1"	
  				   *			     		   });
  				   * var node2 = myTree.addNode({
		  		   *							name  : "SubFolder2",
		  		   *							parent: myTree,
		  		   *							id	  : "node2",
		  		   *							init_icon: "icon/16/places/user-trash.png",
		  		   *							click_icon: "",
		  		   *							checkBox  : true	
  				   *							});
  				   * var node3 = myTree.addNode({
				   *	  						name  : "File1",
				   *	  						parent: node2,
				   *	  						id	  : "node3",
				   *	  						checkBox: true,
				   *	  						init_icon: "icon/16/places/user-desktop.png",
				   *	  						click_icon: "",
				   *	  						file  : true	
  				   *							});
  				   *							 	
  				   * @param config - Object with properties as described above
  				   * @return {AbstractTreeElement} Reference to the created tree node
  				   * 		     		 
                   */ 
          		 addNode: function(config){
          			
          			var treeNode;
                 	
                 	if(config.checkBox != undefined){	// to check if its a standard node with just icon and name
                 										// ... or special node with a icon, checkbox and name 
                 	
                 		//	treeRowStructure = new qx.ui.tree.TreeFolder();
                 		treeNode  = new qx.ui.tree.TreeFolder();
                 		//		treeNode.addOpenButton();                 
                 		//		treeRowStructure.addIndent();
                 		if(config.init_icon != undefined && config.click_icon != undefined){
                 			
                 		//	treeRowStructure.addIcon(config.init_icon, config.click_icon);
                 			treeNode.addIcon(config.init_icon, config.click_icon);
                 		}
                 		// to add default image 
                 		
                 		if (config.checkBox == true){
                 			
                      		var obj = new qx.ui.form.CheckBox();
                     		// 		treeRowStructure.addWidget(obj);//, true
                      		treeNode.addWidget(obj);
                      		//	treeNode.addSpacer();
                  		}
                  		
               			//   		treeRowStructure.addLabel(config.name);
                  		treeNode.addLabel(config.name);
                 	}
                 	
              		/*   	else{			// if standard node with just Icon and name
                 		//	alert("4");
                 			treeRowStructure = new qx.ui.tree.TreeFolder(config.name);
                 	}	*/ 
                  	
                    else if(config.file != undefined){	// to check if node is of type file or folder
                  	
                  			if(config.file == true){
                  			
                  			treeNode = new qx.ui.tree.TreeFile(config.name);
                  			//	treeNode.addOpenButton();
                  		}
                  		else{
                  		
                  			treeNode = new qx.ui.tree.TreeFolder(config.name);
                  			//	treeNode.addOpenButton();
                  		}
                  	}
                  	else{			// by default, node is of Folder type
                  		
                  		treeNode = new qx.ui.tree.TreeFolder(config.name);
                  		//		treeNode.addOpenButton();
                  		
                  	}                                                  
                  	
                  	
                  	if(config.parent == this || config.parent == 'root'){	//if(config.parent == this)
                  	
                  		this._root.add(treeNode);
                  		//this.setUserData(config.id, treeNode);
                  	}
                   	else{
                   	
                   	
                   		//if(config.parent != undefined){//  this.getSelectedItem()
                   			//alert(config.parent.getLabel());
                   		//}
                   		
                   	 	var p = this.getUserData(config.parent).getUserData('node');//config.parent.getUserData('node');
                   	 	p.add(treeNode);
                   	}
                   	
                   	
                   	 var atom = new qx.ui.basic.Atom();
        			 //atom.add(treeNode);//,config.data);
        			 
        			 atom.setUserData('node', treeNode);
        			 
        			 if(config.data != undefined){				//required when dataservice separates tree structure and data
        			 	atom.setUserData('data', config.data);
        			 }
        			// alert(atom.getUserData('node'));
        			 this.setUserData(config.name,atom);	//Label of node is used as its id .. later we can use its level or hierarchy path
                   	 
                   	
                   	//return treeNode;
                   	return atom;
	             },
	             
	             
	             /**
	              * Function to delete a Node from the Tree
	              * <p> It is called by the Context Menu option at a node
	              * <p><p>
	              * 
	              * <p> *Example:-*
	              * <p> var deleteCommand = new qx.event.Command();
        		  *	<p> deleteCommand.addListener("execute", this.deleteNode,this);
	              * <p> var deleteButton = new qx.legacy.ui.menu.Button("Delete",null,deleteCommand);
	              *                                               	               
	              */
	             deleteNode: function(e){
	               /*	             	
	             	if(currentItem == this.getRoot()){
	             		alert("Root node cannot be deleted");
	             	}
          			else if (currentItem != null) {
          					currentItem.destroy();
	              			currentItem = null;
          			}
          			
          			//this.getSelectedElement().getParent().setSelected(true);	//else try getParentFolder()
          			*/
          			
          			var current = this.getSelectedItem();
					var parent = current.getParent();
					parent.remove(current);
					this.select(parent);
					/*
					var str="" ;
					var parentChildren = parent.getChildren();
			        for(i in parentChildren){
			        	str = str+parentChildren[i].getLabel()+"\n";
			        }
			        alert(str);   
          			*/
                 },
                 
                 /**
	              * Function to delete a Node from the Tree based on Node id
	              * 
	              * @param id{String} Node-id referring to the selected Node
	              *                                                   	               
	              */
                 deleteNodeById: function(id){
                 	var currentItem = this.getUseData(id);
                 	
                 	//if root then show alert
                 	//else
          			if (currentItem != null) {
              			currentItem.destroy();
              			currentItem = null;
          			}
          			
          			//this.getSelectedElement().getParent().setSelected(true);
                 },
                 
                 deleteTreeNode: function(){
                 	/*
                 	 * working but long version
                 	var currentItem = this.getSelectedElement();
                 	var atomLabel = currentItem.getLabel();
                 	var atom = this.getUserData(atomLabel);
                 	var node = atom.getUserData('node');
                 	var nodeParent = node.getParentFolder();
                 	nodeParent.remove(node);
                 	*/
                 	
                 	var currentItem = this.getSelectedItem();
                 	var nodeParent = currentItem.getParentChildrenContainer();
                 	nodeParent.remove(currentItem);
                 //	nodeParent.setSelected(true);
                 	nodeParent.setVisibility("visible");
                 	
                 	//alert(nodeParent.getChildrenLength()); why 2 as horizontalbox and vbox??
                 	//node.destroy();
                 	//this.remove(atom); //removeFromTreeQueue() // this.removeChildFromTreeQueue(node)
                 	
                 },
                 /**
                  * Function to get the currently selected node of the tree
                  * 
                  * <p>*Example:-*
                  * <p> var myTree = new spagobi.ui.Tree({root: "Root" });
                  * <p> var node3 = myTree.addNode({
						  							name  : "File3",
						  							parent: myTree,
						  							id	  : "node3",
						  							file  : true	
  													});
                  * <p> var n = myTree.getSelectedElement(node3);
                  * 
                  * @return {AbstractTreeElement} - The currently selected Node of the tree
                  */
                 getCurrentNode:function(){
                 	return this.getSelectedItem();//getSelectedElement()
                 },
                 
                 /**
                  * 
                  */
                 moveUpNode: function(){
                 	
		              var item = this.getSelectedItem();
		              var previousItem = this.getPreviousSibling(item);
		              var parentItem = item.getParent();
		              parentItem.remove(item);
		              parentItem.addBefore(item,previousItem);
		              item.setVisibility("visible");
                 },
                 
                 /**
                  * 
                  */
                 moveDownNode: function(){
             
	               	var item = this.getSelectedItem();
		            var nextItem = this.getNextSibling(item);
		            var parentItem = item.getParent();
		            parentItem.remove(item);
		            parentItem.addAfter(item,nextItem);
		            item.setVisibility("visible");
                 },
                 
                 /*
                 getNodeID: function(n){
                 	return this.getUserData();
                 },
                 */
                 
                 getNodeData: function(){
                 	
                 	var info = {};
               //  	var node = this.getCurrentNode();
                 //	alert(node);
                 	var nodeId = this.getSelectedItem().getLabel();
                 	
                 	var atom = this.getUserData(nodeId);	//nodeid = label .. to be changed
                 	
                 	if(atom.getUserData('data') != undefined)
                 		info.data = atom.getUserData('data');
                 	
                 	info.tree = this;
                 	return info;
                 },
                 
                 getPreviousSibling: function(treeItem){
                 	var parent = treeItem.getParent();
				      if (!parent) {
				        return null;
				      }
					/*	
				      if (this.getHideRoot())
				      {
				        if (parent == this.getRoot())
				        {
				          if (parent.getChildren()[0] == treeItem) {
				            return null;
				          }
				        }
				      }
				      else
				      {
				        if (treeItem == this.getRoot()) {
				          return null;
				        }
				      }
					*/
				      var parentChildren = parent.getChildren();
				      var index = parentChildren.indexOf(treeItem);
				      if (index > 0){
				        return parentChildren[index-1];
				      }
				      else{
				        return null;
				      }
                 },
                 
                 getNextSibling: function(treeItem){
                 	var parent = treeItem.getParent();
			        if (!parent) {
			          return null;
			        }
			
			        var parentChildren = parent.getChildren();
			        /*
			        var str="" ;
			        for(i in parentChildren){
			        	str = str+parentChildren[i].getLabel()+"\n";
			        }
			        alert(str);
			        */ 
			        var index = parentChildren.indexOf(treeItem);
			        //alert(index);
			        if (index > -1 && index < parentChildren.length-1) {
			          return parentChildren[index+1];
			        }
			        else{
			        	return null;
			        }
                 }
          }
});