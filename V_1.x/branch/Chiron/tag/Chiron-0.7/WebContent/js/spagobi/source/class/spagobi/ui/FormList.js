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
	//	this._tabView.setBorder(new qx.ui.core.Border(10));
		this.addInstance();
	/*	this.atom = new qx.ui.basic.Atom();
		this.atom.add(this._tabView);
		this.atom.setLeft(0);
		this.atom.setBorder(new qx.ui.core.Border(3));
	  	this.add(this.atom);
	  	*/
	// 	this._tabView.setBorder(new qx.ui.core.Border(10));
	//	this.child = this._tabView.getChildren();
		
	//	alert(this.child.length);
		//this.child[2].setBorder(new qx.ui.core.Border(3));
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
		
		_tabButton: undefined,
		_tabcount: 0,
		_formCount:0,
		_checkObject: {
						_checkedFields : [],
						_subFormid: []
					  },
		_currentSubform: undefined,
		
		str:"",
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
		//	subform.setBorder(new qx.ui.core.Border(3));	
			if(o) {
				subform.setData(o);
			}		
			
			this._currentSubform = subform;
			var subFormButton = new qx.ui.pageview.tabview.Button('tab-' + this._tabcount);
			this._tabcount++;
			
			subFormButton.setChecked(true);
			subFormButton.setShowCloseButton(true);
			//this._tabButton = subFormButton;
			
	        this._tabView.getBar().add(subFormButton);
	        var subFormPage = new qx.ui.pageview.tabview.Page(subFormButton);
	  		this._tabView.getPane().add(subFormPage);
	  	//	alert (this._tabView.getPane().getChildren()[0]);
	  		this._tabView.getPane().setDimension('auto','auto');
	  		subFormPage.add(subform);
	  		this._tabView.getPane().setDimension('auto','auto');
	  //	subFormPage.setBorder(new qx.ui.core.Border(1));
	  		
	  		this._instances[this._instances.length] = subform;
	  		
	  		this._dummyFunction();
	  		
	  		subFormButton.addEventListener("changeChecked",this._changeSubForm,this);
	  		subFormButton.addEventListener("closetab",this._closeSubForm,this);
	  		
	  		if(this._instances.length > 2){	//this part of code is executed only when there are at least 2 subforms
		  		var itemsList1 = this._tabView.getBar().getChildren();
				var oldForm = itemsList1.indexOf(this._tabButton);
				var newForm = itemsList1.indexOf(subFormButton);
				this._currentSubform = this._instances[oldForm];
				var newSubForm = this._instances[newForm];
				//alert("Old form id is: " + oldForm);
				if(this._currentSubform.getInputField('mychecklist') != undefined){
					this._checkbox(this._currentSubform,newSubForm,oldForm,newForm);
				}
	  		}
			this._tabButton = subFormButton;
		},
		
		/**
		 * Protected member function.
		 * <p>This function is used to create the dummy tab, which cretaes a new subform tab when it is clicked.
		 * <p> Called by addInstance() function.
		 */
		_dummyFunction: function(){
			var dummysubFormButton = new qx.ui.pageview.tabview.Button('');
			dummysubFormButton.setShowCloseButton(true);
			dummysubFormButton.setCloseButtonImage("icon/16/actions/edit-add.png");
			
			this._tabView.getBar().add(dummysubFormButton);
	        var dummysubFormPage = new qx.ui.pageview.tabview.Page(dummysubFormButton);
	  		this._tabView.getPane().add(dummysubFormPage);
	  		
	  		dummysubFormButton.addEventListener("changeChecked",this._dummytab,this);
	  		
	  		dummysubFormPage.add();
	  		this._instances[this._instances.length] = {};
	  		
	  	},
		
		/**
		 * Event Listner function for the dummy tab button.
		 * <p> The function checks if the dummy tab is selected, and if so, it deletes the current dummy tab,
		 *     adds a new subform in the tab list and creates the new dummy tab after it.
		 */
		_dummytab: function(e){
			if(e.getTarget().isChecked() == true){
				this.deleteDataAt(this._instances.length-1);
				this._tabButton.setShowCloseButton(false);
				//this.addInstance();
				/*
				var itemsList1 = this._tabView.getBar().getChildren();
				var btnIndex1 = itemsList1.indexOf(this._tabButton);
				this._currentSubform = this._instances[btnIndex1];
				if(this._currentSubform.getInputField('mychecklist') != undefined){
					//this._checkbox(this._currentSubform);	//new form to be passed and not current
				}
				*/
				this.addInstance();
			}
		},
		
		_changeSubForm: function(e){ 
			var btn = e.getTarget(); 
			if(btn.isChecked() == true){
				this._tabButton.setShowCloseButton(false); 
				btn.setShowCloseButton(true);
				//this._tabButton = btn;
				
				var itemsList1 = this._tabView.getBar().getChildren();
				var oldForm = itemsList1.indexOf(this._tabButton);
				var newForm = itemsList1.indexOf(btn);
				this._currentSubform = this._instances[oldForm];
				var newSubForm = this._instances[newForm];
				
				if(this._currentSubform.getInputField('mychecklist') != undefined){
					this._checkbox(this._currentSubform,newSubForm,oldForm,newForm);	//new form to be passed and not current
				}
				this._tabButton = btn;
			}
		},
		
		_closeSubForm: function(e){ 
			if(e.getTarget().isChecked() == true){
				var btn1 = e.getData();
				//btn1.setShowCloseButton(false);
				var itemsList1 = this._tabView.getBar().getChildren();
				var lengthList1 = itemsList1.length;
				var btnIndex1 = itemsList1.indexOf(btn1);
				
				if( lengthList1 > 2 ) {			// if there are more than 1 tabs created (1 tab plus dummy tab should always be present)
    				
	            	if (btnIndex1 < lengthList1-2 ){		//if closing tab is not the last tab in list, select next tab on deletion of current tab
                		itemsList1[btnIndex1+1].setChecked(true);
                		this._tabButton = itemsList1[btnIndex1+1];
              		}
              		else {									//closing tab is last tab on list, so select previous tab on the deletion of current tab 
                		itemsList1[btnIndex1-1].setChecked(true);
                		this._tabButton = itemsList1[btnIndex1-1];
              		}
              		if(this._currentSubform.getInputField('mychecklist') != undefined){
					this._checklistAllow(btn1,btnIndex1);
      		   }
				this.deleteDataAt(btnIndex1);
              		
               }
			   else {
        			alert("Last Tab won't be removed!");
      		   }
      		   /*
      		   if(this._currentSubform.getInputField('mychecklist') != undefined){
					this._checklistAllow(btn1,btnIndex1);
      		   }
				this.deleteDataAt(btnIndex1);
				*/
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
            
            for(i=index; i<this._instances.length-1; i++){
					this._instances[i] = this._instances[i+1];
				}
			this._instances.length--;
			
			
				
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
		},
		
		_checkbox: function(subform,newSubForm,oldFormIndex,newFormIndex){
			//alert("inside fn. defn");
			var dummyWidget = subform.getInputField('mychecklist'); 
			if(dummyWidget.getUserData('type')== 'check'){
				//if(dummyWidget.exclusivecheckbox != undefined){
				var grid = dummyWidget.getUserData('field').getChildren();//has to be specific to a form
				var gridNew = newSubForm.getInputField('mychecklist').getUserData('field').getChildren();
				
				
				
				
				for(i=0; i<grid.length; i++){			//current form's checked labels stored in global array
					if(grid[i].getUserData('field').getChecked() == true){
						if(this._checkObject._checkedFields.length == 0){
							this._checkObject._checkedFields[this._checkObject._checkedFields.length] = grid[i].getUserData('label').getText();
							this._checkObject._subFormid[this._checkObject._subFormid.length] = oldFormIndex;
							//alert("Old form id is: " + oldFormIndex);
							//this.str = this.str + oldFormIndex+ ":"+grid[i].getUserData('label').getText() + ", ";
						}
						else{
							var flag = 0;
							for(j=0; j<this._checkObject._checkedFields.length; j++){
								if(this._checkObject._subFormid[j] == oldFormIndex  && this._checkObject._checkedFields[j]==grid[i].getUserData('label').getText()){
									flag = 1;
									break;
								}
							}
							if(flag == 0){		
									this._checkObject._checkedFields[this._checkObject._checkedFields.length] = grid[i].getUserData('label').getText();
									this._checkObject._subFormid[this._checkObject._subFormid.length] = oldFormIndex;
							//alert("Old form id is: " + oldFormIndex);
							//this.str = this.str + oldFormIndex + ":"+ grid[i].getUserData('label').getText() + ", ";
							}
						}
					}
					
					/* if checkbox unchecked in form, make it enable in rest subforms */
					for(j=0; j<this._checkObject._checkedFields.length; j++){
						if(this._checkObject._subFormid[j] == oldFormIndex){
							for(ii=0; ii<grid.length; ii++){
								if(this._checkObject._checkedFields[j] == grid[ii].getUserData('label').getText() && grid[ii].getUserData('field').getChecked() == false){ //if checkbox made unchecked
									
									/* make the newly made unchecked checkbox as available in rest of subforms*/
									for(cnt=0; cnt<this._instances.length-1; cnt++){
										var myForm = this._instances[cnt];
										var chklst = myForm.getInputField('mychecklist'); 
										var chkGrid = chklst.getUserData('field').getChildren();
										for(l=0; l<chkGrid.length; l++){
											if(chkGrid[l].getUserData('label').getText() == this._checkObject._checkedFields[j]){
												chkGrid[l].getUserData('field').setEnabled(true);
											}	
										}
									}
									
									/*delete from global */
									for(k=j; k<this._checkObject._checkedFields.length-1; k++){
										this._checkObject._checkedFields[k] = this._checkObject._checkedFields[k+1];
										this._checkObject._subFormid[k] = this._checkObject._subFormid[k+1]; 
									}
									this._checkObject._checkedFields.length--;
									this._checkObject._subFormid.length--;
							
									//break;
								}
							}
						}
						
					}
						
				}
				//alert(this.str);
				
				for(i=0; i<this._checkObject._checkedFields.length; i++){	//if there are already other tabs
					if(this._checkObject._subFormid[i] != newFormIndex){
						for(j=0; j<gridNew.length; j++){
							if(this._checkObject._checkedFields[i] == gridNew[j].getUserData('label').getText()){
								//alert('global: '+this._checkObject._checkedFields[i] + ",form: " + gridNew[j].getUserData('label').getText());
								gridNew[j].getUserData('field').setEnabled(false);
								break;
							}
						}
					}
				}	
				
			}//end of if
		},//end of fn.
		
		_checklistAllow:function(button,buttonIndex){
			var delForm = this._instances[buttonIndex];
			var checkList = delForm.getInputField('mychecklist'); 
			var grid = checkList.getUserData('field').getChildren();
			var checkedLabel = [];
			
		//	var str = "";
			for(i=0; i<grid.length; i++){			
				if(grid[i].getUserData('field').getChecked() == true){
					checkedLabel[checkedLabel.length] = grid[i].getUserData('label').getText();
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
							if(chkGrid[l].getUserData('label').getText() == checkedLabel[j]){
								chkGrid[l].getUserData('field').setEnabled(true);
								break;
							}
						}
					}
			 	}		//end of if 	
			}
			
			
		}
	}	
});