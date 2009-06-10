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

Sbi.execution.ExecutionPanel = function(config) {
	
	var title = config.title || 'doc(?)';
	var closable = config.closable !== undefined || true;
	if(config.title !== undefined) delete config.title;
	if(config.closable !== undefined) delete config.closable;
	
	this.execWizard = new Sbi.execution.ExecutionWizard( config );
	
	this.execWizard.documentExecutionPage.on('crossnavigation', function(config){
		this.remove(this.execWizard);
		this.execWizard = new Sbi.execution.ExecutionWizard( {preferences: config.preferences} );
		this.add(this.execWizard);
		this.doLayout();
		this.execWizard.execute(config.document);
	}, this);
	
	var c = Ext.apply({}, config || {}, {
		title: title
		, closable: closable
		, border: false
		, layout: 'fit'
		, items: [this.execWizard]
	});
	
	// constructor
    Sbi.execution.ExecutionPanel.superclass.constructor.call(this, c);
    
    
    
    
    //this.addEvents();	
};

Ext.extend(Sbi.execution.ExecutionPanel, Ext.Panel, {
	execWizard: null	
	
	, execute : function( doc ) {
	this.execWizard.execute( doc );
	}
});
