qx.Class.define("qooxdoo.ui.form.InputField",
{
	type : "abstract"
	, extend : qx.ui.container.Composite,

  	/*
  	*****************************************************************************
     CONSTRUCTOR
  	*****************************************************************************
  	*/
  	/* InputField is a class that is extended by all the possible input fields (textFields, comboboxes, flagboxes ...)
  	   It constructs a label container and a field container. The label is constructed here, while the field is constructed in the class that extends InputField */

	construct : function(config) {
		var labelContainer;
		var fieldContainer;
		
		this.base(arguments, new qx.ui.layout.HBox);
    	
		var defaultConfig = {
				top: 0,
        		left: 10,
        		maxLength:100,        		
        		labelwidth: 100,
        		mandatory: false,
        		'readOnly': false
		}
		
		config = qooxdoo.commons.CoreUtils.apply(defaultConfig, config);
		
		
		//alert(config.toSource());
		
		if(config.invisibleLabel){
				
		}
		else{	// add label to a container in order to set left and top
				this._createLabel(config);
				labelContainer = new qx.ui.container.Composite(new qx.ui.layout.Basic);
    			labelContainer.add(this._label, {top: config.top, left: config.left + 10});
    			this.add(labelContainer);
		}

    	this._createField(config);  	 
    	fieldContainer = new qx.ui.container.Composite(new qx.ui.layout.Basic);
    	fieldContainer.add(this._field, {top: config.top, left: config.left + 10});
  	
    	this.add(fieldContainer); 
    	
    	
    	
    	if(config.mandatory) {
    		var markConfig = {
        		content : '*'
        		, top : config.top
        		, left : config.left + 5 
        		, width: 20
    		};
    		var mandatoryMarker = new qx.ui.basic.Label();
    		mandatoryMarker.set({content: markConfig.content, width: markConfig.width});  
    		var markContainer = new qx.ui.container.Composite(new qx.ui.layout.Basic);
        	markContainer.add(mandatoryMarker, {top: markConfig.top, left: markConfig.left});
    		this.add(markContainer);
    	}
    	
    	if(config.visible != undefined){
			if(config.visible){
				this.setVisibility("visible");
			}
			else{
				this.setVisibility("excluded");
			}
    	}
    	
    	
  	},

  	members :
  	{
  		_label: null
  		, _field: null
  		, isInputField: true
  		
    	, getData: function() {
  			// abstract method
  		}
  	
  		, setData: function(data) {
  			// abstract method
  		}
  		
  		
  		// public methods
  		
  		
  		
  		// private methods
  		, _createLabel : function( config ) {
  			
  			var defaultConfig = {
        		text: '',
        		top : 0,
        		left : 10,
        		width: 200,
        		height: 20,
        		labelwidth: 100     		
        	};
  			config = qooxdoo.commons.CoreUtils.apply(defaultConfig, config);
  			
        	this._label = new qx.ui.basic.Label();
        	this._label.set({content: config.text, width: config.labelwidth, height: config.height, left : config.left});        	

        }
  		
  		, _createField: function(config) {
  			// abstract method
  			alert('xxx');
  		}
  	}
});