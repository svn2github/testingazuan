qx.Class.define("qooxdoo.ui.form.FormInputField",
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
  			return this._field.getData();
  		}
  	
  		, setData: function(data) {
  			this._field.setData(data);
  		}
  		
  		, _createField: function(config) {

			if(typeof(config.form) ==  'object') {
				this._field = new qooxdoo.ui.form.Form( config.form );
			} else {
				this._field = new config.form();
			}
			
			this._field.setUserData('field', this._field);
			
			if(config.visible != undefined){
				if(config.visible){
					this._field.setVisibility("visible");
				}
				else{
					this._field.setVisibility("excluded");
				}
			}	        					
  		}
  	}
});