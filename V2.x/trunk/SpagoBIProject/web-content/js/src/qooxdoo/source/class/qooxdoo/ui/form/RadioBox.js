qx.Class.define("qooxdoo.ui.form.RadioBox",
{
  	extend : qooxdoo.ui.form.InputField,

  	/*
  	*****************************************************************************
     CONSTRUCTOR
  	*****************************************************************************
  	*/

	construct : function(config) {
    	this.base(arguments, config);
  	},

  	members :
  	{
  		getData: function() {
  			return this._field.getValue();
  		}
  	
  		, setData: function(data) {
  			this._field.setValue(data);
  		}
  		
  		, _createField: function(config) {
  		
  			var defaultConfig = {  	
  	        		top: 0,
	        		left: 0,
	        		items: [],
	        		listeners: []  
  	        };
  			config = qooxdoo.commons.CoreUtils.apply(defaultConfig, config);

  	        this._field = new qx.ui.container.Composite(new qx.ui.layout.Basic);
  	        
  	        var radioButtons = [];
          	var radioManager = new qx.ui.form.RadioGroup();
          	
          	for(i=0; i<config.items.length; i++){
          		
          		radioButtons[i] = new qx.ui.form.RadioButton(config.items[i]);
          		radioManager.add(radioButtons[i]);
          		this._field.add(radioButtons[i], { top: config.top, left: (config.left+ i*60)});
          		
          	}
          	
          	for(var i=0; i< config.listeners.length; i++) {
            	if(config.listeners[i].scope) {
            		radioManager.addListener(config.listeners[i].event, config.listeners[i].handler, config.listeners[i].scope); 
            	} else {
            		radioManager.addListener(config.listeners[i].event, config.listeners[i].handler); 
            	}
            }
            radioButtons[0].setChecked(true);
  	        
  	        this._field.set({
  	        	 top: config.top
  	        });           		
  		}
  	}
});