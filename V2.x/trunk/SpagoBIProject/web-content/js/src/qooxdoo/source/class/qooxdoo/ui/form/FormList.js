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

/*
#asset(qx/icon/Oxygen/16/actions/edit-delete.png)
#asset(qx/icon/Oxygen/16/actions/list-add.png)
*/

qx.Class.define("qooxdoo.ui.form.FormList", {
	extend: qooxdoo.ui.form.Form,
	
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
 * var formList = new qooxdoo.ui.FormList(prop);
 * </code>
 * 
 * @param config The parameter is an object with the above properties.
 */	
	 construct : function(config) { 
		this.base(arguments);
		
		this._instances = [];
		this.setConfig(config);
		
		this._tabView = new qx.ui.tabview.TabView();
		
		this.addInstance();
		
	  	this.add(this._tabView);
	},
	 
 /**
  * Members of the class
  */
  
	members: {
		
		 child : undefined,
		 atom : undefined,	
		_config: undefined,
		
		_instances: undefined,
		
		_tabView: undefined,
		
		//_tabButton: undefined,//change
		_tabPage: undefined,//change .. added
		_tabPagePrev : undefined,//change .. added
		_tabcount: 0,
		_dummycount: 0,
		_check: false,
		_formCount:0,
		_checkObject: {						//common array for all formlists to share checkbox list
						_checkedFields : [],
						_subFormid: []
					  },
		_currentSubform: undefined,
		
		str:"",
		treeLabel:undefined,
		_val:undefined,
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
		//	alert(typeof(this._config));
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
			
				subform = new qooxdoo.ui.form.Form(this._config);
				if (this._config.schedule != undefined){
			
					this._val = this._config.val;
				}
			} /*else if ( typeof(o) == 'string' ){
				
				this.treeLabel = o;
			}
			*/
			else {
				subform = new this._config();
			}
					//	subform.setBorder(new qx.legacy.ui.core.Border(3));	
			if(o) {
				subform.setData(o);
			}		
			
			this._currentSubform = subform;
			this.treeLabel = o;
		
	  		if (this.treeLabel != undefined)
	  		{
	  			var subFormPage = new qx.ui.tabview.Page(this.treeLabel);//, "qx/icon/Oxygen/16/actions/edit-delete.png"
	  		}
	  		else if (this._config.schedule != undefined){
	  			var subFormPage = new qx.ui.tabview.Page(this._val);
	  		}
	  		
	  		else
	  		{
	  			var subFormPage = new qx.ui.tabview.Page('tab-' + this._tabcount, "qx/icon/Oxygen/16/actions/edit-delete.png");
	  		}
	  		subFormPage.setLayout(new qx.ui.layout.Grow());//VBox
	  		this._tabcount++;
	  		subFormPage.add(subform);
	  		this._tabView.add(subFormPage);
	  		
	  		
	  		this._instances[this._instances.length] = subform;
	  		
			this._check = false;
			this._tabView.addListener("changeSelected",this._choosePage,this);
    		//this._tabView.setSelected(this._tabPage);//HERE ..commented
    		this._tabView.setSelected(subFormPage); //event listener for tab called
    		//alert("event listener for " + (this._tabcount-1) + " done");
    		if(this._tabcount>1){
    			this._check = true;
    		}
    		//this.childrenList("after New add");
    		this._tabPage = subFormPage;//moved up
    		if (this.treeLabel != undefined || this._config.schedule != undefined)
    		{}
    		else {
    		this._dummyFunction();
    		}
    		//alert("Form label"+ this._tabView.getSelected().getLabel());
    		//this._tabView.addListener("changeSelected",this._choosePage,this);
		},
		
		/**
		 * Protected member function.
		 * <p>This function is used to create the dummy tab, which cretaes a new subform tab when it is clicked.
		 * <p> Called by addInstance() function.
		 */
		_dummyFunction: function(){
			
      		var dummysubFormPage = new qx.ui.tabview.Page("","qx/icon/Oxygen/16/actions/list-add.png");
      		
      		dummysubFormPage.setLayout(new qx.ui.layout.Grow());
	  		//dummysubFormPage.add(new qx.ui.basic.Label("dummy"));//change .. no need maybe
      		this._tabView.add(dummysubFormPage);
      		this._instances[this._instances.length] = {};
	  		//this.childrenList("after new dummy");
	  	},
		
		_choosePage: function(e){
			//alert(e.getTarget().getSelected().getLabel() +", " + this._check);
			//alert("inside event listener" + this._check);
			if(this._check){	//Event Lister gets called for the deleted dummy tab
				
				this._check = !this._check;
				this._tabView.removeListener("changeSelected",this._choosePage,this);
				this._tabView.setSelected(this._tabPage);//HERE ..commented
				this._tabView.addListener("changeSelected",this._choosePage,this);
				return;
			}
			
			var tabView = e.getTarget();
			var pg = tabView.getSelected();
			
			//alert(pg.getLabel());
			
			if(pg.getLabel()== ""){				//if(pg.getLabel().substring(0,5) == "Dummy"){
				//this._tabView.setSelected(this._tabPage);
				this._dummytab();//deleting dummy tab calls this event .. prevented by removing lstener
			}
			else{
				this._tabPagePrev = this._tabPage;
				this._tabPage = pg;
				this._changeSubForm();
			}
			
			//e.setBubbles(true);//alert(e.getBubbles());
			
			//e.setEventPhase(e.BUBBLING_PHASE);
			//e.stopPropagation();//works here
		},
		
		/**
		 * Event Listner function for the dummy tab button.
		 * <p> The function checks if the dummy tab is selected, and if so, it deletes the current dummy tab,
		 *     adds a new subform in the tab list and creates the new dummy tab after it.
		 */
		_dummytab: function(){
			//alert("2");
			
			//if(e.getTarget().isChecked() == true){//change..find equivalent
				this.deleteDataAt(this._instances.length-1);//commenting this works
				
				this.addInstance();//uncomment
				//alert("dummy deleted and new added");
			//}
		},
		
		 
		_changeSubForm: function(){
			
				
				var itemsList1 = this._tabView.getChildren();
				var oldForm = itemsList1.indexOf(this._tabPagePrev);
				var newForm = itemsList1.indexOf(this._tabPage);
				
				//alert(oldForm + "," +  newForm);//forms are same .. do diff.
				this._currentSubform = this._instances[oldForm];
				var newSubForm = this._instances[newForm];
				
				if(this._currentSubform.getInputField('mychecklist') != undefined){
					this._checkbox(this._currentSubform,newSubForm,oldForm,newForm);	//new form to be passed and not current
				}
					//this._tabButton = btn;//change
				//this._tabPage = pg;
				
			//}
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
			//var pagesArray = this._tabView.getPane().getChildren();//change
			var pagesArray = this._tabView.getChildren();
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
			//alert("3");
			
			//var pagesArray = this._tabView.getPane().getChildren();//change
			var pagesArray = this._tabView.getChildren();
			var targetPage = pagesArray[ index ];
			
			
    		this._tabView.removeListener("changeSelected",this._choosePage,this);
    		//eve.setBubbles(true);no effect
    		//eve.stopPropagation();
    		this._tabView.remove(targetPage);	//event listner calling postponed until addListner is added
    		this._check = true;
    		//this._tabView.addListener("changeSelected",this._choosePage,this);
    		//targetPage.dispose();
            	//itemsList[ index ].dispose();
            //this.childrenList("after remove");
            for(i=index; i<this._instances.length-1; i++){
					this._instances[i] = this._instances[i+1];
				}
			this._instances.length--;
		},
		
		//Function to display the list of tabs added in tabview array
		childrenList: function(s){
		//	alert(s);
			var list = "";
			var children = this._tabView.getChildren();
			for(i in children){
				list = list + children[i].getLabel();
			}
		//	alert(list);
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
		},
		
		printglobalArrayData: function(s){
			
			var str = s;		  
			for(a in this._checkObject._checkedFields){
					str = str + this._checkObject._subFormid[a] + " : "+ this._checkObject._checkedFields[a]+"\n";
			}
		//	alert(str);	  
		},
		
		
		_checkbox: function(subform,newSubForm,oldFormIndex,newFormIndex){
			var dummyWidget = subform.getInputField('mychecklist');
			
			if(dummyWidget.getUserData('type')== 'check'){
				//if(dummyWidget.exclusivecheckbox != undefined){
				
				//var grid = dummyWidget.getUserData('field').getChildren();//has to be specific to a form //change
				//var gridNew = newSubForm.getInputField('mychecklist').getUserData('field').getChildren();//change
				var grid 	= subform.getInputField('mychecklist').getUserData('field').getChildren();
				var gridNew = newSubForm.getInputField('mychecklist').getUserData('field').getChildren();
				
				for(i=0; i<grid.length; i++){
							
					if(grid[i].getUserData('field').getChecked() == true){	//current form's checked labels are stored in global array
						//this.printglobalArrayData('before');
						if(this._checkObject._checkedFields.length == 0){	//if global array is empty
							this._checkObject._checkedFields[this._checkObject._checkedFields.length] = grid[i].getUserData('label').getContent();
							this._checkObject._subFormid[this._checkObject._subFormid.length] = oldFormIndex;
							//alert("Old form id is: " + oldFormIndex);
							//this.str = this.str + oldFormIndex+ ":"+grid[i].getUserData('label').getContent() + ", ";
						}
						else{		//if global array contains data
							var flag = 0;
							for(j=0; j<this._checkObject._checkedFields.length; j++){
								if(this._checkObject._subFormid[j] == oldFormIndex  && this._checkObject._checkedFields[j]==grid[i].getUserData('label').getContent()){
									flag = 1;	//current form's data already in global array
									break;
								}
							}
							if(flag == 0){		
									this._checkObject._checkedFields[this._checkObject._checkedFields.length] = grid[i].getUserData('label').getContent();
									this._checkObject._subFormid[this._checkObject._subFormid.length] = oldFormIndex;
							//alert("Old form id is: " + oldFormIndex);
							//this.str = this.str + oldFormIndex + ":"+ grid[i].getUserData('label').getContent() + ", ";
							}
						}
					}
				}//added here	
					//this.printglobalArrayData("after");
					//if checkbox unchecked in form, make it enable in rest subforms
					for(j=0; j<this._checkObject._checkedFields.length; j++){
						if(this._checkObject._subFormid[j] == oldFormIndex){
							for(ii=0; ii<grid.length; ii++){
								if(this._checkObject._subFormid[j] == oldFormIndex && this._checkObject._checkedFields[j] == grid[ii].getUserData('label').getContent() && grid[ii].getUserData('field').getChecked() == false){ //if checkbox made unchecked
									
									// make the newly made unchecked checkbox as available in rest of subforms
									for(cnt=0; cnt<this._instances.length-1; cnt++){
										var myForm = this._instances[cnt];
										var chklst = myForm.getInputField('mychecklist'); 
										var chkGrid = chklst.getUserData('field').getChildren();//change
										//var chkGrid = chklst.getUserData('field').getChildren()[0];
										for(l=0; l<chkGrid.length; l++){
											if(chkGrid[l].getUserData('label').getContent() == this._checkObject._checkedFields[j]){
												chkGrid[l].getUserData('field').setEnabled(true);
											}	
										}
									}
									
									//delete from global
									for(k=j; k<this._checkObject._checkedFields.length-1; k++){
										this._checkObject._checkedFields[k] = this._checkObject._checkedFields[k+1];
										this._checkObject._subFormid[k] = this._checkObject._subFormid[k+1]; 
									}
									this._checkObject._checkedFields.length--;
									this._checkObject._subFormid.length--;
									this.printglobalArrayData("now");
									//break;
								}
							}
						}
						
					}
						
				//}
				//alert(this.str);
				
				for(i=0; i<this._checkObject._checkedFields.length; i++){	//if there are already other tabs, disable their chechboxes checked in current tab
					if(this._checkObject._subFormid[i] != newFormIndex){
						for(j=0; j<gridNew.length; j++){
							if(this._checkObject._checkedFields[i] == gridNew[j].getUserData('label').getContent()){
								//alert('global: '+this._checkObject._checkedFields[i] + ",form: " + gridNew[j].getUserData('label').getContent());
								gridNew[j].getUserData('field').setEnabled(false);
								break;
							}
						}
					}
				}	
				
			}//end of if
		}//,//end of fn.
		
		
		
	}	
});