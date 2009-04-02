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

Sbi.browser.groupTpl = new Ext.XTemplate(
        '<div id="sample-ct">',
            '<tpl for=".">',
            '<div><a name="{id}"></a><h2><div>{title} ({[values.samples.length]})</div></h2>',
            '<dl>',
            	'<tpl if="samples.length == 0">',
            		'<div id="empty-group-message">No items in this group</div>',
            	'</tpl>',
                '<tpl for="samples">',   
                	'{[engine=""]}',
                    '<dd ext:url="{url}">',
                        '<div id="control-panel" class="control-panel">',
                        /*
                            '<a href="javascript:alert(\'Document sended succesfully\')"><img title="send by email" src="../img/analiticalmodel/browser/send.gif"/></a>',
                            '<a href="javascript:alert(\'Document scheduled succesfully\')"><img title="shedule" src="../img/analiticalmodel/browser/schedule.gif"/></a>',
                        */
                        '</div>',
                     // -- DOCUMENT -----------------------------------------------
                        '<tpl if="this.exists(engine) == true">',
                        	'<div id="icon" class="document"></div>',
                        	//'<div id="icon" class="{parent.iconCls}"></div>',
	                        '<div id="description">',
		                        '<h4>{name}</h4>',
		                        '<p><b>Description:</b> {description} ',
		                        '{extendedDescription}</p>',		                        
	                        '</div>',
                        '</tpl>',
                        // -- FOLDER -----------------------------------------------
                        '<tpl if="this.exists(engine) == false">',
                        	'<tpl if="this.isHomeFolder(codType) == true">',
	                        	'<div id="icon" class="folder_home"></div>',
	                        '</tpl>',
	                        '<tpl if="this.isHomeFolder(codType) == false">',
	                        	'<div id="icon" class="folder"></div>',
                        	'</tpl>',
	                        '<div id="description">',
		                        '<h4>{name}</h4>',
	                        '</div>',
                        '</tpl>',
                    '</dd>',
                '</tpl>',
            '<div style="clear:left"></div></dl></div>',
            '</tpl>',
        '</div>', {
        	exists: function(o){
        		return typeof o != 'undefined' && o != null && o!='';
        	}
        	, isHomeFolder: function(s) {
        		return s == 'USER_FUNCT';
        	}

        }
);

Sbi.browser.FolderView = function(config) {
   config.store.on('load', this.createIndex, this, {groupIndex: 'samples'});
   Sbi.browser.FolderView.superclass.constructor.call(this, config);
   //this.createIndex('samples');
}; 
    
    
Ext.extend(Sbi.browser.FolderView, Ext.DataView, {
//Sbi.browser.FolderView = Ext.extend(Ext.DataView, {
    frame:true
    , itemSelector: 'dd'
    , overClass: 'over'
    , lookup: null
    
    , tpl : Sbi.browser.groupTpl

    , onClick : function(e){
        
        var group = e.getTarget('h2', 3, true);
        if(group){
            group.up('div').toggleClass('collapsed');
        }else {
        	/*
            var t = e.getTarget('dd', 5, true);
            if(t && !e.getTarget('a', 2)){
                var url = t.getAttributeNS('ext', 'url');
                window.open(url);
            }
            */
        }
       
        
        return Sbi.browser.FolderView.superclass.onClick.apply(this, arguments);
    }
    
    , onMouseOver : function(e) {    
      var group = e.getTarget('h2', 3, true);
        if(!group){
            var d = e.getTarget('dd', 5, true);
            if(d){
                var t = d.first('div.control-panel', false);
                if(t){   
                  t.applyStyles('visibility:visible');
                }
            }
        }
        return Sbi.browser.FolderView.superclass.onMouseOver.apply(this, arguments);
    }
    
    ,onMouseOut : function(e){
        var group = e.getTarget('h2', 3, true);
        if(!group){
            var d = e.getTarget('dd', 5, true);
            if(d){
                var t = d.first('div.control-panel', false);
                if(t){   
                  t.applyStyles('visibility:hidden');
                }
            }
        }
        return Sbi.browser.FolderView.superclass.onMouseOut.apply(this, arguments);
    }
    
    , getRecord : function(n){
        var i = (typeof n == 'number')?n:n.viewIndex;
        return this.lookup[i];
    }
    
    , createIndex : function(s, r, o) {
      var id = 0;
      
      o.groupIndex = 'samples';
      
      this.lookup = {};
      
      var groups = this.store.getRange(0, this.store.getCount());
      for(var i = 0; i < groups.length; i++) {
        var records = groups[i].data[o.groupIndex];
        for(var j = 0; j < records.length; j++) {
          this.lookup[id++] = records[j];
        }
        
      }
    }
    
     
});