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
 
/*
#asset(img/spagobi/test/*)
#asset(qx/icon/Oxygen/16/actions/dialog-ok.png)

*/ 
 
qx.Class.define("spagobi.app.Chiron",
{
  extend : qx.application.Standalone,
	
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
  	_container: undefined,//change..added
  	containerToolBar: undefined,//chnage.. added
  	mainPane: {},
  	//_defaultToolbarButton: undefined,
  	
    main : function()
    {
		 this.base(arguments);
         // Define alias for custom resource path
      	 //qx.util.AliasManager.getInstance().add("spagobi", qx.core.Setting.get("spagobi.resourceUri"));
      	 //qx.util.AliasManager.getInstance().add("libResource", qx.core.Setting.get("qx.resourceUri"));      	 
      	 qx.util.AliasManager.getInstance().add("spagobi", qxlibraries.spagobi.resourceUri);
      	 qx.util.AliasManager.getInstance().add("libResource", qxlibraries.qx.resourceUri);      	 
      	   
      	   
      	   
      	 // Include CSS file
         //qx.legacy.html.StyleSheet.includeFile(qx.util.AliasManager.getInstance().resolve("spagobi/css/reader.css"));//change
      	 qx.bom.Stylesheet.includeFile(qx.util.AliasManager.getInstance().resolve("spagobi/css/reader.css"));//change2
      	       	 
      	 // Increase parallel requests
      	qx.io.remote.RequestQueue.getInstance().setMaxConcurrentRequests(10);
      	
      	// Create Application Layout
      	var applicationLayout = this._createLayout();
      	
      	var executionMode = qx.core.Setting.get("spagobi.executionMode");
      	if(executionMode && executionMode === 'portal') {
      		//var d = qx.legacy.ui.core.ClientDocument.getInstance();//change
			var d = this.getRoot();
			
			//var inlineWidget = new qx.legacy.ui.basic.Inline("myInlineWidget");//change
			var inlineWidget = new qx.ui.root.Inline("myInlineWidget");//change
			
	        inlineWidget.setHeight("70%");
	        inlineWidget.setWidth("100%");
	        d.add(inlineWidget);
         
         	inlineWidget.add( applicationLayout );
      	} else {
      		this.getRoot().add(applicationLayout, {edge:0});//, {edge:0} //, width: "100%"
      		/*
         	var str = "Hello World";
			var myLabel = new qx.ui.basic.Label();
			myLabel.set({content: this.tr(str)});
			alert(myLabel.getContent());
			*/    
      	}
      	
      	//this._defaultToolbarButton = 'resources';
      	this._selectToolbar('resources');
     	//this._selectToolbar(this._defaultToolbarButton);
    },
    
    /**
     * Function which creates the Layout of the complete page.
     * <p> Defines a Dock Layout to which all the entities 
     * __Header, Top ToolBar, Vertical Toolbar and Page__ are added.
     */ 
    _createLayout: function() {
	   	//var dockLayout = new qx.legacy.ui.layout.DockLayout();//change
	   	var dockLayout = new qx.ui.layout.Dock();
	   	dockLayout.setSeparatorY("separator-vertical");
	   	//var dockLayout = new qx.ui.layout.Canvas();//trying canvas instead..not working
      
        //dockLayout.setEdge(0);//change .. find equivalent .. maybe {edge:0}) when ading to getRoot();
 
        //dockLayout.setBackgroundColor('white');//change..find equivalent
        
	  	// Create header
      	this._headerView = new spagobi.app.ui.Header();
      	     	
      	//dockLayout.addTop(this._headerView);//change
      	this._container = new qx.ui.container.Composite(dockLayout);
		this._container.add(this._headerView, {edge:"north"});
		//this._container.add(this._headerView, {left:0,top:0});//try canvas..not working
 //     	var AL = new qx.ui.basic.Atom("Gaurav",qx.util.AliasManager.getInstance().resolve('spagobi/img/spagobi/test/schedule.png'));
  //    	var DL = new qx.ui.basic.Atom("Amit",qx.util.AliasManager.getInstance().resolve('spagobi/img/spagobi/test/detail.gif'));
//		this._container.add(DL, {edge:"north"});
      	// Create toolbar
      	this._toolBarView = new spagobi.ui.ToolBar([
      		{
		  		command: 'Control+Q',
		  		handler: function() {this._selectToolbar('resources');},
		  		context: this,
		  		"label": this.tr('Resources'),
		  		icon: 'qx/icon/Oxygen/16/actions/dialog-ok.png',
		  		tooltip: 'Reload the feeds.',
		  		defaultbutton: true
	  		}, {
		  		command: 'Control+W',
		  		handler: function() {this._selectToolbar('catalogues');},
		  		context: this,
		  		"label": this.tr('Catalogues'),
		  		icon: 'qx/icon/Oxygen/16/actions/dialog-ok.png',
		  		tooltip: 'Reload the feeds.'
	  		}, {
		  		command: 'Control+E',
		  		handler: function() {this._selectToolbar('behaviouralModel');},
		  		context: this,
		  		"label": this.tr('Behavioural Model'),
		  		icon: 'qx/icon/Oxygen/16/actions/dialog-ok.png',
		  		tooltip: 'Reload the feeds.'
	  		}, {
		  		command: 'Control+R',
		  		handler: function() {this._selectToolbar('analyticalModel');},
		  		context: this,
		  		"label": this.tr('Analytical Model'),
		  		icon: 'qx/icon/Oxygen/16/actions/dialog-ok.png',
		  		tooltip: 'Reload the feeds.'
	  		}, {
		  		command: 'Control+T',
		  		handler: function() {this._selectToolbar('adminDistributionList');},
		  		context: this,
		  		"label": this.tr('Admin Distribution List'),
		  		icon: 'qx/icon/Oxygen/16/actions/dialog-ok.png',
		  		tooltip: 'Reload the feeds.'
	  		}, {
		  		command: 'Control+Y',
		  		handler: function() {this._selectToolbar('functionality');},
		  		context: this,
		  		"label": this.tr('Functionality'),
		  		icon: 'qx/icon/Oxygen/16/actions/dialog-ok.png',
		  		tooltip: 'Reload the feeds.'
	  		}, {
		  		command: 'Control+U',
		  		handler: function() {this._selectToolbar('events');},
		  		context: this,
		  		"label": this.tr('Events'),
		  		icon: 'qx/icon/Oxygen/16/actions/dialog-ok.png',
		  		tooltip: 'Reload the feeds.'
	  		}, {
		  		command: 'Control+I',
		  		handler: function() {this._selectToolbar('tools');},
		  		context: this,
		  		"label": this.tr('Tools'),
		  		icon: 'qx/icon/Oxygen/16/actions/dialog-ok.png',
		  		tooltip: 'Reload the feeds.'
	  		}, {
		  		command: 'Control+O',
		  		handler: function() {this._selectToolbar('hotLinks');},
		  		context: this,
		  		"label": this.tr('Hot Links'),
		  		icon: 'qx/icon/Oxygen/16/actions/dialog-ok.png',
		  		tooltip: 'Reload the feeds.'
	  		}
      	]);
      	      	
      	//dockLayout.addTop(this._toolBarView);//change
      	this._container.add(this._toolBarView, {edge:"north"});//, width: "100%"
      	//this._container.add(this._toolBarView, {left:0, width: "100%"});//not working
      	
      	// Create horizontal split pane
      	this.containerToolBar = new qx.ui.container.Stack();
      	      	
      	this.toolbars['resources'] = new spagobi.ui.PageView({
      		toolbar: {
      			//defaultBackgroudColor: 'white',
      			//focusedBackgroudColor: '#DEFF83',
      			buttons: [
      				{
      					name: 'engine',
						image: qx.util.AliasManager.getInstance().resolve('spagobi/img/spagobi/test/engineAdministrationIcon.png'),
						page: 'engine',
						tooltip: 'Engine Details'
      				}, {
      					name: 'datasource',
						image:qx.util.AliasManager.getInstance().resolve('spagobi/img/spagobi/test/datasourceAdministrationIcon.png'),
						page: 'datasource',
						tooltip: 'Datasource Details'
      				}, {
      					name: 'dataset',
						image:qx.util.AliasManager.getInstance().resolve('spagobi/img/spagobi/test/datasetAdministrationIcon.png'),
						page: 'dataset',
						tooltip: 'Dataset Details'
      				}
      			]
      		},
      		defaultSelectedPage: 'engine'
      	});
      	
      	//change
      	/*dockLayout.add( this.toolbars['resources'] );//change
      	this.toolbars['resources'].setLiveResize(true);//change .. find equivalent .. maybe container resizer 	
      	this.toolbars['resources'].setVisibility(false);
      	*/
      	this.containerToolBar.add(this.toolbars['resources']);
      	
      	this.toolbars['catalogues'] = new spagobi.ui.PageView({
      		toolbar: {
      			buttons: [
      				{
      					name: 'mapmgmt',
						image:qx.util.AliasManager.getInstance().resolve('spagobi/img/spagobi/test/mapManagementIcon.png'),
						page: 'mapmgmt',
						tooltip: 'Map Details'
      				}, {
      					name: 'featuremgmt',
						image:qx.util.AliasManager.getInstance().resolve('spagobi/img/spagobi/test/featureManagementIcon.png'),
						page: 'featuremgmt',
						tooltip: 'Feature Details'
      				}
      			]
      		},
      		defaultSelectedPage: 'mapmgmt'
      	});
      	 
      	//change
      	/*dockLayout.add( this.toolbars['catalogues'] );
      	  this.toolbars['catalogues'].setLiveResize(true);      	
      	  this.toolbars['catalogues'].setVisibility(false);
      	*/
      	this.containerToolBar.add(this.toolbars['catalogues']);
      	
      	this.toolbars['behaviouralModel'] = new spagobi.ui.PageView({
      		toolbar: {
      			buttons: [
      				{
      					name: 'lov',
						image:qx.util.AliasManager.getInstance().resolve('spagobi/img/spagobi/test/lovManagementIcon.png'),
						page: 'lov',
						tooltip: 'LoV Details'//Predefined List of Values
      				}, {
      					name: 'constraints',
						image:qx.util.AliasManager.getInstance().resolve('spagobi/img/spagobi/test/constraintManagementIcon.png'),
						page: 'constraints',
						tooltip: 'Constraint Details'
      				}, {
      					name: 'parameters',
						image:qx.util.AliasManager.getInstance().resolve('spagobi/img/spagobi/test/parameterManagementIcon.png'),
						page: 'parameters',
						tooltip: 'Analytical Driver Details'// Analytical Drivers Management
      				}
      			]
      		},
      		defaultSelectedPage: 'lov'
      	});
      	
      	//change
      	/*dockLayout.add( this.toolbars['behaviouralModel'] );
      	this.toolbars['behaviouralModel'].setLiveResize(true);
      	this.toolbars['behaviouralModel'].setVisibility(false);
      	*/
      	this.containerToolBar.add(this.toolbars['behaviouralModel']);
      	
      	this.toolbars['analyticalModel'] = new spagobi.ui.PageView({
      		toolbar: {
      			buttons: [
      				{
      					name: 'funcManagement',
						image:qx.util.AliasManager.getInstance().resolve('spagobi/img/spagobi/test/folderAdministrationIcon.png'),
						page: 'funcManagement',
						tooltip: 'Functionalities Management'
      				}, {
      					name: 'configuration',
						image:qx.util.AliasManager.getInstance().resolve('spagobi/img/spagobi/test/objectAdministrationIcon.png'),
						page: 'configuration',
						tooltip: 'Documents Configuration'
      				}
      			]
      		},
      		defaultSelectedPage: 'funcManagement'
      	});
      	
      	//change
      	/*
      	dockLayout.add( this.toolbars['analyticalModel'] );
      	this.toolbars['analyticalModel'].setLiveResize(true);
      	this.toolbars['analyticalModel'].setVisibility(false);
      	*/
      	this.containerToolBar.add(this.toolbars['analyticalModel']);
      	
      	this.toolbars['adminDistributionList'] = new spagobi.ui.PageView({
      		toolbar: {
      			buttons: [
      				{
      					name: 'distributionList',
						image:qx.util.AliasManager.getInstance().resolve('spagobi/img/spagobi/test/distributionlistuser.png'),
						page: 'distributionList',
						tooltip: 'Distribution List User'
      				}, {
      					name: 'distributionListConfig',
						image:qx.util.AliasManager.getInstance().resolve('spagobi/img/spagobi/test/distributionlist.png'),
						page: 'distributionListConfig',
						tooltip: 'Distribution List Configuration'
      				}
      			]
      		},
      		defaultSelectedPage: 'distributionList'
      	});
      	
      	//change
      	/*dockLayout.add( this.toolbars['adminDistributionList'] );
        this.toolbars['adminDistributionList'].setLiveResize(true);
      	this.toolbars['adminDistributionList'].setVisibility(false);
      	*/
      	this.containerToolBar.add(this.toolbars['adminDistributionList']);
      	
      	this.toolbars['functionality'] = new spagobi.ui.PageView({
      		toolbar: {
      			buttons: [
      				{
      					name: 'func',
						image:qx.util.AliasManager.getInstance().resolve('spagobi/img/spagobi/test/mapManagementIcon.png'),
						page: 'func',
						tooltip: 'Functionalities'
      				}
      			]
      		},
      		defaultSelectedPage: 'func'
      	});
      	
      	//change
      	/*dockLayout.add( this.toolbars['functionality'] );
        this.toolbars['functionality'].setLiveResize(true);
      	this.toolbars['functionality'].setVisibility(false);
      	*/
      	this.containerToolBar.add(this.toolbars['functionality']);
      	
      	this.toolbars['events'] = new spagobi.ui.PageView({
      		toolbar: {
      			buttons: [
      				{
      					name: 'workflow',
						image:qx.util.AliasManager.getInstance().resolve('spagobi/img/spagobi/test/todoList_2.png'),
						page: 'workflow',
						tooltip: 'Workflow To Do List'
      				}, {
      					name: 'event',
						image:qx.util.AliasManager.getInstance().resolve('spagobi/img/spagobi/test/events.png'),
						page: 'event',
						tooltip: 'Events'
      				}
      			]
      		},
      		defaultSelectedPage: 'workflow'
      	});
      	
      	//change
      	/*dockLayout.add( this.toolbars['events'] );
        this.toolbars['events'].setLiveResize(true);
      	this.toolbars['events'].setVisibility(false);
      	*/
      	this.containerToolBar.add(this.toolbars['events']);
      	
      	this.toolbars['tools'] = new spagobi.ui.PageView({
      		toolbar: {
      			buttons: [
      				{
      					name: 'tool',
						image:qx.util.AliasManager.getInstance().resolve('spagobi/img/spagobi/test/importexport64.png'),
						page: 'tool',
						tooltip: 'Import/Export'//Import / Export
      				}, {
      					name: 'schedule',
						image:qx.util.AliasManager.getInstance().resolve('spagobi/img/spagobi/test/scheduleIcon64_blu.png'),
						page: 'schedule',
						tooltip: 'Schedule Document Executions'
      				}, {
      					name: 'roles',
						image:qx.util.AliasManager.getInstance().resolve('spagobi/img/spagobi/test/rolesynch64.jpg'),
						page: 'roles',
						tooltip: 'Roles Synchronization'
      				}
      			]
      		},
      		defaultSelectedPage: 'tool'
      	});
      	
      	//change
      	/*dockLayout.add( this.toolbars['tools'] );
        this.toolbars['tools'].setLiveResize(true);
      	this.toolbars['tools'].setVisibility(false);
      	*/
      	this.containerToolBar.add(this.toolbars['tools']);
      	
      	this.toolbars['hotLinks'] = new spagobi.ui.PageView({
      		toolbar: {
      			buttons: [
      				{
      					name: 'link1',
						image:qx.util.AliasManager.getInstance().resolve('spagobi/img/spagobi/test/todoList_2.png'),
						page: 'link1',
						tooltip: 'Remember Me'
      				}, {
      					name: 'link2',
						image:qx.util.AliasManager.getInstance().resolve('spagobi/img/spagobi/test/scheduleIcon64_blu.png'),
						page: 'link2',
						tooltip: 'Most Popular'
      				}, {
      					name: 'link3',
						image:qx.util.AliasManager.getInstance().resolve('spagobi/img/spagobi/test/engineAdministrationIcon.png'),
						page: 'link3',
						tooltip: 'Most Recently Used'
      				}
      			]
      		},
      		defaultSelectedPage: 'link1'
      	});
      	
      	//change
      	/*dockLayout.add( this.toolbars['hotLinks'] );
        this.toolbars['hotLinks'].setLiveResize(true);
      	this.toolbars['hotLinks'].setVisibility(false);
      	*/ 
  		this.containerToolBar.add(this.toolbars['hotLinks']);
  		
  		//this.containerToolBar.setSelected(this.containerToolBar.getChildren()[0]);//default resources selected
  		this._container.add(this.containerToolBar, {edge:"west", height: "100%", width: "100%"});
      	//return dockLayout;
      	
      	return this._container  		
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
    	
    	/*
    	if(this.toolbars[toolbarName]) {
    		if(this.selectToolbarName) {
    			this.toolbars[this.selectToolbarName].setVisibility("excluded");
    		}
    		//alert(this.toolbars[toolbarName].getVisibility());
    		this.toolbars[toolbarName].setVisibility("visible");
    		
    		if(!this.toolbars[toolbarName].getSelectedPageName()) {
    			this.toolbars[toolbarName].selectDefaultPage();
    		}
    		this.selectToolbarName = toolbarName;
    		
    		
    	} else {
    		alert(toolbarName + ' is not yet implemented !');
    	}
    	*/
    	if(this.toolbars[toolbarName]) {
	    	var index = this.containerToolBar.indexOf(this.toolbars[toolbarName]);
	    	this.containerToolBar.setSelected(this.containerToolBar.getChildren()[index]);
	    	if(!this.toolbars[toolbarName].getSelectedPageName()) {
	    			this.toolbars[toolbarName].selectDefaultPage();
	    	}
    	}	
    	else {
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
    	//alert(document.body.className = qx.legacy.theme.manager.Meta.getInstance().getTheme());
       //document.body.className = qx.legacy.theme.manager.Meta.getInstance().getTheme() == qx.legacy.theme.ClassicRoyale ? "Ext" : "Classic";//change
       document.body.className = qx.theme.manager.Meta.getInstance().getTheme() == qx.theme.Modern ? "Ext" : "Classic";
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