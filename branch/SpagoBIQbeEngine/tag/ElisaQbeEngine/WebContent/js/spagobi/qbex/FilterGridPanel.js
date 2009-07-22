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
	
	this.services = new Array();
	var params = {};
	this.services['loadDataStore'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'EXEC_QUERY_ACTION'
		, baseParams: params
	});
	
	//this.addEvents();
	
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
	
	this.on('show', function(){
		if(this.dropTarget === null) {
			//this.dropTarget = new Sbi.qbe.FilterGridDropTarget(this);
		}
	}, this) ;
	
    if(c.query && c.query.filters && c.query.filters.length > 0){
    	this.loadSavedData(c.query);
    }
    
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
                ['AND', 'AND', 'Connect this filter and the next one using the boolean operator AND'],
                ['OR', 'OR', 'Connect this filter and the next one using the boolean operator OR']
        ]
    })

	, filterOptStore: new Ext.data.SimpleStore({
	    fields: ['funzione', 'nome', 'descrizione'],
	    data : [
	            ['NONE', 'none', 'no filter applayed'],
	            ['EQUALS TO', 'equals to',  'true iff the field\'s value is equal to filter\'s value'],
	            ['NOT EQUALS TO', 'not equals to',  'true iff the field\'s value is not equal to filter\'s value'],
	            ['GREATER THAN', 'greater than',  'true iff the field\'s value is greater than filter\'s value'],
	            ['EQUALS OR GREATER THAN', 'equals or greater than',  'true iff the field\'s value is equal or greater than filter\'s value'],
	            ['LESS THAN', 'less than',  'true iff the field\'s value is less than filter\'s value'],
	            ['EQUALS OR LESS THAN', 'equals or less than',  'true iff the field\'s value is equal or less than filter\'s value'],
	            ['STARTS WITH', 'starts with',  'true iff the field\'s value starts with filter\'s value'],
	            ['NOT STARTS WITH', 'not starts with',  'true iff the field\'s value doesn\'t start with filter\'s value'],
	            ['ENDS WITH', 'ends with',  'true iff the field\'s value ends with filter\'s value'],
	            ['NOT ENDS WITH', 'not ends with',  'true iff the field\'s value doesn\'t end with filter\'s value'],
	            ['CONTAINS', 'contains',  'true iff the field\'s value contains filter\'s value'],
	            ['NOT CONTAINS', 'not contains',  'true iff the field\'s value doesn\'t contain filter\'s value'],
	            
	            ['BETWEEN', 'between',  'true iff the field\'s value is between the range specified in the filter value'],
	            ['NOT BETWEEN', 'not between',  'true iff the field\'s value is not between the range specified in the filter value'],
	            ['IN', 'in',  'true iff the field\'s value is equal to one of the values specified in the filter value'],
	            ['NOT IN', 'not in',  'true iff the field\'s value is not equal to any of the values specified in the filter value'],
	            
	            ['NOT NULL', 'not null',  'true iff the field\'s value is not null'],
	            ['IS NULL', 'is null',  'true iff the field\'s value is null']
	    ]
	})


	// public methods

	, deleteFilters : function() {
		this.grid.store.removeAll();
	}

    
	, loadSavedData : function(query) {
		this.setFilters(query.filters);
		this.setFiltersExpression(query.expression);	
  	}
	
	, getFilters : function() {
		var filters = [];
		for(i = 0; i <  this.grid.store.getCount(); i++) {
			var record =  this.grid.store.getAt(i);
			var filter = Ext.apply({}, record.data);
			filter.operand = (filter.otype === 'Static Value')? filter.odesc: filter.operand;
			filter.isfree = filter.isfree || false;
			filters.push(filter);
		}
		
		return filters;
	}
	
	, getRowsAsJSONParams : function() {
		var jsonStr = '[';
		for(i = 0; i <  this.grid.store.getCount(); i++) {
			var tmpRec =  this.grid.store.getAt(i);
			if(i != 0) jsonStr += ',';
			jsonStr += '{';
			jsonStr += 	'"fname" : "' + tmpRec.data['fname'] + '",';	
			jsonStr += 	'"id" : "' + tmpRec.data['id'] + '",';	
			jsonStr += 	'"entity" : "' + tmpRec.data['entity'] + '",';	
			jsonStr += 	'"field"  : "' + tmpRec.data['field']  + '",';	
			jsonStr += 	'"operator"  : "' + tmpRec.data['operator']  + '",';
			if(tmpRec.data['otype'] == "Static Value") {
				jsonStr += 	'"operand"  : "' + tmpRec.data['odesc']  + '",';
			} else {
				jsonStr += 	'"operand"  : "' + tmpRec.data['operand']  + '",';
			}
			jsonStr += 	'"isfree"  : ' + tmpRec.data['isfree']  + ',';
			jsonStr += 	'"otype"  : "' + tmpRec.data['otype']  + '",';
			jsonStr += 	'"odesc"  : "' + tmpRec.data['odesc']  + '",';
			jsonStr += 	'"boperator"  : "' + tmpRec.data['boperator']  + '"';
			
			jsonStr += '}';	
		}
		jsonStr += ']';
		
		return jsonStr;
	}
	
	, setFilters: function(filters) {
		this.deleteFilters();
		for(var i = 0; i < filters.length; i++) {
  			var filter = filters[i];
  			var record = new this.Record(filter);
  			this.store.add(record); 
  		}
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
  	
  
	, addRow : function(config, i) {
		  var fname = 'filter' + (++this.idCount);
		  var newRow = {
		  	fname: fname, 
		  	id: '', 
		  	entity: '',
		  	field: '',
		  	operator:'',
		  	operand: '',
		    otype: 'Static Value',		    
		    odesc: '',
		    boperator:'AND',
		    defaultvalue: null,
		    lastvalue: null,
		  };
		  
		  Ext.apply(newRow, config.data);
		  
		  
		  var record = new this.Record(newRow);
	   
	      if(i != undefined) {
  			this.grid.store.insert(i, record); 
		  } else {
  			this.grid.store.add(record); 
		  }
	}
		
	, getFreeFilters : function() {
		var filters = [];
		for(i = 0; i <  this.grid.store.getCount(); i++) {
			var record =  this.grid.store.getAt(i);
			var filter = Ext.apply({}, record.data);
			if (filter.isfree) {
				filters.push(filter);
			}
		}
		
		return filters;
	}
	
    , setFreeFiltersLastValues: function(formState) {
    	for (var filterName in formState) {
    		var index = this.grid.store.find('fname', filterName);
    		if (index != -1) {
    			var aRecord = this.grid.store.getAt(index);
    			aRecord.set('lastvalue', formState[filterName]);
    		}
    	}
    }
	
    , setFreeFiltersDefaultValues: function(formState) {
    	for (var filterName in formState) {
    		var index = this.grid.store.find('fname', filterName);
    		if (index != -1) {
    			var aRecord = this.grid.store.getAt(index);
    			aRecord.set('defaultvalue', formState[filterName]);
    		}
    	}
    }
    
	, syncWizardExpressionWithGrid: function() {
		var exp = '';
		for(i = 0; i <  this.grid.store.getCount(); i++) {
			var tmpRec =  this.grid.store.getAt(i);
			if(i > 0) {
				exp += ' ' + this.grid.store.getAt(i-1).data['boperator'] + ' $F{' +  tmpRec.data['fname'] + '}';
			} else {
				exp += '$F{' + tmpRec.data['fname'] + '}';
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
		if(!this.isWizardExpression()) {
			this.syncWizardExpressionWithGrid();
		}					
		var operands = [];			
		for(i = 0; i <  this.grid.store.getCount(); i++) {
			var tmpRec =  this.grid.store.getAt(i);
			operands[i] = {
				text: tmpRec.data['fname'],
				ttip: tmpRec.data['fname'] + ': description goes here',
				type: 'operand',
				value: '$F{' +  tmpRec.data['fname'] + '}'
			};
		}
		
		it.eng.spagobi.engines.qbe.filterwizard.setOperands(operands);
		it.eng.spagobi.engines.qbe.filterwizard.show();	 
		this.setWizardExpression(true);
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
		this.store = new Ext.data.SimpleStore({
	        fields: [
	           {name: 'fname'},
	           {name: 'id'},
	           {name: 'entity'},
	           {name: 'field'},
	           {name: 'operator'},
	           {name: 'operand'},
	           {name: 'isfree'},
	           {name: 'otype'},	           
	           {name: 'odesc'},
	           {name: 'boperator'},    
	           {name: 'del'},
	           {name: 'defaultvalue'},
	           {name: 'lastvalue'}
	        ]
		});
		
		// create row structure
	    this.Record = Ext.data.Record.create([
	      {name: 'fname', type: 'string'},
	      {name: 'id', type: 'string'},
	      {name: 'entity', type: 'string'},
	      {name: 'field', type: 'string'},
	      {name: 'operator', type: 'string'},
	      {name: 'operand', type: 'auto'},
	      {name: 'isfree', type: 'bool'},
	      {name: 'otype', type: 'string'},
	      {name: 'odesc', type: 'string'},
	      {name: 'boperator', type: 'string'},
	      {name: 'del', type: 'bool'},
	      {name: 'defaultvalue', type: 'string'},
	      {name: 'lastvalue', type: 'string'}
	    ]);
	}
	
	, initSelectionModel: function(config) {
		this.sm = new Ext.grid.RowSelectionModel();
	}
	
	, initColumnModel: function(config) {
			
			var delButtonColumn = new Ext.grid.ButtonColumn({
		       header: LN('sbi.qbe.filtergridpanel.headers.delete'),
		       dataIndex: 'del',
		       imgSrc: '../img/querybuilder/delete.gif',
		       clickHandler:function(e, t){
		          var index = this.grid.getView().findRowIndex(t);
		          var record = this.grid.store.getAt(index);
		          this.grid.store.remove(record);
		       }
		       //width: 55
		    });
		    
		    
		    var booleanOptColumnEditor = new Ext.form.ComboBox({
		    	tpl: '<tpl for="."><div ext:qtip="{nome}: {descrizione}" class="x-combo-list-item">{funzione}</div></tpl>',	
		        store: this.booleanOptStore, 
		        displayField:'funzione',
		        valueField: 'funzione',
		        allowBlank: false,
		        editable: false,
		        typeAhead: true,
		        mode: 'local',
		        triggerAction: 'all',
		        emptyText:'Select operator...',
		        selectOnFocus:true,
		        autocomplete: 'off',
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
			       header: LN('sbi.qbe.filtergridpanel.headers.isfree'),
			       dataIndex: 'isfree'
			});
		    
		    var filterOptColumnEditor = new Ext.form.ComboBox({
	           	  tpl: '<tpl for="."><div ext:qtip="{nome}: {descrizione}" class="x-combo-list-item">{funzione}</div></tpl>',	
	           	  store: this.filterOptStore, 
	           	  displayField:'funzione',
	              valueField: 'funzione',
	              allowBlank: true,
	              editable: false,
	              typeAhead: true,
	              mode: 'local',
	              triggerAction: 'all',
	              emptyText:'Select function...',
	              autocomplete: 'off',
	              selectOnFocus:true
	        });
		    
		    var nameColumnEditor = new Ext.form.TextField({
	             allowBlank: false							               
	        });
		    
		    
		    var parentFieldEditor = new Ext.form.TriggerField({
	             allowBlank: true
	             , triggerClass: 'x-form-search-trigger'

		    });
		    parentFieldEditor.onTriggerClick = this.onOpenValueEditor.createDelegate(this);
		   
		    var textEditor = new Ext.form.TextField({
	             allowBlank: true
		    });
		    		    
		    
		    this.valueColumnEditors = {
		    		parentFieldEditor: new Ext.grid.GridEditor(parentFieldEditor)		    		
		    		, textEditor: new Ext.grid.GridEditor(textEditor)
		    }
		    
		    /*
		    this.valueColumnEditor = new Ext.form.TriggerField({
	             allowBlank: true
	             , triggerClass: 'x-form-search-trigger'

		    });
		    */
		    
		    
		    
		    this.cm = new Ext.grid.ColumnModel([
		        new Ext.grid.RowNumberer(),
		        {
		           header: LN('sbi.qbe.filtergridpanel.headers.name'),
		           dataIndex: 'fname',
		           //width: 75,		           
		           editor: nameColumnEditor
		           
		        },{
		           header: LN('sbi.qbe.filtergridpanel.headers.entity'),
		           dataIndex: 'entity'
		           //width: 75
		        },{
		           header: LN('sbi.qbe.filtergridpanel.headers.field'),
		           dataIndex: 'field'
		           //width: 75
		        }, {
		           header: LN('sbi.qbe.filtergridpanel.headers.operator'),
		           dataIndex: 'operator',
		           //width: 75		           
		           autocomplete: 'off',
		           editor: filterOptColumnEditor
		        },{
		           header: LN('sbi.qbe.filtergridpanel.headers.value'),
		           dataIndex: 'odesc',
		           //width: 75,
		           
		           editor: textEditor            		           
		        },
		        isFreeCheckColumn,
		        {
		           header: LN('sbi.qbe.filtergridpanel.headers.type'),
		           dataIndex: 'otype'
		           //width: 75
		        }, {
		           header: LN('sbi.qbe.filtergridpanel.headers.boperator'),
		           dataIndex: 'boperator',
		           //width: 75,
		           editor: booleanOptColumnEditor,
		           renderer: function(val){
		        		return '<span style="color:green;">' + val + '</span>';  
			       }
		           
		        },
		        delButtonColumn
		    ]);
		    
		    this.cm.defaultSortable = true;
		    
		    this.plgins = [delButtonColumn, isFreeCheckColumn];
	}

	, initToolbar: function(config) {
		this.toolbar = new Ext.Toolbar({
			items: [
				{
					text: 'Delete',
				    tooltip: 'Delete selected filter',
				    iconCls:'remove',
				    listeners: {
				    	'click': {
			    			fn: this.deleteFilters,
			    			scope: this
			    		}
				    }
				}, {
				  	text: 'Exp Wizard',
				    tooltip: 'Exp Wizard',
				    iconCls:'option',
				    listeners: {
				      	'click': {
							fn: this.showWizard,
			    			scope: this
			    		}
				    }
				} 
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
	        clicksToEdit:2,	        
	        style:'padding:10px',
	        frame: true,
	        height: 300,
	        border:true,  
	        collapsible:true,
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
		if(col == 5) {
			var editor;
			if(this.parentQuery !== null) {
				editor = this.valueColumnEditors.parentFieldEditor;
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
				var r = this.activeEditingContext.record;
				r.data['otype'] = 'Parent Field Content';
				r.data['odesc'] = this.parentQuery.id  + ' / ' +  node.attributes.entity + ' / ' + node.attributes.field;
				r.data['operand'] = this.parentQuery.id + ' ' + node.id;
				this.store.fireEvent('datachanged', this.store) ;
				this.activeEditingContext = null;
			}, this);
			this.operandChooserWindow.on('applyselection', function(win, node) {
				this.activeEditingContext = null;
			}, this);
		}
		this.grid.stopEditing();
		this.operandChooserWindow.setParentQuery(this.parentQuery);
		this.operandChooserWindow.show();
	}
});