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
        	del: 'Delete',
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
        	clearBT: 'Clear All',
        	clearTT: 'Clear all selected fields',
        	
        	// Aggregation functions
        	agrFuncNames: [
        		'Nessuna',
        		'Somma',
        		'Media',
        		'Massimo',
        		'Minimo'        		
        	],
        	agrFuncDescriptions: [
        		'Non Applica nessuna funzione di aggregazione',
        		'Somma dei valori degli elementi all interno del gruppo',
        		'Media dei valori degli elementi all interno del gruppo',
        		'Massimo dei valori degli elementi all interno del gruppo',
        		'Minimo dei valori degli elementi all interno del gruppo'
        	]
        },
        
        
        // configuration objects
        
		initData : [
		        //['Somma','unit_sales','fact_sales_1997','Unità Vendute','','','si','', '']
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
			'NULL',
			'SUM',
		    'AVG',
		    'MAX',
		    'MIN'
		],
		    
        grid : null,	
        
      
        
        // public methods
        init: function() {
        
        	
	 		
		    this.store.loadData(this.initData);
		    
		    
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
		       header:  this.labels.del,
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
		          it.eng.spagobi.engines.qbe.querybuilder.filterGrid.addRow(record);
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
		        ['NULL','Nessuno', 'Non applica nessuna ordinamento sulla colonna'],
		        ['ASC', 'Crescente', 'Ordina le righe del risultato in modo crescente rispetto al valore di questa colonna'],
		        ['DESC', 'Decrescente', 'Ordina le righe del risultato in modo decrescente rispetto al valore di questa colonna']
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
		              allowBlank: true,
		              editable:false,
		              store: aggregationFunctionsStore,
		              displayField:'nome',
		              typeAhead: true,
		              mode: 'local',
		              triggerAction: 'all',
		              emptyText:'Select a state...',
		              selectOnFocus:true
		            })
		        },{
		           header: this.labels.order,
		           dataIndex: 'order',
		           width: 75,
		           editor: new Ext.form.ComboBox({
		              allowBlank: true,
		              editable:false,
		              store: orderingTypesStore,
		              displayField:'nome',
		              typeAhead: true,
		              mode: 'local',
		              triggerAction: 'all',
		              emptyText:'Select a state...',
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
		        store: this.store,
		        cm: cm,
		        clicksToEdit:1,
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
		        // inline toolbars
		        tbar:[{
		            text: this.labels.hideBT,
		            tooltip:this.labels.hideTT,
		            iconCls:'option'
		        }, '-', {
		            text: this.labels.groupBT,
		            tooltip: this.labels.groupTT,
		            iconCls:'option'
		        },'-',{
		            text: this.labels.addBT,
		            tooltip: this.labels.addTT,
		            iconCls:'option'
		        },'-',{
		            text: this.labels.clearBT,
		            tooltip: this.labels.clearTT,
		            iconCls:'remove'
		        }],
		        iconCls:'icon-grid'        
		    });
		    cm.defaultSortable = true;		 
		    
		         
		},  
	  
		addRow : function(config) {
		  var record = new this.Record({
		       funct: '',
		       order: '',
		       alias: 
		       '',
		       id: config.data['id'], 
		       entity: config.data['entity'], 
		       field: config.data['field'],
		       visible: 'si' 
		    });
		    
		    this.store.add(record); 
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
				jsonStr += 	'"visible" : "' + tmpRec.data['visible'] + '"';	
				jsonStr += '}';	
			}
			jsonStr += ']';
			
			return jsonStr;
		}
	};
	
}();

