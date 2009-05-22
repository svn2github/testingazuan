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
	
	this.store = config.store;
	this.params = config.params;
	this.initWin();
	
	
	
	var c = Ext.apply({}, config, {
		triggerClass: 'x-form-search-trigger'
		,  width: 150
	});   
	
	// constructor
	Sbi.widgets.LookupField.superclass.constructor.call(this, c);
	
	this.store.on('metachange', function( store, meta ) {
		alert('things gonna change');
		   if(this.grid){
			   alert('I wish you were here');
			   meta.fields[0] = new Ext.grid.RowNumberer();
			   this.grid.getColumnModel().setConfig(meta.fields);
		   } else {
			   alert('ERROR: store meta changed before grid instatiation')
		   }
		   
	}, this);
	
	this.on("render", function(field) {
		field.trigger.on("click", function(e) {
			this.onLookUp(); 
		}, this);
	}, this);
};

Ext.extend(Sbi.widgets.LookupField, Ext.form.TriggerField, {
    
    store: null
    , grid: null
    , win: null
   
   
    // public methods
    
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
		
		this.grid = new Ext.grid.GridPanel({
			store: this.store,
   	     	cm: cm,
   	     	frame: false,
   	     	border:false,  
   	     	collapsible:false,
   	     	loadMask: true,
   	     	viewConfig: {
   	        	forceFit:true,
   	        	enableRowBody:true,
   	        	showPreview:true
   	     	}
		});
		
		this.win = new Ext.Window({
			title: 'Select value ...',   
            layout      : 'fit',
            width       : 500,
            height      : 300,
            closeAction :'hide',
            plain       : true,
            items       : [this.grid]
		});
	}
	
	, onLookUp: function() {
		
		this.win.show(this);
		/*
		var param = {};
		Ext.apply(param, executionInstance);
		Ext.apply(param, {
			PARAMETER_ID: f.name
			, MODE: 'complete'
		});
		*/
		this.store.load({params: this.params});
	}
});