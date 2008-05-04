// create namespace
Ext.namespace('it.eng.spagobi.engines.qbe.locale');

it.eng.spagobi.engines.qbe.locale.module = function(){
	// do NOT access DOM from here; elements don't exist yet
 
    // private variables
    var selectGridModuleLabels = {
    	// column headers
        visible: 'Visible',
        group: 'Group',
        del: 'Delete',
        filter: 'Filter' 
        	
        // tooltips   
        //...
    };    
 
    // public space
	return {
		init : function() {
			//alert("init");
		},		
		applyLocale : function() {
        	Ext.apply(it.eng.spagobi.engines.qbe.querybuilder.selectGrid.app.labels, selectGridModuleLabels);        	
        }
        
	};
}();