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
	 * var navigationBar = new spagobi.ui.NavigationBar();
	 * </code>
	 * 
	 */
	construct : function() {
		this.base(arguments);
		this.setLayout(new qx.ui.layout.HBox);
		this.getLayout().setAlignX("center");
		//this.setWidth("100%");// try also "auto"
		//this.setHeight("100%");
				//	this.setCentered(true);//setLeft("center");
				//	this.setTop(-5);
//		var number = [{label : '1',url:'http://www.google.com'}];
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
		
		PageNum :[ {
					label : '',
					url : undefined
		}], 
		
		createNavBar : function(){
			
			//var firstPageButton = new qx.legacy.ui.toolbar.Button("", "spagobi/img/spagobi/test/firstPage.png");//change
			var firstPageButton = new qx.ui.toolbar.Button("", qx.util.AliasManager.getInstance().resolve("spagobi/img/spagobi/test/firstPage.png"));
			this.add(firstPageButton);
					//	horizontalbarLayout.add(firstPageButton);
			
			//var prevPageButton = new qx.legacy.ui.toolbar.Button("", "spagobi/img/spagobi/test/previousPage.png");//change
			var prevPageButton = new qx.ui.toolbar.Button("", qx.util.AliasManager.getInstance().resolve("spagobi/img/spagobi/test/previousPage.png"));
			this.add(prevPageButton);
					//horizontalbarLayout.add(prevPageButton);
			
					//horizontalbarLayout.add(new qx.legacy.ui.basic.HorizontalSpacer());
			
			//var nextPageButton = new qx.legacy.ui.toolbar.Button("", "spagobi/img/spagobi/test/nextPage.png");//change
			var number = [];
			number = spagobi.app.data.DataService.loadPageMeta();
			var labelOfPage = [];
			for (i in number){
//				PageNum[i] = number[i];
//				alert(number[i].label);
				labelOfPage[i] = new qx.ui.basic.Label(number[i].name);
				var dummyLabel = new qx.ui.basic.Label("  ");
				this.add(labelOfPage[i]);
				this.add(dummyLabel);	
//				labelOfPage[i].setAlignX("center");
				labelOfPage[i].setAlignY("middle");
//				labelOfPage[i].setTextAlign("center");
				labelOfPage[i].setUserData('url', number[i].url);
				labelOfPage[i].addListener("click",this._onClick, this);
				labelOfPage[i].addListener("mouseover",this._onmouseover, this);
				labelOfPage[i].addListener("mouseout", this._onmouseout, this);
			}
			var dummyLabel1 = new qx.ui.basic.Label("  ");
			this.add(dummyLabel);
				
			var nextPageButton = new qx.ui.toolbar.Button("", qx.util.AliasManager.getInstance().resolve("spagobi/img/spagobi/test/nextPage.png"));
			this.add(nextPageButton);
					//horizontalbarLayout.add(nextPageButton);
			
			//var lastPageButton = new qx.legacy.ui.toolbar.Button("", "spagobi/img/spagobi/test/lastPage.png");//change
			var lastPageButton = new qx.ui.toolbar.Button("", qx.util.AliasManager.getInstance().resolve("spagobi/img/spagobi/test/lastPage.png"));
			this.add(lastPageButton);
					//horizontalbarLayout.add(lastPageButton);
			
			
		},
		
		
		_onClick : function(e){
			
//		alert(e.getTarget().getLabel());
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
