/** SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. **/
 
  
 
  
 
  

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

Ext.ns("Sbi.qbe");

Sbi.qbe.RelationshipsWizardWindow = function(config) {
	 
	var defaultSettings = {
		title : LN('sbi.qbe.relationshipswizardwindow.title')
		, width : 820
		, height : 520
		, closeAction : 'hide'
		, maximizable: true
	};
	  
	if (Sbi.settings && Sbi.settings.qbe && Sbi.settings.qbe.relationshipswizardwindow) {
		defaultSettings = Ext.apply(defaultSettings, Sbi.settings.qbe.relationshipswizardwindow);
	}
	  
	var c = Ext.apply(defaultSettings, config || {});
	  
	Ext.apply(this, c);
	  
	this.services = this.services || new Array(); 

	this.init();
	 
	c = Ext.apply(c, {
		items : [this.relationshipsWizard]
		, layout : 'fit'
		, buttons : [
             {
				text: LN('sbi.qbe.relationshipswizardwindow.buttons.apply'),
				handler: this.applyHandler,
				scope: this
			 }, {
				text: LN('sbi.qbe.relationshipswizardwindow.buttons.cancel'),
				handler: this.cancelHandler,
				scope: this
			 }
		]
	});

	// constructor
	Sbi.qbe.RelationshipsWizardWindow.superclass.constructor.call(this, c);
	
	this.addEvents("apply");

};

Ext.extend(Sbi.qbe.RelationshipsWizardWindow, Ext.Window, {
    
	relationshipsWizard : null
	, ambiguousFields : null // must be set in the object passed to the constructor
	
	,
	init : function () {
		this.relationshipsWizard = new Sbi.qbe.RelationshipsWizard({
			ambiguousFields : this.ambiguousFields
			, title : ''
		});
	}

	,
	applyHandler : function () {
		var userChoices = this.relationshipsWizard.getUserChoices();
		this.fireEvent('apply', this, userChoices);
	}
	
	,
	cancelHandler : function () {
		this.close();
	}

});