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

Sbi.formbuilder.VariableEditorPanel = function(config) {
	
	var defaultSettings = {
		
		title: 'Grouping Varaibles'
		, emptyMsg: 'Drag a field here to add a new group variable'
		, filterItemName: 'dynamic filter group'
		
		, layout: 'table'
	    , layoutConfig: {
	        columns: 100
	    }
		, enableDebugBtn: true
	};
	if (Sbi.settings && Sbi.settings.formbuilder && Sbi.settings.formbuilder.variableEditorPanel) {
		defaultSettings = Ext.apply(defaultSettings, Sbi.settings.formbuilder.variableEditorPanel);
	}
	var c = Ext.apply(defaultSettings, config || {});
	
	Ext.apply(this, c);
	
	// constructor
    Sbi.formbuilder.VariableEditorPanel.superclass.constructor.call(this, c);
};

Ext.extend(Sbi.formbuilder.VariableEditorPanel, Sbi.formbuilder.EditorPanel, {
    
	wizard: null
	
	
	// --------------------------------------------------------------------------------
	// public methods
	// --------------------------------------------------------------------------------
		
	
	, addFilter: function(filterConf) {	
		//var filtersGroup = new Sbi.formbuilder.StaticCloseFilterGroupEditor(filtersGroupConf);		
		//this.addFilterItem(filtersGroup);
	}
	
	, showFilterGroupWizard: function(targetFilterGroup) {
		if(this.wizard === null) {
			this.wizard = new Sbi.formbuilder.StaticCloseFilterGroupWizard();
			this.wizard.on('apply', function(win, target, state) {
				if(target === null) {
					this.addFilterGroup(state);
				} else {
					 alert('edit');
				}
				
			}, this);
		}
		
		this.wizard.setTarget(targetFilterGroup || null);		
		this.wizard.show();
	}

	// --------------------------------------------------------------------------------
	// private methods
	// --------------------------------------------------------------------------------
	
	, init: function() {
		Sbi.formbuilder.VariableEditorPanel.superclass.init.call(this);
		var variable1GroupEditor, variable2GroupEditor;
		
		variable1GroupEditor = new Sbi.formbuilder.VariableGroupEditor({
			groupTitle: 'Variable 1'
		});
		this.addFilterItem(variable1GroupEditor);
		
		variable2GroupEditor = new Sbi.formbuilder.VariableGroupEditor({
			groupTitle: 'Variable 2'
		});
		this.addFilterItem(variable2GroupEditor);
	}	
	
	
	, onDebug: function() {
		alert(this.getContents().toSource());
	}
	
  	
});