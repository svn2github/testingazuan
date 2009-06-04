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
  * - name (mail)
  */

Ext.ns("Sbi.widgets");

Sbi.widgets.FilteringToolbar = function(config) {	
	Sbi.widgets.FilteringToolbar.superclass.constructor.call(this, config);
	
	this.state = 'editing';
};

Ext.extend(Sbi.widgets.FilteringToolbar, Ext.Toolbar, {
    
	state: null
	, columnNameStore: null
	, columnNameCombo: null
	, columnNameItem: null
	
	, typeStore: null
	, typeCombo: null
	, typeItem: null
	
	, filterStore: null
	, filterCombo: null
	, filteItem: null
	
	, inputField: null
	, inputItem: null
	
	, onRender : function(ct, position) {
	    
		Sbi.widgets.FilteringToolbar.superclass.onRender.call(this, ct, position);
	    
		this.addText('Il valore della colonna');
	   	
		
		this.addSpacer();
		
		
	    this.columnNameStore = new Ext.data.SimpleStore({
	        fields: ['value', 'label'],
	        data : []
	    });	    
	    
	    this.columnNameCombo = new Ext.form.ComboBox({
	        store: this.columnNameStore,
	        width: 100,
	        displayField:'label',
	        valueField:'value',
	        typeAhead: true,
	        triggerAction: 'all',
	        emptyText:'...',
	        selectOnFocus:true,
	        mode: 'local'
	    });
	    
	    this.columnNameItem = this.addField( this.columnNameCombo );
	    
	    this.store.on('metachange', function( store, meta ) {
			this.columnNameStore.removeAll();
			for(var i = 0; i < meta.fields.length; i ++) {
				if(meta.fields[i].name) {
					var r = new  this.columnNameStore.recordType({
						'value': meta.fields[i].name, 
						'label': meta.fields[i].header || meta.fields[i].name
					});
					this.columnNameStore.add([r]);
				}
			}
		}, this);
	    
	    this.store.on('beforeload', function(store, o) {
	    	/*
			var p = this.getFilterBarState(false);
			o.params.FILTERS = p;
			
			alert('FILTERING_TOOLBAR\n' +  o.params.toSource());
			*/
			return true;
		}, this);
	    
	    
	    this.addSpacer();
	    
	    
	    this.addText('inteso come');
	    
	    
	    this.addSpacer();
	    
	    
	    this.typeStore = new Ext.data.SimpleStore({
	        fields: ['value', 'label'],
	        data : [
	                ['string', 'string']
	                , ['number', 'number']
	                , ['date', 'date']
	        ]
	    });	    
	    
	    this.typeCombo = new Ext.form.ComboBox({
	        store: this.typeStore,
	        width: 65,
	        displayField:'label',
	        valueField:'value',
	        typeAhead: true,
	        triggerAction: 'all',
	        emptyText:'...',
	        selectOnFocus:true,
	        mode: 'local'
	    });
	    
	    this.typeItem = this.addField( this.typeCombo );
	    
	    
	    this.addSpacer();
	    
	    
	    this.filterStore = new Ext.data.SimpleStore({
	        fields: ['value', 'label'],
	        data : [
	                ['contain', 'contains']
	                , ['start', 'starts with']
	                , ['end', 'ends with']
	                , ['equal', '=']
	                , ['less', '<']
	                , ['lessequal', '<=']
	                , ['greater', '>']
	 	            , ['greaterequal', '>=']
	        ]
	    });	    
	    
	    this.filterCombo = new Ext.form.ComboBox({
	        store: this.filterStore,
	        width: 80,
	        displayField:'label',
	        valueField:'value',
	        typeAhead: true,
	        triggerAction: 'all',
	        emptyText:'...',
	        selectOnFocus:true,
	        mode: 'local'
	    });
	    
	    this.filterItem = this.addField( this.filterCombo );
	    
	    
	    this.addSpacer();
	    
	    
	    this.inputField = new Ext.form.TextField({width: 70});
	    
	    this.inputItem = this.addField( this.inputField );
	    
	     
	    this.addSeparator();
	    
	    
	    this.doButton = this.addButton({
	        tooltip: 'apply filter'
	        //, iconCls: 'icon-filter'
	        , iconCls: 'icon-execute'
	        , disabled: false
	        , handler: this.onClick.createDelegate(this)
	    });
	    
	    this.unfilterButton = this.addButton({
	        tooltip: 'remove filter'
	        //, iconCls: 'icon-unfilter'
	        , iconCls: 'icon-remove'
	        , disabled: false
	        , handler: this.onUnfilter.createDelegate(this)
	    });
	    
	    this.addFill();	    
	}

	, getFilterBarState: function(asObject) {
		var filterBarState = {};
		
		filterBarState.columnFilter = this.columnNameCombo.getValue();
		filterBarState.typeValueFilter = this.typeCombo.getValue();
		filterBarState.typeFilter = this.filterCombo.getValue();
		filterBarState.valueFilter = this.inputField.getValue();
		
		if(asObject !== undefined && asObject === false) {
			filterBarState = Ext.util.JSON.encode(filterBarState);
		}	
		
		return filterBarState;
	}
	
	, reset: function() {		
		this.columnNameCombo.reset();		
		this.typeCombo.reset();		
		this.filterCombo.reset();		
		this.inputField.reset();
	}
	
	, disable: function() {		
		this.columnNameItem.disable();		
		this.columnNameCombo.disable();		
		this.typeItem.disable();		
		this.filterItem.disable();		
		this.inputItem.disable();
		this.doLayout();
	}
	
	, disable: function() {		
		this.columnNameItem.enable();		
		this.typeItem.enable();		
		this.filterItem.enable();		
		this.inputItem.enable();
	}

	, onClick: function() {
		if(this.state === 'editing') {
			alert('applyFilter');
			this.unfilterButton.enable();
			alert('applyFilter1');
			this.disable();
			alert('applyFilter2');
			this.state = 'filtering';		
			//var p = Ext.apply({}, this.store.baseParams);
			//this.store.load({params: p});			
		} else if(this.state === 'filtering') {
			alert('editFilter');
			this.enable();
			this.state = 'editing';
		}
		
		 
		 //alert(this.store.baseParams.toSource());
		
	}
	
	, onUnfilter: function() {
		this.unfilterButton.disable();
		this.reset();
		this.enable();
		this.state = 'editing';
		//this.store.load({params: this.store.baseParams || {} });
	}
});