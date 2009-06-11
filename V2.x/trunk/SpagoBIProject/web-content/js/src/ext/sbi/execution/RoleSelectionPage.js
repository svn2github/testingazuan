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

Ext.ns("Sbi.execution");

Sbi.execution.RoleSelectionPage = function(config) {
	
	// always declare exploited services first!
	var params = {LIGHT_NAVIGATOR_DISABLED: 'TRUE', SBI_EXECUTION_ID: null};
	this.services = new Array();
	this.services['getRolesForExecutionService'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'GET_ROLES_FOR_EXECUTION_ACTION'
		, baseParams: params
	});
	
	/*
	var Role = Ext.data.Record.create([
	    {name: 'id'}                 
	    , {name: 'name'} 
	    , {name: 'description'} 
	]);
	 */                                  
	
    this.roleComboBoxStore = new Ext.data.Store({
		proxy: new Ext.data.HttpProxy({
				url: this.services['getRolesForExecutionService']
		})
		   
	   	, reader: new Ext.data.JsonReader(/*{   
	   		root: "root",                        
		    id: "name" }, Role*/)
	   	
	    , listeners: {
    		/*
		    datachanged : {
		    	fn: this.handleHierarchySelectorDataChanged
		    	, scope: this
		    }
		    */
	    }      
    });  
    
    this.roleComboBoxStore.on('load', function() {
    	this.fireEvent('onload', this, this.roleComboBoxStore);
    }, this);
    
    
	
    this.roleComboBox = new Ext.form.ComboBox({
		tpl: '<tpl for="."><div ext:qtip="{name}: {description}" class="x-combo-list-item">{name}</div></tpl>',	
	    editable  : false,
	    fieldLabel : LN('sbi.execution.roleselection.fieldlabel'),
	    forceSelection : true,
	    mode : 'local',
	    name : 'typeFilter',
	    store : this.roleComboBoxStore,
	    displayField:'name',
	    valueField:'name',
	    emptyText: LN('sbi.execution.roleselection.emptytext'),
	    typeAhead: true,
	    triggerAction: 'all',
	    //width: 100,
	    selectOnFocus:true,
	    listeners: {
	    	'select': {
	       		fn: function(){}
	       		, scope: this
	    	}
	    }
	});	
    
    
	var fieldset = new Ext.form.FieldSet({
		title: LN('sbi.execution.roleselection.title')
        , collapsible: false
        , autoHeight:true
        , defaultType: 'textfield'
        , items :[this.roleComboBox]
        	
	});
	
	var c = Ext.apply({}, config, {
		bodyStyle:'padding:16px 16px 16px 16px;'
		, listeners: {
		    'render': {
            	fn: function() {
          	 	this.loadingMask = new Sbi.decorator.LoadMask(this.body, {msg:'Loading roles ...'}); 
            	},
            	scope: this
          	}
        }      	
		, items :[this.roleComboBox]
	});   
	
	// constructor
    Sbi.execution.RoleSelectionPage.superclass.constructor.call(this, c);
    
    this.addEvents("onload");
};

Ext.extend(Sbi.execution.RoleSelectionPage, Ext.FormPanel, {
	
	services: null
	, roleComboBoxStore: null
	, roleComboBox: null
	, loadingMask: null
	   
    // public methods
	
	, loadRolesForExecution: function( executionInstance ) {
		this.roleComboBoxStore.load({params: executionInstance});
	}

	, getSelectedRole: function() {
		return this.roleComboBox.getValue();
	}
});