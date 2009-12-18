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


qx.Class.define("qooxdoo.ui.custom.DatasourceDetailsForm", {
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
        		dataIndex: 'label',
        		text: 'Label',
        		mandatory: true	
        	}, {
        		type: 'text',
        		dataIndex: 'description',
        		text: 'Description',
        		mandatory: false	
        	},  {
        		type: 'combo',
        		dataIndex: 'dialect',
        		text: 'Dialect',
        		items: ["Sql Server","HQL", "MySql","Ingres"]		
        	}, {
        		type: 'combo',
        		dataIndex: 'type',
        		text: 'Type',
        		items: ["With Jndi Name","With Analytical Drivers"],
	        	listeners: [
	        		{
	        			event: 'changeValue',
	        			handler: this._documentTypeChangeValueHandler,
	        			scope: this
	        		}        		
        		]
        	}, {
        		type: 'text',
        		dataIndex: 'jndiname',
        		text: 'Jndi Name',
        		mandatory: false
        	}, {
        		type: 'text',
        		dataIndex: 'url',
        		text: 'Url',
        		mandatory: false,
        		visible: false
        	},{
        		type: 'text',
        		dataIndex: 'user',
        		text: 'User',
        		mandatory: false,
        		visible: false
        	},{
        		type: 'text',
        		dataIndex: 'password',
        		text: 'Password',
        		mandatory: false,
        		visible: false,
        		password: true
        	},{
        		type: 'text',
        		dataIndex: 'driver',
        		text: 'Driver',
        		mandatory: false,
        		visible: false
        	}
        ]);
		
	},
	
	members: {
		_documentTypeChangeValueHandler : function(e) {
        	if( this && this.getInputField('jndiname') ) {
        		/*//change
        		if (e.getValue()=="With Jndi Name") {
					this.getInputField('jndiname').setDisplay(true);
					this.getInputField('url').setDisplay(false);
					this.getInputField('user').setDisplay(false);
					this.getInputField('password').setDisplay(false);
					this.getInputField('driver').setDisplay(false);//
				*/
				if (e.getData()=="With Jndi Name") {
					this.getInputField('jndiname').setVisibility("visible");
					this.getInputField('url').setVisibility("excluded");
					this.getInputField('user').setVisibility("excluded");
					this.getInputField('password').setVisibility("excluded");
					this.getInputField('driver').setVisibility("excluded");
					
				} else {
					/*//change
					this.getInputField('jndiname').setDisplay(false);//
					this.getInputField('url').setDisplay(true);
					this.getInputField('user').setDisplay(true);
					this.getInputField('password').setDisplay(true);
					this.getInputField('driver').setDisplay(true);
					*/
					this.getInputField('jndiname').setVisibility("excluded");
					this.getInputField('url').setVisibility("visible");
					this.getInputField('user').setVisibility("visible");
					this.getInputField('password').setVisibility("visible");
					this.getInputField('driver').setVisibility("visible");
				}
        	}        	
        } 
	}
});