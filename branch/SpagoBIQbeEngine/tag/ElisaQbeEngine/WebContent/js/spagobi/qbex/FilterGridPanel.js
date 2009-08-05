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
	
    /*
	this.on('show', function(){
		if(this.dropTarget === null) {
			this.dropTarget = new Sbi.qbe.FilterGridDropTarget(this);
		}
	}, this) ;
	*/
    
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
		    lastvalue: null
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
		       header: LN('sbi.qbe.filtergridpanel.headers.delete')
		       , tooltip: LN('sbi.qbe.filtergridpanel.tooltip.notdef')
		       , dataIndex: 'del'
		       , imgSrc: '../img/querybuilder/delete.gif'
		       , clickHandler:function(e, t){
		          var index = this.grid.getView().findRowIndex(t);
		          var record = this.grid.store.getAt(index);
		          this.grid.store.remove(record);
		       }
			   , hideable: true
		       , hidden: true
		    });
		    
		    
		    var booleanOptColumnEditor = new Ext.form.ComboBox({
		    	tpl: '<tpl for="."><div ext:qtip="{nome}: {descrizione}" class="x-combo-list-item">{funzione}</div></tpl>',	
		        store: this.booleanOptStore, 
		        displayField:'funzione',
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
			       , dataIndex: 'isfree'
			       , hideable: true
				   , hidden: false
			});
		    
		    var filterOptColumnEditor = new Ext.form.ComboBox({
	           	  tpl: '<tpl for="."><div ext:qtip="{nome}: {descrizione}" class="x-combo-list-item">{funzione}</div></tpl>',	
	           	  store: this.filterOptStore, 
	           	  displayField:'funzione',
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
		    
		    textEditor.on('change', function(){
		    	//alert("This is the change you can believe in");
		    	if(this.activeEditingContext) {
		    		//alert("do it");
		    		var r = this.activeEditingContext.record;
					r.data['otype'] = 'Static Value';
					this.store.fireEvent('datachanged', this.store) ;
		    	}
		    	
		    }, this);
		    		
		    this.valueColumnEditors = {
		    		parentFieldEditor: new Ext.grid.GridEditor(parentFieldEditor)		    		
		    		, textEditor: new Ext.grid.GridEditor(textEditor)
		    }
		    
		    this.cm = new Ext.grid.ColumnModel([
		        new Ext.grid.RowNumberer(),
		        {
		           header: LN('sbi.qbe.filtergridpanel.headers.name')
		           , tooltip: LN('sbi.qbe.filtergridpanel.tooltip.notdef')
		           , dataIndex: 'fname'       
		           , editor: nameColumnEditor
		           , hideable: true
		           , hidden: false		 
		           , sortable: false
		        },{
		           header: LN('sbi.qbe.filtergridpanel.headers.entity')
		           , tooltip: LN('sbi.qbe.filtergridpanel.tooltip.notdef')
		           , dataIndex: 'entity'
		           , hideable: true
		           , hidden: false
		           , sortable: false
		        },{
		           header: LN('sbi.qbe.filtergridpanel.headers.field')
		           , tooltip: LN('sbi.qbe.filtergridpanel.tooltip.notdef')
		           , dataIndex: 'field'
		           , hideable: false
		           , hidden: false	
		           , sortable: false
		        }, {
		           header: LN('sbi.qbe.filtergridpanel.headers.operator')
		           , tooltip: LN('sbi.qbe.filtergridpanel.tooltip.notdef')
		           , dataIndex: 'operator'     
		           , editor: filterOptColumnEditor
		           , hideable: false
		           , hidden: false	
		           , sortable: false
		        },{
		           header: LN('sbi.qbe.filtergridpanel.headers.value')
		           , tooltip: LN('sbi.qbe.filtergridpanel.tooltip.notdef')
		           , dataIndex: 'odesc'
		           , editor: textEditor 
		           , hideable: false
		           , hidden: false	
		           , sortable: false
		        },
		        isFreeCheckColumn,
		        {
		           header: LN('sbi.qbe.filtergridpanel.headers.type')
		           , tooltip: LN('sbi.qbe.filtergridpanel.tooltip.notdef')
		           , dataIndex: 'otype'
		           , hideable: true
		           , hidden: false	
		           , sortable: false
		        }, {
		           header: LN('sbi.qbe.filtergridpanel.headers.boperator')
		           , tooltip: LN('sbi.qbe.filtergridpanel.tooltip.notdef')
		           , dataIndex: 'boperator'
		           , editor: booleanOptColumnEditor
		           , renderer: function(val){
		        		return '<span style="color:green;">' + val + '</span>';  
			       }
		           , hideable: true
		           , hidden: false	
		           , sortable: false
		           
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