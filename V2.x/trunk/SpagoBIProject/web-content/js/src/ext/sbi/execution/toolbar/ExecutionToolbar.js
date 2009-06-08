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
  * - Davide Zerbetto (davide.zerbetto@eng.it)
  */

Ext.ns("Sbi.execution.toolbar");

Sbi.execution.toolbar.ExecutionToolbar = function(config) {
	
	this.buttons = this.createRoleSelectionButtons();
	
	var c = Ext.apply({}, config, {
		items: ['->', this.buttons]
	}); 
    
	// constructor
    Sbi.execution.toolbar.ExecutionToolbar.superclass.constructor.call(this, c);
    
    this.addEvents('rolesformsubmit');
    this.addEvents('parametersformsubmit');
    this.addEvents('saveviewpointbuttonclick');
    this.addEvents('clearparametersbuttonclick');
    this.addEvents('refreshbuttonclick');
    this.addEvents('sendmailbuttonclick');
    this.addEvents('saveintopersonalfolderbuttonclick');
    this.addEvents('saveremembermebuttonclick');
    this.addEvents('notesbuttonclick');
    this.addEvents('metadatabuttonclick');
    this.addEvents('ratingbuttonclick');
    this.addEvents('printbuttonclick');
    
};

Ext.extend(Sbi.execution.toolbar.ExecutionToolbar, Ext.Toolbar, {
	
    reset: function() {
    	this.items.each(function(item) {
            this.items.remove(item);
            item.destroy();           
        }, this.items); 
    }

	, update: function(pageNumber, executionInstance) {
		this.reset();
		this.addFill();
		if (pageNumber == 0) {
			this.addButton(this.createRoleSelectionButtons());
		} else if (pageNumber == 1) {
			this.addButton(this.createParametersSelectionButtons());
		}  else if (pageNumber == 2) {
			this.addButton(this.createExecutionPageButtons());
		}
	}
	
	, createRoleSelectionButtons: function() {
		var nextButton =  new Ext.Toolbar.Button({
			iconCls: 'icon-execute'
		    , scope: this
		    , handler : function() {this.fireEvent('rolesformsubmit');}
		});
		return nextButton;
	}
	
	, createParametersSelectionButtons: function() {
		var executeButton = new Ext.Toolbar.Button({
			iconCls: 'icon-execute'
		    , scope: this
		    , handler : function() {this.fireEvent('parametersformsubmit');}
		});
		var saveViewPointButton = new Ext.Toolbar.Button({
			iconCls: 'icon-save'
	     	, scope: this
	    	, handler : function() {this.fireEvent('saveviewpointbuttonclick');}
		});
		var clearParametersButton = new Ext.Toolbar.Button({
			iconCls: 'icon-clear'
	     	, scope: this
	    	, handler : function() {this.fireEvent('clearparametersbuttonclick');}
		});
		return [executeButton, saveViewPointButton, clearParametersButton];
	}
	
	, createExecutionPageButtons: function() {
		var refreshButton = new Ext.Toolbar.Button({
			iconCls: 'icon-refresh' 
	     	, scope: this
	    	, handler : function() {this.fireEvent('refreshbuttonclick');}
		});
		var sendMailButton = new Ext.Toolbar.Button({
			iconCls: 'icon-sendMail' 
	     	, scope: this
	    	, handler : function() {this.fireEvent('sendmailbuttonclick');}
		});
		var saveIntoPersonalFolderButton = new Ext.Toolbar.Button({
			iconCls: 'icon-saveIntoPersonalFolder' 
	     	, scope: this
	    	, handler : function() {this.fireEvent('saveintopersonalfolderbuttonclick');}
		});
		var saveRememberMeButton = new Ext.Toolbar.Button({
			iconCls: 'icon-saveRememberMe' 
	     	, scope: this
	    	, handler : function() {this.fireEvent('saveremembermebuttonclick');}
		});
		var notesButton = new Ext.Toolbar.Button({
			iconCls: 'icon-notes' 
	     	, scope: this
	    	, handler : function() {this.fireEvent('notesbuttonclick');}
		});
		var metadataButton = new Ext.Toolbar.Button({
			iconCls: 'icon-metadata' 
	     	, scope: this
	    	, handler : function() {this.fireEvent('metadatabuttonclick');}
		});
		var ratingButton = new Ext.Toolbar.Button({
			iconCls: 'icon-rating' 
	     	, scope: this
	    	, handler : function() {this.fireEvent('ratingbuttonclick');}
		});
		var printButton = new Ext.Toolbar.Button({
			iconCls: 'icon-print' 
	     	, scope: this
	    	, handler : function() {this.fireEvent('printbuttonclick');}
		});
		return [refreshButton, sendMailButton, saveIntoPersonalFolderButton, saveRememberMeButton, 
		        notesButton, metadataButton, ratingButton, printButton];
	}
    
});