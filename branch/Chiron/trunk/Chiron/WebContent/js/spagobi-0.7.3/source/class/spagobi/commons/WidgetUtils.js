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
 * Class for the creation of a form elements like Label, textfield, checkbox, combobox
 */ 


qx.Class.define("spagobi.commons.WidgetUtils", {
  type : "static",
  statics : {
  	   createLabel : function( config ) {
        	var defultConfig = {
        		text : '',
        		top : 0,
        		left : 0,
        		width: 100     		
        	};
        	/*alert("label: "+config.width);*/
        	var test_label = new qx.ui.basic.Label();
        	test_label.set(defultConfig);
        	test_label.set(config);
        	 
        	return test_label;
        },
        
        
        createTextField : function( config,password ) {
        	var defultConfig = {
        		top: 0,
        		left: 0,
        		maxLength:100,        		
        		width: 0,
        		height: 0,
        		value: ''
        	};
        
        	if(password != true){
            	var test_textfield = new qx.ui.form.TextField();
        	}
        	else{
        		var test_textfield = new qx.ui.form.PasswordField();
        	}
            test_textfield.set( defultConfig );
            test_textfield.set( config );
            return test_textfield;
          
        },
        
        createComboBox : function( config ) {
        	var defultConfig = {
        		top: 0,
        		left: 0,
        		items: [],
        		listeners: []  		
           	};
          
          	config = spagobi.commons.CoreUtils.apply(defultConfig, config);
         
          	var combo_box = new qx.ui.form.ComboBox();
          	combo_box.set({ 
          		top: config.top, 
          		left: config.left 
          	});
          	
          	for(var i=0; i< config.items.length; i++) {
              var item = new qx.ui.form.ListItem(config.items[i]);
              combo_box.add(item);
            }
            
            for(var i=0; i< config.listeners.length; i++) {
            	if(config.listeners[i].scope) {
            		combo_box.addEventListener(config.listeners[i].event, config.listeners[i].handler, config.listeners[i].scope); 
            	} else {
            		combo_box.addEventListener(config.listeners[i].event, config.listeners[i].handler); 
            	}
            }
            
            combo_box.setSelected(combo_box.getList().getFirstChild());
            return combo_box;
          
        },
        
        createFlagBox : function( config ) {
          	var defultConfig = {
        		checked: false,
        		top: 0,
        		left: 0,
        		listeners: []
           	};
           	
           	config = spagobi.commons.CoreUtils.apply(defultConfig, config);
          
       		var check_box = new qx.ui.form.CheckBox();
       		check_box.set({
       			checked: config.checked,
       			top: config.top,
       			left: config.left
        	});
        	
        	for(var i=0; i< config.listeners.length; i++) {
            	if(config.listeners[i].scope) {
            		check_box.addEventListener(config.listeners[i].event, config.listeners[i].handler, config.listeners[i].scope); 
            	} else {
            		check_box.addEventListener(config.listeners[i].event, config.listeners[i].handler); 
            	}
            }
        	
            return check_box;
        },  	
        
        createTextArea: function( config ) {
        	var defultConfig = {
        		top: 0,
        		left: 0,
        		//rows: 10,        		
        		width: 0,
        		height: 0   		
        	};
        	
        	config = spagobi.commons.CoreUtils.apply(defultConfig, config);
            var test_textarea = new qx.ui.form.TextArea();
            //test_textarea.set( defultConfig );
            test_textarea.set( config );
            return test_textarea;
          
        },
        
        createCheckBox: function( config ){		//To do ...handle long text labels
        	var defultConfig = {
        		checked: false,
	        	top: 0,
	        	left: 0,
	        	items: [],
	        	listeners: [],
	        	columns: 1
        	};
        	config = spagobi.commons.CoreUtils.apply(defultConfig, config);
        	
        	var checkbox_grid = new qx.ui.layout.GridLayout;
		    checkbox_grid.auto();
        	checkbox_grid.set({ left: config.left });
        	
        	checkbox_grid.setColumnCount(config.columns);	
    		checkbox_grid.setRowCount(Math.ceil(config.items.length/config.columns));
    		
    		//alert('hi');		// why is it coming 2 times ??
    		
    		var rows = checkbox_grid.getRowCount();
    		var cols = checkbox_grid.getColumnCount();
    		
    		for(i=0,k=0;i<rows ; i++){
    			checkbox_grid.setRowHeight(i, 30);			
    			for(j=0; j<cols && k<config.items.length; j++,k++){
    				checkbox_grid.setColumnWidth(j, 50);	
    				
    				var label_text = new qx.ui.basic.Label(config.items[k]);
		    		var check_box = new qx.ui.form.CheckBox();
       				
       				var atom = new qx.ui.basic.Atom();
       				atom.add(check_box, label_text);
       				atom.setUserData('label', label_text);
        			atom.setUserData('field', check_box);
       				checkbox_grid.add(atom, j, i);
       				
    			}
    		}
    		
        	return checkbox_grid;
        },
        
        createRadioBox: function( config ) {
        	var defultConfig = {
        		top: 0,
        		left: 0,
        		items: [],
        		listeners: []  		
           	};
          
          	config = spagobi.commons.CoreUtils.apply(defultConfig, config);
          	
          	var atom = new qx.ui.basic.Atom();
          	var radioButtons = [];
          	var radioManager = new qx.ui.selection.RadioManager("mygroup");
          	
          	for(i=0; i<config.items.length; i++){
          		
          		radioButtons[i] = new qx.ui.form.RadioButton(config.items[i]);
          		radioButtons[i].set({ top: config.top, left: config.left});
          		
          		atom.add(radioButtons[i]);		// to return the group of radio buttons as returning a radio manager gives error
          		radioManager.add(radioButtons[i]);	//to make only 1 radio button be selected at any time
          		
          	}
          	
          	for(var i=0; i< config.listeners.length; i++) {
            	if(config.listeners[i].scope) {
            		radioManager.addEventListener(config.listeners[i].event, config.listeners[i].handler, config.listeners[i].scope); 
            	} else {
            		radioManager.addEventListener(config.listeners[i].event, config.listeners[i].handler); 
            	}
            }
            radioButtons[0].setChecked(true);	//by default, the 1st radio button is selected
            
          	return atom;
		},
        
        createInputTextField: function( config ) {
        	var defultConfig = {
        		top: 0,
        		left: 10,
        		text: '',
        		maxLength:100,        		
        		width: 200,
        		height: 20,
        		labelwidth: 100,
        		value: '',
        		mandatory: false,
        		password: false
        	};
        	/*alert(config.labelwidth);*/
        	
        	/*
        	var flag = 0;
        	
        	if(config.labelwidth != defultConfig.labelwidth){
        		var flag = 1;
        		
        	}
        	*/
        	
        	config = spagobi.commons.CoreUtils.apply(defultConfig, config);
        	
        
        	var labelField = this.createLabel({
        		text : config.text,
        		top : config.top,
        		left : config.left,
        		width : config.labelwidth   
        	});
        	
        	/*
        	if(flag == 1){
        		config.left = config.left - (config.labelwidth - defultConfig.labelwidth) + 10;
        	}
        	*/
        	
        	var textField = this.createTextField({
	        		top: config.top,
	        		left: config.left + 30,
	        		maxLength: config.maxLength,        		
	        		width: config.width,
	        		height: config.height,
	        		value: config.value
	           	},config.password);
        	
        	var atom = new qx.ui.basic.Atom();
        	atom.add(labelField, textField);
        	
        	if(config.mandatory) {
        		var mandatoryMarker = this.createLabel({
	        		text : '*',
	        		top : config.top,
	        		left : config.left + 35 
        		});
        		atom.add(mandatoryMarker);
        	}
        	
        	atom.setUserData('label', labelField);
        	atom.setUserData('field', textField);
        	
        	if(config.visible != undefined){
				atom.setDisplay(config.visible);
        	}
        	
        	return atom;
        },
        
        createInputComboBox: function( config ) {
        	var defultConfig = {
        		top: 0,
        		left: 10,
        		text: '',
        		items: [],
        		listeners: [],
        		labelwidth: 100
        	};
        	
        	config = spagobi.commons.CoreUtils.apply(defultConfig, config);
        	
        	var labelField = this.createLabel({
        		text : config.text,
        		top : config.top,
        		left : config.left,
        		width : config.labelwidth    
        	});
        	        	   
	        var comboBox = this.createComboBox({
	        	top: config.top,
	        	left: config.left + 30,
	        	items: config.items,
	        	listeners: config.listeners
	        });
        	
        	var atom = new qx.ui.basic.Atom();
        	atom.add(labelField, comboBox);
        	atom.setUserData('label', labelField);
        	atom.setUserData('field', comboBox);
        	
        	if(config.visible != undefined){
				atom.setDisplay(config.visible);
        	}
        	
        	return atom;
        },
        
        createInputFlagBox: function( config ) {
        	var defultConfig = {
        		top: 0,
        		left: 10,
        		text: '',
        		checked: false,
        		listeners: [],
        		labelwidth: 100
        	};
        	
        	config = spagobi.commons.CoreUtils.apply(defultConfig, config);
        	
        	var labelField = this.createLabel({
        		text : config.text,
        		top : config.top,
        		left : config.left,
        		width : config.labelwidth   
	        });
	        
	        var checkBox = this.createFlagBox({
	        	checked: config.checked,
	        	top: config.top,
	        	left: config.left + 30,
	        	listeners: config.listeners
	        });
        	
        	var atom = new qx.ui.basic.Atom();
        	atom.add(labelField, checkBox);
        	atom.setUserData('label', labelField);
        	atom.setUserData('field', checkBox);
        	
        	if(config.visible != undefined){
				atom.setDisplay(config.visible);
        	}
        	
        	return atom;
        },
        
        createInputForm: function( config ) {
			
			var subform;
			
			if(typeof(config.form) ==  'object') {
				subform = new spagobi.ui.Form( config.form );
			} else {
				//alert( typeof(config.form) );
				subform = new config.form();
			}
			/*
			var atom = new qx.ui.basic.Atom();
			atom.add(subform);
			atom.setUserData('field', subform);
			atom.setBorder( new qx.ui.core.Border(2,'solid') );
			*/
			subform.setUserData('field', subform);
			
			if(config.visible != undefined){
				subform.setDisplay(config.visible);
			}	
			return subform;
		},
		
		createInputFormList: function( config ) {
			
			var subform;
			
			subform = new spagobi.ui.FormList( config.formList );
			
			subform.setUserData('field', subform);
			
			return subform;
		},  	
        
        createInputTextArea: function( config ) {
        	var defultConfig = {
        		top: 0,
        		left: 10,
        		text: '',
        		//rows: 10,        		
        		width: 200,
        		height: 50,
        		mandatory: false,
        		labelwidth: 100		
        	};
        	
        	config = spagobi.commons.CoreUtils.apply(defultConfig, config);
        	
        	var labelField = this.createLabel({
        		text : config.text,
        		top : config.top,
        		left : config.left,
        		width : config.labelwidth
        	});
        	
        
        	var textArea = this.createTextArea({
        		top: config.top,
        		left: config.left + 30,
        		//rows: config.rows,        		
        		width: config.width,
        		height: config.height   
        	});
        	
        	var atom = new qx.ui.basic.Atom();
        	atom.add(labelField, textArea);
        	
        	if(config.mandatory) {
        		var mandatoryMarker = this.createLabel({
	        		text : '*',
	        		top : config.top,
	        		left : config.left + 35 
        		});
        		atom.add(mandatoryMarker);
        	}
        	
        	atom.setUserData('label', labelField);
        	atom.setUserData('field', textArea);
        	
        	if(config.visible != undefined){
				atom.setDisplay(config.visible);
        	}
        	
        	return atom;
        },
        
        /**
         * Function to create a list of check boxes.
         * <p> The function accepts an object as a parameter with the following properties:
         * <p> type - Type of field
         * <p> dataIndex - To identify the field in the form by a name
         * <p> checked - Indicates if checkbox list will have elements as checked or not
         * <p> text - The label of the check box list
         * <p> columns - The number of columns in which the checkboxes are to be displayed
         * <p> items - Array containg the label of the checkboxes
         * 
         * <p> *Example :- *
 		 * <p><code>
         * {
         *     type: 'check',
         *     dataIndex: 'mychecklist',
         *     checked: false,
         *     text: 'Header',
         *     columns: 6,
         *     items: ["aaaa","bbbb","cccc","dddd","eeee","ffff","gggg","hhhh","iiii","jjjj"]
         * }
         * 
         * @param config - Object with properties as described above.
         */
        createInputCheckBox: function( config ) {
        	var defultConfig = {
        		top: 0,
        		left: 10,
        		text: '',
        		checked: false,
        		items: [],
        		listeners: [],
        		columns: 1,
        		labelwidth: 100
        	};
        	
        	config = spagobi.commons.CoreUtils.apply(defultConfig, config);
        	
        	var labelField = this.createLabel({
        		text : config.text,
        		top : config.top,
        		left : config.left,
        		width : config.labelwidth   
	        });
	        /*
	        var checkBox = this.createCheckBox({
	        	checked: config.checked,
	        	top: 0,		//config.top,
	        	left: config.left + 30,
	        	items: config.items,
	        	listeners: config.listeners,
	        	columns: config.columns
	        });
        	*/
        	var checkBox = new spagobi.ui.CheckBoxList({
	        	checked: config.checked,
	        	top: 0,	
	        	left: config.left + 30,
	        	items: config.items,
	        	listeners: config.listeners,
	        	columns: config.columns
	        });
        	
        	var atom = new qx.ui.basic.Atom();
        	atom.add(labelField );
        	atom.add(checkBox);
        	atom.setUserData('label', labelField);
        	atom.setUserData('field', checkBox);
        	
        	if(config.visible != undefined){
				atom.setDisplay(config.visible);
        	}
        	
        	return atom;
        },
        
        createInputRadio: function( config ) {
        	var defultConfig = {
        		top: 0,
        		left: 10,
        		text: '',
        		checked: false,
        		items: [],
        		listeners: [],
        		labelwidth: 100
        	};
        	
        	config = spagobi.commons.CoreUtils.apply(defultConfig, config);
        	
        	var labelField = this.createLabel({
        		text : config.text,
        		top : config.top,
        		left : config.left,
        		width : config.labelwidth   
	        });
	        
	       
	        var radioButton = this.createRadioBox({
	        	checked: config.checked,
	        	top: config.top,
	        	left: config.left + 30,
	        	items: config.items,
	        	listeners: config.listeners
	        });
        	
        	var atom = new qx.ui.basic.Atom();
        	atom.add(labelField, radioButton);
        	atom.setUserData('label', labelField);
        	atom.setUserData('field', radioButton);
        	
        	if(config.visible != undefined){
				atom.setDisplay(config.visible);
        	}
        	
        	return atom;
        }
         	
  }
});