qx.Class.define("qooxdoo.ui.form.XComboBox",
{
  	extend : qooxdoo.ui.form.InputField,

  	/*
  	*****************************************************************************
     CONSTRUCTOR
  	*****************************************************************************
  	*/
  	/*Input Filed of type ComboBox */

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
	        		proxy: undefined,
	        		items: [],
	        		listeners: [],  	
  	        		width: 150,
  	        		height: 20
  	        };
  			config = qooxdoo.commons.CoreUtils.apply(defaultConfig, config);
  			
  			this._field = new qx.ui.form.SelectBox();
          	
          	for(var i=0; i< config.items.length; i++) {
          		
              var item;
              if(typeof config.items[i] == 'object') {
            	
            	  item = new qx.ui.form.ListItem(config.items[i].label, null, config.items[i].value);
              } else {
            	
            	  item = new qx.ui.form.ListItem(config.items[i]);
              }
             
              this._field.add(item);
            }
            
            for(var i=0; i< config.listeners.length; i++) {
            	if(config.listeners[i].scope) {
            		this._field.addListener(config.listeners[i].event, config.listeners[i].handler, config.listeners[i].scope); 
            	} else {
            		this._field.addListener(config.listeners[i].event, config.listeners[i].handler); 
            	}
            }
  			
  			
  	        this._field.set({
  	        	width:config.width
  	        	, height:config.height
  	        });
  	            					
  		}
  	}
});