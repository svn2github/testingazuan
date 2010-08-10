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


/*
 * @author Andrea Gioia (andrea.gioia@eng.it)
 * @author Amit Rana (amit.rana@eng.it), 
 * @author Gaurav Jauhri (gaurav.jauhri@eng.it)
 */


/**
* This class defines the Engine Details Form.  
*   
*/


qx.Class.define("spagobi.ui.custom.EngineDetailsForm", {
	extend: spagobi.ui.Form,

	/** 
	*  When the constructor is called it returns an object of form type.
	* <p> To this form is associated the following fields :- 
	* <p> Label -> dataIndex: 'label'
	* <p> Name  -> dataIndex: 'name'
	* <p> Description -> dataIndex: 'description'
	* <p> Document Type -> dataIndex: 'documentType'
	* <p> Engine Type  -> dataIndex: 'engineType'
	* <p> Use Data Set -> dataIndex: 'useDataSet'
	* <p> Use Data source -> dataIndex: 'useDataSource'
	* <p> Data Source  ->  dataIndex: 'dataSource'
	* <p> Class  ->  dataIndex: 'class'
	* <p> Url  ->    dataIndex: 'url'
	* <p> Driver Name ->  dataIndex: 'driver'
	* <p> *Example :- *
	*  var simpleform = new spagobi.ui.custom.EngineDetailsForm();
	*  simpleform.setData({
	*  label:'Label',
	*  name:'Engine',
	*  description:'Description',
	*  documentType:'Document Type',
	*  engineType:'Engine Type',
	*  useDataSet: 'Use Data Set',
	*  useDataSource:'Use Data source',
	*  dataSource: 'Data Source',
	*  class: 'Class',
	*  url: 'Url',
	*  driver: 'Driver Name'
	*  });
	*
	*/ 	
	construct : function() { 
		//this.base(arguments, this.self(arguments).formStructure);
		this.base(arguments,[
        	{
        		type: 'text',
        		dataIndex: 'label',
        		text: 'Label',
        		mandatory: true	
        	}, {
        		type: 'text',
        		dataIndex: 'name',
        		text: 'Name',
        		mandatory: true	
        	}, {
        		type: 'text',
        		dataIndex: 'description',
        		text: 'Description',
        		mandatory: false		
        	}, {
        		type: 'combo',
        		dataIndex: 'documentType',
        		text: 'Document type',
        		items: ["Report","Map"]		
        	}, {
        		type: 'combo',
        		dataIndex: 'engineType',
        		text: 'Engine type',
        		items: ["Internal","External"],
	        	listeners: [
	        		{
	        			event: 'changeValue',
	        			handler: this._documentTypeChangeValueHandler,
	        			scope: this
	        		}        		
        		]
        	}, {
        		type: 'flag',
        		dataIndex: 'useDataSet',
        		text: 'Use Data Set',
        		checked: false	
        	}, {
        		type: 'flag',
        		dataIndex: 'useDataSource',
        		text: 'Use Data Source',
	        	checked: true,
	        	listeners: [
	        		{
	        			event: 'changeChecked',
	        			handler: this._useDataSourceChangeCheckedHandler,
	        			scope: this
	        		} 
				]
        	}, {
        		type: 'combo',
        		dataIndex: 'dataSource',
        		text: 'Data Source',
        		items: ["foodmart","geo", "spagobi"]
        	}, {
        		type: 'text',
        		dataIndex: 'class',
        		text: 'Class',
        		mandatory: true			
        	}, {
        		type: 'text',
        		dataIndex: 'url',
        		text: 'Url',
        		mandatory: true,
        		visible: false			
        	}, {
        		type: 'text',
        		dataIndex: 'driver',
        		text: 'Driver Name',
        		mandatory: true,
        		visible: false			
        	}
        ]);
		
	},
	
	members: {
		_documentTypeChangeValueHandler : function(e) {
        	if( this && this.getInputField('url') ) {
        		if (e.getValue()=="Internal") {
					this.getInputField('url').setDisplay(false);
					this.getInputField('driver').setDisplay(false);
					this.getInputField('class').setDisplay(true);
				} else {
					this.getInputField('url').setDisplay(true);
					this.getInputField('driver').setDisplay(true);
					this.getInputField('class').setDisplay(false);
				}
        	}
        	
        },       
        
        _useDataSourceChangeCheckedHandler : function(e) {
        	if( this && this.getInputField('dataSource') ) {
        		this.getInputField('dataSource').setDisplay( e.getValue() );
        	}
        }  
	}
});