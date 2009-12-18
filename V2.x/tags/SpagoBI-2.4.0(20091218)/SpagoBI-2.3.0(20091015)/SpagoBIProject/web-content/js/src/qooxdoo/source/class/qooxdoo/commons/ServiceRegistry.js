qx.Class.define("qooxdoo.commons.ServiceRegistry",
{
  	extend : qx.core.Object,

  	/*
  	*****************************************************************************
     CONSTRUCTOR
  	*****************************************************************************
  	*/

	construct : function(config) {
    	this.base(arguments);
    	
    	var defaultConfig = {
    		protocol: 'http',       
      		host: 'localhost',
      		port: '8080',
      		contextPath: 'SpagoBI',
      		controllerPath: 'servlet/AdapterHTTP',
      		execId: -1 
    	};
    	
    	this._baseUrl = qooxdoo.commons.CoreUtils.apply({}, config, defaultConfig);
  	},

  	members :
  	{
  		_baseUrl : undefined
  		
  		, getServiceUrl : function(actionName, absolute) {
  			var baseUrlStr;
  	        var serviceUrl;
  	        	
  	        if(absolute === undefined || absolute === false) {
  	        	baseUrlStr = 'AdapterHTTP';
  	        } else {
  	        	baseUrlStr = this._baseUrl.protocol + "://" + this._baseUrl.host + ":" + this._baseUrl.port + "/" + this._baseUrl.contextPath + "/" + this._baseUrl.controllerPath;
  	        }
  	        
  	        serviceUrl = baseUrlStr + "?ACTION_NAME=" + actionName + "&SBI_EXECUTION_ID=" + this._baseUrl.execId;
  	        
  	        return serviceUrl;
  		}
  	}
});