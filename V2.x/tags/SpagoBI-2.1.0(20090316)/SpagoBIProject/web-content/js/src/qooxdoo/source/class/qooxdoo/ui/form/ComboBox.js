qx.Class.define("qooxdoo.ui.form.ComboBox",
{
  	extend : qx.ui.form.SelectBox,

  	/*
  	*****************************************************************************
     CONSTRUCTOR
  	*****************************************************************************
  	*/

	construct : function(config) {
    	this.base(arguments);
    	
    	var defultConfig = {
    			proxy: undefined
    			, items: []
    			, listeners: []  		
        };
        	
        config = qooxdoo.commons.CoreUtils.apply(defultConfig, config);
        
        if(config.proxy) {
        	this._displayField = config.displayField;
        	this._valueField = config.valueField;
        	config.proxy.load( {}, true, this.loadOptions, this);
        } else {
        	for(var i=0; i< config.items.length; i++) {
          		
            	var item;
                if(typeof config.items[i] == 'object') {
                	item = new qx.ui.form.ListItem(config.items[i][config.displayField], null, config.items[i][config.valueField]);
                } else {
                	item = new qx.ui.form.ListItem(config.items[i]);
                }             
                this.add(item);
            }
        }
        
        
        
            
        for(var i=0; i< config.listeners.length; i++) {
           	if(config.listeners[i].scope) {
           		this.addListener(config.listeners[i].event, config.listeners[i].handler, config.listeners[i].scope); 
         	} else {
           		this.addListener(config.listeners[i].event, config.listeners[i].handler); 
           	}
        }
  	},

  	members :
  	{	
  		_displayField: undefined
  		, _valueField: undefined
    	
  		, loadOptions : function(dataset){
  			var str = '';
			var rows = dataset.rows;
			for(var i in rows) {
				str += rows[i][this._displayField] + ' - '+ rows[i][this._valueField] + ';\n'
				var item = new qx.ui.form.ListItem('' + rows[i][this._displayField], null, '' + rows[i][this._valueField]);
				this.add(item);
			}
  		}
  	}
});