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
	               		
	               		
	               		
          }
  
  
  
  }
  });