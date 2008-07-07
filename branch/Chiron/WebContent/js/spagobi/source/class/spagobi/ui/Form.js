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


/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 */
qx.Class.define("spagobi.ui.Form", {
	extend: qx.ui.layout.VerticalBoxLayout,
	
	construct : function(config) { 
		this.base(arguments);
		
		this.setSpacing(5);
  		
  		this.dataMappings = [];
  		
  		if(config) {
	  		for(var i = 0; i < config.length; i++) {
	  			this.addInputField( config[i] );
	  		}
  		}
	},
	
	members: {
		dataObject: {},
		
		dataMappings: [],
		
		getData: function() {
			for(prop in this.dataMappings) {
				if(this.dataObject[prop] != undefined) {
					this.dataObject[prop] = this.getInputFieldValue(prop);
				}
			}
			return this.dataObject;
		},
	
		
		setData: function(o) {
			for(prop in o) {
				this.setInputFieldValue(prop, o[prop]);
			}	
			this.dataObject = o;		
		},
		
		getInputField: function(dataIndex) {
			return this.dataMappings[dataIndex];
		},
		
		getInputFieldValue: function(dataIndex) {
			var value;
			
			if(!this.getInputField(dataIndex)) return null;
			if(this.getInputField(dataIndex).getUserData('type') === 'text') {
				value = this.getInputField(dataIndex).getUserData('field').getValue();
			} else if(this.getInputField(dataIndex).getUserData('type') === 'combo') {
				value = this.getInputField(dataIndex).getUserData('field').getValue();
			} else if(this.getInputField(dataIndex).getUserData('type') === 'check') {
				value = this.getInputField(dataIndex).getUserData('field').isChecked();
			} else if(this.getInputField(dataIndex).getUserData('type') === 'form') {	
				value = this.getInputField(dataIndex).getUserData('field').getData();
			} else if(this.getInputField(dataIndex).getUserData('type') === 'formList') {	
				value = this.getInputField(dataIndex).getUserData('field').getData();
			}			
			
			return value;			
		},
		
		setInputFieldValue: function(dataIndex, value) {
			if(!this.getInputField(dataIndex)) {
				return;
			}
			if(this.getInputField(dataIndex).getUserData('type') === 'text') {
				this.getInputField(dataIndex).getUserData('field').setValue(value);
			} else if(this.getInputField(dataIndex).getUserData('type') === 'combo') {
				this.getInputField(dataIndex).getUserData('field').setValue(value);
			} else if(this.getInputField(dataIndex).getUserData('type') === 'check') {
				this.getInputField(dataIndex).getUserData('field').setChecked(value);
			} else if(this.getInputField(dataIndex).getUserData('type') === 'form') {		
				this.getInputField(dataIndex).getUserData('field').setData(value);
			} else if(this.getInputField(dataIndex).getUserData('type') === 'formList') {		
				this.getInputField(dataIndex).getUserData('field').setData(value);
			}
			
		},
		
		addInputField: function(config) {
			if(config.type === 'text') {
  				inputField = spagobi.commons.WidgetUtils.createInputTextField(config); 
  				inputField.setUserData('type', 'text');       
  			} else if(config.type === 'combo') {
  				inputField = spagobi.commons.WidgetUtils.createInputComboBox(config);    
  				inputField.setUserData('type', 'combo');    
  			} else if(config.type === 'check') {
  				inputField = spagobi.commons.WidgetUtils.createInputCheckBox(config);    
  				inputField.setUserData('type', 'check');    
  			} else if(config.type === 'form') {
  				inputField = spagobi.commons.WidgetUtils.createInputForm(config);    
  				inputField.setUserData('type', 'form');    
  			} else if(config.type === 'formList') {
  				inputField = spagobi.commons.WidgetUtils.createInputFormList(config);    
  				inputField.setUserData('type', 'formList');    
  			}
  			
  			this.dataMappings[config.dataIndex] = inputField;
  			this.add(inputField);
		}
	},
	
	statics : {
	
	}
});