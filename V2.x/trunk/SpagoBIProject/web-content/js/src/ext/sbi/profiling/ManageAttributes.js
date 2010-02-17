/**
 * SpagoBI - The Business Intelligence Free Platform
 *
 * Copyright (C) 2004 - 2008 Engineering Ingegneria Informatica S.p.A.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 **/

/**
 * Object name
 * 
 * [description]
 * 
 * 
 * Public Properties
 * 
 * [list]
 * 
 * 
 * Public Methods
 * 
 * [list]
 * 
 * 
 * Public Events
 * 
 * [list]
 * 
 * Authors - Chiara Chiarelli
 */
Ext.ns("Sbi.profiling");

Sbi.profiling.ManageAttributes = function(config) { 


	var paramsGetList = {LIGHT_NAVIGATOR_DISABLED: 'TRUE', MESSAGE_DET: 'ATTR_LIST'};
	this.services = new Array();
	this.services['manageAttributes'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'MANAGE_ATTRIBUTES_ACTION'
		, baseParams: paramsGetList
	});
		
	var paramsDelete = {LIGHT_NAVIGATOR_DISABLED: 'TRUE', MESSAGE_DET: 'ATTR_DELETE'};
	this.services['manageAttributesDelete'] = Sbi.config.serviceRegistry.getServiceUrl({
			serviceName: 'MANAGE_ATTRIBUTES_ACTION'
			, baseParams: paramsDelete
		});
	
		// Typical JsonReader.  Notice additional meta-data params for defining the core attributes of your json-response
	this.reader = new Ext.data.JsonReader({
		    totalProperty: 'total',
		    successProperty: 'success',
		    idProperty: 'id',
		    root: 'samples',
		    messageProperty: 'message'  // <-- New "messageProperty" meta-data
		}, [
		    {name: 'id'},
		    {name: 'name', allowBlank: false},
		    {name: 'description', allowBlank: false}
	]);

	
	// The new DataWriter component.
	this.writer = new Ext.data.JsonWriter({
	    encode: false   // <-- don't return encoded JSON -- causes Ext.Ajax#request to send data using jsonData config rather than HTTP params
	});

	// Typical Store collecting the Proxy, Reader and Writer together.
	this.store = new Ext.data.Store({
	    id: 'user',
	    restful: true,     // <-- This Store is RESTful
	    proxy: new Ext.data.HttpProxy({
					url: this.services['manageAttributes'],
					listeners: {
						'exception': function(proxy, type, action, options, response, arg){
			                Ext.MessageBox.show({
			                    title: LN('sbi.attributes.validationError'),
			                    msg: response.responseText,
			                    width: 400,
			                    buttons: Ext.MessageBox.OK
			               });

						}
			        },
			        scope: this
			}),

	    reader: this.reader,
	    writer: this.writer    // <-- plug a DataWriter into the store just as you would a Reader
	});
	

	// load the store immeditately
	this.store.load();

	
	// Let's pretend we rendered our grid-columns with meta-data from our ORM framework.
	this.userColumns =  [
	    {header: LN('sbi.attributes.headerName'), 
	    	id:'name',
	    	width: 250, 
	    	sortable: true, dataIndex: 'name', editor: 
	    		new Ext.form.TextField({
	    			id:'aa',
	    				maxLength:255,
	    				minLength:1,
	    				allowBlank: false,
	    				regex : new RegExp("[A-Za-z0-9_]", "g"),
	    				regexText : 'Richiesta stringa alfanumerica'
	    				})},
	    {header: LN('sbi.attributes.headerDescr'), width: 250, 
				id:'description',
				sortable: true, dataIndex: 'description',  
				editor: new Ext.form.TextField({
					id:'bb',
					maxLength:500,
					minLength:1,
    				regex : new RegExp("[A-Za-z0-9_]", "g"),
    				regexText : 'Richiesta stringa alfanumerica',
					allowBlank: false})}
	];
	/*
	this.colModel = new Ext.grid.ColumnModel({
	    columns: [
	        { header: "Name", width: 60, menuDisabled: false},
	        { header: "Description", width: 150, id: 'company'},
	        { header: "Market Cap."},
	        { header: "$ Sales", renderer: money},
	        { header: "Employees", resizable: false}
	    ],
	    defaults: {
	        sortable: true,
	        menuDisabled: true,
	        width: 100
	    },
	    listeners: {
	        hiddenchange: function(cm, colIndex, hidden) {
	            saveConfig(colIndex, hidden);
	        }
	    }
	});
	*/
	 // use RowEditor for editing
    this.editor = new Ext.ux.grid.RowEditor({
        saveText: LN('sbi.attributes.update')
    });
    
    
    
    this.editor.on({
  			scope: this,
  			afteredit: function() {
 				this.store.commitChanges();
		    },
		    exceptionOnValidate: function(){
		    	this.validationErrors();
		    }
		});

    // Create a typical GridPanel with RowEditor plugin
    
    var tb = new Ext.Toolbar({
    	buttonAlign : 'left',
    	items:[new Ext.Toolbar.Button({
            text: LN('sbi.attributes.add'),
            iconCls: 'icon-add',
            handler: this.onAdd,
            width: 30,
            scope: this
        }), '-', new Ext.Toolbar.Button({
            text: LN('sbi.attributes.delete'),
            iconCls: 'icon-remove',
            handler: this.onDelete,
            width: 30,
            scope: this
        }), '-'
    	]
    });
    
    var c = Ext.apply( {}, config, {
        frame: true,
        title: LN('sbi.attributes.title'),
        autoScroll: true,
        height: 400,
        width: 600,
        store: this.store,
        plugins: [this.editor],
        columns : this.userColumns,
        tbar: tb,
        viewConfig: {
            forceFit: true
        }
        ,renderTo: Ext.getBody()
    });


    // constructor
    Sbi.profiling.ManageAttributes.superclass.constructor.call(this, c);

}

Ext.extend(Sbi.profiling.ManageAttributes, Ext.grid.GridPanel, {
  
  	reader:null
  	,services:null
  	,writer:null
  	,store:null
  	,userColumns:null
  	,editor:null
  	,userGrid:null
	/**
     * onAdd
     */
    ,onAdd: function (btn, ev) {
        var u = new this.store.recordType({
           name: '',
           description : ''
        });
        this.editor.stopEditing();
        this.store.insert(0, u);
        this.editor.startEditing(0);
        this.store.commitChanges();

    }
    /**
     * onDelete
     */
    ,onDelete: function() {
        var rec = this.getSelectionModel().getSelected();
        if (!rec) {
            return false;
        }
        this.store.proxy = new Ext.data.HttpProxy({
					url: this.services['manageAttributesDelete']
			});
        this.store.remove(rec);
        this.store.commitChanges();
        this.store.proxy = new Ext.data.HttpProxy({
			url: this.services['manageAttributes']
        });
     }


});

Ext.reg('manageattr', Sbi.profiling.ManageAttributes);
