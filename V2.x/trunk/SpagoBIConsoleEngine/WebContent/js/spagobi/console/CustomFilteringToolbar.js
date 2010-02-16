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

Sbi.console.CustomFilteringToolbar = function(config, store) {
	alert("store: "+ store.toSource());
		var defaultSettings = {
		    // default goes here
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
		alert("onRender");
  		var s, cb;
  		
  	alert("type2: "+ config.defaults.type.toSource());
  		
  		if (this.defaults.type === 'automatic' ){
  		/*
      		s = new Ext.data.ArrayStore({
                          fields: ['field', 'value', 'label']
                          , data : [
                                   ['FieldA','valA1' + i, 'Value A 1' + i]
                                 , ['FieldA','valA2' + i, 'Value A 2' + i]
                                 , ['FieldA','valA3' + i, 'Value A 3' + i]
                                 , ['FieldA','valA4' + i, 'Value A 4' + i]
                                 , ['FieldA','valA5' + i, 'Value A 5' + i]
                                 , ['FieldB','valB1' + i, 'Value B 1' + i]
                                 , ['FieldB','valB2' + i, 'Value B 2' + i]
                                 , ['FieldB','valB3' + i, 'Value B 3' + i]
                                 , ['FieldB','valB4' + i, 'Value B 4' + i]
                                 , ['FieldC','valC1' + i, 'Value C 1' + i]
                                 , ['FieldC','valC2' + i, 'Value C 2' + i]
                                 , ['FieldC','valC3' + i, 'Value C 3' + i]
                                 , ['FieldD','valD1' + i, 'Value D 1' + i]
                                 , ['FieldD','valD2' + i, 'Value D 2' + i]                              
                            ]
      		});
      		*/
      		
  				cb = new Ext.form.ComboBox({
        	        store: this.store,
        	        width: 100,
        	        displayField:'label',
        	        valueField:'value',
        	        typeAhead: true,
        	        triggerAction: 'all',
        	        emptyText:'...',
        	        selectOnFocus:true,
        	        mode: 'local'
        	    });	 
    			
    		
    		//	this.addText(s.data.field);
    			this.addField(cb);	
        }
        else{
      		for(var i=0, l=this.filters.length; i<l; i++) {
      		alert(this.filters[i].text.toSource());
      		/*
      			s = new Ext.data.ArrayStore({
                      fields: ['value', 'label']
                      , data : [
                               ['valA1' + i, 'Value A 1' + i]
                             , ['valA2' + i, 'Value A 2' + i]
                             , ['valA3' + i, 'Value A 3' + i]
                             , ['valA4' + i, 'Value A 4' + i]
                             , ['valA5' + i, 'Value A 5' + i]        
                        ]
      			});
          */
      			cb = new Ext.form.ComboBox({
          	        store: this.store,
          	        width: 100,
          	        displayField:'column-1',
          	        valueField:'column-1',
          	        typeAhead: true,
          	        triggerAction: 'all',
          	        emptyText:'...',
          	        selectOnFocus:true,
          	        mode: 'local'
          	    });	 
      			
      			this.addText(this.filters[i].text);
      			this.addField(cb);	    	
      		}
    		} //for
    		//calls superclass for define buttons
    		Sbi.console.CustomFilteringToolbar.superclass.onRender.call(this, ct, position);
		
    } 
   
    
    
    // -- private methods ---------------------------------------------------------------
    
});