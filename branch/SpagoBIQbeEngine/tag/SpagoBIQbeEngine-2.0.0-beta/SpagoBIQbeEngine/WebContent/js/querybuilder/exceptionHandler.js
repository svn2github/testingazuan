// create namespace
Ext.namespace('it.eng.spagobi.engines.qbe.exceptionhandler');


it.eng.spagobi.engines.qbe.exceptionhandler.module = function(){
	// do NOT access DOM from here; elements don't exist yet
 
    // private variables
    
 
    // public space
	return {
	
		init : function() {
			//alert("init");
		},
		
		
        handleFailure : function(response, options) {
        	var str = "";
        	for(p in response) {
        		str += p + " = " + response[p] + "\n";
        	}
        	
        	var content = Ext.util.JSON.decode( response.responseText );
        	Ext.MessageBox.show({
           		title: 'Service Error',
           		msg: content.cause,
           		buttons: Ext.MessageBox.OK,
           		//fn: showResult,
           		icon: Ext.MessageBox.ERROR
       		});
        	
        	//alert();
        }
        
	};
}();