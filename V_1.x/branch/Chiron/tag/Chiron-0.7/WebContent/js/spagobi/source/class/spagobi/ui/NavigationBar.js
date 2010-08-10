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
 * Class to create the navigation bar
 */ 


qx.Class.define("spagobi.ui.NavigationBar", {
	
	extend : qx.ui.layout.HorizontalBoxLayout,
	//extend : qx.ui.toolbar.ToolBar,
	
	/**
	 * Constructor to create the navigation bar.
	 * <p>The constructor calls the createNavBar() function.
	 * <p>Also see the createNavBar() function
	 * 
	 * <p>*Example*
	 * <p><code>
	 * var navigationBar = new spagobi.ui.NavigationBar();
	 * </code>
	 * 
	 */
	construct : function() {
		this.base(arguments);
		this.setWidth("100%");// try also "auto"
		this.setHeight("100%");
	//	this.setCentered(true);//setLeft("center");
	//	this.setTop(-5);
		this.createNavBar();
		
	},
	
	members : {
		
		/**
		 * Function to create the navigation bar.
		 * <p>It is called by the constructor.
		 * <p> The navigation bar has 4 buttons :
		 * <p> Button to go to first page of list
		 * <p> Button to go to previous page of list
		 * <p> Button to go to next page of list
		 * <p> Button to go to last page of list
		 */
		createNavBar : function(){
			
			var firstPageButton = new qx.ui.toolbar.Button("", "spagobi/img/spagobi/test/firstPage.png");
			this.add(firstPageButton);
		//	horizontalbarLayout.add(firstPageButton);
			
			var prevPageButton = new qx.ui.toolbar.Button("", "spagobi/img/spagobi/test/previousPage.png");
			this.add(prevPageButton);
			//horizontalbarLayout.add(prevPageButton);
			
			//horizontalbarLayout.add(new qx.ui.basic.HorizontalSpacer());
			
			var nextPageButton = new qx.ui.toolbar.Button("", "spagobi/img/spagobi/test/nextPage.png");
			this.add(nextPageButton);
			//horizontalbarLayout.add(nextPageButton);
			
			var lastPageButton = new qx.ui.toolbar.Button("", "spagobi/img/spagobi/test/lastPage.png");
			this.add(lastPageButton);
			//horizontalbarLayout.add(lastPageButton);
			
			/*horizontalbarLayout.setAlign("right",null);
			this.add(horizontalbarLayout);*/
		}	
	}
});
