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

Sbi.execution.ExecutionWizardPanel = function(config) {
	
	// always declare exploited services first!
	var params = {LIGHT_NAVIGATOR_DISABLED: 'TRUE', SBI_EXECUTION_ID: null};
	this.services = new Array();
	this.services['startExecutionService'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'START_EXECUTION_PROCESS_ACTION'
		, baseParams: params
	});
	
	this.services['getParametersForExecutionService'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'GET_PARAMETERS_FOR_EXECUTION_ACTION'
		, baseParams: params
	});
	
	this.services['showSendToForm'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'SHOW_SEND_TO_FORM'
		, baseParams: params
	});
	
	this.services['saveIntoPersonalFolder'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'SAVE_PERSONAL_FOLDER'
		, baseParams: params
	});
	
	this.subObjectsPanel = new Sbi.execution.SubObjectsPanel();
	this.roleSelectionPanel = new Sbi.execution.RoleSelectionPanel();
	this.parametersSelectionPanel =  new Sbi.execution.ParametersSelectionPanel();
	this.documentViewPanel = new Sbi.execution.DocumentViewPanel();
	
	this.activePanel = 0;
	
	this.ttBarButtons = new Array();
	
	this.ttBarButtons['previous'] =  new Ext.Toolbar.Button({
		id: 'card-prev'
		, text: '&laquo; Previous'
		, listeners: {
			'click': {
	        	fn: this.moveToPreviousPage,
	          	scope: this
	        } 
		}
	});
	
	this.ttBarButtons['next'] =  new Ext.Toolbar.Button({
		id: 'card-next'
		, text: 'Next &raquo;'
			// permetto ad altri oggetti di mettersi in ascolto di certi eventi
		, listeners: {
			'click': {
			// verrà eseguita la funzione moveToNextPage all'interno del contesto definito da scope
	        	fn: this.moveToNextPage,
	          	scope: this
	        } 
		}
	});
	
	// potevo anche fare: this.ttBarButtons.on('click', funzione, contesto), il contrario è un
	// oppure this.ttBarButtons.addListener('click', funzione, contesto), il contrario è removeListener
	
	
	// '->' indica che i bottoni devono essere allineati destra senza separatore: sono dei caratteri speciali
    this.tb = new Ext.Toolbar({
    	hideMode: "offsets",
        items: ['->', this.ttBarButtons['previous'], this.ttBarButtons['next']]
    });
	
	var c = Ext.apply({}, config, {
		layout:'card',
		activeItem: this.activePanel, // index or id
		tbar: this.tb,
		items: [
		 this.roleSelectionPanel
		 , this.parametersSelectionPanel
		 , this.documentViewPanel
		]		        
	});   
	
	// constructor
    Sbi.execution.ExecutionWizardPanel.superclass.constructor.call(this, c);
    
    this.roleSelectionPanel.addListener('onload', this.onRolesForExecutionLoaded, this);
    
    this.documentViewPanel.addListener('loadurlfailure', this.onLoadUrlFailure, this);
    this.documentViewPanel.addListener('sendMailButtonClicked', this.onSendMailButtonClicked, this);
    this.documentViewPanel.addListener('saveIntoPersonalFolderButtonClicked', this.onSaveIntoPersonalFolderButtonClicked, this);
    this.documentViewPanel.addListener('saveRememberMeButtonClicked', this.onSaveRememberMeButtonClicked, this);
    this.documentViewPanel.addListener('notesButtonClicked', this.onNotesButtonClicked, this);
    this.documentViewPanel.addListener('metadataButtonClicked', this.onMetadataButtonClicked, this);
    this.documentViewPanel.addListener('ratingButtonClicked', this.onRatingButtonClicked, this);
    
    this.subObjectsPanel.addListener('onselected', this.onSubObjectSelected, this);
    
    if(config.document) {
    	this.execute( config.document );
    }
    
    
    //this.addEvents();	
};

Ext.extend(Sbi.execution.ExecutionWizardPanel, Ext.Panel, {
    
	services: null
    , executionInstance: null
    , activePanel: null
    , roleSelectionPanel: null
    , parametersSelectionPanel: null
    , documentViewPanel: null   
    , subObjectsPanel: null
   
    // public methods
    
    // toolbar
    , moveToPage: function(pageNumber) {
		if(this.activePanel == 0 && pageNumber == 1) { // role to params selection 
			this.startExecution();
		}
		if(this.activePanel == 1 && pageNumber == 2) { // from parameters to document view 			
			this.loadUrlForExecution();
			//this.tbar.hide(); // lascia uno spazio bianco al posto della toolbars
			//this.getTopToolbar().destroy(); // fa comparire sotto il document browser, l'iframe non viene ridimensionato
			//this.getTopToolbar().disable(); // funziona però non la toglie dalla ui
			//this.remove(this.getTopToolbar()); // come this.getTopToolbar().destroy()
			//this.getTopToolbar().hide();
			//this.doLayout();
		}
		if(this.activePanel == 2 && pageNumber == 1) {
			delete this.executionInstance.SBI_SUBOBJECT_ID;
		}
			
		this.activePanel = pageNumber;
		this.getLayout().setActiveItem( this.activePanel );
	}
    
    , moveToPreviousPage: function() {
    	this.moveToPage( this.activePanel-1 );
	}
    
    , moveToNextPage: function() {
    	this.moveToPage( this.activePanel+1 );
	}
    
    // execution
    , execute : function( doc ) {
		if(!doc || !doc.id || !doc.label) {
			Sbi.exception.ExceptionHandler.showErrorMessage('ExecutionWizardPanel: document id is required in order to execute a document', 'Intenal Error');
		}
		
		this.executionInstance = {}
		if(doc.id) this.executionInstance.OBJECT_ID = doc.id;
		if(doc.label) this.executionInstance.OBJECT_LABEL = doc.label;
		
		this.loadRolesForExecution();
	}

	, loadRolesForExecution: function() {
		this.roleSelectionPanel.loadRolesForExecution( this.executionInstance );
	}
	
	, loadUrlForExecution: function() {
		var formState = this.parametersSelectionPanel.getFormState();
		var str = '{';
		for (p in formState) {
			var obj = formState[p];
			if (typeof obj == 'object') {
				str += p + ': ['
				for (count in obj) {
					var temp = obj[count];
					if (typeof temp == 'function') {
						continue;
					}
					if (typeof temp == 'string') {
						// the String.escape function escapes the passed string for ' and \
						temp = String.escape(temp);
						str += '\'' + temp + '\', ';
					} else if (typeof temp == 'date') {
						temp = Ext.util.Format.date(temp, Sbi.config.clientServerDateFormat);
						str += p + ': \'' +  temp + '\', ';
					} else {
						str += temp + ', ';
					}
				}
				// removing last ', ' string
				if (str.length > 1 && str.substring(str.length - 3, str.length - 1) == ', ') {
					str = str.substring(0, str.length - 3);
				}
				str += '], ';
			} else if (typeof obj == 'string') {
				// the String.escape function escapes the passed string for ' and \
				obj = String.escape(obj);
				str += p + ': \'' +  obj + '\', ';
			} else if (typeof obj == 'date') {
				obj = Ext.util.Format.date(obj, Sbi.config.clientServerDateFormat);
				str += p + ': \'' +  obj + '\', ';
			} else {
				// case number or boolean
				str += p + ': ' +  obj + ', ';
			}
		}
		if (str.length > 1 && str.substring(str.length - 3, str.length - 1) == ', ') {
			str = str.substring(0, str.length - 3);
		}
		str += '}';
		this.executionInstance.PARAMETERS = str;
		this.documentViewPanel.loadUrlForExecution( this.executionInstance );
	}

	, onRolesForExecutionLoaded: function(ds) {
		//alert('roles for execution loaded');
	}
	
	, startExecution: function() {
		var role = this.roleSelectionPanel.getSelectedRole();
		this.executionInstance.ROLE = role;
		
		Ext.Ajax.request({
	          url: this.services['startExecutionService'],
	          params: this.executionInstance,
	          callback : function(options , success, response){
	    	  	if(success && response !== undefined) {   
		      		if(response.responseText !== undefined) {
		      			var content = Ext.util.JSON.decode( response.responseText );
		      			if(content !== undefined) {
		      				this.onExecutionStarted(content.execContextId);
		      				
		      			} 
		      		} else {
		      			Sbi.exception.ExceptionHandler.showErrorMessage('Server response is empty', 'Service Error');
		      		}
	    	  	}
	          },
	          scope: this,
	  		  failure: Sbi.exception.ExceptionHandler.handleFailure      
	     });
	}
	
	, onExecutionStarted: function( execContextId ) {
		//alert(execContextId);
		this.executionInstance.SBI_EXECUTION_ID = execContextId;
		this.parametersSelectionPanel.loadParametersForExecution(this.executionInstance);
		this.loadSubObjects();
		this.parametersSelectionPanel.add(this.subObjectsPanel);
		this.parametersSelectionPanel.doLayout();
	}
	
	, onLoadUrlFailure: function ( errors ) {
		this.moveToPage(1); // go to parameters page
		
	}
	
	, loadSubObjects: function() {
		this.subObjectsPanel.loadSubObjects( this.executionInstance );
	}
	
	, onSubObjectSelected: function (subObjectId) {
		this.executionInstance.SBI_SUBOBJECT_ID = subObjectId;
		this.moveToPage(2); // go to execution page
	}
	
	, onSendMailButtonClicked: function () {
		var sendToIframeUrl = this.services['showSendToForm'] 
		        + '&objlabel=' + this.executionInstance.OBJECT_LABEL
		        + '&objid=' + this.executionInstance.OBJECT_ID
				+ '&' + this.parametersSelectionPanel.getFormStateAsStringOldSyntax();
		this.win_sendTo = new Sbi.execution.SendToWindow({'url': sendToIframeUrl});
		this.win_sendTo.show();
	}
	
	, onSaveIntoPersonalFolderButtonClicked: function () {
		Ext.Ajax.request({
	          url: this.services['saveIntoPersonalFolder'],
	          params: {documentId: this.executionInstance.OBJECT_ID},
	          callback : function(options , success, response){
	    	  	if (success && response !== undefined) {   
		      		if(response.responseText !== undefined) {
		      			var responseText = response.responseText;
		      			var iconSaveToPF;
		      			var message;
		      			if (responseText=="sbi.execution.stpf.ok") {
		      				message = LN('sbi.execution.stpf.ok');
		      				iconSaveToPF = Ext.MessageBox.INFO;
		      			}
		      			if (responseText=="sbi.execution.stpf.alreadyPresent") {
		      				message = LN('sbi.execution.stpf.alreadyPresent');
		      				iconSaveToPF = Ext.MessageBox.WARNING;
		      			}
		      			if (responseText=="sbi.execution.stpf.error") {
		      				message = LN('sbi.execution.stpf.error');
		      				iconSaveToPF = Ext.MessageBox.ERROR;
		      			}

		      			var messageBox = Ext.MessageBox.show({
		      				title: 'Status',
		      				msg: message,
		      				modal: false,
		      				buttons: Ext.MessageBox.OK,
		      				width:300,
		      				icon: iconSaveToPF,
		      				animEl: 'root-menu'        			
		      			});
		      	    	//var buddy = new Sbi.commons.ComponentBuddy({
		      	    	//	buddy : messageBox
		      	    	//});
		      		} else {
		      			Sbi.exception.ExceptionHandler.showErrorMessage('Server response is empty', 'Service Error');
		      		}
	    	  	}
	          },
	          scope: this,
	  		  failure: Sbi.exception.ExceptionHandler.handleFailure      
	     });
	}
	
	
	, onSaveRememberMeButtonClicked: function () {
		this.win_saveRememberMe = new Sbi.execution.SaveRememberMeWindow({});
		this.win_saveRememberMe.show();
	}
	
	, onNotesButtonClicked: function () {
		this.win_notes = new Sbi.execution.NotesWindow();
		this.win_notes.show();
	}
	
	, onMetadataButtonClicked: function () {
		this.win_metadata = new Sbi.execution.MetadataWindow({'OBJECT_ID': this.executionInstance.OBJECT_ID});
		this.win_metadata.show();
	}
	
	, onRatingButtonClicked: function () {
		this.win_rating = new Sbi.execution.RatingWindow({'OBJECT_ID': this.executionInstance.OBJECT_ID});
		this.win_rating.show();
	}
});