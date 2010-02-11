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
		    
		};
		
		if(Sbi.settings && Sbi.settings.console && Sbi.settings.console.customFilteringToolbar) {
			defaultSettings = Ext.apply(defaultSettings, Sbi.settings.console.customFilteringToolbar);
		}
		
		var c = Ext.apply(defaultSettings, config || {});
		
		Ext.apply(this, c);
		
		/*
		this.services = this.services || new Array();	
		this.services['doThat'] = this.services['doThat'] || Sbi.config.serviceRegistry.getServiceUrl({
			serviceName: 'DO_THAT_ACTION'
			, baseParams: new Object()
		});
		
		this.addEvents('customEvents');
		*/
		this.initFilters(c || {});
	
		c = Ext.apply(c, {items:this.customFilterBar});

		// constructor
		Sbi.console.CustomFilteringToolbar.superclass.constructor.call(this, c);
    
	//	this.addEvents();
};

//Ext.extend(Sbi.console.CustomFilteringToolbar, Sbi.console.FilteringToolbar, {
Ext.extend(Sbi.console.CustomFilteringToolbar, Ext.Toolbar, {  
    services: null
    , customFilterBar: null
   

    // public methods
    /*
    , onRender : function(ct, position) {
      alert('sono in onRender!' );
	   //  alert('sono in onRender! ct: ' + ct + ' - position: ' + position);
    		Sbi.console.CustomFilteringToolbar.superclass.onRender.call(this, ct, position);
    	    
    		this.addText('aaa');	
    		this.addSpacer();
    		
    		
    	    this.columnNameStore = new Ext.data.SimpleStore({
    	        fields: ['value', 'label'],
    	        data : []
    	    });	 	    
    	    this.columnNameCombo = new Ext.form.ComboBox({
    	        store: this.columnNameStore,
    	        width: 100,
    	        displayField:'label',
    	        valueField:'value',
    	        typeAhead: true,
    	        triggerAction: 'all',
    	        emptyText:'...',
    	        selectOnFocus:true,
    	        mode: 'local'
    	    });	    
    	    this.addField( this.columnNameCombo );	    
    	    this.addSpacer();
     } 
   */
    
    
    // private methods
    ,initFilters: function(c) {
    
          this.customFilterBar = [];
        		
         // for(var i=0; i < c.filters.length; i++){
   		   for(var i=0; i < 2; i++){
        	    var columnNameStore = new Ext.data.ArrayStore({
                      autoDestroy: true
                      ,fields: ['value', 'label']
                      ,data : [
                               ['val1' + i, 'Value 1' + i]
                             , ['val2' + i, 'Value 2' + i]
                             , ['val3' + i, 'Value 3' + i]
                             , ['val4' + i, 'Value 4' + i]
                             , ['val5' + i, 'Value 5' + i]                              
                             ]
                          });	 	  
             
    	        this.customFilterBar.push(c.filters[i].text);	
    		    //  this.customFilterBar.push('  ');
        	    
              var columnNameCombo = new Ext.form.ComboBox({
        	        store: columnNameStore,
        	        width: 100,
        	        displayField:'label',
        	        valueField:'value',
        	        typeAhead: true,
        	        triggerAction: 'all',
        	        emptyText:'...',
        	        selectOnFocus:true,
        	        mode: 'local'
        	    });	    
        	    
    
        	    this.customFilterBar.push(columnNameCombo);     
        	    //this.customFilterBar.push(addSpacer());
        	}
    	    
    }
    
});