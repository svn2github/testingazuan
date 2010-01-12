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

Sbi.formbuilder.VariableGroupEditor = function(config) {
	

	var defaultSettings = {	
		groupTitle: 'Variable Group'
		, width: 300
        , height: 150
        , autoWidth: false    	
        , emptyMsg: 'Drag a field here to add a new admissible value to the variable'
        , ddGroup    : 'formbuilderDDGroup'
        , droppable: {
			onFieldDrop: this.addField
		} 
		
	};
	if (Sbi.settings && Sbi.settings.formbuilder && Sbi.settings.formbuilder.variableGroupEditor) {
		defaultSettings = Ext.apply(defaultSettings, Sbi.settings.formbuilder.variableGroupEditor);
	}
	var c = Ext.apply(defaultSettings, config || {});
	
	Ext.apply(this, c);
	
	Ext.apply(c, {
		filterTitle: this.groupTitle
		, filterFrame: true
	});
	
	// constructor
	Sbi.formbuilder.VariableGroupEditor.superclass.constructor.call(this, c);
};

Ext.extend(Sbi.formbuilder.VariableGroupEditor, Sbi.formbuilder.EditorPanel, {
    

	wizard: null
	
	, groupTitle: null
	
	//--------------------------------------------------------------------------------
	// public methods
	// --------------------------------------------------------------------------------
	
	, setContents: function(contents) {
		alert('Error: "setContents" unimlpemented');
		
		/*
		for(var i = 0, l = contents.length; i < l; i++) {
			this.addFilter(contents[i]);
		}
		*/
		
	}
	
	, getContents: function() {
		alert('Error: "getContents" unimlpemented');
		/*
		var c = {};
		
		c.title = this.groupTitle;
		c.singleSelection = this.singleSelection;
		c.allowNoSelection = this.allowNoSelection;
		if(this.singleSelection === true) {
			c.noSelectionText = this.noSelectionText;
		}
		c.filters = Sbi.formbuilder.StaticCloseFilterGroupEditor.superclass.getContents.call(this);
		
		return c;
		*/
	}
	
	, addField: function(fieldConf) {
		
		alert('Error: "addFilter" unimlpemented');
		/*
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
		*/
	}
	
	, deleteFilter: function(f) {
		f.destroy();
	}
	
	, editFilter: function(f) {
		alert('Error: "editFilter" unimlpemented');
		this.onFilterWizardShow(f)
	}
	

	
	// --------------------------------------------------------------------------------
	// private methods
	// --------------------------------------------------------------------------------
	/*
	, onFilterWizardShow: function(targetFilter) {
		if(this.wizard === null) {
			this.wizard = new Sbi.formbuilder.StaticCloseFilterWizard();
			this.wizard.on('apply', function(win, target, state) {
				if(target === null) {
					this.addFilter(state);
				} else {
					target.setContents(state);
				}
				
			}, this);
		}
		
		this.wizard.setTarget(targetFilter || null);		
		this.wizard.show();
	}
	*/
	
});