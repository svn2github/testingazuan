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
 
qx.Class.define("qooxdoo.app.Chiron",
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
  	_container: undefined,
  	containerToolBar: undefined,
  	mainPane: {},
  	
    main : function()
    {
		 this.base(arguments);
         // Define alias for custom resource path
      	 qx.util.AliasManager.getInstance().add("spagobi", qxlibraries.qooxdoo.resourceUri);
      	 qx.util.AliasManager.getInstance().add("libResource", qxlibraries.qx.resourceUri);      	 
      	        	   
      	 // Include CSS file
      	 qx.bom.Stylesheet.includeFile(qx.util.AliasManager.getInstance().resolve("spagobi/css/reader.css"));
      	       	 
      	 // Increase parallel requests
      	 qx.io.remote.RequestQueue.getInstance().setMaxConcurrentRequests(10);
      	
      	// Create Application Layout
      	var applicationLayout = this._createLayout();
      	
      	var executionMode = qx.core.Setting.get("qooxdoo.executionMode");
      	if(executionMode && executionMode === 'portal') {
      		var d = this.getRoot();
			
			var inlineWidget = new qx.ui.root.Inline("myInlineWidget");//change
			
	        inlineWidget.setHeight("70%");
	        inlineWidget.setWidth("100%");
	        d.add(inlineWidget);
         
         	inlineWidget.add( applicationLayout );
      	} else {
      		this.getRoot().add(applicationLayout, {edge:0});   
      	}
      	
      	this._selectToolbar('resources');	// 	//kpi	//analyticalModel
    },
    
    /**
     * Function which creates the Layout of the complete page.
     * <p> Defines a Dock Layout to which all the entities 
     * __Header, Top ToolBar, Vertical Toolbar and Page__ are added.
     */ 
    _createLayout: function() {
	   	var dockLayout = new qx.ui.layout.Dock();
	   	dockLayout.setSeparatorY("separator-vertical");
	   	
	  	// Create header
      	this._headerView = new qooxdoo.app.ui.Header();
      	     	
      	//dockLayout.addTop(this._headerView);//change
      	this._container = new qx.ui.container.Composite(dockLayout);
		this._container.add(this._headerView, {edge:"north"});
		
      	// Create toolbar
      	this._toolBarView = new qooxdoo.ui.ToolBar([
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
	  		}/*, {
		  		command: 'Control+U',
		  		handler: function() {this._selectToolbar('events');},
		  		context: this,
		  		"label": this.tr('Events'),
		  		icon: 'qx/icon/Oxygen/16/actions/dialog-ok.png',
		  		tooltip: 'Reload the feeds.'
	  		}*/, {
		  		command: 'Control+I',
		  		handler: function() {this._selectToolbar('tools');},
		  		context: this,
		  		"label": this.tr('Tools'),
		  		icon: 'qx/icon/Oxygen/16/actions/dialog-ok.png',
		  		tooltip: 'Reload the feeds.'
	  		}/*, {
		  		command: 'Control+O',
		  		handler: function() {this._selectToolbar('hotLinks');},
		  		context: this,
		  		"label": this.tr('Hot Links'),
		  		icon: 'qx/icon/Oxygen/16/actions/dialog-ok.png',
		  		tooltip: 'Reload the feeds.'
	  		}*/, {
		  		command: 'Control+P',
		  		handler: function() {this._selectToolbar('kpi');},
		  		context: this,
		  		"label": this.tr('KPI Model'),
		  		icon: 'qx/icon/Oxygen/16/actions/dialog-ok.png',
		  		tooltip: 'Reload the feeds.'
	  		}
      	]);
      	      	
      	
      	this._container.add(this._toolBarView, {edge:"north"});
      	
      	// Create horizontal split pane
      	this.containerToolBar = new qx.ui.container.Stack();
      	      	
      	this.toolbars['resources'] = new qooxdoo.ui.PageView({
      		toolbar: {
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
      	this.containerToolBar.add(this.toolbars['resources']);
      	
      	
      	this.toolbars['catalogues'] = new qooxdoo.ui.PageView({
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
      	this.containerToolBar.add(this.toolbars['catalogues']);
      	
      	
      	this.toolbars['behaviouralModel'] = new qooxdoo.ui.PageView({
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
      	this.containerToolBar.add(this.toolbars['behaviouralModel']);
      	
      	
      	this.toolbars['analyticalModel'] = new qooxdoo.ui.PageView({
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
      	this.containerToolBar.add(this.toolbars['analyticalModel']);
      	
      	
      	this.toolbars['adminDistributionList'] = new qooxdoo.ui.PageView({
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
      	this.containerToolBar.add(this.toolbars['adminDistributionList']);
      	
      	this.toolbars['functionality'] = new qooxdoo.ui.PageView({
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
      	this.containerToolBar.add(this.toolbars['functionality']);
      	
      	
      	this.toolbars['events'] = new qooxdoo.ui.PageView({
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
      	this.containerToolBar.add(this.toolbars['events']);
      	
      	this.toolbars['tools'] = new qooxdoo.ui.PageView({
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
      	this.containerToolBar.add(this.toolbars['tools']);
      	
      	
      	this.toolbars['hotLinks'] = new qooxdoo.ui.PageView({
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
      	this.containerToolBar.add(this.toolbars['hotLinks']);
  		
      	this.toolbars['kpi'] = new qooxdoo.ui.PageView({
      		toolbar: {
      			buttons: [
      				{
      					name: 'kpi',
						image:qx.util.AliasManager.getInstance().resolve('spagobi/img/spagobi/test/kpiDefinition.png'),
						page: 'kpi',
						tooltip: 'KPI Definition'
      				}, {
      					name: 'threshold',
						image:qx.util.AliasManager.getInstance().resolve('spagobi/img/spagobi/test/bidocuments16.png'),
						page: 'threshold',
						tooltip: 'Threshold Definition'
      				}, {
      					name: 'modelDefinition',
						image:qx.util.AliasManager.getInstance().resolve('spagobi/img/spagobi/test/modelDefinition.png'),
						page: 'modelDefinition',
						tooltip: 'Model Definition'
      				}, {
      					name: 'modelInstance',
						image:qx.util.AliasManager.getInstance().resolve('spagobi/img/spagobi/test/bidocuments16.png'),
						page: 'modelInstance',
						tooltip: 'Model Instance'
      				}, {
      					name: 'resource',
						image:qx.util.AliasManager.getInstance().resolve('spagobi/img/spagobi/test/bidocuments16.png'),
						page: 'resource',
						tooltip: 'Resource Definition'
      				}, {
      					name: 'alarm',
						image:qx.util.AliasManager.getInstance().resolve('spagobi/img/spagobi/test/bidocuments16.png'),
						page: 'alarm',
						tooltip: 'Alarm Definition'
      				}
      			]
      		},
      		defaultSelectedPage: 'kpi'
      	});
      	this.containerToolBar.add(this.toolbars['kpi']);
      	
  		this._container.add(this.containerToolBar, {edge:"west", height: "100%", width: "100%"});
      	
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
     * Function to set the theme of the page
     */
    _applyCssTheme : function() {
    	document.body.className = qx.theme.manager.Meta.getInstance().getTheme() == qx.theme.Modern ? "Ext" : "Classic";
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