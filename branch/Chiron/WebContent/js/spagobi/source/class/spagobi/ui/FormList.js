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


qx.Class.define("spagobi.ui.FormList", {
	extend: spagobi.ui.Form,
	
/**
 * Constructor to create a FormList.
 * <p> It creates a tab of with an empty subform and a dummy tab at the end.
 * <p> Clicking on the dummy tab allows to create a new subform tab.
 * <p> The constructor accepts an array with an object of type Form. 
 * <p> The elements of this Form object are reflected in each of the subforms. 
 * <p> The parameter accepted by the constructor has the following properties:
 * <p> Type -> Type of object to be shown in the subforms. It is of type 'form'. 
 * <p> Also see setConfig() and addInstance() methods.
 * 
 * <p> *Example :- *
 * <p><code>
 * var prop = [
 * 				{ 
 *	        		type: 'form',
 * 					dataindex: 'subform',		
 *	        		form:	
 *	        			[{
 *							type: 'text',
 *							dataIndex: 'value',
 *							text: 'Value',
 *							labelwidth: 100,
 *							mandatory: true
 *					     },
 *	        			 {
 *							type: 'text',
 *							dataIndex: 'description2',
 *							text: 'Description',
 *							labelwidth: 100,
 *							mandatory: true
 *					     }]
 *		        }
 *				];
 * var formList = new spagobi.ui.FormList(prop);
 * </code>
 * 
 * @param config The parameter is an object with the above properties.
 */	
	 construct : function(config) { 
		this.base(arguments);
		
		this._instances = [];
		this.setConfig(config);
		
		this._tabView = new qx.ui.pageview.tabview.TabView();
		
		this.addInstance();
	  	this.add(this._tabView);
	},
	 
 /**
  * Members of the class
  */
  
	members: {
		
		_config: undefined,
		
		_instances: undefined,
		
		_tabView: undefined,
		
		/**
		 * Function to get the current value of config object
		 * @return The current config object
		 */
		getConfig: function() {
			return _config;
		},
		
		/**
		 * Function to set the value of current of local config objecr to current config
		 * <p> Called by the construtor.
		 * 
		 * @param config The config object passed to the constuctor
		 */
		setConfig: function(config) {
			this._config = config;
		},
		
		/**
		 * Function to add the subform in the list.
		 * <p> It adds a new sunform to the tab and moves the dummy tab to the end of the form list.
		 * 
		 * @param o {Object ? null) Object whose elements need to be to set
		 */
		addInstance: function(o) {
			var subform;
			if( typeof(this._config) == 'object' ) {
				subform = new spagobi.ui.Form(this._config);
			} else {
				subform = new this._config();
			}	
			if(o) {
				subform.setData(o);
			}		
			
			var subFormButton = new qx.ui.pageview.tabview.Button('tab-' + this._instances.length);
			subFormButton.setChecked(true);
	        this._tabView.getBar().add(subFormButton);
	        var subFormPage = new qx.ui.pageview.tabview.Page(subFormButton);
	  		this._tabView.getPane().add(subFormPage);
	  		subFormPage.add(subform);
	  		
	  		this._instances[this._instances.length] = subform;
	  		
	  		this._dummyFunction();
	  		
		},
		
		/**
		 * Protected member function.
		 * <p>This function is used to create the dummy tab, which cretaes a new subform tab when it is clicked.
		 * <p> Called by addInstance() function.
		 */
		_dummyFunction: function(){
			var dummysubFormButton = new qx.ui.pageview.tabview.Button('tab');
			this._tabView.getBar().add(dummysubFormButton);
	        var dummysubFormPage = new qx.ui.pageview.tabview.Page(dummysubFormButton);
	  		this._tabView.getPane().add(dummysubFormPage);
	  		
	  		dummysubFormButton.addEventListener("changeChecked",this._checktab,this);
	  		
	  		dummysubFormPage.add();
	  		this._instances[this._instances.length] = {};
	  		
	  	},
		
		/**
		 * Event Listner function for the dummy tab button.
		 * <p> The function checks if the dummy tab is selected, and if so, it deletes the current dummy tab,
		 *     adds a new subform in the tab list and creates the new dummy tab after it.
		 */
		_checktab: function(e){
			if(e.getTarget().isChecked() == true){
				this.deleteDataAt(this._instances.length-1);
				this._instances.length--;
				this.addInstance();
			}
		},
		
		/**
		 * Function to get the data of the subform list
		 */		
		getData: function() {
			for(var i = 0; i < this._instances.length; i++) {
				 this.dataObject[i] = this._instances[i].getData();
			}
			return this.dataObject;
		},
	
	 	/**
	 	 * Function to delete the complete subform list including all the tabs in it. 
	 	 */
		deleteData: function() {
			var pagesArray = this._tabView.getPane().getChildren();
			var pageNum = pagesArray.length;
			var i = 0;
			while(i < pageNum) {
				this.deleteDataAt(0);
				//alert('tab' + i + ' of ' + pageNum +' deleted');
				i++;
			}
			this.dataObject = [];
			this._instances = [];
		},
		
		/**
		 * Function to delete a single subform from the formlist.
		 * 
		 * @param index The index number of the subform to be deleted
		 */
		deleteDataAt: function(index) {
			var pagesArray = this._tabView.getPane().getChildren();
			var targetPage = pagesArray[ index ];
			 
			var itemsList = this._tabView.getBar().getChildren();
            var lengthList = itemsList.length;
           
       
            itemsList[ index ].getManager().remove( itemsList[ index ] );
            this._tabView.getBar().remove(itemsList[ index ]);
    		
            this._tabView.getPane().remove(targetPage);
    
            targetPage.dispose();
            //itemsList[ index ].dispose();
            
            /*
            if(this.dataObject && this.dataObject[index]) {
            this.dataObject.splice(index, 1);
            }
            * */
		},
		
		/**
		 * Function to set the data of the formlist.
		 * 
		 * @param o The Object whose elements need to be set.
		 */
		setData: function(o) {
			this.deleteData();
			//alert( 'setdata' );
			for(var i = 0; i < o.length; i++) {
				this.addInstance(o[i]);
			}
			
			this.dataObject = o;		
		}
		
	},
	
	statics : {
	
	}
});