/* ************************************************************************

   Copyright:

   License:

   Authors:

************************************************************************ */

/* ************************************************************************

#resource(custom.image:image)

// List all static resources that should be copied into the build version,
// if the resource filter option is enabled (default: disabled)
#embed(qx.icontheme/32/status/dialog-information.png)
#embed(custom.image/test.png)

************************************************************************ */

/**
 * Your custom application
 */
qx.Class.define("spagobi.test.TestApplication",
{
  extend : qx.application.Gui,

  /*
  *****************************************************************************
     SETTINGS
  *****************************************************************************
  */

  settings : {
    "spagobi.imageUri" : "../img"
  },


  /*
  *****************************************************************************
     MEMBERS
  *****************************************************************************
  */

  members :
  {
    form: null,
    
    main : function()
    {
		this.base(arguments);
      
        // Current document on screen
        var d = qx.ui.core.ClientDocument.getInstance(); 
       
       	this.form = this.createForm();
       	
       	this.form.setData({
        	id: '135',
        	"label": 'JASPER',
        	name: 'JasperReport Engine',
        	description: 'Compatible with JasperReport engine v3.1',
        	documentType: 'Map',
        	engineType: 'External',
        	useDataSet: false,
        	useDataSource: true,
        	dataSource: 'geo',
        	"class": '',
        	url: 'http://localhost:8080/SpagoBIQbeEngine/AdapterHTTP?ACTION_NAME=START_ACTION',
        	driver: 'it.eng.spagobi.engines.drivers.QbeDriver'        	        	
        });
       	
       	       
        var buttonView = new qx.ui.pageview.buttonview.ButtonView;
        buttonView.setEdge(20);
        
        var title = spagobi.commons.WidgetUtils.createLabel({
        	text : 'Engine Details',
        	top : 15,
        	left : 10,
        	width: 80 
        });	    
	    buttonView.getBar().add(title, this.createSaveButton(), this.createBackButton());
	    
	    var pageTabButton = new qx.ui.pageview.buttonview.Button("", "");
        pageTabButton.setDisplay(false);        
        pageTabButton.setChecked(true);  		
  		var page = new qx.ui.pageview.buttonview.Page( pageTabButton );
        page.add( this.form );  		 
  		buttonView.getPane().add( page );
              
        d.add( buttonView );      
        
        //this.saveData();
    },
    
    saveData: function() {
    	var dataObject = this.form.getData();
    	var prettyPrint = '';
    	for(prop in dataObject) {
    		prettyPrint += prop + ': ' + dataObject[prop] + ";\n";
    	}
        alert( prettyPrint );
    },
    
    createSaveButton : function() {
  		// Save button  
        var saveButton = new qx.ui.form.Button("", "icon/32/devices/video-display.png");
        saveButton.setLeft(800);
        
        var saveTooltip = new qx.ui.popup.ToolTip("Save","");
        saveButton.setToolTip(saveTooltip);
        saveTooltip.setShowInterval(10);      
            
            
        saveButton.addEventListener("execute", this.saveData, this);
        
        return saveButton;
  	},
  	
  	createBackButton : function() {
  		// Back Button
        var backButton = new qx.ui.form.Button("", "icon/32/apps/graphics-snapshot.png");
        backButton.setLeft(810);
        
        var backTooltip = new qx.ui.popup.ToolTip("Back","");
        backButton.setToolTip(backTooltip);
        backTooltip.setShowInterval(10);
        
        backButton.addEventListener("execute", function(){alert('go back')});
        
        return backButton;
  	},
    
    
    createForm: function() {
    	var newForm;
    	
    	var documentTypeChangeValueHandler = function(e) {
        	if( newForm && newForm.getInputField('url') ) {
        		if (e.getValue()=="Internal") {
					newForm.getInputField('url').setDisplay(false);
					newForm.getInputField('driver').setDisplay(false);
					newForm.getInputField('class').setDisplay(true);
				} else {
					newForm.getInputField('url').setDisplay(true);
					newForm.getInputField('driver').setDisplay(true);
					newForm.getInputField('class').setDisplay(false);
				}
        	}
        };    
        
        
        var useDataSourceChangeCheckedHandler = function(e) {
        	if( newForm && newForm.getInputField('dataSource') ) {
        		newForm.getInputField('dataSource').setDisplay( e.getValue() );
        	}
        }  
        
        
        var newForm = new spagobi.ui.Form([
        	{
        		type: 'text',
        		dataIndex: 'label',
        		text: 'Label',
        		mandatory: true	
        	}, {
        		type: 'text',
        		dataIndex: 'name',
        		text: 'Name',
        		mandatory: true	
        	}, {
        		type: 'text',
        		dataIndex: 'description',
        		text: 'Description',
        		mandatory: false		
        	}, {
        		type: 'combo',
        		dataIndex: 'documentType',
        		text: 'Document type',
        		items: ["Report","Map"]		
        	}, {
        		type: 'combo',
        		dataIndex: 'engineType',
        		text: 'Engine type',
        		items: ["Internal","External"],
	        	listeners: [
	        		{
	        			event: 'changeValue',
	        			handler: documentTypeChangeValueHandler
	        		}        		
        		]
        	}, {
        		type: 'check',
        		dataIndex: 'useDataSet',
        		text: 'Use Data Set',
        		checked: false	
        	}, {
        		type: 'check',
        		dataIndex: 'useDataSource',
        		text: 'Use Data Source',
	        	checked: true,
	        	listeners: [
	        		{
	        			event: 'changeChecked',
	        			handler: useDataSourceChangeCheckedHandler
	        		} 
				]
        	}, {
        		type: 'combo',
        		dataIndex: 'dataSource',
        		text: 'Data Source',
        		items: ["foodmart","geo", "spagobi"]
        	}, {
        		type: 'text',
        		dataIndex: 'class',
        		text: 'Class',
        		mandatory: true			
        	}, {
        		type: 'text',
        		dataIndex: 'url',
        		text: 'Url',
        		mandatory: true			
        	}, {
        		type: 'text',
        		dataIndex: 'driver',
        		text: 'Driver Name',
        		mandatory: true			
        	}
        ]);
        return newForm;
    },
    
    


    /**
     * TODOC
     *
     * @type member
     */
    close : function()
    {
      this.base(arguments);

      // Prompt user
      // return "Do you really want to close the application?";
    },


    /**
     * TODOC
     *
     * @type member
     */
    terminate : function() {
      this.base(arguments);
    }
  }




  
});