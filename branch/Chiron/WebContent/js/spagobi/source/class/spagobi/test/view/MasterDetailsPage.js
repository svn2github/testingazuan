qx.Class.define("spagobi.test.view.MasterDetailsPage",
{
  extend : qx.ui.splitpane.VerticalSplitPane,

  construct : function(type)
  {
    this.base(arguments, "1*", "2*");
    
    var listPage;
    var detailPage;
    var detailHeader;
    //var detailBody ;
    
    this.setEdge(0);
   	this.setBorder("line-left");
      		
    var records;
    var form;  	
    this._type = type;	
	if(type === 'engine') {
		records = spagobi.test.DataService.loadEngineRecords();
		form = new spagobi.ui.custom.EngineDetailsForm(); 
	} else if(type === 'dataset') {
		records = spagobi.test.DataService.loadDatasetRecords();
		form = new spagobi.ui.custom.DatasetDetailsForm(); 
	} else if(type === 'datasource') {
		records = spagobi.test.DataService.loadDatasourceRecords();
		form = new spagobi.ui.custom.DatasourceDetailsForm(); 
	}
		
		
		
   	// Create list view
   	listPage = new spagobi.test.view.Table(this, records );
   	this.addTop( listPage );
      	
   	// Create detail view
   	detailPage = new qx.ui.pageview.buttonview.ButtonView();
    detailPage.setEdge(0);     
        
    // Create detail view toolbar
    var saveButton = new qx.ui.pageview.buttonview.Button("Save", "spagobi/img/spagobi/test/save.png");
    var deleteButton = new qx.ui.pageview.buttonview.Button("Delete", "spagobi/img/spagobi/test/delete.png");
    var createButton = new qx.ui.pageview.buttonview.Button("New", "spagobi/img/spagobi/test/create.png");                 
    detailPage.getBar().add(createButton, saveButton, deleteButton);   
        
    // Create detail view body
   	this._form = form;       	      	
   	detailHeader = new qx.ui.pageview.buttonview.Button("", "");
    detailHeader.setDisplay(false);        
    detailHeader.setChecked(true);  		
	this.detailBody = new qx.ui.pageview.buttonview.Page( detailHeader );
    this.detailBody.add( this._form );  		 
  	detailPage.getPane().add( this.detailBody );
  		
  	this.addBottom( detailPage );
  },

  members :
  {
    _form : undefined,
    detailBody : undefined,
    
    getForm: function() {
    	return this._form;
    },
    
    selectDataObject: function(dataObject) {
    	//alert(this._form + ' - ' + this._type + " ->\n " + this.printObject(dataObject));
    	this._form.setData(dataObject);
    },
    
    show: function() {
    	//this._form.setVisibility(false);
    	this.detailBody.remove(this._form)
    	//this._form.dispose();
    	if(this._type === 'engine') {
			this._form = new spagobi.ui.custom.EngineDetailsForm(); 
		} else if(this._type === 'dataset') {
			this._form = new spagobi.ui.custom.DatasetDetailsForm(); 
		} else if(this._type === 'datasource') {
			this._form = new spagobi.ui.custom.DatasourceDetailsForm(); 
		}
		this.detailBody.add(this._form);
		if(!this.isVisibility()) {
			this.setVisibility(true);
		}
    },
    
    printObject: function(o) {
    	var str = '';
    	for(p in o) {
    		str += p + ': ' + o[p] + ';\n'
    	}
    	return str;
    }
  }
});