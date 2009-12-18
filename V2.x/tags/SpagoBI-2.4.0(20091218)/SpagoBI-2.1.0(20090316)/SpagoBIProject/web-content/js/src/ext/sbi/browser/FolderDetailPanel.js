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
    // sub-components   
    
    this.store = new Ext.data.JsonStore({
        idProperty: 'id',
        fields: ['id', 'title', 'samples'],
        data: Sbi.browser.sampleData.folder1Data
    });
    
    this.folderView = new Sbi.browser.FolderView({
            store: this.store
            , listeners: {
                'click': {
    							fn: this.onClick
    							, scope: this
    						}
              }    
        });
        
    var ttbarTextItem = new Ext.Toolbar.TextItem('> ?');  
    var ttbarToggleViewButton = new Ext.Toolbar.Button({
      tooltip: 'List view',
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
      label: 'Foodmart'
      , url: 'http://www.google.it/search?q=Foodmart'
    }, {
      label: 'General Manager'
      , url: 'http://www.google.it/search?q=GeneralManager'
    } , {
      label: 'Staff'
      , url: 'http://www.google.it/search?q=Staff'
    }]);
      
    if(config.modality && (
      config.modality === 'list-view' || 'group-view'
    )) {
      this.modality = config.modality;
    } else {
      this.modality = 'group-view';
    }
    
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
          	 this.loadingMask = new Sbi.decorator.LoadMask(this.mainPanel.body, {msg:"Please wait ..."}); 
            },
          	scope: this
          }
        }        
    });
    
    var c = Ext.apply({}, config, {
      items: [this.mainPanel]
    });
    
    
    
    
    Sbi.browser.FolderDetailPanel.superclass.constructor.call(this, c);   
    
    
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
        
        b.tooltip = 'Group view';
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
      // ...
      this.loadingMask.hide();      
    }
    
    
    
    , setBreadcrumbs: function(breadcrumbs) {
      var txt = '';
      for(var i=0; i<breadcrumbs.length-1; i++) {
        txt += '> <a href="' + breadcrumbs[i].url + '">' + breadcrumbs[i].label + '</a> ';
      }
      txt += '> ' + breadcrumbs[breadcrumbs.length-1].label;
      
      this.setToolbarText(txt);
    }
    
    // private methods 
    
    , onClick: function(dataview, i, node, e) {
      alert( this.folderView.getRecord(i).text );
    }
    
    , setToolbarText: function(text) {
      this.toolbar.text.getEl().innerHTML = text;
    }
});


