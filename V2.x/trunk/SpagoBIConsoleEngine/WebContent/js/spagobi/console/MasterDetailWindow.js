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
  * - Andrea Gioia (andrea.gioia@eng.it)
  */

Ext.ns("Sbi.console");

Sbi.console.MasterDetailWindow = function(config) {
	

	var defaultSettings = Ext.apply({}, config || {}, {
		title: 'Master/Detail windows'
		, width: 500
		, height: 250
		, hasBuddy: false		
	});
	
		
	if(Sbi.settings && Sbi.settings.console && Sbi.settings.console.masterDetailWindow) {
		defaultSettings = Ext.apply(defaultSettings, Sbi.settings.console.masterDetailWindow);
	}
		
	var c = Ext.apply(defaultSettings, config || {});
		
	Ext.apply(this, c);
		
		
	this.services = this.services || new Array();	
	this.services['doThat'] = this.services['doThat'] || Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'DO_THAT_ACTION'
		, baseParams: new Object()
	});
		
	this.addEvents('customEvents');
		
	this.initMainPanel(c);	
	
	c = Ext.apply(c, {  	
		layout: 'fit',
		closeAction:'hide',
		plain: true,
		modal:true,
		title: this.title,
		buttonAlign : 'center',
		buttons: [{
			text: 'Ok',
			handler: function(){
	        	this.hide();
	        }
	        , scope: this
		}, {
			text: 'Presa visione',
			handler: function() {
	        	alert('Bene ora sappiamo chi contattare in caso di problemi :)');
	        }
	        , scope: this
		}],
		items: [this.mainPanel]
	});

	// constructor
	Sbi.console.MasterDetailWindow.superclass.constructor.call(this, c);
    
};

Ext.extend(Sbi.console.MasterDetailWindow, Ext.Window, {
    
	mainPanel: null
	, masterPanel: null
	, detailPanel: null
   
    // public methods
    
   
    
    
    // private methods
    
    , initMainPanel: function() {
		this.initMasterlPanel();
		this.initDetailPanel();
		
		this.mainPanel = new Ext.Panel({
			layout: 'border',
		    frame: false, 
		    border: false,
		    bodyStyle:'background:#E8E8E8;',
		    style:'padding:3px;',
		    items: [this.masterPanel, this.detailPanel]
		});
    }

	, initMasterlPanel: function() {
		this.masterPanel = new Ext.Panel({
			region:'north',
    		split: true,
    		frame:false,
    		border:false,
    		height: 70,
    	    bodyStyle:'padding:5px;background:#E8E8E8;border-width:1px;border-color:#D0D0D0;',
    	    style: 'padding-bottom:3px',
		    html: 'Io sono il master ...'
		});
	}

	, initDetailPanel: function() {
		
		this.detailText = new Ext.form.HtmlEditor({
			 enableAlignments : false,
	    	 enableColors : false,
	    	 enableFont :  false,
	    	 enableFontSize : false, 
	    	 enableFormat : false,
	    	 enableLinks :  false,
	    	 enableLists : false,
	    	 enableSourceEdit : false,
	    	 autoScroll: true
		});
		
		this.detailText.setValue('Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis sed nibh ipsum. Ut dui nulla, viverra vitae condimentum eget, faucibus id quam. Praesent dapibus velit ut sem tincidunt pretium. Quisque scelerisque nisl in turpis ornare at vulputate nulla varius. Donec ut sem sed nunc accumsan mattis. In hac habitasse platea dictumst. Nulla et est eros, quis aliquet massa. Aliquam non ante ut sapien tincidunt porttitor. Duis commodo tincidunt egestas. Pellentesque eget pulvinar quam. Etiam lorem augue, fringilla et commodo in, viverra nec leo. Fusce molestie vehicula neque, sit amet tempus neque mattis vitae. Donec viverra vestibulum lectus, sit amet vestibulum ligula auctor sed. Maecenas lorem urna, congue id auctor eu, pharetra a libero. Quisque aliquam, enim nec auctor molestie, mi ipsum convallis dui, non condimentum leo quam nec dui. Fusce augue nisl, laoreet at tincidunt ut, malesuada et tellus. Maecenas sit amet nulla nisi, id rutrum orci. ');	
		this.detailText.setReadOnly(true);
		
		this.detailPanel = new Ext.Panel({
			region:'center',
		    frame: false, 
		    border: false,
		    autoScroll: false,
		    height: 'auto',
		    //html: '... e io sono il detail'
		    items: [
			    new Ext.Panel({
			    	layout: 'fit'
			    	, bodyStyle:'padding:5px;background:#E8E8E8;border-width:1px;border-color:#D0D0D0;'
			    	, items: [this.detailText]
			    })
		    ]
		});
		
		// dirty fix: without it do not fit :(
		/*
		this.detailText.on('render', function() {
			//alert( this.detailPanel.getSize().toSource() );
			this.detailText.setHeight( this.detailPanel.getSize().height - 12 );
		}, this);
		*/
		
		this.detailPanel.on('resize', function(panel, w, h, w1, h1) {
			this.detailText.setHeight( this.detailPanel.getSize().height - 12 );
		}, this);
		// dirty fix
	}
    
    
});