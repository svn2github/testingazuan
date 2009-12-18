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
* This class defines the Data Source Details Form.  
*   
*/


qx.Class.define("qooxdoo.ui.custom.DistributionListDetailsForm", {
	extend: qooxdoo.ui.form.Form,
	
	/** 
	*  When the constructor is called it returns an object of form type.
	* <p> To this form is associated the following fields :- 
	* <p> Label -> dataIndex: 'label'
	* <p> Description  -> dataIndex: 'description'
	* <p> Dialect  -> dataIndex: 'dialect'
	* <p> Type -> dataIndex: 'type'
	* <p> Jndi Name  -> dataIndex: 'jndiname'
	* <p> Url  -> dataIndex: 'url'
	* <p> User  -> dataIndex: 'user'
	* <p> Password  -> dataIndex: 'passord'
	* <p> Driver  -> dataIndex: 'driver'
	* <p> *Example :- *
	*  var simpleform = new qooxdoo.ui.custom.DatasourceDetailsForm();
	*  simpleform.setData({
	*  label:'Label',
	*  description:'Description',
	*  dialect: 'Dialect',
	*  type: 'Type',
	*  jndiname:  'Jndi Name',
	*  url: 'URL',
	*  user: 'User',
	*  password: 'Password',
	*  driver: 'Driver'
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
        	}, {
        		type: 'text',
        		dataIndex: 'description',
        		text: 'Description'
        	}
        ]);
		
	}
});