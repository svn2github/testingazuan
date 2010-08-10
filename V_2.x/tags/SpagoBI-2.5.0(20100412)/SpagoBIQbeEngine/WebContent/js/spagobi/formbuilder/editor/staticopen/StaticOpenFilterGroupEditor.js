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

Ext.ns("Sbi.formbuilder");

Sbi.formbuilder.StaticOpenFilterGroupEditor = function(config) {
		
	var defaultSettings = {
	};
	if (Sbi.settings && Sbi.settings.formbuilder && Sbi.settings.formbuilder.staticOpenFilterGroupEditor) {
		defaultSettings = Ext.apply(defaultSettings, Sbi.settings.formbuilder.staticOpenFilterGroupEditor);
	}
	var c = Ext.apply(defaultSettings, config || {});
	
	this.initToolbar();
	
	Ext.apply(c, {
		tbar: this.toolbar
	});
	
	// constructor
    Sbi.formbuilder.StaticOpenFilterGroupEditor.superclass.constructor.call(this, c);
    
};

Ext.extend(Sbi.formbuilder.StaticOpenFilterGroupEditor, Sbi.formbuilder.EditorPanel, {
	
	wizard: null
	
	//--------------------------------------------------------------------------------
	// public methods
	// --------------------------------------------------------------------------------
	
	, setContents: function(contents) {
		for(var i = 0, l = contents.length; i < l; i++) {
			this.addFilter(contents[i]);
		}
	}
	
	, getContents: function() {
		return this.contents[0].getContents();
	}
	
	, addFilter: function(filterConf) {
		var filter = new Sbi.formbuilder.StaticOpenFilterEditor(filterConf); 
		this.addFilterItem(filter);
	}
	
	, editFilter: function(f) {
		this.onFilterWizardShow(f);
	}
	
	, modifyFilter: function(filterConf) {
		this.clearContents();
		this.addFilter(filterConf);
	}
	
	// --------------------------------------------------------------------------------
	// private methods
	// --------------------------------------------------------------------------------
	

	, initToolbar: function() {
		this.toolbar =  new Ext.Toolbar({
			items: [
			    '->' , {
					text: LN('sbi.formbuilder.staticopenfiltergroupeditor.edit'),
					handler: function() {this.editFilter(this.contents[0]);},
					scope: this
			    } , {
					text: LN('sbi.formbuilder.staticopenfiltergroupeditor.remove'),
					handler: function() {
				    	if(this.ownerCt) {
			    			this.ownerCt.remove(this, true);
			    		} else {
			    			this.destroy();
			    		}
				    },
					scope: this
			    }
			  ]
		});
	}
	
	, onFilterWizardShow: function(targetFilter) {
		var staticOpenFilterWindow = new Sbi.formbuilder.StaticOpenFilterWizard(targetFilter.getContents(), {});
		staticOpenFilterWindow.show();
		staticOpenFilterWindow.on('apply', this.modifyFilter , this);
		
		/*
		if(this.wizard === null) {
			this.wizard = new Sbi.formbuilder.StaticCloseFilterWizard();
			this.wizard.on('apply', function(win, target, state) {
				if(target === null) {
					this.addFilter(state);
				} else {
					target.setContents(state);
				}
				
			}, this);
		}
		
		this.wizard.setTarget(targetFilter || null);		
		this.wizard.show();
		*/
	}
	
});