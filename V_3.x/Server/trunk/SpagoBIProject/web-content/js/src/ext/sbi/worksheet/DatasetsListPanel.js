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


Ext.ns("Sbi.worksheet");

Sbi.worksheet.DatasetsListPanel = function(config) {

	var defaultSettings = {
		frame : true
		, pagingSize : 15
	};
		
	if (Sbi.settings && Sbi.settings.worksheet && Sbi.settings.worksheet.datasetslistpanel) {
		defaultSettings = Ext.apply(defaultSettings, Sbi.settings.worksheet.datasetslistpanel);
	}
	
	var c = Ext.apply(defaultSettings, config || {});
	
	Ext.apply(this, c);
	
	this.services = this.services || new Array(); 
	this.services['getDatasetsList'] = this.services['getDatasetsList'] || Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'GET_DATASETS_USER_LIST'
		, baseParams: new Object()
	});
	
	this.init();
	
	c = Ext.apply(c, {
		items: [this.datasetsGrid]
	});
	
	// constructor
    Sbi.worksheet.DatasetsListPanel.superclass.constructor.call(this, c);
    
};

Ext.extend(Sbi.worksheet.DatasetsListPanel, Ext.Panel, {
	
	datasetsStore : null
	, datasetsGrid : null
	, labelFilterInput : null
	, nameFilterInput : null
	
	,
	init : function () {
		
		this.datasetsStore = new Sbi.widgets.store.InMemoryFilteredStore({
	    	autoLoad: false    	  
			, url: this.services['getDatasetsList']
			, reader : new Ext.data.JsonReader({
				root: 'rows'
				, id : 'id'
		    	, fields: ['id', 'name',
							'label', 'description', 'dsTypeCd',
							'catTypeVn', 'usedByNDocs', 'fileName',
							'query', 'dataSource', 'wsAddress',
							'wsOperation', 'script', 'scriptLanguage',
							'jclassName', 'customData', 'pars', 'trasfTypeCd',
							'pivotColName', 'pivotColValue',
							'pivotRowName', 'pivotIsNumRows', 'dsVersions',
							'qbeSQLQuery', 'qbeJSONQuery', 'qbeDataSource',
							'qbeDatamarts',	'userIn', 'dateIn', 'versNum', 'versId']
			})
		});
		this.datasetsStore.load({params : {start: 0, limit: this.pagingSize}});
		
		var pagingBar = new Ext.PagingToolbar({
			pageSize : this.pagingSize
			, store : this.datasetsStore
			, displayInfo : false
			, scope : this
			, emptyMsg : "No topics to display"
	        , items : this.extraButtons
		});
		
		this.labelFilterInput = new Ext.form.TextField({
			emptyText: LN('sbi.generic.search.label.msg')
			, enableKeyEvents : true
			, listeners : {
				keyup : this.filterStore
				, scope: this
			 }
		});
		
		this.nameFilterInput = new Ext.form.TextField({
			emptyText: LN('sbi.generic.search.name.msg')
			, enableKeyEvents : true
			, listeners : {
				keyup : this.filterStore
				, scope: this
			}
		});
		
		var toolbar =  new Ext.Toolbar([
		    {xtype: 'tbtext', text: LN('sbi.generic.search.label.title'), style: {'padding-left': 20}}
			, this.labelFilterInput
			, {xtype: 'tbtext', text: LN('sbi.generic.search.name.title'), style: {'padding-left': 20}}
			, this.nameFilterInput
			, {xtype: 'tbspacer', width: 30}
			, {
				iconCls : 'icon-clear'
				, handler : this.clearFilterForm
				, scope : this
				, tooltip: LN('sbi.config.manageconfig.fields.clearFilter')
			}
		]);
		
		this.datasetsGrid = new Ext.grid.GridPanel({
		    store: this.datasetsStore
		    , tbar : toolbar
		    , columns : [
		        {header: LN('sbi.generic.label'), dataIndex: 'label', renderer: function (value) {return '<b>' + value + '</b>';}}
		        , {header: LN('sbi.generic.name'), dataIndex: 'name', renderer: function (value) {return '<b>' + value + '</b>';}}
		        , {header: LN('sbi.generic.type'), dataIndex : 'dsTypeCd'}
		        , {header: LN('sbi.generic.author'), dataIndex : 'userIn'}
		    ]
		    , viewConfig : {
		        forceFit : true
	            , enableRowBody : true
	            , showPreview : true
	            , getRowClass : function(record, rowIndex, p, store){
	                if (this.showPreview) {
	                	var description = record.data.description;
	                    p.body = (description == null || description == '') ? 
	                    		'<p class="x-grid-empty">[' + LN('sbi.generic.missing.description') + ']</p>'
	                    		: '<p class="x-grid3-cell-inner">' + description + '</p>';
	                    return 'x-grid3-row-expanded';
	                }
	                return 'x-grid3-row-collapsed';
	            }
		    }
		    , sm : new Ext.grid.RowSelectionModel({singleSelect:true})
            , bbar : pagingBar
		});
		
	}

	,
	getSelectedRecord : function () {
		var record = this.datasetsGrid.getSelectionModel().getSelected();
		return record;
	}
	
	,
	filterStore : function () {
		var label = this.labelFilterInput.getValue().toUpperCase();
		var name = this.nameFilterInput.getValue().toUpperCase();
		var filters = {};
		if (label !== '') {
			filters['label'] = label;
		}
		if (name !== '') {
			filters['name'] = name;
		}
		this.datasetsStore.load(
			{
				params : {
					start: 0
					, limit: this.pagingSize
					, filters : filters
				}
			}
		);
	}
	
	,
	clearFilterForm : function () {
		this.labelFilterInput.setValue('');
		this.nameFilterInput.setValue('');
		this.datasetsStore.load({params : {start: 0, limit: this.pagingSize, filters: {}}});
	}

});