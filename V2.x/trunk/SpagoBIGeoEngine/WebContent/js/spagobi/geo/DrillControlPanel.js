/**
  * ...
  * by Andrea Gioia
  */
 

Sbi.geo.DrillControlPanel = function(conf) {
    
    var Hierarchy = Ext.data.Record.create([
    	{name: 'id'}                 
    	, {name: 'name'} 
    	, {name: 'description'} 
    	, {name: 'levels'} 
	]);
    
        
  	this.hierarchyComboBoxStore = new Ext.data.Store({
	   proxy: new Ext.data.HttpProxy({
           url: 'AdapterHTTP?ACTION_NAME=GET_HIERARCHIES_ACTION',
           success: function(response){
           	 // do nothing 
           },
   		   failure: function(){alert('Impossible to get hierachies');}        
        })
        , reader: new Ext.data.JsonReader({   
    		root: "hierarchies",                        
    		id: "id" }, Hierarchy)
        //, autoLoad: true
        , listeners: {
  			datachanged : {
  				fn: this.handleHierarchySelectorDataChanged
          		, scope: this
  			}
        }      
	});  
		    
	this.hierarchyComboBox = new Ext.form.ComboBox({
	   tpl: '<tpl for="."><div ext:qtip="{name}: {description}" class="x-combo-list-item">{name}</div></tpl>',	
	   editable  : false,
	   fieldLabel : 'Hiearachy',
	   forceSelection : true,
	   mode : 'local',
	   name : 'hierarchy',
	   store : this.hierarchyComboBoxStore,
	   displayField:'name',
		 valueField:'id',
		 emptyText:'Select hierarchy...',
		 typeAhead: true,
		 triggerAction: 'all',
		 width: 150,
		 selectOnFocus:true,
		 listeners: {
		    'select': {
          fn: this.handleSelectHierarchy
          , scope: this
        }
      }
	  });	  
    
    var hierarchySelector = new Ext.FormPanel({
        labelWidth: 65,
        frame:false,
        bodyStyle:'padding:5px 5px 0',
        margins:'10 10 10 15',
        border: true,
        defaultType: 'textfield',
        items: [this.hierarchyComboBox]
    });    
    
    var hierarchyRootNode = new Ext.tree.TreeNode({text:'Hierarchy', expanded:true});
    
    this.hierarchyTree = new Ext.tree.TreePanel({
      root:hierarchyRootNode,
      enableDD:false,
      expandable:true,
      collapsible:true,
      bodyStyle:'padding:5px 0px 5px; margin: 5px 0;',
      leaf:false,
      lines:true,
      border: false,
      animate:true,
      listeners: {
		    'checkchange': {
          fn: this.handleChangeHierarchyLevel
          , scope: this
        }
      }
    });
    
    var topPanel = new Ext.Panel({
      autoScroll: true,
      border: false,
      items: [hierarchySelector, this.hierarchyTree]
    });
    
    
    // --------------------------------------------------------------------------
		
	var Map = Ext.data.Record.create([
    	{name: 'id'}                 
    	, {name: 'name'} 
    	, {name: 'description'} 
    	, {name: 'features'} 
	]);	
		
	this.mapComboBoxStore = new Ext.data.Store({
	   proxy: new Ext.data.HttpProxy({
	   	   params: {
	   	   	featureName: undefined
	   	   },	   	  
           url: 'AdapterHTTP?ACTION_NAME=X_GET_MAPS_ACTION',
           success: function(response){
           	 // do nothing 
           },
   		   failure: function(){alert('Impossible to get maps');}        
        })
        , reader: new Ext.data.JsonReader({   
    		root: "maps",                        
    		id: "id" }, Map)
        //, autoLoad: true
        , listeners: {
  			datachanged : {
  				fn: this.handleMapSelectorDataChanged
          		, scope: this
  			}
        }        
	}); 
		    
		    
	this.mapComboBox = new Ext.form.ComboBox({
	    	tpl: '<tpl for="."><div ext:qtip="{name}: {description}" class="x-combo-list-item">{name}</div></tpl>',	
	    	editable  : false,
	    	fieldLabel : 'Map',
	    	forceSelection : true,
	    	mode : 'local',
	    	name : 'map',
	    	store : this.mapComboBoxStore,
	    	displayField:'name',
		    valueField:'id',
		    emptyText:'Select map...',
		    typeAhead: true,
		    triggerAction: 'all',
		    width: 150,
		    selectOnFocus:true,
		    listeners: {
  		    'select': {
            fn: this.handleSelectMap
            , scope: this
          }
        }
	    });
    
    var mapSelector = new Ext.FormPanel({
        labelWidth: 65,
        frame:false,
        bodyStyle:'padding:5px 5px 0',
        margins:'10 10 10 15',
        border: true,
        defaultType: 'textfield',
        items: [this.mapComboBox]
    });
    
    var featuresRootNode = new Ext.tree.TreeNode({text:'Features', expanded:true});
    
    this.featuresTree = new Ext.tree.TreePanel({
      root:featuresRootNode,
      enableDD:false,
      expandable:true,
      collapsible:true,
      bodyStyle:'padding:5px 0px 5px; margin: 5px 0;',
      leaf:false,
      lines:true,
      border: false,
      animate:true
      /*
      listeners: {
		    'checkchange': {
          fn: this.handleChangeHierarchyLevel
          , scope: this
        }
      }
      */
    });
    
    
    var bottomPanel = new Ext.Panel({
        autoScroll: true,
        border: false,
        items: [mapSelector, this.featuresTree]
    });
    
    // constructor
    Sbi.geo.DrillControlPanel.superclass.constructor.call(this, {
           
        border:false,
        //autoScroll: true,             
        layout:'border',
        layoutConfig:{
          animate:true
        },
        //baseCls : 'x-accordion-hd',
        items: [{
          title: 'Geo Dimension',  
          region: 'north',
          split: true,         
          autoScroll: true,
          //collapsible: true,   
          //collapsed: false,
          hideCollapseTool: true,
          titleCollapse: true,
          //collapseMode: 'mini',
          height: 200,
          //anchor: '100%, 50%',
          border:true,
          bodyStyle:'padding:3px 3px 3px 3px',
          items: [topPanel]
        },{
          title: 'Map', 
          region: 'center',          
          autoScroll: true,
          //collapsible: true,   
          //collapsed: false,
          hideCollapseTool: true,
          titleCollapse: true,
          //collapseMode: 'mini',
          //anchor: '100%, 50%',
          border:true,
          bodyStyle:'padding:3px 3px 3px 3px',
          items: [bottomPanel]
                       
        }]
    });
    
    this.hierarchyComboBoxStore.load();
}




Ext.extend(Sbi.geo.DrillControlPanel, Ext.Panel, {
    
    // static contens and methods definitions
   
   
    // public methods
    getAnalysisState : function() {
      var analysisState = {};
      
      /*
      public static final String HIERARCHY = "hierarchy";
	  public static final String HIERARCHY_LEVEL = "level";
	  public static final String MAP = "map";
	  public static final String FEATURES = "features";	
      */
      
      analysisState.hierarchy = this.hierarchyComboBox.getValue();
      analysisState.level = this.hierarchyTree.getChecked()[0].text;
      
      analysisState.map = this.mapComboBox.getValue();
      analysisState.features = [];
      var checkedFeatures = this.featuresTree.getChecked();
      for(var i = 0; i < checkedFeatures.length; i++) {
      	analysisState.features[i] = checkedFeatures[i].text;
      }      
      
      return analysisState;
    }, 
    
    // private handlers
    
    handleHierarchySelectorDataChanged: function(store) {
    	var hierarchy = store.getAt(0);
    	this.hierarchyComboBox.setValue(hierarchy.id);
    	this.hierarchyComboBox.fireEvent('select', this.hierarchyComboBox, hierarchy, 0);
    },
    
    handleSelectHierarchy : function(combo, record, index){
      var hierarchyName = record.data['name'];
      var levels = record.data['levels'];
      var hierarchyRoot = new Ext.tree.TreeNode({text:hierarchyName, expanded: true});
      for(var i = 0; i < levels.length; i++) {
        var node;    
        
        node = new Ext.tree.TreeNode({
          text: levels[i].name,
          iconCls: 'noimage', 
          checked:false
        });
        
        Ext.apply(node.attributes, levels[i]);
     
        hierarchyRoot.appendChild( node );
      }
      this.hierarchyTree.setRootNode(hierarchyRoot);
      
      var node = this.hierarchyTree.getRootNode().firstChild;
      node.getUI().toggleCheck(true);
      
      this.hierarchyTree.fireEvent('checkchange', node, true);
      
    },
    
    handleChangeHierarchyLevel : function(node, checked){
      if(!checked) {
        node.getUI().toggleCheck(true);
      } else {
        this.hierarchyTree.getRootNode().cascade(function(n){
          n.getUI().toggleCheck(false);
        });
        node.getUI().toggleCheck(true);
      }
      this.mapComboBoxStore.load({params: {featureName: node.attributes['feature']}});
    },
    
    handleMapSelectorDataChanged: function(store) {
    	var map = store.getAt(0);
    	this.mapComboBox.setValue(map.id);
    	this.mapComboBox.fireEvent('select', this.mapComboBox, map, 0);
    },
    
    handleSelectMap  : function(combo, record, index){
      var mapName = record.data['name'];
      var features = record.data['features'];
      var hierarchyRoot = new Ext.tree.TreeNode({text:mapName, expanded: true});
      for(var i = 0; i < features.length; i++) {
        var node;    
        
        node = new Ext.tree.TreeNode({
          text: features[i].name,
          iconCls: 'noimage', 
          checked:false
        });
        
         Ext.apply(node.attributes, features[i]);
         
        hierarchyRoot.appendChild( node );
      }
      this.featuresTree.setRootNode(hierarchyRoot);
      
      var node = this.featuresTree.getRootNode().findChild('id', this.mapComboBoxStore.lastOptions.params.featureName);
      node.getUI().toggleCheck(true);
    }
    
    
    
});