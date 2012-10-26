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
	
	this.services['getVersionsService'] = this.services['getVersionsService'] || Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'GET_META_MODEL_VERSIONS_ACTION'
		, baseParams: baseParams
	});
	
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

	this.init();
	
	c = Ext.apply(c, {
        store 			: this.store
        , cm 			: this.cm
        , sm 			: this.sm
        , frame 		: true
        , autoscroll 	: true
        , tbar			: this.tb
        , plugins		: [ this.downloadColumn ]
	}); 

    // constructor
    Sbi.tools.catalogue.MetaModelsVersionsGridPanel.superclass.constructor.call(this, c);

};

Ext.extend(Sbi.tools.catalogue.MetaModelsVersionsGridPanel, Ext.grid.GridPanel, {
  
	services : null

	,
	init : function () {
		this.initStore();
		this.initCm();
		this.initSm();
		this.initTb();
	}
	
  	,
  	initCm : function () {
  		
        this.downloadColumn = new Ext.grid.ButtonColumn({
			header 					:  ' '
			, iconCls 				: 'icon-download'
			, clickHandler 			: this.downloadHandler
			, scope 				: this  
			, width 				: 25
			, renderer : function(v, p, record) {
			       return '<center><img class="x-mybutton-'+this.id+' grid-button ' +this.iconCls+'" width="16px" height="16px" src="'+Ext.BLANK_IMAGE_URL+'"/></center>';
			}
        });
  		
  		this.cm = new Ext.grid.ColumnModel({
  			columns : [
	       		{
	       		    name			: 'id'
	       		    , hidden		: true
	       		},{
	       	    	header			: LN('sbi.tools.catalogue.metamodelsversionsgridpanel.userIn')
	       	    	, width			: 80
	       			, id			: 'creationUser'
	       			, sortable		: true
	       			, dataIndex		: 'creationUser' 
	       	    },{
	       	    	header			: LN('sbi.tools.catalogue.metamodelsversionsgridpanel.dateIn')
	       	    	, width			: 80
	       			, id			: 'creationDate'
	       			, sortable		: true
	       			, dataIndex		: 'creationDate'
	       			, renderer: Ext.util.Format.dateRenderer(Sbi.config.localizedDateFormat)
	       	    },{
	       	    	header			: LN('sbi.tools.catalogue.metamodelsversionsgridpanel.fileName')
	       	    	, width			: 80
	       			, id			: 'fileName'
	       			, sortable		: true
	       			, dataIndex		: 'fileName' 
	       	    }
	       	    , this.downloadColumn
  			]
  		});
  	}
  	
  	,
  	initStore : function () {
  		
  	    this.store = new Ext.data.JsonStore({
  	        root: 'results'
  	        , id : 'id'
  	        , fields: ['id', 'creationUser', 'fileName', 'dimension', 'modelId', 'active'
  	                   	, {name:'creationDate', type:'date', dateFormat: Sbi.config.clientServerTimestampFormat}
  	                   ]
  			, url: this.services['getVersionsService']
  	    });
  	    
  	}
  	
  	,
  	initSm : function () {
		this.sm = new Ext.grid.CheckboxSelectionModel({
	         singleSelect: true
	    });
  	}
  	
  	,
  	initTb : function () {
		this.tb = new Ext.Toolbar({
			items : [
			    '->'
			    , new Ext.Toolbar.Button({
					text: LN('sbi.tools.catalogue.metamodelsversionsgridpanel.deleteNonActive')
					, iconCls: 'icon-clear'
					, handler: this.onDelete
					, width: 30
					, scope: this
				})
			]
		});
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
    onDelete: function() {
    	alert('to be implemented');
    }
    
    ,
    downloadHandler: function(e, t) {
    	alert('to be implemented');
    }

});

