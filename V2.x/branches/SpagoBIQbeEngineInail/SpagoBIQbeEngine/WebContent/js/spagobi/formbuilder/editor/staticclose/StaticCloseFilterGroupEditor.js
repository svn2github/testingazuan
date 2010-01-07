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

Ext.ns("Sbi.formviewer");

Sbi.formbuilder.StaticCloseFilterGroupEditor = function(config) {
	

	var defaultSettings = {		
		width: 300
        , height: 150
        , autoWidth: false
		
	};
	if (Sbi.settings && Sbi.settings.formbuilder && Sbi.settings.formbuilder.staticCloseFilterGroupEditor) {
		defaultSettings = Ext.apply(defaultSettings, Sbi.settings.formbuilder.staticCloseFilterGroupEditor);
	}
	var c = Ext.apply(defaultSettings, config || {});
	
	Ext.apply(this, c);
	
	this.initToolbar();
	Ext.apply(c, {
		filterTitle: this.groupTitle || 'Filter Group'
		, filterFrame: true
		, tbar: this.toolbar
	});
	
	// constructor
	Sbi.formbuilder.StaticCloseFilterGroupEditor.superclass.constructor.call(this, c);
};

Ext.extend(Sbi.formbuilder.StaticCloseFilterGroupEditor, Sbi.formbuilder.EditorPanel, {
    
	
	filtersGroupPanel: null
	, filters: null
	, baseFiltersGroupConf: null
	, filterWizard: null

	//--------------------------------------------------------------------------------
	// public methods
	// --------------------------------------------------------------------------------
	
	, loadContents: function(contents) {
		alert(contents);
	}
	
	, addFilter: function(filtersConf) {
		filtersConf.singleSelection = this.singleSelection;
		var filter = new Sbi.formbuilder.StaticCloseFilterEditor(filtersConf);	
		filter.index = this.contents.length;
		this.addFilterItem(filter);
		
		filter.on('actionrequest', function(action, filter) {
			if(action === 'edit') {
				this.editFilter(filter);
			} else if(action === 'delete') {
				this.deleteFilter(filter);
			}
		}, this);
	}
	
	, deleteFilter: function(f) {
		f.destroy();
	}
	
	, editFilter: function(f) {
		alert('edit filter');
	}
	

	
	// --------------------------------------------------------------------------------
	// private methods
	// --------------------------------------------------------------------------------
	

	, initToolbar: function() {
		this.toolbar = new Ext.Toolbar({
			items: [
			    '->'
			    , {
					text: 'Add',
					handler: function() {this.onFilterWizardShow();},
					scope: this
			    }, {
					text: 'Delete',
					handler: function() { this.destroy(); },
					scope: this
			    }
			  ]
		});		
	}
	
	
	, onFilterWizardShow: function(targetFilter) {
		if(this.filterWizard === null) {
			this.filterWizard = new Sbi.formbuilder.StaticCloseFilterWizard();
			this.filterWizard.on('apply', function(win, target, state) {
				if(target === null) {
					this.addFilter(state);
				} else {
					alert('edit');
				}
				
			}, this);
		}
		
		this.filterWizard.setTarget(targetFilter || null);		
		this.filterWizard.show();
	}
	
});