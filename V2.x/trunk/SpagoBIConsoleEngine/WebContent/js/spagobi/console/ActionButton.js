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

Sbi.console.ActionButton = function(config) {
	 
		var defaultSettings = {
			//title: LN('sbi.console.title')
			scope:this
		};
		
		if(Sbi.settings && Sbi.settings.console && Sbi.settings.console.actionButton) {
			defaultSettings = Ext.apply(defaultSettings, Sbi.settings.console.actionButton);
		}
		
		var c = Ext.apply(defaultSettings, config || {});
		
		Ext.apply(this, c);
		
	
		this.services = this.services || new Array();	
		this.services['REFRESH'] = this.services['REFRESH'] || Sbi.config.serviceRegistry.getServiceUrl({
			serviceName: 'REFRESH_ACTION'
			, baseParams: new Object()
		});
			this.services['ERRORS'] = this.services['ERRORS'] || Sbi.config.serviceRegistry.getServiceUrl({
			serviceName: 'ERRORS_ACTION'
			, baseParams: new Object()
		});
			this.services['WARNINGS'] = this.services['WARNINGS'] || Sbi.config.serviceRegistry.getServiceUrl({
			serviceName: 'WARNINGS_ACTION'
			, baseParams: new Object()
		});
			this.services['VIEWS'] = this.services['VIEWS'] || Sbi.config.serviceRegistry.getServiceUrl({
			serviceName: 'VIEWS_ACTION'
			, baseParams: new Object()
		});
		
		//this.addEvents('customEvents');
		

	//	this.initButton(c || {});
		
      c = Ext.apply(c, this);

		// constructor
		Sbi.console.ActionButton.superclass.constructor.call(this, c);
		this.on('click', this.execAction, this);
		//this.addEvents();
};

Ext.extend(Sbi.console.ActionButton, Ext.Button, {
    
    services: null
    
   
    // public methods
    , execAction: function(){
      Ext.Ajax.request({
			        url: this.services[this.name],
			        params: {'message': this.name, 'userId': Sbi.user.userId},
			        callback : function(options , success, response) {
			  	  		if (success) {
				      		if(response !== undefined && response.responseText !== undefined) {
				      			var content = Ext.util.JSON.decode( response.responseText );
				      			if (content !== undefined) {				      			  
				      				alert(content.toSource());
				      			}				      		
				      		} else {
				      			Sbi.exception.ExceptionHandler.showErrorMessage('Server response is empty', 'Service Error');
				      		}
			  	  		} else { 
			  	  			Sbi.exception.ExceptionHandler.showErrorMessage('Cannot exec action: ' + this.name, 'Service Error');
			  	  		}
			        },
			        scope: this,
					failure: Sbi.exception.ExceptionHandler.handleFailure      
				});
			}
   
    
    
    // private methods
  
});
    
