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

Ext.ns("Sbi.formviewer");

Sbi.formbuilder.DynamicFilterGroupEditor = function(config) {
	
	var defaultSettings = {	
		groupTitle: 'Dynamic Filter Group'
		, width: 300
        , height: 150
        , autoWidth: false    	
        , emptyMsg: 'Drag a field here to add a new dynamic filter'
        , ddGroup    : 'formbuilderDDGroup'
        , droppable: {
			onFieldDrop: this.addField
		} 
	};
	
	if (Sbi.settings && Sbi.settings.formbuilder && Sbi.settings.formbuilder.dynamicFilterGroupEditor) {
		defaultSettings = Ext.apply(defaultSettings, Sbi.settings.formbuilder.dynamicFilterGroupEditor);
	}
	
	this.operator = config.operator;
	delete config.operator;
	
	var c = Ext.apply(defaultSettings, config || {});
	
	Ext.apply(this, c);
	
	Ext.apply(c, {
		filterTitle: this.groupTitle
		, filterFrame: true
	});
	
	// constructor
	Sbi.formbuilder.DynamicFilterGroupEditor.superclass.constructor.call(this, c);
};

Ext.extend(Sbi.formbuilder.DynamicFilterGroupEditor, Sbi.formbuilder.EditorPanel, {
    

	wizard: null
	, operator: null
	, groupTitle: null
	
	//--------------------------------------------------------------------------------
	// public methods
	// --------------------------------------------------------------------------------
	
	, setContents: function(contents) {
		for(var i = 0, l = contents.length; i < l; i++) {
			this.addField(contents[i]);
		}		
	}
	
	, getContents: function() {
		var c = {};
		c.operator = this.operator;
		c.admissibleFields = Sbi.formbuilder.DynamicFilterGroupEditor.superclass.getContents.call(this)
		return c;
	}
	
	, addField: function(fieldConf) {
		
		var field = new Sbi.formbuilder.VariableEditor(fieldConf);				
		this.addFilterItem(field);
		
		field.on('actionrequest', function(action, field) {
			if(action === 'edit') {
				this.editFilter(field);
			} else if(action === 'delete') {
				this.deleteField(field);
			}
		}, this);
	}
	
	, deleteField: function(f) {
		this.remove(f, true);
	}
	
	, editFilter: function(f) {
		alert('Error: "editFilter" unimlpemented');
	}
	
});