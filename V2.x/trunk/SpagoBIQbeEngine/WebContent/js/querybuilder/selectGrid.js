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
  * Qbe - selectGrid Component
  * by Andrea Gioia
  */
  
Ext.namespace('it.eng.spagobi.engines.qbe.querybuilder.selectGrid');

// shorthand alias
var selectGridComponent = it.eng.spagobi.engines.qbe.querybuilder.selectGrid;
 
 
// create application
it.eng.spagobi.engines.qbe.querybuilder.selectGrid.app = function() {
    // do NOT access DOM from here; elements don't exist yet
 
    // private variables
   	
  
 	
    // public space
    return {
       
        // public properties
        
        
        
        // string to translate
        
        labels : {
        	title: 'Select Fields',
        	
        	// column headers
        	visible: 'Visible',
        	group: 'Group',
        	filter: 'Filter',
        	entity: 'Entity',
        	alias: 'Alias',
        	order: 'Order',
        	group: 'Group',
        	funct: 'Function',
        	field: 'Field',
        	
        	// buttons    
        	hideBT: 'Hide non-visible',
        	hideTT: 'Hide all non visible fields',
        	groupBT: 'Group by entity',
        	groupTT: 'Group fileds by parent entity',
        	addBT: 'Add calculated',
        	addTT: 'Add an ad-hoc calculated field (i.e. valid only for this query)',
        	deleteBT: 'Delete',
        	deleteTT: 'Delete selected filed',
        	clearBT: 'Clear All',
        	clearTT: 'Clear all selected fields',
        	
        	// Aggregation functions
        	agrFuncNames: [
        		'none',
        		'sum',
        		'average',
        		'maximum',
        		'minimum'        		
        	],
        	agrFuncDescriptions: [
        		'No aggregation function applied',
        		'Return the sum of all values in group',
        		'Return the average of all values in group',
        		'Return the max of all values in group',
        		'Return the min of all values in group'
        	]
        },
        
        
        // configuration objects
        
		initData : [
		        	/*[
		        		'it.eng.spagobi.ProductClass:productFamily',
		        		'',
		        		'productClassId',
  						'ProductClass', 
  						'',
  						'',
  						'',
  						'si',
  						''
  					]*/
		],
		
		store : new Ext.data.SimpleStore({
		        fields: [
		           {name: 'id'},
		           {name: 'funct'},
		           {name: 'field'},
		           {name: 'entity'},
		           {name: 'alias'},
		           {name: 'order'},
		           {name: 'group'},
		           {name: 'visible'},
		           {name: 'del'}          
		        ]
		}),
		
		Record : Ext.data.Record.create([
		      {name: 'id', type: 'string'},
		      {name: 'funct', type: 'string'},
		      {name: 'field', type: 'string'},
		      {name: 'entity', type: 'string'},
		      {name: 'alias', type: 'string'},
		      {name: 'order', type: 'string'},
		      {name: 'group', type: 'string'},
		      {name: 'visible', type: 'bool'},
		      {name: 'filter', type: 'string'},
		      {name: 'del', type: 'string'}
		]),   
		
		aggregationFunctions : [
			'NONE',
        	'SUM',
        	'AVG',
        	'MAX',
        	'MIN'  
		],
		    
        grid : null,	
        
      
        
        // public methods
        init: function(query) {
        
        	this.store.loadData(this.initData);
		    
		    if(query && query.fields && query.fields.length > 0){
		    	this.loadSavedData(query);
		    }
		    
		    // columns definition
		    var visibleCheckColumn = new Ext.grid.CheckColumn({
		       header: this.labels.visible,
		       dataIndex: 'visible',
		       width: 55
		    });
		    
		    var groupCheckColumn = new Ext.grid.CheckColumn({
		       header:  this.labels.group,
		       dataIndex: 'group',
		       width: 55
		    });
		    
		     var delButtonColumn = new Ext.grid.ButtonColumn({
		       header:  this.labels.deleteBT,
		       dataIndex: 'delete',
		       imgSrc: '../img/querybuilder/delete.gif',
		       clickHandler:function(e, t){
		          var index = this.grid.getView().findRowIndex(t);
		          var record = this.grid.store.getAt(index);
		          this.grid.store.remove(record);
		       },
		       width: 55
		    });
		    
				    
		    var filterButtonColumn = new Ext.grid.ButtonColumn({
		       header:  this.labels.filter,
		       dataIndex: 'filter',
		       imgSrc: '../img/querybuilder/filter.png',
		       
		       clickHandler:function(e, t){
		          var index = this.grid.getView().findRowIndex(t);
		          var record = this.grid.store.getAt(index);
		          it.eng.spagobi.engines.qbe.querybuilder.filterGrid.app.addRow(record);
		       },
		       
		       width: 55
		    });
		    
		    var aggregationFunctionsData = [];
		    for(i = 0; i < this.aggregationFunctions.length; i++) {
		    	aggregationFunctionsData[i] = [
		    		this.aggregationFunctions[i],
		    		this.labels.agrFuncNames[i],
		    		this.labels.agrFuncDescriptions[i]
		    	];
		    }
		    
		    	    
		    var aggregationFunctionsStore = new Ext.data.SimpleStore({
		        fields: ['funzione', 'nome', 'descrizione'],
		        data : aggregationFunctionsData 
		    });
		    
		     var orderingTypesData = [
		        ['NONE','none', 'No ordering applied to the given colunm'],
		        ['ASC', 'ascending', 'Order values of the given column in asecnding way'],
		        ['DESC', 'descending', 'Order values of the given column in descending way']
		    ];
		    
		    
		    // simple array store
		    var orderingTypesStore = new Ext.data.SimpleStore({
		        fields: ['type', 'nome', 'descrizione'],
		        data : orderingTypesData 
		    });
		    
		  
		    var cm = new Ext.grid.ColumnModel([
		        new Ext.grid.RowNumberer(),
		        {
		           header: this.labels.entity,
		           dataIndex: 'entity',
		           width: 75
		        } ,{
		           id:'field',
		           header: this.labels.field,
		           dataIndex: 'field',
		           width: 75
		        },{
		           header: this.labels.alias,
		           dataIndex: 'alias',
		           width: 75,
		           editor: new Ext.form.TextField({
		               allowBlank: true
		           })
		        },{
		           header: this.labels.funct,
		           dataIndex: 'funct',
		           width: 75,
		           editor: new Ext.form.ComboBox({
		              tpl: '<tpl for="."><div ext:qtip="{nome}: {descrizione}" class="x-combo-list-item">{funzione}</div></tpl>',	
		              allowBlank: true,
		              editable:false,
		              store: aggregationFunctionsStore,
		              displayField:'funzione',
		              valueField:'funzione',
		              typeAhead: true,
		              mode: 'local',
		              triggerAction: 'all',
		              autocomplete: 'off',
		              emptyText:'Select a function...',
		              /*
		              listeners: {
		              	'change': {
        					fn: this.updateGroupByColumn
        					, scope: this
		              	}
        			  },
        			  */
		              selectOnFocus:true
		            })
		        },{
		           header: this.labels.order,
		           dataIndex: 'order',
		           width: 75,
		           editor: new Ext.form.ComboBox({
		           	  tpl: '<tpl for="."><div ext:qtip="{nome}: {descrizione}" class="x-combo-list-item">{type}</div></tpl>',	
		              allowBlank: true,
		              editable:false,
		              store: orderingTypesStore,
		              displayField:'type',
		              valueField:'type',
		              typeAhead: true,
		              mode: 'local',
		              triggerAction: 'all',
		              autocomplete: 'off',
		              emptyText:'Select ordering direction...',
		              selectOnFocus:true
		            })
		        },
		        groupCheckColumn, 
		        visibleCheckColumn,
		        filterButtonColumn,
		        delButtonColumn
		     
		    ]);
		    
		    
		    
		     
		
		    // create the Grid
		    this.grid = new Ext.grid.EditorGridPanel({
		    	id: 'select-grid',
		        store: this.store,
		        cm: cm,  
		        sm : new Ext.grid.RowSelectionModel(),
        		clicksToEdit:2,
		        plugins: [visibleCheckColumn, groupCheckColumn, delButtonColumn, filterButtonColumn],
		        style:'padding:10px',
		        frame: true,
		        region:'center',
		        width: 1100,  
		        height: 350,
		        border:true,  
		        title: this.labels.title,   
		        collapsible:true,
		        viewConfig: {
		            forceFit:true
		        },		       
		         enableDragDrop:true,
        		//ddText: 'drag and drop to change order',
        		ddGroup: 'gridDDGroup',
         
		        // inline toolbars
		        tbar:[{
		            text: this.labels.addBT,
		            tooltip: this.labels.addTT,
		            iconCls:'option'
		        },'-',{
		            text: this.labels.hideBT,
		            tooltip:this.labels.hideTT,
		            enableToggle: true,
		            iconCls:'option',
		            listeners: {
		            	'toggle': {
        					fn: this.hideNonVisibleRows,
        					scope: this
        				}
		            }
		        },'-',{
		            text: this.labels.clearBT,
		            tooltip: this.labels.clearTT,
		            iconCls:'remove',
		            listeners: {
		            	'click': {
        					fn: this.deleteGrid,
        					scope: this
        				}
		            }
		        }],
		        iconCls:'icon-grid'        
		    });
		    cm.defaultSortable = true;	
		    
		    this.grid.on("mouseover", function(e, t){
		    	var row;
		        this.targetRow = t;
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
		        var sm=this.getSelectionModel();
		        var ds = this.getStore()
		        var rows=sm.getSelections();
		        for (i = 0; i < rows.length; i++) {
		          this.store.remove( ds.getById(rows[i].id) );
		        }
		      }      
		    }, this.grid);	
		    
		    this.grid.store.on('update', function(e){
		    	this.updateGroupByColumn();
		    }, this); 
		    
		         
		},  
	  	
	  	loadInitData : function() {
	  		/*
	  		this.initData[0] = [	  		
		        		'it.eng.spagobi.ProductClass:productFamily',
		        		'',
		        		'productClassId',
  						'ProductClass', 
  						'',
  						'',
  						'',
  						'si',
  						''
  					];
  			*/
	  	},
	  	
	  	loadSavedData : function(query) {
	  		//alert('FIELD\'S GRID - loadSavedData');
	  		for(var i = 0; i < query.fields.length; i++) {
	  			var field = query.fields[i];
	  			var record = new this.Record(field);
	  			this.store.add(record); 
	  		}
	  		/*
	  		 var record = new this.Record({ id : 'it.eng.spagobi.ProductClass:productClassId',
			  entity : 'ProductClass',
			  field  : 'productClassId',
			  alias  : 'pippo',
			  group  : 'undefined',
			  order  : '',
			  funct  : '',
			  visible : 'si'
			 });
			 
			 this.store.add(record);
			 */ 
	  	},
	  	
		addRow : function(config, i) {		    
		   var record = new this.Record({
		       funct: '',
		       order: '',
		       alias: config.data['field'], 
		       id: config.data['id'], 
		       entity: config.data['entity'], 
		       field: config.data['field'],
		       visible: true 
		    });
	    
	    	//alert("C: " + i); 
	    
	    	if(i != undefined) {
	      		this.store.insert(i, record); 
	    	} else {
	      		this.store.add(record); 
	    	}
		},
	
		getRowsAsJSONParams : function() {
			var jsonStr = '[';
			for(i = 0; i < this.store.getCount(); i++) {
				var tmpRec = this.store.getAt(i);
				if(i != 0) jsonStr += ',';
				jsonStr += '{';
				jsonStr += 	'"id" : "' + tmpRec.data['id'] + '",';	
				jsonStr += 	'"entity" : "' + tmpRec.data['entity'] + '",';	
				jsonStr += 	'"field"  : "' + tmpRec.data['field']  + '",';	
				jsonStr += 	'"alias"  : "' + tmpRec.data['alias']  + '",';	
				jsonStr += 	'"group"  : "' + tmpRec.data['group']  + '",';
				jsonStr += 	'"order"  : "' + tmpRec.data['order']  + '",';
				jsonStr += 	'"funct"  : "' + tmpRec.data['funct']  + '",';
				jsonStr += 	'"visible" : ' + tmpRec.data['visible'] + '';	
				jsonStr += '}';	
			}
			jsonStr += ']';
			
			return jsonStr;
		},
		
		deleteGrid : function() {
			this.grid.store.removeAll();
		},
		
		hideNonVisibleRows : function(button, pressed) {
			
			this.grid.store.filterBy(function(record, id) {
				if(!pressed) return true; // show all
				
				return record.data['visible'];
			});
		},
		
		updateGroupByColumn : function() {
			
			var index = this.grid.store.findBy(function(record) {
				var isFunction = !(record.data['funct'] == undefined
						|| record.data['funct'].trim() == ''
							|| record.data['funct'] == 'NONE');
				return isFunction;
			});
						
			var groupFlag = (index == -1? '': 'true');			
						
			this.grid.store.each(function(record) {
				
				//alert('[' + record.data['funct'] + '] - ' + (record.data['funct'] == undefined));	
				//alert('[' + record.data['funct'] + '] - ' + (record.data['funct'].trim() == ''));	
				//alert('[' + record.data['funct'] + '] - ' + (record.data['funct'] == 'NONE'));	
				
					
				if( record.data['funct'] == undefined
						|| record.data['funct'].trim() == ''
							|| record.data['funct'] == 'NONE') {
						//alert('true');
						record.data['group'] = groupFlag;	
					} else {
						//alert('false');
						record.data['group'] = '';	
					}								
			});
			
						
			this.grid.getView().refresh();		
			
		}
		
	};
	
}();

