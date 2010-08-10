qx.Class.define("qooxdoo.commons.ComponentRegistry",
{
	type : "singleton"
  	, extend : qx.core.Object


  	/*
  	*****************************************************************************
     CONSTRUCTOR
  	*****************************************************************************
  	*/

	, construct : function() {
    	this.base(arguments);
  	}

  	, members :
  	{
  		// this object is shared by all instances of the componentRegistry class 
  		_componentMap: {
  			'textfield' : qooxdoo.ui.form.TextField
  			, 'textarea' : qooxdoo.ui.form.TextArea
  			, 'combo' : qooxdoo.ui.form.XComboBox
  			, 'check' : qooxdoo.ui.form.CheckBox
  			, 'formList' : qooxdoo.ui.form.FormListInputField	
  			, 'propertiesList' : qooxdoo.ui.form.PropertiesList
  			, 'flag' : qooxdoo.ui.form.FlagBox
  			, 'form' : qooxdoo.ui.form.FormInputField
  			, 'radio' : qooxdoo.ui.form.RadioBox
  			, 'groupbox' : qooxdoo.ui.form.GroupBox
  		}
  		
  		/**
  		 * Creates a new Component from the specified config object using the config object's xtype to determine the class to instantiate. 
		 *
		 * @param config {Object} A configuration object for the Component you wish to create.
		 * @param defaultType {String} The default Component type if the config object does not contain an xtype. (Optional if the config contains an xtype).
		 * @return {Object} The newly instantiated Component.
  		 */
  		, createComponent: function(config, defaultXType) {
  			var component;
  			var componentClass;
  			
  			config.xtype = config.xtype || defaultXType;
  			componentClass = this._componentMap[config.xtype];
  			if(componentClass) {
  				component = new componentClass(config);
  			} else {
  				alert('[' + config.xtype + '] is not registered into the component registry');
  			}
  			
  			return component;
  		}
  		
  		/**
  		 * Registers a new component, keyed by a new type.
  		 * 
  		 * @param type The mnemonic string by which the component class may be looked up
  		 * @param component The new Component class
  		 */
  		, registerComponent: function(xtype, componentClass) {
  			this._componentMap[xtype] = componentClass;
  		}
  	}
});