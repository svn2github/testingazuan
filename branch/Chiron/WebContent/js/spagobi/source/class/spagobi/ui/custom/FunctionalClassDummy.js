qx.Class.define("spagobi.ui.custom.FunctionalClassDummy",
{
  extend : qx.ui.splitpane.HorizontalSplitPane,
  
  construct : function(type)
  {
   // this.base(arguments, "1*", "2*");
    this.base(arguments,"1*","4*");//, "2*"); //  180
    this.setWidth("100%");
    this.setHeight("100%");
    this.setLiveResize(false);
    this.setShowKnob(false);
    
   
   if(type === 'funcManagement') { 
    var bottomPart = new qx.ui.layout.VerticalBoxLayout();
  		
  		var headerLabel = new qx.ui.basic.Label("Functionalities Tree");
  		with(headerLabel){
  			width = 200;
  			height = 300;
  		};
  		
  	var dummyTree = new spagobi.ui.Tree({root: "Functionalities" });
  	var tree = new spagobi.ui.Tree({root: "Functionalities" });
  	
  	var node1 = tree.addNode({
		  							name  : "Report",
		  							parent: tree,
		  							id	  : "node1"
  								});
  		var node2 = tree.addNode({
		  							name  : "OLAP",
		  							parent: tree,
		  							id	  : "node2"	
  								});
  		var node3 = tree.addNode({
		  							name  : "myOLAP",
		  							parent: node2,
		  							id	  : "node3",
		  							file  : true	
  								});
  		var node4 = tree.addNode({
		  							name  : "DashBoard",
		  							parent: tree,
		  							id	  : "node4",
		  							init_icon: "icon/16/places/user-trash.png",
		  							click_icon: "",
		  							checkBox  : true	
  								});
  		var node5 = tree.addNode({
		  							name  : "myDashBoardFolder",
		  							parent: node4,
		  							id	  : "node5",
		  							checkBox: true
		  						});
  		var node6 = tree.addNode({
		  							name  : "myDashBoard",
		  							parent: node4,
		  							id	  : "node6",
		  							checkBox: true,
		  							init_icon: "icon/16/places/user-desktop.png",
		  							click_icon: "",
		  							file  : true	
  								});
  								
  		tree.addEventListener("click",tree.onClickMenu,tree);						
  								
  		bottomPart.add(headerLabel, tree);	
  		
  		this.addLeft(bottomPart);
  		
  		var rightPart = new spagobi.ui.custom.FunctionalityTreeSubClass();
  		
  		this.addRight(rightPart);
   } 		
  }
  
});  