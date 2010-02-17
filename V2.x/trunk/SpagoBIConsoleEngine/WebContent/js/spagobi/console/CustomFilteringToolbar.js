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
  * - Antonella Giachino (antonella.giachino@eng.it)
  */

Ext.ns("Sbi.console");

Sbi.console.CustomFilteringToolbar = function(config) {

		var defaultSettings = {
		    // default goes here
		};
		
		if(Sbi.settings && Sbi.settings.console && Sbi.settings.console.customFilteringToolbar) {
			defaultSettings = Ext.apply(defaultSettings, Sbi.settings.console.customFilteringToolbar);
		}
		
		var c = Ext.apply(defaultSettings, config || {});
	//	var filterBarConfig = c.filterBar || {};
	//	delete c.filterBar;
		Ext.apply(this, c);
		
		/*
		this.services = this.services || new Array();	
		this.services['doThat'] = this.services['doThat'] || Sbi.config.serviceRegistry.getServiceUrl({
			serviceName: 'DO_THAT_ACTION'
			, baseParams: new Object()
		});
		*/
		
		
		//this.initFilters(c || {});
	
		c = Ext.apply(c, {items:this.customFilterBar});

		// constructor
		Sbi.console.CustomFilteringToolbar.superclass.constructor.call(this, c);
		
};

Ext.extend(Sbi.console.CustomFilteringToolbar, Sbi.console.FilteringToolbar, {  
    services: null
    , customFilterBar: null
   

    // -- public methods ---------------------------------------------------------------
    , onRender : function(ct, position) {
		  
  		var s, cb;
      
  		if (this.filterBar.type === 'automatic' ){
  		
  			for(var i=0, l=this.filterBar.filters.length; i<l; i++) {
    		  
    				cb = new Ext.form.ComboBox({
          	        store: this.store,
          	        width: 100,
          	        displayField:'column-'+(i+1),
          	        valueField:'column-'+(i+1),
          	        typeAhead: true,
          	        triggerAction: 'all',
          	        emptyText:'...',
          	        selectOnFocus:true,
          	        mode: 'local'
          	    });	 
      			
      	
      		  this.addText('header');
      			this.addField(cb);	
    			}
        }
        else{
          //'custom' type
      		for(var i=0, l=this.filterBar.filters.length; i<l; i++) {
      		
      			cb = new Ext.form.ComboBox({
          	        store: this.store,
          	        width: 100,
          	        displayField:'column-'+(i+1),
          	        valueField:'column-'+(i+1),
          	        typeAhead: true,
          	        triggerAction: 'all',
          	        emptyText:'...',
          	        selectOnFocus:true,
          	        mode: 'local'
          	    });	 
      			
      			this.addText(this.filterBar.filters[i].text);
      			this.addField(cb);	    	
      		}
		} //for
		//calls superclass for define buttons
		Sbi.console.CustomFilteringToolbar.superclass.onRender.call(this, ct, position);
		
    } 
   
    
    
    // -- private methods ---------------------------------------------------------------
    
});