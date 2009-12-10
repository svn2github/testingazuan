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
 * Authors - Chiara Chiarelli (chiara.chiarelli@eng.it)
 */

Ext.ns("Sbi.execution.toolbar");

Sbi.execution.toolbar.MetadataWindow = function(config) {

	var params = {
		LIGHT_NAVIGATOR_DISABLED : 'TRUE'
	};
	this.services = new Array();

	this.services['getMetadataService'] = Sbi.config.serviceRegistry
			.getServiceUrl( {
				serviceName : 'GET_METADATA_ACTION',
				baseParams : params
			})
			+ '&OBJECT_ID=' + config.OBJECT_ID;

	this.services['saveMetadataService'] = Sbi.config.serviceRegistry
			.getServiceUrl( {
				serviceName : 'SAVE_METADATA_ACTION',
				baseParams : params
			})
			+ '&OBJECT_ID=' + config.OBJECT_ID;

	this.buddy = undefined;

	this.store = this.createGridSourceStore();
	this.createGridPanel();

	this.storeTabs = this.createTabsSourceStore();
	this.createTabsPanel();
	
	var c = Ext.apply( {}, config, {
		id : 'win_metadata',
		items : [ this.shortTextPanel, this.longTextPanel ],
		//items : [ this.shortTextPanel ],
		buttons : [ {
			id : 'save',
			text : LN('sbi.execution.notes.savenotes'),
			scope : this,
			handler : this.saveMetadata,
			disabled : false
		} ],
		layout : 'fit',
		width : 650,
		height : 410,
		plain : true,
		autoScroll : true,
		title : LN('sbi.execution.metadata')
	});

	// constructor
	Sbi.execution.toolbar.MetadataWindow.superclass.constructor.call(this, c);

	if (this.buddy === undefined) {
		this.buddy = new Sbi.commons.ComponentBuddy( {
			buddy : this
		});
	}

};

Ext.extend(Sbi.execution.toolbar.MetadataWindow, Ext.Window, {
	services : null,
	gridStore : null,
	shortTextStore : null,
	longTextStore : null,
	shortTextPanel : null,
	shortTextGrid : null,
	longTextPanel : null,
	longTextTabPanel : null,
	longTextEditor : null

	,
	createGridPanel : function() {

		this.shortTextGrid = new Ext.grid.EditorGridPanel( {
			store : this.store,
			autoHeight : true,
			columns : [ {
				header : "Name",
				width : 100,
				sortable : true,
				dataIndex : 'meta_name'
			}, {
				header : "Value",
				width : 540,
				sortable : true,
				dataIndex : 'meta_content',
				editor : new Ext.form.TextField( {})
			} ],
			viewConfig : {
				forceFit : true,
				scrollOffset : 2
			// the grid will never have scrollbars
			},
			singleSelect : true,
			//selModel : new Ext.grid.RowSelectionModel(),
			clicksToEdit : 2
		});

		this.shortTextPanel = new Ext.Panel( {
			title : 'Short Text Metadata',
			layout : 'fit',
			collapsible : true,
			collapsed : true,
			items : [ this.shortTextGrid ],
			autoWidth : true,
			autoHeight : true
		});


	},
	createTabsPanel : function(){
		var tabs = new Array();
		for(i =0; i<this.storeTabs.getTotalCount(); i++){
			var tab = new Ext.Panel( {
				"title" : this.storeTabs.getAt(i).data.meta_name,
				"html" : this.storeTabs.getAt(i).data.meta_content
				
			});

			tab.on("show", function() {
				this.longTextEditor = new Ext.form.HtmlEditor( {
					frame : true,
					value : tab.body,
					width : '100%',
					disabled : false,
					height : 258,
					enableSourceEdit : false
				});
				tab.add(this.longTextEditor);
			}, this);
			tabs[i] =tab;
		}

		this.longTextTabPanel = new Ext.TabPanel( {
			hideMode : 'offsets',
			resizeTabs : true,
			minTabWidth : 115,
			tabWidth : 135,
			enableTabScroll : true,
			activeTab : 0,
			autoScroll : true,

			items : tabs
		});

		this.longTextPanel = new Ext.Panel( {
			title : 'Long Text Metadata',
			collapsible : true,
			collapsed : false,
			items : [ this.longTextTabPanel ]
		});
	}

	,
	createGridSourceStore : function() {
		var store = new Ext.data.SimpleStore( {
			fields : [ 'meta_name', 'meta_content' ],
			data : [ [ "BE", "Belgium" ], [ "BR", "Brazil" ],
					[ "BG", "Bulgaria" ], [ "CA", "Canada" ],
					[ "CL", "Chile" ], [ "CY", "Cyprus" ],
					[ "CZ", "Czech Republic" ], [ "FI", "Finland" ],
					[ "FR", "France" ], [ "DE", "Germany" ],
					[ "HU", "Hungary" ], [ "IE", "Ireland" ],
					[ "IL", "Israel" ], [ "IT", "Italy" ], [ "LV", "Latvia" ],
					[ "LT", "Lithuania" ], [ "MX", "Mexico" ],
					[ "NL", "Netherlands" ], [ "NZ", "New Zealand" ],
					[ "NO", "Norway" ], [ "PK", "Pakistan" ],
					[ "PL", "Poland" ], [ "RO", "Romania" ],
					[ "SK", "Slovakia" ], [ "SI", "Slovenia" ],
					[ "ES", "Spain" ], [ "SE", "Sweden" ],
					[ "CH", "Switzerland" ], [ "GB", "United Kingdom" ] ]
		}); // end of Ext.data.SimpleStore

		/*
		 * var store = new Ext.data.Store({ {"meta_name":"metadata1",
		 * "meta_content":"aaaaaaaaa", "meta_id":"1", "meta_creation_date":"",
		 * "meta_change_date":""}, {"meta_name":"metadata2",
		 * "meta_content":"bbbbbbbbbb", "meta_id":"2", "meta_creation_date":"",
		 * "meta_change_date":""} });
		 */

		return store;
	},
	createTabsSourceStore : function() {
		var store = new Ext.data.SimpleStore( {
			fields : [ 'meta_name', 'meta_content' ],
			data : [ [ "BE", "html Belgium" ], [ "BR", "html Brazil" ],
					[ "BG", "html Bulgaria" ], [ "CA", "html Canada" ],
					[ "CL", "html Chile" ], [ "CY", "html Cyprus" ] ]
		}); // end of Ext.data.SimpleStore

		return store;
	}

	,
	saveMetadata : function() {
		alert("save");

		var modifiedRecords = this.store.getModifiedRecords();
		// store.commitChanges();
}

});