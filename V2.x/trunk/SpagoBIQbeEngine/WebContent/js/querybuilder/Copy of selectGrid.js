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
 
selectGridComponent.grid;
selectGridComponent.store;
selectGridComponent.Record;
 
selectGridComponent.init = function() {
 
    // initialization data
    var myData = [
        //['Somma','unit_sales','fact_sales_1997','Unità Vendute','','','si','', '']
    ];

    // create the data store
    var store = new Ext.data.SimpleStore({
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
    });
    
    // initialize data store
    store.loadData(myData);
    
    // create row structure
    selectGridComponent.Record = Ext.data.Record.create([
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
    ]);    
    
    
    
    // columns definition
    var visibleCheckColumn = new Ext.grid.CheckColumn({
       header: "Visible",
       dataIndex: 'visible',
       width: 55
    });
    
    var groupCheckColumn = new Ext.grid.CheckColumn({
       header: "Group",
       dataIndex: 'group',
       width: 55
    });
    
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
    
    var filterButtonColumn = new Ext.grid.ButtonColumn({
       header: "Filtra",
       dataIndex: 'filter',
       imgSrc: '../img/querybuilder/filter.png',
       
       clickHandler:function(e, t){
          var index = this.grid.getView().findRowIndex(t);
          var record = this.grid.store.getAt(index);
          it.eng.spagobi.engines.qbe.querybuilder.filterGrid.addRow(record);
       },
       
       width: 55
    });
    
    
    
    var aggregationFunctionsData = [
        ['NULL','Nessuna', 'Non Applica nessuna funzione di aggregazione'],
        ['SUM', 'Somma', 'Somma dei valori degli elementi all interno del gruppo'],
        ['AVG', 'Media', 'Media dei valori degli elementi all interno del gruppo'],
        ['MAX', 'Massimo', 'Massimo dei valori degli elementi all interno del gruppo'],
        ['MIN', 'Minimo', 'Minimo dei valori degli elementi all interno del gruppo']
    ];
    
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
        /*{
           header: "Id",
           dataIndex: 'id',
           visible: false
        }, */{
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
           width: 75,
           editor: new Ext.form.TextField({
               allowBlank: true
           })
        },{
           header: "Function",
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
           header: "Ordinamento",
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
    var grid = new Ext.grid.EditorGridPanel({
        store: store,
        cm: cm,
        clicksToEdit:1,
        plugins: [visibleCheckColumn, groupCheckColumn, delButtonColumn, filterButtonColumn],
        style:'padding:10px',
        frame: true,
        region:'center',
        width: 1100,  
        height: 350,
        border:true,  
        title: 'Select Fields',   
        collapsible:true,
        viewConfig: {
            forceFit:true
        },
        
        
        
        // inline toolbars
        tbar:[{
            text:'Hide non-visible',
            tooltip:'Hide all non visible fields',
            iconCls:'option'
        }, '-', {
            text:'Group by entity',
            tooltip:'Group fileds by parent entity',
            iconCls:'option'
        },'-',{
            text:'Add calculated',
            tooltip:'Add an ad-hoc calculated field (i.e. valid only for this query)',
            iconCls:'option'
        },'-',{
            text:'Clear All',
            tooltip:'Clear all selected fields',
            iconCls:'remove'
        }],
        iconCls:'icon-grid'        
    });
    cm.defaultSortable =true;
            

	/*
    var myNewRecord = new selectGridComponent.Record({
        funct: 'Somma',
        field: 'store_sales',
        entity: 'fact_sales_1997',
        alias: 'Prezzo di vendita',
        order: 'Decrescente',
        group: '',
        visible: 'si'
    });
    store.add(myNewRecord);
     var myNewRecord = new selectGridComponent.Record({
        funct: '',
        field: 'brand_name',
        entity: 'product',
        alias: 'Brand Name',
        order: '',
        group: 'si',
        visible: 'si'
    });
    store.add(myNewRecord);
	*/
    
    
    return selectGridComponent.grid = grid;
    
  };
  
  
     
selectGridComponent.addRow = function(config) {
  var record = new selectGridComponent.Record({
       funct: '',
       order: '',
       id: config.data['id'], 
       entity: config.data['entity'], 
       field: config.data['field'],
       visible: 'si' 
    });
    
    selectGridComponent.grid.store.add(record); 
};

selectGridComponent.getSelectedFields = function() {
	var tmpStore = selectGridComponent.grid.store;
	var recNo = tmpStore.getTotalCount();
	var jsonStr = '[';
	for(i = 0; i < tmpStore.getCount(); i++) {
		var tmpRec = tmpStore.getAt(i);
		if(i != 0) jsonStr += ',';
		jsonStr += '{';
		jsonStr += 	'"id" : "' + tmpRec.data['id'] + '",';	
		jsonStr += 	'"entity" : "' + tmpRec.data['entity'] + '",';	
		jsonStr += 	'"field"  : "' + tmpRec.data['field']  + '",';	
		jsonStr += 	'"alias"  : "' + tmpRec.data['alias']  + '",';	
		jsonStr += 	'"group"  : "' + tmpRec.data['group']  + '",';
		jsonStr += 	'"order"  : "' + tmpRec.data['order']  + '",';
		jsonStr += 	'"funct"  : "' + tmpRec.data['funct']  + '"';
		jsonStr += '}';	
	}
	jsonStr += ']';
	
	return {record: jsonStr};
};