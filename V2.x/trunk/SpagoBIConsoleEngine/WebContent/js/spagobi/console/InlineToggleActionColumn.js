Ext.ns("Sbi.console");


Sbi.console.InlineToggleActionColumn = function(config){
	
    // parent constructor
    Sbi.console.InlineToggleActionColumn.superclass.constructor.call(this, config);
    
    
    
};


Ext.extend(Sbi.console.InlineToggleActionColumn, Sbi.console.InlineActionColumn, {
	
	column: null
	
	, ACTIVE_VALUE: 0
	, INACTIVE_VALUE: 1
	
	// -- public methods ------------------------------------------------------------------------
	
	, isBoundToColumn: function() {
		return (this.column !== null);
	}

	, getBoundColumnValue: function(record) {
		var v, s;
		
		s = this.grid.store;
		v = record.get(s.getFieldNameByAlias(this.column));
    	if (v === undefined || v === null) {
    		Sbi.Msg.showError('Impossible to draw toggle column [' + this.dataIndex + ']. Dataset [' + s.storeId + ']does not contain column [' + this.column + ']');
    	};
    	
    	if(v !== this.ACTIVE_VALUE && v !== this.INACTIVE_VALUE) {
    		Sbi.Msg.showError('Column [' + this.column + '] of dataset [' + s.storeId + '] contains a wrong value [' + v + ']. Impossible to determinate the state of the bounded toglle column [' + this.column + ']');
    	}
    	
    	return v;
	}

	, isActive: function(record) {
		var v, active;
		if(this.isBoundToColumn()) {
			v = this.getBoundColumnValue(record);
			
	    	active = (v === this.ACTIVE_VALUE);
	    	//alert(v + ' === '+ this.ACTIVE_VALUE + ' : ' + active);
		}
		
		return active;		
	}
	
	, setActive: function(record, b) {
		var v, s;
		if(this.isBoundToColumn()) {
			s = this.grid.store;
			v = (b? this.ACTIVE_VALUE: this.INACTIVE_VALUE);
			record.set (s.getFieldNameByAlias(this.column), v );
            //record.commit();
		}
	}
	
	, toggle: function(record) {
		this.setActive(record, !this.isActive(record));
	}

	

	
	
	// -- private methods ------------------------------------------------------------------------

    , onClick : function(e, t){

        if(t.className && t.className.indexOf('x-mybutton-'+ this.id) != -1){
            e.stopEvent();
            
            var index = this.grid.getView().findRowIndex(t);
            var record = this.grid.store.getAt(index);   
                      
            this.toggle(record);
  
            this.handler.call(this.scope, this.name, record, index, this.options);          
        }
    }


    , renderer : function(v, p, record){

	    if (this.isActive(record)) {
	    	img = '<center><img class="x-mybutton-'+this.id+'" width="13px" height="13px" src="' + this.imgSrcActive + '" title= "' + this.tooltipActive + '"/></center>';
	    } else {
	    	img = '<center><img class="x-mybutton-'+this.id+'" width="13px" height="13px" src="' + this.imgSrcInactive + '" title= "' + this.tooltipInactive + '"/></center>';  
	    }	    		
    	
        return img;
    }
});