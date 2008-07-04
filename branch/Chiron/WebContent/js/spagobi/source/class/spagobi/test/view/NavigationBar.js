qx.Class.define("spagobi.test.view.NavigationBar", {
	
	extend : qx.ui.toolbar.ToolBar,
	
	construct : function() {
		this.base(arguments);
		this.createNavBar();
	},
	
	members : {
		
		createNavBar : function(){
			//var addBtn = new qx.ui.toolbar.Button("", "icon/16/actions/dialog-ok.png");
			
			var firstPageButton = new qx.ui.Button("", "spagobi/img/spagobi/test/firstPage.png");
			this.add(firstPageButton);
		}	
	}
});
