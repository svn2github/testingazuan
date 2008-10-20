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
          	
          	 var trs = qx.ui.tree.TreeRowStructure.getInstance().standard(config.root);
		     this.base(arguments, trs);
          	
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
	               * <p> myTree.addEventListener("click",myTree.onClickMenu,myTree);
	               *                                                                                     	               
	               */                               
	               onClickMenu: function(e){
	               		
	               		//alert(this.getManager().getSelectedItem());
	               		
	               		
	               		
	               		if(this.getSelectedElement() == undefined || this.getManager().getSelectedItem() == undefined){		// null
	               			//alert("no Element Selected");
	               			return;
	               		}
	               		
	               		//alert(this.getSelectedElement().getLabel());
	               		//alert(this.getSelectedElement()); //qx.ui.tree.Tree / qx.ui.tree.TreeFolder / qx.ui.tree.Treefile
	               		//alert(this.getSelectedElement().getLabel());		//gives label of Node
	               		
	               		
	               		//var node = e.getValue();//error
	               		//var node = this.getSelectedElement();
	               		//alert("clicked: " + e.getTarget() + "tree node: " + this.getSelectedElement());
	               		//alert("left:" + this.getSelectedElement().getLeft() + ", width: " + this.getSelectedElement().getWidth());
	               		
	               		//var nodeLabel = e.getTarget()[0]._labelObject.getText();
	               		//alert(nodeLabel);
	               		
	               		var insertCmd = new qx.client.Command();
        				insertCmd.addEventListener("execute", this._insertCmd,this
        													/*
        													function(e) {			// show form, get details ...
        													//alert("Insert");	// ... and call addNode(config)
        													
        													var insertWin = new qx.ui.window.Window("Insert");
        													insertWin.setSpace(200, 400, 200, 250);
        													var d = qx.ui.core.ClientDocument.getInstance();
  															d.add(insertWin);
  															
  															var nameLabel = new qx.ui.basic.Label("Name:");
  															nameLabel.setLocation(50, 50);
  															
  															var nameText = new qx.ui.form.TextField();
  															nameText.setLocation(100, 50);
  															
  															var parentLabel = new qx.ui.basic.Label("Parent:");
  															parentLabel.setLocation(50, 100);
  															
  															var parentText = new qx.ui.form.TextField();
  															parentText.setLocation(100, 100);
  															
  															var goButton = new qx.ui.form.Button("Insert");
													        goButton.setLocation(150, 150);
													        goButton.addEventListener("execute", function(e) {
													          
        													var c = {};
        													c.name = nameText.getValue();
        													c.parent = parentText.getValue();
        													c.id = "node"+this._nodeId;
        													this._nodeId++;
        													this.addNode(c);
													        });
  															
  															insertWin.add(nameLabel,nameText,parentLabel,parentText,goButton);
  															
  															insertWin.open();
        													
        												}*/
        									);
	               		
	               		var deleteCmd = new qx.client.Command();
        				deleteCmd.addEventListener("execute", this.deleteNode,this
        													/*function(e) {			// call deleteNode()
        													alert("Delete");
        													}*/
        									);
        				
        				var moveUpCmd = new qx.client.Command();
        				moveUpCmd.addEventListener("execute", this.moveUpNode,this);
        				
        				var moveDownCmd = new qx.client.Command();
        				moveDownCmd.addEventListener("execute", this.moveDownNode,this);
        									
	               		var contextMenu = new qx.ui.menu.Menu;
	               		var insertButton = new qx.ui.menu.Button("Insert",null,insertCmd); // handleClick(var vItem, Event e)
	               		var deleteButton = new qx.ui.menu.Button("Delete",null,deleteCmd);
	               		var moveUpButton = new qx.ui.menu.Button("Move Up",null,moveUpCmd);
	               		var moveDownButton = new qx.ui.menu.Button("Move Down",null,moveDownCmd);
	               		
	               		if(this.getManager().getSelectedItem() == this){		// If Root Node
	               			contextMenu.add(insertButton,deleteButton);
	               		} else {
		               			var selectionManager = this.getManager();
		               			var item = selectionManager.getSelectedItem();
		               			
		               			if(item instanceof qx.ui.tree.TreeFile){			//leaf nodes don't have insert option	
		               				
		               				if(selectionManager.getPreviousSibling(item) == undefined){		// first child cannot be moved up
		               					contextMenu.add(deleteButton,moveDownButton);
		               				}
		               			
		               				else if(selectionManager.getNextSibling(item) == undefined){	// last child cannot be moved down
		               					contextMenu.add(deleteButton,moveUpButton);	               				
		               				}
	                 				else{
		               					contextMenu.add(deleteButton,moveUpButton,moveDownButton);
	                 				}	
		               			} else{															// For folders, you have insert option even if they are at leaf
		               				if(selectionManager.getPreviousSibling(item) == undefined){		// first child cannot be moved up
		               					contextMenu.add(insertButton,deleteButton,moveDownButton);
		               				}
		               			
		               				else if(selectionManager.getNextSibling(item) == undefined){	// last child cannot be moved down
		               					contextMenu.add(insertButton,deleteButton,moveUpButton);	               				
		               				}
	                 				//var previousItem = selectionManager.getPrevious(this.getSelectedItem()); //getLeadItem(), getNextSibling, getPreviousSibling, 
	                 				else{
		               					contextMenu.add(insertButton,deleteButton,moveUpButton,moveDownButton);
	                 				}
	               				}
	               		}
  						
  						contextMenu.addToDocument();		//var d = qx.ui.core.ClientDocument.getInstance();	//d.add(contextMenu);
  						contextMenu.show();
  						
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
				  			//alert(qx.html.Location.getPageBoxLeft(ele) + "," + qx.html.Location.getPageBoxRight(ele));	//same
				  			//alert(this.getSelectedElement().getLeft() + ", " + qx.html.Location.getPageBoxRight(ele));
				  			//alert(this.getSelectedElement().getTop()+ this.getSelectedElement().getHeight()+ ", " + qx.html.Location.getPageBoxBottom(ele));
				  			
				  			
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
	               
	               /**
	                * Event Listener Function called when the Insert option of Context menu is clicked
	                * <p>It is called internally by the onClickMenu() function
	                * <p><p>
	                * 
	                * <p> *Example :- *
	                * <p> var insertCommand = new qx.client.Command();
        			* <p> insertCommand.addEventListener("execute", this._insertCmd,this);
	                * <p> var insertButton = new qx.ui.menu.Button("Insert",null,insertCommand);
	                * 
	                */
	               _insertCmd: function(e){
	               	
	               		//alert(e);	//Data Event
	               		this.insertWin = new qx.ui.window.Window("Insert");
						this.insertWin.setSpace(200, 400, 200, 250);
						var d = qx.ui.core.ClientDocument.getInstance();
						d.add(this.insertWin);
						
						var nameLabel = new qx.ui.basic.Label("Name:");
						nameLabel.setLocation(50, 50);
						
						this.nameText = new qx.ui.form.TextField();	//global .. MAYBE can make LOCAL .. try
						this.nameText.setLocation(100, 50);
						this.atom = new qx.ui.basic.Atom();
						this.atom.add(this.nameText);
						this.atom.setUserData('newNodeName',this.nameText);
						//this.nameText.setUserData('newNodeName',nameText);
						
						/*
						var parentLabel = new qx.ui.basic.Label("Parent:");
						parentLabel.setLocation(50, 100);
						
						var parentText = new qx.ui.form.TextField();
						parentText.setLocation(100, 100);
						*/
						
						var goButton = new qx.ui.form.Button("Insert");
				        goButton.setLocation(150, 150);
				        goButton.addEventListener("execute", this._insertDetails,this
				        /*
				        function(e) {
				          
						var c = {};
						c.name = nameText.getValue();
						c.parent = parentText.getValue();
						c.id = "node"+this._nodeId;
						this._nodeId++;
						this.addNode(c);
				        }*/);
						
						this.insertWin.add(nameLabel,this.nameText/*,parentLabel,parentText*/,goButton);
						this.insertWin.open();
						
						//alert("Window "+insertWin);									//window
						//alert("Type: " + typeof(insertWin));						//object
						//alert("children count: "+ insertWin.getChildrenLength()); 	//1
						//alert(insertWin.getChildren());								//vertical Box layout
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
						c.parent = this.getSelectedElement();
						c.id = "node"+this._nodeId;
						this._nodeId++;
						var node = this.addNode(c);
						this.insertWin.close();
						
						//node.setSelected(true);
						
	               },
	               	
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
          			
          			var treeRowStructure;
                 	var treeNode;
                 	
                 	if(config.checkBox != undefined){	// to check if its a standard node with just icon and name
                 										// ... or specia node with a icon, checkbox and name 
                 		
                 		treeRowStructure = qx.ui.tree.TreeRowStructure.getInstance().newRow();                 
                 		treeRowStructure.addIndent();
                 		if(config.init_icon != undefined && config.click_icon != undefined){
                 			treeRowStructure.addIcon(config.init_icon, config.click_icon);
                 		}
                 		// to add default image 
                 		if (config.checkBox == true){ 
                      		var obj = new qx.ui.form.CheckBox();
                      		treeRowStructure.addObject(obj, true);
                  		}
                  		
                  		treeRowStructure.addLabel(config.name);
                 	}
                 	
                 	else{			// if standard node with just Icon and name
                 		treeRowStructure = qx.ui.tree.TreeRowStructure.getInstance().standard(config.name);
                 	}	 
                  	
                  	if(config.file != undefined){	// to check if node is of type file or folder
                  		if(config.file == true){
                  			treeNode = new qx.ui.tree.TreeFile(treeRowStructure);
                  		}
                  		else{
                  			treeNode = new qx.ui.tree.TreeFolder(treeRowStructure);
                  		}
                  	}
                  	else{							// by default, node is of Folder type
                  		treeNode = new qx.ui.tree.TreeFolder(treeRowStructure);
                  	}                                                  
                  	
                  	//alert(config.parent);
                  	if(config.parent == this){ //instanceof qx.ui.tree.Tree){
                  		//alert('adding in tree');
                  		config.parent.add(treeNode);
                  		
                   	//this.setUserData(config.id, treeNode);
                  	}
                   	else{
                   		//alert('adding below tree');
                   	 	var p = config.parent.getUserData('node');
                   	 	p.add(treeNode);
                   	}
                   	
                   	
                   	 var atom = new qx.ui.basic.Atom();
        			 //atom.add(treeNode);//,config.data);
        			 
        			 atom.setUserData('node', treeNode);
        			 atom.setUserData('data', config.data);
        			 
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
	              * <p> var deleteCommand = new qx.client.Command();
        		  *	<p> deleteCommand.addEventListener("execute", this.deleteNode,this);
	              * <p> var deleteButton = new qx.ui.menu.Button("Delete",null,deleteCommand);
	              *                                               	               
	              */
	             deleteNode: function(e){
	               
	             	//var currentItem = this.getSelectedElement();
	             	
	             	 var currentItem = this.getManager().getSelectedItem();
	             	
	             	//alert(currentItem + ","+currentItem.getLabel());
	             	if(currentItem == this){
	             		alert("Root node cannot be deleted");
	             	}
          			else if (currentItem != null) {
          					
	              			currentItem.destroy();
	              			
	              			currentItem = null;
          			}
          			
          			//this.getSelectedElement().getParent().setSelected(true);	//else try getParentFolder()
          			
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
                 	
                 	var currentItem = this.getSelectedElement();
                 	var nodeParent = currentItem.getParentFolder();
                 	nodeParent.remove(currentItem);
                 	nodeParent.setSelected(true);
                 	
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
                 	return this.getSelectedElement();
                 },
                 
                 /**
                  * 
                  */
                 moveUpNode: function(){
                 	
                 	var selectionManager = this.getManager();
	               	var item = selectionManager.getSelectedItem();
	               	var previousItem = selectionManager.getPreviousSibling(item);
	               	var parentItem = item.getParentFolder();
	               	
	               	//parentItem.remove(item);
	               	parentItem.addBeforeToFolder(item,previousItem);
	               	
	               	item.setSelected(true);
                 },
                 
                 /**
                  * 
                  */
                 moveDownNode: function(){
                 	var selectionManager = this.getManager();
	               	var item = selectionManager.getSelectedItem();
	               	var nextItem = selectionManager.getNextSibling(item);
	               	var parentItem = item.getParentFolder();
	               	
	               	//parentItem.remove(item);
	               	parentItem.addAfterToFolder(item,nextItem);
	               	
	               	item.setSelected(true);
                 },
                 
                 /*
                 getNodeID: function(n){
                 	return this.getUserData();
                 },
                 */
                 
                 getNodeData: function(){
                 	
                 	var info = {};
                 	var node = this.getCurrentNode();
                 	
                 	var nodeId = node.getLabel();
                 	var atom = this.getUserData(nodeId);	//nodeid = label .. to be changed
                 	
                 	if(atom.getUserData('data') != undefined)
                 		info.data = atom.getUserData('data');
                 	
                 	info.tree = this;
                 	return info;
                 }
          }
});