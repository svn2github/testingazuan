	
	var cmX;
	var storeX;
	var readerX = new Ext.data.JsonReader();
	var danielaServiceUrl = it.eng.spagobi.engines.qbe.serviceregistry.module.getServiceUrl('DANIELA_ACTION');
	
	
	var storeConfigX = {

        params: {start:0,
                 limit: 7 },
        proxy: new Ext.data.HttpProxy({
           url: danielaServiceUrl
        }),

        // create reader that reads the Topic records
        reader: readerX,

        // turn on remote sorting
        remoteSort: true
    };
   
    
   var createGridNodi = function() {
   	  // create the Data Store
      storeX = new Ext.data.Store( storeConfigX );
      storeX.on('metachange', updateColumnModelX);
    
    
      cmX = new Ext.grid.ColumnModel([
        //new Ext.grid.RowNumberer(),
        {
           header: "Data",
           dataIndex: 'data',
           width: 75
        }
      ]);	
   		
    	var grid = new Ext.grid.GridPanel({
	        store: storeX,
	        cm: cmX,
	        clicksToEdit:1,
	        style:'padding:10px',
	        frame: true,
	        border:true,  
	        title: 'Results',   
	        collapsible:true,
	        viewConfig: {
	            forceFit:true,
	            enableRowBody:true,
	            showPreview:true
	        },
	               
	        title:'Nodi',	      
	        stripeRows: true,
	        autoExpandColumn: 1,
	        tbar: new Ext.PagingToolbar({
	            pageSize: 7,
	            store: storeX,
	            displayInfo: true,
	            displayMsg: 'Visualizzazione nodi {0} - {1} of {2}',
	            emptyMsg: "Non esistono nodi"
	        })
    	});
    
    	//storeX.load( storeConfigX );
    	return grid;
   }
  
   var updateColumnModelX = function( store, meta ) {
	   //meta.fields[0] = new Ext.grid.RowNumberer();
	   cmX.setConfig(meta.fields);
	} 
    
