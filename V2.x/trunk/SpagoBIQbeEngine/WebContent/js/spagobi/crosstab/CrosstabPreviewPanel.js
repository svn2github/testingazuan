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
  * - Davide Zerbetto (davide.zerbetto@eng.it)
  */

Ext.ns("Sbi.crosstab");

Sbi.crosstab.CrosstabPreviewPanel = function(config) {
	
	var defaultSettings = {
		title: LN('sbi.crosstab.crosstabpreviewpanel.title')
  	};
	if(Sbi.settings && Sbi.settings.qbe && Sbi.settings.qbe.crosstabPreviewPanel) {
		defaultSettings = Ext.apply(defaultSettings, Sbi.settings.qbe.crosstabPreviewPanel);
	}
	
	this.services = new Array();
	var params = {};
	this.services['loadCrosstab'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'LOAD_CROSSTAB_ACTION'
		, baseParams: params
	});
	
	var c = Ext.apply(defaultSettings, config || {
		html: "ciao davide"
	});
	
	this.initStore();
	
	// constructor
    Sbi.crosstab.CrosstabPreviewPanel.superclass.constructor.call(this, c);
	
};

Ext.extend(Sbi.crosstab.CrosstabPreviewPanel, Ext.Panel, {
	
	services: null
	, proxy: null
	, store: null
	
	, load: function(crosstabDefinition) {
		this.store.removeAll();
		var params = {
				crosstabDefinition: Ext.util.JSON.encode(crosstabDefinition)
		};
		this.store.load({params: params});
	}

	, initStore: function() {
		
		this.proxy = new Ext.data.HttpProxy({
	           url: this.services['loadCrosstab']
	   		   , failure: Sbi.exception.ExceptionHandler.handleFailure
	    });
		
		this.store = new Ext.data.Store({
	        proxy: this.proxy,
	        reader: new Ext.data.JsonReader(),
	        remoteSort: true
	    });
		
	}
	
});