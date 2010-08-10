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


/* *
 * @author Andrea Gioia (andrea.gioia@eng.it)
 * @author Amit Rana (amit.rana@eng.it)
 * @author Gaurav Jauhri (gaurav.jauhri@eng.it)
 * 
 */
 
/**
 * Class to create the Page view for a selected button of the  top toolbar
 */
 
qx.Class.define("spagobi.ui.PageView", {
	
	extend : qx.ui.splitpane.HorizontalSplitPane,
	
	/**
	 * Constructor to create a Page.
	 * <p> It splits the page into 2 parts. 
	 * Left part -  vertical toolbar is added
	 * Right part - selected page (based on button selected on toolbar) is added
	 * <hr>
	 * <p> *Config Options*
	 * <p> *toolbar*: Object 
	 * 	<p>&nbsp;&nbsp;&nbsp;&nbsp; 
	 * *toolbar options*:
	 * 		<p>&nbsp;&nbsp;&nbsp;&nbsp; 
	 * 			defaultBackgroudColor: String
	 * 				<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 				Background color of the button on the left vertical toolbar.
	 * 
	 * 		<p>&nbsp;&nbsp;&nbsp;&nbsp;
	 *  		focusedBackgroudColor: String
	 * 				<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 				Color of button on left vertical tool bar when pressd.	
	 * 
	 * 		<p>&nbsp;&nbsp;&nbsp;&nbsp;
	 * 			buttons : Array
	 * 				<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 				Array of objects with following properties:
	 * 
	 * 				<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 				name : String
	 * 					Name of the button for future reference
	 * 
	 *  			<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 				image : String
	 * 					Image of the button
	 * 
	 * 				<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 *  			page : String
	 * 					Name of the form to be displayed when button is selected.
	 * 
	 * <p>*defaultSelectedPage*: String 
	 * 			<p>&nbsp;&nbsp;&nbsp;&nbsp; Name of the page based on the top toolbar button
	 * 
	 * <p>*Example*
	 * <p><code>
	 * 	var obj = new spagobi.ui.PageView({
	 * 			toolbar: {
     * 					defaultBackgroudColor: 'white',
     * 					focusedBackgroudColor: '#DEFF83',
     * 					buttons: [
     * 							{
     * 								name: 'engine',
	 *								image:'spagobi/img/spagobi/test/abc.png',
	 *								page: 'engine'
     * 							}, {
     * 								name: 'datasource',
	 *								image:'spagobi/img/spagobi/test/xyz.png',
	 *								page: 'datasource'
     * 							}]
     * 			 		},
     * 			defaultSelectedPage: 'engine' });		 
	 * </code>
	 * 
	 * @param config The config has the properties as described above.
	 */
	construct : function(config) {
		this.base(arguments, 70, "1*");
		this.setShowKnob(false);
		this.setSplitterSize(0);
		
		this._pages = [];
		
		var toolbarConfig = {
			defaultBackgroudColor: config.toolbar.defaultBackgroudColor,
			focusedBackgroudColor: config.toolbar.focusedBackgroudColor,
			defaultSelectedPage : config.defaultSelectedPage
		};
		this._toolbar = new spagobi.ui.IconBar( toolbarConfig );
		
		var buttonsConfig = config.toolbar.buttons;
		for(var i = 0; i < buttonsConfig.length; i++) {
			
			//
			/* New code
					if(buttonsConfig[i].name == 'engine'){
						this.setSplitterSize(50);
						//alert('Children : '+this.getChildrenLength());
						var c = this.getChildren();
						for(j=0; j<c.length; j++){
							alert('Child '+ j + ': '+ c[j]);
							alert(c[j].getAllowStretchX());
							c[j].setAllowStretchX(false); 	//boxlayout
							alert(c[j].getAllowStretchX());
						}	
						//this.getFirstChild().getAllowStretchX();
					}
			//  
			 */	
			buttonsConfig[i].handler =  function(e){this.selectPage(e.getTarget().getUserData('name'));};
			buttonsConfig[i].context = this;
			this._toolbar.addButton( buttonsConfig[i] );
		}
		
		if(config.defaultSelectedPage) {
			this._defaultSelectedPageName = config.defaultSelectedPage;
		}	
		
		this.addLeft( this._toolbar );
		
	},
	
	/**
	 * Members of the PageView class
	 */
	members : {
		
		_toolbar: undefined,
		_pages: undefined,
		_selectedPageName: undefined,
		_defaultSelectedPageName: undefined,
		
		/**
		 * Function to show the required page on the right side on the window.
		 * <p>It hides the previously displayed page and makes the current page visible.
		 * <p>The displayed page contains a list and a form.
		 * 
		 * <p>*Example*
		 * <p><code>
		 * var p = new spagobi.ui.PageView(..);
		 * p.selectPage('engine');
		 * </code>
		 * 
		 * @param pageName Name of the page to be shown 
		 */
		selectPage: function(pageName) {
    		if(!this._pages[pageName]) {
    			if (pageName== "funcManagement")
    			{
    				this._pages[pageName] = new spagobi.ui.custom.FunctionalClassDummy(pageName);
    			}
    			else
    			{	
    				this._pages[pageName] = new spagobi.ui.custom.MasterDetailsPage(pageName);
    			}
    			
    			this.addRight( this._pages[pageName] );
    		}
    	
    	
	    	if(this._selectedPageName) {
	    		//this._pages[this._selectedPageName].setVisibility(false);
	    		this._pages[this._selectedPageName].setDisplay(false);
	    	}
	    	this._selectedPageName = pageName;
	    	
	    	
	    	//this._pages[pageName].setVisibility(true);	// added
	    	this._pages[pageName].setDisplay(true);	// added
	    	
	    			//if (pageName != "funcManagement"){	
	    	//this._pages[pageName].show();
	    			//}
	    	 	  
    	},
    	
    	/**
    	 * Function to select the default page
    	 */
    	selectDefaultPage: function() {
    		this.selectPage(this._defaultSelectedPageName);
    	},
    	
    	/**
    	 * Function to select the current page name
    	 */
    	getSelectedPageName: function() {
    		return this._selectedPageName;
    	}
    	
    	
			
	}
});