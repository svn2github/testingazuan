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

Ext.ns("Sbi.formbuilder");

Sbi.formbuilder.FormPanel = function(config) {
	
	var defaultSettings = {
		title: 'Form builder'
	};
		
	if(Sbi.settings && Sbi.settings.formbuilder && Sbi.settings.formbuilder.formPanel) {
		defaultSettings = Ext.apply(defaultSettings, Sbi.settings.formbuilder.formPanel);
	}
		
	var c = Ext.apply(defaultSettings, config || {});
		
	Ext.apply(this, c);
	
	this.formBuilderPage = new Sbi.formbuilder.FormBuilderPage({closable: false, template: config.template});
	this.formPreviewPage = new Sbi.formbuilder.FormPreviewPage({closable: false});
	
	c = Ext.apply(c, {
		border: false
		, tabPosition: 'bottom'
  		, activeTab: 0
      	, items: [this.formBuilderPage, this.formPreviewPage]
	});

	// constructor
	Sbi.formbuilder.FormPanel.superclass.constructor.call(this, c);
};

Ext.extend(Sbi.formbuilder.FormPanel, Ext.TabPanel, {
    
});