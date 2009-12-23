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

Ext.ns("Sbi.formbuilder");

Sbi.formbuilder.StaticClosedFiltersEditorPanel = function(config) {
	
	var defaultSettings = {
		// set default values here
		title: 'Static closed filters'
		, layout: 'table'
	    , layoutConfig: {
	        columns: 100
	    }
		, frame: true
		, autoScroll: true
		, autoWidth: true
		, autoHeight: true
	};
	if (Sbi.settings && Sbi.settings.formbuilder && Sbi.settings.formbuilder.staticClosedFiltersEditorPanel) {
		defaultSettings = Ext.apply(defaultSettings, Sbi.settings.formbuilder.staticClosedFiltersEditorPanel);
	}
	var c = Ext.apply(defaultSettings, config || {});
	
	this.init();
	this.initTools();
	
	Ext.apply(c, {
		tools: this.tools,
  		items: this.contents
	});
	
	// constructor
    Sbi.formbuilder.StaticClosedFiltersEditorPanel.superclass.constructor.call(this, c);
    
    this.doLayout();
};

Ext.extend(Sbi.formbuilder.StaticClosedFiltersEditorPanel, Ext.Panel, {
    
	services: null
	, contents: null
	, empty: null
	, emptyMsgPanel: null
	, tools: null
	, filterGroupWizard: null
	
	
	// --------------------------------------------------------------------------------
	// public methods
	// --------------------------------------------------------------------------------
		
	, loadContents: function(staticFiltersConf) {
		Sbi.qbe.commons.Utils.unimplementedFunction('loadContents');
		this.initEmptyMsgPanel();
		this.contents = [this.emptyMsgPanel];	
	}

	, clearContents: function() {
		if(this.empty === false) {		
			this.reset();
		}
	}
	
	, addFiltersGroup: function(filtersGroupConf) {
		if(this.empty === true) {
			this.reset();
			this.empty = false;
			this.contents = [];
		}
				
		var filtersGroup = new Sbi.formbuilder.StaticClosedXORFiltersEditorPanel(filtersGroupConf);		
		filtersGroup.on('destroy', this.onFiltersGroupDestroy, this);
		
		this.contents.push(filtersGroup);
		this.add(filtersGroup);
		this.doLayout();	
		
	
		
	}

	// --------------------------------------------------------------------------------
	// private methods
	// --------------------------------------------------------------------------------
	
	, reset: function() {		
		if(this.contents && this.contents.length) {
			for(var i = this.contents.length-1; i >= 0; i--) {
				// beware: "remove" fire destroy event that is catched above here. the callback modify the length of contents) 
				this.remove(this.contents[i], true);
			}
		}
	}
	
	, init: function() {
		if(this.baseStaticFiltersConf !== undefined) {
			this.loadContents(baseStaticFiltersConf);	
		} else {
			this.initEmptyMsgPanel();
			this.contents = [this.emptyMsgPanel];
		}
	}	
	
	, initEmptyMsgPanel: function() {
		this.empty = true;
		this.emptyMsgPanel = new Ext.Panel({
			html: 'Click on the button in the top-rigtht corner in order to add a new filter group'
		});
	}

	, initTools: function() {
		this.tools = [];
		
		this.tools.push({
		    id:'plus',
		    qtip: 'Add static closed filter',
		    handler: function(event, toolEl, panel){
		  		this.onFiltersGroupWizardShow();
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
	
	
	, onFiltersGroupWizardShow: function(targetFilterGroup) {
		if(this.filterGroupWizard === null) {
			this.filterGroupWizard = new Sbi.formbuilder.StaticClosedXORFiltersWizard();
			this.filterGroupWizard.on('apply', function(win, target, state) {
				//alert(state.toSource());
				if(target === null) {
					this.addFiltersGroup(state);
				} else {
					alert('edit');
				}
				
			}, this);
		}
		
		this.filterGroupWizard.setTarget(targetFilterGroup || null);		
		this.filterGroupWizard.show();
	}
	
	, onFiltersGroupDestroy: function(filtersGroup) {
		var t = this.contents;
		this.contents = [];
		for(var i = 0; i < t.length; i++) {
			if(filtersGroup.id !== t[i].id) {
				this.contents.push(t[i]);
			}
		}
		if(this.contents.length === 0) {
		
			this.initEmptyMsgPanel();
			
			this.add(this.emptyMsgPanel);
			this.contents = [this.emptyMsgPanel];
			this.doLayout();
		}
	}

	   
	
  	
});