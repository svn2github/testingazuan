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
* This class defines the Dataset Details Form. 
* 
*/

qx.Class.define("qooxdoo.ui.custom.DatasetDetailsForm", {
	extend: qooxdoo.ui.form.Form,
	
	/** 
	*  When the constructor is called it returns an object of form type.
	* <p> To this form is associated the following fields :- 
	* <p> Label -> dataIndex: 'label'
	* <p> Name  -> dataIndex: 'name'
	* <p> Description -> dataIndex: 'description'
	* <p> Type -> dataIndex: 'type'
	* <p> File name  -> dataIndex: 'fileName'
	* <p> *Example :- *
	*  var simpleform = new qooxdoo.ui.custom.DatasetDetailsForm();
	*  simpleform.setData({
	*  label:'Label',
	*  name:'Engine',
	*  description:'Description',
	*  Type:'Type',
	*  fileName:'File Name',
	*  });
	*
	*/ 	
	construct : function() { 
		
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
        	},{
        		type: 'combo',
        		dataIndex: 'type',
        		text: 'Type',
        		items: ["File","WebService","Query","Script","Classe Java"],
	        	listeners: [
	        		{
	        			event: 'changeValue',
	        			handler: this._documentTypeChangeValueHandler,
	        			scope: this
	        		}        		
        		]
        	}, {
	          type: 'text',
	          dataIndex: 'fileName',
	          text: 'File name',
	          mandatory: false 
	         },{
        		type: 'textarea',
        		dataIndex: 'query',
        		text: 'Query',
        		mandatory: true	,
        		visible: false
        	}, {
        		type: 'combo',
        		dataIndex: 'datasource',
        		text: 'Data Source',
        		items: ["Foodmart","SpagoBI"],
        		mandatory: true	,
        		visible: false
        	}, {
        		type: 'combo',
        		dataIndex: 'trasformation',
        		text: 'Trasformation',
        		items: ["","Pivot Trasformer"],
        		mandatory: false,
        		listeners: [
	        		{
	        			event: 'changeValue',
	        			handler: this._changeTransformationHandler,
	        			scope: this
	        		}        		
        		]
        	}, {
        		type: 'text',
        		dataIndex: 'column',
        		text: 'Pivot over Column',
        		mandatory: false	,
        		visible: false
        	}, {
        		type: 'text',
        		dataIndex: 'row',
        		text: 'Pivot over Row',
        		mandatory: false	,
        		visible: false
        	}, {
        		type: 'text',
        		dataIndex: 'value',
        		text: 'Pivot Value',
        		mandatory: false	,
        		visible: false
        	}, {
        		type: 'flag',
        		dataIndex: 'columnnumbers',
        		text: 'Column Numbers',
        		mandatory: false	,
        		visible: false
        	}, {
        		type: 'text',
        		dataIndex: 'address',
        		text: 'Address',
        		mandatory: false	,
        		visible: false
        	}, {
        		type: 'text',
        		dataIndex: 'operation',
        		text: 'Operation',
        		mandatory: false	,
        		visible: false
        	}, {
        		type: 'textarea',
        		dataIndex: 'script',
        		text: 'Script',
        		mandatory: true	,
        		visible: false,
        		height: 100
        	}, {
        		type: 'combo',
        		dataIndex: 'language',
        		text: 'Language',
        		items: ["Groovy","Javascript"],
        		mandatory: true	,
        		visible: false
        	}, {
        		type: 'text',
        		dataIndex: 'javaclass',
        		text: 'Java Class Name',
        		mandatory: true	,
        		visible: false
        	}, {
        		type: 'propertiesList',
        		dataIndex: 'parameters',
        		text: 'Parameters',
        		visible: false,
        		nameColumnLabel: 'Name' ,
        		valueColumnLabel: 'Type',
        		cellNameRendered: 'text',
        		cellValueRendered: 'combo',
        		cellValueOptions: [ '' , 'String', 'Integer' ]
					          	  
			}
					        	
        ]);
		
	},
	
	members: {
		_documentTypeChangeValueHandler : function(e) {
				
				this.setFileInputFieldsVisible( e.getData()==="File" );
				this.setWebInputFieldsVisible( e.getData()==="WebService" );
				this.setQueryInputFieldsVisible( e.getData()==="Query" );
				this.setScriptInputFieldsVisible( e.getData()==="Script" );
				this.setJavaClassInputFieldsVisible( e.getData()==="Classe Java" );
   	
        }
        
        , setFileInputFieldsVisible : function(b) {
        	if (b) {
				this.getInputField('fileName').setVisibility("visible");
			} else {
				this.getInputField('fileName').setVisibility("excluded");
			}
        }
        
        , setWebInputFieldsVisible : function(b) {
        	if (b) {
					this.getInputField('address').setVisibility("visible");
					this.getInputField('operation').setVisibility("visible");
				} else {
					this.getInputField('address').setVisibility("excluded");
					this.getInputField('operation').setVisibility("excluded");
				}
        }
        
        , setQueryInputFieldsVisible : function(b) {
        	if (b) {
					this.getInputField('query').setVisibility("visible");
					this.getInputField('datasource').setVisibility("visible");
					this.getInputField('parameters').setVisibility("visible");
				} else {
					this.getInputField('query').setVisibility("excluded");
					this.getInputField('datasource').setVisibility("excluded");
					this.getInputField('parameters').setVisibility("excluded");
				}
        }
        
        , setScriptInputFieldsVisible : function(b) {
        	if (b) {
					this.getInputField('script').setVisibility("visible");
					this.getInputField('language').setVisibility("visible");		
				} else {
					this.getInputField('script').setVisibility("excluded");
					this.getInputField('language').setVisibility("excluded");
				}
        }
        
        , setJavaClassInputFieldsVisible : function(b) {
        	if (b) {
					this.getInputField('javaclass').setVisibility("visible");
				} else {
					this.getInputField('javaclass').setVisibility("excluded");
				}
        }
        
        ,  _changeTransformationHandler : function(e) {
				
				this.setPivotTransformerInputFieldsVisible( e.getData()==="Pivot Trasformer" );
        }
        
        , setPivotTransformerInputFieldsVisible : function(b) {
        	if (b) {
					this.getInputField('column').setVisibility("visible");
					this.getInputField('row').setVisibility("visible");
					this.getInputField('value').setVisibility("visible");
					this.getInputField('columnnumbers').setVisibility("visible");
				} else {
					this.getInputField('column').setVisibility("excluded");
					this.getInputField('row').setVisibility("excluded");
					this.getInputField('value').setVisibility("excluded");
					this.getInputField('columnnumbers').setVisibility("excluded");
				}
        }
	}
})