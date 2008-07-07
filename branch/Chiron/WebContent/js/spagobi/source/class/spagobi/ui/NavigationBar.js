qx.Class.define("spagobi.ui.NavigationBar", {
	
	extend : qx.ui.toolbar.ToolBar,
	
	construct : function() {
		this.base(arguments);
		this.createNavBar();
		
	},
	
	members : {
		
		createNavBar : function(){
			//var addBtn = new qx.ui.toolbar.Button("", "icon/16/actions/dialog-ok.png");
			
			//this._toolBarView = new spagobi.test.view.ToolBar(this);
			
			var firstPageButton = new qx.ui.toolbar.Button("", "spagobi/img/spagobi/test/firstPage.png");
			this.add(firstPageButton);
			
			var prevPageButton = new qx.ui.toolbar.Button("", "spagobi/img/spagobi/test/previousPage.png");
			this.add(prevPageButton);
			
			this.add(new qx.ui.basic.HorizontalSpacer());
			
			var nextPageButton = new qx.ui.toolbar.Button("", "spagobi/img/spagobi/test/nextPage.png");
			this.add(nextPageButton);
			
			var lastPageButton = new qx.ui.toolbar.Button("", "spagobi/img/spagobi/test/lastPage.png");
			this.add(lastPageButton);
		}	
	}
});
