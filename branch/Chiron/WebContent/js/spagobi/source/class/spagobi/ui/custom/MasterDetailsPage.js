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
  
/*
 * TODO generalize the constructor in this way ...
 * 
 * ... construct : function(records, form) {...}
 * 
 * and move the class in the parent package (i.e spagobi.ui)
 */

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 */

/**
 * Class to create the page displayed on right side.
 * <p> The page contains a Filter bar, a list and a navigation bar on top and a form at the bottom.   
 */

qx.Class.define("spagobi.ui.custom.MasterDetailsPage",
{
  extend : qx.ui.splitpane.VerticalSplitPane,

  /**
   * Constructor to create the page on right side.
   * <p> It splits the given page into 2 parts: top and bottom.
   * <p> Top part - the Filter bar, list and navigation bar are added.
   * <p> Bottom part - the form (based on the button selected in vertical tool bar) is added.
   * 
   * <p>*Example*
   * <p><code>
   * var myForm = new spagobi.ui.custom.MasterDetailsPage('engine');
   * </code>
   * 
   * @param type The name for referring to the selected page. The selected values can be : 
   * (__'engine'__ / __'dataset'__ / __'datasource'__ /  __'mapmgmt'__ / __'featuremgmt'__)
   */
   	
  construct : function(type, parentContainer)
  {
  	this.base(arguments,180, "1*", "3*");
  	parentContainer.addRight(this);
  	
  	
    this.setWidth("100%");
    this.setHeight("100%");
    //var listPage;
    var detailPage;
    var detailHeader;
    //var detailBody ;
    
    this.setEdge(0);
   	this.setBorder("line-left");
      		
    //var records;
    var form;  	
    this._type = type;	
	if(type === 'engine') {
		this.records = spagobi.app.data.DataService.loadEngineRecords();
		form = new spagobi.ui.custom.EngineDetailsForm(); 
	} else if(type === 'dataset') {
		this.records = spagobi.app.data.DataService.loadDatasetRecords();
		form = new spagobi.ui.custom.DatasetDetailsForm(); 
	} else if(type === 'datasource') {
		this.records = spagobi.app.data.DataService.loadDatasourceRecords();
		form = new spagobi.ui.custom.DatasourceDetailsForm(); 
	} else if(type == 'mapmgmt') {
		this.records = spagobi.app.data.DataService.loadMapRecords();
		form = new spagobi.ui.custom.MapDetailsForm(); 
	} else if(type == 'featuremgmt') {
		this.records = spagobi.app.data.DataService.loadFeatureRecords();
		form = new spagobi.ui.custom.FeatureDetailsForm(); 
	} else if(type == 'lov') {
		this.records = spagobi.app.data.DataService.loadLOVRecords();
		form = new spagobi.ui.custom.LOVDetailsForm(); 
	} else if(type == 'constraints') {
		this.records = spagobi.app.data.DataService.loadLOVRecords();
		form = new spagobi.ui.custom.ConstraintDetailsForm(); 
	} else if(type == 'parameters') {
		this.records = spagobi.app.data.DataService.loadLOVRecords();
		form = new spagobi.ui.custom.AnalyticalDriverDetailsForm(); 
	} else if(type == 'configuration') {									
		this.records = spagobi.app.data.DataService.loadConfigurationRecords();
		form = new spagobi.ui.custom.DocumentConfigurationForm(); 
	} else if(type == 'link1') {									
		this.records = spagobi.app.data.DataService.loadlink1Records();
		form = new spagobi.ui.custom.Link1DummyForm(); 
	} else if(type == 'link2') {									
		this.records = spagobi.app.data.DataService.loadlink2Records();
		form = new spagobi.ui.custom.Link2DummyForm(); 
	} else if(type == 'link3') {									
		this.records = spagobi.app.data.DataService.loadlink3Records();
		form = new spagobi.ui.custom.Link3DummyForm(); 
	} else if(type == 'roles') {									
		this.records = spagobi.app.data.DataService.loadRolesRecords();
		form = new spagobi.ui.custom.RolesDummyForm(); 
	} else if(type === 'distributionList') {
		this.records = spagobi.app.data.DataService.loadDatasourceRecords();
		form = new spagobi.ui.custom.DatasourceDetailsForm(); 
	} else if(type === 'distributionListConfig') {
		this.records = spagobi.app.data.DataService.loadDatasourceRecords();
		form = new spagobi.ui.custom.DatasourceDetailsForm(); 
	} else if(type === 'func') {
		this.records = spagobi.app.data.DataService.loadDatasourceRecords();
		form = new spagobi.ui.custom.DatasourceDetailsForm(); 
	} else if(type === 'workflow') {
		this.records = spagobi.app.data.DataService.loadDatasourceRecords();
		form = new spagobi.ui.custom.DatasourceDetailsForm(); 
	} else if(type === 'event') {
		this.records = spagobi.app.data.DataService.loadDatasourceRecords();
		form = new spagobi.ui.custom.DatasourceDetailsForm(); 
	} else if(type === 'tool') {
		this.records = spagobi.app.data.DataService.loadDatasourceRecords();
		form = new spagobi.ui.custom.DatasourceDetailsForm(); 
	} else if(type === 'schedule') {
		this.records = spagobi.app.data.DataService.loadDatasourceRecords();
		form = new spagobi.ui.custom.DatasourceDetailsForm(); 
	}
	
	if(type != 'funcManagement'){	
	   	// Create list view
	   	this.listPage = new spagobi.ui.PagedTable(this,this.records); 
	   	this.addTop( this.listPage );
	    //this.listPage.setWidth('100%'); 
	    //this.listPage.setHeight('100%');  	
	      	
	      	
	   	// Create detail view
	   	detailPage = new qx.ui.pageview.buttonview.ButtonView();
	    detailPage.setEdge(0);     
	        
	    // Create detail view toolbar
	      var saveButton = new qx.ui.pageview.buttonview.Button(/*"Save");//*/"", "spagobi/img/spagobi/test/save.png");
	    var SB = new qx.ui.popup.ToolTip("Save");
	    saveButton.setToolTip(SB);
	    var deleteButton = new qx.ui.pageview.buttonview.Button(/*"Delete");//*/"", "spagobi/img/spagobi/test/delete.png");
	    var SD = new qx.ui.popup.ToolTip("Delete");
	    deleteButton.setToolTip(SD);
	    var createButton = new qx.ui.pageview.buttonview.Button(/*"New");//*/"", "spagobi/img/spagobi/test/create.png");
	    var NB = new qx.ui.popup.ToolTip("New");
	    createButton.setToolTip(NB);
	                     
	    detailPage.getBar().add(createButton, saveButton, deleteButton);               
	   
	    // Functionality for Save button
	    var save_page = new qx.ui.pageview.buttonview.Page(saveButton);
		save_page.setDisplay(false);
		detailPage.getPane().add(save_page);
		save_page.addEventListener("appear", this.ShowDetails, this);
		
		
	        
	    // Create detail view body
	   	this._form = form;       	      	
	   	detailHeader = new qx.ui.pageview.buttonview.Button("", "");
	    detailHeader.setDisplay(false);        
	    detailHeader.setChecked(true);  		
		this.detailBody = new qx.ui.pageview.buttonview.Page( detailHeader ); 
	    this.detailBody.setOverflow("auto");
	    this.detailBody.add( this._form );  		 
	  	detailPage.getPane().add( this.detailBody );
	  	
	  	this.addBottom( detailPage );
  	}		// if (type != func mgmt)
  	
  	if(type == 'funcManagement'){
  		
  		var bottomPart = new qx.ui.layout.VerticalBoxLayout();
  		
  		var headerLabel = new qx.ui.basic.Label("Functionalities Tree");
  		with(headerLabel){
  			width = 200;
  			height = 300;
  		};
  		
  		//var h1 = new qx.ui.embed.HtmlEmbed("<h1>Fn</h1>");
  		
  		var tree = new spagobi.ui.Tree({root: "Functionalities" });
  		/*
  		 with(tree)
        {
          setBackgroundColor("white");
          setBorder("inset");
          setOverflow("scrollY");
  
          setHeight(null);
          setTop(48);
          setLeft(20);
          setWidth(200);
          setBottom(48);
        };
  		 */
  		 		
  		var node1 = tree.addNode({
		  							name  : "Report",
		  							parent: tree,
		  							id	  : "node1"
  								});
  		var node2 = tree.addNode({
		  							name  : "OLAP",
		  							parent: tree,
		  							id	  : "node2"	
  								});
  		var node3 = tree.addNode({
		  							name  : "myOLAP",
		  							parent: node2,
		  							id	  : "node3",
		  							file  : true	
  								});
  		var node4 = tree.addNode({
		  							name  : "DashBoard",
		  							parent: tree,
		  							id	  : "node4",
		  							init_icon: "icon/16/places/user-trash.png",
		  							click_icon: "",
		  							checkBox  : true	
  								});
  		var node5 = tree.addNode({
		  							name  : "myDashBoardFolder",
		  							parent: node4,
		  							id	  : "node5",
		  							checkBox: true
		  						});
  		var node6 = tree.addNode({
		  							name  : "myDashBoard",
		  							parent: node4,
		  							id	  : "node6",
		  							checkBox: true,
		  							init_icon: "icon/16/places/user-desktop.png",
		  							click_icon: "",
		  							file  : true
  								});
  		//tree.deleteNode(node3);							
  		//tree.deleteNodebyID("node3");						
  								 											
  		//tree.addEventListener("click",tree.onClickMenu,tree);		// mouseevent
  		//tree.addEventListener("changeSelected",tree.onClickMenu,tree);
  		tree.getManager().addEventListener("changeSelection",tree.onClickMenu,tree);	//data event
  		//tree.getManager().addEventListener("changeSelection",this._onSelectTreeNode,tree);
  		
  		//tree.setSelectedElement(tree);
  		//alert (tree.getSelectedElement()); // changeSelected
  		//var e = tree.getSelectedElement();  	e.addEventListener("click",tree.onClickMenu,tree); 		
  		//tree.setUseDoubleClick(true);
  		  			
  		bottomPart.add(headerLabel/*,h1*/, tree);	
  		
  			
  		/* 
  		Context menu using a button - to test context menu functionality
  		var w1 = new qx.ui.form.Button("Open");
  		w1.setTop(120);
        w1.setLeft(320);
        
  		var contextMenu = new qx.ui.menu.Menu;
   		var insertButton = new qx.ui.menu.Button("Insert");
   		var deleteButton = new qx.ui.menu.Button("Delete");
   		contextMenu.add(insertButton,deleteButton);
  		var d = qx.ui.core.ClientDocument.getInstance();
  		d.add(contextMenu);
  		
  		w1.addEventListener("click", function(e)
        {
          if (contextMenu.isSeeable())
          {
            contextMenu.hide();
          }
          else
          {
            var el = this.getElement();
  
            contextMenu.setLeft(qx.html.Location.getPageBoxLeft(el));
            contextMenu.setTop(qx.html.Location.getPageBoxBottom(el));
  
            contextMenu.show();
          };
  
          e.setPropagationStopped(true);
        });
  		
  		bottomPart.add(w1);
  		*/
  		
  				
  		this.addBottom(bottomPart);
  		//this.addBottom(tree);
  	}
  	/*
  	   else{
  		this.addBottom( detailPage );
  	}
  	*/
  	
  },

  members :
  {
    _form : undefined,
    detailBody : undefined,
    records : undefined,
    listPage : undefined,
    
    /**
     * Function to get the current form
     * 
     * @return form The selected form 
     */
    getForm: function() {
    	return this._form;
    },
    
    /**
     * Function to select the object
     * 
     * @param dataObject The data object
     */
    selectDataObject: function(dataObject) {
    	//alert(this._form + ' - ' + this._type + " ->\n " + this.printObject(dataObject));
    	this._form.setData(dataObject);
    },
    
    /**
     * Function to display the current form on the page
     */
    show: function() {
    	//this._form.setVisibility(false);
    	if(this.detailBody != undefined) {	// this._type != 'funcManagement'
    		this.detailBody.remove(this._form)
    	}	
    	//this._form.dispose();
    	if(this._type === 'engine') {
			this._form = new spagobi.ui.custom.EngineDetailsForm(); 
		
		} else if(this._type === 'dataset') {
			this._form = new spagobi.ui.custom.DatasetDetailsForm(); 
		
		} else if(this._type === 'datasource') {
			this._form = new spagobi.ui.custom.DatasourceDetailsForm(); 
		
		} else if(this._type === 'mapmgmt') {
			this._form = new spagobi.ui.custom.MapDetailsForm();
		
		} else if(this._type === 'featuremgmt') {
			this._form = new spagobi.ui.custom.FeatureDetailsForm();
		
		} else if(this._type === 'lov') {
			this._form = new spagobi.ui.custom.LOVDetailsForm(); 
		
		} else if(this._type === 'constraints') {
			this._form = new spagobi.ui.custom.ConstraintDetailsForm(); 
		
		} else if(this._type === 'parameters') {
			this._form = new spagobi.ui.custom.AnalyticalDriverDetailsForm();
		
		} else if(this._type === 'link1') {
			this._form = new spagobi.ui.custom.Link1DummyForm();
		
		} else if(this._type === 'link2') {
			this._form = new spagobi.ui.custom.Link2DummyForm();
		
		} else if(this._type === 'link3') {
			this._form = new spagobi.ui.custom.Link3DummyForm();	
		
		}  else if(this._type == 'configuration') {
			this._form = new spagobi.ui.custom.DocumentConfigurationForm(); 
		
		} /* else if(this._type === 'funcManagement') {
			this._form = new spagobi.ui.custom.LOVDetailsForm(); 
		
		}*/ else if(this._type === 'roles') {
			this._form = new spagobi.ui.custom.RolesDummyForm();
		} else if(this._type === 'distributionList') {
			this._form = new spagobi.ui.custom.DatasourceDetailsForm(); 
		
		} else if(this._type === 'distributionListConfig') {
			this._form = new spagobi.ui.custom.DatasourceDetailsForm(); 
		
		} else if(this._type === 'func') {
			this._form = new spagobi.ui.custom.DatasourceDetailsForm(); 
		
		} else if(this._type === 'workflow') {
			this._form = new spagobi.ui.custom.DatasourceDetailsForm(); 
		
		} else if(this._type === 'event') {
			this._form = new spagobi.ui.custom.DatasourceDetailsForm(); 
		
		} else if(this._type === 'tool') {
			this._form = new spagobi.ui.custom.DatasourceDetailsForm(); 
		
		} else if(this._type === 'schedule') {
			this._form = new spagobi.ui.custom.DatasourceDetailsForm(); 
		
		}
			
			//testing for parameter form's getData() function for checkbox list.. Don't delete
   			/*
	  		var b = new qx.ui.form.Button("dummy button");
	  		b.addEventListener("execute",this.myFunction,this);
	  		this._form.add(b);
  			*/
  			
  			//testing for parameter form's getData() function for radio button.. Don't delete
  			/*var b = new qx.ui.form.Button("dummy button");
	  		b.addEventListener("execute",this.myRadioFunction,this);
	  		this._form.add(b);
	  		*/ 
		
		if(this.detailBody != undefined) {
			this.detailBody.add(this._form);
		}
			
		if(!this.isVisibility()) {
			this.setVisibility(true);
		}
    },
    
    /**
     * Function to print the "property: value" pairs of an object
     * 
     * @param o The object to be printed in the form of property: value pairs
     */
    printObject: function(o) {
    	var str = '';
    	for(p in o) {
    		str += p + ': ' + o[p] + ';\n'
    	}
    	return str;
    },
    
    //testing for parameter form's getData() function for checkbox list... Don't Delete
    myFunction:function(){
    	alert("button works");
    	this._form.setData({mychecklist: ["eeee","jjjj"]});
    	var o = this._form.getData();
    	var list = "";
    	for(prop in o){
    		if(prop == 'mychecklist'){
    			for(i=0; i<o[prop].length; i++){
    				list = list + o[prop][i]+ ",";
    			}
    		}
    	}
    	alert(list);
    },
    
    //testing for parameter form's getData() function for radio button... Don't Delete
    myRadioFunction:function(){
    	alert("button works");
    	this._form.setData({type: "Number"});
    	var o = this._form.getData();
    	var list = "";
    	for(prop in o){
    		alert("getData gave: " + o[prop]);
    	}
    	
    },
    
    _onClickMenu: function(e){
    	alert("value:" + this.getCurrentNode());		//working	
    	//alert("value:" + this.getCurrentNode().label);
    	//treerowstructure.getLabelObject()
    },
    
    _onSelectTreeNode:function(e){
    	//this.addEventListener("click",this.onClickMenu,this);
    	alert(this + "," + e.getData());
    	this.onClickMenu();
    },
    
    ShowDetails: function () {
		if (this.records.ID != undefined){
    		if (this.records.ID == "ROLES"){
				alert (this.listPage._table.getUpdatedData());
			}
    	}
		else{
			var alias = this.getForm().getData();
			alert (this.printObject(alias));
		}	
	}	
  }
});