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

qx.Class.define("qooxdoo.ui.custom.KpiModelInstanceForm", {
	extend: qooxdoo.ui.form.Form,
	
	/**
	 * 
	 */
	
	construct : function() { 
				
		this.base(arguments,[

					{
						type: 'groupbox',
						dataIndex: 'modelinstance',
						text: 'Model Instance',
						form:	[{
									type: 'text',
									dataIndex: 'instancename',
									text: 'Name',
									mandatory: true	
								}, {
									type: 'text',
									dataIndex: 'instancelabel',
									text: 'Label'		
								}, {
									type: 'textarea',
									dataIndex: 'instancedesc',
									text: 'Description'
								}
							   ]
					},
					{
						type: 'groupbox',
						dataIndex: 'modeldefn',
						text: 'Model Definition',
						form:	[{
									type: 'text',
									dataIndex: 'defnname',
									text: 'Name',
									mandatory: true	
								}, {
									type: 'textarea',
									dataIndex: 'defndesc',
									text: 'Description'
								}, {
									type: 'text',
									dataIndex: 'defncode',
									text: 'Code'		
								}, {
									type: 'text',
									dataIndex: 'defntypename',
									text: 'Type Name'		
								}, {
									type: 'textarea',
									dataIndex: 'defntypedesc',
									text: 'Type Description'
								}
							   ],
						read : true	   
					},
					{
						type: 'groupbox',
						dataIndex: 'modelattrib',
						text: 'Model Attributes'
					},
					{
						type: 'groupbox',
						dataIndex: 'kpiinstance',
						text: 'Kpi Instance',
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
								},
								{
									type: 'flag',
									dataIndex: 'default',
									text: 'Restore Default'
								},
								{
					        		type: 'combo',
					        		dataIndex: 'thresholdname',
					        		text: 'Threshold Name',
					        		items: ["Vallore","Alarme"]
								},
								{
									type: 'text',
									dataIndex: 'wtdefault',
									text: 'Weight default value '
								},
								{
					        		type: 'combo',
					        		dataIndex: 'charttype',
					        		text: 'Chart Type',
					        		items: ["Meter","SimpleDial"]
								},
								{
					        		type: 'combo',
					        		dataIndex: 'periodicity',
					        		text: 'Periodicity',
					        		items: ["Daily","Weekly"]
								}		
						      ]
					}	
			]
		);	
	},
	
	members: {
		
		_lookupKpiName : function(e) {
			var c1 = this.getInputField('kpiinstance');		//container having sub-container
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