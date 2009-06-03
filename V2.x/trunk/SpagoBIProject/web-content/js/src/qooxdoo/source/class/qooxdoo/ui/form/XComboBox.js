qx.Class.define("qooxdoo.ui.form.XComboBox",
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
	        		proxy: undefined,
	        		items: [],
	        		listeners: [],  	
  	        		width: 200,
  	        		height: 20
  	        };
  			config = qooxdoo.commons.CoreUtils.apply(defaultConfig, config);
  			 alert('combo items '+config.items);
  	        this._field = new qx.ui.form.ComboBox(config);
  	        qooxdoo.commons.CoreUtils.dump(config);
  	        this._field.set({
  	        	width:config.width
  	        	, height:config.height
	        	, left: config.left + 10
	        	, items: config.items
	        	, listeners: config.listeners
  	        });
  	            					
  		}
  	}
});