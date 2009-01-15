qx.Class.define("spagobi.ui.custom.FunctionalTree",{

  extend : spagobi.ui.Tree,
  
  construct : function()  {//type
 
	  		this.base(arguments,{root: "Functionalities"});  
		  	this.createFunctionalTree();
		  			  	
  },
  
  members : {
  	
  			insertWin: undefined,
  			
  			createFunctionalTree : function(){
  			
  		 		var node1 = this.addNode({	
		  							name  : "Report",
		  							parent: this,
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
		  					
	  				
  				var node2 = this.addNode({
		  							name  : "OLAP",
		  							parent: this,
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
  				var node3 = this.addNode({
		  							name  : "myOLAP",
		  							parent: node2,
		  							file : true,
		  							data  : {
		  							 			label : 'myOLAP Label',
		  							 			name  : 'myOLAP Name',
		  							 			desc  : 'myOLAP Desc'
		  							 		}
  								});
  				var node4 = this.addNode({
		  							name  : "DashBoard",
		  							parent: this,
		  							data  : {
		  							 			label : 'DashBoardLabel',
		  							 			name  : 'DashBoardName',
		  							 			desc  : 'DashBoardDesc'
		  							 		}	
  								});
  				var node5 = this.addNode({
		  							name  : "myDashBoardFolder",
		  							parent: node4,
		  							file : true, 
		  							data  : {
		  							 			label : 'myDashBoardFolderLabel',
		  							 			name  : 'myDashBoardFolderName',
		  							 			desc  : 'myDashBoardFolderDesc'
		  							 		}
		  						});
  				var node6 = this.addNode({
		  							name  : "myDashBoard",
		  							parent: node4,
		  							file : true, 
		  							data  : {
		  							 			label : 'myDashBoard Label',
		  							 			name  : 'myDashBoard Name',
		  							 			desc  : 'myDashBoard Desc'
		  							 		}
  								});
  		
  			//this._add(this.tree, {height: "100%"});
  		
  			this.addListener("contextmenu",this._createMenu,this);	//right-click calls the function
  			
  	},
  
  /*
          _createMenu : function(e){
          				//e is Qooxdoo wrapper event for Javascript mouseevent e._native
          				//alert(spagobi.commons.CoreUtils.toStr(e._native));
          				var contextMenu = new qx.ui.menu.Menu;
          				
	             		var insertButton = new qx.ui.menu.Button("Insert");//,null,insertCmd); // handleClick(var vItem, Event e)
	               		var deleteButton = new qx.ui.menu.Button("Delete");//,null,deleteCmd);
	             //  	var moveUpButton = new qx.ui.menu.Button("Move Up");//,null,moveUpCmd);
	             //  	var moveDownButton = new qx.ui.menu.Button("Move Down");//,null,moveDownCmd);
	               		
	               		contextMenu.add(insertButton);
	               		contextMenu.add(deleteButton);
	               		
	               		//var node = this.tree.getSelectedItem();
	               		//var ele = node.getContentElement();
	               		//this._getRoot().add(contextMenu, {left: e._native.clientX, top: e._native.clientY});
	               		this.getApplicationRoot().add(contextMenu, {left: e._native.clientX, top: e._native.clientY});
	               		contextMenu.setOpener(this.getSelectedItem());
	               		
	               		contextMenu.show();
	               		
	               		
	               		
          },
   */    
          _createMenu : function(e){
          				//e is Qooxdoo wrapper event for Javascript mouseevent e._native
          				//alert(spagobi.commons.CoreUtils.toStr(e._native));
          				var insertCmd = new qx.event.Command();
        				insertCmd.addListener("execute", this._insertCmd,this);
        				
	               		var deleteCmd = new qx.event.Command();
        				deleteCmd.addListener("execute", this.deleteNode,this);
        				
          				var moveUpCmd = new qx.event.Command();
        				moveUpCmd.addListener("execute", this.moveUpNode,this);
        				
        				var moveDownCmd = new qx.event.Command();
        				moveDownCmd.addListener("execute", this.moveDownNode,this);
          				
          				var contextMenu = new qx.ui.menu.Menu;
          				
	             		var insertButton = new qx.ui.menu.Button("Insert",null,insertCmd); 
	               		var deleteButton = new qx.ui.menu.Button("Delete",null,deleteCmd);
	               		var moveUpButton = new qx.ui.menu.Button("Move Up",null,moveUpCmd);
	             	  	var moveDownButton = new qx.ui.menu.Button("Move Down",null,moveDownCmd);
	               		
	               		var node = this.getSelectedItem();
	               		
	               		if(node == this.getRoot()){		// If Root Node
	               				contextMenu.add(insertButton);
		               			//contextMenu.add(deleteButton);
	               				
	               		} else {
		               		//	var selectionManager = this.getManager();
		               		//	var item = this.getSelectedItem();
		               			
		               			if(node instanceof qx.ui.tree.TreeFile){	//leaf nodes don't have insert option	
		               				
		               				if (this.getNextSibling(node) == undefined && this.getPreviousSibling(node) == undefined ){
	                 					contextMenu.add(deleteButton);
	                 				}
		               			
		               				else if(this.getNextSibling(node) == undefined){	// last child cannot be moved down
		               					contextMenu.add(deleteButton);
		               					contextMenu.add(moveUpButton);               				
		               				}
	                 				else if(this.getPreviousSibling(node) == undefined){		// first child cannot be moved up
		               					contextMenu.add(deleteButton);
		               					contextMenu.add(moveDownButton);
		               				}
	                 				else{
		               					contextMenu.add(deleteButton);
		               					contextMenu.add(moveUpButton);
		               					contextMenu.add(moveDownButton);
		               					
		               				}	
		               			} else{		// For folders, you have insert option even if they are at leaf
		               				if (this.getNextSibling(node) == undefined && this.getPreviousSibling(node) == undefined ){
	                 					contextMenu.add(insertButton);
		               					contextMenu.add(deleteButton);
	                 				}
		               				else if(this.getPreviousSibling(node) == undefined){		// first child cannot be moved up
		               					contextMenu.add(insertButton);
		               					contextMenu.add(deleteButton);
		               					contextMenu.add(moveDownButton);
		               				}
		               				else if(this.getNextSibling(node) == undefined){	// last child cannot be moved down
		               					contextMenu.add(insertButton);
		               					contextMenu.add(deleteButton);
		               					contextMenu.add(moveUpButton);	               				
		               				}
	                 				//var previousItem = selectionManager.getPrevious(this.getSelectedItem()); //getLeadItem(), getNextSibling, getPreviousSibling, 
	                 				 
	                 				else{
		               					contextMenu.add(insertButton);
		               					contextMenu.add(deleteButton);
		               					contextMenu.add(moveUpButton);
		               					contextMenu.add(moveDownButton);
	                 				}
	               				}
	               		}
	               		this.getApplicationRoot().add(contextMenu, {left: e._native.clientX, top: e._native.clientY});
	               		contextMenu.setOpener(this.getSelectedItem());
	               		contextMenu.show();	
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
						
						//node.setSelected(true);
						
	               }
  
  
  }
  });