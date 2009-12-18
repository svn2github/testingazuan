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
* This class defines the KPI Alarm Contact Details Form. 
* 
*/

qx.Class.define("qooxdoo.ui.custom.KpiAlarmContactDetailsForm", {
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
        		dataIndex: 'name',
        		text: 'Name',
        		mandatory: true	
        	},{
        		type: 'text',
        		dataIndex: 'mobile',
        		text: 'Mobile',
        		mandatory: true	
        	},{
        		type: 'text',
        		dataIndex: 'email',
        		text: 'Email',
        		mandatory: true	
        	},{
        		type: 'text',
        		dataIndex: 'resource',
        		text: 'Resource',
        		//mandatory: true,
				button : [
			   				{
			   					label : 'Select',
			   					event: "execute",//change to execute //mousedown
			   					handler: this._lookupResourceName,
			   					scope : this
			   				}	
							 ]
        	}
        	
        ]);
		
	},
	
	members: {
		
		_lookupResourceName : function(e) {
			var c1 = this.getInputField('resource');		//container having sub-container
			var c2 = c1.getUserData('field');			//sub-container having label, filed and button
			var f = c2.getChildren()[0];                //field inside sub-container
			f.setValue("Resource Name after Lookup");
		}
	}
});