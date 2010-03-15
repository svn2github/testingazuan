Ext.ns("Sbi.console");


Sbi.console.InlineToggleActionColumn = function(config){
	
    // parent constructor
    Sbi.console.InlineToggleActionColumn.superclass.constructor.call(this, config);
    
};


Ext.extend(Sbi.console.InlineToggleActionColumn, Sbi.console.InlineActionColumn, {

    onClick : function(e, t){

        if(t.className && t.className.indexOf('x-mybutton-'+this.id) != -1){
            e.stopEvent();
            
            var index = this.grid.getView().findRowIndex(t);
            var record = this.grid.store.getAt(index);   
            
            var value = record.get(this.grid.store.getFieldNameByAlias(this.column));
          
            if (value == 1)
            	value = 0; //false
            else
            	value = 1; //true
            
            //this.column = value;
            
            record.set (this.grid.store.getFieldNameByAlias(this.column), value);
            record.commit();
            
            if (this.name === 'monitor'){
	            //force the list refresh	                   
            	//alert(this.grid.store.filterPlugin.getFilters().toSource());
	            this.grid.store.filterPlugin.applyFilters();	            
            }
            this.handler.call(this.scope, this.name, record, index, this.options);          
        }
    }


    , renderer : function(v, p, record){

    	var value = record.get(this.grid.store.getFieldNameByAlias(this.column));
    	if (value === undefined) return '';
    	
    	var img= '';
    	if (this.column){
	    	if (value == 1)
	    		img = '<center><img class="x-mybutton-'+this.id+'" width="13px" height="13px" src="' + this.imgSrcInactive + '" title= "' + this.tooltipInactive + '"/></center>';
	    	else
	    		img = '<center><img class="x-mybutton-'+this.id+'" width="13px" height="13px" src="' + this.imgSrcActive + '" title= "' + this.tooltipActive + '"/></center>';
    	}	
    	
        return img;
    }
  });