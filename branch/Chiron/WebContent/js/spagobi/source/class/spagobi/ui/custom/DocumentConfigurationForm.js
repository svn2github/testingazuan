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


qx.Class.define("spagobi.ui.custom.DocumentConfigurationForm", {
	extend: spagobi.ui.Form,

	/** 
	*  When the constructor is called it returns an object of form type.
	* <p> To this form is associated the following fields :- 
	* <p> Label -> dataIndex: 'label'
	* <p> Name  -> dataIndex: 'name'
	* <p> Description -> dataIndex: 'description'
	* <p> Type -> dataIndex: 'type'
	* <p> Engine  -> dataIndex: 'engine'
	* <p> Data Source -> dataIndex: 'datasource'
	* <p> State -> dataIndex: 'state'
	* <p> Refresh Seconds  ->  dataIndex: 'refreshseconds'
	* <p> Criptable  ->  dataIndex: 'criptable'
	* <p> Visible  ->    dataIndex: 'visible'
	* <p> Template ->  dataIndex: 'template'
	* <p> Template Build ->  dataIndex: 'templatebuild'
	* <p> formList: spagobi.ui.custom.ConfigurationSubform  ->  dataIndex: 'features'
	* <p> *Example :- *
	*  var simpleform = new spagobi.ui.custom.EngineDetailsForm();
	*  simpleform.setData({
	*  label:'Label',
	*  name:'Engine',
	*  description:'Description',
	*  type:'Type',
	*  engine:'Engine',
	*  datasource: 'Data Source',
	*  state:'State',
	*  refreshseconds: 'Refresh Seconds',
	*  criptable: 'Criptable',
	*  visible: 'Visible',
	*  template: 'Template'
	*  templatebuild: 'Template Build'
	*  features: [{
	*  title: 'Title',
	*  analyticaldriver: 'Analytical Driver',
	*  urlname: 'Url Name',
	*  priority: 'Priority'
	*     [{
	*  });
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
        		dataIndex: 'type',
        		text: 'Type',
        		items: ["","Report","On-line Analytical Processing","Data Mining Model","Dashboard","Datamart Model","Map","Dossier","Office Document","ETL Process", "Document Composite"],
        		listeners: [
	        		{
	        			event: 'changeValue',
	        			handler: this._documentTypeChangeValueHandler,
	        			scope: this
	        		}        		
        		]		
        	}, {
        		type: 'combo',
        		dataIndex: 'engine',
        		text: 'Engine',
        		items: ["","JPIVOT"]
	       	}, {
        		type: 'combo',
        		dataIndex: 'datasource',
        		text: 'Data Source',
        		items: ["","FoodMart","SpagoBI","GeoData","XaltiaOracle"]
           	}, {
        		type: 'text',
        		dataIndex: 'useDataSet',
        		text: 'Use Data Set',
        		mandatory: false		
        	}, {
        		type: 'combo',
        		dataIndex: 'state',
        		text: 'State',
        		items: ["","Suspended","Development","Test","Release"]
        	}, {
        		type: 'text',
        		dataIndex: 'refreshseconds',
        		text: 'Refresh Seconds',
        		mandatory: false			
        	}, {
        		type: 'radio',
        		dataIndex: 'cryptable',
        		text: 'Criptable',
        		items: ["true", "false"]
           	}, {
        		type: 'radio',
        		dataIndex: 'visible',
        		text: 'Visible',
        		items: ["true", "false"]			
        	}, {
        		type: 'text',
        		dataIndex: 'template',
        		text: 'Template',
        		mandatory: false			
        	}, {
        		type: 'text',
        		dataIndex: 'templatebuild',
        		text: 'Template Build',
        		mandatory: false			
        	}, {
        		type: 'formList',
        		dataIndex: 'features',
        		formList: spagobi.ui.custom.ConfigurationSubform 
        	}
        ]);
		
	},
	
	members: {
		_documentTypeChangeValueHandler : function(e) {
        	if( this && this.getInputField('type') ) {
        		if (e.getValue()=="Report") {
					this.getInputField('useDataSet').setDisplay(false);
					this.getInputField('templatebuild').setDisplay(false);
				}  else if (e.getValue()=="On-line Analytical Processing") {
					this.getInputField('useDataSet').setDisplay(false); 
				}  else if (e.getValue()=="Data Mining Model" || e.getValue()=="Office Document") {
					this.getInputField('datasource').setDisplay(false);
					this.getInputField('useDataSet').setDisplay(false);
					this.getInputField('templatebuild').setDisplay(false);
			   	} else if (e.getValue()=="Dashboard") {
					this.getInputField('datasource').setDisplay(false);
					this.getInputField('templatebuild').setDisplay(false);
				} else if (e.getValue()=="Datamart Model" || e.getValue()=="Map" || e.getValue()=="ETL Process" || e.getValue()=="Document Composite") {
					this.getInputField('useDataSet').setDisplay(false);
					this.getInputField('templatebuild').setDisplay(false);
				} else if (e.getValue()=="Dossier") {
					this.getInputField('datasource').setDisplay(false); 
				} 
        	}
        	
        }
        
     }
});