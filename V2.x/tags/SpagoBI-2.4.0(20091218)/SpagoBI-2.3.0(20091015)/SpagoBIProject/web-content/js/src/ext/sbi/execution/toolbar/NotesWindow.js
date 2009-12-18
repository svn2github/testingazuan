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

Sbi.execution.toolbar.NotesWindow = function(config) {
	
	// always declare exploited services first!
	var params = {LIGHT_NAVIGATOR_DISABLED: 'TRUE', SBI_EXECUTION_ID: null};
	this.services = new Array();
	this.services['getNotesService'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'GET_NOTES_ACTION'
		, baseParams: params
	});
	this.services['saveNotesService'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'SAVE_NOTES_ACTION'
		, baseParams: params
	});
	
	this.previousNotes = undefined;
	this.SBI_EXECUTION_ID = config.SBI_EXECUTION_ID;
	
	this.buddy = undefined;
	
    this.editor = new Ext.form.HtmlEditor({
        frame: true,
        value: '',
        bodyStyle:'padding:5px 5px 0',
        width:'100%',
        disabled: true,
	    height: 265,
        id:'notes'        
    });   
	
	var c = Ext.apply({}, config, {
		title: LN('sbi.execution.notes.insertNotes'),
		width:700,
		height:300,
		items: [this.editor],
		buttons: [
		          {
		        	  text: LN('sbi.execution.notes.savenotes'), 
		        	  scope: this,
		        	  handler: this.saveNotes
		          }
		]
	});   
	
	this.loadNotes();
	
	// constructor
    Sbi.execution.toolbar.NotesWindow.superclass.constructor.call(this, c);
    
    if (this.buddy === undefined) {
    	this.buddy = new Sbi.commons.ComponentBuddy({
    		buddy : this
    	});
    }
    
};

Ext.extend(Sbi.execution.toolbar.NotesWindow, Ext.Window, {
	
	loadNotes: function () {
		Ext.Ajax.request({
	        url: this.services['getNotesService'],
	        params: {SBI_EXECUTION_ID: this.SBI_EXECUTION_ID},
	        callback : function(options , success, response) {
	  	  		if (success) {
		      		if(response !== undefined && response.responseText !== undefined) {
		      			var content = Ext.util.JSON.decode( response.responseText );
		      			if (content !== undefined) {
		      				this.previousNotes = content.notes;
		      				this.editor.setValue(Ext.util.Format.htmlDecode(content.notes));
		      				this.editor.enable();
		      			} 
		      		} else {
		      			Sbi.exception.ExceptionHandler.showErrorMessage('Server response is empty', 'Service Error');
		      		}
	  	  		} else { 
	  	  			Sbi.exception.ExceptionHandler.showErrorMessage('Cannot load notes', 'Service Error');
	  	  		}
	        },
	        scope: this,
			failure: Sbi.exception.ExceptionHandler.handleFailure      
		});
	}

	, saveNotes: function () {
		Ext.Ajax.request({
	        url: this.services['saveNotesService'],
	        params: {'SBI_EXECUTION_ID': this.SBI_EXECUTION_ID, 
						'PREVIOUS_NOTES': this.previousNotes, 'NOTES': this.editor.getValue()},
	        callback : function(options , success, response) {
	  	  		if (success) {
		      		if(response !== undefined && response.responseText !== undefined) {
		      			var content = Ext.util.JSON.decode( response.responseText );
		      			if (content !== undefined) {
		      				if (content.result === 'conflict') {
		      					Sbi.exception.ExceptionHandler.showErrorMessage(LN('sbi.execution.notes.notesConflict'), 'Service Error');
		      				} else {
				      			Ext.MessageBox.show({
				      				title: 'Status',
				      				msg: LN('sbi.execution.notes.notedSaved'),
				      				modal: false,
				      				buttons: Ext.MessageBox.OK,
				      				width:300,
				      				icon: Ext.MessageBox.INFO,
				      				animEl: 'root-menu'        			
				      			});
				      			this.previousNotes = this.editor.getValue();
		      				}
		      			}
		      		} else {
		      			Sbi.exception.ExceptionHandler.showErrorMessage('Server response is empty', 'Service Error');
		      		}
	  	  		} else { 
	  	  			Sbi.exception.ExceptionHandler.showErrorMessage('Cannot load notes', 'Service Error');
	  	  		}
	        },
	        scope: this,
			failure: Sbi.exception.ExceptionHandler.handleFailure      
		});
		
	}
	
});