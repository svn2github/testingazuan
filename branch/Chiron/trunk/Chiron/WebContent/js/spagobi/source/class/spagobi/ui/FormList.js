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
		
		//this._tabView = new qx.legacy.ui.pageview.tabview.TabView();//change
		this._tabView = new qx.ui.tabview.TabView();
		
		//this._tabView.setWidth(500);//try later
		
			//	this._tabView.setBorder(new qx.legacy.ui.core.Border(10));
		this.addInstance();
		
		//Event listener should be after addInstance(), else it gets triggered on addition of dummy tab
		//this._tabView.addListener("changeSelected",this._choosePage,this); //HERE commented
																		
				/*	this.atom = new qx.legacy.ui.basic.Atom();
					this.atom.add(this._tabView);
					this.atom.setLeft(0);
					this.atom.setBorder(new qx.legacy.ui.core.Border(3));
				  	this.add(this.atom);
				  	*/
				// 	this._tabView.setBorder(new qx.legacy.ui.core.Border(10));
				//	this.child = this._tabView.getChildren();
					
				//	alert(this.child.length);
					//this.child[2].setBorder(new qx.legacy.ui.core.Border(3));
				//	alert (this.getWidth());
				//	alert (this.getHeight());
				//	this._tabView.setDimension(500,'auto');
				//	this._tabView.setDimension(500,500);		
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
		//	alert("debug 3"+typeof(o));
			var subform;
			if( typeof(this._config) == 'object' ) {
			//	alert("debug 6"+ o);
				subform = new spagobi.ui.Form(this._config);
				if (this._config.schedule != undefined){
			//		alert("Gaurav");
					this._val = this._config.val;
				}
			} /*else if ( typeof(o) == 'string' ){
				alert("debug 5"+ o);
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
		//	alert("debug 4"+this.treeLabel);
			//change
			/*
			var subFormButton = new qx.legacy.ui.pageview.tabview.Button('tab-' + this._tabcount);
			this._tabcount++;
			
			subFormButton.setChecked(true);
			subFormButton.setShowCloseButton(true);
				//this._tabButton = subFormButton;
			
	        this._tabView.getBar().add(subFormButton);
	        var subFormPage = new qx.legacy.ui.pageview.tabview.Page(subFormButton);
	  		this._tabView.getPane().add(subFormPage);
	  			//	alert (this._tabView.getPane().getChildren()[0]);
	  		this._tabView.getPane().setDimension('auto','auto');
	  		subFormPage.add(subform);
	  		this._tabView.getPane().setDimension('auto','auto');
	  			//	subFormPage.setBorder(new qx.legacy.ui.core.Border(1));
	  		*/
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
	  		//alert("Form label"+ this._tabView.getSelected().getLabel());
	  		//	subFormButton.setShowCloseButton(true);//change ... find for the close button
	  		
	  		//this._tabView.add(subFormPage);
	  		
	  		this._instances[this._instances.length] = subform;
	  		//alert("Form length: "+this._instances.length);
	  		
	  		//this._tabPage = subFormPage;
	  		//this._dummyFunction();
	  			
	  			//subFormPage.addListener("appear",this._changeSubForm,this);// focus activate
	  			//subFormButton.addListener("changeChecked",this._changeSubForm,this);//change
	  			//this._tabView.addListener("changeSelected",this._choosePage,this);
	  			//subFormButton.addListener("closetab",this._closeSubForm,this);//change .. find uncomment 1
	  		
	  		//uncomment 4 ..maybe not needed now
	  		/*
	  		if(this._instances.length > 2){	//this part of code is executed only when there are at least 2 subforms
		  		//change
		  		//var itemsList1 = this._tabView.getBar().getChildren();
		  		//var oldForm = itemsList1.indexOf(this._tabButton);
				//var newForm = itemsList1.indexOf(subFormButton);
				
				var itemsList1 = this._tabView.getChildren();
		  		var oldForm = itemsList1.indexOf(this._tabPage);
		  		var newForm = itemsList1.indexOf(subFormPage);
		  		
				this._currentSubform = this._instances[oldForm];
				var newSubForm = this._instances[newForm];
				//alert("Old form id is: " + oldForm);
				if(this._currentSubform.getInputField('mychecklist') != undefined){
					this._checkbox(this._currentSubform,newSubForm,oldForm,newForm);
				}
	  		}
	  		*/
			//this._tabButton = subFormButton;//change
			//this._tabPagePrev = this._tabPage;
			
			
			//subFormPage.setVisibility("visible");
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
			//change
			/*var dummysubFormButton = new qx.legacy.ui.pageview.tabview.Button('');
			dummysubFormButton.setShowCloseButton(true);
			dummysubFormButton.setCloseButtonImage("icon/16/actions/edit-add.png");
			
			this._tabView.getBar().add(dummysubFormButton);
	        var dummysubFormPage = new qx.legacy.ui.pageview.tabview.Page(dummysubFormButton);
	  		this._tabView.getPane().add(dummysubFormPage);
	  		dummysubFormButton.addListener("changeChecked",this._dummytab,this);
	  		*/
	  		//var dummysubFormPage = new qx.ui.tabview.Page("Dummy"+this._dummycount,"qx/icon/Oxygen/16/actions/list-add.png");
      		//this._dummycount++;
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
			//this.childrenList("1");
			//alert(e + ", " + e.getEventPhase());
			//e.stopPropagation();//doesnt work here
			//alert(e.getPropagationStopped());
			//e.setBubbles(true);
			/*
			if(e.BUBBLING_PHASE == e.getEventPhase())	//AT_TARGET i.e. 2
			 	e.stopPropagation();//very important
			*/
			var tabView = e.getTarget();
			var pg = tabView.getSelected();
			
			//alert(pg.getLabel());
			
			if(pg.getLabel()== ""){				//if(pg.getLabel().substring(0,5) == "Dummy"){
				//this._tabView.setSelected(this._tabPage);
				this._dummytab();//deleting dummy tab calls this event .. prevented by removing lstener
				//this.childrenList("should stop now");
				//this._tabView.addListener("changeSelected",this._choosePage,this);//HERE
				
				//e.stopPropagation();//very important
				
				//
				
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
				//this._tabButton.setShowCloseButton(false);//change..find
						//this.addInstance();
						/*
						var itemsList1 = this._tabView.getBar().getChildren();
						var btnIndex1 = itemsList1.indexOf(this._tabButton);
						this._currentSubform = this._instances[btnIndex1];
						if(this._currentSubform.getInputField('mychecklist') != undefined){
							//this._checkbox(this._currentSubform);	//new form to be passed and not current
						}
						*/
				this.addInstance();//uncomment
				//alert("dummy deleted and new added");
			//}
		},
		
		 
		_changeSubForm: function(){
			//alert(this._tabPagePrev.getLabel() +", "+this._tabPage.getLabel());
			//var pg = e.getTarget();
			//if(btn.isChecked() == true){//change..find equivalent
				//this._tabButton.setShowCloseButton(false);//change...find
				//btn.setShowCloseButton(true);//change..find
					//this._tabButton = btn;
				
				//change
				/*var itemsList1 = this._tabView.getBar().getChildren();
				var oldForm = itemsList1.indexOf(this._tabButton);
				var newForm = itemsList1.indexOf(btn);
				*/
				
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
		//uncomment 2
		/*
		_closeSubForm: function(e){ 
			if(e.getTarget().isChecked() == true){
				var btn1 = e.getData();//change ..maybe as below
				var page1 = e.getData();
				
				//var itemsList1 = this._tabView.getBar().getChildren();//change
				var itemsList1 = this._tabView.getChildren();
				
				var lengthList1 = itemsList1.length;
				//var btnIndex1 = itemsList1.indexOf(btn1);//change
				var btnIndex1 = itemsList1.indexOf(page1);
				
				if( lengthList1 > 2 ) {			// if there are more than 1 tabs created (1 tab plus dummy tab should always be present)
    				
	            	if (btnIndex1 < lengthList1-2 ){		//if closing tab is not the last tab in list, select next tab on deletion of current tab
                		itemsList1[btnIndex1+1].setChecked(true);
                		//this._tabButton = itemsList1[btnIndex1+1];
                		this._tabPage = itemsList1[btnIndex1+1];
              		}
              		else {									//closing tab is last tab on list, so select previous tab on the deletion of current tab 
                		itemsList1[btnIndex1-1].setChecked(true);
                		//this._tabButton = itemsList1[btnIndex1-1];
                		this._tabPage = itemsList1[btnIndex1-1];
              		}
              		if(this._currentSubform.getInputField('mychecklist') != undefined){
					this._checklistAllow(btn1,btnIndex1);
      		   }
				this.deleteDataAt(btnIndex1);
              		
               }
			   else {
        			alert("Last Tab won't be removed!");
      		   }
      		   / *
      		   if(this._currentSubform.getInputField('mychecklist') != undefined){
					this._checklistAllow(btn1,btnIndex1);
      		   }
				this.deleteDataAt(btnIndex1);
				* /
			}
		},
		*/
		
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
			
			//change
			/* 
			var itemsList = this._tabView.getBar().getChildren();
            var lengthList = itemsList.length;
           	itemsList[ index ].getManager().remove( itemsList[ index ] );
            this._tabView.getBar().remove(itemsList[ index ]);
    		this._tabView.getPane().remove(targetPage);
    		*/
    		//this.childrenList("before remove");
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
			//alert(this._check);
			
				
            /*
            if(this.dataObject && this.dataObject[index]) {
            this.dataObject.splice(index, 1);
            }
            * */
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
		
		
		//uncomment 3
		/*
		_checklistAllow:function(button,buttonIndex){
			var delForm = this._instances[buttonIndex];
			var checkList = delForm.getInputField('mychecklist'); 
			var grid = checkList.getUserData('field').getChildren();
			var checkedLabel = [];
			
		//	var str = "";
			for(i=0; i<grid.length; i++){			
				if(grid[i].getUserData('field').getChecked() == true){
					checkedLabel[checkedLabel.length] = grid[i].getUserData('label').getContent();
					}
			}
			
			for(j=0; j<checkedLabel.length; j++){
				for(i=0; i<this._checkObject._checkedFields.length; i++){
					if(this._checkObject._subFormid[i] == buttonIndex){
						if(this._checkObject._checkedFields[i] == checkedLabel[j]){
							for(k=i; k<this._checkObject._checkedFields.length-1; k++){
								this._checkObject._checkedFields[k] = this._checkObject._checkedFields[k+1];
								this._checkObject._subFormid[k] = this._checkObject._subFormid[k+1]; 
								}
							this._checkObject._checkedFields.length--;
							this._checkObject._subFormid.length--;	
						}
					}
				}
			}
				
			for(cnt=0; cnt<this._instances.length-1; cnt++){
				var myForm = this._instances[cnt];
				if(myForm!=delForm){
					var chklst = myForm.getInputField('mychecklist'); 
					var chkGrid = chklst.getUserData('field').getChildren();
					for(j=0; j<checkedLabel.length; j++){
						for(l=0; l<chkGrid.length; l++){
							if(chkGrid[l].getUserData('label').getContent() == checkedLabel[j]){
								chkGrid[l].getUserData('field').setEnabled(true);
								break;
							}
						}
					}
			 	}		//end of if 	
			}//end for
			
			
		}//end fn.
		*/ 
	}	
});