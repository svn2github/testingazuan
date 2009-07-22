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
		
		var itemsForBanner = [];
		
		itemsForBanner.push({
	            html: '<div style="overflow:hidden; width:100%;background-image:url(\'/SpagoBI/themes/sbi_default/img/wapp/spagobiMiddle.png\'); background-repeat: repeat-x;background-position:bottom;" ><div style="float:left"><img src="/SpagoBI/themes/sbi_default/img/wapp/spagobiLeft.png" /></div><div style="float:right"><img src="/SpagoBI/themes/sbi_default/img/wapp/spagobiRight.png" /></div></div>'
	        });
		//TODO: tenere questo pannello per modificare il banner in futuro
		/*itemsForBanner.push(new  Ext.ux.ManagedIframePanel({
						frameConfig:{autoCreate:{id: 'iframeBanner', name:'iframeBanner',style: 'height:95; margins:0px 0px 0px 0px;',scrolling  : 'no' ,border: false ,layout: 'fit'	}}
		                ,defaultSrc : Sbi.config.contextName+'/themes/'+Sbi.config.currTheme+'/html/banner.html'
		                ,border		: false 
		                ,height: 95
		                ,loadMask   : true
						,collapseMode: 'mini'
						,scrolling  : 'no'	
						, margins:'0 0 0 0'
						, bodyStyle:'padding:0px 0px 0px 0px'
				}));*/
				
		itemsForBanner.push(this.tbx);
		
       	if(this.useToolbar2){	
       		itemsForBanner.push(this.tbx2);
       	}
        if(this.useToolbar3){	
       		itemsForBanner.push(this.tbx3);
       	}
		
		var c = Ext.apply({}, config, {
			region: 'north',
	        margins:'0 0 0 0',
	        items: itemsForBanner,
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
	 
	 tbx2:null ,  
	 
	 useToolbar2: false,
	 
	 tbx3:null ,  
	 
	 useToolbar3: false,
	 
	 languages: null,
	 
	 themes: null,
	 
	 tbThemesButton: null,
	    
	 tbInfoButton: null,	
		        
	 tbLanguagesButton: null,	
	 
	 tbWelcomeText: null,	
	 
	 menuArray: null,
	 
	 menuThemesArray: null,
	 
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
                              //group: 'group_2',
							  menu:{
									listeners: {'mouseexit': function(item) {item.hide();}},
				        			items: tempIt
				        		   },
				        	  text: menu.items[i].text,
						 	  icon: menu.items[i].icon,
							  href: menu.items[i].href                       
                         	 }) 
						  )
		 				}else{
		 				 toReturn.push(
						   new Ext.menu.Item({
								id: menu.items[i].id,
								//group: 'group_3',
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
	            			icon:  menu.icon,
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
	 		
	 		var menuThemesList = config.themesMenu;
	 		if(drawSelectTheme && menuThemesList){
	 			this.menuThemesArray = [];
	 			for(var i = 0; i < menuThemesList.items.length; i++) {
					this.menuThemesArray.push(
					new Ext.menu.Item({
					 	id: menuThemesList.items[i].id,
 						text: menuThemesList.items[i].text,
						href: menuThemesList.items[i].href				
 						})
					);
	 			}
	 			
	 			this.themes = new Ext.menu.Menu({ 
 								id: 'themes', 
			 					items: this.menuThemesArray
			 					});
			 	this.themes.addListener('mouseexit', function(item) {item.hide();});
			 	
			 	this.tbThemesButton = new Ext.Toolbar.Button({
			 		text: themesViewName,
			 		icon: themesIcon,
			 		cls: 'x-btn-text-icon bmenu',
			 		menu: this.themes,
			 		scope: this
		 		});	
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
					text: LN('sbi.home.Welcome')+'<b>'+ Sbi.user.userId+'<b>&nbsp;&nbsp;&nbsp;'
				});
	 },
	 	
	 initToolbar: function(){
		this.tbx = new Ext.Toolbar({
			items: ['']
		});
		
		this.tbx2 = new Ext.Toolbar({
			items: ['']
		});
		
		this.tbx3 = new Ext.Toolbar({
			items: ['']
		});

		var lenghtUserName = Sbi.user.userId.length+10;
	    var lenghtUserNameInPixel = lenghtUserName*5;
	    var menulenght = lenghtUserNameInPixel + 140;
	    if(drawSelectTheme){
	   		 menulenght = menulenght + 100;
	    }
	    var menuArrayIterator2 = this.menuArray.length;
	    var menuArrayIterator3 = this.menuArray.length;
       	
       	for(var i = 0; i < this.menuArray.length; i++) {
				    var tempMenuLength = this.menuArray[i].text.length*8;
				    if(!tempMenuLength){
				    	tempMenuLength = 30;
				    }
					if(menulenght+tempMenuLength<browserWidth){
						menulenght = menulenght+tempMenuLength;
					}else{
						menulenght = 0;
						this.useToolbar2 = true;
						menuArrayIterator2 = i;
						break;
					}
		}
		
		if(this.useToolbar2){
			for(var i = menuArrayIterator2; i < this.menuArray.length; i++) {
						    var tempMenuLength = this.menuArray[i].text.length*8;
						    if(!tempMenuLength){
						    	tempMenuLength = 30;
						    }
							if(menulenght+tempMenuLength<browserWidth){
								menulenght = menulenght+tempMenuLength;
							}else{
								menulenght = 0;
								this.useToolbar3 = true;
								menuArrayIterator3 = i;
								break;
							}
			}
		}
	
		this.tbx.on('render', function() {

			if(this.menuArray){		
				for(var i = 0; i < menuArrayIterator2; i++) {
						this.tbx.add(this.menuArray[i]);
						this.tbx.addSeparator();
				}
			}				
		    this.tbx.addFill();    
		 	this.tbx.add(this.tbWelcomeText);
			this.tbx.addSeparator();
			if(drawSelectTheme){
				this.tbx.add(this.tbThemesButton);
				this.tbx.addSeparator();
			}
			this.tbx.add(this.tbLanguagesButton);
	 	    this.tbx.addButton(this.tbInfoButton);
			this.tbx.add(this.tbExitButton);

		}, this);

		if(this.useToolbar2){
		
			this.tbx2.on('render', function() {
				if(this.menuArray){				
					for(var i = menuArrayIterator2; i < menuArrayIterator3; i++) {
							this.tbx2.addButton(this.menuArray[i]);
							this.tbx2.addSeparator();
					}
				}	
				this.tbx2.addFill();    			
			}, this);
		}
		
		if(this.useToolbar3){      		 
			this.tbx3.on('render', function() {	
				if(this.menuArray){					
					for(var i = menuArrayIterator3; i < this.menuArray.length; i++) {
							this.tbx3.addButton(this.menuArray[i]);
							this.tbx3.addSeparator();
					}
				}	
				this.tbx3.addFill();    				
			}, this);
		}		
		
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
			window.location =logoutUrl;
	   }
});