qx.Class.define("qooxdoo.data.DataProxy",
{
  	extend : qx.core.Object,

  	/*
  	*****************************************************************************
     CONSTRUCTOR
  	*****************************************************************************
  	*/

	construct : function( config ) {
    	this.base(arguments);
    	
    	config = config || {};
    	
    	if(config instanceof qx.io.remote.Request)  {
    		this._request = config;
    	} else {
    		var defaultConfig = {
    	    		url: ''
    	    		, params: {}
    	    		, method: 'GET'    	    		
    	    		, responseMimeType: 'application/json'
    	    };
    		
    		var c = qooxdoo.commons.CoreUtils.apply({}, config, defaultConfig);
    		
    		this._url = c.url;
    		this._params = c.params;
    		this._request = new qx.io.remote.Request(c.url, c.method, c.responseMimeType);    	
    		this._request.addListener('completed', this.handleRequestCompleted, this);
    		this._request.addListener('failed', this.handleRequestFailed, this);
    	}
  	},

  	members :
  	{
  		_url: undefined
  		, _params: undefined
  		, _request: undefined
  		, _datastore: undefined
  		
  		
  		, load: function( params, append, callback, scope ) {
  			
  			var p = qooxdoo.commons.CoreUtils.apply({}, this._params);
  			if(params) {
  				qooxdoo.commons.CoreUtils.apply(p, params);
  			}
  			
  			var u = this._url;
  			var s = append? '&': '?';
  			for(var x in p){
 	            u += s + x + '=' + p[x];
 	            s = '&';
 	        }
  			
  			this._request.setUrl(u);
  			this._request.setUserData('callback', callback);
  			this._request.setUserData('scope', scope);
  			this._request.setUserData('scope', scope);
  			//this._request.setAsynchronous(false);
  			
  			/*
  			this._request.addListener("completed", function(e) {
  				this._datastore = e.getContent();
  			}, this);
  			*/
  			
  			this._request.send();
  			
  			return this._datastore;
  		}
  	
  		, handleRequestCompleted: function(response) {
  			
  			var request = response.getTarget()
  			var callback = request.getUserData('callback');
  			var scope = request.getUserData('scope');
  			  			
  			callback.call(scope, response.getContent());
  		}
  		
  		, handleRequestFailed: function(response) {
  			
  			//this._datastore = response.getContent();
  			var request = response.getTarget()
  			qooxdoo.commons.CoreUtils.msg('ERROR: Request failed [' + request.getUrl() + ']');
  			qooxdoo.commons.CoreUtils.dump(response);
  		}
  		
  		
  	}
});