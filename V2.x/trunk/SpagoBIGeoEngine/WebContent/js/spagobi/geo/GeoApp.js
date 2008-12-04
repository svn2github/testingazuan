/**
  * ...
  * by Andrea Gioia
  */
 
 
// create application
Sbi.geo.app = function() {
    // do NOT access DOM from here; elements don't exist yet
 
    // private variables
     
    // private functions

 
    // public space
    return {
        // public properties, e.g. strings to translate
        
        serviceRegistry: undefined
        
        , saveAnalysisWin: undefined
        
        // public methods
        , init: function( config ) {
            Ext.QuickTips.init();
            
            var messageBoxBuddy =  new Sbi.commons.ComponentBuddy({
				buddy : Ext.MessageBox.getDialog()
			});
			messageBoxBuddy.hide();
            
            
            var drillPanel = new Sbi.geo.DrillControlPanel(config);
           
            var viewport = new Ext.Viewport({
              layout: 'border',
              border: false,
              items: [
              { // CENTER REGION ---------------------------------------------------------
                region: 'center',
                title: 'Map',                
                //collapsible: true,
                collapsed: false,
                split: true,
                autoScroll: false,
                height: 100,
                minHeight: 100,
                width: 100,
                minWidth: 0,
                layout: 'fit',
                
                tools:[
                {
                    id:'save',
                    handler: function(event, toolEl, panel){
                      if(this.saveAnalysisWin === undefined) {
                      	var sequence = new Sbi.commons.ServiceSequence({
                      	  	onSequenceExecuted: function(response) {
                      			var content = Ext.util.JSON.decode( response.responseText );
								content.text = content.text || "";
								if (content.text.match('OK - ')) {
									try {
										parent.loadSubObject(window.name, content.text.substr(5));
										Ext.MessageBox.show({
							           		title: 'Customized view saved'
							           		, msg: 'Customized view saved succesfully !!!'
							           		, buttons: Ext.MessageBox.OK     
							           		, icon: Ext.MessageBox.INFO 
							           		, modal: false
							       		});
									} catch (ex) {
										alert('ERROR: ' + ex.toSource())
									}
								}
                      		} 
                      		, onSequenceExecutedScope: this  
                      	});
                      	
                      	
                      	this.saveAnalysisWin = new Sbi.geo.SaveAnalysisWindow({
                      		saveServiceSequence : sequence      		                   		
                      	});
                      	
                      	sequence.add({
		                	url: Sbi.geo.app.serviceRegistry.getServiceUrl('SET_ANALYSIS_STATE_ACTION')
							, failure: Sbi.commons.ExceptionHandler.handleFailure
							, params: drillPanel.getAnalysisState
							, scope: drillPanel
						});
						sequence.add({
		                    url: Sbi.geo.app.serviceRegistry.getServiceUrl('SAVE_ANALYSIS_STATE_ACTION')
							, failure: Sbi.commons.ExceptionHandler.handleFailure
							, params: this.saveAnalysisWin.getAnalysisMeta
							, scope: this.saveAnalysisWin
						});   
                      }
                      this.saveAnalysisWin.show();
                    }
                } , {
                    id:'gear',
                    handler: function(event, toolEl, panel){
                    	
                    	Ext.Ajax.request({
                    		url: Sbi.geo.app.serviceRegistry.getServiceUrl('SET_ANALYSIS_STATE_ACTION')
                    		, success: function() {
                    			
                    			var iframeEl = Ext.get('iframe_1');
                    			iframeEl.dom.src = Sbi.geo.app.serviceRegistry.getServiceUrl('DRAW_MAP_ACTION');
                    		}
							, failure: Sbi.commons.ExceptionHandler					
							, params: drillPanel.getAnalysisState
							, scope: drillPanel 
                    	});   
                    }
                }],
                
                
                bodyCfg: {
        					tag:'div',
          				cls:'x-panel-body',
          				children:[{
          					tag:'iframe',
          					src: Sbi.geo.app.serviceRegistry.getServiceUrl('DRAW_MAP_ACTION'),
          	      			//src: 'AdapterHTTP?ACTION_NAME=DRAW_MAP_ACTION',
          	      			frameBorder:0,
          	      			width:'100%',
          	      			height:'100%',
          	      			id: 'iframe_1',
          	      			name: 'iframe_1'
        	 				   }]
      	 				}
                //contentEl : 'docPanel'  
              }, { // EAST REGION -----------------------------------------------------------
              region: 'east',
              title: 'East Panel',
              collapsible: true,
              collapsed: true,
              hideCollapseTool: true,
              titleCollapse: true,
              collapseMode: 'mini',
              split: true,
              autoScroll: true,
              height: 100,
              minHeight: 100,
              width: 200,
              minWidth: 100,
              
              tools:[{
                id:'refresh',
                qtip:'Refresch map',
                handler: function(event, toolEl, panel){
                	
                }
              }],
              
              //items: [east]
              html: ''
            }, 
            { // WEST REGION -----------------------------------------------------------
              region: 'west',
              //title: 'Control Panel',
              border: false,
              margins: '0 0 0 5',
              collapsible: true,
              collapsed: false,
              hideCollapseTool: true,
              titleCollapse: true,
              collapseMode: 'mini',
              split: true,
              autoScroll: false,
              width: 250,
              minWidth: 250,
              layout: 'fit',
                            
              items: [drillPanel]
              //html: ''
            }, 
            { // WEST REGION -----------------------------------------------------------
              region: 'south',
              title: 'Log',
              collapsible: true,
              collapsed: true,
              hideCollapseTool: true,
              titleCollapse: true,
              collapseMode: 'mini',
              split: true,
              height: 100,
              minHeight: 100,
              layout: 'fit',
                    
              //items: [south]
              html: ''
            }, 
            { // NORT HREGION -----------------------------------------------------------
              region: 'north',
              title: 'Header Panel',
              collapsible: true,
              collapsed: true,
              hideCollapseTool: true,
              titleCollapse: true,
              collapseMode: 'mini',
              split: true,
              autoScroll: false,
              height: 100,
              minHeight: 100,
              layout: 'fit',
                            
              //items: [west]
              html: ''
            }
            ]
          });           
        }
    };
}(); // end of app
 
// end of file
