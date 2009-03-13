/*

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

*/

/* *
 * @author Andrea Gioia (andrea.gioia@eng.it)
 * @author Amit Rana (amit.rana@eng.it)
 * @author Gaurav Jauhri (gaurav.jauhri@eng.it)
 * 
 */
 

/**
 * Class to create a list (table) 
 */

/*

#asset(qx/icon/Oxygen/16/actions/schedule.png)
#asset(qx/icon/Oxygen/16/actions/dialog-ok.png)
#asset(qx/icon/Oxygen/16/actions/detail.gif)
*/ 
qx.Class.define("qooxdoo.app.data.DataService", {
  type : "static",
  statics : {
  	
  	loadRolesRecords: function(){
  		var records = {};
    	
    	records.ID = "ROLES";
    	records.columns = [2,3,4,5,6,7,8,9,10];
    	records.meta =  this.loadRolesMeta();
    	records.rows =  this.loadRolesData();
    	
    	return records;
  	},
  	
  	loadRolesMeta: function() {
  		return [
    		{
	    		dataIndex: 'name',
	    		name: 'Name'
    		}, {
	    		dataIndex: 'type',
	    		name: 'Type' 
    		}, {
	    		dataIndex: 'savecustomview',
	    		name: 'SaveCustomView' 
    		}, {
	    		dataIndex: 'seecustomview',
	    		name: 'SeeCustomView' 
    		}, {
	    		dataIndex: 'notes',
	    		name: 'Notes'
    		}, {
	    		dataIndex: 'metadata',
	    		name: 'MetaData'
    		}, {
	    		dataIndex: 'sendmail',
	    		name: 'SendMail'
    		}, {
	    		dataIndex: 'hotlinks',
	    		name: 'HotLinks'
    		}, {
	    		dataIndex: 'ziipa',
	    		name: 'AB' 
    		}, {
	    		dataIndex: 'zuppo',
	    		name: 'ABC' 
    		}, {
	    		dataIndex: 'zippi',
	    		name: 'ABCD' 
    		}
    	];
  	},
  	
  	loadRolesData: function() {
  		        
  		return [
    		{ 
	        	id: '180',
	        	name: '/Admin',
	        	type: 'Functional_Role',
	        	savecustomview: true,
	        	seecustomview: true,
	        	notes: true,
	        	metadata: true,
	        	sendmail: true, 
	        	hotlinks: true,
	        	ziipa: false,
	        	zuppo: true,
	        	zippi: false         	        	
        	}, {
	        	id: '181',
	        	name: '/Community',
	        	type: 'Functional_Role',
	        	savecustomview: true,
	        	seecustomview: true,
	        	notes: true,
	        	metadata: true,
	        	sendmail: true, 
	        	hotlinks: true,
	        	ziipa: false,
	        	zuppo: true,
	        	zippi: false    	        	
        	}, {
	        	id: '182',
	        	name: '/Guest',
	        	type: 'Functional_Role',
	        	savecustomview: true,
	        	seecustomview: true,
	        	notes: true,
	        	metadata: true,
	        	sendmail: true, 
	        	hotlinks: true,
	        	ziipa: false,
	        	zuppo: true,
	        	zippi: false      	        	
        	}, {
	        	id: '183',
	        	name: '/Guest',
	        	type: 'Functional_Role',
	        	savecustomview: true,
	        	seecustomview: true,
	        	notes: true,
	        	metadata: true,
	        	sendmail: true, 
	        	hotlinks: true,
	        	ziipa: false,
	        	zuppo: true,
	        	zippi: false      	        	
        	}, {
	        	id: '184',
	        	name: '/Guest',
	        	type: 'Functional_Role',
	        	savecustomview: true,
	        	seecustomview: true,
	        	notes: true,
	        	metadata: true,
	        	sendmail: true, 
	        	hotlinks: true,
	        	ziipa: false,
	        	zuppo: true,
	        	zippi: false      	        	
        	}
    	];
  	},
  	
  	loadlink1Records: function(){
  		var records = {};
    	records.ID = "LINK";
    	records.meta =  this.loadLink1Meta();
    	records.rows =  this.loadLink1Data();
    	
    	return records;
  	},
  	
  	loadLink1Meta: function() {
  		return [
    		{
	    		dataIndex: 'name',
	    		name: 'Name'
    		}, {
	    		dataIndex: 'document',
	    		name: 'Document' 
    		}, {
	    		dataIndex: 'documentname',
	    		name: 'DocumentName' 
    		}, {
	    		dataIndex: 'documentdescription',
	    		name: 'DocumentDescription' 
    		},{
	    		dataIndex: 'documenttype',
	    		name: 'DocumentType'
    		}
    	];
  	},
  	
  	loadLink1Data: function() {
  		return [
    		{
	        	id: '190',
	        	name: 'hot_Customer_Profile',
	        	document: 'Customer_Profile',
	        	documentname: 'Customer_Profile',
	        	documentdescription: 'Customer_Profile',
	        	documenttype: 'Report'        	        	
        	}, {
	        	id: '191',
	        	name: 'Ciao',
	        	document: 'Qbe_Test',
	        	documentname: 'A Simple QBE on FoodMart',
	        	documentdescription: '',
	        	documenttype: 'dataMart'       	        	
        	}, {
	        	id: '192',
	        	name: 'Blaaa',
	        	document: 'OlAP',
	        	documentname: 'A simple OLAP',
	        	documentdescription: 'OLAP',
	        	documenttype: 'OLAP'            	        	
        	}
    	];
  	},
  	
  	loadlink2Records: function(){
  		var records = {};
    	records.ID = "LINK";
    	records.meta =  this.loadLink2Meta();
    	records.rows =  this.loadLink2Data();
    	
    	return records;
  	},
  	
  	loadLink2Meta: function() {
  		return [
    		{
	    		dataIndex: 'document',
	    		name: 'Document' 
    		}, {
	    		dataIndex: 'documentname',
	    		name: 'DocumentName' 
    		}, {
	    		dataIndex: 'documentdescription',
	    		name: 'DocumentDescription' 
    		},{
	    		dataIndex: 'documenttype',
	    		name: 'DocumentType'
    		}
    	];
  	},
  	
  	loadLink2Data: function() {
  		return [
    		{
	        	id: '193',
	        	document: 'BarChart',
	        	documentname: 'A Simple bar chart',
	        	documentdescription: '',
	        	documenttype: 'DASH'        	        	
        	}, {
	        	id: '194',
	        	document: 'mapUSA',
	        	documentname: 'A map with sales of FoodMart',
	        	documentdescription: 'Map USA',
	        	documenttype: 'MAP'       	        	
        	}, {
	        	id: '195',
	        	document: 'OlAP',
	        	documentname: 'A simple OLAP',
	        	documentdescription: '',
	        	documenttype: 'OLAP'            	        	
        	}
    	];
  	},
  	
  	loadlink3Records: function(){
  		var records = {};
    	records.ID = "LINK";
    	records.meta =  this.loadLink3Meta();
    	records.rows =  this.loadLink3Data();
    	
    	return records;
  	},
  	
  	loadLink3Meta: function() {
  		return [
    		{
	    		dataIndex: 'document',
	    		name: 'Document' 
    		}, {
	    		dataIndex: 'documentname',
	    		name: 'DocumentName' 
    		}, {
	    		dataIndex: 'documentdescription',
	    		name: 'DocumentDescription' 
    		},{
	    		dataIndex: 'documenttype',
	    		name: 'DocumentType'
    		}
    	];
  	},
  	
  	loadLink3Data: function() {
  		return [
    		{
	        	id: '196',
	        	document: 'SpeedoMeterChart',
	        	documentname: 'A Simple speedo Meter',
	        	documentdescription: 'yahoo',
	        	documenttype: 'DASH'        	        	
        	}, {
	        	id: '197',
	        	document: 'SpeedoMeterChart',
	        	documentname: 'A Simple speedo Meter',
	        	documentdescription: 'Google',
	        	documenttype: 'DASH'            	        	
        	}, {
	        	id: '198',
	        	document: 'SpeedoMeterChart',
	        	documentname: 'A Simple speedo Meter',
	        	documentdescription: 'Ask',
	        	documenttype: 'DASH'                	        	
        	}
    	];
  	},
  	
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
	          	dialect: 'MYSQL',
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
	          	dialect: 'MYSQL',
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
	          	dialect: 'INGRES',
	          	type : 'With Analytical Drivers',
	          	jndiname : 'java:comp/env/jdbc/genova',
	          	url : '',
	          	user : 'BIADMIN',
	          	password : '*******',
	          	driver : ''          
       		}
  		];
  	},
  	
  	loadScheduleRecords: function() {
  		var records = {};
    	    	
    	records.ID = "Scheduler";
    	records.columns = [3,4];
    	records.meta =  this.loadScheduleMeta();
    	records.rows =  this.loadScheduleData();
    	
    	return records;
  	},
  	
  	loadScheduleMeta: function() {
  		return [
    		{
	    		dataIndex: 'label',
	    		name: 'Label'
    		}, {
	    		dataIndex: 'description',
	    		name: 'Description' 
    		}, {
	    		dataIndex: 'type',
	    		name: 'Number of Schedules'
    		}, {
	    		dataIndex: 'ScheduleList',
	    		name: 'Schedule List'
    		}, {
	    		dataIndex: 'detail',
	    		name: 'Details'
    		}
    	];
  	},
  	
  	loadScheduleData: function() {
  		return [
  			{
	        	id: '51',
	        	"label": 'Report Clienti',
	        	description: 'Dettaglio del profilo cliente',
	        	type: '0',
	        	ScheduleList: 'qx/icon/Oxygen/16/actions/schedule.png',//qx.util.AliasManager.getInstance().resolve(//'qx/icon/Oxygen/16/actions/insert-link.png'
	        	detail: 'qx/icon/Oxygen/16/actions/detail.gif'//qx.util.AliasManager.getInstance().resolve('spagobi/img/spagobi/test/detail.gif')//'qx/icon/Oxygen/16/actions/window-new.png'        	
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
  	},
  	
  	loadMapRecords: function() {
  		var records = {};
    	
    	records.meta =  this.loadMapMeta();
    	records.rows =  this.loadMapData();
    	
    	return records;
  	},
  	
  	loadMapMeta: function() {
  		return [
    		{
	    		dataIndex: 'name',
	    		name: 'Name'
    		}, {
	    		dataIndex: 'description',
	    		name: 'Description' 
    		}, {
	    		dataIndex: 'template',
	    		name: 'Template'
    		}, {
	    		dataIndex: 'format',
	    		name: 'Format'
    		}, {
	    		dataIndex: 'numfeatures',
	    		name: 'Num. Features'
    		}/*, {
	    		dataIndex: 'features',
	    		name: 'Features'
    		}*/
    	];
  	},
  	
  	loadMapData: function() {
  		return [
  			{
	        	id: '1',
	        	name: 'aaa',
	        	description: 'aaa',
	        	template: '/components/mapcatalogue/maps/import.txt',
	        	format: 'SVG',
	        	numfeatures: '0'/*,
	        	features: [{
		        	id: '1',
		        	name: 'States',
		        	description: 'States of EU',
		        	type: 'Territorial',
		        	nummaps: '1'
		        }, {
		        	id: '2',
		        	name: 'States2',
		        	description: 'States of EU2',
		        	type: 'Territorial2',
		        	nummaps: '2'
		        }]*/
	        	 	        	
        	}, {
	        	id: '2',
	        	name: 'USA_States',
	        	description: 'USA_States',
	        	template: '/components/mapcatalogue/maps/UsaStates.svg',
	        	format: 'SVG',
	        	numfeatures: '1'/*,
	        	features: [{
		        	id: '2',
		        	name: 'Frame',
		        	description: 'Just a frame',
		        	type: 'Territorial',
		        	nummaps: '2'
		        },{
		        	id: '2b',
		        	name: 'Frameb',
		        	description: 'Just a frameb',
		        	type: 'Territorialb',
		        	nummaps: '2b'
		        }] */	
        	}, {
	        	id: '3',
	        	name: 'Senzioni_Censimento',
	        	description: 'Sezioni_Censimento',
	        	template: '/components/mapcatalogue/maps/Sezioni_Censimento.svg',
	        	format: 'SVG',
	        	numfeatures: '13'/*,
	        	features: [{
		        	id: '3',
		        	name: 'Unita Urbanistiche',
		        	description: 'Divisione territoriale comune GE',
		        	type: 'Territorial',
		        	nummaps: '3'
		        }, {
		        	id: '4',
		        	name: 'Unita Urbanistiche4',
		        	description: 'Divisione territoriale comune GE4',
		        	type: 'Territorial4',
		        	nummaps: '34'
		        }]*/	     	
        	}  			
  		];
  	},
  	
  	loadFeatureRecords: function() {
  		var records = {};
    	
    	records.meta =  this.loadFeatureMeta();
    	records.rows =  this.loadFeatureData();
    	
    	return records;
  	},
  	
  	loadFeatureMeta: function() {
  		return [
    		{
	    		dataIndex: 'name',
	    		name: 'Name'
    		}, {
	    		dataIndex: 'description',
	    		name: 'Description' 
    		}, {
	    		dataIndex: 'type',
	    		name: 'Type'
    		}, {
	    		dataIndex: 'nummaps',
	    		name: 'Num. Maps'
    		}
    	];
  	},
  	
  	loadFeatureData: function() {
  		return [
	  			{
		        	id: '1',
		        	name: 'States',
		        	description: '',
		        	type: '',
		        	nummaps: '1'
		        }, {
		        	id: '2',
		        	name: 'Frame',
		        	description: '',
		        	type: '',
		        	nummaps: '2'
		        }, {
		        	id: '3',
		        	name: 'Unita Urbanistiche',
		        	description: '',
		        	type: '',
		        	nummaps: '3'
		        }  			
  			 ];
  	},
  	
  	loadLOVRecords: function() {
  		var records = {};
    	
    	records.meta =  this.loadLOVMeta();
    	records.rows =  this.loadLOVData();
    	
    	return records;
  	},
  	
  	loadLOVMeta: function() {
  		return [
    		{
	    		dataIndex: 'name',
	    		name: 'Name'
    		}, {
	    		dataIndex: 'description',
	    		name: 'Description' 
    		}
    	];
  	},
  	
  	loadLOVData: function() {
  		return [
	  			{
		        	id: '1',
		        	name: 'name1',
		        	description: 'ddd1'
		        }, {
		        	id: '2',
		        	name: 'name2',
		        	description: 'aaa2'
		        }, {
		        	id: '3',
		        	name: 'name3',
		        	description: 'cvcw3'
		        }  			
  			 ];
  	},
  	
  	
	loadConfigurationRecords: function() {
  		var records = {};
    	
    	records.meta =  this.loadConfigurationMeta();
    	records.rows =  this.loadConfigurationData();
    	
    	return records;
  	},
  	
  	loadConfigurationMeta: function() {
  		return [
    		{
	    		dataIndex: 'label',
	    		name: 'Label'
    		}, {
	    		dataIndex: 'name',
	    		name: 'Name'
    		}, {
	    		dataIndex: 'engine',
	    		name: 'Engine' 
    		}, {
	    		dataIndex: 'state',
	    		name: 'State'
    		}, {
	    		dataIndex: 'instance',
	    		name: 'Number Of Instance'
    		}
    	];
  	},
  	
  	loadConfigurationData: function() {
  	return [
    		{
	        	id: '91',
	        	"label": 'OLAP',
	        	name: 'A simple OLAP',
	        	description: 'Compatible with OLAP engine v3.1',
	        	type: 'On-line Analytical Processing',
	        	engine: 'JPIVOT',
	        	datasource: 'Foodmart',
	        	useDataSet: 'Dummy Input',
	        	state: 'Released',
	        	refreshseconds: '0',
	        	cryptable: 'false',
	        	visibility: 'true',
	        	template: 'Dummy Input',
	        	templatebbuild: 'Dummy Input'/*,
	        	features: [{
		        	id: '11',
		        	title: 'Dummy Input',
		        	analyticaldriver: 'Dummy Input',
		        	urlname: 'Dummy Input',
		        	priority: 'Dummy Input'
		        }, {
		        	id: '12',
		        	title: 'Dummy Input',
		        	analyticaldriver: 'Dummy Input',
		        	urlname: 'Dummy Input',
		        	priority: 'Dummy Input'
		        }]*/      	        	
        	}, {
	        	id: '92',
	        	"label": 'OLAP',
	        	name: 'A simple OLAP',
	        	description: 'Compatible with OLAP engine v3.1',
	        	type: 'On-line Analytical Processing',
	        	engine: 'JPIVOT',
	        	datasource: 'Foodmart',
	        	useDataSet: 'Dummy Input',
	        	state: 'Released',
	        	refreshseconds: '0',
	        	cryptable: 'false',
	        	visibility: 'true',
	        	template: 'Dummy Input',
	        	templatebbuild: 'Dummy Input'/*,
	        	features: [{
		        	id: '11',
		        	title: 'Dummy Input',
		        	analyticaldriver: 'Dummy Input',
		        	urlname: 'Dummy Input',
		        	priority: 'Dummy Input'
		        }, {
		        	id: '12',
		        	title: 'Dummy Input',
		        	analyticaldriver: 'Dummy Input',
		        	urlname: 'Dummy Input',
		        	priority: 'Dummy Input'
		        }]*/        	        	
        	}, {
	        	id: '93',
	        	"label": 'OLAP',
	        	name: 'A simple OLAP',
	        	description: 'Compatible with OLAP engine v3.1',
	        	type: 'On-line Analytical Processing',
	        	engine: 'JPIVOT',
	        	datasource: 'Foodmart',
	        	useDataSet: 'Dummy Input',
	        	state: 'Released',
	        	refreshseconds: '0',
	        	cryptable: 'false',
	        	visibility: 'true',
	        	template: 'Dummy Input',
	        	templatebbuild: 'Dummy Input'/*,
	        	features: [{
		        	id: '11',
		        	title: 'Dummy Input',
		        	analyticaldriver: 'Dummy Input',
		        	urlname: 'Dummy Input',
		        	priority: 'Dummy Input'
		        }, {
		        	id: '12',
		        	title: 'Dummy Input',
		        	analyticaldriver: 'Dummy Input',
		        	urlname: 'Dummy Input',
		        	priority: 'Dummy Input'
		        }]*/        	        	
        	}
    	];
  	},
  	
  	loadFunctionalitiesRecords: function() {
  		var records = {};
    	records.ID = "ROLES";
    	records.columns = [2,3,4];
    	records.meta =  this.loadFunctionalitiesMeta();
    	records.rows =  this.loadFunctionalitiesData();
    	
    	return records;
  	},
  	
  	loadFunctionalitiesMeta: function() {
  		return [
    		{
	    		dataIndex: 'roles',
	    		name: 'Roles'
    		}, {
	    		dataIndex: 'group',
	    		name: 'Group' 
    		}, {
	    		dataIndex: 'dev',
	    		name: 'Development'
    		}, {
	    		dataIndex: 'test',
	    		name: 'Test' 
    		}, {
	    		dataIndex: 'exe',
	    		name: 'Execution' 
    		}
    	];
  	},
  	
  	loadFunctionalitiesData: function() {
  		return [
  				{
				  	id		: '1',
				    roles	: '/admin',
				    group   : '/admin',
				    dev		: false,
				  	test	: false,
				  	exe		: false
				},{
				  	id		: '2',
				    roles   : '/community',
				    group   : '/community',
				  	dev		: false,
				  	test	: false,
				  	exe		: false
				},{
				  	id		: '3',
				    roles   : '/community/direction',
				    group   : '/community/direction',
				  	dev		: false,
				  	test	: false,
				  	exe		: false
				},{
				  	id		: '4',
				    roles   : '/community/editors',
				    group   : '/community/editors',
				  	dev		: false,
				  	test	: false,
				  	exe		: false
				},{
				  	id		: '5',
				    roles   : '/community/hr',
				    group   : '/community/hr',
				  	dev		: false,
				  	test	: false,
				  	exe		: false
				},{
				  	id		: '6',
				    roles   : '/guest',
				    group   : '/guest',
				  	dev		: false,
				  	test	: false,
				  	exe		: false
				},{
				  	id		: '7',
				    roles   : '/portal',
				    group   : '/portal',
				  	dev		: false,
				  	test	: false,
				  	exe		: false
				},{
				  	id		: '8',
				    roles   : '/portal/admin',
				    group   : '/portal/admin',
				  	dev		: false,
				  	test	: false,
				  	exe		: false
				},{
				  	id		: '9',
				    roles   : '/portal/share',
				    group   : '/portal/share',
				  	dev		: false,
				  	test	: false,
				  	exe		: false
				},{
				  	id		: '10',
				    roles   : '/portal/site',
				    group   : '/portal/site',
				  	dev		: false,
				  	test	: false,
				  	exe		: false
				},{
				   id  		: '11',
				   roles   	: '/spagobi',
				   group   	: 'spagobi',
				   dev     	: false,
				   test    	: false,
				   exe     	: false
				},{
				   id  		: '12',
				   roles    : '/spagobi/admin',
				   group    : 'admin',
				   dev      : false,
				   test     : false,
				   exe      : false
				},{
				   id  		: '13',
				   roles    : '/spagobi/dev',
				   group    : 'dev',
				   dev      : false,
				   test     : false,
				   exe      : false
				},{
				   id  		: '14',
				   roles    : '/spagobi/test',
				   group    : 'test',
				   dev      : false,
				   test     : false,
				   exe      : false
				},{
				   id  		: '15',
				   roles    : '/spagobi/user',
				   group    : 'user',
				   dev      : false,
				   test     : false,
				   exe      : false
				},{
				   id  		: '16',
				   roles    : '/user',
				   group    : '/user',
				   dev      : false,
				   test     : false,
				   exe      : false
				},{
				   id  		: '17',
				   roles    : '/spagobi/modeladmin',
				   group    : 'modeladmin',
				   dev      : false,
				   test     : false,
				   exe      : false
				},{
				   id  		: '18',
				   roles    : '/Test',
				   group    : 'Test',
				   dev      : false,
				   test     : false,
				   exe      : false
				},{
				   id  		: '19',
				   roles    : '/spagobi/user/general_manager',
				   group    : 'general_manager',
				   dev      : false,
				   test     : false,
				   exe      : false
				},{
				   id  		: '20',
				   roles    : '/spagobi/user/product_manager',
				   group    : 'product_manager',
				   dev      : false,
				   test     : false,
				   exe      : false
				},{
				   id  		: '21',
				   roles    : '/spagobi/user/sales_manager',
				   group    : 'sales_manager',
				   dev      : false,
				   test     : false,
				   exe      : false
				},{
				   id  		: '22',
				   roles    : '/spagobi/model',
				   group    : 'model',
					 dev    : false,
					 test   : false,
					 exe    : false
				},{
				   id  		: '23',
				   roles    : '/spagobi/user/demo',
				   group    : 'demo',
				   dev      : false,
				   test     : false,
				   exe      : false
				},{
				   id  		: '24',
				   roles    : '/spagobi/demo',
				   group    : 'demo',
				   dev      : false,
				   test     : false,
				   exe      : false
				}
	  		   ];
  	},
  	
  	loadTreeNodes: function() {
  		var records = {};
    	
    	//records.root = "Functionalities";
    	records.treeStructure =  this.loadTreeMeta();	//defines the tree structure
    	records.treeData =  this.loadTreeData();		//defines the tree data
    	
    	return records;
  	},
  	
  	
  	loadTreeMeta: function() {
  		//return [
  		var t = qooxdoo.app.data.DataService.loadTreeData();
    	var treeStructure = {
				    		root: 	{
				    				root		: 'Functionalities',
				    				dataIndex	: 'root'
				    			  	},
				    			  	
				    		node11:	{
				    				name		: 'Report',
				    				parent		: 'root',
					    			dataIndex	: 'report',	//'node11'
		  							data  		: t['report']
		  									
				    				},
				    			  	
				    		node12:	{
				    				name		: 'OLAP',
				    				parent		: 'root',
					    			dataIndex	: 'olap',
					    			data  		: t['olap']
				    				},
				    			  	
				    		node121:{
				    				name		: 'myOLAP',
				    				parent		: 'OLAP',
					    			dataIndex	: 'myolap',
		  							file 		: true,
		  							data  		: t['myolap']
		  							
				    				},
				    			  	
				    		node13:{
				    				name		: 'DashBoard',
				    				parent		: 'root',
					    			dataIndex	: 'dashboard',
		  							data  		: t['dashboard']
		  							
				    				},
				    			  	
				    		node131:{
				    				name		: 'myDashBoardFolder',
				    				parent		: 'DashBoard',
					    			dataIndex	: 'mydashboardfolder',
		  							file 		: true, 
		  							data  		: t['mydashboardfolder']
				    				},
				    			  	
				    		node132:{
				    				name		: 'myDashBoard',
				    				parent		: 'DashBoard',
					    			dataIndex	: 'mydashboard',
		  							file 		: true, 
		  							data  		: t['mydashboard']
				    				}
				    			  		
    					}
  		//];
  		return treeStructure;
  	},
  	
  	
  	loadTreeData: function() {
  		//return [
  		var treeData =	{
							report:{	  
							  		label : 'ReportLabel',
						 			name  : 'ReportName',
						 			desc  : 'ReportDesc',
						 			func  : [
						 						{
						 							role	: '/admin',
						 							dev		: true,
						 							test	: true,
						 							exe		: true
						 							
						 						},
						 						{
						 							role	: '/community/direction',
						 							dev		: true,
						 							test	: true,
						 							exe		: true
						 							
						 						}
						 					]	
		  							},
		  					olap:{
		  								
						 			label : 'OLAPLabel',
						 			name  : 'OLAPName',
						 			desc  : 'OLAPDesc',
						 			func  : [
						 						{
						 							role	: '/community',
						 							dev		: true,
						 							test	: true,
						 							exe		: true
						 							
						 						},
						 						{
						 							role	: '/guest',
						 							dev		: true,
						 							test	: true,
						 							exe		: true
						 							
						 						}
						 					]
		  							 		
		  							},
		  					myolap:{
		  							label : 'myOLAP Label',
						 			name  : 'myOLAP Name',
						 			desc  : 'myOLAP Desc'
		  							 		
		  							},
		  					dashboard:{
		  								
						 			label : 'DashBoardLabel',
						 			name  : 'DashBoardName',
						 			desc  : 'DashBoardDesc',
						 			func  : [
						 						{
						 							role	: '/community/editors',
						 							dev		: true,
						 							test	: true,
						 							exe		: true
						 							
						 						},
						 						{
						 							role	: '/community/hr',
						 							dev		: true,
						 							test	: true,
						 							exe		: true
						 							
						 						}
						 					]
		  							 			
		  							},
		  					mydashboardfolder:{
						 			label : 'myDashBoardFolderLabel',
						 			name  : 'myDashBoardFolderName',
						 			desc  : 'myDashBoardFolderDesc'
						 			
		  							 },
		  					mydashboard:{
						 			label : 'myDashBoard Label',
						 			name  : 'myDashBoard Name',
						 			desc  : 'myDashBoard Desc'
		  							 }	
		  							 	
		  							 						
				}// end of treeData
		return treeData;
				//];
  	},
  	
  	loadPageMeta: function() {
  		return [
    		{
	    		dataIndex: 'page1',
	    		name: ' 1 ',
	    		url : 'http://www.google.com/' 
    		}, {
	    		dataIndex: 'page2',
	    		name: ' 2 ',
	    		url : 'http://www.yahoo.com/' 
    		}, {
	    		dataIndex: 'page3',
	    		name: ' 3 ',
	    		url : 'http://www.msn.com/'
    		}
    	];
  	},
  	
  	userName : function(){
  		var userid = "biadmin";
  		return userid;
  	},
  	
  	loadKpiRecords: function() {
  		var records = {};
    	
    	records.meta =  this.loadKpiMeta();
    	records.rows =  this.loadKpiData();
    	
    	return records;
  	},
  	
  	loadKpiMeta: function() {
  		return [
    		{
	    		dataIndex: 'code',
	    		name: 'Code'
    		}, {
	    		dataIndex: 'name',
	    		name: 'Name' 
    		}
    	];
  	},
  	
  	loadKpiData: function() {
  		return [
  			{       
          		id		: '401',
	        	code	: 'NUMERO_ALLARMI',
	          	name	: 'NUMERO_ALLARMI',
	          	description: 'NUMERO_ALLARMI'
       		}, {       
          		id		: '402',
          		code	: 'CSP_BILLING_W',
          		name	: 'CSP_BILLING_W',
          		description: 'CSP_BILLING_W'
       		}
  		];
  	},
  	
  	loadThresholdRecords: function() {
  		var records = {};
    	
    	records.meta =  this.loadThresholdMeta();
    	records.rows =  this.loadThresholdData();
    	
    	return records;
  	},
  	
  	loadThresholdMeta: function() {
  		return [
    		{
	    		dataIndex: 'name',
	    		name: 'Name' 
    		}, {
	    		dataIndex: 'description',
	    		name: 'Description' 
    		}
    	];
  	},
  	
  	loadThresholdData: function() {
  		return [
  			{       
          		id		: '501',
	        	name	: 'Valore_100',
	          	description: 'Valore Assoluto 100',
	          	code	: 'Valore Assoluto 100',
	          	thresholdtype : 'Range Values'
       		}, {       
          		id		: '502',
          		name	: 'ALLARME',
          		description: 'Soglia per la generazione allarme',
          		thresholdtype	: 'Range Values'
          		
       		}
  		];
  	},
  	
  	loadKpiModelDefinitionRecords: function() {
        var records = {};
                  
        records.meta =  this.loadKpiModelDefinitionMeta();                
        records.rows =  this.loadKpiModelDefinitionData();
        
        return records;
    },
    
    loadKpiModelDefinitionMeta: function() {
        return [
                {
                    dataIndex: 'code',
                    name: 'Code'
                },
                {
                    dataIndex: 'name',
                    name: 'Name'
                }
            ];
          },
          
    loadKpiModelDefinitionData: function() {
        return [
                {
                    id: '171',
                    code: 'INDICATORI CSP',
                    name: 'INDICATORI CSP',
                    description: 'INDICATORI CSP',
                    typename: 'GQM root',
                    typedescription :'null',
                    kpiname : 'Numero Alarmi CSP'
                },
                {
                    id: '172',
                    code: 'INDICATORI TIM',
                    name: 'INDICATORI TIM',
                    description: 'INDICATORI TIM',
                    typename: 'GQM root',
                    typedescription: 'null',
                    kpiname : 'Numero Alarmi CSP'
                }
                ];
    },
  	
  	loadKpiModelInstanceRecords: function() {
        var records = {};
                  
        records.meta =  this.loadKpiModelInstanceMeta();                
        records.rows =  this.loadKpiModelInstanceData();
        
        return records;
    },
    
    loadKpiModelInstanceMeta: function() {
        return [
                {
                    dataIndex: 'instancename',
                    name: 'Name'
                }
            ];
          },
          
     loadKpiModelInstanceData: function() {
        return [
                {
                    id: '181',
                    instancename: 'INDICATORI CSP',
                    instancelabel: 'INDICATORI CSP',
                    instancedesc: 'INDICATORI CSP',
                    defnname: 'INDICATORI CSP',
                    defndesc : 'INDICATORI CSP',
                    defncode: 'INDICATORI CSP',
                    defntypename: 'GQM root',
                    defntypedesc :'null',
                    kpiname : 'Numero Alarmi CSP'
                },
                {
                    id: '182',
                    instancename: 'INDICATORI TIM',
                    instancelabel: 'INDICATORI TIM',
                    instancedesc: 'INDICATORI TIM',
                    defnname: 'INDICATORI TIM',
                    defndesc : 'INDICATORI TIM',
                    defncode: 'INDICATORI TIM',
                    defntypename: 'GQM root',
                    defntypedesc: 'null',
                    kpiname : 'Numero Alarmi CSP'
                }
                ];
    },    
    
  	loadKpiResourceRecords: function() {
        var records = {};
                  
        records.meta =  this.loadKpiResourceMeta();                
        records.rows =  this.loadKpiResourceData();
        
        return records;
    },
    
    loadKpiResourceMeta: function() {
        return [
          {
              dataIndex: 'name',
              name: 'Name'
          }
      ];
    },
    
    
    loadKpiResourceData: function() {
        return [
          {
              id: '162',
              name: 'MISP_ILVILLAGE_MSITE_MP',
              discription: 'MISP_ILVILLAGE_MSITE_MP',
              tablename: 'O_SERVICES',
              columnname: 'ORIG_SERVICE_ID',
              typename: 'CSP'
          }, {
              id: '163',
              name: 'MISP_PUREBROS_OI_NEWS',
              discription: 'MISP_PUREBROS_OI_NEWS',
              tablename: 'O_SERVICES',
              columnname: 'ORIG_SERVICE_ID',
              typename: 'CSP'
          }, {
              id: '164',
              name: 'MISP_PUREBROS_NAVIGAZIONE_SERVIZI_SEXY',
              discription: 'MISP_PUREBROS_NAVIGAZIONE_SERVIZI_SEXY',
              tablename: 'O_SERVICES',
              columnname: 'ORIG_SERVICE_ID',
              typename: 'CSP'
          }
      ];
    }
  	
  }
});