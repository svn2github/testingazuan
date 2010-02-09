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
  * - name (mail)
  */

Ext.ns("Sbi.console");

Sbi.console.NavigationToolbar = function(config) {
	
		var defaultSettings = {
			//title: LN('sbi.qbe.queryeditor.title')
		};
		
		if(Sbi.settings && Sbi.settings.console && Sbi.settings.console.navigationToolbar) {
			defaultSettings = Ext.apply(defaultSettings, Sbi.settings.console.navigationToolbar);
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
		
		this.initToolbarButtons(c.documents || {});
	
		c = Ext.apply(c, {  	
	      	items: this.toolbarButtons
		});

		// constructor
		Sbi.console.NavigationToolbar.superclass.constructor.call(this, c);
    
	//	this.addEvents();
};

Ext.extend(Sbi.console.NavigationToolbar, Ext.Toolbar, {
    
    services: null
    , toolbarButtons: null
    
   
    // public methods
    
   
    
    
    // private methods
    ,initToolbarButtons: function(documents) {
		
		this.toolbarButtons = [];
		for(var i=0; i < documents.length; i++){
			this.toolbarButtons.push({
				 text: documents[i].text
				,tooltip: documents[i].tooltip
				,documentConf: documents[i] 
				,handler: function execCrossNavigation (b){
					var msg = {
						label: b.documentConf.label
						,windowName: this.name										
					};
					if(b.documentConf.staticParams) {
						msg.parameters = '';
						var separator = '';
						for(p in b.documentConf.staticParams) {
							msg.parameters += separator + p + '=' + b.documentConf.staticParams[p];
							separator = '&';
						}
						alert("msg.parameters: " + msg.parameters.toSource());
					}
					
					sendMessage(msg, 'crossnavigation');
				}
				,scope: this
			});
		}
	}
    
    
    
});