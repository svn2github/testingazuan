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
qx.Class.define("spagobi.ui.FilterBar", {
	extend : qx.ui.layout.HorizontalBoxLayout,
	
	/**
	 * Constructor to create the filter bar.
	 * <p>The constructor calls the createFilterBar() function.
	 * <p>Also see the createFilterBar() function
	 * 
	 * <p>*Example*
	 * <p><code>
	 * var filterBar = new spagobi.ui.FilterBar();
	 * </code>
	 * 
	 */
	construct : function() {
		this.base(arguments);
		this.createFilterBar();
	},
	
	members : {
		createFilterBar : function(){
			
			
			var combo1 = spagobi.commons.WidgetUtils.createInputComboBox(
											{
												text: 'The value of the column',
												width: 120,
											 	items: ["Label","Name","Description"]
											
											}
			);
			this.add(combo1);
			
			var combo2 = spagobi.commons.WidgetUtils.createInputComboBox(
											{
												text: 'as a',
												width: 50,
											 	items: ["string","number","date"]
											
											}
			);
			this.add(combo2);
			
			var combo3 = spagobi.commons.WidgetUtils.createComboBox(
											{
	        									items: ["starts with","ends with","contains","=","<","<=",">",">="]
	       									});
			this.add(combo3);
			
			var txt = spagobi.commons.WidgetUtils.createTextField(
											{
												width: 70,
        										height: 20
											});
																 
			this.add(txt);													 
			/*
			var label = new qx.ui.basic.Label("The value of the column");
			
			var combo_box = new qx.ui.form.ComboBox();
			var items = ["Label","Name","Description"];
			for(var i=0; i< items.length; i++) {
              var item = new qx.ui.form.ListItem(items[i]);
              combo_box.add(item);
            }
            combo_box.setSelected(combo_box.getList().getFirstChild());
            
            this.add(txtField,combo_box);
            */
		}
	}
	
});