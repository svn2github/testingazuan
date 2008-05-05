/**
  * Qbe - filterGrid Component
  * by Andrea Gioia
  */


Ext.namespace('it.eng.spagobi.engines.qbe.querybuilder.filterGrid');

// shorthand alias
var filterGridComponent = it.eng.spagobi.engines.qbe.querybuilder.filterGrid;
 
filterGridComponent.grid;
filterGridComponent.store;
filterGridComponent.Record;


/**
  * Initialize the filterGrid Component.Do not use public variables of this component before intialization because
  * they maybe not valorized.
  */
filterGridComponent.init = function() {
    
    
    // initialization data
    var myData = [
        //['ProductClass', 'product_family', 'Famiglia di prodotto', 'uguale a', 'Food', 'Static value', 'X']
    ];

    // create the data store
    var store = new Ext.data.SimpleStore({
        fields: [
           {name: 'id'},
           {name: 'entity'},
           {name: 'field'},
           {name: 'alias'},
           {name: 'operator'},
           {name: 'value'},
           {name: 'type'},
           {name: 'del'}          
        ]
    });
    
    // initialize data store
    store.loadData(myData);
    
    // create row structure
    filterGridComponent.Record = Ext.data.Record.create([
      {name: 'id', type: 'string'},
      {name: 'entity', type: 'string'},
      {name: 'field', type: 'string'},
      {name: 'alias', type: 'string'},
      {name: 'operator', type: 'string'},
      {name: 'value', type: 'string'},
      {name: 'type', type: 'string'},
      {name: 'del', type: 'bool'}
    ]);     
    
    
    var delButtonColumn = new Ext.grid.ButtonColumn({
       header: "Cancella",
       dataIndex: 'del',
       imgSrc: '../img/querybuilder/delete.gif',
       clickHandler:function(e, t){
          var index = this.grid.getView().findRowIndex(t);
          var record = this.grid.store.getAt(index);
          this.grid.store.remove(record);
       },
       width: 55
    });
    
    var filterFunctionsData = [
        ['EQUAL', 'uguale a', 'SSe è uguale alla striga specificata come valore del filtro'],
        ['NOTEQUAL', 'non uguale a', 'SSe non è uguale alla striga specificata come valore del filtro'],
        ['START WITH', 'comincia per', 'SSe termina comincia con una striga uguale a quella specificata come valore del filtro'],
        ['END WITH', 'finisce con', 'SSe termina con una striga uguale a quella specificata come valore del filtro'],
        ['MATCH', 'match', 'SSe è conforme alla regular esperssion specificata come valore del filtro']
    ];
    
    var filterFunctionsStore = new Ext.data.SimpleStore({
        fields: ['funzione', 'nome', 'descrizione'],
        data : filterFunctionsData 
    });
    
    var cm = new Ext.grid.ColumnModel([
        new Ext.grid.RowNumberer(),
        {
           header: "Entity",
           dataIndex: 'entity',
           width: 75
        },{
           id:'field',
           header: "Field",
           dataIndex: 'field',
           width: 75
        },{
           header: "Alias",
           dataIndex: 'alias',
           width: 75
        },{
           header: "Operator",
           dataIndex: 'operator',
           editor: new Ext.form.ComboBox({
              allowBlank: true,
              editable:false,
              store: filterFunctionsStore,
              displayField:'nome',
              typeAhead: true,
              mode: 'local',
              triggerAction: 'all',
              emptyText:'Select a state...',
              selectOnFocus:true
            }),
           width: 75
        },{
           header: "Value",
           dataIndex: 'value',
           editor: new Ext.form.TextField({
               allowBlank: true
           }),
           width: 75
        },{
           header: "Value Type",
           dataIndex: 'type',
           width: 75
        },
        delButtonColumn
    ]);
    cm.defaultSortable =true;
    
    // create the Grid
    var grid = new Ext.grid.EditorGridPanel({
        store: store,
        cm: cm,
        clicksToEdit:1,
        plugins: [delButtonColumn],
        style:'padding:10px',
        frame: true,
        region:'center',
        width: 1100,  
        height: 300,
        border:true,  
        title: 'Filters',   
        collapsible:true,
        viewConfig: {
            forceFit:true
        },
        
        
        
        // inline toolbars
        tbar:[{
            text:'Clear All',
            tooltip:'Clear all',
            iconCls:'remove'
        }],
        iconCls:'icon-grid'        
    });
    
    filterGridComponent.grid = grid;
};

/**
 * Add a new row to the filter grid. 
 *
 * config: a filterGridComponent.Record object used as initial configuration for the added record. 
 */
filterGridComponent.addRow = function(config) {
  var record = new filterGridComponent.Record({
  	   id: config.data['id'], 
       type:'Static Value',
       operator:'uguale a',
       value: '',
       alias: config.data['alias'],
       entity: config.data['entity']  , 
       field: config.data['field']  
    });
    
    filterGridComponent.grid.store.add(record); 
};

filterGridComponent.getRowsAsJSONParams = function() {
			var jsonStr = '[';
			for(i = 0; i <  filterGridComponent.grid.store.getCount(); i++) {
				var tmpRec =  filterGridComponent.grid.store.getAt(i);
				if(i != 0) jsonStr += ',';
				jsonStr += '{';
				jsonStr += 	'"id" : "' + tmpRec.data['id'] + '",';	
				jsonStr += 	'"entity" : "' + tmpRec.data['entity'] + '",';	
				jsonStr += 	'"field"  : "' + tmpRec.data['field']  + '",';	
				jsonStr += 	'"alias"  : "' + tmpRec.data['alias']  + '",';	
				jsonStr += 	'"operator"  : "' + tmpRec.data['operator']  + '",';
				jsonStr += 	'"value"  : "' + tmpRec.data['value']  + '",';
				jsonStr += 	'"type"  : "' + tmpRec.data['type']  + '",';
				jsonStr += '}';	
			}
			jsonStr += ']';
			
			return jsonStr;
}