qx.Class.define("qooxdoo.ui.form.CheckBox",
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
  					checked: false,
		        	top: 0,
		        	left: 0,
		        	items: [],
		        	listeners: [],
		        	columns: 1
  	        };
  			config = qooxdoo.commons.CoreUtils.apply(defaultConfig, config);
  			 
  	        this._field = new qx.ui.layout.Grid;
		    this._field.auto();
        	this._field.set({ left: config.left });
        	
    		var rows = config.columns;
    		var cols = Math.ceil(config.items.length/config.columns);
    		
    		for(i=0,k=0;i<rows ; i++){
    			this._field.setRowHeight(i, 30);			
    			for(j=0; j<cols && k<config.items.length; j++,k++){
    				this._field.setColumnWidth(j, 50);	
    				
    				var label_text = new qx.ui.basic.Label(config.items[k]);
		    		var check_box = new qx.ui.form.CheckBox();
		    		
       				var atom = new qx.ui.basic.Atom();
       				
       				atom.add(check_box, label_text);
       				atom.setUserData('label', label_text);
        			atom.setUserData('field', check_box);
       				this._field.add(atom, j, i);
       				
    			}
    		}
  	            					
  		}
  	}
});