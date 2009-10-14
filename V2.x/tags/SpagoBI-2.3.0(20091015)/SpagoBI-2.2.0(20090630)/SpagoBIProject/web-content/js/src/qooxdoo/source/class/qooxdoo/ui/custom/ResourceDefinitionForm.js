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
qx.Class.define("qooxdoo.ui.custom.ResourceDefinitionForm", {
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
        		dataIndex: 'name',
        		text: 'Name',
        		labelwidth: 100,
        		mandatory: true	
        	},{
        		type: 'textarea',
        		dataIndex: 'discription',
        		text: 'Description',
        		labelwidth: 100,
        		height: 50,
        		width: 300
    		}, {
        		type: 'text',
        		dataIndex: 'tablename',
        		text: 'Table Name',
        		labelwidth: 100
        //		mandatory: true	
        	},  {
        		type: 'text',
        		dataIndex: 'columnname',
        		text: 'Column Name',
        		labelwidth: 100
       // 		mandatory: true	
        	}, {
        		type: 'text',
        		dataIndex: 'typename',
        		text: 'Type Name',
        		labelwidth: 100,
        		'readOnly': true
       // 		mandatory: true	
        	}	 
        	]);
}
});