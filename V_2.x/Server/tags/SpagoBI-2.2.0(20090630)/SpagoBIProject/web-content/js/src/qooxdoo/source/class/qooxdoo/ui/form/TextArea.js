qx.Class.define("qooxdoo.ui.form.TextArea",
{
  	extend : qooxdoo.ui.form.InputField,

  	/*
  	*****************************************************************************
     CONSTRUCTOR
  	*****************************************************************************
  	*/
	/*Input Filed of type Text Area  */
	
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
        		width: 200,
        		height: 50
        	};
  			
  			config = qooxdoo.commons.CoreUtils.apply(defaultConfig, config);		 
  			
  	        this._field = new qx.ui.form.TextArea();
  	        this._field.set({
  	        	width:config.width
  	        	, height:config.height 
  	        	, left: config.left + 10
  	        	, top: config.top
  	        });
  	            					
  		}
  	}
});