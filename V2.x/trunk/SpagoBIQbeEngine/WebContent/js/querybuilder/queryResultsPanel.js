/**
  * Qbe - query result panel
  * by Andrea Gioia
  */

var cm;
var store;
var reader = new Ext.data.JsonReader();
var execServiceUrl = it.eng.spagobi.engines.qbe.serviceregistry.module.getServiceUrl('EXEC_QUERY_ACTION');
var exportServiceUrl = it.eng.spagobi.engines.qbe.serviceregistry.module.getServiceUrl('EXPORT_RESULT_ACTION');

var storeConfig = {

        params: {start:0,
                 limit: 25 },
        proxy: new Ext.data.HttpProxy({
           url: execServiceUrl,
           success: function(response){
           	 var o = Ext.util.JSON.decode( response.responseText );
           	 if(o.results == 0) {
           		alert("Query returns no data.");
           	 }           
           },
   		   failure: it.eng.spagobi.engines.qbe.exceptionhandler.module.handleFailure
           
        }),

        // create reader that reads the Topic records
        reader: reader,

        // turn on remote sorting
        remoteSort: true
    };

var getQueryResultsPanel = function() {
       
    // create the Data Store
    store = new Ext.data.Store( storeConfig );
    
    store.on('metachange', updateColumnModel);
    /*
    store.proxy.on('loadexception', function(proxy, options, response){          
    	alert(response.responseText);
    	alert(response.toSource());   
    	alert(options.toSource());  
    	alert(proxy.toSource());            
    }); 
    */
    
    cm = new Ext.grid.ColumnModel([
        new Ext.grid.RowNumberer(),
        {
           header: "Data",
           dataIndex: 'data',
           width: 75
        }
    ]);
    
    // create the Grid
    var grid = new Ext.grid.EditorGridPanel({
        store: store,
        cm: cm,
        clicksToEdit:1,
        style:'padding:10px',
        frame: true,
        border:true,  
        title: 'Results',   
        collapsible:true,
        //loadMask: true,
        viewConfig: {
            forceFit:true,
            enableRowBody:true,
            showPreview:true
        },
        // inline toolbars
        tbar:[{
            tooltip:'Export in pdf',
            iconCls:'pdf',
            handler: exportResultToPdf
        },{
            tooltip:'Export in rtf',
            iconCls:'rtf',
            handler: exportResultToRtf
        },{
            tooltip:'Export in xls',
            iconCls:'xls',
            handler: exportResultToXls
        },{
            tooltip:'Export in csv',
            iconCls:'csv',
            handler: exportResultToCsv
        },{
            tooltip:'Export in jrxml',
            iconCls:'jrxml',
            handler: exportResultToJrxml
        }],
        bbar: new Ext.PagingToolbar({
        	//width:200,
            pageSize: 25,
            store: store,
            displayInfo: true,
            displayMsg: 'Displaying topics {0} - {1} of {2}',
            emptyMsg: "No topics to display"
        })
        //iconCls:'icon-grid'        
    });   
    
    return  grid;
};

var execQuery = function() {  
  store.load( storeConfig );
}

var exportResultToCsv = function() {
	exportResult('text/csv');
}

var exportResultToRtf = function() {
	exportResult('application/rtf');
}

var exportResultToXls = function() {
	exportResult('application/vnd.ms-excel');
}

var exportResultToPdf = function() {
	exportResult('application/pdf');
}

var exportResultToJrxml = function() {
	exportResult('text/jrxml');
}

var exportResult = function(mimeType) {
	var form = document.getElementById('form');
	form.action = exportServiceUrl + '&MIME_TYPE=' + mimeType +'&RESPONSE_TYPE=RESPONSE_TYPE_ATTACHMENT';
	form.submit();
}

var updateColumnModel = function( store, meta ) {
   meta.fields[0] = new Ext.grid.RowNumberer();
   cm.setConfig(meta.fields);
}
