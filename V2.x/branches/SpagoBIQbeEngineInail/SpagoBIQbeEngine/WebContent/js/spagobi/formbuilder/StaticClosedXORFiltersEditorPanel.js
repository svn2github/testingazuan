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

Sbi.formbuilder.StaticClosedXORFiltersEditorPanel = function(config) {
	

	var defaultSettings = {
		// set default values here
		frame: true
        , width: 300
        , height: 150
		, autoScroll: true
		, autoWidth: false
		, autoHeight: true
	};
	if (Sbi.settings && Sbi.settings.formbuilder && Sbi.settings.formbuilder.staticClosedXORFiltersEditorPanel) {
		defaultSettings = Ext.apply(defaultSettings, Sbi.settings.formbuilder.staticClosedXORFiltersEditorPanel);
	}
	var c = Ext.apply(defaultSettings, config || {});
	
	Ext.apply(this, c);
	
	this.init();
	this.initToolbar();
	
	Ext.apply(c, {
		tbar: this.toolbar,
        items: this.contents
	});
	
	// constructor
	Sbi.formbuilder.StaticClosedXORFiltersEditorPanel.superclass.constructor.call(this, c);
};

Ext.extend(Sbi.formbuilder.StaticClosedXORFiltersEditorPanel, Ext.Panel, {
    
	services: null
	, contents: null
	, filtersGroupPanel: null
	, filters: null
	, baseFiltersGroupConf: null
	, filterWizard: null

	//--------------------------------------------------------------------------------
	// public methods
	// --------------------------------------------------------------------------------
	
	, loadContents: function(filtersGroupConf) {
		Sbi.qbe.commons.Utils.unimplementedFunction('loadContents');
		this.initFiltersGroupPanel();
		this.contents = [this.filtersGroupPanel];
	}
	
	, clearContents: function() {
		Sbi.qbe.commons.Utils.unimplementedFunction('clearContents');
	}
	
	, addFilter: function(filtersConf) {
		if(this.filters === null) {
			this.filters = [];
			this.filtersGroupPanel.remove(0, true);
			this.filtersGroupPanel.doLayout();
		}

		var filter = this.createFilterEditor(filtersConf);
		filter.index = this.filters.length;
		
		this.filters.push(filter);
		
		this.filtersGroupPanel.add(filter);
		this.filtersGroupPanel.doLayout();
	}
	
	, deleteFilter: function(f) {
		f.destroy();
	}
	
	, editFilter: function(f) {
		alert('edit filter');
	}
	
	, createFilterEditor: function(filtersConf) {
		
		var buttons = [];
		
		var editBtn = new Ext.Button({
	    	text: 'E',
	        width: 30,
	        hidden: true
	    });
		buttons.push(editBtn);
		
		var deleteBtn = new Ext.Button({
	    	text: 'X',
	        width: 30,
	        hidden: true
	    });
		buttons.push(deleteBtn);
		
		
		var filterConf = {
			width: 150,
			hideLabel: true,
			boxLabel: filtersConf.filterTitle,
	        name: 'option-' + this.filters.length,
	        inputValue: 'option-' + this.filters.length
		};
		
		var filter;
		
		if(this.singleSelection === true) {
			filter = new Ext.form.Radio(filterConf);
		} else {
			filter = new Ext.form.Checkbox(filterConf);
		}
		
		var filterEditor = new Ext.Panel({
			layout: 'column'
			//, style: 'background: yellow;'
			, layoutConfig: {
			     columns: 3
			}
			
			, controls: buttons
		
			, items: [				
				{
				    columnWidth: .99,
				    //cellCls: 'table-cell-top',
				    items: [filter]
				},{
				    width: 30,
				    //cellCls: 'table-cell-top',
				    items: [ buttons[0] ]
				},{
				    width: 30,
				    //cellCls: 'table-cell-top',
				    items: [ buttons[1] ]
				}			    
			]
		});
		
		var editFn = this.editFilter.createDelegate(this, [filterEditor]);
		editBtn.on('click', function() {editFn();}, this);
		var deleteFn = this.deleteFilter.createDelegate(this, [filterEditor]);
		deleteBtn.on('click', function() {deleteFn();}, this);
		
		filterEditor.on('render', function(f) {
			
			f.getEl().on('mouseover', function(el) {
				f.addClass('filter-select');
				f.controls[0].setVisible(true);
				f.controls[1].setVisible(true);
			}, this);
			
			f.getEl().on('mouseout', function(el) {
				f.removeClass('filter-select');
				f.controls[0].setVisible(false);
				f.controls[1].setVisible(false);
			}, this);
			
		}, this);
		
		
		
		return filterEditor;
	}
	
	// --------------------------------------------------------------------------------
	// private methods
	// --------------------------------------------------------------------------------
		
	, init: function() {
		
		if(this.baseFiltersGroupConf !== undefined) {
			this.loadContents(this.baseFiltersGroupConf);	
		} else {
			this.initFiltersGroupPanel();
			this.contents = [this.filtersGroupPanel];
		}
	}
	
	, initFiltersGroupPanel: function() {
		this.filtersGroupPanel = new Ext.form.FieldSet({
			title: this.groupTitle,
	        autoHeight: true,
	        autoWidth: true,
	        //defaultType: (this.singleSelection === true? 'radio': 'checkbox'),
	        items: [
	           new Ext.Panel({
	              	layout: 'fit',
	               	html: 'drag a field here to create a new static filter'
	           })
	        ]
		});
	}
	
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
			this.filterWizard = new Sbi.formbuilder.StaticClosedXORFilterWizard();
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