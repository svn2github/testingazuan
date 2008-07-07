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


/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 */
qx.Class.define("spagobi.ui.ToolBar", {
	extend : qx.ui.toolbar.ToolBar,

  	construct : function( config ) {
	    this.base(arguments);
	    
	    for(var i = 0; i < config.length; i++) {
	    	this.addButton( config[i] );
	    }
	
	    // Define commands
	/*
		this.addButton({
	  		command: 'Control+A',
	  		handler: this._controller.showAddFeed,
	  		context: this._controller,
	  		"label": 'Resources',
	  		icon: 'icon/16/actions/dialog-ok.png',
	  		tooltip: 'Reload the feeds.'
	  	});
	  	
	  	this.addButton({
	  		command: 'Control+D',
	  		handler: this._controller.showRemoveFeed,
	  		context: this._controller,
	  		"label": 'Catalogues',
	  		icon: 'icon/16/actions/dialog-ok.png',
	  		tooltip: 'Reload the feeds.'
	  	});
	  	
	  	this.add(new qx.ui.toolbar.Separator());
	  	
		this.addButton({
	  		command: 'Control+R',
	  		handler: this._controller.reload,
	  		context: this._controller,
	  		"label": 'Behavioural Model',
	  		icon: 'icon/16/actions/dialog-ok.png',
	  		tooltip: 'Reload the feeds.'
	  	});
	
		this.addButton({
	  		command: 'Control+P',
	  		handler: this._controller.showPreferences,
	  		context: this._controller,
	  		"label": 'Analytical Model',
	  		icon: 'icon/16/actions/dialog-ok.png',
	  		tooltip: 'Reload the feeds.'
	  	});
	  	*/
	  	
	  	this.add(new qx.ui.toolbar.Separator());
	
		this.add(new qx.ui.basic.HorizontalSpacer());	   

    	// Poulate languages menu and add it to the toolbar
	    var locales =
	    {
	      en : this.tr("English"),
	      de : this.tr("German"),
	      tr : this.tr("Turkish"),
	      it : this.tr("Italian"),
	      es : this.tr("Spanish"),
	      sv : this.tr("Swedish"),
	      ru : this.tr("Russian")
	    };
	
	    var availableLocales = qx.locale.Manager.getInstance().getAvailableLocales();
	    var locale = qx.locale.Manager.getInstance().getLocale();
	    var lang_menu = new qx.ui.menu.Menu();
	    var radioManager = new qx.ui.selection.RadioManager("lang");
	
	    for (var lang in locales)
	    {
	      if (availableLocales.indexOf(lang) == -1) {
	        continue;
	      }
	
	      var menuButton = new qx.ui.menu.RadioButton(locales[lang], null, locale == lang);
	      menuButton.setUserData("locale", lang);
	      lang_menu.add(menuButton);
	      radioManager.add(menuButton);
	    }
	
	    radioManager.addEventListener("changeSelected", function(e)
	    {
	      var lang = e.getValue().getUserData("locale");
	      qx.locale.Manager.getInstance().setLocale(lang);
	    });
	
	    lang_menu.addToDocument();
	    this.add(new qx.ui.toolbar.MenuButton(null, lang_menu, "spagobi/img/spagobi/test/locale.png"));
		
		
		
		this.addButton({
	  		command: 'F1',
	  		handler: this.showAbout,
	  		context: this,
	  		"label": 'Help',
	  		icon: 'icon/16/actions/help-about.png',
	  		tooltip: 'Help'
	  	});
	},
  
	members: {
		showAbout: function() {alert('Help');},
		
		addButton: function(btnConfig) {  			
  			var command;
  			if(btnConfig.command && btnConfig.handler) {
	    		var command = new qx.client.Command( btnConfig.command );
	    		command.addEventListener("execute", btnConfig.handler, btnConfig.context);
  			}
    		
    		var button = new qx.ui.toolbar.Button(this.tr(btnConfig.label), btnConfig.icon);
			
			if(command) {
				button.setCommand( command );
			} else if( btnConfig.handler ) {
				button.addEventListener("execute", btnConfig.handler, btnConfig.context);
			}
			
			if(btnConfig.tooltip) {
				if(command) {
					button.setToolTip(new qx.ui.popup.ToolTip(this.tr('(%1) ' + btnConfig.tooltip, command.toString())));
				} else {
					button.setToolTip(new qx.ui.popup.ToolTip(this.tr(btnConfig.tooltip)));				
				}				
			}
			
			this.add(button);
  		}
  }
});
