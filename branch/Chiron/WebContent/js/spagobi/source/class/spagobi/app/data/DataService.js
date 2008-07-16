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

qx.Class.define("spagobi.app.data.DataService", {
  type : "static",
  statics : {
  	
  	/**
  	 * Function to display the list for the Engine details page.
  	 * <p>The function defines the layout of the list and then loads the data into it.
  	 * <p> see functions : loadEngineMeta() and loadEngineData()
  	 * 
  	 * *Example*
  	 * <p><code>
  	 * var rec = spagobi.app.data.DataService.loadEngineRecords();
  	 * </code>
  	 * 
  	 * @return The object containing the layout and the data in the list and form
  	 */
  	 
  	loadEngineRecords: function() {
  		var records = {};
    	
    	records.meta =  this.loadEngineMeta();
    	records.rows =  this.loadEngineData();
    	
    	return records;
  	},
  	
  	/**
  	 * Function which defines the layout of the list for the Engine Details page.
  	 * <p> This function is called by the loadEngineRecords() function.
  	 * 
  	 * @return An array of objects with dataIndex and name properties defined for each column of list
  	 */
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
  	
  	/**
  	 * Function used to load the data in the list and form for the Engine Details page.
  	 * <p> This function is called by the loadEngineRecords() function.
  	 * 
  	 * @return Array of objects with data for all the columns of list and elements of the form
  	 */
  	 
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
  	
  	
  	/**
  	 * Function to display the list for the Data Source details page
  	 * <p>The function defines the layout of the list and then loads the data into it.
  	 * <p> see functions : loadDatasourceMeta() and loadDatasourceData()
  	 * 
  	 * *Example*
  	 * <p><code>
  	 * var rec = spagobi.app.data.DataService.loadDatasourceRecords();
  	 * </code>
  	 * 
  	 * @return The object containing the layout of the list and the data in the list and form
  	 */
  	
  	
  	loadDatasourceRecords: function() {
  		var records = {};
    	
    	records.meta =  this.loadDatasourceMeta();
    	records.rows =  this.loadDatasourceData();
    	
    	return records;
  	},
  	
  	/**
  	 * Function which defines the layout of the list for the Data source details page
  	 * 
  	 * @return An array of objects with dataIndex and name properties defined for each column of list
  	 */
  
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
  	
  	/**
  	 * Function used to load the data in the list and form for the Data Source details page
  	 * 
  	 * @return Array of objects with data for all the columns of list and elements of the form
  	 */
  	
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
  	
  	/**
  	 * Function to display the list for the Data Set page
  	 * <p>The function defines the layout of the list and then loads the data into it.
  	 * <p> see functions : loadDatasetMeta() and loadDatasetData()
  	 * 
  	 * *Example*
  	 * <p><code>
  	 * var rec = spagobi.app.data.DataService.loadDatasetRecords();
  	 * </code>
  	 * 
  	 * @return The object containing the layout of the list and the data in the list and form
  	 */
  	
  	loadDatasetRecords: function() {
  		var records = {};
    	
    	records.meta =  this.loadDatasetMeta();
    	records.rows =  this.loadDatasetData();
    	
    	return records;
  	},
  	
  	
  	/**
  	 * Function which defines the layout of the list for the Data set page
  	 * 
  	 * @return An array of objects with dataIndex and name properties defined for each column of list
  	 */  	
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
  	
  	/**
  	 * Function used to load the data in the list and form for the Data Set page
  	 * 
  	 * @return Array of objects with data for all the columns of list and elements of the form
  	 */
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
  	
  	
  	/**
  	 * Function to display the list for the Map management page
  	 * <p>The function defines the layout of the list and then loads the data into it.
  	 * <p> see functions : loadMapMeta() and loadMapData()
  	 * 
  	 * *Example*
  	 * <p><code>
  	 * var rec = spagobi.app.data.DataService.loadMapRecords();
  	 * </code>
  	 * 
  	 * @return The object containing the layout of the list and the data in the list and form
  	 */
  	
  	loadMapRecords: function() {
  		var records = {};
    	
    	records.meta =  this.loadMapMeta();
    	records.rows =  this.loadMapData();
    	
    	return records;
  	},
  	
  	/**
  	 * Function which defines the layout of the list for the Map management page
  	 * 
  	 * @return An array of objects with dataIndex and name properties defined for each column of list
  	 */
  	 
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
  	
  	/**
  	 * Function used to load the data in the list and form for the Map management page
  	 * 
  	 * @return Array of objects with data for all the columns of list and elements of the form
  	 */
  	loadMapData: function() {
  		return [
  			{
	        	id: '1',
	        	name: 'aaa',
	        	description: 'aaa',
	        	template: '/components/mapcatalogue/maps/import.txt',
	        	format: 'SVG',
	        	numfeatures: '0',
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
		        }]
	        	 	        	
        	}, {
	        	id: '2',
	        	name: 'USA_States',
	        	description: 'USA_States',
	        	template: '/components/mapcatalogue/maps/UsaStates.svg',
	        	format: 'SVG',
	        	numfeatures: '1',
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
		        }] 	
        	}, {
	        	id: '3',
	        	name: 'Senzioni_Censimento',
	        	description: 'Sezioni_Censimento',
	        	template: '/components/mapcatalogue/maps/Sezioni_Censimento.svg',
	        	format: 'SVG',
	        	numfeatures: '13',
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
		        }]	     	
        	}  			
  		];
  	},
  	
  	/**
  	 * Function to display the list for the Feature management page
  	 * <p>The function defines the layout of the list and then loads the data into it.
  	 * <p> see functions : loadFeatureMeta() and loadFeatureData()
  	 * 
  	 * *Example*
  	 * <p><code>
  	 * var rec = spagobi.app.data.DataService.loadFeatureRecords();
  	 * </code>
  	 * 
  	 * @return The object containing the layout of the list and the data in the list and form
  	 */
  	
  	loadFeatureRecords: function() {
  		var records = {};
    	
    	records.meta =  this.loadFeatureMeta();
    	records.rows =  this.loadFeatureData();
    	
    	return records;
  	},
  	
  	 /**
  	 * Function which defines the layout of the list for the Feature management page
  	 * 
  	 * @return An array of objects with dataIndex and name properties defined for each column of list
  	 */
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
  	
  	/**
  	 * Function used to load the data in the list and form for the Feature management page
  	 * 
  	 * @return Array of objects with data for all the columns of list and elements of the form
  	 */
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
  	}
  	
  }
});