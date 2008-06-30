/**
 *
 
 */


qx.Class.define("spagobi.test.DataService", {
  type : "static",
  statics : {
  	
  	loadEngineRecords: function() {
  		var records = {};
    	
    	records.meta =  this.loadEngineMeta();
    	records.rows =  this.loadEngineData();
    	
    	return records;
  	},
  	
  	loadEngineMeta: function() {
  		return [
    		{
	    		dataIndex: 'label',
	    		name: 'Label'
    		}, {
	    		dataIndex: 'name',
	    		name: 'Name'
    		}, {
	    		dataIndex: 'description',
	    		name: 'Description' 
    		}, {
	    		dataIndex: 'documentType',
	    		name: 'DocumentType'
    		}
    	];
  	},
  	
  	loadEngineData: function() {
  		return [
    		{
	        	id: '135',
	        	"label": 'JASPER',
	        	name: 'JasperReport Engine',
	        	description: 'Compatible with JasperReport engine v3.1',
	        	documentType: 'Map',
	        	engineType: 'External',
	        	useDataSet: false,
	        	useDataSource: true,
	        	dataSource: 'geo',
	        	"class": '',
	        	url: 'http://localhost:8080/SpagoBIJasperEngine/AdapterHTTP?ACTION_NAME=START_ACTION',
	        	driver: 'it.eng.spagobi.engines.drivers.QbeDriver'        	        	
        	}, {
	        	id: '137',
	        	"label": 'QBE',
	        	name: 'Qbe Engine',
	        	description: 'Query by Example',
	        	documentType: 'Map',
	        	engineType: 'External',
	        	useDataSet: false,
	        	useDataSource: true,
	        	dataSource: 'geo',
	        	"class": '',
	        	url: 'http://localhost:8080/SpagoBIQbeEngine/AdapterHTTP?ACTION_NAME=START_ACTION',
	        	driver: 'it.eng.spagobi.engines.drivers.QbeDriver'        	        	
        	}, {
	        	id: '138',
	        	"label": 'DASH',
	        	name: 'Dashboard Engine',
	        	description: 'Dashboard Engine',
	        	documentType: 'Map',
	        	engineType: 'Internal',
	        	useDataSet: false,
	        	useDataSource: true,
	        	dataSource: 'geo',
	        	"class": 'it.eng.spagobi.Dashboard',
	        	url: '',
	        	driver: ''        	        	
        	}
    	];
  	},
  	
  	
  	
  	
  	
  	loadDatasourceRecords: function() {
  		var records = {};
    	
    	records.meta =  this.loadDatasourceMeta();
    	records.rows =  this.loadDatasourceData();
    	
    	return records;
  	},
  	
  	loadDatasourceMeta: function() {
  		return [
    		{
	    		dataIndex: 'label',
	    		name: 'Label'
    		}, {
	    		dataIndex: 'description',
	    		name: 'Description' 
    		}, {
	    		dataIndex: 'type',
	    		name: 'Type'
    		}
    	];
  	},
  	
  	loadDatasourceData: function() {
  		return [
  			{       
          		id: '336',
	        	"label": 'foodmart',
	          	description: 'Connection to the DWH',
	          	Dialect: 'MYSQL',
	          	type : 'With Analytical Drivers',
	          	jndiname : 'java:comp/env/jdbc/foodmart',
	          	url : '',
	          	user : 'BIADMIN',
	          	password : '*******',
	          	driver : ''          
       		}, {       
          		id: '337',
	        	"label": 'spagobi',
	          	description: 'Connection to the metadata database',
	          	Dialect: 'MYSQL',
	          	type : 'With Analytical Drivers',
	          	jndiname : 'java:comp/env/jdbc/spagobi',
	          	url : '',
	          	user : 'BIADMIN',
	          	password : '*******',
	          	driver : ''          
       		}, {       
          		id: '338',
	        	"label": 'GeoData',
	          	description: 'Connection to the database used to test the geo engine',
	          	Dialect: 'INGRES',
	          	type : 'With Analytical Drivers',
	          	jndiname : 'java:comp/env/jdbc/genova',
	          	url : '',
	          	user : 'BIADMIN',
	          	password : '*******',
	          	driver : ''          
       		}
  		];
  	},
  	
  	
  	
  	
  	loadDatasetRecords: function() {
  		var records = {};
    	
    	records.meta =  this.loadDatasetMeta();
    	records.rows =  this.loadDatasetData();
    	
    	return records;
  	},
  	
  	loadDatasetMeta: function() {
  		return [
    		{
	    		dataIndex: 'label',
	    		name: 'Label'
    		}, {
	    		dataIndex: 'name',
	    		name: 'Name'
    		}, {
	    		dataIndex: 'description',
	    		name: 'Description' 
    		}, {
	    		dataIndex: 'type',
	    		name: 'Type'
    		}
    	];
  	},
  	
  	loadDatasetData: function() {
  		return [
  			{
	        	id: '1',
	        	"label": 'DatiDashBoard',
	        	name: 'Dati per test DashBoard',
	        	description: '',
	        	type: 'File',
	        	fileName: '../dataset/DatiTabella.txt'       	        	
        	}, {
	        	id: '2',
	        	"label": 'Chart1',
	        	name: 'Dati per test Chart1',
	        	description: '',
	        	type: 'Query',
	        	fileName: ''       	        	
        	}, {
	        	id: '3',
	        	"label": 'Chart2',
	        	name: 'Dati per test Chart2',
	        	description: '',
	        	type: 'Web Service',
	        	fileName: ''       	        	
        	}  			
  		];
  	}
  }
});