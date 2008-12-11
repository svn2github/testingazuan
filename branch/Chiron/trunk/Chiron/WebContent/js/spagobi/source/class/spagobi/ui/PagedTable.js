
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
  
	//extend : qx.legacy.ui.layout.VerticalBoxLayout, //qx.legacy.ui.layout.GridLayout,//change
	extend : qx.ui.core.Widget,//qx.ui.container.Composite,
	
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
    	var layout = new qx.ui.layout.VBox();
	  	this._setLayout(layout);
    //	this.setLayout(new qx.ui.layout.VBox);
    	  	
    	//this.setWidth("100%");// try also "auto"
		//this.setHeight("100%");
						//	this.setOverflow("auto");
						//	alert (document.documentElement.clientWidth);
					  //	alert (window.innerWidth);
					//		alert (screen.width);
						//  alert (this.getWidthValue());
						//	alert (this.getMaxWidth());
		this._filterBar = new spagobi.ui.FilterBar();
    	
    	this._table = new spagobi.ui.Table(controller, data);
    	this._navigationBar = new spagobi.ui.NavigationBar(); 
    	
    	//var atom1 = new qx.legacy.ui.basic.Atom();//change
    	//var atom1 = new qx.ui.basic.Atom();
    			//	atom1.setWidth('100%'); 
    	//atom1.add( this._filterBar );
    	//atom1.setHorizontalAlign("center");//change 
    	//this.add( atom1 );
    	this._add(this._filterBar);
    	/*
    	var scroll = new qx.ui.container.Scroll().set({
 //   width: 300,
    height: 150
  });
    	;
	    scroll.add(this._table);//,{flex:1});
      	this._add(scroll,{flex:1});
      	
      	 var scroll = new qx.ui.container.Scroll();
	     scroll.add(this._table,{flex:1});
	     this._add(scroll);
	      */
 	  	this._add(this._table,{flex:1});//
    	this._add(this._navigationBar);
			    	/*    	
			    	var atom0 = new qx.legacy.ui.basic.Atom();
			    	atom0.setWidth('100%'); 
			        atom0.setHeight('1*');
			        atom0.setBackgroundColor('white');
			    	atom0.add( this._table );
			    	this.add( atom0 );
			    	*/
    	
    	/*
    	this._table.setWidth('100%');
    	this._table.setHeight('1*');
    	this.add(this._table);
    	
    	    	
    	var atom2 = new qx.legacy.ui.basic.Atom();
  				//  	atom2.setWidth('100%');  
    	atom2.add( this._navigationBar );
    	atom2.setHorizontalAlign("center");
    	this.add( atom2 );
    	*/
  	},

	members : {
    	_filterBar: undefined,
    	_navigationBar: undefined,
    	_table: undefined    
    }
});
