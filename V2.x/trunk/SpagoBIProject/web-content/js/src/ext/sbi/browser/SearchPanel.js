/**
 * SpagoBI - The Business Intelligence Free Platform
 *
 * Copyright (C) 2004 - 2009 Engineering Ingegneria Informatica S.p.A.
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
 * 
 * @author Andrea Gioia (andrea.gioia@eng.it)
 */

Ext.ns("Sbi.browser");

Sbi.browser.SearchPanel = function(config) { 
	
	
	
	this.searchField =  new Sbi.browser.SearchField({
    	hideLabel: true 
    	, width:220
    });
	
	this.searchField.addListener('onsearch', function(field, query) {
		
		alert(query);
		
		var searchContentService = Sbi.config.serviceRegistry.getServiceUrl('SEARCH_CONTENT_ACTION');
		searchContentService += '&LIGHT_NAVIGATOR_DISABLED=TRUE';
    	
		var p = {
				'valueFilter'		: query
				, 'scope'			: 'tree'	
				, 'folderId'		: 1
				, 'typeFilter'		: 'CONTAINS'
				, 'columnFilter'	: 'NAME'
			}
		
		Ext.Ajax.request({
	        url: searchContentService,
	        params: p,
	        callback : function(options , success, response){
	  	  	if(success && response !== undefined) {   
		      		if(response.responseText !== undefined) {
		      			var content = Ext.util.JSON.decode( response.responseText );
		      			if(content !== undefined) {
		      				alert(response.responseText);
		      			} 
		      		} else {
		      			Sbi.exception.ExceptionHandler.showErrorMessage('Server response is empty', 'Service Error');
		      		}
	  	  	}
	        },
	        scope: this,
			failure: Sbi.exception.ExceptionHandler.handleFailure      
	     });
	}, this);
	this.searchField.addListener('onreset', function(){alert('X')}, this);
	
	this.scopeCheckbox =  new Ext.form.Checkbox({
    	boxLabel: 'search only in current folder'
    	,hideLabel: true 
    	, width:220
    });	
	
	var filterTypes = [
	  ['CONTAINS', LN('sbi.browser.searchpanel.filters.contains.name'), LN('sbi.browser.searchpanel.filters.contains.description')]
	  , ['START_WITH', LN('sbi.browser.searchpanel.filters.startwith.name'), LN('sbi.browser.searchpanel.filters.contains.description')]
	  , ['END_WITH', LN('sbi.browser.searchpanel.filters.endwith.name'), LN('sbi.browser.searchpanel.filters.contains.description')]
	  , ['EQUALS_TO', LN('sbi.browser.searchpanel.filters.equalsto.name'), LN('sbi.browser.searchpanel.filters.contains.description')]
	  , ['GREATER_THAN', LN('sbi.browser.searchpanel.filters.gt.name'), LN('sbi.browser.searchpanel.filters.contains.description')]
	  , ['EQUALS_OR_GREATER_THAN', LN('sbi.browser.searchpanel.filters.egt.name'), LN('sbi.browser.searchpanel.filters.contains.description')]
	  , ['LESS_THAN', LN('sbi.browser.searchpanel.filters.lt.name'), LN('sbi.browser.searchpanel.filters.contains.description')]
	  , ['EQUALS_OR_LESS_THAN', LN('sbi.browser.searchpanel.filters.elt.name'), LN('sbi.browser.searchpanel.filters.contains.description')]
    ];
           
	var filterTypesComboBoxStore = new Ext.data.SimpleStore({
		   fields: ['value', 'field', 'description']
		   , data : filterTypes
	});
	
	this.filterTypesComboBox = new Ext.form.ComboBox({
		   tpl: '<tpl for="."><div ext:qtip="{field}: {description}" class="x-combo-list-item">{field}</div></tpl>',	
		   editable  : false,
		   fieldLabel : 'Function',
		   forceSelection : true,
		   mode : 'local',
		   name : 'typeFilter',
		   store : filterTypesComboBoxStore,
		   displayField:'field',
		   valueField:'value',
		   emptyText:'Select a function...',
		   typeAhead: true,
		   triggerAction: 'all',
		   width: 100,
		   selectOnFocus:true,
		   listeners: {
		   	'select': {
	          fn: function(){}
	          , scope: this
	        }
	       }
	});	
	
	var columnNames = [
	   ['LABEL', LN('sbi.browser.searchpanel.columns.label.name'), LN('sbi.browser.searchpanel.columns.label.description')]
	   , ['NAME', LN('sbi.browser.searchpanel.columns.name.name'), LN('sbi.browser.searchpanel.columns.label.description')]
	   , ['ENGINE', LN('sbi.browser.searchpanel.columns.engine.name'), LN('sbi.browser.searchpanel.columns.label.description')]
	   , ['STATE', LN('sbi.browser.searchpanel.columns.state.name'), LN('sbi.browser.searchpanel.columns.label.description')]
	   , ['CREATION_DATE', LN('sbi.browser.searchpanel.columns.creationdate.name'), LN('sbi.browser.searchpanel.columns.label.description')]
	];
	                        
	var columnNamesComboBoxStore = new Ext.data.SimpleStore({
		fields: ['value', 'field', 'description']
	    , data : columnNames
	});
	             	
	this.columnNamesComboBox = new Ext.form.ComboBox({
		tpl: '<tpl for="."><div ext:qtip="{field}: {description}" class="x-combo-list-item">{field}</div></tpl>',	
	    editable  : false,
	    fieldLabel : 'Attribute',
	    forceSelection : true,
	    mode : 'local',
	    name : 'columnFilter',
	    store : columnNamesComboBoxStore,
	    displayField:'field',
	    valueField:'value',
	    emptyText:'Select an attribute...',
	    typeAhead: true,
	    triggerAction: 'all',
	    width: 100,
	    selectOnFocus:true,
	    listeners: {
	    	'select': {
	           fn: function(){}
	           , scope: this
	         }
	    }
	});	
	
	
    
	var c = Ext.apply({}, config, {
		title:'Search',
        border:true,
		bodyStyle:'padding:6px 6px 6px 6px; background-color:#FFFFFF',
    	items: [
    	   {
	            xtype:'fieldset',
	            title: 'Query',
	            collapsible: false,
	            autoHeight:true,
	            //defaults: {width: 10},
	            defaultType: 'textfield',
	            items :[this.searchField, this.scopeCheckbox]
	           }
    	    , {
	            xtype:'fieldset',
	            title: 'Advanced options',
	            collapsible: true,
	            autoHeight:true,
	            //defaults: {width: 10},
	            defaultType: 'textfield',
	            items :[this.filterTypesComboBox, this.columnNamesComboBox]
	           }
    	    ]
	});   
	
	Sbi.browser.SearchPanel.superclass.constructor.call(this, c);   
};

Ext.extend(Sbi.browser.SearchPanel, Ext.FormPanel, {
    
	searchField: null
    
});

Ext.reg('searchpanel', Sbi.browser.SearchPanel);