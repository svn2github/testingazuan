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
 * Class to create the Filter bar for searching an item in the list 
 */
qx.Class.define("qooxdoo.ui.FilterBar", {
	//extend : qx.legacy.ui.layout.HorizontalBoxLayout,//change
	extend : qx.ui.container.Composite,
		
	/**
	 * Constructor to create the filter bar.
	 * <p>The constructor calls the createFilterBar() function.
	 * <p>Also see the createFilterBar() function
	 * 
	 * <p>*Example*
	 * <p><code>
	 * var filterBar = new qooxdoo.ui.FilterBar();
	 * </code>
	 * 
	 */
	construct : function() {
		this.base(arguments);
		this.setLayout(new qx.ui.layout.HBox(5));
		this.getLayout().setAlignX("center");
		this.getLayout().setAlignY("middle");

		this.createFilterBar();
	},
	
	members : {
		
		/**
		 * Function to create the filter bar.
		 * <p> It is called by the constructor.
		 * <p> The filter bar contains the following items:
		 * <p> Label - Tells to select the column in the list to search into
		 * <p> Combo box - Shows a list of columns
		 * <p> Label - tells the search criteria to be selected
		 * <p> Combo box - to select the search criteria
		 * <p> Combo box - select the search criteria
		 * <p> text field - Tells to specify the text to be searched
		 */
		createFilterBar : function(){
			
			/*
			var combo1 = qooxdoo.commons.WidgetUtils.createInputComboBox(
											{
												text: 'The value of the column',
												width: 120,
											 	items: ["Label","Name","Description"]
											
											}
			);
			this.add(combo1);
			
			var combo2 = qooxdoo.commons.WidgetUtils.createInputComboBox(
											{
												text: 'as a',
												width: 50,
											 	items: ["string","number","date"]
											
											}
			);
			this.add(combo2);
			
			var combo3 = qooxdoo.commons.WidgetUtils.createComboBox(
											{
	        									items: ["starts with","ends with","contains","=","<","<=",">",">="]
	       									});
			this.add(combo3);
			
			var txt = qooxdoo.commons.WidgetUtils.createTextField(
											{
												width: 70,
        										height: 20
											});
																 
			this.add(txt);
			*/
			
			//var combo1_label = new qx.legacy.ui.basic.Label("  The value of the column   ");//change
			var combo1_label = new qx.ui.basic.Label("  The value of the column   ");
					
			this.add(combo1_label);
			
			
			//var combo1 = new qx.legacy.ui.form.ComboBox();//change
			var combo1 = new qx.ui.form.ComboBox();
			var items1 = ["Label","Name","Description"];
			for(var i=0; i< items1.length; i++) {
              //var item = new qx.legacy.ui.form.ListItem(items1[i]);//change
              var item = new qx.ui.form.ListItem(items1[i]);
              combo1.add(item);
            }
			this.add(combo1);
			
			//var combo2_label = new qx.legacy.ui.basic.Label(" as a ");//change
			var combo2_label = new qx.ui.basic.Label(" as a ");
				//with(combo2_label){	setTextAlign("center");	}
			this.add(combo2_label);
			
			//var combo2 = new qx.legacy.ui.form.ComboBox();//change
			var combo2 = new qx.ui.form.ComboBox();
			var items2 = ["Label","Name","Description"];
			for(var i=0; i< items2.length; i++) {
              //var item = new qx.legacy.ui.form.ListItem(items2[i]);//change
               var item = new qx.ui.form.ListItem(items2[i]);
              combo2.add(item);
            }
			this.add(combo2);
			
			//var dummy_label = new qx.legacy.ui.basic.Label(" ");//change
			var dummy_label = new qx.ui.basic.Label(" ");
			this.add(dummy_label);
			
			//var combo3 = new qx.legacy.ui.form.ComboBox();//change
			var combo3 = new qx.ui.form.ComboBox();
			var items3 = ["starts with","ends with","contains","=","<","<=",">",">="];
			for(var i=0; i< items3.length; i++) {
              //var item = new qx.legacy.ui.form.ListItem(items3[i]);//change
              var item = new qx.ui.form.ListItem(items3[i]);
              combo3.add(item);
            }
			this.add(combo3);													 
			
			//var dummy_label1 = new qx.legacy.ui.basic.Label(" ");//change
			var dummy_label1 = new qx.ui.basic.Label(" ");
			this.add(dummy_label1);
			
			//var txt1 = new qx.legacy.ui.form.TextField();//change
			var txt1 = new qx.ui.form.TextField();
			with(txt1){
				width : 70
			}
			this.add(txt1);
			
			var filterButton = new qx.ui.form.Button("Filter");
			this.add(filterButton);
			filterButton.addListener("execute", this.filterFunction,this);
			
			var removeFilterButton = new qx.ui.form.Button("All");
			this.add(removeFilterButton);
			removeFilterButton.addListener("execute", this.removeFilterFunction,this);
		},
		
		filterFunction: function(e){
			alert("Filter");
			
		},
		
		removeFilterFunction: function(e){
			alert("Filter Removed");
			
		}
	}
	
});