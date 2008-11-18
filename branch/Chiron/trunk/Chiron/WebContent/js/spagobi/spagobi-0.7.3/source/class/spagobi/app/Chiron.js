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
      	var applicationLayout = this._createLayout();
      	
      	var executionMode = qx.core.Setting.get("spagobi.executionMode");
      	if(executionMode && executionMode === 'portal') {
      		var d = qx.ui.core.ClientDocument.getInstance();

			var inlineWidget = new qx.ui.basic.Inline("myInlineWidget");

	        inlineWidget.setHeight("70%");
	        inlineWidget.setWidth("100%");
	        d.add(inlineWidget);
         
         	inlineWidget.add( applicationLayout );
      	} else {         	
         	applicationLayout.addToDocument();
         	var d = qx.ui.core.ClientDocument.getInstance();
	        // Color Themes
	        //qx.util.ThemeList.createMetaButtons(d, 600, 500);
      	}
      	
  
      	this._selectToolbar('resources');
      	
    },
    
    /**
     * Function which creates the Layout of the complete page.
     * <p> Defines a Dock Layout to which all the entities 
     * __Header, Top ToolBar, Vertical Toolbar and Page__ are added.
     */ 
    _createLayout: function() {
	   	var dockLayout = new qx.ui.layout.DockLayout();
        dockLayout.setEdge(0);
 //       dockLayout.setWidth(screen.width);
        dockLayout.setBackgroundColor('white');
        
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
		  		tooltip: 'Reload the feeds.',
		  		defaultbutton: true
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
	  		}, {
		  		command: 'Control+T',
		  		handler: function() {this._selectToolbar('adminDistributionList');},
		  		context: this,
		  		"label": 'Admin Distribution List',
		  		icon: 'icon/16/actions/dialog-ok.png',
		  		tooltip: 'Reload the feeds.'
	  		}, {
		  		command: 'Control+Y',
		  		handler: function() {this._selectToolbar('functionality');},
		  		context: this,
		  		"label": 'Functionality',
		  		icon: 'icon/16/actions/dialog-ok.png',
		  		tooltip: 'Reload the feeds.'
	  		}, {
		  		command: 'Control+U',
		  		handler: function() {this._selectToolbar('events');},
		  		context: this,
		  		"label": 'Events',
		  		icon: 'icon/16/actions/dialog-ok.png',
		  		tooltip: 'Reload the feeds.'
	  		}, {
		  		command: 'Control+I',
		  		handler: function() {this._selectToolbar('tools');},
		  		context: this,
		  		"label": 'Tools',
		  		icon: 'icon/16/actions/dialog-ok.png',
		  		tooltip: 'Reload the feeds.'
	  		}, {
		  		command: 'Control+O',
		  		handler: function() {this._selectToolbar('hotLinks');},
		  		context: this,
		  		"label": 'Hot Links',
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
      	dockLayout.add( this.toolbars['resources'] );
      	this.toolbars['resources'].setLiveResize(true);      	
      	this.toolbars['resources'].setVisibility(false);
      	
      	
      	this.toolbars['catalogues'] = new spagobi.ui.PageView({
      		toolbar: {
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
      	dockLayout.add( this.toolbars['catalogues'] );
      	this.toolbars['catalogues'].setLiveResize(true);      	
      	this.toolbars['catalogues'].setVisibility(false);
      	
      	this.toolbars['behaviouralModel'] = new spagobi.ui.PageView({
      		toolbar: {
      			buttons: [
      				{
      					name: 'lov',
						image:'spagobi/img/spagobi/test/lovManagementIcon.png',
						page: 'lov',
						tooltip: ' Predefined List of Values'
      				}, {
      					name: 'constraints',
						image:'spagobi/img/spagobi/test/constraintManagementIcon.png',
						page: 'constraints',
						tooltip: 'Predefined Values Constraints'
      				}, {
      					name: 'parameters',
						image:'spagobi/img/spagobi/test/parameterManagementIcon.png',
						page: 'parameters',
						tooltip: 'Analytical Drivers Management'
      				}
      			]
      		},
      		defaultSelectedPage: 'lov'
      	});
      	dockLayout.add( this.toolbars['behaviouralModel'] );
      	this.toolbars['behaviouralModel'].setLiveResize(true);
      	this.toolbars['behaviouralModel'].setVisibility(false);
      	
      	this.toolbars['analyticalModel'] = new spagobi.ui.PageView({
      		toolbar: {
      			buttons: [
      				{
      					name: 'funcManagement',
						image:'spagobi/img/spagobi/test/folderAdministrationIcon.png',
						page: 'funcManagement',
						tooltip: 'Functionalities Management'
      				}, {
      					name: 'configuration',
						image:'spagobi/img/spagobi/test/objectAdministrationIcon.png',
						page: 'configuration',
						tooltip: 'Documents Configuration'
      				}
      			]
      		},
      		defaultSelectedPage: 'funcManagement'
      	});
      	dockLayout.add( this.toolbars['analyticalModel'] );
      	this.toolbars['analyticalModel'].setLiveResize(true);
      	this.toolbars['analyticalModel'].setVisibility(false);
      	
      	
      	this.toolbars['adminDistributionList'] = new spagobi.ui.PageView({
      		toolbar: {
      			buttons: [
      				{
      					name: 'distributionList',
						image:'spagobi/img/spagobi/test/distributionlistuser.png',
						page: 'distributionList',
						tooltip: 'Distribution List User'
      				}, {
      					name: 'distributionListConfig',
						image:'spagobi/img/spagobi/test/distributionlist.png',
						page: 'distributionListConfig',
						tooltip: 'Distribution List Configuration'
      				}
      			]
      		},
      		defaultSelectedPage: 'distributionList'
      	});
      	dockLayout.add( this.toolbars['adminDistributionList'] );
        this.toolbars['adminDistributionList'].setLiveResize(true);
      	this.toolbars['adminDistributionList'].setVisibility(false);
      	
      	this.toolbars['functionality'] = new spagobi.ui.PageView({
      		toolbar: {
      			buttons: [
      				{
      					name: 'func',
						image:'spagobi/img/spagobi/test/mapManagementIcon.png',
						page: 'func',
						tooltip: 'Functionalities'
      				}
      			]
      		},
      		defaultSelectedPage: 'func'
      	});
      	dockLayout.add( this.toolbars['functionality'] );
        this.toolbars['functionality'].setLiveResize(true);
      	this.toolbars['functionality'].setVisibility(false);
      	
      	this.toolbars['events'] = new spagobi.ui.PageView({
      		toolbar: {
      			buttons: [
      				{
      					name: 'workflow',
						image:'spagobi/img/spagobi/test/todoList_2.png',
						page: 'workflow',
						tooltip: 'Workflow To Do List'
      				}, {
      					name: 'event',
						image:'spagobi/img/spagobi/test/events.png',
						page: 'event',
						tooltip: 'Events'
      				}
      			]
      		},
      		defaultSelectedPage: 'workflow'
      	});
      	dockLayout.add( this.toolbars['events'] );
        this.toolbars['events'].setLiveResize(true);
      	this.toolbars['events'].setVisibility(false);
      	
      	this.toolbars['tools'] = new spagobi.ui.PageView({
      		toolbar: {
      			buttons: [
      				{
      					name: 'tool',
						image:'spagobi/img/spagobi/test/importexport64.png',
						page: 'tool',
						tooltip: 'Import / Export '
      				}, {
      					name: 'schedule',
						image:'spagobi/img/spagobi/test/scheduleIcon64_blu.png',
						page: 'schedule',
						tooltip: 'Schedule Document Executions '
      				}, {
      					name: 'roles',
						image:'spagobi/img/spagobi/test/rolesynch64.jpg',
						page: 'roles',
						tooltip: 'Roles Synchronization'
      				}
      			]
      		},
      		defaultSelectedPage: 'tool'
      	});
      	dockLayout.add( this.toolbars['tools'] );
        this.toolbars['tools'].setLiveResize(true);
      	this.toolbars['tools'].setVisibility(false);
      	
      	this.toolbars['hotLinks'] = new spagobi.ui.PageView({
      		toolbar: {
      			buttons: [
      				{
      					name: 'link1',
						image:'spagobi/img/spagobi/test/todoList_2.png',
						page: 'link1',
						tooltip: 'Remember Me'
      				}, {
      					name: 'link2',
						image:'spagobi/img/spagobi/test/scheduleIcon64_blu.png',
						page: 'link2',
						tooltip: 'Most Popular'
      				}, {
      					name: 'link3',
						image:'spagobi/img/spagobi/test/engineAdministrationIcon.png',
						page: 'link3',
						tooltip: 'Most Recently Used'
      				}
      			]
      		},
      		defaultSelectedPage: 'link1'
      	});
      	dockLayout.add( this.toolbars['hotLinks'] );
        this.toolbars['hotLinks'].setLiveResize(true);
      	this.toolbars['hotLinks'].setVisibility(false);
      	//alert("1");//3
      	return dockLayout;  		
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
     /*
    _selectPage: function(pageName) {
    	if(!this.pages[pageName]) {
    		if (pageName == "funcManagement")
    			{
    				this._pages[pageName] = new spagobi.ui.custom.FunctionalClassDummy(pageName);
    			}
    		else
    			{	
    				this.pages[pageName] = new spagobi.ui.custom.MasterDetailsPage(pageName);
    			}
    		this.mainPane.addRight( this.pages[pageName] ); 		//HorizontalSplitPane
    		//this.mainPane.add( this.pages[pageName] );
    	}
    	
    	
    	if(this.selectPageName) {
    		this.pages[this.selectPageName].setVisibility(false);
    	}
    	this.selectPageName = pageName;
    		
    	//this.pages[pageName].show();
    	this._pages[pageName].setVisibility(true);
    	
    	/*
    	if(!this.pages[pageName].isVisibility()) {
    		this.pages[pageName].setVisibility(true);   
    	} 
    	 	
    	if(!this.pages[pageName]) {
    		this.pages[pageName] = new spagobi.test.view.MasterDetailsPage(pageName);
    		this.mainPane.addRight( this.pages[pageName] ); 
    	}
    	if(this.selectPageName) {
    		this.pages[pageName].moveSelfBefore( this.pages[this.selectPageName] );
    	}
    	this.selectPageName = pageName;
    	* /
    	  
    },
    */
    
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