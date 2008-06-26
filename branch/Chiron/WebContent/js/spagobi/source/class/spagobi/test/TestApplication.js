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
    main : function()
    {
		this.base(arguments);  
		
		this._dataObjects = [];    
      	
      	 // Define alias for custom resource path
      	 qx.io.Alias.getInstance().add("spagobi", qx.core.Setting.get("spagobi.resource"));
      	
      	
      	
      	// Create Application Layout
      	this._createLayout();
      
      	this._loadDataObject();
      	this._tableView.getTableModel().setDataAsMapArray(this._dataObjects, true);
        this._tableView.getSelectionModel().setSelectionInterval(0, 0);
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
      	var horSplitPane = new qx.ui.splitpane.HorizontalSplitPane(100, "1*");
      	dockLayout.add(horSplitPane);

		
      	// Create vertical split pane
      	var vertSplitPane = new qx.ui.splitpane.VerticalSplitPane("1*", "2*");
      	vertSplitPane.setEdge(0);
      	vertSplitPane.setBorder("line-left");
      	horSplitPane.addRight(vertSplitPane);
		
		
      	// Create table view
      	this._tableView = new spagobi.test.view.Table(this);
      	vertSplitPane.addTop(this._tableView);
      	
      	// Create detail page
      	this._buttonView = new qx.ui.pageview.buttonview.ButtonView();
        this._buttonView.setEdge(0);     
                
        var saveButton = new qx.ui.pageview.buttonview.Button("Save", "spagobi/img/spagobi/test/save.png");
        var deleteButton = new qx.ui.pageview.buttonview.Button("Delete", "spagobi/img/spagobi/test/delete.png");
        var createButton = new qx.ui.pageview.buttonview.Button("New", "spagobi/img/spagobi/test/create.png");
                 
        this._buttonView.getBar().add(createButton, saveButton, deleteButton);   
      	this._form = new spagobi.ui.custom.EngineDetailsForm();       	      	
      	this._pageTabButton = new qx.ui.pageview.buttonview.Button("", "");
        this._pageTabButton.setDisplay(false);        
        this._pageTabButton.setChecked(true);  		
  		this._page = new qx.ui.pageview.buttonview.Page( this._pageTabButton );
        this._page.add( this._form );  		 
  		this._buttonView.getPane().add( this._page );
  		vertSplitPane.addBottom(this._buttonView);
  		
  		// create navigation page
  		var verticalLayout = new qx.ui.layout.VerticalBoxLayout();
  		verticalLayout.setSpacing(5);
  		var btn1 = new qx.ui.form.Button("", "spagobi/img/spagobi/test/save.png");
  		var btn2 = new qx.ui.form.Button("", "spagobi/img/spagobi/test/delete.png");
  		var btn3 = new qx.ui.form.Button("", "spagobi/img/spagobi/test/create.png");
  		verticalLayout.add(btn1);
  		verticalLayout.add(btn2);
  		verticalLayout.add(btn3);
  		
  		horSplitPane.addLeft(verticalLayout);      	
    },
    
    saveData: function() {
    	var dataObject = this.form.getData();
    	var prettyPrint = '';
    	for(prop in dataObject) {
    		prettyPrint += prop + ': ' + dataObject[prop] + ";\n";
    	}
        alert( prettyPrint );
    },
    
    createSaveButton : function() {
  		// Save button  
       
        //saveButton.setLeft(800);
        
        //var saveTooltip = new qx.ui.popup.ToolTip("Save","");
        //saveButton.setToolTip(saveTooltip);
        //saveTooltip.setShowInterval(10);      
            
            
        saveButton.addEventListener("execute", this.saveData, this);
        
        return saveButton;
  	},
  	
  	createDeleteButton : function() {
  		// Back Button
       
        //backButton.setLeft(810);
        
        //var backTooltip = new qx.ui.popup.ToolTip("Back","");
        //backButton.setToolTip(backTooltip);
        //backTooltip.setShowInterval(10);
        
        backButton.addEventListener("execute", function(){alert('go back')});
        
        return backButton;
  	},
    
    _loadDataObject: function() {
    	this._dataObjects = [
    		{
	        	id: '135',
	        	"label": 'JASPER',
	        	name: 'JasperReport Engine',
	        	description: 'Compatible with JasperReport engine v3.1',
	        	documentType: 'Map',
	        	engineType: 'External',
	        	useDataSet: false,
	        	useDataSource: true,
	        	dataSource: 'geo',
	        	"class": '',
	        	url: 'http://localhost:8080/SpagoBIJasperEngine/AdapterHTTP?ACTION_NAME=START_ACTION',
	        	driver: 'it.eng.spagobi.engines.drivers.QbeDriver'        	        	
        	}, {
	        	id: '137',
	        	"label": 'QBE',
	        	name: 'Qbe Engine',
	        	description: 'Query by Example',
	        	documentType: 'Map',
	        	engineType: 'External',
	        	useDataSet: false,
	        	useDataSource: true,
	        	dataSource: 'geo',
	        	"class": '',
	        	url: 'http://localhost:8080/SpagoBIQbeEngine/AdapterHTTP?ACTION_NAME=START_ACTION',
	        	driver: 'it.eng.spagobi.engines.drivers.QbeDriver'        	        	
        	}, {
	        	id: '138',
	        	"label": 'DASH',
	        	name: 'Dashboard Engine',
	        	description: 'Dashboard Engine',
	        	documentType: 'Map',
	        	engineType: 'Internal',
	        	useDataSet: false,
	        	useDataSource: true,
	        	dataSource: 'geo',
	        	"class": 'it.eng.spagobi.Dashboard',
	        	url: '',
	        	driver: ''        	        	
        	}
    	];    	
    },
    
    getDataObjects: function() {
    	return this._dataObjects;
    },
    
    selectDataObject: function(dataObject) {
    	this._form.setData(dataObject);
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