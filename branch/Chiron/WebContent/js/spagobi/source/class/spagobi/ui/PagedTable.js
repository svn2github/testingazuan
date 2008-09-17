
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


qx.Class.define("spagobi.ui.PagedTable", {
  
	extend : qx.ui.layout.VerticalBoxLayout, //qx.ui.layout.GridLayout,

	/**
	 * Constructor to create the entity with filter bar, list and navigation bar
	 * 
	 * *Example*
	 * <p><code>
	 * var obj = new spagobi.ui.custom.MasterDetailsPage();
	 * var records = spagobi.app.data.DataService.loadFeatureRecords();
	 * var listPage = new spagobi.ui.PagedTable(obj, records );
	 * </code>
	 * 
	 * @param controller Object which is used to set the data of the form
	 * @param data Object containing the layout of the list and the data of the list and form
	 */
	construct : function(controller, data)  {
    	    	
    	this.base(arguments);    	
    	
    	this._filterBar = new spagobi.ui.FilterBar();
    	this._table = new spagobi.ui.Table(controller, data);
    	this._table.set(
		{ 
			width : 1060
		});
    	this._navigationBar = new spagobi.ui.NavigationBar(); 
    	
    	/* 
    	this.addColumn(); 
    	
    	this.addRow();
    	this.add(this._filterBar, 0, 0);
    	
    	this.addRow();
    	this.add(this._table, 0, 1);
    	
    	this.addRow();
    	this.add(this._navigationBar, 0, 2);
    	
    	this.setDimension("100%", "100%");
    	*/
    	
    	
    	var atom1 = new qx.ui.basic.Atom();
    	atom1.add( this._filterBar );
    	atom1.setHorizontalAlign("center");
    	//atom1.setVerticalAlign("bottom");
    	atom1.set ({height: 50/*, marginTop: 0*/});
    	this.add( atom1 );
    	    	
    	var atom0 = new qx.ui.basic.Atom();
    	atom0.add( this._table );
    	this.add( atom0 );
    	    	
    	var atom2 = new qx.ui.basic.Atom();
    	atom2.add( this._navigationBar );
    	this.add( atom2 );
    	
  	},

	members : {
    	_filterBar: undefined,
    	_navigationBar: undefined,
    	_table: undefined    
    }
});
