qx.Class.define("spagobi.ui.custom.FunctionalTree",{

   
  //extend : qx.ui.core.Widget,//change
  extend : spagobi.ui.Tree,
  
  construct : function()  {//type
 
	  		//this.base(arguments);//change
	  		this.base(arguments,{root: "Functionalities"});  
		  	//var layout = new qx.ui.layout.Canvas();//change
		  	//this._setLayout(layout);//change
		  	this.createFunctionalTree();
		  	
		  	
 },
  
  members : {
  	
  			//tree : undefined,//change
  			
  			createFunctionalTree : function(){
  			
  		 		//this.tree = new spagobi.ui.Tree({root: "Functionalities"});//change	
  		
  				//var node1 = this.tree.addNode({//change
  				var node1 = this.addNode({	
		  							name  : "Report",
		  							//parent: this.tree,//change
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
  
  
          _createMenu : function(e){
          				//e is Qooxdoo wrapper event for Javascript mouseevent e._native
          				//alert(spagobi.commons.CoreUtils.toStr(e._native));
          				
          				var moveUpCmd = new qx.event.Command();
        				moveUpCmd.addListener("execute", this.moveUpNode,this);
        				
        				var moveDownCmd = new qx.event.Command();
        				moveDownCmd.addListener("execute", this.moveDownNode,this);
          				
          				var contextMenu = new qx.ui.menu.Menu;
          				
	             		var insertButton = new qx.ui.menu.Button("Insert");//,null,insertCmd); // handleClick(var vItem, Event e)
	               		var deleteButton = new qx.ui.menu.Button("Delete");//,null,deleteCmd);
	             	  	var moveUpButton = new qx.ui.menu.Button("Move Up",null,moveUpCmd);
	             	  	var moveDownButton = new qx.ui.menu.Button("Move Down",null,moveDownCmd);
	               		
	               		var node = this.getSelectedItem();
	               		
	               		if(node == this.getRoot()){		// If Root Node
	               				contextMenu.add(insertButton);
		               			contextMenu.add(deleteButton);
	               				
	               		} else {
		               		//	var selectionManager = this.getManager();
		               	//		var item = this.getSelectedItem();
		               			
		               			if(node instanceof qx.ui.tree.TreeFile){			//leaf nodes don't have insert option	
		               				
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
		               			} else{															// For folders, you have insert option even if they are at leaf
		               				if(this.getPreviousSibling(node) == undefined){		// first child cannot be moved up
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
	                 				else if (this.getNextSibling(node) == undefined && this.getPreviousSibling(node) == undefined ){
	                 					contextMenu.add(insertButton);
		               					contextMenu.add(deleteButton);
	                 					
	                       				}
	                 				else{
		               					contextMenu.add(insertButton);
		               					contextMenu.add(deleteButton);
		               					contextMenu.add(moveUpButton);
		               					contextMenu.add(moveDownButton);
	                 				}
	               				}
	               		}
  						
	               		
	               	/*	contextMenu.add(insertButton);
	               		contextMenu.add(deleteButton);
	               		contextMenu.add(moveUpButton);
	               		contextMenu.add(moveDownButton);
	               		
	                */
	             //		var ele = node.getContentElement();
	               			//alert(node.getDecorator() );//ele.getDecorator() not valid
	               			//alert(this._getLayout.get(left));
	               		
	               		this.getApplicationRoot().add(contextMenu, {left: e._native.clientX, top: e._native.clientY});
	               		//this._add(contextMenu, {left: e._native.clientX, top: e._native.clientY});
	               		contextMenu.setOpener(node);
	               		
	               		
	               		/*
	               		var leftCord = qx.bom.element.Location.getLeft(ele);
	               		var topCord = qx.bom.element.Location.getTop(ele);
	               		alert(leftCord + "," + topCord);
	               		*/
	               		
	               		contextMenu.show();
	               		
	               		/*
	              		var button = new qx.ui.form.MenuButton("Menu Button",qx.util.AliasManager.getInstance().resolve('spagobi/img/spagobi/test/preferences-users.png'), contextMenu);
	               		//button.setMaxWidth(50);
	               		//button.setMaxHeight(50);
	               		//button.setMargin(30,20,30,20);
	             //  		button.setVisibility("excluded");
	               		button.setEnabled(true);
	               		//button.setVisibility("hidden");
	              // 		button.setMarginTop(50);
	              //	button.setVisibility("visible");
	               	//	button.setIconPosition({left: 20, top: 20});
	               //	alert("amit");,
	       //           e._add(contextMenu);
	              // 	this.setContextMenu(contextMenu);
	               		this._add(button);//{edge:"north",height: "10%", width: "10%"});
	               		
	               		if (button.isEnabled()== true)
	               		{
	               			button.setEnabled(false);
	               			button.setVisibility("hidden");
	               		}
          				*/
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
  
  
  
  }
  });