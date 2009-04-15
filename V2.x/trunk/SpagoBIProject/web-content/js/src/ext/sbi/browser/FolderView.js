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

//Sbi.browser.groupTpl = new Sbi.browser.FolderViewTemplate();

/*
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
                        
                           // '<a href="javascript:alert(\'Document sended succesfully\')"><img title="send by email" src="../img/analiticalmodel/browser/send.gif"/></a>',
                           // '<a href="javascript:alert(\'Document scheduled succesfully\')"><img title="shedule" src="../img/analiticalmodel/browser/schedule.gif"/></a>',
                        
                        '</div>',
                     // -- DOCUMENT -----------------------------------------------
                        '<tpl if="this.exists(engine) == true">',
                        	'<div id="icon" class="document"></div>',
                        	//'<div id="icon" class="{parent.iconCls}"></div>',
	                        '<div id="description">',
	                        	'<h4>{label}</h4>',
		                        '<h4>{name}</h4>',
		                        '<i>{creationDate}</i>',
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
*/

Sbi.browser.FolderView = function(config) {
	
	this.tpl =  new Sbi.browser.FolderViewTemplate(config);
	this.viewState = {
		filterType: 'all'
		, sortGroup: 'Documents'
		, sortAttribute: 'name'
		, groupGroup: 'Documents'
		, groupAttribute: 'ungroup'
	};
    config.store.on('load', this.onLoad, this, {groupIndex: 'samples'});
    Sbi.browser.FolderView.superclass.constructor.call(this, config);
}; 
    
    
Ext.extend(Sbi.browser.FolderView, Ext.DataView, {
    frame:true
    , itemSelector: 'dd'
    , overClass: 'over'
    , groups: null
    , lookup: null
    , viewState: null
    , ready: false
    
    , tpl : null

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
    
    , onLoad : function(s, r) {
    	this.groups = this.store.getRange();
    	this.ready = true;
    	this.applyState();
    }
    
    , createIndex : function() {
      var id = 0;   
      
      this.lookup = {};
      
      var groups = this.store.getRange(0, this.store.getCount());
      for(var i = 0; i < groups.length; i++) {
        var records = groups[i].data['samples'];
        for(var j = 0; j < records.length; j++) {
          this.lookup[id++] = records[j];
        }        
      }
    }
    
    , getRecord : function(n){
        var i = (typeof n == 'number')?n:n.viewIndex;
        return this.lookup[i];
    }
    
    , reset : function() {
    	this.store.removeAll();
    	this.store.add( this.groups );
    }
    
    , getCollection : function(groupName) {  
    	var collection = null;
    	
    	var groupIndex = this.store.find('title', groupName);    	
    	if(groupIndex === -1) return null;
    	var group = this.store.getAt( groupIndex );
    	var records = group.data['samples'];
    	collection = new Ext.util.MixedCollection(false);
    	collection.addAll(records);
    	
    	return collection;
    }
    
    
    , applyState : function() {
    	if(!this.ready) return;
    	this.reset();
    	this.applyFilter(this.viewState.filterType);
    	this.applySort(this.viewState.sortGroup, this.viewState.sortAttribute);
    	this.applyGroup(this.viewState.groupGroup, this.viewState.groupAttribute);
    	this.createIndex();
    	this.refresh();
    }
    
    , sort : function(groupName, attributeName) { 
    	this.viewState.sortGroup = groupName;
    	this.viewState.sortAttribute = attributeName;
    	this.applyState();
    	
    	/*
    	this.reset();
    	this.applySort(groupName, attributeName);
    	this.createIndex();
    	this.refresh();
    	*/
    }
    
    , applySort : function(groupName, attributeName) {
    	var collection = this.getCollection(groupName, attributeName);
    	if(collection == null) return;
    	collection.sort('ASC', function(r1, r2) {
            var v1 = r1[attributeName], v2 = r2[attributeName];
            return v1 > v2 ? 1 : (v1 < v2 ? -1 : 0);
        });
    	
    	var groupIndex = this.store.find('title', groupName);    	
    	var group = this.store.getAt( groupIndex );
    	group.data['samples'] = collection.getRange();   
    }
    
    , group : function(groupName, attributeName) { 
    	
    	this.viewState.groupGroup = groupName;
    	this.viewState.groupAttribute = attributeName;
    	this.applyState();
    	/*
    	this.reset();
    	this.applyGroup(groupName, attributeName);
    	this.createIndex();
    	this.refresh();
    	*/
    }
    
    , applyGroup : function(groupName, attributeName) { 
    	if(attributeName === 'ungroup') return;
    	var collection = this.getCollection(groupName);
    	if(collection == null) return;
    	var distinctValues = {};
    	collection.each(function(item) {
    		distinctValues[item[attributeName]] = true;
    	});
    	var groupIndex = this.store.find('title', groupName);    	
    	var group = this.store.getAt( groupIndex );
    	this.store.remove(group);    
    	
    	var GroupRecord = Ext.data.Record.create([
    	    {name: 'title', type: 'string'},
    	    {name: 'icon', type: 'string'}, 
    	    {name: 'samples', type: 'string'}
    	]);
    	
    	for(p in distinctValues) {
    		var newGroup = collection.filter(attributeName, p)
    		this.store.add([
    		          	  new GroupRecord({
    		          		  title: p
    		          		  , icon: 'document.png'
    		          		  , samples: newGroup.getRange()
    		          	  })
    		          	]);
    	}
    	
    }
    
    , filter : function(type) {
    	this.viewState.filterType = type;
    	this.applyState();
    	/*
    	this.reset();
    	this.applyFilter(type);
    	this.createIndex();
    	this.refresh();
    	*/
    }
    
    , applyFilter: function(type) {
    	if(type === 'folders') {
    		var groupIndex = this.store.find('title', 'Folders'); 
    		if(groupIndex !== -1) {
    			var group = this.store.getAt( groupIndex );
    			this.store.removeAll();  
    			this.store.add([group]);
    		}
    	} else if (type === 'documents') {
    		var groupIndex = this.store.find('title', 'Folders');  
    		if(groupIndex !== -1) {
	        	var group = this.store.getAt( groupIndex );
	        	this.store.remove(group);   
    		}
    	}    	
    }
    
     
});