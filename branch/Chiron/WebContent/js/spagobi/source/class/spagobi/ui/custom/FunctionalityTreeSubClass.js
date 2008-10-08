qx.Class.define("spagobi.ui.custom.FunctionalityTreeSubClass", {

  extend : qx.ui.layout.VerticalBoxLayout,
  
  construct : function()  
  {
  	
	  this.base(arguments);    	
	  this.setWidth("100%");
	  this.setHeight("100%");
	  
	  this.createToolbar();
	  
	  this.createTextField({
        		type: 'text',
        		dataIndex: 'label',
        		text: 'Label',
        		labelwidth: 100,
        		mandatory: true	
        	});
	  this.createTextField({
        		type: 'text',
        		dataIndex: 'name',
        		text: 'Name',
        		labelwidth: 100,
        		mandatory: true	
        	});
      
      this.createTextField({
        		type: 'text',
        		dataIndex: 'description',
        		text: 'Description',
        		labelwidth: 100,
        		mandatory: false	
        	});
	 
	 // this.CreateTableWithCheckbox();
  },
  
  members :
  {
	  	  createToolbar : function(){
	  	
		  var tb = new qx.ui.toolbar.ToolBar;
		  
		  var saveButton = new qx.ui.toolbar.Button("", "spagobi/img/spagobi/test/save.png");
		  var SB = new qx.ui.popup.ToolTip("Save");
		  saveButton.setToolTip(SB);
		  var deleteButton = new qx.ui.toolbar.Button("", "spagobi/img/spagobi/test/delete.png");
		  var SD = new qx.ui.popup.ToolTip("Delete");
		  deleteButton.setToolTip(SD);
		  var createButton = new qx.ui.toolbar.Button("", "spagobi/img/spagobi/test/create.png");
		  var NB = new qx.ui.popup.ToolTip("New");
		  createButton.setToolTip(NB);
		  	
		  	
		  tb.add(createButton,saveButton,deleteButton);
		  
		  this.add(tb);	
     },
  
  		  createTextField : function(config){
  		
  		  var textfield = spagobi.commons.WidgetUtils.createInputTextField(config);
  		  textfield.setTop(20);
  		  this.add(textfield);
   	 },
   	 
   	 	  CreateTableWithCheckbox : function (){
   	 	  
   	 	  var tableData = spagobi.app.data.DataService.loadFunctinalitiesRecords();
   	 	  var newTable = new spagobi.ui.Table(this, tableData);
   	 	  newTable.setTop(20);
   	 	  this.add( newTable );
   	 	  /*var atom0 = new qx.ui.basic.Atom();
	      atom0.setWidth('100%'); 
	      atom0.setHeight('1*');
	      atom0.setBackgroundColor('white');
	      atom0.add( newTable );
	      this.add( atom0 );
	     */
   	 	  	
   	 },
   	 
   	 	selectDataObject: function(dataObject) {
   	 	
   	 }
   	 
   	 
  	
  }
  
});