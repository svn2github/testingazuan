/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005 Engineering Ingegneria Informatica S.p.A.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

**/

/**
  * Qbe main script file
  * by Andrea Gioia
  */
 
// reference local blank image
Ext.BLANK_IMAGE_URL = '../js/lib/ext-2.0.1/resources/images/default/s.gif';

 
// create namespace
Ext.namespace('it.eng.spagobi.engines.qbe');
 
// create application
it.eng.spagobi.engines.qbe.app = function() {
    // do NOT access DOM from here; elements don't exist yet
 
    // private variables
    var tabs = [];
    var tabPanel; 
    var query;

 
    // public space
    return {
        // public properties, e.g. strings to translate
        
        // public methods
        init: function() {
            Ext.QuickTips.init();
            
            //Ext.util.CSS.swapStyleSheet('theme', '../js/ext/resources/css/xtheme-gray.css');
            
            
            it.eng.spagobi.engines.qbe.locale.module.init();
            it.eng.spagobi.engines.qbe.locale.module.applyLocale();
           
            tabs[1] = new Sbi.widgets.DataStorePanel();
            tabs[0] = getQueryBuilderPanel(query, tabs[1]);
            //tabs[1] = getQueryResultsPanel();
           
            //alert('debug1');      
            
            // Main (Tabbed) Panel            
            tabPanel = new Ext.TabPanel({
          		region:'center',
          		deferredRender:false,
          		autoScroll: true, 
          		margins:'0 4 4 0',
          		activeTab:0,
          		items: tabs        
            });     
                        
            // Configure viewport
            var viewport = new Ext.Viewport({
              layout:'border',
              items:[tabPanel]}); 
              
           	
           	// ------------------------------------------------------------------
           	// Setup Drop Targets
  	        // ------------------------------------------------------------------
  	
  			var selectGrid = it.eng.spagobi.engines.qbe.querybuilder.selectGrid.app.grid;
  			var filterGrid = it.eng.spagobi.engines.qbe.querybuilder.filterGrid.app.grid;
  	
  	  		// ------------------------------------------------------------------
  			var selectGridDropTargetEl =  selectGrid.getView().el.dom.childNodes[0].childNodes[1];
  			var selectGridDropTarget = new Ext.dd.DropTarget(selectGridDropTargetEl, {
      			ddGroup    : 'gridDDGroup',
				copy       : false,
				
				notifyOver : function(ddSource, e, data){
        			return (ddSource.grid &&  ddSource.grid.id === 'filter-grid')? this.dropNotAllowed : this.dropAllowed;
      			 },
  				
  				notifyDrop : function(ddSource, e, data){
      
       				// the row index on which the tree node has been dropped on
         			var rowIndex;
         
        			if(selectGrid.targetRow) {
          				rowIndex = selectGrid.getView().findRowIndex( selectGrid.targetRow );
        			}
        
        			if(rowIndex == undefined || rowIndex === false) {
          			// append the new row
          				rowIndex = undefined;//selectGrid.getStore().getCount();
        			}   
        
        			//alert("A:" + rowIndex );
          
          			var sourceObject;
		          	if(ddSource.tree) {
		            	this.notifyDropFromTree(ddSource, e, data, rowIndex);
		          	} else if(ddSource.grid &&  ddSource.grid.id === 'select-grid') {
		            	this.notifyDropFromSelectGrid(ddSource, e, data, rowIndex);
		          	} else if(ddSource.grid &&  ddSource.grid.id === 'filter-grid') {
		            	this.notifyDropFromFilterGrid(ddSource, e, data);
		          	} else {
		            	//alert('Source object: unknown');
		          	}        
				}, // enf of notifyDrop
        
      			notifyDropFromTree: function(ddSource, e, data, rowIndex) {
        			//alert('Source object: tree');
        
			        // the node dragged from tree to grid
			        var node;        
        			node = ddSource.dragData.node;             

			        if(node.attributes.field && node.attributes.type == 'field') {
			        	var record = new it.eng.spagobi.engines.qbe.querybuilder.selectGrid.app.Record({
			        		id: ddSource.dragData.node.id , 
			            	entity: ddSource.dragData.node.attributes.entity , 
			            	field: ddSource.dragData.node.attributes.field  
			          	});
			        
			          	it.eng.spagobi.engines.qbe.querybuilder.selectGrid.app.addRow(record, rowIndex);
			          	
			        } else if(node.attributes.attributes.type == 'entity'){
	        			
	        			for(var i = 0; i < node.attributes.children.length; i++) {
	        				if(node.attributes.children[i].attributes.type != 'field') continue;
	        				
	        				var record = new it.eng.spagobi.engines.qbe.querybuilder.filterGrid.app.Record({
		          				id: node.attributes.children[i].id , 
		            			entity: node.attributes.children[i].attributes.entity , 
		            			field: node.attributes.children[i].attributes.field  
		          			});
		          			it.eng.spagobi.engines.qbe.querybuilder.selectGrid.app.addRow(record, rowIndex);
	        			}
			        
			        } else {
			        	alert("Error: unknown node type");
			        	//it.eng.spagobi.engines.qbe.commons.dump(node);
			        }
        
        			selectGrid.getView().refresh();
      			}, // notifyDropFromTree
      
			    notifyDropFromSelectGrid: function(ddSource, e, data, rowIndex) {
			    	//alert('Source object: select-grid');
			        var sm=selectGrid.getSelectionModel();
			        var ds = selectGrid.getStore();
			        var rows=sm.getSelections();
			        
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
			         
			         selectGrid.getView().refresh();
			      }, // notifyDropFromSelectGrid
			      
			      notifyDropFromFilterGrid: function(ddSource, e, data) {
			      	//alert('Source object: filter-grid');
			      } // notifyDropFromFilterGrid  		
			}); 
  	
  	
  	
  	
		  	// ------------------------------------------------------------------
		  	var filterGridDropTargetEl =  filterGrid.getView().el.dom.childNodes[0].childNodes[1];
		  	var filterGridDropTarget = new Ext.dd.DropTarget(filterGridDropTargetEl, {
  				ddGroup    : 'gridDDGroup',
  				copy       : false,
  		
  				notifyDrop : function(ddSource, e, data){
       
       				// the row index and the column number on whitch the tree node has been dropped on
         			var rowIndex;
         			var colIndex;
         
			        if(filterGrid.targetRow) {			          
			          rowIndex = filterGrid.targetRowIndex;
			          colIndex = filterGrid.targetColIndex;
			          //rowIndex = filterGrid.getView().findRowIndex( filterGrid.targetRow );
			          //colIndex = selectGrid.getView().findCellIndex( filterGrid.targetRow );			          
			        }
        
			        if(rowIndex == undefined || rowIndex === false) {
			          // append the new row
			          rowIndex = undefined;
			        }   
			        
			        if(colIndex == undefined || colIndex === false) {
          				colIndex = undefined;
        			} 
        
        			//alert("A:" + rowIndex );
          
          
		          	var sourceObject;
		          	if(ddSource.tree) {
		            	this.notifyDropFromTree(ddSource, e, data, rowIndex, colIndex);
		          	} else if(ddSource.grid &&  ddSource.grid.id === 'select-grid') {
		            	this.notifyDropFromSelectGrid(ddSource, e, data, rowIndex, colIndex);
		          	} else if(ddSource.grid &&  ddSource.grid.id === 'filter-grid') {
		            	this.notifyDropFromFilterGrid(ddSource, e, data, rowIndex, colIndex);
		          	} else {
		            	alert('Source object: unknown');
		          	}        
      			},
        
	      		notifyDropFromTree: function(ddSource, e, data, rowIndex, colIndex) {
	        		//alert('Source object: tree');
	        
	        		// the node dragged from tree to grid
	        		var node;
	        
	        		node = ddSource.dragData.node;        
	          
	
	        		if(node.attributes.field && node.attributes.type == 'field') {
	        			if(colIndex === 5) {
	        				var store = filterGrid.getStore();
	        				var row = store.getAt(rowIndex);
	        				row.data['otype'] = 'Field Content';
	        				row.data['odesc'] = ddSource.dragData.node.attributes.entity + ' / ' + ddSource.dragData.node.attributes.field;
	        				row.data['operand'] = ddSource.dragData.node.id;
	        				filterGrid.store.fireEvent('datachanged', filterGrid.store) ;
	        			} else {
		          			var record = new it.eng.spagobi.engines.qbe.querybuilder.filterGrid.app.Record({
		          				id: ddSource.dragData.node.id , 
		            			entity: ddSource.dragData.node.attributes.entity , 
		            			field: ddSource.dragData.node.attributes.field  
		          			});
		          			it.eng.spagobi.engines.qbe.querybuilder.filterGrid.app.addRow(record, rowIndex);
	        			}
	        		} else if(node.attributes.attributes.type == 'entity'){
	        			
	        			for(var i = 0; i < node.attributes.children.length; i++) {
	        				if(node.attributes.children[i].attributes.type != 'field') continue;
	        				
	        				var record = new it.eng.spagobi.engines.qbe.querybuilder.filterGrid.app.Record({
		          				id: node.attributes.children[i].id , 
		            			entity: node.attributes.children[i].attributes.entity , 
		            			field: node.attributes.children[i].attributes.field  
		          			});
		          			it.eng.spagobi.engines.qbe.querybuilder.filterGrid.app.addRow(record, rowIndex);
	        			}
	        		} else {
	        			alert("Error: unknown node type");
	        		}
	        
	        		filterGrid.getView().refresh();
	      		},
      
	      		notifyDropFromSelectGrid: function(ddSource, e, data, rowIndex, colIndex) {
	        		//alert('Source object: select-grid');
	        		var sm=filterGrid.getSelectionModel();
	        		var ds = filterGrid.getStore();
	        		var ddDs = selectGrid.getStore();;
	        		var rows=sm.getSelections();
	       
	        		
	        		var rows = ddSource.dragData.selections;  
	        		if(colIndex === 5) {
	        			if(rows.length > 1 ) {
	        				alert('Error: impossible to use as value into a filter a collection of fields');
	        			}
	        			var store = filterGrid.getStore();
	        			var row = store.getAt(rowIndex);
	        			row.data['otype'] = 'Field Content';
	        			row.data['odesc'] = rows[0].data['entity'] + ' / ' + rows[0].data['field'];
	        			row.data['operand'] = rows[0].data['id'];
	        			filterGrid.store.fireEvent('datachanged', filterGrid.store) ;
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
		               			it.eng.spagobi.engines.qbe.querybuilder.filterGrid.app.addRow( rows[i], rowIndex );
		          			}
		        		}     
		        
		        		filterGrid.getView().refresh();
	        		}
	        		
	        		
	        		
	      		},
      
	      		notifyDropFromFilterGrid: function(ddSource, e, data,rowIndex, colIndex) {
	        		//alert('Source object: filter-grid');
	       			var sm=filterGrid.getSelectionModel();
	          		var ds = filterGrid.getStore();
	          		var rows=sm.getSelections();
	       
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
            
            		filterGrid.getView().refresh();        
      			}
	  		}); 	
	    	// ------------------------------------------------------------------                   
        },
        
        setQuery: function(q) {
        	query = q;
        },
        
        activateTab:function() {
        	tabPanel.activate(tabs[1]);
        }
    };
}(); // end of app
 
// end of file
