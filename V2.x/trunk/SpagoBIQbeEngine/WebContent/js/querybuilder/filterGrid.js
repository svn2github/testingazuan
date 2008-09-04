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
   	
  
 	
    // public space
    return {
       
        // public properties
        
        
        
        // string to translate
        
        labels : {
        	title: 'Filters',

        	// column headers
        	entity: 'Entity',
        	field: 'Field',
        	alias: 'Alias',
        	operator: 'Operator',
        	value: 'Value',
        	type: 'Value Type',
        	
        	// buttons 
        	deleteBT: 'Delete',
        	deleteTT: 'Delete selected filters',
        	clearBT: 'Clear All',
        	clearTT: 'Clear all filters',
        	  
        	
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
        		'true iff the field\'s value is not null',
        		'true iff the field\'s value is null',
        	]
        },
        
        
        
        // configuration objects
         
        initData : [],
         
        store : new Ext.data.SimpleStore({
	        fields: [
	           {name: 'id'},
	           {name: 'entity'},
	           {name: 'field'},
	          // {name: 'alias'},
	           {name: 'operator'},
	           {name: 'value'},
	           {name: 'type'},
	           {name: 'del'}          
	        ]
    	}),
    	
    	// create row structure
	    Record : Ext.data.Record.create([
	      {name: 'id', type: 'string'},
	      {name: 'entity', type: 'string'},
	      {name: 'field', type: 'string'},
	      //{name: 'alias', type: 'string'},
	      {name: 'operator', type: 'string'},
	      {name: 'value', type: 'string'},
	      {name: 'type', type: 'string'},
	      {name: 'del', type: 'bool'}
	    ]), 
	    
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
		    
		    'NOT NULL',
		    'IS NULL'
		],
 
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
		       },
		       width: 55
		    });
		    
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
		    
		    var cm = new Ext.grid.ColumnModel([
		        new Ext.grid.RowNumberer(),
		        {
		           header: this.labels.entity,
		           dataIndex: 'entity',
		           width: 75
		        },{
		           id:'field',
		           header: this.labels.field,
		           dataIndex: 'field',
		           width: 75
		        },/*{
		           header: this.labels.alias,
		           dataIndex: 'alias',
		           width: 75
		        },*/{
		           header: this.labels.operator,
		           dataIndex: 'operator',
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
		              selectOnFocus:true
		            }),
		           width: 75
		        },{
		           header: this.labels.value,
		           dataIndex: 'value',
		           editor: new Ext.form.TextField({
		               allowBlank: true
		           }),
		           width: 75
		        },{
		           header: this.labels.type,
		           dataIndex: 'type',
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
		        tbar:[{
		            text: this.labels.clearBT,
		            tooltip: this.labels.clearTT,
		            iconCls:'remove'
		        }],
		        iconCls:'icon-grid'        
		    });
		    
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
		},


		loadSavedData : function(query) {
	  		//alert('FILTER\'S GRID - loadSavedData');
	  		for(var i = 0; i < query.filters.length; i++) {
	  			var filter = query.filters[i];
	  			var record = new this.Record(filter);
	  			this.store.add(record);  
	  		}
	  	},
		
		/**
		 * Add a new row to the filter grid. 
		 *
		 * config: a filterGridComponent.Record object used as initial configuration for the added record. 
		 */
		addRow : function(config, i) {
		  var record = new this.Record({
		  	   id: config.data['id'], 
		       type:'Static Value',
		       operator:'',
		       value: '',
		       //alias: config.data['alias'],
		       entity: config.data['entity']  , 
		       field: config.data['field']  
		    });
		    
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
						jsonStr += 	'"id" : "' + tmpRec.data['id'] + '",';	
						jsonStr += 	'"entity" : "' + tmpRec.data['entity'] + '",';	
						jsonStr += 	'"field"  : "' + tmpRec.data['field']  + '",';	
						//jsonStr += 	'"alias"  : "' + tmpRec.data['alias']  + '",';	
						jsonStr += 	'"operator"  : "' + tmpRec.data['operator']  + '",';
						jsonStr += 	'"value"  : "' + tmpRec.data['value']  + '",';
						jsonStr += 	'"type"  : "' + tmpRec.data['type']  + '",';
						jsonStr += '}';	
					}
					jsonStr += ']';
					
					return jsonStr;
		}
		
	};
	
	
	
}();