/** SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. **/
 
  
 
  
 
  
 

/**
  * Sbi.qbe.AmbiguousFields
  * 
  * [list]
  * 
  * Public Methods
  * 
  *  [list]
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

Sbi.qbe.AmbiguousFields = function(config) {
	
	var defaultSettings = {
		// defaults here
	};
	
	if (Sbi.settings && Sbi.settings.qbe && Sbi.settings.qbe.ambiguousfields) {
		defaultSettings = Ext.apply(defaultSettings, Sbi.settings.qbe.ambiguousfields);
	}
	
	var c = Ext.apply(defaultSettings, config || {});
	
	Ext.apply(this, c);
	
	this.init();
	
	// constructor
	Sbi.qbe.AmbiguousFields.superclass.constructor.call(this, c);

};

Ext.extend(Sbi.qbe.AmbiguousFields, Ext.util.Observable, {

	store : null
	
	,
	init: function () {
		
    	this.store = new Ext.data.JsonStore({
    		idIndex : 0
    	    , fields : [ 'id', 'name', 'entity', 'choices' ]
    		, data : this.ambiguousFields || []
    	});
		
	}

	,
	merge : function (other) {
		this.store.each(function (aRecord) {
    		var otherRecordIndex = other.getByData(aRecord.data);
    		if (otherRecordIndex > -1) {
    			var otherRecord = other.getAt(otherRecordIndex);
    			this.mergeChoices(aRecord, other, otherRecord);
    		}
    	}, this);
	}
	
	,
	getByData : function (data) {
		var toReturn = -1;
		for (var i = 0; i < this.store.getTotalCount(); i++) {
			var recordData = this.store.getAt(i).data;
    		if (data.id == recordData.id && data.name == recordData.name && data.entity == recordData.entity) {
    			toReturn = i;
    			break;
    		}
    	};
		return toReturn;
	}
	
	,
	getAt : function (index) {
		return this.store.getAt(index);
	}

	,
	mergeChoices : function (aRecord, other, otherRecord) {
		var activeOption = other.getActiveOption(otherRecord);
		this.setActiveOptionIfExists(aRecord, activeOption);
	}
	
	,
	getActiveOption : function (record) {
		var choices = record.data.choices;
		if (choices.length == 1) {
			return choices[0];
		}
		for (var i = 0; i < choices.length; i++) {
			var anOption = choices[i];
			if (anOption.active) {
				return anOption;
			}
		}
	}
	
	,
	setActiveOptionIfExists : function (aRecord, option) {
		var choices = aRecord.data.choices;
		for (var i = 0; i < choices.length; i++) {
			var anOption = choices[i];
			delete anOption.active;
			delete option.active;
			if (Ext.encode(option.nodes) == Ext.encode(anOption.nodes)) {
				anOption.active = true;
			}
		}
	}
	
	,
	getAmbiguousFieldsAsJSONArray : function () {
		var toReturn = [];
		this.store.each(function (aRecord) {
			toReturn.push(aRecord.data);
    	});
		return toReturn;
	}
	
});