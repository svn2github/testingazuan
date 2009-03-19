/**
 * SpagoBI - The Business Intelligence Free Platform
 *
 * Copyright (C) 2004 - 2009 Engineering Ingegneria Informatica S.p.A.
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
 * 
 * @author Andrea Gioia (andrea.gioia@eng.it)
 */

Ext.ns("Sbi.browser");

Sbi.browser.FolderDetailPanel = function(config) {    
    
	var loadFolderContentService = Sbi.config.serviceRegistry.getServiceUrl('GET_FOLDER_CONTENT_ACTION');
	loadFolderContentService += '&LIGHT_NAVIGATOR_DISABLED=TRUE';
	
	// -- store -------------------------------------------------------
	this.store = new Ext.data.JsonStore({
	    url: loadFolderContentService,
	    /*
	    baseParams: {
			folderId: 3
		},
		*/
	    root: 'folderContent',
	    fields: ['title', 'icon', 'samples']
	});	
	this.store.on('loadexception', Sbi.exception.ExceptionHandler.handleFailure);
	
	// -- folderView ----------------------------------------------------
    this.folderView = new Sbi.browser.FolderView({
    	store: this.store
        , listeners: {
        	'click': {
    			fn: this.onClick
    			, scope: this
    		}
    	}
    	, emptyText: LN('sbi.browser.folderdetailpanel.emptytext')
    });
    
    // -- toolbar -----------------------------------------------------------
    var ttbarTextItem = new Ext.Toolbar.TextItem('> ?');  
    var ttbarToggleViewButton = new Ext.Toolbar.Button({
    	tooltip: LN('sbi.browser.folderdetailpanel.listviewTT'),
		iconCls:'icon-list-view',
		listeners: {
			'click': {
          		fn: this.toggleDisplayModality,
          		scope: this
        	} 
		}
    });
    
    this.toolbar = new Ext.Toolbar({
      //cls: 'top-toolbar'
      items: [
          ttbarTextItem
          ,'->'
          , ttbarToggleViewButton  
      ]
    });
    this.toolbar.text = ttbarTextItem;
    this.toolbar.buttonsL = new Array();
    this.toolbar.buttonsL['toggleView'] = ttbarToggleViewButton;
     
    
    this.setBreadcrumbs([{
      id: 1
      , label: 'Foodmart'
    }, {
    	id: 2
        , label: 'General Manager'
    } , {
    	id: 3
        , label: 'Staff'
    }]);
      
    if(config.modality && (
      config.modality === 'list-view' || 'group-view'
    )) {
      this.modality = config.modality;
    } else {
      this.modality = 'group-view';
    }
    
    // -- mainPanel -----------------------------------------------------------
    this.mainPanel = new Ext.Panel({
        id:'doc-details'
        , cls: this.modality
        , autoHeight: true
        , collapsible: true
        , frame: true
        , autoHeight: false
        , autoScroll:true
               
        , items: this.folderView        
        , tbar: this.toolbar 
        
        , listeners: {
		    'render': {
            	fn: function() {
          	 	this.loadingMask = new Sbi.decorator.LoadMask(this.mainPanel.body, {msg:LN('sbi.browser.folderdetailpanel.waitmsg')}); 
            	},
            	scope: this
          	}
        }        
    });
    
    var c = Ext.apply({}, config, {
      items: [this.mainPanel]
    });   
    
    Sbi.browser.FolderDetailPanel.superclass.constructor.call(this, c);   
    
    this.addEvents("ondocumentclick", "onfolderclick");
    
    this.store.load();    
}




Ext.extend(Sbi.browser.FolderDetailPanel, Ext.Panel, {
    
    modality: null // list-view || group-view
    , store: null
    , toolbar: null
    , folderView: null
    , loadingMask: null
    
    , toggleDisplayModality: function() {
      this.loadingMask.show();
      var b = this.toolbar.buttonsL['toggleView'];
      if(this.modality === 'list-view') {
        this.mainPanel.getEl().removeClass('list-view');
        this.mainPanel.addClass('group-view');
        this.modality = 'group-view';
        
        b.tooltip = 'List view';
        b.setIconClass('icon-list-view');
      } else {
        this.mainPanel.getEl().removeClass('group-view');
        this.mainPanel.addClass('list-view');
        this.modality = 'list-view';
        
        b.tooltip = LN('sbi.browser.folderdetailpanel.groupviewTT');
        b.setIconClass('icon-group-view');        
      }
      
       var btnEl = b.el.child(b.buttonSelector);
       if(b.tooltip){
            if(typeof b.tooltip == 'object'){
                Ext.QuickTips.register(Ext.apply({
                      target: btnEl.id
                }, b.tooltip));
            } else {
                btnEl.dom[b.tooltipType] = b.tooltip;
            }
        }   
      
      this.loadingMask.hide();
    }
    
    , loadFolder: function(folderId) {
      this.loadingMask.show();
      this.store.baseParams = {
			'folderId': folderId
	  }
      this.store.load();
      this.loadingMask.hide();      
    }
    
    , setBreadcrumbs: function(breadcrumbs) {
    	var txt = '';
        for(var i=0; i<breadcrumbs.length-1; i++) {
          txt += '> <span id="' + breadcrumbs[i].id + '">' + breadcrumbs[i].label + '</span> ';
        }
        txt += '> ' + breadcrumbs[breadcrumbs.length-1].label;        
        this.setToolbarText(txt);
        
        
        for(var i=0; i<breadcrumbs.length-1; i++) {
        	var el =  Ext.ComponentMgr.get(breadcrumbs[i].id);
        	alert(el.on);
        	
        	el.on('click', this.onBreadClick, this, {
        	    bread: breadcrumbs[i]
        	});
        	
        }
    }
    
    , onBreadClick: function(a, b, c, d) {
    	alert('onBreadClick');
    	alert(a);
    	alert(b);
    	alert(c);
    	alert(d);
    	/*
    	alert(a.toSource());
    	alert(b.toSource());
    	alert(c.toSource());
    	alert(d.toSource());
    	*/
    }
    
    /*
    , setBreadcrumbs: function(breadcrumbs) {
      var txt = '';
      for(var i=0; i<breadcrumbs.length-1; i++) {
        txt += '> <a href="#">' + breadcrumbs[i].label + '</a> ';
      }
      txt += '> ' + breadcrumbs[breadcrumbs.length-1].label;
      
      this.setToolbarText(txt);
    }
    */
    
    // private methods 
    
    , onClick: function(dataview, i, node, e) {
      //this.folderView.getRecord(i).text;
      var r = this.folderView.getRecord(i);
      if(r.engine) {
    	  this.fireEvent('ondocumentclick', this, r, e);
      } else{
    	  this.fireEvent('onfolderclick', this, r, e);
      }
      
    }
    
    , setToolbarText: function(text) {
      this.toolbar.text.getEl().innerHTML = text;
    }
});


