qx.Class.define("spagobi.ui.PageView", {
	
	extend : qx.ui.splitpane.HorizontalSplitPane,
	
	construct : function(config) {
		this.base(arguments, 70, "1*");
		
		this._pages = [];
		
		var toolbarConfig = {
			defaultBackgroudColor: config.toolbar.defaultBackgroudColor,
			focusedBackgroudColor: config.toolbar.focusedBackgroudColor
		};
		this._toolbar = new spagobi.ui.IconBar( toolbarConfig );
		
		var buttonsConfig = config.toolbar.buttons;
		for(var i = 0; i < buttonsConfig.length; i++) {			
			buttonsConfig[i].handler =  function(e){this.selectPage(e.getTarget().getUserData('name'));};
			buttonsConfig[i].context = this;
			this._toolbar.addButton( buttonsConfig[i] );
		}
		
		if(config.defaultSelectedPage) {
			this._defaultSelectedPageName = config.defaultSelectedPage;
		}	
		
		this.addLeft( this._toolbar );	
	},
	
	members : {
		
		_toolbar: undefined,
		_pages: undefined,
		_selectedPageName: undefined,
		_defaultSelectedPageName: undefined,
		
		selectPage: function(pageName) {
    		if(!this._pages[pageName]) {
    			this._pages[pageName] = new spagobi.ui.custom.MasterDetailsPage(pageName);
    			this.addRight( this._pages[pageName] ); 
    		}
    	
    	
	    	if(this._selectedPageName) {
	    		this._pages[this._selectedPageName].setVisibility(false);
	    	}
	    	this._selectedPageName = pageName;
	    		
	    	this._pages[pageName].show();    	  
    	},
    	
    	selectDefaultPage: function() {
    		this.selectPage(this._defaultSelectedPageName);
    	},
    	
    	getSelectedPageName: function() {
    		return this._selectedPageName;
    	}
    	
    	
			
	}
});