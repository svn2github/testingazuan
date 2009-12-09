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

Ext.ns("Sbi.execution.toolbar");

Sbi.execution.toolbar.MetadataWindow = function(config) {
	
	var params = {LIGHT_NAVIGATOR_DISABLED: 'TRUE', SBI_EXECUTION_ID: null};
	this.services = new Array();
	this.services['showMetadataService'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'MetadataBIObjectPage'
		, serviceType: 'PAGE'
		, baseParams: params
	}) + '&MESSAGEDET=METADATA_SELECT&OBJECT_ID=' + config.OBJECT_ID;
	
	this.services['getValuesForMetadataGridService'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'GET_METADATA_ACTION'
		, baseParams: params
	})+ '&OBJECT_ID=' + config.OBJECT_ID;
	
	this.buddy = undefined;
	
	this.initMetadata();
	
	 var store = this.createGridSourceStore();
	    store.load();
	   // alert(this.gridStore.toSource());
	   // this.shortTextStore = this.gridStore.shorttext;
	  //  this.longTextStore = this.gridStore.longtext;
	    var temp = this.createGrid();
	    this.shortTextGrid.setSource(temp);
	
		var c = Ext.apply({}, config, {
		id:'win_metadata'
		/*bodyCfg: {
			tag:'div',
			cls:'x-panel-body',
			children:[{
				tag:'iframe',
  				src: this.services['showMetadataService'],
  				frameBorder:0,
  				width:'100%',
  				height:'100%',
  				style: {overflow:'auto'}  
			}]
		},*/
		,items: [this.shortTextPanel,this.longTextPanel]
		,buttons: [
		          {
		            id: 'save'
		        	  ,text: LN('sbi.execution.notes.savenotes') 
		        	  ,scope: this
		        	  ,handler: this.saveMetadata
		        	  ,disabled: false
		          }
		]
		,layout:'fit'
		,width:650
		,height:410
		,plain: true
		,autoScroll:true
		,title: LN('sbi.execution.metadata')
	});   
	
	
	
	// constructor
    Sbi.execution.toolbar.MetadataWindow.superclass.constructor.call(this, c);
	
    if (this.buddy === undefined) {
    	this.buddy = new Sbi.commons.ComponentBuddy({
    		buddy : this
    	});
    }
    
};

Ext.extend(Sbi.execution.toolbar.MetadataWindow, Ext.Window, {
	services:null
   ,gridStore:null
   ,shortTextStore:null
   ,longTextStore:null
   ,shortTextPanel: null
   ,shortTextGrid:null
   ,longTextPanel: null
   ,longTextTabPanel: null
   ,longTextEditor1:null
   ,longTextEditor2:null
   
   ,initMetadata: function(){
	    
	   
	    
	    this.shortTextGrid = new Ext.grid.PropertyGrid({
	        width: 640,
	        autoHeight: true,
	        cm: new Ext.grid.ColumnModel([
				{header: "Ticker", width: 100, sortable: true},
				{header: "Company Name", width: 540, sortable: true}
			 ]),
			columns: ["Ticker","Company Name"],
	        source: {},
	        viewConfig : {
	            forceFit: true,
	            scrollOffset: 2 // the grid will never have scrollbars
	        }
	    });
	     
	    
	    this.shortTextPanel = new Ext.Panel({
	        title: 'Short Text Metadata',
	        collapsible: true,
	        collapsed : true,
	        items: [this.shortTextGrid],
	        autoWidth : true,
			autoHeight : true
	    });
		  
	var tab1 =  new Ext.Panel({
			title: 'Objective'
	});
	
	var tab2 =  new Ext.Panel({
			title: 'Long Description'
	});
	
	
	tab1.on("show", function() {
		this.longTextEditor1 = new Ext.form.HtmlEditor({
		         frame: true
		        ,value: 'cc'
		      	,width: '100%'
		        ,disabled: false
			   ,height: 258
			    ,enableSourceEdit: false      
		  }); 
		tab1.add(this.longTextEditor1);
	}, this);
	
	tab2.on("show", function() {
		
		this.longTextEditor2 = new Ext.form.HtmlEditor({
		         frame: true
		        ,value: 'cc'
		      	,width: '100%'
		        ,disabled: false
			   ,height: 258
			    ,enableSourceEdit: false      
		  }); 
		  
		tab2.add(this.longTextEditor2);
	}, this);
	  
	this.longTextTabPanel = new Ext.TabPanel({
		 hideMode: 'offsets'	
	       , resizeTabs: true, 
	        minTabWidth: 115,
	        tabWidth: 135,
	        enableTabScroll: true,
	        activeTab: 1,
		    autoScroll:true,
		    
		    items: [tab1,tab2]
		});
		
	this.longTextPanel = new Ext.Panel({
	        title: 'Long Text Metadata',
	        collapsible: true,
	        collapsed : false,
	        items: [this.longTextTabPanel]
	    });

	}
	
	, createGridSourceStore: function() {

		var createGridStoreUrl = this.services['getValuesForMetadataGridService'];	
		var store = new Ext.data.JsonStore({
			url: createGridStoreUrl
		});
		store.on('loadexception', function(store, options, response, e) {
			var msg = '';
			var content = Ext.util.JSON.decode( response.responseText );		
  			if(content !== undefined) {
  				this.gridStore = content;
  				msg += content.serviceName + ' : ' + content.message;
  			} else {
  				msg += 'Server response is empty';
  			}
	
			Sbi.exception.ExceptionHandler.showErrorMessage(msg, response.statusText);
		});
		return store;
	}
	
	,saveMetadata: function(){
	
	}
	
	,createGrid: function(){
		var toReturn;
		if(this.shortTextStore){
		alert(this.shortTextStore.toSource());
			var tempStr = '{ ';
		      for(var i=0;i<this.shortTextStore.length;i++){
			      if(i!=0){
			      	tempStr += ' , ';
			      }
		      	  tempStr += this.shortTextStore[i].meta_name +': '+this.shortTextStore[i].meta_content;
		      }
		      tempStr += ' }';
		      alert(tempStr);
		      toReturn = eval(tempStr);
		}else{
			toReturn = {};
		}
	    return toReturn;
	 }

});