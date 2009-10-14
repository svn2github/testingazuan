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


qx.Class.define("qooxdoo.ui.NavigationBar", {
	
			//extend : qx.legacy.ui.toolbar.ToolBar,
	//extend : qx.legacy.ui.layout.HorizontalBoxLayout,//change
	extend : qx.ui.container.Composite,
	
	/**
	 * Constructor to create the navigation bar.
	 * <p>The constructor calls the createNavBar() function.
	 * <p>Also see the createNavBar() function
	 * 
	 * <p>*Example*
	 * <p><code>
	 * var navigationBar = new qooxdoo.ui.NavigationBar();
	 * </code>
	 * 
	 */
	construct : function() {
		this.base(arguments);
		this.setLayout(new qx.ui.layout.HBox(5));
		this.getLayout().setAlignX("center");
		
		this.PageNum = [ {
							label : '',
							url : undefined
						}
					   ];
		
		this.createNavBar();
		
	},
	
	members : {
		
		
		PageNum : undefined, 
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
		
			var firstPageButton = new qx.ui.toolbar.Button("", qx.util.AliasManager.getInstance().resolve("spagobi/img/spagobi/test/firstPage.png"));
			this.add(firstPageButton);
			
			var prevPageButton = new qx.ui.toolbar.Button("", qx.util.AliasManager.getInstance().resolve("spagobi/img/spagobi/test/previousPage.png"));
			this.add(prevPageButton);
			
					//horizontalbarLayout.add(new qx.legacy.ui.basic.HorizontalSpacer());
			
			var number = [];
			number = qooxdoo.app.data.DataService.loadPageMeta();
			var labelOfPage = [];
			for (i in number){
				labelOfPage[i] = new qx.ui.basic.Label(number[i].name);
				this.add(labelOfPage[i]);
					labelOfPage[i].setAlignY("middle");
					labelOfPage[i].setUserData('url', number[i].url);
					labelOfPage[i].addListener("click",this._onClick, this);
					labelOfPage[i].addListener("mouseover",this._onmouseover, this);
					labelOfPage[i].addListener("mouseout", this._onmouseout, this);
			}
			
			var nextPageButton = new qx.ui.toolbar.Button("", qx.util.AliasManager.getInstance().resolve("spagobi/img/spagobi/test/nextPage.png"));
			this.add(nextPageButton);
			
			var lastPageButton = new qx.ui.toolbar.Button("", qx.util.AliasManager.getInstance().resolve("spagobi/img/spagobi/test/lastPage.png"));
			this.add(lastPageButton);
						
		},
		
		_onClick : function(e){
			var url1 = e.getTarget().getUserData('url');
			alert (url1);
		},
		
		 _onmouseover: function(e) {	 		
	 	
	 		e.getTarget().setBackgroundColor("orange");
	 	},
	 	
	 	_onmouseout: function(e) {
	    
	 		e.getTarget().setBackgroundColor(null);
	 	}	
	}
});
