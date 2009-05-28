/**
 * SpagoBI - The Business Intelligence Free Platform
 *
 * Copyright (C) 2004 - 2008 Engineering Ingegneria Informatica S.p.A.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 **/
 
/**
  * Object name 
  * 
  * [description]
  * 
  * 
  * Public Properties
  * 
  * [list]
  * 
  * 
  * Public Methods
  * 
  *  [list]
  * 
  * 
  * Public Events
  * 
  *  [list]
  * 
  * Authors
  * 
  * - Andrea Gioia (andrea.gioia@eng.it)
  */

Ext.ns("Sbi.widgets");

Sbi.widgets.LookupField = function(config) {
	
	Ext.apply(this, config);
	
	this.store = config.store;
	this.store.on('metachange', function( store, meta ) {
		/*
		if(this.grid){
			
			this.valueField = meta.valueField;
			this.displayField = meta.displayField;
			this.descriptionField = meta.descriptionField;
			
			meta.fields[0] = new Ext.grid.RowNumberer();
			meta.fields[ meta.fields.length - 1 ] = this.sm;
			this.grid.getColumnModel().setConfig(meta.fields);
		} else {
		   alert('ERROR: store meta changed before grid instatiation')
		}
		*/
		this.updateMeta( meta );
	}, this);
	this.store.on('load', function( store, records, options  ) {
		this.applySelection();		
	}, this);
	

	this.store.baseParams  = config.params;
	this.params = config.params;
	this.initWin();
	
	var c = Ext.apply({}, config, {
		triggerClass: 'x-form-search-trigger'
		,  width: 150
	});   
	
	// constructor
	Sbi.widgets.LookupField.superclass.constructor.call(this, c);
	
	
	this.on("render", function(field) {
		field.trigger.on("click", function(e) {
			this.onLookUp(); 
		}, this);
	}, this);
};

Ext.extend(Sbi.widgets.LookupField, Ext.form.TriggerField, {
    
	// ----------------------------------------------------------------------------------------
	// members
	// ----------------------------------------------------------------------------------------
    
	// STATE MEMBERS
	  valueField: null
    , displayField: null
    , descriptionField: null
    
    // oggetto (value: description, *)
    , xvalue: null
    // oggetto (value: description, *)
    , xselection: null
    
    , singleSelect: true
    
    , paging: true
    , start: 0 
    , limit: 20
    
	// SUB-COMPONENTS MEMBERS
	, store: null
	, sm: null
    , grid: null
    , win: null
    
       
   
    // ----------------------------------------------------------------------------------------
    // public methods
	// ----------------------------------------------------------------------------------------
    
    
    , getValue : function(){
		var v = [];
		this.xvalue = this.xvalue || {};
		for(p in this.xvalue) {
			v.push(p);
		}
			
		if(this.singleSelect === true) {
			v = (v.length > 0)? v[0] : '';
		}
		
		return v;
	}

	, setValue : function(v){	 
		alert(v.toSource());
		
		if(typeof v === 'object') {
			this.xvalue = {};
			Ext.apply(this.xvalue, v);
			alert(this.xvalue.toSource());
			var displayText = '';
			for(p in this.xvalue) {
				displayText += this.xvalue[p] + ';'
			}				
			alert(displayText);
			Sbi.widgets.LookupField.superclass.setValue.call(this, displayText);
		} else {
			alert('orrore');
		}
		/*
		if(typeof v === 'object') {
			
			if(v instanceof Array) { // multivalue
				var displayText = '';
				this.xvalue = new Array();
				for(var i = 0; i < v.length; i++) {
					displayText += v[i].data[this.displayField] + ';'
					this.xvalue[i] = v[i].data[this.valueField];
				}				
				Sbi.widgets.LookupField.superclass.setValue.call(this, displayText);
			} else { // single value
				
				this.xvalue = v.data[this.valueField];
				Sbi.widgets.LookupField.superclass.setValue.call(this, v.data[this.displayField]);
			}
			
		} else {
			this.xvalue = v;
			Sbi.widgets.LookupField.superclass.setValue(v);
		}
		*/
	}
    
    // private methods
    , initWin: function() {
		var cm = new Ext.grid.ColumnModel([
		   new Ext.grid.RowNumberer(),
	       {
	       	  header: "Data",
	          dataIndex: 'data',
	          width: 75
	       }
	    ]);
		
		var pagingBar = new Ext.PagingToolbar({
	        pageSize: this.limit,
	        store: this.store,
	        displayInfo: true,
	        displayMsg: '', //'Displaying topics {0} - {1} of {2}',
	        emptyMsg: "No topics to display",
	        
	        items:[
	               '->'
	               , {
	            	   text: 'Annulla'
	            	   , listeners: {
		           			'click': {
		                  		fn: this.onCancel,
		                  		scope: this
		                	} 
	               		}
	               } , {
	            	   text: 'Conferma'
	            	   , listeners: {
		           			'click': {
		                  		fn: this.onOk,
		                  		scope: this
		                	} 
	               		}
	               }
	        ]
	    });
		
		this.sm = new Ext.grid.CheckboxSelectionModel( {singleSelect: this.singleSelect } )
		this.sm.on('rowselect', this.onSelect, this);
		this.sm.on('rowdeselect', this.onDeselect, this);
		
		this.grid = new Ext.grid.GridPanel({
			store: this.store
   	     	, cm: cm
   	     	, sm: this.sm
   	     	, frame: false
   	     	, border:false  
   	     	, collapsible:false
   	     	, loadMask: true
   	     	, viewConfig: {
   	        	forceFit:true
   	        	, enableRowBody:true
   	        	, showPreview:true
   	     	}
		
	        , bbar: pagingBar
		});
		
		this.win = new Ext.Window({
			title: 'Select value ...',   
            layout      : 'fit',
            width       : 580,
            height      : 300,
            closeAction :'hide',
            plain       : true,
            items       : [this.grid]
		});
	}
    
    , updateMeta: function(meta) {
    	//alert('updateMeta');
    	if(this.grid){			
			this.valueField = meta.valueField;
			this.displayField = meta.displayField;
			this.descriptionField = meta.descriptionField;
			
			meta.fields[0] = new Ext.grid.RowNumberer();
			meta.fields[ meta.fields.length - 1 ] = this.sm;
			this.grid.getColumnModel().setConfig(meta.fields);
		} else {
		   alert('ERROR: store meta changed before grid instatiation')
		}
	}
    
    , resetSelection: function() {
    	this.xselection = Ext.apply({}, this.xvalue);    
	}
    
    , onSelect: function(sm, rowIndex, record) {
    	//alert('onSelect');
    	if(this.singleSelect === true){
    		this.xselection = {}
    	}
    	this.xselection[ record.data[this.valueField] ] = record.data[this.displayField];
    }
    
    , onDeselect: function(sm, rowIndex, record) {
    	//alert('onDeselect');
    	if( this.xselection[ record.data[this.valueField]] ) {
    		delete this.xselection[ record.data[this.valueField]];
    	}    	
    }
    
    , applySelection: function() {
    	alert('applySelection');
  
    	if(this.grid) {    		    		
			var selectedRecs = [];
			this.grid.getStore().each(function(rec){
		        if(this.xselection[ rec.data[this.valueField]] !== undefined){
		        	selectedRecs.push(rec);
		        }
		    }, this);
		    this.sm.selectRecords(selectedRecs);
		 }		
    }
	
	, onLookUp: function() {
		this.resetSelection();
		this.win.show(this);
		var p = Ext.apply({}, this.params, {
			start: this.start
			, limit: this.limit
		});
		this.store.load({params: p});
	}
	
	, onOk: function() {
		this.setValue(this.xselection);
		this.win.hide();		
	}
	
	, onCancel: function() {
		this.win.hide();
	}
});