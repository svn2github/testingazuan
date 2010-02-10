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


	var params = {LIGHT_NAVIGATOR_DISABLED: 'TRUE', MESSAGE_DET: 'ATTR_LIST'};
	this.services = new Array();
	this.services['manageAttributes'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'MANAGE_ATTRIBUTES_ACTION'
		, baseParams: params
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
	    //data:[{'id':3,'email':'ciao','first':'cc','last':'dd'}],
	    proxy: new Ext.data.HttpProxy({
					url: this.services['manageAttributes']
			}),
	    reader: this.reader,
	    writer: this.writer    // <-- plug a DataWriter into the store just as you would a Reader
	});
	

	// load the store immeditately
	this.store.load();

	
	// Let's pretend we rendered our grid-columns with meta-data from our ORM framework.
	this.userColumns =  [
	    //{header: "ID", width: 40, sortable: true, dataIndex: 'id'},
	    {header: "Name", width: 100, sortable: true, dataIndex: 'name', editor: new Ext.form.TextField({})},
	    {header: "Description", width: 100, sortable: true, dataIndex: 'description', editor: new Ext.form.TextField({})},
	];

	
	 // use RowEditor for editing
    this.editor = new Ext.ux.grid.RowEditor({
        saveText: 'Update'
    });

    // Create a typical GridPanel with RowEditor plugin
    
    var tb = new Ext.Toolbar({
    	buttonAlign : 'left',
    	items:[new Ext.Toolbar.Button({
            text: 'Add',
            iconCls: 'icon-add',
            handler: this.onAdd,
            width: 30,
            scope: this
        }), '-', new Ext.Toolbar.Button({
            text: 'Delete',
            iconCls: 'icon-remove',
            handler: this.onDelete,
            width: 30,
            scope: this
        }), '-'
    	]
    });
    
    var c = Ext.apply( {}, config, {
        frame: true,
        title: 'Profile Attributes',
        autoScroll: true,
        height: 300,
        width: 500,
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
        var params = {LIGHT_NAVIGATOR_DISABLED: 'TRUE', MESSAGE_DET: 'ATTR_INSERT'};
		this.services['manageAttributes'] = Sbi.config.serviceRegistry.getServiceUrl({
			serviceName: 'MANAGE_ATTRIBUTES_ACTION'
			, baseParams: params
		});
        this.editor.stopEditing();
        this.store.insert(0, u);
        this.editor.startEditing(0);
    }
    /**
     * onDelete
     */
    ,onDelete: function() {
        var rec = this.getSelectionModel().getSelected();
        if (!rec) {
            return false;
        }
        this.store.remove(rec);
    }


});

Ext.reg('manageattr', Sbi.profiling.ManageAttributes);
