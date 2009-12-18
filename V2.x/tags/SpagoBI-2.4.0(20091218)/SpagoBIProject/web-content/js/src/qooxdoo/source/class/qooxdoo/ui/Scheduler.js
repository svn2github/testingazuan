/* *

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

* */

/* *
 * @author Andrea Gioia (andrea.gioia@eng.it)
 * @author Amit Rana (amit.rana@eng.it)
 * @author Gaurav Jauhri (gaurav.jauhri@eng.it)
 
 */
 
 qx.Class.define("qooxdoo.ui.Scheduler",
{
  //extend : qx.legacy.ui.splitpane.VerticalSplitPane,
  extend : qx.ui.window.Window,
  
  construct : function(type)
  {
	 
	
	if (type.getColumn() <= 2){
	 return ;
	}
  	else {
  		
  		this.base(arguments,null, "qx/icon/Oxygen/16/apps/office-calendar.png");
	    this.setLayout(new qx.ui.layout.VBox(10));
	    this.setShowStatusbar(true);
		this.setStatus("Details loaded");
		this.addListener("resize", function(){ this.center();}, this);
		this.open();
		this.setModal(true);

		if (type.getColumn() == 4){
			
			this.setCaption("Details Window");
			var textfield1 = qooxdoo.commons.WidgetUtils.createInputTextField({
								        		type: 'text',
								        		dataIndex: 'name',
								        		text: 'Name',
								        		labelwidth: 100,
								        		mandatory: true	
								        	});
			this.add(textfield1);
					 
			var textfield2 = qooxdoo.commons.WidgetUtils.createInputTextField({
								        		type: 'text',
								        		dataIndex: 'description',
								        		text: 'Description',
								        		labelwidth: 100,
								        		mandatory: false
								        	});
					
			this.add(textfield2);									        

			var box = new qx.ui.container.Composite;
			box.setLayout(new qx.ui.layout.HBox(10));
			this.add(box, {flex:1});
			
			this.treeFunction = qooxdoo.app.data.DataService.loadTreeNodes();
			this.tree1 = new qooxdoo.ui.Tree(this.treeFunction.treeStructure.root);	
			
			for(var p in this.treeFunction.treeStructure){	//check as for..in is changed in v0.8.1
				if(p != 'root'){
					this.tree1.addNode(this.treeFunction.treeStructure[p]);
				}
			}	
				
			box.add(this.tree1);
			
			this.tree1.addListener("dblclick",this.treeLabel,this);	//changeSelection
			
			this.config.formList = {
					schedule : "Scheduler",
					val : "Functionalities"
       		/*	type: 'formList',
        		dataIndex: 'features',
        		formList: qooxdoo.ui.custom.FeatureDetailsForm  */
        	}
			this.inputField = qooxdoo.commons.WidgetUtils.createInputFormList(this.config);
        	
        	var box1 = new qx.ui.container.Scroll().set({
					    width: 200,
					    height: 200
					  });
        	
			box1.add(this.inputField);
			box.add(box1,{flex:1});
			//box.add(this.inputField);
        	  
        	  
        /*	  
        	  var tabView = new qx.ui.tabview.TabView;
      		  box.add(tabView);//, {flex:1}
      				
      	      var page1 = new qx.ui.tabview.Page('tab-', "qx/icon/Oxygen/16/actions/edit-delete.png");
      		  tabView.add(page1);
      		  page1.setLayout(new qx.ui.layout.Grow())
      				
        	   var textfield3 = qooxdoo.commons.WidgetUtils.createInputTextField({
								        		type: 'text',
        		dataIndex: 'customer',
        		text: 'Customer',
        		mandatory: false	
								        	});
				
				page1.add(textfield3);
				
				*/
				
				}
				
				//
				else {
					this.setCaption("Scheduler Window");
					// can also override the container
					var m = new qooxdoo.ui.custom.MasterDetailsPage("datasource");//"datasource"
					/*
					var records = qooxdoo.app.data.DataService.loadDatasourceRecords();
					var form = new qooxdoo.ui.custom.DatasourceDetailsForm();
					m.setForm(form);
					var window_table = new qooxdoo.ui.table.Table(m, records);
					this.add(window_table);
					this.add(form);
					*/
					this.add(m);
					
					var dateField = new qx.ui.form.DateField();
			        var format = new qx.util.format.DateFormat("dd.MM.yyyy");
			        dateField.setDateFormat(format);
			        dateField.setDate(new Date());
			        this.add(dateField);
					
					/*
					this.container = new qx.ui.container.Composite();
	     			this.container.setLayout(new qx.ui.layout.HBox(10));
	     			this.add(this.container);
	     			this.textfield3 = qooxdoo.commons.WidgetUtils.createInputTextField({
											        		type: 'text',
											        		dataIndex: 'description',
											        		text: 'Choose Date',
											        		labelwidth: 100,
											        		mandatory: false
											        	});
								
					this.container.add(this.textfield3);
					var chooser = new qx.ui.control.DateChooser();
					this.container.add(chooser);
					chooser.addListener("changeDate",this._OnDate, this);
					 */
					 
			  		var timeField = new qooxdoo.ui.TimeField();
			        var format = new qx.util.format.DateFormat("hh.mm a");
			        timeField.setTimeFormat(format);
			        timeField.setDate(new Date());
			        this.add(timeField);
				}
				
		  	}
		  	
		 
  },
  
  	members :
  {
  	config : {},
  	tree1 : undefined,
  	nodeLabel: undefined,
    inputField: undefined,
    inputField3: undefined, 
    container : undefined,
  	treeFunction: undefined,
  	
  	treeLabel : function(e){
		    	
		
		    	var item = this.tree1.getSelectedItem();
		
		    	if(item instanceof qx.ui.tree.TreeFile){	//qx.ui.tree.TreeFolder
		    		this.nodeLabel = item.getLabel();
		    		this.inputField.addInstance(this.nodeLabel);
			   	}
	}
	/*	    
	,_OnDate : function(e){
		
		
		// 	var object = this.container.getChildren()[0];
		// 	alert(e.getData().toString());//(object.setValue(String value));
		
		// 	if(this.textfield3.getUserData('type') === 'text')
		// 	this.textfield3.dataMappings[dataIndex].getUserData('field');
		
		 	var dummy = e.getData().toString();
	
		 	var obj = this.textfield3.getUserData('field');
		 	var obj1 = obj.getChildren()[0];
		 	obj1.setValue(dummy);
	
		
	},
	
	showTime: function(e){
		alert("Image clicked");
	}
	*/
  }
  
});
  