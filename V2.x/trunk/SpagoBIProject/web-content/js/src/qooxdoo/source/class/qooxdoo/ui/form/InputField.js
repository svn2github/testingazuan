qx.Class.define("qooxdoo.ui.form.InputField",
{
	type : "abstract"
	, extend : qx.ui.container.Composite,

  	/*
  	*****************************************************************************
     CONSTRUCTOR
  	*****************************************************************************
  	*/

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
		
		
		this._createLabel(config);
		this._createField(config);
		
		alert(config.toSource());
		
		
    	// add label to a container in order to set left and top
    	labelContainer = new qx.ui.container.Composite(new qx.ui.layout.Basic);
    	labelContainer.add(this._label, {top: config.top, left: config.left});
    	    	 
    	fieldContainer = new qx.ui.container.Composite(new qx.ui.layout.Basic);
    	fieldContainer.add(this._field, {top: config.top, left: config.left + 10});
    	
    	    	
    	this.add(this._label);
    	this.add(this._field); 
    	
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
    	
    	//this.setUserData('label', labelField);
    	//this.setUserData('field', textField);
    	
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