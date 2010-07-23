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
Ext.ns("Sbi.kpi");

Sbi.kpi.ManageThresholdValues = function(config) { 


	var paramsGetListNew = {LIGHT_NAVIGATOR_DISABLED: 'TRUE', MESSAGE_DET: 'ATTR_LIST', IS_NEW_ATTR : 'true'};
	this.services = new Array();
	this.services['manageAttributesNew'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'MANAGE_ATTRIBUTES_ACTION'
		, baseParams: paramsGetListNew
	});
	
	var paramsGetList = {LIGHT_NAVIGATOR_DISABLED: 'TRUE', MESSAGE_DET: 'ATTR_LIST'};
	this.services['manageAttributes'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'MANAGE_ATTRIBUTES_ACTION'
		, baseParams: paramsGetList
	});
		
	var paramsDelete = {LIGHT_NAVIGATOR_DISABLED: 'TRUE', MESSAGE_DET: 'ATTR_DELETE'};
	this.services['manageAttributesDelete'] = Sbi.config.serviceRegistry.getServiceUrl({
			serviceName: 'MANAGE_ATTRIBUTES_ACTION'
			, baseParams: paramsDelete
		});

	
	// Let's pretend we rendered our grid-columns with meta-data from our ORM framework.
	this.userColumns =  [
	    {header: 'Position', 
	    	id:'position',
	    	width: 50, 
	    	sortable: true, dataIndex: 'name', editor: 
	    		new Ext.form.TextField({})
	    },
	    {header: 'Label', width: 50, 
				id:'label',
				sortable: true, dataIndex: 'description',  
				editor: new Ext.form.TextField({})
	    },
		{header: 'Min', width: 50, 
				id:'min',
				sortable: true, 
				xtype: 'numbercolumn',
				dataIndex: 'min',  
				editor: new Ext.form.NumberField({
		                allowBlank: false,
		                minValue: 1,
		                maxValue: 150000
		            })				
		},new Ext.form.Checkbox({
			header: 'Max', 
			width: 50, 
			xtype: 'booleancolumn',
			sortable: true, 
			dataIndex: 'max' 
		})
		/*{header: 'Max', width: 50, 
				id:'max',
				xtype: 'booleancolumn',
				sortable: true, dataIndex: 'max',  
				editor:new Ext.form.Checkbox({})
		}*/,new Ext.form.ComboBox({
			header: 'Severity', 
			width: 50, 
			sortable: true, 
			dataIndex: 'severityCd'})
		/*{header: 'Severity', width: 50, 
				id:'severity',
				sortable: true, dataIndex: 'severity',  
				editor: new Ext.form.ComboBox({})
		}*/,new Ext.ux.ColorField({
			header: 'Color', 
			width: 50, 
			sortable: true, 
			dataIndex: 'color',
			value: '#FFFFFF', 
			msgTarget: 'qtip', 
			fallback: true
		})
		/*{header: 'Color', width: 50, 
				id:'color',
				sortable: true, dataIndex: 'color',  
				editor: new Ext.ux.ColorField({ value: '#FFFFFF', msgTarget: 'qtip', fallback: true})
					}*/,
		{header: 'Value', width: 50, 
				id:'val',
				sortable: true, dataIndex: 'val',  
				editor: new Ext.form.NumberField({})
		}
					
	];

	 // use RowEditor for editing
   /* this.editor = new Ext.ux.grid.RowEditor({
        saveText: LN('sbi.attributes.update')
    });*/
    
  ///PROVA
	 var cm = new Ext.grid.ColumnModel({
	        // specify any defaults for each column
	        defaults: {
	            sortable: true // columns are not sortable by default           
	        },
	        columns: this.userColumns
	    });

	 this.store = new Ext.data.JsonStore({
	    	autoLoad: false    	  
	    	, id : 'id'		
	    	, fields: [
        	            'label'
        	          , 'position'
        	          , 'min'
        	          , 'minIncluded'
        	          , 'max'
        	          , 'maxIncluded'
        	          , 'val'
        	          , 'color'
        	          , 'severityCd'
        	          ]
	    	, root: 'rows'
			, url: this.services['manageAttributes']		
		});
	 
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

	    // create the editor grid
	    var grid = new Ext.grid.EditorGridPanel({
	        store: this.store,
	        cm: cm,
	        width: 600,
	        height: 300,
	        autoExpandColumn: 'label', // column with this id will be expanded
	        frame: true,
	        clicksToEdit: 1,
	        tbar: tb
	    });

	
	///PROVA
	    
    this.store.load();
    
    var c = Ext.apply( {}, config, grid);
    


    // constructor
    Sbi.kpi.ManageThresholdValues.superclass.constructor.call(this, c);

}

Ext.extend(Sbi.kpi.ManageThresholdValues, Ext.grid.GridPanel, {
  
  	reader:null
  	,services:null
  	,writer:null
  	,store:null
  	,userColumns:null
  	,editor:null
  	,userGrid:null
	
  	
    ,onAdd: function (btn, ev) {
        var emptyRecToAdd = new Ext.data.Record({
			  itThrVal: 0,
			  label: '',
              position: '',
              min: '',
              minIncluded: '',
              max: '',
              maxIncluded: '',
              val: '',
              color: '',
              severityCd: ''     
			 });   
        this.store.insert(0,emptyRecToAdd);
		/*this.gridForm.getForm().loadRecord(emptyRecToAdd);
	
	    this.tabs.items.each(function(item)
		    {		
		    	item.doLayout();
		    });   
	    this.gridForm.doLayout();*/
    }

    ,onDelete: function() {
        var rec = this.getSelectionModel().getSelected();

        var remove = true;

        this.store.proxy = new Ext.data.HttpProxy({
				url: this.services['manageAttributesDelete']
				, listeners: {
					'exception': function(proxy, type, action, options, response, arg){	    	
        				var content = Ext.util.JSON.decode( response.responseText );
        				if(content == undefined || !content.success){
			                Ext.MessageBox.show({
			                    title: LN('sbi.attributes.error'),
			                    msg: LN('sbi.attributes.error.msg'),
			                    width: 400,
			                    buttons: Ext.MessageBox.OK
			                });

			               remove = false;
        				}else{
			                Ext.MessageBox.show({
			                    title: LN('sbi.attributes.ok'),
			                    msg: LN('sbi.attributes.ok.msg'),
			                    width: 400,
			                    buttons: Ext.MessageBox.OK
			                });
        				}
					}
    				
		        }
		        ,scope: this
			});
        this.store.remove(rec);
        this.store.commitChanges();

        if(!remove){
        	//readd record
            this.store.add(rec);
            this.store.commitChanges();
        }


        this.store.proxy = new Ext.data.HttpProxy({
			url: this.services['manageAttributes']
        });
     }


});

