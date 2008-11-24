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
        
        // public methods
        init: function() {
            Ext.QuickTips.init();
            
            var geoconf = {
              
              hierachies: [
		            ['STANDARD','Standard Hierachy', 'Standard Hierachy Desc.', 
                  [
                  {text: 'level1', ttip: 'level1: description goes here', type: 'operand', value: '$F{level1}'}
                  , {text: 'level2', ttip: 'level2: description goes here', type: 'operand', value: '$F{level2}'}
                  ]
                ], ['CUSTOM', 'Custom Hierachy', 'Custom Hierachy Desc.', 
                  [
                  {text: 'L1', ttip: 'L1: description goes here', type: 'operand', value: '$F{L1}'}
                  , {text: 'L2', ttip: 'L2: description goes here', type: 'operand', value: '$F{L2}'}
                  , {text: 'L3', ttip: 'L3: description goes here', type: 'operand', value: '$F{L3}'}
                  , {text: 'L4', ttip: 'L4: description goes here', type: 'operand', value: '$F{L4}'}
                  ]
                ]
              ],
              
              maps: [
    		       ['REGIONS','RegionMap', 'Everybody that can execute this qbe will see also your saved query', 
                [
                  {id: 'regions', name: 'Regions', description: 'Regions of France', category: 'Territorias'},
                  {id: 'departments', name: 'Departments', description: 'Departments of France', category: 'Territorias'},
                  {id: 'regionL', name: 'Region names', description: 'Region names', category: 'Labels'},
                  {id: 'departmentL', name: 'Department names', description: 'epartment names', category: 'Labels'}
                ]           
               ],
    		       ['DEPARTMENTS', 'DeparmentMap', 'The saved quary will be visible only to you', 
                [
                  {id: 'departments',  name: 'Departments', description: 'Departments of France', category: 'Territorias'},
                  {id: 'cities',  name: 'Cities', description: 'Greatest cities in France', category: 'Point of interest'},
                  {id: 'nuclear',  name: 'Nuclear Plants', description: 'Nuclear Plant', category: 'Point of interest'},
                  {id: 'cityL',  name: 'City names', description: 'City names', category: 'Labels'},
                  {id: 'departmentL',  name: 'Department names', description: 'epartment names', category: 'Labels'}
                ]           
               ]
		          ]
            };
            
            var drillPanel = new Sbi.geo.DrillControlPanel(geoconf);
            
            
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
                
                tools:[{
                    id:'gear',
                    qtip:'Exec',
                    // hidden:true,
                    handler: function(event, toolEl, panel){
                      alert(drillPanel.getAnalysisState().toSource());
                    }
                }],
                
                
                bodyCfg: {
        					tag:'div',
          				cls:'x-panel-body',
          				children:[{
          					tag:'iframe',
          	      			src: 'AdapterHTTP?ACTION_NAME=DRAW_MAP_ACTION',
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
                qtip:'Refresch expression structure'
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
