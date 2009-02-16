

/**
  * FilterWizard
  * [TBD]
  *    
  * by Andrea Gioia
  */
 

// create namespace
Ext.namespace('it.eng.spagobi.engines.qbe.filterwizard');
 
// create module
it.eng.spagobi.engines.qbe.filterwizard = function() {
  // do NOT access DOM from here; elements don't exist yet
 
  // private variables
  var win = undefined;
  
  // true only after the first time the window get rendered (used for lazy initialization)
  var active = false;
  // used if someone want to set the expression before the first visualization
  var expression = undefined;
   
  var getWizardWindow =  function() {
    if(!win) {
      win = new Ext.Window({
        id:'filterWizard',
        title: 'Expression Editor',
			 	layout:'fit',
				width:500,
				height:300,
				closeAction:'hide',
				plain: true,
				
				
				items: [viewport]
			});
    }
      
    return win;
  };    
  
  /*
  var getExpressionAsJSON = function(tree) {
          var s = "";
          
          if(!tree) tree = getExpStructureTree();
          
          s += "{";
          s += "value: '" + tree.attributes.value + "'"; 
          
          if(tree.childNodes.length > 0) {
            s += ", child: [";
             
            for(var i = 0; i < tree.childNodes.length; i++) {
              s += (i>0?",": "") + getExpressionAsJSON(tree.childNodes[i]);
            }
             
            s += "]";
           
          }          
          s += "}";
          return s;          
        };
        */
	 
  //  ------------------------------------------------------------------------
  //  WEST Region (Expression Items)
  //  ------------------------------------------------------------------------
    
  var logPanel = new Ext.Panel({
    autoScroll: true,
	  html: ''
  });
    
  var log = function(msg, isError) {
    //alert(logPanel.body.dom.innerHTML );
    var date = new Date();
    var str = date.getHours()+":"+date.getMinutes() +":"+ date.getSeconds();
     
    msg = '[' + str + '] ' + msg;
    if(isError != undefined && isError == true) {
      msg = "<font color='red'>" + msg + "</font>";
    }
    logPanel.body.update(logPanel.body.dom.innerHTML + '<p>' + msg);
  }
    
    
    
  //  ------------------------------------------------------------------------
  //  WEST Region (Expression Items)
  //  ------------------------------------------------------------------------
  
  var operands;
  var operators;
  
  var expItemTreePanel;
  
  var createExpItemNode = function(conf) {
    var node;
    
    node = new Ext.tree.TreeNode({text: conf.text, iconCls: conf.type});
    Ext.apply(node.attributes, conf);
    
    return node;
  };
 
  var getExpItemTreePanel = function() {            
      
	if(!expItemTreePanel) {  
      expItemTreePanel = new Ext.tree.TreePanel({
        root: getExpItemTree(),
        enableDD:false,
        expandable:true,
        collapsible:true,
        autoHeight:true ,
        bodyBorder:false ,
        width:300,
        leaf:false,
        lines:true,
        animate:true
      });
        
      expItemTreePanel.addListener('click', selectExpItemNode, this);
    }
    
    return expItemTreePanel;
  };
  
  var getExpItemTree = function() {
  	var expItemTreeRootNode;
    var operandsRootNode;
    var operatorsRootNode;
      
    expItemTreeRootNode = new Ext.tree.TreeNode({text:'Exp. Items', iconCls:'database',expanded:true});
    
    if(!operands) {  
    	// create a dummy list of possible operands just for test purpose
        operands = [
	        {text: 'filter1', ttip: 'Filter1: description goes here', type: 'operand', value: '$F{filter1}'}
        	, {text: 'filter2', ttip: 'Filter1: description goes here', type: 'operand', value: '$F{filter2}'}
          	, {text: 'filter3', ttip: 'Filter1: description goes here', type: 'operand', value: '$F{filter3}'}
          	, {text: 'filter4', ttip: 'Filter1: description goes here', type: 'operand', value: '$F{filter4}'}
          	, {text: 'filter5', ttip: 'Filter1: description goes here', type: 'operand', value: '$F{filter5}'}
        ];
	}
      
        
    operandsRootNode = new Ext.tree.TreeNode({text:'Operands', iconCls:'cube'});
    for(var i = 0; i < operands.length; i++) {
       operandsRootNode.appendChild( createExpItemNode( operands[i] ) );
    }
        
    if(!operators) {
    	operators = [
        	{text: 'AND', ttip: 'AND: description of operator goes here', type: 'operator', value: 'AND'}
          	, {text: 'OR', ttip: 'OR: description of operator goes here', type: 'operator', value: 'OR'}
          	, {text: ' ( ', ttip: '( : description of operator goes here', type: 'operator', value: '('}
          	, {text: ' ) ', ttip: ') : description of operator goes here', type: 'operator', value: ')'}
        ];
    }
        
    operatorsRootNode = new Ext.tree.TreeNode({text:'Operators', iconCls:'cube'});
    for(var i = 0; i < operators.length; i++) {
    	operatorsRootNode.appendChild( createExpItemNode( operators[i] ) );
    }
      
    expItemTreeRootNode.appendChild([
        operatorsRootNode,
        operandsRootNode
    ]);
    
    return expItemTreeRootNode;
  };
  
  var refreshExpItemTreePanel = function() {
  	                     
    expItemTreePanel.setRootNode( getExpItemTree() );
  };

  var selectExpItemNode = function(node, e) { 
    if(!node.attributes.value) return;
    var text = node.attributes.value + ' ';
    editor.insertAtCursor(text) ; 
  };
    
    
  //  ------------------------------------------------------------------------
  //  EAST Region (Expression Structure)
  //  ------------------------------------------------------------------------
  
  var expStructureTreePanel;
  
  var getExpStructureTreePanel = function() {    
    
    if(!expStructureTreePanel) {
      
      var rootNode = new Ext.tree.TreeNode({text:'Exp. Structure', iconCls:'database',expanded:true});
        
      expStructureTreePanel = new Ext.tree.TreePanel({
        root:rootNode,
        enableDD:false,
        ddGroup: 'gridDDGroup',
        dropConfig: {
        isValidDropPoint : function(n, pt, dd, e, data){
          return false;
          }      
        },
        expandable:true,
        collapsible:true,
        autoHeight:true ,
        bodyBorder:false ,
        width:300,
        leaf:false,
        lines:true,
          animate:true
      });
    }
      
    return expStructureTreePanel;
  };
    
  var getExpStructureTree = function(isLogActive) {
    var _log = function(msg){};
    
    if(isLogActive === true) {
    	_log = log;
    } 
    
    var error_offsets = new Array(); 
    var error_lookaheads = new Array(); 
    var error_count = 0; 
    var str = getExpression();
    
    _log("Parsing expression: " + str);
    var startDate = new Date();                      
    error_count = boolstaf.module.parse( str, error_offsets, error_lookaheads );  
    var endDate = new Date();
    
    if(error_count > 0) { 
      var errstr = new String(); 
      for( var i = 0; i < error_count; i++ ) {
        errstr += "Parse error in line " + ( str.substr( 0, error_offsets[i] ).match( /\n/g ) ? str.substr( 0, error_offsets[i] ).match( /\n/g ).length : 1 ) + " near \"" + str.substr( error_offsets[i] ) + "\", expecting \"" + error_lookaheads[i].join() + "\"\n" ; 
        _log( errstr, true );
      }
    }  else {
      var elapsed = endDate.getMilliseconds() - startDate.getMilliseconds();
      _log( "Expression parsed succesfully is " + elapsed + " ms." );
    }
    
    return boolstaf.module.getExperssionNode();
  };  
    
  var refreshExpStructureTreePanel = function() {
    var rootNode = new Ext.tree.TreeNode({text:'Exp. Structure', iconCls:'database',expanded:true});
     
    rootNode.appendChild([
      getExpStructureTree(true)
    ]);
                      
    getExpStructureTreePanel().setRootNode(rootNode);
  };
     
  //  ------------------------------------------------------------------------
  //  CENTER Region (Expression editor)
  //  ------------------------------------------------------------------------
    
    
  var xbutton = new Ext.Toolbar.Button({
    text:'Clear All',
    tooltip:'Clear all selected fields',
    iconCls:'remove',
    handler: function(){
      editor.reset();
      //alert(getExpressionAsJSON());
    }
  });

	  
  var editor = new Ext.form.HtmlEditor({
    frame: true,
    enableAlignments : false,
    enableColors : false,
    enableFont :  false,
    enableFontSize : false, 
    enableFormat : false,
    enableLinks :  false,
    enableLists : false,
    enableSourceEdit : false,
       
    listeners:{
    	'render': function(editor){
          var tb = editor.getToolbar();
          tb.add(xbutton);
        },
        'activate': function(){
          //alert('activate');
          active = true;
        },
        'initialize': function(){
          //alert('initialize');
          init = true;
          this.onFirstFocus();
          editor.insertAtCursor(expression) ; 
        },
        'beforesync': function(){
          // do nothings
        }
    }
  });
  
  
  var getExpression = function() {
    var str;
    
	var value = (active?editor.getValue(): expression);
    
    str = Ext.util.Format.stripTags( value );
    str = str.replace("&nbsp;"," ");
    
    return str;
  };
  
  var setExpression = function(exp, refresh, logger) {
  	if(active) {
  		editor.reset();
  		if(exp.trim() == "") {return;}
        editor.insertAtCursor(exp) ;        
        if(refresh) refreshExpStructureTreePanel(logger);
  	} else {
  		expression = exp;
  	}
  }
  
  
   
    
  //  --------------------------------------------------------------------------
  //  Window structure
  //  --------------------------------------------------------------------------
    
  var viewport = new Ext.Panel({
    layout: 'border',
    border: false,
    items: [
    { // CENTER REGION ---------------------------------------------------------
      region: 'center',
      title: 'Expression',
      collapsible: false,
      collapsed: false,
      split: true,
      autoScroll: false,
      height: 100,
      minHeight: 100,
      width: 100,
      minWidth: 0,
      layout: 'fit',
      
      //html: 'expression'
      items: [editor]
    }, 
    { // EAST REGION -----------------------------------------------------------
      region: 'east',
      title: 'Exp. Structure',
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
        qtip:'Refresch expression structure',
        handler: refreshExpStructureTreePanel
      }],
      
      items: [getExpStructureTreePanel()]
    }, 
    { // WEST REGION -----------------------------------------------------------
      region: 'west',
      title: 'Exp. Items',
      collapsible: true,
      hideCollapseTool: true,
      titleCollapse: true,
      collapseMode: 'mini',           
      split: true,
      autoScroll: true,
      height: 100,
      minHeight: 100,
      width: 150,
      minWidth: 100,
      
      items: [getExpItemTreePanel()]
    }, 
    { // WEST REGION -----------------------------------------------------------
      region: 'south',
      title: 'Log',
      collapsible: true,
      hideCollapseTool: true,
      titleCollapse: true,
      collapseMode: 'mini',
      split: true,
      height: 100,
      minHeight: 100,
      layout: 'fit',
            
      items: [logPanel]
    }
    ]
  });
              
  
         
    
 
    // public space
    return {
        // public properties, e.g. strings to translate
        
        // public methods
        init: function() { 
          // lazy initialization...
          getWizardWindow();
        },
        
        setOperands: function(array) {
          operands = array;
        },
        
        setExpression: function(exp) {
        	/*
          editor.reset();
          editor.insertAtCursor(exp) ; 
          refreshExpStructureTreePanel();
          */
          setExpression(exp);
        },
        
        getExpression: function() {
          return getExpression();
        },
        
        getExpressionAsTree: function() {
          return getExpStructureTree();
        },
        
        
        getExpressionAsJSON: function(tree) {
          var s = "";
          
          if(getExpression().trim() =="") return '{}';
          if(!tree) tree = getExpStructureTree();          
          if(!tree) return '{}';
          
          var types = ['UNDEF', 'NODE_OP', 'NODE_CONST']; 
		  var values = ['UNDEF', 'AND', 'OR', 'GROUP'];
 
          s += "{";
          s += "type: '" + types[tree.attributes.type] + "', ";
          s += "value: '" + (tree.attributes.type==1? values[tree.attributes.value]: tree.attributes.value) + "'"; 
        
          s += ", childNodes: [";
          if(tree.childNodes.length > 0) {             
            for(var i = 0; i < tree.childNodes.length; i++) {
              s += (i>0?",": "") + this.getExpressionAsJSON(tree.childNodes[i]);
            }           
          }   
          s += "]";       
          s += "}";
          return s;        
        },
        
        
        show: function() {
          refreshExpItemTreePanel();
          getWizardWindow().show();
        },
        
        hide: function(){
          getWizardWindow().hide();
        }
    };
}(); // end of app
