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
 */
 
/**
 * Class to create a list of check boxes
 */ 
 
qx.Class.define("spagobi.ui.CheckBoxList", {
	extend: qx.ui.layout.GridLayout,
	
/**
 * Constructor to create a Checkbox List.
 * <p> It creates a list of checkboxes which belong to a single group element.
 * <p> Multiple checkboxes can be created and set to be checked.
 * <p> The parameter accepted by the constructor is an object with properties set for the 
 * checkbox list.
 * <p> Also see setAtom() and createCheckBoxList() methods.
 * 
 * <p> *Example :- *
 * <p><code>
 * var checkBoxList = new spagobi.ui.CheckBoxList({
 *									 	        	checked: config.checked,
 *									 	        	top: 0,	
 * 	        										left: config.left + 30,
 * 	        										items: config.items,
 * 	        										listeners: config.listeners,
 * 	        										columns: config.columns
 * 	        									});
 * </code>
 * 
 * @param config The parameter is an object with the above properties.
 */	
	 construct : function(config) { 
		this.base(arguments);
 		
 		this._setAtom();
 		
 		this.createCheckBoxList(config);
		
    	
     },
	 
 /**
  * Members of the class
  */
  
	members: {
		
		//_atom : [],
		atom : [],
		label_text: [],
		check_box: [],
		
		
		_setAtom: function(){
			this.atom = [];
			this.label_text = [];
			this.check_box = [];
		},
		
		/**
		 * Function to create the checkbox list element.
		 * <p> It is called by the constructor.
		 * <p> It accepts an object with the following properties:
		 * <p> checked: To indicate if the checkbox is checked or not (true/false)
		 * <p> items: Array of labels for the checkboxes
		 * <p> columns: The number of columns in which the checkbox need to be displayed.
		 * <p> *Example :- *
		 * <p><code>
		 * createCheckBoxList({
		 *						checked: false,
		 *						top: 0,	
		 * 	        			left: 30,
		 * 	        			items: ["aaaa","bbbb","cccc"],
		 * 	        			columns: 4
		 * 	        			});
		 */
		createCheckBoxList: function(config){
			var defultConfig = {
    		checked: false,
        	top: 0,
        	left: 0,
        	items: [],
        	listeners: [],
        	columns: 1
	    	};
	    	config = spagobi.commons.CoreUtils.apply(defultConfig, config);
	    	
	    	
		    this.auto();
	    	this.set({ left: config.left });
	    	
	    	this.setColumnCount(config.columns);	
			this.setRowCount(Math.ceil(config.items.length/config.columns));
			
			//alert('hi');		// why is it coming 2 times ??
			
			var rows = this.getRowCount();
			var cols = this.getColumnCount();
			
			var index = 0;
			
			for(i=0,k=0;i<rows ; i++){
				this.setRowHeight(i, 30);			
				for(j=0; j<cols && k<config.items.length; j++,k++){
					this.setColumnWidth(j, 50);	
					
					this.label_text[index] = new qx.ui.basic.Label(config.items[k]);
		    		this.check_box[index]  = new qx.ui.form.CheckBox();
	   				
	   				//var atom = new qx.ui.basic.Atom();
	   				this.atom[index] = new qx.ui.basic.Atom();
	   				this.atom[index].add(this.check_box[index], this.label_text[index]);
	   				this.atom[index].setUserData('label', this.label_text[index]);
	    			this.atom[index].setUserData('field', this.check_box[index]);
	   				this.add(this.atom[index], j, i);
	   				//this.setUserData('element'+index, this.atom[index]);
	   				index++;
				}
		 	}
		},
		
		/**
		 * Function to get the data of the checkbox list.
		 * <p> The function returns an array of strings corrosponding to the labels of the 
		 * checkboxes which are selected.
		 * @return Array of selected checkbox labels 
		 */
		getData: function() {
			//alert("Form's getData works");
			var temp = [];
			for(i=0,j=0; i<this.atom.length; i++){
				if(this.atom[i].getUserData('field').getChecked() == true ){
					temp[j] = this.atom[i].getUserData('label').getText();
					j++;
				}
			}
			return temp;
		},
		
		/**
		 * Function to set the data of the checkbox list.
		 * <p> The function sets those checkboxes to true whose labels are provided in the parameter.
		 * <p> Note that the existing selected checkboxes remain checked and are not reset.
		 * 
		 * @param value Array of strings containing the label of checkboxes which need to be set.
		 */
		setData: function(value) {
			var flag = false;
			for(i=0; i<value.length; i++){
				for(j=0; j<this.atom.length; j++){
					if( value[i] == this.atom[j].getUserData('label').getText() ){
						this.atom[j].getUserData('field').setChecked(true);
						flag = true;
						break;
					}
				}
				if(flag == false){
					alert('Checkbox Label '+ value[i] + ' is not valid');
				}
				else
					flag = false;
				
			}
		}
	}	
	
});