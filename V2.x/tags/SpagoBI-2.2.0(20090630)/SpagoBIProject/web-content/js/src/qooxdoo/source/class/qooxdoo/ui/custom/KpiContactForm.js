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
 * @author Andrea Gioia (andrea.gioia@eng.it)
 * @author Amit Rana (amit.rana@eng.it), 
 * @author Gaurav Jauhri (gaurav.jauhri@eng.it)
 */

/**
* This class defines the Dataset Details Form. 
* 
*/

qx.Class.define("qooxdoo.ui.custom.KpiContactForm", {
	extend : qx.ui.container.Composite,
	
	/** 
	*  When the constructor is called it returns an object of form type.
	* <p> To this form is associated the following fields :- 
	* <p> Label -> dataIndex: 'label'
	* <p> Name  -> dataIndex: 'name'
	* <p> Description -> dataIndex: 'description'
	* <p> Type -> dataIndex: 'type'
	* <p> File name  -> dataIndex: 'fileName'
	* <p> *Example :- *
	*  var simpleform = new qooxdoo.ui.custom.DatasetDetailsForm();
	*  simpleform.setData({
	*  label:'Label',
	*  name:'Engine',
	*  description:'Description',
	*  Type:'Type',
	*  fileName:'File Name',
	*  });
	*
	*/ 	
	construct : function() {
		this.base(arguments);
		this.setLayout(new qx.ui.layout.VBox(10));
		this.createToolbar();
		this.createTable();
	},
	
	members: {
		
		tableData: undefined,
		records: undefined,
		
		createToolbar: function(){
		
		var formBar = new qx.ui.container.Composite(new qx.ui.layout.HBox);
	   	formBar.setBackgroundColor("#F9F9F9");
	   	
	   	var createButton = new qx.ui.toolbar.Button("", qx.util.AliasManager.getInstance().resolve("spagobi/img/spagobi/test/create.png"));
	    var createToolTip = new qx.ui.tooltip.ToolTip("New");
	    createButton.setToolTip(createToolTip);
	    createButton.addListener("execute", this.create, this);
	    
	    var deleteAll = new qx.ui.toolbar.Button("Delete All");//, qx.util.AliasManager.getInstance().resolve("spagobi/img/spagobi/test/delete.png"));
	    var deleteToolTip = new qx.ui.tooltip.ToolTip("Delete All");
	    deleteAll.setToolTip(deleteToolTip);
	    deleteAll.addListener("execute", this.deleteall, this);
	    
	    var formBarArray = [createButton, deleteAll];
		for(var i=0; i<formBarArray.length; i++){
	   		formBar.add(formBarArray[i]);
	   		//formBarManager.add(formBarArray[i]);
	    }
	    this.add(formBar);
	    
	    
	},
		
		createTable: function(){
			this.records = qooxdoo.app.data.DataService.loadKpiAlarmContactStructureRecords();
			var config = {};
			config.dataset = this.records;
			this.tableData = new qooxdoo.ui.table.Table(this, config);
			this.setUserData('contactinfo', this.tableData);
			this.add(this.tableData);
	},
	
		deleteall: function(e) {
	    	var model = this.tableData.getTableModel();
	    	var rowcount = model.getRowCount();
	    	model.removeRows(0, rowcount);
	    },
		
		create: function(e) {
	    	var w = new qx.ui.window.Window(null, "qx/icon/Oxygen/16/apps/office-calendar.png");
			w.setLayout(new qx.ui.layout.VBox(5));
			this.setUserData('contactwindow',w);
			this.addListener("resize", function(){ this.center();}, this);
			w.open();
			w.setModal(true);
			var m = new qooxdoo.ui.custom.MasterDetailsPage("kpi_alarm_contact_info");
			w.add(m);
			this.setUserData('masterdetailspage',m);
			//Adding button to add contacts from table to sub-table
			var fBar = m.getUserData('formbar');
			var addContactButton = new qx.ui.toolbar.Button("", qx.util.AliasManager.getInstance().resolve("spagobi/img/spagobi/test/create.png"));
		    var addContactToolTip = new qx.ui.tooltip.ToolTip("Add selected contacts");
		    addContactButton.setToolTip(addContactToolTip);
		    addContactButton.addListener("execute", this._onAddContact, this);
		    fBar.add(addContactButton);
	    }
	    
	    ,_onAddContact: function(e){
	    	
	    	var m = this.getUserData('masterdetailspage');
	    	var t = m._pagedTable._table;
	    	
	    	var selectedRowData = [];
	    	t.getSelectionModel().iterateSelection(function(index) {
	    		selectedRowData.push(t.getTableModel().getRowData(index));
	    		//alert(t.getTableModel().getRowData(index));
	    	});
	    	
	    	//qooxdoo.commons.CoreUtils.dump(selectedRowData);
	    	var t2 = this.getUserData('contactinfo');
	    	var rowsToBeAdded = [];
	    	for(var p in selectedRowData){
	    		//qooxdoo.commons.CoreUtils.dump(selectedRowData[p]);
	    		//t2.getTableModel().addRows([['User10','1122334455','user10@user10.com','Yes','qx/icon/Oxygen/16/actions/dialog-close.png']]);
	    		//t2.getTableModel().addRows(selectedRowData[p]);
	    		
	    		var temp = [];
	    		delete selectedRowData[p].id;
	    		selectedRowData[p].deletebutton = "qx/icon/Oxygen/16/actions/dialog-close.png";
	    		
	    		for(prop in selectedRowData[p]){
	    			temp.push(selectedRowData[p][prop]);
	    		}	
	    		rowsToBeAdded.push(temp);
	    		
	    	}
	    	t2.getTableModel().addRows(rowsToBeAdded);
	    	
	    	var w = this.getUserData('contactwindow');
	    	w.close();
	    	
	    }	
		
		
	}
});