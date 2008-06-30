/**
 *
 
 */


qx.Class.define("spagobi.ui.custom.DatasetDetailsForm", {
	extend: spagobi.ui.Form,
	
	construct : function() { 
		//this.base(arguments, this.self(arguments).formStructure);
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
        		text: 'Descr',
        		mandatory: false		
        	},{
        		type: 'combo',
        		dataIndex: 'type',
        		text: 'Type',
        		items: ["File","Web","Query"],
	        	listeners: [
	        		{
	        			event: 'changeValue',
	        			handler: this._documentTypeChangeValueHandler,
	        			scope: this
	        		}        		
        		]
        	}, {
        		type: 'text',
        		dataIndex: 'fileName',
        		text: 'File name',
        		mandatory: false	
        	}
        ]);
		
	},
	
	members: {
		_documentTypeChangeValueHandler : function(e) {
        	 if( this && this.getInputField('fileName') ) {
        		if (e.getValue()=="File") {
					this.getInputField('fileName').setDisplay(true);
					
				} else {
					this.getInputField('fileName').setDisplay(false);
				}
        	}        	
        }
	}
});