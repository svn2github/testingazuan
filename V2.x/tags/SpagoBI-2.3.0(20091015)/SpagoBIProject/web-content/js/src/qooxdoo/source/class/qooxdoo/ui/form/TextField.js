qx.Class.define("qooxdoo.ui.form.TextField",
{
  	extend : qooxdoo.ui.form.InputField,

  	/*
  	*****************************************************************************
     CONSTRUCTOR
  	*****************************************************************************
  	*/
  	/*Input Filed of type Text  */

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
  	        		maxLength:100,    		
  	        		width: 200,
        			height: 20,
  	        		value: '',
  	        		'readOnly': false
  	        };
  			config = qooxdoo.commons.CoreUtils.apply(defaultConfig, config);
  			 
  	        this._field = new qx.ui.form.TextField();
  	        this._field.set({
  	        	width:config.width
  	        	, height:config.height
	        	, left: config.left + 10
  	        	, value:config.value
  	        	, maxLength:config.maxLength
  	        	, readOnly:config.readOnly 
  	        });
  	            					
  		}
  	}
});