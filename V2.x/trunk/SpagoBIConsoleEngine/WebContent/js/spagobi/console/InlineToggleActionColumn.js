Ext.ns("Sbi.console");


Sbi.console.InlineToggleActionColumn = function(config){
	
    // parent constructor
    Sbi.console.InlineToggleActionColumn.superclass.constructor.call(this, config);
    
    
    
};


Ext.extend(Sbi.console.InlineToggleActionColumn, Sbi.console.InlineActionColumn, {
	
	checkColumn: null
	, flagColumn: null
	
	
	, UNFLAGGED_VALUE: 0
	, CHECKED_VALUE: 0
	, UNCHECKED_VALUE: 1
	
	// -- public methods ------------------------------------------------------------------------
	
	, isBoundToColumn: function() {
		return (this.checkColumn !== null);
	}

	

	, getBoundColumnValue: function(record) {
		var v, s;
		
		s = this.grid.store;
		v = record.get(s.getFieldNameByAlias(this.checkColumn));
    	if (v === undefined || v === null) {
    		Sbi.Msg.showError('Impossible to draw toggle column [' + this.dataIndex + ']. Dataset [' + s.storeId + ']does not contain column [' + this.checkColumn + ']');
    	};
    	
    	if(v !== this.CHECKED_VALUE && v !== this.UNCHECKED_VALUE) {
    		Sbi.Msg.showError('Column [' + this.checkColumn + '] of dataset [' + s.storeId + '] contains a wrong value [' + v + ']. Impossible to determinate the state of the bounded togle column [' + this.checkColumn + ']');
    	}
    	
    	return v;
	}
	
	, isActive: function(record) {
		var active = true;
		if(this.flagColumn) {
			var v, s;
			
			s = this.grid.store;
			v = record.get(s.getFieldNameByAlias(this.flagColumn));
	    	if (v === undefined || v === null) {
	    		Sbi.Msg.showError('Impossible to draw toggle column [' + this.dataIndex + ']. Dataset [' + s.storeId + ']does not contain column [' + this.flagColumn + ']');
	    	};
	    	active = (this.UNFLAGGED_VALUE !== v);
	    	//alert(v + ' !== '+ this.UNFLAGGED_VALUE + ' : ' + active);
		}
			
		return active;
	}

	, isChecked: function(record) {
		var v, active;
		if(this.isBoundToColumn()) {
			v = this.getBoundColumnValue(record);
			
	    	active = (v === this.CHECKED_VALUE);
	    	//alert(v + ' === '+ this.ACTIVE_VALUE + ' : ' + active);
		}
		
		return active;		
	}
	
	, setChecked: function(record, b) {
		var v, s;
		if(this.isBoundToColumn()) {
			s = this.grid.store;
			v = (b? this.CHECKED_VALUE: this.UNCHECKED_VALUE);
			record.set (s.getFieldNameByAlias(this.checkColumn), v );
            //record.commit();
		}
	}
	
	, toggle: function(record) {
		this.setChecked(record, !this.isChecked(record));
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
    	
    	if(this.isActive(record) === false) {
    		return '';
    	}

	    if (this.isChecked(record)) {
	    	img = '<center><img class="x-mybutton-'+this.id+'" width="13px" height="13px" src="' + this.imgSrcActive + '" title= "' + this.tooltipActive + '"/></center>';
	    } else {
	    	img = '<center><img class="x-mybutton-'+this.id+'" width="13px" height="13px" src="' + this.imgSrcInactive + '" title= "' + this.tooltipInactive + '"/></center>';  
	    }	    		
    	
        return img;
    }
});