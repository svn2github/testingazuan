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
	
		c = Ext.apply(c, {items:this.customFilterBar});

		// constructor
		Sbi.console.CustomFilteringToolbar.superclass.constructor.call(this, c);
		
		//adds events		
		this.store.on('metachange', this.onMetaChange, this);
		
};

Ext.extend(Sbi.console.CustomFilteringToolbar, Sbi.console.FilteringToolbar, {  
    services: null
    , customFilterBar: null
    , txtField: null
   // , headers: null
   

    // -- public methods ---------------------------------------------------------------
    , onRender : function(ct, position) {
  		
  		this.txtField = new Ext.form.TextField({fieldLabel: 'Loading...'});  		
		Sbi.console.CustomFilteringToolbar.superclass.onRender.call(this, ct, position);

    }
   
    
    
    // -- private methods ---------------------------------------------------------------
    , onMetaChange: function( store, meta ) {

       this.cleanFilterToolbar();
      
       //loads headers map with dataIndex info
       //this.headers = [];
       
       for(var i = 0; i < meta.fields.length; i++) {
          var localHeader = meta.fields[i].header;
          var localDataIndex = meta.fields[i].dataIndex;
      /*    if (localHeader != ''){
            this.headers[localHeader] = localDataIndex;
          }*/
       } 
      
       	if (this.filterBar.type === 'automatic' ){
       	  // automatic: all dataset fields are added as filter
      		for(var i = 0; i < meta.fields.length; i++) { 		  
      		  if (meta.fields[i].header !== undefined && meta.fields[i].header !== ''){   
          	   //this.createFilterField(this.filterBar.defaults.operator,  meta.fields[i].header, this.headers[meta.fields[i].header]);
          	   this.createFilterField(this.filterBar.defaults.operator,  meta.fields[i].header, store.getFieldNameByAlias(meta.fields[i].header));
                
      		  }
      		} 
      	}
      	else{
      	 //custom: only configurated fields are added as filter      	 
        	for(var i = 0; i < meta.fields.length; i++) { 		           
        		 if (meta.fields[i].header !== undefined &&  meta.fields[i].header !== '' && this.isConfiguratedFilter(meta.fields[i].header)){         		   
                  //this.createFilterField(this.getFilterOperator(meta.fields[i].header), this.getColumnText(meta.fields[i].header),  this.headers[meta.fields[i].header]);  	
                  this.createFilterField(this.getFilterOperator(meta.fields[i].header), this.getColumnText(meta.fields[i].header),  store.getFieldNameByAlias(meta.fields[i].header));  	                  
            	}        		  
        	} 
      	}

    		//adds actions
    		this.addActionButtons();    		
    		this.doLayout();
      	
    	}
	
    //returns true if the input field is a filter defined into template, false otherwise.
    , isConfiguratedFilter: function (field){   
          if (this.filterBar.filters){    
            for(var i=0, l=this.filterBar.filters.length; i<l; i++) {              
              if (field === this.filterBar.filters[i].column)
                return true;
        		}
        	}
          return false;
    }
    
    , getColumnText: function (columnName){  
        if (this.filterBar.filters){       
          for(var i=0, l=this.filterBar.filters.length; i<l; i++) {              
            if (columnName === this.filterBar.filters[i].column)
              return this.filterBar.filters[i].text;
      		}
      	}
        return columnName;
    }
   
    , getFilterOperator: function (columnName){         
      	for(var i=0, l=this.filterBar.filters.length; i<l; i++) {              
            if (columnName === this.filterBar.filters[i].column)
              return this.filterBar.filters[i].operator;
  		}
      	return null;
    }
    
});