Ext.define('Sbi.tools.datasource.DataSourceDetailPanel', {
    extend: 'Ext.form.Panel'

    ,config: {
    	//frame: true,
    	bodyPadding: '5 5 0',
    	defaults: {
            width: 400
        },        
        fieldDefaults: {
            labelAlign: 'right',
            msgTarget: 'side'
        },
        border: false,
        services:[]
    }

	, constructor: function(config) {
		this.initConfig(config);
		this.initFields();
		this.items=[this.dataSourceId, this.dataSourceDialectId, this.dataSourceLabel , this.dataSourceDescription, this.dataSourceDialect, this.dataSourceMultischema , this.dataSourceMultischemaAttribute, this.dataSourceTypeJdbc, this.dataSourceTypeJndi,this.dataSourceJndiName, this.dataSourceJdbcUrl, this.dataSourceJdbcUser, this.dataSourceJdbcPassword ,this.dataSourceDriver]
		
		this.addEvents('save');
		this.tbar = Sbi.widget.toolbar.StaticToolbarBuilder.buildToolbar({items:[{name:'->'},{name:'save'}]},this);
		this.tbar.on("save",function(){
			this.fireEvent("save", this.getValues());
		},this)
		
		this.callParent(arguments);
    }

	, initFields: function(){
		this.dataSourceId = Ext.create("Ext.form.field.Hidden",{
			name: "DATASOURCE_ID"
		});
//		this.dataSourceDialectId = Ext.create("Ext.form.field.Hidden",{
//			name: "DIALECT_ID"
//		});
		this.dataSourceLabel = Ext.create("Ext.form.field.Text",{
			name: "DATASOURCE_LABEL",
			fieldLabel: LN('sbi.datasource.label'),
			allowBlank: false
		});
		this.dataSourceDescription = Ext.create("Ext.form.field.Text",{
			name: "DESCRIPTION",
			fieldLabel: LN('sbi.datasource.description'),
			allowBlank: false
		});

	
		Ext.create("Ext.form.field.Text",{
			name: "DIALECT_NAME",
			fieldLabel: LN('sbi.datasource.dialect'),
			allowBlank: false
		});	   
    	
    	Ext.define("DialectModel", {
    		extend: 'Ext.data.Model',
            fields: ["VALUE_NM","VALUE_DS","VALUE_ID"]
    	});
    	
    	var dialectStore=  Ext.create('Ext.data.Store',{
    		model: "DialectModel",
    		proxy: {
    			type: 'ajax',
    			extraParams : {DOMAIN_TYPE:"DIALECT_HIB"},
    			url:  this.services['getDialects'],
    			reader: {
    				type:"json"
    			}
    		}
    	});
    	dialectStore.load();
		this.dataSourceDialect = new Ext.create('Ext.form.ComboBox', {
			fieldLabel: LN('sbi.datasource.dialect'),
	        store: dialectStore,
	        name: "DIALECT_ID",
	        displayField:'VALUE_DS',
	        valueField:'VALUE_ID'
	    });
    	
		this.dataSourceMultischema = Ext.create("Ext.form.Checkbox",{
	        fieldLabel: LN('sbi.datasource.multischema'),
	        name: "MULTISCHEMA",
	        value: false
		});
		
		this.dataSourceMultischemaAttribute = Ext.create("Ext.form.field.Text",{
			name: "SCHEMA",
			fieldLabel: LN('sbi.datasource.multischema.attribute'),
			allowBlank: true,
			hidden: true
		});
		
		this.dataSourceMultischema.on("change", function(field, newValue, oldValue, eOpts){
			if(newValue){
				this.dataSourceMultischemaAttribute.show();
			}else{
				this.dataSourceMultischemaAttribute.hide();
			}
		},this);
		
		this.dataSourceTypeJdbc = Ext.create("Ext.form.field.Radio",{
			fieldLabel: LN('sbi.datasource.type'),
			boxLabel: LN('sbi.datasource.type.jdbc'), 
			name: 'TYPE' , 
			inputValue:'jdbc'
		})
		
		this.dataSourceTypeJndi = Ext.create("Ext.form.field.Radio",{
            hideEmptyLabel: false,
			boxLabel: LN('sbi.datasource.type.jndi'), 
			name: 'TYPE' , 
			inputValue:'jndi'
		});

		this.dataSourceJndiName= Ext.create("Ext.form.field.Text",{
			name: "JNDI_URL",
			fieldLabel: LN('sbi.datasource.type.jndi.name'),
			allowBlank: false,
			hidden: true
		});	 
		
		this.dataSourceJdbcUser = Ext.create("Ext.form.field.Text",{
			name: "USER",
			fieldLabel: LN('sbi.datasource.type.jdbc.user'),
			allowBlank: true,
			hidden: true
		});
		
		this.dataSourceJdbcUrl = Ext.create("Ext.form.field.Text",{
			name: "CONNECTION_URL",
			fieldLabel: LN('sbi.datasource.type.jdbc.url'),
			allowBlank: true,
			hidden: true
		});
		
		this.dataSourceJdbcPassword = Ext.create("Ext.form.field.Text",{
			name: "PASSWORD",
			fieldLabel: LN('sbi.datasource.type.jdbc.password'),
			allowBlank: true,
			hidden: true
			
		});
		
		this.dataSourceDriver = Ext.create("Ext.form.field.Text",{
			name: "DIALECT_CLASS",
			fieldLabel: LN('sbi.datasource.driver'),
			allowBlank: false,
			hidden: true
		});	

		this.dataSourceTypeJdbc.on("change", function(field, newValue, oldValue, eOpts){
			if(newValue){
				this.dataSourceJdbcPassword.show();
				this.dataSourceJdbcUrl.show();
				this.dataSourceJdbcUser.show();
				this.dataSourceDriver.show();
			}else{
				this.dataSourceJdbcPassword.hide();
				this.dataSourceJdbcUrl.hide();
				this.dataSourceJdbcUser.hide();
				this.dataSourceDriver.hide();
			}
		},this);
		
		this.dataSourceTypeJndi.on("change", function(field, newValue, oldValue, eOpts){
			if(newValue){
				this.dataSourceJndiName.show();
			}else{
				this.dataSourceJndiName.hide();

			}
		},this);
	}
	

	, setFormState: function(values){
		var v = values;
		if(v.JNDI_URL){
			v.TYPE='jndi';
		}else{
			v.TYPE='jdbc';
		}
		this.getForm().setValues(v);
	}
});
    