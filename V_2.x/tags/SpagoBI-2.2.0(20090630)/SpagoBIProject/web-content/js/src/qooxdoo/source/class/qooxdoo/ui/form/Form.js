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
 */
 
/**
 * Class for the creation of a form
 */ 
qx.Class.define("qooxdoo.ui.form.Form", {

	extend : qx.ui.container.Composite,
	
	/**
	 * Constructor to create a form. <hr>
	 * <p> *Config Options*
	 * <p> *type*: String 
	 * 		<p>&nbsp;&nbsp;&nbsp;&nbsp; Type of form element  
	 * 	__('text' / 'combo' / 'check' / 'form' / 'formList')__,
	 * <p> *dataIndex*: String 
	 * 		<p>&nbsp;&nbsp;&nbsp;&nbsp; Identify form element in associative array
	 * __('abc')__,
	 * <p> *text*: String 
	 * 		<p>&nbsp;&nbsp;&nbsp;&nbsp; Name of the label seen on form
	 * <p> *mandatory*: Boolean 
	 * 		<p>&nbsp;&nbsp;&nbsp;&nbsp; Indicate if text field is mandatory
	 * __(true/false)__,
	 * <p> *items*: Array 
	 * 		<p>&nbsp;&nbsp;&nbsp;&nbsp; Array of strings for each item of Combo box
	 * <p> *listeners*: Array 
	 * 		<p>&nbsp;&nbsp;&nbsp;&nbsp; Array of objects for event and its handler
	 * <p> *checked*: Boolean 
	 * 		<p>&nbsp;&nbsp;&nbsp;&nbsp; Indicate if Checkbox is checked
	 * <p> *formList*: Object/Array 
	 * 		<p>&nbsp;&nbsp;&nbsp;&nbsp; Indicates type of subform 
	 * 		(Can be another form or an array of form elemets)
	 *  
	 * <p>*Example*
	 * <p><code> var f = new qooxdoo.ui.Form([
	 * <br>&nbsp;&nbsp;&nbsp;&nbsp;
	 * 			{
	 *       		type: 'text',
	 *       		dataIndex: 'name',
	 *       		text: 'Name',
	 *       		mandatory: true	
	 *   		},
     * <br>&nbsp;&nbsp;&nbsp;&nbsp;	
     * 			{
	 *      		type: 'combo',
	 *       		dataIndex: 'myEngine',
	 *       		text: 'Engine type',
	 * 				items: ["abc","xyz"],
	 * <br>&nbsp;&nbsp;&nbsp;&nbsp;     		
	 * 				listeners: [
	 *			        		{
	 *			        			event: 'changeValue',
	 *			        			handler: this.myFunctionName,
	 *			        			scope: this
	 *			        		}        		
	 *       				   ]
     *   		}, 	
     *<br>&nbsp;&nbsp;&nbsp;&nbsp;
     * 			{
	 *       		type: 'check',
	 *       		dataIndex: 'myCheck',
	 *       		text: 'Use This a CheckBox',
	 *       		checked: false	
     *   		}
	 * 			]);
	 * </code></p>		
	 * @param config {qx.legacy.ui.Object} Array of objects where each object  
	 * represents a Form element with properties described above.
	 * 
	 */
	construct : function(config) { 
		this.base(arguments);
		this.setLayout(new qx.ui.layout.VBox(5));
				
		this.dataObject = {};
  		this.dataMappings = [];
  		
  		
  		if(config) {
	  		for(var i = 0; i < config.length; i++) {
	  			this.addInputField( config[i] );
	  		}
  		}
	},
	
	members: {
		
		dataObject: null,		
		dataMappings: null,
		
		/**
		 * Function to get the data of the form fields
		 */
		getData: function() {
			for(prop in this.dataMappings) {
					
				
					this.dataObject[prop] = this.getInputFieldValue(prop);
				
			}
			return this.dataObject;
		},
	
		/**
		 * Function to set data of form fields based on the property values
		 * of form object
		 */
		setData: function(o) {
			for(prop in o) {
				this.setInputFieldValue(prop, o[prop]);
			}	
			this.dataObject = o;		
		},
		
		/**
		 * Function to get the input field index of form
		 * @param dataIndex The index used to refer to a form field
		 */
		getInputField: function(dataIndex) {
			return this.dataMappings[dataIndex];
		},
		
		/**
		 * Function to get the input field value of a form
		 * @param dataIndex The index used to refer to the form field
		 */
		getInputFieldValue: function(dataIndex) {
			var value;
			
			if(!this.getInputField(dataIndex)) return null;
			
			
					
				
			if(this.getInputField(dataIndex).isInputField) {
				
				return this.getInputField(dataIndex).getData();
			}
			
			var container = this.getInputField(dataIndex).getUserData('field');
			var object = container.getChildren()[0];
			
			if(this.getInputField(dataIndex).getUserData('type') === 'text') {
				alert('flag');	
				value = object.getValue();				
			} else if(this.getInputField(dataIndex).getUserData('type') === 'combo') {
				alert('combo');	
				value = object.getValue();				
			} else if(this.getInputField(dataIndex).getUserData('type') === 'flag') {
				alert('flag');	
				value = object.isChecked();				
			} else if(this.getInputField(dataIndex).getUserData('type') === 'form') {	
				
				value = object.getData();				
			} else if(this.getInputField(dataIndex).getUserData('type') === 'formList') {	
				value = object.getData();				
			} else if(this.getInputField(dataIndex).getUserData('type') === 'textarea') {	
				alert('textarea');	
				value = object.getValue();				
			} else if(this.getInputField(dataIndex).getUserData('type') === 'check') {	
				alert('check');	
				value = object.getData();				
			} else if(this.getInputField(dataIndex).getUserData('type') === 'propertiesList') {	
				alert('propertiesList');	
				value = object.getData();				
			} else if(this.getInputField(dataIndex).getUserData('type') === 'radio') {
				alert('radio');	
				var radioButton = container.getChildren();
				for(i=0; i<radioButton.length; i++){
					if(radioButton[i].getChecked() == true){
						value = radioButton[i].getLabel();
					}
				}
			} else if(this.getInputField(dataIndex).getUserData('type') === 'groupbox') {
				var form = object.getChildren()[0];	//set to form added to the groupbox
				if(form){							//Not all groupboxes have form inside - some are empty
					value = form.getData();
				}
			}	
			
			return value;			
		},
		
		/**
		 * Function to set the input fields of the form
		 * @param dataIndex The index used to refer to a form field
		 * @param value The value of the input field of form
		 */
		setInputFieldValue: function(dataIndex, value) {
			
			if(!this.getInputField(dataIndex)) {
				return;
			}
			
			if(this.getInputField(dataIndex).isInputField) {
				this.getInputField(dataIndex).setData(value);
				return;
			}
			
			
			var container = this.getInputField(dataIndex).getUserData('field');
			var object = container.getChildren()[0];
			
			if(this.getInputField(dataIndex).getUserData('type') === 'text') {
				object.setValue(value);				
			} else if(this.getInputField(dataIndex).getUserData('type') === 'combo') {
				object.setValue('' + value);				
			} else if(this.getInputField(dataIndex).getUserData('type') === 'flag') {
				object.setChecked(value);				
			} else if(this.getInputField(dataIndex).getUserData('type') === 'form') {		
				object.setData(value);				
			} else if(this.getInputField(dataIndex).getUserData('type') === 'formList') {		
				object.setData(value);				
			} else if(this.getInputField(dataIndex).getUserData('type') === 'textarea') {		
				object.setValue(value);				
			}  else if(this.getInputField(dataIndex).getUserData('type') === 'propertiesList') {		
				object.setData(value);				
			}else if(this.getInputField(dataIndex).getUserData('type') === 'check') {
				//object = container.getChildren()[1];
				//alert("test");
				//alert(qooxdoo.commons.CoreUtils.arrayToStr(value));
				
				container.setData(value);
				
				//object.setData(value);				
			} else if(this.getInputField(dataIndex).getUserData('type') === 'radio') {
				var radioButton = container.getChildren();
				for(i=0; i<radioButton.length; i++){
					if(radioButton[i].getLabel() == value){
						radioButton[i].setChecked(true);
						break;
					}
				}
			} else if(this.getInputField(dataIndex).getUserData('type') === 'groupbox') {
				var form = object.getChildren()[0];
				if(form){
					form.setData(value);
				}	
			}
			
		},
		
		/**
		 * Function to add the form fields to the page
		 * <p>It is called by the form constructor.
		 * @param config - Config object as described in the constructor.
		 */
		addInputField: function(config) {
			if(config.type === 'text') {
  				//inputField = qooxdoo.commons.WidgetUtils.createInputTextField(config); 
  				//inputField.setUserData('type', 'text');
				var componentRegistry = qooxdoo.commons.ComponentRegistry.getInstance();				
				inputField = componentRegistry.createComponent(config, 'textfield');
  				       
  			} else if(config.type === 'combo') {
  				//inputField = qooxdoo.commons.WidgetUtils.createInputComboBox(config);    
  				//inputField.setUserData('type', 'combo');
  				var componentRegistry = qooxdoo.commons.ComponentRegistry.getInstance();				
				inputField = componentRegistry.createComponent(config, 'combo');
  				    
  			} else if(config.type === 'flag') {
  				//inputField = qooxdoo.commons.WidgetUtils.createInputFlagBox(config);    
  				//inputField.setUserData('type', 'flag');
  				var componentRegistry = qooxdoo.commons.ComponentRegistry.getInstance();				
				inputField = componentRegistry.createComponent(config, 'flag');
  				    
  			} else if(config.type === 'form') {
  				//inputField = qooxdoo.commons.WidgetUtils.createInputForm(config);    
  				//inputField.setUserData('type', 'form');
  				var componentRegistry = qooxdoo.commons.ComponentRegistry.getInstance();				
				inputField = componentRegistry.createComponent(config, 'form');
  				    
  			} else if(config.type === 'formList') {
  				//inputField = qooxdoo.commons.WidgetUtils.createInputFormList(config);    
  				//inputField.setUserData('type', 'formList');
  				var componentRegistry = qooxdoo.commons.ComponentRegistry.getInstance();				
				inputField = componentRegistry.createComponent(config, 'formList');
  				    
  			} else if(config.type === 'textarea') {
  				//inputField = qooxdoo.commons.WidgetUtils.createInputTextArea(config);    
  				//inputField.setUserData('type', 'textarea');
  				var componentRegistry = qooxdoo.commons.ComponentRegistry.getInstance();				
				inputField = componentRegistry.createComponent(config, 'textarea');
  				    
  			} else if(config.type === 'check') {
  				//inputField = qooxdoo.commons.WidgetUtils.createInputCheckBox(config);    
  				//inputField.setUserData('type', 'check'); 
  				var componentRegistry = qooxdoo.commons.ComponentRegistry.getInstance();				
				inputField = componentRegistry.createComponent(config, 'check');
  				   
  			} else if(config.type === 'radio') {
  				//inputField = qooxdoo.commons.WidgetUtils.createInputRadio(config);    
  				//inputField.setUserData('type', 'radio');
  				var componentRegistry = qooxdoo.commons.ComponentRegistry.getInstance();				
				inputField = componentRegistry.createComponent(config, 'radio');
  				
  			}  else if(config.type === 'propertiesList') {
  				//inputField = qooxdoo.commons.WidgetUtils.createPropertiesList(config);    
  				//inputField.setUserData('type', 'propertiesList');
  				var componentRegistry = qooxdoo.commons.ComponentRegistry.getInstance();				
				inputField = componentRegistry.createComponent(config, 'propertiesList');
  				
  			} else if(config.type === 'groupbox') {
  				inputField = qooxdoo.commons.WidgetUtils.createInputGroupBox(config);    
  				inputField.setUserData('type', 'groupbox');
  				//var componentRegistry = qooxdoo.commons.ComponentRegistry.getInstance();				
				//inputField = componentRegistry.createComponent(config, 'groupbox');
  				
  			}
  			
			if(config.read != undefined){
					var f = inputField.getUserData('field').getChildren()[0];
					f.setEnabled(false);
				}
			
			if(config.button != undefined){
				var b = new qx.ui.form.Button(config.button[0].label);
				inputField.add(b);
				inputField.setUserData('button',b);
				b.addListener(config.button[0].event, config.button[0].handler, config.button[0].scope);
			}
			
  			this.dataMappings[config.dataIndex] = inputField;
  			this.add(inputField);
		}
	}
});