/**
 *
 
 */


qx.Class.define("spagobi.ui.FormList", {
	extend: spagobi.ui.Form,
	
	construct : function(config) { 
		this.base(arguments);
		
		this._instances = [];
		this.setConfig(config);
		
		this._tabView = new qx.ui.pageview.tabview.TabView();
		this.addInstance();
		
		this.add(this._tabView);
  		
  		
	},
	
	members: {
		
		
		_config: undefined,
		
		_instances: undefined,
		
		_tabView: undefined,
		
		getConfig: function() {
			return _config;
		},
		
		setConfig: function(config) {
			this._config = config;
		},
		
		addInstance: function(o) {
			var subform;
			if( typeof(this._config) == 'object' ) {
				subform = new qx.ui.layout.spagobi.ui.Form(this._config);
			} else {
				subform = new this._config();
			}	
			if(o) {
				subform.setData(o);
			}		
			
			
			var subFormButton = new qx.ui.pageview.tabview.Button('tab-' + this._instances.length);
			subFormButton.setChecked(true);
	        this._tabView.getBar().add(subFormButton);
	        var subFormPage = new qx.ui.pageview.tabview.Page(subFormButton);
	  		this._tabView.getPane().add(subFormPage);
	  		subFormPage.add(subform);
	  		
	  		this._instances[this._instances.length] = subform;
		},
		
		
		
		getData: function() {
			for(var i = 0; i < this._instances.length; i++) {
				 this.dataObject[i] = this._instances[i].getData();
			}
			return this.dataObject;
		},
	
	
		deleteData: function() {
			var pagesArray = this._tabView.getPane().getChildren();
			var pageNum = pagesArray.length;
			var i = 0;
			while(i < pageNum) {
				this.deleteDataAt(0);
				//alert('tab' + i + ' of ' + pageNum +' deleted');
				i++;
			}
			this.dataObject = [];
			this._instances = [];
		},
		
		deleteDataAt: function(index) {
			var pagesArray = this._tabView.getPane().getChildren();
			var targetPage = pagesArray[ index ];
			 
			var itemsList = this._tabView.getBar().getChildren();
            var lengthList = itemsList.length;
           
       
            itemsList[ index ].getManager().remove( itemsList[ index ] );
            this._tabView.getBar().remove(itemsList[ index ]);
    
            this._tabView.getPane().remove(targetPage);
    
            targetPage.dispose();
            //itemsList[ index ].dispose();
            
            /*
            if(this.dataObject && this.dataObject[index]) {
            this.dataObject.splice(index, 1);
            }
            * */
		},
		
		setData: function(o) {
			this.deleteData();
			//alert( 'setdata' );
			for(var i = 0; i < o.length; i++) {
				this.addInstance(o[i]);
			}
			
			this.dataObject = o;		
		}
		
	},
	
	statics : {
	
	}
});