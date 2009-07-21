/**
 * SpagoBI - The Business Intelligence Free Platform
 *
 * Copyright (C) 2004 - 2008 Engineering Ingegneria Informatica S.p.A.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 **/
 
/**
  * Object name 
  * 
  * [description]
  * 
  * 
  * Public Properties
  * 
  * [list]
  * 
  * 
  * Public Methods
  * 
  *  [list]
  * 
  * 
  * Public Events
  * 
  *  [list]
  * 
  * Authors
  * 
  * - Chiara Chiarelli (chiara.chiarelli@eng.it)
  */

Ext.ns("Sbi.home");

Sbi.home.Banner = function(config) {
		
		this.initButtons(config);
		this.initToolbar();
		
		var c = Ext.apply({}, config, {
			region: 'north',
	        xtype: 'panel',
	        margins:'0 0 0 0',
	         bodyStyle:'padding:0px 0px 0px 0px',
	        items: [new  Ext.ux.ManagedIframePanel({
						frameConfig:{autoCreate:{id: 'iframeBanner', name:'iframeBanner',style: 'height:95; margins:0px 0px 0px 0px;',scrolling  : 'no' ,border: false ,layout: 'fit'	}}
		                ,defaultSrc : Sbi.config.contextName+'/themes/'+Sbi.config.currTheme+'/html/banner.html'
		                ,border		: false 
		                ,height: 95
		                ,loadMask   : true
						,collapseMode: 'mini'
						,scrolling  : 'no'	
						 , margins:'0 0 0 0'
						, bodyStyle:'padding:0px 0px 0px 0px'
				}),
	        	{
            	 height: 15,
           		 bbar : this.tbx
       		 }],
	        border: false,
	        scrolling  : 'no',	
			collapseMode: 'mini',
	        autoHeight: true
		});   
		
      
		Sbi.home.Banner.superclass.constructor.call(this, c);
    	
	
};

Ext.extend(Sbi.home.Banner, Ext.Panel, {
	
	// ---------------------------------------------------------------------------
    // object's members
	// ---------------------------------------------------------------------------
	 tbx:null ,  
	 
	 languages: null,
	    
	 tbInfoButton: null,	
		        
	 tbLanguagesButton: null,	
	 
	 tbWelcomeText: null,	
	 
	 menuArray: null,
	 
	// ---------------------------------------------------------------------------
    // public methods
	// ---------------------------------------------------------------------------
	
	hasItems: function(menu){
		var toReturn = false;
		if(menu.items){toReturn = true;}
		else{toReturn = false;}
		
		return toReturn;
	},
	
	getItems: function(menu){
	 		var toReturn = [];
	 		var hasIt= this.hasItems(menu);
	 		if(hasIt){
	 		
	 				for(var i = 0; i < menu.items.length; i++) {
	 				var hasIt2= this.hasItems(menu.items[i]);
		 				if(hasIt2){
		 				 var tempIt = this.getItems(menu.items[i]);
		 				 toReturn.push(    
       
       
                          new Ext.menu.Item({
                              id: menu.items[i].id,
                              group: 'group_2',
							//listeners: {'mouseexit': function(item) {item.hide();}},
											menu:{
												listeners: {'mouseexit': function(item) {item.hide();}},
				        						items: tempIt
				        					},
				        					text: menu.items[i].text,
						 					icon: menu.items[i].icon,
											href: menu.items[i].href                
                           
                         	 })                        
                              
             
										/*{
											id: menu.items[i].id,
											//listeners: {'mouseexit': function(item) {item.hide();}},
											menu:{
												listeners: {'mouseexit': function(item) {item.hide();}},
				        						items: tempIt
				        					},
				        					text: menu.items[i].text,
						 					icon: menu.items[i].icon,
											href: menu.items[i].href
										}*/
								)
		 				}else{
		 				 toReturn.push(
										new Ext.menu.Item({
											id: menu.items[i].id,
											group: 'group_3',
											//listeners: {'mouseexit': function(item) {item.hide();}},
				        					text: menu.items[i].text,
						 					icon: menu.items[i].icon,
											href: menu.items[i].href
										})
								)
		 				}
					}
			}	
	 		return toReturn;
	 		
	 },
	 
	 getMenu: function(menu){
	 	var toReturn ;
	 	var hasIt= this.hasItems(menu);
	 		if (hasIt){
	 			toReturn = new Ext.Toolbar.MenuButton({
							id: menu.id,
					        text: menu.text,
							path: menu.path,	
							   
	            			menu: new Ext.menu.Menu({
	            				id: menu.id,
								listeners: {'mouseexit': function(item) {item.hide();}},
								items: this.getItems(menu)
								
								}),
							handler:  function() {eval(menu.href);} ,
	            			//listeners: {'mouseexit': function(item) {item.hide();}},
	            			icon:  menu.icon,
	            			//scope: this,
					        cls: 'x-btn-menubutton x-btn-text-icon bmenu '
					        })
	 		}else{
	 			toReturn = new Ext.Toolbar.Button({
							id: menu.id,
					        text: menu.text,
							path: menu.path,	
							handler:  function() {eval(menu.href);} ,
	            			icon:  menu.icon,
	            			scope: this,
					        cls: 'x-btn-menubutton x-btn-text-icon bmenu '
					        })
	 		}	
	 	return toReturn;
	 },
	 
	 getLanguageUrl: function(config){
	   var languageUrl = "javascript:execUrl('"+Sbi.config.contextName+"/servlet/AdapterHTTP?ACTION_NAME=CHANGE_LANGUAGE&LANGUAGE_ID="+config.language+"&COUNTRY_ID="+config.country+"')";
	   return languageUrl;
	 },
	
	 initButtons: function(config){
	 		var menus = config.bannerMenu;
	 		
	 		if(menus){
	 			this.menuArray = [];
	 			for(var i = 0; i < menus.items.length; i++) {
					this.menuArray.push(this.getMenu(menus.items[i]));
	 			}
	 		}
	 		
	 		this.languages = new Ext.menu.Menu({ 
 			id: 'languages', 
			 items: [ 
 				new Ext.menu.Item({
					 id: '',
 					text: 'it',
 					iconCls:'icon-it',
					href: this.getLanguageUrl({language:'it' ,country:'IT'}) 
 				}),
	 			new Ext.menu.Item({
					 id: '',
 					text: 'en',
 					iconCls: 'icon-en',
					href: this.getLanguageUrl({language:'en' ,country:'US'})
 				}),
 				new Ext.menu.Item({
					 id: '',
 					text: 'fr',
 					iconCls: 'icon-fr',
 					href: this.getLanguageUrl({language:'fr' ,country:'FR'}) 
 				}),
 				new Ext.menu.Item({
					 id: '',
 					text: 'es',
 					iconCls: 'icon-es',
 					href: this.getLanguageUrl({language:'es' ,country:'ES'}) 
 				})
				]
	 		});
	 	this.languages.addListener('mouseexit', function(item) {item.hide();});	
 	    
 		this.tbExitButton = new Ext.Toolbar.Button({
	            id: '5',
	            text:  LN('sbi.home.Exit'),
	            iconCls: 'icon-exit',
	            cls: 'x-btn-logout x-btn-text-icon bmenu',
	            handler: this.logout,	
	            scope: this
	        })  ;
	    
	    this.tbInfoButton = new Ext.Toolbar.Button({
		            id: '',
		            iconCls: 'icon-info',
		            cls: 'x-btn-logout x-btn-text-icon bmenu',
		            handler: this.info,
		            scope: this
		        })	;	
		        
		this.tbLanguagesButton = new Ext.Toolbar.Button({
			 		text: '',
			 		iconCls:'icon-it',
			 		cls: 'x-btn-text-icon bmenu',
			 		menu: this.languages,
			 		scope: this
		 		});	
		
		 this.tbWelcomeText = new Ext.Toolbar.TextItem({
					text: LN('sbi.home.Welcome')+'<b>'+ Sbi.user.uniqueId+'<b>'
				});
	 },
	 	
	 initToolbar: function(){
		this.tbx = new Ext.Toolbar({
			items: ['']
		});

		this.tbx.on('render', function() {

			if(this.menuArray){
				
				for(var i = 0; i < this.menuArray.length; i++) {
					this.tbx.addButton(this.menuArray[i]);
					this.tbx.addSeparator();
				}
			}				
		    this.tbx.addFill();    
		 	this.tbx.add(this.tbWelcomeText);
			this.tbx.addSeparator();
			this.tbx.add(this.tbLanguagesButton);
	 	    this.tbx.addButton(this.tbInfoButton);
			this.tbx.add(this.tbExitButton);

		}, this);
	  },	
	 
	  info: function(){
		
		var win_info_1;
		if(!win_info_1){
			win_info_1= new Ext.Window({
			id:'win_info_1',
			autoLoad: {url: Sbi.config.contextName+'/themes/'+Sbi.config.currTheme+'/html/infos.html'},             				
				layout:'fit',
				width:210,
				height:180,
				closeAction:'hide',
 				buttonAlign : 'left',
				plain: true,
				title: LN('sbi.home.Info')
			});
		};
		win_info_1.show();
	  },
	
	  logout: function() {
			window.location = Sbi.config.contextName+"/servlet/AdapterHTTP?ACTION_NAME=LOGOUT_ACTION&LIGHT_NAVIGATOR_DISABLED=TRUE";
	   }
});