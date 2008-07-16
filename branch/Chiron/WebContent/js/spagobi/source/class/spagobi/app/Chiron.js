/*

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005 Engineering Ingegneria Informatica S.p.A.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

*/


/* *
 * @author Andrea Gioia (andrea.gioia@eng.it)
 * @author Amit Rana (amit.rana@eng.it)
 * @author Gaurav Jauhri (gaurav.jauhri@eng.it)
 * 
 */
 
/**
 * Main Application Class
 */ 
qx.Class.define("spagobi.app.Chiron",
{
  extend : qx.application.Gui,

  /*
  *****************************************************************************
     SETTINGS
  *****************************************************************************
  */

  /*	
  settings : {
    "spagobi.resource" : ".."
  },
  */

  /*
  *****************************************************************************
     MEMBERS
  *****************************************************************************
  */

  members :
  {
  	toolbars: [],
  	selectToolbarName: undefined,
  	pages: [],
  	selectPageName: undefined,
  	
  	mainPane: {},
  	
   	main : function()
    {
		this.base(arguments);
		
		
      	      	
      	 // Define alias for custom resource path
      	 qx.io.Alias.getInstance().add("spagobi", qx.core.Setting.get("spagobi.resourceUri"));
      	 //qx.io.Alias.getInstance().add("spagobi", qx.core.Setting.get("spagobi.test.resourceUri"));
      	 
      	 // Include CSS file
         qx.html.StyleSheet.includeFile(qx.io.Alias.getInstance().resolve("spagobi/css/reader.css"));
      	 
      	 // Increase parallel requests
      	qx.io.remote.RequestQueue.getInstance().setMaxConcurrentRequests(10);
      	   
      	
      	// Create Application Layout
      	this._createLayout();
      	
  
      	this._selectToolbar('resources');
      	//this._selectPage('engine');
      	
      	// React on theme selection changes
      	//qx.theme.manager.Meta.getInstance().addEventListener("changeTheme", this._applyCssTheme, this);
      	//this._applyCssTheme();
      	
    },
    
    /**
     * Function which creates the Layout of the complete page.
     * <p> Defines a Dock Layout to which all the entities 
     * __Header, Top ToolBar, Vertical Toolbar and Page__ are added.
     * 
     */ 
    _createLayout: function() {
	   	var dockLayout = new qx.ui.layout.DockLayout();
        dockLayout.setEdge(0);
        
        dockLayout.setBackgroundColor('white');
        
		dockLayout.addToDocument(); 
	  	
	  	// Create header
      	this._headerView = new spagobi.app.ui.Header();
      	dockLayout.addTop(this._headerView);

      	// Create toolbar
      	this._toolBarView = new spagobi.ui.ToolBar([
      		{
		  		command: 'Control+Q',
		  		handler: function() {this._selectToolbar('resources');},
		  		context: this,
		  		"label": 'Resources',
		  		icon: 'icon/16/actions/dialog-ok.png',
		  		tooltip: 'Reload the feeds.'
	  		}, {
		  		command: 'Control+W',
		  		handler: function() {this._selectToolbar('catalogues');},
		  		context: this,
		  		"label": 'Catalogues',
		  		icon: 'icon/16/actions/dialog-ok.png',
		  		tooltip: 'Reload the feeds.'
	  		}, {
		  		command: 'Control+E',
		  		handler: function() {this._selectToolbar('behaviouralModel');},
		  		context: this,
		  		"label": 'Behavioural Model',
		  		icon: 'icon/16/actions/dialog-ok.png',
		  		tooltip: 'Reload the feeds.'
	  		}, {
		  		command: 'Control+R',
		  		handler: function() {this._selectToolbar('analyticalModel');},
		  		context: this,
		  		"label": 'Analytical Model',
		  		icon: 'icon/16/actions/dialog-ok.png',
		  		tooltip: 'Reload the feeds.'
	  		}
      	]);
      	      	
      	dockLayout.addTop(this._toolBarView);
      	
      	// Create horizontal split pane
      	
      	this.toolbars['resources'] = new spagobi.ui.PageView({
      		toolbar: {
      			//defaultBackgroudColor: 'white',
      			//focusedBackgroudColor: '#DEFF83',
      			buttons: [
      				{
      					name: 'engine',
						image:'spagobi/img/spagobi/test/engineAdministrationIcon.png',
						page: 'engine',
						tooltip: 'Engines'
      				}, {
      					name: 'datasource',
						image:'spagobi/img/spagobi/test/datasourceAdministrationIcon.png',
						page: 'datasource',
						tooltip: 'Datasources'
      				}, {
      					name: 'dataset',
						image:'spagobi/img/spagobi/test/datasetAdministrationIcon.png',
						page: 'dataset',
						tooltip: 'Dataset'
      				}
      			]
      		},
      		defaultSelectedPage: 'engine'
      	});
      	//this.toolbars['resources'].selectPage('engine');
      	dockLayout.add( this.toolbars['resources'] );
      	this.toolbars['resources'].setLiveResize(true);
      	
      	this.toolbars['resources'].setVisibility(false);
      	
      	
      	this.toolbars['catalogues'] = new spagobi.ui.PageView({
      		toolbar: {
      			defaultBackgroudColor: 'gray',
      			focusedBackgroudColor: '#DEFF83',
      			buttons: [
      				{
      					name: 'mapmgmt',
						image:'spagobi/img/spagobi/test/mapManagementIcon.png',
						page: 'mapmgmt',
						tooltip: 'Maps'
      				}, {
      					name: 'featuremgmt',
						image:'spagobi/img/spagobi/test/featureManagementIcon.png',
						page: 'featuremgmt',
						tooltip: 'Features'
      				}
      			]
      		},
      		defaultSelectedPage: 'mapmgmt'
      	});
      	//this.toolbars['catalogues'].selectPage('mapmgmt');
      	dockLayout.add( this.toolbars['catalogues'] );
      	this.toolbars['catalogues'].setLiveResize(true);
      	
      	this.toolbars['catalogues'].setVisibility(false);
      	
      	//My code starts
      	/*
      	this.toolbars['behaviouralModel'] = new spagobi.ui.PageView({
      		toolbar: {
      			defaultBackgroudColor: 'white',
      			focusedBackgroudColor: '#DEFF83',
      			buttons: [
      				{
      					name: 'mapmgmt',
						image:'spagobi/img/spagobi/test/predefinedLoVIcon.png',
						page: 'mapmgmt'
      				}, {
      					name: 'featuremgmt',
						image:'spagobi/img/spagobi/test/predefinedValConstraintsIcon.png',
						page: 'featuremgmt'
      				}
      			]
      		},
      		defaultSelectedPage: 'mapmgmt'
      	});
      	
      	dockLayout.add( this.toolbars['behaviouralModel'] );
      	this.toolbars['behaviouralModel'].setLiveResize(true);
      	this.toolbars['behaviouralModel'].setVisibility(false);
      	*/
      	 
      	 //My code ends
      	
      	/*
      	this.mainPane = new qx.ui.splitpane.HorizontalSplitPane(70, "1*");
      	dockLayout.add(this.mainPane);

    	// create RESOURCES iconbar
    	var resourcesIconBar = new spagobi.ui.IconBar();
  		resourcesIconBar.addButton("", "spagobi/img/spagobi/test/engineAdministrationIcon.png",function(){this._selectPage('engine');}, this)
  		resourcesIconBar.addButton("", "spagobi/img/spagobi/test/datasourceAdministrationIcon.png",function(){this._selectPage('datasource');}, this)
  		resourcesIconBar.addButton("", "spagobi/img/spagobi/test/datasetAdministrationIcon.png",function(){this._selectPage('dataset');}, this)
  		this.mainPane.setLiveResize(true);
  		this.mainPane.addLeft(resourcesIconBar);
  		resourcesIconBar.setVisibility(false);
  		this.toolbars['resources'] = resourcesIconBar;
  		
  		// create CATALOGUES iconbar
  		var cataloguesIconBar = new spagobi.ui.IconBar();
  		cataloguesIconBar.addButton("", "spagobi/img/spagobi/test/mapManagementIcon.png",function(){this._selectPage('mapmgmt');}, this)
  		cataloguesIconBar.addButton("", "spagobi/img/spagobi/test/featureManagementIcon.png",function(){this._selectPage('featuremgmt');}, this)	
  		this.mainPane.setLiveResize(true);
  		this.mainPane.addLeft(cataloguesIconBar);
  		cataloguesIconBar.setVisibility(false);
  		this.toolbars['catalogues'] = cataloguesIconBar;
  		*/
  		
    },
    
    /**
     * Function to set the vertical bar (on left side of page) corresponding to the 
     * button selected on top toolbar.
     * <p>It hides the previously selected vertical toolbar and shows the
     * currently selected vertical toolbar.
     * <p>It the toolbar functionality is not yet implemented, it displays
     * a pop-up alert box.
     * 
     * <p>*Example*
     * <p><code> _selectToolbar('resources'); </code>
     * <p> shows the vertical toolbar on the left side of page with buttons 
     * corresponding to "Resources" button on the top tool bar.
     * 
     * @param toolbarName {String} The name of the Toolbar button
     */
    _selectToolbar: function(toolbarName) {
    	if(this.toolbars[toolbarName]) {
    		if(this.selectToolbarName) {
    			this.toolbars[this.selectToolbarName].setVisibility(false);
    		}
    		this.toolbars[toolbarName].setVisibility(true);
    		if(!this.toolbars[toolbarName].getSelectedPageName()) {
    			this.toolbars[toolbarName].selectDefaultPage();
    		}
    		this.selectToolbarName = toolbarName;
    	} else {
    		alert(toolbarName + ' is not yet implemented !');
    	}
    },
    
    /**
     * Function to show the selected page based on the button selected on left
     * vertical bar.
     * <p> It hides the previously selected page and shows the currently
     * selected page.
     *
     * @param pageName {String} Name of the page to be displayed
     */
    _selectPage: function(pageName) {
    	if(!this.pages[pageName]) {
    		this.pages[pageName] = new spagobi.ui.custom.MasterDetailsPage(pageName);
    		this.mainPane.addRight( this.pages[pageName] ); 
    	}
    	
    	
    	if(this.selectPageName) {
    		this.pages[this.selectPageName].setVisibility(false);
    	}
    	this.selectPageName = pageName;
    		
    	this.pages[pageName].show();
    	
    	/*
    	if(!this.pages[pageName].isVisibility()) {
    		this.pages[pageName].setVisibility(true);   
    	} 
    	*/
    	/* 	
    	if(!this.pages[pageName]) {
    		this.pages[pageName] = new spagobi.test.view.MasterDetailsPage(pageName);
    		this.mainPane.addRight( this.pages[pageName] ); 
    	}
    	if(this.selectPageName) {
    		this.pages[pageName].moveSelfBefore( this.pages[this.selectPageName] );
    	}
    	this.selectPageName = pageName;
    	*/
    	  
    },
    
    /**
     * Function to set the theme of the page
     */
    _applyCssTheme : function() {
    	alert(document.body.className = qx.theme.manager.Meta.getInstance().getTheme());
       document.body.className = qx.theme.manager.Meta.getInstance().getTheme() == qx.theme.Ext ? "Ext" : "Classic";
       //document.body.className = "Ext";
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