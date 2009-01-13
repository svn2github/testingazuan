qx.Class.define("spagobi.ui.custom.FunctionalTree",{

  extend : qx.ui.core.Widget,
  
  construct : function(type)  {
 
	  		this.base(arguments);  
		  	var layout = new qx.ui.layout.Canvas();
		  	this._setLayout(layout);
		  	this.createFunctionalTree();
		  	
		  	
 },
  
  members :
			
  
  {
  	
  			tree : undefined,
  			
  			createFunctionalTree : function(){
  			
  		 this.tree = new spagobi.ui.Tree({root: "Functionalities"});	
  		
  		var node1 = this.tree.addNode({
		  							name  : "Report",
		  							parent: this.tree,
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
		  					
	  				
  		var node2 = this.tree.addNode({
		  							name  : "OLAP",
		  							parent: this.tree,
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
  		var node3 = this.tree.addNode({
		  							name  : "myOLAP",
		  							parent: node2,
		  							file : true,
		  							data  : {
		  							 			label : 'myOLAP Label',
		  							 			name  : 'myOLAP Name',
		  							 			desc  : 'myOLAP Desc'
		  							 		}
  								});
  		var node4 = this.tree.addNode({
		  							name  : "DashBoard",
		  							parent: this.tree,
		  							data  : {
		  							 			label : 'DashBoardLabel',
		  							 			name  : 'DashBoardName',
		  							 			desc  : 'DashBoardDesc'
		  							 		}	
  								});
  		var node5 = this.tree.addNode({
		  							name  : "myDashBoardFolder",
		  							parent: node4,
		  							file : true, 
		  							data  : {
		  							 			label : 'myDashBoardFolderLabel',
		  							 			name  : 'myDashBoardFolderName',
		  							 			desc  : 'myDashBoardFolderDesc'
		  							 		}
		  						});
  		var node6 = this.tree.addNode({
		  							name  : "myDashBoard",
		  							parent: node4,
		  							file : true, 
		  							data  : {
		  							 			label : 'myDashBoard Label',
		  							 			name  : 'myDashBoard Name',
		  							 			desc  : 'myDashBoard Desc'
		  							 		}
  								});
  		
  		this._add(this.tree, {height: "100%"});
  		
  		this.tree.addListener("contextmenu",this._createMenu,this);	
  
  },
  
  
          _createMenu : function(e){
          				//alert("hi");
          			//	alert(typeof e);
          		//      e.get
          				var contextMenu = new qx.ui.menu.Menu;
          				contextMenu.setOpener(this.tree.getSelectedItem());
          				
	             		var insertButton = new qx.ui.menu.Button("Insert");//,null,insertCmd); // handleClick(var vItem, Event e)
	               		var deleteButton = new qx.ui.menu.Button("Delete");//,null,deleteCmd);
	             //  	var moveUpButton = new qx.ui.menu.Button("Move Up");//,null,moveUpCmd);
	             //  	var moveDownButton = new qx.ui.menu.Button("Move Down");//,null,moveDownCmd);
	               		
	               		contextMenu.add(insertButton);
	               		contextMenu.add(deleteButton);
	               		
	               		
	               		//var temp = this.tree.getSelectedItem();
	               		//alert(temp + ", "+ typeof(temp));
	               		var e = this.tree.getSelectedItem().getContentElement();
	               		//alert(e.getLeft());
	               		this._add(contextMenu/*, {left: , top: }*/);
	               		
	               		contextMenu.show();
	               		/*
	              		var button = new qx.ui.form.MenuButton("Menu Button",qx.util.AliasManager.getInstance().resolve('spagobi/img/spagobi/test/preferences-users.png'), contextMenu);
	               		button.setMaxWidth(50);
	               		button.setMaxHeight(50);
	               		button.setMargin(30,20,30,20);
	             //  		button.setVisibility("excluded");
	               		button.setEnabled(true);
	               		
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
          }
  
  
  
  }
  });