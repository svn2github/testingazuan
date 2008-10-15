qx.Class.define("spagobi.ui.custom.FunctionalityTreeSubClass", {

  extend : qx.ui.layout.VerticalBoxLayout,
  
  construct : function()  
  {
  	
	  this.base(arguments);    	
	  this.setWidth("100%");
	  this.setHeight("100%");
	//  this.setBackgroundColor('green');
	  
	  this.createToolbar();
	  
	  this._textfield1 = this.createTextField({
								        		type: 'text',
								        		dataIndex: 'label',
								        		text: 'Label',
								        		labelwidth: 100,
								        		mandatory: true	
								        	});
     // this.setUserData('label',_textfield1); //required if local variable used for constuctor
        	
	  this._textfield2 = this.createTextField({
								        		type: 'text',
								        		dataIndex: 'name',
								        		text: 'Name',
								        		labelwidth: 100,
								        		mandatory: true	
								        	});
      
      this._textfield3 = this.createTextField({
								        		type: 'text',
								        		dataIndex: 'description',
								        		text: 'Description',
								        		labelwidth: 100,
								        		mandatory: false	
								        	});
	 
	  this.CreateTableWithCheckbox();
  },
  
  members :
  {
  		 newTable : undefined,
  		 columnIds : [],
     	 columnNames : {},
  		 _textfield1 : undefined,
  		 _textfield2 : undefined,
  		 _textfield3 : undefined,
  		 
	  	  createToolbar : function(){
	  	
		  var tb = new qx.ui.toolbar.ToolBar;
		  
		  var createButton = new qx.ui.toolbar.Button("", "spagobi/img/spagobi/test/create.png");
		  var NB = new qx.ui.popup.ToolTip("New");
		  createButton.setToolTip(NB);
		  
		  var saveButton = new qx.ui.toolbar.Button("", "spagobi/img/spagobi/test/save.png");
		  var SB = new qx.ui.popup.ToolTip("Save");
		  saveButton.setToolTip(SB);
		  
		  saveButton.addEventListener("execute", this.ShowDetails,this);
		  
		  var deleteButton = new qx.ui.toolbar.Button("", "spagobi/img/spagobi/test/delete.png");
		  var SD = new qx.ui.popup.ToolTip("Delete");
		  deleteButton.setToolTip(SD);
		  
		  
		  var moveUpButton = new qx.ui.toolbar.Button("", "spagobi/img/spagobi/test/go-up.png");
		  var moveUpToolTip = new qx.ui.popup.ToolTip("Move up");
		  moveUpButton.setToolTip(moveUpToolTip);
		  
		  var moveDownButton = new qx.ui.toolbar.Button("", "spagobi/img/spagobi/test/go-down.png");
		  var moveDownToolTip = new qx.ui.popup.ToolTip("Move Down");
		  moveDownButton.setToolTip(moveDownToolTip);	
		  	
		  tb.add(createButton,saveButton,deleteButton,moveUpButton,moveDownButton);
		  
		  tb.setUserData('create',createButton);
		  tb.setUserData('save',saveButton);
		  tb.setUserData('delete',deleteButton);
		  tb.setUserData('moveUp',moveUpButton);
		  tb.setUserData('moveDown',moveDownButton);
		  
		  this.add(tb);
		  this.setUserData('toolBar', tb);
		  // Button event listeners in parent class - FunctionalClassDummy.js
     },
  		  
		
  		  createTextField : function(config){
  		
  		  var textfield = spagobi.commons.WidgetUtils.createInputTextField(config);
  		  //textfield.setTop(20);
  		  this.add(textfield);
  		  
  		  return textfield;  // ask AMIT about the return statement!!
   	 },
   	 
   	 	  CreateTableWithCheckbox : function (){
   	 	  
   	 	  var tableData = spagobi.app.data.DataService.loadFunctinalitiesRecords();
   	 	  
   	 	  this.newTable = new spagobi.ui.Table(this, tableData);
   	 	  this.newTable.setWidth('100%');
		  this.newTable.setHeight('1*');
		  this.add(this.newTable);

    	  
	      /*
	      var b = new qx.ui.form.Button("Check");
	      b.setLeft(100);
	      b.setTop(100);
	      b.addEventListener("execute", function(e){
	      alert(this.getLeft() + "," + atom0.getLeft() + ","+ newTable.getLeft());
	      alert(this.getWidth() + "," + atom0.getWidth() + "," + newTable.getWidth());	
	      },this);
	      b.addToDocument();
	      */ 
   	 },
   	 
   	 	propertyCellEditorFactoryFunc : function (cellInfo)
   	 	
   	 	{
  			return new qx.ui.table.celleditor.CheckBox;
  		},
   	 	
   	 	propertyCellRendererFactoryFunc : function (cellInfo)
	    {
	      	return new qx.ui.table.cellrenderer.Boolean;
	    },
   	 	
   	 	_onCellCilck : function(e){
	
			if ( ! e instanceof qx.event.type.DataEvent){
				return;
			}
			
  			//var event_data = new qx.ui.table.pane.CellEvent(null,null,event);
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
  		  
      },
   	 	
   	 	ShowDetails: function () {
		
				alert (this.newTable.getUpdatedData());
			
    },
   	 	
   	 
   	 	setData: function(config){
   	 	
   	 	//this.getUserData('label');
   	 	var txt1 = this._textfield1.getUserData('field');
   	 	txt1.setValue(config.label);
   	 	
   	 	var txt2 = this._textfield2.getUserData('field');
   	 	txt2.setValue(config.name);
   	 	
   	 	var txt3 = this._textfield3.getUserData('field');
   	 	txt3.setValue(config.desc);
   	 }
   	 
   	 
  	
  }
  
});