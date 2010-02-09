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
  * 
  * - Andrea Gioia (andrea.gioia@eng.it)
  * - Antonella Giachino (antonella.giachino@eng.it)
  */

Ext.ns("Sbi.console");

Sbi.console.DetailPage = function(config) {
	
		var defaultSettings = {
			title: 'DetailPage'
		};
		
		if(Sbi.settings && Sbi.settings.console && Sbi.settings.console.detailPage) {
			defaultSettings = Ext.apply(defaultSettings, Sbi.settings.console.detailPage);
		}
		
		var c = Ext.apply(defaultSettings, config || {});
		
		Ext.apply(this, c);
		
		this.services = this.services || new Array();		
		this.services['getDataset'] = this.services['getDataset'] || Sbi.config.serviceRegistry.getServiceUrl({
			serviceName: 'TEST_DATASET_ACTION'
			, baseParams: new Object()
		});
		this.initDetailPage(c.detailPageConfig || {});
		
		c = Ext.apply(c, {  	
			//html: this.msg
	      	items: [this.getDSButton]
		});

		// constructor
		Sbi.console.DetailPage.superclass.constructor.call(this, c);
    
};

Ext.extend(Sbi.console.DetailPage, Ext.Panel, {
    
    //services: null
    
   
    // public methods
    
   
    
    
    // private methods
     initDetailPage: function(conf) {
		this.getDSButton = new Ext.Button({
			  text: 'Get Dataset'
			, handler: function() { 

				
				Ext.Ajax.request({
			        url: this.services['getDataset'],
			        params: {ds_label: 'testmeter'},
			        callback : function(options , success, response) {
			  	  		if (success) {
				      		if(response !== undefined && response.responseText !== undefined) {
				      			var content = Ext.util.JSON.decode( response.responseText );
				      			if (content !== undefined) {				      			  
				      				alert(content);
				      			}				      		
				      		} else {
				      			Sbi.exception.ExceptionHandler.showErrorMessage('Server response is empty', 'Service Error');
				      		}
			  	  		} else { 
			  	  			Sbi.exception.ExceptionHandler.showErrorMessage('Cannot load dataset', 'Service Error');
			  	  		}
			        },
			        scope: this,
					failure: Sbi.exception.ExceptionHandler.handleFailure      
				});
			},
			scope: this
		});
	}
    
    
    
});