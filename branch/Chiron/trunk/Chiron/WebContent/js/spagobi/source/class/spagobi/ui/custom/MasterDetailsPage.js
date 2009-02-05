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
  //extend : qx.legacy.ui.splitpane.VerticalSplitPane,
  extend : qx.ui.splitpane.Pane,
  

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
   	
  construct : function(type)
  {
	//this.base(arguments, "1*", "2*");//extend :	qx.ui.splitpane.Pane,
	this.base(arguments,"vertical");
//	this.setLayout(new qx.ui.layout.Dock);
	
	
//	var pane = new qx.ui.splitpane.Pane("vertical");
	
//	var containerTop = new qx.ui.container.Composite(new qx.ui.layout.VBox);//.set({
    //    height: 150
   //   });

 //   var containerBottom = new qx.ui.container.Composite(new qx.ui.layout.VBox);
	
	
	/*
    this.setWidth("100%");
    this.setHeight("100%");
    
    this.setLiveResize(true);
 			//   this.setOverflow("auto");
  			//   this.setShowKnob(false);
    
 
    var detailPage;
    var detailHeader;
   
    
    this.setEdge(0);
   	this.setBorder("line-left");
      		
    		//var records;
    var form;  	
    this._type = type;
    
   */
	if(type === 'engine') {
		this.records = spagobi.app.data.DataService.loadEngineRecords();
		form = new spagobi.ui.custom.EngineDetailsForm(); 
	} else if(type === 'datasource') {
		this.records = spagobi.app.data.DataService.loadDatasourceRecords();
		form = new spagobi.ui.custom.DatasourceDetailsForm(); 
	} else if(type === 'dataset') {
		this.records = spagobi.app.data.DataService.loadDatasetRecords();
		form = new spagobi.ui.custom.DatasetDetailsForm(); 
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
		
	} else if(type == 'link2') {									
		this.records = spagobi.app.data.DataService.loadlink2Records();
		
	} else if(type == 'link3') {									
		this.records = spagobi.app.data.DataService.loadlink3Records();
		
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
		this.records = spagobi.app.data.DataService.loadScheduleRecords();
	//	form = new spagobi.ui.custom.DatasourceDetailsForm(); 
	} else if(type == 'roles') {									
		this.records = spagobi.app.data.DataService.loadRolesRecords();
		 
	} 
	
	
	
		this.listPage = new spagobi.ui.PagedTable(this,this.records); 
	   	
	   	//this.addTop( this.listPage );//change
//	   	containerTop.add(this.listPage);
	   	this.add(this.listPage,0);
	   	
	   	
	   	
	   	if(type != 'roles' && type != 'link1' &&type != 'link2' &&type != 'link3' &&type !='schedule'){ //
	   		
	   	containerBottom = new qx.ui.core.Widget();
	   	var Vbox = new qx.ui.layout.VBox();
	   	containerBottom._setLayout(Vbox);
	   	
	   	//var button = new qx.ui.form.Button("Toggle Splitpane Orientation");
	   	var formBar = new qx.ui.container.Composite(new qx.ui.layout.HBox);
	   	//var formBarManager = new qx.ui.form.RadioGroup(null);
	   	//qx.Class.include(qx.ui.toolbar.RadioButton, qx.ui.core.MExecutable);
	   	
	   	var createButton = new qx.ui.toolbar.Button("", qx.util.AliasManager.getInstance().resolve("spagobi/img/spagobi/test/create.png"));
	    var createToolTip = new qx.ui.tooltip.ToolTip("New");
	    createButton.setToolTip(createToolTip);
	   		   	   	
	   	var saveButton = new qx.ui.toolbar.Button("", qx.util.AliasManager.getInstance().resolve("spagobi/img/spagobi/test/save.png"));
	    var saveToolTip = new qx.ui.tooltip.ToolTip("Save");
	    saveButton.setToolTip(saveToolTip);
	    saveButton.addListener("execute", this.ShowDetails, this);
	    
	    var deleteButton = new qx.ui.toolbar.Button("", qx.util.AliasManager.getInstance().resolve("spagobi/img/spagobi/test/delete.png"));
	    var deleteToolTip = new qx.ui.tooltip.ToolTip("Delete");
	    deleteButton.setToolTip(deleteToolTip);
	    
	    var formBarArray = [createButton, saveButton, deleteButton];//since add() adds only 1 widget at a time
	    
	    for(var i=0; i<formBarArray.length; i++){
	   		formBar.add(formBarArray[i]);
	   		//formBarManager.add(formBarArray[i]);
	    }
	    
	    
	   	containerBottom._add(formBar);
		
	   	
	   	//this.listPage.setOverflow("auto");//change ..find
			    //this.listPage.setWidth('100%'); 
			    //this.listPage.setHeight('100%');  	
	    this._form = form;
	    
	    containerBottom._add(this._form);
	    var scroll = new qx.ui.container.Scroll();
	     scroll.add(containerBottom);
	    this.add(scroll,1);
	   	}
		//this.add(pane,{edge: "west",width: "100%"});
	   	
		/* Don't Delete 
	    //testing for parameter form's getData() function
	    if(type ==='parameters'){
	  		var b = new qx.legacy.ui.form.Button("dummy button");
	  		b.addListener("execute",this.myFunction,this);
	  		form.add(b);
	  	}
	  	*/
  	
	   	
	   	
		/* 
	    // Create detail view body
	   	this._form = form;       	      	
	   	detailHeader = new qx.legacy.ui.pageview.buttonview.Button("", "");
	    detailHeader.setDisplay(false);        
	    detailHeader.setChecked(true);  		
		
		this.detailBody = new qx.legacy.ui.pageview.buttonview.Page( detailHeader );
		
	    this.detailBody.setOverflow("auto");
	    this.detailBody.add( this._form );
	    //this._form.setDimension("100%","1*");		 
	  	
	  	detailPage.getPane().add( this.detailBody );
	  	this.addBottom( detailPage );
	  	
	  	//detailPage.getPane().setDimension("100%","100%");
	  	//detailPage.setDimension("100%","1*");
	  	//this.detailBody.setDimension("100%","100%");
	  	//this._form.setDimension("100%","100%");
	  	//this.detailBody.setBorder(new qx.legacy.ui.core.Border(1));
	  	//this.setOverflow("auto");
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
     * Function to set the current form
     * 
     * @param form The form to be selected 
     */
    setForm: function(f) {
    	this._form = f;
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
 //   show: function() {
    	//this._form.setVisibility(false);
    	/*
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
		
		} else if(this._type == 'configuration') {
			this._form = new spagobi.ui.custom.DocumentConfigurationForm(); 
		
		} else if(this._type === 'roles') {
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
		*/	
			//testing for parameter form's getData() function for checkbox list.. Don't delete
   			/*
	  		var b = new qx.legacy.ui.form.Button("dummy button");
	  		b.addListener("execute",this.myFunction,this);
	  		this._form.add(b);
  			*/
  			
  			//testing for parameter form's getData() function for radio button.. Don't delete
  			/*var b = new qx.legacy.ui.form.Button("dummy button");
	  		b.addListener("execute",this.myRadioFunction,this);
	  		this._form.add(b);
	  		*/ 
		/*
		if(this.detailBody != undefined) {
			this.detailBody.add(this._form);
		}
		*/
		/*
		show: function() {
		if(!this.isVisibility()) {
			this.setVisibility(true);
		}
    },
    */
    
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
    	//this.addListener("click",this.onClickMenu,this);
    	alert(this + "," + e.getData());
    	this.onClickMenu();
    },
    
    ShowDetails: function (e) {
    	
    	/*
    	if(e.getTarget().getChecked() == false){
    		return;
    	}
    	*/ 
		if (this.records.ID != undefined){
    		if (this.records.ID == "ROLES"){
				alert (this.listPage._table.getUpdatedData());
			}
    	}
		else{
			
			
			var alias = this.getForm().getData();
			alert (this.printObject(alias));
			
		}
		//e.getTarget().getButton().setChecked(false);
		//e.getTarget().setChecked(false);
	}	
  }
});