qx.Class.define("qooxdoo.proxy.ComponetProxy",
{
  	extend : qx.core.Object,

  	/*
  	*****************************************************************************
     CONSTRUCTOR
  	*****************************************************************************
  	*/

	construct : function() {
    	this.base(arguments);
    	
    	this._serviceRegistry = new qooxdoo.commons.ServiceRegistry();
    	this._services = new Array();		
  	},

  	members :
  	{
  		_serviceRegistry: undefined
  		, _services: undefined
  		
  		, _addService: function(serviceAlias, serviceName) {
  			this._services[serviceAlias]	= serviceName;  			
  		}
  	
  		, invokeService: function(serviceAlias, config) {
  			var serviceName = this._services[serviceAlias];
  			var serviceUrl = this._serviceRegistry.getServiceUrl( serviceName );
  			alert(serviceUrl);
  			if(config.params) serviceUrl = this._addParams(serviceUrl, config.params);
  			alert(serviceUrl);
  			var request = new qx.io.remote.Request(serviceUrl, 
  					'POST', 'application/json'); 
  			request.setUserData('successHandler', config.success);
  			request.setUserData('failureHandler', config.failure);
  			request.addListener('completed', this._onRequestCompleted, this);
  			request.addListener('failed', this._onRequestFailed, this);
  			
  			request.send();
  		}
  		
  		, _addParams: function(url, params) {
  			var u = url;
  			var p = params;
  			for(var x in p){
  				var v = ((typeof p[x] == 'object')
  						? qx.util.Json.stringify( p[x] )
  						:p[x]
  				);
  				alert('-> ' + v);
  				
 	            u += '&' + x + '=' + v;
 	        }
  			return u;
  		}
  	
  		, _onRequestCompleted: function(response) {
  			
  			var request = response.getTarget()
  			var successHandler = request.getUserData('successHandler');
  			if(successHandler) {
	  			var handlerFunction = successHandler.fn
	  			var handlerScope = successHandler.scope;  			  			
	  			handlerFunction.call(handlerScope, response.getContent());
  			}
  		}
  		
  		, _onRequestFailed: function(response) {
  			
  			var request = response.getTarget()
  			var failureHandler = request.getUserData('failureHandler');
  			if(successHandler) {
	  			var handlerFunction = failureHandler.fn
	  			var handlerScope = failureHandler.scope;  			  			
	  			handlerFunction.call(handlerScope, response.getContent());
  			}
  		}
  		
  	}
});