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

Ext.ns("Sbi.qbe");

Sbi.qbe.FilterGridDropTarget = function(filterGridPanel, config) {
	
	var c = Ext.apply({
		ddGroup    : 'gridDDGroup',
		copy       : false
	}, config || {});
	
	this.targetPanel = filterGridPanel;
	this.targetGrid = this.targetPanel.grid; 
	this.targetElement = this.targetGrid.getView().el.dom.childNodes[0].childNodes[1];
	
	// constructor
    Sbi.qbe.FilterGridDropTarget.superclass.constructor.call(this, this.targetElement, c);
    
};

Ext.extend(Sbi.qbe.FilterGridDropTarget, Ext.dd.DropTarget, {
    
    services: null
    , targetPanel: null
    , targetGrid: null
    , targetElement: null
   
    , notifyDrop : function(ddSource, e, data){
    	
		// the row index and the column number on whitch the tree node has been dropped on
		var rowIndex;
		var colIndex;

	    if(this.targetGrid.targetRow) {			          
	      rowIndex = this.targetGrid.targetRowIndex;
	      colIndex = this.targetGrid.targetColIndex;		          
	    }
	
	    if(rowIndex == undefined || rowIndex === false) {
	      // append the new row
	      rowIndex = undefined;
	    }   
	    
	    if(colIndex == undefined || colIndex === false) {
				colIndex = undefined;
		} 
	
	    
	    
	  	var sourceObject;
	  	if(ddSource.tree && ddSource.tree.type ===  'datamartstructuretree') {
	    	this.notifyDropFromDatamartStructureTree(ddSource, e, data, rowIndex, colIndex);
	  	} else if(ddSource.tree && ddSource.tree.type ===  'querycataloguetree') {
	    	this.notifyDropFromQueryCatalogueTree(ddSource, e, data, rowIndex, colIndex);		
		} else if(ddSource.grid &&  ddSource.grid.type === 'selectgrid') {
	    	this.notifyDropFromSelectGrid(ddSource, e, data, rowIndex, colIndex);
	  	} else if(ddSource.grid &&  ddSource.grid.type === 'filtergrid') {
	    	this.notifyDropFromFilterGrid(ddSource, e, data, rowIndex, colIndex);
	  	} else {
	    	alert('Source object: unknown');
	  	}        
	}
	
	, notifyDropFromQueryCatalogueTree: function(ddSource, e, data, rowIndex, colIndex) {


		var node;
		
		node = ddSource.dragData.node;        
	
		if(colIndex === 5) {
			var store = this.targetGrid.store;
			var row = store.getAt(rowIndex);
			row.data['otype'] = 'Subquery';
			row.data['odesc'] = node.props.query.name;
			row.data['operand'] = node.id;
			this.targetGrid.store.fireEvent('datachanged', this.targetGrid.store) ;
		} else {
			Ext.Msg.show({
				   title:'Drop target not allowed',
				   msg: 'Subqueries can be dropped only on the "operand" column of an existing filter',
				   buttons: Ext.Msg.OK,
				   icon: Ext.MessageBox.WARNING
			});
		}
	}
	, notifyDropFromDatamartStructureTree: function(ddSource, e, data, rowIndex, colIndex) {
		//alert('Source object: tree');
	
		// the node dragged from tree to grid
		var node;
	
		node = ddSource.dragData.node;        
	
	
		if(node.attributes.field && node.attributes.type == 'field') {
			if(colIndex === 5) {
				var store = this.targetGrid.store;
				var row = store.getAt(rowIndex);
				row.data['otype'] = 'Field Content';
				row.data['odesc'] = ddSource.dragData.node.attributes.entity + ' / ' + ddSource.dragData.node.attributes.field;
				row.data['operand'] = ddSource.dragData.node.id;
				this.targetGrid.store.fireEvent('datachanged', this.targetGrid.store) ;
			} else {
	  			var record = new this.targetPanel.Record({
	  				id: ddSource.dragData.node.id , 
	    			entity: ddSource.dragData.node.attributes.entity , 
	    			field: ddSource.dragData.node.attributes.field  
	  			});
	  			this.targetPanel.addRow(record, rowIndex);
			}
		} else if(node.attributes.attributes.type == 'entity'){
			
			for(var i = 0; i < node.attributes.children.length; i++) {
				if(node.attributes.children[i].attributes.type != 'field') continue;
				
				var record = new this.targetPanel.Record({
	  				id: node.attributes.children[i].id , 
	    			entity: node.attributes.children[i].attributes.entity , 
	    			field: node.attributes.children[i].attributes.field  
	  			});
				this.targetPanel.addRow(record, rowIndex);
			}
		} else {
			alert("Error: unknown node type");
		}
	
		this.targetGrid.getView().refresh();
	}

	, notifyDropFromSelectGrid: function(ddSource, e, data, rowIndex, colIndex) {
		//alert('Source object: select-grid');
		var sm = this.targetGrid.getSelectionModel();
		var ds = this.targetGrid.getStore();
		var ddDs = this.targetGrid.getStore();;
		var rows = sm.getSelections();
	
		
		var rows = ddSource.dragData.selections;  
		if(colIndex === 5) {
			if(rows.length > 1 ) {
				alert('Error: impossible to use as value into a filter a collection of fields');
			}
			var store = this.targetGrid.getStore();
			var row = store.getAt(rowIndex);
			row.data['otype'] = 'Field Content';
			row.data['odesc'] = rows[0].data['entity'] + ' / ' + rows[0].data['field'];
			row.data['operand'] = rows[0].data['id'];
			this.targetGrid.store.fireEvent('datachanged', this.targetGrid.store) ;
		} else {
			rows = rows.sort(function(r1, r2) {
	      		var row1 = ddDs.getById(r1.id);
	      		var row2 = ddDs.getById(r2.id);
	      		return ddDs.indexOf(r2) - ddDs.indexOf(r1);
				});
			if(rowIndex == undefined) {
	  			rows = rows.reverse();
			}
	   
			for (i = 0; i < rows.length; i++) {
	  			if(!this.copy) {
	  				this.targetPanel.addRow( rows[i], rowIndex );
	  			}
			}     
	
			this.targetGrid.getView().refresh();
		}
	}

	, notifyDropFromFilterGrid: function(ddSource, e, data,rowIndex, colIndex) {
		//alert('Source object: filter-grid');
		var sm= this.targetGrid.getSelectionModel();
		var ds = this.targetGrid.getStore();
		var rows = sm.getSelections();

		//alert("B:" + rowIndex); 

		rows = rows.sort(function(r1, r2) {
  		var row1 = ds.getById(r1.id);
  		var row2 = ds.getById(r2.id);
  		return ds.indexOf(r2) - ds.indexOf(r1);
		});
		if(rowIndex == undefined) {
		rows = rows.reverse();
		}

		for (i = 0; i < rows.length; i++) {
	 		var rowData=ds.getById(rows[i].id);
	 		if(!this.copy) {
	    		ds.remove(ds.getById(rows[i].id));
	    		if(rowIndex != undefined) {
	      			ds.insert(rowIndex, rowData);
	    		} else {
	      			ds.add(rowData);
	    		}
	  		}
		}

		this.targetGrid.getView().refresh();        
	}
});