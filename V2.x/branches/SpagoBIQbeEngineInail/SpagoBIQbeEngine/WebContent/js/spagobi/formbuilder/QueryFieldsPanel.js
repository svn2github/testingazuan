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
  *  [list]
  * 
  * 
  * Public Events
  * 
  *  [list]
  * 
  * Authors
  * 
  * - Andrea Gioia (andrea.gioia@eng.it)
  */

Ext.ns("Sbi.formbuilder");

Sbi.formbuilder.QueryFieldsPanel = function(config) {
	
	var defaultSettings = {
		title: LN('Selected Fields')
	};
		
	if(Sbi.settings && Sbi.settings.formbuilder && Sbi.settings.formbuilder.queryFieldsPanel) {
		defaultSettings = Ext.apply(defaultSettings, Sbi.settings.formbuilder.queryFieldsPanel);
	}
		
	var c = Ext.apply(defaultSettings, config || {});
		
	Ext.apply(this, c);
		
	
	this.services = this.services || new Array();	
	this.services['getQueryFields'] = this.services['getQueryFields'] || Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'GET_QUERY_FIELDS_ACTION'
		, baseParams: new Object()
	});
	
	//.addEvents('customEvents');
	
		
		
	this.initGrid();

	
	c = Ext.apply(c, {
		title: this.title,
		border: true,
		//bodyStyle:'background:green',
		bodyStyle:'padding:3px',
      	layout: 'fit',   
      	items: [this.grid]
	});

	// constructor
	Sbi.formbuilder.QueryFieldsPanel.superclass.constructor.call(this, c);
};

Ext.extend(Sbi.formbuilder.QueryFieldsPanel, Ext.Panel, {
    
    services: null
    , grid: null
    , store: null
    , myData: [
       ['3m Co',71.72,0.02,0.03,'9/1 12:00am'],
       ['Alcoa Inc',29.01,0.42,1.47,'9/1 12:00am'],
       ['Altria Group Inc',83.81,0.28,0.34,'9/1 12:00am'],
       ['American Express Company',52.55,0.01,0.02,'9/1 12:00am'],
       ['American International Group, Inc.',64.13,0.31,0.49,'9/1 12:00am'],
       ['AT&T Inc.',31.61,-0.48,-1.54,'9/1 12:00am'],
       ['Boeing Co.',75.43,0.53,0.71,'9/1 12:00am'],
       ['Caterpillar Inc.',67.27,0.92,1.39,'9/1 12:00am'],
       ['Citigroup, Inc.',49.37,0.02,0.04,'9/1 12:00am'],
       ['E.I. du Pont de Nemours and Company',40.48,0.51,1.28,'9/1 12:00am'],
       ['Exxon Mobil Corp',68.1,-0.43,-0.64,'9/1 12:00am'],
       ['General Electric Company',34.14,-0.08,-0.23,'9/1 12:00am'],
       ['General Motors Corporation',30.27,1.09,3.74,'9/1 12:00am'],
       ['Hewlett-Packard Co.',36.53,-0.03,-0.08,'9/1 12:00am']
    ]
   
   
    // public methods
    
    , refresh: function() {
		this.store.load();
	}
    
    // private
    
    , initGrid: function(c) {
		/*
		this.store = new Ext.data.SimpleStore({
			fields: [
			   {name: 'company'},
			   {name: 'price', type: 'float'},
			   {name: 'change', type: 'float'},
			   {name: 'pctChange', type: 'float'},
		       {name: 'lastChange', type: 'date', dateFormat: 'n/j h:ia'}
			],
			data: this.myData
		});
		*/
	
		this.store = new Ext.data.JsonStore({
			root: 'results'
			, fields: ['id', 'alias']
			, url: this.services['getQueryFields']
		}); 
		
		this.store.on('loadexception', function(store, options, response, e){
			Sbi.exception.ExceptionHandler.handleFailure(response, options);
		}, this);
		
		this.grid = new Ext.grid.GridPanel({
	        store: this.store,
	        columns: [
	            {id:'alias',header: 'Field Name', width: 160, sortable: true, dataIndex: 'alias'}
	        ],
	        stripeRows: false,
	        autoExpandColumn: 'alias',
	        enableDragDrop: true,
	        ddGroup: 'formbuilderDDGroup'
	    });
    }
    
   

	
	
    
});