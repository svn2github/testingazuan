/*

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

*/


/* *
 * @author Andrea Gioia (andrea.gioia@eng.it)
 */
 
/**
 * Class to create the main toolbar on top of the page
 */ 

/*
#asset(qx/icon/Oxygen/16/actions/help-about.png)
#asset(qx/icon/Oxygen/16/actions/system-shutdown.png)
*/

qx.Class.define("spagobi.ui.ToolBar", {
	//extend : qx.legacy.ui.toolbar.ToolBar,//change
	extend : qx.ui.toolbar.ToolBar,

	/**
	 * Constructor to create the top toolbar with buttons on it.
	 * <p>It accepts an array of objects each of which corresponds to a button on 
	 * the toolbar and calls the addButton() function.
	 * <p>See the addButton() function below for more details.
	 * <p> It also cretes the buttons on its right side for choosing 
	 * a different language or the Help button.
	 * <p>
	 * <code>
	 * var toolbarView = new spagobi.ui.ToolBar([
      		{
		  		command: 'Control+Q',
		  		handler: function() {...},
		  		context: this,
		  		"label": 'Resources',
		  		icon: 'icon/16/actions/myPic1.png',
		  		tooltip: 'button1 to press'
	  		}, {
		  		command: 'Control+W',
		  		handler: function() {....},
		  		context: this,
		  		"label": 'Catalogues',
		  		icon: 'icon/16/actions/myPic2.png',
		  		tooltip: 'button2 to press'
	  		}
	 * </code>
	 * 
	 * @param config  - Array of objects each having 
	 * a set of properties like command, label, handler etc. as described above.
	 * 
	 * */
  	
  	construct : function( config ) {
	    this.base(arguments);
	    qx.Class.include(qx.ui.toolbar.RadioButton, qx.ui.core.MExecutable);
	    //this._toolBarManager = new qx.legacy.ui.selection.RadioManager(null);//change
	    this._toolBarManager = new qx.ui.form.RadioGroup(null);//so that only 1 is selected ..but doesnt work with command
	    
	    //TRYING PUTTING THIS IN CONTAINER (COMPOSITE OR OTHER)
	    //this._container = new qx.ui.container.Composite(new qx.ui.layout.Grow());
	    
	    for(var i = 0; i < config.length; i++) {
	    	this.addButton( config[i] );
	    }
		
	  	//this.add(new qx.legacy.ui.toolbar.Separator());//change
	  	this.addSeparator(); //or this.add(new qx.ui.toolbar.Separator());
	  	//this._container.add(new qx.ui.toolbar.Separator());
	
		//this.add(new qx.legacy.ui.basic.HorizontalSpacer());//change
		//this.add(new qx.ui.core.Spacer());   //maybe also {flex:1}
		this.addSpacer();

    	// Poulate languages menu and add it to the toolbar
	    var locales =
	    {
	      en : this.tr("English"),
	      de : this.tr("German"),
	      fr : this.tr("French"),
	      tr : this.tr("Turkish"),
	      it : this.tr("Italian"),
	      es : this.tr("Spanish"),
	      sv : this.tr("Swedish"),
	      ru : this.tr("Russian")
	    };
	
	    var availableLocales = qx.locale.Manager.getInstance().getAvailableLocales();
	    var locale = qx.locale.Manager.getInstance().getLocale();
	    //var lang_menu = new qx.legacy.ui.menu.Menu();//change
	    var lang_menu = new qx.ui.menu.Menu();
	    
	    //var radioManager = new qx.legacy.ui.selection.RadioManager("lang");//change
	    var radioManager = new 	qx.ui.form.RadioGroup();
		radioManager.setName("lang");
		
	    for (var lang in locales)
	    {
	      if (availableLocales.indexOf(lang) == -1) {
	        continue;
	      }
	
	      //var menuButton = new qx.legacy.ui.menu.RadioButton(locales[lang], null, locale == lang);//change
	      var menuButton = new qx.ui.menu.RadioButton(locales[lang], null, locale == lang);
	      menuButton.setUserData("locale", lang);
	      lang_menu.add(menuButton);
	      radioManager.add(menuButton);
	    }
	
	    radioManager.addListener("changeSelected", function(e)
	    {
	      var lang = e.getValue().getUserData("locale");
	      qx.locale.Manager.getInstance().setLocale(lang);
	    });
	
	    //lang_menu.addToDocument();//change
	    //this.getRoot().add(lang_menu);//not working
	    
	    //this.add(new qx.legacy.ui.toolbar.MenuButton(null, lang_menu, "spagobi/img/spagobi/test/locale.png"));
		this.add(new qx.ui.toolbar.MenuButton(null, qx.util.AliasManager.getInstance().resolve("spagobi/img/spagobi/test/locale.png"), lang_menu));
		
				
		this.addButton({
	  		command: 'F1',
	  		handler: this.showAbout,
	  		context: this,
	  		//"label": this.tr('Help'),
	  		icon: "qx/icon/Oxygen/16/actions/help-about.png",
	  		tooltip: 'Help'
	  	});
	  	
	  	this.addSeparator();
	  	
	  	var welcomeMsg = "Welcome:";
	  	var userid = spagobi.app.data.DataService.userName();
		var userWelcomeLabel = spagobi.commons.WidgetUtils.createLabel({
																		content: userid,
																		width: 50
		});
		userWelcomeLabel.setLayout(new qx.ui.layout.HBox);
		userWelcomeLabel.getLayout().setAlignY("middle");
		this.add(userWelcomeLabel);
	  	
	  	var logoutButton = new qx.ui.toolbar.Button(this.tr('Logout'), "qx/icon/Oxygen/16/actions/system-shutdown.png");
	  	logoutButton.addListener("execute",this.logoutButton,this);
	  	logoutButton.setToolTip(new qx.ui.tooltip.ToolTip("Logout"));
	  	this.add(logoutButton);
	  	/*
	  	this.addButton({
	  		command: 'F1',
	  		handler: this.logoutButton,
	  		context: this,
	  		"label": this.tr('Logout'),
	  		icon: "qx/icon/Oxygen/16/actions/system-shutdown.png",
	  		tooltip: 'Logout'
	  	});
	  	*/
	},
  
	members: {
		
		_toolBarManager: undefined,
		_container: undefined,
	/**
	 * Shows a popup box
	 */	
		showAbout: function(e) {
			alert('Help');
		
		},
		
		logoutButton: function(e) {
			alert('Logout');
		
		},
					
	/**
	 * Function to add a button on the top tool bar.
	 * <p> It is called by the spagobi.ui.ToolBar object's constructor
	 * for each object passed in it.
	 * <p> It Creates a button, sets its label, icon and tooltip and
	 * associates the command to it.
	 * 
	 * <p> *btnConfig Options*:
	 * <p> *command*: String
	 * 		<p>&nbsp;&nbsp;&nbsp;&nbsp; Keyboard shortcut for the button,
	 * <p>*handler*: Function()
	 * 		<p>&nbsp;&nbsp;&nbsp;&nbsp; Function to be executed when
	 * 									button is pressed,
	 * <p>*context*: Object
	 * 		<p>&nbsp;&nbsp;&nbsp;&nbsp; Object within whose context the handler 
	 * 									function can be executed,
	 * <p>*label* : String
	 * 		<p>&nbsp;&nbsp;&nbsp;&nbsp;	Label of the command	
	 * <p>*icon* : String
	 * 		<p>&nbsp;&nbsp;&nbsp;&nbsp; Path of the icon to be displayed
	 * <p>*tooltip*: String
	 * 		<p>&nbsp;&nbsp;&nbsp;&nbsp; Tooltip of the button
	 * 
	 * <p><code>
	 * toolbarObject.addButton({
						  		command: 'Control+Q',
						  		handler: function() {...},
						  		context: this,
						  		"label": 'Resources',
						  		icon: 'icon/16/actions/myPic1.png',
						  		tooltip: 'button1 to press'
	  						});
	 * </code>
	 * 
	 * @param btnConfig {qx.core.Object} An object having the above properties.
	 *  
	 */
		
		addButton: function(btnConfig) {  			
  			var command;
  			if(btnConfig.command && btnConfig.handler) {
	    		//var command = new qx.event.Command( btnConfig.command );//change
	    		var command = new qx.event.Command( btnConfig.command );
	    		
	    		command.addListener("execute", btnConfig.handler, btnConfig.context);
  			}
    		
    		//var button = new qx.legacy.ui.toolbar.RadioButton(this.tr(btnConfig.label), btnConfig.icon);
			//var button = new qx.legacy.ui.toolbar.RadioButton(btnConfig.label, btnConfig.icon);//change
			
			//qx.Class.include(qx.ui.toolbar.RadioButton, qx.ui.core.MExecutable);//change..added to include setCommand()
			var button = new qx.ui.toolbar.RadioButton(btnConfig.label, btnConfig.icon);
			  
        	this._toolBarManager.add(button);		// Radio Mananger
			
			if(command) {
				//qx.Class.include(qx.ui.toolbar.RadioButton, qx.ui.core.MExecutable);//change..added to include setCommand()
				button.setCommand( command );
			} else if( btnConfig.handler ) {
				//button.addListener("execute", btnConfig.handler, btnConfig.context);//change
				button.addListener("click", btnConfig.handler, btnConfig.context);//or mousedowm
				
			}
			//button.addListener("execute", btnConfig.handler, btnConfig.context);//change
			button.addListener("click", btnConfig.handler, btnConfig.context);//or mousedowm
			
			if(btnConfig.tooltip) {
				if(command) {
							//button.setToolTip(new qx.legacy.ui.popup.ToolTip(this.tr('(%1) ' + btnConfig.tooltip, command.toString())));
					//button.setToolTip(new qx.legacy.ui.popup.ToolTip(btnConfig.tooltip));//change
					button.setToolTip(new qx.ui.tooltip.ToolTip(btnConfig.tooltip));
				} else {
							//button.setToolTip(new qx.legacy.ui.popup.ToolTip(this.tr(btnConfig.tooltip)));
          			//button.setToolTip(new qx.legacy.ui.popup.ToolTip(btnConfig.tooltip));//change
          			button.setToolTip(new qx.ui.tooltip.ToolTip(btnConfig.tooltip));			
				}				
			}
			
			this.add(button);
			
			if(btnConfig.defaultbutton != undefined){
				if(btnConfig.defaultbutton == true){
					button.setChecked(true);
				}
			}
  		}
  		
  }
});
