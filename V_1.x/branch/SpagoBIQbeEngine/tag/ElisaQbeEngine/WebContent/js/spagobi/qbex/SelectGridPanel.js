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
  * - name (mail)
  */

Ext.ns("Sbi.qbe");

Sbi.qbe.SelectGridPanel = function(config) {
	
	var c = Ext.apply({
		// set default values here
	}, config || {});
	
	this.services = new Array();
	
	this.services['getParameters'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'GET_PARAMETERS_ACTION'
		, baseParams: {}
	});
	
	this.services['getAttributes'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'GET_ATTRIBUTES_ACTION'
		, baseParams: {}
	});
		
	
	this.addEvents('filter', 'having');
	
	this.initStore(c);
	this.initSelectionModel(c);
	this.initColumnModel(c);
	this.initToolbar(c);
	this.initGrid(c);
	this.initGridListeners(c);
	
	c = Ext.apply(c, {
		border: true, 
		layout: 'fit',
		autoWidth: Ext.isIE ? false : true,
		width: Ext.isIE ? undefined : 'auto',
		//width: 1000,
		items: [this.grid]
	});
	
	// constructor
	Sbi.qbe.SelectGridPanel.superclass.constructor.call(this, c);
	/*
	this.on('render', function(){
		if(this.dropTarget === null) {
			this.dropTarget = new Sbi.qbe.SelectGridDropTarget(this);
		}
	}, this) ;
	*/
	if(c.query && c.query.fields && c.query.fields.length > 0){
    	this.loadSavedData(c.query);
    }

};

Ext.extend(Sbi.qbe.SelectGridPanel, Ext.Panel, {
    
	services: null
	, store: null
	, Record: null
	, sm: null
	, cm: null
	, plgins: null
	, toolbar: null
	, distinctCheckBox: null
	, grid: null
	, dropTarget: null
	, calculatedFieldWizard : null
	
	, type: 'selectgrid'
	
	// static members
	, aggregationFunctionsStore:  new Ext.data.SimpleStore({
		 fields: ['funzione', 'nome', 'descrizione'],
	     data : [
	        ['NONE', LN('sbi.qbe.selectgridpanel.aggfunc.name.none'), LN('sbi.qbe.selectgridpanel.aggfunc.desc.none')],
	        ['SUM', LN('sbi.qbe.selectgridpanel.aggfunc.name.sum'), LN('sbi.qbe.selectgridpanel.aggfunc.desc.sum')],
	        ['AVG', LN('sbi.qbe.selectgridpanel.aggfunc.name.avg'), LN('sbi.qbe.selectgridpanel.aggfunc.desc.avg')],
	        ['MAX', LN('sbi.qbe.selectgridpanel.aggfunc.name.max'), LN('sbi.qbe.selectgridpanel.aggfunc.desc.max')],
	        ['MIN', LN('sbi.qbe.selectgridpanel.aggfunc.name.min'), LN('sbi.qbe.selectgridpanel.aggfunc.desc.min')],
	        ['COUNT', LN('sbi.qbe.selectgridpanel.aggfunc.name.count'), LN('sbi.qbe.selectgridpanel.aggfunc.desc.count')]
	     ] 
	 })

	, orderingTypesStore: new Ext.data.SimpleStore({
	     fields: ['type', 'nome', 'descrizione'],
	     data : [
		    ['NONE', LN('sbi.qbe.selectgridpanel.sortfunc.name.none'), LN('sbi.qbe.selectgridpanel.sortfunc.desc.none')],
		    ['ASC', LN('sbi.qbe.selectgridpanel.sortfunc.name.asc'), LN('sbi.qbe.selectgridpanel.sortfunc.desc.asc')],
		    ['DESC', LN('sbi.qbe.selectgridpanel.sortfunc.name.desc'), LN('sbi.qbe.selectgridpanel.sortfunc.desc.desc')]
		] 
	})
	
	, DATAMART_FIELD: 'datamartField'
	, CALCULATED_FIELD: 'calculatedField'

	// public methods
	
	, loadSavedData: function(query) {
  		this.setFields(query.fields);
  		
  		if (query.distinct === true) {
  			this.distinctCheckBox.setValue(true);
  		} else {
  			this.distinctCheckBox.setValue(false);
  		}
  	}
  	
	, createField: function() {
		var field;
		
		field = new Object();		
		field = Ext.apply(field, {
			id: '', 
			alias: '', 
			type: this.DATAMART_FIELD,
			
			entity: '', 
			field: '',
			
			funct: '',
			group: '',
			order: '',
			
			
			'include': true, 
			visible: true
		});
			      
		return field;
	}

	, addField : function(field) {
		field = field || {};
		field = Ext.apply(this.createField(), field || {});
		var record = new this.Record( field );
		this.grid.store.add(record); 
	}
	
	, insertField: function(field, i) {
		if(i != undefined) {
			field = field || {};
			field = Ext.apply(this.createField(), field || {});
			var record = new this.Record( field );
			this.grid.store.insert(i, record); 
		} else {
			this.addField(field);
		}
	}
	
	, modifyField: function(field, i) {
		if(i != undefined) {			
			var record = this.store.getAt( i );
			Ext.apply(record.data, field || {});	
			record = this.store.getAt( i );
			this.store.fireEvent('datachanged', this.store) ;
		}
	}

	, addRow: function(config, i) {	
	   Sbi.qbe.commons.deprectadeFunction('SelectGridPanel', 'addRow');
	   this.insertField(config, i);
	   /*
	   var record = new this.Record({
	       funct: '',
	       order: '',
	       alias: config.data['field'], 
	       id: config.data['id'], 
	       entity: config.data['entity'], 
	       field: config.data['field'],
	       'include': true, 
	       visible: true
	    });
    
    	if(i != undefined) {
      		this.store.insert(i, record); 
    	} else {
      		this.store.add(record); 
    	}
    	*/
	}
	
	, deleteFields: function() {
		this.grid.store.removeAll();
	}
	
	, setFields: function(fields) {
		this.deleteFields();
		for(var i = 0; i < fields.length; i++) {
  			var field = fields[i];
  			var record = new this.Record(field);
  			this.store.add(record); 
  		}
	}
	
	, getFields: function() {
		var fields = [];
		for(i = 0; i < this.store.getCount(); i++) {
			var record = this.store.getAt(i);
			var field = Ext.apply({}, record.data);
			field.group = field.group || false;
			fields.push(field);
		}
		
		return fields;
	}
	
	, getRowsAsJSONParams: function() {
		Sbi.qbe.commons.deprectadeFunction('SelectGridPanel', 'getRowsAsJSONParams');
		var jsonStr = '[';
		for(i = 0; i < this.store.getCount(); i++) {
			var tmpRec = this.store.getAt(i);
			if(i != 0) jsonStr += ',';
			jsonStr += '{';
			jsonStr += 	'"id" : "' + tmpRec.data['id'] + '",';	
			jsonStr += 	'"entity" : "' + tmpRec.data['entity'] + '",';	
			jsonStr += 	'"field"  : "' + tmpRec.data['field']  + '",';	
			jsonStr += 	'"alias"  : "' + tmpRec.data['alias']  + '",';	
			jsonStr += 	'"group"  : "' + tmpRec.data['group']  + '",';
			jsonStr += 	'"order"  : "' + tmpRec.data['order']  + '",';
			jsonStr += 	'"funct"  : "' + tmpRec.data['funct']  + '",';
			jsonStr += 	'"include" : ' + tmpRec.data['include'] + '';	
			jsonStr += 	'"visible" : ' + tmpRec.data['visible'] + '';	
			jsonStr += '}';	
		}
		jsonStr += ']';
		
		return jsonStr;
	}
	
	, hideNonVisibleRows: function(button, pressed) {
		
		this.grid.store.filterBy(function(record, id) {
			if(!pressed) return true; // show all
			
			return record.data['visible'];
		});
	}
	
	
	, updateGroupByColumn: function() {
		
		var index = this.grid.store.findBy(function(record) {
			var isFunction = !(record.data['funct'] == undefined
					|| record.data['funct'].trim() == ''
						|| record.data['funct'] == 'NONE');
			return isFunction;
		});
					
		var groupFlag = (index == -1? '': 'true');			
					
		this.grid.store.each(function(record) {
			
			if( record.data['funct'] == undefined
					|| record.data['funct'].trim() == ''
						|| record.data['funct'] == 'NONE') {
					//alert('true');
					record.data['group'] = groupFlag;	
				} else {
					//alert('false');
					record.data['group'] = '';	
				}								
		})
	}
	
   
    // private methods
	
	
	, initStore: function(config) {
		this.store =  new Ext.data.SimpleStore({
	        fields: [
	           {name: 'id'},
	           {name: 'alias'},
	           {name: 'type'},
	           
	           {name: 'entity'},
	           {name: 'longDescription'},
	           {name: 'field'},
	           
	           {name: 'funct'},	 
	           {name: 'group'},
	           {name: 'order'},
	           
	           {name: 'include'},
	           {name: 'visible'},
	           
	           {name: 'filter'},
	           {name: 'del'}          
	        ]
		});
		
		this.Record = Ext.data.Record.create([
		      {name: 'id', type: 'string'},
		      {name: 'alias', type: 'string'},
		      {name: 'type', type: 'string'},
		      
		      {name: 'entity', type: 'string'},
		      {name: 'longDescription', type: 'string'},
		      {name: 'field', type: 'string'},
		     
		      {name: 'funct', type: 'string'},
		      {name: 'group', type: 'string'},
		      {name: 'order', type: 'string'},
		      
		      {name: 'include', type: 'bool'},
		      {name: 'visible', type: 'bool'},
		      
		      {name: 'filter', type: 'string'},
		      {name: 'having', type: 'string'},
		      {name: 'del', type: 'string'}
		]); 
	}
	
	
	, initSelectionModel: function(config) {
		this.sm = new Ext.grid.RowSelectionModel();
	}
	
	, initColumnModel: function(config) {
		// check-columns
	    var visibleCheckColumn = new Ext.grid.CheckColumn({
	       header: LN('sbi.qbe.selectgridpanel.headers.visible')
	       , dataIndex: 'visible'
	       , hideable: true
		   , hidden: false	
		   , width: 50
		   , sortable: false
	    });
	    
	    var groupCheckColumn = new Ext.grid.CheckColumn({
	       header:  LN('sbi.qbe.selectgridpanel.headers.group')
	       , dataIndex: 'group'
	       , hideable: true
		   , hidden: false	
		   , width: 50
		   , sortable: false
	    });
	    
	    var includeCheckColumn = new Ext.grid.CheckColumn({
		       header:  LN('sbi.qbe.selectgridpanel.headers.include')
		       , dataIndex: 'include'
		       , hideable: true
			   , hidden: false	
			   , width: 50
			   , sortable: false
		});
	    
	    // button-columns
	    var delButtonColumn = new Ext.grid.ButtonColumn({
	       header:  LN('sbi.qbe.selectgridpanel.headers.delete')
	       , dataIndex: 'delete'
	       , imgSrc: '../img/querybuilder/delete.gif'
	       , clickHandler:function(e, t){
	          var index = this.grid.getView().findRowIndex(t);
	          var record = this.grid.store.getAt(index);
	          this.grid.store.remove(record);
	       }
	       , hideable: true
	       , hidden: true	
	       , width: 50
	       , sortable: false
	     });
	     
	    var filterButtonColumn = new Ext.grid.ButtonColumn({
		   header:  LN('sbi.qbe.selectgridpanel.headers.filter')
		   , dataIndex: 'filter'
		   , imgSrc: '../img/querybuilder/filter.gif'
		      
		   , clickHandler:function(e, t){
		          var index = this.grid.getView().findRowIndex(t);
		          var record = this.grid.store.getAt(index);
		          this.grid.fireEvent('actionrequest', this, 'filter', record);
		   }
	       , hideable: true
	       , hidden: false	
	       , width: 50
	       , sortable: false
		});
	    
	    var havingButtonColumn = new Ext.grid.ButtonColumn({
			   header:  LN('sbi.qbe.selectgridpanel.headers.having')
			   , dataIndex: 'having'
			   , imgSrc: '../img/querybuilder/filter.gif'
			      
			   , clickHandler:function(e, t){
			          var index = this.grid.getView().findRowIndex(t);
			          var record = this.grid.store.getAt(index);
			          this.grid.fireEvent('actionrequest', this, 'having', record);
			   }
		       , hideable: true
		       , hidden: false	
		       , width: 50
		       , sortable: false
		});
		
	    this.cm = new Ext.grid.ColumnModel([
		     new Ext.grid.RowNumberer(),
		     {
		    	 header: LN('sbi.qbe.selectgridpanel.headers.entity')
		    	 , dataIndex: 'entity'
		    	 , hideable: true
			     , hidden: false	
			     , sortable: false
		     }, {
		    	 id:'field'
		         , header: LN('sbi.qbe.selectgridpanel.headers.field')
		         , dataIndex: 'field'
		         , hideable: true
				 , hidden: false	
				 , sortable: false
				 , renderer: this.getCellTooltip
		     }, {
		         header: LN('sbi.qbe.selectgridpanel.headers.alias')
		         , dataIndex: 'alias'
		         , editor: new Ext.form.TextField({
		        	 allowBlank: true
		         })
			     , hideable: true
			     , hidden: false	
			     , sortable: false
		     }, {
		    	 header: LN('sbi.qbe.selectgridpanel.headers.function')
		         , dataIndex: 'funct'
		         , editor: new Ext.form.ComboBox({
			         tpl: '<tpl for="."><div ext:qtip="{nome}: {descrizione}" class="x-combo-list-item">{funzione}</div></tpl>',	
			         allowBlank: true,
			         editable:false,
			         store: this.aggregationFunctionsStore,
			         displayField:'funzione',
			         valueField:'funzione',
			         typeAhead: true,
			         mode: 'local',
			         triggerAction: 'all',
			         autocomplete: 'off',
			         emptyText: LN('sbi.qbe.selectgridpanel.aggfunc.editor.emptymsg'),
			         selectOnFocus:true
		         })
			     , hideable: true
			     , hidden: false
			     , width: 50
			     , sortable: false
		     }, {
		    	 header: LN('sbi.qbe.selectgridpanel.headers.order')
		         , dataIndex: 'order'
		         , editor: new Ext.form.ComboBox({
			         tpl: '<tpl for="."><div ext:qtip="{nome}: {descrizione}" class="x-combo-list-item">{type}</div></tpl>',	
			         allowBlank: true,
			         editable:false,
			         store: this.orderingTypesStore,
			         displayField:'type',
			         valueField:'type',
			         typeAhead: true,
			         mode: 'local',
			         triggerAction: 'all',
			         autocomplete: 'off',
			         emptyText: LN('sbi.qbe.selectgridpanel.sortfunc.editor.emptymsg'),
			         selectOnFocus:true
		         })
			     , hideable: true
			     , hidden: false	
			     , width: 50
			     , sortable: false
		     },
		     groupCheckColumn, 
		     includeCheckColumn,
		     visibleCheckColumn,
		     filterButtonColumn,
		     havingButtonColumn,
		     delButtonColumn
	     ]);	
	    
	    this.plgins = [visibleCheckColumn, includeCheckColumn, groupCheckColumn, delButtonColumn, filterButtonColumn, havingButtonColumn];
	}
	
	
	
	, initCalculatedFieldWizard: function() {
		
		var fields = new Array();
		
		var parametersLoader = new Ext.tree.TreeLoader({
	        baseParams: {},
	        dataUrl: this.services['getParameters']
	    });
		
		var attributesLoader = new Ext.tree.TreeLoader({
	        baseParams: {},
	        dataUrl: this.services['getAttributes']
	    });
		
		var crossFn = Ext.util.Format.htmlEncode("String label = 'bestByRegion';") + '<br>' + 
			Ext.util.Format.htmlEncode("String text= fields['salesRegion'];") + '<br>' + 
			Ext.util.Format.htmlEncode("String params= 'region=5';") + '<br>' + 
			Ext.util.Format.htmlEncode("String subobject;") + '<p>' + 
			Ext.util.Format.htmlEncode("String result = '';") + '<p>' + 
			Ext.util.Format.htmlEncode("result +='<a href=\"#\" onclick=\"javascript:sendMessage({';") + '<br>' + 
			Ext.util.Format.htmlEncode("result +='\\'label\\':\\'' + label + '\\'';") + '<br>' + 
			Ext.util.Format.htmlEncode("result +=', parameters:\\'' + params + '\\'';") + '<br>' + 
			Ext.util.Format.htmlEncode("result +=', windowName: this.name';") + '<br>' + 
			Ext.util.Format.htmlEncode("if(subobject != null) result +=', subobject:\\'' + subobject +'\\'';") + '<br>' + 
			Ext.util.Format.htmlEncode("result += '},\\'crossnavigation\\')\"';") + '<br>' + 
			Ext.util.Format.htmlEncode("result += '>' + text + '</a>';") + '<p>' + 
			Ext.util.Format.htmlEncode("return result;");
		
		var functions = [
		    {
			    text: 'link'
			    , qtip: 'create a link to external web page'
			    , type: 'function'
			    , value: Ext.util.Format.htmlEncode('\'<a href="${URL}">\' + ${LABEL} + \'</a>\'')
		    }, {
			    text: 'image' , 
			    qtip: 'include an external image'
			    , type: 'function'
			    , value: Ext.util.Format.htmlEncode('\'<img src="${URL}"></img>\'')
		    }, {
			    text: 'cross-navigation'
			    , qtip: 'create a cross navigation link'
			    , type: 'function'
			    , value: crossFn
		    }
		];
		
		
		//this.treeLoader.on('load', this.oonLoad, this);
		//this.treeLoader.on('loadexception', this.oonLoadException, this);
		
		this.calculatedFieldWizard = new Sbi.qbe.CalculatedFieldWizard({
    		title: 'Calculated Field Wizard',
    		expItemGroups: [
    		    {name:'fields', text: 'Fields'}, 
    		    {name:'parameters', text: 'Parameters', loader: parametersLoader}, 
    		    {name:'attributes', text: 'Attributes', loader: attributesLoader},
    		    {name:'functions', text: 'Functions'}
    		],
    		fields: fields,
    		functions: functions,
    		validationService: {
				serviceName: 'VALIDATE_EXPRESSION_ACTION'
				, baseParams: {contextType: 'query'}
				, params: null
			}
    	});
		
    	this.calculatedFieldWizard.on('apply', function(win, formState, targetRecord){
    		var field = {id: formState, alias: formState.alias, type: this.CALCULATED_FIELD};
    		if(targetRecord) {
    			Ext.apply(targetRecord.data, field);	
    			this.store.fireEvent('datachanged', this.store) ;
    		} else {
    			this.addField({id: formState, alias: formState.alias, type: this.CALCULATED_FIELD});
    		}
    	}, this);
	}
	
	, addCalculatedField: function(targetRecord) {
		
		
		if(this.calculatedFieldWizard === null) {
			this.initCalculatedFieldWizard();
		}
		
		var records = this.store.queryBy( function(record) {
			return record.data.include === true;
		});
		
		var fields = new Array();
		records.each(function(r) {
			var field = {
				uniqueName: r.data.id,
				alias: r.data.alias,
				text: r.data.alias, 
				qtip: r.data.entity + ' : ' + r.data.field, 
				type: 'field', 
				value: 'fields[\'' + r.data.alias + '\']'
			};	
			fields.push(field);
		});					
		this.calculatedFieldWizard.validationService.params = {fields: Ext.util.JSON.encode(fields)};
		this.calculatedFieldWizard.setExpItems('fields', fields);
		this.calculatedFieldWizard.setTargetRecord(targetRecord);
		
		this.calculatedFieldWizard.show();
	}
	
	, initToolbar: function(config) {
		this.distinctCheckBox = new Ext.form.Checkbox({
			checked: false,
			boxLabel: LN('sbi.qbe.selectgridpanel.buttons.text.distinct')
		});
		
		this.toolbar = new Ext.Toolbar({
			items: [
			  this.distinctCheckBox,'-',
			  {
	            text: LN('sbi.qbe.selectgridpanel.buttons.text.add'),
	            tooltip: LN('sbi.qbe.selectgridpanel.buttons.tt.add'),
	            iconCls:'option',
	            listeners: {
				  'click': {
					fn: function(){this.addCalculatedField(null);},
					scope: this
				   }
	            }
	          },'-',{
	            text: LN('sbi.qbe.selectgridpanel.buttons.text.hide'),
	            tooltip: LN('sbi.qbe.selectgridpanel.buttons.tt.hide'),
	            enableToggle: true,
	            iconCls:'option',
	            listeners: {
	            	'toggle': {
 						fn: this.hideNonVisibleRows,
 						scope: this
 					}
	            }
	          },'-',{
	            text: LN('sbi.qbe.selectgridpanel.buttons.text.deleteall'),
	            tooltip: LN('sbi.qbe.selectgridpanel.buttons.tt.deleteall'),
	            iconCls:'remove',
	            listeners: {
	            	'click': {
 						fn: this.deleteFields,
 						scope: this
 					}
	            }
	        }]
		});
	}

	, initGrid: function(config) {
		
	     // create the Grid
		 this.grid = new Ext.grid.EditorGridPanel({
			    title: LN('sbi.qbe.selectgridpanel.title'),   
			 	store: this.store,
		        cm: this.cm,  
		        sm : this.sm,
		        tbar: this.toolbar,
		        
		        clicksToEdit:1,
		        plugins: this.plgins,
		        		        
		        height: 350,
		        frame: true,
		        border:true,  
		        style:'padding:10px',
		        iconCls:'icon-grid',
		        collapsible:false,
		        layout: 'fit',
		        viewConfig: {
		            forceFit:true
		        },		
		        
		        enableDragDrop:true,
     			ddGroup: 'gridDDGroup'	
		    });
		 	this.grid.type = this.type;
	}
	
	, initGridListeners: function( config ) {
		this.grid.addEvents('actionrequest');
		this.grid.on("actionrequest", function(grid, action, record){
			if(action === 'filter') {
				this.fireEvent('filter', this, record);
			}
			if(action === 'having') {
				this.fireEvent('having', this, record);
			}
		}, this);
		
		this.grid.on("rowdblclick", function(grid,  rowIndex, e){
	    	var row;
	       	var record = grid.getStore().getAt( rowIndex );
	       	if(record.data.type === this.CALCULATED_FIELD) {
	       		this.addCalculatedField(record);
	       	}
	     }, this);
		
		
		this.grid.on("mouseover", function(e, t){
	    	var row;
	        this.targetRow = t;
	        if((row = this.getView().findRowIndex(t)) !== false){
	            this.getView().addRowClass(row, "row-over");
	        }     
	     }, this.grid);
	     
	     this.grid.on("mouseout", function(e, t){
	        var row;
	        this.targetRow = undefined;
	        if((row = this.getView().findRowIndex(t)) !== false && row !== this.getView().findRowIndex(e.getRelatedTarget())){
	            this.getView().removeRowClass(row, "row-over");
	        }
	     }, this.grid);
	    
	    this.grid.on('keydown', function(e){ 
	      if(e.keyCode === 46) {
	        var sm=this.getSelectionModel();
	        var ds = this.getStore()
	        var rows=sm.getSelections();
	        for (i = 0; i < rows.length; i++) {
	          this.store.remove( ds.getById(rows[i].id) );
	        }
	      }      
	    }, this.grid);	
	    
	    this.grid.store.on('update', function(e){
	    	this.updateGroupByColumn();
	    }, this); 
	}
	
	, getCellTooltip: function (value, cell, record) {
	 	var tooltipString = record.data.longDescription;
	 	if (tooltipString !== undefined && tooltipString != null) {
	 		cell.attr = ' ext:qtip="'  + tooltipString + '"';
	 	}
	 	return value;
	}
	
});