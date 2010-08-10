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

        	var content = Ext.util.JSON.decode( response.responseText );
        	
        	var errMessage = ''
            	if(response !== undefined) {
            		
            		if(response.responseText !== undefined) {
            			var content = Ext.util.JSON.decode( response.responseText );
            			if (content.errors !== undefined) {
        					for (var count = 0; count < content.errors.length; count++) {
        						var anError = content.errors[count];
			        			if (anError.localizedMessage !== undefined && anError.localizedMessage !== '') {
			        				errMessage += anError.localizedMessage;
			        			} else if (anError.message !== undefined && anError.message !== '') {
			        				errMessage += anError.message;
			        			}
			        			if (count < content.errors.length - 1) {
			        				errMessage += '<br/>';
			        			}
        					}
        				}
            		} else {
            			errMessage = 'Generic error';
            		}
            	}
        	
        	Ext.MessageBox.show({
           		title: 'Service Error',
           		msg: errMessage,
           		buttons: Ext.MessageBox.OK,
           		//fn: showResult,
           		icon: Ext.MessageBox.ERROR
       		});
        	
        	//alert();
        }
        
	};
}();