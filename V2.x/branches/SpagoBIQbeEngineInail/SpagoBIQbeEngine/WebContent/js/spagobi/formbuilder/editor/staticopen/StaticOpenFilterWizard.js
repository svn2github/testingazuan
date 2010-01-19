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

Sbi.formbuilder.StaticOpenFilterWizard = function(openFilter, config) {
	
	var defaultSettings = {
		// set default values here
		title: 'Static open filter definition'
		, autoScroll: true
		, width: 550
		, height: 400
	};
	if (Sbi.settings && Sbi.settings.formbuilder && Sbi.settings.formbuilder.staticOpenFilterWindow) {
		defaultSettings = Ext.apply(defaultSettings, Sbi.settings.formbuilder.staticOpenFilterWindow);
	}
	var c = Ext.apply(defaultSettings, config || {});
	
	var params = {'fieldId': openFilter.field};
	this.services = this.services || new Array();
	this.services['getEntityFields'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'GET_ENTITY_FIELDS'
		, baseParams: params
	});
	
	this.init(openFilter);
	
	c = Ext.apply(c, {
      	items: [this.openFilterForm]
	});
	
	// constructor
    Sbi.formbuilder.StaticOpenFilterWizard.superclass.constructor.call(this, c);
    
    this.addEvents('apply');
    
};

Ext.extend(Sbi.formbuilder.StaticOpenFilterWizard, Ext.Window, {

	init: function(openFilter) {
	
		this.entityId = openFilter.field;
	
		this.filterName = new Ext.form.TextField({
			id: 'text',
			name: 'text',
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
	    	name: 'operator',
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
			fieldLabel: 'Operator',
			value: openFilter.operator
	    });
	    
	    var selectionNumbersStore = new Ext.data.SimpleStore({
		    fields: ['name', 'value'],
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
	    	name: 'maxSelectedNumber',
			store:  selectionNumbersStore, 
			displayField: 'name',
			valueField: 'value',
			maxHeight: 200,
			allowBlank: false,
			editable: true,
			typeAhead: true, // True to populate and autoselect the remainder of the text being typed after a configurable delay
			mode: 'local',
			forceSelection: true, // True to restrict the selected value to one of the values in the list
			triggerAction: 'all',
			emptyText: '',
			selectOnFocus: true, //True to select any existing text in the field immediately on focus
			fieldLabel: 'Max selection',
			value: openFilter.maxSelectedNumber
	    });
	    
	    
	    var orderByFieldStore = new Ext.data.Store({
	    	proxy: new Ext.data.HttpProxy({
				url: this.services['getEntityFields']
		    })
	    	, reader: new Ext.data.JsonReader({id: 'id'}, [
                 {name:'id'},
                 {name:'name'}
     	    ])
	    });
	    
	    this.orderByFieldCombo = new Ext.form.ComboBox({
	    	name: 'orderBy',
			store: orderByFieldStore, 
			displayField: 'name',
			valueField: 'id',
			maxHeight: 200,
			allowBlank: true,
			editable: true,
			typeAhead: true, // True to populate and autoselect the remainder of the text being typed after a configurable delay
			forceSelection: true, // True to restrict the selected value to one of the values in the list
			triggerAction: 'all',
			emptyText: '',
			selectOnFocus: true, //True to select any existing text in the field immediately on focus
			fieldLabel: 'Order results by'
			//value: openFilter.orderBy === undefined ? '' : openFilter.orderBy
	    });
	    
	    this.orderByValue = openFilter.orderBy;
	    if (this.orderByValue !== undefined && this.orderByValue !== '') {
		    orderByFieldStore.on('load', function() {
		    	this.orderByFieldCombo.setValue(this.orderByValue);
		    }, this);
		    orderByFieldStore.load();
	    }
	    
	    var orderingTypesStore = new Ext.data.SimpleStore({
		     fields: ['type', 'nome', 'descrizione'],
		     data : [
			    ['NONE', LN('sbi.qbe.selectgridpanel.sortfunc.name.none'), LN('sbi.qbe.selectgridpanel.sortfunc.desc.none')],
			    ['ASC', LN('sbi.qbe.selectgridpanel.sortfunc.name.asc'), LN('sbi.qbe.selectgridpanel.sortfunc.desc.asc')],
			    ['DESC', LN('sbi.qbe.selectgridpanel.sortfunc.name.desc'), LN('sbi.qbe.selectgridpanel.sortfunc.desc.desc')]
			] 
		});
	     
	    this.orderTypeCombo = new Ext.form.ComboBox({
	    	 name: 'orderType',
	         tpl: '<tpl for="."><div ext:qtip="{nome}: {descrizione}" class="x-combo-list-item">{nome}</div></tpl>',	
	         allowBlank: true,
	         editable: false,
	         store: orderingTypesStore,
	         displayField:'nome',
	         valueField:'type',
	         typeAhead: true,
	         mode: 'local',
	         triggerAction: 'all',
	         autocomplete: 'off',
	         emptyText: LN('sbi.qbe.selectgridpanel.sortfunc.editor.emptymsg'),
	         fieldLabel: 'Order type',
	         selectOnFocus: true,
	         value: openFilter.orderType === undefined ? '' : openFilter.orderType
        });
	    
	    if (openFilter.queryRootEntity === undefined || openFilter.queryRootEntity == null) {
	    	openFilter.queryRootEntity = false;
	    } else {
	    	openFilter.queryRootEntity = (typeof openFilter.queryRootEntity === "string") ? openFilter.queryRootEntity === 'true' : openFilter.queryRootEntity;
	    }

	    this.queryDetails = {
            xtype: 'fieldset',
            title: 'Dettagli della query di lookup',
            autoHeight: true,
            autoWidth: true,
            items: []
        }
		this.queryDetails.items.push(
			this.orderByFieldCombo, 
			this.orderTypeCombo,
			{
				xtype: 'radio',
				hideLabel: false,
				fieldLabel: 'Proponi i valori',
	            boxLabel: 'ammissibili per il campo indicato nel filtro',
	            name: 'queryRootEntity',
	            inputValue: false,
	            checked: (openFilter.queryRootEntity === undefined ? true : !openFilter.queryRootEntity)
			}, {
				xtype: 'radio',
				hideLabel: false,
				fieldLabel: '',
				labelSeparator: '',
	            boxLabel: 'ammissibili per il campo corrispondente nell\'entità di primo livello',
	            name: 'queryRootEntity',
	            inputValue: true,
	            checked: (openFilter.queryRootEntity === undefined ? false : openFilter.queryRootEntity)
		});
	    
	    /*
		this.queryRootEntityCheckBox = new Ext.form.Checkbox({
	    	boxLabel: 'Interroga dimensione a sè'
	    	, checked: openFilter.queryRootEntity === undefined ? false : openFilter.queryRootEntity
	    	, hideLabel: true
	    });
	    */
	    
	    Ext.form.Field.prototype.msgTarget = 'side';
	    this.openFilterForm = new Ext.form.FormPanel({
	        frame: true,
	        bodyStyle: 'padding:5px 5px 0',
	        items: [this.filterName, this.filterEntity, this.filterOperator, this.maxSelectionNumber, 
	                this.queryDetails],
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
		
		/*
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
		openFilter.orderBy = this.orderByFieldCombo.getValue();
		openFilter.orderType = this.orderTypeCombo.getValue();
		openFilter.queryRootEntity = this.queryRootEntityCheckBox.getValue();
		alert(openFilter.toSource());
		return openFilter;
		*/
		
		var openFilter = this.openFilterForm.getForm().getValues();
		openFilter.orderBy = this.orderByFieldCombo.getValue();
		openFilter.orderType = this.orderTypeCombo.getValue();
		openFilter.field = this.entityId;
		if (openFilter.maxSelectedNumber == undefined || openFilter.maxSelectedNumber == null || openFilter.maxSelectedNumber == 1) {
			openFilter.singleSelection = true;
		} else {
			openFilter.singleSelection = false;
		}
		openFilter.queryRootEntity = (typeof openFilter.queryRootEntity === "string") ? openFilter.queryRootEntity === 'true' : openFilter.queryRootEntity;
		return openFilter;
		
		
	}
	
});