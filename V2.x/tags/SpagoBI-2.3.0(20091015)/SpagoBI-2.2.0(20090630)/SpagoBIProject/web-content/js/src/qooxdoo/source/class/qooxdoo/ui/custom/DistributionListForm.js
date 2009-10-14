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
* This class defines the Data Source Details Form.  
*   
*/


qx.Class.define("qooxdoo.ui.custom.DistributionListForm", {
	extend: qx.ui.container.Composite,
	
	construct : function(config) { 
		this.base(arguments);
		this.setLayout(new qx.ui.layout.VBox(5));
		this.createTextFields();
		
		this.createUserDocTabs();
		
	},
	
	/**
	  * Members of the class
	  */
	  
	members: {
		
		createTextFields: function(){
			var f = new qooxdoo.ui.custom.DistributionListDetailsForm();
			this.add(f);
			this.setUserData('form',f);
		},
		
		createUserDocTabs: function(){
			var tabview = new qx.ui.tabview.TabView();
			
			var userpage = new qx.ui.tabview.Page("Users");
			//userpage.setLayout(new qx.ui.layout.Grid());
			userpage.setLayout(new qx.ui.layout.VBox());
			
			var records = qooxdoo.app.data.DataService.loadDistributionListUserData();
			var config = {};
			config.dataset = records;
			var usertable = new qooxdoo.ui.table.Table(this, config);
			usertable.getSelectionModel().removeListener("changeSelection",usertable._onChangeSelection, usertable);
			this.setUserData('usertable', usertable);
			userpage.add(usertable);
			tabview.add(userpage);
			
			
			var docpage = new qx.ui.tabview.Page("Documents");
			docpage.setLayout(new qx.ui.layout.VBox());
			var records2 = qooxdoo.app.data.DataService.loadDistributionListDocData();
			var config2 = {};
			config2.dataset = records2;
			var doctable = new qooxdoo.ui.table.Table(this, config2);
			doctable.getSelectionModel().removeListener("changeSelection",doctable._onChangeSelection, doctable);
			this.setUserData('doctable', doctable);
			docpage.add(doctable);
			tabview.add(docpage);
			
			this.add(tabview);
		}
	}	
	
});	