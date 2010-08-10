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
            '<div><a name="{id}"></a><h2><div>{title}</div></h2>',
            '<dl>',
                '<tpl for="samples">',                    
                    '<dd ext:url="{url}">',
                        '<div id="control-panel" class="control-panel">',
                            '<a href="javascript:alert(\'Document sended succesfully\')"><img title="send by email" src="images/send.gif"/></a>',
                            '<a href="javascript:alert(\'Document scheduled succesfully\')"><img title="shedule" src="images/schedule.gif"/></a>',
                        '</div>',
                        '<div id="icon"><img src="images/{icon}"/></div>',
                        '<div id="description"><h4>{text}</h4><p>{desc}</p></div>',
                    '</dd>',
                '</tpl>',
            '<div style="clear:left"></div></dl></div>',
            '</tpl>',
        '</div>'
);

Sbi.browser.FolderView = function(config) {   
   Sbi.browser.FolderView.superclass.constructor.call(this, config);
   this.createIndex('samples');
}; 
    
    
Ext.extend(Sbi.browser.FolderView, Ext.DataView, {
//Sbi.browser.FolderView = Ext.extend(Ext.DataView, {
    frame:true
    , itemSelector: 'dd'
    , overClass: 'over'
    , lookup: null
    
    , tpl : Sbi.browser.groupTpl

    , onClick : function(e){
        /*
        var group = e.getTarget('h2', 3, true);
        if(group){
            group.up('div').toggleClass('collapsed');
        }else {
            var t = e.getTarget('dd', 5, true);
            if(t && !e.getTarget('a', 2)){
                var url = t.getAttributeNS('ext', 'url');
                window.open(url);
            }
        }
        */
        
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
    
    , createIndex : function(groupIndex) {
      var id = 0;
      
      this.lookup = {};
      
      var groups = this.store.getRange(0, this.store.getCount());
      for(var i = 0; i < groups.length; i++) {
        var records = groups[i].data[groupIndex];
        for(var j = 0; j < records.length; j++) {
          this.lookup[id++] = records[j];
        }
        
      }
    }
    
     
});



/*    
Sbi.browser.FolderView.bkp = new Ext.XTemplate(
        '<div id="sample-ct">',
            '<tpl for=".">',
            '<div><a name="{id}"></a><h2><div>{title}</div></h2>',
            '<dl>',
                '<tpl for="samples">',
                    '<dd ext:url="{url}">',
                        '<img src="images/{icon}"/>',
                        '<div><h4>{text}</h4><p>{desc}</p></div>',
                    '</dd>',
                '</tpl>',
            '<div style="clear:left"></div></dl></div>',
            '</tpl>',
        '</div>'
    );
*/