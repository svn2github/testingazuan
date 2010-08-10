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
* This class defines the Sub form used in the Analytical Driver details form.  
*   
*/

qx.Class.define("spagobi.ui.custom.UseModeSubForm", {
	extend: spagobi.ui.Form,
	
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
        	}, {
        		type: 'radio',
        		dataIndex: 'type',
        		text: 'Type',
        		items: ["Lov", "Manual Input"],
        		
        		listeners: [
	        		{
	        			event: 'changeSelected',
	        			handler: this._lovTypeChangeValueHandler,
	        			scope: this
	        		} 
				]
				
        	}, {
        		type: 'text',
        		dataIndex: 'lov',
        		text: 'Lov',
        		mandatory: true	
        	}, {
        		type: 'combo',
        		dataIndex: 'lovlist',
        		text: 'Selection List',
        		items: ["List","Check List","Combo Box"]		
        	}, {
        		type: 'text',
        		dataIndex: 'manualinput',
        		text: 'Manual Input',
        		mandatory: false,
        		visible: false	
        	}, {
               type: 'check',
               dataIndex: 'mychecklist',
               checked: false,
               text: 'Roles',
               columns: 5,
               items: ["aaaa","bbbb","cccc","dddd","eeee","ffff","gggg","hhhh","iiii","jjjj"]
           }, {
               type: 'check',
               dataIndex: 'checklist1',
               checked: false,
               text: 'Constraints',
               columns: 3,
               items: ["aaaa","bbbb","cccc","dddd","eeee","ffff","gggg","hhhh","iiii"]
           }
        	]);
	},
	
	members: {
		_lovTypeChangeValueHandler : function(e) {
        	if( this && this.getInputField('type') ) {
        		if (e.getValue().getLabel()=="Lov") {
        			this.getInputField('lov').setDisplay(true);
					this.getInputField('lovlist').setDisplay(true);
					this.getInputField('manualinput').setDisplay(false);
					
				} else if (e.getValue().getLabel()=="Manual Input") {
					this.getInputField('lov').setDisplay(false);
					this.getInputField('lovlist').setDisplay(false);
					this.getInputField('manualinput').setDisplay(true);
					
				}
        	}
        	
        }      
        
        
	}
});