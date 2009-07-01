
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
 
/**
 * Class to create a list (table) with Filter bar at its top and the Navigation bar below
 * 
 */ 


qx.Class.define("qooxdoo.ui.table.PagedTable", {
  
	extend : qx.ui.core.Widget,//qx.ui.container.Composite,
	
	/**
	 * Constructor to create the entity with filter bar, list and navigation bar
	 * 
	 * *Example*
	 * <p><code>
	 * var obj = new qooxdoo.ui.custom.MasterDetailsPage();
	 * var records = qooxdoo.app.data.DataService.loadFeatureRecords();
	 * var listPage = new qooxdoo.ui.PagedTable(obj, records );
	 * </code>
	 * 
	 * @param controller Object which is used to set the data of the form
	 * @param data Object containing the layout of the list and the data of the list and form
	 */
	construct : function(controller, config)  {
    	   	
    	this.base(arguments);
    	var layout = new qx.ui.layout.VBox();
	  	this._setLayout(layout);
   
		this._filterBar = new qooxdoo.ui.FilterBar();
    	
    	this._table = new qooxdoo.ui.table.Table(controller, config);
    	this._navigationBar = new qooxdoo.ui.NavigationBar(); 
  
    	this._add(this._filterBar);
  
 	  	this._add(this._table,{flex:1});
    	this._add(this._navigationBar);
  	},

	members : {
    	_filterBar: undefined,
    	_navigationBar: undefined,
    	_table: undefined    
    }
});
