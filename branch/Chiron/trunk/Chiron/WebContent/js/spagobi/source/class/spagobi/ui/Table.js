
/* *

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

* */

/* *
 * @author Andrea Gioia (andrea.gioia@eng.it)
 * @author Amit Rana (amit.rana@eng.it)
 * @author Gaurav Jauhri (gaurav.jauhri@eng.it)
 
 */
 
/**
 * Class to create a list (table) 
 * 
 */ 

/*
#asset(img/spagobi/test/*)
#asset(qx/icon/Oxygen/16/apps/office-calendar.png)
*/

qx.Class.define("spagobi.ui.Table",
{
  //extend : qx.legacy.ui.table.Table,//change
  extend : qx.ui.table.Table,

  /**
   * Constructor to create a list (table)
   * 
   * *Example*
   * <p><code>
   * var obj = new spagobi.ui.custom.MasterDetailsPage();
   * var records = spagobi.app.data.DataService.loadFeatureRecords();
   * var listPage = new spagobi.ui.Table(obj, records );
   * </code>
   * 
   * @param controller Object which is used to set the data of the form
   * @param data Object containing the layout of the list and the data of the list and form
   */	
  construct : function(controller, data)
  {
    // Establish controller link
    this._controller = controller;
    this._data = data;
    // Reset the class member variables so that each button of icon bar shows its resp. table columns
    this.columnIds = [];
    this.columnNames = {};
    
    for(var i = 0; i < this._data.meta.length; i++) {
    	this.columnIds[i] =  this._data.meta[i].dataIndex;
    	this.columnNames[this._data.meta[i].dataIndex] = this._data.meta[i].name;
    }

    // Create table model
    //this._tableModel = new qx.legacy.ui.table.model.Simple();//change
    this._tableModel = new qx.ui.table.model.Simple();
    
    this._tableModel.setColumnIds( this.columnIds );
	this._tableModel.setColumnNamesById( this.columnNames );

	
    // Customize the table column model. We want one that
    // automatically resizes columns.
    this.base(arguments, this._tableModel,
    {
      tableColumnModel : function(obj) {
        //return new qx.legacy.ui.table.columnmodel.Resize(obj);//change
        return new qx.ui.table.columnmodel.Resize(obj);
      }
    });

  this.set({
     // 	flex: 1
     //     height: 150 
      });
   

	
    // Configure columns
    var columnModel = this.getTableColumnModel();
    var resizeBehavior = columnModel.getBehavior();
    
//	alert(this._data.ID);
	
   if (this._data.ID != undefined){
   		this.Identity.dummyId = this._data.ID;
 //   	alert(this.Identity);
    	if (this._data.ID == "ROLES"){
		
	//		var propertyCellRendererFactory = new qx.legacy.ui.table.cellrenderer.Dynamic(this.propertyCellRendererFactoryFunc);//change
			var propertyCellRendererFactory = new qx.ui.table.cellrenderer.Dynamic(this.propertyCellRendererFactoryFunc);
	 	
   		 	//var propertyCellEditorFactory = new qx.legacy.ui.table.celleditor.Dynamic(this.propertyCellEditorFactoryFunc);//change
   		 	var propertyCellEditorFactory = new qx.ui.table.celleditor.Dynamic(this.propertyCellEditorFactoryFunc);

			for(i=0; i<this._data.columns.length; i++){
				columnModel.setDataCellRenderer(this._data.columns[i], propertyCellRendererFactory);
				columnModel.setCellEditorFactory(this._data.columns[i], propertyCellEditorFactory);
			}
			
			this.addListener("cellClick",this._onCellClick, this,false );//this.Identity,
			
    }
    
   		else if (this._data.ID == "Scheduler"){
    		
    		var propertyCellRendererFactory = new qx.ui.table.cellrenderer.Dynamic(this.cellRendererFactoryFunction);
   // 		var propertyCellEditorFactory = new qx.ui.table.cellrenderer.Image();//new qx.ui.table.cellrenderer.Dynamic(this.propertyCellEditorFactoryFunc1);
	 		
   		 	//var propertyCellEditorFactory = new qx.legacy.ui.table.celleditor.Dynamic(this.propertyCellEditorFactoryFunc);//change
  // 		 	var propertyCellEditorFactory = new qx.ui.table.celleditor.Dynamic(this.propertyCellEditorFactoryFunc);
			
	//		alert(this._data.columns.length);
			for(i=0; i<this._data.columns.length; i++){
				
	/*			columnModel.setCellRenderer(function(cellInfo){
					if (cellInfo.row == this._data.columns[i])
					    return new qx.ui.table.cellrenderer.Image;
});*/
				columnModel.setDataCellRenderer(this._data.columns[i], propertyCellRendererFactory);
		//		columnModel.setCellEditorFactory(this._data.columns[i], propertyCellEditorFactory);
			}
	//		this.addListener("changeDataRowRenderer",this._onImageClick,this, false);//this._data.ID,
			this.addListener("cellClick",this._onCellClick,this, false);//this._data.ID,
	
	}
	
		else if (this._data.ID == "LINK"){
			
		
		}
			this.setStatusBarVisible(false);
		    this.getDataRowRenderer().setHighlightFocusRow(true);
			this._tableModel.setDataAsMapArray(this._data.rows, true);
    }
	//this.setWidth('100%');//change
    //this.setHeight('100%');//change
    //this.setBorder("inset-thin");//change
    //this.setDecorator(new qx.ui.decoration.Single(null,"solid",null));
    
    //this.setOverflow("auto");//change ... find
    else {
    this.setStatusBarVisible(false);
    this.getDataRowRenderer().setHighlightFocusRow(true);
   // this.getPaneScroller(0).setShowCellFocusIndicator(false);
    
    this._tableModel.setDataAsMapArray(this._data.rows, true);
    
    // Add selection listener
    this.getSelectionModel().addListener("changeSelection", this._onChangeSelection, this);
    
    }
  },

  /**
   * members of the class
   */	
  members :
  {
  	 _data : undefined,		
  	 columnIds : [],
     columnNames : {},
     Identity: {},
     config : {}, 	
     tree1 : undefined,
     nodeLabel: undefined,
     inputField: undefined,
  	 propertyCellEditorFactoryFunc : function (cellInfo) 
  	  {
  			return new qx.ui.table.celleditor.CheckBox;
  	  },
	
	 propertyCellRendererFactoryFunc1 : function (cellInfo)
      {
      		return new qx.ui.table.cellrenderer.Default;
      },
  	 
  	 propertyCellRendererFactoryFunc : function (cellInfo)
      {
        	return new qx.ui.table.cellrenderer.Boolean;
      },
      
     cellRendererFactoryFunction : function (cellInfo)
      {
      	//	alert("Image");
      		return new qx.ui.table.cellrenderer.Image;
      },
  	/**
  	 * Function used to set the data in the list
  	 * 
  	 * *Example*
  	 * <p><code>
  	 * 
  	 * </code>
  	 * 
  	 * @param data Object containing the layout of the list and the data of the list and form
  	 */
    setData: function(data) {
     	
    	var columnIds = [];
    	var columnNames = {};
    	for(var i = 0; i < data.meta.length; i++) {
    		this.columnIds[i] =  data.meta[i].dataIndex;
    		columnNames[data.meta[i].dataIndex] = data.meta[i].name;
    	}
    	
    	this._tableModel.setColumnIds( columnIds );
	    this._tableModel.setColumnNamesById( columnNames );
	    this.getTableModel().setDataAsMapArray(data.rows, true);
        this.getSelectionModel().setSelectionInterval(0, 0);
    },
    
    /**
  	 * Function used to get the data in the list
  	 * 
  	 * *Example*
  	 * <p><code>
  	 * 
  	 * </code>
  	 * 
  	 * @param data Object containing the layout of the list and the data of the list and form
  	 */
  	 
     getUpdatedData: function() {
     		
     		return this._tableModel.getData();
     },
     
     /**
     * TODOC
     *
     * @type member
     * @param e {Event} TODOC
     * @return {void}
     */
     
     
    _onChangeSelection : function(e) {
      	
      var selectedEntry = this.getSelectionModel().getAnchorSelectionIndex();
     
      if (selectedEntry >= 0) {
        var itemData = this.getTableModel().getRowData(selectedEntry);
		 
		 
        			// If this is undefined, the data is not yet ready...
        if (itemData) {
        			//if (this._controller != spagobi.ui.custom.FunctionalityTreeSubClass )
          this._controller.selectDataObject(itemData);
        }
      }
            
    },
    
    _onCellClick : function(e){
    	
    		
	
			//if ( ! e instanceof qx.event.type.DataEvent){//change
			if ( ! e instanceof qx.event.type.Data){
				return;
			}
	
		  if(this.Identity.dummyId=="ROLES"){
  			var colnum = e.getColumn();
  			var romnum = e.getRow();
  			
  			if(typeof(this.getTableModel().getValue(colnum,romnum)) != 'boolean'){
  				return;
  			}
		  			//	alert(event +" ," +colnum +" ,"+ romnum);
		  			//var changedData = event.getData();
		  			//alert (typeof(this.getTableModel().getValue(colnum,romnum)));
			
			if ( this.getTableModel().getValue(colnum,romnum) === true )
			{
					//event.setData(false);// == false;
				this.getTableModel().setValue(colnum,romnum,false);
					//	alert (this.getTableModel().getValue(colnum,romnum));
			}
			else 
			{
					//event.setData(true);// == true;
				this.getTableModel().setValue(colnum,romnum,true);
					//	alert (this.getTableModel().getValue(colnum,romnum));
					//event_data.setData(true);
			}
			
		  }
    
		  
		  else{
		  	
		  	if (e.getColumn() <= 2){
		  	}
		  	else {

				if (e.getColumn() == 4){
					 
					  //var layout = new qx.ui.layout.VBox(20);
					  //var windowWidget = new qx.ui.core.Widget();
					 // windowWidget._setLayout
					  var win = new qx.ui.window.Window("Details Window", "qx/icon/Oxygen/16/apps/office-calendar.png");
				      win.setLayout(new qx.ui.layout.VBox(20));
				      win.setShowStatusbar(true);
				      win.setStatus("Details loaded");
				      //win.setHeight(200);
				      //win.setWidth(200);
				      //win.setMaxWidth(500);
				      win.open();
				      win.setModal(true);
				      //win.setResizable(false);
					  //this.getRoot().add(win);//, {left:500, top:400});  //
				      
				      var textfield1 = spagobi.commons.WidgetUtils.createInputTextField({
								        		type: 'text',
								        		dataIndex: 'name',
								        		text: 'Name',
								        		labelwidth: 100,
								        		mandatory: true	
								        	});
					 win.add(textfield1);
					 
					 var textfield2 = spagobi.commons.WidgetUtils.createInputTextField({
								        		type: 'text',
								        		dataIndex: 'description',
								        		text: 'Description',
								        		labelwidth: 100,
								        		mandatory: false
								        	});
					
					win.add(textfield2);									        

					var box = new qx.ui.container.Composite;
				    box.setLayout(new qx.ui.layout.HBox(10));
				    win.add(box, {flex:1});
					this.tree1 = new spagobi.ui.Tree({root: "Functionalities"});	
  		
  					var node1 = this.tree1.addNode({
		  							name  : "Report",
		  							parent: this.tree1,
		  							//checkBox : true,
		  							data  : {
		  							 			label : 'ReportLabel',
		  							 			name  : 'ReportName',
		  							 			desc  : 'ReportDesc',
		  							 			func  : [
		  							 						{
		  							 							role	: '/admin',
		  							 							dev		: true,
		  							 							test	: true,
		  							 							exe		: true
		  							 							
		  							 						},
		  							 						{
		  							 							role	: '/community/direction',
		  							 							dev		: true,
		  							 							test	: true,
		  							 							exe		: true
		  							 							
		  							 						}
		  							 			]	
		  							 		}
		  							 		
		  					});
		  					
	  				
  		var node2 = this.tree1.addNode({
		  							name  : "OLAP",
		  							parent: this.tree1,
		  							//checkBox : true,
		  							data  : {
		  							 			label : 'OLAPLabel',
		  							 			name  : 'OLAPName',
		  							 			desc  : 'OLAPDesc',
		  							 			func  : [
		  							 						{
		  							 							role	: '/community',
		  							 							dev		: true,
		  							 							test	: true,
		  							 							exe		: true
		  							 							
		  							 						},
		  							 						{
		  							 							role	: '/guest',
		  							 							dev		: true,
		  							 							test	: true,
		  							 							exe		: true
		  							 							
		  							 						}
		  							 			]
		  							 		}
  								});
  		var node3 = this.tree1.addNode({
		  							name  : "myOLAP",
		  							parent: node2,
		  							file : true,
		  							data  : {
		  							 			label : 'myOLAP Label',
		  							 			name  : 'myOLAP Name',
		  							 			desc  : 'myOLAP Desc'
		  							 		}
  								});
  		var node4 = this.tree1.addNode({
		  							name  : "DashBoard",
		  							parent: this.tree1,
		  							//checkBox : true,
		  							data  : {
		  							 			label : 'DashBoardLabel',
		  							 			name  : 'DashBoardName',
		  							 			desc  : 'DashBoardDesc'
		  							 		}	
  								});
  		var node5 = this.tree1.addNode({
		  							name  : "myDashBoardFolder",
		  							parent: node4,
		  							file : true, 
		  							data  : {
		  							 			label : 'myDashBoardFolderLabel',
		  							 			name  : 'myDashBoardFolderName',
		  							 			desc  : 'myDashBoardFolderDesc'
		  							 		}
		  						});
  		var node6 = this.tree1.addNode({
		  							name  : "myDashBoard",
		  							parent: node4,
		  							file : true, 
		  							data  : {
		  							 			label : 'myDashBoard Label',
		  							 			name  : 'myDashBoard Name',
		  							 			desc  : 'myDashBoard Desc'
		  							 		}
  								});
					
				
				
				box.add(this.tree1);
			
			this.tree1.addListener("dblclick",this.treeLabel,this);	//changeSelection
			
				this.config.formList = {
					schedule : "Scheduler",
					val : "Functionalities"
       		/*	type: 'formList',
        		dataIndex: 'features',
        		formList: spagobi.ui.custom.FeatureDetailsForm  */
        	}
				this.inputField = spagobi.commons.WidgetUtils.createInputFormList(this.config);
        	
        	var box1 = new qx.ui.container.Scroll().set({
					    width: 200,
					    height: 200
					  });
        	
			box1.add(this.inputField);
			box.add(box1,{flex:1});
			//box.add(this.inputField);
        	  
        	  
        /*	  
        	  var tabView = new qx.ui.tabview.TabView;
      		  box.add(tabView);//, {flex:1}
      				
      	      var page1 = new qx.ui.tabview.Page('tab-', "qx/icon/Oxygen/16/actions/edit-delete.png");
      		  tabView.add(page1);
      		  page1.setLayout(new qx.ui.layout.Grow())
      				
        	   var textfield3 = spagobi.commons.WidgetUtils.createInputTextField({
								        		type: 'text',
        		dataIndex: 'customer',
        		text: 'Customer',
        		mandatory: false	
								        	});
				
				page1.add(textfield3);
				
				*/
				
				}

		  	}
		  	}
		  	
		 
		  },
		  
		    treeLabel : function(e){
		    	
		//    	alert("1");
		    	var item = this.tree1.getSelectedItem();
		//    	alert(item);
		    	if(item instanceof qx.ui.tree.TreeFile){
		    		this.nodeLabel = item.getLabel();
		    	//	alert("debug 1"+this.nodeLabel);
	//	    		alert(typeof(this.nodeLabel) );
		    		this.inputField.addInstance(this.nodeLabel);
		    //		alert("debug 2"+this.nodeLabel);
		    		
		    	}
		    }
  		  
   
    }
      
      
   /*   _onImageClick : function(e){
      	var colnum = e.getColumn();
  			var romnum = e.getRow();
  			alert(typeof(this.getTableModel().getValue(colnum,romnum)));
      	
      	
      }
      
   */
      
  
});
