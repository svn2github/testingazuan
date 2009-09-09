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
  * - Andrea Gioia (andrea.gioia@eng.it)
  */

Ext.ns("Sbi.qbe");

Sbi.qbe.QbePanel = function(config) {
	
	var c = Ext.apply({
		// set default values here
	}, config || {});
	
	this.addEvents();
	
	this.queryResultPanel = new Sbi.widgets.DataStorePanel(c);
	
	this.items = null;
	this.queryEditorPanel = null;
	
	if (Sbi.user.isPowerUser === true) {
		this.queryEditorPanel = new Sbi.qbe.QueryBuilderPanel(c);
		this.items = [this.queryEditorPanel, this.queryResultPanel];
	} else {
		this.items = [this.queryResultPanel];
	}
	
	this.tabs = new Ext.TabPanel({
		border: false,
  		activeTab: 0,//(Sbi.user.isPowerUser === false) ? 1 : 0,
  		items: this.items
	});
	
	if (this.queryEditorPanel != null) {
		this.queryEditorPanel.on('execute', function(editorPanel, query){
			//alert('execution time baby');
			this.tabs.activate(this.queryResultPanel);
			this.queryResultPanel.execQuery(query);
		}, this);
	}
	
	c = Ext.apply(c, {
		layout: 'fit',
		autoScroll: true, 
  		margins:'0 4 4 0',
  		items: [this.tabs] 
	});
	
	// constructor
    Sbi.qbe.QbePanel.superclass.constructor.call(this, c);

    // the this.loadingMask object is a work-around: when user in not a power user,
    // query is executed immediately but the loading mask is not shown, maybe because the body of the 
    // grid of the queryResultPanel is empty
    this.loadingMask = new Ext.LoadMask(Ext.getBody(), {msg:"Loading..."});
    this.hideLoadingMask = function () {
    	this.loadingMask.hide();
    	this.queryResultPanel.store.un('load', this.hideLoadingMask, this);
    }
    
	if (Sbi.user.isPowerUser === false) {
		this.loadingMask.show();
		this.queryResultPanel.store.on('load', this.hideLoadingMask, this);
		this.queryResultPanel.execQuery(null);
	}
    
};

Ext.extend(Sbi.qbe.QbePanel, Ext.Panel, {
    
    services: null
    , queryResultPanel: null
    , queryEditorPanel: null
    , tabs: null
    , query: null
   
   
    // public methods
    
    , setQuery: function(q) {
    	query = q;
    }
    
  
    
    // private methods
});