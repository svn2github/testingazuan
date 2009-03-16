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
* This class defines the Constraint Details Form.  
*   
*/



qx.Class.define("qooxdoo.ui.custom.ConstraintDetailsForm", {
	extend: qooxdoo.ui.form.Form,

	/** 
	*  When the constructor is called it returns an object of form type.
	* <p> To this form is associated the following fields :- 
	* <p> Name  -> dataIndex: 'name'
	* <p> Description -> dataIndex: 'description',
	* <p> Type -> dataIndex: 'type'
	* <p> *Example :- *
	*  var simpleform = new qooxdoo.ui.custom.FeatureDetailsForm();
	*  simpleform.setData({
	*  name: 'Name',
	*  description: 'Description',
	*  type: 'Type'
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
        	}, {
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
        		mandatory: false	
        	}, {
        		type: 'combo',
        		dataIndex: 'type',
        		text: 'Check Type',
        		items: ["Date", "Regexp", "Max Length", "Range", "Decimal", "Min Length"],
        		mandatory: false,
        		labelwidth: 100,
        		listeners: [
	        		{
	        			event: 'changeValue',
	        			handler: this._documentTypeChangeValueHandler,
	        			scope: this
	        		}        		
        		]	
        	}, {
        		type: 'text',
        		dataIndex: 'datevalueformat',
        		text: 'Date Value Format',
        		value: 'dd/mm/yyyy',
        		labelwidth: 100,
        		mandatory: false	
        	}, {
        		type: 'text',
        		dataIndex: 'regularexpression',
        		text: 'Regular Expression',
        		labelwidth: 100,
        		mandatory: false,
			    visible: false 	
        	}, {
        		type: 'text',
        		dataIndex: 'maxlengthvalue',
        		text: 'Max Length Value',
        		labelwidth: 100,
        		mandatory: false,
			    visible: false	
        	}, {
        		type: 'text',
        		dataIndex: 'lrv',
        		text: 'Lower Range Value ',
        		labelwidth: 100,
        		mandatory: false,
			    visible: false	
        	}, {
        		type: 'text',
        		dataIndex: 'higherrangevalue',
        		text: 'Higher Range Value',
        		labelwidth: 100,
        		mandatory: false,
			    visible: false	
        	}, {
        		type: 'text',
        		dataIndex: 'decimalplaces',
        		text: 'Decimal Places',
        		labelwidth: 100,
        		mandatory: false,
			    visible: false		
        	},{
        		type: 'text',
        		dataIndex: 'minlengthvalue',
        		text: 'Min Length Value ',
        		labelwidth: 100,
        		mandatory: false,
			    visible: false	
        	}
        	
        ]);
	},
	
	members: {
		_documentTypeChangeValueHandler : function(e) {
        	if( this && this.getInputField('datevalueformat') ) {	
        		//change
        		/*if (e.getValue()==null) {
					this.getInputField('datevalueformat').setDisplay(false);
					this.getInputField('regularexpression').setDisplay(false);
					this.getInputField('maxlengthvalue').setDisplay(false);
					this.getInputField('lrv').setDisplay(false);
					this.getInputField('higherrangevalue').setDisplay(false);
					this.getInputField('decimalplaces').setDisplay(false);
					this.getInputField('minlengthvalue').setDisplay(false);
					
				} else if (e.getValue()=="Date") { 
					this.getInputField('datevalueformat').setDisplay(true);
					this.getInputField('regularexpression').setDisplay(false);
					this.getInputField('maxlengthvalue').setDisplay(false);
					this.getInputField('lrv').setDisplay(false);
					this.getInputField('higherrangevalue').setDisplay(false);
					this.getInputField('decimalplaces').setDisplay(false);
					this.getInputField('minlengthvalue').setDisplay(false);
					
				} else if (e.getValue()=="Regexp") {
					this.getInputField('datevalueformat').setDisplay(false);
					this.getInputField('regularexpression').setDisplay(true);
					this.getInputField('maxlengthvalue').setDisplay(false);
					this.getInputField('lrv').setDisplay(false);
					this.getInputField('higherrangevalue').setDisplay(false);
					this.getInputField('decimalplaces').setDisplay(false);
					this.getInputField('minlengthvalue').setDisplay(false);
					
				} else if (e.getValue()=="Max Length") {
					this.getInputField('datevalueformat').setDisplay(false);
					this.getInputField('regularexpression').setDisplay(false);
					this.getInputField('maxlengthvalue').setDisplay(true);
					this.getInputField('lrv').setDisplay(false);
					this.getInputField('higherrangevalue').setDisplay(false);
					this.getInputField('decimalplaces').setDisplay(false);
					this.getInputField('minlengthvalue').setDisplay(false);
					
				} else if (e.getValue()=="Range") {
					this.getInputField('datevalueformat').setDisplay(false);
					this.getInputField('regularexpression').setDisplay(false);
					this.getInputField('maxlengthvalue').setDisplay(false);
					this.getInputField('lrv').setDisplay(true);
					this.getInputField('higherrangevalue').setDisplay(true);
					this.getInputField('decimalplaces').setDisplay(false);
					this.getInputField('minlengthvalue').setDisplay(false);
					
				} else if (e.getValue()=="Decimal") {
					this.getInputField('datevalueformat').setDisplay(false);
					this.getInputField('regularexpression').setDisplay(false);
					this.getInputField('maxlengthvalue').setDisplay(false);
					this.getInputField('lrv').setDisplay(false);
					this.getInputField('higherrangevalue').setDisplay(false);
					this.getInputField('decimalplaces').setDisplay(true);
					this.getInputField('minlengthvalue').setDisplay(false);
					
				} else if (e.getValue()=="Min Length") {
					this.getInputField('datevalueformat').setDisplay(false);
					this.getInputField('regularexpression').setDisplay(false);
					this.getInputField('maxlengthvalue').setDisplay(false);
					this.getInputField('lrv').setDisplay(false);
					this.getInputField('higherrangevalue').setDisplay(false);
					this.getInputField('decimalplaces').setDisplay(false);
					this.getInputField('minlengthvalue').setDisplay(true);
					
				}*/
				
				if (e.getData()==null) {
					this.getInputField('datevalueformat').setVisibility("excluded");
					this.getInputField('regularexpression').setVisibility("excluded");
					this.getInputField('maxlengthvalue').setVisibility("excluded");
					this.getInputField('lrv').setVisibility("excluded");
					this.getInputField('higherrangevalue').setVisibility("excluded");
					this.getInputField('decimalplaces').setVisibility("excluded");
					this.getInputField('minlengthvalue').setVisibility("excluded");
					
				} else if (e.getData()=="Date") { 
					this.getInputField('datevalueformat').setVisibility("visible");
					this.getInputField('regularexpression').setVisibility("excluded");
					this.getInputField('maxlengthvalue').setVisibility("excluded");
					this.getInputField('lrv').setVisibility("excluded");
					this.getInputField('higherrangevalue').setVisibility("excluded");
					this.getInputField('decimalplaces').setVisibility("excluded");
					this.getInputField('minlengthvalue').setVisibility("excluded");
					
				} else if (e.getData()=="Regexp") {
					this.getInputField('datevalueformat').setVisibility("excluded");
					this.getInputField('regularexpression').setVisibility("visible");
					this.getInputField('maxlengthvalue').setVisibility("excluded");
					this.getInputField('lrv').setVisibility("excluded");
					this.getInputField('higherrangevalue').setVisibility("excluded");
					this.getInputField('decimalplaces').setVisibility("excluded");
					this.getInputField('minlengthvalue').setVisibility("excluded");
					
				} else if (e.getData()=="Max Length") {
					this.getInputField('datevalueformat').setVisibility("excluded");
					this.getInputField('regularexpression').setVisibility("excluded");
					this.getInputField('maxlengthvalue').setVisibility("visible");
					this.getInputField('lrv').setVisibility("excluded");
					this.getInputField('higherrangevalue').setVisibility("excluded");
					this.getInputField('decimalplaces').setVisibility("excluded");
					this.getInputField('minlengthvalue').setVisibility("excluded");
					
				} else if (e.getData()=="Range") {
					this.getInputField('datevalueformat').setVisibility("excluded");
					this.getInputField('regularexpression').setVisibility("excluded");
					this.getInputField('maxlengthvalue').setVisibility("excluded");
					this.getInputField('lrv').setVisibility("visible");
					this.getInputField('higherrangevalue').setVisibility("visible");
					this.getInputField('decimalplaces').setVisibility("excluded");
					this.getInputField('minlengthvalue').setVisibility("excluded");
					
				} else if (e.getData()=="Decimal") {
					this.getInputField('datevalueformat').setVisibility("excluded");
					this.getInputField('regularexpression').setVisibility("excluded");
					this.getInputField('maxlengthvalue').setVisibility("excluded");
					this.getInputField('lrv').setVisibility("excluded");
					this.getInputField('higherrangevalue').setVisibility("excluded");
					this.getInputField('decimalplaces').setVisibility("visible");
					this.getInputField('minlengthvalue').setVisibility("excluded");
					
				} else if (e.getData()=="Min Length") {
					this.getInputField('datevalueformat').setVisibility("excluded");
					this.getInputField('regularexpression').setVisibility("excluded");
					this.getInputField('maxlengthvalue').setVisibility("excluded");
					this.getInputField('lrv').setVisibility("excluded");
					this.getInputField('higherrangevalue').setVisibility("excluded");
					this.getInputField('decimalplaces').setVisibility("excluded");
					this.getInputField('minlengthvalue').setVisibility("visible");
					
				}
			}	 
		}	
        	
        }       
        
       
	
});