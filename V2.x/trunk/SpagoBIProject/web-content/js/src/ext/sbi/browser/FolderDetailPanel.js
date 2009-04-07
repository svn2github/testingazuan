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
	
	var searchContentService = Sbi.config.serviceRegistry.getServiceUrl('SEARCH_CONTENT_ACTION');
	searchContentService += '&LIGHT_NAVIGATOR_DISABLED=TRUE';
	
	// -- store -------------------------------------------------------
	this.store = new Ext.data.JsonStore({
	    url: loadFolderContentService
	    , browseUrl: loadFolderContentService
	    , searchUrl: searchContentService
	    , root: 'folderContent'
	    , fields: ['title', 'icon', 'samples']
	});	
	this.store.on('loadexception', Sbi.exception.ExceptionHandler.handleFailure);
	this.store.on('beforeload', function(){if(this.loadingMask) this.loadingMask.show();}, this);
	this.store.on('load', function(){if(this.loadingMask) this.loadingMask.hide();}, this);
	
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
    ttbarTextItem.isBreadcrumb = true;
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
    this.toolbar.breadcrumbs = new Array();
    this.toolbar.breadcrumbs.push(ttbarTextItem);
    //this.toolbar.text = ttbarTextItem;
    this.toolbar.buttonsL = new Array();
    this.toolbar.buttonsL['toggleView'] = ttbarToggleViewButton;
     
    
    
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
    
    this.addEvents("ondocumentclick", "onfolderclick", "onbreadcrumbclick");
    
    //this.store.load();   
    this.loadFolder(config.folderId);
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
      var p;
      if(folderId) {
	      p = {'folderId': folderId};
      }
      
      this.store.proxy.conn.url = this.store.browseUrl;
      this.store.baseParams = p || {};
      this.store.load();
     
      var loadFolderPathService = Sbi.config.serviceRegistry.getServiceUrl('GET_FOLDER_PATH_ACTION');
      loadFolderPathService += '&LIGHT_NAVIGATOR_DISABLED=TRUE';
      Ext.Ajax.request({
          url: loadFolderPathService,
          params: p,
          callback : function(options , success, response){
    	  	if(success && response !== undefined) {   
	      		if(response.responseText !== undefined) {
	      			var content = Ext.util.JSON.decode( response.responseText );
	      			if(content !== undefined) {
	      				this.setBreadcrumbs(content);
	      			} 
	      		} else {
	      			Sbi.exception.ExceptionHandler.showErrorMessage('Server response is empty', 'Service Error');
	      		}
    	  	}
          },
          scope: this,
  		  failure: Sbi.exception.ExceptionHandler.handleFailure      
       });
    }
    
    , searchFolder: function(params) {       
    	this.store.proxy.conn.url = this.store.searchUrl;
        this.store.baseParams = params || {};
        this.store.load();  
        this.setTitle('Query results ...');
    }
    
   
    , setTitle: function(title) {
    	this.resetToolbar();
    	this.toolbar.add({
    		xtype: 'tbtext'
    		, text: title || ''
    	});
    	this.reinitToolbar();
    }
    
    , setBreadcrumbs: function(breadcrumbs) {
    	
    	this.resetToolbar();
    	
    	this.add({
            iconCls: 'icon-ftree-root'
            //, disabled: true
        });
    	
        for(var i=0; i<breadcrumbs.length-1; i++) {
        	this.toolbar.add({
        		text: breadcrumbs[i].name
        		, breadcrumb: breadcrumbs[i]
        		, listeners: {
        			'click': {
                  		fn: this.onBreadCrumbClick,
                  		scope: this
                	} 
        	}
        	}, ' > ');
        }
        
        this.toolbar.add({
    		text: breadcrumbs[breadcrumbs.length-1].name
    		, breadcrumb: breadcrumbs[breadcrumbs.length-1]
    		, disabled: true
    		, cls: 'sbi-last-folder'
    	});
        
        this.reinitToolbar();
    }
    
    , resetToolbar: function() {
    	this.toolbar.items.each(function(item){            
            this.items.remove(item);
            item.destroy();           
        }, this.toolbar.items); 
    }
    
    , reinitToolbar: function() {
    	var tt;
        var cls;
        if(this.modality === 'list-view') {
        	tt = LN('sbi.browser.folderdetailpanel.groupviewTT');
        	cls = 'icon-group-view';
        } else {
        	tt = LN('sbi.browser.folderdetailpanel.listviewTT');
        	cls = 'icon-list-view';
        }
        this.toolbar.buttonsL['toggleView'] = new Ext.Toolbar.Button({
        	tooltip: tt,
    		iconCls: cls,
    		listeners: {
    			'click': {
              		fn: this.toggleDisplayModality,
              		scope: this
            	} 
    		}
        });
        this.toolbar.add('->', this.toolbar.buttonsL['toggleView']);
    }
   
    
    , onBreadCrumbClick: function(b, e) {    	
    	this.fireEvent('onbreadcrumbclick', this, b.breadcrumb, e);
    }
    
    
    
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
    
    , sort : function(groupName, attributeName) {
    	if(this.loadingMask) this.loadingMask.show();
    	//alert('sort: ' + groupName + ' - ' + attributeName);
    	this.folderView.sort(groupName, attributeName);
    	if(this.loadingMask) this.loadingMask.hide();
    }
    
    , group : function(groupName, attributeName) {
    	if(this.loadingMask) this.loadingMask.show();
    	//alert('group: ' + groupName + ' - ' + attributeName);
    	this.folderView.group(groupName, attributeName);
    	if(this.loadingMask) this.loadingMask.hide();
    }
    
    , filter : function(type) {
    	if(this.loadingMask) this.loadingMask.show();
    	//alert('filter: ' + type);
    	this.folderView.filter(type);
    	if(this.loadingMask) this.loadingMask.hide();
    }
    
   
});


