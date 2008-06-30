/**
 *
 
 */


qx.Class.define("spagobi.ui.Form", {
	extend: qx.ui.layout.VerticalBoxLayout,
	
	construct : function(config) { 
		this.base(arguments);
		
		this.setSpacing(5);
  		
  		for(var i = 0; i < config.length; i++) {
  			this.addInputField( config[i] );
  		}
  		
  		var btn2 = new qx.ui.form.Button("save", "spagobi/img/spagobi/test/save.png");
  		btn2.addEventListener("execute", function(){alert(this.getData().toSource());}, this);
  		this.add(btn2);
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
  			}
  			
  			this.dataMappings[config.dataIndex] = inputField;
  			this.add(inputField);
		}
	},
	
	statics : {
	
	}
});