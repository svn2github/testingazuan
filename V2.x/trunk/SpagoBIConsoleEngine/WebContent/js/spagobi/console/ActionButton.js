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
			//title: LN('sbi.qbe.queryeditor.title')
		};
		
		if(Sbi.settings && Sbi.settings.console && Sbi.settings.console.actionButton) {
			defaultSettings = Ext.apply(defaultSettings, Sbi.settings.console.actionButton);
		}
		
		var c = Ext.apply(defaultSettings, config || {});
		
		Ext.apply(this, c);
		
	
		this.services = this.services || new Array();	
		this.services['REFRESH'] = this.services['REFRESH'] || Sbi.config.serviceRegistry.getServiceUrl({
			serviceName: 'REFRESH'
			, baseParams: new Object()
		});
			this.services['ERRORS'] = this.services['ERRORS'] || Sbi.config.serviceRegistry.getServiceUrl({
			serviceName: 'ERRORS'
			, baseParams: new Object()
		});
			this.services['WARNINGS'] = this.services['WARNINGS'] || Sbi.config.serviceRegistry.getServiceUrl({
			serviceName: 'WARNINGS'
			, baseParams: new Object()
		});
			this.services['VIEWS'] = this.services['VIEWS'] || Sbi.config.serviceRegistry.getServiceUrl({
			serviceName: 'VIEWS'
			, baseParams: new Object()
		});
		
		//this.addEvents('customEvents');
		
		
		this.initButton(c.actionConfig || {});
		
	
		c = Ext.apply(c, this.actionBtn);

		// constructor
		Sbi.console.ActionButton.superclass.constructor.call(this, c);
    
		//this.addEvents();
};

Ext.extend(Sbi.console.ActionButton, Ext.Button, {
    
    services: null
    , actionBtn: null
    
   
    // public methods
    
   
    
    
    // private methods
    , initButton: function(actionsConf){
        
        //actionsConf={name: 'REFRESH', hidden:false}
    	  for(p in actionsConf) {
			     alert(p.toSource() + " - " +  actionsConf[p].toSource() );
          this.actionBtn = new Ext.Button({text: 'ActionBtn 1'
                                           ,handler: function(){
                                                alert('Hai pigiato il primo ActionButton...'+ p.toSource());
                                              }
                                           ,scope: this
                                          });
        }
    }
});
    
