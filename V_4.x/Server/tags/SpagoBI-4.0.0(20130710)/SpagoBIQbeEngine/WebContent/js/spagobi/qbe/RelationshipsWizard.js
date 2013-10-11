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

Sbi.qbe.RelationshipsWizard = function(config) {
	 
	var defaultSettings = {
		title : LN('sbi.qbe.relationshipswizard.title')
		, width : 700
		, height : 400
		, mainGridConfig : {
			width : 300
		}
	};
	  
	if (Sbi.settings && Sbi.settings.qbe && Sbi.settings.qbe.relationshipswizard) {
		defaultSettings = Ext.apply(defaultSettings, Sbi.settings.qbe.relationshipswizard);
	}
	  
	var c = Ext.apply(defaultSettings, config || {});
	  
	Ext.apply(this, c);
	  
	this.services = this.services || new Array(); 

	this.init();
	 
	c = Ext.apply(c, {
		//layout: 'column'
		//, layoutConfig: {
		//	columns: 100
		//}
		layout : 'border'
		, items : [this.mainGrid, this.detailGrid]
	});

	// constructor
	Sbi.qbe.RelationshipsWizard.superclass.constructor.call(this, c);

};

Ext.extend(Sbi.qbe.RelationshipsWizard, Ext.Panel, {
    
    services : null
    , mainGrid : null
    , detailGrid : null
    , mainStore : null
    , detailStore : null
   
    // private methods
    ,
    init : function () {
    	this.initMainStore();
    	this.initDetailStore();
    	this.initMainGrid(this.mainGridConfig || {});
    	this.initDetailGrid(this.detailGridConfig || {});
    }

    ,
    initMainStore : function () {
    	this.mainStore = new Ext.data.GroupingStore({
    		idIndex : 0
    		, reader: new  Ext.data.JsonReader({
				fields : [ 'id', 'name', 'entity', 'choices' ]
    		})
    		, groupField : 'entity'
    		, data : this.ambiguousFields 
    	});
    }
    
    ,
    initDetailStore : function () {
    	this.detailStore = new Ext.data.JsonStore({
    		idIndex : 0
    	    , fields : [ 'path', 'active', 'nodes' ]
    		, data : []
    	});
    	this.detailStore.on('load', this.setRecordsPath, this);
    }
    
    ,
    setRecordsPath : function ( theStore, records, options ) {
    	for (var i = 0; i < records.length; i++) {
    		var aRecord = records[i];
    		aRecord.set('path', this.getRecordPath(aRecord));
    	}
    	theStore.commitChanges();
    }
    
    ,
    getRecordPath : function (aRecord) {
    	var toReturn = '';
    	var nodes = aRecord.get('nodes');
    	var lastTarget = null; // this is useful because the relations are undirected, therefore I have to understand the target of a node
    	for (var i = 0; i < nodes.length; i++) {
    		var node = nodes[i];
    		if (i == 0) {
    			toReturn += node.sourceName + ' -- ' + node.relationshipName + ' -- ' + node.targetName;
    			lastTarget = node.targetName;
    		} else {
    			var nextTarget = node.targetName == lastTarget ? node.sourceName : node.targetName;
    			toReturn += ' -- ' + node.relationshipName + ' -- ' + nextTarget;
    			lastTarget = nextTarget;
    		}
    	}
    	return toReturn;
    }
    
    ,
    initMainGrid: function (gridConfig) {
	    this.mainGrid = new Ext.grid.GridPanel(Ext.apply(gridConfig || {}, {
	        store : this.mainStore
	        , colModel: new Ext.grid.ColumnModel({
	            columns: [
	                {header: LN('sbi.qbe.relationshipswizard.columns.name'), dataIndex: 'name'}
	                , {header: LN('sbi.qbe.relationshipswizard.columns.entity'), dataIndex: 'entity'}
	            ]
	        })
	        , sm : new Ext.grid.RowSelectionModel({singleSelect : true})
	        , frame : true
	        , border : true  
	        , collapsible : false
	        , layout : 'fit'
			, view : new Ext.grid.GroupingView({
				forceFit : true
				, groupTextTpl : '{text}'
			})
	    	, region : 'west'
		}));
	    this.mainGrid.on('rowclick', this.mainGridOnRowclickHandler, this);
    }
    
    ,
    initDetailGrid: function (gridConfig) {
 	    this.detailGrid = new Ext.grid.GridPanel(Ext.apply(gridConfig || {}, {
	        store : this.detailStore
	        , colModel: new Ext.grid.ColumnModel({
	            columns: [{
	            	header: LN('sbi.qbe.relationshipswizard.columns.path')
	                , dataIndex: 'path'
	                , renderer: this.getCellTooltip
	            }]
	        })
	        , sm : new Ext.grid.RowSelectionModel({singleSelect : true})
	        , frame : true
	        , border : true  
	        , collapsible : false
	        , layout : 'fit'
	        , viewConfig : {
	            forceFit : true
				, getRowClass : function(row, index) {
					var cls = '';
					var data = row.data;
					if (data.active == true) {
						cls = 'green-row'
					}
					return cls;
				}
	        }
	    	, region : 'center'
		}));
 	   this.detailGrid.on('rowdblclick', this.detailGridOnRowdblclickHandler, this);
    }
    
    ,
    mainGridOnRowclickHandler : function ( theGrid, rowIndex, e ) {
    	var choises = this.getOptionsByIndex(rowIndex);
    	this.detailStore.loadData(choises);
    }
    
    ,
    getOptionsByIndex : function ( rowIndex ) {
    	var record = this.mainStore.getAt(rowIndex);
    	var choises = record.data.choices;
    	return choises;
    }
    
    ,
    detailGridOnRowdblclickHandler : function ( theGrid, rowIndex, e ) {
    	this.toggleOptionByIndex(rowIndex);
    }
    
    ,
    toggleOptionByIndex : function ( rowIndex ) {
    	//this.removeCurrentActive();
		var activeRecord = this.detailStore.getAt(rowIndex);
		var active = activeRecord.get('active');
		activeRecord.set('active', !active);
		this.storeChangesInMainStore();
    }
    
    ,
    storeChangesInMainStore : function () {
    	var selectedRecord = this.mainGrid.getSelectionModel().getSelected();
    	var options = this.getDetailStoreContent();
    	selectedRecord.set('choices', options);
    }
    
    ,
    getDetailStoreContent: function () {
    	var toReturn = [];
    	this.detailStore.each(function (aRecord) {
    		toReturn.push(aRecord.data);
    	});
    	return toReturn;
    }
    
    /*
    ,
    removeCurrentActive : function () {
    	var activeIndex = this.detailStore.find( 'active', true );
    	if (activeIndex > -1) {
    		var activeRecord = this.detailStore.getAt(activeIndex);
    		activeRecord.set('active', false);
    	}
    }
    */
    
    /*
	,
	getUserChoiceForField : function (theFieldRecord) {
		var toReturn = [];
		var choices = theFieldRecord.data.choices;
		var length = choices.length, element = null;
		for ( var i = 0; i < length; i++) {
			element = choices[i];
			if (element.active) {
				toReturn.push(element);
			}
		}
		return toReturn;
	}
	*/
	
	,
	getCellTooltip: function (value, cell, record) {
	 	var tooltipString = record.data.path;
	 	if (tooltipString !== undefined && tooltipString != null) {
	 		cell.attr = ' ext:qtip="'  + Sbi.qbe.commons.Utils.encodeEscapes(tooltipString)+ '"';
	 	}
	 	return value;
	}
	
	/*
	,
	removeNonActiveOptions : function (aFieldData) {
		var newArray = [];
		var oldArray = aFieldData['choices'];
		for (var i = 0; i < oldArray.length; i++) {
			var option = oldArray[i];
			if (option.active) {
				delete option.active;
				newArray.push(option);
			}
		}
		aFieldData['choices'] = newArray;
	}
	*/
	
	// public methods
	,
	getUserChoices : function() {
    	var toReturn = [];
    	this.mainStore.each(function (aRecord) {
    		var clone = Ext.apply({}, aRecord.data);
    		//this.removeNonActiveOptions(clone);
    		toReturn.push(clone);
    	}, this);
    	return toReturn;
	}
    
});