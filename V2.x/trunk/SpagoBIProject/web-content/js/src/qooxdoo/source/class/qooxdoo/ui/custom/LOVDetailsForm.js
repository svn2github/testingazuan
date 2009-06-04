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
 * This class defines the Predefined List of Values Form.
 *  
 */
qx.Class.define("qooxdoo.ui.custom.LOVDetailsForm", {
	extend: qooxdoo.ui.form.Form,
	
/** 
*  When the constructor is called it returns an object of form type.
* <p> To this form is associated the following fields :- 
* <p> Label -> dataindex: 'label'
* <p> Name  -> dataIndex: 'name'
* <p> Description -> dataIndex: 'description',
* <p> Type -> dataIndex: 'type'
* <p> Form -> dataIndex: 'querystmt'. It contains the following fields:
* 			<p>&nbsp;&nbsp;&nbsp;&nbsp; Data Source label -> 'datasourcelabel'
* 			<p>&nbsp;&nbsp;&nbsp;&nbsp; Query Definition -> 'querydef'
* <p> Form -> dataIndex: 'scriptloadvalues'. It contains the following fields:
* 			<p>&nbsp;&nbsp;&nbsp;&nbsp; Script -> 'script'
* <p> Form -> dataIndex: 'fixedlov'. It contains the following fields:
* 			<p>&nbsp;&nbsp;&nbsp;&nbsp; Value -> 'value'
* 			<p>&nbsp;&nbsp;&nbsp;&nbsp; Description -> 'description2'
* <p> Form -> dataIndex: 'javaclass'. It contains the following fields:
*			<p>&nbsp;&nbsp;&nbsp;&nbsp; Java Class Name -> 'classname'
* 
* <p> *Example :- *
*  var predefLoVform = new qooxdoo.ui.custom.PredefinedLoVForm();
*  predefLoVform.setData({
* 						label: 'Label',
*  						name: 'Name',
*						description: 'Description',
*  						type: 'Type',
* 						querystmt: {
* 									datasourcelabel: 'Data Source label',
* 									querydef: 'Query Definition'
* 									},
* 						scriptloadvalues: {
* 									script: 'Script',
* 									},
* 						fixedlov: 	{
* 									value: 'Value',
* 									description2: 'Description',
* 								  	},	
* 						javaclass: {
* 									classname: 'Java Class Name'
* 									}
*  });
*
*/ 
	
	construct : function() { 
		this.base(arguments,[
        	{
        		type: 'text',
        		dataIndex: 'label',
        		text: 'Label',
        		labelwidth: 100,
        		mandatory: true	
        	},{
        		type: 'text',
        		dataIndex: 'name',
        		text: 'Name',
        		labelwidth: 100,
        		mandatory: true	
        	}, {
        		type: 'text',
        		dataIndex: 'description',
        		text: 'Description',
        		labelwidth: 100,
        		mandatory: true	
        	},  {
        		type: 'combo',
        		dataIndex: 'type',
        		text: 'Type',
        		labelwidth: 100,
        		items: ["Query Statement", "Script to load values", "Fixed List of values", "Java class"],
        		listeners: [
	        		{
	        			event: 'changeValue',
	        			handler: this._documentTypeChangeValueHandler,
	        			scope: this
	        		}     		
        		]
        	},  {
        		type: 'form',
        		dataIndex: 'querystmt',
        		form:
        				[
        					{
				        		type: 'combo',
				        		dataIndex: 'datasourcelabel',
				        		text: 'Data Source label',
				        		labelwidth: 100,
				        		items: ["FoodMart", "Pool", "Connection"]
			        		},	{
				        		type: 'textarea',
				        		dataIndex: 'querydef',
				        		text: 'Query Definition',
				        		labelwidth: 100,
				        		height: 50	
			        		}
			        	]
			     ,  invisibleLabel: true  	//,	     visible: false  
        	}, {
        		type: 'combo',
        		dataIndex: 'language',
        		text: 'Language',
        		items: ["Groovy","Javascript"],
        		mandatory: true	,
        		visible: false
        	}, {
        		type: 'form',
        		dataIndex: 'scriptloadvalues',
        		form:
        				[       		
        					{
				        		type: 'textarea',
				        		dataIndex: 'script',
				        		text: 'Script',
				        		labelwidth: 100,
				        		height: 50
        					}
        				],
			     visible: false ,
			     invisibleLabel: true  
        	}, {
        		type: 'propertiesList',
        		dataIndex: 'fixedlov',
        		text: 'Wizard Fix Lov',
        		visible: false ,
        		nameColumnLabel: 'Value' ,
        		valueColumnLabel: 'Description',	
        		cellNameRendered: 'text',
        		cellValueRendered: 'text',
        		invisibleLabel: true
        		
        	},  /*{
        		type: 'form',
        		dataIndex: 'fixedlov',
        		form:
        				[				
        					{
        					type: 'formList',
        					dataIndex: 'subformlist',
        					formList:
        								[	
				        					{ 
				        					  type: 'form',
				        					  dataindex: 'subform',		
				        					  form:	
				        					  		[{
										        		type: 'text',
										        		dataIndex: 'value',
										        		text: 'Value',
										        		labelwidth: 100,
										        		mandatory: true
								        			},
				        							{
										        		type: 'text',
										        		dataIndex: 'description2',
										        		text: 'Description',
										        		labelwidth: 100,
										        		mandatory: true
								        			}]
				        					}
								        ]
				        	}
				        ],
				 form:
        				[       		
        					{
				        		type: 'text',
				        		dataIndex: 'dummy',
				        		text: 'Dummy',
				        		labelwidth: 100,
				        		height: 50
        					}
        				],    
			     visible: false 
        	}, */ {
        		type: 'form',
        		dataIndex: 'javaclass',
        		form:
        				[		
				        	{
				        		type: 'text',
				        		dataIndex: 'classname',
				        		text: 'Java Class Name',
				        		labelwidth: 100,
				        		mandatory: true
				        	}
			        	],
			     visible: true, 	
			     invisibleLabel: true
        	}
        ]);
	},
	
	/**
	 * Event handler to display the sub form based on the value of the combo box
	 */
	members: {
		_documentTypeChangeValueHandler : function(e) {
			if( this && this.getInputField('querystmt') ) {
				//change
	        	/*if (e.getValue()== "Query Statement") {
	        		this.getInputField('querystmt').setDisplay(true);
	        		this.getInputField('scriptloadvalues').setDisplay(false);
	        		this.getInputField('fixedlov').setDisplay(false);
	        		this.getInputField('javaclass').setDisplay(false);
	        		        		
				} else if (e.getValue()== "Script to load values") {
	        		this.getInputField('querystmt').setDisplay(false);
	        		this.getInputField('scriptloadvalues').setDisplay(true);
	        		this.getInputField('fixedlov').setDisplay(false);
	        		this.getInputField('javaclass').setDisplay(false);
	        		        		
	        	} else if (e.getValue()== "Fixed List of values") {
	        		this.getInputField('querystmt').setDisplay(false);
	        		this.getInputField('scriptloadvalues').setDisplay(false);
	        		this.getInputField('fixedlov').setDisplay(true);
	        		this.getInputField('javaclass').setDisplay(false);
	        		        		
	        	} else if (e.getValue()== "Java class") {
	        		this.getInputField('querystmt').setDisplay(false);
	        		this.getInputField('scriptloadvalues').setDisplay(false);
	        		this.getInputField('fixedlov').setDisplay(false);
	        		this.getInputField('javaclass').setDisplay(true);
	        	}
	        	*/
	        	if (e.getData()== "Query Statement") {
	        		this.getInputField('querystmt').setVisibility("visible");
	        		this.getInputField('scriptloadvalues').setVisibility("excluded");
	        		this.getInputField('language').setVisibility("excluded");
	        		this.getInputField('fixedlov').setVisibility("excluded");
	        		this.getInputField('javaclass').setVisibility("excluded");
	        		        		
				} else if (e.getData()== "Script to load values") {
	        		this.getInputField('querystmt').setVisibility("excluded");
	        		this.getInputField('scriptloadvalues').setVisibility("visible");
	        		this.getInputField('language').setVisibility("visible");
	        		this.getInputField('fixedlov').setVisibility("excluded");
	        		this.getInputField('javaclass').setVisibility("excluded");
	        		        		
	        	} else if (e.getData()== "Fixed List of values") {
	        		this.getInputField('querystmt').setVisibility("excluded");
	        		this.getInputField('scriptloadvalues').setVisibility("excluded");
	        		this.getInputField('language').setVisibility("excluded");
	        		this.getInputField('fixedlov').setVisibility("visible");
	        		this.getInputField('javaclass').setVisibility("excluded");
	        		        		
	        	} else if (e.getData()== "Java class") {
	        		this.getInputField('querystmt').setVisibility("excluded");
	        		this.getInputField('scriptloadvalues').setVisibility("excluded");
	        		this.getInputField('language').setVisibility("excluded");
	        		this.getInputField('fixedlov').setVisibility("excluded");
	        		this.getInputField('javaclass').setVisibility("visible");
	        	}
	        }
		} 
	}
});