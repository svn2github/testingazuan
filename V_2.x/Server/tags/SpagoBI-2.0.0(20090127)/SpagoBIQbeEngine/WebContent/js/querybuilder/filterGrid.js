 /**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005 Engineering Ingegneria Informatica S.p.A.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

**/

/**
  * Qbe - filterGrid Component
  * by Andrea Gioia
  */


Ext.namespace('it.eng.spagobi.engines.qbe.querybuilder.filterGrid');

// shorthand alias
var filterGridComponent = it.eng.spagobi.engines.qbe.querybuilder.filterGrid;


// create application
it.eng.spagobi.engines.qbe.querybuilder.filterGrid.app = function() {
    // do NOT access DOM from here; elements don't exist yet
 
    // private variables
   	var idCount = 0;
  
 	
    // public space
    return {
       
        // public properties
        
        
        
        // string to translate
        
        labels : {
        	title: 'Filters',

        	// column headers
        	fname: 'Filter Name',
        	entity: 'Entity',
        	field: 'Field',
        	alias: 'Alias',
        	operator: 'Operator',
        	value: 'Operand',
        	type: 'Operand Type',
        	boperator: 'Bol.Connector',
        	
        	// buttons 
        	deleteBT: 'Delete',
        	deleteTT: 'Delete selected filters',
        	clearBT: 'Clear All',
        	clearTT: 'Clear all filters',
        	  
        	// Boolean operators
        	filterBooleanOpNames: [
        		'AND',
        		'OR'     		
        	],
        	
        	filterBooleanOpDescriptions: [
        		'Connect this filter and the next one using the boolean operator AND',
        		'Connect this filter and the next one using the boolean operator OR'    		
        	],
        	
        	
        	// Aggregation functions
        	filterFuncNames: [
        		'none',
        		'equals to',
        		'not equals to',
        		'greater than',
        		'equals or greater than',
        		'less than',
        		'equals or less than',
        		'starts with',
        		'not starts with',
        		'ends with',
        		'not ends with',
        		'contains',
        		'not contains',
        		//'left join',
        		//'right join',
        		'between',
        		'not between',
        		'in',
        		'not in',
        		'not null',
        		'is null'        		       		
        	],
        	
        	filterFuncDescriptions: [
        		'no filter applayed',
        		'true iff the field\'s value is equal to filter\'s value',
        		'true iff the field\'s value is not equal to filter\'s value',
        		'true iff the field\'s value is greater than filter\'s value',
        		'true iff the field\'s value is equal or greater than filter\'s value',
        		'true iff the field\'s value is less than filter\'s value',
        		'true iff the field\'s value is equal or less than filter\'s value',
        		'true iff the field\'s value starts with filter\'s value',
        		'true iff the field\'s value doesn\'t start with filter\'s value',
        		'true iff the field\'s value ends with filter\'s value',
        		'true iff the field\'s value doesn\'t end with filter\'s value',
        		'true iff the field\'s value contains filter\'s value',
        		'true iff the field\'s value doesn\'t contain filter\'s value',
        		//'true iff the field\'s value is in left join relationship with the other field value',
        		//'true iff the field\'s value is in right join relationship with the other field value',
        		'true iff the field\'s value is between the range specified in the filter value',
        		'true iff the field\'s value is not between the range specified in the filter value',
        		'true iff the field\'s value is equal to one of the values specified in the filter value',
        		'true iff the field\'s value is not equal to any of the values specified in the filter value',
        		'true iff the field\'s value is not null',
        		'true iff the field\'s value is null'
        	]
        },
        
        
        filterBooleanOp: [
			'AND',
			'OR'
       	],
        
        filterFunctions : [
			'NONE',
			'EQUALS TO',
		    'NOT EQUALS TO',
		    'GREATER THAN',
		    'EQUALS OR GREATER THAN',
		    'LESS THAN',
		    'EQUALS OR LESS THAN',
		    
		    'STARTS WITH',
		    'NOT STARTS WITH',
		    'ENDS WITH',
		    'NOT ENDS WITH',
		    'CONTAINS',
		    'NOT CONTAINS',
		    
		    //'LEFT JOIN ON',
		    //'RIGHT JOIN ON',
		    
		    'BETWEEN',
		    'NOT BETWEEN',
		    'IN',
		    'NOT IN',
		    
		    'NOT NULL',
		    'IS NULL'
		],
		
		getComboDataStore : function(ids, names, descriptions) {
			var data = [];
		    for(i = 0; i < ids.length; i++) {
		    	data[i] = [
		    		ids[i],
		    		names[i],
		    		descriptions[i]
		    	];
		    } 
		    
		    var dataStore = new Ext.data.SimpleStore({
		        fields: ['funzione', 'nome', 'descrizione'],
		        data : data 
		    });
		    
		    return dataStore;
		},
        
        
        // configuration objects
         
        initData : [],
         
        store : new Ext.data.SimpleStore({
	        fields: [
	           {name: 'fname'},
	           {name: 'id'},
	           {name: 'entity'},
	           {name: 'field'},
	           {name: 'operator'},
	           {name: 'operand'},
	           {name: 'otype'},	           
	           {name: 'odesc'},
	           {name: 'boperator'},    
	           {name: 'del'}          
	        ]
    	}),
    	
    	// create row structure
	    Record : Ext.data.Record.create([
	      {name: 'fname', type: 'string'},
	      {name: 'id', type: 'string'},
	      {name: 'entity', type: 'string'},
	      {name: 'field', type: 'string'},
	      {name: 'operator', type: 'string'},
	      {name: 'operand', type: 'auto'},
	      {name: 'otype', type: 'string'},
	      {name: 'odesc', type: 'string'},
	      {name: 'boperator', type: 'string'},
	      {name: 'del', type: 'bool'}
	    ]), 
	    
	    
 
		grid : null,

		/**
		  * Initialize the filterGrid Component.Do not use public variables of this component before intialization because
		  * they maybe not valorized.
		  */
		init : function(query) {
		         
		    // initialize data store
		    this.store.loadData( this.initData );
		    
		    if(query && query.filters && query.filters.length > 0){
		    	this.loadSavedData(query);
		    }
		        
		    var delButtonColumn = new Ext.grid.ButtonColumn({
		       header: this.labels.deleteBT,
		       dataIndex: 'del',
		       imgSrc: '../img/querybuilder/delete.gif',
		       clickHandler:function(e, t){
		          var index = this.grid.getView().findRowIndex(t);
		          var record = this.grid.store.getAt(index);
		          this.grid.store.remove(record);
		          //this.setWizardExpression(false);
		       },
		       width: 55
		    });
		    
		    var filterFunctionsStore = this.getComboDataStore(this.filterFunctions, this.labels.filterFuncNames, this.labels.filterFuncDescriptions);
		    var filterBooleanOpStore = this.getComboDataStore(this.filterBooleanOp, this.labels.filterBooleanOpNames, this.labels.filterBooleanOpDescriptions);
		    
		    var handleChange = function(){
        						//alert('change'); 
        						this.setWizardExpression(false);        						
        					};
		    
		    var cb = new Ext.form.ComboBox({
		           	  tpl: '<tpl for="."><div ext:qtip="{nome}: {descrizione}" class="x-combo-list-item">{funzione}</div></tpl>',	
		           	  store: filterBooleanOpStore, 
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
        					fn: handleChange
        					, scope: this
        				}
		              }
		            });
		    
		    /*
		    var filterFunctionsData = [];
		    for(i = 0; i < this.filterFunctions.length; i++) {
		    	filterFunctionsData[i] = [
		    		this.filterFunctions[i],
		    		this.labels.filterFuncNames[i],
		    		this.labels.filterFuncDescriptions[i]
		    	];
		    } 
		    
		    var filterFunctionsStore = new Ext.data.SimpleStore({
		        fields: ['funzione', 'nome', 'descrizione'],
		        data : filterFunctionsData 
		    });
		    */
		    
		    var boperatorRenderer = function(val){
        	 return '<span style="color:green;">' + val + '</span>';  
        	}; 
		    
		    var cm = new Ext.grid.ColumnModel([
		        new Ext.grid.RowNumberer(),
		        {
		           header: this.labels.fname,
		           dataIndex: 'fname',
		           editor: new Ext.form.TextField({
		             allowBlank: false							               
		           }),
		           width: 75
		        },{
		           header: this.labels.entity,
		           dataIndex: 'entity',
		           width: 75
		        },{
		           id:'field',
		           header: this.labels.field,
		           dataIndex: 'field',
		           width: 75
		        }, {
		           header: this.labels.operator,
		           dataIndex: 'operator',
		           autocomplete: 'off',
		           editor: new Ext.form.ComboBox({
		           	  tpl: '<tpl for="."><div ext:qtip="{nome}: {descrizione}" class="x-combo-list-item">{funzione}</div></tpl>',	
		           	  store: filterFunctionsStore, 
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
		            }),
		           width: 75
		        },{
		           header: this.labels.value,
		           dataIndex: 'odesc',
		           editor: new Ext.form.TextField({
		               allowBlank: true,
		               
		               listeners : {
		              
					   }		               
		           }),
		           width: 75
		        },{
		           header: this.labels.type,
		           dataIndex: 'otype',
		           width: 75
		        }, {
		           header: this.labels.boperator,
		           dataIndex: 'boperator',
		           editor: cb,
		           renderer:this.boperatorRenderer,
		           width: 75
		        },
		        delButtonColumn
		    ]);
		    
		    
		    
		    
		    cm.defaultSortable =true;
		    
		    // create the Grid
		    this.grid = new Ext.grid.EditorGridPanel({
		    	id: 'filter-grid',
		        store: this.store,
		        cm: cm,
		        sm : new Ext.grid.RowSelectionModel(),
		        clicksToEdit:2,
		        plugins: [delButtonColumn],
		        style:'padding:10px',
		        frame: true,
		        region:'center',
		        width: 1100,  
		        height: 300,
		        border:true,  
		        title: this.labels.title,   
		        collapsible:true,
		        viewConfig: {
		            forceFit:true
		        },
		        enableDragDrop:true,
        		ddGroup: 'gridDDGroup',
		        
		        
		        
		        // inline toolbars
		        tbar:[
		        {
		            text: this.labels.clearBT,
		            tooltip: this.labels.clearTT,
		            iconCls:'remove',
		            listeners: {
		            	'click': {
        					fn: this.deleteGrid,
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
		            //handler: this.showWizard   		
		        } 
		        
		        ],
		        iconCls:'icon-grid'        
		    });
		    
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
		    
		    this.grid.store.on('remove', function(e){
		    	this.setWizardExpression(false);
		    }, this);
		},


		loadSavedData : function(query) {
	  		for(var i = 0; i < query.filters.length; i++) {
	  			var filter = query.filters[i];
	  			var record = new this.Record(filter);
	  			this.store.add(record); 
	  		}
	  		
	  		if(query.expression) {
	  			var expStr = this.loadSavedExpression(query.expression);
	  			it.eng.spagobi.engines.qbe.filterwizard.setExpression( expStr ); 
	  			this.setWizardExpression(true); // togle oce fixed
	  		}
	  		
	  	},
	  	
	  	loadSavedExpression : function(expression) {
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
	  	},
	  	
	  	/*
	  	 String str = "";
		
		String type = filterExp.getType();
		if("NODE_OP".equalsIgnoreCase( type )) {
			for(int i = 0; i < filterExp.getChildNodes().size(); i++) {
				ExpressionNode child = (ExpressionNode)filterExp.getChildNodes().get(i);
				String childStr = getFilterExpAsString(child);
				if("NODE_OP".equalsIgnoreCase( child.getType() )) {
					childStr = "(" + childStr + ")";
				}
				str += (i==0?"": " " + filterExp.getValue());
				str += " " + childStr;
			}
		} else {
			str += filterExp.getValue();
		}
		
		return str;
	  	 */
		
		/**
		 * Add a new row to the filter grid. 
		 *
		 * config: a filterGridComponent.Record object used as initial configuration for the added record. 
		 */
		addRow : function(config, i) {
		  var fname = 'filter' + (++idCount);
		  var newRow = {
		  	fname: fname, 
		  	id: '', 
		  	entity: '',
		  	field: '',
		  	operator:'',
		  	operand: '',
		    otype: 'Static Value',		    
		    odesc: '',
		    boperator:'AND'
		  };
		  
		  Ext.apply(newRow, config.data);
		  
		  
		  var record = new this.Record(newRow);
		   
		    if(i != undefined) {
      			this.grid.store.insert(i, record); 
    		} else {
      			this.grid.store.add(record); 
    		}
		},
		
		getRowsAsJSONParams : function() {
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
						jsonStr += 	'"otype"  : "' + tmpRec.data['otype']  + '",';
						jsonStr += 	'"odesc"  : "' + tmpRec.data['odesc']  + '",';
						jsonStr += 	'"boperator"  : "' + tmpRec.data['boperator']  + '"';
						
						jsonStr += '}';	
					}
					jsonStr += ']';
					
					return jsonStr;
		},
		
		syncWizardExpressionWithGrid: function() {
			var exp = '';
			for(i = 0; i <  this.grid.store.getCount(); i++) {
				var tmpRec =  this.grid.store.getAt(i);
				if(i > 0) {
					exp += ' ' + this.grid.store.getAt(i-1).data['boperator'] + ' $F{' +  tmpRec.data['fname'] + '}';
				} else {
					exp += '$F{' + tmpRec.data['fname'] + '}';
				}
			}
			it.eng.spagobi.engines.qbe.filterwizard.setExpression(exp);
			
		},
		
		getFiltersExpressionAsJSON : function() {	
			if(!this.isWizardExpression()) {
				this.syncWizardExpressionWithGrid();
			}
			var json = it.eng.spagobi.engines.qbe.filterwizard.getExpressionAsJSON();
			
			return json;
		},
		
		showWizard: function() {	
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
		},
		
		setWizardExpression: function(b) {
			if(b) {
				this.wizardExpression = true;
				//alert('I will use the xpression defined into wizard');
			} else {
				this.wizardExpression = false;
				//alert('I will use inline expression');
			}
		},
		
		isWizardExpression: function() {
			return this.wizardExpression;
		},
		
		deleteGrid : function() {
			this.grid.store.removeAll();
		},
		
		wizardExpression: false
		
		
	};
	
	
	
}();