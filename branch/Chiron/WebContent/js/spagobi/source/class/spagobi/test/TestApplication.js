/* ************************************************************************

   Copyright:

   License:

   Authors:

************************************************************************ */

/* ************************************************************************

#resource(custom.image:image)

// List all static resources that should be copied into the build version,
// if the resource filter option is enabled (default: disabled)
#embed(qx.icontheme/32/status/dialog-information.png)
#embed(custom.image/test.png)

************************************************************************ */

/**
 * Your custom application
 */
qx.Class.define("spagobi.test.TestApplication",
{
  extend : qx.application.Gui,

  /*
  *****************************************************************************
     SETTINGS
  *****************************************************************************
  */

  settings : {
    "spagobi.resource" : ".."
  },


  /*
  *****************************************************************************
     MEMBERS
  *****************************************************************************
  */

  members :
  {
  	pages: [],
  	selectPageName: undefined,
  	
  	mainPane: {},
  	
  	
    main : function()
    {
		this.base(arguments);
      	
      	 // Define alias for custom resource path
      	 qx.io.Alias.getInstance().add("spagobi", qx.core.Setting.get("spagobi.resource"));
      	 
      	 // Include CSS file
         qx.html.StyleSheet.includeFile(qx.io.Alias.getInstance().resolve("spagobi/css/reader.css"));
      	 
      	      	
      	// Create Application Layout
      	this._createLayout();
      	
      	this._selectPage('engine');
      	
      	// React on theme selection changes
      	//qx.theme.manager.Meta.getInstance().addEventListener("changeTheme", this._applyCssTheme, this);
      	//this._applyCssTheme();
      	
    },
     
    _createLayout: function() {
    	var dockLayout = new qx.ui.layout.DockLayout();
        dockLayout.setEdge(0);
	  	dockLayout.addToDocument(); 
	  	
	  	// Create header
      	this._headerView = new spagobi.test.view.Header();
      	dockLayout.addTop(this._headerView);

      	// Create toolbar
      	this._toolBarView = new spagobi.test.view.ToolBar(this);
      	dockLayout.addTop(this._toolBarView);
      	
      	// Create horizontal split pane
      	this.mainPane = new qx.ui.splitpane.HorizontalSplitPane(70, "1*");
      	dockLayout.add(this.mainPane);

      	//var mainPage = new spagobi.test.view.MasterDetailsPage('datasource');      	
      	//mainPane.addRight( mainPage );      
  		
  		// create navigation page
  		var verticalLayout = new qx.ui.layout.VerticalBoxLayout();
  		verticalLayout.setSpacing(5);
  		var btn1 = new qx.ui.form.Button("", "spagobi/img/spagobi/test/engineAdministrationIcon.png");
  		btn1.addEventListener("execute", function(){this._selectPage('engine');}, this);
  		
  		var btn2 = new qx.ui.form.Button("", "spagobi/img/spagobi/test/datasourceAdministrationIcon.png");
  		btn2.addEventListener("execute", function(){this._selectPage('datasource');}, this);
  		
  		var btn3 = new qx.ui.form.Button("", "spagobi/img/spagobi/test/datasetAdministrationIcon.png");
  		btn3.addEventListener("execute", function(){this._selectPage('dataset');}, this);
  		
  		verticalLayout.add(btn1);
  		verticalLayout.add(btn2);
  		verticalLayout.add(btn3);
  		
  		this.mainPane.addLeft(verticalLayout);      	
    },
    
    _selectPage: function(pageName) {
    	if(!this.pages[pageName]) {
    		this.pages[pageName] = new spagobi.test.view.MasterDetailsPage(pageName);
    		this.mainPane.addRight( this.pages[pageName] ); 
    	}
    	if(this.selectPageName) {
    		this.pages[this.selectPageName].setVisibility(false);
    	}
    	this.selectPageName = pageName;
    	if(!this.pages[pageName].isVisibility()) {
    		this.pages[pageName].setVisibility(true);   
    	} 	  
    },
    
    _applyCssTheme : function() {
    	alert(document.body.className = qx.theme.manager.Meta.getInstance().getTheme());
       document.body.className = qx.theme.manager.Meta.getInstance().getTheme() == qx.theme.Ext ? "Ext" : "Classic";
       //document.body.className = "Ext";
    },
       
    reload: function() {
    	alert('reload');
    },
    showAbout: function() {
    	alert('showAbout');
    },
    showPreferences: function() {
    	alert('showPreferences');
    },
    showAddFeed: function() {
    	alert('showAddFeed');
    },
    showRemoveFeed: function() {
    	alert('showRemoveFeed');
    },
    
    


    /**
     * TODOC
     *
     * @type member
     */
    close : function()
    {
      this.base(arguments);

      // Prompt user
      // return "Do you really want to close the application?";
    },


    /**
     * TODOC
     *
     * @type member
     */
    terminate : function() {
      this.base(arguments);
    }
  }




  
});