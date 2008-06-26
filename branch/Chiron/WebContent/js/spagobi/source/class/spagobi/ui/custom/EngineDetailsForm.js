/**
 *
 
 */


qx.Class.define("spagobi.ui.custom.EngineDetailsForm", {
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
        		text: 'Description',
        		mandatory: false		
        	}, {
        		type: 'combo',
        		dataIndex: 'documentType',
        		text: 'Document type',
        		items: ["Report","Map"]		
        	}, {
        		type: 'combo',
        		dataIndex: 'engineType',
        		text: 'Engine type',
        		items: ["Internal","External"],
	        	listeners: [
	        		{
	        			event: 'changeValue',
	        			handler: this._documentTypeChangeValueHandler,
	        			scope: this
	        		}        		
        		]
        	}, {
        		type: 'check',
        		dataIndex: 'useDataSet',
        		text: 'Use Data Set',
        		checked: false	
        	}, {
        		type: 'check',
        		dataIndex: 'useDataSource',
        		text: 'Use Data Source',
	        	checked: true,
	        	listeners: [
	        		{
	        			event: 'changeChecked',
	        			handler: this._useDataSourceChangeCheckedHandler,
	        			scope: this
	        		} 
				]
        	}, {
        		type: 'combo',
        		dataIndex: 'dataSource',
        		text: 'Data Source',
        		items: ["foodmart","geo", "spagobi"]
        	}, {
        		type: 'text',
        		dataIndex: 'class',
        		text: 'Class',
        		mandatory: true			
        	}, {
        		type: 'text',
        		dataIndex: 'url',
        		text: 'Url',
        		mandatory: true			
        	}, {
        		type: 'text',
        		dataIndex: 'driver',
        		text: 'Driver Name',
        		mandatory: true			
        	}
        ]);
		
	},
	
	members: {
		_documentTypeChangeValueHandler : function(e) {
        	if( this && this.getInputField('url') ) {
        		if (e.getValue()=="Internal") {
					this.getInputField('url').setDisplay(false);
					this.getInputField('driver').setDisplay(false);
					this.getInputField('class').setDisplay(true);
				} else {
					this.getInputField('url').setDisplay(true);
					this.getInputField('driver').setDisplay(true);
					this.getInputField('class').setDisplay(false);
				}
        	}
        	
        },       
        
        _useDataSourceChangeCheckedHandler : function(e) {
        	if( this && this.getInputField('dataSource') ) {
        		this.getInputField('dataSource').setDisplay( e.getValue() );
        	}
        }  
	}
});