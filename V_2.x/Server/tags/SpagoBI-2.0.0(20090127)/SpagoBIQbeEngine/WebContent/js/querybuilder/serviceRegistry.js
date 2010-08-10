// create namespace
Ext.namespace('it.eng.spagobi.engines.qbe.serviceregistry');


it.eng.spagobi.engines.qbe.serviceregistry.module = function(){
	// do NOT access DOM from here; elements don't exist yet
 
    // private variables
    var baseUrl = {
		protocol: 'http',       
		host: '1313',
        port: '8080',
        contextPath: 'SpagoBIQbeEngine',
        controllerPath: 'servlet/AdapterHTTP' 
    };
    
 
    // public space
	return {
	
		init : function() {
			//alert("init");
		},
		
		setBaseUrl : function(url) {
        	Ext.apply(baseUrl, url); 
        },
        
        getServiceUrl : function(actionName, absolute){
        	var baseUrlStr;
        	var serviceUrl;
        	
        	if(absolute === undefined || absolute === false) {
        		baseUrlStr = 'AdapterHTTP';
        	} else {
        		baseUrlStr = baseUrl.protocol + "://" + baseUrl.host + ":" + baseUrl.port + "/" + baseUrl.contextPath + "/" + baseUrl.controllerPath;
        	}
        	
        	serviceUrl = baseUrlStr + "?ACTION_NAME=" + actionName;
        	//alert("Service invocation: " + serviceUrl); 
        	return serviceUrl;
        }       
        
	};
}();