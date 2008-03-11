// create namespace
Ext.namespace('it.eng.spagobi.engines.qbe.locale');


it.eng.spagobi.engines.qbe.locale.module = function(){
	// do NOT access DOM from here; elements don't exist yet
 
    // private variables
    var selectGridModuleLabels = {
    	// column headers
        visible: 'XVisibleX',
        group: 'XGroupX',
        del: 'XDeleteX',
        filter: 'XFilterX' 
        	
        // tooltips   
    };
    
 
    // public space
	return {
		init : function() {
			//alert("init");
		}
		,
		
		applyLocale : function() {
			//alert("applyLocale");
        	//Ext.apply(it.eng.spagobi.engines.qbe.querybuilder.selectGrid.app.labels, selectGridModuleLabels);
        	//alert(it.eng.spagobi.engines.qbe.querybuilder.selectGrid.app.labels.filter);
        }
        
	};
}();