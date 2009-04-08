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
 * @author Amit Rana (amit.rana@eng.it)
 * @author Gaurav Jauhri (gaurav.jauhri@eng.it)
 * 
 */
 


/**
 * This class defines the Formlist for a set of subforms on a form
 *  
 */

qx.Class.define("qooxdoo.ui.custom.KpiModelInstanceForm", {
	extend: qx.ui.tabview.TabView,
	
	/**
	 * 
	 */
	
	construct : function() { 
				
		this.base(arguments);
		
		this.createDetailsTab();
		
		this.createTreeTab();
		
		this.createResourceTab();
		
	},
		 
	/**
	 * Members of the class
	 */
		  
	members: {
		
		createDetailsTab: function(){
			var page = new qx.ui.tabview.Page("DETAIL");
			page.setLayout(new qx.ui.layout.Grow());
			
			var form = new qooxdoo.ui.custom.KpiModelInstanceDetailForm();
			page.add(form);
			
			this.add(page);
			this.setUserData('details',page);
		},
		
		createTreeTab: function(){
			var page = new qx.ui.tabview.Page("TREE");
			page.setLayout(new qx.ui.layout.Grow());
			
			var form = new qooxdoo.ui.custom.KpiModelInstanceTreeForm();
			page.add(form);
			
			this.add(page);
			this.setUserData('tree',page);
		},
		
		createResourceTab: function(){
			var page = new qx.ui.tabview.Page("Resource");
			page.setLayout(new qx.ui.layout.Grow());
			
			var form = new qooxdoo.ui.custom.KpiModelInstanceResourceForm();
			page.add(form);
			
			this.add(page);
			this.setUserData('resource',page);
		}
	}	
});
	