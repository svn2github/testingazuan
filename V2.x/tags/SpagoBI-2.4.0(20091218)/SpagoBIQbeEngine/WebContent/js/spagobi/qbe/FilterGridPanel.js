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

Ext.ns("Sbi.qbe");

Sbi.qbe.FilterGridPanel = function(config) {
	
	
	var c = Ext.apply({
		// set default values here
	}, config || {});
	
	var params = {LIGHT_NAVIGATOR_DISABLED: 'TRUE'};
	this.services = new Array();
	
	this.services['getValuesForQbeFilterLookupService'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'GET_VALUES_FOR_QBE_FILTER_LOOKUP_ACTION'
		, baseParams: params
	});
	
	this.documentParametersStore = c.documentParametersStore;
	
	this.filterIdPrefix = 'filter';
	
	this.idCount = 0;
	this.initStore(c);
	this.initSelectionModel(c);
	this.initColumnModel(c);
	this.initToolbar(c);
	this.initGrid(c);
	this.initGridListeners(c);
	
	c = Ext.apply(c, {
		border: true,
		layout: 'fit',
		autoWidth: true,
		width: 'auto',
		//width: 1000,
		items: [this.grid]
	});
	
	// constructor
    Sbi.qbe.FilterGridPanel.superclass.constructor.call(this, c);
	
    /*
    this.on('render', function(){
		if(this.dropTarget === null) {
			this.dropTarget = new Sbi.qbe.FilterGridDropTarget(this);
		}
	}, this) ;
	*/
};

Ext.extend(Sbi.qbe.FilterGridPanel, Ext.Panel, {
    
	services: null
	
	, parentQuery: null
	, query: null
	
	, store: null
	, Record: null
	, sm: null
	, cm: null
	, plgins: null
	, toolbar: null
	, grid: null
	, wizardExpression: false
	, idCount: null
	, dropTarget: null
	
	, type: 'filtergrid'
	

	// static members
	
	, booleanOptStore: new Ext.data.SimpleStore({
        fields: ['funzione', 'nome', 'descrizione'],
        data : [
                ['AND', LN('sbi.qbe.filtergridpanel.boperators.name.and'), LN('sbi.qbe.filtergridpanel.boperators.desc.and')],
                ['OR', LN('sbi.qbe.filtergridpanel.boperators.name.or'), LN('sbi.qbe.filtergridpanel.boperators.desc.or')]
        ]
    })

	, filterOptStore: new Ext.data.SimpleStore({
	    fields: ['funzione', 'nome', 'descrizione'],
	    data : [
	            ['NONE', LN('sbi.qbe.filtergridpanel.foperators.name.none'), LN()],
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
	            
	            ['BETWEEN', LN('sbi.qbe.filtergridpanel.foperators.name.between'),  LN('sbi.qbe.filtergridpanel.foperators.desc.between')],
	            ['NOT BETWEEN', LN('sbi.qbe.filtergridpanel.foperators.name.notbetween'),  LN('sbi.qbe.filtergridpanel.foperators.desc.notbetween')],
	            ['IN', LN('sbi.qbe.filtergridpanel.foperators.name.in'),  LN('sbi.qbe.filtergridpanel.foperators.desc.in')],
	            ['NOT IN', LN('sbi.qbe.filtergridpanel.foperators.name.notin'),  LN('sbi.qbe.filtergridpanel.foperators.desc.notin')],
	            
	            ['NOT NULL', LN('sbi.qbe.filtergridpanel.foperators.name.notnull'),  LN('sbi.qbe.filtergridpanel.foperators.desc.notnull')],
	            ['IS NULL', LN('sbi.qbe.filtergridpanel.foperators.name.isnull'),  LN('sbi.qbe.filtergridpanel.foperators.desc.isnull')]
	    ]
	})

	// public methods
	
	, createFilter: function() {
		var filter;
		var filterName;
		
		filter = new Object();
		filterName = this.filterIdPrefix + (++this.idCount);
		
		filter = Ext.apply(filter, {
			filterId: filterName
			, filterDescripion: filterName
			
			, promptable: false
			
			, leftOperandValue: ''
			, leftOperandDescription: ''
			, leftOperandLongDescription: null
			, leftOperandType: 'Static Value'
			, leftOperandDefaultValue: null
			, leftOperandLastValue: null
			
			, operator: ''
				
			, rightOperandValue: ''
			, rightOperandDescription: ''
			, rightOperandLongDescription: null
			, rightOperandType: 'Static Value'
			, rightOperandDefaultValue: null
			, rightOperandLastValue: null
			
			, booleanConnector: 'AND'
				
			, deleteButton: false
		});
		
		return filter;
	}

	, addFilter : function(filter) {
		filter = filter || {};
		filter = Ext.apply(this.createFilter(), filter || {});
		filter = this.documentParametersStore.modifyFilter(filter);
		var record = new this.Record( filter );
		this.grid.store.add(record); 
	}
	
	, insertFilter: function(filter, i) {
		if(i != undefined) {
			filter = filter || {};
			filter = Ext.apply(this.createFilter(), filter || {});
			var record = new this.Record( filter );
			this.grid.store.insert(i, record); 
		} else {
			this.addFilter(filter);
		}
	}
	
	, modifyFilter: function(filter, i) {
		if(i != undefined) {			
			var record = this.store.getAt( i );
			Ext.apply(record.data, filter || {});	
			record = this.store.getAt( i );
			this.store.fireEvent('datachanged', this.store) ;
		}
	}
		
	, addRow : function(config, i) {
		Sbi.qbe.commons.unimplementedFunction('FilterGridPanel', 'addRow');
		this.insertFilter(config, i);
	}

	, deleteFilters : function() {
		this.grid.store.removeAll();
		this.setWizardExpression(false);
	}
	
	, getFilters : function() {
		var filters = [];
		var record;
		var filter;
		
		for(i = 0; i <  this.grid.store.getCount(); i++) {
			record =  this.grid.store.getAt(i);
			filter = Ext.apply({}, record.data);
			//filter.operand = (filter.otype === 'Static Value')? filter.odesc: filter.operand;
			filter.promptable = filter.promptable || false;
			filters.push(filter);
		}
		
		return filters;
	}
	
	, setFilters: function(filters) {
		this.deleteFilters();
		for(var i = 0; i < filters.length; i++) {
  			var filter = filters[i];
  			this.addFilter(filter);
  		}
		this.updateCounter(filters);
	}
	
	, updateCounter: function(filters) {
		var max = 0;
		for(var i = 0; i < filters.length; i++) {
  			var filterName = filters[i].filterId;
  			if (filterName.substring(0, this.filterIdPrefix.length) == this.filterIdPrefix) {
  				var suffix = filterName.substring(this.filterIdPrefix.length);
  				var number = new Number(suffix);
  				if (!isNaN(number) && number > max) {
  					max = number;
  				}
  			}
  		}
		this.idCount = max;
	}
	
	, setFiltersExpression: function(expression) {
		if(expression !== undefined) {
			var expStr = this.loadSavedExpression(expression);
			it.eng.spagobi.engines.qbe.filterwizard.setExpression( expStr, true ); 
			this.setWizardExpression(true);
		}
	}
	  	
  	, loadSavedExpression : function(expression) {
  		var str = "";
  		
  		if(expression.type == 'NODE_OP') {
  			for(var i = 0; i < expression.childNodes.length; i++) {
  				var child = expression.childNodes[i];
  				var childStr = this.loadSavedExpression(child); 
  				if(child.type == "NODE_OP") {
  					childStr = "(" + childStr + ")";
  				}
  				str += (i==0?"": " " + expression.value);
				str += " " + childStr;
  			}
  		} else {
  			str += expression.value;
  		}
  		
  		return str;
  	}
  	
  	/*
  	, getPromptableFilters : function() {
		var filters = [];
		for(i = 0; i <  this.grid.store.getCount(); i++) {
			var record =  this.grid.store.getAt(i);
			var filter = Ext.apply({}, record.data);
			if (filter.promptable) {
				filters.push(filter);
			}
		}
		
		return filters;
	}
  	*/
  	
  	, setPromptableFiltersLastValues: function(formState) {
    	for (var filterName in formState) {
    		var index = this.grid.store.find('filterId', filterName);
    		if (index != -1) {
    			var record = this.grid.store.getAt(index);
    			record.set('rightOperandLastValue', formState[filterName]);
    		}
    	}
    }
  	
  	, setPromptableFiltersDefaultValues: function(formState) {
    	for (var filterName in formState) {
    		var index = this.grid.store.find('filterId', filterName);
    		if (index != -1) {
    			var record = this.grid.store.getAt(index);
    			record.set('rightOperandDefaultValue', formState[filterName]);
    		}
    	}
    }
  	
  	/*
	, getFreeFilters : function() {
		Sbi.qbe.commons.deprectadeFunction('FilterGridPanel', 'getFreeFilters');
		return this.getPromptableFilters();
	}
	*/
	
    , setFreeFiltersLastValues: function(formState) {
    	Sbi.qbe.commons.deprectadeFunction('FilterGridPanel', 'setFreeFiltersLastValues');
    	this.setPromptableFiltersLastValues(formState);
    }
	
    , setFreeFiltersDefaultValues: function(formState) {
    	Sbi.qbe.commons.deprectadeFunction('FilterGridPanel', 'setFreeFiltersDefaultValues');
    	this.setPromptableFiltersDefaultValues(formState);
    }
    
	, syncWizardExpressionWithGrid: function() {
		var exp = '';
		var store = this.grid.store;
		for(i = 0; i <  store.getCount(); i++) {
			var currRecord =  store.getAt(i);
			var prevRecord =  store.getAt(i-1);
			if(i > 0) {
				exp += ' ' + prevRecord.data.booleanConnector + ' $F{' +  currRecord.data.filterId + '}';
			} else {
				exp += '$F{' + currRecord.data.filterId + '}';
			}
		}
		it.eng.spagobi.engines.qbe.filterwizard.setExpression(exp, true);
		
	}
	
	, getFiltersExpression : function() {	
		if(!this.isWizardExpression()) {
			this.syncWizardExpressionWithGrid();
		}
		return it.eng.spagobi.engines.qbe.filterwizard.getExpressionAsObject();
	}
	
	, getFiltersExpressionAsJSON : function() {	
		if(!this.isWizardExpression()) {
			this.syncWizardExpressionWithGrid();
		}
		var json = it.eng.spagobi.engines.qbe.filterwizard.getExpressionAsJSON();
		
		return json;
	}
	
	, showWizard: function() {	
		if(this.grid.store.getCount() == 0) {
			Ext.Msg.show({
				   title:'Warning',
				   msg: 'Impossible to create a filter expression. No filters have been defined yet.',
				   buttons: Ext.Msg.OK,
				   icon: Ext.MessageBox.WARNING
			});
		} else { 		
			if(!this.isWizardExpression()) {
				this.syncWizardExpressionWithGrid();
			}					
			var operands = [];			
			for(i = 0; i <  this.grid.store.getCount(); i++) {
				var tmpRec =  this.grid.store.getAt(i);
				operands[i] = {
					text: tmpRec.data.filterId,
					ttip: tmpRec.data.filterId + ': ' + tmpRec.data.filterDescription,
					type: 'operand',
					value: '$F{' +  tmpRec.data.filterId + '}'
				};
			}
			
			it.eng.spagobi.engines.qbe.filterwizard.setOperands(operands);
			it.eng.spagobi.engines.qbe.filterwizard.show();	 
			this.setWizardExpression(true);
		}
	}
	
	, setWizardExpression: function(b) {
		if(b) {
			this.wizardExpression = true;
			//alert('I will use the xpression defined into wizard');
		} else {
			this.wizardExpression = false;
			//alert('I will use inline expression');
		}
	}
	
	, isWizardExpression: function() {
		return this.wizardExpression;
	}
	
	
    // -- private methods ----------------------------------------------------------------------------------------

	, initStore: function(config) {
		
		this.Record = Ext.data.Record.create([
		   {name: 'filterId', type: 'string'}, 
		   {name: 'filterDescripion', type: 'string'},
		   
		   {name: 'promptable', type: 'bool'},
		   
		   {name: 'leftOperandValue', type: 'auto'}, // id (field unique name)
		   {name: 'leftOperandDescription', type: 'string'}, // entity(entity label) + field(field label)
		   {name: 'leftOperandLongDescription', type: 'string'}, // entity(entity label) / ... / entity(entity label) + field(field label)
		   {name: 'leftOperandType', type: 'string'}, // NEW
		   {name: 'leftOperandDefaultValue', type: 'string'}, // RESERVED FOR FUTURE USE
		   {name: 'leftOperandLastValue', type: 'string'}, // RESERVED FOR FUTURE USE
		   
		   {name: 'operator', type: 'string'},
		   
		   {name: 'rightOperandValue', type: 'auto'}, // operand
		   {name: 'rightOperandDescription', type: 'string'}, // odesc
		   {name: 'rightOperandLongDescription', type: 'string'}, // entity(entity label) / ... / entity(entity label) + field(field label)
		   {name: 'rightOperandType', type: 'string'}, // otype
		   {name: 'rightOperandDefaultValue', type: 'string'}, // defaultvalue
		   {name: 'rightOperandLastValue', type: 'string'}, // lastvalue
		   
		   {name: 'booleanConnector', type: 'string'},
		   
		   {name: 'deleteButton', type: 'bool'}
		]);
		   
		
		
		this.store = new Ext.data.SimpleStore({
			reader: new Ext.data.ArrayReader({}, this.Record)
			, fields: [] // just to keep SimpleStore constructor happy (fields are taken from record)
		});
	}
	
	, initSelectionModel: function(config) {
		this.sm = new Ext.grid.RowSelectionModel();
	}
	
	, initColumnModel: function(config) {
			
			var delButtonColumn = new Ext.grid.ButtonColumn({
		       header: LN('sbi.qbe.filtergridpanel.headers.delete')
		       , tooltip: LN('sbi.qbe.filtergridpanel.tooltip.notdef')
		       , dataIndex: 'deleteButton'
		       , imgSrc: '../img/actions/delete.gif'
		       , clickHandler:function(e, t){
		          var index = this.grid.getView().findRowIndex(t);
		          var record = this.grid.store.getAt(index);
		          this.grid.store.remove(record);
		       }
			   , hideable: true
		       , hidden: true
		       , width: 50
		    });
		    
		    
		    var booleanOptColumnEditor = new Ext.form.ComboBox({
		    	tpl: '<tpl for="."><div ext:qtip="{nome}: {descrizione}" class="x-combo-list-item">{nome}</div></tpl>',	
		        store: this.booleanOptStore, 
		        displayField:'nome',
		        valueField: 'funzione',
		        allowBlank: false,
		        editable: true,
		        typeAhead: true, // True to populate and autoselect the remainder of the text being typed after a configurable delay
		        mode: 'local',
		        forceSelection: true, // True to restrict the selected value to one of the values in the list
		        triggerAction: 'all',
		        emptyText: LN('sbi.qbe.filtergridpanel.boperators.editor.emptymsg'),
		        selectOnFocus:true, //True to select any existing text in the field immediately on focus
		        listeners: {
		        	'change': {
     					fn: function(){
		     				this.setWizardExpression(false);        						
		     			}
     					, scope: this
     				}
		         }
		    });
		   
		    
		    var isFreeCheckColumn = new Ext.grid.CheckColumn({
			       header: LN('sbi.qbe.filtergridpanel.headers.isfree')
			       , tooltip: LN('sbi.qbe.filtergridpanel.tooltip.notdef')
			       , dataIndex: 'promptable'
			       , hideable: true
				   , hidden: false
				   , width: 50
			});
		    
		    var filterOptColumnEditor = new Ext.form.ComboBox({
	           	  tpl: '<tpl for="."><div ext:qtip="{nome}: {descrizione}" class="x-combo-list-item">{nome}</div></tpl>',	
	           	  store: this.filterOptStore, 
	           	  displayField:'nome',
	              valueField: 'funzione',
	              maxHeight: 200,
	              allowBlank: true,
	              editable: true,
	              typeAhead: true, // True to populate and autoselect the remainder of the text being typed after a configurable delay
	              mode: 'local',
	              forceSelection: true, // True to restrict the selected value to one of the values in the list
	              triggerAction: 'all',
	              emptyText: LN('sbi.qbe.filtergridpanel.foperators.editor.emptymsg'),
	              selectOnFocus: true //True to select any existing text in the field immediately on focus
	        });
		    
		    
		   
		    
		    var parentFieldEditor = new Ext.form.TriggerField({
	             allowBlank: true
	             , triggerClass: 'trigger-up'

		    });
		    parentFieldEditor.onTriggerClick = this.onOpenValueEditor.createDelegate(this);
		    parentFieldEditor.on('change', function(f, newValue, oldValue){
		    	if(this.activeEditingContext) {
		    		if(this.activeEditingContext.dataIndex === 'leftOperandDescription') {
		    			this.modifyFilter({leftOperandValue: newValue, leftOperandType: 'Static Value', leftOperandLongDescription: null}, this.activeEditingContext.row);
		    		} else if(this.activeEditingContext.dataIndex === 'rightOperandDescription') {
		    			this.modifyFilter({rightOperandValue: newValue, rightOperandType: 'Static Value', rightOperandLongDescription: null}, this.activeEditingContext.row);
		    		}
		    	}		    	
		    }, this);
		    
		    
		    var textEditor = new Ext.form.TextField({
	             allowBlank: true
		    });
		    
		    textEditor.on('change', function(f, newValue, oldValue){
		    	if(this.activeEditingContext) {
		    		if(this.activeEditingContext.dataIndex === 'leftOperandDescription') {
		    			this.modifyFilter({leftOperandValue: newValue, leftOperandType: 'Static Value', leftOperandLongDescription: null}, this.activeEditingContext.row);
		    		} else if(this.activeEditingContext.dataIndex === 'rightOperandDescription') {
		    			this.modifyFilter({rightOperandValue: newValue, rightOperandType: 'Static Value', rightOperandLongDescription: null}, this.activeEditingContext.row);
		    		}
		    	}		    	
		    }, this);
		    
		    //FATTO DA ME	    
		    
		   var lookupFieldEditor = new Ext.form.TriggerField({
	             allowBlank: true
	             , triggerClass: 'x-form-search-trigger'

		    });
		    lookupFieldEditor.onTriggerClick = this.openValuesForQbeFilterLookup.createDelegate(this);
		    lookupFieldEditor.on('change', function(f, newValue, oldValue){
		    	if(this.activeEditingContext) {
		    		if(this.activeEditingContext.dataIndex === 'rightOperandDescription') {
		    			this.modifyFilter({rightOperandValue: newValue, rightOperandType: 'Static Value', rightOperandLongDescription: null}, this.activeEditingContext.row);
		    		}
		    	}		    	
		    }, this);
		    
		    
		    var multiButtonEditor = new Sbi.widgets.TriggerFieldMultiButton({
	         		allowBlank: true
	         		
		    });
		    
		    multiButtonEditor.onTrigger1Click = this.openValuesForQbeFilterLookup.createDelegate(this);
		    multiButtonEditor.onTrigger2Click = this.onOpenValueEditor.createDelegate(this);  	
		    
		   /* multiButtonEditor.on('onlookupvalues', function(f){
		    	this.openValuesForQbeFilterLookup.createDelegate(this);	    	
		    }, this);
		    
		    multiButtonEditor.on('onparentvalues', function(f){
		    	this.onOpenValueEditor.createDelegate(this);  	
		    }, this);*/
		    
		    multiButtonEditor.on('change', function(f, newValue, oldValue){
		    	if(this.activeEditingContext) {
		    		if(this.activeEditingContext.dataIndex === 'rightOperandDescription') {
		    			this.modifyFilter({rightOperandValue: newValue, rightOperandType: 'Static Value', rightOperandLongDescription: null}, this.activeEditingContext.row);
		    		}
		    	}		    	
		    }, this);
		    
		    //FINE FATTO DA ME
		    		
		    this.valueColumnEditors = {
		    		parentFieldEditor: new Ext.grid.GridEditor(parentFieldEditor)		    		
		    		, textEditor: new Ext.grid.GridEditor(textEditor)
		    		, lookupFieldEditor: new Ext.grid.GridEditor(lookupFieldEditor)	
		    		, multiButtonEditor: new Ext.grid.GridEditor(multiButtonEditor)
		    }
		    
			
		    this.cm = new Ext.grid.ColumnModel([
		        new Ext.grid.RowNumberer(),
		        { 
		            header: LN('sbi.qbe.filtergridpanel.headers.name')
		           , tooltip: LN('sbi.qbe.filtergridpanel.tooltip.name')
		           , dataIndex: 'filterId'       
		           , editor: new Ext.form.TextField({allowBlank: false})
		           , hideable: true
		           , hidden: false		 
		           , sortable: false
		        }, {
			       header: LN('sbi.qbe.filtergridpanel.headers.desc')
			       , tooltip: LN('sbi.qbe.filtergridpanel.tooltip.desc')
			       , dataIndex: 'filterDescripion'       
			       , editor: new Ext.form.TextField({allowBlank: false})
			       , hideable: true
			       , hidden: true		 
			       , sortable: false
			    },
			    
			    // == LEFT OPERAND ========================================
			    /*
			    {
				    header: LN('sbi.qbe.filtergridpanel.headers.loval')
				    , tooltip: LN('sbi.qbe.filtergridpanel.tooltip.loval')
				    , dataIndex: 'leftOperandValue'       
				    , hideable: true
				    , hidden: true		 
				    , sortable: false
				}, */
			    {
				    header: LN('sbi.qbe.filtergridpanel.headers.lodesc')
				    , tooltip: LN('sbi.qbe.filtergridpanel.tooltip.lodesc')
				    , dataIndex: 'leftOperandDescription'       
				    , editor: new Ext.form.TextField({allowBlank: false})
				    , hideable: false
				    , hidden: false		 
				    , sortable: false
				    , renderer: this.getLeftOperandTooltip
				}, {
				    header: LN('sbi.qbe.filtergridpanel.headers.lotype')
				    , tooltip: LN('sbi.qbe.filtergridpanel.tooltip.lotype')
				    , dataIndex: 'leftOperandType'       
				    , hideable: true
				    , hidden: true		 
				    , sortable: false
				}, /*{
				    header: LN('sbi.qbe.filtergridpanel.headers.lodef')
				    , tooltip: LN('sbi.qbe.filtergridpanel.tooltip.lodef')
				    , dataIndex: 'leftOperandDefaultValue'       
				    , hideable: false
				    , hidden: false		 
				    , sortable: false
				},	{
				    header: LN('sbi.qbe.filtergridpanel.headers.lolast')
				    , tooltip: LN('sbi.qbe.filtergridpanel.tooltip.lolast')
				    , dataIndex: 'leftOperandLastValue'       
				    , hideable: false
				    , hidden: false		 
				    , sortable: false
				}, */
				// == OPERATOR ========================================
				{
					header: LN('sbi.qbe.filtergridpanel.headers.operator')
			        , tooltip: LN('sbi.qbe.filtergridpanel.tooltip.notdef')
			        , dataIndex: 'operator'     
			        , editor: filterOptColumnEditor
			        , hideable: false
			        , hidden: false	
			        , sortable: false
			    },
				// == RIGHT OPERAND ========================================
				/*
			    {
				    header: LN('sbi.qbe.filtergridpanel.headers.roval')
				    , tooltip: LN('sbi.qbe.filtergridpanel.tooltip.roval')
				    , dataIndex: 'rightOperandValue'       
				    , hideable: true
				    , hidden: true		 
				    , sortable: false
				}, */
			    {
				    header: LN('sbi.qbe.filtergridpanel.headers.rodesc')
				    , tooltip: LN('sbi.qbe.filtergridpanel.tooltip.rodesc')
				    , dataIndex: 'rightOperandDescription'       
				    , editor: textEditor
				    , hideable: false
				    , hidden: false	
				    , sortable: false
				    , renderer: this.getRightOperandTooltip
				}, {
				    header: LN('sbi.qbe.filtergridpanel.headers.rotype')
				    , tooltip: LN('sbi.qbe.filtergridpanel.tooltip.rotype')
				    , dataIndex: 'rightOperandType'       
				    , hideable: true
				    , hidden: true		 
				    , sortable: false
				}, /*{
				    header: LN('sbi.qbe.filtergridpanel.headers.rodef')
				    , tooltip: LN('sbi.qbe.filtergridpanel.tooltip.rodef')
				    , dataIndex: 'rightOperandDefaultValue'       
				    , hideable: false
				    , hidden: false		 
				    , sortable: false
				},	{
				    header: LN('sbi.qbe.filtergridpanel.headers.rolast')
				    , tooltip: LN('sbi.qbe.filtergridpanel.tooltip.rolast')
				    , dataIndex: 'rightOperandLastValue'       
				    , hideable: false
				    , hidden: false		 
				    , sortable: false
				}, */
		        
		        isFreeCheckColumn, 
		        {
		           header: LN('sbi.qbe.filtergridpanel.headers.boperator')
		           , tooltip: LN('sbi.qbe.filtergridpanel.tooltip.notdef')
		           , dataIndex: 'booleanConnector'
		           , editor: booleanOptColumnEditor
		           , renderer: function(val){
		        		return '<span style="color:green;">' + val + '</span>';  
			       }
		           , hideable: true
		           , hidden: false	
		           , width: 55
		           , sortable: false
		           
		        }
		        , delButtonColumn
		    ]);
		    
		    this.cm.defaultSortable = true;
		    
		    this.plgins = [delButtonColumn, isFreeCheckColumn];
	}

	, initToolbar: function(config) {
		this.toolbar = new Ext.Toolbar({
			items: [
				{
					text: LN('sbi.qbe.filtergridpanel.buttons.text.add'),
				    tooltip: LN('sbi.qbe.filtergridpanel.buttons.tt.add'),
				    iconCls:'add',
				    listeners: {
				    	'click': {
							fn: function() {this.addFilter();},
							scope: this
						}
				    }
				} , {
					text: LN('sbi.qbe.filtergridpanel.buttons.text.delete'),
				    tooltip: LN('sbi.qbe.filtergridpanel.buttons.tt.delete'),
				    iconCls:'remove',
				    listeners: {
				    	'click': {
			    			fn: this.deleteFilters,
			    			scope: this
			    		}
				    }
				}, {
					text: LN('sbi.qbe.filtergridpanel.buttons.text.wizard'),
				    tooltip: LN('sbi.qbe.filtergridpanel.buttons.tt.wizard'),
				    iconCls:'option',
				    listeners: {
				      	'click': {
							fn: this.showWizard,
			    			scope: this
			    		}
				    }
				} /*, {
				  	text: 'Debug',
				    tooltip: 'Remove before release',
				    iconCls:'option',
				    listeners: {
				      	'click': {
							fn: function() {
								alert('filters: ' + this.getFilters().toSource() + '\n\nexpression: '+ this.getFiltersExpression().toSource() + '\n\nuseExpression: ' + this.isWizardExpression());
							},
			    			scope: this
			    		}
				    }
				} */
			]
		});
	}
	
	, initGrid: function(config) {
		 // create the Grid
	    this.grid = new Ext.grid.EditorGridPanel({
	    	title: LN('sbi.qbe.filtergridpanel.title'),   
	        store: this.store,
	        cm: this.cm,
	        sm : this.sm,
	        tbar: this.toolbar,
	        plugins: this.plgins,
	        clicksToEdit:1,	        
	        style:'padding:10px',
	        frame: true,
	        height: 300,
	        border:true,  
	        collapsible:false,
	        layout: 'fit',
	        viewConfig: {
	            forceFit:true
	        },
	        enableDragDrop:true,
    		ddGroup: 'gridDDGroup',
	        iconCls:'icon-grid'        
	    });
	    this.grid.type = this.type;
	}
	
	, initGridListeners: function(config) {
		this.grid.on("mouseover", function(e, t){
	    	var row;
	        this.targetRow = t;
	        
	         this.targetRowIndex = this.getView().findRowIndex(t);
    		 this.targetColIndex = this.getView().findCellIndex(t);
	        
	        if((row = this.getView().findRowIndex(t)) !== false){
	            this.getView().addRowClass(row, "row-over");
	        }     
	     }, this.grid);
	     
	     this.grid.on("mouseout", function(e, t){
	        var row;
	        this.targetRow = undefined;
	        if((row = this.getView().findRowIndex(t)) !== false && row !== this.getView().findRowIndex(e.getRelatedTarget())){
	            this.getView().removeRowClass(row, "row-over");
	        }
	     }, this.grid);
	    
	    this.grid.on('keydown', function(e){ 
	      if(e.keyCode === 46) {
	        var sm=this.grid.getSelectionModel();
	        var ds = this.grid.getStore();
	        var rows=sm.getSelections();
	        for (i = 0; i < rows.length; i++) {
	          this.store.remove( ds.getById(rows[i].id) );
	        }
	      }      
	    }, this);
	    
	    this.grid.on('beforeedit', this.onBeforeEdit, this);
	    
	    this.grid.store.on('remove', function(e){
	    	this.setWizardExpression(false);
	    }, this);
	}
	
	, onBeforeEdit: function(e) {
		/*
		 	grid - This grid
			record - The record being edited
			field - The field name being edited
			value - The value for the field being edited.
			row - The grid row index
			column - The grid column index
			cancel - Set this to true to cancel the edit or return false from your handler.
		 */
		this.activeEditingContext = Ext.apply({}, e);
		var col = this.activeEditingContext.column;
		var row = this.activeEditingContext.row;		
		var dataIndex = this.activeEditingContext.grid.getColumnModel().getDataIndex( col );
		this.activeEditingContext.dataIndex = dataIndex;
		
		if(dataIndex === 'leftOperandDescription' || dataIndex === 'rightOperandDescription') {
			var editor;
			if(this.parentQuery !== null) {
			//FATTO DA ME
				if(dataIndex === 'rightOperandDescription'){
					editor = this.valueColumnEditors.multiButtonEditor;
				}else{
					editor = this.valueColumnEditors.parentFieldEditor;
				}
			} else if(dataIndex === 'rightOperandDescription'){
			   
				editor = this.valueColumnEditors.lookupFieldEditor;
			  
			    //FINE FATTO DA ME
			} else {
				editor = this.valueColumnEditors.textEditor;
			}

			this.grid.colModel.setEditor(col,editor);
		}			
	}
	
	, onOpenValueEditor: function(e) {
		if(this.operandChooserWindow === undefined) {
			this.operandChooserWindow = new Sbi.qbe.OperandChooserWindow();
			this.operandChooserWindow.on('applyselection', function(win, node) {
				//var r = this.activeEditingContext.record;
				var filter;
				if(this.activeEditingContext.dataIndex === 'leftOperandDescription') {
					filter = {
						leftOperandType: 'Parent Field Content'
						, leftOperandDescription: this.parentQuery.id  + ' : ' +  node.attributes.entity + ' : ' + node.attributes.field
						, leftOperandValue: this.parentQuery.id + ' ' + node.id
						, leftOperandLongDescription: 'Query ' + this.parentQuery.id + ', ' + node.attributes.longDescription
					}
					this.modifyFilter(filter, this.activeEditingContext.row);
				} else if(this.activeEditingContext.dataIndex === 'rightOperandDescription') {
					filter = {
						rightOperandType: 'Parent Field Content'
						, rightOperandDescription: this.parentQuery.id  + ' : ' +  node.attributes.entity + ' : ' + node.attributes.field
						, rightOperandValue: this.parentQuery.id + ' ' + node.id
						, rightOperandLongDescription: 'Query ' + this.parentQuery.id + ', ' + node.attributes.longDescription
					}
					this.modifyFilter(filter, this.activeEditingContext.row);
				}
				//this.store.fireEvent('datachanged', this.store) ;
				//this.activeEditingContext = null;
			}, this);
			
			this.operandChooserWindow.on('applyselection', function(win, node) {
				this.activeEditingContext = null;
			}, this);
		}
		this.grid.stopEditing();
		this.operandChooserWindow.setParentQuery(this.parentQuery);
		this.operandChooserWindow.show();
	}
	
	, createStore: function(entityId) {
		var record = this.activeEditingContext.grid.store.getAt(this.activeEditingContext.row);
		var entityId = record.get('leftOperandValue');
		var createStoreUrl = this.services['getValuesForQbeFilterLookupService']
		        + '&ENTITY_ID=' + entityId;
		var store;	
		store = new Ext.data.JsonStore({
			url: createStoreUrl
		});
		store.on('loadexception', function(store, options, response, e) {
			var msg = '';
			var content = Ext.util.JSON.decode( response.responseText );
  			if(content !== undefined) {
  				msg += content.serviceName + ' : ' + content.message;
  			} else {
  				msg += 'Server response is empty';
  			}
	
			Sbi.exception.ExceptionHandler.showErrorMessage(msg, response.statusText);
		});
		return store;	
	}
	
	, getFormState: function() {
		var state;
		
		state = {};
		for(p in this.fields) {
			var field = this.fields[p];
			var value = field.getValue();
			state[field.name] = value;
			var rawValue = field.getRawValue();
			if (rawValue !== undefined) {
				// TODO to improve: the value of the field should be an object with actual value and its description
				// Conflicts with other parameters are avoided since the parameter url name max lenght is 20
				state[field.name + '_field_visible_description'] = rawValue;
			}
		}
		return state;
	}
	
	, openValuesForQbeFilterLookup: function(e) {
			var store = this.createStore();
			//store.on('beforeload', function(store, o) {
				//var p = Sbi.commons.JSON.encode(this.getFormState());
				//o.params.PARAMETERS = p;
				//return true;
			//}, this);
			var baseConfig = {
		       store: store
		     , singleSelect: false
			};
			
			this.lookupField = new Sbi.widgets.LookupField(baseConfig);
			var record = this.activeEditingContext.grid.store.getAt(this.activeEditingContext.row);
		    var valuesToload = record.get('rightOperandValue');
			this.lookupField.onLookUp(valuesToload);
			this.lookupField.on('selectionmade', function(xselection) {
				var filter;
				if(this.activeEditingContext.dataIndex === 'rightOperandDescription') {
					filter = {
						rightOperandType: 'Static Value'
						, rightOperandDescription: xselection.Values
						, rightOperandValue: xselection.Values
						, rightOperandLongDescription: xselection.Values
					}
					this.modifyFilter(filter, this.activeEditingContext.row);
				}
			}, this);
			
	}
	
	, getLeftOperandTooltip: function (value, metadata, record) {
	 	var tooltipString = record.data.leftOperandLongDescription;
	 	if (tooltipString !== undefined && tooltipString != null) {
	 		metadata.attr = ' ext:qtip="'  + tooltipString + '"';
	 	}
	 	return value;
	}
	
	, getRightOperandTooltip: function (value, metadata, record) {
	 	var tooltipString = record.data.rightOperandLongDescription;
	 	if (tooltipString !== undefined && tooltipString != null) {
	 		metadata.attr = ' ext:qtip="'  + tooltipString + '"';
	 	}
	 	return value;
	}
	
});