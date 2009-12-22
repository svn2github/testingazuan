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

Sbi.formbuilder.StaticClosedXORFiltersEditorPanel = function(filtersGroupConf, config) {
	
	var width = filtersGroupConf !== undefined?   filtersGroupConf.width || 300: 300;
	var height = filtersGroupConf !== undefined?   filtersGroupConf.height || 150: 150;
	
	var defaultSettings = {
		// set default values here
		frame: true
        , width: width
        //, height: height
		, autoScroll: true
		, autoWidth: false
		, autoHeight: true
	};
	if (Sbi.settings && Sbi.settings.formbuilder && Sbi.settings.formbuilder.staticClosedXORFiltersEditorPanel) {
		defaultSettings = Ext.apply(defaultSettings, Sbi.settings.formbuilder.staticClosedXORFiltersEditorPanel);
	}
	var c = Ext.apply(defaultSettings, config || {});
	
	this.init(filtersGroupConf);
	
	this.toolbar = new Ext.Toolbar({
		items: [
		    '->'
		    , {
				text: 'Add',
				handler: function() {this.addFilter();},
				scope: this
		    }, {
				text: 'Delete',
				handler: function() { this.destroy(); },
				scope: this
		    }
		  ]
	});
	
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

	//--------------------------------------------------------------------------------
	// public methods
	// --------------------------------------------------------------------------------
		
	
	, loadStructure: function(c) {
		
		// TODO: remove all previously added items
		
		this.items = {
			xtype: 'fieldset',
	        title: c.title,
	        autoHeight: true,
	        autoWidth: true,
	        defaultType: 'radio',
	        items: []
	    }
			
		if (c.allowNoSelection !== null && c.allowNoSelection === true) {
			// create No Selection Item
			this.items.items.push({
				hideLabel: true,
				boxLabel: c.noSelectionText,
				name: c.id,
				inputValue: 'noSelection'
			});
		}
			
		for (var i = 0; i < c.filters.length; i++) {
			// create items
			var aFilter = c.filters[i];
			this.items.items.push({
				hideLabel: true,
				boxLabel: aFilter.text,
	            name: c.id,
	            inputValue: aFilter.id
			});
		}
	}
	
	, loadContents: function(filtersGroupConf) {
		Sbi.qbe.commons.Utils.unimplementedFunction('loadContents');
		this.initFiltersGroupPanel();
		this.contents = [this.filtersGroupPanel];
	}
	
	, clearContents: function() {
		Sbi.qbe.commons.Utils.unimplementedFunction('clearContents');
	}
	
	, addFilter: function() {
		if(this.filters === null) {
			this.filters = [];
			this.filtersGroupPanel.remove(0, true);
			this.filtersGroupPanel.doLayout();
		}

		var filter = this.createFilterEditor();
		filter.index = this.filters.length;
		
		this.filters.push(filter);
		
		this.filtersGroupPanel.add(filter);
		this.filtersGroupPanel.doLayout();
	}
	
	, deleteFilter: function(f) {
		alert('delete filter');
		f.destroy();
	}
	
	, editFilter: function(f) {
		alert('edit filter');
	}
	
	, createFilterEditor: function() {
		
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
				    items: [new Ext.form.Radio({
				    	width: 150,
						hideLabel: true,
						boxLabel: 'option-' + this.filters.length,
			            name: 'option-' + this.filters.length,
			            inputValue: 'option-' + this.filters.length
					})]
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
		
	, init: function(filtersGroupConf) {
		
		if(filtersGroupConf !== undefined) {
			this.loadContents(filtersGroupConf);	
		} else {
			this.initFiltersGroupPanel();
			this.contents = [this.filtersGroupPanel];
		}
	}
	
	, initFiltersGroupPanel: function() {
		this.filtersGroupPanel = new Ext.form.FieldSet({
			title: 'Prova',
	        autoHeight: true,
	        autoWidth: true,
	        defaultType: 'radio',
	        items: [
	           new Ext.Panel({
	              	layout: 'fit',
	               	html: 'drag a field here to create a new static filter'
	           })
	        ]
		});
	}
	
	
	
});