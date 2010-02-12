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
  * - Antonella Giachino (antonella.giachino@eng.it)
  */

Ext.ns("Sbi.console");

Sbi.console.FilteringToolbar = function(config) {
	
	var defaultSettings = {
	    autoWidth: true
	  , width:'100%'
	};
		
	if(Sbi.settings && Sbi.settings.console && Sbi.settings.console.filteringToolbar) {
		defaultSettings = Ext.apply(defaultSettings, Sbi.settings.console.filteringToolbar);
	}
	
	var c = Ext.apply(defaultSettings, config || {});
	
	Ext.apply(this, c);
	
	/*
	this.services = this.services || new Array();	
	this.services['doThat'] = this.services['doThat'] || Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'DO_THAT_ACTION'
		, baseParams: new Object()
	});
	*/
	
	/*
	if (c.type === 'default'){
		this.initDefaultFilters(c.filters || {});
    } else if (c.type === 'custom'){
    	this.toolbarElements = new Sbi.console.CustomFilteringToolbar(c);
    } else if (c.type === 'automatic'){
    	this.initAutomaticFilters(c.filters || {});
    }
	*/
	
	//adds general action buttons
	//this.initToolbarActions(c.actions || {});
	
	c = Ext.apply(c, {});

	// constructor
	Sbi.console.FilteringToolbar.superclass.constructor.call(this, c);
    
	//	this.addEvents();
};

Ext.extend(Sbi.console.FilteringToolbar, Ext.Toolbar, {
    
	services: null
	
	// -- public methods -----------------------------------------------------------------
    
   
    
    
	// -- private methods ---------------------------------------------------------------
  
	, onRender : function(ct, position) { 
	  
		Sbi.console.FilteringToolbar.superclass.onRender.call(this, ct, position);
		
		//alert('filtering IN');
		
		var b;
		
		this.addFill();
		for(var i=0; i < this.actions.length; i++){
			b = new Sbi.console.ActionButton(this.actions[i]);
    		this.addButton(b);	
    	}	
		
		//alert('filtering OUT');
	}
  
  	
  
  
  
  
  
  
  
/*	
    ,initDefaultFilters: function(filtersConf) {
        this.toolbarElements = [];
      	for(var i=0; i < filtersConf.length; i++){
    		  this.toolbarElements.push({
    				 text: filtersConf[i].text
    				,tooltip: filtersConf[i].text
    				,scope: this
    			});        			
      	}
	}
	  
	  
    ,initAutomaticFilters: function(filtersConf) {
       this.toolbarElements = [];
       alert('initAutomaticFilters...working in progress...');
	}
	
	
	,initToolbarActions: function(actions) {
  	   if(this.toolbarElements === null){
  	     this.toolbarElements = [];
  	   }
       
  	   this.toolbarElements.add(new Ext.Toolbar.Spacer({width:1000}));
  	   	for(var i=0; i < actions.length; i++){
  	   		//this.toolbarElements.push(new Sbi.console.ActionButton(actions[i]));
    		this.toolbarElements.add(new Sbi.console.ActionButton(actions[i]));
    	}
    
    }
	*/
    
});