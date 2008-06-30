/**
 *
 
 */


qx.Class.define("spagobi.ui.custom.DatasourceDetailsForm", {
	extend: spagobi.ui.Form,
	
	construct : function() { 
		//this.base(arguments, this.self(arguments).formStructure);
		this.base(arguments,[
        	{
        		type: 'text',
        		dataIndex: 'label',
        		text: 'LABEL',
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
        		items: ["SQL SERVER","HQL", "MYSQL","INGRES"]		
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
        		mandatory: false
        	},{
        		type: 'text',
        		dataIndex: 'user',
        		text: 'User',
        		mandatory: false
        	},{
        		type: 'text',
        		dataIndex: 'password',
        		text: 'Password',
        		mandatory: false
        	},{
        		type: 'text',
        		dataIndex: 'driver',
        		text: 'Driver',
        		mandatory: false
        	}
        ]);
		
	},
	
	members: {
		_documentTypeChangeValueHandler : function(e) {
        	if( this && this.getInputField('type') ) {
        		if (e.getValue()=="With Jndi Name") {
					this.getInputField('jndiname').setDisplay(true);
					this.getInputField('url').setDisplay(false);
					this.getInputField('user').setDisplay(false);
					this.getInputField('password').setDisplay(false);
					this.getInputField('driver').setDisplay(false);
					
				} else {
					this.getInputField('jndiname').setDisplay(false);
					this.getInputField('url').setDisplay(true);
					this.getInputField('user').setDisplay(true);
					this.getInputField('password').setDisplay(true);
					this.getInputField('driver').setDisplay(true);
					
				}
        	}        	
        } 
	}
});