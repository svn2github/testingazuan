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

Ext.ns("Sbi.qbe");

Sbi.qbe.CrosstabDefinitionPanel = function(config) {
	
	var defaultSettings = {
		title: LN('sbi.qbe.crosstabDefinitionPanel.title')
  	};
	if(Sbi.settings && Sbi.settings.qbe && Sbi.settings.qbe.crosstabDefinitionPanel) {
		defaultSettings = Ext.apply(defaultSettings, Sbi.settings.qbe.crosstabDefinitionPanel);
	}
	
	var c = Ext.apply(defaultSettings, config || {});
	
	Ext.apply(this, c);
	
	this.init(c);
	
	c = Ext.apply(c, {
      	items: [this.crosstabDefinitionPanel]
      	, autoScroll: true
	});
	
	// constructor
    Sbi.qbe.CrosstabDefinitionPanel.superclass.constructor.call(this, c);
    
};

Ext.extend(Sbi.qbe.CrosstabDefinitionPanel, Ext.Panel, {
	
	crosstabDefinitionPanel: null
	, columnsContainerPanel: null
	, rowsContainerPanel: null
	
	, init: function(c) {
	
		this.columnsContainerPanel = new Sbi.qbe.FieldsContainerPanel({
            title: LN('sbi.qbe.crosstabDefinitionPanel.columns')
            , width: 400
		});
		
		this.rowsContainerPanel = new Sbi.qbe.FieldsContainerPanel({
            title: LN('sbi.qbe.crosstabDefinitionPanel.rows')
            , width: 200
		});
	
	    this.crosstabDefinitionPanel = new Ext.Panel({
	        layout: 'table'
	        , baseCls:'x-plain'
	        , padding: '30 30 30 100'
	        , layoutConfig: {columns:2}
	        // applied to child components
	        , defaults: {height: 150}
	        , items:[
	            {
		        	border: false
		        }
		        , this.columnsContainerPanel
		        , this.rowsContainerPanel
		        , {
		            header: false
		            , width: 400
	        }]
	    });
	
	}
	
});