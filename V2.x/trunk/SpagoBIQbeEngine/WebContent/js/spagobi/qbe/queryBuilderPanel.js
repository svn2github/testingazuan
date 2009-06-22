/**
  * Qbe - query builder panel
  * by Andrea Gioia
  */



 
var getQueryBuilderPanel = function(query, dataStorePanel) {

            var menuTree1 = it.eng.spagobi.engines.qbe.querybuilder.treePanel.getFoodmartTreePanel();                
            //var menuTree2 = it.eng.spagobi.engines.qbe.querybuilder.treePanel.getFoodmartTreePanel();    
       
            this.dataStorePanel = dataStorePanel;
    
            var treesPanel = new Ext.Panel({
                id:'treepanel',
                collapsible: true,
                margins:'0 0 0 5',
                layout:'accordion',
                layoutConfig:{
                  animate:true
                },
                items: [{
                  id:'block1',
                  autoScroll       : true,
        		  containerScroll  : true,
                  title:'Datamart 1',
                  items: [menuTree1],
                  border:false
                }/*,{
                  id:'block2',
                  autoScroll       : true,
        		  containerScroll  : true,
                  title:'Datamart 2',
                  items: [menuTree2],
                  border:false
                }*/]
              });
    
              var queryBuilderWESTPanel = new Ext.Panel({
                  id:'WESTPanel',
                  title:'Schema',
                  region:'west',
                  width:250,
                  margins: '5 5 5 5',
                  layout:'fit',
                  collapsible: true,
                  collapseMode: 'mini',
                  collapseFirst	 : false,
                  tools:[{
                    id:'pin',
                    qtip:'Expand all',
                    // hidden:true,
                    handler: function(event, toolEl, panel){
                      // refresh logic
                    }
                  }, {
                    id:'unpin',
                    qtip:'Collapse all',
                    // hidden:true,
                    handler: function(event, toolEl, panel){
                      // refresh logic
                    }
                  }, {
                    id:'gear',
                    qtip:'Flat view',
                    // hidden:true,
                    handler: function(event, toolEl, panel){
                      // refresh logic
                    }
                  }, {
                    id:'plus',
                    qtip:'Add calulated field',
                    // hidden:true,
                    handler: function(event, toolEl, panel){
                      // refresh logic
                    }
                  }],
                  items:[treesPanel]
              });
              
              
              it.eng.spagobi.engines.qbe.querybuilder.filterGrid.app.init(query);
             
              it.eng.spagobi.engines.qbe.querybuilder.selectGrid.app.init(query);
             
                               
              var queryBuilderCENTERPanel = new Ext.Panel({
                  id:'CENTERPanel',
                  title:'.',
                  region:'center',
                  autoScroll       : true,
        		  containerScroll  : true,
                  margins: '5 5 5 0',
                  tools:[{
                    id:'save',
                    qtip:'Save query as subobject',
                    // hidden:true,
                    handler: function(event, toolEl, panel){
                      showDialog();
                    }
                  }, {
                    id:'saveView',
                    qtip:'Save query as view',
                    // hidden:true,
                    handler: function(event, toolEl, panel){
                      Ext.Ajax.request({
						   url: it.eng.spagobi.engines.qbe.serviceregistry.module.getServiceUrl('REFRESH_QUERY_ACTION'),
						   success: handleSaveView,
						   failure: it.eng.spagobi.engines.qbe.exceptionhandler.module.handleFailure,					
						   params: getParams
						});  
                      
                      //showDialog2();	   
					} 
                  },{
                    id:'gear',
                    qtip:'Execute query',
                    // hidden:true,
                    handler: function(event, toolEl, panel){
                      Ext.Ajax.request({
						   url: it.eng.spagobi.engines.qbe.serviceregistry.module.getServiceUrl('REFRESH_QUERY_ACTION'),
						   success: handleExecQuery,
						   failure: it.eng.spagobi.engines.qbe.exceptionhandler.module.handleFailure,					
						   params: getParams
						});                      
                    }
                  },{
                    id:'search',
                    qtip:'Validate query',
                    // hidden:true,
                    handler: function(event, toolEl, panel){
                      // refresh logic
                    }
                  }, {
                    id:'help',
                    qtip:'Help me please',
                    // hidden:true,
                    handler: function(event, toolEl, panel){
                      // refresh logic
                    }
                  }],
                  items: [it.eng.spagobi.engines.qbe.querybuilder.selectGrid.app.grid, it.eng.spagobi.engines.qbe.querybuilder.filterGrid.app.grid]
              });
        
        
        
        
              var queryBuilderPagePanel = new Ext.Panel({
              	id:'queryBuilderPagePanel',
              	title: 'Query',
              	layout: 'border',
              	border: false,
              	contentEl:'tabs',    
              	items: [queryBuilderWESTPanel, queryBuilderCENTERPanel]
              });
              
			
            return queryBuilderPagePanel;                   
        };
        
        
        
        
        var handleExecQuery = function(response, options) {
     		it.eng.spagobi.engines.qbe.app.activateTab();
     		if(this.dataStorePanel) {
     			this.dataStorePanel.execQuery();
     		} else {
     			execQuery();
     		}
        };
    
        
       
       var viewName = new Ext.form.TextField({
				id:'viewName',
				name:'viewName',
				allowBlank:false, 
				inputType:'text',
				maxLength:50,
				width:250,
				fieldLabel:'Name' 
	    }); 
        
       var queryName = new Ext.form.TextField({
				id:'queryName',
				name:'queryName',
				allowBlank:false, 
				inputType:'text',
				maxLength:50,
				width:250,
				fieldLabel:'Name' 
	    });
	    
	    var queryDescription = new Ext.form.TextField({
				id:'queryDescription',
				name:'queryDescription',
				allowBlank:false, 
				inputType:'text',
				maxLength:50,
				width:250,
				fieldLabel:'Description' 
	    });
	    
	    
	   
		var scopeComboBoxData = [
		       ['PUBLIC','Public', 'Everybody that can execute this qbe will see also your saved query'],
		       ['PRIVATE', 'Private', 'The saved quary will be visible only to you']
		];
		
		 var scopeComboBoxStore = new Ext.data.SimpleStore({
		        fields: ['value', 'field', 'description'],
		        data : scopeComboBoxData 
		    });
		    
		    
	    var queryScope = new Ext.form.ComboBox({
	    	tpl: '<tpl for="."><div ext:qtip="{field}: {description}" class="x-combo-list-item">{field}</div></tpl>',	
	    	editable  : false,
	    	fieldLabel : 'Scope',
	    	forceSelection : true,
	    	mode : 'local',
	    	name : 'queryScope',
	    	store : scopeComboBoxStore,
	    	displayField:'field',
		    valueField:'value',
		    emptyText:'Select scope...',
		    typeAhead: true,
		    triggerAction: 'all',
		    selectOnFocus:true
	    });
	    
	    var handleSaveQuery = function() {
        	var qName = queryName.getValue();
        	var qDescription = queryDescription.getValue();
        	var qScope = queryScope.getValue();
        	
        	var qRecords = it.eng.spagobi.engines.qbe.querybuilder.selectGrid.app.getRowsAsJSONParams();
	        var qFilters = it.eng.spagobi.engines.qbe.querybuilder.filterGrid.app.getRowsAsJSONParams();
	        var qFilterExp = it.eng.spagobi.engines.qbe.querybuilder.filterGrid.app.getFiltersExpressionAsJSON();
	        
	        var url = it.eng.spagobi.engines.qbe.serviceregistry.module.getServiceUrl('SAVE_QUERY_ACTION');
	        url += '&queryName=' + qName;
	        url += '&queryDescription=' + qDescription;
	        url += '&queryScope=' + qScope;
	        url += '&queryRecords=' + qRecords;
	        url += '&queryFilters=' + qFilters;
	        url += '&queryFilterExp=' + qFilterExp;
	        
	        Ext.Ajax.request({
				url:  url,
				success: function(response, options) {
					var content;
					
					win.hide();
					content = Ext.util.JSON.decode( response.responseText );
					content.text = content.text || "";
					if (content.text.match('OK - ')) {
						try {
							parent.loadSubObject(window.name, content.text.substr(5));
						} catch (ex) {}
						try {
							sendMessage("Subobject saved!!!!","subobjectsaved");
						} catch (ex) {}
					}
				},
				failure: it.eng.spagobi.engines.qbe.exceptionhandler.module.handleFailure					
			});   
        };
        
        var handleSaveView = function() {
        	showDialog2();
        };
        
        var handleCreateView = function() {
        	var vName = viewName.getValue();
        	
        	var qRecords = it.eng.spagobi.engines.qbe.querybuilder.selectGrid.app.getRowsAsJSONParams();
	        var qFilters = it.eng.spagobi.engines.qbe.querybuilder.filterGrid.app.getRowsAsJSONParams();
	        var qFilterExp = it.eng.spagobi.engines.qbe.querybuilder.filterGrid.app.getFiltersExpressionAsJSON();
	        
	        
	        var url = it.eng.spagobi.engines.qbe.serviceregistry.module.getServiceUrl('CREATE_VIEW_ACTION');
	        url += '&viewName=' + vName;
	        //url += '&queryRecords=' + qRecords;
	        //url += '&queryFilters=' + qFilters;
	        //alert('url: ' + url);
        	Ext.Ajax.request({
				url:  url,
				success: refreshTreeAndExit,
				failure: it.eng.spagobi.engines.qbe.exceptionhandler.module.handleFailure				
			});   
        	
        };
        
        var refreshTreeAndExit = function() {
        	win2.hide();
        	it.eng.spagobi.engines.qbe.querybuilder.treePanel.refresh();
        }
        
        var formPanel = new Ext.form.FormPanel({
	        frame:true,
	        bodyStyle:'padding:5px 5px 0',
	        buttonAlign : 'center',
	        items: [queryName,queryDescription,queryScope],
	        buttons: [{
		            text: 'Save',
		            handler: handleSaveQuery
		      	},{
		            text: 'Cancel',
		            handler: function(){
                        win.hide();
                    }
		        }]
	    });
	    
        var win;
        var showDialog = function(){
            if(!win) {
	            win = new Ext.Window({
					id:'id1',
					layout:'fit',
					width:500,
					height:300,
					closeAction:'hide',
					plain: true,
					title: 'Save as ...',
					items: [formPanel]
				});
			}
            win.show();
        };
        
        
         var formPanel2 = new Ext.form.FormPanel({
	        frame:true,
	        bodyStyle:'padding:5px 5px 0',
	        buttonAlign : 'center',
	        items: [viewName],
	        buttons: [{
		            text: 'Save',
		            handler: handleCreateView
		      	},{
		            text: 'Cancel',
		            handler: function(){
                        win2.hide();
                    }
		        }]
	    });
	    
        var win2;
        var showDialog2 = function(){
            if(!win2) {
	            win2 = new Ext.Window({
					id:'id2',
					layout:'fit',
					width:500,
					height:300,
					closeAction:'hide',
					plain: true,
					title: 'Save as view...',
					items: [formPanel2]
				});
			}
            win2.show();
        };
        
        
        var getParams = function() {
        	
        	var queryStr = '{';
        	queryStr += 'fields : ' + it.eng.spagobi.engines.qbe.querybuilder.selectGrid.app.getRowsAsJSONParams() + ',';
        	queryStr += 'filters : ' + it.eng.spagobi.engines.qbe.querybuilder.filterGrid.app.getRowsAsJSONParams() + ',';
        	queryStr += 'expression: ' +  it.eng.spagobi.engines.qbe.querybuilder.filterGrid.app.getFiltersExpressionAsJSON();
        	queryStr += '}';
        	
        	//alert('>>> ' + queryStr);
        	
        	var params = {
        		query: queryStr 
        	}	;        	
        	
        	return params;
        };
        
        
        
