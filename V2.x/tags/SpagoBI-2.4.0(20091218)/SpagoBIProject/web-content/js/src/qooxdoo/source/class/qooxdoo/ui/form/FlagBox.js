qx.Class.define("qooxdoo.ui.form.FlagBox",
{
  	extend : qooxdoo.ui.form.InputField,

  	/*
  	*****************************************************************************
     CONSTRUCTOR
  	*****************************************************************************
  	*/
  	/*Input Filed of type FlagBox: only one checkBox A?[X] */

	construct : function(config) {
    	this.base(arguments, config);
  	},

  	members :
  	{
  		getData: function() {
  		
  			return this._field.isChecked();		
  		}
  	
  		, setData: function(data) {
  			
  			this._field.setChecked(data);
  		}
  		
  		, _createField: function(config) {
  		
  			var defaultConfig = {
  	        	checked: false,
        		top: 0,
        		left: 0,
        		listeners: []
  	        };
  			config = qooxdoo.commons.CoreUtils.apply(defaultConfig, config);
  			 
  	        this._field = new qx.ui.form.CheckBox();
  	        this._field.set({
  	        	 checked: config.checked
  	        });
  	        
  	        for(var i=0; i< config.listeners.length; i++) {
            	if(config.listeners[i].scope) {
            		this._field.addListener(config.listeners[i].event, config.listeners[i].handler, config.listeners[i].scope); 
            	} else {
            		this._field.addListener(config.listeners[i].event, config.listeners[i].handler); 
            	}
            }
  	            					
  		}
  	}
});