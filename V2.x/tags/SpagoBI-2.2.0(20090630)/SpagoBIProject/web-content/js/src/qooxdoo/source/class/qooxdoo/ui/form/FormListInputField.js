qx.Class.define("qooxdoo.ui.form.FormListInputField",
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
  						
			this._field = new qooxdoo.ui.form.FormList( config.formList );
			this._field.setUserData('field', this._field );
  	            					
  		}
  	}
});