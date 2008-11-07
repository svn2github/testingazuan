/*
SpagoBI - The Business Intelligence Free Platform

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
* This class defines the Map Details Form.  
*   
*/


qx.Class.define("spagobi.ui.custom.MapDetailsForm", {
	extend: spagobi.ui.Form,

	/** 
	*  When the constructor is called it returns an object of form type.
	* <p> To this form is associated the following fields :- 
	* <p> Name  -> dataIndex: 'name'
	* <p> Description -> dataIndex: 'description'
	* <p> Template -> dataIndex: 'template'
	* <p> Format  -> dataIndex: 'format'
	* <p> formList: spagobi.ui.custom.FeatureDetailsForm -> dataIndex: 'features'
	* <p> *Example :- *
	*  var simpleform = new spagobi.ui.custom.MapDetailsForm();
	*  simpleform.setData({
	*  name:'Name',
	*  description:'Description',
	*  template:'Template',
	*  format:'Format',
	*  features: [{
	*  name:'Name',
	*  description:'Description',
	*  type: 'Type'
	*     [{
	*  });
	*
	*/ 
	construct : function() { 
		//this.base(arguments, this.self(arguments).formStructure);
		this.base(arguments,[
        	{
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
        		type: 'text',
        		dataIndex: 'template',
        		text: 'Template',
        		mandatory: false	
        	}, {
        		type: 'combo',
        		dataIndex: 'format',
        		text: 'Format',
        		items: ["SVG"]		
        	}, {
        		//type: 'form',
        		type: 'formList',
        		dataIndex: 'features',
        		formList: spagobi.ui.custom.FeatureDetailsForm
        		
        		/*[
        			{
			        	type: 'text',
			        	dataIndex: 'name',
			        	text: 'Name',
			        	//left: 0,
			        	mandatory: false	
			        }, {
			        	type: 'text',
			        	dataIndex: 'description',
			        	text: 'Description',
			        	mandatory: false	
			        }, {
			        	type: 'text',
			        	dataIndex: 'type',
			        	text: 'Type',
			        	mandatory: false	
        			}
        		]	*/	
        	}
        ]);
		
	}
});