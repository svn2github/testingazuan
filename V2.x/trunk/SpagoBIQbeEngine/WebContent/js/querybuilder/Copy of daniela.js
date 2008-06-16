	
	var danielaServiceUrl = it.eng.spagobi.engines.qbe.serviceregistry.module.getServiceUrl('DANIELA_ACTION');
	
	var RecordDef = Ext.data.Record.create([
	    {name: 'name', mapping: 'name'},     
	    {name: 'occupation', mapping: 'occupation'},
	    {name: 'id', mapping: 'id'}                 
	]);

	var readerX = new Ext.data.JsonReader({
	    totalProperty: 'results',    
	    root: 'rows',                
	    id: 'id'                    
	}, RecordDef);
	
	
 	var nodiConfig = {
        params: {start:0,
                 limit: 5 },
        proxy: new Ext.data.HttpProxy({
	           url: danielaServiceUrl
	        }),
		reader: readerX,
		remoteSort: true,
		fields: [
	        {name: 'id'},
	        {name: 'name'},
	        {name: 'occupation'}
	        ]
    };
 
		
   var storeX; 
    
   var createGridNodi = function() {
	  storeX = new Ext.data.SimpleStore(nodiConfig);
      //storeX.load(nodiConfig);
      
    	var grid = new Ext.grid.GridPanel({
	        store: storeX,
	        title:'Nodi',
	        columns: [
	 	    {id:"id", header: "id", width: 30, sortable: false, dataIndex: 'id'},
	       	{header: "Name Test", width: 30, sortable: false, dataIndex: 'name'},
	        {header: "Occupation test", width: 150, sortable: true, dataIndex: 'occupation'}
	        ],
	        stripeRows: true,
	        autoExpandColumn: 1,
	        tbar: new Ext.PagingToolbar({
	            pageSize: 5,
	            store: storeX,
	            displayInfo: true,
	            displayMsg: 'Visualizzazione nodi {0} - {1} of {2}',
	            emptyMsg: "Non esistono nodi"
	        })
    	});
    
    
    return grid;
   }
  
    
    
