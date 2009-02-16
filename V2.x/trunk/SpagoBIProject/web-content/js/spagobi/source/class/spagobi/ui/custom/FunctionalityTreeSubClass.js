qx.Class.define("spagobi.ui.custom.FunctionalityTreeSubClass", {

  extend : qx.ui.core.Widget,//qx.ui.container.Composite,//qx.legacy.ui.layout.VerticalBoxLayout,
  
  construct : function()  
  {
  	
	  this.base(arguments);  
	  var layout = new qx.ui.layout.VBox();
	  this._setLayout(layout);
//    this.setLayout(new qx.ui.layout.VBox);  	
//	  this.setWidth("100%");
//	  this.setHeight("100%");
	  
	 this.createToolbar();
	
	 this.createDummyLabel(); 
	
	 this._textfield1 = this.createTextField({
								        		type: 'text',
								        		dataIndex: 'label',
								        		text: 'Label',
								        		labelwidth: 100,
								        		mandatory: true	
								        	});
     this.setUserData('label',this._textfield1);
      	
	 this._textfield2 = this.createTextField({
								        		type: 'text',
								        		dataIndex: 'name',
								        		text: 'Name',
								        		labelwidth: 100,
								        		mandatory: true	
								        	});
      this.setUserData('name',this._textfield2);
      
      this._textfield3 = this.createTextField({
								        		type: 'text',
								        		dataIndex: 'description',
								        		text: 'Description',
								        		labelwidth: 100,
								        		mandatory: false	
								        	});
	  this.setUserData('description',this._textfield3);
	  
	  this.createDummyLabel(); 
	  
	  this._table = this.CreateTableWithCheckbox();
	  this.setUserData('table',this._table);
	  
  },
  
  members : 
  {
  		 _textfield1 : undefined,
  		 _textfield2 : undefined,
  		 _textfield3 : undefined,
  		 _table 	 : undefined,
  		 
	  	  createToolbar : function(){
	  	
		  var formBar = new qx.ui.container.Composite(new qx.ui.layout.HBox);
		//  var tb = new qx.ui.toolbar.ToolBar;
		  
		   var createButton = new qx.ui.toolbar.Button("", qx.util.AliasManager.getInstance().resolve('spagobi/img/spagobi/test/create.png'));
		  var NB = new qx.ui.tooltip.ToolTip("New");//qx.ui.popup.ToolTip("New"); Replaced
		  createButton.setToolTip(NB);
		  
		  var saveButton = new qx.ui.toolbar.Button("", qx.util.AliasManager.getInstance().resolve('spagobi/img/spagobi/test/save.png'));
		  var SB = new qx.ui.tooltip.ToolTip("Save"); // Replaced
		  saveButton.setToolTip(SB);
		  
		  var deleteButton = new qx.ui.toolbar.Button("", qx.util.AliasManager.getInstance().resolve('spagobi/img/spagobi/test/delete.png'));
		  var SD = new qx.ui.tooltip.ToolTip("Delete");// Replaced
		  deleteButton.setToolTip(SD);
		  
		  
		  var moveUpButton = new qx.ui.toolbar.Button("", qx.util.AliasManager.getInstance().resolve('spagobi/img/spagobi/test/go-up.png'));
		  var moveUpToolTip = new qx.ui.tooltip.ToolTip("Move up");// Replaced
		  moveUpButton.setToolTip(moveUpToolTip);
		  
		  var moveDownButton = new qx.ui.toolbar.Button("", qx.util.AliasManager.getInstance().resolve('spagobi/img/spagobi/test/go-down.png'));
		  var moveDownToolTip = new qx.ui.tooltip.ToolTip("Move Down");// Replaced
		  moveDownButton.setToolTip(moveDownToolTip);	
		  	
		  var clearAllButton = new qx.ui.toolbar.Button("", qx.util.AliasManager.getInstance().resolve('spagobi/img/spagobi/test/reset.gif'));
		  var clearAllToolTip = new qx.ui.tooltip.ToolTip("Clear all Data");// Replaced
		  clearAllButton.setToolTip(clearAllToolTip);
		  
		  var formBarArray = [createButton, saveButton, deleteButton,moveUpButton,moveDownButton,clearAllButton];//since add() adds only 1 widget at a time
	    
	    for(var i=0; i<formBarArray.length; i++){
	   		formBar.add(formBarArray[i]);
	   		//formBarManager.add(formBarArray[i]);
	    }	
		 // tb.add(createButton,saveButton,deleteButton,moveUpButton,moveDownButton,clearAllButton);
		  
		  formBarArray[0].setUserData('create',createButton);
		  formBarArray[1].setUserData('save',saveButton);
		  formBarArray[2].setUserData('delete',deleteButton);
		  formBarArray[3].setUserData('moveUp',moveUpButton);
		  formBarArray[4].setUserData('moveDown',moveDownButton);
		  formBarArray[5].setUserData('clearAll',clearAllButton);
		  
		  this.setUserData('toolBar', formBarArray);
		  this._add(formBar);
		//  this.setUserData('toolBar', tb);
		  // Button event listeners in parent class - FunctionalClassDummy.js
     },
  
  		  createTextField : function(config){
  		
  		  var textfield = spagobi.commons.WidgetUtils.createInputTextField(config);
  		  //textfield.setTop(20);
  		  this._add(textfield);
  		  
  		  return textfield;
   	 },
   	 
   	 	  createDummyLabel: function(){
   	 	  	var space = spagobi.commons.WidgetUtils.createLabel(" ");
  		  //textfield.setTop(20);
  		  this._add(space);
   	 	  	
   	 },
   	 	  
   	 	  CreateTableWithCheckbox : function (){
   	 	  
   	 	  var tableData = spagobi.app.data.DataService.loadFunctionalitiesRecords();
   	 	  var newTable = new spagobi.ui.Table(this, tableData);
   	 	  //newTable.setTop(20);
   	 	  //this.add( newTable );
   	 	  /*
   	 	  var atom0 = new qx.legacy.ui.basic.Atom();
	      atom0.setWidth('100%'); 
	      atom0.setHeight('1*');
	      atom0.setBackgroundColor('white');
	      atom0.add( newTable );
	      this.add( atom0 );
	      */
	      
	    //  newTable.setWidth('100%');
    	//  newTable.setHeight(343);
    	  this._add(newTable,{flex:1});
    	  
    	  //alert(newTable.getDataRowRenderer());    	  
    	  return newTable;
    	  
   	 },
   	 
   	 	selectDataObject: function(dataObject) {
   	 	
   	 },
   	 
   	 resetOldData: function(){//(/*txt1,txt2,txt3,tableModel,tableData*/){
   	 	//if(txt1 == undefined){
	   	 	var txt1 = this._textfield1.getUserData('field');
	  	 	var txt2 = this._textfield2.getUserData('field');
	   	 	var txt3 = this._textfield3.getUserData('field');
	   	 	
	   	 	
	 //  	 	alert(txt1);
	   	 	var tableModel = this._table.getTableModel();
	 		var tableData = tableModel.getData();
	 		
	 		var tableRowCount = tableData.length;
   	 	//}
   	 	
   	 	txt1.getChildren()[0].setValue('');
   	 	txt2.getChildren()[0].setValue('');
   	 	txt3.getChildren()[0].setValue('');
   	 	for(var i=0; i<tableRowCount; i++){
 			
 			if(tableData[i][2]){
 				tableData[i][2] = false;
 			}	
 			if(tableData[i][3]){
 				tableData[i][3] = false;
 			}	
 			if(tableData[i][4]){
 				tableData[i][4] = false;	
 			}
 		}
 		tableModel.setData(tableData);
  		
  		
   	 },
   	 
   	 setData: function(config){
   	 	
   	 	var txt1 = this._textfield1.getUserData('field');
   	 	var txt2 = this._textfield2.getUserData('field');
   	 	var txt3 = this._textfield3.getUserData('field');
   	 	/*
   	 	//Reset original Data
   	 	txt1.setValue('');
   	 	txt2.setValue('');
   	 	txt3.setValue('');
   	 	*/
   	 	
   	 	//var rowcount = this._table.getTableModel().getRowCount();
   	 		
   	 	var obj1 = txt1.getChildren()[0];	
   	 	var obj2 = txt2.getChildren()[0];
   	 	var obj3 = txt3.getChildren()[0];
 		
 		var tableModel = this._table.getTableModel();
 		var tableData = tableModel.getData();
 		
 		var tableRowCount = tableData.length;
 		/*   	 		
 		//Reset old Data
 		for(var i=0; i<tableRowCount; i++){
 			
 			if(tableData[i][2]){
 				tableData[i][2] = false;
 			}	
 			if(tableData[i][3]){
 				tableData[i][3] = false;
 			}	
 			if(tableData[i][4]){
 				tableData[i][4] = false;	
 			}
 		}
 		tableModel.setData(tableData);
 		*/
 		
 		this.resetOldData();//(obj1,obj2,obj3,tableModel,tableData);
 	//	this.resetOldData(txt1,txt2,txt3,tableModel,tableData);
 		
 		if(config.tree.getSelectedItem() == config.tree.getRoot()){		// If Root Node
  			return;
  		}
   	 	
 		//Set Current Data
   	 	obj1.setValue(config.data.label);
   	 	obj2.setValue(config.data.name);
   	 	obj3.setValue(config.data.desc);
   	 	
   	 		//Set new data
   	 		if(config.data.func != undefined){
   	 			
   	 			var roleCount = config.data.func.length;
   	 			for(var j=0; j<roleCount; j++){
	   	 			for(var i=0; i<tableRowCount; i++){
	   	 				if(tableData[i][0] == config.data.func[j].role){
	   	 					tableModel.setValue(2,i,config.data.func[j].dev);
	   	 					tableModel.setValue(3,i,config.data.func[j].test);
	   	 					tableModel.setValue(4,i,config.data.func[j].exe);
	   	 					break;
	   	 				}
	   	 			}
	   	 		} // end for
   	 	} //end if
   	 },
   	 
   	 getData: function(){
   	 	
   	 	//this._table.getDataRowRenderer().setHighlightFocusRow(false);
   	 	
   	 	var info = {};
   	 	
   	 	var nodeLabel = this._textfield1.getUserData('field');
    	var nodeName  = this._textfield2.getUserData('field');
    	var nodeDesc  = this._textfield3.getUserData('field');
    	
    	info.label = nodeLabel.getChildren()[0].getValue();
		info.name = nodeName.getChildren()[0].getValue();
		info.desc = nodeDesc.getChildren()[0].getValue();
		info.func = [];
		
    	var table 	  = this._table;
    	var tableModel = table.getTableModel();
 		var tableData  = tableModel.getData();
 		var tableRowCount = tableData.length;
 		
 		for(var i=0,j=-1; i<tableRowCount; i++){
 			if(tableData[i][2] || tableData[i][3] || tableData[i][4]){
 				j++;
 				info.func[j] = {};
 				info.func[j].role = tableData[i][0];
 				info.func[j].dev  = tableData[i][2];
 				info.func[j].test = tableData[i][3];
 				info.func[j].exe  = tableData[i][4];
 				
 			}
 		}
    	
    	/*
    	alert('elements: '+ info.func.length);
    	var str = '';
    	for(k=0; k<info.func.length; k++){
	    	str+= k+';\n';
	    	for(p in info.func[k]) {
	    		str += p + ': ' + info.func[k][p] + ';\n'
	    	}
	    	
    	}
		alert(str);
		*/
   	 	return info;
   	 }
  	
  }
  
});