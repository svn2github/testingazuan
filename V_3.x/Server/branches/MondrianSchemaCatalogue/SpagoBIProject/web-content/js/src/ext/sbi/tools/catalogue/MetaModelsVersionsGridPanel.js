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
 * [list]
 * 
 * 
 * Public Events
 * 
 * [list]
 * 
 * Authors - Davide Zerbetto (davide.zerbetto@eng.it)
 */
Ext.ns("Sbi.tools.catalogue");

Sbi.tools.catalogue.MetaModelsVersionsGridPanel = function(config) {

	var baseParams = {LIGHT_NAVIGATOR_DISABLED: 'TRUE'};
	
	var defaultSettings = {
		title: LN('sbi.tools.catalogue.metamodelsversionsgridpanel.title')
		, width : 370
        , height : 200
        , layout : 'fit'
	};

	if (Sbi.settings && Sbi.settings.tools && Sbi.settings.tools.catalogue && Sbi.settings.tools.catalogue.metamodelsversionsgridpanel) {
		defaultSettings = Ext.apply(defaultSettings, Sbi.settings.tools.catalogue.metamodelsversionsgridpanel);
	}

	var c = Ext.apply(defaultSettings, config || {});
	
	Ext.apply(this, c);
	
	this.services = this.services || new Array(); 
	
	this.services['deleteAllMetaModelVersions'] = this.services['deleteAllMetaModelVersions'] || Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'DELETE_ALL_META_MODEL_VERSIONS_ACTION'
		, baseParams: baseParams
	});
	
	this.services['deleteMetaModelVersion'] = this.services['deleteMetaModelVersion'] || Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'DELETE_META_MODEL_VERSION_ACTION'
		, baseParams: baseParams
	});
	
	this.services['restoreDsVersionService'] = this.services['restoreDsVersionService'] || Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'RESTORE_META_MODEL_VERSION_ACTION'
		, baseParams: baseParams
	});

	this.addEvents('verionrestored', 'verionsdeleted');

	c = Ext.apply(c, {
        store 			: this.initStore()
        , cm 			: this.initCm()
        , sm 			: this.initSm()
        , frame 		: true
        , autoscroll 	: true
        , tbar			: this.initTb()
	}); 

    // constructor
    Sbi.tools.catalogue.MetaModelsVersionsGridPanel.superclass.constructor.call(this, c);

};

Ext.extend(Sbi.tools.catalogue.MetaModelsVersionsGridPanel, Ext.grid.GridPanel, {
  
	services : null

  	,
  	initCm : function () {
  		var cm = new Ext.grid.ColumnModel({
  			columns : [
	       		{
	       		    name			: 'id'
	       		    , hidden		: true
	       		},{
	       	    	header			: LN('sbi.tools.catalogue.metamodelsversionsgridpanel.userIn')
	       	    	, width			: 180
	       			, id			: 'userIn'
	       			, sortable		: false
	       			, dataIndex		: 'userIn' 
	       	    },{
	       	    	header			: LN('sbi.tools.catalogue.metamodelsversionsgridpanel.dateIn')
	       	    	, width			: 130
	       			, id			: 'dateIn'
	       			, sortable		: true
	       			, dataIndex		: 'dateIn' 
	       	    }
  			]
  		});
  		return cm;
  	}
  	
  	,
  	initStore : function () {
       	var store = new Ext.data.JsonStore({
   		    id : 'id'
   		    , fields : [
   		       'id'
   		       , 'userIn'
   		       , 'dateIn'
         	]
       	});
       	return store;
  	}
  	
  	,
  	initSm : function () {
		var sm = new Ext.grid.RowSelectionModel({
	         singleSelect: true
	    });
		return sm;
  	}
  	
  	,
  	initTb : function () {
		var tb = new Ext.Toolbar({
			items : [
			    new Ext.Toolbar.Button({
					text: LN('sbi.tools.catalogue.metamodelsversionsgridpanel.deleteAll')
					, iconCls: 'icon-clear'
					, handler: this.onDeleteAll
					, width: 30
					, scope: this
				})
			]
		});
  		return tb;
  	}

  	,
  	loadItems: function (versions) {
		this.getStore().loadData(versions);
	}

    ,
    onRestore: function () {
    	alert('to be implemented');
    }
    
    ,
    onDelete: function() {   	
    	alert('to be implemented');
    }
    
    ,
    onDeleteAll: function() {
    	alert('to be implemented');
    }
    
    ,
    getCurrentVersions: function() {
	    var toReturn = new Array();
		var length = this.getStore().getCount();
		for (var i = 0 ; i < length ; i++ ) {
			var item = this.getStore().getAt(i);
			var data = item.data;
			toReturn.push(data);
		}
		return toReturn;
    }

});

