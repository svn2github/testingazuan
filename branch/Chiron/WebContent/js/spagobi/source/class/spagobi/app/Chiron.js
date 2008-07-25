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


/**
 * @author Amit Rana (amit.rana@eng.it)
 * @author Gaurav Jauhri (gaurav.jauhri@eng.it)
 * @author Andrea Gioia (andrea.gioia@eng.it)
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
	        qx.util.ThemeList.createMetaButtons(d, 600, 500);
      	}
      	
  
      	this._selectToolbar('resources');
      	
    },
     
    _createLayout: function() {
    	var dockLayout = new qx.ui.layout.DockLayout();
        dockLayout.setEdge(0);
        
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
						tooltip: 'Maps'
      				}, {
      					name: 'constraints',
						image:'spagobi/img/spagobi/test/constraintManagementIcon.png',
						page: 'constraints',
						tooltip: 'Features'
      				}, {
      					name: 'parameters',
						image:'spagobi/img/spagobi/test/parameterManagementIcon.png',
						page: 'parameters',
						tooltip: 'Features'
      				}
      			]
      		},
      		defaultSelectedPage: 'lov'
      	});
      	dockLayout.add( this.toolbars['behaviouralModel'] );
      	this.toolbars['behaviouralModel'].setLiveResize(true);      	
      	this.toolbars['behaviouralModel'].setVisibility(false);
      	
      	
      	return dockLayout;  		
    },
    
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