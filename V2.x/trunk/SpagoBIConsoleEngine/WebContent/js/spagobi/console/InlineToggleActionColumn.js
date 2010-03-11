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
           // alert(this.grid.store.getFieldNameByAlias(this.column));
            if (value === true || value === 1)
            	this.column = 0; //false
            else
            	this.column = 1; //true
            
      //      alert("orig value: " + value + " - new value: " +  this.column);
            
            //this.renderer(null, null, record),
            this.handler.call(this.scope, this.name, record, index, this.options);          
        }
    }


    , renderer : function(v, p, record){

    	var value = record.get(this.grid.store.getFieldNameByAlias(this.column));

    	var img= '';
    	if (this.column){
	    	if (value === true || value === 1)
	    		img = '<center><img class="x-mybutton-'+this.id+'" width="13px" height="13px" src="' + this.imgSrcActive + '" title= "' + this.tooltipActive + '"/></center>';
	    	else
	    		img = '<center><img class="x-mybutton-'+this.id+'" width="13px" height="13px" src="' + this.imgSrcInactive + '" title= "' + this.tooltipInactive + '"/></center>';
    	}	
    	
        return img;
    }
  });