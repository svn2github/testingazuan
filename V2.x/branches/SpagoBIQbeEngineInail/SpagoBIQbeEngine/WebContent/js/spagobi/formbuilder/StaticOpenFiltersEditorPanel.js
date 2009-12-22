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

Sbi.formbuilder.StaticOpenFiltersEditorPanel = function(openFilters, config) {
	
	var defaultSettings = {
		// set default values here
		title: 'Static open filters'
        , layout: 'column'
		, frame: true
		, autoScroll: true
		, autoWidth: true
		, autoHeight: true
	};
	if (Sbi.settings && Sbi.settings.formbuilder && Sbi.settings.formbuilder.staticOpenFiltersEditorPanel) {
		defaultSettings = Ext.apply(defaultSettings, Sbi.settings.formbuilder.staticOpenFiltersEditorPanel);
	}
	
	var c = Ext.apply(defaultSettings, config || {});
	this.baseConfig = c;
	
	this.init(openFilters);
	
	Ext.apply(c, {
		tools: this.tools
  		, items: this.contents
	});
	
	// constructor
    Sbi.formbuilder.StaticOpenFiltersEditorPanel.superclass.constructor.call(this, c);
    
    this.on('render', this.initDD, this);
    
    //this.doLayout();
    
};

Ext.extend(Sbi.formbuilder.StaticOpenFiltersEditorPanel, Ext.Panel, {

	contents: null
	, empty: null
	, emptyMsgPanel: null
	
	, loadContents: function(staticFiltersConf) {
		Sbi.qbe.commons.Utils.unimplementedFunction('loadContents');
	}
	
	, initDD: function() {
		this.removeListener('render', this.initDD, this);
		this.dropTarget = new Sbi.formbuilder.StaticOpenFiltersEditorPanelDropTarget(this);
	}

	, init: function(openFiltersConf) {
		if(openFiltersConf !== undefined) {
			this.loadContents(openFiltersConf);	
		} else {
			this.initEmptyMsgPanel();
			this.initTools();
			this.contents = [this.emptyMsgPanel];
		}
	}
	
	, initEmptyMsgPanel: function() {
		this.empty = true;
		this.emptyMsgPanel = new Ext.Panel({
			html: 'drag a field here to create a new static open filter'
		});
	}
	
	, initTools: function() {
		this.tools = [];
		this.tools.push({
		    id:'delete',
		    qtip: 'clear all',
		    handler: function(event, toolEl, panel){
		  		this.clearContents();
		    }
		    , scope: this
		});
	}
	
	, clearContents: function() {
		Sbi.qbe.commons.Utils.unimplementedFunction('clearContents');
	}
	
	, addFilter: function(openFilter) {
		this.remove(this.emptyMsgPanel);
		var aStaticOpenFilterEditorPanel = new Sbi.formbuilder.StaticOpenFilterEditorPanel(openFilter);
		this.add(aStaticOpenFilterEditorPanel);
		this.doLayout();
	}
	
});

/*
Ext.extend(Sbi.formbuilder.StaticOpenFiltersEditorPanel, Ext.Panel, {
    
	services: null
	, contents: null
	, empty: null
	, emptyMsgPanel: null
	, tools: null
	
	
	// --------------------------------------------------------------------------------
	// public methods
	// --------------------------------------------------------------------------------
		
	, loadContents: function(staticFiltersConf) {
		Sbi.qbe.commons.Utils.unimplementedFunction('loadContents');
		this.initEmptyMsgPanel();
		this.initTools();
		this.contents = [this.emptyMsgPanel];	
	}

	, clearContents: function() {
		Sbi.qbe.commons.Utils.unimplementedFunction('clearContents');
	}
	
	, addStaticClosedXORFilters: function(staticFiltersGroupConf) {
		if(this.empty === true) {
			this.remove(0, true);
			this.doLayout();
			this.empty = false;
		}
				
		var staticFiltersForm = new Sbi.formbuilder.StaticClosedXORFiltersEditorPanel(staticFiltersGroupConf);		
		this.add(staticFiltersForm);
		this.doLayout();	
	}

	// --------------------------------------------------------------------------------
	// private methods
	// --------------------------------------------------------------------------------
	
	, init: function(staticFiltersConf) {
		if(staticFiltersConf !== undefined) {
			this.loadContents(staticFiltersConf);	
		} else {
			this.initEmptyMsgPanel();
			this.initTools();
			this.contents = [this.emptyMsgPanel];
		}
	}
	
	, initEmptyMsgPanel: function() {
		this.empty = true;
		this.emptyMsgPanel = new Ext.Panel({
			html: 'drag a field here to create a new static filter'
		});
	}

	, initTools: function() {
		this.tools = [];
		
		this.tools.push({
		    id:'plus',
		    qtip: 'Add static closed filter',
		    handler: function(event, toolEl, panel){
		  		this.addStaticClosedXORFilters();
		    }
		    , scope: this
		});
		
		this.tools.push({
		    id:'delete',
		    qtip: 'clear all',
		    handler: function(event, toolEl, panel){
		  		this.clearContents();
		    }
		    , scope: this
		});
	}
	
	
	
	

	   
	
  	
});
*/