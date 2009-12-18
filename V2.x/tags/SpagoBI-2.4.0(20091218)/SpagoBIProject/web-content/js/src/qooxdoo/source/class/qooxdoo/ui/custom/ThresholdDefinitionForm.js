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
* This class defines the Threshold Definition Form. 
* 
*/

qx.Class.define("qooxdoo.ui.custom.ThresholdDefinitionForm", {
	extend: qooxdoo.ui.form.Form,
	
	/**
	 * 
	 */
	
	construct : function() { 
				
		this.base(arguments,[

					{
						type: 'text',
						dataIndex: 'name',
						text: 'Label',
						mandatory: true	
					}, {
						type: 'text',
						dataIndex: 'code',
						text: 'Code',
						mandatory: false		
					}, {
						type: 'textarea',
						dataIndex: 'description',
						text: 'Description',
						mandatory: false	
					}, {
						type: 'combo',
						dataIndex: 'thresholdtype',
						text: 'Threshold Type',
						items: ["Range Values","Minimum Value","Maximum Value"],
						mandatory: false,
						read : true
					}, {
		        		type: 'formList',
		        		dataIndex: 'thresholdvalues',
		        		formList: [
		        		           {
										type: 'text',
										dataIndex: 'position',
										text: 'Position',
										mandatory: false		
		        		           },{
										type: 'text',
										dataIndex: 'label',
										text: 'Label',
										mandatory: false		
			        		       },{
										type: 'text',
										dataIndex: 'minvalue',
										text: 'Min Value',
										mandatory: false		
			        		       },{
										type: 'text',
										dataIndex: 'maxvalue',
										text: 'Max value',
										mandatory: false		
			        		       },{
										type: 'text',
										dataIndex: 'color',
										text: 'Color',
										mandatory: false,
										button : [
			        		    	   				{
			        		    	   					label : 'Select',
			        		    	   					event: "mousedown",
			        		    	   					handler: this._colorSelectorFunction,
			        		    	   					scope : this
			        		    	   				}	
			        		       				 ]
			        		       },{
				   						type: 'combo',
										dataIndex: 'severity',
										text: 'Severity',
										items: ["Urgent","High","Medium","Low"],
										mandatory: false
			        		       }
		        		           ] 
		        	    }          
			]
		);
		
		this.colorSelectorPopup = new qx.ui.control.ColorPopup();
		this.colorSelectorPopup.exclude();
		this.colorSelectorPopup.setValue("#FFFFFF");
		this.colorSelectorPopup.addListener("changeValue", this._showValueInLabel, this);

	},
	
	members: {
		
		colorSelectorPopup: undefined,
		
		_colorSelectorFunction : function(e) {
			
			this.colorSelectorPopup.placeToMouse(e)
			this.colorSelectorPopup.show();

		},
		
		_showValueInLabel : function(e) {
			
			/*Patch for 0.8 corrected in 0.8.1 
			
		    if (e.getTarget().getRed() === null){
		    	e.getTarget().setRed(255);
		    }	
		    	
		    if (e.getTarget().getGreen() === null){
		    	e.getTarget().setGreen(255);
		    }
		    
		    if (e.getTarget().getBlue() === null){
		    	e.getTarget().setBlue(255);
		      }
		    */
			
			var myFormList = this.getInputField('thresholdvalues');
			var myTabView = myFormList.getChildren()[0];
			var myPage = myTabView.getSelected();
			var myForm = myPage.getChildren()[0];
			var l = myForm.getInputField('color').getUserData('field').getChildren()[0];
			
			//l.setDecorator(null);
			l.setDecorator(new qx.ui.decoration.Single(1,"solid","black"));
			
			//l.setValue(''+e.getTarget().getValue());
			if(e.getTarget().getValue()!= null){
				l.setValue(e.getTarget().getValue());
			}	
			else{
				/*Patch for 0.8 corrected in 0.8.1 */
				e.getTarget().setRed(255);
				e.getTarget().setGreen(255);
				e.getTarget().setBlue(255);
				
				l.setValue("");
			}
			
			l.setBackgroundColor(e.getData());
	      }
	}
});
				                     
				                     
				                     
				                     
				                     
				                     
				                     



