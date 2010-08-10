qx.Class.define("qooxdoo.dao.SpagoBIEngineDAO",
{
  	extend : qooxdoo.proxy.ComponetProxy,

  	/*
  	*****************************************************************************
     CONSTRUCTOR
  	*****************************************************************************
  	*/

	construct : function() {
    	this.base(arguments);
    	    	
    	this._addService('list', 'LIST_ENGINES_ACTION');
    	this._addService('save', 'SAVE_ENGINE_ACTION');
  	},

  	members :
  	{
  		getEngines: function(config) {
  			this.invokeService('list', config);
  		}
  	
  		, saveEngine: function(engine, config) {
  			config.params = config.params || {};
  			config.params.ENGINE = engine;
  			this.invokeService('save', config);
  		}  		
  	}
});