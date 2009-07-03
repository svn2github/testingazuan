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
	var params = {};
	this.services['loadDataStore'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'EXEC_QUERY_ACTION'
		, baseParams: params
	});
	
	this.addEvents('filter');
	
	this.initStore(c);
	this.initSelectionModel(c);
	this.initColumnModel(c);
	this.initToolbar(c);
	this.initGrid(c);
	this.initGridListeners(c);
	
	c = Ext.apply(c, {
		border: false, 
		layout: 'fit',
		width: 1000,
		items: [this.grid]
	})
	
	// constructor
	Sbi.qbe.SelectGridPanel.superclass.constructor.call(this, c);
	
	this.on('show', function(){
		if(this.dropTarget === null) {
			//this.dropTarget = new Sbi.qbe.SelectGridDropTarget(this);
		}
	}, this) ;
	
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
	, grid: null
	, dropTarget: null
	
	, type: 'selectgrid'
	
	// static members
	, aggregationFunctionsStore:  new Ext.data.SimpleStore({
		 fields: ['funzione', 'nome', 'descrizione'],
	     data : [
	        ['NONE', LN('sbi.qbe.selectgridpanel.aggfunc.name.none'), LN('sbi.qbe.selectgridpanel.aggfunc.desc.none')],
	        ['SUM', LN('sbi.qbe.selectgridpanel.aggfunc.name.sum'), LN('sbi.qbe.selectgridpanel.aggfunc.desc.sum')],
	        ['AVG', LN('sbi.qbe.selectgridpanel.aggfunc.name.avg'), LN('sbi.qbe.selectgridpanel.aggfunc.desc.avg')],
	        ['MAX', LN('sbi.qbe.selectgridpanel.aggfunc.name.max'), LN('sbi.qbe.selectgridpanel.aggfunc.desc.max')],
	        ['MIN', LN('sbi.qbe.selectgridpanel.aggfunc.name.min'), LN('sbi.qbe.selectgridpanel.aggfunc.desc.min')]
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
	
	// public methods
	
	, loadSavedData: function(query) {
  		for(var i = 0; i < query.fields.length; i++) {
  			var field = query.fields[i];
  			var record = new this.Record(field);
  			this.store.add(record); 
  		}
  	}
  	
	, addRow: function(config, i) {		    
	   var record = new this.Record({
	       funct: '',
	       order: '',
	       alias: config.data['field'], 
	       id: config.data['id'], 
	       entity: config.data['entity'], 
	       field: config.data['field'],
	       visible: true 
	    });
    
    	if(i != undefined) {
      		this.store.insert(i, record); 
    	} else {
      		this.store.add(record); 
    	}
	}
	
	
	, getRowsAsJSONParams: function() {
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
			jsonStr += 	'"visible" : ' + tmpRec.data['visible'] + '';	
			jsonStr += '}';	
		}
		jsonStr += ']';
		
		return jsonStr;
	}
	
	, deleteGrid: function() {
		this.grid.store.removeAll();
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
	           {name: 'funct'},
	           {name: 'field'},
	           {name: 'entity'},
	           {name: 'alias'},
	           {name: 'order'},
	           {name: 'group'},
	           {name: 'visible'},
	           {name: 'del'}          
	        ]
		});
		
		this.Record = Ext.data.Record.create([
		      {name: 'id', type: 'string'},
		      {name: 'funct', type: 'string'},
		      {name: 'field', type: 'string'},
		      {name: 'entity', type: 'string'},
		      {name: 'alias', type: 'string'},
		      {name: 'order', type: 'string'},
		      {name: 'group', type: 'string'},
		      {name: 'visible', type: 'bool'},
		      {name: 'filter', type: 'string'},
		      {name: 'del', type: 'string'}
		]); 
	}
	
	
	, initSelectionModel: function(config) {
		this.sm = new Ext.grid.RowSelectionModel();
	}
	
	, initColumnModel: function(config) {
		// check-columns
	    var visibleCheckColumn = new Ext.grid.CheckColumn({
	       header: LN('sbi.qbe.selectgridpanel.headers.visible'),
	       dataIndex: 'visible'
	       //width: 55
	    });
	    
	    var groupCheckColumn = new Ext.grid.CheckColumn({
	       header:  LN('sbi.qbe.selectgridpanel.headers.group'),
	       dataIndex: 'group'
	       //width: 55
	    });
	    
	    // button-columns
	    var delButtonColumn = new Ext.grid.ButtonColumn({
	       header:  LN('sbi.qbe.selectgridpanel.headers.delete'),
	       dataIndex: 'delete',
	       imgSrc: '../img/querybuilder/delete.gif',
	       clickHandler:function(e, t){
	          var index = this.grid.getView().findRowIndex(t);
	          var record = this.grid.store.getAt(index);
	          this.grid.store.remove(record);
	       }
	       //width: 55
	     });
	     
	    var filterButtonColumn = new Ext.grid.ButtonColumn({
		   header:  LN('sbi.qbe.selectgridpanel.headers.filter'),
		   dataIndex: 'filter',
		   imgSrc: '../img/querybuilder/filter.png',
		      
		   clickHandler:function(e, t){
		          var index = this.grid.getView().findRowIndex(t);
		          var record = this.grid.store.getAt(index);
		          this.grid.fireEvent('actionrequest', this, 'filter', record);
		   }
		      
		   //width: 55
		});
		
	    this.cm = new Ext.grid.ColumnModel([
		     new Ext.grid.RowNumberer(),
		     {
		    	 header: LN('sbi.qbe.selectgridpanel.headers.entity'),
		    	 dataIndex: 'entity'
		    	 //width: 75
		     }, {
		    	 id:'field',
		         header: LN('sbi.qbe.selectgridpanel.headers.field'),
		         dataIndex: 'field'
		         //width: 75
		     }, {
		         header: LN('sbi.qbe.selectgridpanel.headers.alias'),
		         dataIndex: 'alias',
		         //width: 75
		         editor: new Ext.form.TextField({
		        	 allowBlank: true
		         })
		     }, {
		    	 header: LN('sbi.qbe.selectgridpanel.headers.function'),
		         dataIndex: 'funct',
		         //width: 75
		         editor: new Ext.form.ComboBox({
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
		     }, {
		    	 header: LN('sbi.qbe.selectgridpanel.headers.order'),
		         dataIndex: 'order',
		         //width: 75
		         editor: new Ext.form.ComboBox({
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
		     },
		     groupCheckColumn, 
		     visibleCheckColumn,
		     filterButtonColumn,
		     delButtonColumn
	     ]);	
	    
	    this.plgins = [visibleCheckColumn, groupCheckColumn, delButtonColumn, filterButtonColumn];
	}
	
	, initToolbar: function(config) {
		this.toolbar = new Ext.Toolbar({
			items: [{
	            text: LN('sbi.qbe.selectgridpanel.buttons.text.add'),
	            tooltip: LN('sbi.qbe.selectgridpanel.buttons.tt.add'),
	            iconCls:'option'
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
 						fn: this.deleteGrid,
 						scope: this
 					}
	            }
	        }]
		});
	}

	, initGrid: function(config) {
		
	     // create the Grid
		 this.grid = new Ext.grid.EditorGridPanel({
			 //id: 'select-grid',
			    title: LN('sbi.qbe.selectgridpanel.title'),   
			 	store: this.store,
		        cm: this.cm,  
		        sm : this.sm,
		        tbar: this.toolbar,
		        
		        clicksToEdit:2,
		        plugins: this.plgins,
		        		        
		        height: 350,
		        frame: true,
		        border:true,  
		        style:'padding:10px',
		        iconCls:'icon-grid',
		        collapsible:true,
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
});