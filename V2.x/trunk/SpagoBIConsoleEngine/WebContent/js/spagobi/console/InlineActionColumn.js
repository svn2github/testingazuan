Ext.ns("Sbi.console");


Sbi.console.InlineActionColumn = function(config){
	
	
	config = Ext.apply({
		hideable: true
		, width: 25
	}, config || {});
	
	config.options = config.config;
	config.tooltip = config.tooltip || inlineActionColumnConfig.name;
	config.handler = config.handler || Ext.emptyFn;
	config.scope = config.scope || this;

	delete config.config;
	
	Ext.apply(this, config);
    if(!this.id){
        this.id = Ext.id();
    }
    if(!this.dataIndex) {
    	this.dataIndex = this.name + "_" + this.id;
    }
    this.renderer = this.renderer.createDelegate(this);
    
    // parent constructor
    Sbi.console.InlineActionColumn.superclass.constructor.call(this, config);
    
    if(this.grid) this.init(this.grid);
};


Ext.extend(Sbi.console.InlineActionColumn, Ext.util.Observable, {
	
	grid: null
	, name: null
	, options: null
	, handler: null
	, scope: null

	, init : function(grid){
		this.grid = grid;
        if(this.grid.rendered === true) {
        	var view = this.grid.getView();
            view.mainBody.on('click', this.onClick, this);
        } else {
        	this.grid.on('render', function(){
        		var view = this.grid.getView();
        		view.mainBody.on('click', this.onClick, this);
        	}, this);
        }
    }

    , onClick : function(e, t){
        if(t.className && t.className.indexOf('x-mybutton-'+this.id) != -1){
            e.stopEvent();
            
            var index = this.grid.getView().findRowIndex(t);
            var record = this.grid.store.getAt(index);   
            this.handler.call(this.scope, this.name, record, index, this.options);          
        }
    }
    
    
    
    
    
    
   

    , renderer : function(v, p, record){
        return '<center><img class="x-mybutton-'+this.id+'" width="13px" height="13px" src="' + this.imgSrc + '" title= "' + this.tooltip + '"/></center>';
    }
  });