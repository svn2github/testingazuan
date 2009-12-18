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
* This class defines the KPI Model Definition Form. 
* 
*/

qx.Class.define("qooxdoo.ui.custom.KpiModelDefinitionDetailForm", {
	extend: qooxdoo.ui.form.Form,
	
	/**
	 * 
	 */
	
	construct : function() { 
				
		this.base(arguments,[

					{
						type: 'groupbox',
						dataIndex: 'modeldefn',
						text: 'Model Definition',
						form:	[{
									type: 'text',
									dataIndex: 'name',
									text: 'Name',
									mandatory: true	
								}, {
									type: 'textarea',
									dataIndex: 'description',
									text: 'Description'
								}, {
									type: 'text',
									dataIndex: 'code',
									text: 'Code'		
								}, {
									type: 'text',
									dataIndex: 'typename',
									text: 'Type Name'		
								}, {
									type: 'textarea',
									dataIndex: 'typedescription',
									text: 'Type Description'
								}
							   ]
					},
					{
						type: 'groupbox',
						dataIndex: 'modelattrib',
						text: 'Model Attributes'
					},
					{
						type: 'groupbox',
						dataIndex: 'setkpi',
						text: 'Set Kpi',
						form:[
								{
									type: 'text',
									dataIndex: 'kpiname',
									text: 'Kpi Name',
									button : [
								   				{
								   					label : 'Select',
								   					event: "mousedown",
								   					handler: this._lookupKpiName,
								   					scope : this
								   				}	
												 ]
								}
						      ]
					}	
			]
		);	
	},
	
	members: {
		
		_lookupKpiName : function(e) {
			var c1 = this.getInputField('setkpi');		//container having sub-container
			var c2 = c1.getUserData('field');					     //sub-container having label, filed and button
			var gb = c2.getChildren()[0];                   //field inside sub-container
			var form = gb.getChildren()[0];
			var fieldctr1 = form.getInputField('kpiname');
			var fieldctr2 = fieldctr1.getUserData('field');
			var f = fieldctr2.getChildren()[0];
			f.setValue("KPI Name after Lookup");
		}
	}
});