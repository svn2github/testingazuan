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

Sbi.formbuilder.StaticOpenFilterWindow = function(openFilter, config) {
	
	var defaultSettings = {
		// set default values here
		title: 'Static open filter definition'
		, autoScroll: true
		, width: 500
		, height: 400
	};
	if (Sbi.settings && Sbi.settings.formbuilder && Sbi.settings.formbuilder.staticOpenFilterWindow) {
		defaultSettings = Ext.apply(defaultSettings, Sbi.settings.formbuilder.staticOpenFilterWindow);
	}
	var c = Ext.apply(defaultSettings, config || {});
	
	this.init(openFilter);
	
	c = Ext.apply(c, {
      	items: [this.openFilterForm]
	});
	
	// constructor
    Sbi.formbuilder.StaticOpenFilterWindow.superclass.constructor.call(this, c);
    
    this.addEvents('apply');
    
};

Ext.extend(Sbi.formbuilder.StaticOpenFilterWindow, Ext.Window, {

	init: function(openFilter) {

		this.entityId = openFilter.field;
	
		this.filterName = new Ext.form.TextField({
			id: 'name',
			name: 'name',
			value: openFilter.text,
			allowBlank: false, 
			inputType: 'text',
			maxLength: 100,
			width: 250,
			fieldLabel: 'Name'
		});
		
		this.filterEntity = new Ext.form.TextField({
			id: 'entity',
			name: 'entity',
			value: openFilter.text,
			allowBlank: false, 
			inputType: 'text',
			maxLength: 100,
			width: 250,
			fieldLabel: 'Field',
			disabled: true
		});
		
		var filterOptStore = new Ext.data.SimpleStore({
		    fields: ['funzione', 'nome', 'descrizione'],
		    data : [
		            //['NONE', LN('sbi.qbe.filtergridpanel.foperators.name.none'), LN()],
		            ['EQUALS TO', LN('sbi.qbe.filtergridpanel.foperators.name.eq'),  LN('sbi.qbe.filtergridpanel.foperators.desc.eq')],
		            ['NOT EQUALS TO', LN('sbi.qbe.filtergridpanel.foperators.name.noteq'),  LN('sbi.qbe.filtergridpanel.foperators.desc.noteq')],
		            ['GREATER THAN', LN('sbi.qbe.filtergridpanel.foperators.name.gt'),  LN('sbi.qbe.filtergridpanel.foperators.desc.gt')],
		            ['EQUALS OR GREATER THAN', LN('sbi.qbe.filtergridpanel.foperators.name.eqgt'),  LN('sbi.qbe.filtergridpanel.foperators.desc.eqgt')],
		            ['LESS THAN', LN('sbi.qbe.filtergridpanel.foperators.name.lt'),  LN('sbi.qbe.filtergridpanel.foperators.desc.lt')],
		            ['EQUALS OR LESS THAN', LN('sbi.qbe.filtergridpanel.foperators.name.eqlt'),  LN('sbi.qbe.filtergridpanel.foperators.desc.eqlt')],
		            ['STARTS WITH', LN('sbi.qbe.filtergridpanel.foperators.name.starts'),  LN('sbi.qbe.filtergridpanel.foperators.desc.starts')],
		            ['NOT STARTS WITH', LN('sbi.qbe.filtergridpanel.foperators.name.notstarts'),  LN('sbi.qbe.filtergridpanel.foperators.desc.notstarts')],
		            ['ENDS WITH', LN('sbi.qbe.filtergridpanel.foperators.name.ends'),  LN('sbi.qbe.filtergridpanel.foperators.desc.ends')],
		            ['NOT ENDS WITH', LN('sbi.qbe.filtergridpanel.foperators.name.notends'),  LN('sbi.qbe.filtergridpanel.foperators.desc.notends')],
		            ['CONTAINS', LN('sbi.qbe.filtergridpanel.foperators.name.contains'),  LN('sbi.qbe.filtergridpanel.foperators.desc.contains')],
		            ['NOT CONTAINS', LN('sbi.qbe.filtergridpanel.foperators.name.notcontains'),  LN('sbi.qbe.filtergridpanel.foperators.desc.notcontains')],
		            
		            //['BETWEEN', LN('sbi.qbe.filtergridpanel.foperators.name.between'),  LN('sbi.qbe.filtergridpanel.foperators.desc.between')],
		            //['NOT BETWEEN', LN('sbi.qbe.filtergridpanel.foperators.name.notbetween'),  LN('sbi.qbe.filtergridpanel.foperators.desc.notbetween')],
		            ['IN', LN('sbi.qbe.filtergridpanel.foperators.name.in'),  LN('sbi.qbe.filtergridpanel.foperators.desc.in')],
		            ['NOT IN', LN('sbi.qbe.filtergridpanel.foperators.name.notin'),  LN('sbi.qbe.filtergridpanel.foperators.desc.notin')],
		            
		            //['NOT NULL', LN('sbi.qbe.filtergridpanel.foperators.name.notnull'),  LN('sbi.qbe.filtergridpanel.foperators.desc.notnull')],
		            //['IS NULL', LN('sbi.qbe.filtergridpanel.foperators.name.isnull'),  LN('sbi.qbe.filtergridpanel.foperators.desc.isnull')]
		    ]
		});
		
	    this.filterOperator = new Ext.form.ComboBox({
			//tpl: '<tpl for="."><div ext:qtip="{nome}: {descrizione}" class="x-combo-list-item">{nome}</div></tpl>',	
			store:  filterOptStore, 
			displayField: 'nome',
			valueField: 'funzione',
			maxHeight: 200,
			allowBlank: false,
			editable: true,
			typeAhead: true, // True to populate and autoselect the remainder of the text being typed after a configurable delay
			mode: 'local',
			forceSelection: true, // True to restrict the selected value to one of the values in the list
			triggerAction: 'all',
			emptyText: LN('sbi.qbe.filtergridpanel.foperators.editor.emptymsg'),
			selectOnFocus: true, //True to select any existing text in the field immediately on focus
			fieldLabel: 'Operator'
	    });
	    
	    var selectionNumbersStore = new Ext.data.SimpleStore({
		    fields: ['nome', 'valore'],
		    data : [
		            [1, 1],
		            [2, 2],
		            [3, 3],
		            [4, 4],
		            [5, 5],
		            [6, 6],
		            [7, 7],
		            [8, 8],
		            [9, 9]
		    ]
		});
	
	    this.maxSelectionNumber = new Ext.form.ComboBox({
			store:  selectionNumbersStore, 
			displayField: 'nome',
			valueField: 'valore',
			maxHeight: 200,
			allowBlank: false,
			editable: true,
			typeAhead: true, // True to populate and autoselect the remainder of the text being typed after a configurable delay
			mode: 'local',
			forceSelection: true, // True to restrict the selected value to one of the values in the list
			triggerAction: 'all',
			emptyText: '',
			selectOnFocus: true, //True to select any existing text in the field immediately on focus
			fieldLabel: 'Max selection'
	    });
	    
	    Ext.form.Field.prototype.msgTarget = 'side';
	    this.openFilterForm = new Ext.form.FormPanel({
	        frame: true,
	        bodyStyle: 'padding:5px 5px 0',
	        items: [this.filterName, this.filterEntity, this.filterOperator, this.maxSelectionNumber],
	        buttons: [
	                  {text: 'Apply', handler: this.apply, scope: this}
	                  , {text: 'Cancel', handler: function () {this.close();}, scope: this}
	                 ]
	    });
	}

	, apply : function () {
		var formState = this.getFormState();
		this.fireEvent('apply', formState);
		this.close();
	}
	
	, getFormState : function () {
		var openFilter = {};
		openFilter.text = this.filterName.getValue();
		openFilter.field = this.entityId;
		openFilter.operator = this.filterOperator.getValue();
		openFilter.maxSelectedNumber = this.maxSelectionNumber.getValue();
		if (openFilter.maxSelectedNumber == undefined || openFilter.maxSelectedNumber == null || openFilter.maxSelectedNumber == 1) {
			openFilter.singleSelection = true;
		} else {
			openFilter.singleSelection = false;
		}
		return openFilter;
	}
	
});