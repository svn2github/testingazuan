/**
  * Qbe - query builder panel
  * by Andrea Gioia
  */



 
var getQueryBuilderPanel = function() {

            var menuTree1 = it.eng.spagobi.engines.qbe.querybuilder.treePanel.getFoodmartTreePanel();                
            var menuTree2 = it.eng.spagobi.engines.qbe.querybuilder.treePanel.getFoodmartTreePanel();    
       
    
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
                  title:'Foodmart 1',
                  items: [menuTree1],
                  border:false
                },{
                  id:'block2',
                  autoScroll       : true,
        		  containerScroll  : true,
                  title:'Foodmart 2',
                  items: [menuTree2],
                  border:false
                }]
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
              
              
              it.eng.spagobi.engines.qbe.querybuilder.filterGrid.init();
              it.eng.spagobi.engines.qbe.querybuilder.selectGrid.app.init();
                               
              var queryBuilderCENTERPanel = new Ext.Panel({
                  id:'CENTERPanel',
                  title:'CENTERPanel',
                  region:'center',
                  autoScroll       : true,
        		  containerScroll  : true,
                  margins: '5 5 5 0',
                  tools:[{
                    id:'save',
                    qtip:'Save query',
                    // hidden:true,
                    handler: function(event, toolEl, panel){
                      // refresh logic
                    }
                  }, {
                    id:'gear',
                    qtip:'Execute query',
                    // hidden:true,
                    handler: function(event, toolEl, panel){
                      Ext.Ajax.request({
						   url: it.eng.spagobi.engines.qbe.serviceregistry.module.getServiceUrl('REFRESH_QUERY_ACTION'),
						   success: handleExecQuery,
						   failure: function(){alert('failure')},					
						   params: getParams
						});
                      
                    }
                  }, {
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
                  items: [it.eng.spagobi.engines.qbe.querybuilder.selectGrid.app.grid, it.eng.spagobi.engines.qbe.querybuilder.filterGrid.grid]
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
        
        var handleExecQuery = function() {
        	it.eng.spagobi.engines.qbe.app.activateTab();
        	execQuery();
        };
        
        var getParams = function() {
        	var params = {
        		records : it.eng.spagobi.engines.qbe.querybuilder.selectGrid.app.getRowsAsJSONParams(),
	        	filters : it.eng.spagobi.engines.qbe.querybuilder.filterGrid.getRowsAsJSONParams()
        	};
        	return params;
        };
