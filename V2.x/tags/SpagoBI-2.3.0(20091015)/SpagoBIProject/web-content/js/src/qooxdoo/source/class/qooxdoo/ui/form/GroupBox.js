qx.Class.define("qooxdoo.ui.form.GroupBox",
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
  			var value ;
  			var form = this._field.getChildren()[0];	//set to form added to the groupbox
				if(form){							//Not all groupboxes have form inside - some are empty
					value = form.getData();
				}
  			return value;
  		}
  	
  		, setData: function(data) {
  			
  			var form = this._field.getChildren()[0];
				if(form){
					form.setData(data);
				}	
  		}
  		
  		, _createField: function(config) {
  		
  			var defaultConfig = {top: 0,
	        					left: 0,
	        					height: 50,
	        					width: 400};
			
			config = qooxdoo.commons.CoreUtils.apply(defaultConfig, config);
			
			var gb = new qx.ui.groupbox.GroupBox(config.text);
			gb.setLayout(new qx.ui.layout.VBox());

			
			if(config.form){
				var subform = this.createInputForm(config);
				gb.add(subform);
			}
			this._field = new qx.ui.container.Composite(new qx.ui.layout.Basic);
			this._field.add(gb, {top: config.top, left: config.left});
  	            					
  		}
  	}
});